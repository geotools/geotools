package org.geotools.gml4wcs.bindings;


import org.geotools.gml4wcs.GML;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gml:StringOrRefType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="StringOrRefType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;
 *  This type is available wherever there is a need for a "text" type property. It is of string type, so the text can be included inline, but the value can also be referenced remotely via xlinks from the AssociationAttributeGroup. If the remote reference is present, then the value obtained by traversing the link should be used, and the string content of the element can be used for an annotation.  &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base="string"&gt;
 *              &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
 *          &lt;/extension&gt;
 *      &lt;/simpleContent&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class StringOrRefTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return GML.StringOrRefType;
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