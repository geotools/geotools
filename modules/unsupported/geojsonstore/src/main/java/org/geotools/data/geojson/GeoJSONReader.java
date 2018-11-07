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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;

/**
 * Utility class to provide a reader for GeoJSON streams
 *
 * @author ian
 */
public class GeoJSONReader {
    private static final Logger LOGGER = Logging.getLogger(GeoJSONReader.class);

    private FeatureJSON reader = new FeatureJSON();

    private InputStream inputStream;

    private URL url;

    public GeoJSONReader(URL url) {
        this.url = url;
    }

    public boolean isConnected() {
        try {
            inputStream = url.openStream();
            if (inputStream == null) {
                url = new URL(url.toExternalForm());
                inputStream = url.openStream();
            }
        } catch (IOException e) {
            // whoops
            return false;
        }
        try {
            if (inputStream.available() == 0) {
                url = new URL(url.toExternalForm());
                inputStream = url.openStream();
            }

            LOGGER.finest("inputstream is " + inputStream);
            return (inputStream != null) && (inputStream.available() > 0);
        } catch (IOException e) {
            // something went wrong
            LOGGER.throwing(this.getClass().getName(), "isConnected", e);
            return false;
        }
    }

    public FeatureCollection getFeatures() throws IOException {
        if (!isConnected()) {
            throw new IOException("not connected to " + url.toExternalForm());
        }
        reader = new FeatureJSON();
        LOGGER.fine("reading features from " + url.toExternalForm() + " inputstream");
        FeatureCollection collection = reader.readFeatureCollection(inputStream);
        inputStream.close();

        return collection;
    }

    public FeatureIterator<SimpleFeature> getIterator() throws IOException {
        if (!isConnected()) {
            return new DefaultFeatureCollection(null, null).features();
        }
        return reader.streamFeatureCollection(inputStream);
    }

    public FeatureType getSchema() throws IOException {
        if (!isConnected()) {
            throw new IOException("not connected to " + url.toExternalForm());
        }
        FeatureType schema = reader.readFeatureCollection(inputStream).getSchema();

        return schema;
    }
}
