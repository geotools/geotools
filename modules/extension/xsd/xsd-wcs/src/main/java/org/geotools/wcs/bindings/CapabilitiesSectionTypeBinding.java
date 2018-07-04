package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;
import net.opengis.wcs10.CapabilitiesSectionType;
import org.geotools.wcs.WCS;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/wcs:CapabilitiesSectionType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;simpleType name=&quot;CapabilitiesSectionType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Identification of desired part of full Capabilities XML document to be returned. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base=&quot;string&quot;&gt;
 *          &lt;enumeration value=&quot;/&quot;&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;TBD. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/enumeration&gt;
 *          &lt;enumeration value=&quot;/WCS_Capabilities/Service&quot;&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;TBD. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/enumeration&gt;
 *          &lt;enumeration value=&quot;/WCS_Capabilities/Capability&quot;&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;TBD. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/enumeration&gt;
 *          &lt;enumeration value=&quot;/WCS_Capabilities/ContentMetadata&quot;&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;TBD. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/enumeration&gt;
 *      &lt;/restriction&gt;
 *  &lt;/simpleType&gt;
 *
 * </code>
 *  </pre>
 *
 * @generated
 * @source $URL$
 */
public class CapabilitiesSectionTypeBinding extends AbstractSimpleBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.CapabilitiesSectionType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return CapabilitiesSectionType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        return CapabilitiesSectionType.get((String) value);
    }
}
