/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;

/**
 * PreparedStatement inserts must be split in function of those criteria: - useExisting - type of the geometries
 *
 * <p>This class allows to do the splitting.
 */
class InsertionClassifier {
    public final boolean useExisting;
    public final Map<String, Class<? extends Geometry>> geometryTypes;

    public static Map<InsertionClassifier, Collection<SimpleFeature>> classify(
            SimpleFeatureType featureType, Collection<? extends SimpleFeature> features) {
        Map<InsertionClassifier, Collection<SimpleFeature>> kinds = new HashMap<>();
        for (SimpleFeature feature : features) {
            InsertionClassifier kind = new InsertionClassifier(featureType, feature);
            Collection<SimpleFeature> currents = kinds.get(kind);
            if (currents == null) {
                currents = new ArrayList<>();
                kinds.put(kind, currents);
            }
            currents.add(feature);
        }
        return kinds;
    }

    private InsertionClassifier(SimpleFeatureType featureType, SimpleFeature feature) {
        useExisting = useExisting(feature);
        geometryTypes = new TreeMap<>();
        for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            if (att instanceof GeometryDescriptor) {
                Geometry geometry = (Geometry) feature.getAttribute(att.getName());
                if (geometry == null) {
                    geometryTypes.put(att.getLocalName(), null);
                } else {
                    geometryTypes.put(att.getLocalName(), geometry.getClass());
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InsertionClassifier that = (InsertionClassifier) o;
        if (useExisting != that.useExisting) {
            return false;
        }
        return geometryTypes.equals(that.geometryTypes);
    }

    @Override
    public int hashCode() {
        int result = useExisting ? 1 : 0;
        result = 31 * result + geometryTypes.hashCode();
        return result;
    }

    public static boolean useExisting(SimpleFeature feature) {
        return Boolean.TRUE.equals(feature.getUserData().get(Hints.USE_PROVIDED_FID));
    }
}
