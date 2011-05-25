package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:WCSCapabilityType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="WCSCapabilityType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;XML encoded WCS GetCapabilities operation response. The Capabilities document provides clients with service metadata about a specific service instance, including metadata about the coverages served. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element name="Request"&gt;
 *              &lt;complexType name="WCSCapabilityType_Request"&gt;
 *                  &lt;sequence&gt;
 *                      &lt;element name="GetCapabilities"&gt;
 *                          &lt;complexType&gt;
 *                              &lt;sequence&gt;
 *                                  &lt;element maxOccurs="unbounded"
 *                                      name="DCPType" type="wcs:DCPTypeType"/&gt;
 *                              &lt;/sequence&gt;
 *                          &lt;/complexType&gt;
 *                      &lt;/element&gt;
 *                      &lt;element name="DescribeCoverage"&gt;
 *                          &lt;complexType&gt;
 *                              &lt;sequence&gt;
 *                                  &lt;element maxOccurs="unbounded"
 *                                      name="DCPType" type="wcs:DCPTypeType"/&gt;
 *                              &lt;/sequence&gt;
 *                          &lt;/complexType&gt;
 *                      &lt;/element&gt;
 *                      &lt;element name="GetCoverage"&gt;
 *                          &lt;complexType&gt;
 *                              &lt;sequence&gt;
 *                                  &lt;element maxOccurs="unbounded"
 *                                      name="DCPType" type="wcs:DCPTypeType"/&gt;
 *                              &lt;/sequence&gt;
 *                          &lt;/complexType&gt;
 *                      &lt;/element&gt;
 *                  &lt;/sequence&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *          &lt;element name="Exception"&gt;
 *              &lt;complexType name="WCSCapabilityType_Exception"&gt;
 *                  &lt;sequence&gt;
 *                      &lt;element maxOccurs="unbounded" name="Format" type="string"/&gt;
 *                  &lt;/sequence&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="VendorSpecificCapabilities"&gt;
 *              &lt;complexType name="WCSCapabilityType_VendorSpecificCapabilities"&gt;
 *                  &lt;sequence&gt;
 *                      &lt;any/&gt;
 *                  &lt;/sequence&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute fixed="1.0.0" name="version" type="string" use="optional"/&gt;
 *      &lt;attribute name="updateSequence" type="string" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Service metadata document version, having values that are "increased" whenever any change is made in service metadata document. Values are selected by each server, and are always opaque to clients. When not supported by server, server shall not return this attribute. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/wcs/bindings/WCSCapabilityTypeBinding.java $
 */
public class WCSCapabilityTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.WCSCapabilityType;
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
