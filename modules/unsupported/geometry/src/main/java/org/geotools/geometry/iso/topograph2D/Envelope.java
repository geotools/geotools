/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2006  Vivid Solutions
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
package org.geotools.geometry.iso.topograph2D;

import java.io.Serializable;
import org.opengis.geometry.Geometry;

/**
 * Defines a rectangular region of the 2D coordinate plane. It is often used to represent the
 * bounding box of a {@link Geometry}, e.g. the minimum and maximum x and y values of the {@link
 * Coordinate}s.
 *
 * <p>Note that Envelopes support infinite or half-infinite regions, by using the values of <code>
 * Double.POSITIVE_INFINITY</code> and <code>Double.NEGATIVE_INFINITY</code>.
 *
 * <p>When Envelope objects are created or initialized, the supplies extent values are automatically
 * sorted into the correct order.
 */
public class Envelope implements Serializable {

    public int hashCode() {
        // Algorithm from Effective Java by Joshua Bloch [Jon Aquino]
        int result = 17;
        result = 37 * result + Coordinate.hashCode(minx);
        result = 37 * result + Coordinate.hashCode(maxx);
        result = 37 * result + Coordinate.hashCode(miny);
        result = 37 * result + Coordinate.hashCode(maxy);
        return result;
    }

    // USED!!!
    /**
     * Test the point q to see whether it intersects the Envelope defined by p1-p2
     *
     * @param p1 one extremal point of the envelope
     * @param p2 another extremal point of the envelope
     * @param q the point to test for intersection
     * @return <code>true</code> if q intersects the envelope p1-p2
     */
    public static boolean intersects(Coordinate p1, Coordinate p2, Coordinate q) {
        // OptimizeIt shows that Math#min and Math#max here are a bottleneck.
        // Replace with direct comparisons. [Jon Aquino]
        if (((q.x >= (p1.x < p2.x ? p1.x : p2.x)) && (q.x <= (p1.x > p2.x ? p1.x : p2.x)))
                && ((q.y >= (p1.y < p2.y ? p1.y : p2.y)) && (q.y <= (p1.y > p2.y ? p1.y : p2.y)))) {
            return true;
        }
        return false;
    }

    /**
     * Test the envelope defined by p1-p2 for intersection with the envelope defined by q1-q2
     *
     * @param p1 one extremal point of the envelope P
     * @param p2 another extremal point of the envelope P
     * @param q1 one extremal point of the envelope Q
     * @param q2 another extremal point of the envelope Q
     * @return <code>true</code> if Q intersects P
     */
    public static boolean intersects(Coordinate p1, Coordinate p2, Coordinate q1, Coordinate q2) {
        double minq = Math.min(q1.x, q2.x);
        double maxq = Math.max(q1.x, q2.x);
        double minp = Math.min(p1.x, p2.x);
        double maxp = Math.max(p1.x, p2.x);

        if (minp > maxq) return false;
        if (maxp < minq) return false;

        minq = Math.min(q1.y, q2.y);
        maxq = Math.max(q1.y, q2.y);
        minp = Math.min(p1.y, p2.y);
        maxp = Math.max(p1.y, p2.y);

        if (minp > maxq) return false;
        if (maxp < minq) return false;
        return true;
    }

    /** the minimum x-coordinate */
    private double minx;

    /** the maximum x-coordinate */
    private double maxx;

    /** the minimum y-coordinate */
    private double miny;

    /** the maximum y-coordinate */
    private double maxy;

    /** Creates a null <code>Envelope</code>. */
    public Envelope() {
        init();
    }

    /**
     * Creates an <code>Envelope</code> for a region defined by maximum and minimum values.
     *
     * @param x1 the first x-value
     * @param x2 the second x-value
     * @param y1 the first y-value
     * @param y2 the second y-value
     */
    public Envelope(double x1, double x2, double y1, double y2) {
        init(x1, x2, y1, y2);
    }

    /**
     * Creates an <code>Envelope</code> for a region defined by two Coordinates.
     *
     * @param p1 the first Coordinate
     * @param p2 the second Coordinate
     */
    public Envelope(Coordinate p1, Coordinate p2) {
        init(p1, p2);
    }

