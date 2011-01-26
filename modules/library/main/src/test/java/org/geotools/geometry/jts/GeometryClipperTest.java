package org.geotools.geometry.jts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class GeometryClipperTest {

    static final double EPS = 1e-3;
    static final double EPS_CORNERS = 1e-1;
    GeometryClipper clipper;
    Geometry boundsPoly;
    WKTReader wkt;
    
    @Before
    public void setUp() throws Exception {
        // System.setProperty("org.geotools.test.interactive", "true");
        clipper = new GeometryClipper(new Envelope(0, 10, 0, 10));
        wkt = new WKTReader();
        boundsPoly = wkt.read("POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))");
    }
    
    @Test
    public void testFullyInside() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(1 1, 2 5, 9 1)");
        LineString clipped = (LineString) clipper.clip(ls, false);
        assertTrue(ls.equals(clipped));
        showResult("Fully inside", ls, clipped);
    }
    
    @Test
    public void testInsideBorders() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(0 0, 2 5, 10 0)");
        LineString clipped = (LineString) clipper.clip(ls, false);
        assertTrue(ls.equals(clipped));
        showResult("Inside touching borders", ls, clipped);
    }
    
    @Test
    public void testFullyOutside() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-5 0, -5 15, 15 15)");
        LineString clipped = (LineString) clipper.clip(ls, false);
        assertNull(clipped);
        showResult("Inside touching borders", ls, clipped);
    }
    
    @Test
    public void testCross() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-5 -5, 15 15)");
        LineString clipped = (LineString) clipper.clip(ls, false);
        assertTrue(clipped.equals(wkt.read("LINESTRING(0 0, 10 10)")));
        showResult("Cross", ls, clipped);
    }
    
    @Test
    public void testTouchLine() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(0 0, 0 10)");
        LineString clipped = (LineString) clipper.clip(ls, false);
        assertTrue(clipped.equals(ls));
        showResult("Touch border", ls, clipped);
    }
    
    @Test
    public void testTouchPoint() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-5 5, 0 5)");
        Geometry clipped = clipper.clip(ls, false);
        assertNull(clipped);
        showResult("Touch point", ls, clipped);
    }
    
    @Test
    public void testMultiTouch() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-5 0, 0 1, -5 2, 0 3, -5 4, 0 5)");
        Geometry clipped = clipper.clip(ls, false);
        assertNull(clipped);
        showResult("Multitouch", ls, clipped);
    }
    
    @Test
    public void testTouchAndCross() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-5 0, 0 1, -5 2, 5 2, 5 3, -5 3, 0 4)");
        Geometry clipped = clipper.clip(ls, false);
        assertTrue(clipped.equals(wkt.read("LINESTRING(0 2, 5 2, 5 3, 0 3)")));
        showResult("Touch and cross", ls, clipped);
    }
    
    @Test
    public void testTouchAndParallel() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-5 0, 0 1, -5 2, 0 2, 0 3, -5 3, 0 4)");
        Geometry clipped = clipper.clip(ls, false);
        assertTrue(clipped.equals(wkt.read("LINESTRING(0 2, 0 3)")));
        showResult("Touch and parallel", ls, clipped);
    }
    
    @Test
    public void testInsideOut() throws Exception {
        LineString ls = (LineString) wkt.read("LINESTRING(-2 8, 12 8, 12 2, -2 2)");
        MultiLineString clipped = (MultiLineString) clipper.clip(ls, false);
        assertTrue(clipped.equals(wkt.read("MULTILINESTRING((0 8, 10 8), (10 2, 0 2))")));
        showResult("Touch border", ls, clipped);
    }
    
    @Test
    public void testFullyOutsideCircle() throws Exception {
        Point p = (Point) wkt.read("POINT(5 5)");
        LineString ls = ((Polygon) p.buffer(10)).getExteriorRing();
        Geometry clipped = clipper.clip(ls, false);
        assertNull(clipped);
        showResult("Circle around", ls, clipped);
    }
    
    @Test
    public void testCrossingCircle() throws Exception {
        Point p = (Point) wkt.read("POINT(5 5)");
        LineString ls = ((Polygon) p.buffer(6)).getExteriorRing();
        MultiLineString clipped = (MultiLineString) clipper.clip(ls, false);
        assertEquals(4, clipped.getNumGeometries());
        showResult("Circle around", ls, clipped);
    }
    
    @Test
    public void testInsidePolygon() throws Exception {
        Geometry g = wkt.read("POINT(5 5)").buffer(2);
        Geometry clipped = clipper.clip(g, false);
        assertTrue(g.equals(clipped));
        showResult("Polygon inside", g, clipped);
    }
    
    @Test
    public void testOutsidePolygon() throws Exception {
        Geometry g = wkt.read("POINT(5 5)").buffer(10);
        Geometry clipped = clipper.clip(g, false);
        assertTrue(boundsPoly.equals(clipped));
        showResult("Polygon outside", g, clipped);
    }
    
    @Test
    public void testPolygonCrossingSide() throws Exception {
        Geometry g = wkt.read("POLYGON((-2 2, 2 2, 2 4, -2 4, -2 2))");
        Geometry clipped = clipper.clip(g, false);
        //assertTrue(clipped.equals(wkt.read("POLYGON((0 2, 2 2, 2 4, 0 4, 0 2))")));
        showResult("Crossing side", g, clipped);
    }
    
    @Test
    public void testCrossingOtherSide() throws Exception {
        Geometry g = wkt.read("POLYGON((6 2, 12 2, 12 6, 6 6, 6 2))");
        Geometry clipped = clipper.clip(g, false);
        //assertTrue(clipped.equals(wkt.read("POLYGON((0 2, 10 2, 10 10, 0 10, 0 2))")));
        showResult("Donut crossing", g, clipped);
    }
    
    @Test
    public void testPolygonCrossingTwoSides() throws Exception {
        Geometry g = wkt.read("POLYGON((-2 2, 2 2, 2 12, -2 12, -2 2))");
        Geometry clipped = clipper.clip(g, false);
        assertTrue(clipped.equals(wkt.read("POLYGON((0 2, 2 2, 2 10, 0 10, 0 2))")));
        showResult("Crossing two sides", g, clipped);
    }
    
    @Test
    public void testPolygonCrossingThreeSides() throws Exception {
        Geometry g = wkt.read("POLYGON((-2 2, 12 2, 12 12, -2 12, -2 2))");
        Geometry clipped = clipper.clip(g, false);
        assertTrue(clipped.equals(wkt.read("POLYGON((0 2, 10 2, 10 10, 0 10, 0 2))")));
        showResult("Crossing three sides", g, clipped);
    }
    
    @Test
    public void testDonutCrossingInvalid() throws Exception {
        Geometry g = wkt.read("POLYGON((6 2, 14 2, 14 8, 6 8, 6 2), (8 4, 12 4, 12 6, 8 6, 8 4))");
        Geometry clipped = clipper.clip(g, false);
        // System.out.println(clipped);
        assertTrue(clipped.equalsExact(wkt.read("POLYGON ((10 2, 10 8, 6 8, 6 2, 10 2), (10 4, 10 6, 8 6, 8 4, 10 4))")));
        showResult("Donut crossing, invalid geom", g, clipped);
    }
    
    @Test
    public void testDonutHoleOutside() throws Exception {
        Geometry g = wkt.read("POLYGON((6 2, 14 2, 14 8, 6 8, 6 2), (11 4, 12 4, 12 6, 11 6, 11 4))");
        Geometry clipped = clipper.clip(g, false);
        // System.out.println(clipped);
        assertTrue(clipped.equalsExact(wkt.read("POLYGON ((10 2, 10 8, 6 8, 6 2, 10 2))")));
        showResult("Donut crossing, invalid geom", g, clipped);
    }
    
    @Test
    public void testDonutCrossingValid() throws Exception {
        Geometry g = wkt.read("POLYGON((6 2, 14 2, 14 8, 6 8, 6 2), (8 4, 12 4, 12 6, 8 6, 8 4))");
        Geometry clipped = clipper.clip(g, true);
        assertTrue(clipped.equals(wkt.read("POLYGON ((10 2, 6 2, 6 8, 10 8, 10 6, 8 6, 8 4, 10 4, 10 2))")));
        showResult("Donut crossing, valid geom", g, clipped);
    }
    
    @Test
    public void testGeotXYWZ() throws Exception {
        clipper = new GeometryClipper(new Envelope(-11, 761, -11, 611));
        Geometry g = wkt.read("POLYGON((367 -13, 459 105, 653 -42, 611 -96, 562 -60, 514 -124, 367 -13))");
        System.out.println(g.getNumPoints());
        Geometry clipped = clipper.clip(g, false);
        assertNotNull(clipped);
        assertTrue(!clipped.isEmpty());
//        System.out.println(clipped);
    }
    
    public void showResult(String title, Geometry original, Geometry clipped) throws Exception {
        final String headless = System.getProperty("java.awt.headless", "false");
        if (headless.equalsIgnoreCase("true") || !TestData.isInteractiveTest()) {
            return;
        }
        
        BufferedImage image = new BufferedImage(600, 600, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D gr = image.createGraphics();
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setColor(Color.WHITE);
        gr.fillRect(0, 0, image.getWidth(), image.getHeight());
        gr.setColor(Color.LIGHT_GRAY);

        gr.setStroke(new BasicStroke(1, 0, 0, 1, new float[] { 5, 5 }, 0));
        gr.draw(new Line2D.Double(0, 300, 600, 300));
        gr.draw(new Line2D.Double(300, 0, 300, 600));

        AffineTransform at = new AffineTransform();
        at.translate(300, 300);
        at.scale(10, -10);

        gr.setStroke(new BasicStroke(1));
        gr.setColor(Color.LIGHT_GRAY);
        gr.draw(new LiteShape(boundsPoly, at, false));
        gr.setStroke(new BasicStroke(0.5f));
        gr.setColor(Color.BLUE);
        gr.draw(new LiteShape(original, at, false));
        if(clipped != null) {
            gr.setStroke(new BasicStroke(2));
            if(clipped instanceof Polygon || clipped instanceof MultiPolygon) {
                gr.setColor(Color.LIGHT_GRAY);
                gr.fill(new LiteShape(clipped, at, false));
                gr.setColor(Color.BLUE);
            }
            gr.draw(new LiteShape(clipped, at, false));
        }

        gr.dispose();

        JFrame frame = new JFrame(title);
        frame.setContentPane(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);

        Thread.sleep(3000);
        frame.setVisible(false);
    }
    
//    public static void main(String[] args) throws Exception {
//        GeometryClipper clipper = new GeometryClipper(new Envelope(0, 10, 0, 10));
//        Geometry boundsPoly = new WKTReader().read("POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))");
//        
//        Point p = (Point) new WKTReader().read("POINT(5 5)");
//        LineString ls = ((Polygon) p.buffer(6, 128)).getExteriorRing();
//        
//        long start = System.nanoTime();
//        for (int i = 0; i < 1000000; i++) {
//            clipper.clip(ls);
//        }
//        long end = System.nanoTime();
//        System.out.println("GeometryClipper: " + (end - start) / 1000000000.0);
        
//        start = System.nanoTime();
//        for (int i = 0; i < 10000; i++) {
//            ls.intersection(boundsPoly);
//        }
//        end = System.nanoTime();
//        System.out.println("JTS: " + (end - start) / 1000000000.0);
//        
//        MultiLineString clipped = (MultiLineString) clipper.clip(ls);
//        System.out.println(clipped);
//        System.out.println(ls.intersection(boundsPoly));
//    }
    
    
}
