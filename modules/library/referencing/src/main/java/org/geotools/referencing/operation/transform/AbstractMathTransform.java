/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2015, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.operation.transform;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.IllegalPathStateException;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.text.MessageFormat;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.Position;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.parameter.InvalidParameterValueException;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform1D;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.Matrix;
import org.geotools.api.referencing.operation.NoninvertibleTransformException;
import org.geotools.api.referencing.operation.Operation;
import org.geotools.api.referencing.operation.OperationMethod;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.GeneralPosition;
import org.geotools.geometry.util.ShapeUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.matrix.Matrix1;
import org.geotools.referencing.operation.matrix.MatrixFactory;
import org.geotools.referencing.operation.matrix.SingularMatrixException;
import org.geotools.referencing.operation.matrix.XMatrix;
import org.geotools.referencing.wkt.Formattable;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.util.Classes;
import org.geotools.util.Utilities;
import si.uom.NonSI;
import si.uom.SI;

/**
 * Provides a default implementation for most methods required by the {@link MathTransform} interface.
 * {@code AbstractMathTransform} provides a convenient base class from which other transform classes can be easily
 * derived. In addition, {@code AbstractMathTransform} implements methods required by the {@link MathTransform2D}
 * interface, but <strong>does not</strong> implements {@code MathTransform2D}. Subclasses must declare
 * {@code implements MathTransform2D} themselves if they know to map two-dimensional coordinate systems.
 *
 * @since 2.0
 * @author Martin Desruisseaux (IRD)
 */
public abstract class AbstractMathTransform extends Formattable implements MathTransform {
    /** Constructs a math transform. */
    protected AbstractMathTransform() {}

    /**
     * Returns a name for this math transform (never {@code null}). This convenience methods returns the name of the
     * {@linkplain #getParameterDescriptors parameter descriptors} if any, or the short class name otherwise.
     *
     * @return A name for this math transform (never {@code null}).
     * @since 2.5
     */
    public String getName() {
        final ParameterDescriptorGroup descriptor = getParameterDescriptors();
        if (descriptor != null) {
            final Identifier identifier = descriptor.getName();
            if (identifier != null) {
                final String code = identifier.getCode();
                if (code != null) {
                    return code;
                }
            }
        }
        return Classes.getShortClassName(this);
    }

    /** Gets the dimension of input points. */
    @Override
    public abstract int getSourceDimensions();

    /** Gets the dimension of output points. */
    @Override
    public abstract int getTargetDimensions();

    /**
     * Returns the parameter descriptors for this math transform, or {@code null} if unknow. This method is similar to
     * {@link OperationMethod#getParameters}, except that {@code MathTransform} returns parameters in standard units
     * (usually {@linkplain SI#METER meters} or {@linkplain NonSI#DEGREE_ANGLE decimal degrees}).
     *
     * @return The parameter descriptors for this math transform, or {@code null}.
     * @see OperationMethod#getParameters
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return null;
    }

    /**
     * Returns the parameter values for this math transform, or {@code null} if unknow. This method is similar to
     * {@link Operation#getParameterValues}, except that {@code MathTransform} returns parameters in standard units
     * (usually {@linkplain SI#METER meters} or {@linkplain NonSI#DEGREE_ANGLE decimal degrees}). Since this method
     * returns a copy of the parameter values, any change to a value will have no effect on this math transform.
     *
     * @return A copy of the parameter values for this math transform, or {@code null}.
     * @see Operation#getParameterValues
     */
    public ParameterValueGroup getParameterValues() {
        return null;
    }

    /**
     * Tests whether this transform does not move any points. The default implementation always returns {@code false}.
     */
    @Override
    public boolean isIdentity() {
        return false;
    }

    /**
     * Constructs an error message for the {@link MismatchedDimensionException}.
     *
     * @param argument The argument name with the wrong number of dimensions.
     * @param dimension The wrong dimension.
     * @param expected The expected dimension.
     */
    private static String constructMessage(final String argument, final int dimension, final int expected) {
        return MessageFormat.format(ErrorKeys.MISMATCHED_DIMENSION_$3, argument, dimension, expected);
    }

