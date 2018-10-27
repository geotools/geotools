package org.geotools.wmts.bindings;

import javax.xml.namespace.QName;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/wmts/1.0:RequestServiceType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;simpleType name="RequestServiceType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;restriction base="string"&gt;
 *  			&lt;enumeration value="WMTS"/&gt;
 *  		&lt;/restriction&gt;
 *  	&lt;/simpleType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class RequestServiceTypeBinding extends AbstractSimpleBinding {

    wmtsv_1Factory factory;

    public RequestServiceTypeBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.RequestServiceType;
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
    public Object parse(InstanceComponent instance, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, value);
    }
}
