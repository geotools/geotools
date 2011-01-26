/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory;

// J2SE dependencies
import java.util.Map;
import java.util.logging.Level;

// JUnit dependencies
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

// OpenGIS dependencies
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.SingleCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Matrix;

// Geotools dependencies
import org.geotools.factory.Hints;
import org.geotools.resources.Arguments;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.factory.epsg.LongitudeFirstFactory;


/**
 * Tests the usage of {@link OrderedAxisAuthorityFactory} with the help of the
 * EPSG database. Any EPSG plugin should fit. However, this test live in the
 * {@code plugin/epsg-hsql} module since the HSQL plugin is the only one which
 * is garantee to work on any machine running Maven.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Jody Garnett
 */
public class OrderedAxisAuthorityFactoryTest extends TestCase {
    /**
     * {@code true} if metadata (especially identifiers) should be erased, or {@code false} if
     * they should be kepts. The {@code true} value matches the pre GEOT-854 state, while the
     * {@code false} value mathes the post GEOT-854 state.
     *
     * @see http://jira.codehaus.org/browse/GEOT-854
     */
    private static final boolean METADATA_ERASED = false;

    /**
     * Small number for floating points comparaisons.
     */
    private static final double EPS = 1E-8;

    /**
     * Run the suite from the command line. If {@code "-log"} flag is specified on the
     * command-line, then the logger will be set to {@link Level#CONFIG}. This is usefull
     * for tracking down which data source is actually used.
     */
    public static void main(final String[] args) {
        final Arguments arguments = new Arguments(args);
        final boolean log = arguments.getFlag("-log");
        arguments.getRemainingArguments(0);
        org.geotools.util.logging.Logging.GEOTOOLS.forceMonolineConsoleOutput(log ? Level.CONFIG : null);
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Returns the test suite.
     */
    public static Test suite() {
        return new TestSuite(OrderedAxisAuthorityFactoryTest.class);
    }

    /**
     * Constructs a test case with the given name.
     */
    public OrderedAxisAuthorityFactoryTest(final String name) {
        super(name);
    }

    /**
     * Returns the ordered axis factory for the specified set of hints.
     */
    private static OrderedAxisAuthorityFactory getFactory(final Hints hints) {
        CRSAuthorityFactory factory;
        factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);

        assertTrue(factory.getClass().toString(), factory instanceof LongitudeFirstFactory);
        final LongitudeFirstFactory asLongitudeFirst = (LongitudeFirstFactory) factory;
        final Map implementationHints = asLongitudeFirst.getImplementationHints();
        factory = (CRSAuthorityFactory) implementationHints.get(Hints.CRS_AUTHORITY_FACTORY);

        assertTrue(factory.getClass().toString(), factory instanceof OrderedAxisAuthorityFactory);
        final OrderedAxisAuthorityFactory asOrdered = (OrderedAxisAuthorityFactory) factory;
        assertFalse(asOrdered.isCodeMethodOverriden());

        return asOrdered;
    }

    /**
     * Returns a positive number if the specified coordinate system is right-handed,
     * or a negative number if it is left handed.
     */
    private static double getAngle(final CoordinateReferenceSystem crs) {
        final CoordinateSystem cs = crs.getCoordinateSystem();
        assertEquals(2, cs.getDimension());
        return DefaultCoordinateSystemAxis.getAngle(cs.getAxis(0).getDirection(),
                                                    cs.getAxis(1).getDirection());
    }

