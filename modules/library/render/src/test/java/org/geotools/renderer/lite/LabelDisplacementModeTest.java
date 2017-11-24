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
package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.style.FontCache;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import junit.framework.TestCase;

/**
 * Test  class for verifying behaviour of displacementMode vendor option
 * for both single layer and multi-layer map
 * 
 * @author nprigour
 *
 * @source $URL$
 */
public class LabelDisplacementModeTest extends TestCase {

    private static final long TIME = 10000;
    SimpleFeatureSource fs;
    SimpleFeatureSource fs2;
    ReferencedEnvelope bounds;

    @Override
    protected void setUp() throws Exception {
        // register a cross platform test
        FontCache.getDefaultInstance().registerFont(
                Font.createFont(Font.TRUETYPE_FONT, TestData.getResource(this, "Vera.ttf")
                        .openStream()));

        bounds = new ReferencedEnvelope(0, 10, 0, 10, null);
        
        // System.setProperty("org.geotools.test.interactive", "true");
        
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.add("geom", Point.class);
        builder.add("label", String.class);
        builder.setName("labelDisplacement");
        SimpleFeatureType type = builder.buildFeatureType();
        
        GeometryFactory gf = new GeometryFactory();
        SimpleFeature f1 = SimpleFeatureBuilder.build(type, new Object[]{gf.createPoint(new Coordinate(5, 8)), "labelDisplacement1"}, null);
        SimpleFeature f2 = SimpleFeatureBuilder
                .build(type, new Object[] { gf.createPoint(new Coordinate(5, 8.2)),
                        "labelDisplacement1.1" }, null);
        SimpleFeature f3 = SimpleFeatureBuilder.build(type,
                new Object[] { gf.createPoint(new Coordinate(5, 2)),
                        "labelDisplacement2" }, null);
        SimpleFeature f4 = SimpleFeatureBuilder.build(type,
                new Object[] { gf.createPoint(new Coordinate(5, 3)),
                        "labelDisplacement2.1" }, null);
        
        MemoryDataStore data = new MemoryDataStore();
        data.addFeature(f1);
        data.addFeature(f2);
        data.addFeature(f3);
        data.addFeature(f4);
        fs = data.getFeatureSource("labelDisplacement");
        
        
        SimpleFeatureTypeBuilder builder2 = new SimpleFeatureTypeBuilder();
        builder2.add("geom", Polygon.class);
        builder2.add("label", String.class);
        builder2.setName("labelPolyDisplacement");
        SimpleFeatureType type2 = builder2.buildFeatureType();
        
        SimpleFeature f11 = SimpleFeatureBuilder.build(type2, new Object[]{gf.createPolygon(
                new Coordinate[]{new Coordinate(4, 7), new Coordinate(4, 9), new Coordinate(6, 9), new Coordinate(6, 7), new Coordinate(4, 7)}), "labelPolyDisplacement1"}, null);
        /*
        SimpleFeature f22 = SimpleFeatureBuilder
                .build(type, new Object[] { gf.createPoint(new Coordinate(5, 8.2)),
                        "labelDisplacement1.1" }, null);
        SimpleFeature f33 = SimpleFeatureBuilder.build(type,
                new Object[] { gf.createPoint(new Coordinate(5, 2)),
                        "labelDisplacement2" }, null);
        SimpleFeature f44 = SimpleFeatureBuilder.build(type,
                new Object[] { gf.createPoint(new Coordinate(5, 3)),
                        "labelDisplacement2.1" }, null);
        */
        MemoryDataStore data2 = new MemoryDataStore();
        data2.addFeature(f11);
        //data.addFeature(f2);
        //data.addFeature(f3);
        //data.addFeature(f4);
        fs2 = data2.getFeatureSource("labelPolyDisplacement");
        
    }
    
