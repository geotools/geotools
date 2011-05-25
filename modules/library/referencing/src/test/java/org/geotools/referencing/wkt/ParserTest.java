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
package org.geotools.referencing.wkt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;

import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.operation.MathTransform;

import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.ScriptRunner;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.test.TestData;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the WKT {@link Parser} implementation.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Yann Cézard
 * @author Remi Eve
 * @author Martin Desruisseaux (IRD)
 */
public final class ParserTest {
    /**
     * Test a hard coded version of a WKT. This is more convenient for debugging.
     */
    @Test
    public void testHardCoded() throws ParseException {
        final Parser parser = new Parser();
        String              wkt1, wkt2;
        DefaultProjectedCRS crs1, crs2;
        ParameterValueGroup param;
        /*
         * First, rather simple Mercator projection.
         * Uses standard units and axis order.
         */
        wkt1 = "PROJCS[\"Mercator test\",\n"                             +
               "  GEOGCS[\"WGS84\",\n"                                   +
               "    DATUM[\"WGS84\",\n"                                  +
               "      SPHEROID[\"WGS84\", 6378137.0, 298.257223563]],\n" +
               "    PRIMEM[\"Greenwich\", 0.0],\n"                       +
               "    UNIT[\"degree\", 0.017453292519943295],\n"           +
               "    AXIS[\"Longitude\", EAST],\n"                        +
               "    AXIS[\"Latitude\", NORTH]],\n"                       +
               "  PROJECTION[\"Mercator_1SP\"],\n"                       +
               "  PARAMETER[\"central_meridian\", -20.0],\n"             +
               "  PARAMETER[\"scale_factor\", 1.0],\n"                   +
               "  PARAMETER[\"false_easting\", 500000.0],\n"             +
               "  PARAMETER[\"false_northing\", 0.0],\n"                 +
               "  UNIT[\"m\", 1.0],\n"                                   +
               "  AXIS[\"x\", EAST],\n"                                  +
               "  AXIS[\"y\", NORTH]]\n";
        assertTrue(Symbols.DEFAULT.containsAxis(wkt1));
        crs1  = (DefaultProjectedCRS) parser.parseObject(wkt1);
        wkt2  = parser.format(crs1);
        crs2  = (DefaultProjectedCRS) parser.parseObject(wkt2);
        param = crs1.getConversionFromBase().getParameterValues();
        assertEquals(crs1, crs2);
        assertEquals("Mercator_1SP", crs1.getConversionFromBase().getMethod().getName().getCode());
        assertTrue(crs1.getConversionFromBase().getMathTransform().toWKT().startsWith("PARAM_MT[\"Mercator_1SP\""));
        assertFalse (wkt2.contains("semi_major"));
        assertFalse (wkt2.contains("semi_minor"));
        assertEquals("semi_major",   6378137.0, param.parameter("semi_major"      ).doubleValue(), 1E-4);
        assertEquals("semi_minor",   6356752.3, param.parameter("semi_minor"      ).doubleValue(), 1E-1);
        assertEquals("central_meridian", -20.0, param.parameter("central_meridian").doubleValue(), 1E-8);
        assertEquals("scale_factor",       1.0, param.parameter("scale_factor"    ).doubleValue(), 1E-8);
        assertEquals("false_easting", 500000.0, param.parameter("false_easting"   ).doubleValue(), 1E-4);
        assertEquals("false_northing",     0.0, param.parameter("false_northing"  ).doubleValue(), 1E-4);
        /*
         * Same Mercator projection as above, but
         * switch longitude and latitude axis.
         */
        wkt1 = "PROJCS[\"Mercator test\",\n"                             +
               "  GEOGCS[\"WGS84\",\n"                                   +
               "    DATUM[\"WGS84\",\n"                                  +
               "      SPHEROID[\"WGS84\", 6378137.0, 298.257223563]],\n" +
               "    PRIMEM[\"Greenwich\", 0.0],\n"                       +
               "    UNIT[\"degree\", 0.017453292519943295],\n"           +
               "    AXIS[\"Latitude\", NORTH],\n"                        +
               "    AXIS[\"Longitude\", EAST]],\n"                       +
               "  PROJECTION[\"Mercator_1SP\"],\n"                       +
               "  PARAMETER[\"central_meridian\", -20.0],\n"             +
               "  PARAMETER[\"scale_factor\", 1.0],\n"                   +
               "  PARAMETER[\"false_easting\", 500000.0],\n"             +
               "  PARAMETER[\"false_northing\", 0.0],\n"                 +
               "  UNIT[\"m\", 1.0],\n"                                   +
               "  AXIS[\"x\", EAST],\n"                                  +
               "  AXIS[\"y\", NORTH]]\n";
        assertTrue(Symbols.DEFAULT.containsAxis(wkt1));
        crs1  = (DefaultProjectedCRS) parser.parseObject(wkt1);
        wkt2  = parser.format(crs1);
        crs2  = (DefaultProjectedCRS) parser.parseObject(wkt2);
        param = crs1.getConversionFromBase().getParameterValues();
        assertEquals(crs1, crs2);
        assertEquals("Mercator_1SP", crs1.getConversionFromBase().getMethod().getName().getCode());
        assertTrue(crs1.getConversionFromBase().getMathTransform().toWKT().startsWith("CONCAT_MT[PARAM_MT["));
        assertFalse (wkt2.contains("semi_major"));
        assertFalse (wkt2.contains("semi_minor"));
        assertEquals("semi_major",   6378137.0, param.parameter("semi_major"      ).doubleValue(), 1E-4);
        assertEquals("semi_minor",   6356752.3, param.parameter("semi_minor"      ).doubleValue(), 1E-1);
        assertEquals("central_meridian", -20.0, param.parameter("central_meridian").doubleValue(), 1E-8);
        assertEquals("scale_factor",       1.0, param.parameter("scale_factor"    ).doubleValue(), 1E-8);
        assertEquals("false_easting", 500000.0, param.parameter("false_easting"   ).doubleValue(), 1E-4);
        assertEquals("false_northing",     0.0, param.parameter("false_northing"  ).doubleValue(), 1E-4);
        /*
         * Try an other projection (Transverse Mercator).
         */
        wkt1 = "PROJCS[\"OSGB 1936 / British National Grid\",\n"                                          +
               "  GEOGCS[\"OSGB 1936\",\n"                                                                +
               "    DATUM[\"OSGB_1936\",\n"                                                               +
               "      SPHEROID[\"Airy 1830\", 6377563.396, 299.3249646, AUTHORITY[\"EPSG\",\"7001\"]],\n" +
               "      TOWGS84[375.0, -111.0, 431.0, 0.0, 0.0, 0.0, 0.0],\n"                               +
               "      AUTHORITY[\"EPSG\",\"6277\"]],\n"                                                   +
               "    PRIMEM[\"Greenwich\",0.0, AUTHORITY[\"EPSG\",\"8901\"]],\n"                           +
               "    UNIT[\"DMSH\",0.0174532925199433, AUTHORITY[\"EPSG\",\"9108\"]],\n"                   +
               "    AXIS[\"Lat\",NORTH],AXIS[\"Long\",EAST], AUTHORITY[\"EPSG\",\"4277\"]],\n"            +
               "  PROJECTION[\"Transverse_Mercator\"],\n"                                                 +
               "  PARAMETER[\"latitude_of_origin\", 49.0],\n"                                             +
               "  PARAMETER[\"central_meridian\", -2.0],\n"                                               +
               "  PARAMETER[\"scale_factor\", 0.999601272],\n"                                            +
               "  PARAMETER[\"false_easting\", 400000.0],\n"                                              +
               "  PARAMETER[\"false_northing\", -100000.0],\n"                                            +
               "  UNIT[\"metre\", 1.0, AUTHORITY[\"EPSG\",\"9001\"]],\n"                                  +
               "  AXIS[\"E\",EAST],\n"                                                                    +
               "  AXIS[\"N\",NORTH],\n"                                                                   +
               "  AUTHORITY[\"EPSG\",\"27700\"]]\n";
        assertTrue(Symbols.DEFAULT.containsAxis(wkt1));
        crs1  = (DefaultProjectedCRS) parser.parseObject(wkt1);
        wkt2  = parser.format(crs1);
        crs2  = (DefaultProjectedCRS) parser.parseObject(wkt2);
        param = crs1.getConversionFromBase().getParameterValues();
        assertEquals(crs1, crs2);
        assertFalse (wkt2.contains("semi_major"));
        assertFalse (wkt2.contains("semi_minor"));
        assertEquals("Transverse_Mercator", crs1.getConversionFromBase().getMethod().getName().getCode());
        assertEquals("semi_major",   6377563.396, param.parameter("semi_major"        ).doubleValue(), 1E-4);
        assertEquals("semi_minor",   6356256.909, param.parameter("semi_minor"        ).doubleValue(), 1E-3);
        assertEquals("latitude_of_origin",  49.0, param.parameter("latitude_of_origin").doubleValue(), 1E-8);
        assertEquals("central_meridian",    -2.0, param.parameter("central_meridian"  ).doubleValue(), 1E-8);
        assertEquals("scale_factor",      0.9996, param.parameter("scale_factor"      ).doubleValue(), 1E-5);
        assertEquals("false_easting",   400000.0, param.parameter("false_easting"     ).doubleValue(), 1E-4);
        assertEquals("false_northing", -100000.0, param.parameter("false_northing"    ).doubleValue(), 1E-4);
        /*
         * Try a projection with feet units.
         */
        wkt1 = "PROJCS[\"TransverseMercator\",\n"                     +
               "  GEOGCS[\"Sphere\",\n"                               +
               "    DATUM[\"Sphere\",\n"                              +
               "      SPHEROID[\"Sphere\", 6370997.0, 0.0],\n"        +
               "      TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]],\n" +
               "    PRIMEM[\"Greenwich\", 0.0],\n"                    +
               "    UNIT[\"degree\", 0.017453292519943295],\n"        +
               "    AXIS[\"Longitude\", EAST],\n"                     +
               "    AXIS[\"Latitude\", NORTH]],\n"                    +
               "  PROJECTION[\"Transverse_Mercator\",\n"              +
               "    AUTHORITY[\"OGC\",\"Transverse_Mercator\"]],\n"   +
               "  PARAMETER[\"central_meridian\", 170.0],\n"          +
               "  PARAMETER[\"latitude_of_origin\", 50.0],\n"         +
               "  PARAMETER[\"scale_factor\", 0.95],\n"               +
               "  PARAMETER[\"false_easting\", 0.0],\n"               +
               "  PARAMETER[\"false_northing\", 0.0],\n"              +
               "  UNIT[\"feet\", 0.304800609601219],\n"               +
               "  AXIS[\"x\", EAST],\n"                               +
               "  AXIS[\"y\", NORTH]]\n";
        assertTrue(Symbols.DEFAULT.containsAxis(wkt1));
        crs1  = (DefaultProjectedCRS) parser.parseObject(wkt1);
        wkt2  = parser.format(crs1);
        crs2  = (DefaultProjectedCRS) parser.parseObject(wkt2);
        param = crs1.getConversionFromBase().getParameterValues();
        assertEquals(crs1, crs2);
        assertFalse (wkt2.contains("semi_major"));
        assertFalse (wkt2.contains("semi_minor"));
        assertEquals("Transverse_Mercator", crs1.getConversionFromBase().getMethod().getName().getCode());
        assertEquals("semi_major",     6370997.0, param.parameter("semi_major"        ).doubleValue(), 1E-5);
        assertEquals("semi_minor",     6370997.0, param.parameter("semi_minor"        ).doubleValue(), 1E-5);
        assertEquals("latitude_of_origin",  50.0, param.parameter("latitude_of_origin").doubleValue(), 1E-8);
        assertEquals("central_meridian",   170.0, param.parameter("central_meridian"  ).doubleValue(), 1E-8);
        assertEquals("scale_factor",        0.95, param.parameter("scale_factor"      ).doubleValue(), 1E-8);
        assertEquals("false_easting",        0.0, param.parameter("false_easting"     ).doubleValue(), 1E-8);
        assertEquals("false_northing",       0.0, param.parameter("false_northing"    ).doubleValue(), 1E-8);
        /*
         * Try with a number using scientific notation.
         */
        wkt1 = "GEOGCS[\"NAD83 / NFIS Seconds\",DATUM[\"North_American_Datum_1983\",\n" +
               "  SPHEROID[\"GRS 1980\", 6378137, 298.257222101]],\n"                   +
               "  PRIMEM[\"Greenwich\", 0],\n"                                          +
               "  UNIT[\"Decimal_Second\", 4.84813681109536e-06],\n"                    +
               "  AUTHORITY[\"EPSG\", \"100001\"]]";
        assertFalse(Symbols.DEFAULT.containsAxis(wkt1));
        wkt2 = parser.format(parser.parseObject(wkt1));
        assertFalse(wkt2.contains("semi_major"));
        assertFalse(wkt2.contains("semi_minor"));
    }

