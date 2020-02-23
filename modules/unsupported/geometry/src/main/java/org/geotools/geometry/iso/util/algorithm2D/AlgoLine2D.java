/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.util.algorithm2D;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Stack;

/**
 * @author roehrig
 *     <p>TODO To change the template for this generated type comment go to Window - Preferences -
 *     Java - Code Style - Code Templates
 */
public class AlgoLine2D {

    public static final int EQUALSEGMENT = 1;
    public static final int OPPOSITESEGMENT = -1;
    public static final int DIFFERENTSEGMENT = 0;

    public static double length(Line2D line) {
        return line.getP1().distance(line.getP2());
    }

    public static ArrayList<Line2D> splitLines(double maxLength, ArrayList<Line2D> lines) {
        if (maxLength <= 0.0) return lines;
        ArrayList<Line2D> result = new ArrayList<Line2D>(lines.size());
        for (int i = 0; i < lines.size(); ++i) {
            Line2D line = (Line2D) lines.get(i);
            if (AlgoLine2D.length(line) > maxLength) {
                result.addAll(AlgoLine2D.split(line, maxLength));
            } else {
                result.add(line);
            }
        }
        return result;
    }

    public static ArrayList<Line2D> split(Line2D line, double maxSpacing) {
        ArrayList<Line2D> result = new ArrayList<Line2D>();
        int n = (int) Math.ceil(AlgoLine2D.length(line) / maxSpacing);
        double x1 = line.getX1();
        double y1 = line.getY1();
        double x2 = line.getX2();
        double y2 = line.getY2();
        double deltaX = (x2 - x1) / n;
        double deltaY = (y2 - y1) / n;
        Point2D p1 = line.getP1();
        for (int j = 1; j < n; ++j) {
            Point2D p2 = new Point2D.Double(x1 + deltaX * j, y1 + deltaY * j);
            result.add(new Line2D.Double(p1, p2));
            p1 = p2;
        }
        /** add the last node */
        result.add(new Line2D.Double(p1, line.getP2()));
        return result;
    }

    //	// first lines which are shorter as minLength will be merged
    //	public static ArrayList mergeLines(double minLength, ArrayList lines) {
    //		ArrayList result = new ArrayList(lines.size());
    //		int i = 0;
    //		int j = 0;
    //		while(i < lines.size()){
    //			Line2D line = (Line2D)lines.get(i);
    //			if (line == lines.get(lines.size()-1)&& (AlgoLine.length(line) < minLength)&&
    // (line.getP1().lineSize()< 3)){
    //				//line= line.merge(result.get(result.size()-1));
    //				line= (result.get(result.size()-1)).merge(line);
    //				result.remove(result.size()-1);
    //				result.add(line);
    //				i++;
    //			}
    //			else if(AlgoLine.length(line) < minLength && (line.getP2().lineSize()< 3)){
    //				j=i+1;
    //				while((AlgoLine.length(line) < minLength) && (j<=(lines.size()-1))&&
    // (((AlgoLine)lines.get(j)).getP1().lineSize()< 3)){
    //					line = line.merge((AlgoLine)lines.get(j));
    //					j++;
    //				}
    //				i = j;
    //				result.add(line);
    //			}else {
    //				result.add(line);
    //				i += 1;
    //			}
    //		}
    //		return result;
    //	}

    //	Ziethen 23.02.05 param minlengh
    //	first lines witch are shorter as minLength will be merged
    public static Line2D merge(Line2D line, Line2D other) {
        Line2D.Double result = new Line2D.Double(line.getP1(), other.getP2());
        return result;
    }

    public static boolean isParallel(Line2D l0, Line2D l1) {
        return AlgoLine2D.isParallel(l0.getP1(), l0.getP2(), l1.getP1(), l1.getP2());
    }

    public static boolean isParallel(Point2D p0, Point2D p1, Point2D q0, Point2D q1) {
        return Math.abs(
                        (double)
                                AlgoPoint2D.cross(
                                        AlgoPoint2D.subtract(p1, p0), AlgoPoint2D.subtract(q1, q0)))
                <= AlgoPoint2D.EPSILON;
    }

    public static double constrParamForPoint(Point2D p0, Point2D p1, Point2D dp) {
        // return the construction parametric coordinate (0.0 <= result <= 1.0) of
        // dp on the line (p0,p1)
        // if p0.equals(p1) then: if dp.equals(p0) return 0.0, else Double.NaN
        // if dp is close to p0 then result = 0.0
        // if dp is close to p1 then result = 1.0
        // if the line (p0,p1) does not contain dp then return Double.NaN
        double eps = AlgoPoint2D.EPSILON;
        double result = java.lang.Double.NaN;
        // return nothing if dp is not on the same line as (p0,p1): colinear
        if (!AlgoLine2D.isParallel(p0, p1, p0, dp)) return java.lang.Double.NaN;
        if (p0.equals(p1)) {
            if (AlgoPoint2D.equals(p0, dp, eps)) return 0.0;
            else return java.lang.Double.NaN;
        }
        if (Math.abs(p0.getX() - p1.getX()) > Math.abs(p0.getY() - p1.getY()))
            result = (dp.getX() - p0.getX()) / (p1.getX() - p0.getX());
        else result = (dp.getY() - p0.getY()) / (p1.getY() - p0.getY());
        if (Math.abs(result) <= eps) result = 0.0;
        if (Math.abs(1.0 - result) <= eps) result = 1.0;
        if (result < 0.0 || result > 1.0) return java.lang.Double.NaN;
        return result;
    }

