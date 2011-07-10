/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing;

import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import org.geotools.factory.Hints;
import org.geotools.factory.GeoTools;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.factory.OrderedAxisAuthorityFactory;


/**
 * Tests if the CRS utility class is functioning correctly when using HSQL datastore.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 * @author Andrea Aime
 */
public class CRSTest extends TestCase {
    /**
     * {@code true} for tracing operations on the standard output.
     */
    private static boolean verbose = false;

    /**
     * Run the suite from the command line.
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Returns the test suite.
     */
    public static Test suite() {
        return new TestSuite(CRSTest.class);
    }

    /**
     * Constructs a test case with the given name.
     */
    public CRSTest(final String name) {
        super(name);
    }

    /**
     * Tests the (latitude, longitude) axis order for EPSG:4326.
     */
    public void testCorrectAxisOrder() throws NoSuchAuthorityCodeException, FactoryException {
        final CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
        final CoordinateSystem cs = crs.getCoordinateSystem();
        assertEquals(2, cs.getDimension());

        CoordinateSystemAxis axis0 = cs.getAxis(0);
        assertEquals("Lat", axis0.getAbbreviation());

        CoordinateSystemAxis axis1 = cs.getAxis(1);
        assertEquals("Long", axis1.getAbbreviation());
    }

    /**
     * Tests the (longitude, latitude) axis order for EPSG:4326.
     */
    public void testForcedAxisOrder() throws NoSuchAuthorityCodeException, FactoryException {
        final CoordinateReferenceSystem crs = CRS.decode("EPSG:4326", true);
        final CoordinateSystem cs = crs.getCoordinateSystem();
        assertEquals(2, cs.getDimension());

        CoordinateSystemAxis axis0 = cs.getAxis(0);
        assertEquals("Long", axis0.getAbbreviation());

        CoordinateSystemAxis axis1 = cs.getAxis(1);
        assertEquals("Lat", axis1.getAbbreviation());

        final CoordinateReferenceSystem standard = CRS.decode("EPSG:4326");
        assertFalse("Should not be (long,lat) axis order.", CRS.equalsIgnoreMetadata(crs, standard));

        final CoordinateReferenceSystem def;
        try {
            assertNull(Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
            def = CRS.decode("EPSG:4326");
        } finally {
            assertEquals(Boolean.TRUE, Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER));
        }
        assertEquals("Expected (long,lat) axis order.", crs, def);
        assertSame("Should be back to (lat,long) axis order.", standard, CRS.decode("EPSG:4326"));
    }

