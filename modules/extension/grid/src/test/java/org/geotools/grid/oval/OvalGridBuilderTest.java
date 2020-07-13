/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid.oval;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.Neighbor;
import org.geotools.grid.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;

/** Unit tests for the OvalGridBuilder class. */
public class OvalGridBuilderTest extends TestBase {

    private static final ReferencedEnvelope bounds = new ReferencedEnvelope(0, 100, 0, 100, null);
    private static final double WIDTH = 10;
    private static final double HEIGHT = 5;

    private OvalBuilder gridBuilder;

    @Before
    public void setup() {
        gridBuilder = new OvalBuilder(bounds, WIDTH, HEIGHT);
    }

    @Test
    public void createNeighbor() {
        Oval neighbor = null;

        class Shift {

            double dx;
            double dy;

            public Shift(double dx, double dy) {
                this.dx = dx;
                this.dy = dy;
            }
        }

        Map<Neighbor, Shift> shifts = new HashMap<Neighbor, Shift>();
        shifts.put(Neighbor.LOWER, new Shift(0.0, -HEIGHT));
        shifts.put(Neighbor.LOWER_LEFT, new Shift(-WIDTH, -HEIGHT));
        shifts.put(Neighbor.LOWER_RIGHT, new Shift(WIDTH, -HEIGHT));
        shifts.put(Neighbor.LEFT, new Shift(-WIDTH, 0.0));
        shifts.put(Neighbor.RIGHT, new Shift(WIDTH, 0.0));
        shifts.put(Neighbor.UPPER, new Shift(0.0, HEIGHT));
        shifts.put(Neighbor.UPPER_LEFT, new Shift(-WIDTH, HEIGHT));
        shifts.put(Neighbor.UPPER_RIGHT, new Shift(WIDTH, HEIGHT));

        Oval oval = Ovals.create(0.0, 0.0, WIDTH, HEIGHT, null);

        for (Neighbor n : Neighbor.values()) {
            neighbor = gridBuilder.createNeighbor(oval, n);

            Shift shift = shifts.get(n);
            assertNotNull("Error in test code", shift);
            assertNeighbor(oval, neighbor, shift.dx, shift.dy);
        }
    }

    private void assertNeighbor(Oval refEl, Oval neighbor, double dx, double dy) {
        Coordinate[] refCoords = refEl.getVertices();
        Coordinate[] neighborCoords = neighbor.getVertices();

        for (int i = 0; i < refCoords.length; i++) {
            refCoords[i].x += dx;
            refCoords[i].y += dy;
            assertCoordinate(refCoords[i], neighborCoords[i]);
        }
    }
}
