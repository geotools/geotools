/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.visitor;

import org.junit.Assert;
import org.junit.Test;

/*
  So far CalcResult contains the following conversion methods which call
  getValue() and then convert to the type we want. The ones marked below with a
  "!" haven't really found an implementation yet and no tests have been
  written.  They should work under normal circumstances, but will likely explode:
  public int toInt()
  public double toDouble();
  public String toString();
  public long toLong();
  public float toFloat();
  ! public Geometry toGeometry();
  ! public Point toPoint();
  ! public Set toSet();
  ! public List toList();
  ! public Object[] toArray();
  ! public Map toMap();
*/

// TODO: add more tests as needed

/** Purpose: these tests ensure that the output of CalcResult converts as expected. */
public class CalcResultTest {
    MockCalcResult result = new MockCalcResult();
    Integer val1 = Integer.valueOf(4);
    Long val2 = Long.valueOf(5);
    Float val3 = Float.valueOf(6f);
    Double val4 = Double.valueOf(7.0);
    String val5 = "8";
    String val6 = "Random text of arbitrary complexity.";

    @Test
    public void testInt() {
        // int --> int
        result.setValue(val1);
        Assert.assertEquals(4, result.toInt());

        // long --> int
        result.setValue(val2);
        Assert.assertEquals(5, result.toInt());

        // float --> int
        result.setValue(val3);
        Assert.assertEquals(6, result.toInt());

        // double --> int
        result.setValue(val4);
        Assert.assertEquals(7, result.toInt());

        // string --> int (a string that looks like a number, should we add this functionality?)
        result.setValue(val5);
        Assert.assertEquals(0, result.toInt());

        // string --> int (a real string which clearly isn't an int, so we expect nothing good)
        result.setValue(val6);
        Assert.assertEquals(0, result.toInt());
    }

    @Test
    public void testLong() {
        // int --> long
        result.setValue(val1);
        Assert.assertEquals(4, result.toLong());

        // long --> long
        result.setValue(val2);
        Assert.assertEquals(5, result.toLong());

        // float --> long
        result.setValue(val3);
        Assert.assertEquals(6, result.toLong());

        // double --> long
        result.setValue(val4);
        Assert.assertEquals(7, result.toLong());

        // string --> long (a string that looks like a number, should we add this functionality?)
        result.setValue(val5);
        Assert.assertEquals(0, result.toLong());

        // string --> long (a real string which clearly isn't an int, so we expect nothing good)
        result.setValue(val6);
        Assert.assertEquals(0, result.toLong());
    }

    @Test
    public void testFloat() {
        // int --> float
        result.setValue(val1);
        Assert.assertEquals((float) 4, result.toFloat(), 0);

        // long --> float
        result.setValue(val2);
        Assert.assertEquals((float) 5, result.toFloat(), 0);

        // float --> float
        result.setValue(val3);
        Assert.assertEquals((float) 6, result.toFloat(), 0);

        // double --> float
        result.setValue(val4);
        Assert.assertEquals((float) 7, result.toFloat(), 0);

        // string --> float (a string that looks like a number, should we add this functionality?)
        result.setValue(val5);
        Assert.assertEquals((float) 0, result.toFloat(), 0);

        // string --> float (a real string which clearly isn't an int, so we expect nothing good)
        result.setValue(val6);
        Assert.assertEquals((float) 0, result.toFloat(), 0);
    }

    @Test
    public void testDouble() {
        // int --> double
        result.setValue(val1);
        Assert.assertEquals(4, result.toDouble(), 0);

        // long --> double
        result.setValue(val2);
        Assert.assertEquals(5, result.toDouble(), 0);

        // float --> double
        result.setValue(val3);
        Assert.assertEquals(6, result.toDouble(), 0);

        // double --> double
        result.setValue(val4);
        Assert.assertEquals(7, result.toDouble(), 0);

        // string --> double (a string that looks like a number, should we add this functionality?)
        result.setValue(val5);
        Assert.assertEquals(0, result.toDouble(), 0);

        // string --> double (a real string which clearly isn't an int, so we expect nothing good)
        result.setValue(val6);
        Assert.assertEquals(0, result.toDouble(), 0);
    }

    @Test
    public void testString() {
        // int --> string
        result.setValue(val1);
        Assert.assertEquals("4", result.toString());

        // long --> string
        result.setValue(val2);
        Assert.assertEquals("5", result.toString());

        // float --> string
        result.setValue(val3);
        Assert.assertEquals("6.0", result.toString());

        // double --> string
        result.setValue(val4);
        Assert.assertEquals("7.0", result.toString());

        // string --> string
        result.setValue(val5);
        Assert.assertEquals("8", result.toString());

        // string --> string
        result.setValue(val6);
        Assert.assertEquals("Random text of arbitrary complexity.", result.toString());
    }

    /** A simple results set which allows us to test the AbstractCalcResult type conversions */
    public static class MockCalcResult extends AbstractCalcResult {
        Object value;

        public MockCalcResult() {}

        @Override
        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
