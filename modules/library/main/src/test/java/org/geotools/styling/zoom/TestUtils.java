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

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public enum TestUtils {
    ;

    public static Matcher<ScaleRange> rangeContains(double scale) {
        return describedAs(
                "scale range that contains 1:%0",
                allOf(
                        Matchers.hasProperty("maxDenom", greaterThan(scale)),
                        Matchers.hasProperty("minDenom", lessThanOrEqualTo(scale))),
                scale);
    }
}
