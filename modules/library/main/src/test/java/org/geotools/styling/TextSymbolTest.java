/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    Created on April 12, 2002, 1:18 PM
 */
package org.geotools.styling;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Mark;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * Test for text symbols.
 *
 * @author jamesm
 * @task REVISIT: redo the Map stuff - I commented it out since DefaultMap is deprecated - cholmes.
 */
public class TextSymbolTest {
    String dataFolder;

    public TextSymbolTest() {
        dataFolder = System.getProperty("dataFolder");

        if (dataFolder == null) {
            // then we are being run by maven
            dataFolder = System.getProperty("basedir");
            dataFolder += "/tests/unit/testData";
        }
    }

    @org.junit.Test
    public void testRender() throws Exception {
        // System.out.println("\n\nTextSymbolTest\n");

        // Request extent
        GeometryFactory geomFac = new GeometryFactory();
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setCRS(null);
        ftb.add("centre", org.locationtech.jts.geom.Point.class);
        ftb.add("size", Double.class);
        ftb.add("rotation", Double.class);
        ftb.add("symbol", String.class);
        ftb.setName("test");
        SimpleFeatureType pointType = ftb.buildFeatureType();
        ListFeatureCollection data = new ListFeatureCollection(pointType);

        // FlatFeatureFactory pointFac = feaTypeFactory.(pointType);
        Point point;
        SimpleFeature pointFeature;

        // load font
        String[] symbol = {"\uF04A", "\uF04B", "\uF059", "\uF05A", "\uF06B", "\uF06C", "\uF06E"};
        double size = 16;
        double rotation = 0.0;
        int rows = 8;

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < symbol.length; i++) {
                point = makeSamplePoint(geomFac, i * 5.0 + 5.0, 5.0 + j * 5);
                pointFeature = SimpleFeatureBuilder.build(
                        pointType,
                        new Object[] {point, Double.valueOf(size), Double.valueOf(rotation), symbol[i]},
                        null);
                data.add(pointFeature);
            }

            size += 2;
            rotation += 45;
        }

        // REVISIT: Removed since it is deprecated, not sure what this test is
        // is doing, what should replace it.  If someone with more knowledge of
        // this stuff could update the tests that'd be great - ch.
        // org.geotools.map.Map map = new DefaultMap();
        // The following is complex, and should be built from
        // an SLD document and not by hand
        Mark textMark = new MarkImpl("square");

        GraphicImpl graphic = new GraphicImpl();
        graphic.graphicalSymbols().add(textMark);

        PointSymbolizerImpl pointsym = new PointSymbolizerImpl();
        pointsym.setGeometryPropertyName("centre");
        pointsym.setGraphic(graphic);

        RuleImpl rule = new RuleImpl();
        rule.symbolizers().add(pointsym);

        FeatureTypeStyle fts = new FeatureTypeStyleImpl();
        fts.rules().add(rule);
        fts.featureTypeNames().add(new NameImpl("testPoint"));

        StyleImpl style = new StyleImpl();
        style.featureTypeStyles().add(fts);

        // map.addFeatureTable(ft,style);

        /*Java2DRenderer renderer = new org.geotools.renderer.Java2DRenderer();
        Frame frame = new Frame("text symbol test");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {e.getWindow().dispose(); }
        });
        Panel p = new Panel();
        frame.add(p);
        frame.setSize(300,300);
        frame.setVisible(true);
        renderer.setOutput(p.getGraphics(),p.getBounds());
        map.render(renderer,ex.getBounds());//and finaly try and draw it!

        int w = 400, h =400;
        BufferedImage image = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0,w,h);
        renderer.setOutput(g,new java.awt.Rectangle(0,0,w,h));
        map.render(renderer,ex.getBounds());//and finaly try and draw it!
        File file = new File(dataFolder, "TextSymbolTest.jpg");
        FileOutputStream out = new FileOutputStream(file);
        ImageIO.write(image, "JPEG", out);
        Thread.sleep(5000);
        frame.dispose();*/
    }

    private Point makeSamplePoint(final GeometryFactory geomFac, double x, double y) {
        Coordinate c = new Coordinate(x, y);
        Point point = geomFac.createPoint(c);

        return point;
    }
}
