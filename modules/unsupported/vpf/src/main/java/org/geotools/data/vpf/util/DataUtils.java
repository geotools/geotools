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
package org.geotools.data.vpf.util;

import org.geotools.data.vpf.ifc.DataTypesDefinition;
import org.geotools.data.vpf.io.VPFDate;


/**
 * Class DataUtils.java is responsible for a bunch of 
 * miscellaneous operations for reading and converting
 * data
 * 
 * <p>
 * Created: Wed Jan 29 10:06:37 2003
 * </p>
 *
 * @author <a href="mailto:kobit@users.sourceforge.net">Artur Hefczyc</a>
 *
 * @source $URL$
 * @version $Id$
 */
public class DataUtils implements DataTypesDefinition {
    /**
     * Describe <code>toBigEndian</code> method here.
     *
     * @param source a <code>byte[]</code> value
     * @return a <code>byte[]</code> value
     */
    public static byte[] toBigEndian(byte[] source) {
        byte[] result = new byte[source.length];

        for (int i = 0; i < source.length; i++) {
            result[i] = source[source.length - (i + 1)];
        }

        return result;
    }

    /**
     * Describe <code>decodeData</code> method here.
     *
     * @param bytes a <code>byte[]</code> value
     * @param type a <code>char</code> value
     * @return an <code>Object</code> value
     */
    public static Object decodeData(byte[] bytes, char type) {
        Object result = null;

        switch (type) {
        case DATA_TEXT:
        case DATA_LEVEL1_TEXT:
        case DATA_LEVEL2_TEXT:
        case DATA_LEVEL3_TEXT:

            StringBuffer sb = new StringBuffer(bytes.length);

            for (int i = 0; i < bytes.length; i++) {
                sb.append((char) bytes[i]);
            }

            boolean isNull = false;

            for (int i = 0; i < STRING_NULL_VALUES.length; i++) {
                isNull |= sb.toString().trim()
                            .equalsIgnoreCase(STRING_NULL_VALUES[i]);
            }

            if (isNull) {
                result = null;
            } else {
                result = sb.toString().trim();
            }

            break;

        case DATA_SHORT_FLOAT:
            result = new Float(decodeFloat(bytes));

            break;

        case DATA_LONG_FLOAT:
            result = new Double(decodeDouble(bytes));

            break;

        case DATA_SHORT_INTEGER:
            result = new Short(decodeShort(bytes));

            break;

        case DATA_LONG_INTEGER:
            result = new Integer(decodeInt(bytes));

            break;

        case DATA_2_COORD_F:
//        {
//            // I doubt this is being used
//          float[][] coords = new float[bytes.length / DATA_2_COORD_F_LEN][2];
//          byte[] floatData = new byte[DATA_SHORT_FLOAT_LEN];
//
//          for (int i = 0; i < coords.length; i++) {
//              copyArrays(floatData, bytes, i * DATA_2_COORD_F_LEN);
//              coords[i][0] = decodeFloat(floatData);
//              copyArrays(floatData, bytes, i * (DATA_2_COORD_F_LEN + 1));
//              coords[i][1] = decodeFloat(floatData);
//          }
//          DirectPosition[] coords = new DirectPosition2D[bytes.length / DATA_2_COORD_F_LEN];
//          double xval, yval;
//          byte[] floatData = new byte[DATA_SHORT_FLOAT_LEN];
//
//          for (int i = 0; i < coords.length; i++) {
//              copyArrays(floatData, bytes, i * DATA_2_COORD_F_LEN);
//              xval = decodeFloat(floatData);
//              copyArrays(floatData, bytes, i * (DATA_2_COORD_F_LEN + 1));
//              yval = decodeFloat(floatData);
//              coords[i] = new DirectPosition2D(xval, yval);
//          }

//          result = coords;
//            result = new Coordinate2DFloat(coords);
//        }
//
//        break;

        case DATA_2_COORD_R:
//        {
            // I doubt this is being used
//            DirectPosition[] coords = new DirectPosition2D[bytes.length / DATA_2_COORD_R_LEN];
//            double xval, yval;
//            byte[] doubleData = new byte[DATA_LONG_FLOAT_LEN];
//
//            for (int i = 0; i < coords.length; i++) {
//                copyArrays(doubleData, bytes, i * DATA_2_COORD_R_LEN);
//                xval = decodeDouble(doubleData);
//                copyArrays(doubleData, bytes, i * (DATA_2_COORD_R_LEN + 1));
//                yval = decodeDouble(doubleData);
//                coords[i] = new DirectPosition2D(xval, yval);
//            }
//
//            result = coords;
//            double[][] coords = new double[bytes.length / DATA_2_COORD_R_LEN][2];
//            byte[] doubleData = new byte[DATA_LONG_FLOAT_LEN];
//
//            for (int i = 0; i < coords.length; i++) {
//                copyArrays(doubleData, bytes, i * DATA_2_COORD_R_LEN);
//                coords[i][0] = decodeDouble(doubleData);
//                copyArrays(doubleData, bytes, i * (DATA_2_COORD_R_LEN + 1));
//                coords[i][1] = decodeDouble(doubleData);
//            }
//
//            result = new Coordinate2DDouble(coords);
//        }
//
//        break;
//
        case DATA_3_COORD_F:
//        {
          // I doubt this is being used
//            DirectPosition[] coords = new DirectPosition2D[bytes.length / DATA_2_COORD_R_LEN];
//            double xval, yval, zval;
//            byte[] floatData = new byte[DATA_SHORT_FLOAT_LEN];
//
//            for (int i = 0; i < coords.length; i++) {
//                copyArrays(floatData, bytes, i * DATA_3_COORD_F_LEN);
//                xval = decodeFloat(floatData);
//                copyArrays(floatData, bytes, i * (DATA_3_COORD_F_LEN + 1));
//                yval = decodeFloat(floatData);
//                copyArrays(floatData, bytes, i * (DATA_3_COORD_F_LEN + 2));
//                zval = decodeFloat(floatData);
//                coords[i] = new GeneralDirectPosition(xval, yval, zval);
//            }
//
//            result = coords;
//            float[][] coords = new float[bytes.length / DATA_3_COORD_F_LEN][3];
//            byte[] floatData = new byte[DATA_SHORT_FLOAT_LEN];
//
//            for (int i = 0; i < coords.length; i++) {
//                copyArrays(floatData, bytes, i * DATA_3_COORD_F_LEN);
//                coords[i][0] = decodeFloat(floatData);
//                copyArrays(floatData, bytes, i * (DATA_3_COORD_F_LEN + 1));
//                coords[i][1] = decodeFloat(floatData);
//                copyArrays(floatData, bytes, i * (DATA_3_COORD_F_LEN + 2));
//                coords[i][2] = decodeFloat(floatData);
//            }
//
//            result = new Coordinate3DFloat(coords);
//        }
//
//        break;
//
        case DATA_3_COORD_R:
//        {
            // I doubt this is being used
//            DirectPosition[] coords = new DirectPosition2D[bytes.length / DATA_2_COORD_R_LEN];
//            double xval, yval, zval;
//            byte[] doubleData = new byte[DATA_LONG_FLOAT_LEN];
//
//            for (int i = 0; i < coords.length; i++) {
//                copyArrays(doubleData, bytes, i * DATA_3_COORD_R_LEN);
//                xval = decodeDouble(doubleData);
//                copyArrays(doubleData, bytes, i * (DATA_3_COORD_R_LEN + 1));
//                yval = decodeDouble(doubleData);
//                copyArrays(doubleData, bytes, i * (DATA_3_COORD_R_LEN + 2));
//                zval = decodeDouble(doubleData);
//                coords[i] = new GeneralDirectPosition(xval, yval, zval);
//            }
//
//            result = coords;
//            double[][] coords = new double[bytes.length / DATA_3_COORD_R_LEN][3];
//            byte[] doubleData = new byte[DATA_LONG_FLOAT_LEN];
//
//            for (int i = 0; i < coords.length; i++) {
//                copyArrays(doubleData, bytes, i * DATA_3_COORD_R_LEN);
//                coords[i][0] = decodeDouble(doubleData);
//                copyArrays(doubleData, bytes, i * (DATA_3_COORD_R_LEN + 1));
//                coords[i][1] = decodeDouble(doubleData);
//                copyArrays(doubleData, bytes, i * (DATA_3_COORD_R_LEN + 2));
//                coords[i][2] = decodeDouble(doubleData);
//            }
//
//            result = new Coordinate3DDouble(coords);
//        }
        {    
            throw new RuntimeException("If this code is actually being used, replace it with equivalent code from VPFFile.");
        }
        case DATA_DATE_TIME:
            result = new VPFDate(bytes);

            break;

        case DATA_NULL_FIELD:
            break;

        case DATA_TRIPLET_ID:
        default:
            break;
        }

        return result;
    }

