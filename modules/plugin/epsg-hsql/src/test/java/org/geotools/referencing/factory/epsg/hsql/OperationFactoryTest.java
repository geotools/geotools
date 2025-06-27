/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CRSFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.ConcatenatedOperation;
import org.geotools.api.referencing.operation.Conversion;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.api.referencing.operation.Transformation;
import org.geotools.geometry.Position2D;
import org.geotools.geometry.TransformedPosition;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.operation.AbstractCoordinateOperation;
import org.geotools.referencing.operation.AuthorityBackedFactory;
import org.geotools.referencing.operation.BufferedCoordinateOperationFactory;
import org.geotools.referencing.operation.TransformTestBase;
import org.geotools.util.Classes;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the usage of {@link CoordinateOperationFactory} with the help of the EPSG database. Any EPSG plugin should fit.
 * However, this test live in the {@code plugin/epsg-hsql} module since the HSQL plugin is the only one which is
 * guaranteed to work on any machine running Maven.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class OperationFactoryTest {

    protected CRSAuthorityFactory crsAuthFactory;

    /** The default coordinate reference system factory. */
    protected CRSFactory crsFactory;

    /** The default transformations factory. */
    protected CoordinateOperationFactory opFactory;

    /** Returns the first identifier for the specified object. */
    private static String getIdentifier(final IdentifiedObject object) {
        return object.getIdentifiers().iterator().next().getCode();
    }

    /** Sets up the fixture, for example, open a network connection. This method is called before a test is executed. */
    @Before
    public void setUp() throws Exception {
        if (crsAuthFactory == null) {
            crsAuthFactory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
        }
        if (crsFactory == null) {
            crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
        }
        if (opFactory == null) {
            opFactory = ReferencingFactoryFinder.getCoordinateOperationFactory(null);
        }
    }

    /**
     * Tears down the fixture, for example, close a network connection. This method is called after a test is executed.
     */
    @After
    public void tearDown() throws Exception {}

    /**
     * Tests the creation of an operation from EPSG:4230 to EPSG:4326. They are the same CRS than the one tested in
     * {@link DefaultDataSourceTest#testTransformations}.
     */
    @Test
    public void testCreate() throws FactoryException {

        crsAuthFactory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
        opFactory = ReferencingFactoryFinder.getCoordinateOperationFactory(null);
        CoordinateReferenceSystem sourceCRS = crsAuthFactory.createCoordinateReferenceSystem("4230");
        CoordinateReferenceSystem targetCRS = crsAuthFactory.createCoordinateReferenceSystem("4326");
        CoordinateOperation operation = opFactory.createOperation(sourceCRS, targetCRS);

        Assert.assertSame(sourceCRS, operation.getSourceCRS());
        Assert.assertSame(targetCRS, operation.getTargetCRS());
        Assert.assertSame(operation, opFactory.createOperation(sourceCRS, targetCRS));
        Assert.assertTrue(
                "Expected a buffered factory but got " + opFactory.getClass().getName(),
                opFactory instanceof BufferedCoordinateOperationFactory);
        Assert.assertTrue(
                "EPSG authority factory not found.",
                ((BufferedCoordinateOperationFactory) opFactory)
                                .getImplementationHints()
                                .get(Hints.COORDINATE_OPERATION_FACTORY)
                        instanceof AuthorityBackedFactory);
        Assert.assertEquals("1612", getIdentifier(operation)); // See comment in DefaultDataSourceTest.
        Assert.assertEquals(1.0, AbstractCoordinateOperation.getAccuracy(operation), 1E-6);
        Assert.assertTrue(operation instanceof Transformation);
        /*
         * Tests a transformation not backed directly by an authority factory. However, the inverse transform may exist in the authority factory.
         */
        sourceCRS = crsAuthFactory.createCoordinateReferenceSystem("4326");
        targetCRS = crsAuthFactory.createCoordinateReferenceSystem("2995");
        operation = opFactory.createOperation(sourceCRS, targetCRS);
        Assert.assertTrue(
                "This test needs an operation not backed by the EPSG factory.",
                operation.getIdentifiers().isEmpty());
        // Should contains exactly one transformations and an arbitrary number of conversions.
        Assert.assertTrue(operation instanceof ConcatenatedOperation);
        int count = 0;
        for (org.geotools.api.referencing.operation.SingleOperation singleOperation :
                ((ConcatenatedOperation) operation).getOperations()) {
            final CoordinateOperation op = singleOperation;
            if (op instanceof Transformation) {
                count++;
            } else {
                Assert.assertTrue(
                        "Expected Conversion but got "
                                + Classes.getShortName(AbstractCoordinateOperation.getType(op))
                                + ". ",
                        op instanceof Conversion);
            }
        }
        Assert.assertEquals("The coordinate operation should contains exactly 1 transformation", 1, count);
        Assert.assertTrue(AbstractCoordinateOperation.getAccuracy(operation) <= 25);
    }

    /**
     * The TransformedPosition class is a thin wrapper around CoordinateOperationFactory used for time critical
     * functions.
     *
     * <p>This test isolates a regression introduced during GEOT-22 change of vector library.
     */
    @Test
    public void testTransformedDirectPosition() throws Exception {
        CoordinateReferenceSystem requestedCRS = CRS.parseWKT("PROJCS[\"WGS84 / Google Mercator\","
                + "  GEOGCS[\"WGS 84\", "
                + "   DATUM[\"World Geodetic System 1984\", "
                + "     SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]],"
                + "     AUTHORITY[\"EPSG\",\"6326\"]], "
                + "   PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],"
                + "   UNIT[\"degree\", 0.017453292519943295],"
                + "   AXIS[\"Longitude\", EAST], "
                + "   AXIS[\"Latitude\", NORTH], "
                + "   AUTHORITY[\"EPSG\",\"4326\"]],"
                + " PROJECTION[\"Mercator_1SP\"], "
                + " PARAMETER[\"semi_minor\", 6378137.0], "
                + " PARAMETER[\"latitude_of_origin\", 0.0],"
                + " PARAMETER[\"central_meridian\", 0.0],"
                + " PARAMETER[\"scale_factor\", 1.0], "
                + " PARAMETER[\"false_easting\", 0.0], "
                + " PARAMETER[\"false_northing\", 0.0],"
                + " UNIT[\"m\", 1.0], "
                + " AXIS[\"x\", EAST], "
                + " AXIS[\"y\", NORTH], "
                + " AUTHORITY[\"EPSG\",\"900913\"]]");
        CoordinateReferenceSystem targetCRS = CRS.parseWKT("PROJCS[\"Lisboa_Hayford_Gauss_IGeoE\", "
                + "  GEOGCS[\"GCS_Datum_Lisboa_Hayford\", "
                + "    DATUM[\"D_Datum_Lisboa_Hayford\", "
                + "      SPHEROID[\"International_1924\", 6378388.0, 297.0], "
                + "      TOWGS84[-288.885, -91.744, 126.244, -1.691, 0.41, -0.211, -4.598]],"
                + "    PRIMEM[\"Greenwich\", 0.0], "
                + "    UNIT[\"degree\", 0.017453292519943295],"
                + "    AXIS[\"Longitude\", EAST], "
                + "    AXIS[\"Latitude\", NORTH]], "
                + "  PROJECTION[\"Transverse_Mercator\"], "
                + "  PARAMETER[\"central_meridian\", -8.131906111111114], "
                + "  PARAMETER[\"latitude_of_origin\", 39.666666666666664],"
                + "  PARAMETER[\"scale_factor\", 1.0], "
                + "  PARAMETER[\"false_easting\", 200000.0], "
                + "  PARAMETER[\"false_northing\", 300000.0],"
                + "  UNIT[\"m\", 1.0], "
                + "  AXIS[\"x\", EAST], "
                + "  AXIS[\"y\", NORTH]]");

        Position position = new Position2D(requestedCRS, -886885.0962724264, 4468200.416916506);
        final TransformedPosition arbitraryToInternal =
                new TransformedPosition(requestedCRS, targetCRS, new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));
        arbitraryToInternal.transform(position);

        Position expected = new Position2D(targetCRS, 214741.10238960697, 26957.60506898933);

        // geoserver vecmath: TransformedPosition[214741.10238960697, 26957.60506898933]
        // geoserver ejml: TransformedPosition[214636.7447572897, 27218.077500249085]
        // geotools ejml: TransformedPosition[214636.7447572897, 27218.077500249085]
        Assert.assertEquals(expected.getOrdinate(0), arbitraryToInternal.getOrdinate(0), 1e-9);
        Assert.assertEquals(expected.getOrdinate(1), arbitraryToInternal.getOrdinate(1), 1e-9);
    }

    /**
     * Tests findOperations method for a pair of known CRSs. 23030 (Projected) to 4326 (geographic 2D) with different
     * datum
     */
    @Test
    public void testFindOperationsProjected2Geographic() throws Exception {
        String source = "EPSG:23030";
        String target = "EPSG:4326";
        int min = 37;
        String expectedText = "AUTHORITY[\"EPSG\",\"1612\"]";

        Set<CoordinateOperation> operations = findOperations(source, target);
        int size = operations.size();
        Assert.assertTrue(size >= min); // at least min operations should be registered in the database for
        // this CRS pair
        assertOperationContained(operations, expectedText);
    }

    /** Tests findOperations method for a pair of known CRSs. 25830 (Projected) to 3035 (Projected), same datum */
    @Test
    public void testFindOperationsProjected2ProjectedSameDatum() throws Exception {
        String source = "EPSG:25830";
        String target = "EPSG:3035";
        String expectedText = "AUTHORITY[\"EPSG\",\"19986\"]";

        Set<CoordinateOperation> operations = findOperations(source, target);
        Assert.assertEquals(1, operations.size()); // same datum, exactly one operation expected
        assertOperationContained(operations, expectedText);
    }

    /**
     * Tests findOperations method for a pair of known CRSs. 23030 (Projected) to 3034 (Projected), with different datum
     */
    @Test
    public void testFindOperationsProjected2ProjectedDiffDatum() throws Exception {
        String source = "EPSG:3034";
        String target = "EPSG:23030";
        int min = 9;
        String expectedText = "AUTHORITY[\"EPSG\",\"9606\"]";

        Set<CoordinateOperation> operations = findOperations(source, target);
        int size = operations.size();
        Assert.assertTrue(size >= min); // at least min operations should be registered in the database for
        // this CRS pair
        assertOperationContained(operations, expectedText);
    }

    /**
     * Tests findOperations method for a pair of known CRSs. 4258 (Geographic2D) to 4326 (Geographic2D), with different
     * datum
     */
    @Test
    public void testFindOperationsGeographic2Geographic() throws Exception {
        String source = "EPSG:4258";
        String target = "EPSG:4326";
        int min = 2;
        String expectedText = "AUTHORITY[\"EPSG\",\"1149\"]";

        Set<CoordinateOperation> operations = findOperations(source, target);
        int size = operations.size();
        Assert.assertTrue(size >= min); // at least min operations should be registered in the database for
        // this CRS pair
        assertOperationContained(operations, expectedText);
    }

    /**
     * Tests findOperations method for a pair of known CRSs when a nadcon grid transformation exists among them: 4258
     * (Geographic2D) to 4326 (Geographic2D) with different datum
     */
    @Test
    public void testFindOperationsGeographic2GeographicNadCon() throws Exception {
        String source = "EPSG:4138";
        String target = "EPSG:4326";
        int min = 2;
        String expectedText = "NADCON";

        Set<CoordinateOperation> operations = findOperations(source, target);
        int size = operations.size();
        Assert.assertTrue(size >= min); // at least min operations should be registered in the database for
        // this CRS pair
        assertOperationContained(operations, expectedText);
    }

    /**
     * Asserts that all the operations in the provided set fulfill the following conditions:
     *
     * <ul>
     *   <li>the source CRS of the operation matches the expected source
     *   <li>the target CRS of the operation matches the expected target
     *   <li>Tests the math transform using TransformTestBase.assertInterfaced
     */
    public void assertOperations(
            CoordinateReferenceSystem sourceCRS,
            CoordinateReferenceSystem targetCRS,
            Set<CoordinateOperation> operations) {
        for (CoordinateOperation operation : operations) {
            Assert.assertSame(sourceCRS, operation.getSourceCRS());
            Assert.assertSame(targetCRS, operation.getTargetCRS());
            final MathTransform transform = operation.getMathTransform();
            TransformTestBase.assertInterfaced(transform);
        }
    }

    /**
     * Asserts that at least one of the operations contains the <code>expectedText</code> in the output of toString()
     * method
     */
    public void assertOperationContained(Set<CoordinateOperation> operations, String expectedText) {
        boolean textFound = false;
        for (CoordinateOperation operation : operations) {
            if (operation.toString().contains(expectedText)) {
                textFound = true;
            }
        }
        Assert.assertTrue(textFound);
    }

    /**
     * Tests the method findOperations for the provided CRS code pairs. The operations are tested in the provided order
     * and also in reverse order (since they involve different implementation paths)
     *
     * @param source The code of the source CRS to test
     * @param target The code of the target CRS to test
     * @return The set of found operations in direct order
     * @see #assertOperations(CoordinateReferenceSystem, CoordinateReferenceSystem, Set)
     */
    public Set<CoordinateOperation> findOperations(String source, String target)
            throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateReferenceSystem sourceCRS = crsAuthFactory.createCoordinateReferenceSystem(source);
        CoordinateReferenceSystem targetCRS = crsAuthFactory.createCoordinateReferenceSystem(target);
        // direct order
        Set<CoordinateOperation> operations = opFactory.findOperations(sourceCRS, targetCRS);
        assertOperations(sourceCRS, targetCRS, operations);

        int size = operations.size();

        // try reverse order
        opFactory = ReferencingFactoryFinder.getCoordinateOperationFactory(null);
        Set<CoordinateOperation> reverseOperations = opFactory.findOperations(targetCRS, sourceCRS);
        Assert.assertEquals(size, reverseOperations.size());

        assertOperations(targetCRS, sourceCRS, reverseOperations);
        return operations;
    }

    /**
     * Ensures the provided source and target CRS match the source and target CRS of the operation. Ensures the
     * operation math transform passes TransformTestBase.assertInterfaced assertion.
     */
    public static void assertOperation(
            CoordinateOperation operation, CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS) {
        Assert.assertSame(sourceCRS, operation.getSourceCRS());
        Assert.assertSame(targetCRS, operation.getTargetCRS());
        TransformTestBase.assertInterfaced(operation.getMathTransform());
    }

    /**
     * Tests findOperations method for CompoundCRS with a VerticalCRS component using the same datum for the horizontal
     * component. EPSG:5555 (Projected+Vertical) to EPSG:5554 (Projected+Vertical) using same datum.
     * EPSG:25831+EPSG:5783 (Projected+Vertical) to EPSG:5554 (Projected+Vertical) using same datum.
     */
    @Test
    public void testCreateOperationCompound2CompoundVertical() throws Exception {

        CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
        final CRSAuthorityFactory crsAuthFactory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
        final CoordinateOperationFactory opFactory = ReferencingFactoryFinder.getCoordinateOperationFactory(null);

        CoordinateReferenceSystem sourceCRS = crsAuthFactory.createCoordinateReferenceSystem("EPSG:5555");
        CoordinateReferenceSystem targetCRS = crsAuthFactory.createCoordinateReferenceSystem("EPSG:5554");
        // direct order
        CoordinateOperation operation = opFactory.createOperation(sourceCRS, targetCRS);
        assertOperation(operation, sourceCRS, targetCRS);

        // try reverse order
        operation = opFactory.createOperation(targetCRS, sourceCRS);
        assertOperation(operation, targetCRS, sourceCRS);

        CoordinateReferenceSystem sourceHorizontalCRS = crsAuthFactory.createCoordinateReferenceSystem("EPSG:25831");
        CoordinateReferenceSystem sourceVerticalCRS = crsAuthFactory.createCoordinateReferenceSystem("EPSG:5783");
        Map<String, Object> properties = new HashMap<>();
        properties.put(
                IdentifiedObject.NAME_KEY, new NamedIdentifier(Citations.fromName("TEST"), "Compound 28530+5783"));
        CoordinateReferenceSystem[] elements = {sourceHorizontalCRS, sourceVerticalCRS};
        sourceCRS = crsFactory.createCompoundCRS(properties, elements);

        // direct order
        operation = opFactory.createOperation(sourceCRS, targetCRS);
        assertOperation(operation, sourceCRS, targetCRS);

        // try reverse order
        operation = opFactory.createOperation(targetCRS, sourceCRS);
        assertOperation(operation, targetCRS, sourceCRS);
    }

    @Test
    public void testGetSpecificTransform() throws FactoryException, TransformException, ParseException {

        CoordinateReferenceSystem sourceCRS = crsAuthFactory.createCoordinateReferenceSystem("EPSG:21036");
        CoordinateReferenceSystem targetCRS = crsAuthFactory.createCoordinateReferenceSystem("EPSG:32736");

        MathTransform crsTransform = CRS.findMathTransform(sourceCRS, targetCRS, "EPSG:1285");
        // in an ideal world we would examine the transform to see what the TOWGS parameter is but
        // that is locked away from us.
        assertTrue(crsTransform.toString().contains("PARAMETER[\"dx\", -175.0]"));
        assertTrue(crsTransform.toString().contains("PARAMETER[\"dy\", -23.0]"));
        assertTrue(crsTransform.toString().contains("PARAMETER[\"dz\", -303.0]"));
        Position input = new Position2D(sourceCRS, 790615.026, 9316007.421);
        Position expected = new Position2D(targetCRS, 790691.8117871293, 9315700.848637346);
        Position output = new Position2D();
        crsTransform.transform(input, output);
        assertPointsEqual(expected, output);

        sourceCRS = crsAuthFactory.createCoordinateReferenceSystem("EPSG:27700");
        targetCRS = crsAuthFactory.createCoordinateReferenceSystem("EPSG:4326");
        crsTransform = CRS.findMathTransform(sourceCRS, targetCRS, "EPSG:1199");
        assertNotNull(crsTransform);
        input = new Position2D(sourceCRS, 10000, 40000);
        expected = new Position2D(targetCRS, 50.1314925, -7.4594847);
        output = new Position2D();
        crsTransform.transform(input, output);
        assertPointsEqual(expected, output);
    }

    @Test
    public void testGetTransforms() throws FactoryException, TransformException, ParseException {

        CoordinateReferenceSystem sourceCRS = crsAuthFactory.createCoordinateReferenceSystem("EPSG:21036");
        CoordinateReferenceSystem targetCRS = crsAuthFactory.createCoordinateReferenceSystem("EPSG:32736");
        Map<String, AbstractCoordinateOperation> transforms = CRS.getTransforms(sourceCRS, targetCRS);
        String[] expected = {"EPSG:3998", "EPSG:1284", "EPSG:1122", "EPSG:1285"};
        Set<String> keys = transforms.keySet();

        assertEquals("Wrong number of transforms", expected.length, keys.size());
        for (String code : expected) {
            assertTrue("Expected key " + code + " not found", keys.contains(code));
        }

        sourceCRS = crsAuthFactory.createCoordinateReferenceSystem("EPSG:27700");
        targetCRS = crsAuthFactory.createCoordinateReferenceSystem("EPSG:4326");
        transforms = CRS.getTransforms(sourceCRS, targetCRS);
        String[] expected2 = {"EPSG:1195", "EPSG:1314", "EPSG:5622", "EPSG:1197", "EPSG:1196", "EPSG:1199", "EPSG:1198"
        };
        keys = transforms.keySet();

        assertEquals("Wrong number of transforms", expected2.length, keys.size());
        for (String code : expected2) {
            assertTrue("Expected key " + code + " not found", keys.contains(code));
            AbstractCoordinateOperation op = transforms.get(code);
            assertNotNull(op);
            assertNotNull(op.getMathTransform());
        }

        CoordinateReferenceSystem gda94 = CRS.decode("epsg:4283");
        CoordinateReferenceSystem gda2020 = CRS.decode("epsg:7844");

        transforms = CRS.getTransforms(gda94, gda2020);

        String[] expected3 = {"EPSG:8048", "EPSG:8447"};
        keys = transforms.keySet();

        assertEquals("Wrong number of transforms", expected3.length, keys.size());
        for (String code : expected3) {
            assertTrue("Expected key " + code + " not found", keys.contains(code));
            AbstractCoordinateOperation op = transforms.get(code);
            assertNotNull(op);
            assertNotNull(op.getMathTransform());
        }
    }
    /**
     * @param expected
     * @param output
     */
    private void assertPointsEqual(Position expected, Position output) {
        Assert.assertEquals(expected.getOrdinate(0), output.getOrdinate(0), 1e-6);
        Assert.assertEquals(expected.getOrdinate(1), output.getOrdinate(1), 1e-6);
    }
}
