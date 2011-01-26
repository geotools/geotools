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

import net.opengis.wfs.WFSCapabilitiesType;
import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/wfs:WFS_CapabilitiesType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="WFS_CapabilitiesType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *              XML encoded WFS GetCapabilities operation response. This
 *              document provides clients with service metadata about a
 *              specific service instance, including metadata about the
 *              tightly-coupled data served. If the server does not implement
 *              the updateSequence parameter, the server shall always return
 *              the complete Capabilities document, without the updateSequence
 *              parameter. When the server implements the updateSequence
 *              parameter and the GetCapabilities operation request included
 *              the updateSequence parameter with the current value, the server
 *              shall return this element with only the "version" and
 *              "updateSequence" attributes. Otherwise, all optional elements
 *              shall be included or not depending on the actual value of the
 *              Contents parameter in the GetCapabilities operation request.
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="ows:CapabilitiesBaseType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element minOccurs="0" ref="wfs:FeatureTypeList"/&gt;
 *                  &lt;xsd:element minOccurs="0" ref="wfs:ServesGMLObjectTypeList"/&gt;
 *                  &lt;xsd:element minOccurs="0" ref="wfs:SupportsGMLObjectTypeList"/&gt;
 *                  &lt;xsd:element ref="ogc:Filter_Capabilities"/&gt;
 *              &lt;/xsd:sequence&gt;
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
public class WFS_CapabilitiesTypeBinding extends AbstractComplexEMFBinding {
    public WFS_CapabilitiesTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.WFS_CapabilitiesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return WFSCapabilitiesType.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}
