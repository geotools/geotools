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
 * Binding object for the type http://www.opengis.net/wcs:SupportedCRSsType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="SupportedCRSsType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Unordered list(s) of identifiers of Coordinate Reference Systems (CRSs) supported in server operation requests and responses. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;choice&gt;
 *              &lt;element maxOccurs="unbounded" name="requestResponseCRSs" type="gml:CodeListType"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Unordered list of identifiers of the CRSs in which the server can both accept requests and deliver responses for this data. These CRSs should include the native CRSs defined below. &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/element&gt;
 *              &lt;sequence&gt;
 *                  &lt;element maxOccurs="unbounded" name="requestCRSs" type="gml:CodeListType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of identifiers of the CRSs in which the server can accept requests for this data. These CRSs should include the native CRSs defined below. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" name="responseCRSs" type="gml:CodeListType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of identifiers of the CRSs in which the server can deliver responses for this data. These CRSs should include the native CRSs defined below. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *          &lt;/choice&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" name="nativeCRSs" type="gml:CodeListType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Unordered list of identifiers of the CRSs in which the server stores this data, that is, the CRS(s) in which data can be obtained without any distortion or degradation. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class SupportedCRSsTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.SupportedCRSsType;
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
