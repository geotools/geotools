package org.geotools.mbstyle.parse;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;

import org.geotools.mbstyle.BackgroundMBLayer;
import org.geotools.mbstyle.FillMBLayer;
import org.geotools.mbstyle.MBLayer;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.RasterMBLayer;
import org.geotools.mbstyle.source.RasterMBSource;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class StyleParseTests {

    /**
     * Verify that the root properties for a Mapbox Style are parsed correctly.
     */
    @Test
    public void testParseRootProperties() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("rootPropertyTest.json");
        MBStyle mapboxStyle = new MBStyle(jsonObject);

        assertEquals(29, mapboxStyle.getBearing().intValue());
        assertEquals(new Point2D.Double(-73.9749, 40.7736), mapboxStyle.getCenter());
        assertEquals("mapbox://fonts/test/{fontstack}/{range}.pbf", mapboxStyle.getGlyphs());
        assertEquals("rootPropertyTest", mapboxStyle.getName());
        assertEquals(50, mapboxStyle.getPitch().intValue());
        assertEquals("mapbox://sprites/test/ciym62qtf005d2rnopgu725qo", mapboxStyle.getSprite());
        assertEquals(12.5, mapboxStyle.getZoom().doubleValue(), .00001);
        assertNotNull(mapboxStyle.getMetadata());
    }

    /**
     * Verify that the default root properties for a Mapbox Style are provided correctly.
     */
    @Test
    public void testParseRootPropertyDefaults() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("rootPropertyTestDefaults.json");
        MBStyle mapboxStyle = new MBStyle(jsonObject);

        assertEquals(0, mapboxStyle.getBearing().intValue());
        assertNull(mapboxStyle.getCenter());
        assertNull(mapboxStyle.getGlyphs());
        assertNull(mapboxStyle.getName());
        assertEquals(0, mapboxStyle.getPitch().intValue());
        assertNull(mapboxStyle.getSprite());
        assertNull(mapboxStyle.getZoom());
    }

    private <T extends MBLayer> T getSingleLayerOfType(MBStyle s, Class<T> clazz) {
        assertEquals(1, s.layers().size());
        assertTrue(clazz.isInstance(s.layers().get(0)));
        return clazz.cast(s.layers().get(0));
    }

    /**
     * Verify that fill layer properties are parsed correctly.
     */
    @Test
    public void testParseFillLayer() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("fillStyleTest.json");
        MBStyle s = new MBStyle(jsonObject);
        FillMBLayer l = getSingleLayerOfType(s, FillMBLayer.class);
        assertEquals("fill", l.getType());
        assertEquals("testid", l.getId());
        assertEquals("geoserver-states", l.getSource());
        assertEquals(new Color(0xFF595E), l.getFillColor());
        assertEquals(0.84, l.getFillOpacity().doubleValue(), .0001);
        assertEquals(2, l.getFillTranslate().length);
        assertEquals(20.0, l.getFillTranslate()[0], .0001);
        assertEquals(20.0, l.getFillTranslate()[1], .0001);
        assertEquals(new Color(0x1982C4), l.getFillOutlineColor());
    }

    /**
     * Verify that raster layer properties are parsed correctly.
     */
    @Test
    public void testParseRasterLayer() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("rasterStyleTest.json");
        MBStyle s = new MBStyle(jsonObject);
        RasterMBLayer l = getSingleLayerOfType(s, RasterMBLayer.class);
        assertEquals("raster", l.getType());
        assertEquals("testid", l.getId());
        assertEquals("geoserver-raster", l.getSource());
        assertEquals(.59, l.getOpacity().doubleValue(), .00001);
        assertEquals(.69, l.getBrightnessMax().doubleValue(), .00001);
        assertEquals(.28, l.getBrightnessMin().doubleValue(), .00001);
        assertEquals(.94, l.getContrast().doubleValue(), .00001);
        assertEquals(250.0, l.getFadeDuration().doubleValue(), .00001);
        assertEquals(30.0, l.getHueRotate().doubleValue(), .00001);
        assertEquals(0.9, l.getSaturation().doubleValue(), .00001);
    }
    

    /**
     * Verify that background layer properties are parsed correctly.
     */
    @Test
    public void testParseBackgroundLayer()  throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("backgroundColorStyleTest.json");
        MBStyle s = new MBStyle(jsonObject);
        BackgroundMBLayer l = getSingleLayerOfType(s, BackgroundMBLayer.class);
        assertEquals(new Color(0x00FF00),  l.getBackgroundColor());
        assertEquals(0.45,  l.getBackgroundOpacity().doubleValue(), .00001);
        assertNull(l.getBackgroundPattern());
    }

}
