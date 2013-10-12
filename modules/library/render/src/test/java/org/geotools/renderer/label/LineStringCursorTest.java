package org.geotools.renderer.label;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.WKTReader;

public class LineStringCursorTest {

    @Test
    public void testMaxAngle() throws Exception {
        LineString ls = (LineString) new WKTReader().read("LINESTRING(20 0, 10 1, 0 0)");
        LineStringCursor cursor = new LineStringCursor(ls);
        double maxAngle = cursor.getMaxAngleChange(0, ls.getLength());
        assertTrue(maxAngle < 11.5);
    }
}
