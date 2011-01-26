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
 */
package org.geotools.filter.function;

import java.util.Collection;

import com.vividsolutions.jts.operation.polygonize.Polygonizer;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class StaticGeometry {

    // --------------------------------------------------------------------------
    // JTS SF SQL functions

    static public Geometry geomFromWKT(String wkt) {
        WKTReader wktreader = new WKTReader();

        try {
            return wktreader.read(wkt);
        } catch (Exception e) {
            throw new IllegalArgumentException("bad wkt");
        }
    }

    static public String toWKT(Geometry arg0) {
        Geometry _this = arg0;

        return _this.toString();
    }

    static public boolean contains(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.contains(arg1);
    }

    static public boolean isEmpty(Geometry arg0) {
        Geometry _this = arg0;

        return _this.isEmpty();
    }

    static public double geomLength(Geometry arg0) {
        Geometry _this = arg0;

        return _this.getLength();
    }

    static public boolean intersects(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.intersects(arg1);
    }

    static public boolean isValid(Geometry arg0) {
        Geometry _this = arg0;

        return _this.isValid();
    }

    static public String geometryType(Geometry arg0) {
        Geometry _this = arg0;

        return _this.getGeometryType();
    }

    static public int numPoints(Geometry arg0) {
        Geometry _this = arg0;

        return _this.getNumPoints();
    }

    static public boolean isSimple(Geometry arg0) {
        Geometry _this = arg0;

        return _this.isSimple();
    }

    static public double distance(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.distance(arg1);
    }

    static public boolean isWithinDistance(Geometry arg0, Geometry arg1,
            double arg2) {
        Geometry _this = arg0;

        return _this.isWithinDistance(arg1, arg2);
    }

    static public double area(Geometry arg0) {
        Geometry _this = arg0;

        return _this.getArea();
    }

    static public Geometry centroid(Geometry arg0) {
        Geometry _this = arg0;

        return _this.getCentroid();
    }

    static public Geometry interiorPoint(Geometry arg0) {
        Geometry _this = arg0;

        return _this.getInteriorPoint();
    }

    static public int dimension(Geometry arg0) {
        Geometry _this = arg0;

        return _this.getDimension();
    }

    static public Geometry boundary(Geometry arg0) {
        Geometry _this = arg0;

        return _this.getBoundary();
    }

    static public int boundaryDimension(Geometry arg0) {
        Geometry _this = arg0;

        return _this.getBoundaryDimension();
    }

    static public Geometry envelope(Geometry arg0) {
        Geometry _this = arg0;

        return _this.getEnvelope();
    }

    static public boolean disjoint(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.disjoint(arg1);
    }

    static public boolean touches(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.touches(arg1);
    }

    static public boolean crosses(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.crosses(arg1);
    }

    static public boolean within(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.within(arg1);
    }

    static public boolean overlaps(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.overlaps(arg1);
    }

    static public boolean relatePattern(Geometry arg0, Geometry arg1,
            String arg2) {
        Geometry _this = arg0;

        return _this.relate(arg1, arg2);
    }

    static public String relate(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.relate(arg1).toString();
    }

    static public Geometry bufferWithSegments(Geometry arg0, double arg1,
            int arg2) {
        Geometry _this = arg0;

        return _this.buffer(arg1, arg2);
    }

    static public Geometry buffer(Geometry arg0, double arg1) {
        Geometry _this = arg0;

        return _this.buffer(arg1);
    }

    static public Geometry convexHull(Geometry arg0) {
        Geometry _this = arg0;

        return _this.convexHull();
    }

    static public Geometry intersection(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.intersection(arg1);
    }

    static public Geometry union(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.union(arg1);
    }

    static public Geometry difference(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.difference(arg1);
    }

    static public Geometry symDifference(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.symDifference(arg1);
    }

    static public boolean equalsExactTolerance(Geometry arg0, Geometry arg1,
            double arg2) {
        Geometry _this = arg0;

        return _this.equalsExact(arg1, arg2);
    }

    static public boolean equalsExact(Geometry arg0, Geometry arg1) {
        Geometry _this = arg0;

        return _this.equalsExact(arg1);
    }

    static public int numGeometries(Geometry arg0) {
        GeometryCollection _this = (GeometryCollection) arg0;

        return _this.getNumGeometries();
    }

    static public Geometry getGeometryN(Geometry arg0, int arg1) {
        GeometryCollection _this = (GeometryCollection) arg0;

        return _this.getGeometryN(arg1);
    }

    static public double getX(Geometry arg0) {
        Point _this = (Point) arg0;

        return _this.getX();
    }

    static public double getY(Geometry arg0) {
        Point _this = (Point) arg0;

        return _this.getY();
    }

    static public boolean isClosed(Geometry arg0) {
        LineString _this = (LineString) arg0;

        return _this.isClosed();
    }

    static public Geometry pointN(Geometry arg0, int arg1) {
        LineString _this = (LineString) arg0;

        return _this.getPointN(arg1);
    }

    static public Geometry startPoint(Geometry arg0) {
        LineString _this = (LineString) arg0;

        return _this.getStartPoint();
    }

    static public Geometry endPoint(Geometry arg0) {
        LineString _this = (LineString) arg0;

        return _this.getEndPoint();
    }

    static public boolean isRing(Geometry arg0) {
        LineString _this = (LineString) arg0;

        return _this.isRing();
    }

    static public Geometry exteriorRing(Geometry arg0) {
        Polygon _this = (Polygon) arg0;

        return _this.getExteriorRing();
    }

    static public int numInteriorRing(Geometry arg0) {
        Polygon _this = (Polygon) arg0;

        return _this.getNumInteriorRing();
    }

    static public Geometry interiorRingN(Geometry arg0, int arg1) {
        Polygon _this = (Polygon) arg0;

        return _this.getInteriorRingN(arg1);
    }

    // --------------------------------------------------------------------------
    // JAVA String functions

    static public String strConcat(String s1, String s2) {
        return s1 + s2;
    }

    static public boolean strEndsWith(String s1, String s2) {
        return s1.endsWith(s2);
    }

    static public boolean strStartsWith(String s1, String s2) {
        return s1.startsWith(s2);
    }

    static public boolean strEqualsIgnoreCase(String s1, String s2) {
        return s1.equalsIgnoreCase(s2);
    }

    static public int strIndexOf(String s1, String s2) {
        return s1.indexOf(s2);
    }

    static public int strLastIndexOf(String s1, String s2) {
        return s1.lastIndexOf(s2);
    }

    static public int strLength(String s1) {
        return s1.length();
    }

    static public boolean strMatches(String s1, String s2) {
        return s1.matches(s2);
    }

    static public String strSubstring(String s1, int beg, int end) {
        return s1.substring(beg, end);
    }

    static public String strSubstringStart(String s1, int beg) {
        return s1.substring(beg);
    }

    static public String strTrim(String s1) {
        return s1.trim();
    }

    // --------------------------------------------------------------------------
    // data type xform

    static public double parseDouble(String s) {
        return Double.parseDouble(s);
    }

    static public int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) // be nice for silly people!
        {
            return (int) Math.round(Double.parseDouble(s));
        }
    }

    static public boolean parseBoolean(String s) {
        if (s.equalsIgnoreCase("") || s.equalsIgnoreCase("f")
                || s.equalsIgnoreCase("false") || s.equalsIgnoreCase("0")
                || s.equalsIgnoreCase("0.0"))
            return false;
        return true;
    }

    static public int roundDouble(double d) {
        return (int) Math.round(d);
    }

    static public double int2ddouble(int i) {
        return (double) i;
    }

    static public boolean int2bbool(int i) {
        return i == 0;
    }

    static public boolean double2bool(double d) {
        return d == 0;
    }

    static public Object if_then_else(boolean p, Object a, Object b) {
        if (p)
            return a;
        else
            return b;
    }

    // --------------------------------------------------------------------------
    // OGC Filter comparisionOP functions

    static public boolean equalTo(Object o1, Object o2) {
        if (o1.getClass() == o2.getClass())
            return o1.equals(o2);
        if ((o1 instanceof Number) && (o2 instanceof Number)) {
            return ((Number) o1).doubleValue() == ((Number) o2).doubleValue();
        }
        return (o1).toString().equals((o2).toString());
    }

    static public boolean notEqualTo(Object o1, Object o2) {
        return !(equalTo(o1, o2));
    }

    static public boolean lessThan(Object o1, Object o2) {
        if ((o1 instanceof Integer) && (o2 instanceof Integer)) {
            return ((Integer) o1).intValue() < ((Integer) o2).intValue();
        }
        if ((o1 instanceof Number) && (o2 instanceof Number)) {
            return ((Number) o1).doubleValue() < ((Number) o2).doubleValue();
        }
        return (o1).toString().compareTo((o2).toString()) == 0;
    }

    static public boolean greaterThan(Object o1, Object o2) {
        if ((o1 instanceof Integer) && (o2 instanceof Integer)) {
            return ((Integer) o1).intValue() > ((Integer) o2).intValue();
        }
        if ((o1 instanceof Number) && (o2 instanceof Number)) {
            return ((Number) o1).doubleValue() > ((Number) o2).doubleValue();
        }
        return (o1).toString().compareTo((o2).toString()) == 2;
    }

    static public boolean greaterEqualThan(Object o1, Object o2) {
        if ((o1 instanceof Integer) && (o2 instanceof Integer)) {
            return ((Integer) o1).intValue() >= ((Integer) o2).intValue();
        }
        if ((o1 instanceof Number) && (o2 instanceof Number)) {
            return ((Number) o1).doubleValue() >= ((Number) o2).doubleValue();
        }
        return (((o1).toString().compareTo((o2).toString()) == 2) || ((o1)
                .toString().compareTo((o2).toString()) == 1));
    }

    static public boolean lessEqualThan(Object o1, Object o2) {
        if ((o1 instanceof Integer) && (o2 instanceof Integer)) {
            return ((Integer) o1).intValue() <= ((Integer) o2).intValue();
        }
        if ((o1 instanceof Number) && (o2 instanceof Number)) {
            return ((Number) o1).doubleValue() <= ((Number) o2).doubleValue();
        }
        return (((o1).toString().compareTo((o2).toString()) == 0) || ((o1)
                .toString().compareTo((o2).toString()) == 1));
    }

    static public boolean isLike(String s1, String s2) {
        return s1.matches(s2); // this sucks, but hay...
    }

    static public boolean isNull(Object o) {
        return o == null;
    }

    static public boolean between(Object o, Object o_low, Object o_high) {

        return StaticGeometry.greaterEqualThan(o, o_low)
                && StaticGeometry.lessEqualThan(o, o_high);
    }

    static public boolean not(boolean b) {
        return !b;
    }

    // --------------------------------------------------------------------------
    // SQL "var in (list)"

    static public boolean in2(Object s, Object s1, Object s2) {
        return (equalTo(s, s1) || equalTo(s, s2));
    }

    static public boolean in3(Object s, Object s1, Object s2, Object s3) {
        return (equalTo(s, s1) || equalTo(s, s2) || equalTo(s, s3));
    }

    static public boolean in4(Object s, Object s1, Object s2, Object s3,
            Object s4) {
        return (equalTo(s, s1) || equalTo(s, s2) || equalTo(s, s3) || equalTo(
                s, s4));
    }

    static public boolean in5(Object s, Object s1, Object s2, Object s3,
            Object s4, Object s5) {
        return (equalTo(s, s1) || equalTo(s, s2) || equalTo(s, s3)
                || equalTo(s, s4) || equalTo(s, s5));
    }

    static public boolean in6(Object s, Object s1, Object s2, Object s3,
            Object s4, Object s5, Object s6) {
        return (equalTo(s, s1) || equalTo(s, s2) || equalTo(s, s3)
                || equalTo(s, s4) || equalTo(s, s5) || equalTo(s, s6));
    }

    static public boolean in7(Object s, Object s1, Object s2, Object s3,
            Object s4, Object s5, Object s6, Object s7) {
        return (equalTo(s, s1) || equalTo(s, s2) || equalTo(s, s3)
                || equalTo(s, s4) || equalTo(s, s5) || equalTo(s, s6) || equalTo(
                s, s7));
    }

    static public boolean in8(Object s, Object s1, Object s2, Object s3,
            Object s4, Object s5, Object s6, Object s7, Object s8) {
        return (equalTo(s, s1) || equalTo(s, s2) || equalTo(s, s3)
                || equalTo(s, s4) || equalTo(s, s5) || equalTo(s, s6)
                || equalTo(s, s7) || equalTo(s, s8));
    }

    static public boolean in9(Object s, Object s1, Object s2, Object s3,
            Object s4, Object s5, Object s6, Object s7, Object s8, Object s9) {
        return (equalTo(s, s1) || equalTo(s, s2) || equalTo(s, s3)
                || equalTo(s, s4) || equalTo(s, s5) || equalTo(s, s6)
                || equalTo(s, s7) || equalTo(s, s8) || equalTo(s, s9));
    }

    static public boolean in10(Object s, Object s1, Object s2, Object s3,
            Object s4, Object s5, Object s6, Object s7, Object s8, Object s9,
            Object s10) {
        return (equalTo(s, s1) || equalTo(s, s2) || equalTo(s, s3)
                || equalTo(s, s4) || equalTo(s, s5) || equalTo(s, s6)
                || equalTo(s, s7) || equalTo(s, s8) || equalTo(s, s9) || equalTo(
                s, s10));
    }

}
