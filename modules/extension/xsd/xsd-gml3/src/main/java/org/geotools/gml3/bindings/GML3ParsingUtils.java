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

import java.util.List;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.geometry.jts.CircularArc;
import org.geotools.geometry.jts.CurvedGeometries;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.geometry.jts.JTS;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml2.bindings.GML2ParsingUtils;
import org.geotools.gml3.ArcParameters;
import org.geotools.gml3.Circle;
import org.geotools.xsd.BindingWalkerFactory;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Utility class for gml3 parsing.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
public class GML3ParsingUtils {
    /**
     * Utility method to implement Binding.parse for a binding which parses into A feature.
     *
     * @param instance The instance being parsed.
     * @param node The parse tree.
     * @param value The value from the last binding in the chain.
     * @param ftCache The feature type cache.
     * @param bwFactory Binding walker factory.
     * @return A feature.
     */
    public static SimpleFeature parseFeature(
            ElementInstance instance,
            Node node,
            Object value,
            FeatureTypeCache ftCache,
            BindingWalkerFactory bwFactory)
            throws Exception {
        return GML2ParsingUtils.parseFeature(instance, node, value, ftCache, bwFactory);
    }

    /**
     * Turns a xml type definition into a geotools feature type.
     *
     * @return The corresponding geotools feature type.
     */
    public static SimpleFeatureType featureType(
            XSDElementDeclaration element, BindingWalkerFactory bwFactory) throws Exception {
        return GML2ParsingUtils.featureType(element, bwFactory);
    }

    /** Turns a parse node + feature type + fid info a feature. */
    static SimpleFeature feature(SimpleFeatureType fType, String fid, Node node) throws Exception {
        return GML2ParsingUtils.feature(fType, fid, node);
    }

    static CoordinateReferenceSystem crs(Node node) {
        return GML2ParsingUtils.crs(node);
    }

    /**
     * Returns the number of dimensions for the specified node, eventually recursing up to find the
     * parent node that has the indication of the dimensions (normally the top-most geometry element
     * has it, not the posList). If no srsDimension can be found, check the srsName the same way and
     * return the srsDimensions instead. Returns 2 if no srsDimension or srsName attribute could be
     * found.
     */
    public static int dimensions(Node node) {
        Node current = node;
        while (current != null) {
            Node dimensions = current.getAttribute("srsDimension");
            if (dimensions != null) {
                return ((Number) dimensions.getValue()).intValue();
            }
            current = current.getParent();
        }
        current = node;
        while (current != null) {
            CoordinateReferenceSystem crs = crs(current);
            if (crs != null) {
                return crs.getCoordinateSystem().getDimension();
            }
            current = current.getParent();
        }

        return 2;
    }

    static LineString lineString(Node node, GeometryFactory gf, CoordinateSequenceFactory csf) {
        return line(node, gf, csf, false);
    }

    static LinearRing linearRing(Node node, GeometryFactory gf, CoordinateSequenceFactory csf) {
        return (LinearRing) line(node, gf, csf, true);
    }

    static LineString line(
            Node node, GeometryFactory gf, CoordinateSequenceFactory csf, boolean ring) {
        if (node.hasChild(DirectPosition.class)) {
            List dps = node.getChildValues(DirectPosition.class);
            DirectPosition dp = (DirectPosition) dps.get(0);

            CoordinateSequence seq = JTS.createCS(csf, dps.size(), dp.getDimension());

            for (int i = 0; i < dps.size(); i++) {
                dp = (DirectPosition) dps.get(i);

                for (int j = 0; j < dp.getDimension(); j++) {
                    seq.setOrdinate(i, j, dp.getOrdinate(j));
                }
            }

            return ring ? gf.createLinearRing(seq) : gf.createLineString(seq);
        }

        if (node.hasChild(Point.class)) {
            List points = node.getChildValues(Point.class);
            Coordinate[] coordinates = new Coordinate[points.size()];

            for (int i = 0; i < points.size(); i++) {
                coordinates[i] = ((Point) points.get(0)).getCoordinate();
            }

            return ring ? gf.createLinearRing(coordinates) : gf.createLineString(coordinates);
        }

        if (node.hasChild(Coordinate.class)) {
            List list = node.getChildValues(Coordinate.class);
            Coordinate[] coordinates = (Coordinate[]) list.toArray(new Coordinate[list.size()]);

            return ring ? gf.createLinearRing(coordinates) : gf.createLineString(coordinates);
        }

        if (node.hasChild(DirectPosition[].class)) {
            DirectPosition[] dps = (DirectPosition[]) node.getChildValue(DirectPosition[].class);

            CoordinateSequence seq = null;

            if (dps.length == 0) {
                seq = JTS.createCS(csf, 0, 0);
            } else {
                seq = JTS.createCS(csf, dps.length, dps[0].getDimension());

                for (int i = 0; i < dps.length; i++) {
                    DirectPosition dp = dps[i];

                    for (int j = 0; j < dp.getDimension(); j++) {
                        seq.setOrdinate(i, j, dp.getOrdinate(j));
                    }
                }
            }

            return ring ? gf.createLinearRing(seq) : gf.createLineString(seq);
        }

        if (node.hasChild(CoordinateSequence.class)) {
            CoordinateSequence seq =
                    (CoordinateSequence) node.getChildValue(CoordinateSequence.class);

            return ring ? gf.createLinearRing(seq) : gf.createLineString(seq);
        }

        return null;
    }

    /**
     * Returns a curved geometry factory given the linearization constraints, the original factory,
     * and a coordinate sequence representing the control points of a curved geometry
     */
    public static CurvedGeometryFactory getCurvedGeometryFactory(
            ArcParameters arcParameters, GeometryFactory gFactory, CoordinateSequence cs) {
        CurvedGeometryFactory factory;
        if (gFactory instanceof CurvedGeometryFactory) {
            factory = (CurvedGeometryFactory) gFactory;
        } else if (arcParameters != null && arcParameters.getLinearizationTolerance() != null) {
            double tolerance = Double.MAX_VALUE;
            if (cs != null) {
                CircularArc arc = CurvedGeometries.getArc(cs, 0);
                Circle c = new Circle(arc.getCenter(), arc.getRadius());
                tolerance = arcParameters.getLinearizationTolerance().getTolerance(c);
            }
            factory = new CurvedGeometryFactory(gFactory, tolerance);
        } else {
            factory = new CurvedGeometryFactory(gFactory, Double.MAX_VALUE);
        }
        return factory;
    }
}
