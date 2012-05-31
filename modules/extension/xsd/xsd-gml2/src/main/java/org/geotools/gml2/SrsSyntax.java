/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml2;

/**
 * Enumeration describing the syntax to use for an srsName URI.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public enum SrsSyntax {

    /**
     * Commonly used syntax outside of gml that follows the form: <pre>EPSG:1234</pre>.
     */
    EPSG_CODE ("EPSG:"),

    /**
     * First form of url syntax used by GML 2.1.2 that follows the form:
     * 
     * <pre>http://www.opengis.net/gml/srs/epsg.xml#1234</pre>.
     */
    OGC_HTTP_URL ("http://www.opengis.net/gml/srs/epsg.xml#"),

    /**
     * First form of urn syntax used by GML 3 that follows the form:
     * 
     * <pre>urn:x-ogc:def:crs:EPSG:1234</pre>.
     */
    OGC_URN_EXPERIMENTAL ("urn:x-ogc:def:crs:EPSG:"),

    /**
     * Revised form of urn syntax used by GML 3 that follows the form:
     * 
     * <pre>urn:ogc:def:crs:EPSG::1234</pre>.
     */
    OGC_URN ("urn:ogc:def:crs:EPSG::"),

    /**
     * Newest form from OGC using a url syntax of the form:
     * 
     * <pre>"http://www.opengis.net/def/crs/EPSG/0/1234</pre>.
     */
    OGC_HTTP_URI ("http://www.opengis.net/def/crs/EPSG/0/");

    private String prefix;

    private SrsSyntax(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
