/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style.shape;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Decorator on top of the {@link Shape}. It extends the Shape interface to include a method
 * 'setBounds' for explicitly defining a bounding box (which is not necessarily associated with the
 * actual shape's bounds).
 * 
 * @author fmoura
 */
public class ExplicitBoundsShape implements Shape {

    private Shape shape;

    private Rectangle2D bounds = null;

    /**
     * The Constructor
     * 
     * @param shape
     *            The actual shape on top of which this decorator will stand.
     */
    public ExplicitBoundsShape(Shape shape) {
        if (shape == null)
            throw new IllegalArgumentException("Shape can't be null.");
        this.shape = shape;
    }

    /**
     * Sets the explicitly defined bounds for this shape.
     * 
     * @param bounds
     */
    public void setBounds(Rectangle2D bounds) {
        this.bounds = bounds;
    }

    public boolean contains(double x, double y, double w, double h) {
        return shape.contains(x, y, w, h);
    }

    public boolean contains(double x, double y) {
        return shape.contains(x, y);
    }

    public boolean contains(Point2D p) {
        return shape.contains((Point2D) p);
    }

    public boolean contains(Rectangle2D r) {
        return shape.contains((Rectangle2D) r);
    }

    /**
     * Returns the explicitly defined bounds for this shape. If no bounds were explicitly set, it
     * delegates the call to the actual shape.
     * 
     * @return the Rectangle representing the Shape's bounding box.
     * @see Shape
     */
    public Rectangle getBounds() {
        if (bounds != null)
            return new Rectangle((int) bounds.getMinX(), (int) bounds.getMinY(), (int) bounds
                    .getWidth(), (int) bounds.getHeight());
        return shape.getBounds();
    }

    /**
     * Returns the explicitly defined bounds for this shape. If no bounds were explicitly set, it
     * delegates the call to the actual shape.
     * 
     * @return the Rectangle2D representing the Shape's bounding box.
     * @see Shape
     */
    public Rectangle2D getBounds2D() {
        if (bounds != null)
            return bounds;
        return shape.getBounds2D();
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return shape.getPathIterator(at, flatness);
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return shape.getPathIterator(at);
    }

    public boolean intersects(double x, double y, double w, double h) {
        return shape.intersects(x, y, w, h);
    }

    public boolean intersects(Rectangle2D r) {
        return shape.intersects(r);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ExplicitBoundsShape) {
            ExplicitBoundsShape other = (ExplicitBoundsShape) obj;
            boolean result = shape.equals(other.shape);
            if (bounds == null)
                return result & (other.bounds == null);
            return result & bounds.equals(other.bounds);
        } else if (obj instanceof Shape) {
            if (bounds == null)
                return shape.equals(obj);
            return false;
        }
        return super.equals(obj);

    }

    @Override
    public int hashCode() {
        int hascode = shape.hashCode();
        if (bounds != null)
            hascode += hascode * 37 + bounds.hashCode();
        return hascode;
    }

}
