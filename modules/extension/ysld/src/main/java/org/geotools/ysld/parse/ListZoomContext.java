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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A zoom context specified by an explicit list of scale denominators.
 *
 * @author Kevin Smith, Boundless
 */
public class ListZoomContext extends MedialZoomContext {
    final List<Double> scales;

    final int initial;

    /**
     * Checks if the list contains a decreasing sequence of finite, positive, non-null values.
     *
     * @throws IllegalArgumentException if the values are not decreasing, positive, and finite.
     * @throws NullPointerException if any values are null.
     */
    public static void validate(List<Double> scales) {
        double last = Double.POSITIVE_INFINITY;
        for (double x : scales) {
            if (x >= last)
                throw new IllegalArgumentException("Scale denominator list must be decreasing");
            if (x <= 0) throw new IllegalArgumentException("Scale Denominators must be positive");
            if (Double.isInfinite(x) || Double.isNaN(x))
                throw new IllegalArgumentException("Scale Denominators must be finite");
        }
    }

    public ListZoomContext(List<Double> scales, int initial) {
        super();
        validate(scales);
        this.scales = Collections.unmodifiableList(new ArrayList<>(scales));
        this.initial = initial;
    }

    public ListZoomContext(List<Double> scales) {
        this(scales, 0);
    }

    @Override
    public double getScaleDenominator(int level) {
        final int i = level - initial;
        if (i < 0) return Double.POSITIVE_INFINITY;
        if (i >= scales.size()) return 0;
        return scales.get(i);
    }

    @Override
    public double getMedialScale(int level) {
        final double scale = getScaleDenominator(level);
        final double nextScale = getScaleDenominator(level + 1);

        if (nextScale == 0) return 0;

        return Math.sqrt(
                scale * nextScale); // Geometric mean as zoom levels are usually exponential
    }

    @Override
    public boolean isInRange(int level) {
        return level >= initial && level - initial < scales.size();
    }
}
