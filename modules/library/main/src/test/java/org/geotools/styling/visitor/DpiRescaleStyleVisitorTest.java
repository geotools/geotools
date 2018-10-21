/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.visitor;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.TextSymbolizer;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import si.uom.SI;
import systems.uom.common.USCustomary;

/** Unit test for DpiRescaleStyleVisitor. */
public class DpiRescaleStyleVisitorTest {
    StyleBuilder sb;

    StyleFactory sf;

    FilterFactory2 ff;

    DpiRescaleStyleVisitor visitor;

    double scale;

    @Before
    public void setUp() throws Exception {
        sf = CommonFactoryFinder.getStyleFactory(null);
        ff = CommonFactoryFinder.getFilterFactory2(null);
        sb = new StyleBuilder(sf, ff);
        scale = 2.0;
        visitor = new DpiRescaleStyleVisitor(scale);
    }

    @Test
    public void testNoUnit() throws Exception {
        Stroke original = sb.createStroke(Color.RED, 2, new float[] {5, 10});
        LineSymbolizer ls = sb.createLineSymbolizer(original);
        ls.accept(visitor);
        Stroke clone = ((LineSymbolizer) visitor.getCopy()).getStroke();

        assertEquals(4.0d, clone.getWidth().evaluate(null, Double.class), 0d);
        assertEquals(10.0f, clone.getDashArray()[0], 0f);
        assertEquals(20.0f, clone.getDashArray()[1], 0f);

        TextSymbolizer ts = sb.createTextSymbolizer();
        ts.getOptions().put(TextSymbolizer.SPACE_AROUND_KEY, "10");
        ts.accept(visitor);
        TextSymbolizer clonedTs = (TextSymbolizer) visitor.getCopy();
        assertEquals("20.0", clonedTs.getOptions().get(TextSymbolizer.SPACE_AROUND_KEY));
    }

    @Test
    public void testAllMeters() throws Exception {
        Stroke original = sb.createStroke(Color.RED, 2, new float[] {5, 10});
        LineSymbolizer ls = sb.createLineSymbolizer(original);
        ls.setUnitOfMeasure(SI.METRE);
        ls.accept(visitor);
        Stroke clone = ((LineSymbolizer) visitor.getCopy()).getStroke();

        assertEquals(2d, clone.getWidth().evaluate(null, Double.class), 0d);
        assertEquals(5f, clone.getDashArray()[0], 0f);
        assertEquals(10f, clone.getDashArray()[1], 0f);

        TextSymbolizer ts = sb.createTextSymbolizer();
        ts.getOptions().put(TextSymbolizer.SPACE_AROUND_KEY, "10");
        ts.setUnitOfMeasure(SI.METRE);
        ts.accept(visitor);
        TextSymbolizer clonedTs = (TextSymbolizer) visitor.getCopy();
        assertEquals("10.0", clonedTs.getOptions().get(TextSymbolizer.SPACE_AROUND_KEY));
    }

    @Test
    public void testAllFeet() throws Exception {
        Stroke original = sb.createStroke(Color.RED, 2, new float[] {5, 10});
        LineSymbolizer ls = sb.createLineSymbolizer(original);
        ls.setUnitOfMeasure(USCustomary.FOOT);
        ls.accept(visitor);
        Stroke clone = ((LineSymbolizer) visitor.getCopy()).getStroke();

        assertEquals(2d, clone.getWidth().evaluate(null, Double.class), 0d);
        assertEquals(5f, clone.getDashArray()[0], 0f);
        assertEquals(10f, clone.getDashArray()[1], 0f);

        TextSymbolizer ts = sb.createTextSymbolizer();
        ts.getOptions().put(TextSymbolizer.SPACE_AROUND_KEY, "10");
        ts.setUnitOfMeasure(USCustomary.FOOT);
        ts.accept(visitor);
        TextSymbolizer clonedTs = (TextSymbolizer) visitor.getCopy();
        assertEquals("10.0", clonedTs.getOptions().get(TextSymbolizer.SPACE_AROUND_KEY));
    }

    @Test
    public void testSymbolizerMeterOverrideInPixels() throws Exception {
        Stroke original = sb.createStroke(Color.RED, 2, new float[] {5, 10});
        original.setWidth(ff.literal("2px"));
        LineSymbolizer ls = sb.createLineSymbolizer(original);
        ls.setUnitOfMeasure(SI.METRE);
        ls.accept(visitor);
        Stroke clone = ((LineSymbolizer) visitor.getCopy()).getStroke();

        // this one has been rescaled
        assertEquals(4d, clone.getWidth().evaluate(null, Double.class), 0d);
        // the dash array did not, it's supposed to be meters
        assertEquals(5f, clone.getDashArray()[0], 0f);
        assertEquals(10f, clone.getDashArray()[1], 0f);

        TextSymbolizer ts = sb.createTextSymbolizer();
        ts.getOptions().put(TextSymbolizer.SPACE_AROUND_KEY, "10px");
        ts.setUnitOfMeasure(SI.METRE);
        ts.accept(visitor);
        TextSymbolizer clonedTs = (TextSymbolizer) visitor.getCopy();
        // this one has been rescaled
        assertEquals("20.0", clonedTs.getOptions().get(TextSymbolizer.SPACE_AROUND_KEY));
    }

    @Test
    public void testSymbolizerPixelOverrideInMeters() throws Exception {
        Stroke original = sb.createStroke(Color.RED, 2, new float[] {5, 10});
        original.setWidth(ff.literal("2m"));
        LineSymbolizer ls = sb.createLineSymbolizer(original);
        ls.accept(visitor);
        Stroke clone = ((LineSymbolizer) visitor.getCopy()).getStroke();

        // this one has not been rescaled
        assertEquals("2m", clone.getWidth().evaluate(null, String.class));
        // the dash array did , it's supposed to be pixels
        assertEquals(10f, clone.getDashArray()[0], 0f);
        assertEquals(20f, clone.getDashArray()[1], 0f);

        TextSymbolizer ts = sb.createTextSymbolizer();
        ts.getOptions().put(TextSymbolizer.SPACE_AROUND_KEY, "10m");
        ts.accept(visitor);
        TextSymbolizer clonedTs = (TextSymbolizer) visitor.getCopy();
        // this one has not been rescaled
        assertEquals("10.0m", clonedTs.getOptions().get(TextSymbolizer.SPACE_AROUND_KEY));
    }
}
