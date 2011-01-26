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

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author roehrig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 * @source $URL$
 */
public class AlgoPoint2D {

	public static final double EPSILON = 0.000001;

	public static final double EPSILONSQ = 0.001;

	public static final Point2D point00 = new Point2D.Double(0.0, 0.0);

	public static Point2D copyPoint2D(Point2D p) {
		return new Point2D.Double(p.getX(),p.getY());
	}
	/**
	 * Compares coodinates of Direct Positions
	 * @param Direct Position to compare with
	 * @return TRUE, if coordinates accord, FALSE if they dont.
	 */
	public static boolean equals(Point2D p0, Point2D p1, double tol) {
		return p0.equals(p1) || p0.distanceSq(p1) < (tol * tol);
	}

	public static boolean equalsTol(Point2D p0, Point2D p1) {
		return AlgoPoint2D.equals(p0, p1, EPSILON);
	}

	public static void add(Point2D p0, Point2D p1) {
		p0.setLocation(p0.getX() + p1.getX(), p0.getY() + p1.getY());
	}

	public static void add(Point2D p0, double factor) {
		p0.setLocation(p0.getX() + factor, p0.getY() + factor);
	}

	/**
	 * Adds a DirectPosition to the position
	 * @param DirectPosition to add
	 * @return new Position
	 */
	public static Point2D createAdd(Point2D p0, Point2D p1) {
		return new Point2D.Double(p0.getX() + p1.getX(), p0.getY() + p1.getY());
	}

	public static Point2D creatAdd(Point2D p0, double factor) {
		return new Point2D.Double(p0.getX() + factor, p0.getY() + factor);
	}

	/**
	 * Subtracts a direct position from the position
	 * @param DirectPosition to subtract
	 * @return new Position
	 */
	public static Point2D subtract(Point2D p0, Point2D p1) {
		return new Point2D.Double(p0.getX() - p1.getX(), p0.getY() - p1.getY());
	}

	public static Point2D scale(Point2D p0, double factor) {
		return new Point2D.Double(p0.getX() * factor, p0.getY() * factor);
	}

	/**
	 * Returns the length (Distance between origin and position)
	 * @return Length
	 */
	public static double length(Point2D p0) {
		return p0.distance(point00);
	}

	public static double lengthSq(Point2D p0) {
		return p0.distanceSq(point00);
	}

	/**
	 * Builds the scalar product
	 * @param DirectPosition to multiply with
	 * @return Scalar product
	 */
	public static double scalar(Point2D p0, Point2D p1) {
		return p0.getX() * p1.getX() + p0.getY() + p1.getY();
	}

	public static double cross(Point2D p0, Point2D p1) {
		// corresponds to the 2*area of two vectors 
		return p0.getX() * p1.getY() - p0.getY() * p1.getX();
	}

	public static Point2D normalize(Point2D p0) {
		double len = AlgoPoint2D.length(p0);
		return (len > 0.0) ? AlgoPoint2D.scale(p0, 1.0 / len)
				: new Point2D.Double(0.0, 0.0);
	}

	public static Point2D evaluate(Point2D p0, Point2D p1, double r) {
		return new Point2D.Double((1.0 - r) * p0.getX() + r * p1.getX(),
				(1.0 - r) * p0.getY() + r * p1.getY());
	}

	public static Point2D evaluate(Point2D p0, Point2D p1, Point2D eval) {
		return new Point2D.Double((1.0 - eval.getX()) * p0.getX() + eval.getX()
				* p1.getX(), (1.0 - eval.getY()) * p0.getY() + eval.getY()
				* p1.getY());
	}

	public static boolean intersectWithHorizontalLineFromRight2D(Point2D p,
			Point2D p0, Point2D p1) {
		// returns true when a horizontal line passing at ME:
		// 1) intersects the line with origin p0 and and p1 and 
		// 2) when ME is on the right side of the line
		double x0 = p0.getX(); // line endpoint 2D coords
		double y0 = p0.getY(); // line endpoint 2D coords
		double x1 = p1.getX(); // line endpoint 2D coords
		double y1 = p1.getY(); // line endpoint 2D coords
		double xa = x0; // swap coordinates 
		double ya = y0; // swap coordinates
		double xb = x1; // swap coordinates
		double yb = y1; // swap coordinates
		double max_x = Math.max(x0, x1); // maximum x coordinate
		double min_x = Math.min(x0, x1); // minimum x coordinate
		double max_y = Math.max(y0, y1); // maximum y coordinate
		double min_y = Math.min(y0, y1); // minimum y coordinate

		// the horizontal line does not intersect the line to the 
		// left of location pt if:
		// [1] if line is horizontal				    
		if (y0 == y1)
			return false;
		// (2) if the y coordinate of node is outside the range 
		// max_y and min_y (but not including min_y)		    
		if (((p.getY() < min_y) || (p.getY() >= max_y)))
			return false;
		// (3) if given line is vertical and y coordinate of node is 
		// smaller than that of line				    
		if (((x0 == x1) && (p.getX() < x0)))
			return false;
		// (4) if inclined line is located to the right of given node	
		// (first reduce the problem to a case where yb >ya, always)
		if ((x0 != x1)) {
			if (!(((x1 > x0) && (y1 > y0)) || ((x1 < x0) && (y1 > y0)))) {
				xa = x1;
				ya = y1;
				xb = x0;
				yb = y0;
			}
			if ((((p.getY() - ya) * (xb - xa)) > ((p.getX() - xa) * (yb - ya)))) {
				return false;
			}
		}
		// if we get here that is because the horizontal line passing 
		// at the location this intersects the given line to the left of the pt
		return true;
	}

