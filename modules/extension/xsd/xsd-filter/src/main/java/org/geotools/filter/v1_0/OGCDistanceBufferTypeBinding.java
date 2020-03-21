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
package org.geotools.filter.v1_0;

import javax.xml.namespace.QName;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the type http://www.opengis.net/ogc:DistanceBufferType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="DistanceBufferType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="ogc:SpatialOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="ogc:PropertyName"/&gt;
 *                  &lt;xsd:element ref="gml:_Geometry"/&gt;
 *                  &lt;xsd:element name="Distance" type="ogc:DistanceType"/&gt;
 *              &lt;/xsd:sequence&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class OGCDistanceBufferTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return OGC.DistanceBufferType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return DistanceBufferOperator.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    public Object getProperty(Object object, QName name) throws Exception {
        DistanceBufferOperator operator = (DistanceBufferOperator) object;
        Object property =
                OGCUtils.property(operator.getExpression1(), operator.getExpression2(), name);

        if (property != null) {
            return property;
        }

        if ("Distance".equals(name.getLocalPart())) {
            return DistanceUnits.of(operator.getDistance(), operator.getDistanceUnits());
        }

        return null;
    }
}
