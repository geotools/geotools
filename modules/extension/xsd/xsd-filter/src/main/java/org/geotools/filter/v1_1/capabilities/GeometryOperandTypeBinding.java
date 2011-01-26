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
package org.geotools.filter.v1_1.capabilities;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import org.opengis.filter.capability.GeometryOperand;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.*;
import org.geotools.xs.bindings.XSQNameBinding;


/**
 * Binding object for the type http://www.opengis.net/ogc:GeometryOperandType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:simpleType name="GeometryOperandType"&gt;
 *      &lt;xsd:restriction base="xsd:QName"&gt;
 *          &lt;xsd:enumeration value="gml:Envelope"/&gt;
 *          &lt;xsd:enumeration value="gml:Point"/&gt;
 *          &lt;xsd:enumeration value="gml:LineString"/&gt;
 *          &lt;xsd:enumeration value="gml:Polygon"/&gt;
 *          &lt;xsd:enumeration value="gml:ArcByCenterPoint"/&gt;
 *          &lt;xsd:enumeration value="gml:CircleByCenterPoint"/&gt;
 *          &lt;xsd:enumeration value="gml:Arc"/&gt;
 *          &lt;xsd:enumeration value="gml:Circle"/&gt;
 *          &lt;xsd:enumeration value="gml:ArcByBulge"/&gt;
 *          &lt;xsd:enumeration value="gml:Bezier"/&gt;
 *          &lt;xsd:enumeration value="gml:Clothoid"/&gt;
 *          &lt;xsd:enumeration value="gml:CubicSpline"/&gt;
 *          &lt;xsd:enumeration value="gml:Geodesic"/&gt;
 *          &lt;xsd:enumeration value="gml:OffsetCurve"/&gt;
 *          &lt;xsd:enumeration value="gml:Triangle"/&gt;
 *          &lt;xsd:enumeration value="gml:PolyhedralSurface"/&gt;
 *          &lt;xsd:enumeration value="gml:TriangulatedSurface"/&gt;
 *          &lt;xsd:enumeration value="gml:Tin"/&gt;
 *          &lt;xsd:enumeration value="gml:Solid"/&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class GeometryOperandTypeBinding extends AbstractSimpleBinding {
    NamespaceContext namespaceContext;

    public GeometryOperandTypeBinding(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.GeometryOperandType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return GeometryOperand.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        QName name = (QName) value;

        return GeometryOperand.get(name.getNamespaceURI(), name.getLocalPart());
    }

    public String encode(Object object, String value) throws Exception {
        GeometryOperand operand = (GeometryOperand) object;

        return new XSQNameBinding(namespaceContext).encode(new QName(operand.getNamespaceURI(),
                operand.getLocalPart()), value);
    }
}
