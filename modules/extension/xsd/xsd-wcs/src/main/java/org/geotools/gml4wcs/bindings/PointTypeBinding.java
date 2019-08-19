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

package org.geotools.gml4wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.gml4wcs.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/gml:PointType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="PointType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A Point is defined by a single coordinate tuple.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGeometryType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;This profile uses only the "pos" element - no coordinates or coord.&lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *                  &lt;element ref="gml:pos"/&gt;
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
public class PointTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return GML.PointType;
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
