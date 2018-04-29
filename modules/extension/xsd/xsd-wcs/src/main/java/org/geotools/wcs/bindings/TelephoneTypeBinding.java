package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.wcs.WCS;
import org.geotools.xml.*;

/**
 * Binding object for the type http://www.opengis.net/wcs:TelephoneType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="TelephoneType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Telephone numbers for contacting the responsible individual or organization. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" name="voice" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Telephone number by which individuals can speak to the responsible organization or individual. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" name="facsimile" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Telephone number of a facsimile machine for the responsibleorganization or individual. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 * @source $URL$
 */
public class TelephoneTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.TelephoneType;
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
