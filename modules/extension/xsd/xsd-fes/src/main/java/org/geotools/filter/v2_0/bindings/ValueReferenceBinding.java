package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;
import org.geotools.filter.v2_0.FES;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Binding object for the element http://www.opengis.net/fes/2.0:ValueReference.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="ValueReference" substitutionGroup="fes:expression" type="xsd:string"/&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class ValueReferenceBinding extends AbstractSimpleBinding {

    FilterFactory filterFactory;

    NamespaceSupport namespaceSupport;

    public ValueReferenceBinding(FilterFactory filterFactory, NamespaceSupport namespaceSupport) {
        this.filterFactory = filterFactory;
        this.namespaceSupport = namespaceSupport;
    }

    /** @generated */
    public QName getTarget() {
        return FES.ValueReference;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return PropertyName.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        return ((FilterFactory2) filterFactory)
                .property(
                        (String) value, GML3EncodingUtils.copyNamespaceSupport((namespaceSupport)));
    }
}
