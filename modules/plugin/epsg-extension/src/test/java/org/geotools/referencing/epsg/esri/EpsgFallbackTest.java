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
package org.geotools.referencing.epsg.esri;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.factory.epsg.FactoryUsingWKT;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link FactoryUsingWKT} as a fallback after the default factory. This method performs the tests through the
 * {@link CRS#decode} method.
 *
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 */
public class EpsgFallbackTest {

    /** A random CRS for fun. This CRS is defined in the {@linkplain DefaultFactory default EPSG authority factory}. */
    @Test
    public void test26910() throws FactoryException {
        final String code = "EPSG:26910";
        final CoordinateReferenceSystem crs = CRS.decode(code);
        Assert.assertNotNull(crs);
        assertTrue(crs instanceof ProjectedCRS);
        Assert.assertFalse(CRS.equalsIgnoreMetadata(crs, CRS.decode(code, true)));
    }

    /**
     * UDIG requires this to work. This CRS is defined in the {@linkplain DefaultFactory default EPSG authority
     * factory}.
     */
    @Test
    public void test4326() throws FactoryException {
        final String code = "EPSG:4326";
        final CoordinateReferenceSystem crs = CRS.decode(code);
        Assert.assertNotNull(crs);
        assertTrue(crs instanceof GeographicCRS);
        Assert.assertFalse(CRS.equalsIgnoreMetadata(crs, CRS.decode(code, true)));
    }

    /**
     * UDIG requires this to work. This CRS is defined in the {@linkplain DefaultFactory default EPSG authority
     * factory}.
     */
    @Test
    public void test4269() throws FactoryException {
        final String code = "EPSG:4269";
        final CoordinateReferenceSystem crs = CRS.decode(code);
        Assert.assertNotNull(crs);
        assertTrue(crs instanceof GeographicCRS);
        Assert.assertFalse(CRS.equalsIgnoreMetadata(crs, CRS.decode(code, true)));
    }

    /** UDIG requires this to work. This CRS is defined in {@code unnamed.properties}. */
    @Test
    public void test42102() throws FactoryException {
        final String code = "EPSG:42102";
        final CoordinateReferenceSystem crs = CRS.decode(code);
        Assert.assertNotNull(crs);
        assertTrue(crs instanceof ProjectedCRS);
        Assert.assertSame(crs, CRS.decode(code, true));

        // Checks identifier
        final Collection<ReferenceIdentifier> identifiers = crs.getIdentifiers();
        Assert.assertNotNull(identifiers);
        Assert.assertFalse(identifiers.isEmpty());
        NamedIdentifier expected = new NamedIdentifier(Citations.EPSG, "42102");
        assertTrue(identifiers.contains(expected));
    }

    /** A random CRS for fun. This CRS is defined in the {@linkplain DefaultFactory default EPSG authority factory}. */
    @Test
    public void test26910Lower() throws FactoryException {
        final String code = "epsg:26910";
        final CoordinateReferenceSystem crs = CRS.decode(code);
        Assert.assertNotNull(crs);
        assertTrue(crs instanceof ProjectedCRS);
        Assert.assertFalse(CRS.equalsIgnoreMetadata(crs, CRS.decode(code, true)));
    }

    /** A random CRS for fun. This CRS is defined in the {@linkplain DefaultFactory default EPSG authority factory}. */
    @Test
    public void test26986Lower() throws FactoryException {
        final String code = "epsg:26986";
        final CoordinateReferenceSystem crs = CRS.decode(code);
        Assert.assertNotNull(crs);
        assertTrue(crs instanceof ProjectedCRS);
        Assert.assertFalse(CRS.equalsIgnoreMetadata(crs, CRS.decode(code, true)));
    }

    /**
     * WFS requires this to work. This CRS is defined in the {@linkplain DefaultFactory default EPSG authority factory}.
     */
    @Test
    public void test4326Lower() throws FactoryException {
        final String code = "epsg:4326";
        final CoordinateReferenceSystem crs = CRS.decode(code);
        Assert.assertNotNull(crs);
        assertTrue(crs instanceof GeographicCRS);
        Assert.assertFalse(CRS.equalsIgnoreMetadata(crs, CRS.decode(code, true)));
    }

    /**
     * WFS requires this to work. This CRS is defined in the {@linkplain DefaultFactory default EPSG authority factory}.
     */
    @Test
    public void test26742Lower() throws FactoryException {
        final String code = "epsg:26742";
        final CoordinateReferenceSystem crs = CRS.decode(code);
        Assert.assertNotNull(crs);
        assertTrue(crs instanceof ProjectedCRS);
        Assert.assertFalse(CRS.equalsIgnoreMetadata(crs, CRS.decode(code, true)));
    }

    /**
     * WFS requires this to work. This CRS is defined in the {@linkplain DefaultFactory default EPSG authority factory}.
     */
    @Test
    public void test4269Lower() throws FactoryException {
        final String code = "epsg:4269";
        final CoordinateReferenceSystem crs = CRS.decode(code);
        Assert.assertNotNull(crs);
        assertTrue(crs instanceof GeographicCRS);
        Assert.assertFalse(CRS.equalsIgnoreMetadata(crs, CRS.decode(code, true)));
    }

    /** WFS requires this to work. This CRS is defined in {@code unnamed.properties}. */
    @Test
    public void test42304Lower() throws FactoryException {
        final String code = "epsg:42304";
        final CoordinateReferenceSystem crs = CRS.decode(code);
        Assert.assertNotNull(crs);
        Assert.assertSame(crs, CRS.decode(code, true));
    }

    /** WFS requires this to work. This CRS is defined in {@code unnamed.properties}. */
    @Test
    public void test42102Lower() throws FactoryException {
        final String code = "epsg:42102";
        final CoordinateReferenceSystem crs = CRS.decode(code);
        Assert.assertNotNull(crs);
        Assert.assertSame(crs, CRS.decode(code, true));

        // Checks identifier
        final Collection<ReferenceIdentifier> identifiers = crs.getIdentifiers();
        Assert.assertNotNull(identifiers);
        Assert.assertFalse(identifiers.isEmpty());
        NamedIdentifier expected = new NamedIdentifier(Citations.EPSG, "42102");
        assertTrue(identifiers.contains(expected));
    }

    /** This CRS is defined in {@code esri.properties}. */
    @Test
    public void test54004() throws FactoryException {
        final CRSAuthorityFactory factory = CRS.getAuthorityFactory(false);
        final String code = "EPSG:54004";
        final CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem(code);
        Assert.assertNotNull(crs);
        Assert.assertSame(crs, CRS.decode(code, true));
        Assert.assertEquals("World_Mercator", String.valueOf(factory.getDescriptionText(code)));

        // Equivalent standard ESPG
        Assert.assertEquals("WGS 84 / World Mercator", String.valueOf(factory.getDescriptionText("EPSG:3395")));
        // TODO: enable if we implement more intelligent 'equalsIgnoreMetadata'
        // final CoordinateReferenceSystem standard =
        // factory.createCoordinateReferenceSystem("EPSG:3395");
        // assertTrue(CRS.equalsIgnoreMetadata(crs, standard));
    }

    /** Tests the obtention of various codes. */
    @Test
    public void testCodes() throws FactoryException {
        final CRSAuthorityFactory factory = CRS.getAuthorityFactory(false);
        final Collection codes = factory.getAuthorityCodes(ProjectedCRS.class);
        assertTrue(codes.contains("EPSG:3395")); // Defined in EPSG database
        assertTrue(codes.contains("ESRI:54004")); // Defined in ESRI database
        assertTrue(codes.contains("EPSG:54004")); // With EPSG as well
        assertTrue(codes.contains("EPSG:42304")); // Defined in unnamed database
        assertTrue(codes.contains("EPSG:26742")); // Defined in EPSG database
        assertTrue(codes.contains("EPSG:42102")); // Defined in unnamed database
        Assert.assertFalse(codes.contains("EPSG:4326")); // This is a GeographicCRS, not a
        // ProjectedCRS
        assertTrue(codes.contains("EPSG:100002")); // Defined in unnamed database
        Assert.assertFalse(codes.contains("EPSG:100001")); // This is a GeographicCRS, not a ProjectedCRS
    }
}
