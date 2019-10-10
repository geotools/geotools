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
 * Binding object for the type http://www.opengis.net/wcs:CoverageOfferingType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="CoverageOfferingType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Full description of one coverage available from a WCS instance. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="wcs:CoverageOfferingBriefType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="wcs:domainSet"/&gt;
 *                  &lt;element ref="wcs:rangeSet"/&gt;
 *                  &lt;element ref="wcs:supportedCRSs"/&gt;
 *                  &lt;element ref="wcs:supportedFormats"/&gt;
 *                  &lt;element minOccurs="0" ref="wcs:supportedInterpolations"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Specifies whether and how the server can interpolate coverage values over the spatial domain, when a GetCoverage request requires resampling, reprojection, or other generalization. If supportedInterpolations is absent or empty with no default, then clients should assume nearest-neighbor interpolation. If the only interpolation method listed is none, clients can only retrieve coverages from this layer in its native CRS and at its native resolution. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class CoverageOfferingTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.CoverageOfferingType;
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
