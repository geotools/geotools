package org.geotools.data.shapefile.dbf;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.TimeZone;

import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.junit.Before;
import org.junit.Test;

public class DbaseFieldFormatterTest {

	private DbaseFileWriter.FieldFormatter victim;
	
	@Before
	public void setup() {
		Charset charset = Charset.defaultCharset();
		TimeZone timeZone = TimeZone.getDefault();

		victim = new DbaseFileWriter.FieldFormatter(charset, timeZone);
	}

	private String checkOutput(Number n) {
		int sz = 33;
		int places = 31;
				
		String s = victim.getFieldString(sz, places, n);
		
		// assertEquals("Formatted Output", xpected, s.trim());
		boolean ascii = true;
		int i, c = 0;;
		for (i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (c > 127) {
				ascii = false;
				break;
			}
		}
		assertTrue("ascii ["+i+"]:" + c, ascii);
		assertEquals("Length", sz, s.length());
		
		assertEquals("Value", n.doubleValue(), Double.valueOf(s), 0.001);
		
		System.out.printf("%g->%s%n", n, s);
		
		return s;
	}
		
	@Test
	public void testNaN() {
		checkOutput(Double.NaN);
	}
	
	@Test
	public void testNegative() {
		checkOutput(Double.valueOf(-1.0e39));
	}
	
	@Test
	public void testSmall() {
		checkOutput(Double.valueOf(42.123));
	}
	
	@Test
	public void testLarge() {
		String s = checkOutput(12345.678);
		assertEquals("12345.678", s.trim());
	}
	
	@Test
	public void testMax() {
		checkOutput(Double.MAX_VALUE);
	}

	@Test
	public void testMin() {
		checkOutput(Double.MIN_VALUE);		
	}

	@Test
	public void testIntegral() {
		checkOutput(Double.valueOf(1999.0));		
	}
	
	@Test
	public void testPI() {
		checkOutput(Double.valueOf(Math.PI));		
	}

	@Test
	public void testNoValue() {
		// "Any floating point number smaller than –10e38 is considered by a shapefile reader to represent a "no data" value.", per ESRI
		checkOutput(Double.valueOf(-1.00001e38));		
	}

}
