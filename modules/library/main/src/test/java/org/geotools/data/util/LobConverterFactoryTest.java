/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLFeatureNotSupportedException;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/** @author Stefan Uhrig, SAP SE */
public class LobConverterFactoryTest {

    private LobConverterFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new LobConverterFactory();
    }

    @Test
    public void testBlobConversionDirect() throws Exception {
        byte[] expected = {1, 2, 3};

        Blob blob = EasyMock.createMock(Blob.class);
        EasyMock.expect(blob.length()).andReturn((long) expected.length);
        EasyMock.expect(blob.getBytes(1, expected.length)).andReturn(expected);
        EasyMock.replay(blob);

        byte[] actual = convert(blob);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEmptyBlobConversion() throws Exception {
        Blob blob = EasyMock.createMock(Blob.class);
        EasyMock.expect(blob.length()).andReturn((long) 0);
        EasyMock.replay(blob);

        byte[] actual = convert(blob);
        Assert.assertEquals(0, actual.length);
    }

    @Test
    public void testTooLargeBlobConversion() throws Exception {
        Blob blob = EasyMock.createMock(Blob.class);
        EasyMock.expect(blob.length()).andReturn((long) Integer.MAX_VALUE + 1);
        EasyMock.replay(blob);

        byte[] actual = convert(blob);
        Assert.assertNull(actual);
    }

    @Test
    public void testBlobConversionChunkwise() throws Exception {
        byte[] expected = {1, 2, 3};
        ByteArrayInputStream is = new ByteArrayInputStream(expected);

        Blob blob = EasyMock.createMock(Blob.class);
        EasyMock.expect(blob.length()).andThrow(new SQLFeatureNotSupportedException());
        EasyMock.expect(blob.getBinaryStream()).andReturn(is);
        EasyMock.replay(blob);

        byte[] actual = convert(blob);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testClobConversionDirect() throws Exception {
        String expected = "abc";

        Clob clob = EasyMock.createMock(Clob.class);
        EasyMock.expect(clob.length()).andReturn((long) expected.length());
        EasyMock.expect(clob.getSubString(1, expected.length())).andReturn(expected);
        EasyMock.replay(clob);

        String actual = convert(clob);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEmptyClobConversion() throws Exception {
        Clob clob = EasyMock.createMock(Clob.class);
        EasyMock.expect(clob.length()).andReturn((long) 0);
        EasyMock.replay(clob);

        String actual = convert(clob);
        Assert.assertEquals("", actual);
    }

    @Test
    public void testTooLargeClobConversion() throws Exception {
        Clob clob = EasyMock.createMock(Clob.class);
        EasyMock.expect(clob.length()).andReturn((long) Integer.MAX_VALUE + 1);
        EasyMock.replay(clob);

        String actual = convert(clob);
        Assert.assertNull(actual);
    }

    @Test
    public void testClobConversionChunkwise() throws Exception {
        String expected = "abc";
        try (StringReader reader = new StringReader(expected)) {

            Clob clob = EasyMock.createMock(Clob.class);
            EasyMock.expect(clob.length()).andThrow(new SQLFeatureNotSupportedException());
            EasyMock.expect(clob.getCharacterStream()).andReturn(reader);
            EasyMock.replay(clob);

            String actual = convert(clob);
            Assert.assertEquals(expected, actual);
        }
    }

    private byte[] convert(Blob blob) throws Exception {
        return factory.createConverter(blob.getClass(), byte[].class, null).convert(blob, byte[].class);
    }

    private String convert(Clob clob) throws Exception {
        return factory.createConverter(clob.getClass(), String.class, null).convert(clob, String.class);
    }
}
