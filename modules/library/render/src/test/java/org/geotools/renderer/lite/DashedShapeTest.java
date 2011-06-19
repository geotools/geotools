package org.geotools.renderer.lite;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import org.junit.Test;

import com.vividsolutions.jts.awt.ShapeReader;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class DashedShapeTest {
    static final float EPS = 1e-3f;

    @Test
    public void testHorizontalLine() {
        Line2D line = new Line2D.Double(0, 0, 30, 0);
        Shape stroked = new DashedShape(line, new float[] { 10, 10 }, 0);
        checkExpected(stroked, new float[] { 0, 0, 0 }, new float[] { 1, 10, 0 }, //
                new float[] { 0, 20, 0 }, new float[] { 1, 30, 0 });
    }
    
    @Test
    public void testShortLine() {
        Line2D line = new Line2D.Double(0, 0, 5, 0);
        Shape stroked = new DashedShape(line, new float[] { 10, 10 }, 0);
        checkExpected(stroked, new float[] { 0, 0, 0 }, new float[] { 1, 5, 0 });
    }
    
    @Test
    public void testMidLine() {
        Line2D line = new Line2D.Double(0, 0, 25, 0);
        Shape stroked = new DashedShape(line, new float[] { 10, 10 }, 0);
        checkExpected(stroked, new float[] { 0, 0, 0 }, new float[] { 1, 10, 0 }, //
                new float[] { 0, 20, 0 }, new float[] { 1, 25, 0 });
    }
    
    @Test
    public void testPhase() {
        Line2D line = new Line2D.Double(0, 0, 25, 0);
        Shape stroked = new DashedShape(line, new float[] { 10, 10 }, 5);
        checkExpected(stroked, new float[] { 0, 0, 0 }, new float[] { 1, 5, 0 }, //
                new float[] { 0, 15, 0 }, new float[] { 1, 25, 0 });
    }
    

    @Test
    public void testVerticalLine() {
        Line2D line = new Line2D.Double(0, 0, 0, 30);
        Shape stroked = new DashedShape(line, new float[] { 10, 10 }, 0);
        checkExpected(stroked, new float[] { 0, 0, 0 }, new float[] { 1, 0, 10 }, //
                new float[] { 0, 0, 20 }, new float[] { 1, 0, 30 });
    }
    
    @Test
    public void testLShape() {
        GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);
        gp.lineTo(0, 5);
        gp.lineTo(5, 5);
        Shape stroked = new DashedShape(gp, new float[] {2, 2}, 0);
        checkExpected(stroked, new float[] { 0, 0, 0 }, new float[] { 1, 0, 2 }, //
                new float[] { 0, 0, 4 }, new float[] { 1, 0, 5 }, new float[] { 1, 1, 5 },
                new float[] { 0, 3, 5 }, new float[] { 1, 5, 5 });
    }
    
    @Test
    public void testDisconnected() {
        GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);
        gp.lineTo(5, 0);
        gp.moveTo(10, 5);
        gp.lineTo(15, 5);
        Shape stroked = new DashedShape(gp, new float[] {2, 2}, 0);
        checkExpected(stroked, new float[] { 0, 0, 0 }, new float[] { 1, 2, 0 }, //
                new float[] { 0, 4, 0 }, new float[] { 1, 5, 0 },
                new float[] { 0, 10, 5 }, new float[] { 1, 12, 5 },
                new float[] { 0, 14, 5 }, new float[] { 1, 15, 5 });
    }
    
    @Test
    public void testRectangle() {
        // checks the SEG_CLOSE termination
        Rectangle2D r = new Rectangle2D.Double(0, 0, 4, 4);
        Shape stroked = new DashedShape(r, new float[] {2, 2}, 0);

        checkExpected(stroked, new float[] { 0, 0, 0 }, new float[] { 1, 2, 0 }, //
                new float[] { 0, 4, 0 }, new float[] { 1, 4, 2 }, new float[] { 0, 4, 4 },
                new float[] { 1, 2, 4 }, new float[] { 0, 0, 4 }, new float[] { 1, 0, 2 },
                new float[] { 0, 0, 0 }, new float[] { 4, 0, 0 });
    }

    void checkExpected(Shape stroked, float[]... segments) {
        PathIterator pi = stroked.getPathIterator(new AffineTransform());
        String errorMessage = errorMessage(stroked);
        float[] piSegment = new float[2];
        for (float[] segment : segments) {
            if (pi.isDone()) {
                fail("The iterator has less segments than expected. " + errorMessage);
            }          
            int type = pi.currentSegment(piSegment);
            assertEquals(errorMessage, type, (int) segment[0]);
            assertEquals(errorMessage, segment[1], piSegment[0], EPS);
            assertEquals(errorMessage, segment[2], piSegment[1], EPS);
            pi.next();
        }
        if (!pi.isDone()) {
            fail("The iterator has more segments than expected. " + errorMessage);
        }
    }
    
    String errorMessage(Shape shape) {
        PathIterator pi = shape.getPathIterator(new AffineTransform());
        StringBuffer sb = new StringBuffer("The iterator sequence differs from what expected: \n");
        float[] piSegment = new float[2];
        while(!pi.isDone()) {
            int type = pi.currentSegment(piSegment);
            sb.append("{").append(type).append(", ").append(piSegment[0])
                .append(", ").append(piSegment[1]).append("} ");
            pi.next();
        }
        return sb.toString();
    }
    
}
