package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:valueEnumBaseType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="valueEnumBaseType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;List of all the valid values and/or ranges of values for this variable. For numeric variables, signed values shall be ordered from negative infinity to positive infinity. For intervals, the "type" and "semantic" attributes are inherited by children elements, but can be superceded by them. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;choice maxOccurs="unbounded"&gt;
 *          &lt;element ref="wcs:interval"/&gt;
 *          &lt;element ref="wcs:singleValue"/&gt;
 *      &lt;/choice&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class ValueEnumBaseTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.valueEnumBaseType;
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