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
 * attributes in the http://www.opengis.net/WCS_service-extension_scaling/1.0 schema.
 *
 * @generated
 */
public final class Scaling extends XSD {

    /** singleton instance */
    private static final Scaling instance = new Scaling();
    
    /**
     * Returns the singleton instance.
     */
    public static final Scaling getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private Scaling() {
    }
    
    protected void addDependencies(Set dependencies) {
       // 
    }
    
    /**
     * Returns 'http://www.opengis.net/WCS_service-extension_scaling/1.0'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'rsub.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("scal/v1_0/scal.xsd").toString();
    }
    
    public static final String NAMESPACE = "http://www.opengis.net/WCS_service-extension_scaling/1.0";


    public static final QName ScalingType = new QName(NAMESPACE, "ScalingType");

    public static final QName ScaleByFactorType = new QName(NAMESPACE, "ScaleByFactorType");
    
    public static final QName ScaleAxesByFactorType = new QName(NAMESPACE, "ScaleAxesByFactorType");
    
    public static final QName ScaleAxisType = new QName(NAMESPACE, "ScaleAxisType");
    
    public static final QName ScaleToSizeType = new QName(NAMESPACE, "ScaleToSizeType");
    
    public static final QName TargetAxisSizeType = new QName(NAMESPACE, "TargetAxisSizeType");
    
    public static final QName ScaleToExtentType = new QName(NAMESPACE, "ScaleToExtentType");
    
    public static final QName TargetAxisExtentType = new QName(NAMESPACE, "TargetAxisExtentType");

}
    