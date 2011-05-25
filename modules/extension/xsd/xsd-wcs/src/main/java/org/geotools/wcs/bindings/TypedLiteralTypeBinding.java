package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:TypedLiteralType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="TypedLiteralType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A single value for a variable, encoded as a string. This type can be used for one value, for a spacing between allowed values, or for the default value of a parameter. The "type" attribute indicates the datatype of this value (default is a string). The value for a typed literal is found by applying the datatype mapping associated with the datatype URI to the lexical form string. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base="string"&gt;
 *              &lt;attribute ref="wcs:type" use="optional"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Should be included unless the datatype is string, or this "type" attribute is included in an enclosing element. &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *          &lt;/extension&gt;
 *      &lt;/simpleContent&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/wcs/bindings/TypedLiteralTypeBinding.java $
 */
public class TypedLiteralTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.TypedLiteralType;
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
