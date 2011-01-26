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
 */
package org.geotools.renderer.shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

import junit.framework.TestCase;

import org.geotools.data.shapefile.shp.ShapeType;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Test The PolygonShape class
 *
 * @author jeichar
 *
 * @since 2.1.x
 * @source $URL$
 */
public class PolygonShapeTest extends TestCase {
    private static final boolean DISPLAY = false;

    public static void testPolygonWithHoles() throws Exception {
        double[] coord1 = new double[] {
                0.0, 0.0, 0.0, 200.0, 200, 200.0, 200.0, 0.0, 0.0, 0.0
            };
        double[] coord2 = new double[] {
                50.0, 50.0, 50.0, 150.0, 150.0, 150.0, 150.0, 50.0
            };
        double[][] coords = new double[][] { coord1, coord2 };

        SimpleGeometry geom = new SimpleGeometry(ShapeType.ARC, coords,
                new Envelope(0, 15, 0, 15));

        final PolygonShape shape = new PolygonShape(geom);

        PathIterator i = shape.getPathIterator(null);

        double[] tmp = new double[6];
        int result = i.currentSegment(tmp);
        assertFalse(i.isDone());
        assertEquals(0.0, tmp[0], 0.001);
        assertEquals(0.0, tmp[1], 0.001);
        assertEquals(PathIterator.SEG_MOVETO, result);

        i.next();

        tmp = new double[6];
        result = i.currentSegment(tmp);

        assertFalse(i.isDone());
        assertEquals(0.0, tmp[0], 0.001);
        assertEquals(200.0, tmp[1], 0.001);
        assertEquals(PathIterator.SEG_LINETO, result);

        i.next();

        tmp = new double[6];
        result = i.currentSegment(tmp);

        assertFalse(i.isDone());
        assertEquals(200.0, tmp[0], 0.001);
        assertEquals(200.0, tmp[1], 0.001);
        assertEquals(PathIterator.SEG_LINETO, result);

        i.next();

        tmp = new double[6];
        result = i.currentSegment(tmp);

        assertFalse(i.isDone());
        assertEquals(200.0, tmp[0], 0.001);
        assertEquals(0.0, tmp[1], 0.001);
        assertEquals(PathIterator.SEG_LINETO, result);

        i.next();

        tmp = new double[6];
        result = i.currentSegment(tmp);

        assertFalse(i.isDone());
        assertEquals(0.0, tmp[0], 0.001);
        assertEquals(0.0, tmp[1], 0.001);
        assertEquals(PathIterator.SEG_CLOSE, result);

        i.next();

        tmp = new double[6];
        result = i.currentSegment(tmp);

        assertFalse(i.isDone());
        assertEquals(50.0, tmp[0], 0.001);
        assertEquals(50.0, tmp[1], 0.001);
        assertEquals(PathIterator.SEG_MOVETO, result);

        i.next();

        tmp = new double[6];
        result = i.currentSegment(tmp);

        assertFalse(i.isDone());
        assertEquals(50.0, tmp[0], 0.001);
        assertEquals(150.0, tmp[1], 0.001);
        assertEquals(PathIterator.SEG_LINETO, result);

        i.next();

        tmp = new double[6];
        result = i.currentSegment(tmp);

        assertFalse(i.isDone());
        assertEquals(150.0, tmp[0], 0.001);
        assertEquals(150.0, tmp[1], 0.001);
        assertEquals(PathIterator.SEG_LINETO, result);

        i.next();

        tmp = new double[6];
        result = i.currentSegment(tmp);

        // assertFalse( i.isDone() );
        assertEquals(150.0, tmp[0], 0.001);
        assertEquals(50.0, tmp[1], 0.001);
        assertEquals(PathIterator.SEG_CLOSE, result);

        if (DISPLAY) {
            display("Holes", shape, 500, 500);
        }
    }

    public static void testMultiPolygonNoOverlap() throws Exception {
        double[] coord1 = new double[] {
                0.0, 0.0, 0.0, 100.0, 100, 100.0, 100.0, 0.0, 0.0, 0.0
            };
        double[] coord2 = new double[] {
                150.0, 150.0, 150.0, 250.0, 250.0, 250.0, 250.0, 150.0
            };
        double[][] coords = new double[][] { coord1, coord2 };

        SimpleGeometry geom = new SimpleGeometry(ShapeType.ARC, coords,
                new Envelope(0, 15, 0, 15));

        final PolygonShape shape = new PolygonShape(geom);

        if (DISPLAY) {
            display("MultiNoOverlap", shape, 500, 500);
        }
    }

    public static void testMultiPolygonOverlap() throws Exception {
        double[] coord1 = new double[] {
                0.0, 0.0, 0.0, 100.0, 100, 100.0, 100.0, 0.0, 0.0, 0.0
            };
        double[] coord2 = new double[] {
                50.0, 50.0, 50.0, 250.0, 250.0, 250.0, 250.0, 50.0
            };
        double[][] coords = new double[][] { coord1, coord2 };

        SimpleGeometry geom = new SimpleGeometry(ShapeType.ARC, coords,
                new Envelope(0, 15, 0, 15));

        final PolygonShape shape = new PolygonShape(geom);

        if (DISPLAY) {
            display("MultiOverlap", shape, 500, 500);
        }
    }

    public void testPolygonPoint() throws InterruptedException {
        if (!DISPLAY) {
            return;
        }
        final GeneralPath shape = new GeneralPath();

        shape.moveTo(10.1f, 10.1f);
        shape.lineTo(10.2f, 10.1f);
        shape.lineTo(10.2f, 10.2f);
        shape.lineTo(10.1f, 10.2f);
        shape.lineTo(10.1f, 10.1f);

        Frame frame = new Frame("");
        frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    e.getWindow().dispose();
                }
            });

        Panel p = new Panel() {
                /** <code>serialVersionUID</code> field */
                private static final long serialVersionUID = 1L;

                public void paint(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setPaint(Color.BLUE);
                    g2.setStroke(new BasicStroke(1));
                    (g2).fill(shape);
                    g2.setPaint(Color.BLACK);
                    (g2).draw(shape);
                }
            };

        frame.add(p);
        frame.setSize(100, 100);
        frame.setVisible(true);
        Thread.sleep(1000);
    }

    public static Frame display(String testName, final Shape shape, int w, int h)
        throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            return null;
        }
        Frame frame = new Frame(testName);
        frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    e.getWindow().dispose();
                }
            });

        Panel p = new Panel() {
                /** <code>serialVersionUID</code> field */
                private static final long serialVersionUID = 1L;

                public void paint(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setPaint(Color.BLUE);
                    (g2).fill(shape);
                    g2.setPaint(Color.BLACK);
                    (g2).draw(shape);
                }
            };

        frame.add(p);
        frame.setSize(w, h);
        frame.setVisible(true);
        Thread.sleep(1000);

        return frame;
    }
}
