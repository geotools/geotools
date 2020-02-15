/*
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.process.elasticsearch;

import static org.junit.Assert.*;

import org.junit.Test;

public class RasterScaleTest {

    @Test
    public void testRasterScale_noScale() {
        RasterScale scale = new RasterScale();
        assertFalse(scale.isScaleSet());
    }

    @Test
    public void testRasterScale_maxProvided() {
        float scaleMax = 10.0f;
        RasterScale scale = new RasterScale(scaleMax);
        assertTrue(scale.isScaleSet());
        assertEquals(0, scale.getScaleMin(), 0.0);
        assertEquals(scaleMax, scale.getScaleMax(), 0.0);
    }

    @Test
    public void testRasterScale_minMaxProvided() {
        float scaleMax = 10.0f;
        float scaleMin = 1.0f;
        RasterScale scale = new RasterScale(scaleMin, scaleMax);
        assertTrue(scale.isScaleSet());
        assertEquals(scaleMin, scale.getScaleMin(), 0.0);
        assertEquals(scaleMax, scale.getScaleMax(), 0.0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRasterScale_minMaxSame() {
        float scaleMax = 10.0f;
        new RasterScale(scaleMax, scaleMax);
    }

    @Test
    public void testRasterScale_scaleValue() {
        float scaleMax = 10.0f;
        float scaleMin = 0.0f;
        RasterScale scale = new RasterScale(scaleMin, scaleMax);
        scale.prepareScale(30);
        scale.prepareScale(20);
        scale.prepareScale(10);
        scale.prepareScale(40);
        assertEquals(10, scale.scaleValue(40), 0.0);
        assertEquals(5, scale.scaleValue(25), 0.0);
        assertEquals(0, scale.scaleValue(10), 0.0);
    }

    @Test
    public void testRasterScale_scaleValue_emptyScale() {
        RasterScale scale = new RasterScale();
        scale.prepareScale(30);
        scale.prepareScale(20);
        scale.prepareScale(10);
        assertEquals(30, scale.scaleValue(30), 0.0);
        assertEquals(20, scale.scaleValue(20), 0.0);
        assertEquals(10, scale.scaleValue(10), 0.0);
    }

    @Test
    public void testRasterScale_scaleValue_dataMinAndDataMaxAreTheSame() {
        float scaleMax = 10.0f;
        float scaleMin = 1.0f;
        RasterScale scale = new RasterScale(scaleMin, scaleMax);
        scale.prepareScale(30);
        assertEquals(10, scale.scaleValue(30), 0.0);
    }

    @Test
    public void testRasterScale_log() {
        RasterScale scale = new RasterScale(true);
        assertEquals(1, scale.scaleValue(10), 0.0);
        assertEquals(0, scale.scaleValue(1), 0.0);
    }

    @Test
    public void testRasterScale_logAndScale() {
        float scaleMax = 10.0f;
        float scaleMin = 0.0f;
        RasterScale scale = new RasterScale(scaleMin, scaleMax, true);
        scale.prepareScale(10);
        scale.prepareScale(1);
        assertEquals(10, scale.scaleValue(10), 0.0);
        assertEquals(0, scale.scaleValue(1), 0.0);
    }

}
