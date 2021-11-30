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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BooleanConverterFactoryTest {

    BooleanConverterFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new BooleanConverterFactory();
    }

    @Test
    public void testFromString() throws Exception {
        Assert.assertEquals(Boolean.TRUE, convert("true"));
        Assert.assertEquals(Boolean.TRUE, convert("1"));
        Assert.assertEquals(Boolean.FALSE, convert("false"));
        Assert.assertEquals(Boolean.FALSE, convert("0"));
    }

    @Test
    public void testFromInteger() throws Exception {
        Assert.assertEquals(Boolean.TRUE, convert(Integer.valueOf(1)));
        Assert.assertEquals(Boolean.FALSE, convert(Integer.valueOf(0)));
    }

    @Test
    public void testFromBigDecimal() throws Exception {
        Assert.assertEquals(Boolean.TRUE, convert(BigDecimal.valueOf(1)));
        Assert.assertEquals(Boolean.FALSE, convert(BigDecimal.valueOf(0)));
    }

    Boolean convert(Object value) throws Exception {
        return factory.createConverter(value.getClass(), Boolean.class, null)
                .convert(value, Boolean.class);
    }
}
