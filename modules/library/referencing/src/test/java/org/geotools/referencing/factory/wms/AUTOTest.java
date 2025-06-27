/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.wms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.operation.projection.AzimuthalEquidistant;
import org.geotools.referencing.operation.projection.EquatorialOrthographic;
import org.geotools.referencing.operation.projection.EquidistantCylindrical;
import org.geotools.referencing.operation.projection.GeostationarySatellite;
import org.geotools.referencing.operation.projection.Gnomonic;
import org.geotools.referencing.operation.projection.ObliqueOrthographic;
import org.geotools.referencing.operation.projection.PolarOrthographic;
import org.geotools.referencing.operation.projection.Stereographic;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AutoCRSFactory}.
 *
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 */
public final class AUTOTest {
    /** The factory to test. */
    private CRSAuthorityFactory factory;

    /** Initializes the factory to test. */
    @Before
    public void setUp() {
        factory = new AutoCRSFactory();
    }

    /** Tests the registration in {@link ReferencingFactoryFinder}. */
    @Test
    public void testFactoryFinder() {
        final Collection<String> authorities = ReferencingFactoryFinder.getAuthorityNames();
        assertTrue(authorities.contains("AUTO"));
        assertTrue(authorities.contains("AUTO2"));
        factory = ReferencingFactoryFinder.getCRSAuthorityFactory("AUTO", null);
        assertTrue(factory instanceof AutoCRSFactory);
        assertSame(factory, ReferencingFactoryFinder.getCRSAuthorityFactory("AUTO2", null));
    }

    /** Checks the authority names. */
    @Test
    public void testAuthority() {
        final Citation authority = factory.getAuthority();
        assertTrue(Citations.identifierMatches(authority, "AUTO"));
        assertTrue(Citations.identifierMatches(authority, "AUTO2"));
        assertFalse(Citations.identifierMatches(authority, "EPSG"));
        assertFalse(Citations.identifierMatches(authority, "CRS"));
    }

    /** UDIG requires this to work. */
    @Test
    public void test42001() throws FactoryException {
        final ProjectedCRS utm = factory.createProjectedCRS("AUTO:42001,0.0,0.0");
        assertNotNull("auto-utm", utm);
        assertSame(utm, factory.createObject("AUTO :42001 ,0,0"));
        assertSame(utm, factory.createObject("AUTO2:42001 ,0,0"));
        assertSame(utm, factory.createObject("42001 ,0,0"));
        assertNotSame(utm, factory.createObject("AUTO :42001 ,30,0"));
        assertEquals(
                "Transverse_Mercator",
                utm.getConversionFromBase().getMethod().getName().getCode());
    }

    /** Check we can parse also the unit */
    @Test
    public void test42001Units() throws FactoryException {
        final ProjectedCRS utm = factory.createProjectedCRS("AUTO:42001,9001,0.0,0.0");
        assertNotNull("auto-utm", utm);
        assertSame(utm, factory.createObject("AUTO :42001, 9001,0,0"));
        assertSame(utm, factory.createObject("AUTO2:42001, 9001,0,0"));
        assertSame(utm, factory.createObject("42001, 9001,0,0"));
        assertNotSame(utm, factory.createObject("AUTO :42001, 9001,30,0"));
        assertEquals(
                "Transverse_Mercator",
                utm.getConversionFromBase().getMethod().getName().getCode());
    }

    @Test
    public void test42003() throws FactoryException {
        ProjectedCRS eqc = factory.createProjectedCRS("AUTO:42003,9001,0.0,0");
        assertEquals(
                "Orthographic",
                eqc.getConversionFromBase().getMethod().getName().getCode());
        assertTrue(eqc.getConversionFromBase().getMathTransform() instanceof EquatorialOrthographic);

        eqc = factory.createProjectedCRS("AUTO:42003,9001,0.0,90");
        assertEquals(
                "Orthographic",
                eqc.getConversionFromBase().getMethod().getName().getCode());
        assertTrue(eqc.getConversionFromBase().getMathTransform() instanceof PolarOrthographic);

        eqc = factory.createProjectedCRS("AUTO:42003,9001,0.0,45");
        assertEquals(
                "Orthographic",
                eqc.getConversionFromBase().getMethod().getName().getCode());
        assertTrue(eqc.getConversionFromBase().getMathTransform() instanceof ObliqueOrthographic);
    }

    @Test
    public void test42004() throws FactoryException {
        final ProjectedCRS eqc = factory.createProjectedCRS("AUTO:42004,9001,0.0,35");
        assertEquals(
                "Equidistant_Cylindrical",
                eqc.getConversionFromBase().getMethod().getName().getCode());
        String stdParallel1Code =
                EquidistantCylindrical.Provider.STANDARD_PARALLEL_1.getName().getCode();
        double stdParallel1 = eqc.getConversionFromBase()
                .getParameterValues()
                .parameter(stdParallel1Code)
                .doubleValue();
        assertEquals(35.0, stdParallel1, 1e-9);
    }

