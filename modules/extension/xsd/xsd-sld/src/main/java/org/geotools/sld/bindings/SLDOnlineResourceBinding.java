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
package org.geotools.sld.bindings;

import java.net.URI;
import java.net.URL;
import javax.xml.namespace.QName;
import org.geotools.styling.ResourceLocator;
import org.geotools.xml.*;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the element http://www.opengis.net/sld:OnlineResource.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:element name="OnlineResource"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         An OnlineResource is typically used
 *              to refer to an HTTP URL.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:attributeGroup ref="xlink:simpleLink"/&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 * @source $URL$
 */
public class SLDOnlineResourceBinding extends AbstractComplexBinding {

    ResourceLocator resourceLocator;

    public SLDOnlineResourceBinding(ResourceLocator resourceLocator) {
        this.resourceLocator = resourceLocator;
    }

    /** @generated */
    public QName getTarget() {
        return SLD.ONLINERESOURCE;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return URI.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // just grab the URI and pass it back
        Object href = node.getAttributeValue("href");
        if (href != null) {
            URL located = resourceLocator.locateResource(href.toString());
            if (located != null) {
                // return as a uri
                return located.toURI();
            }
        }
        return href;
    }
}
