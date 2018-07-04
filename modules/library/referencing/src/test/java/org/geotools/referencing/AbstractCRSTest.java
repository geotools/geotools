/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.factory.OrderedAxisAuthorityFactory;
import org.geotools.referencing.operation.projection.LambertConformal1SP;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.referencing.operation.projection.TransverseMercator;
import org.geotools.test.OnlineTestCase;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Tests if the CRS utility class is functioning correctly when using HSQL datastore.
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 * @author Andrea Aime
 */
public abstract class AbstractCRSTest extends OnlineTestCase {
    /** {@code true} for tracing operations on the standard output. */
    private static boolean verbose = false;

    protected void tearDownInternal() throws Exception {
        System.clearProperty("org.geotools.referencing.forceXY");
        Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        CRS.reset("all");
    }

    /** Tests the (latitude, longitude) axis order for EPSG:4326. */
    public void testCorrectAxisOrder() throws NoSuchAuthorityCodeException, FactoryException {
        final CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
        final CoordinateSystem cs = crs.getCoordinateSystem();
        assertEquals(2, cs.getDimension());

        CoordinateSystemAxis axis0 = cs.getAxis(0);
        assertEquals("Lat", axis0.getAbbreviation());

        CoordinateSystemAxis axis1 = cs.getAxis(1);
        assertEquals("Long", axis1.getAbbreviation());
    }

    /** Tests the (longitude, latitude) axis order for EPSG:4326. */
    public void testForcedAxisOrder() throws NoSuchAuthorityCodeException, FactoryException {
        final CoordinateReferenceSystem crs = CRS.decode("EPSG:4326", true);
        final CoordinateSystem cs = crs.getCoordinateSystem();
        assertEquals(2, cs.getDimension());

        CoordinateSystemAxis axis0 = cs.getAxis(0);
        assertEquals("Long", axis0.getAbbreviation());

        CoordinateSystemAxis axis1 = cs.getAxis(1);
        assertEquals("Lat", axis1.getAbbreviation());

        final CoordinateReferenceSystem standard = CRS.decode("EPSG:4326");
        assertFalse(
                "Should not be (long,lat) axis order.", CRS.equalsIgnoreMetadata(crs, standard));

        final CoordinateReferenceSystem def;
        try {
            assertNull(
                    Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
            def = CRS.decode("EPSG:4326");
        } finally {
            assertEquals(
                    Boolean.TRUE,
                    Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER));
        }
        assertEquals("Expected (long,lat) axis order.", crs, def);
        assertSame("Should be back to (lat,long) axis order.", standard, CRS.decode("EPSG:4326"));
    }

