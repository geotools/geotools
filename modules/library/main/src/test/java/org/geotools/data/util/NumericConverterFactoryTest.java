/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import junit.framework.TestCase;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

public class NumericConverterFactoryTest extends TestCase {

    NumericConverterFactory factory;

    protected void setUp() throws Exception {
        factory = new NumericConverterFactory();
    }

    public void testIntegral() throws Exception {
        // to byte
        assertEquals(Byte.valueOf((byte) 127), convert(Byte.valueOf((byte) 127), Byte.class));
        assertEquals(Byte.valueOf((byte) 127), convert(Short.valueOf((short) 127), Byte.class));
        assertEquals(Byte.valueOf((byte) 127), convert(Integer.valueOf(127), Byte.class));
        assertEquals(Byte.valueOf((byte) 127), convert(Long.valueOf(127), Byte.class));
        assertEquals(Byte.valueOf((byte) 127), convert(BigInteger.valueOf(127), Byte.class));

        // to short
        assertEquals(Short.valueOf((short) 127), convert(Byte.valueOf((byte) 127), Short.class));
        assertEquals(Short.valueOf((short) 127), convert(Short.valueOf((short) 127), Short.class));
        assertEquals(Short.valueOf((short) 127), convert(Integer.valueOf(127), Short.class));
        assertEquals(Short.valueOf((short) 127), convert(Long.valueOf(127), Short.class));
        assertEquals(Short.valueOf((short) 127), convert(BigInteger.valueOf(127), Short.class));

        // to integer
        assertEquals(Integer.valueOf(127), convert(Byte.valueOf((byte) 127), Integer.class));
        assertEquals(Integer.valueOf(127), convert(Short.valueOf((short) 127), Integer.class));
        assertEquals(Integer.valueOf(127), convert(Integer.valueOf(127), Integer.class));
        assertEquals(Integer.valueOf(127), convert(Long.valueOf(127), Integer.class));
        assertEquals(Integer.valueOf(127), convert(BigInteger.valueOf(127), Integer.class));

        // to long
        assertEquals(Long.valueOf(127), convert(Byte.valueOf((byte) 127), Long.class));
        assertEquals(Long.valueOf(127), convert(Short.valueOf((short) 127), Long.class));
        assertEquals(Long.valueOf(127), convert(Integer.valueOf(127), Long.class));
        assertEquals(Long.valueOf(127), convert(Long.valueOf(127), Long.class));
        assertEquals(Long.valueOf(127), convert(BigInteger.valueOf(127), Long.class));

        // to big integer
        assertEquals(BigInteger.valueOf(127), convert(Byte.valueOf((byte) 127), BigInteger.class));
        assertEquals(
                BigInteger.valueOf(127), convert(Short.valueOf((short) 127), BigInteger.class));
        assertEquals(BigInteger.valueOf(127), convert(Integer.valueOf(127), BigInteger.class));
        assertEquals(BigInteger.valueOf(127), convert(Long.valueOf(127), BigInteger.class));
        assertEquals(BigInteger.valueOf(127), convert(BigInteger.valueOf(127), BigInteger.class));
    }

    public void testFloat() throws Exception {
        // to float
        assertEquals(Float.valueOf(127.127f), convert(Float.valueOf(127.127f), Float.class));
        assertEquals(Float.valueOf(127.127f), convert(Double.valueOf(127.127), Float.class));
        assertEquals(Float.valueOf(127.127f), convert(new BigDecimal(127.127), Float.class));

        // to double
        assertEquals(
                Double.valueOf(127.127).doubleValue(),
                ((Double) convert(Float.valueOf(127.127f), Double.class)).doubleValue(),
                1e-10);
        assertEquals(Double.valueOf(127.127), convert(Double.valueOf(127.127), Double.class));
        assertEquals(Double.valueOf(127.127), convert(new BigDecimal(127.127), Double.class));

        // to big decimal
        assertEquals(
                new BigDecimal(127.127).doubleValue(),
                ((BigDecimal) convert(Float.valueOf(127.127f), BigDecimal.class)).doubleValue(),
                1e-10);
        assertEquals(new BigDecimal("127.127"), convert(Double.valueOf(127.127), BigDecimal.class));
        assertEquals(new BigDecimal(127.127), convert(new BigDecimal(127.127), BigDecimal.class));
    }

