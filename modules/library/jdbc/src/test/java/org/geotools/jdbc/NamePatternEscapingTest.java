/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NamePatternEscapingTest {
    @Test
    public void escapeWithBackslash() throws Exception {
        NamePatternEscaping escaping = new NamePatternEscaping("\\");
        assertNull(escaping.escape(null));
        assertEquals("TABLE", escaping.escape("TABLE"));
        assertEquals("\\\\TABLE", escaping.escape("\\TABLE"));
        assertEquals("/TABLE", escaping.escape("/TABLE"));
        assertEquals("TABLE\\_NAME", escaping.escape("TABLE_NAME"));
        assertEquals("\\\\TABLE\\_\\%NAME", escaping.escape("\\TABLE_%NAME"));
        assertEquals("/TABLE\\_\\%NAME", escaping.escape("/TABLE_%NAME"));
    }

    @Test
    public void escapeWithSlash() throws Exception {
        NamePatternEscaping escaping = new NamePatternEscaping("/");
        assertNull(escaping.escape(null));
        assertEquals("TABLE", escaping.escape("TABLE"));
        assertEquals("\\TABLE", escaping.escape("\\TABLE"));
        assertEquals("//TABLE", escaping.escape("/TABLE"));
        assertEquals("TABLE/_NAME", escaping.escape("TABLE_NAME"));
        assertEquals("\\TABLE/_/%NAME", escaping.escape("\\TABLE_%NAME"));
        assertEquals("//TABLE/_/%NAME", escaping.escape("/TABLE_%NAME"));
    }
}
