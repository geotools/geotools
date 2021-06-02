/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CharsetConverterFactoryTest {

    CharsetConverterFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new CharsetConverterFactory();
    }

    @Test
    public void testLookupStringToCharset() {
        Set<ConverterFactory> s = Converters.getConverterFactories(String.class, Charset.class);
        for (ConverterFactory cf : s) {
            if (cf instanceof CharsetConverterFactory) {
                return;
            }
        }

        Assert.fail("CharsetConverterFactory not found");
    }

    @Test
    public void testLookupCharsetToString() {
        Set<ConverterFactory> s = Converters.getConverterFactories(Charset.class, String.class);
        for (ConverterFactory cf : s) {
            if (cf instanceof CharsetConverterFactory) {
                return;
            }
        }

        Assert.fail("CharsetConverterFactory not found");
    }

    @Test
    public void testStringToCharset() throws Exception {
        Converter c = factory.createConverter(String.class, Charset.class, null);
        Assert.assertNotNull(c);

        Charset charset = c.convert("UTF-8", Charset.class);
        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset.name());

        Assert.assertNull(c.convert("FOO", Charset.class));
    }

    @Test
    public void testCharsetToString() throws Exception {
        Converter c = factory.createConverter(Charset.class, String.class, null);
        Assert.assertNotNull(c);

        String charset = c.convert(StandardCharsets.UTF_8, String.class);
        Assert.assertEquals("UTF-8", charset);
    }
}
