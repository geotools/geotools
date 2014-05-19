/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014 TOPP - www.openplans.org.
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
package org.geotools.processing.jai.nodata;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This test-class is used for evaluating the functionalities of the {@link Range} class and its subclasses.
 * 
 * @author Nicola Lagomarsini, GeoSolutions S.A.S.
 */
public class RangeTest {

    /** test values byte */
    private static byte[] arrayB;

    /** test values ushort */
    private static short[] arrayUS;

    /** test values short */
    private static short[] arrayS;

    /** test values integer */
    private static int[] arrayI;

    /** test values float */
    private static float[] arrayF;

    /** test values doble */
    private static double[] arrayD;

    /** test values long */
    private static long[] arrayL;

    /** test values byte speed comparison */
    private static Byte[] arrayBtest;

    /** test values short speed comparison */
    private static Short[] arrayStest;

    /** test values integer speed comparison */
    private static Integer[] arrayItest;

    /** test values float speed comparison */
    private static Float[] arrayFtest;

    /** test values doble speed comparison */
    private static Double[] arrayDtest;

    /** Range byte 2 bounds */
    private static Range rangeB2bounds;

    /** Range byte 1 point */
    private static Range rangeBpoint;

    /** Range ushort 2 bounds */
    private static Range rangeU2bounds;

    /** Range ushort 1 point */
    private static Range rangeUpoint;

    /** Range short 2 bounds */
    private static Range rangeS2bounds;

    /** Range short 1 point */
    private static Range rangeSpoint;

    /** Range int 2 bounds */
    private static Range rangeI2bounds;

    /** Range int 1 point */
    private static Range rangeIpoint;

    /** Range float 2 bounds */
    private static Range rangeF2bounds;

    /** Range float 1 point */
    private static Range rangeFpoint;

    /** Range double 2 bounds */
    private static Range rangeD2bounds;

    /** Range double 1 point */
    private static Range rangeDpoint;

    /** Range long 2 bounds */
    private static Range rangeL2bounds;

    /** Range long 1 point */
    private static Range rangeLpoint;

    @BeforeClass
    public static void initialSetup() {
        arrayB = new byte[] { 0, 1, 5, 50, 100 };
        arrayUS = new short[] { 0, 1, 5, 50, 100 };
        arrayS = new short[] { -10, 0, 5, 50, 100 };
        arrayI = new int[] { -10, 0, 5, 50, 100 };
        arrayF = new float[] { -10, 0, 5, 50, 100 };
        arrayD = new double[] { -10, 0, 5, 50, 100 };
        arrayL = new long[] { -10, 0, 5, 50, 100 };

        rangeB2bounds = RangeFactory.create((byte) 2, true, (byte) 60, true);
        rangeBpoint = RangeFactory.create(arrayB[2], true, arrayB[2], true);
        rangeU2bounds = RangeFactory.createU((short) 2, true, (short) 60, true);
        rangeUpoint = RangeFactory.createU(arrayUS[2], true, arrayUS[2], true);
        rangeS2bounds = RangeFactory.create((short) 1, true, (short) 60, true);
        rangeSpoint = RangeFactory.create(arrayS[2], true, arrayS[2], true);
        rangeI2bounds = RangeFactory.create(1, true, 60, true);
        rangeIpoint = RangeFactory.create(arrayI[2], true, arrayI[2], true);
        rangeF2bounds = RangeFactory.create(0.5f, true, 60.5f, true, false);
        rangeFpoint = RangeFactory.create(arrayF[2], true, arrayF[2], true, false);
        rangeD2bounds = RangeFactory.create(1.5d, true, 60.5d, true, false);
        rangeDpoint = RangeFactory.create(arrayD[2], true, arrayD[2], true, false);
        rangeL2bounds = RangeFactory.create(1L, true, 60L, true);
        rangeLpoint = RangeFactory.create(arrayL[2], true, arrayL[2], true);

        arrayBtest = new Byte[100];
        arrayStest = new Short[100];
        arrayItest = new Integer[100];
        arrayFtest = new Float[100];
        arrayDtest = new Double[100];

        // Random value creation for the various Ranges
        for (int j = 0; j < 100; j++) {
            double randomValue = Math.random();

            arrayBtest[j] = (byte) (randomValue * (Byte.MAX_VALUE - Byte.MIN_VALUE) + Byte.MIN_VALUE);
            arrayStest[j] = (short) (randomValue * (Short.MAX_VALUE - Short.MIN_VALUE) + Short.MIN_VALUE);
            arrayItest[j] = (int) (randomValue * (Integer.MAX_VALUE - Integer.MIN_VALUE) + Integer.MIN_VALUE);
            arrayFtest[j] = (float) (randomValue * (Float.MAX_VALUE - Float.MIN_VALUE) + Float.MIN_VALUE);
            arrayDtest[j] = (randomValue * (Double.MAX_VALUE - Double.MIN_VALUE) + Double.MIN_VALUE);
        }
    }

