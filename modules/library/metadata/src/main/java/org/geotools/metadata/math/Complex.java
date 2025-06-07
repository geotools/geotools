/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.metadata.math;

import java.io.Serializable;
import org.geotools.api.util.Cloneable;

/**
 * A simple class for the handling of complex numbers. This is not the purpose of this class to provides a full-fledged
 * library for complex number handling. This class exists mostly for the limited needs of some transformation methods.
 *
 * <p>For performance reasons, the methods in this class never create new objects. They always operate on an object
 * specified in argument, and store the result in the object on which the method was invoked.
 *
 * <p>This class is final for performance reason.
 *
 * @since 2.2
 * @version $Id$
 * @author Justin Deoliveira
 * @author Martin Desruisseaux
 */
public final class Complex implements Cloneable, Serializable {
    /** For compatibility with previous versions during deserialization. */
    private static final long serialVersionUID = -8143196508298758583L;

    /** The real part of the complex number. */
    public double real;

    /** The imaginary part of the complex number. */
    public double imag;

    /** Creates a complex number initialized to (0,0). */
    public Complex() {}

    /** Creates a complex number initialized to the same value than the specified one. */
    public Complex(final Complex c) {
        real = c.real;
        imag = c.imag;
    }

    /** Creates a complex number initialized to the specified real and imaginary parts. */
    public Complex(final double real, final double imag) {
        this.real = real;
        this.imag = imag;
    }

    /**
     * Set this complex number to the same value than the specified one. This method computes the following:
     *
     * <blockquote>
     *
     * <pre>
     * this = c
     * </pre>
     *
     * </blockquote>
     */
    public void copy(final Complex c) {
        real = c.real;
        imag = c.imag;
    }

    /**
     * Multiplies a complex number by a scalar. This method computes the following:
     *
     * <blockquote>
     *
     * <pre>
     * this = c * s
     * </pre>
     *
     * </blockquote>
     */
    public void multiply(final Complex c, final double s) {
        real = c.real * s;
        imag = c.imag * s;
    }

    /**
     * Multplies two complex numbers. This method computes the following:
     *
     * <blockquote>
     *
     * <pre>
     * this = c1 * c2
     * </pre>
     *
     * </blockquote>
     */
    public void multiply(final Complex c1, final Complex c2) {
        final double x1 = c1.real;
        final double y1 = c1.imag;
        final double x2 = c2.real;
        final double y2 = c2.imag;
        real = (x1 * x2) - (y1 * y2);
        imag = (y1 * x2) + (x1 * y2);
    }

    /**
     * Divides one complex number by another. This method computes the following:
     *
     * <blockquote>
     *
     * <pre>
     * this = c1 / c2
     * </pre>
     *
     * </blockquote>
     */
    public void divide(final Complex c1, final Complex c2) {
        final double x1 = c1.real;
        final double y1 = c1.imag;
        final double x2 = c2.real;
        final double y2 = c2.imag;
        final double denom = (x2 * x2) + (y2 * y2);
        real = ((x1 * x2) + (y1 * y2)) / denom;
        imag = ((y1 * x2) - (x1 * y2)) / denom;
    }

    /**
     * Adds to complex numbers. This method computes the following:
     *
     * <blockquote>
     *
     * <pre>
     * this = c1 + c2
     * </pre>
     *
     * </blockquote>
     */
    public void add(final Complex c1, final Complex c2) {
        real = c1.real + c2.real;
        imag = c1.imag + c2.imag;
    }

    /**
     * Multplies two complex numbers, and add the result to a third one. This method computes the following:
     *
     * <blockquote>
     *
     * <pre>
     * this = c0 + (c1 * c2)
     * </pre>
     *
     * </blockquote>
     */
    public void addMultiply(final Complex c0, final Complex c1, final Complex c2) {
        final double x1 = c1.real;
        final double y1 = c1.imag;
        final double x2 = c2.real;
        final double y2 = c2.imag;
        real = c0.real + ((x1 * x2) - (y1 * y2));
        imag = c0.imag + ((y1 * x2) + (x1 * y2));
    }

    /**
     * Computes the integer power of a complex number up to 6. This method computes the following:
     *
     * <blockquote>
     *
     * <pre>
     * this = c ^ power
     * </pre>
     *
     * </blockquote>
     */
    public void power(final Complex c, final int power) {
        final double x = c.real;
        final double y = c.imag;
        switch (power) {
            case 0: {
                real = 1;
                imag = 0;
                break;
            }
            case 1: {
                real = x;
                imag = y;
                break;
            }
            case 2: {
                real = (x * x) - (y * y);
                imag = 2 * x * y;
                break;
            }
            case 3: {
                real = (x * x * x) - (3 * x * y * y);
                imag = (3 * x * x * y) - (y * y * y);
                break;
            }
            case 4: {
                real = (x * x * x * x) - (6 * x * x * y * y) + (y * y * y * y);
                imag = (4 * x * x * x * y) - (4 * x * y * y * y);
                break;
            }
            case 5: {
                real = (x * x * x * x * x) - (10 * x * x * x * y * y) + (5 * x * y * y * y * y);
                imag = (5 * x * x * x * x * y) - (10 * x * x * y * y * y) + (y * y * y * y * y);
                break;
            }
            case 6: {
                real = ((x * x * x * x * x * x) - (15 * x * x * x * x * y * y) + (15 * x * x * y * y * y * y))
                        - (y * y * y * y * y * y);
                imag = (6 * x * x * x * x * x * y) - (20 * x * x * x * y * y * y) + (6 * x * y * y * y * y * y);
                break;
            }
            default: {
                throw new IllegalArgumentException(String.valueOf(power));
            }
        }
    }

    /** Returns a copy of this complex number. */
    @Override
    public Complex clone() {
        return new Complex(this);
    }

    /** Returns {@code true} if this complex number has the same value than the specified one. */
    @SuppressWarnings("NonOverridingEquals") // Type-specific helper method, not overriding Object.equals
    public boolean equals(final Complex c) {
        return Double.doubleToLongBits(real) == Double.doubleToLongBits(c.real)
                && Double.doubleToLongBits(imag) == Double.doubleToLongBits(c.imag);
    }

    /** Compares this complex with the specified object for equality. */
    @Override
    public boolean equals(final Object c) {
        return (c instanceof Complex) && equals((Complex) c);
    }

    /** Returns a hash value for this complex number. */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(real) + 37 * Double.doubleToLongBits(imag);
        return (int) code ^ (int) (code >>> 32);
    }

    /** Returns a string representation of this complex number. */
    @Override
    public String toString() {
        return "Complex[" + real + ", " + imag + ']';
    }
}
