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
 * Binding object for the type http://www.opengis.net/wcs:AxisDescriptionType_values.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="AxisDescriptionType_values"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="wcs:valueEnumType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element minOccurs="0" name="default" type="wcs:TypedLiteralType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Ordered sequence of the parameter value(s) that the server will use for GetCoverage requests which omit a constraint on this parameter axis. (GetCoverage requests against a coverage offering whose AxisDescription has no default must specify a valid constraint for this parameter.) &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class AxisDescriptionType_valuesBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.AxisDescriptionType_values;
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
