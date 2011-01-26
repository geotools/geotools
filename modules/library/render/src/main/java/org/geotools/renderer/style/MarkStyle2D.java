/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style;

// J2SE dependencies
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import org.geotools.geometry.jts.TransformedShape;
import org.geotools.resources.Classes;


/**
 * Style to represent points as small filled and stroked shapes
 *
 * @author Andrea Aime
 * @source $URL$
 * @version $Id$
 */
public class MarkStyle2D extends PolygonStyle2D {
    Shape shape;
    int size;
    float rotation;

    /**
     * Returns the shape rotation, in radians
     *
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * Returns the shape to be used to render the mark
     *
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Returns a shape that can be used to draw the mark at the x, y coordinates
     * with appropriated rotation and size (according to the current style)
     *
     * @param x the x coordinate where the mark will be drawn
     * @param y the y coordinate where the mark will be drawn
     *
     * @return a shape that can be used to draw the mark 
     */
    public Shape getTransformedShape(float x, float y) {
        return getTransformedShape(x, y, rotation);
    }
    
    /**
     * Returns a shape that can be used to draw the mark at the x, y coordinates
     * with appropriated rotation and size (according to the current style)
     *
     * @param x the x coordinate where the mark will be drawn
     * @param y the y coordinate where the mark will be drawn
     * @param rotation a custom rotation, other than the mark build in one
     *
     * @return a shape that can be used to draw the mark 
     */
    public Shape getTransformedShape(float x, float y, float rotation) {
        if (shape != null) {
            Rectangle2D bounds = shape.getBounds2D();
            double shapeSize = bounds.getHeight();
            double scale = size / shapeSize;
            TransformedShape ts = new TransformedShape();
            ts.shape = shape;
            ts.translate(x, y);
            ts.rotate(rotation);
            // flip the symbol to take into account the screen orientation
            // where the y grows from top to bottom
            ts.scale(scale, -scale);

            return ts;
        } else {
            return null;
        }
    }


    /**
     * Returns the size of the shape, in pixels
     *
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the shape rotation, in radians
     *
     * @param f
     */
    public void setRotation(float f) {
        rotation = f;
    }

    /**
     * Sets the shape to be used to render the mark
     *
     * @param shape
     */
    public void setShape(Shape shape) {
        this.shape = shape;
    }

    /**
     * Sets the size of the shape, in pixels
     *
     * @param i
     */
    public void setSize(int i) {
        size = i;
    }

    /**
     * Returns a string representation of this style.
     */
    public String toString() {
        return Classes.getShortClassName(this) + '[' + shape + ']';
    }
}
