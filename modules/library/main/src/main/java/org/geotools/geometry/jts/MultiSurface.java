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
 *    Lesser General Public License for more details.
 */
package org.geotools.geometry.jts;

import java.util.List;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

/**
 * A subclass of {@link MultiPolygon} that can host also {@link CurvePolygon} and will linearize if
 * needed
 *
 * @author Andrea Aime - GeoSolutions
 */
public class MultiSurface extends MultiPolygon implements MultiCurvedGeometry<MultiPolygon> {

    private static final long serialVersionUID = -5796254063449438787L;

    double tolerance;

    public MultiSurface(List<Polygon> components, GeometryFactory factory, double tolerance) {
        super(components.toArray(new Polygon[components.size()]), factory);
        this.tolerance = tolerance;
    }

    public MultiSurface(Polygon[] polygons, GeometryFactory factory, double tolerance) {
        super(polygons, factory);
        this.tolerance = tolerance;
    }

    public MultiPolygon linearize() {
        return linearize(tolerance);
    }

    public MultiPolygon linearize(double tolerance) {
        int numGeometries = getNumGeometries();
        Polygon[] linearized = new Polygon[numGeometries];
        for (int k = 0; k < numGeometries; k++) {
            Polygon component = (Polygon) getGeometryN(k);
            if (component instanceof CurvedGeometry<?>) {
                CurvedGeometry<?> curved = (CurvedGeometry<?>) component;
                linearized[k] = (Polygon) curved.linearize(tolerance);
            } else {
                linearized[k] = component;
            }
        }

        return getFactory().createMultiPolygon(linearized);
    }

    public String toCurvedText() {
        StringBuilder sb = new StringBuilder("MULTISURFACE ");
        int numGeometries = getNumGeometries();
        if (numGeometries == 0) {
            sb.append("EMPTY");
        } else {
            sb.append("(");
            for (int k = 0; k < numGeometries; k++) {
                Polygon component = (Polygon) getGeometryN(k);
                if (component instanceof CurvedGeometry<?>) {
                    CurvedGeometry<?> curved = (CurvedGeometry<?>) component;
                    sb.append(curved.toCurvedText());
                } else {
                    // straight lines polygon
                    sb.append("(");
                    writeCoordinateSequence(
                            sb, component.getExteriorRing().getCoordinateSequence());
                    int numHoles = component.getNumInteriorRing();
                    for (int i = 0; i < numHoles; i++) {
                        sb.append(", ");
                        writeCoordinateSequence(
                                sb, component.getInteriorRingN(i).getCoordinateSequence());
                    }
                    sb.append(")");
                }
                if (k < numGeometries - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }

    private void writeCoordinateSequence(StringBuilder sb, CoordinateSequence cs) {
        sb.append("(");
        for (int i = 0; i < cs.size(); i++) {
            sb.append(cs.getX(i) + " " + cs.getY(i));
            if (i < cs.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
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
    public MultiSurface copyInternal() {
        Polygon[] polys = new Polygon[geometries.length];
        for (int i = 0; i < geometries.length; i++) {
            polys[i] = (Polygon) geometries[i];
        }

        return new MultiSurface(polys, factory, tolerance);
    }
}
