/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2012, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.geotools.factory.FactoryNotFoundException;
import org.geotools.factory.Hints;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;

/**
 * Tests for {@link HTTP_URI_AuthorityFactory}.
 * 
 * @author Martin Desruisseaux
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * 
 * @source $URL$
 */
public class HTTP_URI_AuthorityFactoryTest {

    /**
     * Make sure that a singleton instance is registered.
     */
    @Test
    public void testRegistration() {
        String authority = "http://www.opengis.net/def";
        AuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory(authority, null);
        assertSame(factory, ReferencingFactoryFinder.getCRSAuthorityFactory(authority, null));
        assertSame(factory, ReferencingFactoryFinder.getCSAuthorityFactory(authority, null));
        assertSame(factory, ReferencingFactoryFinder.getDatumAuthorityFactory(authority, null));
    }

    /**
     * Tests the HTTP URI CRS factory.
     */
    @Test
    public void testCRS() throws FactoryException {
        CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory(
                "http://www.opengis.net/def", null);
        GeographicCRS crs;
        try {
            crs = factory.createGeographicCRS("CRS:84");
            fail();
        } catch (NoSuchAuthorityCodeException exception) {
            assertEquals("CRS:84", exception.getAuthorityCode());
        }
        crs = factory.createGeographicCRS("http://www.opengis.net/def/crs/CRS/WMS1.3/84");
        assertSame(crs, factory.createGeographicCRS("http://www.opengis.net/def/crs/CRS/1.3/84"));
        assertSame(crs, factory.createGeographicCRS("HTTP://WWW.OPENGIS.NET/DEF/CRS/CRS/1.3/84"));
        assertSame(crs, factory.createGeographicCRS("HTTP://WWW.OPENGIS.NET/DEF/CRS/CRS/0/84"));
        assertSame(crs, CRS.decode("http://www.opengis.net/def/crs/CRS/1.3/84"));
        assertSame(crs, CRS.decode("CRS:84"));
        assertNotSame(crs, DefaultGeographicCRS.WGS84);
        assertFalse(DefaultGeographicCRS.WGS84.equals(crs));
        assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, crs));
        crs = factory.createGeographicCRS("http://www.opengis.net/def/crs/CRS/1.3/83");
        assertSame(crs, CRS.decode("CRS:83"));
        assertFalse(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, crs));
    }

    /**
     * Tests fetching the HTTP URI CRS factory when the "longitude first axis order" hint is set. This test ensures that the factory ignores this
     * hint.
     */
    @Test
    public void testWhenForceXY() throws FactoryException {
        try {
            Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "http");
            Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
            try {
                ReferencingFactoryFinder.getCRSAuthorityFactory("http://www.opengis.net/def", null);
                fail("HTTP URI factory should not accept FORCE_LONGITUDE_FIRST_AXIS_ORDER = true");
            } catch (FactoryNotFoundException e) {
                // success
            }
            CoordinateReferenceSystem crs = CRS.decode("http://www.opengis.net/def/crs/CRS/0/84",
                    true);
            assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, crs));
            crs = CRS.decode("http://www.opengis.net/def/crs/CRS/0/84");
            assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, crs));
        } finally {
            Hints.removeSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING);
            Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        }
    }

    @Test
    public void testDecode() throws NoSuchAuthorityCodeException, FactoryException {
        assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84,
                CRS.decode("http://www.opengis.net/def/crs/CRS/0/84")));
    }

}
