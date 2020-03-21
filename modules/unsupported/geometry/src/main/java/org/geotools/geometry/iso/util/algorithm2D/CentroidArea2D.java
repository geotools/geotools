/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2006  Vivid Solutions
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
package org.geotools.geometry.iso.util.algorithm2D;

import java.util.Iterator;
import java.util.List;
import org.geotools.geometry.iso.aggregate.MultiSurfaceImpl;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.RingImplUnsafe;
import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Computes the centroid of an area geometry.
 *
 * <h2>Algorithm</h2>
 *
 * Based on the usual algorithm for calculating the centroid as a weighted sum of the centroids of a
 * decomposition of the area into (possibly overlapping) triangles. The algorithm has been extended
 * to handle holes and multi-polygons. See <code>http://www.faqs.org/faqs/graphics/algorithms-faq/
 * </code> for further details of the basic approach.
 */
public class CentroidArea2D {

    static final int X = 0;
    static final int Y = 1;
    static final int Z = 2;
    // private FeatGeomFactoryImpl factory = null;
    private CoordinateReferenceSystem crs = null;

    // the point all triangles are based at
    private DirectPosition basePt = null;

    // partial area sum
    private double areasum2 = 0;

    // partial centroid sum
    // private DirectPositionImpl cg3 = new DirectPositionImpl();
    double centSumX = 0.0;
    double centSumY = 0.0;
    double centSumZ = 0.0;

    /** Creates a new Centroid operation */
    public CentroidArea2D(CoordinateReferenceSystem crs) {
        this.crs = crs;
        this.basePt = null;
    }

    /**
     * Adds the area defined by a Geometry to the centroid total. If the geometry has no area it
     * does not contribute to the centroid.
     *
     * @param geom the geometry to add
     */
    public void add(GeometryImpl geom) {
        if (geom instanceof SurfaceImpl) {
            SurfaceBoundaryImpl sb = ((SurfaceImpl) geom).getBoundary();
            this.setBasePoint(
                    ((CurveImpl) sb.getExterior().getGenerators().iterator().next())
                            .getStartPoint());
            this.addSurface(sb);
        } else if (geom instanceof MultiSurfaceImpl) {
            Iterator<OrientableSurface> surfaces =
                    ((MultiSurfaceImpl) geom).getElements().iterator();
            while (surfaces.hasNext()) {
                this.add((GeometryImpl) surfaces.next());
            }
        }
    }

    public DirectPositionImpl getCentroid() {
        DirectPositionImpl centroid =
                new DirectPositionImpl(
                        crs); // this.factory.getGeometryFactoryImpl().createDirectPosition();
        centroid.setX(this.centSumX / 3 / this.areasum2);
        centroid.setY(this.centSumY / 3 / this.areasum2);
        return centroid;
    }

    private void setBasePoint(DirectPosition basePt) {
        if (this.basePt == null) this.basePt = basePt;
    }

    private void addSurface(SurfaceBoundaryImpl sb) {
        this.addShell(((RingImplUnsafe) sb.getExterior()).asDirectPositions());
        for (int i = 0; i < sb.getInteriors().size(); i++) {
            this.addHole(((RingImplUnsafe) sb.getInteriors().get(i)).asDirectPositions());
        }
    }

    private void addShell(List<DirectPosition> pts) {
        boolean isPositiveArea = !CGAlgorithms.isCCW(pts);
        for (int i = 0; i < pts.size() - 1; i++) {
            addTriangle(basePt, pts.get(i), pts.get(i + 1), isPositiveArea);
        }
    }

    private void addHole(List<DirectPosition> pts) {
        boolean isPositiveArea = CGAlgorithms.isCCW(pts);
        for (int i = 0; i < pts.size() - 1; i++) {
            addTriangle(basePt, pts.get(i), pts.get(i + 1), isPositiveArea);
        }
    }

    private void addTriangle(
            DirectPosition p0, DirectPosition p1, DirectPosition p2, boolean isPositiveArea) {
        double sign = (isPositiveArea) ? 1.0 : -1.0;

        // this.centroid3(p0, p1, p2);
        double tempSumX = p0.getOrdinate(X) + p1.getOrdinate(X) + p2.getOrdinate(X);
        double tempSumY = p0.getOrdinate(Y) + p1.getOrdinate(Y) + p2.getOrdinate(Y);
        // double tempSumZ = 0.0;

        double area2 = area2(p0, p1, p2);
        this.centSumX += sign * area2 * tempSumX;
        this.centSumY += sign * area2 * tempSumY;
        this.areasum2 += sign * area2;
    }

    /**
     * Returns twice the signed area of the triangle p1-p2-p3, positive if a,b,c are oriented ccw,
     * and negative if cw.
     */
    private static double area2(DirectPosition p1, DirectPosition p2, DirectPosition p3) {
        return (p2.getOrdinate(X) - p1.getOrdinate(X)) * (p3.getOrdinate(Y) - p1.getOrdinate(Y))
                - (p3.getOrdinate(X) - p1.getOrdinate(X)) * (p2.getOrdinate(Y) - p1.getOrdinate(Y));
    }
}
