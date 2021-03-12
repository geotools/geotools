package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:_Dismiss.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="_Dismiss" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  			&lt;complexContent&gt;
 *
 *  				&lt;extension base="wps:RequestBaseType"&gt;
 *
 *  					&lt;sequence&gt;
 *
 *  						&lt;element ref="wps:JobID"/&gt;
 *
 *  					&lt;/sequence&gt;
 *
 *  				&lt;/extension&gt;
 *
 *  			&lt;/complexContent&gt;
 *
 *  		&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class _DismissBinding extends AbstractComplexEMFBinding {

    public _DismissBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return WPS._Dismiss;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return super.getType();
    }
}
