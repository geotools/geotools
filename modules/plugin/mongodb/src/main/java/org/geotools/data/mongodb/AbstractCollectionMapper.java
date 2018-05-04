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
import java.util.ArrayList;
import java.util.List;
import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * Maps a collection containing valid GeoJSON.
 *
 * @author tkunicki@boundlessgeo.com
 */
public abstract class AbstractCollectionMapper implements CollectionMapper {

    public static final String MONGO_OBJECT_FEATURE_KEY = "MONGO_OBJECT_FEATURE";

    @Override
    public SimpleFeature buildFeature(DBObject rootDBO, SimpleFeatureType featureType) {

        String gdLocalName = featureType.getGeometryDescriptor().getLocalName();
        List<AttributeDescriptor> adList = featureType.getAttributeDescriptors();

        List<Object> values = new ArrayList<Object>(adList.size());
        for (AttributeDescriptor descriptor : adList) {
            String adLocalName = descriptor.getLocalName();
            if (gdLocalName.equals(adLocalName)) {
                values.add(getGeometry(rootDBO));
            } else {
                String path = getPropertyPath(adLocalName);
                Object o = path == null ? null : MongoUtil.getDBOValue(rootDBO, path);
                values.add(
                        o == null
                                ? null
                                : Converters.convert(o, descriptor.getType().getBinding()));
            }
        }
        SimpleFeature feature =
                new MongoFeature(
                        rootDBO, values.toArray(), featureType, rootDBO.get("_id").toString());
        // we store a reference to the original feature in the user data
        feature.getUserData().put(MONGO_OBJECT_FEATURE_KEY, feature);
        return feature;
    }
}
