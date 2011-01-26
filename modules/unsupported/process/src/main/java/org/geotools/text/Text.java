/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.text;

import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;

public class Text {
    // additional methods needed to register additional
    // properties files at a later time.
    /**
     * Create a international string based on the provided English text.
     * <p>
     * We will hook up this method to a properties file at a later time,
     * making other translations available via the Factory SPI mechanism.
     * 
     * @param english
     */
    public static InternationalString text(String english){
        return new SimpleInternationalString( english );
    }
}
