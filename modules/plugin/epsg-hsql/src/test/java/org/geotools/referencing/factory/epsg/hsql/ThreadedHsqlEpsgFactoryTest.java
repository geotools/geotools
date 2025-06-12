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
package org.geotools.referencing.factory.epsg.hsql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.geometry.Position2D;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.datum.BursaWolfParameters;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.factory.IdentifiedObjectFinder;
import org.geotools.referencing.factory.epsg.DirectEpsgFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * This class makes sure we can find the ThreadedHsqlEpsgFactory using ReferencingFactoryFinder.
 *
 * @author Jody
 */
public class ThreadedHsqlEpsgFactoryTest {

    private static ThreadedHsqlEpsgFactory factory;
    private static IdentifiedObjectFinder finder;
    static final double EPS = 1e-06;

    @Before
    public void setUp() throws Exception {
        if (factory == null) {
            factory = (ThreadedHsqlEpsgFactory) ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
        }
        // force in the standard timeout
        factory.setTimeout(30 * 60 * 1000);
        finder = factory.getIdentifiedObjectFinder(CoordinateReferenceSystem.class);
    }

    @Test
    public void testConnectionCorruption() throws Exception {
        corruptConnection();
        CRS.decode("EPSG:4326");
    }

    @Test
    public void testConnectionCorruptionListAll() throws Exception {
        Set<String> original = CRS.getSupportedCodes("EPSG");
        assertTrue(original.size() > 4000);
        corruptConnection();
        Set<String> afterCorruption = CRS.getSupportedCodes("EPSG");
        assertEquals(original, afterCorruption);
    }

    @SuppressWarnings("PMD.CloseResource")
    private void corruptConnection() throws Exception {
        java.lang.reflect.Field field =
                org.geotools.referencing.factory.BufferedAuthorityFactory.class.getDeclaredField("backingStore");
        field.setAccessible(true);
        Object def = field.get(factory);
        Method getConnection = DirectEpsgFactory.class.getDeclaredMethod("getConnection");
        getConnection.setAccessible(true);
        java.sql.Connection conn = (java.sql.Connection) getConnection.invoke(def);
        conn.close();
    }

    @Test
    public void testCreation() throws Exception {
        assertNotNull(factory);
        CoordinateReferenceSystem epsg4326 = factory.createCoordinateReferenceSystem("EPSG:4326");
        CoordinateReferenceSystem code4326 = factory.createCoordinateReferenceSystem("4326");

        assertEquals("4326 equals EPSG:4326", code4326, epsg4326);
        assertSame("4326 == EPSG:4326", code4326, epsg4326);
    }

    @Test
    public void testFunctionality() throws Exception {
        CoordinateReferenceSystem crs1 = factory.createCoordinateReferenceSystem("4326");
        CoordinateReferenceSystem crs2 = factory.createCoordinateReferenceSystem("3005");

        // reproject
        MathTransform transform = CRS.findMathTransform(crs1, crs2, true);
        Position pos = new Position2D(48.417, 123.35);
        transform.transform(pos, null);
    }

    @Test
    public void testAuthorityCodes() throws Exception {
        Set authorityCodes = factory.getAuthorityCodes(CoordinateReferenceSystem.class);
        assertNotNull(authorityCodes);
        assertTrue(authorityCodes.size() > 3000);
    }

    @Test
    public void testFindWSG84() throws FactoryException {
        String wkt = "GEOGCS[\"WGS 84\",\n"
                + "  DATUM[\"World Geodetic System 1984\",\n"
                + "    SPHEROID[\"WGS 84\", 6378137.0, 298.257223563]],\n"
                + "  PRIMEM[\"Greenwich\", 0.0],\n"
                + "  UNIT[\"degree\", 0.017453292519943295],\n"
                + "  AXIS[\"Geodetic latitude\", NORTH],\n"
                + "  AXIS[\"Geodetic longitude\", EAST]]";

        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        IdentifiedObject find = finder.find(crs);

        assertNotNull("With full scan allowed, the CRS should be found.", find);

        assertTrue(
                "Should found an object equals (ignoring metadata) to the requested one.",
                CRS.equalsIgnoreMetadata(crs, find));
        ReferenceIdentifier found = AbstractIdentifiedObject.getIdentifier(find, factory.getAuthority());
        // assertEquals("4326",found.getCode());
        assertNotNull(found);
        finder.setFullScanAllowed(false);
        String id = finder.findIdentifier(crs);
        assertEquals("The CRS should still be in the cache.", "EPSG:4326", id);
    }

