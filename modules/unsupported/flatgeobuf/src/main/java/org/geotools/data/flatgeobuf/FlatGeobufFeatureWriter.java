/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.flatgeobuf;

import com.google.flatbuffers.FlatBufferBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureBuilder;

public class FlatGeobufFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {

    private ContentState state;

    private File temp;

    private FlatGeobufFeatureReader delegate;

    private boolean appending = false;

    private SimpleFeature currentFeature;

    private int nextRow = 0;

    private File file;

    private FlatGeobufWriter writer;

    private OutputStream outputStream;

    private FlatBufferBuilder builder;

    private FlatGeobufDataStore flatGeobufDataStore;

    public FlatGeobufFeatureWriter(ContentState state, Query query) throws IOException {
        this.state = state;
        String typeName = query.getTypeName();
        DataStore dataStore = state.getEntry().getDataStore();
        if (dataStore instanceof FlatGeobufDirectoryDataStore) {
            this.flatGeobufDataStore =
                    ((FlatGeobufDirectoryDataStore) dataStore).getDataStore(typeName);
            this.file = flatGeobufDataStore.getFile();
        } else {
            this.flatGeobufDataStore = (FlatGeobufDataStore) dataStore;
            this.file = flatGeobufDataStore.getFile();
        }
        File directory = file.getParentFile();
        this.temp =
                File.createTempFile(typeName + System.currentTimeMillis(), "flatgeobuf", directory);
        this.outputStream = new FileOutputStream(this.temp);
        this.builder = FlatBuffers.newBuilder(4096);
        this.writer = new FlatGeobufWriter(this.outputStream, this.builder);
        this.writer.writeFeatureType(state.getFeatureType());
        this.delegate = new FlatGeobufFeatureReader(state, query);
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return state.getFeatureType();
    }

    @Override
    public SimpleFeature next() throws IOException {
        if (this.writer == null) {
            throw new IOException("FeatureWriter has been closed");
        }
        if (this.currentFeature != null) {
            this.write();
        }
        try {
            if (!appending) {
                if (delegate.hasNext()) {
                    this.currentFeature = delegate.next();
                    return this.currentFeature;
                } else {
                    this.appending = true;
                }
            }
            SimpleFeatureType featureType = state.getFeatureType();
            String fid = featureType.getTypeName() + "." + nextRow;
            Object[] values = DataUtilities.defaultValues(featureType);
            this.currentFeature = SimpleFeatureBuilder.build(featureType, values, fid);
            return this.currentFeature;
        } catch (IllegalArgumentException invalid) {
            throw new IOException("Unable to create feature:" + invalid.getMessage(), invalid);
        }
    }

    @Override
    public void remove() throws IOException {
        this.currentFeature = null;
    }

    @Override
    public void write() throws IOException {
        if (this.currentFeature == null) {
            return;
        }
        this.writer.writeFeature(this.currentFeature);
        nextRow++;
        this.currentFeature = null;
    }

    @Override
    public boolean hasNext() throws IOException {
        if (this.writer == null) {
            return false;
        }
        if (this.appending) {
            return false;
        }
        return delegate.hasNext();
    }

    @Override
    public void close() throws IOException {
        if (this.writer == null) {
            throw new IOException("Writer alread closed");
        }
        if (this.currentFeature != null) {
            this.write();
        }
        while (hasNext()) {
            next();
            write();
        }
        this.outputStream.flush();
        this.outputStream.close();
        this.writer = null;
        FlatBuffers.release(this.builder);
        if (delegate != null) {
            this.delegate.close();
            this.delegate = null;
        }
        Files.copy(temp.toPath(), this.file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        temp.delete();

        // invalidate the header cache after write
        flatGeobufDataStore.clearHeaderMeta();
    }
}
