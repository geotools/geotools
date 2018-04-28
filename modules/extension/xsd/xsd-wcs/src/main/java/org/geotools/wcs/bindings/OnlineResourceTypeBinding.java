package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.wcs.WCS;
import org.geotools.xml.*;

/**
 * Binding object for the type http://www.opengis.net/wcs:OnlineResourceType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="OnlineResourceType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Reference to on-line resource from which data can be obtained. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;attributeGroup ref="xlink:simpleLink"/&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 * @source $URL$
 */
public class OnlineResourceTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.OnlineResourceType;
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
