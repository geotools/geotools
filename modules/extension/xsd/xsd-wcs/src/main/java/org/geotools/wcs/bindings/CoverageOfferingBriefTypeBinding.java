package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:CoverageOfferingBriefType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="CoverageOfferingBriefType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Brief description of one coverage avaialble from a WCS. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="wcs:AbstractDescriptionType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="wcs:lonLatEnvelope"/&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="wcs:keywords"/&gt;
 *              &lt;/sequence&gt;
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
public class CoverageOfferingBriefTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.CoverageOfferingBriefType;
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