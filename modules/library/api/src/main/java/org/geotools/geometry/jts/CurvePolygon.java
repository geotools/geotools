/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 */
package org.geotools.geometry.jts;

import java.util.List;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

/**
 * A subclass of polygon that can host also curves and will linearize if needed
 *
 * @author Andrea Aime - GeoSolutions
 */
public class CurvePolygon extends Polygon implements CurvedGeometry<Polygon> {

    private static final long serialVersionUID = -6961191502473439328L;
    private double tolerance;

    public CurvePolygon(
            LinearRing shell, List<LinearRing> holes, GeometryFactory factory, double tolerance) {
        super(shell, holes.toArray(new LinearRing[holes.size()]), factory);
        this.tolerance = tolerance;
    }

    public CurvePolygon(
            LinearRing shell, LinearRing[] holes, GeometryFactory factory, double tolerance) {
        super(shell, holes, factory);
        this.tolerance = tolerance;
    }

    public Polygon linearize() {
        return linearize(tolerance);
    }

    public Polygon linearize(double tolerance) {

        int numHoles = getNumInteriorRing();
        LinearRing shell = linearize(tolerance, (LinearRing) getExteriorRing());
        LinearRing[] holes = new LinearRing[numHoles];
        for (int k = 0; k < numHoles; k++) {
            LinearRing hole = (LinearRing) getInteriorRingN(k);
            hole = linearize(tolerance, hole);
            holes[k] = hole;
        }

        return getFactory().createPolygon(shell, holes);
    }

    private LinearRing linearize(double tolerance, LinearRing hole) {
        if (hole instanceof CurvedGeometry<?>) {
            CurvedGeometry<?> curved = (CurvedGeometry<?>) hole;
            hole = (LinearRing) curved.linearize(tolerance);
        }
        return hole;
    }

    public String toCurvedText() {
        StringBuilder sb = new StringBuilder("CURVEPOLYGON ");
        if (isEmpty()) {
            sb.append(" EMTPY");
        } else {
            sb.append("(");
            writeRing(sb, getExteriorRing());
            int holeNum = getNumInteriorRing();
            for (int k = 0; k < holeNum; k++) {
                sb.append(", ");
                LineString component = getInteriorRingN(k);
                writeRing(sb, component);
            }
            sb.append(")");
        }
        return sb.toString();
    }

    private void writeRing(StringBuilder sb, LineString component) {
        if (component instanceof SingleCurvedGeometry<?>) {
            SingleCurvedGeometry<?> curved = (SingleCurvedGeometry<?>) component;
            sb.append(curved.toCurvedText());
        } else {
            sb.append("(");
            CoordinateSequence cs = component.getCoordinateSequence();
            for (int i = 0; i < cs.size(); i++) {
                sb.append(cs.getX(i) + " " + cs.getY(i));
                if (i < cs.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");
        }
    }

    @Override
    public double getTolerance() {
        return tolerance;
    }

    @Override
    public int getCoordinatesDimension() {
        return 2;
    }

    @Override
    public CurvePolygon copyInternal() {
        return new CurvePolygon(shell, holes, factory, tolerance);
    }
}
