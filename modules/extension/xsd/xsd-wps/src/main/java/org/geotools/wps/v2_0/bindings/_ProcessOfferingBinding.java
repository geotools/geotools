package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:_ProcessOffering.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="_ProcessOffering" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  			&lt;sequence&gt;
 *
 *  				&lt;choice&gt;
 *
 *  					&lt;element ref="wps:Process"/&gt;
 *
 *  					&lt;any namespace="##other" processContents="lax"/&gt;
 *
 *  				&lt;/choice&gt;
 *
 *
 *
 *  			&lt;/sequence&gt;
 *
 *  			&lt;attributeGroup ref="wps:processPropertiesAttributes"/&gt;
 *
 *
 *
 *  		&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class _ProcessOfferingBinding extends AbstractComplexEMFBinding {

    public _ProcessOfferingBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return WPS._ProcessOffering;
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