    /**
     * Transforms the specified {@code ptSrc} and stores the result in {@code ptDst}. The default implementation invokes
     * {@link #transform(double[],int,double[],int,int)} using a temporary array of doubles.
     *
     * @param ptSrc The specified coordinate point to be transformed.
     * @param ptDst The specified coordinate point that stores the result of transforming {@code ptSrc}, or
     *     {@code null}.
     * @return The coordinate point after transforming {@code ptSrc} and storing the result in {@code ptDst}.
     * @throws MismatchedDimensionException If this transform doesn't map two-dimensional coordinate systems.
     * @throws TransformException If the point can't be transformed.
     * @see MathTransform2D#transform(Point2D,Point2D)
     */
    public Point2D transform(final Point2D ptSrc, final Point2D ptDst) throws TransformException {
        int dim;
        if ((dim = getSourceDimensions()) != 2) {
            throw new MismatchedDimensionException(constructMessage("ptSrc", 2, dim));
        }
        if ((dim = getTargetDimensions()) != 2) {
            throw new MismatchedDimensionException(constructMessage("ptDst", 2, dim));
        }
        final double[] ord = {ptSrc.getX(), ptSrc.getY()};
        this.transform(ord, 0, ord, 0, 1);
        if (ptDst != null) {
            ptDst.setLocation(ord[0], ord[1]);
            return ptDst;
        } else {
            return new Point2D.Double(ord[0], ord[1]);
        }
    }

    /**
     * Transforms the specified {@code ptSrc} and stores the result in {@code ptDst}. The default implementation
     * delegates to {@link #transform(double[],int,double[],int,int)}.
     */
    @Override
    public Position transform(final Position ptSrc, Position ptDst) throws TransformException {
        int dimPoint = ptSrc.getDimension();
        final int dimSource = getSourceDimensions();
        final int dimTarget = getTargetDimensions();
        if (dimPoint != dimSource) {
            throw new MismatchedDimensionException(constructMessage("ptSrc", dimPoint, dimSource));
        }
        if (ptDst != null) {
            dimPoint = ptDst.getDimension();
            if (dimPoint != dimTarget) {
                throw new MismatchedDimensionException(constructMessage("ptDst", dimPoint, dimTarget));
            }
            /*
             * Transforms the coordinates using a temporary 'double[]' buffer,
             * and copy the transformation result in the destination position.
             */
            final double[] array;
            if (dimSource >= dimTarget) {
                array = ptSrc.getCoordinate();
            } else {
                array = new double[dimTarget];
                for (int i = dimSource; --i >= 0; ) {
                    array[i] = ptSrc.getOrdinate(i);
                }
            }
            transform(array, 0, array, 0, 1);
            for (int i = dimTarget; --i >= 0; ) {
                ptDst.setOrdinate(i, array[i]);
            }
        } else {
            /*
             * Destination not set.  We are going to create the destination here.  Since we know
             * that the destination will be the Geotools implementation, write directly into the
             * 'ordinates' array.
             */
            final GeneralPosition destination;
            ptDst = destination = new GeneralPosition(dimTarget);
            final double[] source;
            if (dimSource <= dimTarget) {
                source = destination.ordinates;
                for (int i = dimSource; --i >= 0; ) {
                    source[i] = ptSrc.getOrdinate(i);
                }
            } else {
                source = ptSrc.getCoordinate();
            }
            transform(source, 0, destination.ordinates, 0, 1);
        }
        return ptDst;
    }

    /**
     * Transforms a list of coordinate point ordinal values. The default implementation invokes
     * {@link #transform(double[],int,double[],int,int)} using a temporary array of doubles.
     */
    @Override
    public void transform(
            final float[] srcPts, final int srcOff, final float[] dstPts, final int dstOff, final int numPts)
            throws TransformException {
        final int dimSource = getSourceDimensions();
        final int dimTarget = getTargetDimensions();
        final double[] tmpPts = new double[numPts * Math.max(dimSource, dimTarget)];
        for (int i = numPts * dimSource; --i >= 0; ) {
            tmpPts[i] = srcPts[srcOff + i];
        }
        transform(tmpPts, 0, tmpPts, 0, numPts);
        for (int i = numPts * dimTarget; --i >= 0; ) {
            dstPts[dstOff + i] = (float) tmpPts[i];
        }
    }

