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

import java.awt.Shape;
import java.awt.geom.Point2D;

import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.operation.NoninvertibleTransformException;

import org.geotools.referencing.operation.matrix.XMatrix;


/**
 * Concatenated transform where both transforms are two-dimensional.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class ConcatenatedTransformDirect2D extends ConcatenatedTransformDirect
                                       implements MathTransform2D
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 6009454091075588885L;

    /**
     * The first math transform. This field is identical
     * to {@link ConcatenatedTransform#transform1}. Only
     * the type is different.
     */
    private final MathTransform2D transform1;

    /**
     * The second math transform. This field is identical
     * to {@link ConcatenatedTransform#transform1}. Only
     * the type is different.
     */
    private final MathTransform2D transform2;

    /**
     * Constructs a concatenated transform.
     */
    public ConcatenatedTransformDirect2D(final MathTransform2D transform1,
                                         final MathTransform2D transform2)
    {
        super(transform1, transform2);
        this.transform1 = transform1;
        this.transform2 = transform2;
    }

    /**
     * Checks if transforms are compatibles with this implementation.
     */
    @Override
    boolean isValid() {
        return super.isValid() && getSourceDimensions()==2 && getTargetDimensions()==2;
    }

    /**
     * Transforms the specified {@code ptSrc}
     * and stores the result in {@code ptDst}.
     */
    @Override
    public Point2D transform(final Point2D ptSrc, Point2D ptDst) throws TransformException {
        assert isValid();
        ptDst = transform1.transform(ptSrc, ptDst);
        return  transform2.transform(ptDst, ptDst);
    }

    /**
     * Transforms the specified shape.
     */
    @Override
    public Shape createTransformedShape(final Shape shape) throws TransformException {
        assert isValid();
        return transform2.createTransformedShape(transform1.createTransformedShape(shape));
    }

    /**
     * Gets the derivative of this transform at a point.
     *
     * @param  point The coordinate point where to evaluate the derivative.
     * @return The derivative at the specified point (never {@code null}).
     * @throws TransformException if the derivative can't be evaluated at the specified point.
     */
    @Override
    public Matrix derivative(final Point2D point) throws TransformException {
        final XMatrix matrix1 = toXMatrix(transform1.derivative(point));
        final XMatrix matrix2 = toXMatrix(transform2.derivative(transform1.transform(point,null)));
        matrix2.multiply(matrix1);
        return matrix2;
    }

    /**
     * Creates the inverse transform of this object.
     */
    @Override
    public MathTransform2D inverse() throws NoninvertibleTransformException {
        return (MathTransform2D) super.inverse();
    }
}
