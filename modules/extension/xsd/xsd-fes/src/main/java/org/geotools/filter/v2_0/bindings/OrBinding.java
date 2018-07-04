package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;
import org.geotools.filter.v1_0.OGCOrBinding;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.*;
import org.opengis.filter.FilterFactory;

/**
 * Binding object for the element http://www.opengis.net/fes/2.0:Or.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Or" substitutionGroup="fes:logicOps" type="fes:BinaryLogicOpType"/&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class OrBinding extends OGCOrBinding {

    public OrBinding(FilterFactory filterfactory) {
        super(filterfactory);
    }

    public QName getTarget() {
        return FES.Or;
    }
}
