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
package org.geotools.data.oracle.sdo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.PrecisionModel;


/**
 * HelperClass for dealing with JTS14 CoordianteSequences.
 * 
 * <p>
 * JTS14 does not supply suffiecnt API to allow the modification of
 * CoordinateSequence in a lossless manner. To make full use of this class
 * your CoordianteSequence will need to support the additional methods
 * outlined in CoordinateAccess.
 * </p>
 *
 * @author bowens , Refractions Research, Inc.
 * @author $Author: jgarnett $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class Coordinates {
    private Coordinates() {
        // utility class do not inaniate
    }

    /**
     * Sublist opperation for CoordinateSequence.
     * 
     * <p>
     * Opperates in the same manner as corrasponding java util List method.
     * </p>
     *
     * @param factory factory used to manage sequence
     * @param sequence coordinate sequence
     * @param fromIndex - low endpoint (inclusive) of the subList.
     * @param toIndex - high endpoint (exclusive) of the subList.
     *
     * @return sublist of sequence (start,end] as provided by factory
     */
    public static CoordinateSequence subList(
        CoordinateSequenceFactory factory, CoordinateSequence sequence,
        int fromIndex, int toIndex) {
        if ((fromIndex == 0) && (toIndex == sequence.size())) {
            return sequence; // same list so just return it
        }

        if (sequence instanceof List) {
            List sublist = ((List) sequence).subList(fromIndex, toIndex);

            if (sublist instanceof CoordinateSequence) {
                return (CoordinateSequence) sublist;
            }
        }

        if (sequence instanceof CoordinateAccess) {
            CoordinateAccess access = (CoordinateAccess) sequence;
            double[][] coordArray = access.toOrdinateArrays();
            Object[] attributeArray = access.toAttributeArrays();

            double[][] subCoordArray = new double[access.getDimension()][];
            Object[][] subAttributeArray = new Object[access.getNumAttributes()][];

            //							System.out.println("Dimension = " + access.getDimension());
            //							System.out.println("coordArray.length = " + coordArray.length);
            //							System.out.println("fromIndex= " + fromIndex + ", toIndex= " + toIndex);
            //							System.out.println("coordArray: ");
            //							System.out.print("X   ");
            //							for (int p=0; p<coordArray[0].length; p++)
            //								System.out.print(coordArray[0][p] + " ");
            //							System.out.print("\nY   ");
            //							for (int p=0; p<coordArray[1].length; p++)
            //								System.out.print(coordArray[1][p] + " ");
            //							System.out.println("");
            //								
            //							System.out.println("Num attributes = " + access.getNumAttributes());
            //							System.out.println("attributeArray.length = " + attributeArray.length);
            //							System.out.println("attributeArray: ");
            //							System.out.print("Z   ");
            //							for (int p=0; p<attributeArray[0].length; p++)
            //								System.out.print(attributeArray[0][p] + " ");
            //							System.out.print("\nT   ");
            //							for (int p=0; p<attributeArray[1].length; p++)
            //								System.out.print(attributeArray[1][p] + " ");
            //							System.out.println("");
            //			try
            //			{
            for (int i = 0; i < access.getDimension(); i++) {
                subCoordArray[i] = new OrdinateList(coordArray[i], 0, 1,
                        fromIndex, toIndex).toDoubleArray();
            }

            //			}
            //			catch (ArrayIndexOutOfBoundsException e)
            //			{
            //				e.printStackTrace();
            //				System.out.println("Dimension = " + access.getDimension());
            //				System.out.println("coordArray.length = " + coordArray.length);
            //				System.out.println("fromIndex= " + fromIndex + ", toIndex= " + toIndex);
            //				System.out.println("coordArray: ");
            //				System.out.print("X   ");
            //				for (int p=0; p<coordArray[0].length; p++)
            //					System.out.print(coordArray[0][p] + " ");
            //				System.out.print("\nY   ");
            //				for (int p=0; p<coordArray[1].length; p++)
            //					System.out.print(coordArray[1][p] + " ");
            //				System.out.println("");
            //			}
            for (int i = 0; i < access.getNumAttributes(); i++) {
                subAttributeArray[i] = new AttributeList(attributeArray[i], 0,
                        1, fromIndex, toIndex).toObjectArray();
            }

            System.out.println("subCoordArray.length = " + subCoordArray.length);
            System.out.println("subCoordArray: ");
            System.out.print("X   ");

            for (int p = 0; p < subCoordArray[0].length; p++)
                System.out.print(subCoordArray[0][p] + " ");

            System.out.print("\nY   ");

            for (int p = 0; p < subCoordArray[1].length; p++)
                System.out.print(subCoordArray[1][p] + " ");

            System.out.println("");

            System.out.println("subAttributeArray.length = "
                + subAttributeArray.length);
            System.out.println("subAttributeArray: ");
            System.out.print("Z   ");

            for (int p = 0; p < subAttributeArray[0].length; p++)
                System.out.print(subAttributeArray[0][p] + " ");

            System.out.print("\nT   ");

            for (int p = 0; p < subAttributeArray[1].length; p++)
                System.out.print(subAttributeArray[1][p] + " ");

            System.out.println("");

            CoordinateAccess c = (CoordinateAccess) ((CoordinateAccessFactory) factory)
                .create(subCoordArray, subAttributeArray);

            return c;
        }

        Coordinate[] array = new Coordinate[toIndex - fromIndex];
        int index = 0;
        for(int i = fromIndex; i < toIndex; i++, index++) {
            array[index] = sequence.getCoordinate(i);
        }

        return factory.create(array);
    }

    /**
     * DOCUMENT ME!
     *
     * @param factory
     * @param sequence
     *
     */
    public static CoordinateSequence reverse(
        CoordinateSequenceFactory factory, CoordinateSequence sequence) {
        if (sequence instanceof CoordinateAccess) {
            CoordinateAccess access = (CoordinateAccess) sequence;
            double[][] coordArray = access.toOrdinateArrays();
            Object[] attributeArray = access.toAttributeArrays();

            double[][] subCoordArray = new double[access.getDimension()][];
            Object[][] subAttributeArray = new Object[access.getNumAttributes()][];

            for (int i = 0; i < access.getDimension(); i++) {
                subCoordArray[i] = new OrdinateList(coordArray[i], 0, 1,
                        access.size() - 1, -1).toDoubleArray();
            }

            for (int i = 0; i < access.getNumAttributes(); i++) {
                subAttributeArray[i] = new AttributeList(attributeArray[i], 0,
                        1, access.size() - 1, -1).toObjectArray();
            }

            CoordinateAccess c = (CoordinateAccess) ((CoordinateAccessFactory) factory)
                .create(subCoordArray, subAttributeArray);

            return c;
        } else // else CoordinateSequence
         {
            CoordinateList list = new CoordinateList(sequence.toCoordinateArray());
            Collections.reverse(list);

            return factory.create(list.toCoordinateArray());
        }
    }

    public static String toString(CoordinateSequence cs, int coordinate,
        NumberFormat nf) {
        StringBuffer buf = new StringBuffer();
        append(buf, cs, coordinate, nf);

        return buf.toString();
    }

    public static void append(StringBuffer buf, CoordinateSequence cs,
        int coordinate, NumberFormat nf) {
        if (cs instanceof CoordinateAccess) {
            CoordinateAccess ca = (CoordinateAccess) cs;
            append(buf, ca, coordinate, LEN(ca), nf);
        } else {
            append(buf, cs, coordinate, LEN(cs), nf);
        }
    }

    public static void append(StringBuffer buf, CoordinateSequence cs,
        int coordinate, int LEN, NumberFormat nf) {
        Coordinate c = cs.getCoordinate(coordinate);
        buf.append(nf.format(c.x));
        buf.append(" ");
        buf.append(nf.format(c.y));

        if (LEN == 3) {
            buf.append(" ");
            buf.append(nf.format(c.z));
        }
    }

    public static void append(StringBuffer buf, CoordinateAccess ca,
        int coordinate, int LEN, NumberFormat nf) {
        buf.append(nf.format(ca.getOrdinate(coordinate, 0)));

        for (int i = 1; i < LEN; i++) {
            buf.append(" ");
            buf.append(nf.format(ca.getOrdinate(coordinate, i)));
        }
    }

    public static int LEN(CoordinateSequence cs) {
        return D(cs) + L(cs);
    }

    public static int D(CoordinateSequence cs) {
        if (cs instanceof CoordinateAccess) {
            return ((CoordinateAccess) cs).getDimension();
        }

        if (cs.size() > 0) {
            return Double.isNaN(cs.getCoordinate(0).z) ? 2 : 3;
        }

        return 3;
    }

    public static int L(CoordinateSequence cs) {
        if (cs instanceof CoordinateAccess) {
            return ((CoordinateAccess) cs).getNumAttributes();
        }

        return 0;
    }

    public static NumberFormat format(PrecisionModel pm) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setNaN("NaN");

        DecimalFormat f = new DecimalFormat();
        f.setDecimalFormatSymbols(symbols);

        if (pm == null) {
            f.setMaximumFractionDigits(0);

            return f;
        }

        f.setMinimumFractionDigits(0);
        f.setMaximumFractionDigits(pm.getMaximumSignificantDigits());

        return f;
    }

    public static String toString(CoordinateSequence cs, PrecisionModel pm) {
        StringBuffer buf = new StringBuffer();
        append(buf, cs, format(pm));

        return buf.toString();
    }

    public static void append(StringBuffer buf, CoordinateSequence cs,
        NumberFormat nf) {
        if (cs instanceof CoordinateAccess) {
            append(buf, (CoordinateAccess) cs, nf);
        } else {
            int LEN = LEN(cs); // 2 or 3

            if (cs.size() == 0) {
                return;
            }

            append(buf, cs, 0, LEN, nf);

            if (cs.size() == 1) {
                return;
            }

            for (int i = 1; i < cs.size(); i++) {
                buf.append(", ");
                append(buf, cs, i, LEN, nf);
            }
        }
    }

    public static void append(StringBuffer buf, CoordinateAccess ca,
        NumberFormat nf) {
        int LEN = LEN(ca);

        if (ca.size() == 0) {
            return;
        }

        append(buf, ca, 0, LEN, nf);

        if (ca.size() == 1) {
            return;
        }

        for (int i = 1; i < ca.size(); i++) {
            buf.append(", ");
            append(buf, ca, i, LEN, nf);
        }
    }
}
