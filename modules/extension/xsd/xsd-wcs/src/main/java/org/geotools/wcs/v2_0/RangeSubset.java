/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wcs.v2_0;


import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.opengis.net/wcs/range-subsetting/1.0 schema.
 *
 * @generated
 */
public final class RangeSubset extends XSD {

    /** singleton instance */
    private static final RangeSubset instance = new RangeSubset();
    
    /**
     * Returns the singleton instance.
     */
    public static final RangeSubset getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private RangeSubset() {
    }
    
    protected void addDependencies(Set dependencies) {
       // 
    }
    
    /**
     * Returns 'http://www.opengis.net/wcs/range-subsetting/1.0'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'rsub.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("range_subsetting/v1_0/rsub.xsd").toString();
    }
    
    public static final String NAMESPACE = "http://www.opengis.net/wcs/range-subsetting/1.0";


    /** Range subset extension **/
    public static final QName rangeSubsetType = new QName(NAMESPACE, "rangeSubsetType");

    public static final QName rangeItemType = new QName(NAMESPACE, "rangeItemType");

    public static final QName rangeIntervalType = new QName(NAMESPACE,
            "rangeIntervalType");
    

}
    