package org.geotools.mbstyle.parse;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.io.IOException;

import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
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

}