    @Test
    public void testFindBeijing1954() throws FactoryException {
        /*
         * The PROJCS below intentionally uses a name different from the one found in the
         * EPSG database, in order to force a full scan (otherwise the EPSG database would
         * find it by name, but we want to test the scan).
         */
        String wkt = "PROJCS[\"Beijing 1954\",\n"
                + "   GEOGCS[\"Beijing 1954\",\n"
                + "     DATUM[\"Beijing 1954\",\n"
                + "       SPHEROID[\"Krassowsky 1940\", 6378245.0, 298.3]],\n"
                + "     PRIMEM[\"Greenwich\", 0.0],\n"
                + "     UNIT[\"degree\", 0.017453292519943295],\n"
                + "     AXIS[\"Geodetic latitude\", NORTH],\n"
                + "     AXIS[\"Geodetic longitude\", EAST]],\n"
                + "   PROJECTION[\"Transverse Mercator\"],\n"
                + "   PARAMETER[\"central_meridian\", 135.0],\n"
                + "   PARAMETER[\"latitude_of_origin\", 0.0],\n"
                + "   PARAMETER[\"scale_factor\", 1.0],\n"
                + "   PARAMETER[\"false_easting\", 500000.0],\n"
                + "   PARAMETER[\"false_northing\", 0.0],\n"
                + "   UNIT[\"m\", 1.0],\n"
                + "   AXIS[\"Northing\", NORTH],\n"
                + "   AXIS[\"Easting\", EAST]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);

        IdentifiedObject find = finder.find(crs);
        assertNotNull("With full scan allowed, the CRS should be found.", find);

        assertTrue(
                "Should found an object equals (ignoring metadata) to the requested one.",
                CRS.equalsIgnoreMetadata(crs, find));

        assertEquals(
                "2442",
                AbstractIdentifiedObject.getIdentifier(find, factory.getAuthority())
                        .getCode());
        finder.setFullScanAllowed(false);
        String id = finder.findIdentifier(crs);
        assertEquals("The CRS should still be in the cache.", "EPSG:2442", id);
    }

    @Test
    public void testGoogleProjection() throws Exception {
        CoordinateReferenceSystem epsg4326 = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem epsg3785 = CRS.decode("EPSG:3857");

        String wkt900913 = "PROJCS[\"WGS84 / Google Mercator\", "
                + "GEOGCS[\"WGS 84\", "
                + "  DATUM[\"World Geodetic System 1984\", "
                + "    SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], "
                + "    AUTHORITY[\"EPSG\",\"6326\"]], "
                + "  PRIMEM[\"Greenwich\", 0.0, "
                + "  AUTHORITY[\"EPSG\",\"8901\"]], "
                + "  UNIT[\"degree\", 0.017453292519943295], AUTHORITY[\"EPSG\",\"4326\"]], "
                + "PROJECTION[\"Mercator (1SP)\", "
                + "AUTHORITY[\"EPSG\",\"9804\"]], "
                + "PARAMETER[\"semi_major\", 6378137.0], "
                + "PARAMETER[\"semi_minor\", 6378137.0], "
                + "PARAMETER[\"latitude_of_origin\", 0.0], "
                + "PARAMETER[\"central_meridian\", 0.0], "
                + "PARAMETER[\"scale_factor\", 1.0], "
                + "PARAMETER[\"false_easting\", 0.0], "
                + "PARAMETER[\"false_northing\", 0.0], "
                + "UNIT[\"m\", 1.0],  "
                + "AUTHORITY[\"EPSG\",\"900913\"]]";
        CoordinateReferenceSystem epsg900913 = CRS.parseWKT(wkt900913);

        MathTransform t1 = CRS.findMathTransform(epsg4326, epsg3785);
        MathTransform t2 = CRS.findMathTransform(epsg4326, epsg900913);

        // check the two equate each other, we know the above 900913 definition works
        double[][] points = {
            {0, 0},
            {30.0, 30.0},
            {-45.0, 45.0},
            {-20, -20},
            {80, -80},
            {85, 180},
            {-85, -180}
        };
        double[] tp1 = new double[2];
        double[] tp2 = new double[2];
        for (double[] point : points) {
            t1.transform(point, 0, tp1, 0, 1);
            t2.transform(point, 0, tp2, 0, 1);
            assertEquals(tp1[0], tp2[0], EPS);
            assertEquals(tp1[1], tp2[1], EPS);
            // check inverse as well
            t1.inverse().transform(tp1, 0, tp1, 0, 1);
            t2.inverse().transform(tp2, 0, tp2, 0, 1);
            assertEquals(point[0], tp2[0], EPS);
            assertEquals(point[1], tp2[1], EPS);
        }
    }

