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
package org.geotools.gml.producer;

import org.junit.Assert;
import org.junit.Test;

public class CoordinateFormatterTest {
    @Test
    public void testFormatScientific() {
        CoordinateFormatter formatter = new CoordinateFormatter(3);
        formatter.setForcedDecimal(false);
        Assert.assertEquals("2.1396814969E7", formatter.format(21396814.969));
    }

    @Test
    public void testFormatDecimals() {
        CoordinateFormatter formatter = new CoordinateFormatter(3);
        formatter.setForcedDecimal(true);
        Assert.assertEquals("21396814.969", formatter.format(21396814.969));
    }

    @Test
    public void testFormatNumDecimals() {
        CoordinateFormatter formatter = new CoordinateFormatter(2);
        formatter.setForcedDecimal(true);
        Assert.assertEquals("21396814.97", formatter.format(21396814.969));
    }

    @Test
    public void testFormatDecimalsZeroPadded() {
        CoordinateFormatter formatter = new CoordinateFormatter(4);
        formatter.setForcedDecimal(true);
        formatter.setPadWithZeros(true);
        Assert.assertEquals("21396814.9690", formatter.format(21396814.969));
        Assert.assertEquals("21396814.0000", formatter.format(21396814));
        Assert.assertEquals("21396814.9691", formatter.format(21396814.96912));
    }
}
