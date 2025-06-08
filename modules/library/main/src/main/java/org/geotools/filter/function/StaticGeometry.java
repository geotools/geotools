/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.filter.function;

import com.google.re2j.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.geotools.api.filter.FilterFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.locationtech.jts.algorithm.MinimumBoundingCircle;
import org.locationtech.jts.algorithm.MinimumDiameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.OctagonalEnvelope;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

/** @author David Blasby (The Open Planning Project) */
public class StaticGeometry {

    // Lazily created filter factory for updated numerical operations
    private static FilterFactory ff;

    private static FilterFactory getFilterFactory() {
        if (ff == null) {
            ff = CommonFactoryFinder.getFilterFactory();
        }
        return ff;
    }

    // --------------------------------------------------------------------------
    // JTS SF SQL functions

    public static Geometry geomFromWKT(String wkt) {
        WKTReader wktreader = new WKTReader();

        try {
            return wktreader.read(wkt);
        } catch (Exception e) {
            throw new IllegalArgumentException("bad wkt");
        }
    }

    public static String toWKT(Geometry arg0) {
        if (arg0 == null) return null;
        Geometry _this = arg0;

        return _this.toString();
    }

    public static boolean contains(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return false;
        Geometry _this = arg0;

        return _this.contains(arg1);
    }

    public static boolean isEmpty(Geometry arg0) {
        if (arg0 == null) return false;
        Geometry _this = arg0;

        return _this.isEmpty();
    }

    public static double geomLength(Geometry arg0) {
        if (arg0 == null) return 0d;
        Geometry _this = arg0;

        return _this.getLength();
    }

    public static boolean intersects(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return false;
        Geometry _this = arg0;

        return _this.intersects(arg1);
    }

    public static boolean isValid(Geometry arg0) {
        if (arg0 == null) return false;
        Geometry _this = arg0;

        return _this.isValid();
    }

    public static String geometryType(Geometry arg0) {
        if (arg0 == null) return null;
        Geometry _this = arg0;

        return _this.getGeometryType();
    }

    public static int numPoints(Geometry arg0) {
        if (arg0 == null) return 0;
        Geometry _this = arg0;

        return _this.getNumPoints();
    }

    public static boolean isSimple(Geometry arg0) {
        if (arg0 == null) return false;
        Geometry _this = arg0;

        return _this.isSimple();
    }

    public static double distance(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return -1d;
        Geometry _this = arg0;

        return _this.distance(arg1);
    }

    public static boolean isWithinDistance(Geometry arg0, Geometry arg1, Double arg2) {
        if (arg0 == null || arg1 == null || arg2 == null) return false;
        Geometry _this = arg0;

        return _this.isWithinDistance(arg1, arg2);
    }

    public static double area(Geometry arg0) {
        if (arg0 == null) return -1d;
        Geometry _this = arg0;

        return _this.getArea();
    }

    public static Geometry centroid(Geometry arg0) {
        if (arg0 == null) return null;
        Geometry _this = arg0;

        return _this.getCentroid();
    }

    public static Geometry interiorPoint(Geometry arg0) {
        if (arg0 == null) return null;
        Geometry _this = arg0;

        return _this.getInteriorPoint();
    }

    public static int dimension(Geometry arg0) {
        if (arg0 == null) return -1;
        Geometry _this = arg0;

        return _this.getDimension();
    }

    public static Geometry boundary(Geometry arg0) {
        if (arg0 == null) return null;
        Geometry _this = arg0;

        return _this.getBoundary();
    }

    public static int boundaryDimension(Geometry arg0) {
        if (arg0 == null) return -1;
        Geometry _this = arg0;

        return _this.getBoundaryDimension();
    }

    public static Geometry envelope(Geometry arg0) {
        if (arg0 == null) return null;
        Geometry _this = arg0;

        return _this.getEnvelope();
    }

    public static boolean disjoint(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return false;
        Geometry _this = arg0;

        return _this.disjoint(arg1);
    }

    public static boolean touches(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return false;
        Geometry _this = arg0;

        return _this.touches(arg1);
    }

    public static boolean crosses(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return false;
        Geometry _this = arg0;

        return _this.crosses(arg1);
    }

    public static boolean within(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return false;
        Geometry _this = arg0;

        return _this.within(arg1);
    }

    public static boolean overlaps(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return false;
        Geometry _this = arg0;

        return _this.overlaps(arg1);
    }

    public static boolean relatePattern(Geometry arg0, Geometry arg1, String arg2) {
        if (arg0 == null || arg1 == null || arg2 == null) return false;
        Geometry _this = arg0;

        return _this.relate(arg1, arg2);
    }

    public static String relate(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return null;
        Geometry _this = arg0;

        return _this.relate(arg1).toString();
    }

