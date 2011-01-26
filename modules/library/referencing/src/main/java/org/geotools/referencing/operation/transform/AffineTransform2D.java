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
import java.awt.geom.AffineTransform;
import java.util.prefs.Preferences;

import org.opengis.util.Cloneable;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.matrix.Matrix2;
import org.geotools.referencing.operation.matrix.Matrix3;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.referencing.wkt.Symbols;
import org.geotools.resources.Formattable;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.util.Utilities;


/**
 * Transforms two-dimensional coordinate points using an affine transform. This class both
 * extends {@link AffineTransform} and implements {@link MathTransform2D}, so it can be
 * used as a bridge between Java2D and the referencing module.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class AffineTransform2D extends XAffineTransform
        implements MathTransform2D, LinearTransform, Formattable, Cloneable
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -5299837898367149069L;

    /**
     * The inverse transform. This field will be computed only when needed.
     */
    private transient AffineTransform2D inverse;

    /**
     * Constructs a new affine transform with the same coefficient than the specified transform.
     */
    public AffineTransform2D(final AffineTransform transform) {
        super(transform);
    }

    /**
     * Constructs a new {@code AffineTransform2D} from 6 values representing the 6 specifiable
     * entries of the 3&times;3 transformation matrix. Those values are given unchanged to the
     * {@link AffineTransform#AffineTransform(double,double,double,double,double,double) super
     * class constructor}.
     *
     * @since 2.5
     */
    public AffineTransform2D(double m00, double m10, double m01, double m11, double m02, double m12) {
        super(m00, m10, m01, m11, m02, m12);
    }

    /**
     * Throws an {@link UnsupportedOperationException} when a mutable method
     * is invoked, since {@code AffineTransform2D} must be immutable.
     */
    @Override
    protected final void checkPermission() throws UnsupportedOperationException {
        super.checkPermission();
    }

    /**
     * Returns the matrix elements as a group of parameters values. The number of parameters
     * depends on the matrix size. Only matrix elements different from their default value
     * will be included in this group.
     *
     * @return A copy of the parameter values for this math transform.
     */
    public ParameterValueGroup getParameterValues() {
        return ProjectiveTransform.getParameterValues(getMatrix());
    }

    /**
     * Gets the dimension of input points, which is fixed to 2.
     */
    public final int getSourceDimensions() {
        return 2;
    }

    /**
     * Gets the dimension of output points, which is fixed to 2.
     */
    public final int getTargetDimensions() {
        return 2;
    }

    /**
     * Transforms the specified {@code ptSrc} and stores the result in {@code ptDst}.
     */
    public DirectPosition transform(final DirectPosition ptSrc, DirectPosition ptDst) {
        if (ptDst == null) {
            ptDst = new GeneralDirectPosition(2);
        } else {
            final int dimension = ptDst.getDimension();
            if (dimension != 2) {
                throw new MismatchedDimensionException(Errors.format(
                          ErrorKeys.MISMATCHED_DIMENSION_$3, "ptDst", dimension, 2));
            }
        }
        final double[] array = ptSrc.getCoordinate();
        transform(array, 0, array, 0, 1);
        ptDst.setOrdinate(0, array[0]);
        ptDst.setOrdinate(1, array[1]);
        return ptDst;
    }

    /**
     * Transforms the specified shape.
     *
     * @param  shape Shape to transform.
     * @return Transformed shape, or {@code shape} if this transform is the identity transform.
     */
    @Override
    public Shape createTransformedShape(final Shape shape) {
        return transform(this, shape, false);
    }

    /**
     * Returns this transform as an affine transform matrix.
     */
    public Matrix getMatrix() {
        return new Matrix3(this);
    }

    /**
     * Gets the derivative of this transform at a point. For an affine transform,
     * the derivative is the same everywhere.
     */
    public Matrix derivative(final Point2D point) {
        return new Matrix2(getScaleX(), getShearX(),
                           getShearY(), getScaleY());
    }

    /**
     * Gets the derivative of this transform at a point. For an affine transform,
     * the derivative is the same everywhere.
     */
    public Matrix derivative(final DirectPosition point) {
        return derivative((Point2D) null);
    }

    /**
     * Creates the inverse transform of this object.
     *
     * @throws NoninvertibleTransformException if this transform can't be inverted.
     */
    public MathTransform2D inverse() throws NoninvertibleTransformException {
        if (inverse == null) {
            if (isIdentity()) {
                inverse = this;
            } else try {
                synchronized (this) {
                    inverse = new AffineTransform2D(createInverse());
                    inverse.inverse = this;
                }
            } catch (java.awt.geom.NoninvertibleTransformException exception) {
                throw new NoninvertibleTransformException(exception.getLocalizedMessage(), exception);
            }
        }
        return inverse;
    }

    /**
     * Returns a new affine transform which is a modifiable copy of this transform. We override
     * this method because it is {@linkplain AffineTransform#clone defined in the super-class}.
     * However this implementation do not returns a new {@code AffineTransform2D} instance because
     * the later is unmodifiable, which make exact cloning useless.
     */
    @Override
    public AffineTransform clone() {
        return new AffineTransform(this);
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name.
     */
    public String formatWKT(final Formatter formatter) {
        final ParameterValueGroup parameters = getParameterValues();
        formatter.append(formatter.getName(parameters.getDescriptor()));
        formatter.append(parameters);
        return "PARAM_MT";
    }

    /**
     * Returns the WKT for this transform.
     */
    public String toWKT() {
        int indentation = 2;
        try {
            indentation = Preferences.userNodeForPackage(org.geotools.referencing.wkt.Formattable.class)
                    .getInt("Indentation", indentation);
        } catch (SecurityException ignore) {
            // Ignore. Will fallback on the default indentation.
        }
        final Formatter formatter = new Formatter(Symbols.DEFAULT, indentation);
        formatter.append(this);
        return formatter.toString();
    }

    /**
     * Returns the WKT representation of this transform.
     */
    @Override
    public String toString() {
        return toWKT();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AffineTransform)) {
            return false;
        }

        AffineTransform a = (AffineTransform) obj;
        
        return Utilities.equals(getScaleX(), a.getScaleX()) &&
            Utilities.equals(getScaleY(), a.getScaleY()) &&
            Utilities.equals(getShearX(), a.getShearX()) &&
            Utilities.equals(getShearY(), a.getShearY()) &&
            Utilities.equals(getTranslateX(), a.getTranslateX()) &&
            Utilities.equals(getTranslateY(), a.getTranslateY());
    }
}