    /**
     * Transforms a list of coordinate point ordinal values. The default implementation invokes
     * {@link #transform(double[],int,double[],int,int)} using a temporary array of doubles.
     *
     * @since 2.5
     */
    @Override
    public void transform(
            final double[] srcPts, final int srcOff, final float[] dstPts, final int dstOff, final int numPts)
            throws TransformException {
        final int dimSource = getSourceDimensions();
        final int dimTarget = getTargetDimensions();
        final double[] tmpPts = new double[numPts * Math.max(dimSource, dimTarget)];
        System.arraycopy(srcPts, srcOff, tmpPts, 0, numPts * dimSource);
        transform(tmpPts, 0, tmpPts, 0, numPts);
        for (int i = numPts * dimTarget; --i >= 0; ) {
            dstPts[dstOff + i] = (float) tmpPts[i];
        }
    }

    /**
     * Transforms a list of coordinate point ordinal values. The default implementation delegates to
     * {@link #transform(double[],int,double[],int,int)}.
     *
     * @since 2.5
     */
    @Override
    public void transform(
            final float[] srcPts, final int srcOff, final double[] dstPts, final int dstOff, final int numPts)
            throws TransformException {
        final int dimSource = getSourceDimensions();
        final int dimTarget = getTargetDimensions();
        if (dimSource == dimTarget) {
            final int n = numPts * dimSource;
            for (int i = 0; i < n; i++) {
                dstPts[dstOff + i] = srcPts[srcOff + i];
            }
            transform(dstPts, dstOff, dstPts, dstOff, numPts);
        } else {
            final double[] tmpPts = new double[numPts * dimSource];
            for (int i = tmpPts.length; --i >= 0; ) {
                tmpPts[i] = srcPts[srcOff + i];
            }
            transform(tmpPts, 0, dstPts, 0, numPts);
        }
    }

    /**
     * Transform the specified shape. The default implementation computes quadratic curves using three points for each
     * shape segments.
     *
     * @param shape Shape to transform.
     * @return Transformed shape, or {@code shape} if this transform is the identity transform.
     * @throws IllegalStateException if this transform doesn't map 2D coordinate systems.
     * @throws TransformException if a transform failed.
     * @see MathTransform2D#createTransformedShape(Shape)
     */
    public Shape createTransformedShape(final Shape shape) throws TransformException {
        return isIdentity() ? shape : createTransformedShape(shape, null, null, ShapeUtilities.PARALLEL);
    }

