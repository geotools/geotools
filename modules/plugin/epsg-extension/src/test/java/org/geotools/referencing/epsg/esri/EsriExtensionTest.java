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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.junit.Assert;
import org.junit.Before;

/**
 * Tests ESRI CRS support.
 *
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 */
public class EsriExtensionTest {
    /** The factory to test. */
    private EsriExtension factory;

    /** Get the authority factory for ESRI. */
    @Before
    public void setUp() throws Exception {
        factory = (EsriExtension) ReferencingFactoryFinder.getCRSAuthorityFactory("ESRI", null);
    }

    /** Tests the authority code. */
    @org.junit.Test
    public void testAuthority() {
        Citation authority = factory.getAuthority();
        Assert.assertNotNull(authority);
        Assert.assertEquals("ESRI", authority.getTitle().toString());
        Assert.assertTrue(factory instanceof EsriExtension);
    }

    /** Tests the vendor. */
    @org.junit.Test
    public void testVendor() {
        Citation vendor = factory.getVendor();
        Assert.assertNotNull(vendor);
        Assert.assertEquals("Geotools", vendor.getTitle().toString());
    }

    /** Tests the codes. */
    @org.junit.Test
    public void testCodes() throws FactoryException {
        final Set<String> codes = factory.getAuthorityCodes(IdentifiedObject.class);
        final Set<String> subset = factory.getAuthorityCodes(CoordinateReferenceSystem.class);
        Assert.assertNotNull(codes);
        Assert.assertEquals(codes.size(), subset.size());
        Assert.assertTrue(codes.containsAll(subset));
        Assert.assertFalse(codes.contains("26910")); // This is an EPSG code.
        // The following number may be adjusted if esri.properties is updated.
        Assert.assertEquals(796, codes.size());
    }

    /** Checks for duplication with EPSG-HSQL. */
    @org.junit.Test
    public void testDuplication() throws FactoryException {
        final StringWriter buffer = new StringWriter();
        final PrintWriter writer = new PrintWriter(buffer);
        final Set duplicated = factory.reportDuplicatedCodes(writer);
        Assert.assertTrue(buffer.toString(), duplicated.isEmpty());
    }

    /** Checks for CRS instantiations. */
    @org.junit.Test
    public void testInstantiation() throws FactoryException {
        final StringWriter buffer = new StringWriter();
        final PrintWriter writer = new PrintWriter(buffer);
        final Set duplicated = factory.reportInstantiationFailures(writer);
        // The following number may be adjusted if esri.properties is updated.
        Assert.assertTrue(buffer.toString(), duplicated.size() <= 87);
    }

    /** Tests an EPSG code. */
    @org.junit.Test(expected = NoSuchAuthorityCodeException.class)
    public void test26910() throws FactoryException {
        factory.createCoordinateReferenceSystem("26910");
    }

    /** Tests an EPSG code. */
    @org.junit.Test(expected = NoSuchAuthorityCodeException.class)
    public void test4326() throws FactoryException {
        factory.createCoordinateReferenceSystem("4326");
    }

    /** Tests an EPSG code. */
    @org.junit.Test(expected = NoSuchAuthorityCodeException.class)
    public void test4269() throws FactoryException {
        factory.createCoordinateReferenceSystem("4269");
    }

    /** Tests an extra code (neither EPSG or ESRI). */
    @org.junit.Test(expected = NoSuchAuthorityCodeException.class)
    public void test42333() throws FactoryException {
        factory.createCoordinateReferenceSystem("42333");
    }

    /** Tests an ESRI code. */
    @org.junit.Test
    public void test30591() throws FactoryException {
        final CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("30591");
        Assert.assertSame(crs, factory.createCoordinateReferenceSystem("ESRI:30591"));
        Assert.assertSame(crs, factory.createCoordinateReferenceSystem("esri:30591"));
        Assert.assertSame(crs, factory.createCoordinateReferenceSystem(" ESRI : 30591 "));
        Assert.assertSame(crs, factory.createCoordinateReferenceSystem("EPSG:30591"));
        Assert.assertSame(crs, factory.createObject("30591"));
        final Set identifiers = crs.getIdentifiers();
        Assert.assertNotNull(identifiers);
        Assert.assertFalse(identifiers.isEmpty());

        String asString = identifiers.toString();
        Assert.assertTrue(asString, identifiers.contains(new NamedIdentifier(Citations.ESRI, "30591")));
        Assert.assertTrue(asString, identifiers.contains(new NamedIdentifier(Citations.EPSG, "30591")));

        final Iterator iterator = identifiers.iterator();

        // Checks the first identifier.
        Assert.assertTrue(iterator.hasNext());
        Identifier identifier = (Identifier) iterator.next();
        Assert.assertTrue(identifier instanceof NamedIdentifier);
        Assert.assertEquals(Citations.ESRI, identifier.getAuthority());
        Assert.assertEquals("30591", identifier.getCode());
        Assert.assertEquals("ESRI:30591", identifier.toString());

        // Checks the second identifier.
        Assert.assertTrue(iterator.hasNext());
        identifier = (Identifier) iterator.next();
        Assert.assertTrue(identifier instanceof NamedIdentifier);
        Assert.assertEquals(Citations.EPSG, identifier.getAuthority());
        Assert.assertEquals("30591", identifier.getCode());
        Assert.assertEquals("EPSG:30591", identifier.toString());
    }
}
