/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectortiles.store;

import io.tileverse.vectortile.model.VectorTile;
import io.tileverse.vectortile.model.VectorTile.Layer.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;

/**
 * {@code PropertyAccessorFactory} that can access properties of {@link Feature VectorTile.Layer.Feature} so
 * {@link Filter filters} can be evaluated against them before converting to {@link SimpleFeature}.
 */
public class VectorTilesFeaturePropertyAccessorFactory implements PropertyAccessorFactory {

    /**
     * Conventional property name used to indicate the "default geometry" of a feature, that is, the one returned by
     * {@link Feature#getDefaultGeometryProperty()} or {@link SimpleFeature.getDefaultGeometry()}.
     */
    private static final String DEFAULT_GEOMETRY_NAME = "";

    /**
     * Returns a {@link PropertyAccessor} that can handle {@link Feature VectorTile.Layer.Feature}, and is used to
     * pre-filter vector tiles features before converting them to {@link SimpleFeature}.
     *
     * <p>{@inheritDoc}
     */
    @Override
    public PropertyAccessor createPropertyAccessor(Class<?> type, String xpath, Class<?> target, Hints hints) {
        if (VectorTile.Layer.Feature.class.isAssignableFrom(type)) {
            return VectorTilePropertyAccessor;
        }
        return null;
    }

    private static final PropertyAccessor VectorTilePropertyAccessor = new PropertyAccessor() {

        @Override
        public boolean canHandle(Object object, String xpath, Class<?> target) {
            return object instanceof VectorTile.Layer.Feature;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(Object object, String xpath, Class<T> target) throws IllegalArgumentException {
            VectorTile.Layer.Feature vtFeature = (VectorTile.Layer.Feature) object;
            Object value;
            if (isGeometryAttribute(xpath)) {
                value = vtFeature.getGeometry();
            } else {
                value = vtFeature.getAttribute(xpath);
            }
            if (target != null) {
                return Converters.convert(value, target);
            }
            return (T) value;
        }

        private boolean isGeometryAttribute(String xpath) {
            return DEFAULT_GEOMETRY_NAME.equals(xpath) || "the_geom".equals(xpath) || xpath.startsWith("the_geom_");
        }

        @Override
        public <T> void set(Object object, String xpath, T value, Class<T> target) throws IllegalArgumentException {
            throw new UnsupportedOperationException(
                    "This property accessor is meant for read-only access to VectorTile.Layer.Feature properties");
        }
    };
}
