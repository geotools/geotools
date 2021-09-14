/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import java.util.concurrent.TimeUnit;
import org.geotools.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbbreviatedTimeUnitConverterFactoryTest {

    AbbreviatedTimeUnitConverterFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new AbbreviatedTimeUnitConverterFactory();
    }

    @Test
    public void testAbbreviatedTimeUnitsToTimeUnitConversion() throws Exception {
        Converter converter = factory.createConverter(String.class, TimeUnit.class, null);
        assertConversion(converter, "ms", TimeUnit.MILLISECONDS);
        assertConversion(converter, "s", TimeUnit.SECONDS);
        assertConversion(converter, "m", TimeUnit.MINUTES);
        assertConversion(converter, "h", TimeUnit.HOURS);
        assertConversion(converter, "d", TimeUnit.DAYS);
        // Unsupported Time unit
        assertConversion(converter, "k", null);
    }

    private void assertConversion(
            Converter converter, String timeUnitString, TimeUnit expectedTimeUnit)
            throws Exception {
        TimeUnit convertedTimeUnit = converter.convert(timeUnitString, TimeUnit.class);
        Assert.assertEquals(convertedTimeUnit, expectedTimeUnit);
    }
}
