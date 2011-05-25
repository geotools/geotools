package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:AxisDescriptionType_values.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="AxisDescriptionType_values"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="wcs:valueEnumType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element minOccurs="0" name="default" type="wcs:TypedLiteralType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Ordered sequence of the parameter value(s) that the server will use for GetCoverage requests which omit a constraint on this parameter axis. (GetCoverage requests against a coverage offering whose AxisDescription has no default must specify a valid constraint for this parameter.) &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/wcs/bindings/AxisDescriptionType_valuesBinding.java $
 */
public class AxisDescriptionType_valuesBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.AxisDescriptionType_values;
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