    public void testIntegralToFloat() throws Exception {
        assertEquals(Float.valueOf(127f), convert(Byte.valueOf((byte) 127), Float.class));
        assertEquals(Float.valueOf(127f), convert(Short.valueOf((short) 127), Float.class));
        assertEquals(Float.valueOf(127f), convert(Integer.valueOf(127), Float.class));
        assertEquals(Float.valueOf(127f), convert(Long.valueOf(127), Float.class));
        assertEquals(Float.valueOf(127f), convert(BigInteger.valueOf(127), Float.class));

        assertEquals(Double.valueOf(127.0), convert(Byte.valueOf((byte) 127), Double.class));
        assertEquals(Double.valueOf(127.0), convert(Short.valueOf((short) 127), Double.class));
        assertEquals(Double.valueOf(127.0), convert(Integer.valueOf(127), Double.class));
        assertEquals(Double.valueOf(127.0), convert(Long.valueOf(127), Double.class));
        assertEquals(Double.valueOf(127.0), convert(BigInteger.valueOf(127), Double.class));

        assertEquals(new BigDecimal(127.0), convert(Byte.valueOf((byte) 127), BigDecimal.class));
        assertEquals(new BigDecimal(127.0), convert(Short.valueOf((short) 127), BigDecimal.class));
        assertEquals(new BigDecimal(127.0), convert(Integer.valueOf(127), BigDecimal.class));
        assertEquals(new BigDecimal(127.0), convert(Long.valueOf(127), BigDecimal.class));
        assertEquals(new BigDecimal(127.0), convert(BigInteger.valueOf(127), BigDecimal.class));
    }

    public void testFloatToIntegral() throws Exception {
        // to byte
        assertEquals(Byte.valueOf((byte) 127), convert(Float.valueOf(127.127f), Byte.class));
        assertEquals(Byte.valueOf((byte) 127), convert(Double.valueOf(127.127), Byte.class));
        assertEquals(Byte.valueOf((byte) 127), convert(new BigDecimal(127.127), Byte.class));

        // to short
        assertEquals(Short.valueOf((short) 127), convert(Float.valueOf(127.127f), Short.class));
        assertEquals(Short.valueOf((short) 127), convert(Double.valueOf(127.127), Short.class));
        assertEquals(Short.valueOf((short) 127), convert(new BigDecimal(127.127), Short.class));

        // to integer
        assertEquals(Integer.valueOf(127), convert(Float.valueOf(127.127f), Integer.class));
        assertEquals(Integer.valueOf(127), convert(Double.valueOf(127.127), Integer.class));
        assertEquals(Integer.valueOf(127), convert(new BigDecimal(127.127), Integer.class));

        // to long
        assertEquals(Long.valueOf(127), convert(Float.valueOf(127.127f), Long.class));
        assertEquals(Long.valueOf(127), convert(Double.valueOf(127.127), Long.class));
        assertEquals(Long.valueOf(127), convert(new BigDecimal(127.127), Long.class));

        // to big integer
        assertEquals(BigInteger.valueOf(127), convert(Float.valueOf(127.127f), BigInteger.class));
        assertEquals(BigInteger.valueOf(127), convert(Double.valueOf(127.127), BigInteger.class));
        assertEquals(BigInteger.valueOf(127), convert(new BigDecimal(127.127), BigInteger.class));
    }

    public void testStringToInteger() throws Exception {
        assertEquals(Integer.valueOf(127), convert("127", Integer.class));
        assertEquals(Integer.valueOf(127), convert(" 127 ", Integer.class));
        assertEquals(Integer.valueOf(3), convert(" 3.0 ", Integer.class));
        assertEquals(Integer.valueOf(-3), convert("-3.0 ", Integer.class));
        assertEquals(Integer.valueOf(3000), convert("3000.0 ", Integer.class));
        assertEquals(Integer.valueOf(3000), convert("3000,0 ", Integer.class));
    }

    public void testStringToDouble() throws Exception {
        assertEquals(Double.valueOf(4.4), convert("4.4", Double.class));
        assertEquals(Double.valueOf(127), convert("127", Double.class));
        assertEquals(Double.valueOf(127), convert(" 127 ", Double.class));
        assertEquals(Double.valueOf(3), convert(" 3.0 ", Double.class));
        assertEquals(Double.valueOf(-3), convert("-3.0 ", Double.class));
        assertEquals(Double.valueOf(3000), convert("3000.0 ", Double.class));
    }

    public void testStringToNumber() throws Exception {
        assertEquals(Double.valueOf(4.4), convert("4.4", Number.class));
    }

    Object convert(Object source, Class target) throws Exception {
        return factory.createConverter(source.getClass(), target, null).convert(source, target);
    }

