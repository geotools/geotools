/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.parse;

import java.util.List;

/**
 * A zoom context specified by an explicit list of scale denominators.
 *
 * @author Kevin Smith, Boundless
 * @deprecated Use {@link org.geotools.styling.zoom.ListZoomContext} instead.
 */
@Deprecated
public class ListZoomContext extends org.geotools.styling.zoom.ListZoomContext {
    public ListZoomContext(List<Double> scales, int initial) {
        super(scales, initial);
    }

    public ListZoomContext(List<Double> scales) {
        super(scales);
    }
}