	public static double getAngle2D(Point2D p0, Point2D p1) {
		//             * p1
		//            /
		//           /    
		//          /
		//         *------>*
		//       (0,0)    p0
		double angle = Math.atan2(p1.getY(), p1.getX())
				- Math.atan2(p0.getY(), p0.getX());
		if (angle < 0.0)
			angle = angle + 2 * Math.PI;
		if (angle > (2 * Math.PI))
			angle = angle - 2 * Math.PI;
		return angle;
	}

	public static double minAngle2D(Point2D p, Point2D p1, Point2D p2) {
		double ang0 = AlgoPoint2D.getAngle2D(AlgoPoint2D
				.subtract(p1, p), AlgoPoint2D.subtract(p2, p));
		double ang1 = AlgoPoint2D.getAngle2D(AlgoPoint2D.subtract(p2,
				p1), AlgoPoint2D.subtract(p, p1));
		return Math.min(Math.min(ang0, ang1), Math.min(ang0, Math.PI - ang0
				- ang1));
	}

	public static Point2D[] split(Point2D p0, Point2D p1, double maxLength) {
		// The first and last node are not inserted
		if (maxLength<=0.0 || maxLength==Double.NaN) return null;
		int n = (int) Math.ceil(p0.distance(p1) / maxLength);
		if (n==0) return null;
		double x1 = p0.getX();
		double y1 = p0.getY();
		double x2 = p1.getX();
		double y2 = p1.getY();
		double deltaX = (x2-x1) / n;
		double deltaY = (y2-y1) / n;
		Point2D result[] = new Point2D[n-1];
		for (int i = 1; i < n; ++i) {
			result[i-1] = new Point2D.Double(x1 + deltaX * i, y1 + deltaY * i);
		}
		return result;
	}

	public static Point2D createCentroid(Point2D[] points) {
		if (points==null) return null;
		int n = points.length;
		if (n==0) return null;
		double x = points[0].getX();
		double y = points[0].getY();
		for (int i = 1; i<n;++i) {
			x += points[i].getX();
			y += points[i].getY();
		}
		return new Point2D.Double(x/n,y/n);
	}
	
	public static ArrayList<Point2D> split(List<Point2D> points, double maxLength) {
		int n = points.size();
		ArrayList<Point2D> pointList = new ArrayList<Point2D>(n);
		for (int i = 1; i<n;++i) {
			Point2D p0 = points.get(i-1);
			Point2D p1 = points.get(i);
			pointList.add(p0);
			Point2D sp[] = split(p0, p1, maxLength);
			if (sp != null) {
				for (int j = 0; j < sp.length; ++j) {
					pointList.add(sp[j]);
				}
			}
		}
		pointList.add(points.get(n-1));
		return pointList;
	}
	
	public static Boolean pointsOrientation(Collection points) {
		double result = 0.0;
		Iterator it = points.iterator();
		
		if (!it.hasNext()) return null;
		Point2D p0 = (Point2D) it.next();
		
		if (!it.hasNext()) return null;
		Point2D p1 = (Point2D) it.next();
		
		Point2D p10 = AlgoPoint2D.subtract(p1, p0);
		
		double sum = 0.0;
		
		while (it.hasNext()) {
			Point2D p = (Point2D) it.next();
			result += AlgoPoint2D.cross(p10,(AlgoPoint2D.subtract(p, p0)));
		}
		
		return new Boolean(result > 0.0);

	}

	/**
	 * @param collection
	 * @return
	 */
	public static Rectangle2D getEnvelope(Collection coll) {
		// Collection<Point2D>
		Iterator i = coll.iterator();
		if (!i.hasNext()) return null;
		Point2D p = (Point2D)i.next();
		Rectangle2D r = new Rectangle.Double(p.getX(),p.getY(),0.0,0.0);
		while (i.hasNext()) {
			r.add((Point2D)i.next());
		}
		return r;
	}

}