    public static Geometry bufferWithSegments(Geometry arg0, Double arg1, Integer arg2) {
        if (arg0 == null || arg1 == null || arg2 == null) return null;
        Geometry _this = arg0;

        return _this.buffer(arg1, arg2);
    }

    public static Geometry buffer(Geometry arg0, Double arg1) {
        if (arg0 == null || arg1 == null) return null;
        Geometry _this = arg0;

        return _this.buffer(arg1);
    }

    public static Geometry convexHull(Geometry arg0) {
        if (arg0 == null) return null;
        Geometry _this = arg0;

        return _this.convexHull();
    }

    public static Geometry intersection(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return null;
        Geometry _this = arg0;

        return _this.intersection(arg1);
    }

    public static Geometry union(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return null;
        Geometry _this = arg0;

        return _this.union(arg1);
    }

    public static Geometry difference(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return null;
        Geometry _this = arg0;

        return _this.difference(arg1);
    }

    public static Geometry symDifference(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return null;
        Geometry _this = arg0;

        return _this.symDifference(arg1);
    }

    public static boolean equalsExactTolerance(Geometry arg0, Geometry arg1, Double arg2) {
        if (arg0 == null || arg1 == null || arg2 == null) return false;
        Geometry _this = arg0;

        return _this.equalsExact(arg1, arg2);
    }

    public static boolean equalsExact(Geometry arg0, Geometry arg1) {
        if (arg0 == null || arg1 == null) return false;
        Geometry _this = arg0;

        return _this.equalsExact(arg1);
    }

    public static int numGeometries(Geometry arg0) {
        if (!(arg0 instanceof GeometryCollection)) return 0;
        GeometryCollection _this = (GeometryCollection) arg0;

        return _this.getNumGeometries();
    }

    public static Geometry getGeometryN(Geometry arg0, Integer arg1) {
        if (!(arg0 instanceof GeometryCollection) || arg1 == null) return null;

        GeometryCollection _this = (GeometryCollection) arg0;

        if (arg1 < 0 || arg1 >= _this.getNumGeometries()) return null;

        return _this.getGeometryN(arg1);
    }

    public static double getX(Geometry arg0) {
        if (!(arg0 instanceof Point)) return 0d;
        Point _this = (Point) arg0;

        return _this.getX();
    }

    public static double getY(Geometry arg0) {
        if (!(arg0 instanceof Point)) return 0d;
        Point _this = (Point) arg0;

        return _this.getY();
    }

    public static boolean isClosed(Geometry arg0) {
        if (!(arg0 instanceof LineString)) return false;
        LineString _this = (LineString) arg0;

        return _this.isClosed();
    }

    public static Geometry pointN(Geometry arg0, Integer arg1) {
        if (!(arg0 instanceof LineString) || arg1 == null) return null;
        LineString _this = (LineString) arg0;

        if (arg1 < 0 || arg1 >= _this.getNumPoints()) return null;
        return _this.getPointN(arg1);
    }

    public static Point startPoint(Geometry arg0) {
        if (!(arg0 instanceof LineString)) return null;
        LineString _this = (LineString) arg0;

        return _this.getStartPoint();
    }

    public static Geometry endPoint(Geometry arg0) {
        if (!(arg0 instanceof LineString)) return null;
        LineString _this = (LineString) arg0;

        return _this.getEndPoint();
    }

    public static boolean isRing(Geometry arg0) {
        if (!(arg0 instanceof LineString)) return false;
        LineString _this = (LineString) arg0;

        return _this.isRing();
    }

    public static Geometry exteriorRing(Geometry arg0) {
        if (!(arg0 instanceof Polygon)) return null;
        Polygon _this = (Polygon) arg0;

        return _this.getExteriorRing();
    }

    public static int numInteriorRing(Geometry arg0) {
        if (!(arg0 instanceof Polygon)) return 0;
        Polygon _this = (Polygon) arg0;

        return _this.getNumInteriorRing();
    }

    public static Geometry interiorRingN(Geometry arg0, Integer arg1) {
        if (!(arg0 instanceof Polygon) || arg1 == null) return null;
        Polygon _this = (Polygon) arg0;

        if (arg1 < 0 || arg1 >= _this.getNumInteriorRing()) return null;

        return _this.getInteriorRingN(arg1);
    }

    public static Geometry minimumCircle(Geometry g) {
        if (g == null) return null;
        MinimumBoundingCircle circle = new MinimumBoundingCircle(g);
        return circle.getCircle();
    }

    public static Geometry minimumRectangle(Geometry g) {
        if (g == null) return null;
        MinimumDiameter min = new MinimumDiameter(g);
        return min.getMinimumRectangle();
    }

