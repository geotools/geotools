/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.process.geometry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.locationtech.jts.densify.Densifier;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.operation.linemerge.LineMerger;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

public class Skeletonize {

    private static final GeometryFactory GF = new GeometryFactory();
    private static final Logger LOG = Logger.getLogger("org.geotools.process.geometry.CentreLine");

    static Geometry getSkeleton(Geometry poly, double perc_tolerance) {
        Class<? extends Geometry> clazz = poly.getClass();

        if (MultiPolygon.class.isAssignableFrom(clazz)) {
            MultiPolygon multi = (MultiPolygon) poly;
            List<LineString> geoms = new ArrayList<>();
            for (int i = 0; i < multi.getNumGeometries(); i++) {
                Geometry skel = getSkeleton((Polygon) multi.getGeometryN(i), perc_tolerance);

                if (skel instanceof LineString) {
                    geoms.add((LineString) skel);
                } else if (skel instanceof MultiLineString) {
                    for (int k = 0; k < skel.getNumGeometries(); k++) {
                        geoms.add((LineString) skel.getGeometryN(k));
                    }
                } else {
                    LOG.info("Unexpected result type " + skel.getClass());
                }
            }
            return GF.createMultiLineString(GeometryFactory.toLineStringArray(geoms));
        } else if (Polygon.class.isAssignableFrom(clazz)) {
            return getSkeleton((Polygon) poly, perc_tolerance);
        } else {
            LOG.info("Input Geometry was not an expected geometry (" + clazz + ")");
            return poly;
        }
    }

    static Geometry getSkeleton(Geometry poly) {
        return getSkeleton(poly, 5);
    }

    static Geometry getSkeleton(Polygon poly) {
        return getSkeleton(poly, 5);
    }

    static Geometry getSkeleton(Polygon poly, double perc_tolerance) {
        double dist = poly.getLength() * (perc_tolerance / 100);
        Polygon spoly = (Polygon) TopologyPreservingSimplifier.simplify(poly, dist / 100.0);
        Polygon vpoly = (Polygon) Densifier.densify(spoly, dist);

        Geometry triangles = getVoroni(vpoly);

        // System.out.println(outW.toString());
        Set<LineString> edges = new HashSet<>();
        LineMerger merger = new LineMerger();
        for (int i = 0; i < triangles.getNumGeometries(); i++) {
            Polygon p = (Polygon) triangles.getGeometryN(i);

            LinearRing exteriorRing = p.getExteriorRing();
            for (int j = 1; j < exteriorRing.getNumPoints() - 1; j++) {
                Coordinate coordinate1 = exteriorRing.getPointN(j - 1).getCoordinate();
                Coordinate coordinate2 = exteriorRing.getPointN(j).getCoordinate();
                LineString edge = GF.createLineString(new Coordinate[] {coordinate1, coordinate2});
                if (vpoly.contains(edge)) { // exclude touching or external edges
                    edges.add((LineString) edge.norm()); // normalize to avoid duplicates
                }
            }
        }
        merger.add(edges);

        Geometry geom =
                GF.createMultiLineString(
                        GeometryFactory.toLineStringArray(merger.getMergedLineStrings()));
        geom = geom.union();

        LOG.fine(geom.toText());
        return geom;
    }

    static Geometry getVoroni(Polygon poly) {
        VoronoiDiagramBuilder builder = new VoronoiDiagramBuilder();
        builder.setSites(poly);
        builder.setClipEnvelope(poly.getEnvelopeInternal());

        Geometry triangles = builder.getDiagram(GF);
        return triangles;
    }
}
