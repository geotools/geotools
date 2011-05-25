package org.geotools.gml4wcs.bindings;


import org.geotools.gml4wcs.GML;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gml:AbstractGMLType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType abstract="true" name="AbstractGMLType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt; The optional attribute "id" is omitted from this profile. &lt;/documentation&gt;
 *          &lt;documentation&gt;All complexContent GML elements are directly or indirectly derived from this abstract supertype to establish a hierarchy of GML types that may be distinguished from other XML types by their ancestry. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:metaDataProperty"/&gt;
 *          &lt;element minOccurs="0" ref="gml:description"/&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:name"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Multiple names may be provided.  These will often be distinguished by being assigned by different authorities, as indicated by the value of the codeSpace attribute.  In an instance document there will usually only be one name per authority. &lt;/documentation&gt;
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
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/gml4wcs/bindings/AbstractGMLTypeBinding.java $
 */
public class AbstractGMLTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return GML.AbstractGMLType;
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
