package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.BodyReferenceType;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:ReferenceType_BodyReference.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="ReferenceType_BodyReference" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  					&lt;attribute ref="xlink:href" use="required"&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;
 *
 *  								HTTP URI that points to the remote resource where the request body may be retrieved.
 *
 *  							&lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  					&lt;/attribute&gt;
 *
 *  				&lt;/complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class ReferenceType_BodyReferenceBinding extends AbstractComplexEMFBinding {

    public ReferenceType_BodyReferenceBinding(Wps20Factory factory) {
        super(factory);
    }
    /** @generated */
    public QName getTarget() {
        return WPS.ReferenceType_BodyReference;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return BodyReferenceType.class;
    }
}