    /**
     * Tests again EPSG:4326, but forced to (longitude, latitude) axis order.
     *
     * @todo Partially uncomment since we are allowed to compile for J2SE 1.5, but doesn't work
     *       as it did prior some changes in the referencing module. Need to investigate why.
     */
    public void testSystemPropertyToForceXY() throws NoSuchAuthorityCodeException, FactoryException {
        assertNull(System.getProperty(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER));
        System.setProperty(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER, "true");
        try {
            CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
            final CoordinateSystem cs = crs.getCoordinateSystem();
            assertEquals(2, cs.getDimension());

            CoordinateSystemAxis axis0  = cs.getAxis(0);
//            assertEquals("forceXY did not work", "Long", axis0.getAbbreviation());

            CoordinateSystemAxis axis1  = cs.getAxis(1);
//            assertEquals("forceXY did not work", "Lat", axis1.getAbbreviation());
        } finally {
            System.clearProperty(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        }
    }

    /**
     * Tests {@link CRS#lookupIdentifier}.
     */
    public void testFind() throws FactoryException {
        CoordinateReferenceSystem crs = getED50("ED50");
        assertEquals("Should find without scan thanks to the name.", "EPSG:4230",
                     CRS.lookupIdentifier(crs, false));
        assertEquals(Integer.valueOf(4230), CRS.lookupEpsgCode(crs, false));

        crs = getED50("ED50 with unknown name");
        assertNull("Should not find the CRS without a scan.",
                   CRS.lookupIdentifier(crs, false));
        assertEquals(null, CRS.lookupEpsgCode(crs, false));

        assertEquals("With scan allowed, should find the CRS.", "EPSG:4230",
                     CRS.lookupIdentifier(crs, true));
        assertEquals(Integer.valueOf(4230), CRS.lookupEpsgCode(crs, true));
    }

    /**
     * Returns a ED50 CRS with the specified name.
     */
    private static CoordinateReferenceSystem getED50(final String name) throws FactoryException {
        final String wkt =
                "GEOGCS[\"" + name + "\",\n" +
                "  DATUM[\"European Datum 1950\",\n" +
                "  SPHEROID[\"International 1924\", 6378388.0, 297.0]],\n" +
                "PRIMEM[\"Greenwich\", 0.0],\n" +
                "UNIT[\"degree\", 0.017453292519943295]]";
        return CRS.parseWKT(wkt);
    }

    /**
     * Tests the {@link CRS#parseWKT} method.
     */
    public void testWKT() throws FactoryException {
        String wkt = "GEOGCS[\"WGS 84\",\n"
                   + "  DATUM[\"WGS_1984\",\n"
                   + "    SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],\n"
                   + "    TOWGS84[0,0,0,0,0,0,0], AUTHORITY[\"EPSG\",\"6326\"]],\n"
                   + "  PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],\n"
                   + "  UNIT[\"DMSH\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9108\"]],\n"
                   + "  AXIS[\"Lat\",NORTH], AXIS[\"Long\",EAST],\n"
                   + "  AUTHORITY[\"EPSG\",\"4326\"]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        assertNotNull(crs);
    }

    /**
     * Makes sure that the transform between two EPSG:4326 is the identity transform.
     */
    public void testFindMathTransformIdentity() throws FactoryException {
        CoordinateReferenceSystem crs1default = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem crs2default = CRS.decode("EPSG:4326");
        MathTransform tDefault = CRS.findMathTransform(crs1default, crs2default);
        assertTrue("WSG84 transformed to WSG84 should be Identity", tDefault.isIdentity());

        CoordinateReferenceSystem crs1force = CRS.decode("EPSG:4326",true);
        CoordinateReferenceSystem crs2force = CRS.decode("EPSG:4326",true);
        MathTransform tForce = CRS.findMathTransform(crs1force, crs2force);
        assertTrue("WSG84 transformed to WSG84 should be Identity", tForce.isIdentity());
    }

    /**
     * Makes sure that the authority factory has the proper name.
     */
    public void testAuthority() {
        CRSAuthorityFactory factory;
        Citation authority;

        // Tests the official factory.
        factory   = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
        authority = factory.getAuthority();
        assertNotNull(authority);
        assertEquals("European Petroleum Survey Group", authority.getTitle().toString(Locale.US));
        assertTrue(Citations.identifierMatches(authority, "EPSG"));

        // Tests the modified factory.
        factory   = new OrderedAxisAuthorityFactory("EPSG", null, null);
        authority = factory.getAuthority();
        assertNotNull(authority);
        assertTrue(Citations.identifierMatches(authority, "EPSG"));
    }

    /**
     * Tests the vendor name.
     */
    public void testVendor() {
        CRSAuthorityFactory factory;
        Citation vendor;

        factory = new OrderedAxisAuthorityFactory("EPSG", null, null);
        vendor  = factory.getVendor();
        assertNotNull(vendor);
        assertEquals("Geotools", vendor.getTitle().toString(Locale.US));
        assertFalse(Citations.identifierMatches(vendor, "EPSG"));
    }

    /**
     * Tests the amount of codes available.
     */
    public void testCodes() throws FactoryException {
        final CRSAuthorityFactory factory = new OrderedAxisAuthorityFactory("EPSG", null, null);
        final Set codes = factory.getAuthorityCodes(CoordinateReferenceSystem.class);
        assertNotNull(codes);
        assertTrue(codes.size() >= 3000);
    }

    /**
     * A random CRS for fun.
     */
    public void test26910() throws FactoryException {
        final CRSAuthorityFactory factory = new OrderedAxisAuthorityFactory("EPSG", null, null);
        final CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:26910");
        assertNotNull(crs);
        assertSame(crs, factory.createObject("EPSG:26910"));
    }

    /**
     * UDIG requires this to work.
     */
    public void test4326() throws FactoryException {
        final CRSAuthorityFactory factory = new OrderedAxisAuthorityFactory("EPSG", null, null);
        final CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4326");
        assertNotNull(crs);
        assertSame(crs, factory.createObject("EPSG:4326"));
    }

    /**
     * UDIG requires this to work.
     */
    public void test4269() throws FactoryException {
        final CRSAuthorityFactory factory = new OrderedAxisAuthorityFactory("EPSG", null, null);
        final CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4269");
        assertNotNull(crs);
        assertSame(crs, factory.createObject("EPSG:4269"));
    }

    /**
     * A random CRS for fun.
     */
    public void test26910Lower() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("epsg:26910");
        assertNotNull(crs);
    }

