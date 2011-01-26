package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:_GetCapabilities.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="_GetCapabilities"&gt;
 *      &lt;sequence&gt;
 *          &lt;element default="/" minOccurs="0" name="section" type="wcs:CapabilitiesSectionType"/&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute fixed="WCS" name="service" type="string" use="required"/&gt;
 *      &lt;attribute fixed="1.0.0" name="version" type="string" use="optional"/&gt;
 *      &lt;attribute name="updateSequence" type="string" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Service metadata (Capabilities) document version, having values that are "increased" whenever any change is made in service metadata document. Values are selected by each server, and are always opaque to clients. When omitted or not supported by server, server shall return latest complete service metadata document. &lt;/documentation&gt;
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
public class _GetCapabilitiesBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS._GetCapabilities;
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