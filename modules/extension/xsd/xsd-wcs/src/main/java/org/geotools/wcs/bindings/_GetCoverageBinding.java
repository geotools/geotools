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
 * Binding object for the type http://www.opengis.net/wcs:_GetCoverage.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="_GetCoverage"&gt;
 *      &lt;sequence&gt;
 *          &lt;element name="sourceCoverage" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;The coverage offering (identified by its "name") that this request will draw from.&lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element name="domainSubset" type="wcs:DomainSubsetType"/&gt;
 *          &lt;element minOccurs="0" name="rangeSubset" type="wcs:RangeSubsetType"/&gt;
 *          &lt;element minOccurs="0" ref="wcs:interpolationMethod"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Spatial interpolation method to be used in  resampling data from its original form to the requested CRS and/or grid size. Method shall be among those listed for the requested coverage in the DescribeCoverage response.&lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element name="output" type="wcs:OutputType"/&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute fixed="WCS" name="service" type="string" use="required"/&gt;
 *      &lt;attribute fixed="1.0.0" name="version" type="string" use="required"/&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class _GetCoverageBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS._GetCoverage;
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
