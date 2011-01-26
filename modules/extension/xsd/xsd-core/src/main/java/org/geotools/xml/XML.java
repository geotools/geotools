/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import javax.xml.namespace.QName;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.w3.org/XML/1998/namespace schema.
 *
 * @generated
 *
 * @source $URL$
 */
public final class XML extends XSD {

    /** singleton instance */
    private static final XML instance = new XML();
    
    /**
     * Returns the singleton instance.
     */
    public static final XML getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private XML() {
    }
    
    /**
     * Returns 'http://www.w3.org/XML/1998/namespace'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'xml.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("xml.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.w3.org/XML/1998/namespace";
    
    /* Type Definitions */

    /* Elements */

    /* Attributes */
    /** @generated */
    public static final QName base = 
        new QName("http://www.w3.org/XML/1998/namespace","base");
    /** @generated */
    public static final QName id = 
        new QName("http://www.w3.org/XML/1998/namespace","id");
    /** @generated */
    public static final QName lang = 
        new QName("http://www.w3.org/XML/1998/namespace","lang");
    /** @generated */
    public static final QName space = 
        new QName("http://www.w3.org/XML/1998/namespace","space");

}
    
