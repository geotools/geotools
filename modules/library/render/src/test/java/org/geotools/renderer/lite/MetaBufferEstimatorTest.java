package org.geotools.renderer.lite;

import static org.junit.Assert.*;

import org.geotools.styling.Style;
import org.junit.Test;

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
        assertEquals(8, estimator.getBuffer());
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

}
