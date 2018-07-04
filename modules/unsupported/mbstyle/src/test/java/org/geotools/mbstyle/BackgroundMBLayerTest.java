package org.geotools.mbstyle;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import org.geotools.mbstyle.layer.BackgroundMBLayer;
import org.geotools.styling.FeatureTypeStyle;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

public class BackgroundMBLayerTest {
    BackgroundMBLayer layer;
    MBStyle style;
    List<FeatureTypeStyle> FTS;
    BackgroundMBLayer graphicLayer;
    MBStyle graphicStyle;
    List<FeatureTypeStyle> graphicFTS;

    @Before
    public void setUp() throws IOException, ParseException {
        JSONObject json = MapboxTestUtils.parseTestStyle("backgroundColorStyleTest.json");
        style = MBStyle.create(json);
        layer = (BackgroundMBLayer) MBStyle.create(json).layer("backgroundcolor");
        FTS = layer.transformInternal(style);
        JSONObject jsonGraphic = MapboxTestUtils.parseTestStyle("backgroundImgStyleTest.json");
        graphicStyle = MBStyle.create(jsonGraphic);
        graphicLayer = (BackgroundMBLayer) MBStyle.create(jsonGraphic).layer("backgroundimg");
        graphicFTS = layer.transformInternal(style);
    }

    @Test
    public void backgroundColorTest() {
        assertEquals(.45, layer.getBackgroundOpacity());
        assertEquals(Color.decode("#00FF00"), layer.getBackgroundColor());
    }

    @Test
    public void backgroundGraphicTest() {
        assertEquals(.75, graphicLayer.getBackgroundOpacity());
        assertEquals(Color.decode("#0000FF"), graphicLayer.getBackgroundColor());
        assertEquals("owl", graphicLayer.getBackgroundPattern());
    }
}
