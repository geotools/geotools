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
package org.geotools.styling.zoom;

import org.geotools.api.style.Rule;

/**
 * Represents and provides utility methods for a scale range (e.g., "scale: [min, max]") that can be
 * applied to a given {@link Rule}.
 */
public class ScaleRange {
    final double minDenom;

    final double maxDenom;

    /**
     * Creates a new instance of ScaleRange.
     *
     * @param minDenom the minimum scale denominator
     * @param maxDenom the maximum scale denominator
     */
    public ScaleRange(double minDenom, double maxDenom) {
        if (!(minDenom >= 0 && !Double.isNaN(minDenom))) {
            throw new IllegalArgumentException("minDenom must be non-negative");
        }
        if (!(maxDenom >= 0 && !Double.isNaN(minDenom))) {
            throw new IllegalArgumentException("maxDenom must be non-negative");
        }
        if (!(minDenom <= maxDenom)) {
            throw new IllegalArgumentException(
                    "maxDenom must be greater than or equal to minDenom");
        }

        this.minDenom = minDenom;
        this.maxDenom = maxDenom;
    }

    /**
     * Applies this scale range to the given rule.
     *
     * @param r
     */
    public void applyTo(Rule r) {
        r.setMaxScaleDenominator(maxDenom);
        r.setMinScaleDenominator(minDenom);
    }

    /**
     * Checks if the given scale denominator is contained in this range.
     *
     * @param denom the scale denominator to check
     * @return true if the scale denominator is contained in this range, false otherwise
     */
    public boolean contains(double denom) {
        return minDenom <= denom && maxDenom > denom;
    }

    @Override
    public String toString() {
        return String.format("[1:%f, 1:%f)", minDenom, maxDenom);
    }

    /** Returns the minimum scale denominator. */
    public double getMinDenom() {
        return minDenom;
    }

    /** Returns the maximum scale denominator. */
    public double getMaxDenom() {
        return maxDenom;
    }
}