    /**
     * Transforms a geometric shape. This method always copy transformed coordinates in a new object. The new object is
     * usually a {@link GeneralPath}, but may also be a {@link Line2D} or a {@link QuadCurve2D} if such simplification
     * is possible.
     *
     * @param shape The geometric shape to transform.
     * @param preTransform An optional affine transform to apply <em>before</em> the transformation using {@code this},
     *     or {@code null} if none.
     * @param postTransform An optional affine transform to apply <em>after</em> the transformation using {@code this},
     *     or {@code null} if none.
     * @param orientation Base line of quadratic curves. Must be {@link ShapeUtilities#HORIZONTAL} or
     *     {@link ShapeUtilities#PARALLEL}).
     * @return The transformed geometric shape.
     * @throws MismatchedDimensionException if this transform doesn't is not two-dimensional.
     * @throws TransformException If a transformation failed.
     * @todo Use double precision when we will be allowed to target Java 6.
     */
    final Shape createTransformedShape(
            final Shape shape,
            final AffineTransform preTransform,
            final AffineTransform postTransform,
            final int orientation)
            throws TransformException {
        int dim;
        if ((dim = getSourceDimensions()) != 2 || (dim = getTargetDimensions()) != 2) {
            throw new MismatchedDimensionException(constructMessage("shape", 2, dim));
        }
        final PathIterator it = shape.getPathIterator(preTransform);
        final GeneralPath path = new GeneralPath(it.getWindingRule());
        final Point2D.Float ctrl = new Point2D.Float();
        final double[] buffer = new double[6];

        double ax = 0, ay = 0; // Coordinate of the last point before transform.
        double px = 0, py = 0; // Coordinate of the last point after  transform.
        for (; !it.isDone(); it.next()) {
            switch (it.currentSegment(buffer)) {
                default: {
                    throw new IllegalPathStateException();
                }
                case PathIterator.SEG_CLOSE: {
                    /*
                     * Closes the geometric shape and continues the loop. We use the 'continue'
                     * instruction here instead of 'break' because we don't want to execute the
                     * code after the switch (addition of transformed points into the path - there
                     * is no such point in a SEG_CLOSE).
                     */
                    path.closePath();
                    continue;
                }
                case PathIterator.SEG_MOVETO: {
                    /*
                     * Transforms the single point and adds it to the path. We use the 'continue'
                     * instruction here instead of 'break' because we don't want to execute the
                     * code after the switch (addition of a line or a curve - there is no such
                     * curve to add here; we are just moving the cursor).
                     */
                    ax = buffer[0];
                    ay = buffer[1];
                    transform(buffer, 0, buffer, 0, 1);
                    px = buffer[0];
                    py = buffer[1];
                    path.moveTo((float) px, (float) py);
                    continue;
                }
                case PathIterator.SEG_LINETO: {
                    /*
                     * Inserts a new control point at 'buffer[0,1]'. This control point will
                     * be initialised with coordinates in the middle of the straight line:
                     *
                     *  x = 0.5*(x1+x2)
                     *  y = 0.5*(y1+y2)
                     *
                     * This point will be transformed after the 'switch', which is why we use
                     * the 'break' statement here instead of 'continue' as in previous case.
                     */
                    buffer[0] = 0.5 * (ax + (ax = buffer[0]));
                    buffer[1] = 0.5 * (ay + (ay = buffer[1]));
                    buffer[2] = ax;
                    buffer[3] = ay;
                    break;
                }
                case PathIterator.SEG_QUADTO: {
                    /*
                     * Replaces the control point in 'buffer[0,1]' by a new control point lying
                     * on the quadratic curve. Coordinates for a point in the middle of the curve
                     * can be computed with:
                     *
                     *  x = 0.5*(ctrlx + 0.5*(x1+x2))
                     *  y = 0.5*(ctrly + 0.5*(y1+y2))
                     *
                     * There is no need to keep the old control point because it was not lying
                     * on the curve.
                     */
                    buffer[0] = 0.5 * (buffer[0] + 0.5 * (ax + (ax = buffer[2])));
                    buffer[1] = 0.5 * (buffer[1] + 0.5 * (ay + (ay = buffer[3])));
                    break;
                }
                case PathIterator.SEG_CUBICTO: {
                    /*
                     * Replaces the control point in 'buffer[0,1]' by a new control point lying
                     * on the cubic curve. Coordinates for a point in the middle of the curve
                     * can be computed with:
                     *
                     *  x = 0.25*(1.5*(ctrlx1+ctrlx2) + 0.5*(x1+x2));
                     *  y = 0.25*(1.5*(ctrly1+ctrly2) + 0.5*(y1+y2));
                     *
                     * There is no need to keep the old control point because it was not lying
                     * on the curve.
                     *
                     * NOTE: Le point calculé est bien sur la courbe, mais n'est pas
                     *       nécessairement représentatif. Cet algorithme remplace les
                     *       deux points de contrôles par un seul, ce qui se traduit par
                     *       une perte de souplesse qui peut donner de mauvais résultats
                     *       si la courbe cubique était bien tordue. Projeter une courbe
                     *       cubique ne me semble pas être un problème simple, mais ce
                     *       cas devrait être assez rare. Il se produira le plus souvent
                     *       si on essaye de projeter un cercle ou une ellipse, auxquels
                     *       cas l'algorithme actuel donnera quand même des résultats
                     *       tolérables.
                     */
                    buffer[0] = 0.25 * (1.5 * (buffer[0] + buffer[2]) + 0.5 * (ax + (ax = buffer[4])));
                    buffer[1] = 0.25 * (1.5 * (buffer[1] + buffer[3]) + 0.5 * (ay + (ay = buffer[5])));
                    buffer[2] = ax;
                    buffer[3] = ay;
                    break;
                }
            }
            /*
             * Applies the transform on the point in the buffer, and append the transformed points
             * to the general path. Try to add them as a quadratic line, or as a straight line if
             * the computed control point is colinear with the starting and ending points.
             */
            transform(buffer, 0, buffer, 0, 2);
            final Point2D ctrlPoint = ShapeUtilities.parabolicControlPoint(
                    px, py, buffer[0], buffer[1], buffer[2], buffer[3], orientation, ctrl);
            px = buffer[2];
            py = buffer[3];
            if (ctrlPoint != null) {
                assert ctrl == ctrlPoint;
                path.quadTo(ctrl.x, ctrl.y, (float) px, (float) py);
            } else {
                path.lineTo((float) px, (float) py);
            }
        }
        /*
         * La projection de la forme géométrique est terminée. Applique
         * une transformation affine si c'était demandée, puis retourne
         * une version si possible simplifiée de la forme géométrique.
         */
        if (postTransform != null) {
            path.transform(postTransform);
        }
        return ShapeUtilities.toPrimitive(path);
    }

