/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.function;

/** Utility functions for MBExpressions. */
public class MBFunctionUtil {

    /**
     * Compares two Objects for equality. Numeric values are converted to doubles before comparing.
     * Null objects are equal by definition. All other Objects will be compared with {@link
     * java.lang.Object#equals(java.lang.Object)}
     *
     * @param arg0 An Object to compare.
     * @param arg1 A second Object to compare.
     * @return {@link java.lang.Boolean#TRUE} if the arguments are equivalent, {@link
     *     java.lang.Boolean#FALSE} otherwise.
     */
    static Boolean argsEqual(final Object arg0, final Object arg1) {
        if (arg0 == null) {
            // arg0 is null, they are equal if arg1 is null, not equal otherwise
            return arg1 == null;
        }
        // arg0 is not null, if arg1 is null, they are not equal
        if (arg1 == null) {
            // not equal
            return false;
        }
        // both args are not null
        // if they are both Numeric, generalize to double
        final Class arg0Class = arg0.getClass();
        final Class arg1Class = arg1.getClass();
        if (Number.class.isAssignableFrom(arg0Class) && Number.class.isAssignableFrom(arg1Class)) {
            // both numeric
            return Number.class.cast(arg0).doubleValue() == Number.class.cast(arg1).doubleValue();
        }
        // at least one of the args is not numeric, just use Object.equals()
        return arg0.equals(arg1);
    }
}