    /**
     * Tests the Oracle variant of WKT.
     */
    @Test
    public void testOracleWKT() throws ParseException {
        final String wkt =
                "PROJCS[\"Datum 73 / Modified Portuguese Grid\"," +
                 " GEOGCS [ \"Datum 73\"," +
                   " DATUM[\"Datum 73 (EPSG ID 6274)\"," +
                     " SPHEROID [\"International 1924 (EPSG ID 7022)\", 6378388, 297]," +
                     " -231, 102.6, 29.8, .6149999999999993660366746131394108039579," +
                     " -.1979999999999997958947342656936639661522," +
                      " .8809999999999990918346509498793836069706, .99999821]," +
                   " PRIMEM [ \"Greenwich\", 0.000000 ]," +
                   " UNIT [\"Decimal Degree\", 0.01745329251994328]]," +
                 " PROJECTION[\"Transverse_Mercator\"]," +
//               " PROJECTION[\"Modified Portuguese Grid (EPSG OP 19974)\"]," +
// TODO: The real projection is "Modified Portugues", but it is not yet implemented in Geotools.
                 " PARAMETER[\"Latitude_Of_Origin\", 39.66666666666666666666666666666666666667]," +
                 " PARAMETER[\"Central_Meridian\", -8.13190611111111111111111111111111111111]," +
                 " PARAMETER[\"Scale_Factor\", 1]," +
                 " PARAMETER [\"False_Easting\", 180.598]," +
                 " PARAMETER[\"False_Northing\", -86.99]," +
                 " UNIT [\"Meter\", 1]]";
        assertFalse(Symbols.DEFAULT.containsAxis(wkt));
        final Parser parser = new Parser();
        CoordinateReferenceSystem crs1 = parser.parseCoordinateReferenceSystem(wkt);
        final String check = parser.format(crs1);
        assertTrue(check.indexOf("TOWGS84[-231") >= 0);
        CoordinateReferenceSystem crs2 = parser.parseCoordinateReferenceSystem(check);
        assertEquals(crs1, crs2);
        assertFalse(check.contains("semi_major"));
        assertFalse(check.contains("semi_minor"));
    }