    /**
     * Creates an <code>Envelope</code> for a region defined by a single Coordinate.
     *
     * @param p the Coordinate
     */
    public Envelope(Coordinate p) {
        init(p);
    }

    /**
     * Create an <code>Envelope</code> from an existing Envelope.
     *
     * @param env the Envelope to initialize from
     */
    public Envelope(Envelope env) {
        init(env);
    }

    /** Initialize to a null <code>Envelope</code>. */
    public void init() {
        setToNull();
    }

    /**
     * Initialize an <code>Envelope</code> for a region defined by maximum and minimum values.
     *
     * @param x1 the first x-value
     * @param x2 the second x-value
     * @param y1 the first y-value
     * @param y2 the second y-value
     */
    public void init(double x1, double x2, double y1, double y2) {
        if (x1 < x2) {
            minx = x1;
            maxx = x2;
        } else {
            minx = x2;
            maxx = x1;
        }
        if (y1 < y2) {
            miny = y1;
            maxy = y2;
        } else {
            miny = y2;
            maxy = y1;
        }
    }

    /**
     * Initialize an <code>Envelope</code> to a region defined by two Coordinates.
     *
     * @param p1 the first Coordinate
     * @param p2 the second Coordinate
     */
    public void init(Coordinate p1, Coordinate p2) {
        init(p1.x, p2.x, p1.y, p2.y);
    }

    /**
     * Initialize an <code>Envelope</code> to a region defined by a single Coordinate.
     *
     * @param p the first Coordinate
     */
    public void init(Coordinate p) {
        init(p.x, p.x, p.y, p.y);
    }

    /**
     * Initialize an <code>Envelope</code> from an existing Envelope.
     *
     * @param env the Envelope to initialize from
     */
    public void init(Envelope env) {
        this.minx = env.minx;
        this.maxx = env.maxx;
        this.miny = env.miny;
        this.maxy = env.maxy;
    }

    /**
     * Makes this <code>Envelope</code> a "null" envelope, that is, the envelope of the empty
     * geometry.
     */
    public void setToNull() {
        minx = 0;
        maxx = -1;
        miny = 0;
        maxy = -1;
    }

    /**
     * Returns <code>true</code> if this <code>Envelope</code> is a "null" envelope.
     *
     * @return <code>true</code> if this <code>Envelope</code> is uninitialized or is the envelope
     *     of the empty geometry.
     */
    public boolean isNull() {
        return maxx < minx;
    }

    /**
     * Returns the difference between the maximum and minimum x values.
     *
     * @return max x - min x, or 0 if this is a null <code>Envelope</code>
     */
    public double getWidth() {
        if (isNull()) {
            return 0;
        }
        return maxx - minx;
    }

    /**
     * Returns the difference between the maximum and minimum y values.
     *
     * @return max y - min y, or 0 if this is a null <code>Envelope</code>
     */
    public double getHeight() {
        if (isNull()) {
            return 0;
        }
        return maxy - miny;
    }

    /**
     * Returns the <code>Envelope</code>s minimum x-value. min x > max x indicates that this is a
     * null <code>Envelope</code>.
     *
     * @return the minimum x-coordinate
     */
    public double getMinX() {
        return minx;
    }

    /**
     * Returns the <code>Envelope</code>s maximum x-value. min x > max x indicates that this is a
     * null <code>Envelope</code>.
     *
     * @return the maximum x-coordinate
     */
    public double getMaxX() {
        return maxx;
    }

    /**
     * Returns the <code>Envelope</code>s minimum y-value. min y > max y indicates that this is a
     * null <code>Envelope</code>.
     *
     * @return the minimum y-coordinate
     */
    public double getMinY() {
        return miny;
    }

    /**
     * Returns the <code>Envelope</code>s maximum y-value. min y > max y indicates that this is a
     * null <code>Envelope</code>.
     *
     * @return the maximum y-coordinate
     */
    public double getMaxY() {
        return maxy;
    }

