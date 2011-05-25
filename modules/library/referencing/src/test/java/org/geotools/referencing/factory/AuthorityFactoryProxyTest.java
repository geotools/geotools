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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.factory;

import org.opengis.referencing.crs.*;
import org.opengis.referencing.datum.*;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.FactoryException;

import org.geotools.referencing.crs.*;
import org.geotools.referencing.datum.*;
import org.geotools.referencing.ReferencingFactoryFinder;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link AuthorityFactoryProxy} implementation.
 *
 * @author Martin Desruisseaux
 *
 * @source $URL$
 * @version $Id$
 */
public final class AuthorityFactoryProxyTest {
    /**
     * Tests {@link AuthorityFactoryProxy#getType(Class)}.
     */
    @Test
    public void testType() {
        assertEquals(             ProjectedCRS.class, AuthorityFactoryProxy.getType(        ProjectedCRS.class));
        assertEquals(             ProjectedCRS.class, AuthorityFactoryProxy.getType( DefaultProjectedCRS.class));
        assertEquals(            GeographicCRS.class, AuthorityFactoryProxy.getType(       GeographicCRS.class));
        assertEquals(            GeographicCRS.class, AuthorityFactoryProxy.getType(DefaultGeographicCRS.class));
        assertEquals(               DerivedCRS.class, AuthorityFactoryProxy.getType(   DefaultDerivedCRS.class));
        assertEquals(CoordinateReferenceSystem.class, AuthorityFactoryProxy.getType(  AbstractDerivedCRS.class));
        assertEquals(            GeodeticDatum.class, AuthorityFactoryProxy.getType(DefaultGeodeticDatum.class));
    }

    /**
     * Tests {@link AuthorityFactoryProxy#create}. We uses the CRS factory for testing purpose.
     */
    @Test
    public void testCreate() throws FactoryException {
        final CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("CRS", null);
        final CoordinateReferenceSystem expected = factory.createCoordinateReferenceSystem("83");
        AuthorityFactoryProxy proxy;
        /*
         * Try the proxy using the 'createGeographicCRS', 'createCoordinateReferenceSystem'
         * and 'createObject' methods. The later uses a generic implementation, while the
         * first two should use specialized implementations.
         */
        proxy = AuthorityFactoryProxy.getInstance(factory, GeographicCRS.class);
        assertTrue(proxy.getClass().getName().endsWith("Geographic"));
        assertSame(expected, proxy.create("83"));
        assertSame(expected, proxy.create("CRS:83"));

        proxy = AuthorityFactoryProxy.getInstance(factory, CoordinateReferenceSystem.class);
        assertTrue(proxy.getClass().getName().endsWith("CRS"));
        assertSame(expected, proxy.create("83"));
        assertSame(expected, proxy.create("CRS:83"));

        proxy = AuthorityFactoryProxy.getInstance(factory, IdentifiedObject.class);
        assertTrue(proxy.getClass().getName().endsWith("Default"));
        assertSame(expected, proxy.create("83"));
        assertSame(expected, proxy.create("CRS:83"));
        /*
         * Try using the 'createProjectedCRS' method, which should not
         * be supported for the CRS factory (at least not for code "83").
         */
        proxy = AuthorityFactoryProxy.getInstance(factory, ProjectedCRS.class);
        assertTrue(proxy.getClass().getName().endsWith("Projected"));
        try {
            assertSame(expected, proxy.create("83"));
            fail();
        } catch (FactoryException e) {
            // This is the expected exception.
            assertTrue(e.getCause() instanceof ClassCastException);
        }
        /*
         * Try using the 'createTemporalCRS' method, which should not
         * be supported for the CRS factory (at least not for code "83").
         * In addition, this code test the generic proxy instead of the
         * specialized 'GeographicCRS' and 'ProjectedCRS' variants.
         */
        proxy = AuthorityFactoryProxy.getInstance(factory, TemporalCRS.class);
        assertTrue(proxy.getClass().getName().endsWith("Default"));
        try {
            assertSame(expected, proxy.create("83"));
            fail();
        } catch (FactoryException e) {
            // This is the expected exception.
            assertTrue(e.getCause() instanceof ClassCastException);
        }
    }

    /**
     * Tests {@link IdentifiedObjectFinder#createFromCodes}.
     * We uses the CRS factory for testing purpose.
     */
    @Test
    public void testCreateFromCodes() throws FactoryException {
        final CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("CRS", null);
        final IdentifiedObjectFinder proxy = new IdentifiedObjectFinder(factory, GeographicCRS.class);
        CoordinateReferenceSystem expected = factory.createCoordinateReferenceSystem("84");
        assertNotSame(expected, DefaultGeographicCRS.WGS84);
        assertSame   (expected, proxy.createFromCodes      (expected));
        assertSame   (expected, proxy.createFromIdentifiers(expected));
        assertNull   (          proxy.createFromNames      (expected));
        assertSame   (expected, proxy.createFromCodes      (DefaultGeographicCRS.WGS84));
        assertNull   (          proxy.createFromIdentifiers(DefaultGeographicCRS.WGS84));
        assertNull   (          proxy.createFromNames      (DefaultGeographicCRS.WGS84));

        expected = factory.createCoordinateReferenceSystem("83");
        assertSame   (expected, proxy.createFromCodes      (expected));
        assertSame   (expected, proxy.createFromIdentifiers(expected));
        assertNull   (          proxy.createFromNames      (expected));
    }
}
