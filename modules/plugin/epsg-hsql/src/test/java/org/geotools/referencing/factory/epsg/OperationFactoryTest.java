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
package org.geotools.referencing.factory.epsg;

import java.util.Iterator;
import java.util.logging.Level;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.factory.Hints;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.TransformedDirectPosition;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.operation.AbstractCoordinateOperation;
import org.geotools.referencing.operation.AuthorityBackedFactory;
import org.geotools.referencing.operation.BufferedCoordinateOperationFactory;
import org.geotools.resources.Arguments;
import org.geotools.resources.Classes;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.ConcatenatedOperation;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.Transformation;


/**
 * Tests the usage of {@link CoordinateOperationFactory} with the help of the
 * EPSG database. Any EPSG plugin should fit. However, this test live in the
 * {@code plugin/epsg-hsql} module since the HSQL plugin is the only one which
 * is guaranteed to work on any machine running Maven.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class OperationFactoryTest extends TestCase {
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
        return new TestSuite(OperationFactoryTest.class);
    }

    /**
     * Constructs a test case with the given name.
     */
    public OperationFactoryTest(final String name) {
        super(name);
    }

    /**
     * Returns the first identifier for the specified object.
     */
    private static String getIdentifier(final IdentifiedObject object) {
        return object.getIdentifiers().iterator().next().getCode();
    }

    /**
     * Tests the creation of an operation from EPSG:4230 to EPSG:4326. They are the same
     * CRS than the one tested in {@link DefaultDataSourceTest#testTransformations}.
     */
    public void testCreate() throws FactoryException {
        final CRSAuthorityFactory       crsFactory;
        final CoordinateOperationFactory opFactory;
              CoordinateReferenceSystem  sourceCRS;
              CoordinateReferenceSystem  targetCRS;
              CoordinateOperation        operation;

        crsFactory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
        opFactory  = ReferencingFactoryFinder.getCoordinateOperationFactory(null);
        sourceCRS  = crsFactory.createCoordinateReferenceSystem("4230");
        targetCRS  = crsFactory.createCoordinateReferenceSystem("4326");
        operation  = opFactory.createOperation(sourceCRS, targetCRS);

        assertSame(sourceCRS, operation.getSourceCRS());
        assertSame(targetCRS, operation.getTargetCRS());
        assertSame(operation, opFactory.createOperation(sourceCRS, targetCRS));
        assertTrue("Expected a buffered factory but got " + opFactory.getClass().getName(),
                opFactory instanceof BufferedCoordinateOperationFactory);
        assertTrue("EPSG authority factory not found.",
                ((BufferedCoordinateOperationFactory) opFactory).getImplementationHints().
                get(Hints.COORDINATE_OPERATION_FACTORY) instanceof AuthorityBackedFactory);
        assertEquals("1612", getIdentifier(operation)); // See comment in DefaultDataSourceTest.
        assertEquals(1.0, AbstractCoordinateOperation.getAccuracy(operation), 1E-6);
        assertTrue(operation instanceof Transformation);
        /*
         * Tests a transformation not backed directly by an authority factory.
         * However, the inverse transform may exist in the authority factory.
         */
        sourceCRS  = crsFactory.createCoordinateReferenceSystem("4326");
        targetCRS  = crsFactory.createCoordinateReferenceSystem("2995");
        operation  = opFactory.createOperation(sourceCRS, targetCRS);
        assertTrue("This test needs an operation not backed by the EPSG factory.",
                   operation.getIdentifiers().isEmpty());
        // Should contains exactly one transformations and an arbitrary number of conversions.
        assertTrue(operation instanceof ConcatenatedOperation);
        int count = 0;
        for (final Iterator it=((ConcatenatedOperation) operation).getOperations().iterator(); it.hasNext();) {
            final CoordinateOperation op = (CoordinateOperation) it.next();
            if (op instanceof Transformation) {
                count++;
            } else {
                assertTrue("Expected Conversion but got " +
                           Classes.getShortName(AbstractCoordinateOperation.getType(op)) + ". ",
                           (op instanceof Conversion));
            }
        }
        assertEquals("The coordinate operation should contains exactly 1 transformation", 1, count);
        assertTrue(AbstractCoordinateOperation.getAccuracy(operation) <= 25);
    }
    
    /**
     * The TransformedDirectPosition class is a thin wrapper around
     * CoordinateOperationFactory used for time critical functions.
     * 
     * This test isolates a regression introduced during GEOT-22 change
     * of vector library.
     */
    public void testTransformedDirectPosition() throws Exception {
        CoordinateReferenceSystem requestedCRS = CRS.parseWKT(
                "PROJCS[\"WGS84 / Google Mercator\","+ 
                "  GEOGCS[\"WGS 84\", "+
                "   DATUM[\"World Geodetic System 1984\", "+
                "     SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]],"+ 
                "     AUTHORITY[\"EPSG\",\"6326\"]], "+
                "   PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],"+ 
                "   UNIT[\"degree\", 0.017453292519943295],"+ 
                "   AXIS[\"Longitude\", EAST], "+
                "   AXIS[\"Latitude\", NORTH], "+
                "   AUTHORITY[\"EPSG\",\"4326\"]],"+ 
                " PROJECTION[\"Mercator_1SP\"], "+
                " PARAMETER[\"semi_minor\", 6378137.0], "+
                " PARAMETER[\"latitude_of_origin\", 0.0],"+ 
                " PARAMETER[\"central_meridian\", 0.0],"+ 
                " PARAMETER[\"scale_factor\", 1.0], "+
                " PARAMETER[\"false_easting\", 0.0], "+
                " PARAMETER[\"false_northing\", 0.0],"+ 
                " UNIT[\"m\", 1.0], "+
                " AXIS[\"x\", EAST], "+
                " AXIS[\"y\", NORTH], "+
                " AUTHORITY[\"EPSG\",\"900913\"]]");
        CoordinateReferenceSystem targetCRS = CRS. parseWKT(
                "PROJCS[\"Lisboa_Hayford_Gauss_IGeoE\", "+
                "  GEOGCS[\"GCS_Datum_Lisboa_Hayford\", "+
                "    DATUM[\"D_Datum_Lisboa_Hayford\", "+
                "      SPHEROID[\"International_1924\", 6378388.0, 297.0], "+
                "      TOWGS84[-288.885, -91.744, 126.244, -1.691, 0.41, -0.211, -4.598]],"+ 
                "    PRIMEM[\"Greenwich\", 0.0], "+
                "    UNIT[\"degree\", 0.017453292519943295],"+ 
                "    AXIS[\"Longitude\", EAST], "+
                "    AXIS[\"Latitude\", NORTH]], "+
                "  PROJECTION[\"Transverse_Mercator\"], "+
                "  PARAMETER[\"central_meridian\", -8.131906111111114], "+
                "  PARAMETER[\"latitude_of_origin\", 39.666666666666664],"+ 
                "  PARAMETER[\"scale_factor\", 1.0], "+
                "  PARAMETER[\"false_easting\", 200000.0], "+
                "  PARAMETER[\"false_northing\", 300000.0],"+ 
                "  UNIT[\"m\", 1.0], "+
                "  AXIS[\"x\", EAST], "+
                "  AXIS[\"y\", NORTH]]");

        DirectPosition position = new DirectPosition2D(requestedCRS, -886885.0962724264, 4468200.416916506);
        final TransformedDirectPosition arbitraryToInternal = new TransformedDirectPosition(
                requestedCRS, targetCRS, new Hints(Hints.LENIENT_DATUM_SHIFT,
                        Boolean.TRUE));
        arbitraryToInternal.transform(position);
        
        DirectPosition expected = new DirectPosition2D(targetCRS, 214741.10238960697,
                26957.60506898933);
        
        // geoserver vecmath:  TransformedDirectPosition[214741.10238960697, 26957.60506898933]
        // geoserver    ejml:  TransformedDirectPosition[214636.7447572897,  27218.077500249085]
        // geotools     ejml:  TransformedDirectPosition[214636.7447572897,  27218.077500249085]
        assertEquals(expected.getOrdinate(0), arbitraryToInternal.getOrdinate(0), 1e-9);
        assertEquals(expected.getOrdinate(1), arbitraryToInternal.getOrdinate(1), 1e-9);
    }
}
