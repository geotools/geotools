/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml2;

import java.util.concurrent.ConcurrentHashMap;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/** Holds a cache of FeatureTypes by Name. */
public class FeatureTypeCache {
    ConcurrentHashMap<Name, FeatureType> map = new ConcurrentHashMap<Name, FeatureType>();

    public FeatureType get(Name name) {
        return map.get(name);
    }

    public void put(FeatureType type) {
        FeatureType other = map.putIfAbsent(type.getName(), type);
        if (other != null) {
            if (!other.equals(type)) {
                String msg = "Type with same name already exists in cache.";
                throw new IllegalArgumentException(msg);
            }
        }
    }
}
