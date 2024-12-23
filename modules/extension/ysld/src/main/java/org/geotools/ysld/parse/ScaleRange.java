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

import org.geotools.api.style.Rule;

/**
 * Represents and provides utility methods for a scale range (e.g., "scale: [min, max]") that can be applied to a given
 * {@link Rule}.
 *
 * @deprecated Use {@link org.geotools.styling.zoom.ScaleRange} instead.
 */
public class ScaleRange extends org.geotools.styling.zoom.ScaleRange {
    public ScaleRange(double minDenom, double maxDenom) {
        super(minDenom, maxDenom);
    }
}
