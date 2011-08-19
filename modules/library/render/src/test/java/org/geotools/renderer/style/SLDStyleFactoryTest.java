/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.renderer.style.SLDStyleFactory.SymbolizerKey;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.NumberRange;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 *
 * @author jamesm
 *
 * @source $URL$
 */
public class SLDStyleFactoryTest extends TestCase {
    
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    SLDStyleFactory sld = new SLDStyleFactory();
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    NumberRange<Integer> range = NumberRange.create(1,1);
    
    SimpleFeatureType featureType;
    SimpleFeature feature;
    
    @Override
    protected void setUp() throws Exception {
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("test");
        ftb.add("geom", Point.class);
        ftb.add("symb", String.class);
        ftb.add("icon", String.class);
        
        featureType = ftb.buildFeatureType();
        
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(featureType);
        fb.set("geom", new GeometryFactory().createPoint(new Coordinate(0, 0)));
        fb.set("symb", "0xF054");
        fb.set("icon", "draw.png");
        feature = fb.buildFeature(null);
    }
    
    @Override
    protected void tearDown() throws Exception {
        sld.setVectorRenderingEnabled(false);
    }
    
    /** This test created from Render2DTest.testSimplePointRender */
    public void testCreatePointStyle(){
        // The following is complex, and should be built from
        // an SLD document and not by hand
        PointSymbolizer pointsym = sf.createPointSymbolizer();
        pointsym.setGraphic(sf.getDefaultGraphic());
        NumberRange<Double> scaleRange = NumberRange.create( 1.0,50000.0);
        Style2D style = sld.createPointStyle(null, pointsym, scaleRange);
        assertNotNull( style );
    }

    /**
     * Test of createPolygonStyle method, of class org.geotools.renderer.style.SLDStyleFactory.
     */
    public void testCreateIncompletePolygonStyle() {
        PolygonSymbolizer symb;
        
        //full symbolizer
        symb = sf.createPolygonSymbolizer();
        sld.createPolygonStyle(null, symb,range);
    }
    
    /**
     * Test of createPointStyle method, of class org.geotools.renderer.style.SLDStyleFactory.
     */
    public void testCreateCompletePointStyle() {
        PointSymbolizer symb;
        Mark myMark;
        //full symbolizer
        symb = sf.createPointSymbolizer();
        myMark = sf.createMark();
        
        myMark.setFill(sf.createFill(ff.literal("#ffff00")));
        symb.getGraphic().setSize(ff.literal(10));
        symb.getGraphic().addMark(myMark);
        symb.getGraphic().setOpacity(ff.literal(1));
        symb.getGraphic().setRotation(ff.literal(0));
        sld.createPointStyle(null, symb,range);
        
    }
    
    public void testCreateIncompletePointStyle() {
        PointSymbolizer symb;
        Mark myMark;
        //full symbolizer
        symb = sf.createPointSymbolizer();
        myMark = sf.createMark();
        
        symb.getGraphic().addMark(myMark);
        
        sld.createPointStyle(null, symb,range);
    }
    
    public void testCreateDynamicMark() throws Exception {
        PointSymbolizer symb = sf.createPointSymbolizer();
        Mark myMark = sf.createMark();
        final String ttfUrl = "ttf://Serif#${symb}";
        myMark.setWellKnownName(ff.literal(ttfUrl));
        symb.getGraphic().addMark(myMark);
        
        MarkStyle2D ms = (MarkStyle2D) sld.createStyle(feature, symb, range);
        assertNotNull(ms.getShape());
        // make sure the style has been recognized as dynamic
        SymbolizerKey key = new SymbolizerKey(symb, range);
        assertTrue(sld.dynamicSymbolizers.containsKey(key));
        Shape expected = new TTFMarkFactory().getShape(null, ff.literal("ttf://Serif#0xF054"), feature);
        
        // no general path equality implemented, we have to check manually
        PathIterator piExpected = expected.getPathIterator(new AffineTransform());
        PathIterator pi = ms.getShape().getPathIterator(new AffineTransform());
        double[] coordsExpected = new double[2];
        double[] coords = new double[2];
        assertEquals(piExpected.getWindingRule(), pi.getWindingRule());
        while(!piExpected.isDone()) {
            assertFalse(pi.isDone());
            piExpected.currentSegment(coordsExpected);
            pi.currentSegment(coords);
            assertEquals(coordsExpected[0], coords[0], 0.00001);
            assertEquals(coordsExpected[1], coords[1], 0.00001);
            piExpected.next();
            pi.next();
        }
        assertTrue(pi.isDone());
    }
    
