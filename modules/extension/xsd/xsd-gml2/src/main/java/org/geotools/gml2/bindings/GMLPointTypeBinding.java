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

import javax.xml.namespace.QName;

import org.geotools.gml2.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


/**
 * Binding object for the type http://www.opengis.net/gml:PointType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="PointType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         A Point is defined by a single
 *              coordinate tuple.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGeometryType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;choice&gt;
 *                      &lt;element ref="gml:coord"/&gt;
 *                      &lt;element ref="gml:coordinates"/&gt;
 *                  &lt;/choice&gt;
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
 * @source $URL$
 */
public class GMLPointTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;

    public GMLPointTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    public int getExecutionMode() {
        return BEFORE;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.PointType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Point.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        if (node.getChild("coord") != null) {
            Coordinate c = (Coordinate) node.getChild("coord").getValue();

            return gFactory.createPoint(c);
        }

        if (node.getChild("coordinates") != null) {
            CoordinateSequence seq = (CoordinateSequence) node.getChild("coordinates").getValue();

            return gFactory.createPoint(seq);
        }

        throw new RuntimeException("Could not find a coordinate");
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        Point point = (Point) object;

        if (GML.coord.equals(name)) {
            return point.getCoordinate();
        }

        return null;
    }
}
