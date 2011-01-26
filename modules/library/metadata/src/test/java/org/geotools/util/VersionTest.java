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
package org.geotools.util;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link Version} class, especially the {@code compareTo} method.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class VersionTest {
    /**
     * Tests a numeric-only version.
     */
    @Test
    public void testNumeric() {
        final Version version = new Version("6.11.2");
        assertEquals("6.11.2", version.toString());
        assertEquals( 6, version.getMajor());
        assertEquals(11, version.getMinor());
        assertEquals( 2, version.getRevision());
        assertSame(version.getRevision(), version.getComponent(2));
        assertNull(version.getComponent(3));

        assertTrue(version.compareTo(new Version("6.11.2")) == 0);
        assertTrue(version.compareTo(new Version("6.8"   )) >  0);
        assertTrue(version.compareTo(new Version("6.12.0")) <  0);
        assertTrue(version.compareTo(new Version("6.11"  )) >  0);
    }

    /**
     * Tests a alpha-numeric version.
     */
    @Test
    public void testAlphaNumeric() {
        final Version version = new Version("1.6.b2");
        assertEquals("1.6.b2", version.toString());
        assertEquals( 1, version.getMajor());
        assertEquals( 6, version.getMinor());
        assertEquals("b2", version.getRevision());
        assertSame(version.getRevision(), version.getComponent(2));
        assertNull(version.getComponent(3));

        assertTrue(version.compareTo(new Version("1.6.b2")) == 0);
        assertTrue(version.compareTo(new Version("1.6.b1"))  > 0);
        assertTrue(version.compareTo(new Version("1.07.b1")) < 0);
    }
    
    @Test
    public void testSNAPSHOT() {
        final Version version = new Version("2.6-SNAPSHOT");
        assertEquals(2, version.getMajor());
        assertEquals(6, version.getMinor());
        
        assertEquals("SNAPSHOT", version.getRevision());
    }
    
}
