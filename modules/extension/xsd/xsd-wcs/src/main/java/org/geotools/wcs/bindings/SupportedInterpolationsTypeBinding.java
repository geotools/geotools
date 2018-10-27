package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.wcs.WCS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs:SupportedInterpolationsType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="SupportedInterpolationsType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Unordered list of interpolation methods supported. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" ref="wcs:interpolationMethod"/&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute default="nearest neighbor" name="default"
 *          type="wcs:InterpolationMethodType" use="optional"/&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class SupportedInterpolationsTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.SupportedInterpolationsType;
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
