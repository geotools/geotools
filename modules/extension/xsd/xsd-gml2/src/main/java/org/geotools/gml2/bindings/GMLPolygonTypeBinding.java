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
package org.geotools.gml2.bindings;

import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.gml2.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Binding object for the type http://www.opengis.net/gml:PolygonType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="PolygonType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         A Polygon is defined by an outer
 *              boundary and zero or more inner          boundaries which
 *              are in turn defined by LinearRings.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGeometryType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="gml:outerBoundaryIs"/&gt;
 *                  &lt;element ref="gml:innerBoundaryIs" minOccurs="0" maxOccurs="unbounded"/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class GMLPolygonTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;

    public GMLPolygonTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.PolygonType;
    }

    public int getExecutionMode() {
        return BEFORE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Polygon.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        LinearRing shell = (LinearRing) node.getChild("outerBoundaryIs").getValue();

        List innerRings = node.getChildren("innerBoundaryIs");
        LinearRing[] holes = new LinearRing[innerRings.size()];

        for (int i = 0; i < innerRings.size(); i++) {
            Node inode = (Node) innerRings.get(i);
            holes[i] = (LinearRing) inode.getValue();
        }

        return gFactory.createPolygon(shell, holes);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        Polygon polygon = (Polygon) object;

        if (GML.outerBoundaryIs.equals(name)) {
            return polygon.getExteriorRing();
        }

        if (GML.innerBoundaryIs.equals(name)) {
            int n = polygon.getNumInteriorRing();

            if (n > 0) {
                LineString[] interior = new LineString[n];

                for (int i = 0; i < n; i++) {
                    interior[i] = polygon.getInteriorRingN(i);
                }

                return interior;
            }
        }

        return null;
    }
}
