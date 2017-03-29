/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.process.elasticsearch;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class RasterScaleTest {

    @Test
    public void testRasterScale_noScale() throws Exception {
        RasterScale scale = new RasterScale(null);
        assertFalse(scale.isScaleSet());
    }
    
    @Test
    public void testRasterScale_emptyRange() throws Exception {
        RasterScale scale = new RasterScale(ImmutableList.of());
        assertFalse(scale.isScaleSet());
    }
    
    @Test
    public void testRasterScale_maxProvided() throws Exception {
        float scaleMax = 10.0f;
        RasterScale scale = new RasterScale(ImmutableList.of(scaleMax));
        assertTrue(scale.isScaleSet());
        assertEquals(0, scale.getScaleMin(), 0.0);
        assertEquals(scaleMax, scale.getScaleMax(), 0.0);
    }
    
    @Test
    public void testRasterScale_minMaxProvided() throws Exception {
        float scaleMax = 10.0f;
        float scaleMin = 1.0f;
        RasterScale scale = new RasterScale(ImmutableList.of(scaleMax, scaleMin));
        assertTrue(scale.isScaleSet());
        assertEquals(scaleMin, scale.getScaleMin(), 0.0);
        assertEquals(scaleMax, scale.getScaleMax(), 0.0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testRasterScale_minMaxSame() throws Exception {
        float scaleMax = 10.0f;
        float scaleMin = scaleMax;
        new RasterScale(ImmutableList.of(scaleMax, scaleMin));
    }
    
    @Test
    public void testRasterScale_scaleValue() throws Exception {
        float scaleMax = 10.0f;
        float scaleMin = 0.0f;
        RasterScale scale = new RasterScale(ImmutableList.of(scaleMax, scaleMin));
        scale.prepareScale(30);
        scale.prepareScale(20);
        scale.prepareScale(10);
        assertEquals(10, scale.scaleValue(30), 0.0);
        assertEquals(5, scale.scaleValue(20), 0.0);
        assertEquals(0, scale.scaleValue(10), 0.0);
    }
    
    @Test
    public void testRasterScale_scaleValue_emptyScale() throws Exception {
        RasterScale scale = new RasterScale(null);
        scale.prepareScale(30);
        scale.prepareScale(20);
        scale.prepareScale(10);
        assertEquals(30, scale.scaleValue(30), 0.0);
        assertEquals(20, scale.scaleValue(20), 0.0);
        assertEquals(10, scale.scaleValue(10), 0.0);
    }
    
    @Test
    public void testRasterScale_scaleValue_dataMinAndDataMaxAreTheSame() throws Exception {
        float scaleMax = 10.0f;
        float scaleMin = 1.0f;
        RasterScale scale = new RasterScale(ImmutableList.of(scaleMax, scaleMin));
        scale.prepareScale(30);
        assertEquals(10, scale.scaleValue(30), 0.0);
    }
}
