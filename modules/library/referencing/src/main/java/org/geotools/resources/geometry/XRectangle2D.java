/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
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
package org.geotools.resources.geometry;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.io.Serializable;

import org.geotools.resources.Classes;


/**
 * Serializable, high-performance double-precision rectangle. Instead of using
 * {@code x}, {@code y}, {@code width} and {@code height},
 * this class store rectangle's coordinates into the following fields:
 * {@link #xmin}, {@link #xmax}, {@link #ymin} et {@link #ymax}. Methods likes
 * {@code contains} and {@code intersects} are faster, which make this
 * class more appropriate for using intensively inside a loop. Furthermore, this
 * class work correctly with {@linkplain Double#POSITIVE_INFINITY infinites} and
 * {@linkplain Double#NaN NaN} values.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class XRectangle2D extends Rectangle2D implements Serializable {
    /**
     * A small number for testing intersection between an arbitrary shape and a rectangle.
     */
    private static final double EPS = 1E-6;

    /**
     * An immutable instance of a {@link Rectangle2D} with bounds extending toward
     * infinities. The {@link #getMinX} and {@link #getMinY} methods return always
     * {@link java.lang.Double#NEGATIVE_INFINITY},  while the {@link #getMaxX} and
     * {@link #getMaxY} methods return always {@link java.lang.Double#POSITIVE_INFINITY}.
     * This rectangle can be used as argument in the {@link XRectangle2D} constructor for
     * initializing a new {@code XRectangle2D} to infinite bounds.
     */
    public static final Rectangle2D INFINITY = InfiniteRectangle2D.INFINITY;

    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -1918221103635749436L;

    /** Minimal <var>x</var> coordinate. */ protected double xmin;
    /** Minimal <var>y</var> coordinate. */ protected double ymin;
    /** Maximal <var>x</var> coordinate. */ protected double xmax;
    /** Maximal <var>y</var> coordinate. */ protected double ymax;

    /**
     * Construct a default rectangle. Initial coordinates are {@code (0,0,0,0)}.
     */
    public XRectangle2D() {
    }

    /**
     * Construct a rectangle with the specified location and dimension.
     * This constructor uses the same signature than {@link Rectangle2D} for consistency.
     */
    public XRectangle2D(final double x, final double y, final double width, final double height) {
        this.xmin = x;
        this.ymin = y;
        this.xmax = x+width;
        this.ymax = y+height;
    }

    /**
     * Construct a rectangle with the same coordinates than the supplied rectangle.
     *
     * @param rect The rectangle, or {@code null} in none (in which case this constructor
     *             is equivalents to the no-argument constructor). Use {@link #INFINITY} for
     *             initializing this {@code XRectangle2D} with infinite bounds.
     */
    public XRectangle2D(final Rectangle2D rect) {
        if (rect != null) {
            setRect(rect);
        }
    }

    /**
     * Create a rectangle using maximal <var>x</var> and <var>y</var> values
     * rather than width and height. This factory avoid the problem of NaN
     * values when extremums are infinite numbers.
     */
    public static XRectangle2D createFromExtremums(final double xmin, final double ymin,
                                                   final double xmax, final double ymax)
    {
        final XRectangle2D rect = new XRectangle2D();
        rect.xmin = xmin;
        rect.ymin = ymin;
        rect.xmax = xmax;
        rect.ymax = ymax;
        return rect;
    }

    /**
     * Determines whether the {@code RectangularShape} is empty.
     * When the {@code RectangularShape} is empty, it encloses no
     * area.
     *
     * @return {@code true} if the {@code RectangularShape} is empty;
     *      {@code false} otherwise.
     */
    public boolean isEmpty() {
        return !(xmin<xmax && ymin<ymax);
    }

    /**
     * Returns the X coordinate of the upper left corner of
     * the framing rectangle in {@code double} precision.
     *
     * @return the x coordinate of the upper left corner of the framing rectangle.
     */
    public double getX() {
        return xmin;
    }

    /**
     * Returns the Y coordinate of the upper left corner of
     * the framing rectangle in {@code double} precision.
     *
     * @return the y coordinate of the upper left corner of the framing rectangle.
     */
    public double getY() {
        return ymin;
    }

    /**
     * Returns the width of the framing rectangle in
     * {@code double} precision.
     * @return the width of the framing rectangle.
     */
    public double getWidth() {
        return xmax-xmin;
    }

    /**
     * Returns the height of the framing rectangle in {@code double} precision.
     *
     * @return the height of the framing rectangle.
     */
    public double getHeight() {
        return ymax-ymin;
    }

    /**
     * Returns the smallest X coordinate of the rectangle.
     */
    @Override
    public double getMinX() {
        return xmin;
    }

    /**
     * Returns the smallest Y coordinate of the rectangle.
     */
    @Override
    public double getMinY() {
        return ymin;
    }

    /**
     * Returns the largest X coordinate of the rectangle.
     */
    @Override
    public double getMaxX() {
        return xmax;
    }

    /**
     * Returns the largest Y coordinate of the rectangle.
     */
    @Override
    public double getMaxY() {
        return ymax;
    }

    /**
     * Returns the X coordinate of the center of the rectangle.
     */
    @Override
    public double getCenterX() {
        return (xmin + xmax) * 0.5;
    }

    /**
     * Returns the Y coordinate of the center of the rectangle.
     */
    @Override
    public double getCenterY() {
        return (ymin + ymax) * 0.5;
    }

    /**
     * Sets the location and size of this {@code Rectangle2D}
     * to the specified double values.
     *
     * @param x the <var>x</var> coordinates to which to set the
     *        location of the upper left corner of this {@code Rectangle2D}
     * @param y the <var>y</var> coordinates to which to set the
     *        location of the upper left corner of this {@code Rectangle2D}
     * @param width the value to use to set the width of this {@code Rectangle2D}
     * @param height the value to use to set the height of this {@code Rectangle2D}
     */
    @Override
    public void setRect(final double x, final double y, final double width, final double height) {
        this.xmin = x;
        this.ymin = y;
        this.xmax = x + width;
        this.ymax = y + height;
    }

    /**
     * Sets this {@code Rectangle2D} to be the same as the
     * specified {@code Rectangle2D}.
     *
     * @param r the specified {@code Rectangle2D}
     */
    @Override
    public void setRect(final Rectangle2D r) {
        this.xmin = r.getMinX();
        this.ymin = r.getMinY();
        this.xmax = r.getMaxX();
        this.ymax = r.getMaxY();
    }

    /**
     * Tests if the interior of this {@code Rectangle2D}
     * intersects the interior of a specified set of rectangular
     * coordinates.
     *
     * @param x the <var>x</var> coordinates of the upper left corner
     *        of the specified set of rectangular coordinates
     * @param y the <var>y</var> coordinates of the upper left corner
     *        of the specified set of rectangular coordinates
     * @param width the width of the specified set of rectangular coordinates
     * @param height the height of the specified set of rectangular coordinates
     * @return {@code true} if this {@code Rectangle2D}
     * intersects the interior of a specified set of rectangular
     * coordinates; {@code false} otherwise.
     */
    @Override
    public boolean intersects(final double x,     final double y,
                              final double width, final double height)
    {
        if (!(xmin<xmax && ymin<ymax && width>0 && height>0)) {
            return false;
        } else {
            return (x<xmax && y<ymax && x+width>xmin && y+height>ymin);
        }
    }

    /**
     * Tests if the <strong>interior</strong> of this shape intersects the
     * <strong>interior</strong> of a specified rectangle. This methods overrides the default
     * {@link Rectangle2D} implementation in order to work correctly with
     * {@linkplain Double#POSITIVE_INFINITY infinites} and {@linkplain Double#NaN NaN} values.
     *
     * @param  rect the specified rectangle.
     * @return {@code true} if this shape and the specified rectangle intersect each other.
     *
     * @see #intersectInclusive(Rectangle2D, Rectangle2D)
     */
    @Override
    public boolean intersects(final Rectangle2D rect) {
        if (!(xmin<xmax && ymin<ymax)) {
            return false;
        } else {
            final double xmin2 = rect.getMinX();
            final double xmax2 = rect.getMaxX(); if (!(xmax2 > xmin2)) return false;
            final double ymin2 = rect.getMinY();
            final double ymax2 = rect.getMaxY(); if (!(ymax2 > ymin2)) return false;
            return (xmin2<xmax && ymin2<ymax && xmax2>xmin && ymax2>ymin);
        }
    }

    /**
     * Tests if the interior and/or the edge of two rectangles intersect. This method
     * is similar to {@link #intersects(Rectangle2D)} except for the following points:
     * <ul>
     *   <li>This method doesn't test only if the <em>interiors</em> intersect.
     *       It tests for the edges as well.</li>
     *   <li>This method tests also rectangle with zero {@linkplain Rectangle2D#getWidth width} or
     *       {@linkplain Rectangle2D#getHeight height} (which are {@linkplain Rectangle2D#isEmpty
     *       empty} according {@link Shape} contract). However, rectangle with negative width or
     *       height are still considered as empty.</li>
     *   <li>This method work correctly with {@linkplain Double#POSITIVE_INFINITY infinites} and
     *       {@linkplain Double#NaN NaN} values.</li>
     * </ul>
     *
     * This method is said <cite>inclusive</cite> because it tests bounds as closed interval
     * rather then open interval (the default Java2D behavior). Usage of closed interval is
     * required if at least one rectangle may be the bounding box of a perfectly horizontal
     * or vertical line; such a bounding box has 0 width or height.
     *
     * @param  rect1 The first rectangle to test.
     * @param  rect2 The second rectangle to test.
     * @return {@code true} if the interior and/or the edge of the two specified rectangles
     *         intersects.
     */
    public static boolean intersectInclusive(final Rectangle2D rect1, final Rectangle2D rect2) {
        final double xmin1 = rect1.getMinX();
        final double xmax1 = rect1.getMaxX(); if (!(xmax1 >= xmin1)) return false;
        final double ymin1 = rect1.getMinY();
        final double ymax1 = rect1.getMaxY(); if (!(ymax1 >= ymin1)) return false;
        final double xmin2 = rect2.getMinX();
        final double xmax2 = rect2.getMaxX(); if (!(xmax2 >= xmin2)) return false;
        final double ymin2 = rect2.getMinY();
        final double ymax2 = rect2.getMaxY(); if (!(ymax2 >= ymin2)) return false;
	return (xmax2 >= xmin1 &&
		ymax2 >= ymin1 &&
		xmin2 <= xmax1 &&
		ymin2 <= ymax1);
    }

    /**
     * Tests if the interior of the {@code Shape} intersects the interior of a specified
     * rectangle. This method might conservatively return {@code true} when there is a high
     * probability that the rectangle and the shape intersect, but the calculations to accurately
     * determine this intersection are prohibitively expensive. This is similar to
     * {@link Shape#intersects(Rectangle2D)}, except that this method tests also rectangle with
     * zero {@linkplain Rectangle2D#getWidth width} or {@linkplain Rectangle2D#getHeight height}
     * (which are {@linkplain Rectangle2D#isEmpty empty} according {@link Shape} contract). However,
     * rectangle with negative width or height are still considered as empty.
     * <br><br>
     * This method is said <cite>inclusive</cite> because it try to mimic
     * {@link #intersectInclusive(Rectangle2D, Rectangle2D)} behavior, at
     * least for rectangle with zero width or height.
     *
     * @param shape The shape.
     * @param rect  The rectangle to test for inclusion.
     * @return {@code true} if the interior of the shape and  the interior of the specified
     *         rectangle intersect, or are both highly likely to intersect.
     */
    public static boolean intersectInclusive(final Shape shape, final Rectangle2D rect) {
        double x      = rect.getX();
        double y      = rect.getY();
        double width  = rect.getWidth();
        double height = rect.getHeight();
        if(width == 0 && height == 0) {
            width = EPS;
            height = EPS;
        } else if (width == 0) {
            width = height*EPS;
            x -= 0.5*width;
        } else if (height == 0) {
            height = width*EPS;
            y -= 0.5*height;
        }
        return shape.intersects(x, y, width, height);
    }

    /**
     * Returns {@code true} if the two rectangles are equals up to an epsilon value.
     */
    public static boolean equalsEpsilon(final Rectangle2D rect1, final Rectangle2D rect2) {
        double dx = 0.5 * Math.abs(rect1.getWidth()  + rect2.getWidth());
        double dy = 0.5 * Math.abs(rect1.getHeight() + rect2.getHeight());
        if (dx > 0) dx *= EPS; else dx = EPS;
        if (dy > 0) dy *= EPS; else dy = EPS;
        return equalsEpsilon(rect1.getMinX(), rect2.getMinX(), dx) &&
               equalsEpsilon(rect1.getMinY(), rect2.getMinY(), dy) &&
               equalsEpsilon(rect1.getMaxX(), rect2.getMaxX(), dx) &&
               equalsEpsilon(rect1.getMaxY(), rect2.getMaxY(), dy);
    }

    /**
     * Compares the specified numbers with the specified tolerance.
     */
    private static boolean equalsEpsilon(final double v1, final double v2, final double eps) {
        return (Math.abs(v1 - v2) < eps) ||
                (java.lang.Double.doubleToLongBits(v1) == java.lang.Double.doubleToLongBits(v2));
    }

    /**
     * Tests if the interior of this {@code Rectangle2D} entirely
     * contains the specified set of rectangular coordinates.
     *
     * @param x the <var>x</var> coordinates of the upper left corner
     *          of the specified set of rectangular coordinates
     * @param y the <var>y</var> coordinates of the upper left corner
     *          of the specified set of rectangular coordinates
     * @param width the width of the specified set of rectangular coordinates
     * @param height the height of the specified set of rectangular coordinates
     * @return {@code true} if this {@code Rectangle2D}
     *         entirely contains specified set of rectangular
     *         coordinates; {@code false} otherwise.
     */
    @Override
    public boolean contains(final double x,     final double y,
                            final double width, final double height)
    {
        if (!(xmin<xmax && ymin<ymax && width>0 && height>0)) {
            return false;
        } else {
            return (x>=xmin && y>=ymin && (x+width)<=xmax && (y+height)<=ymax);
        }
    }

    /**
     * Tests if the interior of this shape entirely contains the specified rectangle.
     * This methods overrides the default {@link Rectangle2D} implementation in order
     * to work correctly with {@linkplain Double#POSITIVE_INFINITY infinites} and
     * {@linkplain Double#NaN NaN} values.
     *
     * @param  rect the specified rectangle.
     * @return {@code true} if this shape entirely contains the specified rectangle.
     */
    @Override
    public boolean contains(final Rectangle2D rect) {
        if (!(xmin<xmax && ymin<ymax)) {
            return false;
        } else {
            final double xmin2 = rect.getMinX();
            final double xmax2 = rect.getMaxX(); if (!(xmax2 > xmin2)) return false;
            final double ymin2 = rect.getMinY();
            final double ymax2 = rect.getMaxY(); if (!(ymax2 > ymin2)) return false;
            return (xmin2>=xmin && ymin2>=ymin && xmax2<=xmax && ymax2<=ymax);
        }
    }

    /**
     * Tests if a specified coordinate is inside the boundary of this {@code Rectangle2D}.
     *
     * @param x the <var>x</var> coordinates to test.
     * @param y the <var>y</var> coordinates to test.
     * @return {@code true} if the specified coordinates are
     *         inside the boundary of this {@code Rectangle2D};
     *         {@code false} otherwise.
     */
    @Override
    public boolean contains(final double x, final double y) {
        return (x>=xmin && y>=ymin && x<xmax && y<ymax);
    }

    /**
     * Tests if the interior of the {@code inner} rectangle is contained in the interior
     * and/or the edge of the {@code outter} rectangle. This method is similar to
     * {@link #contains(Rectangle2D)} except for the following points:
     * <ul>
     *   <li>This method doesn't test only the <em>interiors</em> of {@code outter}.
     *       It tests for the edges as well.</li>
     *   <li>This method tests also rectangle with zero {@linkplain Rectangle2D#getWidth width} or
     *       {@linkplain Rectangle2D#getHeight height} (which are {@linkplain Rectangle2D#isEmpty
     *       empty} according {@link Shape} contract).</li>
     *   <li>This method work correctly with {@linkplain Double#POSITIVE_INFINITY infinites} and
     *       {@linkplain Double#NaN NaN} values.</li>
     * </ul>
     *
     * This method is said <cite>inclusive</cite> because it tests bounds as closed interval
     * rather then open interval (the default Java2D behavior). Usage of closed interval is
     * required if at least one rectangle may be the bounding box of a perfectly horizontal
     * or vertical line; such a bounding box has 0 width or height.
     *
     * @param  outter The first rectangle to test.
     * @param  inner The second rectangle to test.
     * @return {@code true} if the interior of {@code inner} is inside the interior
     *         and/or the edge of {@code outter}.
     *
     * @todo Check for negative width or height (should returns {@code false}).
     */
    public static boolean containsInclusive(final Rectangle2D outter, final Rectangle2D inner) {
        return outter.getMinX() <= inner.getMinX() && outter.getMaxX() >= inner.getMaxX() &&
               outter.getMinY() <= inner.getMinY() && outter.getMaxY() >= inner.getMaxY();
    }

    /**
     * Determines where the specified coordinates lie with respect
     * to this {@code Rectangle2D}.
     * This method computes a binary OR of the appropriate mask values
     * indicating, for each side of this {@code Rectangle2D},
     * whether or not the specified coordinates are on the same side
     * of the edge as the rest of this {@code Rectangle2D}.
     *
     * @return the logical OR of all appropriate out codes.
     *
     * @see #OUT_LEFT
     * @see #OUT_TOP
     * @see #OUT_RIGHT
     * @see #OUT_BOTTOM
     */
    public int outcode(final double x, final double y) {
        int out=0;
        if (!(xmax > xmin)) out |= OUT_LEFT | OUT_RIGHT;
        else if (x < xmin)  out |= OUT_LEFT;
        else if (x > xmax)  out |= OUT_RIGHT;

        if (!(ymax > ymin)) out |= OUT_TOP | OUT_BOTTOM;
        else if (y < ymin)  out |= OUT_TOP;
        else if (y > ymax)  out |= OUT_BOTTOM;
        return out;
    }

    /**
     * Returns a new {@code Rectangle2D} object representing the
     * intersection of this {@code Rectangle2D} with the specified
     * {@code Rectangle2D}.
     *
     * @param  rect the {@code Rectangle2D} to be intersected with this {@code Rectangle2D}
     * @return the largest {@code Rectangle2D} contained in both the specified
     *         {@code Rectangle2D} and in this {@code Rectangle2D}.
     */
    public Rectangle2D createIntersection(final Rectangle2D rect) {
        final XRectangle2D r=new XRectangle2D();
        r.xmin = Math.max(xmin, rect.getMinX());
        r.ymin = Math.max(ymin, rect.getMinY());
        r.xmax = Math.min(xmax, rect.getMaxX());
        r.ymax = Math.min(ymax, rect.getMaxY());
        return r;
    }

    /**
     * Returns a new {@code Rectangle2D} object representing the
     * union of this {@code Rectangle2D} with the specified
     * {@code Rectangle2D}.
     *
     * @param rect the {@code Rectangle2D} to be combined with
     *             this {@code Rectangle2D}
     * @return the smallest {@code Rectangle2D} containing both
     *         the specified {@code Rectangle2D} and this
     *         {@code Rectangle2D}.
     */
    public Rectangle2D createUnion(final Rectangle2D rect) {
        final XRectangle2D r=new XRectangle2D();
        r.xmin = Math.min(xmin, rect.getMinX());
        r.ymin = Math.min(ymin, rect.getMinY());
        r.xmax = Math.max(xmax, rect.getMaxX());
        r.ymax = Math.max(ymax, rect.getMaxY());
        return r;
    }

    /**
     * Adds a point, specified by the double precision arguments
     * {@code x} and {@code y}, to this {@code Rectangle2D}.
     * The resulting {@code Rectangle2D} is the smallest {@code Rectangle2D}
     * that contains both the original {@code Rectangle2D} and the specified point.
     * <p>
     * After adding a point, a call to {@code contains} with the
     * added point as an argument does not necessarily return
     * {@code true}. The {@code contains} method does not
     * return {@code true} for points on the right or bottom
     * edges of a rectangle. Therefore, if the added point falls on
     * the left or bottom edge of the enlarged rectangle,
     * {@code contains} returns {@code false} for that point.
     */
    @Override
    public void add(final double x, final double y) {
        if (x < xmin) xmin = x;
        if (x > xmax) xmax = x;
        if (y < ymin) ymin = y;
        if (y > ymax) ymax = y;
    }

    /**
     * Adds a {@code Rectangle2D} object to this {@code Rectangle2D}.
     * The resulting {@code Rectangle2D} is the union of the two
     * {@code Rectangle2D} objects.
     *
     * @param rect the {@code Rectangle2D} to add to this {@code Rectangle2D}.
     */
    @Override
    public void add(final Rectangle2D rect) {
        double t;
        if ((t=rect.getMinX()) < xmin) xmin = t;
        if ((t=rect.getMaxX()) > xmax) xmax = t;
        if ((t=rect.getMinY()) < ymin) ymin = t;
        if ((t=rect.getMaxY()) > ymax) ymax = t;
    }

    /**
     * Returns the {@code String} representation of this {@code Rectangle2D}.
     *
     * @return a {@code String} representing this {@code Rectangle2D}.
     */
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer(Classes.getShortClassName(this));
        final NumberFormat format = NumberFormat.getNumberInstance();
        final FieldPosition dummy = new FieldPosition(0);
        buffer.append("[xmin="); format.format(xmin, buffer, dummy);
        buffer.append(" xmax="); format.format(xmax, buffer, dummy);
        buffer.append(" ymin="); format.format(ymin, buffer, dummy);
        buffer.append(" ymax="); format.format(ymax, buffer, dummy);
        buffer.append(']');
        return buffer.toString();
    }
}