    /**
     * Tests parsing with custom axis length. At the difference of the previous test,
     * the WKT formatting in this test should include the axis length as parameter values.
     */
    @Test
    public void testCustomAxisLength() throws FactoryException, ParseException {
        DefaultMathTransformFactory factory = new DefaultMathTransformFactory();
        ParameterValueGroup parameters = factory.getDefaultParameters("Lambert_Conformal_Conic_2SP");

        final double majorAxis = 6.3712e+6;
        final double minorAxis = 6.3712e+6;
        parameters.parameter("semi_major").setValue(majorAxis);
        parameters.parameter("semi_minor").setValue(minorAxis);
        parameters.parameter("latitude_of_origin").setValue(25.0);
        parameters.parameter("standard_parallel_1").setValue(25.0);
        parameters.parameter("standard_parallel_2").setValue(25.0);
        parameters.parameter("longitude_of_origin").setValue(-95.0);
        parameters.parameter("false_easting").setValue(0.0);
        parameters.parameter("false_northing").setValue(0.0);

        GeographicCRS base = DefaultGeographicCRS.WGS84;
        MathTransform mt   = factory.createParameterizedTransform(parameters);
        CartesianCS cs = DefaultCartesianCS.PROJECTED;
        CoordinateReferenceSystem crs = new DefaultProjectedCRS("Lambert", base, mt, cs);

        final String wkt = crs.toWKT();
        assertTrue(wkt.contains("semi_major"));
        assertTrue(wkt.contains("semi_minor"));
        final Parser parser = new Parser();
        CoordinateReferenceSystem check = parser.parseCoordinateReferenceSystem(wkt);
        assertEquals(wkt, check.toWKT());
    }

