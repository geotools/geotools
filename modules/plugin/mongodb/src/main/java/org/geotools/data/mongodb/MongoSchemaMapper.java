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

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;

/** @author tkunicki@boundlessgeo.com */
public class MongoSchemaMapper extends AbstractCollectionMapper {

    MongoGeometryBuilder geomBuilder = new MongoGeometryBuilder();

    final SimpleFeatureType schema;

    public MongoSchemaMapper(SimpleFeatureType schema) {
        this.schema = schema;
    }

    @Override
    public String getGeometryPath() {
        String gdName = schema.getGeometryDescriptor().getLocalName();
        return (String) schema.getDescriptor(gdName).getUserData().get(MongoDataStore.KEY_mapping);
    }

    @Override
    public String getPropertyPath(String property) {
        AttributeDescriptor descriptor = schema.getDescriptor(property);
        return descriptor == null
                ? null
                : (String) descriptor.getUserData().get(MongoDataStore.KEY_mapping);
    }

    @Override
    public Geometry getGeometry(DBObject dbo) {
        Object o = MongoUtil.getDBOValue(dbo, getGeometryPath());
        // TODO legacy coordinate pair
        return o == null ? null : geomBuilder.toGeometry((DBObject) o);
    }

    @Override
    public DBObject toObject(Geometry g) {
        return geomBuilder.toObject(g);
    }

    @Override
    public void setGeometry(DBObject dbo, Geometry g) {
        MongoUtil.setDBOValue(dbo, getGeometryPath(), toObject(g));
    }

    @Override
    public SimpleFeatureType buildFeatureType(Name name, DBCollection collection) {
        return schema;
    }
}
