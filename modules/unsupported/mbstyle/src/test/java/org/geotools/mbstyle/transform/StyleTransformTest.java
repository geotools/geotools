/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.transform;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.mbstyle.*;
import org.geotools.styling.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test parsing and transforming a Mapbox fill layer from json.
 */
public class StyleTransformTest {

    static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();  

    /**
     * Test parsing a Mapbox fill layer
     */
    @Test
    public void testFill() throws IOException, ParseException {

        JSONObject jsonObject = parseTestStyle("fillStyleTest.json");

        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("geoserver-states");

        assertEquals(1, layers.size());

        // Find the MBFillLayer and assert it contains the correct FeatureTypeStyle.
        assertTrue(layers.get(0) instanceof FillMBLayer);
        FillMBLayer mbFill = (FillMBLayer) layers.get(0);
        FeatureTypeStyle fts = new MBStyleTransformer().transform(mbFill, mbStyle);
        
        PolygonSymbolizer psym = SLD.polySymbolizer(fts);
        
        Expression expr =  psym.getFill().getColor();
        assertNotNull("fillColor set", expr);
        assertEquals( Color.decode("#FF595E"), expr.evaluate(null,Color.class) );
        assertEquals(Double.valueOf(.84),
                psym.getFill().getOpacity().evaluate(null, Double.class));
        
        Expression colorStroke = psym.getStroke().getColor();
        assertNotNull("stroke color set", colorStroke);
        assertEquals(Color.decode("#1982C4"), colorStroke.evaluate(null, Color.class));

        assertNotNull("displacement not null", psym.getDisplacement());
        assertNotNull("displacementX not null",  psym.getDisplacement().getDisplacementX());
        assertNotNull("displacementY not null",  psym.getDisplacement().getDisplacementY());
        assertEquals(Integer.valueOf(20), psym.getDisplacement().getDisplacementX().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(20), psym.getDisplacement().getDisplacementY().evaluate(null, Integer.class));
        
    }

    /**
     * Test parsing and generating a MapBox fill extrusion
     */
    @Test
    public void testFillExtrusion() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("fillExtrusionTest.json");

        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("composite");

        assertEquals(1, layers.size());

        // Find the MBFillLayer and assert it contains the correct FeatureTypeStyle.
        assertTrue(layers.get(0) instanceof FillExtrusionMBLayer);
        FillExtrusionMBLayer mbFill = (FillExtrusionMBLayer) layers.get(0);
        FeatureTypeStyle fts = new MBStyleTransformer().transform(mbFill, mbStyle);

        PolygonSymbolizer psym = SLD.polySymbolizer(fts);

        Expression expr =  psym.getFill().getColor();
        assertNotNull("fillColor set", expr);
        assertEquals( Color.decode("#FF595E"), expr.evaluate(null,Color.class) );
        assertEquals(Double.valueOf(.91),
                psym.getFill().getOpacity().evaluate(null, Double.class));


