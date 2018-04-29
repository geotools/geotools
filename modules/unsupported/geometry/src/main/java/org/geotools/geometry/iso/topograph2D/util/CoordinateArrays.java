/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2006  Vivid Solutions
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.topograph2D.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.CoordinateList;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Useful utility functions for handling Coordinate arrays
 *
 * @source $URL$
 */
public class CoordinateArrays {

    private static final Coordinate[] coordArrayType = new Coordinate[0];

    /**
     * Finds a point in a list of points which is not contained in another list of points
     *
     * @param testPts the {@link Coordinate}s to test
     * @param pts an array of {@link Coordinate}s to test the input points against
     * @return a {@link Coordinate} from <code>testPts</code> which is not in <code>pts</code>, ' or
     *     <code>null</code>
     */
    public static Coordinate ptNotInList(Coordinate[] testPts, Coordinate[] pts) {
        for (int i = 0; i < testPts.length; i++) {
            Coordinate testPt = testPts[i];
            if (CoordinateArrays.indexOf(testPt, pts) < 0) return testPt;
        }
        return null;
    }

    /**
     * Compares two {@link Coordinate} arrays in the forward direction of their coordinates, using
     * lexicographic ordering.
     *
     * @param pts1
     * @param pts2
     * @return
     */
    public static int compare(Coordinate[] pts1, Coordinate[] pts2) {
        int i = 0;
        while (i < pts1.length && i < pts2.length) {
            int compare = pts1[i].compareTo(pts2[i]);
            if (compare != 0) return compare;
            i++;
        }
        // handle situation when arrays are of different length
        if (i < pts2.length) return -1;
        if (i < pts1.length) return 1;

        return 0;
    }

    /**
     * A {@link Comparator} for {@link Coordinate} arrays in the forward direction of their
     * coordinates, using lexicographic ordering.
     */
    public static class ForwardComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            Coordinate[] pts1 = (Coordinate[]) o1;
            Coordinate[] pts2 = (Coordinate[]) o2;