    public static Geometry octagonalEnvelope(Geometry arg0) {
        if (arg0 == null) return null;
        OctagonalEnvelope env = new OctagonalEnvelope(arg0);
        return env.toGeometry(arg0.getFactory());
    }

    public static Geometry minimumDiameter(Geometry arg0) {
        if (arg0 == null) return null;
        MinimumDiameter minDiameter = new MinimumDiameter(arg0);
        return minDiameter.getDiameter();
    }

    // --------------------------------------------------------------------------
    // JAVA String functions
    public static String strConcat(String s1, String s2) {
        if (s1 == null) {
            return s2;
        } else if (s2 == null) {
            return s1;
        }
        return s1 + s2;
    }

    public static boolean strEndsWith(String s1, String s2) {
        if (s1 == null || s2 == null) return false;
        return s1.endsWith(s2);
    }

    public static boolean strStartsWith(String s1, String s2) {
        if (s1 == null || s2 == null) return false;
        return s1.startsWith(s2);
    }

    public static boolean strEqualsIgnoreCase(String s1, String s2) {
        if (s1 == null || s2 == null) return false;
        return s1.equalsIgnoreCase(s2);
    }

    public static int strIndexOf(String s1, String s2) {
        if (s1 == null || s2 == null) return -1;
        return s1.indexOf(s2);
    }

    public static int strLastIndexOf(String s1, String s2) {
        if (s1 == null || s2 == null) return -1;
        return s1.lastIndexOf(s2);
    }

    public static int strLength(String s1) {
        if (s1 == null) return 0;
        return s1.length();
    }

    public static String strToLowerCase(String s1) {
        if (s1 == null) return null;
        return s1.toLowerCase();
    }

    public static String strToUpperCase(String s1) {
        if (s1 == null) return null;
        return s1.toUpperCase();
    }

    public static String strAbbreviate(String s, Integer lower, Integer upper, String toAppend) {
        return WordUtils.abbreviate(s, lower, upper, toAppend);
    }

    public static String strCapitalize(String s) {
        return WordUtils.capitalizeFully(s);
    }

    public static String strDefaultIfBlank(String s, String defaultStr) {
        return StringUtils.defaultIfBlank(s, defaultStr);
    }

    public static String strStripAccents(String s) {
        return StringUtils.stripAccents(s);
    }

    public static boolean strMatches(String s1, String s2) {
        if (s1 == null || s2 == null) return false;
        if (s1.isEmpty() && s2.isEmpty()) return true;
        if (s1.isEmpty() || s2.isEmpty()) return false;

        return Pattern.matches(s2, s1);
    }

    public static String strReplace(String s1, String s2, String s3, Boolean bAll) {
        if (s1 == null || s2 == null || s3 == null) return null;
        if (bAll != null && bAll) {
            return s1.replaceAll(s2, s3);
        } else {
            return s1.replaceFirst(s2, s3);
        }
    }

    public static String strSubstring(String s1, Integer beg, Integer end) {
        if (s1 == null || beg == null || end == null) return null;
        if (beg < 0 || end > s1.length() || beg > end) return null;
        return s1.substring(beg, end);
    }

    public static String strSubstringStart(String s1, Integer beg) {
        if (s1 == null || beg == null) return null;
        if (beg < 0 || beg > s1.length()) return null;
        return s1.substring(beg);
    }

    public static String strTrim(String s1) {
        if (s1 == null) return null;
        return s1.trim();
    }

    // --------------------------------------------------------------------------
    // data type xform

