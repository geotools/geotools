package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:ResponsiblePartyType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="ResponsiblePartyType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Identification of, and means of communication with, person(s) and organizations associated with the server. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;choice&gt;
 *              &lt;sequence&gt;
 *                  &lt;element name="individualName" type="string"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Name of the responsible person-surname, given name, title separated by a delimiter. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element minOccurs="0" name="organisationName" type="string"/&gt;
 *              &lt;/sequence&gt;
 *              &lt;element name="organisationName" type="string"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Name of the responsible organizationt. &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/element&gt;
 *          &lt;/choice&gt;
 *          &lt;element minOccurs="0" name="positionName" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Role or position of the responsible person. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="contactInfo" type="wcs:ContactType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Address of the responsible party. &lt;/documentation&gt;
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
public class ResponsiblePartyTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.ResponsiblePartyType;
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