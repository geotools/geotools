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
package org.geotools.referencing.wkt;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link Symbols} implementation.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class SymbolsTest {
    /**
     * Tests the {@link Symbols#containsAxis} method.
     */
    @Test
    public void testContainsAxis() {
        final Symbols s = Symbols.DEFAULT;
        assertTrue("AXIS at the begining of a line.",
                s.containsAxis("AXIS[\"Long\", EAST]"));
        assertTrue("AXIS embeded in GEOGCS.",
                s.containsAxis("GEOGCS[\"WGS84\", AXIS[\"Long\", EAST]]"));
        assertTrue("AXIS followed by spaces and different opening brace.",
                s.containsAxis("GEOGCS[\"WGS84\", AXIS (\"Long\", EAST)]"));
        assertTrue("AXIS in mixed cases.",
                s.containsAxis("GEOGCS[\"WGS84\", aXis[\"Long\", EAST]]"));
        assertFalse("AXIS in quoted text.",
                s.containsAxis("GEOGCS[\"AXIS\"]"));
        assertFalse("AXIS without opening bracket.",
                s.containsAxis("GEOGCS[\"WGS84\", AXIS]"));
        assertFalse("No AXIS.",
                s.containsAxis("GEOGCS[\"WGS84\"]"));
    }
}
