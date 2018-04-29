/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.util.Date;

/**
 * Used internally by PostGIS dialect to signal the use of the "bigdate" domain type.
 *
 * <p>This type is used to store a date as a raw long value in order to provide a greather range of
 * date values than the built in postgresql timestamp types. Using a raw long allows for
 * representing a date range of approximately +- 290 million.
 */
class BigDate extends Date {

    private BigDate(long milliseconds) {
        super(milliseconds);
    }
}