    /**
     * Gets the derivative of this transform at a point. The default implementation always throw an exception.
     * Subclasses that implement the {@link MathTransform2D} interface should override this method. Other subclasses
     * should override {@link #derivative(Position)} instead.
     *
     * @param point The coordinate point where to evaluate the derivative.
     * @return The derivative at the specified point as a 2&times;2 matrix.
     * @throws MismatchedDimensionException if the input dimension is not 2.
     * @throws TransformException if the derivative can't be evaluated at the specified point.
     * @see MathTransform2D#derivative(Point2D)
     */
    public Matrix derivative(final Point2D point) throws TransformException {
        final int dimSource = getSourceDimensions();
        if (dimSource != 2) {
            throw new MismatchedDimensionException(constructMessage("point", 2, dimSource));
        }
        throw new TransformException(ErrorKeys.CANT_COMPUTE_DERIVATIVE);
    }

    /**
     * Gets the derivative of this transform at a point. The default implementation ensure that {@code point} has a
     * valid dimension. Next, it try to delegate the work to an other method:
     *
     * <ul>
     *   <li>If the input dimension is 2, then this method delegates the work to {@link #derivative(Point2D)}.
     *   <li>If this object is an instance of {@link MathTransform1D}, then this method delegates the work to
     *       {@link MathTransform1D#derivative(double) derivative(double)}.
     * </ul>
     *
     * Otherwise, a {@link TransformException} is thrown.
     *
     * @param point The coordinate point where to evaluate the derivative.
     * @return The derivative at the specified point (never {@code null}).
     * @throws NullPointerException if the derivative dependents on coordinate and {@code point} is {@code null}.
     * @throws MismatchedDimensionException if {@code point} doesn't have the expected dimension.
     * @throws TransformException if the derivative can't be evaluated at the specified point.
     */
    @Override
    public Matrix derivative(final Position point) throws TransformException {
        final int dimSource = getSourceDimensions();
        if (point == null) {
            if (dimSource == 2) {
                return derivative((Point2D) null);
            }
        } else {
            final int dimPoint = point.getDimension();
            if (dimPoint != dimSource) {
                throw new MismatchedDimensionException(constructMessage("point", dimPoint, dimSource));
            }
            if (dimSource == 2) {
                if (point instanceof Point2D) {
                    return derivative((Point2D) point);
                }
                return derivative(new Point2D.Double(point.getOrdinate(0), point.getOrdinate(1)));
            }
            if (this instanceof MathTransform1D) {
                return new Matrix1(((MathTransform1D) this).derivative(point.getOrdinate(0)));
            }
        }
        throw new TransformException(ErrorKeys.CANT_COMPUTE_DERIVATIVE);
    }

    /**
     * Creates the inverse transform of this object. The default implementation returns {@code this} if this transform
     * is an identity transform, and throws a {@link NoninvertibleTransformException} otherwise. Subclasses should
     * override this method.
     */
    @Override
    public MathTransform inverse() throws NoninvertibleTransformException {
        if (isIdentity()) {
            return this;
        }
        throw new NoninvertibleTransformException(ErrorKeys.NONINVERTIBLE_TRANSFORM);
    }

