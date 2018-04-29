/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import static org.junit.Assert.*;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.renderer.style.ShapeMarkFactory;
import org.geotools.renderer.style.WellKnownMarkFactory;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

public class LineFillerTest {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    @Test
    public void testSlash() throws Exception {
        ParallelLinesFiller filler =
                ParallelLinesFiller.fromStipple(
                        new ShapeMarkFactory().getShape(null, ff.literal("shape://slash"), null));
        assertNotNull(filler);
        assertEquals(1, filler.lines.size());
        assertEquals(1, filler.xStep, 0d);
        assertEquals(1, filler.yStep, 0d);
    }

    @Test
    public void testTimes() throws Exception {
        ParallelLinesFiller filler =
                ParallelLinesFiller.fromStipple(
                        new ShapeMarkFactory().getShape(null, ff.literal("shape://times"), null));
        assertNotNull(filler);
        assertEquals(2, filler.lines.size());
        assertEquals(1, filler.xStep, 0d);
        assertEquals(1, filler.yStep, 0d);
    }

    @Test
    public void testPlus() throws Exception {
        ParallelLinesFiller filler =
                ParallelLinesFiller.fromStipple(
                        new ShapeMarkFactory().getShape(null, ff.literal("shape://plus"), null));
        assertNotNull(filler);
        assertEquals(2, filler.lines.size());
        assertEquals(1, filler.xStep, 0d);
        assertEquals(1, filler.yStep, 0d);
    }

    @Test
    public void testOArrow() throws Exception {
        ParallelLinesFiller filler =
                ParallelLinesFiller.fromStipple(
                        new ShapeMarkFactory().getShape(null, ff.literal("shape://oarrow"), null));
        assertNull(filler);
    }

    @Test
    public void testCircle() throws Exception {
        ParallelLinesFiller filler =
                ParallelLinesFiller.fromStipple(
                        new WellKnownMarkFactory().getShape(null, ff.literal("circle"), null));
        assertNull(filler);
    }
}