    /**
     * Tests the registration of the various flavor of {@link OrderedAxisAuthorityFactoryTest}
     * for the EPSG authority factory.
     */
    public void testRegistration() {
        final Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        OrderedAxisAuthorityFactory factory;
        factory = getFactory(hints);
        assertFalse(factory.forceStandardDirections);
        assertFalse(factory.forceStandardUnits);

        hints.put(Hints.FORCE_STANDARD_AXIS_DIRECTIONS, Boolean.FALSE);
        assertSame(factory, getFactory(hints));
        assertFalse(factory.forceStandardDirections);
        assertFalse(factory.forceStandardUnits);

        hints.put(Hints.FORCE_STANDARD_AXIS_UNITS, Boolean.FALSE);
        assertSame(factory, getFactory(hints));
        assertFalse(factory.forceStandardDirections);
        assertFalse(factory.forceStandardUnits);

        hints.put(Hints.FORCE_STANDARD_AXIS_UNITS, Boolean.TRUE);
        assertNotSame(factory, factory = getFactory(hints));
        assertFalse  (factory.forceStandardDirections);
        assertTrue   (factory.forceStandardUnits);

        hints.put(Hints.FORCE_STANDARD_AXIS_DIRECTIONS, Boolean.TRUE);
        assertNotSame(factory, factory = getFactory(hints));
        assertTrue   (factory.forceStandardDirections);
        assertTrue   (factory.forceStandardUnits);

        hints.put(Hints.FORCE_STANDARD_AXIS_UNITS, Boolean.FALSE);
        assertNotSame(factory, factory = getFactory(hints));
        assertTrue   (factory.forceStandardDirections);
        assertFalse  (factory.forceStandardUnits);
    }

