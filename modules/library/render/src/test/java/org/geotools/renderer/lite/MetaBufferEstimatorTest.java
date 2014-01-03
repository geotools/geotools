package org.geotools.renderer.lite;

import static org.junit.Assert.*;

import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.junit.Test;
import org.opengis.filter.expression.NilExpression;

public class MetaBufferEstimatorTest {

    @Test
    public void testExternalGraphic() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "externalGraphic.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(48, estimator.getBuffer());
    }
    
    @Test
    public void testExternalGraphicRectangleResized() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "externalGraphicRectImage.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        // 32x64 image to size 16x32 should give the max. of width/height
        assertEquals(32, estimator.getBuffer());
    }
    
    @Test
    public void testExternalGraphicNoSize() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "externalGraphicNoSize.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(64, estimator.getBuffer());
    }
    
    @Test
    public void testMark() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "markCircle.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(32, estimator.getBuffer());
    }
    
    @Test
    public void testThinLine() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "lineGray.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(1, estimator.getBuffer());
    }
    
    @Test
    public void testThickLine() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "lineThick.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(4, estimator.getBuffer());
    }

    @Test
    public void testGraphicStroke() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "lineRailway.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(10, estimator.getBuffer());
    }
    
    @Test
    public void testPolygon() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "polygon.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(1, estimator.getBuffer());
    }
    
    @Test
    public void testLabelShields() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textLabelShield.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(32, estimator.getBuffer());
    }
    
    @Test
    public void testDynamicSize() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "externalGraphicDynamicSize.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertFalse(estimator.isEstimateAccurate());
    }
    
    @Test
    public void testInlineContent() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "base64.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(16, estimator.getBuffer());
    }
    
    @Test
    public void testMarkNoSizeNoStroke() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        Mark mark = sb.createMark("square");
        mark.setStroke(null);
        Graphic graphic = sb.createGraphic(null, mark, null);
        graphic.setSize(NilExpression.NIL);
        PointSymbolizer ps = sb.createPointSymbolizer(graphic);
        Style style = sb.createStyle(ps);
        
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(16, estimator.getBuffer());
    }
    
    @Test
    public void testMarkStroke() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        Mark mark = sb.createMark("square");
        mark.getStroke().setWidth(sb.getFilterFactory().literal(10));
        Graphic graphic = sb.createGraphic(null, mark, null);
        graphic.setSize(NilExpression.NIL);
        PointSymbolizer ps = sb.createPointSymbolizer(graphic);
        Style style = sb.createStyle(ps);
        
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(26, estimator.getBuffer());
    }
    
    @Test
    public void testNullStroke() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        LineSymbolizer ls = sb.createLineSymbolizer(Stroke.NULL);
        Style style = sb.createStyle(ls);
        
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(0, estimator.getBuffer());
    }

}
