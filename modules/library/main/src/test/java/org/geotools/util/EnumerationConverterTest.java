package org.geotools.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class EnumerationConverterTest {
    
    public enum TestEnum { TestA, TestB };

    @Test
    public void testConversion() {
        Object result = Converters.convert("TestA", TestEnum.class);
        assertEquals(TestEnum.TestA, result);
    }
    
    @Test
    public void testMissingValue() {
        Object result = Converters.convert("TestC", TestEnum.class);
        assertNull(result);
    }
    
}
