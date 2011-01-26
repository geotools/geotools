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
package org.geotools.renderer.lite;

import java.awt.Color;
import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;

import org.geotools.styling.Graphic;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.Symbolizer;
import org.geotools.test.TestData;
import org.junit.Test;

public class RenderingBufferExtractorTest {
    StyleBuilder sb = new StyleBuilder();

    @Test
    public void testNoStroke() {
        Style style = sb.createStyle(sb.createTextSymbolizer());
        MetaBufferEstimator rbe = new MetaBufferEstimator();
        assertEquals(0, rbe.getBuffer());
        assertTrue(rbe.isEstimateAccurate());
        rbe.visit(style);
        assertEquals(0, rbe.getBuffer());
        assertTrue(rbe.isEstimateAccurate());
    }

    @Test
    public void testSimpleStroke() {
        Style style = sb.createStyle(sb.createLineSymbolizer(sb.createStroke(10.0)));
        MetaBufferEstimator rbe = new MetaBufferEstimator();
        rbe.visit(style);
        assertEquals(10, rbe.getBuffer());
        assertTrue(rbe.isEstimateAccurate());
    }

    @Test
    public void testSimpleGraphic() {
        PointSymbolizer ps = sb.createPointSymbolizer(sb.createGraphic(null, sb
                .createMark(sb.MARK_CIRCLE), null));
        ps.getGraphic().setSize(sb.literalExpression(15));
        Style style = sb.createStyle(ps);

        MetaBufferEstimator rbe = new MetaBufferEstimator();
        rbe.visit(style);
        assertEquals(15, rbe.getBuffer());
        assertTrue(rbe.isEstimateAccurate());
    }
    
    @Test
    public void testReachableExternalGraphic() {
        URL resource = TestData.getResource(this, "draw.png");
        PointSymbolizer ps = sb.createPointSymbolizer(sb.createGraphic(null, null, sb
                .createExternalGraphic(resource, "image/png")));
        ps.getGraphic().setSize(sb.literalExpression(null));
        Style style = sb.createStyle(ps);

        MetaBufferEstimator rbe = new MetaBufferEstimator();
        rbe.visit(style);
        assertEquals(24, rbe.getBuffer());
        assertTrue(rbe.isEstimateAccurate());
    }
    
    @Test
    public void testUnreachableExternalGraphic() throws Exception {
        File file = new File(TestData.getResource(this, "draw.png").toURI());
        URL resource = new File(file.getParent(), "draw-not-there.png").toURI().toURL();
        PointSymbolizer ps = sb.createPointSymbolizer(sb.createGraphic(null, null, sb
                .createExternalGraphic(resource, "image/png")));
        ps.getGraphic().setSize(sb.literalExpression(null));
        Style style = sb.createStyle(ps);

        MetaBufferEstimator rbe = new MetaBufferEstimator();
        rbe.visit(style);
        assertEquals(0, rbe.getBuffer());
        assertFalse(rbe.isEstimateAccurate());
    }

    @Test
    public void testNonIntegerStroke() {
        Style style = sb.createStyle(sb.createLineSymbolizer(sb.createStroke(10.8)));
        MetaBufferEstimator rbe = new MetaBufferEstimator();
        rbe.visit(style);
        assertEquals(11, rbe.getBuffer());
        assertTrue(rbe.isEstimateAccurate());
    }

    @Test
    public void testMultiSymbolizers() {
        Symbolizer ls = sb.createLineSymbolizer(sb.createStroke(10.8));
        Symbolizer ps = sb.createPolygonSymbolizer(sb.createStroke(12), sb.createFill());
        Rule r = sb.createRule(new Symbolizer[] { ls, ps });
        MetaBufferEstimator rbe = new MetaBufferEstimator();
        rbe.visit(r);
        assertEquals(12, rbe.getBuffer());
        assertTrue(rbe.isEstimateAccurate());
    }

    @Test
    public void testPropertyWidth() {
        Symbolizer ls = sb.createLineSymbolizer(sb.createStroke(sb.colorExpression(Color.BLACK), sb
                .attributeExpression("gimbo")));
        Symbolizer ps = sb.createPolygonSymbolizer(sb.createStroke(12), sb.createFill());
        Rule r = sb.createRule(new Symbolizer[] { ls, ps });
        MetaBufferEstimator rbe = new MetaBufferEstimator();
        rbe.visit(r);
        assertEquals(12, rbe.getBuffer());
        assertTrue(!rbe.isEstimateAccurate());
    }

    @Test
    public void testLiteralParseStroke() {
        Style style = sb.createStyle(sb.createLineSymbolizer(sb.createStroke(sb
                .colorExpression(Color.BLACK), sb.literalExpression("10.0"))));
        MetaBufferEstimator rbe = new MetaBufferEstimator();
        rbe.visit(style);
        assertEquals(10, rbe.getBuffer());
        assertTrue(rbe.isEstimateAccurate());
    }
    
    @Test
    public void testNpePreventionStroke() {
        Style style = sb.createStyle(sb.createLineSymbolizer(sb.createStroke(sb
                .colorExpression(Color.BLACK), sb.literalExpression(null))));
        MetaBufferEstimator rbe = new MetaBufferEstimator();
        rbe.visit(style);
        assertEquals(1, rbe.getBuffer());
        assertTrue(rbe.isEstimateAccurate());
    }
    
    @Test
    public void testLiteralParseGraphics() {
        Graphic g = sb.createGraphic();
        g.setSize(sb.literalExpression("10.0"));
        MetaBufferEstimator rbe = new MetaBufferEstimator();
        rbe.visit(g);
        assertEquals(10, rbe.getBuffer());
        assertTrue(rbe.isEstimateAccurate());
    }
}
