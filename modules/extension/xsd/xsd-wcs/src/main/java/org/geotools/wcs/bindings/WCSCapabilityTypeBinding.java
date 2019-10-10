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
 * Binding object for the type http://www.opengis.net/wcs:WCSCapabilityType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="WCSCapabilityType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;XML encoded WCS GetCapabilities operation response. The Capabilities document provides clients with service metadata about a specific service instance, including metadata about the coverages served. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element name="Request"&gt;
 *              &lt;complexType name="WCSCapabilityType_Request"&gt;
 *                  &lt;sequence&gt;
 *                      &lt;element name="GetCapabilities"&gt;
 *                          &lt;complexType&gt;
 *                              &lt;sequence&gt;
 *                                  &lt;element maxOccurs="unbounded"
 *                                      name="DCPType" type="wcs:DCPTypeType"/&gt;
 *                              &lt;/sequence&gt;
 *                          &lt;/complexType&gt;
 *                      &lt;/element&gt;
 *                      &lt;element name="DescribeCoverage"&gt;
 *                          &lt;complexType&gt;
 *                              &lt;sequence&gt;
 *                                  &lt;element maxOccurs="unbounded"
 *                                      name="DCPType" type="wcs:DCPTypeType"/&gt;
 *                              &lt;/sequence&gt;
 *                          &lt;/complexType&gt;
 *                      &lt;/element&gt;
 *                      &lt;element name="GetCoverage"&gt;
 *                          &lt;complexType&gt;
 *                              &lt;sequence&gt;
 *                                  &lt;element maxOccurs="unbounded"
 *                                      name="DCPType" type="wcs:DCPTypeType"/&gt;
 *                              &lt;/sequence&gt;
 *                          &lt;/complexType&gt;
 *                      &lt;/element&gt;
 *                  &lt;/sequence&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *          &lt;element name="Exception"&gt;
 *              &lt;complexType name="WCSCapabilityType_Exception"&gt;
 *                  &lt;sequence&gt;
 *                      &lt;element maxOccurs="unbounded" name="Format" type="string"/&gt;
 *                  &lt;/sequence&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="VendorSpecificCapabilities"&gt;
 *              &lt;complexType name="WCSCapabilityType_VendorSpecificCapabilities"&gt;
 *                  &lt;sequence&gt;
 *                      &lt;any/&gt;
 *                  &lt;/sequence&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute fixed="1.0.0" name="version" type="string" use="optional"/&gt;
 *      &lt;attribute name="updateSequence" type="string" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Service metadata document version, having values that are "increased" whenever any change is made in service metadata document. Values are selected by each server, and are always opaque to clients. When not supported by server, server shall not return this attribute. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class WCSCapabilityTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.WCSCapabilityType;
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
