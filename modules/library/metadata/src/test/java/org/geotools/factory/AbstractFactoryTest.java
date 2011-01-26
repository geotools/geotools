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
package org.geotools.factory;

import java.awt.RenderingHints;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link AbstractFactory}.
 *
 * @since 2.3
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class AbstractFactoryTest {
    /**
     * A key for testing purpose.
     */
    private static class Key extends RenderingHints.Key {
        Key(final int n) {
            super(n);
        }

        @Override
        public boolean isCompatibleValue(Object value) {
            return true;
        }

    }

    /**
     * Tests {@link AbstractFactory#equals}.
     */
    @Test
    public void testEquals() {
        final Key key1              = new Key(1 );
        final Key key2              = new Key(2 );
        final Key key3              = new Key(3 );
        final Key key3_reference_f1 = new Key(31);
        final Key key2_reference_f3 = new Key(23);
        final Key key1_reference_f2 = new Key(12);

        final AbstractFactory f1 = new AbstractFactory();
        final AbstractFactory f2 = new AbstractFactory();
        final AbstractFactory f3 = new AbstractFactory();
        f1.hints.put(key1,       "Value 1");
        f2.hints.put(key2,       "Value 2");
        f3.hints.put(key3_reference_f1, f1);
        f2.hints.put(key2_reference_f3, f3);
        f1.hints.put(key1_reference_f2, f2);

        assertFalse(f1.toString().length() == 0);

        assertEquals(f1, f1);
        assertEquals(f2, f2);
        assertEquals(f3, f3);
        assertFalse (f1.equals(f2));  // Different number of hints.
        assertFalse (f1.equals(f3));  // Same number of hints, differerent key.
        assertFalse (f2.equals(f3));  // Different number of hints.

        // Tests recursivity on a f2 --> f3 --> f1 --> f2 dependency graph.
        final AbstractFactory f1b = new AbstractFactory();
        final AbstractFactory f2b = new AbstractFactory();
        final AbstractFactory f3b = new AbstractFactory();
        f1b.hints.put(key1,        "Value 1");
        f2b.hints.put(key2,        "Value 2");
        f3b.hints.put(key3_reference_f1, f1b);
        f2b.hints.put(key2_reference_f3, f3b);
        f1b.hints.put(key1_reference_f2, f2b);
        assertEquals(f2, f2b);

        f1b.hints.put(key1, "Different value");
        assertFalse(f2.equals(f2b));
    }
}