    /**
     * Tests the axis reordering.
     */
    public void testAxisReordering() throws FactoryException {
        /*
         * Tests the OrderedAxisAuthorityFactory creating using FactoryFinder. The following
         * conditions are not tested directly, but are required in order to get the test to
         * succeed:
         *
         *    - EPSG factories must be provided for both "official" and "modified" axis order.
         *    - The "official" axis order must have precedence over the modified one.
         *    - The hints are correctly understood by FactoryFinder.
         */
        final AbstractAuthorityFactory factory0, factory1;
        final Hints hints = new Hints(Hints.CRS_AUTHORITY_FACTORY, AbstractAuthorityFactory.class);
        factory0 = (AbstractAuthorityFactory) ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
        assertFalse(factory0 instanceof OrderedAxisAuthorityFactory);
        assertFalse(factory0 instanceof LongitudeFirstFactory);
        hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        hints.put(Hints.FORCE_STANDARD_AXIS_DIRECTIONS,   Boolean.TRUE);
        hints.put(Hints.FORCE_STANDARD_AXIS_UNITS,        Boolean.TRUE);
        factory1 = (AbstractAuthorityFactory) ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
        assertTrue(factory1 instanceof LongitudeFirstFactory);
        /*
         * The local variables to be used for all remaining tests
         * (usefull to setup in the debugger).
         */
        String code;
        CoordinateReferenceSystem crs0, crs1;
        CoordinateOperationFactory opFactory = ReferencingFactoryFinder.getCoordinateOperationFactory(null);
        MathTransform mt;
        Matrix matrix;
        /*
         * Tests a WGS84 geographic CRS (2D) with (NORTH, EAST) axis directions.
         * The factory should reorder the axis with no more operation than an axis swap.
         */
        code = "4326";
        crs0 = factory0.createCoordinateReferenceSystem(code);
        crs1 = factory1.createCoordinateReferenceSystem(code);
        final CoordinateReferenceSystem cacheTest = crs1;
        assertNotSame(crs0, crs1);
        assertNotSame(crs0.getCoordinateSystem(), crs1.getCoordinateSystem());
        assertSame(((SingleCRS) crs0).getDatum(), ((SingleCRS) crs1).getDatum());
        assertEquals("Expected a left-handed CS.",  -90, getAngle(crs0), EPS);
        assertEquals("Expected a right-handed CS.", +90, getAngle(crs1), EPS);
        assertFalse(crs0.getIdentifiers().isEmpty());
        if (METADATA_ERASED) {
            assertTrue(crs1.getIdentifiers().isEmpty());
        } else {
            assertEquals(crs0.getIdentifiers(), crs1.getIdentifiers());
        }
        mt = opFactory.createOperation(crs0, crs1).getMathTransform();
        assertFalse(mt.isIdentity());
        assertTrue(mt instanceof LinearTransform);
        matrix = ((LinearTransform) mt).getMatrix();
        assertEquals(new GeneralMatrix(new double[][] {
            {0, 1, 0},
            {1, 0, 0},
            {0, 0, 1}}), new GeneralMatrix(matrix));
        /*
         * Tests a WGS84 geographic CRS (3D) with (NORTH, EAST, UP) axis directions.
         * Because this CRS uses sexagesimal units, conversions are not supported and
         * will not be tested.
         */
        code = "4329";
        crs0 = factory0.createCoordinateReferenceSystem(code);
        crs1 = factory1.createCoordinateReferenceSystem(code);
        assertNotSame(crs0, crs1);
        assertNotSame(crs0.getCoordinateSystem(), crs1.getCoordinateSystem());
        assertSame(((SingleCRS) crs0).getDatum(), ((SingleCRS) crs1).getDatum());
        assertFalse(crs0.getIdentifiers().isEmpty());
        if (METADATA_ERASED) {
            assertTrue(crs1.getIdentifiers().isEmpty());
        } else {
            assertEquals(crs0.getIdentifiers(), crs1.getIdentifiers());
        }
        /*
         * Tests a WGS84 geographic CRS (3D) with (NORTH, EAST, UP) axis directions.
         * The factory should reorder the axis with no more operation than an axis swap.
         */
        code = "63266413";
        crs0 = factory0.createCoordinateReferenceSystem(code);
        crs1 = factory1.createCoordinateReferenceSystem(code);
        assertNotSame(crs0, crs1);
        assertNotSame(crs0.getCoordinateSystem(), crs1.getCoordinateSystem());
        assertSame(((SingleCRS) crs0).getDatum(), ((SingleCRS) crs1).getDatum());
        assertFalse(crs0.getIdentifiers().isEmpty());
        if (METADATA_ERASED) {
            assertTrue(crs1.getIdentifiers().isEmpty());
        } else {
            assertEquals(crs0.getIdentifiers(), crs1.getIdentifiers());
        }
        mt = opFactory.createOperation(crs0, crs1).getMathTransform();
        assertFalse(mt.isIdentity());
        assertTrue(mt instanceof LinearTransform);
        matrix = ((LinearTransform) mt).getMatrix();
        assertEquals(new GeneralMatrix(new double[][] {
            {0, 1, 0, 0},
            {1, 0, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}}), new GeneralMatrix(matrix));
        /*
         * Tests a projected CRS with (EAST, NORTH) axis orientation. No axis reordering is needed,
         * which means that their coordinate systems are identical and the math transform should be
         * the identity one. Note that while no axis swap is needed, the base GeographicCRS are not
         * the same since an axis reordering has been done there.
         */
        code = "2027";
        crs0 = factory0.createCoordinateReferenceSystem(code);
        crs1 = factory1.createCoordinateReferenceSystem(code);
        assertNotSame(crs0, crs1);
        assertSame(crs0.getCoordinateSystem(), crs1.getCoordinateSystem());
        assertSame(((SingleCRS) crs0).getDatum(), ((SingleCRS) crs1).getDatum());
        assertNotSame(((ProjectedCRS) crs0).getBaseCRS(), ((ProjectedCRS) crs1).getBaseCRS());
        assertFalse(crs0.getIdentifiers().isEmpty());
        if (METADATA_ERASED) {
            assertTrue(crs1.getIdentifiers().isEmpty());
        } else {
            assertEquals(crs0.getIdentifiers(), crs1.getIdentifiers());
        }
        mt = opFactory.createOperation(crs0, crs1).getMathTransform();
        assertTrue(mt.isIdentity());
        /*
         * Tests a projected CRS with (WEST, SOUTH) axis orientation.
         * The factory should arrange the axis with no more operation than a direction change.
         * While the end result is a matrix like the GeographicCRS case, the path that lead to
         * this result is much more complex.
         */
        code = "22275";
        crs0 = factory0.createCoordinateReferenceSystem(code);
        crs1 = factory1.createCoordinateReferenceSystem(code);
        assertNotSame(crs0, crs1);
        assertNotSame(crs0.getCoordinateSystem(), crs1.getCoordinateSystem());
        assertSame(((SingleCRS) crs0).getDatum(), ((SingleCRS) crs1).getDatum());
        assertFalse(crs0.getIdentifiers().isEmpty());
        if (METADATA_ERASED) {
            assertTrue(crs1.getIdentifiers().isEmpty());
        } else {
            assertEquals(crs0.getIdentifiers(), crs1.getIdentifiers());
        }
        mt = opFactory.createOperation(crs0, crs1).getMathTransform();
        assertFalse(mt.isIdentity());
        assertTrue(mt instanceof LinearTransform);
        matrix = ((LinearTransform) mt).getMatrix();
        assertEquals(new GeneralMatrix(new double[][] {
            {-1,  0,  0},
            { 0, -1,  0},
            { 0,  0,  1}}), new GeneralMatrix(matrix));
        /*
         * Tests the cache.
         */
        assertSame(cacheTest, factory1.createCoordinateReferenceSystem("4326"));
    }

