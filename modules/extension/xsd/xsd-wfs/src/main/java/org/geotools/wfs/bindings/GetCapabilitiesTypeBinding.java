/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;


/**
 * Binding object for the type http://www.opengis.net/wfs:GetCapabilitiesType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="GetCapabilitiesType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *            Request to a WFS to perform the GetCapabilities operation.
 *            This operation allows a client to retrieve a Capabilities
 *            XML document providing metadata for the specific WFS server.
 *
 *            The GetCapapbilities element is used to request that a Web Feature
 *            Service generate an XML document describing the organization
 *            providing the service, the WFS operations that the service
 *            supports, a list of feature types that the service can operate
 *            on and list of filtering capabilities that the service support.
 *            Such an XML document is called a capabilities document.
 *         &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="ows:GetCapabilitiesType"&gt;
 *              &lt;xsd:attribute default="WFS" name="service"
 *                  type="ows:ServiceType" use="optional"/&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class GetCapabilitiesTypeBinding extends AbstractComplexEMFBinding {
    public GetCapabilitiesTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.GetCapabilitiesType;
    }
}
