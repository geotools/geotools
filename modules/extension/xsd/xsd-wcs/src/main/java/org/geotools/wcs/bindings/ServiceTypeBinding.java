package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:ServiceType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="ServiceType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A minimal, human readable rescription of the service. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="wcs:AbstractDescriptionType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="wcs:keywords"/&gt;
 *                  &lt;element minOccurs="0" name="responsibleParty" type="wcs:ResponsiblePartyType"/&gt;
 *                  &lt;element name="fees" type="gml:CodeListType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;A text string identifying any fees imposed by the service provider. The keyword NONE shall be used to mean no fees. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" name="accessConstraints" type="gml:CodeListType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;A text string identifying any access constraints imposed by the service provider. The keyword NONE shall be used to mean no access constraints are imposed. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute fixed="1.0.0" name="version" type="string" use="optional"/&gt;
 *              &lt;attribute name="updateSequence" type="string" use="optional"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Service metadata (Capabilities) document version, having values that are "increased" whenever any change is made in service metadata document. Values are selected by each server, and are always opaque to clients. When supported by server, server shall return this attribute. &lt;/documentation&gt;
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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/wcs/bindings/ServiceTypeBinding.java $
 */
public class ServiceTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.ServiceType;
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
