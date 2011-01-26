package org.geotools.gml4wcs.bindings;


import org.geotools.gml4wcs.GML;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gml:VectorType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="VectorType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A Vector is an ordered set of numbers called coordinates that represent a position in a coordinate reference system (CRS). For some application the components of the position may be adjusted to yield a unit vector.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;restriction base="gml:DirectPositionType"/&gt;
 *      &lt;/simpleContent&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class VectorTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return GML.VectorType;
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