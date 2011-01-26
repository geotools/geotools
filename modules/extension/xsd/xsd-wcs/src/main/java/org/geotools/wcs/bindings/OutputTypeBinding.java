package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:OutputType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="OutputType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Asks for the GetCoverage response to be expressed in a particular Coordinate Reference System (crs) and encoded in a particular format. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element minOccurs="0" name="crs" type="gml:CodeType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Identifier of the Coordinate Reference System (crs) in which GetCoverage response shall be expressed. Identifier shall be among those listed under supportedCRSs in the DescribeCoverage XML response. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element name="format" type="gml:CodeType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Identifier of the format in which GetCoverage response shall be encoded. Identifier shall be among those listed under supportedFormats in the DescribeCoverage XML response. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
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
public class OutputTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.OutputType;
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