    public static void testIntegralHandling() {
        assertEquals("3", NumericConverterFactory.toIntegral("3"));
        assertEquals("3", NumericConverterFactory.toIntegral("3.0"));
        assertEquals("-3", NumericConverterFactory.toIntegral("-3"));
        assertEquals("-3", NumericConverterFactory.toIntegral("-3.0"));
        assertEquals("3000", NumericConverterFactory.toIntegral("3000.0"));
        assertEquals("3000", NumericConverterFactory.toIntegral("3000,0"));
    }

    Object convertSafe(Object source, Class<?> target) throws Exception {
        Hints hints = new Hints();
        hints.put(ConverterFactory.SAFE_CONVERSION, Boolean.valueOf(true));
        return factory.createConverter(source.getClass(), target, hints).convert(source, target);
    }

    public void testSafeConversion() throws Exception {
        // byte
        assertEquals(Byte.valueOf((byte) 127), convertSafe(Byte.valueOf((byte) 127), Byte.class));
        assertNull(convertSafe(Short.valueOf((short) 128), Byte.class));
        assertNull(convertSafe(Integer.valueOf(128), Byte.class));
        assertNull(convertSafe(Long.valueOf(128), Byte.class));
        assertNull(convertSafe(BigInteger.valueOf(128), Byte.class));
        assertNull(convertSafe(Double.valueOf(128.1), Byte.class));
        assertNull(convertSafe(new BigDecimal(128.1), Byte.class));
        assertNull(convertSafe(Float.valueOf(128.1f), Byte.class));

        // short
        assertEquals(
                Short.valueOf((short) 127), convertSafe(Byte.valueOf((byte) 127), Short.class));
        assertEquals(
                Short.valueOf((short) 1111), convertSafe(Short.valueOf((short) 1111), Short.class));
        assertNull(convertSafe(Integer.valueOf(128), Short.class));
        assertNull(convertSafe(Long.valueOf(128), Short.class));
        assertNull(convertSafe(BigInteger.valueOf(128), Short.class));
        assertNull(convertSafe(Double.valueOf(128.1), Short.class));
        assertNull(convertSafe(new BigDecimal(128.1), Short.class));
        assertNull(convertSafe(Float.valueOf(128.1f), Short.class));

        // integer
        assertEquals(Integer.valueOf(127), convertSafe(Byte.valueOf((byte) 127), Integer.class));
        assertEquals(
                Integer.valueOf(1111), convertSafe(Short.valueOf((short) 1111), Integer.class));
        assertEquals(Integer.valueOf(12345), convertSafe(Integer.valueOf(12345), Integer.class));
        assertNull(convertSafe(Long.valueOf(128), Integer.class));
        assertNull(convertSafe(BigInteger.valueOf(128), Integer.class));
        assertNull(convertSafe(Double.valueOf(128.1), Integer.class));
        assertNull(convertSafe(new BigDecimal(128.1), Integer.class));
        assertNull(convertSafe(Float.valueOf(128.1f), Integer.class));

        // long
        assertEquals(Long.valueOf(127), convertSafe(Byte.valueOf((byte) 127), Long.class));
        assertEquals(Long.valueOf(1111), convertSafe(Short.valueOf((short) 1111), Long.class));
        assertEquals(Long.valueOf(12345), convertSafe(Integer.valueOf(12345), Long.class));
        assertEquals(Long.valueOf(1234567), convertSafe(Integer.valueOf(1234567), Long.class));
        assertNull(convertSafe(BigInteger.valueOf(128), Long.class));
        assertNull(convertSafe(Double.valueOf(128.1), Long.class));
        assertNull(convertSafe(new BigDecimal(128.1), Long.class));
        assertNull(convertSafe(Float.valueOf(128.1f), Long.class));

        // big integer
        assertEquals(
                BigInteger.valueOf(127), convertSafe(Byte.valueOf((byte) 127), BigInteger.class));
        assertEquals(
                BigInteger.valueOf(1111),
                convertSafe(Short.valueOf((short) 1111), BigInteger.class));
        assertEquals(
                BigInteger.valueOf(12345), convertSafe(Integer.valueOf(12345), BigInteger.class));
        assertEquals(
                BigInteger.valueOf(1234567),
                convertSafe(Integer.valueOf(1234567), BigInteger.class));
        assertEquals(
                BigInteger.valueOf(12345678),
                convertSafe(BigInteger.valueOf(12345678), BigInteger.class));
        assertNull(convertSafe(Double.valueOf(128.1), Long.class));
        assertNull(convertSafe(new BigDecimal(128.1), Long.class));
        assertNull(convertSafe(Float.valueOf(128.1f), Long.class));

        // double
        assertEquals(Double.valueOf(127), convertSafe(Byte.valueOf((byte) 127), Double.class));
        assertEquals(Double.valueOf(1111), convertSafe(Short.valueOf((short) 1111), Double.class));
        assertEquals(Double.valueOf(12345), convertSafe(Integer.valueOf(12345), Double.class));
        assertEquals(Double.valueOf(1234567), convertSafe(Integer.valueOf(1234567), Double.class));
        // assertEquals(Double.valueOf(12345678), convertSafe(BigInteger.valueOf(12345678),
        // Double.class));
        assertEquals(
                Double.valueOf(12.123456), convertSafe(Double.valueOf(12.123456), Double.class));
        assertNull(convertSafe(new BigDecimal(128.1), Long.class));
        assertEquals(Double.valueOf(12.12), convertSafe(Float.valueOf(12.12f), Double.class));

        // float
        assertEquals(Float.valueOf(127), convertSafe(Byte.valueOf((byte) 127), Float.class));
        assertEquals(Float.valueOf(1111), convertSafe(Short.valueOf((short) 1111), Float.class));
        assertEquals(Float.valueOf(12345), convertSafe(Integer.valueOf(12345), Float.class));
        assertEquals(Float.valueOf(1234567), convertSafe(Integer.valueOf(1234567), Float.class));
        assertNull(convertSafe(BigInteger.valueOf(12345678), Float.class));
        assertNull(convertSafe(Double.valueOf(128.1), Float.class));
        assertNull(convertSafe(new BigDecimal(128.1), Float.class));
        assertEquals(Float.valueOf(12.12f), convertSafe(Float.valueOf(12.12f), Float.class));

        // Big Decimal
        assertEquals(new BigDecimal(127), convertSafe(Byte.valueOf((byte) 127), BigDecimal.class));
        assertEquals(
                new BigDecimal(1111), convertSafe(Short.valueOf((short) 1111), BigDecimal.class));
        assertEquals(new BigDecimal(12345), convertSafe(Integer.valueOf(12345), BigDecimal.class));
        assertEquals(
                new BigDecimal(1234567), convertSafe(Integer.valueOf(1234567), BigDecimal.class));
        assertEquals(
                new BigDecimal(12345678),
                convertSafe(BigInteger.valueOf(12345678), BigDecimal.class));
        assertEquals(
                new BigDecimal((Double.valueOf(12.123456)).toString()),
                convertSafe(Double.valueOf(12.123456), BigDecimal.class));
        assertEquals(new BigDecimal(128.1), convertSafe(new BigDecimal(128.1), BigDecimal.class));
        assertEquals(
                new BigDecimal((Float.valueOf(12.12f)).toString()),
                convertSafe(Float.valueOf(12.12f), BigDecimal.class));

        // test strings
        assertEquals(new BigDecimal(127), convertSafe("127", BigDecimal.class));
        assertNull(convertSafe("127f", BigDecimal.class));
        assertEquals(Double.valueOf(127.123), convertSafe("127.123", Double.class));
        assertNull(convertSafe("123.456.456", Double.class));
        assertEquals(Float.valueOf(127.123f), convertSafe("127.123", Float.class));
        assertNull(convertSafe("123.456.456", Float.class));
        assertEquals(BigInteger.valueOf(1234567), convertSafe("1234567", BigInteger.class));
        assertNull(convertSafe("123.456", BigInteger.class));
        assertEquals(Long.valueOf(54), convertSafe("54", Long.class));
        assertNull(convertSafe("123.6", Long.class));
        assertEquals(Integer.valueOf(54), convertSafe("54", Integer.class));
        assertNull(convertSafe("123.6", Integer.class));
        assertEquals(Short.valueOf((short) 54), convertSafe("54", Short.class));
        assertNull(convertSafe("123.6", Short.class));
        assertEquals(Byte.valueOf("1"), convertSafe("1", Byte.class));
        assertNull(convertSafe("123.6", Byte.class));
    }

    public void testPrimitiveTypes() throws Exception {
        assertEquals(1, convert(Integer.valueOf(1), int.class));
        assertEquals(Integer.valueOf(1), convert(Integer.valueOf(1), int.class));
        assertEquals(1, convert(1, Integer.class));
        assertEquals(Integer.valueOf(1), convert(1, Integer.class));

        assertEquals(1, convert("1", int.class));
        assertEquals(Integer.valueOf(1), convert("1", int.class));
        assertEquals(1, convert("1", Integer.class));
        assertEquals(Integer.valueOf(1), convert("1", Integer.class));
    }
}
