/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.aggregate.sort;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Reads the features stored in the specified file
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class FeatureFileReader {

    File file;

    SimpleFeatureType schema;

    SimpleFeatureBuilder builder;

    SimpleFeature curr;

    ObjectInputStream is;

    public FeatureFileReader(File file, SimpleFeatureType schema) throws IOException {
        this.file = file;
        this.builder = new SimpleFeatureBuilder(schema);
        this.is = new ObjectInputStream(new FileInputStream(file));
        this.schema = schema;
    }

    public SimpleFeature feature() throws IOException {
        if (curr == null && is != null) {
            curr = readNextFeature();
        }
        return curr;
    }

    public SimpleFeature next() throws IOException {
        curr = readNextFeature();
        return curr;
    }

    private SimpleFeature readNextFeature() throws IOException {
        try {
            // read the fid, check for file end
            String fid = null;
            try {
                fid = (String) is.readObject();
            } catch (Exception e) {
                // we assume we got to the end of the file
                close();
                return null;
            }

            // read the other attributes, build the feature
            for (int i = 0; i < schema.getAttributeCount(); i++) {
                Object att = is.readObject();
                builder.add(att);
            }
            return builder.buildFeature(fid);
        } catch (ClassNotFoundException e) {
            throw new IOException("Failed to read an attribute, its class is missing", e);
        }
    }

    /**
     * Closes down this reader
     */
    void close() {
        if (is != null) {
            try {
                is.close();
            } catch(IOException e) {
                // ignore
            }
            file.delete();
        }
    }

}
