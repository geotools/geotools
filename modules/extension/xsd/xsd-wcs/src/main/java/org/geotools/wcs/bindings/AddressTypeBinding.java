package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:AddressType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="AddressType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Location of the responsible individual or organization. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0"
 *              name="deliveryPoint" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Address line for the location (as described in ISO 11180, Annex A). &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="city" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;City of the location. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="administrativeArea" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;State ot province of the location. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="postalCode" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;ZIP or other postal code. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="country" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Country of the physical address. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0"
 *              name="electronicMailAddress" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Address of the electronic mailbox of the responsible organization or individual. &lt;/documentation&gt;
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
public class AddressTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.AddressType;
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