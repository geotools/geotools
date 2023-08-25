/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011 - 2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.NilExpression;
import org.geotools.data.DataTestCase;
import org.geotools.filter.function.EnvFunction;
import org.geotools.styling.*;
import org.junit.Test;

public class MetaBufferEstimatorTest extends DataTestCase {

    @Test
    public void testExternalGraphic() throws Exception {
        StyleImpl style = RendererBaseTest.loadStyle(this, "externalGraphic.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(48, estimator.getBuffer());
    }

    @Test
    public void testExternalGraphicRectangleResized() throws Exception {
        StyleImpl style = RendererBaseTest.loadStyle(this, "externalGraphicRectImage.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        // 32x64 image to size 16x32 should give the max. of width/height
        assertEquals(32, estimator.getBuffer());
    }

    @Test
    public void testExternalGraphicNoSize() throws Exception {
        StyleImpl style = RendererBaseTest.loadStyle(this, "externalGraphicNoSize.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(64, estimator.getBuffer());
    }

    @Test
    public void testMark() throws Exception {
        StyleImpl style = RendererBaseTest.loadStyle(this, "markCircle.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(32, estimator.getBuffer());
    }

    @Test
    public void testThinLine() throws Exception {
        StyleImpl style = RendererBaseTest.loadStyle(this, "lineGray.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(1, estimator.getBuffer());
    }

    @Test
    public void testThickLine() throws Exception {
        StyleImpl style = RendererBaseTest.loadStyle(this, "lineThick.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(4, estimator.getBuffer());
    }

    @Test
    public void testGraphicStroke() throws Exception {
        StyleImpl style = RendererBaseTest.loadStyle(this, "lineRailway.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(10, estimator.getBuffer());
    }

    @Test
    public void testPolygon() throws Exception {
        StyleImpl style = RendererBaseTest.loadStyle(this, "polygon.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(1, estimator.getBuffer());
    }

    @Test
    public void testLabelShields() throws Exception {
        StyleImpl style = RendererBaseTest.loadStyle(this, "textLabelShield.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(32, estimator.getBuffer());
    }

    @Test
    public void testDynamicSize() throws Exception {
        StyleImpl style = RendererBaseTest.loadStyle(this, "externalGraphicDynamicSize.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertFalse(estimator.isEstimateAccurate());
    }

    @Test
    public void testInlineContent() throws Exception {
        StyleImpl style = RendererBaseTest.loadStyle(this, "base64.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(16, estimator.getBuffer());
    }

    @Test
    public void testMarkNoSizeNoStroke() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        MarkImpl mark = sb.createMark("square");
        mark.setStroke(null);
        GraphicImpl graphic = sb.createGraphic(null, mark, null);
        graphic.setSize(NilExpression.NIL);
        PointSymbolizerImpl ps = sb.createPointSymbolizer(graphic);
        StyleImpl style = sb.createStyle(ps);

        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(16, estimator.getBuffer());
    }

    @Test
    public void testMarkStroke() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        MarkImpl mark = sb.createMark("square");
        mark.getStroke().setWidth(sb.getFilterFactory().literal(10));
        GraphicImpl graphic = sb.createGraphic(null, mark, null);
        graphic.setSize(NilExpression.NIL);
        PointSymbolizerImpl ps = sb.createPointSymbolizer(graphic);
        StyleImpl style = sb.createStyle(ps);

        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(26, estimator.getBuffer());
    }

    @Test
    public void testNullStroke() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        LineSymbolizerImpl ls = sb.createLineSymbolizer(ConstantStroke.NULL);
        StyleImpl style = sb.createStyle(ls);

        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(0, estimator.getBuffer());
    }

    @Test
    public void testGraphicSizeFunction() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        MarkImpl mark = sb.createMark("square");
        mark.setStroke(null);
        GraphicImpl graphic = sb.createGraphic(null, mark, null);
        FilterFactory ff = sb.getFilterFactory();
        graphic.setSize(ff.function("env", ff.literal("test")));
        PointSymbolizerImpl ps = sb.createPointSymbolizer(graphic);
        StyleImpl style = sb.createStyle(ps);

        try {
            EnvFunction.setGlobalValue("test", 10);
            MetaBufferEstimator estimator = new MetaBufferEstimator();
            style.accept(estimator);
            assertTrue(estimator.isEstimateAccurate());
            assertEquals(10, estimator.getBuffer());
        } finally {
            EnvFunction.clearGlobalValues();
        }
    }

    @Test
    public void testMultiScript() throws Exception {
        StyleImpl style = RendererBaseTest.loadStyle(this, "multiscript/textMultiScriptLine.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(16, estimator.getBuffer());
    }

    @Test
    public void testFeatureBound() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        LineSymbolizerImpl ls = sb.createLineSymbolizer(Color.BLUE);
        ls.getStroke().setWidth(ff.multiply(ff.literal(2), ff.property("flow")));
        StyleImpl style = sb.createStyle(ls);

        MetaBufferEstimator estimator = new MetaBufferEstimator(riverFeatures[0]);
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(9, estimator.getBuffer());
    }
}