    /** GEOT-3497 (given the same accuracy use the transformation method with the largest valid area) */
    @Test
    public void testNad83() throws Exception {
        GeographicCRS crs = (GeographicCRS) CRS.decode("EPSG:4269");
        DefaultGeodeticDatum datum = (DefaultGeodeticDatum) crs.getDatum();
        BursaWolfParameters[] params = datum.getBursaWolfParameters();
        boolean wgs84Found = false;
        for (BursaWolfParameters param : params) {
            if (DefaultGeodeticDatum.isWGS84(param.targetDatum)) {
                wgs84Found = true;
                assertEquals(0.0, param.dx, EPS);
                assertEquals(0.0, param.dy, EPS);
                assertEquals(0.0, param.dz, EPS);
                assertEquals(0.0, param.ex, EPS);
                assertEquals(0.0, param.ey, EPS);
                assertEquals(0.0, param.ez, EPS);
                assertEquals(0.0, param.ppm, EPS);
            }
        }
        assertTrue(wgs84Found);
    }

    /** GEOT-3644, make sure we can decode what we generated */
    @Test
    public void testEncodeAndParse() throws Exception {
        // a crs with out of standard axis orientation "South along 45 deg East"
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3413");
        // format it in a lenient way
        String wkt = crs.toString();
        // parse it back, here it did break
        CoordinateReferenceSystem parsed = CRS.parseWKT(wkt);
        // also make sure we're getting back the same thing
        assertTrue(CRS.equalsIgnoreMetadata(crs, parsed));
    }

    /** GEOT-3482 */
    @Test
    public void testPPMUnit() throws Exception {
        // Create WGS 72 CRS where we know that the EPSG defines a unique
        // Position Vector Transformation to WGS 84 with ppm = 0.219
        GeographicCRS wgs72 = (GeographicCRS) CRS.decode("EPSG:4322");

        // Get datum
        DefaultGeodeticDatum datum = (DefaultGeodeticDatum) wgs72.getDatum();

        // Get BursaWolf parameters
        BursaWolfParameters[] params = datum.getBursaWolfParameters();

        // Check for coherence with the value contained in the EPSG data base
        assertEquals(0.219, params[0].ppm, EPS);
    }

    @Test
    public void testDelay() throws Exception {
        // force a short timeout
        factory.setTimeout(200);

        // make it do some work
        factory.createCoordinateReferenceSystem("EPSG:4326");
        factory.getAuthorityCodes(CoordinateReferenceSystem.class);

        // sleep and force gc to allow the backing store to be released
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < 4000 && factory.isConnected()) {
            Thread.currentThread().sleep(200);
            System.gc();
            System.runFinalization();
        }

        // check it has been disposed of
        assertFalse(factory.isConnected());

        // see if it's able to reconnect
        factory.createCoordinateReferenceSystem("EPSG:4327");
        factory.getAuthorityCodes(CoordinateReferenceSystem.class);
        assertTrue(factory.isConnected());
    }

    /** Test data source creation when <code>java.io.tmpdir</code> contains spaces. */
    @Test
    public void testTmpWithSpaces() throws Exception {
        final String JAVA_IO_TMPDIR_PROPERTY = "java.io.tmpdir";
        String oldTmpDir = System.getProperty(JAVA_IO_TMPDIR_PROPERTY);
        File tmpDir = new File("target/tmp with spaces");
        FileUtils.deleteQuietly(tmpDir);
        tmpDir.mkdirs();
        try {
            System.setProperty(JAVA_IO_TMPDIR_PROPERTY, tmpDir.getPath());
            // force data source re-creation
            factory.dispose();
            assertNotNull(factory.createCoordinateReferenceSystem("EPSG:4326"));
            String creationMarker = String.format(
                    "GeoTools/Databases/HSQL/v%s/EPSG_creation_marker.txt", ThreadedHsqlEpsgFactory.VERSION);
            assertTrue(new File(tmpDir, creationMarker).exists());
        } finally {
            System.setProperty(JAVA_IO_TMPDIR_PROPERTY, oldTmpDir);
            factory.dispose();
            FileUtils.deleteQuietly(tmpDir);
        }
    }
}
