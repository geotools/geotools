package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xml.*;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:WPSCapabilitiesType_Extension.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="WPSCapabilitiesType_Extension" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  							&lt;sequence&gt;
 *
 *  								&lt;any maxOccurs="unbounded" minOccurs="0" namespace="##other" processContents="lax"/&gt;
 *
 *  							&lt;/sequence&gt;
 *
 *  						&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class WPSCapabilitiesType_ExtensionBinding extends AbstractComplexEMFBinding {

    /** @generated */
    public QName getTarget() {
        return WPS.WPSCapabilitiesType_Extension;
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
}
