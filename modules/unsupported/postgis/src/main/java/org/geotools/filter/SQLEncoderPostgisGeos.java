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
package org.geotools.filter;

/**
 * Encodes a filter into a SQL WHERE statement for postgis.  With geos
 * installed.
 *
 * @deprecated use SQLEncoderPostgis with setSupportsGEOS(true)
 * @author Chris Holmes, TOPP
 *
 * @source $URL$
 * @version $Id$
 */
public class SQLEncoderPostgisGeos extends SQLEncoderPostgis
    implements org.geotools.filter.FilterVisitor {

    /**
     * Constructor for SQLEncoderPostgis with GEOS support
     * 
     * @deprecated use SQLEncoderPostgis with setSupportsGEOS(true)
     * 
     */
    public SQLEncoderPostgisGeos() {
        super();
        setSupportsGEOS(true);
        setLooseBbox(false);
    }
    
    /**
     * Constructor for SQLEncoderPostgis with GEOS support
     * 
     * @deprecated use SQLEncoderPostgis with setSupportsGEOS(true)
     * @param srid
     */
    public SQLEncoderPostgisGeos(int srid) {
        super(srid);
        setSupportsGEOS(true);
        setLooseBbox(false);
    }

}
