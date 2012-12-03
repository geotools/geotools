package org.geotools.kml.v22.bindings;

import org.geotools.kml.v22.KML;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/kml/2.2:SchemaDataType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType final="#all" name="SchemaDataType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:AbstractObjectType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:SimpleData"/&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:SchemaDataExtension"/&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute name="schemaUrl" type="anyURI"/&gt;
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
public class SchemaDataTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return KML.SchemaDataType;
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