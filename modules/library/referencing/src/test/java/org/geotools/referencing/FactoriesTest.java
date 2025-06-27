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
package org.geotools.referencing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.measure.Unit;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CRSFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.api.referencing.cs.AxisDirection;
import org.geotools.api.referencing.cs.CSFactory;
import org.geotools.api.referencing.cs.CartesianCS;
import org.geotools.api.referencing.cs.EllipsoidalCS;
import org.geotools.api.referencing.datum.DatumFactory;
import org.geotools.api.referencing.datum.Ellipsoid;
import org.geotools.api.referencing.datum.GeodeticDatum;
import org.geotools.api.referencing.datum.PrimeMeridian;
import org.geotools.api.referencing.operation.Conversion;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransformFactory;
import org.geotools.api.referencing.operation.OperationMethod;
import org.geotools.api.referencing.operation.Projection;
import org.geotools.api.util.GenericName;
import org.geotools.api.util.ScopedName;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.geotools.referencing.factory.DatumAliases;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.geotools.referencing.operation.DefiningConversion;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import si.uom.NonSI;
import si.uom.SI;

/**
 * Tests the creation of {@link CoordinateReferenceSystem} objects and dependencies through factories (not authority
 * factories).
 *
 * @version $Id$
 */
public final class FactoriesTest {
    /** The output stream. Can be redirected to the standard output stream for debugging. */
    private static PrintStream out = System.out;

    /**
     * Convenience method creating a map with only the "name" property. This is the only mandatory property for object
     * creation.
     */
    private static Map<String, ?> name(final String name) {
        return Collections.singletonMap("name", name);
    }

    /**
     * Tests the creation of new coordinate reference systems.
     *
     * @throws FactoryException if a coordinate reference system can't be created.
     */
    @Test
    public void testCreation() throws FactoryException {
        out.println();
        out.println("Testing CRS creations");
        out.println("---------------------");
        out.println();
        out.println("create Coodinate Reference System....1: ");
        final DatumFactory datumFactory = ReferencingFactoryFinder.getDatumFactory(null);
        final CSFactory csFactory = ReferencingFactoryFinder.getCSFactory(null);
        final CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
        final MathTransformFactory mtFactory = ReferencingFactoryFinder.getMathTransformFactory(null);

        final Unit<Length> meters = SI.METRE;
        final Ellipsoid airy1830 = datumFactory.createEllipsoid(name("Airy1830"), 6377563.396, 6356256.910, meters);
        out.println();
        out.println("create Coodinate Reference System....2: ");
        out.println(airy1830.toWKT());

        final Unit<Angle> degrees = NonSI.DEGREE_ANGLE;
        final PrimeMeridian greenwich = datumFactory.createPrimeMeridian(name("Greenwich"), 0, degrees);
        out.println();
        out.println("create Coodinate Reference System....3: ");
        out.println(greenwich.toWKT());

        // NOTE: we could use the following pre-defined constant instead:
        //       DefaultPrimeMeridian.GREENWICH;
        final GeodeticDatum datum = datumFactory.createGeodeticDatum(name("Airy1830"), airy1830, greenwich);
        out.println();
        out.println("create Coodinate Reference System....4: ");
        out.println(datum.toWKT());

        // NOTE: we could use the following pre-defined constant instead:
        //       DefaultEllipsoidalCS.GEODETIC_2D;
        final EllipsoidalCS ellCS = csFactory.createEllipsoidalCS(
                name("Ellipsoidal"),
                csFactory.createCoordinateSystemAxis(name("Longitude"), "long", AxisDirection.EAST, degrees),
                csFactory.createCoordinateSystemAxis(name("Latitude"), "lat", AxisDirection.NORTH, degrees));
        out.println();
        out.println("create Coodinate Reference System....5: ");
        out.println(ellCS); // No WKT for coordinate systems

        final GeographicCRS geogCRS = crsFactory.createGeographicCRS(name("Airy1830"), datum, ellCS);
        out.println();
        out.println("create Coodinate Reference System....6: ");
        out.println(geogCRS.toWKT());

        final ParameterValueGroup param = mtFactory.getDefaultParameters("Transverse_Mercator");
        param.parameter("semi_major").setValue(airy1830.getSemiMajorAxis());
        param.parameter("semi_minor").setValue(airy1830.getSemiMinorAxis());
        param.parameter("central_meridian").setValue(49);
        param.parameter("latitude_of_origin").setValue(-2);
        param.parameter("false_easting").setValue(400000);
        param.parameter("false_northing").setValue(-100000);
        out.println();
        out.println("create Coodinate System....7: ");
        out.println(param);

        // NOTE: we could use the following pre-defined constant instead:
        //       DefaultCartesianCS.PROJECTED;
        final CartesianCS cartCS = csFactory.createCartesianCS(
                name("Cartesian"),
                csFactory.createCoordinateSystemAxis(name("Easting"), "x", AxisDirection.EAST, meters),
                csFactory.createCoordinateSystemAxis(name("Northing"), "y", AxisDirection.NORTH, meters));
        out.println();
        out.println("create Coodinate Reference System....8: ");
        out.println(cartCS); // No WKT for coordinate systems

        final Hints hints = new Hints();
        hints.put(Hints.DATUM_FACTORY, datumFactory);
        hints.put(Hints.CS_FACTORY, csFactory);
        hints.put(Hints.CRS_FACTORY, crsFactory);
        hints.put(Hints.MATH_TRANSFORM_FACTORY, mtFactory);

        final ReferencingFactoryContainer container = new ReferencingFactoryContainer(hints);
        assertSame(datumFactory, container.getDatumFactory());
        assertSame(csFactory, container.getCSFactory());
        assertSame(crsFactory, container.getCRSFactory());
        assertSame(mtFactory, container.getMathTransformFactory());

        final Conversion conversion = new DefiningConversion("GBN grid", param);
        final ProjectedCRS projCRS =
                crsFactory.createProjectedCRS(name("Great_Britian_National_Grid"), geogCRS, conversion, cartCS);
        out.println();
        out.println("create Coodinate System....9: ");
        out.println(projCRS.toWKT());
    }

