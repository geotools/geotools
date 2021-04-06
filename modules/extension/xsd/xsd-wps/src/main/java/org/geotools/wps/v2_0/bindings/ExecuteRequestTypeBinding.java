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
import net.opengis.wps20.ExecuteRequestType;
import net.opengis.wps20.ModeType;
import net.opengis.wps20.ResponseType;
import net.opengis.wps20.Wps20Factory;
import org.eclipse.emf.ecore.EObject;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:ExecuteRequestType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="ExecuteRequestType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;annotation&gt;
 *
 *  			&lt;documentation&gt;
 *
 *  				Schema for a WPS Execute operation request, to execute
 *
 *  				one identified process with the given data and provide the requested
 *
 *  				output data.
 *
 *  			&lt;/documentation&gt;
 *
 *  		&lt;/annotation&gt;
 *
 *  		&lt;complexContent&gt;
 *
 *  			&lt;extension base="wps:RequestBaseType"&gt;
 *
 *  				&lt;sequence&gt;
 *
 *  					&lt;element ref="ows:Identifier"&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;
 *
 *  								Identifier of the process to be executed. All valid process identifiers are
 *
 *  								listed in the wps:Contents section of the Capabilities document.
 *
 *  							&lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  					&lt;/element&gt;
 *
 *  					&lt;element maxOccurs="unbounded" minOccurs="0" name="Input" type="wps:DataInputType"&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;
 *
 *  								One or more input items to be used for process execution, including referenced or inline data.
 *
 *  							&lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  					&lt;/element&gt;
 *
 *  					&lt;element maxOccurs="unbounded" name="Output" type="wps:OutputDefinitionType"&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;
 *
 *  								Defines one or more output items to be delivered by the process execution.
 *
 *  							&lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  					&lt;/element&gt;
 *
 *  				&lt;/sequence&gt;
 *
 *
 *
 *  				&lt;attribute name="mode" use="required"&gt;
 *
 *  					&lt;annotation&gt;
 *
 *  						&lt;documentation&gt;
 *
 *  							Desired execution mode.
 *
 *  						&lt;/documentation&gt;
 *
 *  					&lt;/annotation&gt;
 *
 *  					&lt;simpleType&gt;
 *
 *  						&lt;restriction base="string"&gt;
 *
 *  							&lt;enumeration value="sync"&gt;
 *
 *  								&lt;annotation&gt;
 *
 *  									&lt;documentation&gt;
 *
 *  										"sync" triggers the synchronous execution protocol
 *
 *  									&lt;/documentation&gt;
 *
 *  								&lt;/annotation&gt;
 *
 *  							&lt;/enumeration&gt;
 *
 *  							&lt;enumeration value="async"&gt;
 *
 *  								&lt;annotation&gt;
 *
 *  									&lt;documentation&gt;
 *
 *  										"async" triggers the asynchronous execution protocol
 *
 *  									&lt;/documentation&gt;
 *
 *  								&lt;/annotation&gt;
 *
 *  							&lt;/enumeration&gt;
 *
 *  							&lt;enumeration value="auto"&gt;
 *
 *  								&lt;annotation&gt;
 *
 *  									&lt;documentation&gt;
 *
 *  										"auto" delegates the choice of execution mode to the server.
 *
 *  									&lt;/documentation&gt;
 *
 *  								&lt;/annotation&gt;
 *
 *  							&lt;/enumeration&gt;
 *
 *  						&lt;/restriction&gt;
 *
 *  					&lt;/simpleType&gt;
 *
 *  				&lt;/attribute&gt;
 *
 *
 *
 *  				&lt;attribute name="response" use="required"&gt;
 *
 *  					&lt;simpleType&gt;
 *
 *  						&lt;restriction base="string"&gt;
 *
 *  							&lt;enumeration value="raw"&gt;
 *
 *  								&lt;annotation&gt;
 *
 *  									&lt;documentation&gt;
 *
 *  										The desired response type is raw data. Raw data output can only be used for single outputs, i.e. the process execution must yield only one output item.
 *
 *  										If the request requests more than one output, the server shall return an Exception
 *
 *  									&lt;/documentation&gt;
 *
 *  								&lt;/annotation&gt;
 *
 *  							&lt;/enumeration&gt;
 *
 *  							&lt;enumeration value="document"&gt;
 *
 *  								&lt;annotation&gt;
 *
 *  									&lt;documentation&gt;
 *
 *  										The desired response type is a response document.
 *
 *  									&lt;/documentation&gt;
 *
 *  								&lt;/annotation&gt;
 *
 *  							&lt;/enumeration&gt;
 *
 *  						&lt;/restriction&gt;
 *
 *  					&lt;/simpleType&gt;
 *
 *  				&lt;/attribute&gt;
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
public class ExecuteRequestTypeBinding extends AbstractComplexEMFBinding {

    public ExecuteRequestTypeBinding(Wps20Factory factory) {
        super(factory);
    }
    /** @generated */
    @Override
    public QName getTarget() {
        return WPS.ExecuteRequestType;
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
        return ExecuteRequestType.class;
    }

    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        Object overridenValue = value;
        if ("response".equals(property)) {
            overridenValue = ResponseType.getByName(String.valueOf(value));
        }
        if ("mode".equals(property)) {
            overridenValue = ModeType.getByName(String.valueOf(value));
        }
        super.setProperty(eObject, property, overridenValue, lax);
    }
}
