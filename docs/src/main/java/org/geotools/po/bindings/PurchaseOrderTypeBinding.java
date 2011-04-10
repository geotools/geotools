package org.geotools.po.bindings;


import javax.xml.namespace.QName;

import org.geotools.po.ObjectFactory;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

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
 */
public class PurchaseOrderTypeBinding extends AbstractComplexBinding {

	ObjectFactory factory;		
	public PurchaseOrderTypeBinding( ObjectFactory factory ) {
		super();
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