    /**
     * Enlarges the boundary of the <code>Envelope</code> so that it contains (x,y). Does nothing if
     * (x,y) is already on or within the boundaries.
     *
     * @param p the point to include
     */
    public void expandToInclude(Coordinate p) {
        expandToInclude(p.x, p.y);
    }

    /**
     * Expands this envelope by a given distance in all directions. Both positive and negative
     * distances are supported.
     *
     * @param distance the distance to expand the envelope
     */
    public void expandBy(double distance) {
        expandBy(distance, distance);
    }

    /**
     * Expands this envelope by a given distance in all directions. Both positive and negative
     * distances are supported.
     *
     * @param deltaX the distance to expand the envelope along the the X axis
     * @param deltaY the distance to expand the envelope along the the Y axis
     */
    public void expandBy(double deltaX, double deltaY) {
        if (isNull()) return;

        minx -= deltaX;
        maxx += deltaX;
        miny -= deltaY;
        maxy += deltaY;

        // check for envelope disappearing
        if (minx > maxx || miny > maxy) setToNull();
    }

    /**
     * Enlarges the boundary of the <code>Envelope</code> so that it contains (x,y). Does nothing if
     * (x,y) is already on or within the boundaries.
     *
     * @param x the value to lower the minimum x to or to raise the maximum x to
     * @param y the value to lower the minimum y to or to raise the maximum y to
     */
    public void expandToInclude(double x, double y) {
        if (isNull()) {
            minx = x;
            maxx = x;
            miny = y;
            maxy = y;
        } else {
            if (x < minx) {
                minx = x;
            }
            if (x > maxx) {
                maxx = x;
            }
            if (y < miny) {
                miny = y;
            }
            if (y > maxy) {
                maxy = y;
            }
        }
    }

    /**
     * Enlarges the boundary of the <code>Envelope</code> so that it contains <code>other</code>.
     * Does nothing if <code>other</code> is wholly on or within the boundaries.
     *
     * @param other the <code>Envelope</code> to merge with
     */
    public void expandToInclude(Envelope other) {
        if (other.isNull()) {
            return;
        }
        if (isNull()) {
            minx = other.getMinX();
            maxx = other.getMaxX();
            miny = other.getMinY();
            maxy = other.getMaxY();
        } else {
            if (other.minx < minx) {
                minx = other.minx;
            }
            if (other.maxx > maxx) {
                maxx = other.maxx;
            }
            if (other.miny < miny) {
                miny = other.miny;
            }
            if (other.maxy > maxy) {
                maxy = other.maxy;
            }
        }
    }

    /**
     * Translates this envelope by given amounts in the X and Y direction.
     *
     * @param transX the amount to translate along the X axis
     * @param transY the amount to translate along the Y axis
     */
    public void translate(double transX, double transY) {
        if (isNull()) {
            return;
        }
        init(getMinX() + transX, getMaxX() + transX, getMinY() + transY, getMaxY() + transY);
    }

    /**
     * Computes the coordinate of the centre of this envelope (as long as it is non-null
     *
     * @return the centre coordinate of this envelope <code>null</code> if the envelope is null
     */
    public Coordinate centre() {
        if (isNull()) return null;
        return new Coordinate((getMinX() + getMaxX()) / 2.0, (getMinY() + getMaxY()) / 2.0);
    }

    /**
     * Computes the intersection of two {@link Envelope}s
     *
     * @param env the envelope to intersect with
     * @return a new Envelope representing the intersection of the envelopes (this will be the null
     *     envelope if either argument is null, or they do not intersect)
     */
    public Envelope intersection(Envelope env) {
        if (isNull() || env.isNull() || !intersects(env)) return new Envelope();

        double intMinX = minx > env.minx ? minx : env.minx;
        double intMinY = miny > env.miny ? miny : env.miny;
        double intMaxX = maxx < env.maxx ? maxx : env.maxx;
        double intMaxY = maxy < env.maxy ? maxy : env.maxy;
        return new Envelope(intMinX, intMaxX, intMinY, intMaxY);
    }

