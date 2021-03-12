/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
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
    @Override
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
    @Override
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
    @Override
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        DataTransmissionModeType dataTransmissionMode =
                DataTransmissionModeType.getByName((String) value);
        return super.parse(instance, dataTransmissionMode);
    }
}