    /**
     * Tests parsing of math transforms.
     */
    @Test
    public void testMathTransform() throws IOException, ParseException {
        testParsing(new MathTransformParser(), "wkt/MathTransform.txt");
    }

    /**
     * Tests parsing of coordinate reference systems.
     */
    @Test
    public void testCoordinateReferenceSystem() throws IOException, ParseException {
        testParsing(new Parser(), "wkt/CoordinateReferenceSystem.txt");
    }

    /**
     * Parses all elements from the specified file. Parsing creates a set of
     * geographic objects. No special processing are done with them; we just
     * check if the parsing work without error and produces distincts objects.
     */
    private void testParsing(final AbstractParser parser, final String filename)
            throws IOException, ParseException
    {
        final BufferedReader reader = TestData.openReader(ScriptRunner.class, filename);
        if (reader == null) {
            throw new FileNotFoundException(filename);
        }
        final Collection<Object> pool = new HashSet<Object>();
        String line;
        while ((line=reader.readLine()) != null) {
            line = line.trim();
            if (line.length()==0 || line.startsWith("#")) {
                continue;
            }
            /*
             * Parses a line. If the parse fails, then dump the WKT and rethrow the exception.
             */
            final Object parsed;
            try {
                parsed = parser.parseObject(line);
            } catch (ParseException exception) {
                System.err.println();
                System.err.println("-----------------------------");
                System.err.println("Parse failed. Dump WKT below.");
                System.err.println("-----------------------------");
                System.err.println(line);
                System.err.println();
                throw exception;
            }
            assertNotNull("Parsing returns null.",                 parsed);
            assertEquals("Inconsistent equals method",             parsed, parsed);
            assertSame("Parsing twice returns different objects.", parsed, parser.parseObject(line));
            assertTrue("An identical object already exists.",      pool.add(parsed));
            assertTrue("Inconsistent hashCode or equals method.",  pool.contains(parsed));
            /*
             * Formats the object and parse it again.
             * Ensures that the result is consistent.
             */
            String formatted = parser.format(parsed);
            final Object again;
            try {
                again = parser.parseObject(formatted);
            } catch (ParseException exception) {
                System.err.println();
                System.err.println("------------------------------------");
                System.err.println("Second parse failed. Dump WKT below.");
                System.err.println("------------------------------------");
                System.err.println(line);
                System.err.println();
                System.err.println("------ Reformatted WKT below -------");
                System.err.println();
                System.err.println(formatted);
                System.err.println();
                throw exception;
            }
            assertEquals("Second parsing produced different objects", parsed, again);
            assertTrue("Inconsistent hashCode or equals method.",  pool.contains(again));
        }
        reader.close();
    }
    
    /**
     * Test if the parser handles string and number coded Authority code-values. 
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testAuthorityCodeParsing() throws IOException, ParseException {
        testParsing(new Parser(), "wkt/AuthorityCode.txt");
    }    
}