    /**
     * Tests again EPSG:4326, but forced to (longitude, latitude) axis order.
     *
     * @todo Partially uncomment since we are allowed to compile for J2SE 1.5, but doesn't work as
     *     it did prior some changes in the referencing module. Need to investigate why.
     */
    public void testSystemPropertyToForceXY()
            throws NoSuchAuthorityCodeException, FactoryException {
        assertNull(System.getProperty(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER));
        System.setProperty(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER, "true");
        try {
            CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
            final CoordinateSystem cs = crs.getCoordinateSystem();
            assertEquals(2, cs.getDimension());

            cs.getAxis(0);

            cs.getAxis(1);
        } finally {
            System.clearProperty(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        }
    }

    /** Tests {@link CRS#lookupIdentifier}. */
    public void testFind() throws FactoryException {
        CoordinateReferenceSystem crs = getED50("ED50");
        assertEquals(
                "Should find without scan thanks to the name.",
                "EPSG:4230",
                CRS.lookupIdentifier(crs, false));
        assertEquals(Integer.valueOf(4230), CRS.lookupEpsgCode(crs, false));

        crs = getED50("ED50 with unknown name");
        if (supportsED50QuickScan()) {
            assertEquals(
                    "With scan allowed, should find the CRS.",
                    "EPSG:4230",
                    CRS.lookupIdentifier(crs, false));
            assertEquals(Integer.valueOf(4230), CRS.lookupEpsgCode(crs, false));
        } else {
            assertNull("Should not find the CRS without a scan.", CRS.lookupIdentifier(crs, false));
            assertEquals(null, CRS.lookupEpsgCode(crs, false));
        }

        assertEquals(
                "With scan allowed, should find the CRS.",
                "EPSG:4230",
                CRS.lookupIdentifier(crs, true));
        assertEquals(Integer.valueOf(4230), CRS.lookupEpsgCode(crs, true));
    }

    protected boolean supportsED50QuickScan() {
        return false;
    }

    /** Returns a ED50 CRS with the specified name. */
    private static CoordinateReferenceSystem getED50(final String name) throws FactoryException {
        final String wkt =
                "GEOGCS[\""
                        + name
                        + "\",\n"
                        + "  DATUM[\"European Datum 1950\",\n"
                        + "  SPHEROID[\"International 1924\", 6378388.0, 297.0]],\n"
                        + "PRIMEM[\"Greenwich\", 0.0],\n"
                        + "UNIT[\"degree\", 0.017453292519943295]]";
        return CRS.parseWKT(wkt);
    }

    /** Tests the {@link CRS#parseWKT} method. */
    public void testWKT() throws FactoryException {
        String wkt =
                "GEOGCS[\"WGS 84\",\n"
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

    /** Makes sure that the transform between two EPSG:4326 is the identity transform. */
    public void testFindMathTransformIdentity() throws FactoryException {
        CoordinateReferenceSystem crs1default = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem crs2default = CRS.decode("EPSG:4326");
        MathTransform tDefault = CRS.findMathTransform(crs1default, crs2default);
        assertTrue("WSG84 transformed to WSG84 should be Identity", tDefault.isIdentity());

        CoordinateReferenceSystem crs1force = CRS.decode("EPSG:4326", true);
        CoordinateReferenceSystem crs2force = CRS.decode("EPSG:4326", true);
        MathTransform tForce = CRS.findMathTransform(crs1force, crs2force);
        assertTrue("WSG84 transformed to WSG84 should be Identity", tForce.isIdentity());
    }

    /** Makes sure that the authority factory has the proper name. */
    public void testAuthority() {
        CRSAuthorityFactory factory;
        Citation authority;

        // Tests the official factory.
        factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
        authority = factory.getAuthority();
        assertNotNull(authority);
        assertEquals("European Petroleum Survey Group", authority.getTitle().toString(Locale.US));
        assertTrue(Citations.identifierMatches(authority, "EPSG"));

        // Tests the modified factory.
        factory = new OrderedAxisAuthorityFactory("EPSG", null, null);
        authority = factory.getAuthority();
        assertNotNull(authority);
        assertTrue(Citations.identifierMatches(authority, "EPSG"));
    }

    /** Tests the vendor name. */
    public void testVendor() {
        CRSAuthorityFactory factory;
        Citation vendor;

        factory = new OrderedAxisAuthorityFactory("EPSG", null, null);
        vendor = factory.getVendor();
        assertNotNull(vendor);
        assertEquals("Geotools", vendor.getTitle().toString(Locale.US));
        assertFalse(Citations.identifierMatches(vendor, "EPSG"));
    }

    /** Tests the amount of codes available. */
    public void testCodes() throws FactoryException {
        final CRSAuthorityFactory factory = new OrderedAxisAuthorityFactory("EPSG", null, null);
        final Set codes = factory.getAuthorityCodes(CoordinateReferenceSystem.class);
        assertNotNull(codes);
        assertTrue(codes.size() >= 3000);
    }

    /** A random CRS for fun. */
    public void test26910() throws FactoryException {
        final CRSAuthorityFactory factory = new OrderedAxisAuthorityFactory("EPSG", null, null);
        final CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:26910");
        assertNotNull(crs);
        assertSame(crs, factory.createObject("EPSG:26910"));
    }

    /** UDIG requires this to work. */
    public void test4326() throws FactoryException {
        final CRSAuthorityFactory factory = new OrderedAxisAuthorityFactory("EPSG", null, null);
        final CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4326");
        assertNotNull(crs);
        assertSame(crs, factory.createObject("EPSG:4326"));
    }

    /** UDIG requires this to work. */
    public void test4269() throws FactoryException {
        final CRSAuthorityFactory factory = new OrderedAxisAuthorityFactory("EPSG", null, null);
        final CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4269");
        assertNotNull(crs);
        assertSame(crs, factory.createObject("EPSG:4269"));
    }

    /** A random CRS for fun. */
    public void test26910Lower() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("epsg:26910");
        assertNotNull(crs);
    }

    /** A random CRS for fun. */
    public void test26986Lower() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("epsg:26986");
        assertNotNull(crs);
    }