    /**
     * Concatenates in an optimized way a {@link MathTransform} {@code other} to this {@code MathTransform}. A new math
     * transform is created to perform the combined transformation. The {@code applyOtherFirst} value determine the
     * transformation order as bellow:
     *
     * <ul>
     *   <li>If {@code applyOtherFirst} is {@code true}, then transforming a point <var>p</var> by the combined
     *       transform is equivalent to first transforming <var>p</var> by {@code other} and then transforming the
     *       result by the original transform {@code this}.
     *   <li>If {@code applyOtherFirst} is {@code false}, then transforming a point <var>p</var> by the combined
     *       transform is equivalent to first transforming <var>p</var> by the original transform {@code this} and then
     *       transforming the result by {@code other}.
     * </ul>
     *
     * If no special optimization is available for the combined transform, then this method returns {@code null}. In the
     * later case, the concatenation will be prepared by {@link DefaultMathTransformFactory} using a generic
     * {@link ConcatenatedTransform}.
     *
     * <p>The default implementation always returns {@code null}. This method is ought to be overridden by subclasses
     * capable of concatenating some combination of transforms in a special way. Examples are
     * {@link ExponentialTransform1D} and {@link LogarithmicTransform1D}.
     *
     * @param other The math transform to apply.
     * @param applyOtherFirst {@code true} if the transformation order is {@code other} followed by {@code this}, or
     *     {@code false} if the transformation order is {@code this} followed by {@code other}.
     * @return The combined math transform, or {@code null} if no optimized combined transform is available.
     */
    MathTransform concatenate(final MathTransform other, final boolean applyOtherFirst) {
        return null;
    }

    /** Returns a hash value for this transform. */
    @Override
    public int hashCode() {
        return getSourceDimensions() + 37 * getTargetDimensions();
    }

    /**
     * Compares the specified object with this math transform for equality. The default implementation checks if
     * {@code object} is an instance of the same class than {@code this} and use the same parameter descriptor.
     * Subclasses should override this method in order to compare internal fields.
     *
     * @param object The object to compare with this transform.
     * @return {@code true} if the given object is a transform of the same class and if, given identical source
     *     position, the {@linkplain #transform(Position, Position) transformed} position would be the equals.
     */
    @Override
    public boolean equals(final Object object) {
        // Do not check 'object==this' here, since this
        // optimization is usually done in subclasses.
        if (object != null && getClass().equals(object.getClass())) {
            final AbstractMathTransform that = (AbstractMathTransform) object;
            return Utilities.equals(this.getParameterDescriptors(), that.getParameterDescriptors());
        }
        return false;
    }

