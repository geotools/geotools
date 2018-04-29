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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Wrapper to handle writing GeoJSON FeatureCollections
 *
 * @author ian
 */
public class GeoJSONWriter {
    private FeatureJSON writer = new FeatureJSON();
    private OutputStream out;
    private DefaultFeatureCollection collection = null;

    public GeoJSONWriter(OutputStream outputStream) {
        if (outputStream instanceof BufferedOutputStream) {
            this.out = outputStream;
        } else {
            this.out = new BufferedOutputStream(outputStream);
        }
        collection = new DefaultFeatureCollection();
    }

    public void setSchema(SimpleFeatureType schema) throws IOException {
        writer.setEncodeNullValues(true);
        writer.setFeatureType(schema);
        if (schema.getCoordinateReferenceSystem() != null) {
            writer.writeCRS(schema.getCoordinateReferenceSystem(), out);
        }
    }

    public void write(SimpleFeature currentFeature) throws IOException {
        collection.add(currentFeature);
    }

    public void close() throws IOException {
        writer.writeFeatureCollection(collection, out);
        out.close();
        writer = null;
    }
}