    @Test
    public void test97001() throws FactoryException {
        ProjectedCRS crs = factory.createProjectedCRS("AUTO:97001,9001,-17.0,23.0");
        assertEquals(
                "Gnomonic", crs.getConversionFromBase().getMethod().getName().getCode());
        assertTrue(crs.getConversionFromBase().getMathTransform() instanceof Gnomonic);

        String centreLatCode = Gnomonic.Provider.LATITUDE_OF_CENTRE.getName().getCode();
        double centreLat = crs.getConversionFromBase()
                .getParameterValues()
                .parameter(centreLatCode)
                .doubleValue();
        assertEquals(23.0, centreLat, 1e-9);

        String centreLongCode = Gnomonic.Provider.LONGITUDE_OF_CENTRE.getName().getCode();
        double centreLong = crs.getConversionFromBase()
                .getParameterValues()
                .parameter(centreLongCode)
                .doubleValue();
        assertEquals(-17.0, centreLong, 1e-9);
    }

    @Test
    public void test97002() throws FactoryException {
        ProjectedCRS crs = factory.createProjectedCRS("AUTO:97002,9001,-17.0,23.0");
        assertEquals(
                "Stereographic",
                crs.getConversionFromBase().getMethod().getName().getCode());
        assertTrue(crs.getConversionFromBase().getMathTransform() instanceof Stereographic);

        String centreLatCode =
                Stereographic.Provider.LATITUDE_OF_ORIGIN.getName().getCode();
        double centreLat = crs.getConversionFromBase()
                .getParameterValues()
                .parameter(centreLatCode)
                .doubleValue();
        assertEquals(23.0, centreLat, 1e-9);

        String centreLongCode =
                Stereographic.Provider.CENTRAL_MERIDIAN.getName().getCode();
        double centreLong = crs.getConversionFromBase()
                .getParameterValues()
                .parameter(centreLongCode)
                .doubleValue();
        assertEquals(-17.0, centreLong, 1e-9);

        String semiMajorString = Stereographic.Provider.SEMI_MAJOR.getName().getCode();
        double semiMajor = crs.getConversionFromBase()
                .getParameterValues()
                .parameter(semiMajorString)
                .doubleValue();
        String semiMinorString = Stereographic.Provider.SEMI_MINOR.getName().getCode();
        double semiMinor = crs.getConversionFromBase()
                .getParameterValues()
                .parameter(semiMinorString)
                .doubleValue();
        assertEquals(semiMajor, semiMinor, 1e-9);
    }

    @Test
    public void test97003() throws Exception {
        ProjectedCRS crs = factory.createProjectedCRS("AUTO:97003,9001,-71.43,42.56");
        assertEquals(
                "Azimuthal_Equidistant",
                crs.getConversionFromBase().getMethod().getName().getCode());
        assertTrue(crs.getConversionFromBase().getMathTransform() instanceof AzimuthalEquidistant.Ellipsoidal);

        String centreLongCode =
                Stereographic.Provider.CENTRAL_MERIDIAN.getName().getCode();
        double centreLong = crs.getConversionFromBase()
                .getParameterValues()
                .parameter(centreLongCode)
                .doubleValue();
        assertEquals(-71.43, centreLong, 1e-9);

        String centreLatCode =
                Stereographic.Provider.LATITUDE_OF_ORIGIN.getName().getCode();
        double centreLat = crs.getConversionFromBase()
                .getParameterValues()
                .parameter(centreLatCode)
                .doubleValue();
        assertEquals(42.56, centreLat, 1e-9);
    }

    @Test
    // This fuzzy equality check is using a tolerance less than the gap to the next number (which is ~7.5e-09). You may
    // want a less restrictive tolerance, or to assert equality
    @SuppressWarnings("FloatingPointAssertionWithinEpsilon")
    public void test97004() throws Exception {
        ProjectedCRS crs = factory.createProjectedCRS("AUTO:97004,9001,20,0");
        assertEquals("GEOS", crs.getConversionFromBase().getMethod().getName().getCode());
        assertTrue(crs.getConversionFromBase().getMathTransform() instanceof GeostationarySatellite.Ellipsoidal);

        String centreLongCode =
                GeostationarySatellite.Provider.CENTRAL_MERIDIAN.getName().getCode();
        double centreLong = crs.getConversionFromBase()
                .getParameterValues()
                .parameter(centreLongCode)
                .doubleValue();
        assertEquals(20, centreLong, 1e-9);

        String heightCode =
                GeostationarySatellite.Provider.SATELLITE_HEIGHT.getName().getCode();
        double height = crs.getConversionFromBase()
                .getParameterValues()
                .parameter(heightCode)
                .doubleValue();
        assertEquals(Auto97004.SATELLITE_HEIGHT, height, 1e-9);
    }
}
