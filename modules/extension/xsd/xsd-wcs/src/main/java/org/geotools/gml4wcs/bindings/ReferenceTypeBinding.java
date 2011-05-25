package org.geotools.gml4wcs.bindings;


import org.geotools.gml4wcs.GML;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gml:ReferenceType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="ReferenceType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt; A pattern or base for derived types used to specify complex types corresponding to a UML aggregation association.  An instance of this type serves as a pointer to a remote Object.  
 *   &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence/&gt;
 *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/gml4wcs/bindings/ReferenceTypeBinding.java $
 */
public class ReferenceTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return GML.ReferenceType;
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
