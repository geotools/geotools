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

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.data.simple.SimpleFeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class MongoFeatureReader implements SimpleFeatureReader {

    DBCursor cursor;
    MongoFeatureSource featureSource;
    CollectionMapper mapper;

    public MongoFeatureReader(DBCursor cursor, MongoFeatureSource featureSource) {
        this.cursor = cursor;
        this.featureSource = featureSource;
        mapper = featureSource.getMapper();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return featureSource.getSchema();
    }

    @Override
    public boolean hasNext() throws IOException {
        return cursor.hasNext();
    }

    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        DBObject obj = cursor.next();

        return mapper.buildFeature(obj, featureSource.getSchema());
    }

    @Override
    public void close() throws IOException {
        cursor.close();
    }
}
