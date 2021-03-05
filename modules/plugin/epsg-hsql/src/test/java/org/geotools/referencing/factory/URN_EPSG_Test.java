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

import org.geotools.referencing.CRS;
import org.geotools.util.Version;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Tests the {@link org.geotools.referencing.factory.URN_AuthorityFactory} with EPSG codes.
 *
 * @version $Id$
 * @author Justin Deoliveira
 * @author Martin Desruisseaux
 */
public class URN_EPSG_Test {

    /** Tests {@link AuthorityFactoryAdapter#isCodeMethodOverriden}. */
    @Test
    public void testMethodOverriden() {
        final Versioned test = new Versioned();
        Assert.assertTrue(test.isCodeMethodOverriden());
    }

    /** Tests the 4326 code. */
    @Test
    public void test4326() throws FactoryException {
        CoordinateReferenceSystem expected = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem actual = CRS.decode("urn:ogc:def:crs:EPSG:6.8:4326");
        Assert.assertSame(expected, actual);
        actual = CRS.decode("urn:x-ogc:def:crs:EPSG:6.8:4326");
        Assert.assertSame(expected, actual);
        actual = CRS.decode("urn:ogc:def:crs:EPSG:6.11:4326");
        Assert.assertSame(expected, actual);
    }

    /** Tests versioning. */
    @Test
    public void testVersion() throws FactoryException {
        CRS.reset("all");

        CoordinateReferenceSystem expected = CRS.decode("EPSG:4326");
        final String version = String.valueOf(CRS.getVersion("EPSG"));
        final String urn = "urn:ogc:def:crs:EPSG:" + version + ":4326";
        final Versioned test = new Versioned();
        final int failureCount = FallbackAuthorityFactory.getFailureCount();
        Assert.assertNull(test.lastVersion);
        Assert.assertSame(expected, test.createCoordinateReferenceSystem(urn));
        Assert.assertEquals(version, test.lastVersion.toString());
        Assert.assertEquals(
                "Primary factory should not fail.",
                failureCount,
                FallbackAuthorityFactory.getFailureCount());

        test.lastVersion = null;
        Assert.assertSame(expected, test.createCoordinateReferenceSystem(urn));
        Assert.assertNull("Should not create a new factory.", test.lastVersion);
        Assert.assertEquals(
                "Primary factory should not fail.",
                failureCount,
                FallbackAuthorityFactory.getFailureCount());

        Assert.assertSame(
                expected, test.createCoordinateReferenceSystem("urn:ogc:def:crs:EPSG:6.11:4326"));
        Assert.assertEquals("6.11", test.lastVersion.toString());
        Assert.assertEquals(
                "Should use the fallback factory.",
                failureCount + 2,
                FallbackAuthorityFactory.getFailureCount());
    }

    /** A custom class for testing versioning. */
    private static final class Versioned extends URN_AuthorityFactory {
        static Version lastVersion;

        @Override
        protected AuthorityFactory createVersionedFactory(final Version version)
                throws FactoryException {
            lastVersion = version;
            return super.createVersionedFactory(version);
        }
    }
}
