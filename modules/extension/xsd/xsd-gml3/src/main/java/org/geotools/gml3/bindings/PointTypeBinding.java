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
package org.geotools.gml3.bindings;

import javax.xml.namespace.QName;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.geometry.DirectPosition;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
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
 *          &lt;documentation&gt;A Point is defined by a single coordinate tuple.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGeometricPrimitiveType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;choice&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;GML supports two different ways to specify the direct poisiton of a point. 1. The "pos" element is of type
 *                                                          DirectPositionType.&lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                      &lt;element ref="gml:pos"/&gt;
 *                      &lt;element ref="gml:coordinates"&gt;
 *                          &lt;annotation&gt;
 *                              &lt;documentation&gt;Deprecated with GML version 3.1.0 for coordinates with ordinate values that are numbers. Use "pos"
 *                                                                  instead. The "coordinates" element shall only be used for coordinates with ordinates that require a string
 *                                                                  representation, e.g. DMS representations.&lt;/documentation&gt;
 *                          &lt;/annotation&gt;
 *                      &lt;/element&gt;
 *                      &lt;element ref="gml:coord"&gt;
 *                          &lt;annotation&gt;
 *                              &lt;documentation&gt;Deprecated with GML version 3.0. Use "pos" instead. The "coord" element is included for
 *                                                                  backwards compatibility with GML 2.&lt;/documentation&gt;
 *                          &lt;/annotation&gt;
 *                      &lt;/element&gt;
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
 *
 * @source $URL$
 */
public class PointTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;

    public PointTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.PointType;
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
        if (node.hasChild(DirectPosition.class)) {
            DirectPosition dp = (DirectPosition) node.getChildValue(DirectPosition.class);

            return gFactory.createPoint(new Coordinate(dp.getOrdinate(0), dp.getOrdinate(1)));
        }

        if (node.hasChild(Coordinate.class)) {
            return gFactory.createPoint((Coordinate) node.getChildValue(Coordinate.class));
        }

        if (node.hasChild(CoordinateSequence.class)) {
            return gFactory.createPoint((CoordinateSequence) node.getChildValue(
                    CoordinateSequence.class));
        }

        return null;
    }

    public Object getProperty(Object object, QName name) {
        //hack for xlink stuff
        Geometry geometry = (Geometry) object;
        if ( geometry.isEmpty() ) {
            return null;
        }
        
        if ("pos".equals(name.getLocalPart())) {
            Point point = (Point) object;

            DirectPosition2D dp = new DirectPosition2D();
            dp.setOrdinate(0, point.getX());
            dp.setOrdinate(1, point.getY());

            return dp;
        }

        return null;
    }
}
