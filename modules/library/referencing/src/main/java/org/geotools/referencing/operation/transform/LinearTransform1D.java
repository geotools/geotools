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
package org.geotools.referencing.operation.transform;

import java.io.Serializable;

import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.geometry.DirectPosition;

import org.geotools.referencing.operation.matrix.Matrix1;
import org.geotools.referencing.operation.matrix.Matrix2;
import org.geotools.referencing.operation.LinearTransform;


/**
 * A one dimensional, linear transform. Input values <var>x</var> are converted into
 * output values <var>y</var> using the following equation:
 *
 * <p align="center"><var>y</var> &nbsp;=&nbsp;
 * {@linkplain #offset} + {@linkplain #scale}&times;<var>x</var></p>
 *
 * This class is the same as a 2&times;2 affine transform. However, this specialized
 * {@code LinearTransform1D} class is faster. It is defined there because extensively
 * used by {@link org.geotools.coverage.grid.GridCoverage2D}.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see LogarithmicTransform1D
 * @see ExponentialTransform1D
 */
public class LinearTransform1D extends AbstractMathTransform
                            implements MathTransform1D, LinearTransform, Serializable
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -7595037195668813000L;

    /**
     * The identity transform.
     */
    public static final LinearTransform1D IDENTITY = IdentityTransform1D.ONE;

    /**
     * The value which is multiplied to input values.
     */
    public final double scale;

    /**
     * The value to add to input values.
     */
    public final double offset;

    /**
     * The inverse of this transform. Created only when first needed.
     */
    private transient MathTransform1D inverse;

    /**
     * Constructs a new linear transform. This constructor is provided for subclasses only.
     * Instances should be created using the {@linkplain #create factory method}, which
     * may returns optimized implementations for some particular argument values.
     *
     * @param scale  The {@code scale}  term in the linear equation.
     * @param offset The {@code offset} term in the linear equation.
     */
    protected LinearTransform1D(final double scale, final double offset) {
        this.scale   = scale;
        this.offset  = offset;
    }

    /**
     * Constructs a new linear transform.
     *
     * @param scale  The {@code scale}  term in the linear equation.
     * @param offset The {@code offset} term in the linear equation.
     */
    public static LinearTransform1D create(final double scale, final double offset) {
        if (scale == 0) {
            return new ConstantTransform1D(offset);
        }
        if (scale==1 && offset==0) {
            return IDENTITY;
        }
        return new LinearTransform1D(scale, offset);
    }

    /**
     * Returns the parameter descriptors for this math transform.
     */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return ProjectiveTransform.ProviderAffine.PARAMETERS;
    }

    /**
     * Returns the matrix elements as a group of parameters values. The number of parameters
     * depends on the matrix size. Only matrix elements different from their default value
     * will be included in this group.
     *
     * @return A copy of the parameter values for this math transform.
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        return ProjectiveTransform.getParameterValues(getMatrix());
    }

    /**
     * Gets the dimension of input points, which is 1.
     */
    public int getSourceDimensions() {
        return 1;
    }

    /**
     * Gets the dimension of output points, which is 1.
     */
    public int getTargetDimensions() {
        return 1;
    }

    /**
     * Returns this transform as an affine transform matrix.
     */
    public Matrix getMatrix() {
        return new Matrix2(scale, offset, 0, 1);
    }

    /**
     * Creates the inverse transform of this object.
     */
    @Override
    public MathTransform1D inverse() throws NoninvertibleTransformException {
        if (inverse == null) {
            if (isIdentity()) {
                inverse = this;
            } else if (scale != 0) {
                final LinearTransform1D inverse;
                inverse = create(1/scale, -offset/scale);
                inverse.inverse = this;
                this.inverse = inverse;
            } else {
                inverse = (MathTransform1D) super.inverse();
            }
        }
        return inverse;
    }

    /**
     * Tests whether this transform does not move any points.
     */
    @Override
    public boolean isIdentity() {
       return isIdentity(0);
    }

    /**
     * Tests whether this transform does not move any points by using the provided tolerance.
     * This method work in the same way than
     * {@link org.geotools.referencing.operation.matrix.XMatrix#isIdentity(double)}.
     *
     * @since 2.3.1
     */
    public boolean isIdentity(double tolerance) {
        tolerance = Math.abs(tolerance);
        return Math.abs(offset) <= tolerance && Math.abs(scale-1) <= tolerance;
    }

    /**
     * Gets the derivative of this transform at a point.  This implementation is different
     * from the default {@link AbstractMathTransform#derivative} implementation in that no
     * coordinate point is required and {@link Double#NaN} may be a legal output value for
     * some users.
     */
    @Override
    public Matrix derivative(final DirectPosition point) throws TransformException {
        return new Matrix1(scale);
    }

    /**
     * Gets the derivative of this function at a value.
     */
    public double derivative(final double value) {
        return scale;
    }

    /**
     * Transforms the specified value.
     */
    public double transform(double value) {
        return offset + scale*value;
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     */
    @Override
    public void transform(final float[] srcPts, int srcOff,
                          final float[] dstPts, int dstOff, int numPts)
    {
        if (srcPts!=dstPts || srcOff>=dstOff) {
            while (--numPts >= 0) {
                dstPts[dstOff++] = (float) (offset + scale*srcPts[srcOff++]);
            }
        } else {
            srcOff += numPts;
            dstOff += numPts;
            while (--numPts >= 0) {
                dstPts[--dstOff] = (float) (offset + scale*srcPts[--srcOff]);
            }
        }
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     */
    public void transform(final double[] srcPts, int srcOff,
                          final double[] dstPts, int dstOff, int numPts)
    {
        if (srcPts!=dstPts || srcOff>=dstOff) {
            while (--numPts >= 0) {
                dstPts[dstOff++] = offset + scale*srcPts[srcOff++];
            }
        } else {
            srcOff += numPts;
            dstOff += numPts;
            while (--numPts >= 0) {
                dstPts[--dstOff] = offset + scale*srcPts[--srcOff];
            }
        }
    }

    /**
     * Returns a hash value for this transform.
     * This value need not remain consistent between
     * different implementations of the same class.
     */
    @Override
    public int hashCode() {
        long code;
        code = (int)serialVersionUID + Double.doubleToRawLongBits(offset);
        code =  code*37              + Double.doubleToRawLongBits(scale);
        return (int)(code >>> 32) ^ (int)code;
    }

    /**
     * Compares the specified object with this math transform for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final LinearTransform1D that = (LinearTransform1D) object;
            return Double.doubleToRawLongBits(this.scale)  == Double.doubleToRawLongBits(that.scale) &&
                   Double.doubleToRawLongBits(this.offset) == Double.doubleToRawLongBits(that.offset);
            /*
             * NOTE: 'LinearTransform1D' and 'ConstantTransform1D' are heavily used by 'Category'
             *       from 'org.geotools.cv' package. It is essential for Cateory to differenciate
             *       various NaN values. Because 'equals' is used by CanonicalSet.unique(Object)
             *       (which is used by 'DefaultMathTransformFactory'), test for equality can't use
             *       the doubleToLongBits method because it collapse all NaN into a single canonical
             *       value. The 'doubleToRawLongBits' instead provided the needed functionality.
             */
        }
        return false;
    }
}
