package org.geotools.gml4wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.gml4wcs.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/gml:CodeListType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="CodeListType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;List of values on a uniform nominal scale.  List of text tokens.
 *        In a list context a token should not include any spaces, so xsd:Name is used instead of xsd:string.
 *        If a codeSpace attribute is present, then its value is a reference to
 *        a Reference System for the value, a dictionary or code list.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base="gml:NameList"&gt;
 *              &lt;attribute name="codeSpace" type="anyURI" use="optional"/&gt;
 *          &lt;/extension&gt;
 *      &lt;/simpleContent&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class CodeListTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return GML.CodeListType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}
