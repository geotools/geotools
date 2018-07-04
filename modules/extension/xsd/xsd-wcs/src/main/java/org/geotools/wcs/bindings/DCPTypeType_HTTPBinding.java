package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.wcs.WCS;
import org.geotools.xml.*;

/**
 * Binding object for the type http://www.opengis.net/wcs:DCPTypeType_HTTP.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="DCPTypeType_HTTP"&gt;
 *      &lt;choice maxOccurs="unbounded"&gt;
 *          &lt;element name="Get"&gt;
 *              &lt;complexType&gt;
 *                  &lt;sequence&gt;
 *                      &lt;element name="OnlineResource" type="wcs:OnlineResourceType"/&gt;
 *                  &lt;/sequence&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *          &lt;element name="Post"&gt;
 *              &lt;complexType&gt;
 *                  &lt;sequence&gt;
 *                      &lt;element name="OnlineResource" type="wcs:OnlineResourceType"/&gt;
 *                  &lt;/sequence&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *      &lt;/choice&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 * @source $URL$
 */
public class DCPTypeType_HTTPBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.DCPTypeType_HTTP;
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
