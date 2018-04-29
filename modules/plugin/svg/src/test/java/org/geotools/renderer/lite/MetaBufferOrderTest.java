package org.geotools.renderer.lite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.geotools.styling.Style;
import org.junit.Test;

public class MetaBufferOrderTest {

    @Test
    public void testPngSvg() throws IOException {
        Style style = RendererBaseTest.loadStyle(this, "pointPngSvg.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(32, estimator.getBuffer());
    }

    @Test
    public void testSvgPng() throws IOException {
        Style style = RendererBaseTest.loadStyle(this, "pointSvgPng.sld");
        MetaBufferEstimator estimator = new MetaBufferEstimator();
        style.accept(estimator);
        assertTrue(estimator.isEstimateAccurate());
        assertEquals(32, estimator.getBuffer());
    }
}
