/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentState;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class FlatgeobufFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private ContentState state;

    private SimpleFeature nextFeature;

    private File file;

    private InputStream inputStream;

    private FlatgeobufReader flatgeobufReader;

    public FlatgeobufFeatureReader(ContentState state, Query query) throws IOException {
        this.state = state;
        DataStore dataStore = state.getEntry().getDataStore();
        if (dataStore instanceof FlatgeobufDirectoryDataStore) {
            this.file =
                    ((FlatgeobufDirectoryDataStore) dataStore)
                            .getDataStore(state.getFeatureType().getTypeName())
                            .getFile();
        } else {
            this.file = ((FlatgeobufDataStore) dataStore).getFile();
        }
    }

    private FlatgeobufReader getFlatgeobufReader() throws IOException {
        if (flatgeobufReader == null) {
            this.inputStream = new FileInputStream(this.file);
            this.flatgeobufReader =
                    new FlatgeobufReader(
                            state.getFeatureType().getTypeName(),
                            state.getFeatureType().getGeometryDescriptor().getLocalName(),
                            this.inputStream);
            this.flatgeobufReader.getFeatureType();
        }
        return this.flatgeobufReader;
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return state.getFeatureType();
    }

    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        SimpleFeature feature = null;
        if (nextFeature != null) {
            feature = nextFeature;
            nextFeature = null;
        } else {
            feature = getFlatgeobufReader().getNextFeature();
        }
        return feature;
    }

    @Override
    public boolean hasNext() throws IOException {
        if (nextFeature != null) {
            return true;
        } else {
            nextFeature = getFlatgeobufReader().getNextFeature();
            return nextFeature != null;
        }
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}
