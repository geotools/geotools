/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.vector;

import java.util.ArrayList;
import java.util.Collection;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.util.ProgressListener;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.operation.linemerge.LineMerger;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.quadedge.QuadEdge;
import org.locationtech.jts.triangulate.quadedge.QuadEdgeSubdivision;
import org.locationtech.jts.triangulate.quadedge.Vertex;

public class Contours {

    private enum PointType {
        Point,
        MultiPoint
    }

    private static final GeometryFactory GF = new GeometryFactory();

    private double[] levels;

    private boolean smooth = true;

    private boolean simplify = false;

    private ProgressListener progressListener;

    public SimpleFeatureCollection contour(FeatureCollection features, String elevation) {
        if (progressListener != null) {
            progressListener.started();
        }
        ArrayList<Coordinate> coords = new ArrayList<>();

        PointType ptype = PointType.Point;
        if (MultiPoint.class.equals(
                features.getSchema().getGeometryDescriptor().getType().getBinding())) {
            ptype = PointType.MultiPoint;
        }
        try (SimpleFeatureIterator itr = (SimpleFeatureIterator) features.features()) {
            while (itr.hasNext()) {
                SimpleFeature f = itr.next();
                ArrayList<Point> points = new ArrayList<>();
                if (ptype == PointType.Point) {
                    points.add((Point) f.getDefaultGeometry());
                } else {
                    MultiPoint mp = (MultiPoint) f.getDefaultGeometry();
                    for (int i = 0; i < mp.getNumGeometries(); i++) {
                        points.add((Point) mp.getGeometryN(i));
                    }
                }
                for (Point point : points) {
                    Coordinate coordinate = new Coordinate();
                    coordinate.setX(point.getCoordinate().x);
                    coordinate.setY(point.getCoordinate().y);
                    coordinate.setZ(((Number) f.getAttribute(elevation)).doubleValue());
                    coords.add(coordinate);
                }
            }
        }
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("contour");
        ftb.add("the_geom", LineString.class);
        ftb.add("elevation", Double.class);
        ftb.setCRS(features.getBounds().getCoordinateReferenceSystem());
        ftb.setDefaultGeometry("the_geom");
        SimpleFeatureType type = ftb.buildFeatureType();
        SimpleFeatureBuilder fBuilder = new SimpleFeatureBuilder(type);
        DelaunayTriangulationBuilder builder = new DelaunayTriangulationBuilder();
        builder.setSites(coords);
        QuadEdgeSubdivision quadEdgeSubdivision = builder.getSubdivision();
        @SuppressWarnings("unchecked")
        Collection<QuadEdge> primaryEdges = quadEdgeSubdivision.getPrimaryEdges(false);

        ArrayList<SimpleFeature> feats = new ArrayList<>();
        int percent = 1;
        int steps = levels.length;
        for (double contourValue : levels) {
            if (progressListener != null) {
                progressListener.progress(100 / steps * percent);
            }
            ArrayList<LineString> lines = extractContour(primaryEdges, contourValue);

            MultiLineString ml = GF.createMultiLineString(lines.toArray(new LineString[] {}));

            Geometry inter = ml.union();
            // for each contour value "join" the lines
            LineMerger merger = new LineMerger();
            for (int i = 0; i < inter.getNumGeometries(); i++) {
                merger.add(inter.getGeometryN(i));
            }
            @SuppressWarnings("unchecked")
            Collection<LineString> collection = merger.getMergedLineStrings();

            for (LineString l : collection) {

                if (simplify) { // Should we smooth then simplify or visa versa

                    DouglasPeuckerSimplifier simplifier = new DouglasPeuckerSimplifier(l);
                    l = (LineString) simplifier.getResultGeometry();
                }
                if (smooth) {
                    fBuilder.set("the_geom", JTS.smooth(l, 0.4, GF));
                } else {
                    fBuilder.set("the_geom", l);
                }
                fBuilder.set("elevation", contourValue);
                SimpleFeature f = fBuilder.buildFeature(null);

                feats.add(f);
            }
        }
        if (progressListener != null) {
            progressListener.complete();
        }
        return DataUtilities.collection(feats);
    }

    /**
     * @param primaryEdges
     * @param contourValue
     * @return
     */
    public ArrayList<LineString> extractContour(Collection<QuadEdge> primaryEdges, double contourValue) {
        ArrayList<LineString> lines = new ArrayList<>();
        for (QuadEdge edge : primaryEdges) {
            Vertex[] v = new Vertex[3];
            v[0] = edge.orig();
            v[1] = edge.dest();
            v[2] = edge.oNext().dest();
            ArrayList<Integer> low = new ArrayList<>(3);
            ArrayList<Integer> high = new ArrayList<>(3);
            for (int i = 0; i < 3; i++) {
                if (v[i].getZ() < contourValue) {
                    low.add(i);
                } else if (v[i].getZ() >= contourValue) {
                    high.add(i);
                }
            }

            if (low.size() == 3 || high.size() == 3) {
                continue;
            }
            if (low.isEmpty() || high.isEmpty()) {
                continue;
            }
            QuadEdge[] edges = new QuadEdge[2];
            if (high.size() == 2 && low.size() == 1) {
                int start = low.get(0);
                edges[0] = QuadEdge.makeEdge(v[start], v[high.get(0)]);
                edges[1] = QuadEdge.makeEdge(v[start], v[high.get(1)]);
            } else if (low.size() == 2 && high.size() == 1) {
                int start = high.get(0);
                edges[0] = QuadEdge.makeEdge(v[low.get(0)], v[start]);
                edges[1] = QuadEdge.makeEdge(v[low.get(1)], v[start]);
            } else {
                // off the edge
                continue;
            }
            // now for each edge calculate where the contour crosses
            Coordinate[] pt = new Coordinate[2];
            for (int i = 0; i < 2; i++) {
                double start = edges[i].orig().getZ();
                double end = edges[i].dest().getZ();

                LineSegment lineSegment = edges[i].toLineSegment();
                double delta = end - start;
                double proportion = (contourValue - start) / delta;
                pt[i] = lineSegment.pointAlong(proportion);
            }
            // generate a new line geometry
            LineString l = GF.createLineString(pt);
            lines.add(l);
        }
        return lines;
    }

    /** @return the levels */
    public double[] getLevels() {
        return levels;
    }

    /** @param levels the levels to set */
    public void setLevels(double[] levels) {
        this.levels = levels;
    }

    /** @param booleanValue */
    public void setSmooth(boolean booleanValue) {
        smooth = booleanValue;
    }

    /** @param booleanValue */
    public void setSimplify(boolean booleanValue) {
        simplify = booleanValue;
    }

    /** @param progressListener */
    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }
}
