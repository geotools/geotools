/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xs;

import java.util.Calendar;
import java.util.TimeZone;
import org.geotools.util.factory.Hints;

/** Util class for shared static methods for XS and its bindings. */
public final class XSUtils {

    private XSUtils() {}

    /**
     * Returns a configured Calendar object in base to System properties configurations, for use
     * with temporal types like Date, DateTime and Time.
     */
    public static Calendar getConfiguredCalendar() {
        Object hint = Hints.getSystemDefault(Hints.LOCAL_DATE_TIME_HANDLING);
        if (Boolean.TRUE.equals(hint)) {
            return Calendar.getInstance();
        } else {
            return Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        }
    }
}