    /** WFS requires this to work. */
    public void test4326Lower() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("epsg:4326");
        assertNotNull(crs);
    }

    /** WFS requires this to work. */
    public void test26742Lower() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("epsg:26742");
        assertNotNull(crs);
    }

    /** WFS requires this to work. */
    public void test4269Lower() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("epsg:4269");
        assertNotNull(crs);
    }

    /**
     * Check that a code with a axis direction with a reference to W works
     *
     * @throws FactoryException
     */
    public void testWestDirection() throws FactoryException {
        // see GEOT-2901
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3573");
        assertNotNull(crs);
    }

    /**
     * Check we support Plate Carré projection
     *
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

    /** Tests {@link CRS#getHorizontalCRS} from a compound CRS. */
    public void testHorizontalFromCompound() throws FactoryException {
        // retrives "NTF (Paris) / France II + NGF Lallemand"
        CoordinateReferenceSystem compound = CRS.decode("EPSG:7401");
        CoordinateReferenceSystem horizontal = CRS.getHorizontalCRS(compound);
        // compares with "NTF (Paris) / France II"
        assertEquals(CRS.decode("EPSG:27582"), horizontal);
    }

    /** Tests {@link CRS#getHorizontalCRS} from a Geographic 3D CR. */
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
        for (Iterator i = codes.iterator(); i.hasNext(); ) {
            CoordinateReferenceSystem crs;
            String code = (String) i.next();
            try {
                crs = factory.createCoordinateReferenceSystem(code);
                assertNotNull(crs);
                count++;
            } catch (FactoryException e) {
                System.err.println("WARNING (CRS: " + code + " ):" + e.getMessage());
            }
        }
        System.out.println("Success: " + count + "/" + total + " (" + (count * 100) / total + "%)");
    }

    public void testSRSAxisOrder() throws Exception {
        try {
            CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
            assertEquals("EPSG:4326", CRS.toSRS(crs));
            Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
            CRS.reset("ALL");
            assertEquals("urn:ogc:def:crs:EPSG::4326", CRS.toSRS(crs));
        } finally {
            Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        }
    }

    public void testSRSAxisOrder2() throws Exception {
        try {
            Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
            CoordinateReferenceSystem crsEN = CRS.decode("EPSG:4326");
            assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(crsEN));
            CoordinateReferenceSystem crsNE = CRS.decode("urn:ogc:def:crs:EPSG::4326");
            assertEquals(AxisOrder.NORTH_EAST, CRS.getAxisOrder(crsNE));
        } finally {
            Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        }
    }

    /**
     * Tests similarity transform on the example provided in the EPSG projection guide, page 99
     *
     * @throws Exception
     */
    public void testSimilarityTransform() throws Exception {
        // Tombak LNG Plant
        CoordinateReferenceSystem tombak = CRS.decode("EPSG:5817", true);
        // Nakhl-e Ghanem / UTM zone 39N
        CoordinateReferenceSystem ng39 = CRS.decode("EPSG:3307", true);

        // forward
        double[] src = new double[] {20000, 10000};
        double[] dst = new double[2];
        MathTransform mt = CRS.findMathTransform(tombak, ng39);
        mt.transform(src, 0, dst, 0, 1);

        assertEquals(618336.748, dst[0], 0.001);
        assertEquals(3067774.210, dst[1], 0.001);

        // and back
        mt.inverse().transform(dst, 0, src, 0, 1);
        assertEquals(20000, src[0], 0.001);
        assertEquals(10000, src[1], 0.001);
    }

    public void testOperationSourceTarget() throws Exception {
        // flip one way
        CoordinateReferenceSystem source = CRS.decode("EPSG:32638", true); // lon/lat
        CoordinateReferenceSystem target = CRS.decode("EPSG:4326", false); // lat/lon
        CoordinateOperationFactory coordinateOperationFactory =
                CRS.getCoordinateOperationFactory(true);
        CoordinateOperation co = coordinateOperationFactory.createOperation(source, target);
        assertEquals(source, co.getSourceCRS());
        assertEquals(target, co.getTargetCRS());

        // flip the other
        source = CRS.decode("EPSG:32638", false); // lat/lon
        target = CRS.decode("EPSG:4326", true); // lon/lat
        co = coordinateOperationFactory.createOperation(source, target);
        assertEquals(source, co.getSourceCRS());
        assertEquals(target, co.getTargetCRS());
    }

    public void testNadCon() throws Exception {
        CoordinateReferenceSystem crs4138 = CRS.decode("EPSG:4138");
        CoordinateReferenceSystem crs4326 = CRS.decode("EPSG:4326");
        MathTransform mt = CRS.findMathTransform(crs4138, crs4326);

        assertTrue(mt.toWKT().contains("NADCON"));

        double[] src = new double[] {56.575, -169.625};
        double[] expected = new double[] {56.576034, -169.62744};
        double[] p = new double[2];
        mt.transform(src, 0, p, 0, 1);
        assertEquals(expected[0], p[0], 1e-6);
        assertEquals(expected[1], p[1], 1e-6);
    }

    /**
     * Testing the handling of toSRS method; used to translate from CoordinateReferenceSystem
     * identifiers to the spatial reference system name used by OGC web services. There are a number
     * of options here depending on the specification used.
     */
    public void testSRS() throws Exception {
        try {
            CRS.reset("all");
            System.setProperty("org.geotools.referencing.forceXY", "true");

            assertEquals("CRS:84", CRS.toSRS(DefaultGeographicCRS.WGS84));
            CoordinateReferenceSystem WORLD = CRS.decode("EPSG:4326", false);
            assertEquals("4326", CRS.toSRS(WORLD, true));
            String srs = CRS.toSRS(WORLD, false);
            assertTrue("EPSG:4326", srs.contains("EPSG") && srs.contains("4326"));

            CoordinateReferenceSystem WORLD2 = CRS.decode("EPSG:4326", true);
            srs = CRS.toSRS(WORLD2, false);
            assertTrue("EPSG:4326", srs.contains("EPSG") && srs.contains("4326"));

            CoordinateReferenceSystem WORLD3 = CRS.decode("urn:x-ogc:def:crs:EPSG::4326", false);
            srs = CRS.toSRS(WORLD3, false);
            assertTrue("EPSG:4326", srs.contains("EPSG") && srs.contains("4326"));

            CoordinateReferenceSystem WORLD4 = CRS.decode("urn:x-ogc:def:crs:EPSG::4326", true);
            srs = CRS.toSRS(WORLD4, false);
            assertTrue("EPSG:4326", srs.contains("EPSG") && srs.contains("4326"));
        } finally {
            System.getProperties().remove("org.geotools.referencing.forceXY");
        }
        try {
            CRS.reset("all");
            Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
            assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(CRS.decode("EPSG:4326", false)));
            assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(CRS.decode("EPSG:4326", true)));
            assertEquals(
                    AxisOrder.NORTH_EAST,
                    CRS.getAxisOrder(CRS.decode("urn:x-ogc:def:crs:EPSG::4326", false)));
            assertEquals(
                    AxisOrder.NORTH_EAST,
                    CRS.getAxisOrder(CRS.decode("urn:x-ogc:def:crs:EPSG::4326", true)));
        } finally {
            Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        }
        try {
            CRS.reset("all");
            assertEquals(AxisOrder.NORTH_EAST, CRS.getAxisOrder(CRS.decode("EPSG:4326", false)));
            assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(CRS.decode("EPSG:4326", true)));
            assertEquals(
                    AxisOrder.NORTH_EAST,
                    CRS.getAxisOrder(CRS.decode("urn:x-ogc:def:crs:EPSG::4326", false)));
            assertEquals(
                    AxisOrder.NORTH_EAST,
                    CRS.getAxisOrder(CRS.decode("urn:x-ogc:def:crs:EPSG::4326", true)));
        } finally {
        }
        try {
            CRS.reset("all");
            System.setProperty("org.geotools.referencing.forceXY", "true");
            assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(CRS.decode("EPSG:4326", false)));
            assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(CRS.decode("EPSG:4326", true)));
            assertEquals(
                    AxisOrder.NORTH_EAST,
                    CRS.getAxisOrder(CRS.decode("urn:x-ogc:def:crs:EPSG::4326", false)));
            assertEquals(
                    AxisOrder.NORTH_EAST,
                    CRS.getAxisOrder(CRS.decode("urn:x-ogc:def:crs:EPSG::4326", true)));
        } finally {
            System.getProperties().remove("org.geotools.referencing.forceXY");
        }
    }

    public void testCRS_CH1903_LV03()
            throws NoSuchAuthorityCodeException, FactoryException, MismatchedDimensionException,
                    TransformException {
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326", false); // WGS84
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:21781", false); // CH1903_LV03

        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
        // test coordinate: Berne, old reference point
        // see
        // http://www.swisstopo.admin.ch/internet/swisstopo/en/home/topics/survey/sys/refsys/switzerland.html
        DirectPosition2D source =
                new DirectPosition2D(sourceCRS, 46.9510827861504654, 7.4386324175389165);
        DirectPosition2D result = new DirectPosition2D();

        transform.transform(source, result);
        assertEquals(600000.0, result.x, 0.1);
        assertEquals(200000.0, result.y, 0.1);
    }

    public void testGetMapProjection() throws Exception {
        CoordinateReferenceSystem utm32OnLonLat = CRS.decode("EPSG:23032", true);
        assertTrue(CRS.getMapProjection(utm32OnLonLat) instanceof TransverseMercator);
        CoordinateReferenceSystem utm32OnLatLon = CRS.decode("EPSG:23032", false);
        assertTrue(CRS.getMapProjection(utm32OnLatLon) instanceof TransverseMercator);
        CoordinateReferenceSystem nad27Tennessee = CRS.decode("EPSG:2062", false);
        assertTrue(CRS.getMapProjection(nad27Tennessee) instanceof LambertConformal1SP);
    }

    public void testTransformWgs84PolarStereographic() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031", true);
        Envelope2D envelope = new Envelope2D(DefaultGeographicCRS.WGS84);
        envelope.add(-180, -90);
        envelope.add(180, 0);
        Envelope transformed = CRS.transform(envelope, crs);
        // the result is a square
        assertEquals(transformed.getMaximum(0), transformed.getMaximum(1), 1d);
        assertEquals(transformed.getMinimum(0), transformed.getMinimum(1), 1d);
        assertEquals(Math.abs(transformed.getMinimum(0)), transformed.getMaximum(0), 1d);

        assertEquals(transformed.getMaximum(0), 1.236739621845986E7, 1d);
    }

    public void testTransformPolarStereographicWgs84() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031", true);
        Envelope2D envelope = new Envelope2D(crs);
        // random bbox that does include the pole
        envelope.add(-4223632.8125, -559082.03125);
        envelope.add(5053710.9375, 3347167.96875);
        Envelope transformed = CRS.transform(envelope, DefaultGeographicCRS.WGS84);
        // check we got the whole range of longitudes, since the original bbox contains the pole
        assertEquals(-180d, transformed.getMinimum(0), 0d);
        assertEquals(180d, transformed.getMaximum(0), 0d);
        // another bbox
        envelope = new Envelope2D(crs);
        // random bbox that does not include the pole, but it's really just slightly off it
        envelope.add(-10718812.640513, -10006238.053703);
        envelope.add(12228504.561708, -344209.75803081);
        transformed = CRS.transform(envelope, DefaultGeographicCRS.WGS84);
        assertEquals(-90, transformed.getMinimum(1), 0.1d);
    }

    public void testTransformLambertAzimuthalEqualAreaWgs84() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3574", true);
        Envelope2D envelope = new Envelope2D(crs);
        // random bbox that does include the pole
        envelope.add(-3142000, -3142000);
        envelope.add(3142000, 3142000);
        Envelope transformed = CRS.transform(envelope, DefaultGeographicCRS.WGS84);
        // check we got the whole range of longitudes, since the original bbox contains the pole
        assertEquals(-180d, transformed.getMinimum(0), 0d);
        assertEquals(180d, transformed.getMaximum(0), 0d);
    }

    public void testTransformLambertAzimuthalEqualAreaWgs84NonPolar() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3035", true);
        // a bbox that does _not_ include the pole
        Envelope2D envelope = new Envelope2D(crs);
        envelope.setFrameFromDiagonal(4029000, 2676000, 4696500, 3567700);
        Envelope transformed = CRS.transform(envelope, DefaultGeographicCRS.WGS84);
        // check we did _not_ get the whole range of longitudes
        assertEquals(5.42, transformed.getMinimum(0), 1e-2);
        assertEquals(15.88, transformed.getMaximum(0), 1e-2);
    }

    public void testTransformPolarStereographicWgs84FalseOrigin() throws Exception {
        // this one has false origins at 6000000/6000000
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3032", true);
        Envelope2D envelope = new Envelope2D(crs);
        envelope.add(5900000, 5900000);
        envelope.add(6100000, 6100000);
        Envelope transformed = CRS.transform(envelope, DefaultGeographicCRS.WGS84);
        // check we got the whole range of longitudes, since the original bbox contains the pole
        assertEquals(-180d, transformed.getMinimum(0), 0d);
        assertEquals(180d, transformed.getMaximum(0), 0d);
    }

    public void testTransformPolarStereographicToOther() throws Exception {
        CoordinateReferenceSystem antarcticPs = CRS.decode("EPSG:3031", true);
        CoordinateReferenceSystem australianPs = CRS.decode("EPSG:3032", true);
        Envelope2D envelope = new Envelope2D(antarcticPs);
        envelope.add(-4223632.8125, -559082.03125);
        envelope.add(5053710.9375, 3347167.96875);
        Envelope transformed = CRS.transform(envelope, australianPs);
        // has a false easting and northing, we can only check the spans are equal
        assertEquals(transformed.getSpan(0), transformed.getSpan(1), 1d);

        assertEquals(transformed.getMaximum(0), 1.2309982175378662E7, 1d);
    }

    public void testTransformWorldVanDerGrintenI() throws Exception {
        try {
            MapProjection.SKIP_SANITY_CHECKS = true;
            // @formatter:off
            String wkt =
                    "PROJCS[\"World_Van_der_Grinten_I\", \n"
                            + "  GEOGCS[\"GCS_WGS_1984\", \n"
                            + "    DATUM[\"D_WGS_1984\", \n"
                            + "      SPHEROID[\"WGS_1984\", 6378137.0, 298.257223563]], \n"
                            + "    PRIMEM[\"Greenwich\", 0.0], \n"
                            + "    UNIT[\"degree\", 0.017453292519943295], \n"
                            + "    AXIS[\"Longitude\", EAST], \n"
                            + "    AXIS[\"Latitude\", NORTH]], \n"
                            + "  PROJECTION[\"World_Van_der_Grinten_I\"], \n"
                            + "  PARAMETER[\"central_meridian\", 0.0], \n"
                            + "  PARAMETER[\"false_easting\", 0.0], \n"
                            + "  PARAMETER[\"false_northing\", 0.0], \n"
                            + "  UNIT[\"m\", 1.0], \n"
                            + "  AXIS[\"x\", EAST], \n"
                            + "  AXIS[\"y\", NORTH], \n"
                            + "  AUTHORITY[\"EPSG\",\"54029\"]]";
            // @formatter:on

            CoordinateReferenceSystem worldVanDerGrinten = CRS.parseWKT(wkt);

            Envelope2D envelope = new Envelope2D(worldVanDerGrinten);
            envelope.add(-39842778.796051726, -42306552.87521737);
            envelope.add(40061162.89695589, 37753756.60975308);

            Envelope transformed = CRS.transform(envelope, DefaultGeographicCRS.WGS84);
            System.out.println(transformed);
        } finally {
            MapProjection.SKIP_SANITY_CHECKS = false;
        }
    }

    public void testTransformSouthEmisphereToStereographic() throws Exception {
        String wkt =
                "PROJCS[\"NSIDC Sea Ice Polar Stereographic South\",\n"
                        + "    GEOGCS[\"Unspecified datum based upon the Hughes 1980 ellipsoid\",\n"
                        + "        DATUM[\"Not_specified_based_on_Hughes_1980_ellipsoid\",\n"
                        + "            SPHEROID[\"Hughes 1980\",6378273,298.279411123061,\n"
                        + "                AUTHORITY[\"EPSG\",\"7058\"]],\n"
                        + "            AUTHORITY[\"EPSG\",\"6054\"]],\n"
                        + "        PRIMEM[\"Greenwich\",0,\n"
                        + "            AUTHORITY[\"EPSG\",\"8901\"]],\n"
                        + "        UNIT[\"degree\",0.0174532925199433,\n"
                        + "            AUTHORITY[\"EPSG\",\"9122\"]],\n"
                        + "        AUTHORITY[\"EPSG\",\"4054\"]],\n"
                        + "    PROJECTION[\"Polar_Stereographic\"],\n"
                        + "    PARAMETER[\"latitude_of_origin\",-70],\n"
                        + "    PARAMETER[\"central_meridian\",0],\n"
                        + "    PARAMETER[\"scale_factor\",1],\n"
                        + "    PARAMETER[\"false_easting\",0],\n"
                        + "    PARAMETER[\"false_northing\",0],\n"
                        + "    UNIT[\"metre\",1,\n"
                        + "        AUTHORITY[\"EPSG\",\"9001\"]],\n"
                        + "    AXIS[\"X\",EAST],\n"
                        + "    AXIS[\"Y\",NORTH],\n"
                        + "    AUTHORITY[\"EPSG\",\"3412\"]]";
        CoordinateReferenceSystem polar = CRS.parseWKT(wkt);

        Envelope2D envelope = new Envelope2D(DefaultGeographicCRS.WGS84);
        envelope.add(-180, -90);
        envelope.add(180, 0);

        Envelope transformed = CRS.transform(envelope, polar);
        assertEquals(-1.271387435620243E7, transformed.getMinimum(0), 1e3);
        assertEquals(-1.271387435620243E7, transformed.getMinimum(1), 1e3);
        assertEquals(1.271387435620243E7, transformed.getMaximum(0), 1e3);
        assertEquals(1.271387435620243E7, transformed.getMaximum(1), 1e3);
    }

    @Override
    protected boolean isOnline() throws Exception {
        try {
            CRS.decode("EPSG:4326");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
