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

import static org.geotools.data.mongodb.MongoDataStore.KEY_collection;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/**
 * Maps a collection containing valid GeoJSON.
 *
 * @author Justin Deoliveira, OpenGeo
 */
public class GeoJSONMapper extends AbstractCollectionMapper {

    MongoGeometryBuilder geomBuilder = new MongoGeometryBuilder();

    @Override
    public String getGeometryPath() {
        return "geometry";
    }

    @Override
    public String getPropertyPath(String property) {
        return "properties." + property;
    }

    @Override
    public Geometry getGeometry(DBObject obj) {
        return geomBuilder.toGeometry((DBObject) obj.get("geometry"));
    }

    @Override
    public DBObject toObject(Geometry g) {
        return geomBuilder.toObject(g);
    }

    @Override
    public void setGeometry(DBObject obj, Geometry g) {
        obj.put("geometry", toObject(g));
    }

    @Override
    public SimpleFeatureType buildFeatureType(Name name, DBCollection collection) {

        SimpleFeatureTypeBuilder ftBuilder = new SimpleFeatureTypeBuilder();

        ftBuilder.setName(name);
        ftBuilder.userData(MongoDataStore.KEY_mapping, "geometry");
        ftBuilder.userData(MongoDataStore.KEY_encoding, "GeoJSON");
        ftBuilder.add("geometry", Geometry.class, DefaultGeographicCRS.WGS84);

        DBObject rootDBO = collection.findOne();
        if (rootDBO != null && rootDBO.containsField("properties")) {
            DBObject propertiesDBO = (DBObject) rootDBO.get("properties");
            for (String key : propertiesDBO.keySet()) {
                Object v = propertiesDBO.get(key);
                Class<?> binding = MongoUtil.mapBSONObjectToJavaType(v);
                if (binding != null) {
                    ftBuilder.userData(MongoDataStore.KEY_mapping, "properties." + key);
                    ftBuilder.add(key, binding);
                } else {
                    System.err.println(
                            "unmapped key, " + key + " with type of " + v.getClass().getName());
                }
            }
        }
        SimpleFeatureType ft = ftBuilder.buildFeatureType();
        // pre-populating this makes view creation easier...
        ft.getUserData().put(KEY_collection, ft.getTypeName());

        return ft;
    }
}
