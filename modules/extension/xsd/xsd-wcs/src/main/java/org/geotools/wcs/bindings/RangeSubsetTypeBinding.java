package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:RangeSubsetType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="RangeSubsetType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Definition of a subset of the named coverage range(s). Currently, only a value enumeration definition of a range subset. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" name="axisSubset"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Ordered sequence of points and/or intervals along one axis of a compound range set. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *              &lt;complexType name="RangeSubsetType_axisSubset"&gt;
 *                  &lt;complexContent&gt;
 *                      &lt;extension base="wcs:valueEnumBaseType"&gt;
 *                          &lt;attribute name="name" type="string" use="required"&gt;
 *                              &lt;annotation&gt;
 *                                  &lt;documentation&gt;Name or identifier of one axis in this coverage. This name shall match that of an AxisDescription element in the DescribeCoverage XML response.  &lt;/documentation&gt;
 *                              &lt;/annotation&gt;
 *                          &lt;/attribute&gt;
 *                      &lt;/extension&gt;
 *                  &lt;/complexContent&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class RangeSubsetTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.RangeSubsetType;
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