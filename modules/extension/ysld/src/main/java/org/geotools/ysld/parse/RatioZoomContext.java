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
 */
public class RatioZoomContext extends ContinuousZoomContext implements ZoomContext {

    final int initialLevel;

    final double initialScale;

    final double ratio;

    /**
     * Create a zoom context where zoom level 0 has a scale denominator of {@code initial}, and each
     * subsequent level is {@code ratio} times that.
     *
     * @param initilScalel scale denominator at level 0
     * @param ratio ratio between zoom levels
     */
    public RatioZoomContext(final double initialScale, final double ratio) {
        this(0, initialScale, ratio);
    }

    /**
     * Create a zoom context where zoom level {@code initialLevel} has a scale denominator of {@code
     * initial}, and each subsequent level is {@code ratio} times that.
     *
     * @param initialLevel Level to use as initial
     * @param initialScale scale denominator at level {@code initialLevel}
     * @param ratio ratio between the scale at consecutive zoom levels. Zoom level z+1 has a scale
     *     ratio times that of z and a scale denominator 1/ratio times that of z.
     */
    public RatioZoomContext(final int initialLevel, final double initialScale, final double ratio) {
        super();
        if (initialScale <= 0) {
            throw new IllegalArgumentException("initialScale must be greater than 0");
        }
        if (ratio <= 1) {
            throw new IllegalArgumentException("ratio must be greater than 1");
        }
        this.initialLevel = initialLevel;
        this.initialScale = initialScale;
        this.ratio = ratio;
    }

    @Override
    protected double getScaleDenominator(double level) {
        // We're returning the denominator, so divide by the ratio
        return initialScale / Math.pow(ratio, level - initialLevel);
    }

    protected double getMedialScale(int level) {
        return getScaleDenominator(level + 0.5d);
    }

    @Override
    public boolean isInRange(int level) {
        return true;
    }
}
