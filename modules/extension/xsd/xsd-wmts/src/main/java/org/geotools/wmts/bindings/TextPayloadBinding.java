package org.geotools.wmts.bindings;

import javax.xml.namespace.QName;
import net.opengis.wmts.v_1.TextPayloadType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:TextPayload.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="TextPayload" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;complexType&gt;
 *  			&lt;sequence&gt;
 *  				&lt;element name="Format" type="ows:MimeType"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;MIMEType format of the TextContent&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="TextContent" type="string"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;
 *  							Text string like HTML, XHTML, XML or TXT. HTML and TXT data has
 *  							to be enclosed in a CDATA element to avoid XML parsing.
 *  						&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  			&lt;/sequence&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class TextPayloadBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public TextPayloadBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.TextPayload;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return TextPayloadType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        TextPayloadType payload = factory.createTextPayloadType();
        payload.setFormat((String) node.getChildValue("Format"));
        payload.setTextContent((String) node.getChildValue("TextContent"));
        return payload;
    }
}