    /**
     * A random CRS for fun.
     */
    public void test26986Lower() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("epsg:26986");
        assertNotNull(crs);
    }

    /**
     * WFS requires this to work.
     */
    public void test4326Lower() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("epsg:4326");
        assertNotNull(crs);
    }

    /**
     * WFS requires this to work.
     */
    public void test26742Lower() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("epsg:26742");
        assertNotNull(crs);
    }

    /**
     * WFS requires this to work.
     */
    public void test4269Lower() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("epsg:4269");
        assertNotNull(crs);
    }
    
    /**
     * Check that a code with a axis direction with a reference to W works
     * @throws FactoryException
     */
    public void testWestDirection() throws FactoryException {
        // see GEOT-2901
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3573");
        assertNotNull(crs);
    }
    
    /**
     * Check we support Plate Carré projection
     * @throws FactoryException
     */
    public void testPlateCarre() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:32662");
        assertNotNull(crs);
        
        MathTransform mt = CRS.findMathTransform(DefaultGeographicCRS.WGS84, crs);
        double[] source = new double[] {-20, 35};
        double[] dest = new double[2];
        mt.transform(source, 0, dest, 0, 1);
        
        // reference values computed using cs2cs +init=epsg:4326 +to +init=epsg:32662
        assertEquals(-2226389.82, dest[0], 0.01);
        assertEquals(3896182.18, dest[1], 0.01);
    }

    /**
     * Tests {@link CRS#getHorizontalCRS} from a compound CRS.
     */
    public void testHorizontalFromCompound() throws FactoryException {
        // retrives "NTF (Paris) / France II + NGF Lallemand"
        CoordinateReferenceSystem compound = CRS.decode("EPSG:7401");
        CoordinateReferenceSystem horizontal = CRS.getHorizontalCRS(compound);
        // compares with "NTF (Paris) / France II"
        assertEquals(CRS.decode("EPSG:27582"), horizontal);
    }

    /**
     * Tests {@link CRS#getHorizontalCRS} from a Geographic 3D CR.
     */
    public void testHorizontalFromGeodetic() throws FactoryException {
        // retrives "WGS 84 (geographic 3D)"
        CoordinateReferenceSystem compound = CRS.decode("EPSG:4327");
        CoordinateReferenceSystem horizontal = CRS.getHorizontalCRS(compound);
        // the horizonal version is basically 4326, but it won't compare positively
        // with 4326, not even using CRS.equalsIgnoreMetadata(), so we check the axis directly
        CoordinateSystem cs = horizontal.getCoordinateSystem();
        assertEquals(2, cs.getDimension());
        assertEquals(AxisDirection.NORTH, cs.getAxis(0).getDirection());
        assertEquals(AxisDirection.EAST, cs.getAxis(1).getDirection());
    }

    /**
     * Tests the number of CRS that can be created. This test will be executed only if this test
     * suite is run with the {@code -verbose} option provided on the command line.
     */
    public void testSuccess() throws FactoryException {
        if (!verbose) {
            return;
        }
        final CRSAuthorityFactory factory = new OrderedAxisAuthorityFactory("EPSG", null, null);
        Set codes = factory.getAuthorityCodes(CoordinateReferenceSystem.class);
        int total = codes.size();
        int count = 0;
        for (Iterator i=codes.iterator(); i.hasNext();) {
            CoordinateReferenceSystem crs;
            String code = (String) i.next();
            try {
                crs = factory.createCoordinateReferenceSystem(code);
                assertNotNull(crs);
                count++;
            } catch (FactoryException e) {
                System.err.println("WARNING (CRS: "+code+" ):" + e.getMessage());
            }
        }
        System.out.println("Success: " + count + "/" + total + " (" + (count*100)/total + "%)");
    }
    
    public void testSRSAxisOrder() throws Exception {
        try {
            CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
            assertEquals("EPSG:4326", CRS.toSRS(crs));
            Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
            assertEquals("urn:ogc:def:crs:EPSG::4326", CRS.toSRS(crs));
        } finally {
            Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        }

    }
}
