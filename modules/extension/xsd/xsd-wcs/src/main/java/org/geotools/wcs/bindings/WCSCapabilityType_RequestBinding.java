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
 * Binding object for the type http://www.opengis.net/wcs:WCSCapabilityType_Request.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="WCSCapabilityType_Request"&gt;
 *      &lt;sequence&gt;
 *          &lt;element name="GetCapabilities"&gt;
 *              &lt;complexType&gt;
 *                  &lt;sequence&gt;
 *                      &lt;element maxOccurs="unbounded" name="DCPType" type="wcs:DCPTypeType"/&gt;
 *                  &lt;/sequence&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *          &lt;element name="DescribeCoverage"&gt;
 *              &lt;complexType&gt;
 *                  &lt;sequence&gt;
 *                      &lt;element maxOccurs="unbounded" name="DCPType" type="wcs:DCPTypeType"/&gt;
 *                  &lt;/sequence&gt;
 *              &lt;/complexType&gt;
 *          &lt;/element&gt;
 *          &lt;element name="GetCoverage"&gt;
 *              &lt;complexType&gt;
 *                  &lt;sequence&gt;
 *                      &lt;element maxOccurs="unbounded" name="DCPType" type="wcs:DCPTypeType"/&gt;
 *                  &lt;/sequence&gt;
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
public class WCSCapabilityType_RequestBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.WCSCapabilityType_Request;
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
