/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.windbarbs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Unit tests for WindBarbs factory
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 * 
 */
public class WindBarbsFactoryTest extends Assert {

    /** WKT_WRITER */
    private static final WKTWriter WKT_WRITER = new WKTWriter(2);

    class ShapePanel extends JPanel {

        private Shape shp;

        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.black);
            g2d.setTransform(AffineTransform.getTranslateInstance(-shp.getBounds().getMinX(), -shp
                    .getBounds().getMinY()));
            g2d.draw(shp);
            g2d.dispose();
        }
    }

    private WindBarbsFactory wbf = new WindBarbsFactory();

    private SimpleFeature feature;

    private Expression exp;

    private FilterFactory ff;

    {
        try {
            ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
            SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
            featureTypeBuilder.setName("TestType");
            featureTypeBuilder.add("geom", LineString.class, DefaultGeographicCRS.WGS84);
            SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
            this.feature = featureBuilder.buildFeature(null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInvalidAndRare() {
        // MAX 100 KNOTS
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(110)[kts]");
        Shape shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNull(shp);

        // INF and -INF are rejected
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(Infinity)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNull(shp);
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(-Infinity)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNull(shp);

        // NaN is x-----
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(NaN)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString(),
                "MULTILINESTRING ((0 -0, 0 40), (5 45, -5 35), (-5 45, 5 35))");

        // missing ? before KVP
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(110)[kts]emisphere=N");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNull(shp);

        // wrong separator before KVP
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(110)[kts]&emisphere=N");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNull(shp);
    }

    @Test
    public void otherInvalidTests() {

        // wrong name
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "pippo(110)[kts]");
        Shape shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNull(shp);

        // wrong name
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "(110)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNull(shp);

        // wrong qualifier
        this.exp = ff.literal("wrong://" + "default(110)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNull(shp);

        // missing UoM
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(110)");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNull(shp);

        // wrong syntax for speed
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default110[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNull(shp);

        // null symbol
        shp = (Shape) wbf.getShape(null, null, this.feature);
        assertNull(shp);

        // null feature
        shp = (Shape) wbf.getShape(null, this.exp, null);
        assertNull(shp);

    }

    @Test
    public void testCustomBarbs() {
        // no module --> x--------
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX
                + "default(NaN)[kts]?vectorLength=50");
        Shape shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        String wkt = WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString();
        assertEquals(wkt, "MULTILINESTRING ((0 -0, 0 50), (5 55, -5 45), (-5 55, 5 45))");

        // 2.99999999 KNOTS --> calm
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX
                + "default(2.999999999999)[kts]?zeroWindRadius=15");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Ellipse2D);
        assertEquals(((Ellipse2D) shp).getHeight(), ((Ellipse2D) shp).getWidth(), 1E-6); // circle
        assertEquals(((Ellipse2D) shp).getHeight(), 15, 1E-6); // 15
        assertEquals(((Ellipse2D) shp).getWidth(), 15, 1E-6); // 15

        // 3 KNOTS --> short barb
        // NORTH
        this.exp = ff
                .literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(3)[kts]?vectorLength=50");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        wkt = WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString();
        assertEquals(wkt, "MULTILINESTRING ((0 -0, 0 50), (0 45, 10 46.25))");
        // SOUTH make sure the same shp is flipped on y axis
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX
                + "default(3)[kts]?emisphere=S&vectorLength=50");
        Shape shpS = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shpS);
        assertTrue(shpS instanceof Path2D);
        shpS = WindBarbsFactory.SOUTHERN_EMISPHERE_FLIP.createTransformedShape(shpS); // flip and check
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shpS)).toString(), wkt);

        // 5 KNOTS --> short barb
        this.exp = ff
                .literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(5)[kts]?vectorLength=50");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        wkt = WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString();
        assertEquals(wkt, "MULTILINESTRING ((0 -0, 0 50), (0 45, 10 46.25))");

        // SOUTH make sure the same shp is flipped on y axis
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX
                + "default(5)[kts]?hemisphere=S&vectorLength=50");
        shpS = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shpS);
        assertTrue(shpS instanceof Path2D);
        shpS = WindBarbsFactory.SOUTHERN_EMISPHERE_FLIP.createTransformedShape(shpS); // flip and check
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shpS)).toString(), wkt);

        // 100 KNOTS --> square
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX
                + "default(100)[kts]?vectorLength=30");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        wkt = WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString();
        assertEquals(wkt, "MULTILINESTRING ((0 -0, 0 30), (0 30, 10 30, 10 20, 0 20, 0 30))");

        // SOUTH make sure the same shp is flipped on y axis
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX
                + "default(100)[kts]?hemisphere=s&vectorLength=30");
        shpS = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shpS);
        assertTrue(shpS instanceof Path2D);
        shpS = WindBarbsFactory.SOUTHERN_EMISPHERE_FLIP.createTransformedShape(shpS); // flip and check
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shpS)).toString(), wkt);

        // 15 KNOTS --> square
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX
                + "default(15)[kts]?vectorLength=30");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)),
                "MULTILINESTRING ((0 -0, 0 30), (0 30, 20 32.5), (0 25, 10 26.25))");

        // 20 KNOTS --> 2 long feathers
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX
                + "default(20)[kts]?longBarbLength=50");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)),
                "MULTILINESTRING ((0 -0, 0 40), (0 40, 50 42.5), (0 35, 50 37.5))");

        // 25 KNOTS --> complex
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX
                + "default(25)[kts]?longBarbLength=50");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)),
                "MULTILINESTRING ((0 -0, 0 40), (0 40, 50 42.5), (0 35, 50 37.5), (0 30, 25 31.25))");

        // 50 KNOTS --> pennant
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX
                + "default(50)[kts]?basePennantLength=10");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)),
                "MULTILINESTRING ((0 -0, 0 40), (0 40, 20 35, 0 30, 0 40))");

    }

    @Test
    public void testDefaultValid() {
        // no module --> x--------
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(NaN)[kts]");
        Shape shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString(),
                "MULTILINESTRING ((0 -0, 0 40), (5 45, -5 35), (-5 45, 5 35))");

        // 1 KNOTS --> calm
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(1)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Ellipse2D);
        assertEquals(((Ellipse2D) shp).getHeight(), ((Ellipse2D) shp).getWidth(), 1E-6); // circle

        // 2.99999999 KNOTS --> calm
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(2.999999999999)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Ellipse2D);
        assertEquals(((Ellipse2D) shp).getHeight(), ((Ellipse2D) shp).getWidth(), 1E-6); // circle

        // 3 KNOTS --> short barb
        // NORTH
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(3)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        String shpString = WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString();
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString(),
                "MULTILINESTRING ((0 -0, 0 40), (0 35, 10 36.25))");
        // SOUTH make sure the same shp is flipped on y axis
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(3)[kts]?emisphere=S");
        Shape shpS = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shpS);
        assertTrue(shpS instanceof Path2D);
        shpS = WindBarbsFactory.SOUTHERN_EMISPHERE_FLIP.createTransformedShape(shpS); // flip and check
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shpS)).toString(),
                shpString);

        // 5 KNOTS --> short barb
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(5)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        shpString = WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString();
        assertEquals(shpString, "MULTILINESTRING ((0 -0, 0 40), (0 35, 10 36.25))");

        // SOUTH make sure the same shp is flipped on y axis
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(5)[kts]?hemisphere=S");
        shpS = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shpS);
        assertTrue(shpS instanceof Path2D);
        shpS = WindBarbsFactory.SOUTHERN_EMISPHERE_FLIP.createTransformedShape(shpS); // flip and check
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shpS)).toString(),
                shpString);

        // 100 KNOTS --> square
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(100)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        shpString = WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString();
        assertEquals(shpString, "MULTILINESTRING ((0 -0, 0 40), (0 40, 10 40, 10 30, 0 30, 0 40))");
        // SOUTH make sure the same shp is flipped on y axis
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(100)[kts]?hemisphere=s");
        shpS = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shpS);
        assertTrue(shpS instanceof Path2D);
        shpS = WindBarbsFactory.SOUTHERN_EMISPHERE_FLIP.createTransformedShape(shpS); // flip and check
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shpS)).toString(),
                shpString);

        // 15 KNOTS --> square
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(15)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString(),
                "MULTILINESTRING ((0 -0, 0 40), (0 40, 20 42.5), (0 35, 10 36.25))");

        // 20 KNOTS --> square
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(20)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString(),
                "MULTILINESTRING ((0 -0, 0 40), (0 40, 20 42.5), (0 35, 20 37.5))");

        // 25 KNOTS --> square
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(25)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString(),
                "MULTILINESTRING ((0 -0, 0 40), (0 40, 20 42.5), (0 35, 20 37.5), (0 30, 10 31.25))");

        // 50 KNOTS --> pennat
        this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX + "default(50)[kts]");
        shp = (Shape) wbf.getShape(null, this.exp, this.feature);
        assertNotNull(shp);
        assertTrue(shp instanceof Path2D);
        assertEquals(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp)).toString(),
                "MULTILINESTRING ((0 -0, 0 40), (0 40, 20 37, 0 34, 0 40))");

    }

    @Ignore
    public void testWellKnownTextLineString() {
        WindBarbsFactory wbf = new WindBarbsFactory();
        try {
            this.exp = ff.literal(WindBarbsFactory.WINDBARBS_PREFIX
                    + "default(15)[kts]?vectorLength=30");
            Shape shp = (Shape) wbf.getShape(null, this.exp, this.feature);
            System.out.println(WindBarbsFactoryTest.WKT_WRITER.write(JTS.toGeometry(shp))
                    .toString());
            ShapePanel p = new ShapePanel();
            p.shp = shp;

            JFrame frame = new JFrame("Draw Shapes");
            frame.getContentPane().add(p);
            frame.setSize(100, 100);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            System.in.read();

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
            return;
        }

        assertTrue(true);
    }
}
