package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:WCS_CapabilitiesType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="WCS_CapabilitiesType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Metadata for a WCS server, also known as Capabilities document. Reply from a WCS that performed the GetCapabilities operation. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element ref="wcs:Service"/&gt;
 *          &lt;element ref="wcs:Capability"/&gt;
 *          &lt;element ref="wcs:ContentMetadata"/&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute fixed="1.0.0" name="version" type="string" use="required"/&gt;
 *      &lt;attribute name="updateSequence" type="string" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Service metadata (Capabilities) document version, having values that are "increased" whenever any change is made in service metadata document. Values are selected by each server, and are always opaque to clients. When supported by server, server shall return this attribute. &lt;/documentation&gt;
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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/wcs/bindings/WCS_CapabilitiesTypeBinding.java $
 */
public class WCS_CapabilitiesTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.WCS_CapabilitiesType;
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
