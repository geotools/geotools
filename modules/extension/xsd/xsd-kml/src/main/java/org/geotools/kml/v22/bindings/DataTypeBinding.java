package org.geotools.kml.v22.bindings;

import org.geotools.kml.v22.KML;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/kml/2.2:DataType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType final="#all" name="DataType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:AbstractObjectType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element minOccurs="0" ref="kml:displayName"/&gt;
 *                  &lt;element ref="kml:value"/&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:DataExtension"/&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute name="name" type="string"/&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class DataTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return KML.DataType;
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