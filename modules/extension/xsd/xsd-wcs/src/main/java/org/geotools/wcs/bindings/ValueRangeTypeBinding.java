package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:valueRangeType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="valueRangeType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;The range of an interval. If the "min" or "max" element is not included, there is no value limit in that direction. Inclusion of the specified minimum and maximum values in the range shall be defined by the "closure". (The interval can be bounded or semi-bounded with different closures.) The data type and the semantic of the values are inherited by children and may be superceded by them. This range may be qualitative, i.e., nominal (age range) or qualitative (percentage) meaning that a value between min/max can be queried. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element minOccurs="0" name="min" type="wcs:TypedLiteralType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Minimum value of this numeric parameter. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="max" type="wcs:TypedLiteralType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Maximum value of this numeric parameter. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute ref="wcs:type" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Can be omitted when the datatype of values in this interval is string, or the "type" attribute is included in an enclosing element. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *      &lt;attribute ref="wcs:semantic" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Can be omitted when the semantics or meaning of values in this interval is clearly specified elsewhere, or the "semantic" attribute is included in an enclosing element. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *      &lt;attribute default="false" name="atomic" type="boolean" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;What does this attribute mean? Is it useful and not redundant? When should this attribute be included or omitted? TBD. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *      &lt;attribute ref="wcs:closure" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Shall be included unless the default value applies. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class ValueRangeTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.valueRangeType;
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