/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.parse;

import org.geotools.mbstyle.MBStyle;

/**
 * Thrown if the MapBox JSON used by {@link MBStyle} does not conform to the MapBox specification
 *
 * @author Torben Barsballe (Boundless)
 */
public class MBFormatException extends RuntimeException {
    /** serialVersionUID */
    private static final long serialVersionUID = 8328125000220917830L;

    public MBFormatException(String message) {
        super(message);
    }

    public MBFormatException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
