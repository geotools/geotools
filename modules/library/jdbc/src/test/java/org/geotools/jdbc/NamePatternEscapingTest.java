package org.geotools.jdbc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
