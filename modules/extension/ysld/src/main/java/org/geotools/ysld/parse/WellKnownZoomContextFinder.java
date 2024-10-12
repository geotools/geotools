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
 * A collection of commonly used ZoomContexts
 *
 * @author Kevin Smith, Boundless
 * @deprecated Use {@link org.geotools.styling.zoom.WellKnownZoomContextFinder} instead.
 */
public class WellKnownZoomContextFinder extends org.geotools.styling.zoom.WellKnownZoomContextFinder {

    private static WellKnownZoomContextFinder INSTANCE = new WellKnownZoomContextFinder();

    public static WellKnownZoomContextFinder getInstance() {
        return INSTANCE;
    }

    protected WellKnownZoomContextFinder() {}
}
