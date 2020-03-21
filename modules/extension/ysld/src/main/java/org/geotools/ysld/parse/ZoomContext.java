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

import javax.annotation.Nullable;

/**
 * Represents a mapping between zoom level and scale.
 *
 * @author Kevin Smith, Boundless
 */
public interface ZoomContext {

    public static final String HINT_ID = "ZoomContext";

    /**
     * Find the reciprocal of the scale at a specified zoom level in this context.
     *
     * @param level The level
     * @return The scale denominator
     */
    public double getScaleDenominator(int level);

    /**
     * Return a scale range covering the specified zoom level but no others.
     *
     * @param min Minimum zoom level. Absent for open ended.
     * @param max Maximum zoom level. Absent for open ended.
     */
    public ScaleRange getRange(@Nullable Integer min, @Nullable Integer max);

    /** Is the given level within the range for which the context has clearly defined scales. */
    public boolean isInRange(int level);
}
