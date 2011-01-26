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

import java.io.File;
import java.util.Collection;

import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;

import org.geotools.factory.Hints;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.NamedIdentifier;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link FactoryUsingWKT}.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (Geomatys)
 * @author Jody Garnett
 */
public final class FactoryUsingWktTest {
    /**
     * The factory to test.
     */
    private FactoryUsingWKT factory;

    /**
     * Gets the authority factory for ESRI.
     */
    @Before
    public void setUp() {
        factory = (FactoryUsingWKT) ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG",
                new Hints(Hints.CRS_AUTHORITY_FACTORY, FactoryUsingWKT.class));
    }

    /**
     * Tests the setting of "CRS authority extra directory" hint.
     *
     * @throws Exception If a factory or a transform exception occured.
     */
    @Test
    public void testCrsAuthorityExtraDirectoryHint() throws Exception {
        Hints hints = new Hints(Hints.CRS_AUTHORITY_FACTORY, FactoryUsingWKT.class);
        try {
           hints.put(Hints.CRS_AUTHORITY_EXTRA_DIRECTORY, "invalid");
           fail("Should of been tossed out as an invalid hint");
        }
        catch (IllegalArgumentException expected) {
            // This is the expected exception.
        }
        String directory = new File(".").getAbsolutePath();
        hints = new Hints(Hints.CRS_AUTHORITY_FACTORY, FactoryUsingWKT.class);
        hints.put(Hints.CRS_AUTHORITY_EXTRA_DIRECTORY, directory);
        // TODO: test the factory here.
    }

    /**
     * Tests the authority code.
     */
    @Test
    public void testAuthority(){
        final Citation authority = factory.getAuthority();
        assertNotNull(authority);
        assertEquals("European Petroleum Survey Group", authority.getTitle().toString());
        assertTrue (Citations.identifierMatches(authority, "EPSG"));
        assertFalse(Citations.identifierMatches(authority, "ESRI"));
        assertTrue(factory instanceof FactoryUsingWKT);
    }

    /**
     * Tests the vendor.
     */
    @Test
    public void testVendor(){
        final Citation vendor = factory.getVendor();
        assertNotNull(vendor);
        assertEquals("Geotools", vendor.getTitle().toString());
    }

    /**
     * Tests the {@code 18001} code.
     *
     * @throws FactoryException If the CRS can't be created.
     */
    @Test
    public void test42101() throws FactoryException {
        CoordinateReferenceSystem actual, expected;
        expected = factory.createCoordinateReferenceSystem("42101");
        actual   = CRS.decode("EPSG:42101");
        assertSame(expected, actual);
        assertTrue(actual instanceof ProjectedCRS);
        Collection ids = actual.getIdentifiers();
        assertTrue (ids.contains(new NamedIdentifier(Citations.EPSG, "42101")));
        assertFalse(ids.contains(new NamedIdentifier(Citations.ESRI, "42101")));
    }
}
