/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.sort.SortBy;
import org.locationtech.jts.geom.Geometry;

/** A QueryCapabilities implementation for a MongoSource. */
class MongoQueryCapabilities extends QueryCapabilities {

    private MongoFeatureSource source;

    MongoQueryCapabilities(MongoFeatureSource source) {
        this.source = source;
    }

    @Override
    public boolean supportsSorting(SortBy... sortAttributes) {
        for (SortBy sort : sortAttributes) {
            if (!supportsPropertySorting(sort.getPropertyName())) return false;
        }
        return true;
    }

    /**
     * Check if the property name corresponds to one of the FeatureType attributes.
     *
     * @param propertyName
     * @return true if a correspondence is found, false if not or the attribute is a Geometry.
     */
    private boolean supportsPropertySorting(PropertyName propertyName) {
        AttributeDescriptor descriptor = (AttributeDescriptor) propertyName.evaluate(source.getSchema());
        return descriptor != null
                && !Geometry.class.isAssignableFrom(descriptor.getType().getBinding());
    }
}
