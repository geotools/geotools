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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.geotools.grid.GridElement;

import org.geotools.grid.Orientation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Hexagon class.
 *
 * @author mbedward
 * @since 2.7
 *
 * @source $URL$
 * @version $Id$
 */
public class HexagonTest extends HexagonTestBase {

    @Test
    public void getVerticesFlat() {
        double minx = 1.0;
        double miny = -1.0;
        GridElement hexagon = new HexagonImpl(minx, miny, SIDE_LEN, Orientation.FLAT, null);

        assertVertices(hexagon, minx, miny, SIDE_LEN, Orientation.FLAT);
    }

    @Test(expected=IllegalArgumentException.class)
    public void badSideLen() throws Exception {
        GridElement h = new HexagonImpl(0.0, 0.0, 0.0, Orientation.FLAT, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void badOrientation() throws Exception {
        GridElement h = new HexagonImpl(0.0, 0.0, SIDE_LEN, null, null);
    }

    @Test
    public void getOrientation() {
        Hexagon hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, Orientation.ANGLED, null);
        assertEquals(Orientation.ANGLED, hexagon.getOrientation());

        hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, Orientation.FLAT, null);
        assertEquals(Orientation.FLAT, hexagon.getOrientation());
    }

    @Test
    public void getVerticesAngled() {
        double minx = 1.0;
        double miny = -1.0;
        GridElement hexagon = new HexagonImpl(minx, miny, SIDE_LEN, Orientation.ANGLED, null);

        assertVertices(hexagon, minx, miny, SIDE_LEN, Orientation.ANGLED);
    }

    @Test
    public void getCenterFlat() throws Exception {
        GridElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, Orientation.FLAT, null);
        Coordinate expected = new Coordinate(SIDE_LEN, 0.5 * Math.sqrt(3.0) * SIDE_LEN);
        Coordinate result = hexagon.getCenter();

        assertCoordinate(expected, result);
    }

    @Test
    public void getCenterAngled() {
        GridElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, Orientation.ANGLED, null);
        Coordinate expected = new Coordinate(0.5 * Math.sqrt(3.0) * SIDE_LEN, SIDE_LEN);
        Coordinate result = hexagon.getCenter();

        assertCoordinate(expected, result);
    }

    @Test
    public void getBoundsFlat() {
        GridElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, Orientation.FLAT, null);

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
        GridElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, Orientation.ANGLED, null);

        Envelope expected = new Envelope(
                0.0,
                Math.sqrt(3.0) * SIDE_LEN,
                0.0,
                2.0 * SIDE_LEN);

        Envelope result = hexagon.getBounds();

        assertEnvelope(expected, result);
    }

    @Test
    public void toPolygon() {
        GridElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, Orientation.FLAT, null);
        Geometry polygon = hexagon.toPolygon();
        assertNotNull(polygon);
        assertTrue(polygon instanceof Polygon);

        Set<Coordinate> polyCoords = new HashSet<Coordinate>(Arrays.asList(polygon.getCoordinates()));
        for (Coordinate c : hexagon.getVertices()) {
            assertTrue(polyCoords.contains(c));
        }
    }

    @Test
    public void toDensePolygon() {
        GridElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, Orientation.FLAT, null);

        final int density = 10;
        final double maxSpacing = SIDE_LEN / density;

        Geometry polygon = hexagon.toDensePolygon(maxSpacing);
        assertNotNull(polygon);
        assertTrue(polygon instanceof Polygon);
        assertTrue(polygon.getCoordinates().length - 1 >= 6 * density);
    }

}