    public void testDisplacementStandard() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementStandard.sld");
        BufferedImage image = renderLabels(fs, style, "Label Default Displacement");
        //ImageIO.write(image, "PNG", new File("./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementStandard1.png"));
        String refPath = "./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementStandard.png";
        ImageAssert.assertEquals(new File(refPath), image, 0);

    }
    
    public void testDisplacementVerticalBoth() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementVerticalBoth.sld");
        BufferedImage image = renderLabels(fs, style, "Label Vertical Both Displacement");
        //ImageIO.write(image, "PNG", new File("./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalBoth1.png"));
        String refPath = "./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalBoth.png";
        ImageAssert.assertEquals(new File(refPath), image, 0);

    }
    
    public void testDisplacementVerticalUp() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementVerticalUp.sld");
        BufferedImage image = renderLabels(fs, style, "Label Vertical Up Displacement");
        //ImageIO.write(image, "PNG", new File("./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalUp1.png"));
        String refPath = "./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalUp.png";
        ImageAssert.assertEquals(new File(refPath), image, 0);
    }
    
    public void testDisplacementVerticalDown() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementVerticalDown.sld");
        BufferedImage image = renderLabels(fs, style, "Label Vertical Down Displacement");
        //ImageIO.write(image, "PNG", new File("./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalDown1.png"));  
        String refPath = "./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalDown.png";
        ImageAssert.assertEquals(new File(refPath), image, 0);
    }


    public void testDisplacementVerticalBothConflictDisabled() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementVerticalBoth_conflict_disabled.sld");
        BufferedImage image = renderLabels(fs, style, "Label Vertical Both Displacement (conflict_disabled)");
        //ImageIO.write(image, "PNG", new File("./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalBoth_conflict_disabled.png"));
        String refPath = "./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalBoth_conflict_disabled.png";
        ImageAssert.assertEquals(new File(refPath), image, 0);

    }

    
  
    public void testDisplacementStandardMultiLayer() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementStandard.sld");
        Style style2 = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementStandardPoly.sld");
        BufferedImage image = renderLabels(new SimpleFeatureSource[]{fs, fs2}, 
                new Style[] {style, style2}, "Label Default Displacement");
        //ImageIO.write(image, "PNG", new File("./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementStandard1_multi.png"));
        String refPath = "./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementStandard_multi.png";
        ImageAssert.assertEquals(new File(refPath), image, 0);

    }
    
    public void testDisplacementVerticalBothMultiLayer() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementVerticalBoth.sld");
        Style style2 = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementVerticalBothPoly.sld");
        BufferedImage image = renderLabels(new SimpleFeatureSource[]{fs, fs2}, 
                new Style[] {style, style2}, "Label Vertical Both Displacement");
        //ImageIO.write(image, "PNG", new File("./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalBoth1_multi.png"));
        String refPath = "./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalBoth_multi.png";
        ImageAssert.assertEquals(new File(refPath), image, 0);

    }
    
    public void testDisplacementVerticalUpMultiLayer() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementVerticalUp.sld");
        Style style2 = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementVerticalUpPoly.sld");
        BufferedImage image = renderLabels(new SimpleFeatureSource[]{fs, fs2}, 
                new Style[] {style, style2}, "Label Vertical Up Displacement");
        //ImageIO.write(image, "PNG", new File("./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalUp1_multi.png"));
        String refPath = "./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalUp_multi.png";
        ImageAssert.assertEquals(new File(refPath), image, 0);
    }
    
    public void testDisplacementVerticalDownMultiLayer() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementVerticalDown.sld");
        Style style2 = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementVerticalDownPoly.sld");
        BufferedImage image = renderLabels(new SimpleFeatureSource[]{fs, fs2}, 
                new Style[] {style, style2}, "Label Vertical Down Displacement");
        //ImageIO.write(image, "PNG", new File("./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalDown1_multi.png"));  
        String refPath = "./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalDown_multi.png";
        ImageAssert.assertEquals(new File(refPath), image, 0);
    }


    public void testDisplacementVerticalBothConflictDisabledMultiLayer() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementVerticalBoth_conflict_disabled.sld");
        Style style2 = RendererBaseTest.loadStyle(this, "displacementMode/textDisplacementVerticalBoth_conflict_disabledPoly.sld");
        BufferedImage image = renderLabels(new SimpleFeatureSource[]{fs, fs2}, 
                new Style[] {style, style2}, "Label Vertical Both Displacement (conflict_disabled)");
        //ImageIO.write(image, "PNG", new File("./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalBoth_conflict_disabled_multi.png"));
        String refPath = "./src/test/resources/org/geotools/renderer/lite/test-data/displacementMode/textDisplacementVerticalBoth_conflict_disabled_multi.png";
        ImageAssert.assertEquals(new File(refPath), image, 0);

    }

    
    private BufferedImage renderLabels(SimpleFeatureSource fs, Style style, String title)
            throws Exception {
        MapContent mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        mc.addLayer(new FeatureLayer(fs, style));
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setMapContent(mc);
        
        return RendererBaseTest.showRender(title, renderer, TIME, bounds);
    }
    
    private BufferedImage renderLabels(SimpleFeatureSource[] fs, Style[] style, String title)
            throws Exception {
        MapContent mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        for (int i = 0; i <fs.length; i++) {
            mc.addLayer(new FeatureLayer(fs[i], style[i]));
        }
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setMapContent(mc);
        
        return RendererBaseTest.showRender(title, renderer, TIME, bounds);
    }
}
