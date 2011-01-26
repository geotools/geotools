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
package org.geotools.referencing.operation.transform;

import java.awt.geom.AffineTransform;
import java.io.Serializable;

import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.geometry.DirectPosition;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.matrix.MatrixFactory;


/**
 * The identity transform. The data are only copied without any transformation. This class is
 * used for identity transform of dimension greater than 2. For 1D and 2D identity transforms,
 * {@link LinearTransform1D} and {@link java.awt.geom.AffineTransform} already provide their
 * own optimisations.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class IdentityTransform extends AbstractMathTransform
                            implements LinearTransform, Serializable
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -5339040282922138164L;

    /**
     * The input and output dimension.
     */
    private final int dimension;

    /**
     * Identity transforms for dimensions ranging from to 0 to 7.
     * Elements in this array will be created only when first requested.
     */
    private static final LinearTransform[] POOL = new LinearTransform[8];

    /**
     * Constructs an identity transform of the specified dimension.
     */
    protected IdentityTransform(final int dimension) {
        this.dimension = dimension;
    }

    /**
     * Constructs an identity transform of the specified dimension.
     */
    public static synchronized LinearTransform create(final int dimension) {
        LinearTransform candidate;
        if (dimension < POOL.length) {
            candidate = POOL[dimension];
            if (candidate != null) {
                return candidate;
            }
        }
        switch (dimension) {
            case 1:  candidate = LinearTransform1D.IDENTITY;                   break;
            case 2:  candidate = new AffineTransform2D(new AffineTransform()); break;
            default: candidate = new IdentityTransform(dimension);             break;
        }
        if (dimension < POOL.length) {
            POOL[dimension] = candidate;
        }
        return candidate;
    }

    /**
     * Tests whether this transform does not move any points.
     * This implementation always returns {@code true}.
     */
    @Override
    public boolean isIdentity() {
        return true;
    }

    /**
     * Tests whether this transform does not move any points.
     * This implementation always returns {@code true}.
     */
    public boolean isIdentity(double tolerance) {
        return true;
    }

    /**
     * Gets the dimension of input points.
     */
    public int getSourceDimensions() {
        return dimension;
    }

    /**
     * Gets the dimension of output points.
     */
    public int getTargetDimensions() {
        return dimension;
    }

    /**
     * Returns the parameter descriptors for this math transform.
     */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return ProjectiveTransform.ProviderAffine.PARAMETERS;
    }

    /**
     * Returns the matrix elements as a group of parameters values.
     *
     * @return A copy of the parameter values for this math transform.
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        return ProjectiveTransform.getParameterValues(getMatrix());
    }

    /**
     * Returns a copy of the identity matrix.
     */
    public Matrix getMatrix() {
        return MatrixFactory.create(dimension+1);
    }

    /**
     * Gets the derivative of this transform at a point. For an identity transform,
     * the derivative is the same everywhere.
     */
    @Override
    public Matrix derivative(final DirectPosition point) {
        return MatrixFactory.create(dimension);
    }

    /**
     * Copies the values from {@code ptSrc} to {@code ptDst}.
     * Overrides the super-class method for performance reason.
     *
     * @since 2.2
     */
    @Override
    public DirectPosition transform(final DirectPosition ptSrc, final DirectPosition ptDst) {
        if (ptSrc.getDimension() == dimension) {
            if (ptDst == null) {
                return new GeneralDirectPosition(ptSrc);
            }
            if (ptDst.getDimension() == dimension) {
                for (int i=0; i<dimension; i++) {
                    ptDst.setOrdinate(i, ptSrc.getOrdinate(i));
                }
                return ptDst;
            }
        }
        try {
            // The super class will take care of throwing the MismatchedDimensionException.
            return super.transform(ptSrc, ptDst);
        } catch (TransformException e) {
            throw new AssertionError(e); // Should never happen.
        }
    }

    /**
     * Transforms an array of floating point coordinates by this transform.
     */
    @Override
    public void transform(final float[] srcPts, int srcOff,
                          final float[] dstPts, int dstOff, int numPts)
    {
        System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts*dimension);
    }

    /**
     * Transforms an array of floating point coordinates by this transform.
     */
    public void transform(final double[] srcPts, int srcOff,
                          final double[] dstPts, int dstOff, int numPts)
    {
        System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts*dimension);
    }

    /**
     * Returns the inverse transform of this object, which
     * is this transform itself
     */
    @Override
    public MathTransform inverse() {
        return this;
    }

    /**
     * Returns a hash value for this transform.
     * This value need not remain consistent between
     * different implementations of the same class.
     */
    @Override
    public int hashCode() {
        return (int)serialVersionUID + dimension;
    }

    /**
     * Compares the specified object with
     * this math transform for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final IdentityTransform that = (IdentityTransform) object;
            return this.dimension == that.dimension;
        }
        return false;
    }
}
