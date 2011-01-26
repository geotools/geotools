package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:valueEnumType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="valueEnumType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;List of all the valid values and/or intervals of values for this variable. For numeric variables, signed values shall be ordered from negative infinity to positive infinity. For intervals, the type and semantic attributes are inherited by children elements, but can be superceded here. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="wcs:valueEnumBaseType"&gt;
 *              &lt;attribute ref="wcs:type" use="optional"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Should be included if the data type is not string, and the valueEnumBaseType does not include any "interval" elements that include this attribute. &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *              &lt;attribute ref="wcs:semantic" use="optional"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Should be included if the semantics or meaning is not clearly specified elsewhere, and the valueEnumBaseType does not include any "interval" elements that include this attribute. &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
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
public class ValueEnumTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.valueEnumType;
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