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


import java.util.Date;

import org.geotools.xml.*;

import org.geotools.po.Items;
import org.geotools.po.ObjectFactory;		
import org.geotools.po.PurchaseOrderType;
import org.geotools.po.USAddress;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.geotools.org/po:PurchaseOrderType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:complexType name="PurchaseOrderType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="shipTo" type="USAddress"/&gt;
 *          &lt;xsd:element name="billTo" type="USAddress"/&gt;
 *          &lt;xsd:element minOccurs="0" ref="comment"/&gt;
 *          &lt;xsd:element name="items" type="Items"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name="orderDate" type="xsd:date"/&gt;
 *  &lt;/xsd:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class PurchaseOrderTypeBinding extends AbstractComplexBinding {

	ObjectFactory factory;		
	public PurchaseOrderTypeBinding( ObjectFactory factory ) {
		this.factory = factory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return PO.PurchaseOrderType;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return PurchaseOrderType.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
		
		PurchaseOrderType purchaseOrder = factory.createPurchaseOrderType();
		
		purchaseOrder.setShipTo( (USAddress) node.getChildValue( "shipTo" ) );
		purchaseOrder.setBillTo( (USAddress) node.getChildValue( "billTo" ) );
		purchaseOrder.setComment( (String) node.getChildValue( "comment" ) );
		purchaseOrder.setItems( (Items) node.getChildValue( "items") );
		
		return purchaseOrder;
	}

}
