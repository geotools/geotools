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


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.xml.*;

import org.geotools.po.Items;
import org.geotools.po.ObjectFactory;		
import org.geotools.po.Items.Item;

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.geotools.org/po:Items.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:complexType name="Items"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" minOccurs="0" name="item"&gt;
 *              &lt;xsd:complexType&gt;
 *                  &lt;xsd:sequence&gt;
 *                      &lt;xsd:element name="productName" type="xsd:string"/&gt;
 *                      &lt;xsd:element name="quantity"&gt;
 *                          &lt;xsd:simpleType&gt;
 *                              &lt;xsd:restriction base="xsd:positiveInteger"&gt;
 *                                  &lt;xsd:maxExclusive value="100"/&gt;
 *                              &lt;/xsd:restriction&gt;
 *                          &lt;/xsd:simpleType&gt;
 *                      &lt;/xsd:element&gt;
 *                      &lt;xsd:element name="USPrice" type="xsd:decimal"/&gt;
 *                      &lt;xsd:element minOccurs="0" ref="comment"/&gt;
 *                      &lt;xsd:element minOccurs="0" name="shipDate" type="xsd:date"/&gt;
 *                  &lt;/xsd:sequence&gt;
 *                  &lt;xsd:attribute name="partNum" type="SKU" use="required"/&gt;
 *              &lt;/xsd:complexType&gt;
 *          &lt;/xsd:element&gt;
 *      &lt;/xsd:sequence&gt;
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
public class ItemsBinding extends AbstractComplexBinding {

	ObjectFactory factory;		
	public ItemsBinding( ObjectFactory factory ) {
		this.factory = factory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return PO.Items;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return Items.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
		
		//create the items collection
		Items items = factory.createItems();
		
		//for each item element, turn the map into an item
		List itemElements = node.getChildValues( "item" );
		for ( Iterator i = itemElements.iterator(); i.hasNext(); ) {
			Map map = (Map) i.next();
			
			//create the item
			Item item = factory.createItemsItem();
			item.setProductName( (String) map.get( "productName") );
			item.setQuantity( (BigInteger) map.get( "quantity" ) );
			item.setUSPrice( (BigDecimal) map.get( "USPrice" ) );
			item.setComment( (String) map.get( "comment" ) );
			item.setPartNum( (String) map.get( "partNum" ) );
		
			//add the item
			items.getItem().add( item );
		}
		
		return items;
	}

}
