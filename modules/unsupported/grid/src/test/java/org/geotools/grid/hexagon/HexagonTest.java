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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

import org.geotools.grid.GridElement;


import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Hexagon class.
 *
 * @author mbedward
 * @since 2.7
 * @source $URL$
 * @version $Id$
 */
public class HexagonTest extends HexagonTestBase {

    @Test
    public void getVerticesFlat() {
        double minx = 1.0;
        double miny = -1.0;
        GridElement hexagon = new HexagonImpl(minx, miny, SIDE_LEN, HexagonOrientation.FLAT, null);

        assertVertices(hexagon, minx, miny, SIDE_LEN, HexagonOrientation.FLAT);
    }

    @Test(expected=IllegalArgumentException.class)
    public void badSideLen() throws Exception {
        GridElement h = new HexagonImpl(0.0, 0.0, 0.0, HexagonOrientation.FLAT, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void badOrientation() throws Exception {
        GridElement h = new HexagonImpl(0.0, 0.0, SIDE_LEN, null, null);
    }

    @Test
    public void getOrientation() {
        Hexagon hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.ANGLED, null);
        assertEquals(HexagonOrientation.ANGLED, hexagon.getOrientation());

        hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.FLAT, null);
        assertEquals(HexagonOrientation.FLAT, hexagon.getOrientation());
    }

    @Test
    public void getVerticesAngled() {
        double minx = 1.0;
        double miny = -1.0;
        GridElement hexagon = new HexagonImpl(minx, miny, SIDE_LEN, HexagonOrientation.ANGLED, null);

        assertVertices(hexagon, minx, miny, SIDE_LEN, HexagonOrientation.ANGLED);
    }

    @Test
    public void getCenterFlat() throws Exception {
        GridElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.FLAT, null);
        Coordinate expected = new Coordinate(SIDE_LEN, 0.5 * Math.sqrt(3.0) * SIDE_LEN);
        Coordinate result = hexagon.getCenter();

        assertCoordinate(expected, result);
    }

    @Test
    public void getCenterAngled() {
        GridElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.ANGLED, null);
        Coordinate expected = new Coordinate(0.5 * Math.sqrt(3.0) * SIDE_LEN, SIDE_LEN);
        Coordinate result = hexagon.getCenter();

        assertCoordinate(expected, result);
    }

    @Test
    public void getBoundsFlat() {
        GridElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.FLAT, null);

        Envelope expected = new Envelope(
                0.0,
                2.0 * SIDE_LEN,
                0.0,
                Math.sqrt(3.0) * SIDE_LEN);

        Envelope result = hexagon.getBounds();

        assertEnvelope(expected, result);
    }

    @Test
    public void getBoundsAngled() {
        GridElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.ANGLED, null);

        Envelope expected = new Envelope(
                0.0,
                Math.sqrt(3.0) * SIDE_LEN,
                0.0,
                2.0 * SIDE_LEN);

        Envelope result = hexagon.getBounds();

        assertEnvelope(expected, result);
    }

    @Test
    public void toGeometry() {
        GridElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.FLAT, null);
        Geometry polygon = hexagon.toGeometry();
        assertNotNull(polygon);
        assertTrue(polygon instanceof Polygon);

        Set<Coordinate> polyCoords = new HashSet<Coordinate>(Arrays.asList(polygon.getCoordinates()));
        for (Coordinate c : hexagon.getVertices()) {
            assertTrue(polyCoords.contains(c));
        }
    }

    @Test
    public void toDenseGeometry() {
        GridElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.FLAT, null);

        final int density = 10;
        final double maxSpacing = SIDE_LEN / density;

        Geometry polygon = hexagon.toDenseGeometry(maxSpacing);
        assertNotNull(polygon);
        assertTrue(polygon instanceof Polygon);
        assertTrue(polygon.getCoordinates().length - 1 >= 6 * density);
    }

}
