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
package org.geotools.measure;

import java.text.Format;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.opengis.geometry.MismatchedDimensionException;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.crs.DefaultCompoundCRS;
import org.geotools.referencing.crs.AbstractCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultTemporalCRS;
import org.geotools.referencing.crs.DefaultVerticalCRS;
import org.geotools.referencing.cs.DefaultTimeCS;
import org.geotools.referencing.datum.DefaultTemporalDatum;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests formatting done by the {@link CoordinateFormat} class.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class FormatTest {
    
    @Before
    public void setUp() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @After
    public void tearDown() {
        AngleFormat.setDefaultRoundingMethod(AngleFormat.DEFAULT_ROUNDING_METHOD);
    }

    /**
     * Test {@link AngleFormat}.
     *
     * @throws ParseException If an angle can't be parsed.
     */
    @Test
    public void testAngleFormat() throws ParseException {
        AngleFormat f = new AngleFormat("DD.ddd\u00B0", Locale.CANADA);
        assertFormat( "20.000\u00B0",  new Angle   ( 20.000), f);
        assertFormat( "20.749\u00B0",  new Angle   ( 20.749), f);
        assertFormat("-12.247\u00B0",  new Angle   (-12.247), f);
        assertFormat( "13.214\u00B0N", new Latitude( 13.214), f);
        assertFormat( "12.782\u00B0S", new Latitude(-12.782), f);

        f = new AngleFormat("DD.ddd\u00B0", Locale.FRANCE);
        assertFormat("19,457\u00B0E", new Longitude( 19.457), f);
        assertFormat("78,124\u00B0S", new Latitude (-78.124), f);

        f = new AngleFormat("DDddd", Locale.CANADA);
        assertFormat("19457E", new Longitude( 19.457), f);
        assertFormat("78124S", new Latitude (-78.124), f);

        f = new AngleFormat("DD\u00B0MM.m", Locale.CANADA);
        assertFormat( "12\u00B030.0", new Angle( 12.50), f);
        assertFormat("-10\u00B015.0", new Angle(-10.25), f);
    }

    /**
     * Formats an object and parse the result. The format
     * output is compared with the expected output.
     */
    private static void assertFormat(final String expected,
                                     final Object value,
                                     final Format format) throws ParseException
    {
        final String label = value.toString();
        final String text  = format.format(value);
        assertEquals("Formatting of \""+label+'"', expected, text);
        assertEquals("Parsing of \""   +label+'"', value, format.parseObject(text));
    }

    /**
     * Tests formatting of a 4-dimensional coordinates.
     */
    @Test
    public void testCoordinateFormat() {
        final Date epoch = new Date(1041375600000L); // January 1st, 2003
        final DefaultTemporalDatum datum = new DefaultTemporalDatum("Time", epoch);
        final AbstractCRS crs = new DefaultCompoundCRS("WGS84 3D + time",
                    DefaultGeographicCRS.WGS84, DefaultVerticalCRS.ELLIPSOIDAL_HEIGHT,
                    new DefaultTemporalCRS("Time", datum, DefaultTimeCS.DAYS));
        final CoordinateFormat format = new CoordinateFormat(Locale.FRANCE);
        format.setCoordinateReferenceSystem(crs);
        format.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
        final GeneralDirectPosition position = new GeneralDirectPosition(new double[] {
            23.78, -12.74, 127.9, 3.2
        });        
        format.setDatePattern("dd MM yyyy");        
        assertEquals("23°46,8'E 12°44,4'S 127,9\u00A0m 04 01 2003", format.format(position));
        /*
         * Try a point with wrong dimension.
         */
        final GeneralDirectPosition wrong = new GeneralDirectPosition(new double[] {
            23.78, -12.74, 127.9, 3.2, 8.5
        });
        try {
            assertNotNull(format.format(wrong));
            fail("Excepted a mismatched dimension exception.");
        } catch (MismatchedDimensionException e) {
            // This is the expected dimension.
        }
        /*
         * Try a null CRS. Should formats everything as numbers.
         */
        format.setCoordinateReferenceSystem(null);
        assertEquals("23,78 -12,74 127,9 3,2",     format.format(position));
        assertEquals("23,78 -12,74 127,9 3,2 8,5", format.format(wrong));
        /*
         * Try again the original CRS, but different separator.
         */
        format.setCoordinateReferenceSystem(crs);
        format.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
        format.setSeparator("; ");
        format.setDatePattern("dd MM yyyy");        
        assertEquals("23°46,8'E; 12°44,4'S; 127,9\u00A0m; 04 01 2003", format.format(position));
     }

     @Test
     public void testInstanceRoundingMethod() {
         double value = 2.5d;
         AngleFormat af = new AngleFormat("D");
         assertEquals("2", af.format(value));

         af.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_UP);
         assertEquals("3", af.format(value));
         
         af.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_DOWN);
         assertEquals("2", af.format(value));

         value = 3.5d;
         af.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_EVEN);
         assertEquals("4", af.format(value));

         af = new AngleFormat("D M.m");
         value = 30.45d / 60d ;
         assertEquals("0 30.4", af.format(value));

         af.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_UP);
         assertEquals("0 30.5", af.format(value));

         af.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_DOWN);
         assertEquals("0 30.4", af.format(value));

         value = 30.55d / 60d;
         af.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_EVEN);
         assertEquals("0 30.6", af.format(value));
         
         af = new AngleFormat("D M S.s");
         value = 3661.45d / 3600d;
         assertEquals("1 1 1.4", af.format(value));

         af.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_UP);
         assertEquals("1 1 1.5", af.format(value));

         af.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_DOWN);
         assertEquals("1 1 1.4", af.format(value));

         value = 3661.55d / 3600d;
         af.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_EVEN);
         assertEquals("1 1 1.6", af.format(value));
     }

     @Test
     public void testSetGlobalRoundingMethod() {
         double value = 3.5d;
         AngleFormat af1 = new AngleFormat("D");
         // set the instance rounding method for af1
         af1.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_DOWN);

         // set the default rounding method for all future instances
         AngleFormat.setDefaultRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_UP);
         AngleFormat af2 = new AngleFormat("D");

         // af1 should still be using ROUND_HALF_DOWN
         assertEquals("3", af1.format(value));

         // af2 should be using ROUND_HALF_UP
         assertEquals("4", af2.format(value));
     }

 	@Test
 	public void testOverflow() {

 		AngleFormat af = new AngleFormat("DD MM SS.ss");
 		af.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_EVEN);
 		double value = 0d;
 		assertEquals("00 00 00.00", af.format(value));

 		value = 0.001d / 3600d;
 		assertEquals("00 00 00.00", af.format(value));

 		value = 0.01d / 3600d;
 		assertEquals("00 00 00.01", af.format(value));

 		value = 59.99d / 3600d;
 		assertEquals("00 00 59.99", af.format(value));

 		value = 59.999d / 3600d;
 		assertEquals("00 01 00.00", af.format(value));

 		value = 1.5d / 60d + 29.99d / 3600d;
 		assertEquals("00 01 59.99", af.format(value));

 		value = 1.5d / 60d + 29.999d / 3600d;
 		assertEquals("00 02 00.00", af.format(value));

 		value = 0d + 59d / 60d + 59.99d / 3600d;
 		assertEquals("00 59 59.99", af.format(value));

 		value = 0d + 59d / 60d + 59.999d / 3600d;
 		assertEquals("01 00 00.00", af.format(value));

 		value = 359d + 59d / 60d + 59.99d / 3600d;
 		assertEquals("359 59 59.99", af.format(value));

 		value = 359d + 59d / 60d + 59.999d / 3600d;
 		assertEquals("00 00 00.00", af.format(value));

 		af = new AngleFormat("DD MM SS");
 		af.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_EVEN);
 		value = 59d / 3600d;
 		assertEquals("00 00 59", af.format(value));

 		value = 59.9d / 3600d;
 		assertEquals("00 01 00", af.format(value));

 		value = 59d / 60d;
 		assertEquals("00 59 00", af.format(value));

 		value = 59.999d / 60d;
 		assertEquals("01 00 00", af.format(value));

 		value = 0d + 59d / 60d + 59.9d / 3600d;
 		assertEquals("01 00 00", af.format(value));

 		af = new AngleFormat("DD MM");
 		af.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_EVEN);
 		value = 0d + 59d / 60d + 59.9d / 3600d;
 		assertEquals("01 00", af.format(value));

 		value = 361;
 		assertEquals("01 00", af.format(value));

 		af = new AngleFormat("DDD");
 		af.setRoundingMethod(AngleFormat.RoundingMethod.ROUND_HALF_EVEN);
 		value = 0.9999;
 		assertEquals("001", af.format(value));

 		value = 361.1111;
 		assertEquals("001", af.format(value));

 		value = 361.999;
 		assertEquals("002", af.format(value));

 	}
}
