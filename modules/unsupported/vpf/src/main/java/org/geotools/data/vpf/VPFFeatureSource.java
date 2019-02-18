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
 */

package org.geotools.data.vpf;

import java.util.HashMap;
import java.util.Map;
import org.geotools.data.Query;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;

/**
 * VPFFeature Source
 *
 * @author James Gambale (Alysida AI)
 */
public abstract class VPFFeatureSource extends ContentFeatureSource {

    private static Map<String, VPFFeatureSource> featureSourceMap = new HashMap<>();

    public VPFFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
        if (entry == null) {
            VPFLogger.log("NULL ContentEntry in VPFFeatureSource");
        } else {
            String typeName = entry.getTypeName();
            if (typeName != null) {
                VPFFeatureSource.setFeatureSource(typeName, this);
            } else {
                VPFLogger.log("NULL ContentEntry typeName in VPFFeatureSource");
            }
        }
    }

    public Name getName() {
        String typeName = entry != null ? entry.getTypeName() : null;

        if (typeName != null) return new NameImpl(typeName);
        else return super.getName();
    }

    public static void setFeatureSource(String typeName, VPFFeatureSource featureSource) {
        featureSourceMap.put(typeName, featureSource);
    }

    public static VPFFeatureSource getFeatureSource(String typeName) {
        return featureSourceMap.get(typeName);
    }

    public ContentDataStore getDataStore() {
        return super.getDataStore();
    }
}
