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

/**
 * ZoomContext defined by an initial scale and a ratio between levels.
 *
 * @author Kevin Smith, Boundless
 * @deprecated Use {@link org.geotools.styling.zoom.RatioZoomContext} instead.
 */
@Deprecated
public class RatioZoomContext extends org.geotools.styling.zoom.RatioZoomContext {
    public RatioZoomContext(double initialScale, double ratio) {
        super(initialScale, ratio);
    }

    public RatioZoomContext(int initialLevel, double initialScale, double ratio) {
        super(initialLevel, initialScale, ratio);
    }
}
