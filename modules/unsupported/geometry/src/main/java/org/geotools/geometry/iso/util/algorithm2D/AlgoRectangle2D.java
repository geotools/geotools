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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * @author roehrig
 *     <p>TODO To change the template for this generated type comment go to Window - Preferences -
 *     Java - Code Style - Code Templates
 */
public class AlgoRectangle2D {

    public static Rectangle2D copyRectangle(Rectangle2D r) {
        return new Rectangle2D.Double(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
    }

    public static Rectangle2D createRectangle(Point2D p0, Point2D p1) {
        Rectangle2D r = new Rectangle2D.Double();
        setValues(r, p0.getX(), p0.getY(), p1.getX(), p1.getY());
        return r;
    }

    public static Rectangle2D createRectangle(Point2D p) {
        Rectangle2D r = new Rectangle2D.Double();
        setValues(r, p.getX(), p.getY(), p.getX(), p.getY());
        return r;
    }

    public static Rectangle2D createRectangle(Point2D[] p) {
        if (p.length == 0) return null;
        Rectangle2D result = new Rectangle2D.Double(p[0].getX(), p[0].getY(), 0.0, 0.0);
        for (int i = 1; i < p.length; ++i) {
            // if (p[i] != null) expand(result,p[i]);
            if (p[i] != null) result.add(p[i]);
        }
        return result;
    }

    public static Rectangle2D createRectangle(double xmin, double ymin, double xmax, double ymax) {
        Rectangle2D r = new Rectangle2D.Double();
        setValues(r, xmin, ymin, xmax, ymax);
        return r;
    }

    public static void setValues(Rectangle2D r, Point2D[] p) {
        if (p == null || p.length == 0) return;
        r.setRect(p[0].getX(), p[0].getY(), 0.0, 0.0);
        for (int i = 1; i < p.length; ++i) r.add(p[i]);
    }

    public static void setValues(
            Rectangle2D r, double xmin, double ymin, double xmax, double ymax) {
        if (java.lang.Double.isNaN(xmin)
                || java.lang.Double.isNaN(ymin)
                || java.lang.Double.isNaN(xmax)
                || java.lang.Double.isNaN(ymax))
            throw new IllegalArgumentException("Error on AlgoRectangle::getValues");

        if (xmin > xmax) {
            double tmp = xmin;
            xmin = xmax;
            xmax = tmp;
        }
        if (ymin > ymax) {
            double tmp = ymin;
            ymin = ymax;
            ymax = tmp;
        }
        r.setRect(xmin, ymin, xmax - xmin, ymax - ymin);
    }

    public static Point2D getUpperCorner(Rectangle2D r) {
        return new Point2D.Double(r.getMaxX(), r.getMaxY());
    }

    public static Point2D getLowerCorner(Rectangle2D r) {
        return new Point2D.Double(r.getMinX(), r.getMinY());
    }

    public static Point2D[] getCorners(Rectangle2D r) {
        return new Point2D[] {getLowerCorner(r), getUpperCorner(r)};
    }

    public static Point2D getCenter(Rectangle2D r) {
        return new Point2D.Double(r.getCenterX(), r.getCenterY());
    }

    //	/**
    //	 * Expands the envelope with a direct Position
    //	 */
    //	public static void expand(Rectangle2D r,Point2D p) {
    //		// USE r.add(p);
    //		double xMin = r.getMinX();
    //		double yMin = r.getMinY();
    //		double xMax = r.getMaxX();
    //		double yMax = r.getMaxY();
    //		double x = p.getX();
    //		double y = p.getY();
    //		if (x<xMin) xMin = x;
    //		if (x>xMax) xMax = x;
    //		if (y<yMin) yMin = y;
    //		if (y>yMax) yMax = y;
    //		r.setRect(xMin,yMin,xMax-xMin,yMax-yMin);
    //	}
    //
    //	public static Point2D center(Rectangle2D r) {
    //		// r.getCenterX() AND r.getCenterY()
    //		return AlgoPoint.scale(AlgoPoint.add(getLowerCorner(r),getUpperCorner(r)),0.5);
    //	}

    public static Rectangle2D createScale(Rectangle2D r, double factor) {
        if (factor <= 0.0 || factor == 1.0) return copyRectangle(r);
        double cx = r.getCenterX();
        double cy = r.getCenterY();
        double w = r.getWidth() * 0.5 * factor;
        double h = r.getHeight() * 0.5 * factor;
        return createRectangle(cx - w, cy - h, cx + w, cy + h);
    }

    public static void setScale(Rectangle2D r, double factor) {
        if (factor <= 0.0 || factor == 1.0) return;
        double w = r.getWidth() * factor;
        double h = r.getHeight() * factor;
        r.setRect(r.getCenterX() - w * 0.5, r.getCenterY() - h * 0.5, w, h);
    }

    public static void setMinSize(Rectangle2D r, double size) {
        if (size <= 0.0 || ((r.getWidth() >= size) && (r.getHeight() >= size))) return;
        double w = r.getWidth();
        double h = r.getHeight();
        if ((w < size) && (h < size)) {
            r.setRect(r.getCenterX() - size * 0.5, r.getCenterY() - size * 0.5, size, size);
        } else if (w < size) {
            r.setRect(r.getCenterX() - size * 0.5, r.getMinY(), size, h);
        } else if (h < size) {
            r.setRect(r.getMinX(), r.getCenterY() - size * 0.5, w, size);
        }
    }

    public static void setMaxQuadrat(Rectangle2D r) {
        double cx = r.getCenterX();
        double cy = r.getCenterY();
        double w = (r.getWidth() > r.getHeight()) ? r.getWidth() : r.getHeight();
        r.setRect(cx - w, cy - w, cx + w, cy + w);
    }

    public static void setMinQuadrat(Rectangle2D r) {
        double cx = r.getCenterX();
        double cy = r.getCenterY();
        double w = (r.getWidth() < r.getHeight()) ? r.getWidth() : r.getHeight();
        r.setRect(cx - w, cy - w, cx + w, cy + w);
    }

    //	public static boolean equals(Rectangle2D r, Rectangle2D env) {
    //		// USE r.equals(env)
    //		return (AlgoPoint.equalsTol(getUpperCorner(r),getUpperCorner(env)) &&
    //				AlgoPoint.equalsTol(getLowerCorner(r),getLowerCorner(env)));
    //	}

    /**
     * Verifies whether another envelope intersects with this envelope
     *
     * @return TRUE, if envelopes intersect; FALSE, if they dont intersect
     */
    public static boolean intersects(Rectangle2D r0, Rectangle2D r1) {
        // DON'T Rectangle2D.intersects(Rectangle2D)
        return intersects(r0.getMinX(), r0.getMaxX(), r1.getMinX(), r1.getMaxX())
                && intersects(r0.getMinY(), r0.getMaxY(), r1.getMinY(), r1.getMaxY());
        //		return r0.intersects(r1.x, r1.y, r1.width, r1.height);
    }

    public static boolean intersects(Point2D p0, Point2D p1, Point2D p) {
        return (p0.getX() < p1.getX()
                        ? intersects(p0.getX(), p1.getX(), p.getX())
                        : intersects(p1.getX(), p0.getX(), p.getX()))
                || (p0.getY() < p1.getY()
                        ? intersects(p0.getY(), p1.getY(), p.getY())
                        : intersects(p1.getY(), p0.getY(), p.getY()));
    }

    public static boolean intersects(Point2D p0, Point2D p1, Point2D q0, Point2D q1) {
        return (((((p0.getX() < p1.getX()) ? p0.getX() : p1.getX())
                                <= ((q0.getX() > q1.getX()) ? q0.getX() : q1.getX()))
                        && (((p0.getX() > p1.getX()) ? p0.getX() : p1.getX())
                                >= ((q0.getX() < q1.getX()) ? q0.getX() : q1.getX())))
                || ((((p0.getY() < p1.getY()) ? p0.getY() : p1.getY())
                                <= ((q0.getY() > q1.getY()) ? q0.getY() : q1.getY()))
                        && ((((p0.getY() > p1.getY()) ? p0.getY() : p1.getY())
                                >= ((q0.getY() < q1.getY()) ? q0.getY() : q1.getY())))));
    }

    /**
     * Checks whether two rectangles intersect. First Rectangle : Lower Corner (minx1, miny1), Upper
     * Corner (maxx1, maxy1) Second Rectangle: Lower Corner (minx2, miny2), Upper Corner (maxx2,
     * maxy2)
     *
     * @return TRUE, if the two rectangle intersect in at least one point, FALSE, if the two
     *     rectangle do not intersect at all
     */
    public static boolean intersects(
            double minx1,
            double miny1,
            double maxx1,
            double maxy1,
            double minx2,
            double miny2,
            double maxx2,
            double maxy2) {
        // Test ok - indirect test by AlgoRectangleND.intersects(..) (SJ)
        return !(minx2 > maxx1 || maxx2 < minx1 || miny2 > maxy1 || maxy2 < miny1);
    }

    /** */
    private static boolean intersects(double min0, double max0, double min1, double max1) {
        return (min0 <= max1) && (max0 >= min1);
    }

    /** */
    private static boolean intersects(double min0, double max0, double d) {
        return (min0 <= d) && (max0 >= d);
    }

    //	public static Point2D getSECorner(Rectangle2D r ) {
    //		return new Point2D.Double(r.getMaxX(),r.getMinY());
    //	}
    //
    //	public static Point2D getSWCorner(Rectangle2D r ) {
    //		return new Point2D.Double(r.getMinX(),r.getMinY());
    //	}
    //
    //	public static Point2D getNECorner(Rectangle2D r ) {
    //		return new Point2D.Double(r.getMaxX(),r.getMaxY());
    //	}
    //
    //	public static Point2D getNWCorner(Rectangle2D r ) {
    //		return new Point2D.Double(r.getMinX(),r.getMaxY());
    //	}

    /**
     * returns Quadrants NE,NW,SW,SE
     *
     * @return Rectangle2D[]
     */
    //    public static Rectangle2D[] createQuadrantChildren(Rectangle2D r) {
    //    	Rectangle2D[] result = new Rectangle2D[4];
    //    	double cx = r.getCenterX();
    //    	double cy = r.getCenterY();
    //    	double minX = r.getMinX();
    //    	double minY = r.getMinY();
    //    	double maxX = r.getMaxX();
    //    	double maxY = r.getMaxY();
    //    	// returns Quadrants NE,NW,SW,SE
    //    	result[0] = createRectangle(cx,cy,maxX,maxY);
    //    	result[1] = createRectangle(minX,cy,cx,maxY);
    //    	result[2] = createRectangle(minX,minY,cx,cy);
    //    	result[3] = createRectangle(cx,minY,maxX,cy);
    //    	return result;
    //    }

    public static Point2D[] intersectionRectangleLine(Rectangle2D rec, Line2D line) {
        double x1 = line.getX1();
        double x2 = line.getX2();
        double y1 = line.getY1();
        double y2 = line.getY2();
        double xmin = rec.getMinX();
        double xmax = rec.getMaxX();
        double ymin = rec.getMinY();
        double ymax = rec.getMaxY();
        Point2D p0 = null;
        Point2D p1 = null;
        if ((x1 == x2)
                && (xmin <= x1 && xmax >= x1)
                && ((y1 <= ymin && y2 >= ymax) || (y2 <= ymin && y1 >= ymax))) {
            return new Point2D[] {new Point2D.Double(x1, ymin), new Point2D.Double(x1, ymax)};
        }
        if ((y1 == y2)
                && (ymin <= y1 && ymax >= y1)
                && ((x1 <= xmin && x2 >= xmax) || (x2 <= xmin && x1 >= xmax))) {
            return new Point2D[] {new Point2D.Double(xmin, y1), new Point2D.Double(xmax, y1)};
        }
        double r = (xmin - x1) / (x2 - x1);
        p0 = AlgoPoint2D.evaluate(line.getP1(), line.getP2(), r);
        if ((p0 != null)
                && (p0.getX() < xmin || p0.getX() > xmax || p0.getY() < ymin || p0.getY() > ymax))
            p0 = null;

        double s = (ymin - y1) / (y2 - y1);
        if (p0 == null) p0 = AlgoPoint2D.evaluate(line.getP1(), line.getP2(), s);
        else p1 = AlgoPoint2D.evaluate(line.getP1(), line.getP2(), s);

        if ((p0 != null)
                && (p0.getX() < xmin || p0.getX() > xmax || p0.getY() < ymin || p0.getY() > ymax))
            p0 = null;
        if ((p1 != null)
                && (p1.getX() < xmin || p1.getX() > xmax || p1.getY() < ymin || p1.getY() > ymax))
            p1 = null;
        if (p0 != null && p1 != null) return new Point2D[] {p0, p1};

        r = (xmax - x1) / (x2 - x1);
        if (p0 == null) p0 = AlgoPoint2D.evaluate(line.getP1(), line.getP2(), r);
        else p1 = AlgoPoint2D.evaluate(line.getP1(), line.getP2(), r);

        if ((p0 != null)
                && (p0.getX() < xmin || p0.getX() > xmax || p0.getY() < ymin || p0.getY() > ymax))
            p0 = null;
        if ((p1 != null)
                && (p1.getX() < xmin || p1.getX() > xmax || p1.getY() < ymin || p1.getY() > ymax))
            p1 = null;
        if (p0 != null && p1 != null) return new Point2D[] {p0, p1};

        s = (ymax - y1) / (y2 - y1);
        if (p0 == null) p0 = AlgoPoint2D.evaluate(line.getP1(), line.getP2(), s);
        else p1 = AlgoPoint2D.evaluate(line.getP1(), line.getP2(), s);
        if ((p0 != null)
                && (p0.getX() < xmin || p0.getX() > xmax || p0.getY() < ymin || p0.getY() > ymax))
            p0 = null;
        if ((p1 != null)
                && (p1.getX() < xmin || p1.getX() > xmax || p1.getY() < ymin || p1.getY() > ymax))
            p1 = null;
        if (p0 != null && p1 != null) return new Point2D[] {p0, p1};
        else return null;
    }
}
