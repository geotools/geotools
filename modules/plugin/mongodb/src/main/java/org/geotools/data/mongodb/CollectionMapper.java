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
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/**
 * A strategy for mapping a mongo collection to a feature.
 *
 * @author Justin Deoliveira, OpenGeo
 */
public interface CollectionMapper {

    Geometry getGeometry(DBObject obj);

    void setGeometry(DBObject obj, Geometry g);

    DBObject toObject(Geometry g);

    String getGeometryPath();

    String getPropertyPath(String property);

    SimpleFeatureType buildFeatureType(Name name, DBCollection collection);

    SimpleFeature buildFeature(DBObject obj, SimpleFeatureType featureType);
}
