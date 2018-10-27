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

import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml2.GML;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 * Filter parsing / encoding utility class.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class OGCUtils {
    /**
     * Implementation of getProperty for {@link BinarySpatialOpTypeBinding} and {@link
     * DistanceBufferTypeBinding}
     *
     * @param e1 First expression
     * @param e2 Second expression
     * @param name name of property
     * @return the object for the property, or null
     */
    static Object property(Expression e1, Expression e2, QName name) {
        if (OGC.PropertyName.equals(name)) {
            if (e1 instanceof PropertyName) {
                return e1;
            } else if (e2 instanceof PropertyName) {
                return e2;
            }
        }

        if (new QName(GML.NAMESPACE, "_Geometry").equals(name)) {
            if (e1 instanceof Literal) {
                Literal literal = (Literal) e1;

                if (literal.getValue() instanceof Geometry) {
                    return literal.getValue();
                }
            } else if (e2 instanceof Literal) {
                Literal literal = (Literal) e2;

                if (literal.getValue() instanceof Geometry) {
                    return literal.getValue();
                }
            }
        }

        if (new QName(GML.NAMESPACE, "Box").equals(name) /*filter 1.0*/
                || new QName(GML.NAMESPACE, "Envelope").equals(name) /*filter 1.1*/) {
            if (e1 instanceof Literal) {
                Literal literal = (Literal) e1;

                if (literal.getValue() instanceof Envelope) {
                    return literal.getValue();
                }
            } else if (e2 instanceof Literal) {
                Literal literal = (Literal) e2;

                if (literal.getValue() instanceof Envelope) {
                    return literal.getValue();
                }
            }
        }

        return null;
    }

    /**
     * Returns a two element array of PropertyName, Literal ( Geometry )
     *
     * @param node The parse tree.
     * @return A two element array of expressions for a BinarySpatialOp type.
     */
    static Expression[] spatial(Node node, FilterFactory2 ff, GeometryFactory gf) {
        List names = node.getChildValues(PropertyName.class);
        if (names.size() == 2) {
            // join
            return new Expression[] {(Expression) names.get(0), (Expression) names.get(1)};
        }

        PropertyName name = (PropertyName) node.getChildValue(PropertyName.class);
        Expression spatial = null;

        if (node.hasChild(Geometry.class)) {
            spatial = ff.literal(node.getChildValue(Geometry.class));
        } else if (node.hasChild(Envelope.class)) {
            // JD: creating an envelope here would break a lot of our code, for instance alot of
            // code that encodes a filter into sql will choke on this
            Envelope envelope = (Envelope) node.getChildValue(Envelope.class);
            Polygon polygon =
                    gf.createPolygon(
                            gf.createLinearRing(
                                    new Coordinate[] {
                                        new Coordinate(envelope.getMinX(), envelope.getMinY()),
                                        new Coordinate(envelope.getMaxX(), envelope.getMinY()),
                                        new Coordinate(envelope.getMaxX(), envelope.getMaxY()),
                                        new Coordinate(envelope.getMinX(), envelope.getMaxY()),
                                        new Coordinate(envelope.getMinX(), envelope.getMinY())
                                    }),
                            null);

            if (envelope instanceof ReferencedEnvelope) {
                polygon.setUserData(((ReferencedEnvelope) envelope).getCoordinateReferenceSystem());
            }

            spatial = ff.literal(polygon);
        } else {
            // look for an expression that is not a property name
            for (Iterator c = node.getChildren().iterator(); c.hasNext(); ) {
                Node child = (Node) c.next();

                // if property name, skip
                if (child.getValue() instanceof PropertyName) {
                    continue;
                }

                // if expression, use it
                if (child.getValue() instanceof Expression) {
                    spatial = (Expression) child.getValue();
                    break;
                }
            }
        }

        return new Expression[] {name, spatial};
    }
}
