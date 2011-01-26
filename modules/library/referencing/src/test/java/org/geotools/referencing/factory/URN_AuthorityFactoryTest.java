/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.factory.Hints;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.factory.FactoryNotFoundException;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link URN_AuthorityFactory} class backed by WMS or AUTO factories.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class URN_AuthorityFactoryTest {
    /**
     * Make sure that a singleton instance is registered.
     */
    @Test
    public void testRegistration() {
        String authority = "URN:OGC:DEF";
        final AuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory(authority, null);
        assertSame(factory, ReferencingFactoryFinder.getCRSAuthorityFactory  (authority, null));
        assertSame(factory, ReferencingFactoryFinder.getCSAuthorityFactory   (authority, null));
        assertSame(factory, ReferencingFactoryFinder.getDatumAuthorityFactory(authority, null));
        /*
         * Tests the X-OGC namespace, which should be synonymous.
         */
        authority = "URN:X-OGC:DEF";
        assertSame(factory, ReferencingFactoryFinder.getCRSAuthorityFactory  (authority, null));
        assertSame(factory, ReferencingFactoryFinder.getCSAuthorityFactory   (authority, null));
        assertSame(factory, ReferencingFactoryFinder.getDatumAuthorityFactory(authority, null));
    }

    /**
     * Tests the CRS factory.
     */
    @Test
    public void testCRS() throws FactoryException {
        CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("URN:OGC:DEF", null);
        GeographicCRS crs;
        try {
            crs = factory.createGeographicCRS("CRS:84");
            fail();
        } catch (NoSuchAuthorityCodeException exception) {
            // This is the expected exception.
            assertEquals("CRS:84", exception.getAuthorityCode());
        }
        crs =           factory.createGeographicCRS("urn:ogc:def:crs:CRS:WMS1.3:84");
        assertSame(crs, factory.createGeographicCRS("urn:ogc:def:crs:CRS:1.3:84"));
        assertSame(crs, factory.createGeographicCRS("URN:OGC:DEF:CRS:CRS:1.3:84"));
        assertSame(crs, factory.createGeographicCRS("URN:OGC:DEF:CRS:CRS:84"));
        assertSame(crs, factory.createGeographicCRS("urn:x-ogc:def:crs:CRS:1.3:84"));
        assertSame(crs, CRS.decode("urn:ogc:def:crs:CRS:1.3:84"));
        assertSame(crs, CRS.decode("CRS:84"));
        assertNotSame(crs, DefaultGeographicCRS.WGS84);
        assertFalse(DefaultGeographicCRS.WGS84.equals(crs));
        assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, crs));

        // Test CRS:83
        crs = factory.createGeographicCRS("urn:ogc:def:crs:CRS:1.3:83");
        assertSame(crs, CRS.decode("CRS:83"));
        assertFalse(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, crs));
    }

    /**
     * Tests fetching the URN authority when the "longitude first axis order" hint is set.
     */
    @Test
    public void testWhenForceXY() throws FactoryException {
        try {
            Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "http");
            Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
            try {
                ReferencingFactoryFinder.getCRSAuthorityFactory("URN:OGC:DEF", null);
                fail("URN factory should not accept FORCE_LONGITUDE_FIRST_AXIS_ORDER = TRUE");
            } catch (FactoryNotFoundException e) {
                // This is the expected exception.
            }
            CoordinateReferenceSystem crs = CRS.decode("URN:OGC:DEF:CRS:CRS:84", true);
            assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, crs));
            crs = CRS.decode("URN:OGC:DEF:CRS:CRS:84");
            assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, crs));
        } finally {
            Hints.removeSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING);
            Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        }
    }
}
