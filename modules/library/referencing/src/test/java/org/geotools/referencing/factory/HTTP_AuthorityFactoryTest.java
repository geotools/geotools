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
package org.geotools.referencing.factory;

import org.geotools.factory.Hints;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.CRSAuthorityFactory;

import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link HTTP_AuthorityFactory} class backed by WMS or AUTO factories.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class HTTP_AuthorityFactoryTest {
    /**
     * Tests the {@link HTTP_AuthorityFactory#defaultAxisOrderHints} method.
     */
    @Test
    public void testAxisOrderHints() {
        // The following are required for proper execution of the remaining of this test.
        assertNull(Hints.getSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER));
        assertNull(Hints.getSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING));

        // Standard behavior should be to set FORCE_LONGITUDE_FIRST_AXIS_ORDER to false.
        assertFalse(HTTP_AuthorityFactory.defaultAxisOrderHints(null, "http"));

        try {
            // The hints should be ignored.
            Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
            assertFalse(HTTP_AuthorityFactory.defaultAxisOrderHints(null, "http"));

            // The hints should be honored.
            Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "http");
            assertTrue(HTTP_AuthorityFactory.defaultAxisOrderHints(null, "http"));

            // The hints should be ignored.
            Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "urn");
            assertFalse(HTTP_AuthorityFactory.defaultAxisOrderHints(null, "http"));

            // The hints should be honored.
            Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "http, urn");
            assertTrue(HTTP_AuthorityFactory.defaultAxisOrderHints(null, "http"));

            // The hints should be honored.
            Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "urn, http");
            assertTrue(HTTP_AuthorityFactory.defaultAxisOrderHints(null, "http"));
        } finally {
            Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
            Hints.removeSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING);
        }
    }

    /**
     * Tests the CRS factory.
     */
    @Test
    public void testCRS() throws FactoryException {
        CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("http://www.opengis.net", null);
        GeographicCRS crs;
        try {
            crs = factory.createGeographicCRS("CRS:84");
            fail();
        } catch (NoSuchAuthorityCodeException exception) {
            // This is the expected exception.
            assertEquals("CRS:84", exception.getAuthorityCode());
        }
        crs = factory.createGeographicCRS("http://www.opengis.net/gml/srs/crs.xml#84");
        assertSame(crs, CRS.decode("http://www.opengis.net/gml/srs/crs.xml#84"));
        assertSame(crs, CRS.decode("CRS:84"));
        assertNotSame(crs, DefaultGeographicCRS.WGS84);
        assertFalse(DefaultGeographicCRS.WGS84.equals(crs));
        assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, crs));

        // Test CRS:83
        crs = factory.createGeographicCRS("http://www.opengis.net/gml/srs/crs.xml#83");
        assertSame(crs, CRS.decode("CRS:83"));
        assertFalse(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, crs));
    }
}
