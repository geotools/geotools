package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:AxisDescriptionType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="AxisDescriptionType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Description of a measured or observed quantity, and list of the “valid” quantity values (values for which measurements are available or “by which” aggregate values are available). The semantic is the URI of the quantity (for example observable or mathematical variable). The refSys attribute is a URI to a reference system, and the refSysLabel is the label used by client to refer the reference system. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="wcs:AbstractDescriptionType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element name="values"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;The type and value constraints for the values of this axis.&lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                      &lt;complexType name="AxisDescriptionType_values"&gt;
 *                          &lt;complexContent&gt;
 *                              &lt;extension base="wcs:valueEnumType"&gt;
 *                                  &lt;sequence&gt;
 *                                      &lt;element minOccurs="0"
 *                                      name="default" type="wcs:TypedLiteralType"&gt;
 *                                      &lt;annotation&gt;
 *                                      &lt;documentation&gt;Ordered sequence of the parameter value(s) that the server will use for GetCoverage requests which omit a constraint on this parameter axis. (GetCoverage requests against a coverage offering whose AxisDescription has no default must specify a valid constraint for this parameter.) &lt;/documentation&gt;
 *                                      &lt;/annotation&gt;
 *                                      &lt;/element&gt;
 *                                  &lt;/sequence&gt;
 *                              &lt;/extension&gt;
 *                          &lt;/complexContent&gt;
 *                      &lt;/complexType&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute ref="wcs:semantic" use="optional"/&gt;
 *              &lt;attribute name="refSys" type="anyURI" use="optional"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Pointer to the reference system in which values are expressed. This attribute shall be included either here or in RangeSetType. &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *              &lt;attribute name="refSysLabel" type="string" use="optional"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Short human-readable label denoting the reference system, for human interface display. This attribute shall be included either here or in RangeSetType. &lt;/documentation&gt;
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
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/wcs/bindings/AxisDescriptionTypeBinding.java $
 */
public class AxisDescriptionTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.AxisDescriptionType;
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
