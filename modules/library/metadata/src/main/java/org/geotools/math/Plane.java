/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 1998-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.math;

import java.awt.geom.Point2D;
import java.io.Serializable;

import org.opengis.util.Cloneable;


/**
 * Equation of a plane in a three-dimensional space (<var>x</var>,<var>y</var>,<var>z</var>).
 * The plane equation is expressed by {@link #c}, {@link #cx} and {@link #cy} coefficients as
 * below:
 *
 * <blockquote>
 *     <var>z</var>(<var>x</var>,<var>y</var>) = <var>c</var> +
 *     <var>cx</var>*<var>x</var> + <var>cy</var>*<var>y</var>
 * </blockquote>
 *
 * Those coefficients can be set directly, or computed by a linear regression of this plane
 * through a set of three-dimensional points.
 *
 * @since 2.0
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (PMO, IRD)
 * @author Howard Freeland, for algorithmic inspiration
 */
public class Plane implements Cloneable, Serializable {
    
    /** Point3d data structure used to define Plane. */
    public static class Point3d {
        double x,y,z;
        public Point3d( double x, double y, double z){
            this.x=x;
            this.y=y;
            this.z=z;
        }
        public Point3d( double array[]) throws IllegalArgumentException {
            if( array == null){
                throw new NullPointerException("Vector array required");
            }
            if( array.length == 2 ){
                this.x = array[0];
                this.y = array[1];
                this.z = Double.NaN;
            }
            else if( array.length == 3 ){
                this.x = array[0];
                this.y = array[1];
                this.z = array[2];
            }
            else {
                throw new IllegalArgumentException("Vector array (length " + array.length +") is not expected length of 3");
            }
        }
        public Point3d( Point2D point ){
            this.x = point.getX();
            this.y = point.getY();
            this.z = Double.NaN;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            long temp;
            temp = Double.doubleToLongBits(x);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(y);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(z);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Point3d other = (Point3d) obj;
            if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
                return false;
            if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
                return false;
            if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
                return false;
            return true;
        }
        @Override
        public String toString() {
            return "Point3d [x=" + x + ", y=" + y + ", z=" + z + "]";
        }
        /**
         * Cartesian distance between two points.
         * 
         * @param pt second point
         * @return cartesian distance between two points
         */
        public double distance(Point3d pt) {
            double dX = Math.abs(this.x-pt.x);
            double dY = Math.abs(this.y-pt.y);
            double dZ = Math.abs(this.z-pt.z);
            
            return Math.sqrt( dX*dX + dY*dY + dZ*dZ);
        }
    }
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 2956201711131316723L;

    /**
     * The <var>c</var> coefficient for this plane. This coefficient appears in the place equation
     * <var><strong>c</strong></var>+<var>cx</var>*<var>x</var>+<var>cy</var>*<var>y</var>.
     */
    public double c;

    /**
     * The <var>cx</var> coefficient for this plane. This coefficient appears in the place equation
     * <var>c</var>+<var><strong>cx</strong></var>*<var>x</var>+<var>cy</var>*<var>y</var>.
     */
    public double cx;

    /**
     * The <var>cy</var> coefficient for this plane. This coefficient appears in the place equation
     * <var>c</var>+<var>cx</var>*<var>x</var>+<var><strong>cy</strong></var>*<var>y</var>.
     */
    public double cy;

    /**
     * Construct a new plane. All coefficients are set to 0.
     */
    public Plane() {
    }

    /**
     * Computes the <var>z</var> value for the specified (<var>x</var>,<var>y</var>) point.
     * The <var>z</var> value is computed using the following equation:
     *
     * <blockquote><code>
     *   z(x,y) = c + cx*x + cy*y
     * </code></blockquote>
     *
     * @param x The <var>x</var> value.
     * @param y The <var>y</var> value.
     * @return  The <var>z</var> value.
     */
    public final double z(final double x, final double y) {
        return c + cx*x + cy*y;
    }

    /**
     * Computes the <var>y</var> value for the specified (<var>x</var>,<var>z</var>) point.
     * The <var>y</var> value is computed using the following equation:
     *
     * <blockquote><code>
     *   y(x,z) = (z - (c+cx*x)) / cy
     * </code></blockquote>
     *
     * @param x The <var>x</var> value.
     * @param z The <var>y</var> value.
     * @return  The <var>y</var> value.
     */
    public final double y(final double x, final double z) {
        return (z - (c+cx*x)) / cy;
    }

    /**
     * Computes the <var>x</var> value for the specified (<var>y</var>,<var>z</var>) point.
     * The <var>x</var> value is computed using the following equation:
     *
     * <blockquote><code>
     *   x(y,z) = (z - (c+cy*y)) / cx
     * </code></blockquote>
     *
     * @param y The <var>x</var> value.
     * @param z The <var>y</var> value.
     * @return  The <var>x</var> value.
     */
    public final double x(final double y, final double z) {
        return (z - (c+cy*y)) / cx;
    }

