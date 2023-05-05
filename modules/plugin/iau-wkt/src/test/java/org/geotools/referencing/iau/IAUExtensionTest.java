/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.iau;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class IAUExtensionTest {

    /** The factory to test. */
    private IAUAuthorityFactory factory;

    /** Get the authority factory for IAU. */
    @Before
    public void setUp() throws Exception {
        factory =
                (IAUAuthorityFactory) ReferencingFactoryFinder.getCRSAuthorityFactory("IAU", null);
    }

    /** Tests the authority code. */
    @Test
    public void testAuthority() {
        Citation authority = factory.getAuthority();
        assertNotNull(authority);
        assertEquals("IAU", authority.getTitle().toString());
        assertTrue(factory instanceof IAUAuthorityFactory);
    }

    @Test
    public void testVendor() {
        Citation vendor = factory.getVendor();
        assertNotNull(vendor);
        assertEquals("Geotools", vendor.getTitle().toString());
    }

    @Test
    public void testCodes() throws FactoryException {
        final Set<String> codes = factory.getAuthorityCodes(IdentifiedObject.class);
        final Set<String> subset = factory.getAuthorityCodes(CoordinateReferenceSystem.class);
        assertNotNull(codes);
        assertEquals(codes.size(), subset.size());
        assertTrue(codes.containsAll(subset));
        assertTrue(codes.contains("1010")); // also an EPSG code
        assertEquals(2079, codes.size());
    }

    /** Checks for CRS instantiations. */
    @Test
    public void testInstantiation() throws FactoryException {
        final StringWriter buffer = new StringWriter();
        final PrintWriter writer = new PrintWriter(buffer);
        final Set duplicated = factory.reportInstantiationFailures(writer);
        // The following number may be adjusted if esri.properties is updated.
        assertTrue(buffer.toString(), duplicated.size() <= 87);
    }

    /** Tests an non existing. */
    @Test(expected = NoSuchAuthorityCodeException.class)
    public void test26910() throws FactoryException {
        factory.createCoordinateReferenceSystem("26910");
    }

    /** Tests the code for the Sun. */
    @Test
    @Ignore // have to figure out why the citation is not the expected one
    public void test1000() throws FactoryException {
        final CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("1000");
        assertSame(crs, factory.createCoordinateReferenceSystem("IAU:1000"));
        assertSame(crs, factory.createCoordinateReferenceSystem("iau:1000"));
        assertSame(crs, factory.createCoordinateReferenceSystem(" iau : 1000 "));
        assertSame(crs, factory.createObject("1000"));
        final Set identifiers = crs.getIdentifiers();
        assertNotNull(identifiers);
        assertFalse(identifiers.isEmpty());

        String asString = identifiers.toString();
        assertTrue(asString, identifiers.contains(new NamedIdentifier(Citations.IAU, "1000")));

        final Iterator iterator = identifiers.iterator();

        // Checks the first identifier.
        assertTrue(iterator.hasNext());
        Identifier identifier = (Identifier) iterator.next();
        assertTrue(identifier instanceof NamedIdentifier);
        assertEquals(Citations.IAU, identifier.getAuthority());
        assertEquals("IAU", identifier.getCode());
        assertEquals("IAU:1000", identifier.toString());
    }
}
