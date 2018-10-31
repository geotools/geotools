package org.geotools.wmts.bindings;

import javax.xml.namespace.QName;
import net.opengis.wmts.v_1.URLTemplateType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/wmts/1.0:URLTemplateType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="URLTemplateType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;attribute name="format" type="ows:MimeType" use="required"&gt;
 *  			&lt;annotation&gt;
 *  				&lt;documentation&gt;Format of the resource representation that can
 *  				be retrieved one resolved the URL template.&lt;/documentation&gt;
 *  			&lt;/annotation&gt;
 *  		&lt;/attribute&gt;
 *  		&lt;attribute name="resourceType" use="required"&gt;
 *  			&lt;annotation&gt;
 *  				&lt;documentation&gt;Resource type to be retrieved. It can only
 *  				be "tile" or "FeatureInfo"&lt;/documentation&gt;
 *  			&lt;/annotation&gt;
 *  			&lt;simpleType&gt;
 *  				&lt;restriction base="string"&gt;
 *  					&lt;enumeration value="tile"/&gt;
 *  					&lt;enumeration value="FeatureInfo"/&gt;
 *  				&lt;/restriction&gt;
 *  			&lt;/simpleType&gt;
 *  		&lt;/attribute&gt;
 *  		&lt;attribute name="template" use="required"&gt;
 *  			&lt;annotation&gt;
 *  				&lt;documentation&gt;URL template. A template processor will be
 *  				applied to substitute some variables between {} for their values
 *  				and get a URL to a resource.
 *  				We cound not use a anyURI type (that conforms the character
 *  				restrictions specified in RFC2396 and excludes '{' '}' characters
 *  				in some XML parsers) because this attribute must accept the
 *  				'{' '}' caracters.&lt;/documentation&gt;
 *  			&lt;/annotation&gt;
 *  			&lt;simpleType&gt;
 *  				&lt;restriction base="string"&gt;
 *  					&lt;pattern value="([A-Za-z0-9\-_\.!~\*'\(\);/\?:@\+:$,#\{\}=&amp;]|%[A-Fa-f0-9][A-Fa-f0-9])+"/&gt;
 *  				&lt;/restriction&gt;
 *  			&lt;/simpleType&gt;
 *  		&lt;/attribute&gt;
 *  	&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class URLTemplateTypeBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public URLTemplateTypeBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.URLTemplateType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return URLTemplateType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        URLTemplateType template = factory.createURLTemplateType();
        template.setFormat((String) node.getAttributeValue("format"));
        // TODO: find resourceType binding?
        // template.setResourceType((String) node.getAttributeValue("resourceType"));
        template.setTemplate((String) node.getAttributeValue("template"));
        return template;
    }
}
