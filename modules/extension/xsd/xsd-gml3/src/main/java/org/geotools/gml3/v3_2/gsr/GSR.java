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
package org.geotools.gml3.v3_2.gsr;

import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.gml3.v3_2.StubbedGMLXSD;
import org.geotools.gml3.v3_2.gco.GCO;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.isotc211.org/2005/gsr schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public final class GSR extends StubbedGMLXSD {

    /** singleton instance */
    private static final GSR instance = new GSR();
    static {
        loadSchema( instance );
    }
    
    /**
     * Returns the singleton instance.
     */
    public static final GSR getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private GSR() {
    }
    
    protected void addDependencies(Set dependencies) {
        dependencies.add( GCO.getInstance() );
    }
    
    /**
     * Returns 'http://www.isotc211.org/2005/gsr'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'gsr.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("gsr.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.isotc211.org/2005/gsr";
    
    /* Type Definitions */
    /** @generated */
    public static final QName SC_CRS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gsr","SC_CRS_PropertyType");

    /* Elements */

    /* Attributes */

}
    
