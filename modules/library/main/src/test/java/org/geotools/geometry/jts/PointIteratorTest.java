package org.geotools.geometry.jts;

import static org.junit.Assert.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

import org.junit.Test;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

public class PointIteratorTest {

    
    @Test
    public void testPointIteration() throws Exception {
        Point p = (Point) new WKTReader().read("POINT(1 10)");
        AffineTransform at = AffineTransform.getScaleInstance(2, 2);
        PointIterator it = new PointIterator(p, at);
        double[] coords = new double[2];
        
        
        assertFalse(it.isDone());
        assertEquals(PathIterator.SEG_MOVETO, it.currentSegment(coords));
        assertEquals(2.0, coords[0], 0d);
        assertEquals(20.0, coords[1], 0d);
        assertFalse(it.isDone());
        it.next();
        assertEquals(PathIterator.SEG_LINETO, it.currentSegment(coords));
        assertEquals(2.0, coords[0], 0d);
        assertEquals(20.0, coords[1], 0d);
        assertTrue(it.isDone());
    }
}
