/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.kml.v22;

import java.util.HashMap;
import java.util.Map;
import org.opengis.feature.simple.SimpleFeatureType;

public class SchemaRegistry {

    private Map<String, SimpleFeatureType> featureTypes;

    public SchemaRegistry() {
        this.featureTypes = new HashMap<String, SimpleFeatureType>();
    }

    public void add(String featureTypeName, SimpleFeatureType featureType) {
        featureTypes.put(featureTypeName, featureType);
    }

    public SimpleFeatureType get(String featureTypeName) {
        return featureTypes.get(featureTypeName);
    }

    public SimpleFeatureType get(SimpleFeatureType featureType) {
        return get(featureType.getName().getLocalPart());
    }
}
