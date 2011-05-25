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

import java.awt.geom.Dimension2D;
import java.io.Serializable;


/**
 * Implement float and double version of {@link Dimension2D}. This class
 * is only temporary; it will disappear if <em>JavaSoft</em> implements
 * {@code Dimension2D.Float} and {@code Dimension2D.Double}.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class XDimension2D {
    /**
     * Do not allow instantiation of this class.
     */
    private XDimension2D() {
    }

    /**
     * Implement float version of {@link Dimension2D}. This class is
     * temporary;  it will disappear if <em>JavaSoft</em> implements
     * {@code Dimension2D.Float} and {@code Dimension2D.Double}.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    public static final class Float extends Dimension2D implements Serializable {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = 4011566975974105082L;

        /**
         * The width.
         */
        public float width;

        /**
         * The height.
         */
        public float height;

        /**
         * Construct a new dimension initialized to (0,0).
         */
        public Float() {
        }

        /**
         * Construct a new dimension with the specified values.
         *
         * @param w The width.
         * @param h The height.
         */
        public Float(final float w, final float h) {
            width  = w;
            height = h;
        }

        /**
         * Set width and height for this dimension.
         *
         * @param w The width.
         * @param h The height.
         */
        public void setSize(final double w, final double h) {
            width  = (float) w;
            height = (float) h;
        }

        /**
         * Returns the width.
         */
        public double getWidth() {
            return width;
        }

        /**
         * Returns the height.
         */
        public double getHeight() {
            return height;
        }

        /**
         * Returns a string representation of this dimension.
         */
        @Override
        public String toString() {
            return "Dimension2D[" + width + ',' + height + ']';
        }
    }

    /**
     * Implement double version of {@link Dimension2D}. This class is
     * temporary; it will disappear if <em>JavaSoft</em> implements
     * {@code Dimension2D.Float} and {@code Dimension2D.Double}.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    public static final class Double extends Dimension2D implements Serializable {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = 3603763914115376884L;

        /**
         * The width.
         */
        public double width;

        /**
         * The height.
         */
        public double height;

        /**
         * Construct a new dimension initialized to (0,0).
         */
        public Double() {
        }

        /**
         * Construct a new dimension with the specified values.
         *
         * @param w The width.
         * @param h The height.
         */
        public Double(final double w, final double h) {
            width  = w;
            height = h;
        }

        /**
         * Set width and height for this dimension.
         *
         * @param w The width.
         * @param h The height.
         */
        public void setSize(final double w, final double h) {
            width  = w;
            height = h;
        }

        /**
         * Returns the width.
         */
        public double getWidth() {
            return width;
        }

        /**
         * Returns the height.
         */
        public double getHeight() {
            return height;
        }

        /**
         * Returns a string representation of this dimension.
         */
        @Override
        public String toString() {
            return "Dimension2D[" + width + ',' + height + ']';
        }
    }
}
