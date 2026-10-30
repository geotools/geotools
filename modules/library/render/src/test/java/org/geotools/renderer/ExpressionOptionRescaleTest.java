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
package org.geotools.renderer;

import static org.geotools.api.style.TextSymbolizer.SPACE_AROUND_KEY;
import static org.junit.Assert.assertEquals;

import java.awt.Color;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.measure.Units;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.visitor.DpiRescaleStyleVisitor;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.geotools.styling.visitor.RescaleStyleVisitor;
import org.geotools.styling.visitor.UomRescaleStyleVisitor;
import org.junit.Test;
import si.uom.SI;

/**
 * Checks that the rescaling style visitors keep a <code>${cql}</code> vendor option an expression, evaluated per
 * feature by the renderer, and multiply it by their scale. Each test rescales a spaceAround of ${size} by 2 and reads
 * it for a feature with size 5.
 */
public class ExpressionOptionRescaleTest {

    @Test
    public void testUomRescaleMeters() throws Exception {
        assertEquals(10, rescaledSpaceAround(new UomRescaleStyleVisitor(2), SI.METRE));
    }

    @Test
    public void testUomRescalePixels() throws Exception {
        assertEquals(5, rescaledSpaceAround(new UomRescaleStyleVisitor(2), Units.PIXEL));
    }

    /** With pixels the unit of measure rescaling leaves the expression as written. */
    @Test
    public void testUomRescalePixelsUnchanged() throws Exception {
        TextSymbolizer rescaled = rescale(new UomRescaleStyleVisitor(2), Units.PIXEL, "${size}");
        assertEquals("${size}", rescaled.getOptions().get(SPACE_AROUND_KEY));
    }

    @Test
    public void testDpiRescale() throws Exception {
        assertEquals(10, rescaledSpaceAround(new DpiRescaleStyleVisitor(2), Units.PIXEL));
    }

    /** Options in real world units are left to the unit of measure rescaling, which already includes the DPI. */
    @Test
    public void testDpiRescaleMeters() throws Exception {
        assertEquals(5, rescaledSpaceAround(new DpiRescaleStyleVisitor(2), SI.METRE));
    }

    @Test
    public void testRescale() throws Exception {
        assertEquals(10, rescaledSpaceAround(new RescaleStyleVisitor(2), Units.PIXEL));
    }

    /** A pretty printed SLD keeps the line breaks around the vendor option value. */
    @Test
    public void testRescaleSurroundingWhitespace() throws Exception {
        assertEquals(10, rescaledSpaceAround(new RescaleStyleVisitor(2), Units.PIXEL, "\n    ${size}\n  "));
    }

    private int rescaledSpaceAround(DuplicatingStyleVisitor visitor, Unit<Length> uom) throws Exception {
        return rescaledSpaceAround(visitor, uom, "${size}");
    }

    private int rescaledSpaceAround(DuplicatingStyleVisitor visitor, Unit<Length> uom, String spaceAround)
            throws Exception {
        TextSymbolizer rescaled = rescale(visitor, uom, spaceAround);
        SimpleFeatureType type = DataUtilities.createType("test", "label:String,size:Integer");
        SimpleFeature feature = SimpleFeatureBuilder.build(type, new Object[] {"a", 5}, null);
        return new VendorOptionParser().getIntOption(rescaled, SPACE_AROUND_KEY, 0, feature);
    }

    private TextSymbolizer rescale(DuplicatingStyleVisitor visitor, Unit<Length> uom, String spaceAround) {
        StyleBuilder sb = new StyleBuilder();
        TextSymbolizer text = sb.createTextSymbolizer(Color.BLACK, sb.createFont("Serif", 10), "label");
        text.setUnitOfMeasure(uom);
        text.getOptions().put(SPACE_AROUND_KEY, spaceAround);
        text.accept(visitor);
        return (TextSymbolizer) visitor.getCopy();
    }
}
