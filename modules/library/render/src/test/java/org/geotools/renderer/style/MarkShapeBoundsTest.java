/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style;

import static org.junit.Assert.assertEquals;

import java.awt.geom.Rectangle2D;
import org.geotools.api.filter.FilterFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;

/** Marks with explicit bounds report a unit square centered on the origin, where they are drawn. */
public class MarkShapeBoundsTest {

    private static final Rectangle2D UNIT_SQUARE = new Rectangle2D.Double(-0.5, -0.5, 1, 1);

    private final FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    @Test
    public void testWellKnownBounds() throws Exception {
        WellKnownMarkFactory factory = new WellKnownMarkFactory();
        for (String name : new String[] {"cross", "X", "star", "triangle", "arrow", "hatch"}) {
            assertEquals(name, UNIT_SQUARE, bounds(factory, name));
        }
    }

    @Test
    public void testQGISBounds() throws Exception {
        QGISMarkFactory factory = new QGISMarkFactory();
        String[] names = {
            "triangle",
            "equilateral_triangle",
            "star",
            "cross2",
            "arrow",
            "diamond",
            "pentagon",
            "rectangle",
            "regular_star",
            "arrowhead",
            "filled_arrowhead",
            "crossfill",
            "diagonalhalfsquare",
            "HalfSquare",
            "hexagon",
            "lefthalftriangle",
            "righthalftriangle",
            "quartercircle",
            "semicircle",
            "thirdcircle",
            "quartersquare"
        };
        for (String name : names) {
            assertEquals(name, UNIT_SQUARE, bounds(factory, "qgis://" + name));
        }
    }

    @Test
    public void testDotBounds() throws Exception {
        assertEquals(UNIT_SQUARE, bounds(new ShapeMarkFactory(), "shape://dot"));
    }

    @Test
    public void testTTFBounds() throws Exception {
        assertEquals(UNIT_SQUARE, bounds(new TTFMarkFactory(), "ttf://Serif#56"));
    }

    private Rectangle2D bounds(MarkFactory factory, String name) throws Exception {
        return factory.getShape(null, ff.literal(name), null).getBounds2D();
    }
}
