package org.geotools.gml4wcs.bindings;


import javax.xml.namespace.QName;

import net.opengis.gml.CodeType;
import net.opengis.gml.Gml4wcsFactory;

import org.geotools.gml4wcs.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/gml:CodeType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="CodeType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Name or code with an (optional) authority.  Text token.  
 *        If the codeSpace attribute is present, then its value should identify a dictionary, thesaurus 
 *        or authority for the term, such as the organisation who assigned the value, 
 *        or the dictionary from which it is taken.  
 *        A text string with an optional codeSpace attribute. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base="string"&gt;
 *              &lt;attribute name="codeSpace" type="anyURI" use="optional"/&gt;
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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/gml4wcs/bindings/CodeTypeBinding.java $
 */

public class CodeTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return GML.CodeType;
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
		CodeType code = Gml4wcsFactory.eINSTANCE.createCodeType();
		
		code.setValue((String)value);
		
		return code;
	}

}
