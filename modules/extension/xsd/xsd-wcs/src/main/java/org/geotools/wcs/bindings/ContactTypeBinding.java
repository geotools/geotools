package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:ContactType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="ContactType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Information required to enable contact with the responsible person and/or organization. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element minOccurs="0" name="phone" type="wcs:TelephoneType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Telephone numbers at which the organization or individual may becontacted. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="address" type="wcs:AddressType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Physical and email address at which the organization or individualmay be contacted. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="onlineResource" type="wcs:OnlineResourceType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;On-line information that can be used to contact the individual ororganization. &lt;/documentation&gt;
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
public class ContactTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.ContactType;
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