    public void testCreateDynamicExternalGraphics() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/");
        PointSymbolizer symb = sf.createPointSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "${icon}", "image/png");
        symb.getGraphic().addExternalGraphic(eg);
        
        GraphicStyle2D gs = (GraphicStyle2D) sld.createStyle(feature, symb, range);
        // make sure the style has been recognized as dynamic
        SymbolizerKey key = new SymbolizerKey(symb, range);
        assertTrue(sld.dynamicSymbolizers.containsKey(key));

        BufferedImage img = gs.getImage();
        BufferedImage expected = ImageIO.read(StreamingRenderer.class.getResource("test-data/draw.png"));
        assertEquals(expected.getHeight(), img.getHeight());
        assertEquals(expected.getWidth(), img.getWidth());
        // the two images are equal, but they have different color models due to the
        // different ways they have been loaded
    }
    
    public void testCreateDynamicExternalGraphicsVector() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/");
        PointSymbolizer symb = sf.createPointSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "${icon}", "image/png");
        symb.getGraphic().addExternalGraphic(eg);
        sld.setVectorRenderingEnabled(true);
        
        GraphicStyle2D gs = (GraphicStyle2D) sld.createStyle(feature, symb, range);
        // make sure the style has been recognized as dynamic
        SymbolizerKey key = new SymbolizerKey(symb, range);
        assertTrue(sld.dynamicSymbolizers.containsKey(key));

        BufferedImage expected = ImageIO.read(StreamingRenderer.class.getResource("test-data/draw.png"));
        assertEquals(expected.getHeight(), gs.getImage().getHeight());
        assertEquals(expected.getWidth(), gs.getImage().getWidth());
    }
    
    public void testDefaultSizeExternalGraphic() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/");
        PointSymbolizer symb = sf.createPointSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "icon64.png", "image/png");
        symb.getGraphic().addExternalGraphic(eg);
        
        GraphicStyle2D gs = (GraphicStyle2D) sld.createPointStyle(feature, symb, range);
        BufferedImage img = gs.getImage();
        assertEquals(64, img.getHeight());
        assertEquals(64, img.getWidth());
    }
    
    public void testCreateDynamicExternalFormat() throws Exception {
    	feature.setAttribute("symb", "image/png");
        URL url = StreamingRenderer.class.getResource("test-data/");
        PointSymbolizer symb = sf.createPointSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "${icon}", "${symb}");
        symb.getGraphic().addExternalGraphic(eg);
        
        GraphicStyle2D gs = (GraphicStyle2D) sld.createStyle(feature, symb, range);
        // make sure the style has been recognized as dynamic
        SymbolizerKey key = new SymbolizerKey(symb, range);
        assertTrue(sld.dynamicSymbolizers.containsKey(key));

        BufferedImage img = gs.getImage();
        BufferedImage expected = ImageIO.read(StreamingRenderer.class.getResource("test-data/draw.png"));
        assertEquals(expected.getHeight(), img.getHeight());
        assertEquals(expected.getWidth(), img.getWidth());
        // the two images are equal, but they have different color models due to the
        // different ways they have been loaded
    }

    public void testResizeExternalGraphic() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/");
        PointSymbolizer symb = sf.createPointSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "icon64.png", "image/png");
        symb.getGraphic().graphicalSymbols().add(eg);
        symb.getGraphic().setSize(ff.literal(20));
        
        GraphicStyle2D gs = (GraphicStyle2D) sld.createPointStyle(feature, symb, range);
        BufferedImage img = gs.getImage();
        assertEquals(20, img.getHeight());
        assertEquals(20, img.getWidth());
    }
    
    public void testResizeGraphicFill() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/");
        PolygonSymbolizer symb = sf.createPolygonSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "icon64.png", "image/png");
        Graphic g = sf.createGraphic(new ExternalGraphic[] {eg}, null, null, null, ff.literal(20), null);
        Fill fill = sf.createFill(null, null, null, g);
        symb.setFill(fill);
        
        PolygonStyle2D ps = sld.createPolygonStyle(feature, symb, range);
        assertTrue(ps.getFill() instanceof TexturePaint);
        TexturePaint paint = (TexturePaint) ps.getFill();
        assertEquals(20, paint.getImage().getWidth());
    }
    
    public void testDefaultSizeMark() throws Exception {
        PointSymbolizer symb = sf.createPointSymbolizer();
        Mark myMark = sf.createMark();
        myMark.setWellKnownName(ff.literal("square"));
        symb.getGraphic().addMark(myMark);
        
        MarkStyle2D ms = (MarkStyle2D) sld.createPointStyle(feature, symb, range);
        assertEquals(16, ms.getSize());
    }
    public void testDefaultLineSymbolizerWithColor() throws Exception {
        LineSymbolizer symb = sf.createLineSymbolizer();
        symb.setStroke( sf.createStroke( ff.literal("#0000FF"), ff.literal(1.0)));
        
        Style2D s = sld.createLineStyle( feature, symb, range );
        assertNotNull( s );
        
        DynamicLineStyle2D s2 = (DynamicLineStyle2D) sld.createDynamicLineStyle( feature, symb, range );
        assertNotNull( s2 );
        Stroke stroke = s2.getStroke();
        assertEquals( Color.BLUE, s2.getContour() );
        assertNotNull( s2.getStroke() );
    }
    
    public void testTexturePaintNoSize() throws Exception {
        PolygonSymbolizer symb = sf.createPolygonSymbolizer();
        Mark myMark = sf.createMark();
        myMark.setWellKnownName(ff.literal("square"));
        org.geotools.styling.Fill fill = sf.createFill(null);
        fill.setGraphicFill(sf.createGraphic(null, new Mark[] {myMark}, null, null, null, null));
        symb.setFill(fill); 
        
        PolygonStyle2D ps = (PolygonStyle2D) sld.createPolygonStyle(feature, symb, range);
        assertTrue(ps.getFill() instanceof TexturePaint);
    }
    
    public void testUnknownFont() throws Exception {
    	TextSymbolizer ts = sf.createTextSymbolizer();
    	ts.setFill(sf.createFill(null));
    	Font font = sf.createFont(ff.literal("notExistingFont"), ff.literal("italic"), ff.literal("bold"), ff.literal(20));
    	ts.setFont(font);
    	
    	TextStyle2D tsd = (TextStyle2D) sld.createTextStyle(feature, ts, range);
    	assertEquals(20, tsd.getFont().getSize());
    	assertEquals(java.awt.Font.ITALIC | java.awt.Font.BOLD, tsd.getFont().getStyle());
    	assertEquals("Serif", tsd.getFont().getName());
    }
}
