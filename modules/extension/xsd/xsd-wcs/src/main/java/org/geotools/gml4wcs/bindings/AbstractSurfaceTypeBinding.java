package org.geotools.gml4wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.gml4wcs.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/gml:AbstractSurfaceType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="AbstractSurfaceType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;
 *  				An abstraction of a surface to support the different levels of complexity. A surface is always a continuous region of a plane.
 *  			&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGeometricPrimitiveType"/&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class AbstractSurfaceTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return GML.AbstractSurfaceType;
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
