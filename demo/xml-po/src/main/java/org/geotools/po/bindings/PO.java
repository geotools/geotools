/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.po.bindings;


import javax.xml.namespace.QName;

import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.geotools.org/po schema.
 *
 * @generated
 *
 * @source $URL$
 */
public class PO extends XSD {

	/** @generated */
	public static final String NAMESPACE = "http://www.geotools.org/po";
	
	/* Type Definitions */
	/** @generated */
	public static final QName Items = 
		new QName("http://www.geotools.org/po","Items");
	/** @generated */
	public static final QName PurchaseOrderType = 
		new QName("http://www.geotools.org/po","PurchaseOrderType");
	/** @generated */
	public static final QName SKU = 
		new QName("http://www.geotools.org/po","SKU");
	/** @generated */
	public static final QName USAddress = 
		new QName("http://www.geotools.org/po","USAddress");

	/* Elements */
	/** @generated */
	public static final QName comment = 
		new QName("http://www.geotools.org/po","comment");
	/** @generated */
	public static final QName purchaseOrder = 
		new QName("http://www.geotools.org/po","purchaseOrder");
	/* Attributes */
	
	
    private PO() {}
    
    /**
     * singleton instance.
     */
    private static PO instance = new PO();
    
    /**
     * The singleton instance;
     */
    public static PO getInstance() {
        return instance;
    }

    @Override
    public String getNamespaceURI() {
        return NAMESPACE;
    }
    @Override
    public String getSchemaLocation() {
        return getClass().getResource("po.xsd").toString();
    }

}
	
