package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:_DescribeCoverage.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="_DescribeCoverage"&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" name="Coverage" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Name or identifier of this coverage. The same name value shall not be used for any other coverages available from the same server. A client can obtain this name by a prior GetCapabilities request, or possibly from a third-party source. If this element is omitted, the server may return descriptions of every coverage offering available, or return a service exception. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute fixed="WCS" name="service" type="string" use="required"/&gt;
 *      &lt;attribute fixed="1.0.0" name="version" type="string" use="required"/&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class _DescribeCoverageBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS._DescribeCoverage;
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