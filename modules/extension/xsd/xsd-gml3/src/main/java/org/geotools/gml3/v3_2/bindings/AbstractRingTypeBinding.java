package org.geotools.gml3.v3_2.bindings;

import javax.xml.namespace.QName;
import org.geotools.gml3.v3_2.GML;
import org.geotools.xml.*;
import org.locationtech.jts.geom.LineString;

/**
 * Binding object for the type http://www.opengis.net/gml/3.2:AbstractRingType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType abstract="true" name="AbstractRingType"&gt;
 *      &lt;sequence/&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class AbstractRingTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return GML.AbstractRingType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return LineString.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return value;
    }
}
