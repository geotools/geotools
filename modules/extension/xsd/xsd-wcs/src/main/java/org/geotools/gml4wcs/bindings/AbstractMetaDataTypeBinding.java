package org.geotools.gml4wcs.bindings;


import org.geotools.gml4wcs.GML;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gml:AbstractMetaDataType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType abstract="true" mixed="true" name="AbstractMetaDataType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;An abstract base type for complex metadata types.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;attribute ref="gml:id" use="optional"/&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class AbstractMetaDataTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return GML.AbstractMetaDataType;
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