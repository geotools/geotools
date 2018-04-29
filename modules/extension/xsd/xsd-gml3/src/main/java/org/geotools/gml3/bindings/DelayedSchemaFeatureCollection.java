/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.bindings;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Subclass of {@link ListFeatureCollection} that computes the target schema from the first entry as
 * needed, instead of eagerly requesting one at construction time
 */
class DelayedSchemaFeatureCollection extends ListFeatureCollection {

    static final SimpleFeatureType PLACEHOLDER;

    static {
        SimpleFeatureType placeholder = null;
        try {
            placeholder = DataUtilities.createType("PLACEHOLDER", "");
        } catch (SchemaException e) {
            // unexpected
            throw new RuntimeException(e);
        }
        PLACEHOLDER = placeholder;
    }

    public DelayedSchemaFeatureCollection() {
        super((SimpleFeatureType) null);
    }

    @Override
    public SimpleFeatureType getSchema() {
        if (schema == null) {
            if (isEmpty()) {
                return PLACEHOLDER;
            } else {
                schema = list.get(0).getFeatureType();
            }
        }
        return schema;
    }

    @Override
    public boolean add(SimpleFeature f) {
        // maintain the bounds
        this.bounds = null;
        return this.list.add(f);
    }

    protected ReferencedEnvelope calculateBounds() {
        if (list.isEmpty()) {
            return new ReferencedEnvelope();
        } else {
            return super.calculateBounds();
        }
    }
}
