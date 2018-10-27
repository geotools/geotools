package org.geotools.wmts.bindings;

import javax.xml.namespace.QName;
import net.opengis.ows11.AcceptFormatsType;
import net.opengis.wmts.v_1.GetCapabilitiesType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:GetCapabilities.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="GetCapabilities" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;annotation&gt;
 *  			&lt;documentation&gt;WMTS GetCapabilities operation request.&lt;/documentation&gt;
 *  		&lt;/annotation&gt;
 *  		&lt;complexType&gt;
 *  			&lt;complexContent&gt;
 *  				&lt;extension base="ows:GetCapabilitiesType"&gt;
 *  					&lt;attribute fixed="WMTS" name="service" type="ows:ServiceType" use="required"/&gt;
 *  				&lt;/extension&gt;
 *  			&lt;/complexContent&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class GetCapabilitiesBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public GetCapabilitiesBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.GetCapabilities;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return GetCapabilitiesType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        GetCapabilitiesType capabilities = factory.createGetCapabilitiesType();
        capabilities.setAcceptFormats((AcceptFormatsType) node.getChildValue("AcceptFormats"));
        return capabilities;
    }
}