    /**
     * Describe <code>copyArrays</code> method here.
     *
     * @param dest a <code>byte[]</code> value
     * @param source a <code>byte[]</code> value
     * @param fromIdx an <code>int</code> value
     */
    public static void copyArrays(byte[] dest, byte[] source, int fromIdx) {
        for (int i = 0; i < dest.length; i++) {
            dest[i] = source[i + fromIdx];
        }
    }

    /**
     * Describe <code>decodeShort</code> method here.
     *
     * @param bytes a <code>byte[]</code> value
     * @return a <code>short</code> value
     */
    public static short decodeShort(byte[] bytes) {
        short res = 0;
        int shift = 8;

        for (int i = 0; (i < bytes.length) && (shift >= 0); i++) {
            res |= ((short) (bytes[i] & 0xff) << shift);
            shift -= 8;
        }

        return res;
    }

    //   public static int littleEndianToInt(byte[] fourBytes)
    //   {
    //     int res = 0;
    //     int limit = Math.min(fourBytes.length, 4);
    //     for (int i = 0; i < limit; i++)
    //     {
    //       res |= (fourBytes[i] & 0xFF) << (i*8);
    //     } // end of for (int i = 0; i < limit-1; i++)
    //     return res;
    //   }

    /**
     * Describe <code>decodeInt</code> method here.
     *
     * @param bytes a <code>byte[]</code> value
     * @return an <code>int</code> value
     */
    public static int decodeInt(byte[] bytes) {
        int res = 0;
        int shift = 24;

        for (int i = 0; (i < bytes.length) && (shift >= 0); i++) {
            res |= ((bytes[i] & 0xff) << shift);
            shift -= 8;
        }

        return res;
    }

