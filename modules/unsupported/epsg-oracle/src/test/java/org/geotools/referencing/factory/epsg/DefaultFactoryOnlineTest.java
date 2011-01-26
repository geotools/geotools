/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

// J2SE dependencies and extensions
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.sql.SQLException;
import java.awt.geom.AffineTransform;
import javax.measure.unit.Unit;

// JUnit dependencies
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

// OpenGIS dependencies
import org.opengis.referencing.*;
import org.opengis.referencing.cs.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.datum.*;
import org.opengis.referencing.operation.*;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.geometry.Envelope;

// Geotools dependencies
import org.geotools.TestData;
import org.geotools.factory.Hints;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.operation.AbstractCoordinateOperation;
import org.geotools.referencing.operation.transform.AbstractMathTransform;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.factory.IdentifiedObjectFinder;
import org.geotools.referencing.factory.AbstractAuthorityFactory;
import org.geotools.referencing.factory.epsg.oracle.OracleOnlineTestCase;
import org.geotools.metadata.iso.extent.GeographicBoundingBoxImpl;
import org.geotools.resources.Arguments;


/**
 * Tests transformations from CRS and/or operations created from the EPSG factory, using
 * the default plugin.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Vadim Semenov
 */
public class DefaultFactoryOnlineTest extends OracleOnlineTestCase {
    /**
     * Set to {@code true} for verbose tests.
     */
    private static boolean verbose = false;

    /**
     * Set to {@code true} for more extensive tests.
     */
    private static boolean extensive = false;

    /**
     * Small value for parameter value comparaisons.
     */
    private static final double EPS = 1E-6;

    /**
     * The EPSG factory to test. Will be setup only when first needed.
     */
    private ThreadedEpsgFactory factory;

