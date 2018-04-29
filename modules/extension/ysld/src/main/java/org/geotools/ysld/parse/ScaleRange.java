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

import org.geotools.styling.Rule;

/**
 * Represents and provides utility methods for a scale range (e.g., "scale: [min, max]") that can be
 * applied to a given {@link Rule}.
 */
public class ScaleRange {
    final double minDenom;

    final double maxDenom;

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

    public void applyTo(Rule r) {
        r.setMaxScaleDenominator(maxDenom);
        r.setMinScaleDenominator(minDenom);
    }

    public boolean contains(double denom) {
        return minDenom <= denom && maxDenom > denom;
    }

    @Override
    public String toString() {
        return String.format("[1:%f, 1:%f)", minDenom, maxDenom);
    }

    public double getMinDenom() {
        return minDenom;
    }

    public double getMaxDenom() {
        return maxDenom;
    }
}
