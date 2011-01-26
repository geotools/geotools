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

import org.geotools.xml.*;

import org.geotools.po.ObjectFactory;		
import org.geotools.po.USAddress;

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.geotools.org/po:USAddress.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:complexType name="USAddress"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="name" type="xsd:string"/&gt;
 *          &lt;xsd:element name="street" type="xsd:string"/&gt;
 *          &lt;xsd:element name="city" type="xsd:string"/&gt;
 *          &lt;xsd:element name="state" type="xsd:string"/&gt;
 *          &lt;xsd:element name="zip" type="xsd:decimal"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute fixed="US" name="country" type="xsd:NMTOKEN"/&gt;
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
public class USAddressBinding extends AbstractComplexBinding {

	ObjectFactory factory;		
	public USAddressBinding( ObjectFactory factory ) {
		this.factory = factory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return PO.USAddress;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return null;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
		
		USAddress address = factory.createUSAddress();
		
		//elements
		address.setName( (String) node.getChildValue( "name" ) );
		address.setStreet( (String) node.getChildValue( "street" ) );
		address.setCity( (String) node.getChildValue( "city" ) );
		address.setState( (String) node.getChildValue( "state" ) );
		address.setZip( (BigDecimal) node.getChildValue( "zip" ) );
		
		//attribute
		address.setCountry( (String) node.getAttributeValue( "country" ) );
		
		return address;
	}

}
