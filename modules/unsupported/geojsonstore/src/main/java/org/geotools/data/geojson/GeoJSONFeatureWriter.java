package org.geotools.data.geojson;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.URLs;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GeoJSONFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {
    private static final Logger LOGGER = Logging.getLogger(GeoJSONFeatureWriter.class);

    private ContentState state;

    private File temp;

    private GeoJSONWriter geoJSONWriter;

    private GeoJSONFeatureReader delegate;

    private SimpleFeature currentFeature;

    private boolean appending = false;

    private int nextID = 0;

    private File file;

    public GeoJSONFeatureWriter(ContentState state, Query query) throws IOException {
        this.state = state;
        String typeName = query.getTypeName();
        URL url = ((GeoJSONDataStore) state.getEntry().getDataStore()).url;
        file = URLs.urlToFile(url);
        File directory = file.getParentFile();
        LOGGER.fine("Opening writer for " + url);
        if (directory == null) {
            throw new IOException(
                    "Unable to find parent direcotry of file " + file + " from url " + url);
        }
        if (!directory.canWrite()) {
            throw new IOException("Directory " + directory + " is not writable.");
        }
        this.temp =
                File.createTempFile(typeName + System.currentTimeMillis(), "geojson", directory);
        LOGGER.fine("Writing to " + temp.getAbsolutePath());
        this.geoJSONWriter = new GeoJSONWriter(new FileOutputStream(this.temp));
        this.delegate = new GeoJSONFeatureReader(state, query);
    }

    public SimpleFeatureType getFeatureType() {
        return delegate.getFeatureType();
    }

    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        if (geoJSONWriter == null) {
            throw new IOException("Writer is closed");
        }
        if (this.currentFeature != null) {
            this.write();
        }
        try {
            if (!appending) {
                if (delegate.reader != null && delegate.hasNext()) {
                    this.currentFeature = delegate.next();
                    return this.currentFeature;
                } else {
                    this.appending = true;
                }
            }
            SimpleFeatureType featureType = state.getFeatureType();
            String fid = featureType.getTypeName() + "." + nextID;
            Object values[] = DataUtilities.defaultValues(featureType);

            this.currentFeature = SimpleFeatureBuilder.build(featureType, values, fid);
            return this.currentFeature;
        } catch (IllegalArgumentException invalid) {
            throw new IOException("Unable to create feature:" + invalid.getMessage(), invalid);
        }
    }

    public boolean hasNext() throws IOException {
        if (geoJSONWriter == null) {
            return false;
        }
        if (this.appending) {
            return false; // reader has no more contents
        }
        return delegate.hasNext();
    }

    public void close() throws IOException {
        LOGGER.entering(this.getClass().getName(), "close");
        if (geoJSONWriter == null) {
            throw new IOException("Writer is already closed");
        }
        if (this.currentFeature != null) {
            LOGGER.fine("Writing current feature");
            this.write();
        }
        // now write out the remaining features
        while (this.hasNext()) {
            LOGGER.fine("writing remaining features");
            next();
            write();
        }
        geoJSONWriter.close();
        geoJSONWriter = null;
        if (delegate != null) {
            delegate.close();
            delegate = null;
        }
        LOGGER.fine("Copyting " + temp + " to " + file);
        // now copy over the new file onto the old one
        Files.copy(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void remove() throws IOException {
        LOGGER.fine("deleting current feature (" + currentFeature.getID() + ")");
        this.currentFeature = null;
    }

    @Override
    public void write() throws IOException {
        if (this.currentFeature == null) {
            return; // current feature has been deleted
        }
        LOGGER.fine("writing current feature (" + currentFeature.getID() + ")");
        geoJSONWriter.write(currentFeature);
        nextID++;
        this.currentFeature = null; // indicate that it has been written
    }
}