        assertNotNull("displacement not null", psym.getDisplacement());
        assertNotNull("displacementX not null",  psym.getDisplacement().getDisplacementX());
        assertNotNull("displacementY not null",  psym.getDisplacement().getDisplacementY());
        assertEquals(Integer.valueOf(0), psym.getDisplacement().getDisplacementX().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(30), psym.getDisplacement().getDisplacementY().evaluate(null, Integer.class));
    }

    /**
     * Test parsing a Mapbox fill layer using a sprite fill-pattern
     */
    @Test
    public void testFillSprite() throws IOException, ParseException {

        JSONObject jsonObject = parseTestStyle("fillStyleSpriteTest.json");

        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("geoserver-states");

        assertEquals(1, layers.size());

        // Find the MBFillLayer and assert it contains the correct FeatureTypeStyle.
        assertTrue(layers.get(0) instanceof FillMBLayer);
        FillMBLayer mbFill = (FillMBLayer) layers.get(0);
        FeatureTypeStyle fts = new MBStyleTransformer().transform(mbFill, mbStyle);
        
        PolygonSymbolizer psym = SLD.polySymbolizer(fts);
        Graphic g = psym.getFill().getGraphicFill();
        assertNotNull(g);
        assertNotNull(g.graphicalSymbols());
        assertEquals(1, g.graphicalSymbols().size());
        
    }

    /**
     * Test parsing a Mapbox raster layer
     */
    @Test
    public void testRaster() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("rasterStyleTest.json");

        // Find the MBFillLayer and assert it contains the correct FeatureTypeStyle.

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("geoserver-raster");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof RasterMBLayer);
        RasterMBLayer mbFill = (RasterMBLayer) layers.get(0);

        FeatureTypeStyle fts = new MBStyleTransformer().transform(mbFill);

        assertEquals(1, fts.rules().size());
        Rule r = fts.rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof RasterSymbolizer);
        RasterSymbolizer rsym = (RasterSymbolizer) symbolizer;

        assertEquals(Double.valueOf(.59), rsym.getOpacity().evaluate(null, Double.class));
    }

    /**
     * Test parsing a Mapbox line layer
     */
    @Test
    public void testLine() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("lineStyleTest.json");

        // Find the LineMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof LineMBLayer);
        LineMBLayer mbLine = (LineMBLayer) layers.get(0);

        FeatureTypeStyle fts = new MBStyleTransformer().transform(mbLine);

        assertEquals(1, fts.rules().size());
        Rule r = fts.rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof LineSymbolizer);
        LineSymbolizer lsym = (LineSymbolizer) symbolizer;

        assertEquals(new Color(0, 153, 255),
                lsym.getStroke().getColor().evaluate(null, Color.class));
        assertEquals("square", lsym.getStroke().getLineCap().evaluate(null, String.class));
        assertEquals("round", lsym.getStroke().getLineJoin().evaluate(null, String.class));
        assertEquals(.5d, lsym.getStroke().getOpacity().evaluate(null, Double.class), .0001);
        assertEquals(Integer.valueOf(10), lsym.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(4), lsym.getPerpendicularOffset().evaluate(null, Integer.class));
               
        List<Integer> expectedDashes = Arrays.asList(10, 5, 3, 2);
        assertEquals(expectedDashes.size(), lsym.getStroke().dashArray().size());
        for (int i = 0; i < expectedDashes.size(); i++) {
            Integer n = (Integer) lsym.getStroke().dashArray().get(i).evaluate(null, Integer.class);
            assertEquals(expectedDashes.get(i), n);
        }        
    }
    
    @Test
    public void testLineDefaults() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("lineStyleTestEmpty.json");

        // Find the LineMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof LineMBLayer);
        LineMBLayer mbLine = (LineMBLayer) layers.get(0);

        FeatureTypeStyle fts = new MBStyleTransformer().transform(mbLine);

        assertEquals(1, fts.rules().size());
        Rule r = fts.rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof LineSymbolizer);
        LineSymbolizer lsym = (LineSymbolizer) symbolizer;
        
        assertEquals(Color.BLACK, lsym.getStroke().getColor().evaluate(null, Color.class));
        assertEquals("butt", lsym.getStroke().getLineCap().evaluate(null, String.class));
        assertEquals("mitre", lsym.getStroke().getLineJoin().evaluate(null, String.class));
        assertEquals(Integer.valueOf(1), lsym.getStroke().getOpacity().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(1), lsym.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(0), lsym.getPerpendicularOffset().evaluate(null, Integer.class));
        assertTrue(lsym.getStroke().dashArray() == null);        
    } 
    
    @Test
    public void testCircle() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("circleStyleTest.json");

        // Find the CircleMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof CircleMBLayer);
        CircleMBLayer mbCircle = (CircleMBLayer) layers.get(0);

        FeatureTypeStyle fts = new MBStyleTransformer().transform(mbCircle);

        assertEquals(1, fts.rules().size());
        Rule r = fts.rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof PointSymbolizer);
        PointSymbolizer psym = (PointSymbolizer) symbolizer;

        assertTrue(psym.getGraphic() != null);
        
        assertEquals(Integer.valueOf(30), psym.getGraphic().getSize().evaluate(null, Integer.class));

        assertNotNull(psym.getGraphic().getDisplacement());
        assertEquals(Integer.valueOf(10), psym.getGraphic().getDisplacement().getDisplacementX().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(15),psym.getGraphic().getDisplacement().getDisplacementY().evaluate(null, Integer.class));

        assertEquals(1, psym.getGraphic().graphicalSymbols().size());
        
        GraphicalSymbol gs = psym.getGraphic().graphicalSymbols().get(0);
        assertTrue(gs instanceof Mark);
        Mark m = (Mark) gs;


        assertNotNull(m.getFill());
        assertEquals(Color.RED, m.getFill().getColor().evaluate(null, Color.class));
        assertEquals(Double.valueOf(.5), m.getFill().getOpacity().evaluate(null, Double.class),
                .0001);

        assertNotNull(m.getStroke());
        assertEquals(Color.GREEN, m.getStroke().getColor().evaluate(null, Color.class));
        assertEquals(Integer.valueOf(2), m.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(Double.valueOf(.75), m.getStroke().getOpacity().evaluate(null, Double.class),
                .0001);

    }

    @Test
    public void testCircleDefaults() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("circleStyleTestDefaults.json");

        // Find the CircleMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof CircleMBLayer);
        CircleMBLayer mbCircle = (CircleMBLayer) layers.get(0);

        FeatureTypeStyle fts = new MBStyleTransformer().transform(mbCircle);

        assertEquals(1, fts.rules().size());
        Rule r = fts.rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof PointSymbolizer);
        PointSymbolizer psym = (PointSymbolizer) symbolizer;

        assertTrue(psym.getGraphic() != null);
        
        assertEquals(Integer.valueOf(10), psym.getGraphic().getSize().evaluate(null, Integer.class));

        if (psym.getGraphic().getDisplacement() != null) {
            assertEquals(Integer.valueOf(0), psym.getGraphic().getDisplacement().getDisplacementX()
                    .evaluate(null, Integer.class));
            assertEquals(Integer.valueOf(0), psym.getGraphic().getDisplacement().getDisplacementY()
                    .evaluate(null, Integer.class));
        }

        assertEquals(1, psym.getGraphic().graphicalSymbols().size());

        GraphicalSymbol gs = psym.getGraphic().graphicalSymbols().get(0);
        assertTrue(gs instanceof Mark);
        Mark m = (Mark) gs;

        assertNotNull(m.getFill());
        assertEquals(Color.BLACK, m.getFill().getColor().evaluate(null, Color.class));
        assertEquals(Integer.valueOf(1), m.getFill().getOpacity().evaluate(null, Integer.class));

        assertNotNull(m.getStroke());
        assertEquals(Color.BLACK, m.getStroke().getColor().evaluate(null, Color.class));
        assertEquals(Integer.valueOf(0), m.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(1), m.getStroke().getOpacity().evaluate(null, Integer.class));

    }
    
    @Test
    public void testBackgroundColor() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("backgroundColorStyleTest.json");
        
        // Find the CircleMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof BackgroundMBLayer);
        FeatureTypeStyle fts = new MBStyleTransformer().transform(layers.get(0), mbStyle);

        assertEquals(1, fts.rules().size());
        Rule r = fts.rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof PolygonSymbolizer);
        PolygonSymbolizer psym = (PolygonSymbolizer) symbolizer;
        
        assertEquals(Color.GREEN, psym.getFill().getColor().evaluate(null, Color.class));
        assertEquals(Double.valueOf(.45), psym.getFill().getOpacity().evaluate(null, Double.class), .0001);
        
        assertNull(psym.getStroke());
    }
    
    @Test
    public void testBackgroundDefaults() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("backgroundStyleTestDefault.json");
        
        // Find the CircleMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof BackgroundMBLayer);
        FeatureTypeStyle fts = new MBStyleTransformer().transform(layers.get(0), mbStyle);

        assertEquals(1, fts.rules().size());
        Rule r = fts.rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof PolygonSymbolizer);
        PolygonSymbolizer psym = (PolygonSymbolizer) symbolizer;
        
        assertEquals(Color.BLACK, psym.getFill().getColor().evaluate(null, Color.class));
        assertEquals(Integer.valueOf(1), psym.getFill().getOpacity().evaluate(null, Integer.class));
        
        assertNull(psym.getStroke());
    }

    @Test
    public void testSymbolIcon() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolStyleIconTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("composite");

        MBLayer layer = mbStyle.layer("earthquakes");
        assertNotNull("earthquakes available", layer );
        assertTrue(layer instanceof SymbolMBLayer);
        
        FeatureTypeStyle fts = new MBStyleTransformer().transform(layer, mbStyle);
        
        assertEquals(1, fts.rules().size());
        Rule r = fts.rules().get(0);
        assertEquals(2, r.symbolizers().size());
        
        PointSymbolizer pointSymbolizer = (PointSymbolizer) r.symbolizers().get(0);
        TextSymbolizer textSymbolizer = (TextSymbolizer) r.symbolizers().get(1);
        Graphic graphic = pointSymbolizer.getGraphic();
        ExternalGraphic externalGraphic = (ExternalGraphic) graphic.graphicalSymbols().get(0);
        assertEquals( "mbsprite", externalGraphic.getFormat() );
        
        
        PointPlacement placement = (PointPlacement) textSymbolizer.getLabelPlacement();
        AnchorPoint anchor = placement.getAnchorPoint();
        
        assertEquals( "bottom-right x", 1.0,
                anchor.getAnchorPointX().evaluate(null,Double.class), 0.0 );
        assertEquals( "bottom-right y", 0.0,
                anchor.getAnchorPointY().evaluate(null,Double.class), 0.0 );
    }
    
    /**
     * 
     * Read a test Mapbox Style file (json) and parse it into a {@link JSONObject}.
     */
    private JSONObject parseTestStyle(String filename) throws IOException, ParseException {
        return MapboxTestUtils.parseTestStyle(filename);
    }
}
