/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/** @author tkunicki@boundlessgeo.com */
public interface MongoSchemaStore {

    void storeSchema(SimpleFeatureType schema) throws IOException;

    SimpleFeatureType retrieveSchema(Name name) throws IOException;

    void deleteSchema(Name name) throws IOException;

    List<String> typeNames();

    void close();

    default SimpleFeatureType getSimpleFeatureType(BufferedReader reader, Name name)
            throws IOException {
        try {
            String lineSeparator = System.getProperty("line.separator");
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
                jsonBuilder.append(lineSeparator);
            }
            Object o = JSON.parse(jsonBuilder.toString());
            if (o instanceof DBObject) {
                return FeatureTypeDBObject.convert((DBObject) o, name);
            }
        } finally {
            reader.close();
        }
        return null;
    }
}