    /**
     * Describe <code>decodeFloat</code> method here.
     *
     * @param bytes a <code>byte[]</code> value
     * @return a <code>float</code> value
     */
    public static float decodeFloat(byte[] bytes) {
        int res = 0;
        int shift = 24;

        for (int i = 0; (i < bytes.length) && (shift >= 0); i++) {
            res |= ((bytes[i] & 0xff) << shift);
            shift -= 8;
        }

        return Float.intBitsToFloat(res);
    }

    /**
     * Describe <code>decodeDouble</code> method here.
     *
     * @param bytes a <code>byte[]</code> value
     * @return a <code>double</code> value
     */
    public static double decodeDouble(byte[] bytes) {
        long res = 0;
        int shift = 56;

        for (int i = 0; (i < bytes.length) && (shift >= 0); i++) {
            res |= ((long) (bytes[i] & 0xff) << shift);
            shift -= 8;
        }

        return Double.longBitsToDouble(res);
    }

    /**
     * Describe <code>unsigByteToInt</code> method here.
     *
     * @param b a <code>byte</code> value
     * @return an <code>int</code> value
     */
    public static int unsigByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    /**
     * Describe <code>getDataTypeSize</code> method here.
     *
     * @param type a <code>char</code> value
     * @return an <code>int</code> value
     */
    public static int getDataTypeSize(char type) {
        int size = -1;

        switch (type) {
        case DATA_TEXT:
        case DATA_LEVEL1_TEXT:
        case DATA_LEVEL2_TEXT:
        case DATA_LEVEL3_TEXT:
            size = 1;

            break;

        case DATA_SHORT_FLOAT:
            size = DATA_SHORT_FLOAT_LEN;

            break;

        case DATA_LONG_FLOAT:
            size = DATA_LONG_FLOAT_LEN;

            break;

        case DATA_SHORT_INTEGER:
            size = DATA_SHORT_INTEGER_LEN;

            break;

        case DATA_LONG_INTEGER:
            size = DATA_LONG_INTEGER_LEN;

            break;

        case DATA_2_COORD_F:
            size = DATA_2_COORD_F_LEN;

            break;

        case DATA_2_COORD_R:
            size = DATA_2_COORD_R_LEN;

            break;

        case DATA_3_COORD_F:
            size = DATA_3_COORD_F_LEN;

            break;

        case DATA_3_COORD_R:
            size = DATA_3_COORD_R_LEN;

            break;

        case DATA_DATE_TIME:
            size = DATA_DATE_TIME_LEN;

            break;

        case DATA_NULL_FIELD:
            size = DATA_NULL_FIELD_LEN;

            break;

        case DATA_TRIPLET_ID:
            size = DATA_TRIPLET_ID_LEN;

        default:
            break;
        }

        return size;
    }

    /**
     * Describe <code>isNumeric</code> method here.
     *
     * @param type a <code>char</code> value
     * @return a <code>boolean</code> value
     */
    public static boolean isNumeric(char type) {
        switch (type) {
        case DATA_TEXT:
        case DATA_LEVEL1_TEXT:
        case DATA_LEVEL2_TEXT:
        case DATA_LEVEL3_TEXT:
        case DATA_DATE_TIME:
        case DATA_NULL_FIELD:
            return false;

        case DATA_SHORT_FLOAT:
        case DATA_LONG_FLOAT:
        case DATA_SHORT_INTEGER:
        case DATA_LONG_INTEGER:
            return true;

        case DATA_2_COORD_F:
        case DATA_2_COORD_R:
        case DATA_3_COORD_F:
        case DATA_3_COORD_R:
        case DATA_TRIPLET_ID:
            return true;

        default:
            return false;
        }
    }
}

// DataUtils
