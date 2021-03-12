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
import net.opengis.wps20.RequestBaseType;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:RequestBaseType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType abstract="true" name="RequestBaseType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;annotation&gt;
 *
 *  			&lt;documentation&gt;
 *
 *  				WPS operation request base, for all WPS operations, except GetCapabilities.
 *
 *  				In this XML encoding, no "request" parameter is included, since the element
 *
 *  				name specifies the specific operation.
 *
 *  				An 'Extension' element provides a placeholder for extra request parameters
 *
 *  				that might be defined by WPS extension standards.
 *
 *  			&lt;/documentation&gt;
 *
 *  		&lt;/annotation&gt;
 *
 *  		&lt;sequence&gt;
 *
 *  			&lt;element maxOccurs="unbounded" minOccurs="0" name="Extension" type="anyType"&gt;
 *
 *  				&lt;annotation&gt;
 *
 *  					&lt;documentation&gt;
 *
 *  						Any ancillary information to be sent from client to server.
 *
 *  						Placeholder for further request parameters defined by WPS extension standards.
 *
 *  					&lt;/documentation&gt;
 *
 *  				&lt;/annotation&gt;
 *
 *  			&lt;/element&gt;
 *
 *  		&lt;/sequence&gt;
 *
 *  		&lt;attribute fixed="WPS" name="service" type="string" use="required"&gt;
 *
 *  			&lt;annotation&gt;
 *
 *  				&lt;documentation&gt;
 *
 *  					Service type identifier (WPS)
 *
 *  				&lt;/documentation&gt;
 *
 *  			&lt;/annotation&gt;
 *
 *  		&lt;/attribute&gt;
 *
 *  		&lt;attribute fixed="2.0.0" name="version" type="ows:VersionType" use="required"&gt;
 *
 *  			&lt;annotation&gt;
 *
 *  				&lt;documentation&gt;
 *
 *  					Version of the WPS interface specification implemented by the server (2.0.0)
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
public class RequestBaseTypeBinding extends AbstractComplexEMFBinding {

    public RequestBaseTypeBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return WPS.RequestBaseType;
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
        return RequestBaseType.class;
    }
}
