package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:SupportedCRSsType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="SupportedCRSsType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Unordered list(s) of identifiers of Coordinate Reference Systems (CRSs) supported in server operation requests and responses. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;choice&gt;
 *              &lt;element maxOccurs="unbounded" name="requestResponseCRSs" type="gml:CodeListType"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Unordered list of identifiers of the CRSs in which the server can both accept requests and deliver responses for this data. These CRSs should include the native CRSs defined below. &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/element&gt;
 *              &lt;sequence&gt;
 *                  &lt;element maxOccurs="unbounded" name="requestCRSs" type="gml:CodeListType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of identifiers of the CRSs in which the server can accept requests for this data. These CRSs should include the native CRSs defined below. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" name="responseCRSs" type="gml:CodeListType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of identifiers of the CRSs in which the server can deliver responses for this data. These CRSs should include the native CRSs defined below. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *          &lt;/choice&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" name="nativeCRSs" type="gml:CodeListType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Unordered list of identifiers of the CRSs in which the server stores this data, that is, the CRS(s) in which data can be obtained without any distortion or degradation. &lt;/documentation&gt;
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
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/wcs/bindings/SupportedCRSsTypeBinding.java $
 */
public class SupportedCRSsTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.SupportedCRSsType;
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
