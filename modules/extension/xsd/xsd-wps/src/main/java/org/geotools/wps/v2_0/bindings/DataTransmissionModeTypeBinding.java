package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.DataTransmissionModeType;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:DataTransmissionModeType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;simpleType name="DataTransmissionModeType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;annotation&gt;
 *
 *  			&lt;documentation&gt;
 *
 *  				This attribute type is used to specify data transmission modes for process outputs.
 *
 *  			&lt;/documentation&gt;
 *
 *  		&lt;/annotation&gt;
 *
 *  		&lt;restriction base="string"&gt;
 *
 *  			&lt;enumeration value="value"/&gt;
 *
 *  			&lt;enumeration value="reference"/&gt;
 *
 *  		&lt;/restriction&gt;
 *
 *  	&lt;/simpleType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class DataTransmissionModeTypeBinding extends AbstractSimpleBinding {

    /** @generated */
    public QName getTarget() {
        return WPS.DataTransmissionModeType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return DataTransmissionModeType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        DataTransmissionModeType dataTransmissionMode =
                DataTransmissionModeType.getByName((String) value);
        return super.parse(instance, dataTransmissionMode);
    }
}
