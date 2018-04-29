package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.wcs.WCS;
import org.geotools.xml.*;

/**
 * Binding object for the type
 * http://www.opengis.net/wcs:WCSCapabilityType_VendorSpecificCapabilities.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="WCSCapabilityType_VendorSpecificCapabilities"&gt;
 *      &lt;sequence&gt;
 *          &lt;any/&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 * @source $URL$
 */
public class WCSCapabilityType_VendorSpecificCapabilitiesBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.WCSCapabilityType_VendorSpecificCapabilities;
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
