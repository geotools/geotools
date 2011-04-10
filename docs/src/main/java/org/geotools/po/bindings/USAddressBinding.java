package org.geotools.po.bindings;


import javax.xml.namespace.QName;

import org.geotools.po.ObjectFactory;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

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
 */
public class USAddressBinding extends AbstractComplexBinding {

	ObjectFactory factory;		
	public USAddressBinding( ObjectFactory factory ) {
		super();
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
		
		//TODO: implement and remove call to super
		return super.parse(instance,node,value);
	}

}