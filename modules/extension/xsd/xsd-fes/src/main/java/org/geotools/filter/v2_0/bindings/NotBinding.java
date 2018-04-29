package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;
import org.geotools.filter.v1_0.OGCNotBinding;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.*;
import org.opengis.filter.FilterFactory;

/**
 * Binding object for the element http://www.opengis.net/fes/2.0:Not.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Not" substitutionGroup="fes:logicOps" type="fes:UnaryLogicOpType"/&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class NotBinding extends OGCNotBinding {

    public NotBinding(FilterFactory filterfactory) {
        super(filterfactory);
    }

    public QName getTarget() {
        return FES.Not;
    }
}