    /**
     * Computes the plane's coefficients from the specified points.  Three points
     * are enough for determining exactly the plan, providing that the points are
     * not colinear.
     *
     * @throws ArithmeticException If the three points are colinear.
     */
    public void setPlane(final Point3d P1, final Point3d P2, final Point3d P3)
            throws ArithmeticException
    {
        final double m00 = P2.x*P3.y - P3.x*P2.y;
        final double m01 = P3.x*P1.y - P1.x*P3.y;
        final double m02 = P1.x*P2.y - P2.x*P1.y;
        final double det = m00 + m01 + m02;
        if (det == 0) {
            throw new ArithmeticException("Points are colinear");
        }
        c  = ((   m00   )*P1.z + (   m01   )*P2.z + (   m02   )*P3.z) / det;
        cx = ((P2.y-P3.y)*P1.z + (P3.y-P1.y)*P2.z + (P1.y-P2.y)*P3.z) / det;
        cy = ((P3.x-P2.x)*P1.z + (P1.x-P3.x)*P2.z + (P2.x-P1.x)*P3.z) / det;
    }

    /**
     * Computes the plane's coefficients from a set of points. This method use
     * a linear regression in the least-square sense. Result is undertermined
     * if all points are colinear.
     *
     * @param x vector of <var>x</var> coordinates
     * @param y vector of <var>y</var> coordinates
     * @param z vector of <var>z</var> values
     *
     * @throws MismatchedSizeException if <var>x</var>, <var>y</var> and <var>z</var>
     *         don't have the same length.
     */
    public void setPlane(final double[] x, final double[] y, final double[] z)
            throws IllegalArgumentException
    {
        final int N = x.length;
        if (N!=y.length || N!=z.length) {
            throw new IllegalArgumentException("Vector x (length " + N + "), Vector y (length:"
                    + y.length + ") and Vector (length:" + z.length + ") are not the same length");
        }
        double sum_x  = 0;
        double sum_y  = 0;
        double sum_z  = 0;
        double sum_xx = 0;
        double sum_yy = 0;
        double sum_xy = 0;
        double sum_zx = 0;
        double sum_zy = 0;
        for (int i=0; i<N; i++) {
            final double xi = x[i];
            final double yi = y[i];
            final double zi = z[i];
            sum_x  += xi;
            sum_y  += yi;
            sum_z  += zi;
            sum_xx += xi*xi;
            sum_yy += yi*yi;
            sum_xy += xi*yi;
            sum_zx += zi*xi;
            sum_zy += zi*yi;
        }
        /*
         *    ( sum_zx - sum_z*sum_x )  =  cx*(sum_xx - sum_x*sum_x) + cy*(sum_xy - sum_x*sum_y)
         *    ( sum_zy - sum_z*sum_y )  =  cx*(sum_xy - sum_x*sum_y) + cy*(sum_yy - sum_y*sum_y)
         */
        final double ZX = sum_zx - sum_z*sum_x/N;
        final double ZY = sum_zy - sum_z*sum_y/N;
        final double XX = sum_xx - sum_x*sum_x/N;
        final double XY = sum_xy - sum_x*sum_y/N;
        final double YY = sum_yy - sum_y*sum_y/N;
        final double den= (XY*XY - XX*YY);

        cy = (ZX*XY - ZY*XX) / den;
        cx = (ZY*XY - ZX*YY) / den;
        c  = (sum_z - (cx*sum_x + cy*sum_y)) / N;
    }

    /**
     * Returns a string representation of this plane.
     * The string will contains the plane's equation, as below:
     *
     * <blockquote>
     *     <var>z</var>(<var>x</var>,<var>y</var>) = {@link #c} +
     *     {@link #cx}*<var>x</var> + {@link #cy}*<var>y</var>
     * </blockquote>
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder("z(x,y)= ");
        if (c==0 && cx==0 && cy==0) {
            buffer.append(0);
        } else {
            if (c != 0) {
                buffer.append(c).append(" + ");
            }
            if (cx != 0) {
                buffer.append(cx).append("*x");
                if (cy != 0) {
                    buffer.append(" + ");
                }
            }
            if (cy != 0) {
                buffer.append(cy).append("*y");
            }
        }
        return buffer.toString();
    }

    /**
     * Compares this plane with the specified object for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object!=null && getClass().equals(object.getClass())) {
            final Plane that = (Plane) object;
            return Double.doubleToLongBits(this.c ) == Double.doubleToLongBits(that.c ) &&
                   Double.doubleToLongBits(this.cx) == Double.doubleToLongBits(that.cx) &&
                   Double.doubleToLongBits(this.cy) == Double.doubleToLongBits(that.cy);
        } else {
            return false;
        }
    }

    /**
     * Returns a hash code value for this plane.
     */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(c ) +
                      37*(Double.doubleToLongBits(cx) +
                      37*(Double.doubleToLongBits(cy)));
        return (int) code ^ (int) (code >>> 32);
    }

    /**
     * Returns a clone of this plane.
     */
    @Override
    public Plane clone() {
        try {
            return (Plane) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError(exception);
        }
    }
}