    /**
     * Tests the creation of EPSG:4326 CRS with different axis order.
     */
    public void testLongitudeFirst() throws FactoryException {
        final CoordinateReferenceSystem standard = CRS.decode("EPSG:4326", false);
        final CoordinateReferenceSystem modified = CRS.decode("EPSG:4326", true );
        assertEquals("Expected a left-handed CS.",  -90, getAngle(standard), EPS);
        assertEquals("Expected a right-handed CS.", +90, getAngle(modified), EPS);
        final MathTransform transform = CRS.findMathTransform(standard, modified);
        assertTrue(transform instanceof LinearTransform);
        final Matrix matrix = ((LinearTransform) transform).getMatrix();
        assertEquals(new GeneralMatrix(new double[][] {
            { 0,  1,  0},
            { 1,  0,  0},
            { 0,  0,  1}}), new GeneralMatrix(matrix));
    }

    /**
     * Tests the {@link IdentifiedObjectFinder#find} method with axis order forced.
     */
    public void testFind() throws FactoryException {
        final CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory(
                "EPSG", new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));

        assertTrue(factory instanceof AbstractAuthorityFactory);
        AbstractAuthorityFactory findable = (AbstractAuthorityFactory) factory;
        final IdentifiedObjectFinder finder = findable.getIdentifiedObjectFinder(CoordinateReferenceSystem.class);

        /*
         * We tested in DefaultFactoryTest that WGS84 is not found when searching
         * directly in DefaultFactory. Now we perform the same search through the
         * ordered axis authority factory.
         */
        finder.setFullScanAllowed(false);
        assertNull("Should not find the CRS without a scan.",
                   finder.find(DefaultGeographicCRS.WGS84));

        finder.setFullScanAllowed(true);
        IdentifiedObject find = finder.find(DefaultGeographicCRS.WGS84);
        assertNotNull("With scan allowed, should find the CRS.", find);
        assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, find));
        assertEquals("Expected a right-handed CS.", +90, getAngle((CoordinateReferenceSystem) find), EPS);
        /*
         * Search a CRS using (latitude,longitude) axis order. The IdentifiedObjectFinder
         * should be able to find it even if it is backed by LongitudeFirstAuthorityFactory,
         * because the later is itself backed by EPSG factory and IdentifiedObjectFinder
         * should queries CRS from both.
         */
        final String wkt =
                "GEOGCS[\"WGS 84\",\n" +
                "  DATUM[\"WGS84\",\n" +
                "    SPHEROID[\"WGS 84\", 6378137.0, 298.257223563]],\n" +
                "  PRIMEM[\"Greenwich\", 0.0],\n" +
                "  UNIT[\"degree\", 0.017453292519943295],\n" +
                "  AXIS[\"Geodetic latitude\", NORTH],\n" +
                "  AXIS[\"Geodetic longitude\", EAST]]";
        final CoordinateReferenceSystem search   = CRS.parseWKT(wkt);
        final CoordinateReferenceSystem standard = CRS.decode("EPSG:4326", false);
        assertTrue(CRS.equalsIgnoreMetadata(search, standard));
        assertFalse("Identifiers should not be the same.", search.equals(standard));
        finder.setFullScanAllowed(false);
        assertNull("Should not find the CRS without a scan.", finder.find(search));

        finder.setFullScanAllowed(true);
        find = finder.find(search);
        final CoordinateReferenceSystem crs = (CoordinateReferenceSystem) find;
        assertNotNull("Should find the CRS despite the different axis order.", find);
        assertEquals("Expected a left-handed CS.", -90, getAngle(crs), EPS);
        assertFalse(CRS.equalsIgnoreMetadata(find, DefaultGeographicCRS.WGS84));
        assertTrue (CRS.equalsIgnoreMetadata(find, search));
        assertTrue (CRS.equalsIgnoreMetadata(find, standard));
    }
}