    /**
     * Tests all map projection creation.
     *
     * @throws FactoryException If a CRS can not be created.
     */
    @Test
    public void testMapProjections() throws FactoryException {
        out.println();
        out.println("Testing classification names");
        out.println("----------------------------");
        final CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
        final MathTransformFactory mtFactory = ReferencingFactoryFinder.getMathTransformFactory(null);
        final Collection<OperationMethod> methods = mtFactory.getAvailableMethods(Projection.class);
        final Map<String, ?> dummyName = Collections.singletonMap("name", "Test");
        for (final OperationMethod method : methods) {
            final String classification = method.getName().getCode();
            final ParameterValueGroup param = mtFactory.getDefaultParameters(classification);
            try {
                param.parameter("semi_major").setValue(6377563.396);
                param.parameter("semi_minor").setValue(6356256.909237285);
            } catch (IllegalArgumentException e) {
                // Above parameters do not exists. Ignore.
            }
            final MathTransform mt;
            try {
                mt = mtFactory.createParameterizedTransform(param);
            } catch (FactoryException | UnsupportedOperationException e) {
                // Probably not a map projection. This test is mostly about projection, so ignore.
                continue;
            }
            if (mt instanceof MapProjection) {
                /*
                 * Tests map projection properties. Some tests are ommitted for south-oriented
                 * map projections, since they are implemented as a concatenation of their North-
                 * oriented variants with an affine transform.
                 */
                out.println(classification);
                final boolean skip = classification.equalsIgnoreCase("Transverse Mercator (South Orientated)")
                        || classification.equalsIgnoreCase("Equidistant_Cylindrical")
                        || classification.equalsIgnoreCase("Behrmann")
                        || classification.equalsIgnoreCase("Lambert Cylindrical Equal Area (Spherical)");
                if (!skip) {
                    assertEquals(
                            classification,
                            ((MapProjection) mt)
                                    .getParameterDescriptors()
                                    .getName()
                                    .getCode());
                }
                final ProjectedCRS projCRS = crsFactory.createProjectedCRS(
                        dummyName,
                        DefaultGeographicCRS.WGS84,
                        new DefiningConversion(dummyName, method, mt),
                        DefaultCartesianCS.PROJECTED);
                final Conversion conversion = projCRS.getConversionFromBase();
                assertSame(mt, conversion.getMathTransform());
                final OperationMethod projMethod = conversion.getMethod();
                assertEquals(classification, projMethod.getName().getCode());
            }
        }
    }

