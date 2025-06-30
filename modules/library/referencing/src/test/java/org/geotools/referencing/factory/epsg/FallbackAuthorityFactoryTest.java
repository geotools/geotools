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
package org.geotools.referencing.factory.epsg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.Set;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.WKT;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Checks the exception thrown by the fallback system do report actual errors when the code is available but for some
 * reason broken, and not "code not found" ones.
 *
 * @version $Id$
 * @author Andrea Aime (TOPP)
 */
public final class FallbackAuthorityFactoryTest {
    /** The extra factory. */
    private FactoryEPSGExtra extra;

    /** Adds the extra factory to the set of authority factories. */
    @Before
    public void setUp() {
        assertNull(extra);

        CRS.reset("all");
        extra = new FactoryEPSGExtra();
        ReferencingFactoryFinder.addAuthorityFactory(extra);
        ReferencingFactoryFinder.scanForPlugins();
    }

    /** Removes the extra factory from the set of authority factories. */
    @After
    public void tearDown() {
        assertNotNull(extra);
        ReferencingFactoryFinder.removeAuthorityFactory(extra);
        extra = null;
    }

    /** Makes sure that the testing {@link FactoryEPSGExtra} has precedence over {@link FactoryUsingWKT}. */
    @Test
    public void testFactoryOrdering() {
        Set<CRSAuthorityFactory> factories = ReferencingFactoryFinder.getCRSAuthorityFactories(null);
        boolean foundWkt = false;
        boolean foundExtra = false;
        for (CRSAuthorityFactory factory : factories) {
            Class<?> type = factory.getClass();
            if (type == FactoryEPSGExtra.class) {
                foundExtra = true;
            } else if (type == FactoryUsingWKT.class) {
                foundWkt = true;
                assertTrue("We should have encountered WKT factory after the extra one", foundExtra);
            }
        }
        assertTrue(foundWkt);
        assertTrue(foundExtra);
    }

    /**
     * Tests the {@code 42101} code. The purpose of this test is mostly to make sure that {@link FactoryUsingWKT} is in
     * the chain.
     *
     * @throws FactoryException If the CRS can't be created.
     */
    @Test
    public void test42101() throws FactoryException {
        assertTrue(CRS.decode("EPSG:42101") instanceof ProjectedCRS);
    }

    /**
     * Tests the {@code 00001} fake code.
     *
     * @throws FactoryException If the CRS can't be created.
     */
    @Test
    @SuppressWarnings("CatchFail")
    public void test00001() throws FactoryException {
        try {
            CRS.decode("EPSG:00001");
            fail("This code should not be there");
        } catch (NoSuchAuthorityCodeException e) {
            fail("The code 00001 is there, exception should report it's broken");
        } catch (FactoryException e) {
            // cool, that's what we expected
        }
    }

    /**
     * GEOT-1702, make sure looking up for an existing code does not result in a {@link StackOverflowException}.
     *
     * @throws FactoryException If the CRS can't be created.
     */
    @Test
    public void testLookupSuccessfull() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:42101");
        String code = CRS.lookupIdentifier(crs, true);
        assertEquals("EPSG:42101", code);
    }

    /**
     * GEOT-1702, make sure looking up for a non existing code does not result in a {@link StackOverflowException}.
     *
     * @throws FactoryException If the CRS can't be created.
     */
    @Test
    public void testLookupFailing() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.parseWKT(WKT.MERCATOR_GOOGLE);
        assertNull(CRS.lookupIdentifier(crs, true));
    }

    /**
     * Extra class used to make sure we have {@link FactoryUsingWKT} among the fallbacks (used to check the fallback
     * mechanism).
     *
     * @author Andrea Aime (TOPP)
     */
    private static class FactoryEPSGExtra extends FactoryUsingWKT {
        /** Creates a factory to be registered before {@link FactoryUsingWKT} in the fallback chain. */
        public FactoryEPSGExtra() {
            // make sure we are before FactoryUsingWKT in the fallback chain
            super(null, DEFAULT_PRIORITY + 5);
        }

        /** Returns the URL to the test file that contains a broken CRS for code EPSG:1. */
        @Override
        protected URL getDefinitionsURL() {
            return FactoryUsingWKT.class.getResource("epsg2.properties");
        }
    }
}
