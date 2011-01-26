package org.geotools.renderer.lite;

import static junit.framework.Assert.*;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import org.junit.Test;

public class DashedShapeTest {
    static final float EPS = 1e-3f;

    @Test
    public void testHorizontalLine() {
        Line2D line = new Line2D.Double(0, 0, 30, 0);
        Shape stroked = new DashedShape(line, new float[] { 10, 10 }, 0);
        checkExpected(stroked, new float[] { 0, 0, 0 }, new float[] { 1, 10, 0 }, //
                new float[] { 0, 20, 0 }, new float[] { 1, 30, 0 });
    }
    
    public void testShortLine() {
        Line2D line = new Line2D.Double(0, 0, 5, 0);
        Shape stroked = new DashedShape(line, new float[] { 10, 10 }, 0);
        checkExpected(stroked, new float[] { 0, 0, 0 }, new float[] { 1, 0, 5 });

    }
    
    public void testMidLine() {
        Line2D line = new Line2D.Double(0, 0, 25, 0);
        Shape stroked = new DashedShape(line, new float[] { 10, 10 }, 0);
        checkExpected(stroked, new float[] { 0, 0, 0 }, new float[] { 1, 10, 0 }, //
                new float[] { 0, 20, 0 }, new float[] { 1, 25, 0 });
    }
    
    public void testPhase() {
        Line2D line = new Line2D.Double(0, 0, 25, 0);
        Shape stroked = new DashedShape(line, new float[] { 10, 10 }, 5);
        checkExpected(stroked, new float[] { 0, 5, 0 }, new float[] { 1, 15, 0 }, //
                new float[] { 0, 25, 0 });
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
        float[] piSegment = new float[2];
        for (float[] segment : segments) {
            if (pi.isDone()) {
                fail("The iterator has less segments than expected");
            }
            int type = pi.currentSegment(piSegment);
            assertEquals(type, (int) segment[0]);
            assertEquals(segment[1], piSegment[0], EPS);
            assertEquals(segment[2], piSegment[1], EPS);
            pi.next();
        }
        if (!pi.isDone()) {
            fail("The iterator has more segments than expected");
        }
    }
}
