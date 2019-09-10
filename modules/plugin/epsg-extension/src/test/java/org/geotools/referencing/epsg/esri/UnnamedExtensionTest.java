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
package org.geotools.referencing.epsg.esri;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Set;
import org.geotools.geometry.jts.JTS;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.factory.OrderedAxisAuthorityFactory;
import org.geotools.util.factory.Hints;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.MathTransform;

/**
 * Tests {@link UnnamedExtension}.
 *
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Jody Garnett
 */
public class UnnamedExtensionTest {
    /** The factory to test. */
    private UnnamedExtension factory;

    /** Gets the authority factory for ESRI. */
    @Before
    public void setUp() throws Exception {

        factory =
                (UnnamedExtension)
                        ReferencingFactoryFinder.getCRSAuthorityFactory(
                                "EPSG",
                                new Hints(Hints.CRS_AUTHORITY_FACTORY, UnnamedExtension.class));
    }

    /** Tests the authority code. */
    @Test
    public void testAuthority() {
        final Citation authority = factory.getAuthority();
        assertNotNull(authority);
        assertEquals("European Petroleum Survey Group", authority.getTitle().toString());
        assertTrue(Citations.identifierMatches(authority, "EPSG"));
        assertFalse(Citations.identifierMatches(authority, "ESRI"));
        assertTrue(factory instanceof UnnamedExtension);
    }

    /** Tests the vendor. */
    @Test
    public void testVendor() {
        final Citation vendor = factory.getVendor();
        assertNotNull(vendor);
        assertEquals("Geotools", vendor.getTitle().toString());
    }

    /** Checks for duplication with EPSG-HSQL. */
    @Test
    public void testDuplication() throws FactoryException {
        final StringWriter buffer = new StringWriter();
        final PrintWriter writer = new PrintWriter(buffer);
        final Set duplicated = factory.reportDuplicatedCodes(writer);
        assertTrue(buffer.toString(), duplicated.isEmpty());
    }

    /** Checks for CRS instantiations. */
    @Test
    public void testInstantiation() throws FactoryException {
        final StringWriter buffer = new StringWriter();
        final PrintWriter writer = new PrintWriter(buffer);
        final Set duplicated = factory.reportInstantiationFailures(writer);
        assertTrue(buffer.toString(), duplicated.isEmpty());
    }

    /** Tests the {@code 41001} code. */
    @Test
    public void test41001() throws FactoryException {
        CoordinateReferenceSystem actual, expected;
        expected = factory.createCoordinateReferenceSystem("41001");
        actual = CRS.decode("EPSG:41001");
        assertSame(expected, actual);
        assertTrue(actual instanceof ProjectedCRS);
        Collection<ReferenceIdentifier> ids = actual.getIdentifiers();
        assertTrue(ids.contains(new NamedIdentifier(Citations.EPSG, "41001")));
        assertFalse(ids.contains(new NamedIdentifier(Citations.ESRI, "41001")));
    }

    /** UDIG requires this to work. */
    @Test
    public void test42102() throws FactoryException {
        final Hints hints = new Hints(Hints.CRS_AUTHORITY_FACTORY, UnnamedExtension.class);
        final CRSAuthorityFactory factory = new OrderedAxisAuthorityFactory("EPSG", hints, null);
        final CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:42102");
        assertNotNull(crs);
        assertNotNull(crs.getIdentifiers());
        assertFalse(crs.getIdentifiers().isEmpty());
        NamedIdentifier expected = new NamedIdentifier(Citations.EPSG, "42102");
        assertTrue(crs.getIdentifiers().contains(expected));
    }

    /** WFS requires this to work. */
    @Test
    public void test42102Lower() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("epsg:42102");
        assertNotNull(crs);
        assertNotNull(crs.getIdentifiers());
        assertFalse(crs.getIdentifiers().isEmpty());
        NamedIdentifier expected = new NamedIdentifier(Citations.EPSG, "42102");
        assertTrue(crs.getIdentifiers().contains(expected));
    }

    /** WFS requires this to work. */
    @Test
    public void test42304Lower() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("epsg:42304");
        assertNotNull(crs);
    }

    /**
     * Test for Google's Projection under its unofficial code (EPSG:900913).
     *
     * <p>The official supported code for that projection is EPSG:3857, and both should be
     * equivalent.
     */
    @Test
    public void test900913() {
        try {
            CoordinateReferenceSystem sourceCRS;
            sourceCRS = CRS.decode("EPSG:4326");
            CoordinateReferenceSystem googleCRS = CRS.decode("EPSG:900913");
            CoordinateReferenceSystem officialCRS = CRS.decode("EPSG:3857");

            MathTransform transformGoogle = CRS.findMathTransform(sourceCRS, googleCRS, true);
            MathTransform transformOfficial = CRS.findMathTransform(sourceCRS, officialCRS, true);

            Coordinate sourceCoord = new Coordinate(-22, -44);
            Coordinate destCoordGoogle = JTS.transform(sourceCoord, null, transformGoogle);
            Coordinate destCoordOfficial = JTS.transform(sourceCoord, null, transformOfficial);

            assertEquals(destCoordOfficial, destCoordGoogle);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            fail(e.getClass().getSimpleName() + " should not be thrown.");
        }
    }

    /**
     * Tests the extensions through a URI.
     *
     * @see http://jira.codehaus.org/browse/GEOT-1563
     */
    @Test
    public void testURI() throws FactoryException {
        final String id = "100001";
        final CoordinateReferenceSystem crs = CRS.decode("EPSG:" + id);
        assertSame(crs, CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:" + id));
        assertSame(crs, CRS.decode("http://www.opengis.net/gml/srs/epsg.xml#" + id));
    }
}
