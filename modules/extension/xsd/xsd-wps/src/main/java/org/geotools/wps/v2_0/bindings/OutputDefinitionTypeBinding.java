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
import net.opengis.wps20.OutputDefinitionType;
import net.opengis.wps20.Wps20Factory;
import org.eclipse.emf.ecore.EObject;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:OutputDefinitionType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="OutputDefinitionType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;annotation&gt;
 *
 *  			&lt;documentation&gt;
 *
 *  				This structure contains information elements that describe the format and transmission mode
 *
 *  				of the output data that is delivered by a process execution
 *
 *  			&lt;/documentation&gt;
 *
 *  		&lt;/annotation&gt;
 *
 *  		&lt;sequence&gt;
 *
 *  			&lt;element minOccurs="0" name="Output" type="wps:OutputDefinitionType"/&gt;
 *
 *  		&lt;/sequence&gt;
 *
 *  		&lt;attribute name="id" type="anyURI" use="required"&gt;
 *
 *  			&lt;annotation&gt;
 *
 *  				&lt;documentation&gt;
 *
 *  					Identifier of this output.
 *
 *  				&lt;/documentation&gt;
 *
 *  			&lt;/annotation&gt;
 *
 *  		&lt;/attribute&gt;
 *
 *  		&lt;attributeGroup ref="wps:dataEncodingAttributes"/&gt;
 *
 *  		&lt;attribute name="transmission" type="wps:DataTransmissionModeType"&gt;
 *
 *  			&lt;annotation&gt;
 *
 *  				&lt;documentation&gt;
 *
 *  					The desired transmission mode for this output
 *
 *  				&lt;/documentation&gt;
 *
 *  			&lt;/annotation&gt;
 *
 *  		&lt;/attribute&gt;
 *
 *  	&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class OutputDefinitionTypeBinding extends AbstractComplexEMFBinding {

    public OutputDefinitionTypeBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return WPS.OutputDefinitionType;
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
        return OutputDefinitionType.class;
    }

    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        Object overridedValue = value;
        if ("transmission".equals(property)) {
            overridedValue = DataTransmissionModeType.getByName(String.valueOf(value));
        }
        super.setProperty(eObject, property, overridedValue, lax);
    }
}
