/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.builder;

import java.io.Serializable;

import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.DirectPosition;

import org.geotools.io.TableWriter;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;


/**
 * An association between a {@linkplain #getSource source} and {@linkplain #getTarget target}
 * direct positions. Accuracy information and comments can optionnaly be attached.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Jan Jezek
 * @author Martin Desruisseaux
 */
public class MappedPosition implements Serializable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 3262172371858749543L;

    /**
     * The source position.
     */
    private final DirectPosition source;

    /**
     * The target position.
     */
    private final DirectPosition target;

    /**
     * An estimation of mapping accuracy in units of target CRS axis,
     * or {@link Double#NaN} if unknow.
     */
    private double accuracy = Double.NaN;

    /**
     * Optionnal comments attached to this mapping, or {@code null} if none.
     */
    private String comments;

    /**
     * Creates a mapped position with {@linkplain #getSource source} and
     * {@linkplain #getTarget target} position of the specified dimension.
     * The initial coordinate values are 0.
     */
    public MappedPosition(final int dimension) {
        if (dimension == 2) {
            source = new DirectPosition2D();
            target = new DirectPosition2D();
        } else {
            source = new GeneralDirectPosition(dimension);
            target = new GeneralDirectPosition(dimension);
        }
    }

    /**
     * Creates a mapped position from the specified direct positions.
     *
     * @param source The original direct position.
     * @param target The associated direct position.
     */
    public MappedPosition(final DirectPosition source, final DirectPosition target) {
        ensureNonNull("source", source);
        ensureNonNull("target", target);
        this.source = source;
        this.target = target;
    }

    /**
     * Makes sure an argument is non-null.
     *
     * @param  name   Argument name.
     * @param  object User argument.
     * @throws InvalidParameterValueException if {@code object} is null.
     */
    private static void ensureNonNull(final String name, final Object object)
            throws IllegalArgumentException
    {
        if (object == null) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, name));
        }
    }

    /**
     * Returns the source direct position. For performance reasons, the current
     * implementation returns a reference to the internal object. However users
     * should avoid to modify directly the returned position and use
     * {@link #setSource} instead.
     */
    public DirectPosition getSource() {
        return source;
    }

    /**
     * Set the source direct position to the specified value.
     */
    public void setSource(final DirectPosition point) {
        if (source instanceof DirectPosition2D) {
            ((DirectPosition2D) source).setLocation(point);
        } else {
            ((GeneralDirectPosition) source).setLocation(point);
        }
    }

    /**
     * Returns the target direct position. For performance reasons, the current
     * implementation returns a reference to the internal object. However users
     * should avoid to modify directly the returned position and use
     * {@link #setTarget} instead.
     */
    public DirectPosition getTarget() {
        return target;
    }

    /**
     * Set the target direct position to the specified value.
     */
    public void setTarget(final DirectPosition point) {
        if (source instanceof DirectPosition2D) {
            ((DirectPosition2D) target).setLocation(point);
        } else {
            ((GeneralDirectPosition) target).setLocation(point);
        }
    }

    /**
     * Returns the comments attached to this mapping, or {@code null} if none.
     */
    public String getComments() {
        return comments;
    }

    /**
     * Set the comments attached to this mapping. May be {@code null} if none.
     */
    public void setComments(final String comments) {
        this.comments = comments;
    }

    /**
     * Returns an estimation of mapping accuracy in units of target CRS axis,
     * or {@link Double#NaN} if unknow.
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * Set the accuracy.
     */
    public void setAccuracy(final double accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * Computes the distance between the {@linkplain #getSource source point} transformed
     * by the supplied math transform, and the {@linkplain #getTarget target point}.
     *
     * @param  transform The transform to use for computing the error.
     * @param  buffer An optionnaly pre-computed direct position to use as a buffer,
     *         or {@code null} if none. The content of this buffer will be overwritten.
     * @return The distance in units of the target CRS axis.
     */
    final double getError(final MathTransform transform, final DirectPosition buffer)
            throws TransformException
    {
        return distance(transform.transform(source, buffer), target);
    }

    /**
     * Returns the distance between the specified points.
     */
    private static double distance(final DirectPosition source, final DirectPosition target) {
        final int otherDim  = source.getDimension();
        final int dimension = target.getDimension();
        if (otherDim != dimension) {
            throw new MismatchedDimensionException(Errors.format(ErrorKeys.MISMATCHED_DIMENSION_$2,
                        otherDim, dimension));
        }
        double sum = 0;
        for (int i=0; i<dimension; i++) {
            final double delta = source.getOrdinate(i) - target.getOrdinate(i);
            sum += delta*delta;
        }
        return Math.sqrt(sum / dimension);
    }

    /**
     * Returns a hash code value for this mapped position.
     */
    public int hashCode() {
        return source.hashCode() + 37*target.hashCode();
    }

    /**
     * Compares this mapped position with the specified object for equality.
     */
    public boolean equals(final Object object) {
        if (object!=null && object.getClass().equals(getClass())) {
            final MappedPosition that = (MappedPosition) object;
            return Utilities.equals(this.source,   that.source)   &&
                   Utilities.equals(this.target,   that.target)   &&
                   Utilities.equals(this.comments, that.comments) &&
                   Utilities.equals(this.accuracy, that.accuracy);
        }
        return false;
    }

    /**
     * Returns a string representation of this mapped position.
     *
     * @todo Consider using a {@link java.text.NumberFormat} instance.
     */
    public String toString() {
        final TableWriter table = new TableWriter(null, " ");
        table.write(Vocabulary.format(VocabularyKeys.SOURCE_POINT));
        table.write(':');
        int dimension = source.getDimension();
        for (int i=0; i<dimension; i++) {
            table.nextColumn();
            table.write(String.valueOf(source.getOrdinate(i)));
        }
        table.nextLine();
        table.write(Vocabulary.format(VocabularyKeys.TARGET_POINT));
        table.write(':');
        dimension = target.getDimension();
        for (int i=0; i<dimension; i++) {
            table.nextColumn();
            table.write(String.valueOf(target.getOrdinate(i)));
        }
        return table.toString();
    }
}
