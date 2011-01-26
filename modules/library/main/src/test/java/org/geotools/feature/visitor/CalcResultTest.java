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

import junit.framework.TestCase;


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

//TODO: add more tests as needed

/**
 * Purpose: these tests ensure that the output of CalcResult converts as expected.
 * @source $URL$
 */
public class CalcResultTest extends TestCase {
    MockCalcResult result = new MockCalcResult();
    Integer val1 = new Integer(4);
    Long val2 = new Long(5);
    Float val3 = new Float(6.0);
    Double val4 = new Double(7.0);
    String val5 = new String("8");
    String val6 = new String("Random text of arbitrary complexity.");

    public void testInt() {
        //int --> int
        result.setValue(val1);
        assertEquals((int) 4, result.toInt());

        //long --> int
        result.setValue(val2);
        assertEquals((int) 5, result.toInt());

        //float --> int
        result.setValue(val3);
        assertEquals((int) 6, result.toInt());

        //double --> int
        result.setValue(val4);
        assertEquals((int) 7, result.toInt());

        //string --> int (a string that looks like a number, should we add this functionality?)
        result.setValue(val5);
        assertEquals((int) 0, result.toInt());

        //string --> int (a real string which clearly isn't an int, so we expect nothing good)
        result.setValue(val6);
        assertEquals((int) 0, result.toInt());
    }

    public void testLong() {
        //int --> long
        result.setValue(val1);
        assertEquals((long) 4, result.toLong());

        //long --> long
        result.setValue(val2);
        assertEquals((long) 5, result.toLong());

        //float --> long
        result.setValue(val3);
        assertEquals((long) 6, result.toLong());

        //double --> long
        result.setValue(val4);
        assertEquals((long) 7, result.toLong());

        //string --> long (a string that looks like a number, should we add this functionality?)
        result.setValue(val5);
        assertEquals((long) 0, result.toLong());

        //string --> long (a real string which clearly isn't an int, so we expect nothing good)
        result.setValue(val6);
        assertEquals((long) 0, result.toLong());
    }

    public void testFloat() {
        //int --> float
        result.setValue(val1);
        assertEquals((float) 4, result.toFloat(), 0);

        //long --> float
        result.setValue(val2);
        assertEquals((float) 5, result.toFloat(), 0);

        //float --> float
        result.setValue(val3);
        assertEquals((float) 6, result.toFloat(), 0);

        //double --> float
        result.setValue(val4);
        assertEquals((float) 7, result.toFloat(), 0);

        //string --> float (a string that looks like a number, should we add this functionality?)
        result.setValue(val5);
        assertEquals((float) 0, result.toFloat(), 0);

        //string --> float (a real string which clearly isn't an int, so we expect nothing good)
        result.setValue(val6);
        assertEquals((float) 0, result.toFloat(), 0);
    }

    public void testDouble() {
        //int --> double
        result.setValue(val1);
        assertEquals((double) 4, result.toDouble(), 0);

        //long --> double
        result.setValue(val2);
        assertEquals((double) 5, result.toDouble(), 0);

        //float --> double
        result.setValue(val3);
        assertEquals((double) 6, result.toDouble(), 0);

        //double --> double
        result.setValue(val4);
        assertEquals((double) 7, result.toDouble(), 0);

        //string --> double (a string that looks like a number, should we add this functionality?)
        result.setValue(val5);
        assertEquals((double) 0, result.toDouble(), 0);

        //string --> double (a real string which clearly isn't an int, so we expect nothing good)
        result.setValue(val6);
        assertEquals((double) 0, result.toDouble(), 0);
    }

    public void testString() {
        //int --> string
        result.setValue(val1);
        assertEquals("4", result.toString());

        //long --> string
        result.setValue(val2);
        assertEquals("5", result.toString());

        //float --> string
        result.setValue(val3);
        assertEquals("6.0", result.toString());

        //double --> string
        result.setValue(val4);
        assertEquals("7.0", result.toString());

        //string --> string
        result.setValue(val5);
        assertEquals("8", result.toString());

        //string --> string
        result.setValue(val6);
        assertEquals("Random text of arbitrary complexity.", result.toString());
    }

    /**
     * A simple results set which allows us to test the AbstractCalcResult type
     * conversions
     */
    public class MockCalcResult extends AbstractCalcResult {
        Object value;

        public MockCalcResult() {
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
