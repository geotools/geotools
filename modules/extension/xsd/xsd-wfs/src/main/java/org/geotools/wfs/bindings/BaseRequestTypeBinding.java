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

import net.opengis.wfs.BaseRequestType;
import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;


/**
 * Binding object for the type http://www.opengis.net/wfs:BaseRequestType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType abstract=&quot;true&quot; name=&quot;BaseRequestType&quot;&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *              XML encoded WFS operation request base, for all operations
 *              except GetCapabilities.
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:attribute default=&quot;WFS&quot; name=&quot;service&quot; type=&quot;ows:ServiceType&quot; use=&quot;optional&quot;&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                The service attribute is included to support service
 *                endpoints that implement more than one OGC service.
 *                For example, a single CGI that implements WMS, WFS
 *                and WCS services.
 *                The endpoint can inspect the value of this attribute
 *                to figure out which service should process the request.
 *                The value WFS indicates that a web feature service should
 *                process the request.
 *                This parameter is somewhat redundant in the XML encoding
 *                since the request namespace can be used to determine
 *                which service should process any give request.  For example,
 *                wfs:GetCapabilities and easily be distinguished from
 *                wcs:GetCapabilities using the namespaces.
 *             &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *      &lt;xsd:attribute default=&quot;1.1.0&quot; name=&quot;version&quot; type=&quot;xsd:string&quot; use=&quot;optional&quot;&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                 The version attribute is used to indicate the version of the
 *                 WFS specification that a request conforms to.  All requests in
 *                 this schema conform to V1.1 of the WFS specification.
 *                 For WFS implementations that support more than one version of
 *                 a WFS sepcification ... if the version attribute is not
 *                 specified then the service should assume that the request
 *                 conforms to greatest available specification version.
 *             &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *      &lt;xsd:attribute name=&quot;handle&quot; type=&quot;xsd:string&quot; use=&quot;optional&quot;&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                 The handle attribute allows a client application
 *                 to assign a client-generated request identifier
 *                 to a WFS request.  The handle is included to
 *                 facilitate error reporting.  A WFS may report the
 *                 handle in an exception report to identify the
 *                 offending request or action.  If the handle is not
 *                 present, then the WFS may employ other means to
 *                 localize the error (e.g. line numbers).
 *              &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *  &lt;/xsd:complexType&gt;
 * </code>
 *         </pre>
 *
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class BaseRequestTypeBinding extends AbstractComplexEMFBinding {
    public BaseRequestTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.BaseRequestType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return BaseRequestType.class;
    }
}