    /**
     * Format the inner part of a <A
     * HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well Known
     * Text</cite> (WKT)</A> element. The default implementation formats all parameter values returned by
     * {@link #getParameterValues}. The parameter group name is used as the math transform name.
     *
     * @param formatter The formatter to use.
     * @return The WKT element name, which is {@code "PARAM_MT"} in the default implementation.
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        final ParameterValueGroup parameters = getParameterValues();
        if (parameters != null) {
            formatter.append(formatter.getName(parameters.getDescriptor()));
            formatter.append(parameters);
        }
        return "PARAM_MT";
    }

    /**
     * Makes sure that an argument is non-null. This is a convenience method for subclass constructors.
     *
     * @param name Argument name.
     * @param object User argument.
     * @throws InvalidParameterValueException if {@code object} is null.
     */
    protected static void ensureNonNull(final String name, final Object object) throws InvalidParameterValueException {
        if (object == null) {
            throw new InvalidParameterValueException(
                    MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, name), name, object);
        }
    }

    /**
     * Checks if source coordinates need to be copied before to apply the transformation. This convenience method is
     * provided for {@code transform(...)} method implementation. This method make the following assumptions:
     *
     * <p>
     *
     * <UL>
     *   <LI>Coordinates will be iterated from lower index to upper index.
     *   <LI>Coordinates are read and writen in shrunk. For example (longitude,latitude,height) values for one
     *       coordinate are read together, and the transformed (x,y,z) values are written together only after.
     * </UL>
     *
     * <p>However, this method does not assumes that source and target dimension are the same (in the special case where
     * source and target dimension are always the same, a simplier and more efficient check is possible). The following
     * example prepares a transformation from 2 dimensional points to three dimensional points:
     *
     * <p>
     *
     * <blockquote>
     *
     * <pre>
     * public void transform(double[] srcPts, int srcOff,
     *                       double[] dstPts, int dstOff, int numPts)
     * {
     *     if (srcPts==dstPts && <strong>needCopy</strong>(srcOff, 2, dstOff, 3, numPts) {
     *         final double[] old = srcPts;
     *         srcPts = new double[numPts*2];
     *         System.arraycopy(old, srcOff, srcPts, 0, srcPts.length);
     *         srcOff = 0;
     *     }
     * }</pre>
     *
     * <blockquote>
     *
     * <strong>This method is for internal usage by the referencing module only. Do not use! It will be replaced by a
     * different mechanism in a future GeoTools version.</strong>
     *
     * @param srcOff The offset in the source coordinate array.
     * @param dimSource The dimension of input points.
     * @param dstOff The offset in the destination coordinate array.
     * @param dimTarget The dimension of output points.
     * @param numPts The number of points to transform.
     * @return {@code true} if the source coordinates should be copied before to apply the transformation in order to
     *     avoid an overlap with the destination array.
     */
    protected static boolean needCopy(
            final int srcOff, final int dimSource, final int dstOff, final int dimTarget, final int numPts) {
        if (numPts <= 1 || srcOff >= dstOff && dimSource >= dimTarget) {
            /*
             * Source coordinates are stored after target coordinates. If implementation
             * read coordinates from lower index to upper index, then the destination will
             * not overwrite the source coordinates, even if there is an overlaps.
             */
            return false;
        }
        return srcOff < dstOff + numPts * dimTarget && dstOff < srcOff + numPts * dimSource;
    }

    /**
     * Ensures that the specified longitude stay within &plusmn;&pi; radians. This method is typically invoked after
     * geographic coordinates are transformed. This method may add or substract some amount of 2&pi; radians to
     * <var>x</var>.
     *
     * @param x The longitude in radians.
     * @return The longitude in the range &plusmn;&pi; radians.
     */
    protected static double rollLongitude(final double x) {
        return x - 2 * Math.PI * Math.floor(x / (2 * Math.PI) + 0.5);
    }

    /**
     * Ensures that the specified longitude stay within 0 and {code}PI{code}.
     *
     * @param x An angle
     * @return The normalized angle
     */
    protected static double normalizeAngle(final double x) {
        return x - 2 * Math.PI * Math.floor(x / (2 * Math.PI) + 0.5);
    }

    /**
     * Wraps the specified matrix in a Geotools implementation of {@link Matrix}. If {@code matrix} is already an
     * instance of {@code XMatrix}, then it is returned unchanged. Otherwise, all elements are copied in a new
     * {@code XMatrix} object.
     */
    static XMatrix toXMatrix(final Matrix matrix) {
        if (matrix instanceof XMatrix) {
            return (XMatrix) matrix;
        }
        return MatrixFactory.create(matrix);
    }

    /**
     * Wraps the specified matrix in a Geotools implementation of {@link Matrix}. If {@code matrix} is already an
     * instance of {@code GeneralMatrix}, then it is returned unchanged. Otherwise, all elements are copied in a new
     * {@code GeneralMatrix} object.
     *
     * <p>Before to use this method, check if a {@link XMatrix} (to be obtained with {@link #toXMatrix}) would be
     * suffisient. Use this method only if a {@code GeneralMatrix} is really necessary.
     */
    static GeneralMatrix toGMatrix(final Matrix matrix) {
        if (matrix instanceof GeneralMatrix) {
            return (GeneralMatrix) matrix;
        } else {
            return new GeneralMatrix(matrix);
        }
    }

    /**
     * Inverts the specified matrix in place. If the matrix can't be inverted (for example because of a
     * {@link SingularMatrixException}), then the exception is wrapped into a {@link NoninvertibleTransformException}.
     */
    static Matrix invert(final Matrix matrix) throws NoninvertibleTransformException {
        try {
            final XMatrix m = toXMatrix(matrix);
            m.invert();
            return m;
        } catch (SingularMatrixException exception) {
            NoninvertibleTransformException e = new NoninvertibleTransformException(ErrorKeys.NONINVERTIBLE_TRANSFORM);
            e.initCause(exception);
            throw e;
        }
    }

    /**
     * Default implementation for inverse math transform. This inner class is the inverse of the enclosing
     * {@link MathTransform}. It is serializable only if the enclosing math transform is also serializable.
     *
     * @since 2.0
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    protected abstract class Inverse extends AbstractMathTransform implements Serializable {
        /**
         * Serial number for interoperability with different versions. This serial number is especilly important for
         * inner classes, since the default {@code serialVersionUID} computation will not produce consistent results
         * across implementations of different Java compiler. This is because different compilers may generate different
         * names for synthetic members used in the implementation of inner classes. See:
         *
         * <p>http://developer.java.sun.com/developer/bugParade/bugs/4211550.html
         */
        private static final long serialVersionUID = 3528274816628012283L;

        /** Constructs an inverse math transform. */
        protected Inverse() {}

        /**
         * Returns a name for this math transform (never {@code null}). The default implementation returns the direct
         * transform name concatenated with localized flavor (when available) of "(Inverse transform)".
         *
         * @return A name for this math transform (never {@code null}).
         * @since 2.5
         */
        @Override
        public String getName() {
            return AbstractMathTransform.this.getName()
                    + " ("
                    + Vocabulary.format(VocabularyKeys.INVERSE_TRANSFORM)
                    + ')';
        }

        /**
         * Gets the dimension of input points. The default implementation returns the dimension of output points of the
         * enclosing math transform.
         */
        @Override
        public int getSourceDimensions() {
            return AbstractMathTransform.this.getTargetDimensions();
        }

        /**
         * Gets the dimension of output points. The default implementation returns the dimension of input points of the
         * enclosing math transform.
         */
        @Override
        public int getTargetDimensions() {
            return AbstractMathTransform.this.getSourceDimensions();
        }

        /**
         * Gets the derivative of this transform at a point. The default implementation compute the inverse of the
         * matrix returned by the enclosing math transform.
         */
        @Override
        public Matrix derivative(final Point2D point) throws TransformException {
            return invert(AbstractMathTransform.this.derivative(this.transform(point, null)));
        }

        /**
         * Gets the derivative of this transform at a point. The default implementation compute the inverse of the
         * matrix returned by the enclosing math transform.
         */
        @Override
        public Matrix derivative(final Position point) throws TransformException {
            return invert(AbstractMathTransform.this.derivative(this.transform(point, null)));
        }

        /**
         * Returns the inverse of this math transform, which is the enclosing math transform. This behavior should not
         * be changed since some implementation assume that the inverse of {@code this} is always
         * {@code AbstractMathTransform.this}.
         */
        @Override
        public MathTransform inverse() {
            return AbstractMathTransform.this;
        }

        /**
         * Tests whether this transform does not move any points. The default implementation delegate this tests to the
         * enclosing math transform.
         */
        @Override
        public boolean isIdentity() {
            return AbstractMathTransform.this.isIdentity();
        }

        /** Returns a hash code value for this math transform. */
        @Override
        public int hashCode() {
            return ~AbstractMathTransform.this.hashCode();
        }

        /**
         * Compares the specified object with this inverse math transform for equality. The default implementation tests
         * if {@code object} in an instance of the same class than {@code this}, and then test their enclosing math
         * transforms.
         */
        @Override
        public boolean equals(final Object object) {
            if (object == this) {
                // Slight optimization
                return true;
            }
            if (object instanceof Inverse) {
                final Inverse that = (Inverse) object;
                return Utilities.equals(this.inverse(), that.inverse());
            } else {
                return false;
            }
        }

        /**
         * Format the inner part of a <A
         * HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
         * Known Text</cite> (WKT)</A> element. If this inverse math transform has any parameter values, then this
         * method format the WKT as in the {@linkplain AbstractMathTransform#formatWKT super-class method}. Otherwise
         * this method format the math transform as an <code>"INVERSE_MT"</code> entity.
         *
         * @param formatter The formatter to use.
         * @return The WKT element name, which is <code>"PARAM_MT"</code> or <code>"INVERSE_MT"
         *     </code> in the default implementation.
         */
        @Override
        protected String formatWKT(final Formatter formatter) {
            final ParameterValueGroup parameters = getParameterValues();
            if (parameters != null) {
                formatter.append(formatter.getName(parameters.getDescriptor()));
                formatter.append(parameters);
                return "PARAM_MT";
            } else {
                formatter.append((Formattable) AbstractMathTransform.this);
                return "INVERSE_MT";
            }
        }
    }
}
