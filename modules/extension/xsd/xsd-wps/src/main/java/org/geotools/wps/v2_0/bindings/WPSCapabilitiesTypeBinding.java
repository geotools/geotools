package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.WPSCapabilitiesType;
import net.opengis.wps20.Wps20Factory;
import org.eclipse.emf.ecore.EObject;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:WPSCapabilitiesType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="WPSCapabilitiesType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;complexContent&gt;
 *
 *  			&lt;extension base="ows:CapabilitiesBaseType"&gt;
 *
 *  				&lt;sequence&gt;
 *
 *  					&lt;element ref="wps:Contents"/&gt;
 *
 *  					&lt;element minOccurs="0" name="Extension"&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;container for elements defined by extension specifications&lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  						&lt;complexType name="WPSCapabilitiesType_Extension"&gt;
 *
 *  							&lt;sequence&gt;
 *
 *  								&lt;any maxOccurs="unbounded" minOccurs="0" namespace="##other" processContents="lax"/&gt;
 *
 *  							&lt;/sequence&gt;
 *
 *  						&lt;/complexType&gt;
 *
 *  					&lt;/element&gt;
 *
 *  				&lt;/sequence&gt;
 *
 *  				&lt;attribute fixed="WPS" name="service" use="required"/&gt;
 *
 *  			&lt;/extension&gt;
 *
 *  		&lt;/complexContent&gt;
 *
 *  	&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class WPSCapabilitiesTypeBinding extends AbstractComplexEMFBinding {

    public WPSCapabilitiesTypeBinding(Wps20Factory factory) {
        super(factory);
    }
    /** @generated */
    public QName getTarget() {
        return WPS.WPSCapabilitiesType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return WPSCapabilitiesType.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        Object overridedValue = value;

        super.setProperty(eObject, property, overridedValue, lax);
    }
}
