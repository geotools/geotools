/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.v1_x;

import javax.xml.namespace.QName;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.spatial.BBOX;

/**
 * This strategy addresses a bug in most MapServer implementations where a filter is required in
 * order for all the features to be returned. So if the Filter is Filter.NONE or Query.ALL then a
 * BBox Filter is constructed that is the entire layer.
 */
public class MapServerWFSStrategy extends StrictWFS_1_x_Strategy {

    private static final FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);

    public MapServerWFSStrategy() {
        super();
    }

    @Override
    public Filter[] splitFilters(final QName typeName, final Filter filter) {

        Filter[] splitFilters = super.splitFilters(typeName, filter);

        Filter supported = splitFilters[0];

        if (Filter.INCLUDE.equals(supported)) {

            ReferencedEnvelope wgs84Bounds = super.getFeatureTypeInfo(typeName)
                    .getWGS84BoundingBox();

            BBOX newFilter;
            if (wgs84Bounds == null) {
                newFilter = fac.bbox(null, -180, -90, 180, 90, "EPSG:4326");
            } else {
                newFilter = fac.bbox(null, wgs84Bounds.getMinX(), wgs84Bounds.getMinY(),
                        wgs84Bounds.getMaxX(), wgs84Bounds.getMaxY(), "EPSG:4326");
            }

            splitFilters[0] = newFilter;
        }
        return splitFilters;
    }
}
