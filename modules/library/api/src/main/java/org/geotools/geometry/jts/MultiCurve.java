/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

/**
 * A subclass of multi line string that can host also curves and will linearize if needed
 *
 * @author Andrea Aime - GeoSolutions
 */
public class MultiCurve extends MultiLineString implements MultiCurvedGeometry<MultiLineString> {

    private static final long serialVersionUID = -5796254063449438787L;

    double tolerance;

    public MultiCurve(List<LineString> components, GeometryFactory factory, double tolerance) {
        super(components.toArray(new LineString[components.size()]), factory);
        this.tolerance = tolerance;
    }

    public MultiLineString linearize() {
        return linearize(tolerance);
    }

    public MultiLineString linearize(double tolerance) {
        int numGeometries = getNumGeometries();
        LineString[] linearized = new LineString[numGeometries];
        for (int k = 0; k < numGeometries; k++) {
            LineString component = (LineString) getGeometryN(k);
            if (component instanceof CurvedGeometry<?>) {
                CurvedGeometry<?> curved = (CurvedGeometry<?>) component;
                linearized[k] = (LineString) curved.linearize(tolerance);
            } else {
                linearized[k] = component;
            }
        }

        return getFactory().createMultiLineString(linearized);
    }

    public String toCurvedText() {
        StringBuilder sb = new StringBuilder("MULTICURVE(");
        int numGeometries = getNumGeometries();
        for (int k = 0; k < numGeometries; k++) {
            LineString component = (LineString) getGeometryN(k);
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
            if (k < numGeometries - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public double getTolerance() {
        return tolerance;
    }

    @Override
    public int getCoordinatesDimension() {
        return 2;
    }
}