    @Test
    public void testRange() {
        for (int i = 0; i < arrayB.length; i++) {
            boolean check2pointByte = rangeB2bounds.contains(arrayB[i]);
            boolean check1pointByte = rangeBpoint.contains(arrayB[i]);
            boolean check2pointUshort = rangeU2bounds.contains(arrayUS[i]);
            boolean check1pointUshort = rangeUpoint.contains(arrayUS[i]);
            boolean check2pointShort = rangeS2bounds.contains(arrayS[i]);
            boolean check1pointShort = rangeSpoint.contains(arrayS[i]);
            boolean check2pointInt = rangeI2bounds.contains(arrayI[i]);
            boolean check1pointInt = rangeIpoint.contains(arrayI[i]);
            boolean check2pointFloat = rangeF2bounds.contains(arrayF[i]);
            boolean check1pointFloat = rangeFpoint.contains(arrayF[i]);
            boolean check2pointDouble = rangeD2bounds.contains(arrayD[i]);
            boolean check1pointDouble = rangeDpoint.contains(arrayD[i]);
            boolean check2pointLong = rangeL2bounds.contains(arrayL[i]);
            boolean check1pointLong = rangeLpoint.contains(arrayL[i]);

            if (i == 2) {
                assertTrue(check1pointByte);
                assertTrue(check2pointByte);
                assertTrue(check1pointUshort);
                assertTrue(check2pointUshort);
                assertTrue(check1pointShort);
                assertTrue(check2pointShort);
                assertTrue(check1pointInt);
                assertTrue(check2pointInt);
                assertTrue(check1pointFloat);
                assertTrue(check2pointFloat);
                assertTrue(check1pointDouble);
                assertTrue(check2pointDouble);
                assertTrue(check1pointLong);
                assertTrue(check2pointLong);
            } else if (i == 3) {
                assertFalse(check1pointByte);
                assertTrue(check2pointByte);
                assertFalse(check1pointUshort);
                assertTrue(check2pointUshort);
                assertFalse(check1pointShort);
                assertTrue(check2pointShort);
                assertFalse(check1pointInt);
                assertTrue(check2pointInt);
                assertFalse(check1pointFloat);
                assertTrue(check2pointFloat);
                assertFalse(check1pointDouble);
                assertTrue(check2pointDouble);
                assertFalse(check1pointLong);
                assertTrue(check2pointLong);
            } else {
                assertFalse(check1pointByte);
                assertFalse(check2pointByte);
                assertFalse(check1pointUshort);
                assertFalse(check2pointUshort);
                assertFalse(check1pointShort);
                assertFalse(check2pointShort);
                assertFalse(check1pointInt);
                assertFalse(check2pointInt);
                assertFalse(check1pointFloat);
                assertFalse(check2pointFloat);
                assertFalse(check1pointDouble);
                assertFalse(check2pointDouble);
                assertFalse(check1pointLong);
                assertFalse(check2pointLong);
            }
        }
    }
}
