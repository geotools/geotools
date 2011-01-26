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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.ObjectStreamException;
import java.io.Serializable;


/**
 * An immutable subclass of a {@link Rectangle2D} with bounds extending toward infinities.
 * The {@link #getMinX} and {@link #getMinY} methods return always
 * {@link java.lang.Double#NEGATIVE_INFINITY},  while the {@link #getMaxX} and
 * {@link #getMaxY} methods return always {@link java.lang.Double#POSITIVE_INFINITY}.
 * This rectangle can be used as argument in the {@link XRectangle2D} constructor for
 * initializing a new {@code XRectangle2D} to infinite bounds.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class InfiniteRectangle2D extends Rectangle2D implements Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 5281254268988984523L;

    /**
     * A singleton instance of {@code InfiniteRectangle2D}.
     */
    public static final Rectangle2D INFINITY = new InfiniteRectangle2D();

    /**
     * Private constructor for the {@link #INFINITY} singleton only.
     */
    private InfiniteRectangle2D() {
    }

    /**
     * Returns the minimum value, which is negative infinity.
     */
    public double getX() {
        return java.lang.Double.NEGATIVE_INFINITY;
    }

    /**
     * Returns the minimum value, which is negative infinity.
     */
    public double getY() {
        return java.lang.Double.NEGATIVE_INFINITY;
    }

    /**
     * Returns the minimum value, which is negative infinity.
     */
    @Override
    public double getMinX() {
        return java.lang.Double.NEGATIVE_INFINITY;
    }

    /**
     * Returns the minimum value, which is negative infinity.
     */
    @Override
    public double getMinY() {
        return java.lang.Double.NEGATIVE_INFINITY;
    }

    /**
     * Returns the maximum value, which is positive infinity.
     */
    @Override
    public double getMaxX() {
        return java.lang.Double.POSITIVE_INFINITY;
    }

    /**
     * Returns the maximum value, which is positive infinity.
     */
    @Override
    public double getMaxY() {
        return java.lang.Double.POSITIVE_INFINITY;
    }

    /**
     * Returns the width, which is positive infinity.
     */
    public double getWidth() {
        return java.lang.Double.POSITIVE_INFINITY;
    }

    /**
     * Returns the height, which is positive infinity.
     */
    public double getHeight() {
        return java.lang.Double.POSITIVE_INFINITY;
    }

    /**
     * Returns the center, which is NaN since we can't compute a center from infinite bounds.
     */
    @Override
    public double getCenterX() {
        return java.lang.Double.NaN;
    }

    /**
     * Returns the center, which is NaN since we can't compute a center from infinite bounds.
     */
    @Override
    public double getCenterY() {
        return java.lang.Double.NaN;
    }

    /**
     * Do nothing, since we can't extends an infinite rectangle.
     */
    @Override
    public void add(Rectangle2D rect) {
    }

    /**
     * Do nothing, since we can't extends an infinite rectangle.
     */
    @Override
    public void add(Point2D point) {
    }

    /**
     * Do nothing, since we can't extends an infinite rectangle.
     */
    @Override
    public void add(double x, double y) {
    }

    /**
     * Returns 0, since the specified point can't be outside this rectangle.
     */
    public int outcode(double x, double y) {
        return 0;
    }

    /**
     * Returns 0, since the specified point can't be outside this rectangle.
     */
    @Override
    public int outcode(Point2D point) {
        return 0;
    }

    /**
     * Returns {@code true} since this rectangle contains all points.
     */
    @Override
    public boolean contains(Point2D point) {
        return true;
    }

    /**
     * Returns {@code true} since this rectangle contains all points.
     */
    @Override
    public boolean contains(Rectangle2D rect) {
        return true;
    }

    /**
     * Returns {@code true} since this rectangle contains all points.
     */
    @Override
    public boolean contains(double x, double y) {
        return true;
    }

    /**
     * Returns {@code true} since this rectangle contains all points.
     */
    @Override
    public boolean contains(double x, double y, double w, double h) {
        return true;
    }

    /**
     * Returns {@code true} since this rectangle contains all points.
     */
    @Override
    public boolean intersects(Rectangle2D rect) {
        return true;
    }

    /**
     * Returns {@code true} since this rectangle contains all points.
     */
    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return true;
    }

    /**
     * Returns {@code true} since this rectangle contains all points.
     */
    @Override
    public boolean intersectsLine(double x, double y, double u, double v) {
        return true;
    }

    /**
     * Returns {@code true} since this rectangle contains all points.
     */
    @Override
    public boolean intersectsLine(Line2D line) {
        return true;
    }

    /**
     * Returns {@code false} since an infinite rectangle is far from empty.
     */
    public boolean isEmpty() {
        return false;
    }

    /**
     * Returns {@code this}.
     * No need to returns a clone, since this rectangle is immutable.
     */
    @Override
    public Rectangle2D getFrame() {
        return this;
    }

    /**
     * Returns {@code this}.
     * No need to returns a clone, since this rectangle is immutable.
     */
    @Override
    public Rectangle2D getBounds2D() {
        return this;
    }

    /**
     * Returns {@code this}, since this rectangle can't be extended.
     * No need to returns a clone, since this rectangle is immutable.
     */
    public Rectangle2D createUnion(Rectangle2D rect) {
        return this;
    }

    /**
     * Returns a clone of the specified rectangle.
     */
    public Rectangle2D createIntersection(Rectangle2D rect) {
        return (Rectangle2D)rect.clone();
    }

    /**
     * Always throws an exception, since this rectangle is immutable.
     *
     * @todo Throws UnmodifiableGeometryException instead?
     *       (defined in renderer module for now)
     */
    public void setRect(double x, double y, double w, double h) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the singleton instance of {@code InfiniteRectangle2D}.
     */
    private Object readResolve() throws ObjectStreamException {
        return INFINITY;
    }
}
