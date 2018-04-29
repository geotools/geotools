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
 * Zoom Context supporting non-integer levels.
 *
 * @author Kevin Smith, Boundless
 */
public abstract class ContinuousZoomContext extends MedialZoomContext implements ZoomContext {

    public ContinuousZoomContext() {
        super();
    }

    /**
     * Find the reciprocal of the scale at a specified zoom level in this context.
     *
     * @param level The level
     * @return The scale denominator
     */
    protected abstract double getScaleDenominator(double level);

    /**
     * Find the reciprocal of the scale at a specified zoom level in this context.
     *
     * @param level The level
     * @return The scale denominator
     */
    @Override
    public double getScaleDenominator(int level) {
        return getScaleDenominator(level + 0d);
    }

    /** Get a scale between the given zoom level and the next */
    @Override
    protected double getMedialScale(int level) {
        return getScaleDenominator(level + 0.5d);
    }
}
