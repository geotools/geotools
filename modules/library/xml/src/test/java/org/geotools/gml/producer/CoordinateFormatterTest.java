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
    public void testFormatScientificDECIMAL_MIN() {
        CoordinateFormatter formatter = new CoordinateFormatter(6);
        formatter.setPadWithZeros(true);
        formatter.setForcedDecimal(true);
        Assert.assertEquals("0.000010", formatter.format(0.00001));

        formatter.setForcedDecimal(false);
        Assert.assertEquals("1.0E-5", formatter.format(0.00001));

        formatter.setMaximumFractionDigits(4);
        formatter.setForcedDecimal(true);
        formatter.setPadWithZeros(true);
        // if you set numDecimals to < the number of decimals required to
        // represent small absolute values, you will truncate your data
        Assert.assertEquals("0.0000", formatter.format(0.00001));

        formatter.setForcedDecimal(true);
        formatter.setPadWithZeros(false);
        Assert.assertEquals("0", formatter.format(0.00001));
        // if you set numDecimals to < the number of decimals required to
        // represent small absolute values, you will truncate your data
        formatter.setMaximumFractionDigits(3);
        formatter.setPadWithZeros(true);
        Assert.assertEquals("0.000", formatter.format(0.00001));

        formatter.setPadWithZeros(false);
        formatter.setForcedDecimal(true);
        Assert.assertEquals("-0.001", formatter.format(-0.000623D));
        Assert.assertEquals("0.001", formatter.format(0.000623D));
        // it is expected that this path returns different results
        formatter.setForcedDecimal(false);
        Assert.assertEquals("-6.23E-4", formatter.format(-0.000623D));
        Assert.assertEquals("6.23E-4", formatter.format(0.000623D));
    }

    @Test
    public void testDECIMAL_MAX() {
        CoordinateFormatter formatter = new CoordinateFormatter(5);
        // for x < CoordinateFormatter.DECIMAL_MAX Math.pow(10,7)
        // the result is decimal notation by default
        Assert.assertEquals("9999999.12346", formatter.format(9999999.123456));
        Assert.assertEquals("-9999999.12346", formatter.format(-9999999.123456));
        Assert.assertEquals("2139681.12346", formatter.format(2139681.123456));
        // for absolute values >= CoordinateFormatter.DECIMAL_MAX Math.pow(10, 7)
        // the result is scientific notation by default
        Assert.assertEquals("1.0E7", formatter.format(Math.pow(10, 7)));
        Assert.assertEquals("1.0E7", formatter.format(10000000));
        Assert.assertEquals("-1.0E7", formatter.format(-10000000));
        Assert.assertEquals("1.0000001E7", formatter.format(Math.pow(10, 7) + 1));
        Assert.assertEquals("2.13968140012346E9", formatter.format(2139681400.123456));
        // if you force decimals, you will get decimals despite large numbers
        formatter.setForcedDecimal(true);
        Assert.assertEquals("2139681400.12346", formatter.format(2139681400.123456));
        Assert.assertEquals("-2139681400.12346", formatter.format(-2139681400.123456));
    }

    @Test
    public void testTruncate() {
        CoordinateFormatter formatter = new CoordinateFormatter(1);
        // Assert.assertEquals(expected,actual,delta)
        Assert.assertEquals(0D, formatter.truncate(0D), 0D);
        Assert.assertEquals(0D, formatter.truncate(-0D), 0D);
        Assert.assertEquals(0.1D, formatter.truncate(0.123D), 0D);
        Assert.assertEquals(-0.1D, formatter.truncate(-0.123D), 0D);
        Assert.assertEquals(0.6D, formatter.truncate(0.647D), 0D);
        Assert.assertEquals(-0.6D, formatter.truncate(-0.647D), 0D);
        // despite the name "truncate", it rounds up above 0, down below 0
        Assert.assertEquals(0.7D, formatter.truncate(0.657D), 0D);
        Assert.assertEquals(-0.7D, formatter.truncate(-0.657D), 0D);
        Assert.assertEquals(1.0D, formatter.truncate(0.999D), 0D);
        Assert.assertEquals(-1.0D, formatter.truncate(-0.999D), 0D);
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
