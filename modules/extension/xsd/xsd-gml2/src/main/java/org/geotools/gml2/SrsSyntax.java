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
 */
public enum SrsSyntax {

    /**
     * Deprecated, please use {@link #AUTH_CODE} instead
     *
     * <pre>EPSG:1234</pre>
     *
     * .
     */
    EPSG_CODE("EPSG:") {
        @Override
        public String getSRS(String authority, String code) {
            return authority + ":" + code;
        }
    },

    /**
     * Commonly used syntax outside of gml that follows the form:
     *
     * <pre>EPSG:1234</pre>
     *
     * .
     */
    AUTH_CODE("EPSG:") {
        @Override
        public String getSRS(String authority, String code) {
            return authority + ":" + code;
        }
    },

    /**
     * First form of url syntax used by GML 2.1.2 that follows the form:
     *
     * <pre>http://www.opengis.net/gml/srs/epsg.xml#1234</pre>
     *
     * .
     */
    OGC_HTTP_URL("http://www.opengis.net/gml/srs/epsg.xml#") {
        @Override
        public String getSRS(String authority, String code) {
            return "http://www.opengis.net/gml/srs/" + authority.toLowerCase() + ".xml#" + code;
        }
    },

    /**
     * First form of urn syntax used by GML 3 that follows the form:
     *
     * <pre>urn:x-ogc:def:crs:EPSG:1234</pre>
     *
     * .
     */
    OGC_URN_EXPERIMENTAL("urn:x-ogc:def:crs:EPSG:") {
        @Override
        public String getSRS(String authority, String code) {
            return "urn:x-ogc:def:crs:" + authority + ":" + code;
        }
    },

    /**
     * Revised form of urn syntax used by GML 3 that follows the form:
     *
     * <pre>urn:ogc:def:crs:EPSG::1234</pre>
     *
     * .
     */
    OGC_URN("urn:ogc:def:crs:EPSG::") {
        @Override
        public String getSRS(String authority, String code) {
            return "urn:ogc:def:crs:" + authority + "::" + code;
        }
    },

    /**
     * Newest form from OGC using a url syntax of the form:
     *
     * <pre>"http://www.opengis.net/def/crs/EPSG/0/1234</pre>
     *
     * .
     */
    OGC_HTTP_URI("http://www.opengis.net/def/crs/EPSG/0/") {
        @Override
        public String getSRS(String authority, String code) {
            return "http://www.opengis.net/def/crs/" + authority + "/0/" + code;
        }
    };

    private String prefix;

    private SrsSyntax(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Prefix used by this syntax. Please use getSRS instead, in order to build a full URI, the
     * prefix assumes a fixed EPSG authority.
     */
    public String getPrefix() {
        return prefix;
    }

    public abstract String getSRS(String authority, String code);

    /**
     * Expects either an identifier in the form of "authority:code", or just a code. If just a code
     * then the "EPSG" authority is assumed.
     *
     * @param identifier
     * @return
     */
    public String getSRS(String identifier) {
        int idx = identifier.indexOf(":");
        if (idx == -1) return getSRS("EPSG", identifier);
        return getSRS(identifier.substring(0, idx), identifier.substring(idx + 1));
    }
}