    /**
     * Sets up the authority factory.
     */
    
    
    protected void connect() throws Exception {
        super.connect();
        if (datasource == null) {
            return;
        }
        
        if (factory == null) {
            factory = (ThreadedEpsgFactory) ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG",
                        new Hints(Hints.CRS_AUTHORITY_FACTORY, ThreadedEpsgFactory.class));
            extensive |= TestData.isExtensiveTest();
            if (verbose) {
                System.out.print("Database version: ");
                System.out.println(factory.getImplementationHints().get(Hints.VERSION));
            }
        }
        // No 'tearDown()' method: we rely on the ThreadedEpsgFactory shutdown hook.
    }

    /**
     * Make sure that the factory extracted from the registry in the {@link #setUp} method
     * is a singleton. It may not be the case when there is a bug in {@code FactoryRegistry}.
     */
    public void testRegistry() {
        final Object candidate = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
        if (candidate instanceof ThreadedEpsgFactory) {
            assertSame(factory, candidate);
        }
    }

    /**
     * Returns the first identifier for the specified object.
     */
    private static String getIdentifier(final IdentifiedObject object) {
        return object.getIdentifiers().iterator().next().getCode();
    }

    /**
     * Tests creations of CRS objects.
     */
    public void testCreation() throws FactoryException {
        final CoordinateOperationFactory opf = ReferencingFactoryFinder.getCoordinateOperationFactory(null);
        CoordinateReferenceSystem sourceCRS, targetCRS;
        CoordinateOperation operation;
        ParameterValueGroup parameters;
        
        sourceCRS = factory.createCoordinateReferenceSystem("4274");
        assertEquals("4274", getIdentifier(sourceCRS));
        assertTrue(sourceCRS instanceof GeographicCRS);
        assertEquals(2, sourceCRS.getCoordinateSystem().getDimension());
        
        sourceCRS = factory.createCoordinateReferenceSystem("EPSG:4140");
        assertEquals("4140", getIdentifier(sourceCRS));
        assertTrue(sourceCRS instanceof GeographicCRS);
        assertEquals(2, sourceCRS.getCoordinateSystem().getDimension());
        
        sourceCRS = factory.createCoordinateReferenceSystem("2027");
        assertEquals("2027", getIdentifier(sourceCRS));
        assertTrue(sourceCRS instanceof ProjectedCRS);
        assertEquals(2, sourceCRS.getCoordinateSystem().getDimension());
        parameters = ((ProjectedCRS) sourceCRS).getConversionFromBase().getParameterValues();
        assertEquals(   -93, parameters.parameter("central_meridian"  ).doubleValue(), EPS);
        assertEquals(     0, parameters.parameter("latitude_of_origin").doubleValue(), EPS);
        assertEquals(0.9996, parameters.parameter("scale_factor"      ).doubleValue(), EPS);
        assertEquals(500000, parameters.parameter("false_easting"     ).doubleValue(), EPS);
        assertEquals(     0, parameters.parameter("false_northing"    ).doubleValue(), EPS);
        
        sourceCRS = factory.createCoordinateReferenceSystem(" EPSG : 2442 ");
        assertEquals("2442", getIdentifier(sourceCRS));
        assertTrue(sourceCRS instanceof ProjectedCRS);
        assertEquals(2, sourceCRS.getCoordinateSystem().getDimension());
        parameters = ((ProjectedCRS) sourceCRS).getConversionFromBase().getParameterValues();
        assertEquals(   135, parameters.parameter("central_meridian"  ).doubleValue(), EPS);
        assertEquals(     0, parameters.parameter("latitude_of_origin").doubleValue(), EPS);
        assertEquals(     1, parameters.parameter("scale_factor"      ).doubleValue(), EPS);
        assertEquals(500000, parameters.parameter("false_easting"     ).doubleValue(), EPS);
        assertEquals(     0, parameters.parameter("false_northing"    ).doubleValue(), EPS);
        
        sourceCRS = factory.createCoordinateReferenceSystem("EPSG:4915");
        assertEquals("4915", getIdentifier(sourceCRS));
        assertTrue(sourceCRS instanceof GeocentricCRS);
        assertEquals(3, sourceCRS.getCoordinateSystem().getDimension());
        
        sourceCRS = factory.createCoordinateReferenceSystem("EPSG:4993");
        assertEquals("4993", getIdentifier(sourceCRS));
        assertTrue(sourceCRS instanceof GeographicCRS);
        assertEquals(3, sourceCRS.getCoordinateSystem().getDimension());
        
        sourceCRS = factory.createCoordinateReferenceSystem("EPSG:5735");
        assertEquals("5735", getIdentifier(sourceCRS));
        assertTrue(sourceCRS instanceof VerticalCRS);
        assertEquals(1, sourceCRS.getCoordinateSystem().getDimension());
        
        sourceCRS = factory.createCoordinateReferenceSystem("EPSG:5801");
        assertEquals("5801", getIdentifier(sourceCRS));
        assertTrue(sourceCRS instanceof EngineeringCRS);
        assertEquals(2, sourceCRS.getCoordinateSystem().getDimension());
        
        sourceCRS = factory.createCoordinateReferenceSystem("EPSG:7400");
        assertEquals("7400", getIdentifier(sourceCRS));
        assertTrue(sourceCRS instanceof CompoundCRS);
        assertEquals(3, sourceCRS.getCoordinateSystem().getDimension());

        // GeographicCRS without datum
        sourceCRS = factory.createCoordinateReferenceSystem("63266405");
        assertTrue(sourceCRS instanceof GeographicCRS);
        assertEquals("WGS 84 (deg)", sourceCRS.getName().getCode());
        assertEquals(2, sourceCRS.getCoordinateSystem().getDimension());

        // Operations
        sourceCRS = factory.createCoordinateReferenceSystem("4273");
        targetCRS = factory.createCoordinateReferenceSystem("4979");
        operation = opf.createOperation(sourceCRS, targetCRS);
        assertNotSame(sourceCRS, targetCRS);
        assertFalse(operation.getMathTransform().isIdentity());

        assertSame(sourceCRS, factory.createCoordinateReferenceSystem("EPSG:4273"));
        assertSame(targetCRS, factory.createCoordinateReferenceSystem("EPSG:4979"));

        assertSame(sourceCRS, factory.createCoordinateReferenceSystem(" EPSG : 4273 "));
        assertSame(targetCRS, factory.createCoordinateReferenceSystem(" EPSG : 4979 "));

        // CRS with "South along 180 deg" and "South along 90 deg East" axis
        sourceCRS = factory.createCoordinateReferenceSystem("EPSG:32661");
        targetCRS = factory.createCoordinateReferenceSystem("4326");
        operation = opf.createOperation(sourceCRS, targetCRS);
        final MathTransform    transform = operation.getMathTransform();
        final CoordinateSystem  sourceCS = sourceCRS.getCoordinateSystem();
        final CoordinateSystemAxis axis0 = sourceCS.getAxis(0);
        final CoordinateSystemAxis axis1 = sourceCS.getAxis(1);
        assertEquals("Northing",                axis0.getName().getCode());
        assertEquals("Easting",                 axis1.getName().getCode());
        assertEquals("South along 180 deg",     axis0.getDirection().name());
        assertEquals("South along 90 deg East", axis1.getDirection().name());
        assertFalse(transform.isIdentity());
        assertTrue(transform instanceof ConcatenatedTransform);
        ConcatenatedTransform ct = (ConcatenatedTransform) transform;
        // An affine transform for swapping axis should be
        // performed before and after the map projection.
        final int mask = AffineTransform.TYPE_FLIP              |
                         AffineTransform.TYPE_QUADRANT_ROTATION |
                         AffineTransform.TYPE_UNIFORM_SCALE;
        assertTrue(ct.transform1 instanceof AffineTransform);
        assertEquals(mask, ((AffineTransform) ct.transform1).getType());
        assertTrue(ct.transform2 instanceof ConcatenatedTransform);
        ct = (ConcatenatedTransform) ct.transform2;
        assertTrue(ct.transform1 instanceof AbstractMathTransform);
        assertTrue(ct.transform2 instanceof AffineTransform);
        assertEquals(mask, ((AffineTransform) ct.transform2).getType());
    }

    /**
     * Tests closing the factory after the timeout. <strong>IMPORTANT:</strong> This test must
     * be run after {@link #testCreation} and before any call to {@code getAuthorityCodes()}.
     */
    public void testTimeout() throws FactoryException {
        System.gc();              // If there is any object holding a connection to the EPSG
        System.runFinalization(); // database, running finalizers may help to close them.
        factory.setTimeout(200);
        // Fetch this CRS first in order to prevent garbage collection.
        CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("4273");
        assertEquals("4273", getIdentifier(crs));
        try {
            assertTrue(factory.isConnected());
            Thread.currentThread().sleep(800);
            System.gc();
            System.runFinalization();
            assertFalse(factory.isConnected());
        } catch (InterruptedException e) {
            fail(e.getLocalizedMessage());
        }
        assertFalse(factory.isConnected());
        // Should be in the cache.
        assertSame(crs, factory.createCoordinateReferenceSystem("4273"));
        assertFalse(factory.isConnected());
        // Was not in the cache
        assertEquals("4275", getIdentifier(factory.createCoordinateReferenceSystem("4275")));
        assertTrue(factory.isConnected());
        factory.setTimeout(30*60*1000L);
    }

    /**
     * Tests the creation of CRS using name instead of primary keys. Note that this test
     * contains a call to {@code getDescriptionText(...)}, and concequently must be run
     * after {@link #testTimeout}. See {@link #testDescriptionText}.
     */
    public void testNameUsage() throws FactoryException {
        /*
         * Tests unit
         */
        assertSame   (factory.createUnit("9002"), factory.createUnit("foot"));
        assertNotSame(factory.createUnit("9001"), factory.createUnit("foot"));
        /*
         * Tests CRS
         */
        final CoordinateReferenceSystem primary, byName;
        primary = factory.createCoordinateReferenceSystem("27581");
        assertEquals("27581", getIdentifier(primary));
        assertTrue(primary instanceof ProjectedCRS);
        assertEquals(2, primary.getCoordinateSystem().getDimension());
        /*
         * Gets the CRS by name. It should be the same.
         */
        byName = factory.createCoordinateReferenceSystem("NTF (Paris) / France I");
        assertEquals(primary, byName);
        /*
         * Gets the CRS using 'createObject'. It will requires ony more
         * SQL statement internally in order to determines the object type.
         */
        factory.dispose(); // Clear the cache. This is not a real disposal.
        assertEquals(primary, factory.createObject("27581"));
        assertEquals(byName,  factory.createObject("NTF (Paris) / France I"));
        /*
         * Tests descriptions.
         */
        assertEquals("NTF (Paris) / France I", factory.getDescriptionText("27581").toString());
        /*
         * Tests fetching an object with name containing semi-colon.
         */
        final IdentifiedObject cs = factory.createCoordinateSystem(
                "Ellipsoidal 2D CS. Axes: latitude, longitude. Orientations: north, east.  UoM: DMS");
        assertEquals("6411", getIdentifier(cs));
        /*
         * Tests with a unknown name. The exception should be NoSuchAuthorityCodeException
         */
        try {
            factory.createGeographicCRS("WGS84");
            fail();
        } catch (NoSuchAuthorityCodeException e) {
            // This is the expected exception.
            assertEquals("WGS84", e.getAuthorityCode());
        }
    }

    /**
     * Tests the {@link AuthorityFactory#getDescriptionText} method. Note that the default
     * implementation of {@code getDescriptionText(...)} invokes {@code getAuthorityCodes()},
     * and concequently this test must be run after {@link #testTimeout}.
     */
    public void testDescriptionText() throws FactoryException {
        assertEquals("World Geodetic System 1984", factory.getDescriptionText( "6326").toString(Locale.ENGLISH));
        assertEquals("Mean Sea Level",             factory.getDescriptionText( "5100").toString(Locale.ENGLISH));
        assertEquals("NTF (Paris) / Nord France",  factory.getDescriptionText("27591").toString(Locale.ENGLISH));
        assertEquals("Ellipsoidal height",         factory.getDescriptionText(   "84").toString(Locale.ENGLISH));
    }

    /**
     * Tests the {@code getAuthorityCodes()} method.
     */
    public void testAuthorityCodes() throws FactoryException {
        final Set crs = factory.getAuthorityCodes(CoordinateReferenceSystem.class);
        assertFalse(crs.isEmpty());
        assertEquals("Check size() consistency", crs.size(), crs.size());
        assertTrue(crs.size() > 0); // Must be after the 'assertEquals' above.

        final Set geographicCRS = factory.getAuthorityCodes(GeographicCRS.class);
        assertTrue (geographicCRS instanceof AuthorityCodes);
        assertFalse(geographicCRS.isEmpty());
        assertTrue (geographicCRS.size() > 0);
        assertTrue (geographicCRS.size() < crs.size());
        assertFalse(geographicCRS.containsAll(crs));
        assertTrue (crs.containsAll(geographicCRS));

        final Set projectedCRS = factory.getAuthorityCodes(ProjectedCRS.class);
        assertTrue (projectedCRS instanceof AuthorityCodes);
        assertFalse(projectedCRS.isEmpty());
        assertTrue (projectedCRS.size() > 0);
        assertTrue (projectedCRS.size() < crs.size());
        assertFalse(projectedCRS.containsAll(crs));
        assertTrue (crs.containsAll(projectedCRS));
//        assertTrue(Collections.disjoint(geographicCRS, projectedCRS));
        // TODO: uncomment when we will be allowed to compile for J2SE 1.5.

        final Set datum = factory.getAuthorityCodes(Datum.class);
        assertTrue (datum instanceof AuthorityCodes);
        assertFalse(datum.isEmpty());
        assertTrue (datum.size() > 0);
//        assertTrue(Collections.disjoint(datum, crs));
        // TODO: uncomment when we will be allowed to compile for J2SE 1.5.

        final Set geodeticDatum = factory.getAuthorityCodes(GeodeticDatum.class);
        assertTrue (geodeticDatum instanceof AuthorityCodes);
        assertFalse(geodeticDatum.isEmpty());
        assertTrue (geodeticDatum.size() > 0);
        assertFalse(geodeticDatum.containsAll(datum));
        assertTrue (datum.containsAll(geodeticDatum));

        // Ensures that the factory keept the set in its cache.
        assertSame(crs,           factory.getAuthorityCodes(CoordinateReferenceSystem.class));
        assertSame(geographicCRS, factory.getAuthorityCodes(            GeographicCRS.class));
        assertSame(projectedCRS,  factory.getAuthorityCodes(             ProjectedCRS.class));
        assertSame(datum,         factory.getAuthorityCodes(                    Datum.class));
        assertSame(geodeticDatum, factory.getAuthorityCodes(            GeodeticDatum.class));
        assertSame(geodeticDatum, factory.getAuthorityCodes(     DefaultGeodeticDatum.class));

        // Try a dummy type. Intentional unsafe cast for tricking the parameterized type check.
        assertTrue("Dummy type", factory.getAuthorityCodes((Class) String.class).isEmpty());

        // Tests projections, which are handle in a special way.
        final Set operations      = factory.getAuthorityCodes(Operation     .class);
        final Set conversions     = factory.getAuthorityCodes(Conversion    .class);
        final Set projections     = factory.getAuthorityCodes(Projection    .class);
        final Set transformations = factory.getAuthorityCodes(Transformation.class);

        assertTrue (operations      instanceof AuthorityCodes);
        assertTrue (conversions     instanceof AuthorityCodes);
        assertTrue (projections     instanceof AuthorityCodes);
        assertTrue (transformations instanceof AuthorityCodes);

        assertTrue (conversions    .size() < operations .size());
        assertTrue (projections    .size() < operations .size());
        assertTrue (transformations.size() < operations .size());
        assertTrue (projections    .size() < conversions.size());

        assertFalse(projections.containsAll(conversions));
        assertTrue (conversions.containsAll(projections));
        assertTrue (operations .containsAll(conversions));
        assertTrue (operations .containsAll(transformations));

        assertTrue (Collections.disjoint(conversions, transformations));
        assertFalse(Collections.disjoint(conversions, projections));

        assertFalse(operations     .isEmpty());
        assertFalse(conversions    .isEmpty());
        assertFalse(projections    .isEmpty());
        assertFalse(transformations.isEmpty());

        assertTrue (conversions.contains("101"));
        assertFalse(projections.contains("101"));
        assertTrue (projections.contains("16001"));

        @SuppressWarnings("unchecked") // Intentional unsafe cast for testing purpose.
        final Set units = factory.getAuthorityCodes((Class) Unit.class);
        assertTrue (units instanceof AuthorityCodes);
        assertFalse(units.isEmpty());
        assertTrue (units.size() > 0);

        // Tests the fusion of all types
        final Set all = factory.getAuthorityCodes(IdentifiedObject.class);
        assertFalse(all instanceof AuthorityCodes); // Usually a HashSet.
        assertTrue (all.containsAll(crs));
        assertTrue (all.containsAll(datum));
        assertTrue (all.containsAll(operations));
        assertFalse(all.containsAll(units));  // They are not IdentifiedObjects.
    }

    /**
     * Tests {@link CRS#getEnvelope} and {@link CRS#getGeographicBoundingBox}.
     */
    public void testValidArea() throws FactoryException, TransformException {
        final CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("7400");
        final GeographicBoundingBox bbox = CRS.getGeographicBoundingBox(crs);
        assertNotNull("No bounding box. Maybe an older EPSG database is running? " +
                      "Try to clear the tmp/Geotools directory.", bbox);
        assertEquals(42.25, bbox.getSouthBoundLatitude(), EPS);
        assertEquals(51.10, bbox.getNorthBoundLatitude(), EPS);
        assertEquals(-5.20, bbox.getWestBoundLongitude(), EPS);
        assertEquals( 8.23, bbox.getEastBoundLongitude(), EPS);
        final Envelope envelope = CRS.getEnvelope(crs);
        assertEquals(46.948, envelope.getMinimum(0), 1E-3);
        assertEquals(56.781, envelope.getMaximum(0), 1E-3);
        assertEquals(-8.375, envelope.getMinimum(1), 1E-3);
        assertEquals( 6.548, envelope.getMaximum(1), 1E-3);
        assertNull(CRS.getEnvelope(null));
        final GeographicBoundingBox rep = new GeographicBoundingBoxImpl(envelope);
        assertEquals(42.25, rep.getSouthBoundLatitude(), 1E-3);
        assertEquals(51.10, rep.getNorthBoundLatitude(), 1E-3);
        assertEquals(-5.20, rep.getWestBoundLongitude(), 1E-3);
        assertEquals( 8.23, rep.getEastBoundLongitude(), 1E-3);
    }

    /**
     * Tests the serialization of many {@link CoordinateOperation} objects.
     */
    public void testSerialization() throws FactoryException, IOException, ClassNotFoundException {
        CoordinateReferenceSystem crs1 = factory.createCoordinateReferenceSystem("4326");
        CoordinateReferenceSystem crs2 = factory.createCoordinateReferenceSystem("4322");
        CoordinateOperationFactory opf = ReferencingFactoryFinder.getCoordinateOperationFactory(null);
        CoordinateOperation cop = opf.createOperation(crs1, crs2);
        serialize(cop);

        crs1 = crs2 = null;
        final String crs1_name  = "4326";
        final int crs2_ranges[] = {4326,  4326,
                       /* [ 2] */  4322,  4322,
                       /* [ 4] */  4269,  4269,
                       /* [ 6] */  4267,  4267,
                       /* [ 8] */  4230,  4230,
                       /* [10] */ 32601, 32660,
                       /* [12] */ 32701, 32760,
                       /* [14] */  2759,  2930};

        for (int irange=0; irange<crs2_ranges.length; irange+=2) {
            int range_start = crs2_ranges[irange  ];
            int range_end   = crs2_ranges[irange+1];
            for (int isystem2=range_start; isystem2<=range_end; isystem2++) {
                if (crs1 == null) {
                    crs1 = factory.createCoordinateReferenceSystem(crs1_name);
                }
                String crs2_name = Integer.toString(isystem2);
                crs2 = factory.createCoordinateReferenceSystem(crs2_name);
                cop = opf.createOperation(crs1, crs2);
                serialize(cop);
                if (!extensive) {
                    // If we are not running in extensive test mode,
                    // tests only the first CRS from each range.
                    break;
                }
            }
        }
    }

    /**
     * Tests the serialization of the specified object.
     */
    private static void serialize(final Object object) throws IOException, ClassNotFoundException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        final ObjectOutputStream out = new ObjectOutputStream(buffer);
        out.writeObject(object);
        out.close();
        final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        final Object read = in.readObject();
        in.close();
        assertEquals(object,            read);
        assertEquals(object.hashCode(), read.hashCode());
    }

    /**
     * Tests the creation of {@link Conversion} objects.
     */
    public void testConversions() throws FactoryException {
        /*
         * UTM zone 10N
         */
        final CoordinateOperation operation = factory.createCoordinateOperation("16010");
        assertEquals("16010", getIdentifier(operation));
        assertTrue(operation instanceof Conversion);
        assertNull(operation.getSourceCRS());
        assertNull(operation.getTargetCRS());
        assertNull(operation.getMathTransform());
        /*
         * WGS 72 / UTM zone 10N
         */
        final ProjectedCRS crs = factory.createProjectedCRS("32210");
        final CoordinateOperation projection = crs.getConversionFromBase();
        assertEquals("32210", getIdentifier(crs));
        assertEquals("16010", getIdentifier(projection));
        assertTrue   (projection instanceof Projection);
        assertNotNull(projection.getSourceCRS());
        assertNotNull(projection.getTargetCRS());
        assertNotNull(projection.getMathTransform());
        assertNotSame(projection, operation);
        assertEquals(((Conversion) operation).getMethod(), ((Conversion) projection).getMethod());
        
        // FIXME We have lost track of our interning pool
        // assertSame(((Conversion) operation).getMethod(), ((Conversion) projection).getMethod());                
        /*
         * WGS 72BE / UTM zone 10N
         */
        assertFalse(CRS.equalsIgnoreMetadata(crs, factory.createProjectedCRS("32410")));
        /*
         * Creates a projected CRS from base and projected CRS codes.
         */
        final Set all = factory.createFromCoordinateReferenceSystemCodes("4322", "32210");
        assertEquals(1, all.size());
        assertTrue(all.contains(projection));
    }

    /**
     * Tests the creation of {@link Transformation} objects.
     */
    public void testTransformations() throws FactoryException {
        /*
         * Longitude rotation
         */
        assertTrue(factory.createCoordinateOperation("1764") instanceof Transformation);
        /*
         * ED50 (4230)  -->  WGS 84 (4326)  using
         * Geocentric translations (9603).
         * Accuracy = 999
         */
        final CoordinateOperation      operation1 = factory.createCoordinateOperation("1087");
        final CoordinateReferenceSystem sourceCRS = operation1.getSourceCRS();
        final CoordinateReferenceSystem targetCRS = operation1.getTargetCRS();
        final MathTransform             transform = operation1.getMathTransform();
        assertEquals("1087", getIdentifier(operation1));
        assertEquals("4230", getIdentifier(sourceCRS));
        assertEquals("4326", getIdentifier(targetCRS));
        assertTrue   (operation1 instanceof Transformation);
        assertNotSame(sourceCRS, targetCRS);
        assertFalse  (operation1.getMathTransform().isIdentity());
        assertEquals (999, AbstractCoordinateOperation.getAccuracy(operation1), 1E-6);
        /*
         * ED50 (4230)  -->  WGS 84 (4326)  using
         * Position Vector 7-param. transformation (9606).
         * Accuracy = 1.5
         */
        final CoordinateOperation operation2 = factory.createCoordinateOperation("1631");
        assertEquals("1631", getIdentifier(operation2));
        assertTrue (operation2 instanceof Transformation);
        assertSame (sourceCRS, operation2.getSourceCRS());
        assertSame (targetCRS, operation2.getTargetCRS());
        assertFalse(operation2.getMathTransform().isIdentity());
        assertFalse(transform.equals(operation2.getMathTransform()));
        assertEquals(1.5, AbstractCoordinateOperation.getAccuracy(operation2), 1E-6);
        /*
         * ED50 (4230)  -->  WGS 84 (4326)  using
         * Coordinate Frame rotation (9607).
         * Accuracy = 1.0
         */
        final CoordinateOperation operation3 = factory.createCoordinateOperation("1989");
        assertEquals("1989", getIdentifier(operation3));
        assertTrue (operation3 instanceof Transformation);
        assertSame (sourceCRS, operation3.getSourceCRS());
        assertSame (targetCRS, operation3.getTargetCRS());
        assertFalse(operation3.getMathTransform().isIdentity());
        assertFalse(transform.equals(operation3.getMathTransform()));
        assertEquals(1.0, AbstractCoordinateOperation.getAccuracy(operation3), 1E-6);
        if (false) {
            System.out.println(operation3);
            System.out.println(operation3.getSourceCRS());
            System.out.println(operation3.getTargetCRS());
            System.out.println(operation3.getMathTransform());
        }
        /*
         * Tests "BD72 to WGS 84 (1)" (EPSG:1609) creation. This one has an unusual unit for the
         * "Scale difference" parameter (EPSG:8611). The value is 0.999999 and the unit is "unity"
         * (EPSG:9201) instead of the usual "parts per million" (EPSG:9202). It was used to thrown
         * an exception in older EPSG factory implementations.
         */
        assertEquals(1.0, AbstractCoordinateOperation.getAccuracy(factory.createCoordinateOperation("1609")), 1E-6);
        /*
         * Creates from CRS codes. There is 40 such operations in EPSG version 6.7.
         * The preferred one (according the "supersession" table) is EPSG:1612.
         */
        final Set all = factory.createFromCoordinateReferenceSystemCodes("4230", "4326");
        assertTrue(all.size() >= 3);
        assertTrue(all.contains(operation1));
        assertTrue(all.contains(operation2));
        assertTrue(all.contains(operation3));
        int count=0;
        for (final Iterator it=all.iterator(); it.hasNext();) {
            final CoordinateOperation check = (CoordinateOperation) it.next();
            assertSame(sourceCRS, check.getSourceCRS());
            assertSame(targetCRS, check.getTargetCRS());
            if (count++ == 0) {
                assertEquals("1612", getIdentifier(check)); // see comment above.
            }
        }
        assertEquals(all.size(), count);
    }

    /**
     * Fetchs the accuracy declared in all coordinate operations found in the database.
     */
    public void testAccuracy() throws FactoryException {
        final Set identifiers = factory.getAuthorityCodes(CoordinateOperation.class);
        double min     = Double.POSITIVE_INFINITY;
        double max     = Double.NEGATIVE_INFINITY;
        double sum     = 0;
        int    count   = 0; // Number of coordinate operations (minus the skipped ones).
        int    created = 0; // Number of coordinate operations recognized by the factory.
        int    valid   = 0; // Number of non-NaN accuracies.
        for (final Iterator it=identifiers.iterator(); it.hasNext();) {
            final CoordinateOperation operation;
            final String code = (String) it.next();
            final int n = Integer.parseInt(code);
            if (n>=10087 && n<=10088) {
                // Valid, but log a warning. Will avoid just in order to keep the output clean.
                continue;
            }
            ++count;
            if (!extensive && (count % 20) != 0) {
                // If the tests are not executed in "extensive" mode, tests only 5% of cases.
                continue;
            }
            try {
                operation = factory.createCoordinateOperation(code);
            } catch (FactoryException exception) {
                // Skip unsupported coordinate operations, except if the cause is a SQL exception.
                if (exception.getCause() instanceof SQLException) {
                    throw exception;
                }
                continue;
            }
            created++;
            assertNotNull(operation);
            final double accuracy = AbstractCoordinateOperation.getAccuracy(operation);
            assertFalse(accuracy < 0);
            if (!Double.isNaN(accuracy)) {
                if (accuracy < min) min=accuracy;
                if (accuracy > max) max=accuracy;
                sum += accuracy;
                valid++;
            }
        }
        if (verbose) {
            System.out.print("Number of coordinate operations:    "); System.out.println(identifiers.size());
            System.out.print("Number of tested operations:        "); System.out.println(count);
            System.out.print("Number of recognized operations:    "); System.out.println(created);
            System.out.print("Number of operations with accuracy: "); System.out.println(valid);
            System.out.print("Minimal accuracy value (meters):    "); System.out.println(min);
            System.out.print("Maximal accuracy value (meters):    "); System.out.println(max);
            System.out.print("Average accuracy value (meters):    "); System.out.println(sum / valid);
        }
    }

    /**
     * Compares a WKT found in the field with the EPSG equivalent.
     *
     * @see http://jira.codehaus.org/browse/GEOT-1268
     */
    public void testEquivalent() throws FactoryException {
        final String wkt =
            "PROJCS[\"NAD_1983_StatePlane_Massachusetts_Mainland_FIPS_2001\", " +
              "GEOGCS[\"GCS_North_American_1983\", " +
                "DATUM[\"D_North_American_1983\", " +
                  "SPHEROID[\"GRS_1980\", 6378137.0, 298.257222101]], " +
                "PRIMEM[\"Greenwich\", 0.0], " +
                "UNIT[\"degree\", 0.017453292519943295], " +
                "AXIS[\"Longitude\", EAST], " +
                "AXIS[\"Latitude\", NORTH]], " +
              "PROJECTION[\"Lambert_Conformal_Conic\"], " +
              "PARAMETER[\"central_meridian\", -71.5], " +
              "PARAMETER[\"latitude_of_origin\", 41.0], " +
              "PARAMETER[\"standard_parallel_1\", 41.71666666666667], " +
              "PARAMETER[\"scale_factor\", 1.0], " +
              "PARAMETER[\"false_easting\", 200000.0], " +
              "PARAMETER[\"false_northing\", 750000.0], " +
              "PARAMETER[\"standard_parallel_2\", 42.68333333333334], " +
              "UNIT[\"m\", 1.0], " +
              "AXIS[\"x\", EAST], " +
              "AXIS[\"y\", NORTH]]";

        final CoordinateReferenceSystem crs1 = CRS.parseWKT(wkt);
        final CoordinateReferenceSystem crs2 = CRS.decode("EPSG:26986");

        // This is the current state of Geotools, but it is wrong. The CRS should be equivalent.
        // We will change to 'assertTrue' if a MapProjection.equivalent(MapProjection) method is
        // implemented in some future Geotools version.
        assertFalse(CRS.equalsIgnoreMetadata(crs1, crs2));
    }

    /**
     * Tests {@link ThreadedEpsgFactory#find} method.
     */
    public void testFind() throws FactoryException {
        final IdentifiedObjectFinder finder = factory.getIdentifiedObjectFinder(
                CoordinateReferenceSystem.class);
        assertTrue("Full scan should be enabled by default.", finder.isFullScanAllowed());
        assertNull("Should not find WGS84 because the axis order is not the same.",
                   finder.find(DefaultGeographicCRS.WGS84));

        String wkt;
        wkt = "GEOGCS[\"WGS 84\",\n"                                    +
              "  DATUM[\"World Geodetic System 1984\",\n"               +
              "    SPHEROID[\"WGS 84\", 6378137.0, 298.257223563]],\n"  +
              "  PRIMEM[\"Greenwich\", 0.0],\n"                         +
              "  UNIT[\"degree\", 0.017453292519943295],\n"             +
              "  AXIS[\"Geodetic latitude\", NORTH],\n"                 +
              "  AXIS[\"Geodetic longitude\", EAST]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        finder.setFullScanAllowed(false);
        assertNull("Should not find without a full scan, because the WKT contains no identifier " +
                   "and the CRS name is ambiguous (more than one EPSG object have this name).",
                   finder.find(crs));

        finder.setFullScanAllowed(true);
        IdentifiedObject find = finder.find(crs);
        assertNotNull("With full scan allowed, the CRS should be found.", find);
        assertTrue("Should found an object equals (ignoring metadata) to the requested one.",
                   CRS.equalsIgnoreMetadata(crs, find));
        assertEquals("4326",
                AbstractIdentifiedObject.getIdentifier(find, factory.getAuthority()).getCode());
        finder.setFullScanAllowed(false);
        assertEquals("The CRS should still in the cache.",
                     "EPSG:4326", finder.findIdentifier(crs));
        /*
         * The PROJCS below intentionnaly uses a name different from the one found in the
         * EPSG database, in order to force a full scan (otherwise the EPSG database would
         * find it by name, but we want to test the scan).
         */
        wkt = "PROJCS[\"Beijing 1954\",\n"                                 +
              "   GEOGCS[\"Beijing 1954\",\n"                              +
              "     DATUM[\"Beijing 1954\",\n"                             +
              "       SPHEROID[\"Krassowsky 1940\", 6378245.0, 298.3]],\n" +
              "     PRIMEM[\"Greenwich\", 0.0],\n"                         +
              "     UNIT[\"degree\", 0.017453292519943295],\n"             +
              "     AXIS[\"Geodetic latitude\", NORTH],\n"                 +
              "     AXIS[\"Geodetic longitude\", EAST]],\n"                +
              "   PROJECTION[\"Transverse Mercator\"],\n"                  +
              "   PARAMETER[\"central_meridian\", 135.0],\n"               +
              "   PARAMETER[\"latitude_of_origin\", 0.0],\n"               +
              "   PARAMETER[\"scale_factor\", 1.0],\n"                     +
              "   PARAMETER[\"false_easting\", 500000.0],\n"               +
              "   PARAMETER[\"false_northing\", 0.0],\n"                   +
              "   UNIT[\"m\", 1.0],\n"                                     +
              "   AXIS[\"Northing\", NORTH],\n"                            +
              "   AXIS[\"Easting\", EAST]]";
        crs = CRS.parseWKT(wkt);
        finder.setFullScanAllowed(false);
        assertNull("Should not find the CRS without a full scan.", finder.find(crs));
        finder.setFullScanAllowed(true);
        find = finder.find(crs);
        assertNotNull("With full scan allowed, the CRS should be found.", find);
        assertTrue("Should found an object equals (ignoring metadata) to the requested one.",
                   CRS.equalsIgnoreMetadata(crs, find));
        assertEquals("2442", AbstractIdentifiedObject.getIdentifier(find, factory.getAuthority()).getCode());
        finder.setFullScanAllowed(false);
        assertEquals("The CRS should still in the cache.",
                     "EPSG:2442", finder.findIdentifier(crs));
    }
}