    // USED !!!
    /**
     * Returns <code>true</code> if the given point lies in or on the envelope.
     *
     * @param p the point which this <code>Envelope</code> is being checked for containing
     * @return <code>true</code> if the point lies in the interior or on the boundary of this <code>
     *     Envelope</code>.
     */
    public boolean contains(Coordinate p) {
        return contains(p.x, p.y);
    }

    /**
     * Returns <code>true</code> if the given point lies in or on the envelope.
     *
     * @param x the x-coordinate of the point which this <code>Envelope</code> is being checked for
     *     containing
     * @param y the y-coordinate of the point which this <code>Envelope</code> is being checked for
     *     containing
     * @return <code>true</code> if <code>(x, y)</code> lies in the interior or on the boundary of
     *     this <code>Envelope</code>.
     */
    public boolean contains(double x, double y) {
        return x >= minx && x <= maxx && y >= miny && y <= maxy;
    }

    /**
     * Check if the region defined by <code>other</code> overlaps (intersects) the region of this
     * <code>Envelope</code>.
     *
     * @param other the <code>Envelope</code> which this <code>Envelope</code> is being checked for
     *     overlapping
     * @return <code>true</code> if the <code>Envelope</code>s overlap
     */
    public boolean intersects(Envelope other) {
        if (isNull() || other.isNull()) {
            return false;
        }
        return !(other.minx > maxx || other.maxx < minx || other.miny > maxy || other.maxy < miny);
    }

    /**
     * Check if the point <code>p</code> overlaps (lies inside) the region of this <code>Envelope
     * </code>.
     *
     * @param p the <code>Coordinate</code> to be tested
     * @return <code>true</code> if the point overlaps this <code>Envelope</code>
     */
    public boolean intersects(Coordinate p) {
        return intersects(p.x, p.y);
    }

    /**
     * Check if the point <code>(x, y)</code> overlaps (lies inside) the region of this <code>
     * Envelope</code>.
     *
     * @param x the x-ordinate of the point
     * @param y the y-ordinate of the point
     * @return <code>true</code> if the point overlaps this <code>Envelope</code>
     */
    public boolean intersects(double x, double y) {
        return !(x > maxx || x < minx || y > maxy || y < miny);
    }

    /**
     * Returns <code>true</code> if the <code>Envelope other</code> lies wholely inside this <code>
     * Envelope</code> (inclusive of the boundary).
     *
     * @param other the <code>Envelope</code> which this <code>Envelope</code> is being checked for
     *     containing
     * @return <code>true</code> if <code>other</code> is contained in this <code>Envelope</code>
     */
    public boolean contains(Envelope other) {
        if (isNull() || other.isNull()) {
            return false;
        }
        return other.getMinX() >= minx
                && other.getMaxX() <= maxx
                && other.getMinY() >= miny
                && other.getMaxY() <= maxy;
    }

    /**
     * Computes the distance between this and another <code>Envelope</code>. The distance between
     * overlapping Envelopes is 0. Otherwise, the distance is the Euclidean distance between the
     * closest points.
     */
    public double distance(Envelope env) {
        if (intersects(env)) return 0;
        double dx = 0.0;
        if (maxx < env.minx) dx = env.minx - maxx;
        if (minx > env.maxx) dx = minx - env.maxx;
        double dy = 0.0;
        if (maxy < env.miny) dy = env.miny - maxy;
        if (miny > env.maxy) dy = miny - env.maxy;

        // if either is zero, the envelopes overlap either vertically or
        // horizontally
        if (dx == 0.0) return dy;
        if (dy == 0.0) return dx;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean equals(Object other) {
        if (!(other instanceof Envelope)) {
            return false;
        }
        Envelope otherEnvelope = (Envelope) other;
        if (isNull()) {
            return otherEnvelope.isNull();
        }
        return maxx == otherEnvelope.getMaxX()
                && maxy == otherEnvelope.getMaxY()
                && minx == otherEnvelope.getMinX()
                && miny == otherEnvelope.getMinY();
    }

    public String toString() {
        return "Env[" + minx + " : " + maxx + ", " + miny + " : " + maxy + "]";
    }
}
