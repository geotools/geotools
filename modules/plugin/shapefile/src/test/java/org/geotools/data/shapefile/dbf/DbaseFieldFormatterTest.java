package org.geotools.data.shapefile.dbf;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.TimeZone;
import org.junit.Before;
import org.junit.Test;

public class DbaseFieldFormatterTest {

    private DbaseFileWriter.FieldFormatter victim;

    @Before
    public void setup() {
        Charset charset = Charset.defaultCharset();
        TimeZone timeZone = TimeZone.getDefault();

        victim = new DbaseFileWriter.FieldFormatter(charset, timeZone, false);
    }

    private String checkOutput(Number n, int sz, int places) {

        String s = victim.getFieldString(sz, places, n);

        // assertEquals("Formatted Output", xpected, s.trim());
        boolean ascii = true;
        int i, c = 0;
        ;
        for (i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            if (c > 127) {
                ascii = false;
                break;
            }
        }
        assertTrue("ascii [" + i + "]:" + c, ascii);
        assertEquals("Length", sz, s.length());

        assertEquals("Value", n.doubleValue(), Double.valueOf(s), Math.pow(10.0, -places));

        // System.out.printf("%36s->%36s%n", n, s);

        return s;
    }

    @Test
    public void testNaN() {
        checkOutput(Double.NaN, 33, 31);
    }

    @Test
    public void testNegative() {
        checkOutput(Double.valueOf(-1.0e39), 33, 31);
    }

    @Test
    public void testSmall() {
        checkOutput(Double.valueOf(42.123), 33, 31);
    }

    @Test
    public void testLarge() {
        checkOutput(12345.678, 33, 31);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotFit_1() {
        checkOutput(12345.678, 6, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotFit_2() {
        checkOutput(Double.valueOf(Math.PI * 1.0E10), 4, 1);
    }

    @Test
    public void testSqueeze_ok() {
        checkOutput(Double.valueOf(Math.PI), 3, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSqueeze_x1() {
        checkOutput(Double.valueOf(Math.PI), 2, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSqueeze_x2() {
        checkOutput(Double.valueOf(-Math.PI), 3, 1);
    }

    @Test
    public void testMax() {
        checkOutput(Double.MAX_VALUE, 33, 31);
    }

    @Test
    public void testMin() {
        checkOutput(Double.MIN_VALUE, 33, 31);
    }

    @Test
    public void testIntegral() {
        checkOutput(Double.valueOf(1999.0), 33, 31);
    }

    @Test
    public void testPI() {
        checkOutput(Double.valueOf(Math.PI), 33, 31);
    }

    @Test
    public void testPI_10() {
        checkOutput(Double.valueOf(Math.PI * 1.0E10), 33, 31);
    }

    @Test
    public void testPI_100() {
        checkOutput(Double.valueOf(Math.PI * 1.0E100), 33, 31);
    }

    @Test
    public void testNoValue_1() {
        // "Any floating point number smaller than –10e38 is considered by a shapefile reader to
        // represent a "no data" value.", per ESRI
        checkOutput(Double.valueOf(-1.00001e38), 33, 31);
    }

    @Test
    public void testInt_1999() {
        checkOutput(Integer.valueOf(1999), 33, 31);
    }

    @Test
    public void testInt_0() {
        checkOutput(Integer.valueOf(0), 33, 31);
    }

    @Test
    public void testInt_12345678() {
        checkOutput(Integer.valueOf(12345678), 33, 31);
    }

    @Test
    public void testInt_m1() {
        checkOutput(Integer.valueOf(-1), 33, 31);
    }

    @Test
    public void testInt_m987654321() {
        checkOutput(Integer.valueOf(-987654321), 33, 31);
    }

    @Test
    public void testNoValue_2() {
        // "Any floating point number smaller than –10e38 is considered by a shapefile reader to
        // represent a "no data" value.", per ESRI
        checkOutput(Double.valueOf(-1.00001e38), 12, 6);
    }

    @Test
    public void testI_1999() {
        checkOutput(Integer.valueOf(1999), 8, 0);
    }

    @Test
    public void testI_0() {
        checkOutput(Integer.valueOf(0), 3, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testI_12345678_x() {
        checkOutput(Integer.valueOf(12345678), 6, 0);
    }

    @Test
    public void testI_12345678_ok() {
        checkOutput(Integer.valueOf(12345678), 8, 0);
    }

    @Test
    public void testI_m1() {
        checkOutput(Integer.valueOf(-1), 12, 0);
    }

    @Test
    public void testI_m987654321() {
        checkOutput(Integer.valueOf(-987654321), 10, 0);
    }
}
