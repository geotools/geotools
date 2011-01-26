package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:WCSCapabilityType_Request.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="WCSCapabilityType_Request"&gt;
 *      &lt;sequence&gt;
 *          &lt;element name="GetCapabilities"&gt;
 *              &lt;complexType&gt;
 *                  &lt;sequence&gt;
 *                      &lt;element maxOccurs="unbounded" name="DCPType" type="wcs:DCPTypeType"/&gt;
 *                  &lt;/sequence&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *          &lt;element name="DescribeCoverage"&gt;
 *              &lt;complexType&gt;
 *                  &lt;sequence&gt;
 *                      &lt;element maxOccurs="unbounded" name="DCPType" type="wcs:DCPTypeType"/&gt;
 *                  &lt;/sequence&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *          &lt;element name="GetCoverage"&gt;
 *              &lt;complexType&gt;
 *                  &lt;sequence&gt;
 *                      &lt;element maxOccurs="unbounded" name="DCPType" type="wcs:DCPTypeType"/&gt;
 *                  &lt;/sequence&gt;
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
public class WCSCapabilityType_RequestBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.WCSCapabilityType_Request;
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