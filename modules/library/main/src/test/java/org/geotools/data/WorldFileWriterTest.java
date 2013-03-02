package org.geotools.data;

import static org.junit.Assert.*;

import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Test;

public class WorldFileWriterTest {

    @Test
    public void testWrite() throws Exception {
        AffineTransform at = new AffineTransform(42.34, 0, 0, -42.34,347671.10, 5196940.18);

        File tmp = File.createTempFile("write", "wld", new File("target"));
        new WorldFileWriter(tmp, at);

        BufferedReader r = new BufferedReader(new FileReader(tmp));
        assertEquals(42.34, Double.parseDouble(r.readLine()), 0.1);
        assertEquals(0, Double.parseDouble(r.readLine()), 0.1);
        assertEquals(0, Double.parseDouble(r.readLine()), 0.1);
        assertEquals(-42.34, Double.parseDouble(r.readLine()), 0.1);
        assertEquals(347671.10, Double.parseDouble(r.readLine()), 0.1);
        assertEquals(5196940.18, Double.parseDouble(r.readLine()), 0.1);

        assertNull(r.readLine());
    }
}
