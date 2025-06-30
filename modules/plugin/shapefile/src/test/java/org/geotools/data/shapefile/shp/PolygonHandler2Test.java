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
package org.geotools.data.shapefile.shp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * @version $Id$
 * @author Ian Schneider
 */
public class PolygonHandler2Test extends org.geotools.data.shapefile.TestCaseSupport {

    @Test
    public void testPolygonHandler() {
        Coordinate[] c = new Coordinate[3];
        c[0] = new Coordinate(0, 0, 0);
        c[1] = new Coordinate(1, 1, Double.NaN);
        c[2] = new Coordinate(1, 2, 3);

        PolygonHandler handler = new PolygonHandler(new GeometryFactory());
        assertSame(handler.getShapeType(), ShapeType.POLYGON);

        for (Coordinate coordinate : c) {
            assertTrue(handler.pointInList(coordinate, c));
        }
    }

    @Test
    public void testHoleAssignment() {
        Dimension ps = new Dimension(500, 500);
        PrecisionModel precision = new PrecisionModel();

        ArrayList<LinearRing> shells = new ArrayList<>();
        ArrayList<LinearRing> holes = new ArrayList<>();

        int x = 10;
        int y = 10;

        shells.add(copyTo(x, y, ps.width - 2 * x, ps.height - 2 * y, rectangle(precision, 0)));

        int w = 11;
        int h = 11;
        int s = 10;

        int nx = (ps.width - 2 * x) / (w + s);
        int ny = (ps.height - 2 * y) / (h + s);

        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                holes.add(copyTo(x + s + i * (w + s), y + s + j * (h + s), w, h, rectangle(precision, 0)));
            }
        }

        PolygonHandler ph = new PolygonHandler(new GeometryFactory());
        List<List<LinearRing>> assigned = ph.assignHolesToShells(shells, holes);
        assertEquals(assigned.get(0).size(), holes.size());
    }

    public static LinearRing rectangle(PrecisionModel pm, int SRID) {
        Coordinate[] coords = new Coordinate[5];

        for (int i = 0; i < coords.length; i++) {
            coords[i] = new Coordinate();
        }

        return new GeometryFactory().createLinearRing(coords);
    }

    public static LinearRing copyTo(double x, double y, double w, double h, LinearRing g) {
        if (g.getNumPoints() != 5) {
            throw new IllegalArgumentException("Geometry must have 5 points");
        }

        Coordinate[] coords = g.getCoordinates();
        coords[0].x = x;
        coords[0].y = y;
        coords[1].x = x + w;
        coords[1].y = y;
        coords[2].x = x + w;
        coords[2].y = y + h;
        coords[3].x = x;
        coords[3].y = y + h;
        coords[4].x = x;
        coords[4].y = y;

        return g;
    }
}