            return CoordinateArrays.compare(pts1, pts2);
        }
    }

    /**
     * Determines which orientation of the {@link Coordinate} array is (overall) increasing. In
     * other words, determines which end of the array is "smaller" (using the standard ordering on
     * {@link Coordinate}). Returns an integer indicating the increasing direction. If the sequence
     * is a palindrome, it is defined to be oriented in a positive direction.
     *
     * @param pts the array of Coordinates to test
     * @return <code>1</code> if the array is smaller at the start or is a palindrome, <code>-1
     *     </code> if smaller at the end
     */
    public static int increasingDirection(Coordinate[] pts) {
        for (int i = 0; i < pts.length / 2; i++) {
            int j = pts.length - 1 - i;
            // skip equal points on both ends
            int comp = pts[i].compareTo(pts[j]);
            if (comp != 0) return comp;
        }
        // array must be a palindrome - defined to be in positive direction
        return 1;
    }

    /**
     * Determines whether two {@link Coordinate} arrays of equal length are equal in opposite
     * directions.
     *
     * @param pts1
     * @param pts2
     * @return <code>true</code> if the two arrays are equal in opposite directions.
     */
    private static boolean isEqualReversed(Coordinate[] pts1, Coordinate[] pts2) {
        for (int i = 0; i < pts1.length; i++) {
            Coordinate p1 = pts1[i];
            Coordinate p2 = pts2[pts1.length - i - 1];
            if (p1.compareTo(p2) != 0) return false;
        }
        return true;
    }

    /**
     * A {@link Comparator} for {@link Coordinate} arrays modulo their directionality. E.g. if two
     * coordinate arrays are identical but reversed they will compare as equal under this ordering.
     * If the arrays are not equal, the ordering returned is the ordering in the forward direction.
     */
    public static class BidirectionalComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            Coordinate[] pts1 = (Coordinate[]) o1;
            Coordinate[] pts2 = (Coordinate[]) o2;

            if (pts1.length < pts2.length) return -1;
            if (pts1.length > pts2.length) return 1;

            if (pts1.length == 0) return 0;

            int forwardComp = CoordinateArrays.compare(pts1, pts2);
            boolean isEqualRev = isEqualReversed(pts1, pts2);
            if (isEqualRev) return 0;
            return forwardComp;
        }

        public int OLDcompare(Object o1, Object o2) {
            Coordinate[] pts1 = (Coordinate[]) o1;
            Coordinate[] pts2 = (Coordinate[]) o2;

            if (pts1.length < pts2.length) return -1;
            if (pts1.length > pts2.length) return 1;

            if (pts1.length == 0) return 0;

            int dir1 = increasingDirection(pts1);
            int dir2 = increasingDirection(pts2);

            int i1 = dir1 > 0 ? 0 : pts1.length - 1;
            int i2 = dir2 > 0 ? 0 : pts1.length - 1;

            for (int i = 0; i < pts1.length; i++) {
                int comparePt = pts1[i1].compareTo(pts2[i2]);
                if (comparePt != 0) return comparePt;
                i1 += dir1;
                i2 += dir2;
            }
            return 0;
        }
    }

    /**
     * Creates a deep copy of the argument {@link Coordinate) array.
     *
     * @param coordinates
     *            an array of Coordinates
     * @return a deep copy of the input
     */
    public static Coordinate[] copyDeep(Coordinate[] coordinates) {
        Coordinate[] copy = new Coordinate[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            copy[i] = new Coordinate(coordinates[i]);
        }
        return copy;
    }

    /** Converts the given Collection of Coordinates into a Coordinate array. */
    public static Coordinate[] toCoordinateArray(Collection coordList) {
        return (Coordinate[]) coordList.toArray(coordArrayType);
    }

    /**
     * Returns whether #equals returns true for any two consecutive Coordinates in the given array.
     */
    public static boolean hasRepeatedPoints(Coordinate[] coord) {
        for (int i = 1; i < coord.length; i++) {
            if (coord[i - 1].equals(coord[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns either the given coordinate array if its length is greater than the given amount, or
     * an empty coordinate array.
     */
    public static Coordinate[] atLeastNCoordinatesOrNothing(int n, Coordinate[] c) {
        return c.length >= n ? c : new Coordinate[] {};
    }

    /**
     * If the coordinate array argument has repeated points, constructs a new array containing no
     * repeated points. Otherwise, returns the argument.
     *
     * @see #hasRepeatedPoints(Coordinate[])
     */
    public static Coordinate[] removeRepeatedPoints(Coordinate[] coord) {
        if (!hasRepeatedPoints(coord)) return coord;
        CoordinateList coordList = new CoordinateList(coord, false);
        return coordList.toCoordinateArray();
    }

    /** Reverses the coordinates in an array in-place. */
    public static void reverse(Coordinate[] coord) {
        int last = coord.length - 1;
        int mid = last / 2;
        for (int i = 0; i <= mid; i++) {
            Coordinate tmp = coord[i];
            coord[i] = coord[last - i];
            coord[last - i] = tmp;
        }
    }

    /**
     * Returns true if the two arrays are identical, both null, or pointwise equal (as compared
     * using Coordinate#equals)
     *
     * @see Coordinate#equals(Object)
     */
    public static boolean equals(Coordinate[] coord1, Coordinate[] coord2) {
        if (coord1 == coord2) return true;
        if (coord1 == null || coord2 == null) return false;
        if (coord1.length != coord2.length) return false;
        for (int i = 0; i < coord1.length; i++) {
            if (!coord1[i].equals(coord2[i])) return false;
        }
        return true;
    }

    /**
     * Returns true if the two arrays are identical, both null, or pointwise equal, using a
     * user-defined {@link Comparator} for {@link Coordinate} s
     *
     * @param coord1 an array of Coordinates
     * @param coord2 an array of Coordinates
     * @param coordinateComparator a Comparator for Coordinates
     */
    public static boolean equals(
            Coordinate[] coord1, Coordinate[] coord2, Comparator coordinateComparator) {
        if (coord1 == coord2) return true;
        if (coord1 == null || coord2 == null) return false;
        if (coord1.length != coord2.length) return false;
        for (int i = 0; i < coord1.length; i++) {
            if (coordinateComparator.compare(coord1[i], coord2[i]) != 0) return false;
        }
        return true;
    }

    /**
     * Returns the minimum coordinate, using the usual lexicographic comparison.
     *
     * @param coordinates the array to search
     * @return the minimum coordinate in the array, found using <code>compareTo</code>
     * @see Coordinate#compareTo(Object)
     */
    public static Coordinate minCoordinate(Coordinate[] coordinates) {
        Coordinate minCoord = null;
        for (int i = 0; i < coordinates.length; i++) {
            if (minCoord == null || minCoord.compareTo(coordinates[i]) > 0) {
                minCoord = coordinates[i];
            }
        }
        return minCoord;
    }

    /**
     * Shifts the positions of the coordinates until <code>firstCoordinate</code> is first.
     *
     * @param coordinates the array to rearrange
     * @param firstCoordinate the coordinate to make first
     */
    public static void scroll(Coordinate[] coordinates, Coordinate firstCoordinate) {
        int i = indexOf(firstCoordinate, coordinates);
        if (i < 0) return;
        Coordinate[] newCoordinates = new Coordinate[coordinates.length];
        System.arraycopy(coordinates, i, newCoordinates, 0, coordinates.length - i);
        System.arraycopy(coordinates, 0, newCoordinates, coordinates.length - i, i);
        System.arraycopy(newCoordinates, 0, coordinates, 0, coordinates.length);
    }

    /**
     * Returns the index of <code>coordinate</code> in <code>coordinates</code>. The first position
     * is 0; the second, 1; etc.
     *
     * @param coordinate the <code>Coordinate</code> to search for
     * @param coordinates the array to search
     * @return the position of <code>coordinate</code>, or -1 if it is not found
     */
    public static int indexOf(Coordinate coordinate, Coordinate[] coordinates) {
        for (int i = 0; i < coordinates.length; i++) {
            if (coordinate.equals(coordinates[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Extracts a subsequence of the input {@link Coordinate} array from indices <code>start</code>
     * to <code>end</code> (inclusive).
     *
     * @param pts the input array
     * @param start the index of the start of the subsequence to extract
     * @param end the index of the end of the subsequence to extract
     * @return a subsequence of the input array
     */
    public static Coordinate[] extract(Coordinate[] pts, int start, int end) {
        int len = end - start + 1;
        Coordinate[] extractPts = new Coordinate[len];
        int iPts = 0;
        for (int i = start; i <= end; i++) {
            extractPts[iPts++] = pts[i];
        }
        return extractPts;
    }

    /**
     * SJ TODO faellt evtl weg, wenn coordinate durch DirectPosition ausgetauscht wird
     *
     * @param aPositions
     * @return
     */
    public static Coordinate[] toCoordinateArray(List<DirectPosition> aPositions) {

        Coordinate[] rCoords = new Coordinate[aPositions.size()];

        for (int i = 0; i < aPositions.size(); i++) {
            rCoords[i] = new Coordinate(aPositions.get(i).getCoordinate());
        }

        return rCoords;
    }

    /**
     * Converts a Coordinate array into a list of Positions
     *
     * <p>SJ: faellt evtl weg, wenn coordinate durch DirectPosition ausgetauscht wird
     *
     * @param coordArray
     * @return
     */
    public static List<Position> toPositionList(
            CoordinateReferenceSystem crs, Coordinate[] coordArray) {
        List<Position> rList = new ArrayList<Position>();
        for (int i = 0; i < coordArray.length; i++) {
            DirectPositionImpl position =
                    new DirectPositionImpl(crs, coordArray[i].getCoordinates());
            rList.add(new PositionImpl((DirectPosition) position.clone()));
            // rList.add(coordinateFactory.createPosition(coordArray[i].getCoordinates()));
        }
        return rList;
    }

    /**
     * Converts a Coordinate array into a list of DirectPositions
     *
     * @param crs
     * @param coordArray
     * @return
     */
    public static List<DirectPositionImpl> toDirectPositionList(
            CoordinateReferenceSystem crs, Coordinate[] coordArray) {
        List<DirectPositionImpl> rList = new ArrayList<DirectPositionImpl>();
        for (int i = 0; i < coordArray.length; i++) {
            rList.add(new DirectPositionImpl(crs, coordArray[i].getCoordinates()));
        }
        return rList;
    }
}
