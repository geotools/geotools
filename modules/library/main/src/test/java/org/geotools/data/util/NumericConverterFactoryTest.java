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
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NumericConverterFactoryTest {

    NumericConverterFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new NumericConverterFactory();
    }

    @Test
    public void testIntegral() throws Exception {
        // to byte
        Assert.assertEquals(
                Byte.valueOf((byte) 127), convert(Byte.valueOf((byte) 127), Byte.class));
        Assert.assertEquals(
                Byte.valueOf((byte) 127), convert(Short.valueOf((short) 127), Byte.class));
        Assert.assertEquals(Byte.valueOf((byte) 127), convert(Integer.valueOf(127), Byte.class));
        Assert.assertEquals(Byte.valueOf((byte) 127), convert(Long.valueOf(127), Byte.class));
        Assert.assertEquals(Byte.valueOf((byte) 127), convert(BigInteger.valueOf(127), Byte.class));

        // to short
        Assert.assertEquals(
                Short.valueOf((short) 127), convert(Byte.valueOf((byte) 127), Short.class));
        Assert.assertEquals(
                Short.valueOf((short) 127), convert(Short.valueOf((short) 127), Short.class));
        Assert.assertEquals(Short.valueOf((short) 127), convert(Integer.valueOf(127), Short.class));
        Assert.assertEquals(Short.valueOf((short) 127), convert(Long.valueOf(127), Short.class));
        Assert.assertEquals(
                Short.valueOf((short) 127), convert(BigInteger.valueOf(127), Short.class));

        // to integer
        Assert.assertEquals(Integer.valueOf(127), convert(Byte.valueOf((byte) 127), Integer.class));
        Assert.assertEquals(
                Integer.valueOf(127), convert(Short.valueOf((short) 127), Integer.class));
        Assert.assertEquals(Integer.valueOf(127), convert(Integer.valueOf(127), Integer.class));
        Assert.assertEquals(Integer.valueOf(127), convert(Long.valueOf(127), Integer.class));
        Assert.assertEquals(Integer.valueOf(127), convert(BigInteger.valueOf(127), Integer.class));

        // to long
        Assert.assertEquals(Long.valueOf(127), convert(Byte.valueOf((byte) 127), Long.class));
        Assert.assertEquals(Long.valueOf(127), convert(Short.valueOf((short) 127), Long.class));
        Assert.assertEquals(Long.valueOf(127), convert(Integer.valueOf(127), Long.class));
        Assert.assertEquals(Long.valueOf(127), convert(Long.valueOf(127), Long.class));
        Assert.assertEquals(Long.valueOf(127), convert(BigInteger.valueOf(127), Long.class));

        // to big integer
        Assert.assertEquals(
                BigInteger.valueOf(127), convert(Byte.valueOf((byte) 127), BigInteger.class));
        Assert.assertEquals(
                BigInteger.valueOf(127), convert(Short.valueOf((short) 127), BigInteger.class));
        Assert.assertEquals(
                BigInteger.valueOf(127), convert(Integer.valueOf(127), BigInteger.class));
        Assert.assertEquals(BigInteger.valueOf(127), convert(Long.valueOf(127), BigInteger.class));
        Assert.assertEquals(
                BigInteger.valueOf(127), convert(BigInteger.valueOf(127), BigInteger.class));
    }

    @Test
    public void testFloat() throws Exception {
        // to float
        Assert.assertEquals(Float.valueOf(127.127f), convert(Float.valueOf(127.127f), Float.class));
        Assert.assertEquals(Float.valueOf(127.127f), convert(Double.valueOf(127.127), Float.class));
        Assert.assertEquals(
                Float.valueOf(127.127f), convert(BigDecimal.valueOf(127.127), Float.class));

        // to double
        Assert.assertEquals(
                127.127,
                ((Double) convert(Float.valueOf(127.127f), Double.class)).doubleValue(),
                1e-10);
        Assert.assertEquals(127.127, convert(Double.valueOf(127.127), Double.class));
        Assert.assertEquals(127.127, convert(BigDecimal.valueOf(127.127), Double.class));

        // to big decimal
        Assert.assertEquals(
                BigDecimal.valueOf(127.127).doubleValue(),
                ((BigDecimal) convert(Float.valueOf(127.127f), BigDecimal.class)).doubleValue(),
                1e-10);
        Assert.assertEquals(
                BigDecimal.valueOf(127.127), convert(Double.valueOf(127.127), BigDecimal.class));
        Assert.assertEquals(
                BigDecimal.valueOf(127.127),
                convert(BigDecimal.valueOf(127.127), BigDecimal.class));
    }

    @Test
    public void testIntegralToFloat() throws Exception {
        Assert.assertEquals(Float.valueOf(127f), convert(Byte.valueOf((byte) 127), Float.class));
        Assert.assertEquals(Float.valueOf(127f), convert(Short.valueOf((short) 127), Float.class));
        Assert.assertEquals(Float.valueOf(127f), convert(Integer.valueOf(127), Float.class));
        Assert.assertEquals(Float.valueOf(127f), convert(Long.valueOf(127), Float.class));
        Assert.assertEquals(Float.valueOf(127f), convert(BigInteger.valueOf(127), Float.class));

        Assert.assertEquals(Double.valueOf(127.0), convert(Byte.valueOf((byte) 127), Double.class));
        Assert.assertEquals(
                Double.valueOf(127.0), convert(Short.valueOf((short) 127), Double.class));
        Assert.assertEquals(Double.valueOf(127.0), convert(Integer.valueOf(127), Double.class));
        Assert.assertEquals(Double.valueOf(127.0), convert(Long.valueOf(127), Double.class));
        Assert.assertEquals(Double.valueOf(127.0), convert(BigInteger.valueOf(127), Double.class));

        Assert.assertEquals(
                BigDecimal.valueOf(127), convert(Byte.valueOf((byte) 127), BigDecimal.class));
        Assert.assertEquals(
                BigDecimal.valueOf(127), convert(Short.valueOf((short) 127), BigDecimal.class));
        Assert.assertEquals(
                BigDecimal.valueOf(127), convert(Integer.valueOf(127), BigDecimal.class));
        Assert.assertEquals(BigDecimal.valueOf(127), convert(Long.valueOf(127), BigDecimal.class));
        Assert.assertEquals(
                BigDecimal.valueOf(127), convert(BigInteger.valueOf(127), BigDecimal.class));
    }

    @Test
    public void testFloatToIntegral() throws Exception {
        // to byte
        Assert.assertEquals(Byte.valueOf((byte) 127), convert(Float.valueOf(127.127f), Byte.class));
        Assert.assertEquals(Byte.valueOf((byte) 127), convert(Double.valueOf(127.127), Byte.class));
        Assert.assertEquals(
                Byte.valueOf((byte) 127), convert(BigDecimal.valueOf(127.127), Byte.class));

        // to short
        Assert.assertEquals(
                Short.valueOf((short) 127), convert(Float.valueOf(127.127f), Short.class));
        Assert.assertEquals(
                Short.valueOf((short) 127), convert(Double.valueOf(127.127), Short.class));
        Assert.assertEquals(
                Short.valueOf((short) 127), convert(BigDecimal.valueOf(127.127), Short.class));

        // to integer
        Assert.assertEquals(Integer.valueOf(127), convert(Float.valueOf(127.127f), Integer.class));
        Assert.assertEquals(Integer.valueOf(127), convert(Double.valueOf(127.127), Integer.class));
        Assert.assertEquals(
                Integer.valueOf(127), convert(BigDecimal.valueOf(127.127), Integer.class));

        // to long
        Assert.assertEquals(Long.valueOf(127), convert(Float.valueOf(127.127f), Long.class));
        Assert.assertEquals(Long.valueOf(127), convert(Double.valueOf(127.127), Long.class));
        Assert.assertEquals(Long.valueOf(127), convert(BigDecimal.valueOf(127.127), Long.class));

        // to big integer
        Assert.assertEquals(
                BigInteger.valueOf(127), convert(Float.valueOf(127.127f), BigInteger.class));
        Assert.assertEquals(
                BigInteger.valueOf(127), convert(Double.valueOf(127.127), BigInteger.class));
        Assert.assertEquals(
                BigInteger.valueOf(127), convert(BigDecimal.valueOf(127.127), BigInteger.class));
    }

    @Test
    public void testStringToInteger() throws Exception {
        Assert.assertEquals(Integer.valueOf(127), convert("127", Integer.class));
        Assert.assertEquals(Integer.valueOf(127), convert(" 127 ", Integer.class));
        Assert.assertEquals(Integer.valueOf(3), convert(" 3.0 ", Integer.class));
        Assert.assertEquals(Integer.valueOf(-3), convert("-3.0 ", Integer.class));
        Assert.assertEquals(Integer.valueOf(3000), convert("3000.0 ", Integer.class));
        Assert.assertEquals(Integer.valueOf(3000), convert("3000,0 ", Integer.class));
    }

    @Test
    public void testStringToDouble() throws Exception {
        Assert.assertEquals(Double.valueOf(4.4), convert("4.4", Double.class));
        Assert.assertEquals(Double.valueOf(127), convert("127", Double.class));
        Assert.assertEquals(Double.valueOf(127), convert(" 127 ", Double.class));
        Assert.assertEquals(Double.valueOf(3), convert(" 3.0 ", Double.class));
        Assert.assertEquals(Double.valueOf(-3), convert("-3.0 ", Double.class));
        Assert.assertEquals(Double.valueOf(3000), convert("3000.0 ", Double.class));
    }

    @Test
    public void testStringToNumber() throws Exception {
        Assert.assertEquals(Double.valueOf(4.4), convert("4.4", Number.class));
    }

    Object convert(Object source, Class<?> target) throws Exception {
        return factory.createConverter(source.getClass(), target, null).convert(source, target);
    }

    @Test
    public void testIntegralHandling() {
        Assert.assertEquals("3", NumericConverterFactory.toIntegral("3"));
        Assert.assertEquals("3", NumericConverterFactory.toIntegral("3.0"));
        Assert.assertEquals("-3", NumericConverterFactory.toIntegral("-3"));
        Assert.assertEquals("-3", NumericConverterFactory.toIntegral("-3.0"));
        Assert.assertEquals("3000", NumericConverterFactory.toIntegral("3000.0"));
        Assert.assertEquals("3000", NumericConverterFactory.toIntegral("3000,0"));
    }

    Object convertSafe(Object source, Class<?> target) throws Exception {
        Hints hints = new Hints();
        hints.put(ConverterFactory.SAFE_CONVERSION, Boolean.TRUE);
        return factory.createConverter(source.getClass(), target, hints).convert(source, target);
    }

    @Test
    public void testSafeConversion() throws Exception {
        // byte
        Assert.assertEquals(
                Byte.valueOf((byte) 127), convertSafe(Byte.valueOf((byte) 127), Byte.class));
        Assert.assertNull(convertSafe(Short.valueOf((short) 128), Byte.class));
        Assert.assertNull(convertSafe(Integer.valueOf(128), Byte.class));
        Assert.assertNull(convertSafe(Long.valueOf(128), Byte.class));
        Assert.assertNull(convertSafe(BigInteger.valueOf(128), Byte.class));
        Assert.assertNull(convertSafe(Double.valueOf(128.1), Byte.class));
        Assert.assertNull(convertSafe(BigDecimal.valueOf(128.1), Byte.class));
        Assert.assertNull(convertSafe(Float.valueOf(128.1f), Byte.class));

        // short
        Assert.assertEquals(
                Short.valueOf((short) 127), convertSafe(Byte.valueOf((byte) 127), Short.class));
        Assert.assertEquals(
                Short.valueOf((short) 1111), convertSafe(Short.valueOf((short) 1111), Short.class));
        Assert.assertNull(convertSafe(Integer.valueOf(128), Short.class));
        Assert.assertNull(convertSafe(Long.valueOf(128), Short.class));
        Assert.assertNull(convertSafe(BigInteger.valueOf(128), Short.class));
        Assert.assertNull(convertSafe(Double.valueOf(128.1), Short.class));
        Assert.assertNull(convertSafe(BigDecimal.valueOf(128.1), Short.class));
        Assert.assertNull(convertSafe(Float.valueOf(128.1f), Short.class));

        // integer
        Assert.assertEquals(
                Integer.valueOf(127), convertSafe(Byte.valueOf((byte) 127), Integer.class));
        Assert.assertEquals(
                Integer.valueOf(1111), convertSafe(Short.valueOf((short) 1111), Integer.class));
        Assert.assertEquals(
                Integer.valueOf(12345), convertSafe(Integer.valueOf(12345), Integer.class));
        Assert.assertNull(convertSafe(Long.valueOf(128), Integer.class));
        Assert.assertNull(convertSafe(BigInteger.valueOf(128), Integer.class));
        Assert.assertNull(convertSafe(Double.valueOf(128.1), Integer.class));
        Assert.assertNull(convertSafe(BigDecimal.valueOf(128.1), Integer.class));
        Assert.assertNull(convertSafe(Float.valueOf(128.1f), Integer.class));

        // long
        Assert.assertEquals(Long.valueOf(127), convertSafe(Byte.valueOf((byte) 127), Long.class));
        Assert.assertEquals(
                Long.valueOf(1111), convertSafe(Short.valueOf((short) 1111), Long.class));
        Assert.assertEquals(Long.valueOf(12345), convertSafe(Integer.valueOf(12345), Long.class));
        Assert.assertEquals(
                Long.valueOf(1234567), convertSafe(Integer.valueOf(1234567), Long.class));
        Assert.assertNull(convertSafe(BigInteger.valueOf(128), Long.class));
        Assert.assertNull(convertSafe(Double.valueOf(128.1), Long.class));
        Assert.assertNull(convertSafe(BigDecimal.valueOf(128.1), Long.class));
        Assert.assertNull(convertSafe(Float.valueOf(128.1f), Long.class));

        // big integer
        Assert.assertEquals(
                BigInteger.valueOf(127), convertSafe(Byte.valueOf((byte) 127), BigInteger.class));
        Assert.assertEquals(
                BigInteger.valueOf(1111),
                convertSafe(Short.valueOf((short) 1111), BigInteger.class));
        Assert.assertEquals(
                BigInteger.valueOf(12345), convertSafe(Integer.valueOf(12345), BigInteger.class));
        Assert.assertEquals(
                BigInteger.valueOf(1234567),
                convertSafe(Integer.valueOf(1234567), BigInteger.class));
        Assert.assertEquals(
                BigInteger.valueOf(12345678),
                convertSafe(BigInteger.valueOf(12345678), BigInteger.class));
        Assert.assertNull(convertSafe(Double.valueOf(128.1), Long.class));
        Assert.assertNull(convertSafe(BigDecimal.valueOf(128.1), Long.class));
        Assert.assertNull(convertSafe(Float.valueOf(128.1f), Long.class));

        // double
        Assert.assertEquals(
                Double.valueOf(127), convertSafe(Byte.valueOf((byte) 127), Double.class));
        Assert.assertEquals(
                Double.valueOf(1111), convertSafe(Short.valueOf((short) 1111), Double.class));
        Assert.assertEquals(
                Double.valueOf(12345), convertSafe(Integer.valueOf(12345), Double.class));
        Assert.assertEquals(
                Double.valueOf(1234567), convertSafe(Integer.valueOf(1234567), Double.class));
        // assertEquals(Double.valueOf(12345678), convertSafe(BigInteger.valueOf(12345678),
        // Double.class));
        Assert.assertEquals(
                Double.valueOf(12.123456), convertSafe(Double.valueOf(12.123456), Double.class));
        Assert.assertNull(convertSafe(BigDecimal.valueOf(128.1), Long.class));
        Assert.assertEquals(
                Double.valueOf(12.12), convertSafe(Float.valueOf(12.12f), Double.class));

        // float
        Assert.assertEquals(Float.valueOf(127), convertSafe(Byte.valueOf((byte) 127), Float.class));
        Assert.assertEquals(
                Float.valueOf(1111), convertSafe(Short.valueOf((short) 1111), Float.class));
        Assert.assertEquals(Float.valueOf(12345), convertSafe(Integer.valueOf(12345), Float.class));
        Assert.assertEquals(
                Float.valueOf(1234567), convertSafe(Integer.valueOf(1234567), Float.class));
        Assert.assertNull(convertSafe(BigInteger.valueOf(12345678), Float.class));
        Assert.assertNull(convertSafe(Double.valueOf(128.1), Float.class));
        Assert.assertNull(convertSafe(BigDecimal.valueOf(128.1), Float.class));
        Assert.assertEquals(Float.valueOf(12.12f), convertSafe(Float.valueOf(12.12f), Float.class));

        // Big Decimal
        Assert.assertEquals(
                BigDecimal.valueOf(127), convertSafe(Byte.valueOf((byte) 127), BigDecimal.class));
        Assert.assertEquals(
                BigDecimal.valueOf(1111),
                convertSafe(Short.valueOf((short) 1111), BigDecimal.class));
        Assert.assertEquals(
                BigDecimal.valueOf(12345), convertSafe(Integer.valueOf(12345), BigDecimal.class));
        Assert.assertEquals(
                BigDecimal.valueOf(1234567),
                convertSafe(Integer.valueOf(1234567), BigDecimal.class));
        Assert.assertEquals(
                BigDecimal.valueOf(12345678),
                convertSafe(BigInteger.valueOf(12345678), BigDecimal.class));
        Assert.assertEquals(
                BigDecimal.valueOf(12.123456),
                convertSafe(Double.valueOf(12.123456), BigDecimal.class));
        Assert.assertEquals(
                BigDecimal.valueOf(128.1),
                convertSafe(BigDecimal.valueOf(128.1), BigDecimal.class));
        Assert.assertEquals(
                BigDecimal.valueOf(12.12d), convertSafe(Float.valueOf(12.12f), BigDecimal.class));

        // test strings
        Assert.assertEquals(BigDecimal.valueOf(127), convertSafe("127", BigDecimal.class));
        Assert.assertNull(convertSafe("127f", BigDecimal.class));
        Assert.assertEquals(Double.valueOf(127.123), convertSafe("127.123", Double.class));
        Assert.assertNull(convertSafe("123.456.456", Double.class));
        Assert.assertEquals(Float.valueOf(127.123f), convertSafe("127.123", Float.class));
        Assert.assertNull(convertSafe("123.456.456", Float.class));
        Assert.assertEquals(BigInteger.valueOf(1234567), convertSafe("1234567", BigInteger.class));
        Assert.assertNull(convertSafe("123.456", BigInteger.class));
        Assert.assertEquals(Long.valueOf(54), convertSafe("54", Long.class));
        Assert.assertNull(convertSafe("123.6", Long.class));
        Assert.assertEquals(Integer.valueOf(54), convertSafe("54", Integer.class));
        Assert.assertNull(convertSafe("123.6", Integer.class));
        Assert.assertEquals(Short.valueOf((short) 54), convertSafe("54", Short.class));
        Assert.assertNull(convertSafe("123.6", Short.class));
        Assert.assertEquals(Byte.valueOf("1"), convertSafe("1", Byte.class));
        Assert.assertNull(convertSafe("123.6", Byte.class));
    }

    @Test
    public void testPrimitiveTypes() throws Exception {
        Assert.assertEquals(1, convert(Integer.valueOf(1), int.class));
        Assert.assertEquals(Integer.valueOf(1), convert(Integer.valueOf(1), int.class));
        Assert.assertEquals(1, convert(1, Integer.class));
        Assert.assertEquals(Integer.valueOf(1), convert(1, Integer.class));

        Assert.assertEquals(1, convert("1", int.class));
        Assert.assertEquals(Integer.valueOf(1), convert("1", int.class));
        Assert.assertEquals(1, convert("1", Integer.class));
        Assert.assertEquals(Integer.valueOf(1), convert("1", Integer.class));
    }

    @Test
    public void testStringInScientificNotationToNumber() throws Exception {
        Assert.assertEquals(Long.valueOf("1103442244"), convert("1.1034422448E9", Long.class));
        Assert.assertEquals(Long.valueOf("1103442244"), convert("1.1034422448e9", Long.class));

        Assert.assertEquals(
                Double.valueOf("1103442244.8"), convert("1.1034422448E9", Double.class));
        Assert.assertEquals(
                Double.valueOf("1103442244.8"), convert("1.1034422448e9", Double.class));
    }
}
