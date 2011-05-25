/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link DisjointSet} class.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class DisjointSetTest {
     /**
      * Tests the set.
      */
    @Test
    public void testDisjointSet() {
        DisjointSet<String> t1 = new DisjointSet<String>(true);
        DisjointSet<String> t2 = new DisjointSet<String>(t1);
        DisjointSet<String> t3 = new DisjointSet<String>(t2);

        assertNotNull(t1.getTrash());
        assertSame(t1.getTrash(), t2.getTrash());
        assertSame(t2.getTrash(), t3.getTrash());

        assertTrue(t1.add("alpha"));
        assertTrue(t2.add("bêta"));
        assertTrue(t3.add("gamma"));
        assertTrue(t2.add("delta"));
        assertTrue(t1.add("epsilon"));
        assertTrue(t2.add("alpha"));
        assertTrue(t2.remove("bêta"));

        assertEquals(Collections.singleton("epsilon"), t1);
        assertEquals(new HashSet<String>(Arrays.asList(new String[] {"alpha","delta"})), t2);
        assertEquals(Collections.singleton("gamma"), t3);
        assertEquals(Collections.singleton("bêta"),  t1.getTrash());
    }
}
