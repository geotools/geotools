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
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml2.bindings.GML2ParsingUtils;
import org.geotools.xml.BindingWalkerFactory;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;


/**
 * Utility class for gml3 parsing.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 *
 * @source $URL$
 */
public class GML3ParsingUtils {
    /**
     * Utility method to implement Binding.parse for a binding which parses
     * into A feature.
     *
     * @param instance The instance being parsed.
     * @param node The parse tree.
     * @param value The value from the last binding in the chain.
     * @param ftCache The feature type cache.
     * @param bwFactory Binding walker factory.
     *
     * @return A feature.
     */
    public static SimpleFeature parseFeature(ElementInstance instance, Node node, Object value,
        FeatureTypeCache ftCache, BindingWalkerFactory bwFactory)
        throws Exception {
        return GML2ParsingUtils.parseFeature(instance, node, value, ftCache, bwFactory);
    }

    /**
     * Turns a xml type definition into a geotools feature type.
     * @param type The xml schema tupe.
     *
     * @return The corresponding geotools feature type.
     */
    public static SimpleFeatureType featureType(XSDElementDeclaration element,
        BindingWalkerFactory bwFactory) throws Exception {
        return GML2ParsingUtils.featureType(element, bwFactory);
    }

    /**
     * Turns a parse node + feature type + fid info a feature.
     */
    static SimpleFeature feature(SimpleFeatureType fType, String fid, Node node)
        throws Exception {
        return GML2ParsingUtils.feature(fType, fid, node);
    }

    static CoordinateReferenceSystem crs(Node node) {
        return GML2ParsingUtils.crs(node);
    }

    static LineString lineString(Node node, GeometryFactory gf, CoordinateSequenceFactory csf) {
        return line(node, gf, csf, false);
    }

    static LinearRing linearRing(Node node, GeometryFactory gf, CoordinateSequenceFactory csf) {
        return (LinearRing) line(node, gf, csf, true);
    }

    static LineString line(Node node, GeometryFactory gf, CoordinateSequenceFactory csf,
        boolean ring) {
        if (node.hasChild(DirectPosition.class)) {
            List dps = node.getChildValues(DirectPosition.class);
            DirectPosition dp = (DirectPosition) dps.get(0);

            CoordinateSequence seq = csf.create(dps.size(), dp.getDimension());

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
                seq = csf.create(0, 0);
            } else {
                seq = csf.create(dps.length, dps[0].getDimension());

                for (int i = 0; i < dps.length; i++) {
                    DirectPosition dp = (DirectPosition) dps[i];

                    for (int j = 0; j < dp.getDimension(); j++) {
                        seq.setOrdinate(i, j, dp.getOrdinate(j));
                    }
                }
            }

            return ring ? gf.createLinearRing(seq) : gf.createLineString(seq);
        }

        if (node.hasChild(CoordinateSequence.class)) {
            CoordinateSequence seq = (CoordinateSequence) node.getChildValue(CoordinateSequence.class);

            return ring ? gf.createLinearRing(seq) : gf.createLineString(seq);
        }

        return null;
    }
}
