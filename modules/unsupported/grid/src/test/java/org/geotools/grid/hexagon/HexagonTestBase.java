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

import org.geotools.grid.Orientation;
import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.grid.GridElement;
import org.geotools.grid.TestBase;

import static org.junit.Assert.*;

/**
 * Base class for Hexagon unit tests.
 *
 * @author mbedward
 * @since 2.7
 * @source $URL$
 * @version $Id$
 */
public class HexagonTestBase extends TestBase {
    
    protected final double SIDE_LEN = 1.0;
    protected final double AREA = Math.sqrt(3.0) * 1.5;

    protected void assertVertices(GridElement hexagon,
            double minx, double miny, double sideLen, Orientation orientation) {

        assertTrue("error in test code", hexagon instanceof Hexagon);

        // expected results
        final double span = Math.sqrt(3.0) * sideLen;
        Coordinate[] expected = new Coordinate[6];

        if (orientation == Orientation.FLAT) {
            expected[0] = new Coordinate(minx + 0.5 * sideLen, miny + span);
            expected[1] = new Coordinate(minx + 1.5 * sideLen, miny + span);
            expected[2] = new Coordinate(minx + 2.0 * sideLen, miny + 0.5 * span);
            expected[3] = new Coordinate(minx + 1.5 * sideLen, miny);
            expected[4] = new Coordinate(minx + 0.5 * sideLen, miny);
            expected[5] = new Coordinate(minx, miny + span/2.0);
        } else {
            expected[0] = new Coordinate(minx + 0.5 * span, miny + 2.0 * sideLen);
            expected[1] = new Coordinate(minx + span, miny + 1.5 * sideLen);
            expected[2] = new Coordinate(minx + span, miny + 0.5 * sideLen);
            expected[3] = new Coordinate(minx + 0.5 * span, miny);
            expected[4] = new Coordinate(minx, miny + 0.5 * sideLen);
            expected[5] = new Coordinate(minx, miny + 1.5 * sideLen);
        }

        Coordinate[] vertices = hexagon.getVertices();
        assertEquals(6, vertices.length);

        for (int i = 0; i < vertices.length; i++) {
            assertCoordinate(expected[i], vertices[i]);
        }
    }

}
