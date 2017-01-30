/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.wps.xml;


import java.util.Set;
import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.opengis.net/gpkg schema.
 *
 * @generated
 */
public final class GPKG extends XSD {

    /** singleton instance */
    private static final GPKG instance = new GPKG();
    
    /**
     * Returns the singleton instance.
     */
    public static final GPKG getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private GPKG() {
    }
    
    protected void addDependencies(Set dependencies) {
       dependencies.add(FES.getInstance());
    }
    
    /**
     * Returns 'http://www.opengis.net/gpkg'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'gpkg.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("gpkg.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/gpkg";
    
    /* Type Definitions */
    /** @generated */
    public static final QName coveragetype = 
        new QName("http://www.opengis.net/gpkg","coveragetype");
    /** @generated */
    public static final QName geopkgtype = 
        new QName("http://www.opengis.net/gpkg","geopkgtype");
    /** @generated */
    public static final QName gridsettype = 
        new QName("http://www.opengis.net/gpkg","gridsettype");
    /** @generated */
    public static final QName gridtype = 
        new QName("http://www.opengis.net/gpkg","gridtype");
    /** @generated */
    public static final QName layertype = 
        new QName("http://www.opengis.net/gpkg","layertype");
    /** @generated */
    public static final QName geopkgtype_features = 
        new QName("http://www.opengis.net/gpkg","geopkgtype_features");
    /** @generated */
    public static final QName geopkgtype_tiles = 
        new QName("http://www.opengis.net/gpkg","geopkgtype_tiles");
    /** @generated */
    public static final QName gridsettype_grids = 
        new QName("http://www.opengis.net/gpkg","gridsettype_grids");
    /** @generated */
    public static final QName bboxtype = 
        new QName("http://www.opengis.net/gpkg","bboxtype");

    /* Elements */
    /** @generated */
    public static final QName geopackage = 
        new QName("http://www.opengis.net/gpkg","geopackage");

    /* Attributes */

}
    