    /**
     * Tests datum aliases. Note: ellipsoid and prime meridian are dummy values just (not conform to the usage in real
     * world) just for testing purpose.
     *
     * @throws FactoryException If a CRS can not be created.
     */
    @Test
    @SuppressWarnings(
            "UndefinedEquals") // Subtypes of Collection are not guaranteed to implement a useful #equals method.
    public void testDatumAliases() throws FactoryException {
        final String name0 = "Nouvelle Triangulation Francaise (Paris)";
        final String name1 = "Nouvelle_Triangulation_Francaise_Paris";
        final String name2 = "D_NTF";
        final String name3 = "NTF (Paris meridian)";
        final Ellipsoid ellipsoid = DefaultEllipsoid.WGS84;
        final PrimeMeridian meridian = DefaultPrimeMeridian.GREENWICH;
        DatumFactory factory = new ReferencingObjectFactory();
        final Map<String, ?> properties = Collections.singletonMap("name", name1);
        GeodeticDatum datum = factory.createGeodeticDatum(properties, ellipsoid, meridian);
        assertTrue(datum.getAlias().isEmpty());

        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    factory = new DatumAliases(factory);
                    break;
                case 1:
                    factory = ReferencingFactoryFinder.getDatumFactory(null);
                    break;
                case 2:
                    ((DatumAliases) factory).freeUnused();
                    break;
                default:
                    throw new AssertionError(); // Should not occurs.
            }
            final String pass = "Pass #" + i;
            datum = factory.createGeodeticDatum(properties, ellipsoid, meridian);
            final GenericName[] aliases = datum.getAlias().toArray(new GenericName[0]);
            assertEquals(pass, 4, aliases.length);
            assertEquals(pass, name0, aliases[0].tip().toString());
            assertEquals(pass, name1, aliases[1].tip().toString());
            assertEquals(pass, name2, aliases[2].tip().toString());
            assertEquals(pass, name3, aliases[3].tip().toString());
            assertTrue(pass, aliases[0] instanceof ScopedName);
            assertTrue(pass, aliases[1] instanceof ScopedName);
            assertTrue(pass, aliases[2] instanceof ScopedName);
            assertTrue(pass, aliases[3] instanceof ScopedName);
        }

        datum = factory.createGeodeticDatum(Collections.singletonMap("name", "Tokyo"), ellipsoid, meridian);
        Collection<GenericName> aliases = datum.getAlias();
        assertEquals(4, aliases.size());

        ((DatumAliases) factory).freeUnused();
        datum = factory.createGeodeticDatum(Collections.singletonMap("name", "_toKyo  _"), ellipsoid, meridian);
        assertEquals(4, datum.getAlias().size());
        assertEquals(aliases, datum.getAlias());

        datum = factory.createGeodeticDatum(Collections.singletonMap("name", "D_Tokyo"), ellipsoid, meridian);
        assertEquals(4, datum.getAlias().size());

        datum = factory.createGeodeticDatum(Collections.singletonMap("name", "Luxembourg 1930"), ellipsoid, meridian);
        assertEquals(3, datum.getAlias().size());

        datum = factory.createGeodeticDatum(Collections.singletonMap("name", "Dummy"), ellipsoid, meridian);
        assertTrue("Non existing datum should have no alias.", datum.getAlias().isEmpty());

        datum = factory.createGeodeticDatum(Collections.singletonMap("name", "WGS 84"), ellipsoid, meridian);
        assertTrue(AbstractIdentifiedObject.nameMatches(datum, "WGS 84"));
        assertTrue(AbstractIdentifiedObject.nameMatches(datum, "WGS_1984"));
        assertTrue(AbstractIdentifiedObject.nameMatches(datum, "World Geodetic System 1984"));
        assertFalse(AbstractIdentifiedObject.nameMatches(datum, "WGS 72"));
    }
}
