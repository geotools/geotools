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

// JUnit dependencies
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

// OpenGIS dependencies
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

// Geotools dependencies
import org.geotools.util.Version;
import org.geotools.referencing.CRS;


/**
 * Tests the {@link org.geotools.referencing.factory.URN_AuthorityFactory} with EPSG codes.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Justin Deoliveira
 * @author Martin Desruisseaux
 */
public class URN_EPSG_Test extends TestCase {
    /**
     * Run the suite from the command line.
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Returns the test suite.
     */
    public static Test suite() {
        return new TestSuite(URN_EPSG_Test.class);
    }

    /**
     * Creates a suite of the given name.
     */
    public URN_EPSG_Test(final String name) {
        super(name);
    }

    /**
     * Tests {@link AuthorityFactoryAdapter#isCodeMethodOverriden}.
     */
    public void testMethodOverriden() {
        final Versioned test = new Versioned();
        assertTrue(test.isCodeMethodOverriden());
    }

    /**
     * Tests the 4326 code.
     */
    public void test4326() throws FactoryException {
        CoordinateReferenceSystem expected = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem actual   = CRS.decode("urn:ogc:def:crs:EPSG:6.8:4326");
        assertSame(expected, actual);
        actual = CRS.decode("urn:x-ogc:def:crs:EPSG:6.8:4326");
        assertSame(expected, actual);
        actual = CRS.decode("urn:ogc:def:crs:EPSG:6.11:4326");
        assertSame(expected, actual);
    }

    /**
     * Tests versioning.
     */
    public void testVersion() throws FactoryException {
        CoordinateReferenceSystem expected = CRS.decode("EPSG:4326");
        final String version = String.valueOf(CRS.getVersion("EPSG"));
        final String urn = "urn:ogc:def:crs:EPSG:" + version + ":4326";
        final Versioned test = new Versioned();
        final int failureCount = FallbackAuthorityFactory.getFailureCount();
        assertNull(test.lastVersion);
        assertSame(expected, test.createCoordinateReferenceSystem(urn));
        assertEquals(version, test.lastVersion.toString());
        assertEquals("Primary factory should not fail.",
                failureCount, FallbackAuthorityFactory.getFailureCount());

        test.lastVersion = null;
        assertSame(expected, test.createCoordinateReferenceSystem(urn));
        assertNull("Should not create a new factory.", test.lastVersion);
        assertEquals("Primary factory should not fail.",
                failureCount, FallbackAuthorityFactory.getFailureCount());

        assertSame(expected, test.createCoordinateReferenceSystem("urn:ogc:def:crs:EPSG:6.11:4326"));
        assertEquals("6.11", test.lastVersion.toString());
        assertEquals("Should use the fallback factory.",
                failureCount + 1, FallbackAuthorityFactory.getFailureCount());
    }

    /**
     * A custom class for testing versioning.
     */
    private static final class Versioned extends URN_AuthorityFactory {
        static Version lastVersion;

        protected AuthorityFactory createVersionedFactory(final Version version)
                throws FactoryException
        {
            lastVersion = version;
            return super.createVersionedFactory(version);
        }
    }
}
