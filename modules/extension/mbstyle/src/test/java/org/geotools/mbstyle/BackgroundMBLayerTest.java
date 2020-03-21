package org.geotools.mbstyle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.io.IOException;
import org.geotools.mbstyle.layer.BackgroundMBLayer;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.Fill;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

public class BackgroundMBLayerTest {
    BackgroundMBLayer layer;
    MBStyle style;
    BackgroundMBLayer graphicLayer;
    MBStyle graphicStyle;
    private Fill fill;
    private Fill graphicFill;

    @Before
    public void setUp() throws IOException, ParseException {
        JSONObject json = MapboxTestUtils.parseTestStyle("backgroundColorStyleTest.json");
        style = MBStyle.create(json);
        layer = (BackgroundMBLayer) MBStyle.create(json).layer("backgroundcolor");
        fill = layer.getFill(style);
        JSONObject jsonGraphic = MapboxTestUtils.parseTestStyle("backgroundImgStyleTest.json");
        graphicStyle = MBStyle.create(jsonGraphic);
        graphicLayer = (BackgroundMBLayer) MBStyle.create(jsonGraphic).layer("backgroundimg");
        graphicFill = graphicLayer.getFill(style);
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

    @Test
    public void parsedSolidBackground() {
        assertEquals(Color.decode("#00FF00"), fill.getColor().evaluate(null, Color.class));
        assertEquals(0.45, fill.getOpacity().evaluate(null, Double.class), 0d);
        assertNull(fill.getGraphicFill());
    }

    @Test
    public void parsedGraphicBackground() {
        ExternalGraphic eg =
                (ExternalGraphic) graphicFill.getGraphicFill().graphicalSymbols().get(0);
        assertEquals(
                "mapbox://sprites/testuser/ciz48dnnj004l2rplkpqsra81#icon=${strURLEncode('owl')}&size=${strURLEncode('1')}",
                eg.getURI());
    }
}
