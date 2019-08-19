package org.geotools.data.util;

import static org.junit.Assert.*;

import org.geotools.feature.NameImpl;
import org.geotools.util.Converters;
import org.junit.Test;
import org.opengis.feature.type.Name;

public class NameConverterTest {

    @Test
    public void testToName() {
        assertEquals(new NameImpl("test"), Converters.convert("test", Name.class));
        assertEquals(new NameImpl("a", "b"), Converters.convert("a:b", Name.class));
        assertNull(Converters.convert("a:b:c", Name.class));
    }

    @Test
    public void testNameToString() {
        assertEquals("test", Converters.convert(new NameImpl("test"), String.class));
        assertEquals("a:b", Converters.convert(new NameImpl("a", "b"), String.class));
    }
}
