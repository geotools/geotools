/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.wcs.WCS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs:DCPTypeType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="DCPTypeType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Connect point URLs for the HTTP Distributed Computing Platform (DCP). Normally, only one Get and/or one Post is included in this element. More than one Get and/or Post is allowed to support including alternative URLs for uses such as load balancing or backup. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element name="HTTP"&gt;
 *              &lt;complexType name="DCPTypeType_HTTP"&gt;
 *                  &lt;choice maxOccurs="unbounded"&gt;
 *                      &lt;element name="Get"&gt;
 *                          &lt;complexType&gt;
 *                              &lt;sequence&gt;
 *                                  &lt;element name="OnlineResource" type="wcs:OnlineResourceType"/&gt;
 *                              &lt;/sequence&gt;
 *                          &lt;/complexType&gt;
 *                      &lt;/element&gt;
 *                      &lt;element name="Post"&gt;
 *                          &lt;complexType&gt;
 *                              &lt;sequence&gt;
 *                                  &lt;element name="OnlineResource" type="wcs:OnlineResourceType"/&gt;
 *                              &lt;/sequence&gt;
 *                          &lt;/complexType&gt;
 *                      &lt;/element&gt;
 *                  &lt;/choice&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class DCPTypeTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.DCPTypeType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}
