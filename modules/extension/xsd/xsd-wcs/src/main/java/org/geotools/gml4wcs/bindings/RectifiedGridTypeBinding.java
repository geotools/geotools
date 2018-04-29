package org.geotools.gml4wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.gml4wcs.GML;
import org.geotools.xml.*;

/**
 * Binding object for the type http://www.opengis.net/gml:RectifiedGridType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="RectifiedGridType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A rectified grid has an origin and vectors that define its post locations.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:GridType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element name="origin" type="gml:PointType"/&gt;
 *                  &lt;element maxOccurs="unbounded" name="offsetVector" type="gml:VectorType"/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 * @source $URL$
 */
public class RectifiedGridTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return GML.RectifiedGridType;
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
