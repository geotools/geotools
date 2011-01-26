/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.operation;

import java.awt.Shape;
import java.awt.geom.Point2D;
import org.opengis.annotation.Extension;


/**
 * Transforms two-dimensional coordinate points. {@link CoordinateOperation#getMathTransform} may
 * returns instance of this interface when source and destination coordinate systems are both two
 * dimensional. {@code MathTransform2D} extends {@link MathTransform} by adding some methods for
 * easier interoperability with <A HREF="http://java.sun.com/products/java-media/2D/">Java2D</A>.
 * <p>
 * If the transformation is affine, then {@code MathTransform} shall be an
 * immutable instance of {@link java.awt.geom.AffineTransform}.
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@Extension
public interface MathTransform2D extends MathTransform {
    /**
     * Transforms the specified {@code ptSrc} and stores the result in {@code ptDst}.
     * If {@code ptDst} is {@code null}, a new {@link Point2D} object is allocated
     * and then the result of the transformation is stored in this object. In either case,
     * {@code ptDst}, which contains the transformed point, is returned for convenience.
     * If {@code ptSrc} and {@code ptDst} are the same object, the input point is
     * correctly overwritten with the transformed point.
     *
     * @param  ptSrc the coordinate point to be transformed.
     * @param  ptDst the coordinate point that stores the result of transforming {@code ptSrc},
     *         or {@code null} if a new point should be created.
     * @return the coordinate point after transforming {@code ptSrc} and stroring the result
     *         in {@code ptDst} or in a new point if {@code ptDst} was null.
     * @throws TransformException if the point can't be transformed.
     */
    Point2D transform(final Point2D ptSrc, final Point2D ptDst) throws TransformException;

    /**
     * Transforms the specified shape. This method may replace straight lines by quadratic curves
     * when applicable. It may also do the opposite (replace curves by straight lines). The returned
     * shape doesn't need to have the same number of points than the original shape.
     *
     * @param  shape The Shape to transform.
     * @return The transformed shape. Some implementations may returns
     *         {@code shape} unmodified if this transform is identity.
     * @throws TransformException if a transform failed.
     */
    @Extension
    Shape createTransformedShape(final Shape shape) throws TransformException;

    /**
     * Gets the derivative of this transform at a point. The derivative is the
     * matrix of the non-translating portion of the approximate affine map at
     * the point.
     *
     * @param  point The coordinate point where to evaluate the derivative. Null value is
     *         accepted only if the derivative is the same everywhere. For example affine
     *         transform accept null value since they produces identical derivative no
     *         matter the coordinate value. But most map projection will requires a non-null
     *         value.
     * @return The derivative at the specified point as a 2&times;2 matrix.  This method
     *         never returns an internal object: changing the matrix will not change the
     *         state of this math transform.
     * @throws NullPointerException if the derivative dependents on coordinate
     *         and {@code point} is {@code null}.
     * @throws TransformException if the derivative can't be evaluated at the
     *         specified point.
     */
    Matrix derivative(final Point2D point) throws TransformException;

    /**
     * Creates the inverse transform of this object.
     *
     * @return The inverse transform.
     * @throws NoninvertibleTransformException if the transform can't be inversed.
     *
     * @since GeoAPI 2.2
     */
    MathTransform2D inverse() throws NoninvertibleTransformException;
}
