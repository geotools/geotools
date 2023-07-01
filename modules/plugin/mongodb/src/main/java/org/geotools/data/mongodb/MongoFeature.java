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
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.filter.identity.FeatureIdImpl;

public class MongoFeature extends SimpleFeatureImpl {

    private final DBObject mongoObject;
    private final Object[] values;
    private CoordinateReferenceSystem originalCRS;

    public MongoFeature(Object[] values, SimpleFeatureType featureType, String id) {
        this(null, values, featureType, id);
    }

    public MongoFeature(
            DBObject mongoObject, Object[] values, SimpleFeatureType featureType, String id) {
        super(values, featureType, new FeatureIdImpl(id), false);
        this.values = values;
        this.mongoObject = mongoObject;
        this.originalCRS = this.featureType.getGeometryDescriptor().getCoordinateReferenceSystem();
    }

    public DBObject getMongoObject() {
        return mongoObject;
    }

    public Object[] getValues() {
        return values;
    }

    public CoordinateReferenceSystem getOriginalCRS() {
        return originalCRS;
    }
}