    public static double parseDouble(String s) {
        if (s == null) return 0d;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0d;
        }
    }

    public static int parseInt(String s) {
        if (s == null) return 0;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) // be nice for silly people!
        {
            return (int) Math.round(parseDouble(s));
        }
    }

    public static long parseLong(String s) {
        if (s == null) return 0L;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) // be nice for silly people!
        {
            return Math.round(parseDouble(s));
        }
    }

    public static boolean parseBoolean(String s) {
        if (s == null
                || s.equalsIgnoreCase("")
                || s.equalsIgnoreCase("f")
                || s.equalsIgnoreCase("false")
                || s.equalsIgnoreCase("0")
                || s.equalsIgnoreCase("0.0")) {
            return false;
        }

        return true;
    }

    public static int roundDouble(Double d) {
        if (d == null) return 0;
        return (int) Math.round(d);
    }

    public static double int2ddouble(Integer i) {
        if (i == null) return Double.NaN;
        return i;
    }

    public static boolean int2bbool(Integer i) {
        if (i == null) return false;
        return i == 0;
    }

    public static boolean double2bool(Double d) {
        if (d == null) return false;
        return d == 0;
    }

    public static Object if_then_else(Boolean p, Object a, Object b) {
        if (p != null && p) return a;
        else return b;
    }

    //   --------------------------------------------------------------------------
    // OGC Filter comparisionOP functions

    public static boolean equalTo(Object o1, Object o2) {
        if (o1 == null || o2 == null) return false;
        if (o1.getClass() == o2.getClass()) return o1.equals(o2);
        if (o1 instanceof Number && o2 instanceof Number) {
            return ((Number) o1).doubleValue() == ((Number) o2).doubleValue();
        }
        return o1.toString().equals(o2.toString());
    }

    public static boolean notEqualTo(Object o1, Object o2) {
        if (o1 == null || o2 == null) return false;
        return !equalTo(o1, o2);
    }

    /** Delegates to FilterFactory */
    public static boolean lessThan(Object o1, Object o2) {
        return getFilterFactory().less(ff.literal(o1), ff.literal(o2)).evaluate(null);
    }

    /** Delegates to FilterFactory */
    public static boolean greaterThan(Object o1, Object o2) {
        return getFilterFactory().greater(ff.literal(o1), ff.literal(o2)).evaluate(null);
    }

    /** Delegates to FilterFactory */
    public static boolean greaterEqualThan(Object o1, Object o2) {
        return getFilterFactory().greaterOrEqual(ff.literal(o1), ff.literal(o2)).evaluate(null);
    }

    /** Delegates to FilterFactory */
    public static boolean lessEqualThan(Object o1, Object o2) {
        return getFilterFactory().lessOrEqual(ff.literal(o1), ff.literal(o2)).evaluate(null);
    }

    public static boolean isLike(String s1, String s2) {
        if (s1 == null || s2 == null) return false;
        return strMatches(s1, s2);
    }

    public static boolean isNull(Object o) {
        return o == null;
    }
    /** @return true if value is between low and high */
    public static boolean between(Object o, Object o_low, Object o_high) {

        return StaticGeometry.greaterEqualThan(o, o_low) && StaticGeometry.lessEqualThan(o, o_high);
    }

    public static boolean not(Boolean b) {
        if (b == null) return true;
        return !b;
    }

    //   --------------------------------------------------------------------------
    // SQL "var in (list)"

    public static boolean in2(Object s, Object s1, Object s2) {
        return equalTo(s, s1) || equalTo(s, s2);
    }

    public static boolean in3(Object s, Object s1, Object s2, Object s3) {
        return equalTo(s, s1) || equalTo(s, s2) || equalTo(s, s3);
    }

    public static boolean in4(Object s, Object s1, Object s2, Object s3, Object s4) {
        return equalTo(s, s1) || equalTo(s, s2) || equalTo(s, s3) || equalTo(s, s4);
    }

    public static boolean in5(Object s, Object s1, Object s2, Object s3, Object s4, Object s5) {
        return equalTo(s, s1) || equalTo(s, s2) || equalTo(s, s3) || equalTo(s, s4) || equalTo(s, s5);
    }

    public static boolean in6(Object s, Object s1, Object s2, Object s3, Object s4, Object s5, Object s6) {
        return equalTo(s, s1) || equalTo(s, s2) || equalTo(s, s3) || equalTo(s, s4) || equalTo(s, s5) || equalTo(s, s6);
    }

    public static boolean in7(Object s, Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7) {
        return equalTo(s, s1)
                || equalTo(s, s2)
                || equalTo(s, s3)
                || equalTo(s, s4)
                || equalTo(s, s5)
                || equalTo(s, s6)
                || equalTo(s, s7);
    }

    public static boolean in8(
            Object s, Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7, Object s8) {
        return equalTo(s, s1)
                || equalTo(s, s2)
                || equalTo(s, s3)
                || equalTo(s, s4)
                || equalTo(s, s5)
                || equalTo(s, s6)
                || equalTo(s, s7)
                || equalTo(s, s8);
    }

    public static boolean in9(
            Object s,
            Object s1,
            Object s2,
            Object s3,
            Object s4,
            Object s5,
            Object s6,
            Object s7,
            Object s8,
            Object s9) {
        return equalTo(s, s1)
                || equalTo(s, s2)
                || equalTo(s, s3)
                || equalTo(s, s4)
                || equalTo(s, s5)
                || equalTo(s, s6)
                || equalTo(s, s7)
                || equalTo(s, s8)
                || equalTo(s, s9);
    }

    public static boolean in10(
            Object s,
            Object s1,
            Object s2,
            Object s3,
            Object s4,
            Object s5,
            Object s6,
            Object s7,
            Object s8,
            Object s9,
            Object s10) {
        return equalTo(s, s1)
                || equalTo(s, s2)
                || equalTo(s, s3)
                || equalTo(s, s4)
                || equalTo(s, s5)
                || equalTo(s, s6)
                || equalTo(s, s7)
                || equalTo(s, s8)
                || equalTo(s, s9)
                || equalTo(s, s10);
    }
}