    public static double orientation(Line2D line, Point2D point) {
        // (ZA)21.12.04 returns a Value about a Curve-Orientation (clockwise or counterclockwise)
        return AlgoPoint2D.cross(
                (AlgoPoint2D.subtract(point, line.getP1())),
                AlgoPoint2D.subtract((line.getP2()), line.getP1()));
    }
    /** */
    public static boolean rightSide(Line2D line, Point2D p) {
        /**
         * A return value of 1 relativeCCW indicates that the line segment must turn in the
         * direction that takes the positive X axis towards the negative Y axis. In the default
         * coordinate system used by Java 2D, this direction is counterclockwise, in our coordinate
         * system it is clockwise (right side)
         */
        return rightSide(line.getP1(), line.getP2(), p);
    }

    public static boolean rightSide(Point2D p0, Point2D p1, Point2D p) {
        /**
         * A return value of 1 relativeCCW indicates that the line segment must turn in the
         * direction that takes the positive X axis towards the negative Y axis. In the default
         * coordinate system used by Java 2D, this direction is counterclockwise, in our coordinate
         * system it is clockwise (right side)
         */
        return AlgoPoint2D.cross(AlgoPoint2D.subtract(p, p0), AlgoPoint2D.subtract(p1, p0)) > 0.0;
    }

    public static boolean leftSide(Line2D line, Point2D p) {
        return leftSide(line.getP1(), line.getP2(), p);
        // return this.relativeCCW(p) == -1;
    }

    public static boolean leftSide(Point2D p0, Point2D p1, Point2D p) {
        return AlgoPoint2D.cross(AlgoPoint2D.subtract(p, p0), AlgoPoint2D.subtract(p1, p0)) < 0.0;
        // return this.relativeCCW(p) == -1;
    }

    /**
     * Returns: 0 if it is not the same segment, i.e !equal AND !inverted 1 if it is equal, i.e.
     * pa0.equals(pb0) && pa1.equals(pb1) 2 if it is inverted, i.e. pa0.equals(pb1) &&
     * pa1.equals(pb0)
     */
    public static int sameSegment(Line2D lineA, Line2D lineB) {
        return sameSegment(lineA.getP1(), lineA.getP2(), lineB.getP1(), lineB.getP2());
    }

    /**
     * Returns: 0 if it is not the same segment, i.e !equal AND !inverted 1 if it is equal, i.e.
     * pa0.equals(pb0) && pa1.equals(pb1) 2 if it is inverted, i.e. pa0.equals(pb1) &&
     * pa1.equals(pb0)
     */
    public static int sameSegment(Line2D line, Point2D pb0, Point2D pb1) {
        return sameSegment(line.getP1(), line.getP2(), pb0, pb1);
    }

    /**
     * Returns: 0 if it is not the same segment, i.e !equal AND !inverted 1 if it is equal, i.e.
     * pa0.equals(pb0) && pa1.equals(pb1) 2 if it is inverted, i.e. pa0.equals(pb1) &&
     * pa1.equals(pb0)
     */
    public static int sameSegment(Point2D pa0, Point2D pa1, Point2D pb0, Point2D pb1) {
        return (pa0.equals(pb0) && pa1.equals(pb1))
                ? EQUALSEGMENT
                : (pa0.equals(pb1) && pa1.equals(pb0) ? OPPOSITESEGMENT : DIFFERENTSEGMENT);
    }

    public static Point2D evaluate(Line2D line, double r) {
        return AlgoPoint2D.evaluate(line.getP1(), line.getP2(), r);
    }

    public static GeneralPath reverse(GeneralPath path) {
        // JR uncomplete
        PathIterator pi = path.getPathIterator(new AffineTransform());
        double[] coords = new double[6];
        Stack<Point2D> ps = new Stack<Point2D>();
        while (!pi.isDone()) {
            int type = pi.currentSegment(coords);
            if ((type == PathIterator.SEG_MOVETO) || (type == PathIterator.SEG_LINETO)) {
                ps.push(new Point2D.Double(coords[0], coords[1]));
            }
            pi.next();
        }
        GeneralPath revPath = new GeneralPath();
        Point2D.Double p = (Point2D.Double) ps.pop();
        revPath.moveTo((float) p.x, (float) p.y);
        while (!ps.empty()) {
            revPath.lineTo((float) p.x, (float) p.y);
        }
        return revPath;
    }

    public static double getAngle2D(Line2D line, Point2D point) {
        //             * p1
        //            /
        //           /
        //          /
        //         *------>*
        //       (0,0)    this
        return AlgoPoint2D.getAngle2D(AlgoPoint2D.subtract(line.getP2(), line.getP1()), point);
    }
}
