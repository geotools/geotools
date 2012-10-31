/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid.hexagon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.Neighbor;
import org.geotools.grid.PolygonBuilder;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Unit tests for the HexagonGridBuilder class.
 *
 * @author mbedward
 * @since 2.7
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class HexagonGridBuilderTest extends HexagonTestBase {
    
    private final ReferencedEnvelope bounds = new ReferencedEnvelope(0, 10, 0, 10, null);
    private HexagonBuilder angledBuilder;
    private HexagonBuilder flatBuilder;

    @Before
    public void setup() {
        angledBuilder = new HexagonBuilder(bounds, SIDE_LEN, HexagonOrientation.ANGLED);
        flatBuilder = new HexagonBuilder(bounds, SIDE_LEN, HexagonOrientation.FLAT);
    }

    @Test
    public void validNeighborPosition() {
        class Case {
            HexagonOrientation o;
            Neighbor n;
            boolean valid;

            public Case(HexagonOrientation o, Neighbor n, boolean valid) {
                this.o = o;
                this.n = n;
                this.valid = valid;
            }
        }

        Case[] cases = {
            new Case(HexagonOrientation.ANGLED, Neighbor.LEFT, true),
            new Case(HexagonOrientation.ANGLED, Neighbor.LOWER, false),
            new Case(HexagonOrientation.ANGLED, Neighbor.LOWER_LEFT, true),
            new Case(HexagonOrientation.ANGLED, Neighbor.LOWER_RIGHT, true),
            new Case(HexagonOrientation.ANGLED, Neighbor.RIGHT, true),
            new Case(HexagonOrientation.ANGLED, Neighbor.UPPER, false),
            new Case(HexagonOrientation.ANGLED, Neighbor.UPPER_LEFT, true),
            new Case(HexagonOrientation.ANGLED, Neighbor.UPPER_RIGHT, true),

            new Case(HexagonOrientation.FLAT, Neighbor.LEFT, false),
            new Case(HexagonOrientation.FLAT, Neighbor.LOWER, true),
            new Case(HexagonOrientation.FLAT, Neighbor.LOWER_LEFT, true),
            new Case(HexagonOrientation.FLAT, Neighbor.LOWER_RIGHT, true),
            new Case(HexagonOrientation.FLAT, Neighbor.RIGHT, false),
            new Case(HexagonOrientation.FLAT, Neighbor.UPPER, true),
            new Case(HexagonOrientation.FLAT, Neighbor.UPPER_LEFT, true),
            new Case(HexagonOrientation.FLAT, Neighbor.UPPER_RIGHT, true),
        };

        for (Case c : cases) {
            if (c.o == HexagonOrientation.ANGLED) {
                assertEquals("Failed for case: " + c.o + " " + c.n,
                        c.valid, angledBuilder.isValidNeighbor(c.n));
            } else {
                assertEquals("Failed for case: " + c.o + " " + c.n,
                        c.valid, flatBuilder.isValidNeighbor(c.n));
            }
        }
    }

    @Test
    public void createNeighbor() {
        Hexagon hn = null;

        class Shift {
            double dx;
            double dy;

            public Shift(double dx, double dy) {
                this.dx = dx;
                this.dy = dy;
            }
        }

        final double MAJOR = 2.0 * SIDE_LEN;
        final double MINOR = Math.sqrt(3.0) * SIDE_LEN;

        Map<Neighbor, Shift> flatShifts = new HashMap<Neighbor, Shift>();
        flatShifts.put(Neighbor.LOWER, new Shift(0.0, -MINOR));
        flatShifts.put(Neighbor.LOWER_LEFT, new Shift(-0.75 * MAJOR, -0.5 * MINOR));
        flatShifts.put(Neighbor.LOWER_RIGHT, new Shift(0.75 * MAJOR, -0.5 * MINOR));
        flatShifts.put(Neighbor.UPPER, new Shift(0.0, MINOR));
        flatShifts.put(Neighbor.UPPER_LEFT, new Shift(-0.75 * MAJOR, 0.5 * MINOR));
        flatShifts.put(Neighbor.UPPER_RIGHT, new Shift(0.75 * MAJOR, 0.5 * MINOR));

        Map<Neighbor, Shift> angledShifts = new HashMap<Neighbor, Shift>();
        angledShifts.put(Neighbor.LEFT, new Shift(-MINOR, 0.0));
        angledShifts.put(Neighbor.LOWER_LEFT, new Shift(-0.5 * MINOR, -0.75 * MAJOR));
        angledShifts.put(Neighbor.LOWER_RIGHT, new Shift(0.5 * MINOR, -0.75 * MAJOR));
        angledShifts.put(Neighbor.RIGHT, new Shift(MINOR, 0.0));
        angledShifts.put(Neighbor.UPPER_LEFT, new Shift(-0.5 * MINOR, 0.75 * MAJOR));
        angledShifts.put(Neighbor.UPPER_RIGHT, new Shift(0.5 * MINOR, 0.75 * MAJOR));

        Map<HexagonOrientation, Map<Neighbor, Shift>> table = new HashMap<HexagonOrientation, Map<Neighbor, Shift>>();
        table.put(HexagonOrientation.FLAT, flatShifts);
        table.put(HexagonOrientation.ANGLED, angledShifts);

        for (HexagonOrientation o : HexagonOrientation.values()) {
            PolygonBuilder gridBuilder =
                    o == HexagonOrientation.ANGLED ? angledBuilder : flatBuilder;

            Hexagon h0 = Hexagons.create(0.0, 0.0, SIDE_LEN, o, null);

            for (Neighbor n : Neighbor.values()) {
                boolean expectEx = !gridBuilder.isValidNeighbor(n);
                boolean gotEx = false;
                try {
                    hn = (Hexagon) gridBuilder.createNeighbor(h0, n);
                } catch (IllegalArgumentException ex) {
                    gotEx = true;
                }

                assertEquals("Failed for case " + o + " " + n, expectEx, gotEx);

                if (!gotEx) {
                    Shift shift = table.get(o).get(n);
                    assertNotNull("Error in test code", shift);
                    assertNeighborVertices(h0, hn, shift.dx, shift.dy);
                }
            }
        }
    }

    private void assertNeighborVertices(Hexagon h0, Hexagon h1, double dx, double dy) {
        Coordinate[] expected = h0.getVertices();
        Coordinate[] result = h1.getVertices();
        for (int i = 0; i < 6; i++) {
            expected[i].x += dx;
            expected[i].y += dy;
            assertCoordinate(expected[i], result[i]);
        }

    }
}
