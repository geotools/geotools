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
package org.geotools.gml3.v3_2.gss;

import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.gml3.v3_2.StubbedGMLXSD;
import org.geotools.gml3.v3_2.gco.GCO;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.isotc211.org/2005/gss schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public final class GSS extends StubbedGMLXSD {

    /** singleton instance */
    private static final GSS instance = new GSS();
    static {
        loadSchema(instance);
    }
    
    /**
     * Returns the singleton instance.
     */
    public static final GSS getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private GSS() {
    }
    
    protected void addDependencies(Set dependencies) {
        dependencies.add( GCO.getInstance() );
        //dependencies.add( GML.getInstance() ); JD: this dep introduces a cycle
    }
    
    /**
     * Returns 'http://www.isotc211.org/2005/gss'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'gss.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("gss.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.isotc211.org/2005/gss";
    
    /* Type Definitions */
    /** @generated */
    public static final QName GM_Object_PropertyType = 
        new QName("http://www.isotc211.org/2005/gss","GM_Object_PropertyType");
    /** @generated */
    public static final QName GM_Point_PropertyType = 
        new QName("http://www.isotc211.org/2005/gss","GM_Point_PropertyType");

    /* Elements */

    /* Attributes */

}
    
