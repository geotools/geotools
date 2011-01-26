package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:AbstractDescriptionType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType abstract="true" name="AbstractDescriptionType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Human-readable descriptive information for the object it is included within.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="wcs:AbstractDescriptionBaseType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="wcs:metadataLink"/&gt;
 *                  &lt;element minOccurs="0" ref="wcs:description"/&gt;
 *                  &lt;element ref="wcs:name"/&gt;
 *                  &lt;element name="label" type="string"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Short human-readable label for this object, for human interface display. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute ref="gml:id" use="prohibited"/&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class AbstractDescriptionTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.AbstractDescriptionType;
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