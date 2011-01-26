/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.smil;

import org.geotools.xml.XSD;


public class XMLMOD extends XSD {
    /**
     * Singleton instance.
     */
    private static XMLMOD instance = new XMLMOD();

    /**
     * private constructor.
     */
    private XMLMOD() {
    }

    public static XMLMOD getInstance() {
        return instance;
    }

    /**
     * Returns 'http://www.w3.org/XML/1998/namespace'.
     */
    public String getNamespaceURI() {
        return "http://www.w3.org/XML/1998/namespace";
    }

    /**
     * Returns the location of 'xml-mod.xsd'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("xml-mod.xsd").toString();
    }
}
