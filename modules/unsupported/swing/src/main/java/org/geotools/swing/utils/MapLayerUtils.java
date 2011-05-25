/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.swing.utils;

import java.util.Collection;

import org.geotools.map.MapLayer;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * Helper methods for swing module components that work with {@code MapLayer} objects.
 *
 * @todo Some (all ?) of this may be temporary
 *
 * @author Michael Bedward
 * @since 2.7
 *
 * @source $URL$
 * @version $Id$
 */
public class MapLayerUtils {

    private static final Class<?> BASE_GRID_CLASS = org.opengis.coverage.grid.GridCoverage.class;

    @SuppressWarnings("deprecation")
    private static final Class<?> BASE_READER_CLASS = org.opengis.coverage.grid.GridCoverageReader.class;

    /**
     * Check if the given map layer contains a grid coverage or a grid coverage reader.
     * <p>
     * Implementation note: we avoid referencing org.geotools.coverage.grid classes
     * directly here so that applications dealing only with other data types are not
     * forced to have JAI in the classpath.
     *
     * @param layer the map layer
     *
     * @return true if this is a grid layer; false otherwise
     */
    public static boolean isGridLayer(MapLayer layer) {

        Collection<PropertyDescriptor> descriptors = layer.getFeatureSource().getSchema().getDescriptors();
        for (PropertyDescriptor desc : descriptors) {
            Class<?> binding = desc.getType().getBinding();

            if (BASE_GRID_CLASS.isAssignableFrom(binding) || BASE_READER_CLASS.isAssignableFrom(binding)) {
                return true;
            }
        }

        return false;
    }

    public static String getGridAttributeName(MapLayer layer) {
        String attrName = null;

        Collection<PropertyDescriptor> descriptors = layer.getFeatureSource().getSchema().getDescriptors();
        for (PropertyDescriptor desc : descriptors) {
            Class<?> binding = desc.getType().getBinding();

            if (BASE_GRID_CLASS.isAssignableFrom(binding) || BASE_READER_CLASS.isAssignableFrom(binding)) {
                attrName = desc.getName().getLocalPart();
                break;
            }
        }

        return attrName;
    }
}
