/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry;

import java.awt.geom.Rectangle2D;
import java.text.MessageFormat;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.MismatchedReferenceSystemException;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.cs.AxisDirection;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.api.util.Cloneable;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.CRS;
import org.geotools.util.Utilities;

/**
 * A two-dimensional envelope on top of {@link Rectangle2D}. This implementation is provided for interoperability
 * between Java2D and GeoAPI.
 *
 * <p><strong>Note:</strong> This class inherits {@linkplain #x x} and {@linkplain #y y} fields. But despite their
 * names, they don't need to be oriented toward {@linkplain AxisDirection#EAST East} and {@linkplain AxisDirection#NORTH
 * North} respectively. The (<var>x</var>,<var>y</var>) axis can have any orientation and should be understood as
 * "ordinate 0" and "ordinate 1" values instead. This is not specific to this implementation; in Java2D too, the visual
 * axis orientation depend on the {@linkplain java.awt.Graphics2D#getTransform affine transform in the graphics
 * context}.
 *
 * @since 2.1
 * @version 8.0
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @see GeneralBounds
 * @see org.geotools.geometry.jts.ReferencedEnvelope
 * @see org.geotools.api.metadata.extent.GeographicBoundingBox
 */
public class Envelope2DArchived extends Rectangle2D.Double implements BoundingBox, Bounds, Cloneable {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -3319231220761419350L;

    /** The coordinate reference system, or {@code null}. */
    private CoordinateReferenceSystem crs;

    /**
     * Constructs an initially empty envelope with no CRS.
     * <p>
     * Unlike a normal Envelope2DArchived we set the width and height to -1 so we can tell
     * the difference between:
     * <ul>
     * <li>{@link Envelope2DArchived#isEmpty() width or height is negative inclosing no area
     * <li>{@link #isNull() envelope as constructed, not yet filled in by user
     * </ul>
     * @since 2.5
     */
    public Envelope2DArchived() {
        // set height and width to -1 so that an undefined envelope can be checked by isNull()
        this.width = -1;
        this.height = -1;
    }

    /**
     * Constructs an initially empty envelope with the defined CRS.
     *
     * @param crs The coordinate reference system, or {@code null}.
     */
    public Envelope2DArchived(final CoordinateReferenceSystem crs) {
        this();
        setCoordinateReferenceSystem(crs);
    }

    /**
     * Constructs two-dimensional envelope defined by an other {@link Bounds}.
     *
     * @param envelope The envelope to copy.
     */
    public Envelope2DArchived(final Bounds envelope) {
        super(
                envelope.getMinimum(0), envelope.getMinimum(1),
                envelope.getSpan(0), envelope.getSpan(1));

        // TODO: check below should be first, if only Sun could fix RFE #4093999.
        final int dimension = envelope.getDimension();
        if (dimension != 2) {
            throw new MismatchedDimensionException(MessageFormat.format(ErrorKeys.NOT_TWO_DIMENSIONAL_$1, dimension));
        }
        setCoordinateReferenceSystem(envelope.getCoordinateReferenceSystem());
    }

    /**
     * Constructs two-dimensional envelope defined by an other {@link Rectangle2D}.
     *
     * @param crs The coordinate reference system, or {@code null}.
     * @param rect The rectangle to copy.
     */
    public Envelope2DArchived(final CoordinateReferenceSystem crs, final Rectangle2D rect) {
        super(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        setCoordinateReferenceSystem(crs);
    }

    /**
     * Constructs two-dimensional envelope defined by the specified coordinates. Despite their name, the
     * (<var>x</var>,<var>y</var>) coordinates don't need to be oriented toward ({@linkplain AxisDirection#EAST East},
     * {@linkplain AxisDirection#NORTH North}). Those parameter names simply match the {@linkplain #x x} and
     * {@linkplain #y y} fields. The actual axis orientations are determined by the specified CRS. See the
     * {@linkplain Envelope2DArchived class javadoc} for details.
     *
     * @param crs The coordinate reference system, or {@code null}.
     * @param x The <var>x</var> minimal value.
     * @param y The <var>y</var> minimal value.
     * @param width The envelope width.
     * @param height The envelope height.
     */
    public Envelope2DArchived(
            final CoordinateReferenceSystem crs,
            final double x,
            final double y,
            final double width,
            final double height) {
        super(x, y, width, height);
        setCoordinateReferenceSystem(crs);
    }

    /**
     * Constructs two-dimensional envelope defined by the specified coordinates. Despite their name, the
     * (<var>x</var>,<var>y</var>) coordinates don't need to be oriented toward ({@linkplain AxisDirection#EAST East},
     * {@linkplain AxisDirection#NORTH North}). Those parameter names simply match the {@linkplain #x x} and
     * {@linkplain #y y} fields. The actual axis orientations are determined by the specified CRS. See the
     * {@linkplain Envelope2DArchived class javadoc} for details.
     *
     * <p>The {@code minDP} and {@code maxDP} arguments usually contains the minimal and maximal ordinate values
     * respectively, but this is not mandatory. The ordinates will be rearanged as needed.
     *
     * @param minDP The fist position.
     * @param maxDP The second position.
     * @throws MismatchedReferenceSystemException if the two positions don't use the same CRS.
     * @since 2.4
     */
    public Envelope2DArchived(final Position2D minDP, final Position2D maxDP)
            throws MismatchedReferenceSystemException {
        //  Uncomment next lines if Sun fixes RFE #4093999
        //      ensureNonNull("minDP", minDP);
        //      ensureNonNull("maxDP", maxDP);
        super(
                Math.min(minDP.x, maxDP.x),
                Math.min(minDP.y, maxDP.y),
                Math.abs(maxDP.x - minDP.x),
                Math.abs(maxDP.y - minDP.y));
        setCoordinateReferenceSystem(AbstractBounds.getCoordinateReferenceSystem(minDP, maxDP));
    }

    /**
     * Returns the coordinate reference system in which the coordinates are given.
     *
     * @return The coordinate reference system, or {@code null}.
     */
    @Override
    public final CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }

    /**
     * Set the coordinate reference system in which the coordinate are given.
     *
     * @param crs The new coordinate reference system, or {@code null}.
     */
    public void setCoordinateReferenceSystem(final CoordinateReferenceSystem crs) {
        AbstractPosition.checkCoordinateReferenceSystemDimension(crs, getDimension());
        this.crs = crs;
    }

    /** Returns the number of dimensions. */
    @Override
    public final int getDimension() {
        return 2;
    }

    /**
     * A coordinate position consisting of all the minimal ordinates for each dimension for all points within the
     * {@code Envelope}.
     *
     * @return The lower corner.
     * @todo Change the return type to {@link Position2D} when we will be allowed to compile for J2SE 1.5.
     */
    @Override
    public Position getLowerCorner() {
        return new Position2D(crs, getMinX(), getMinY());
    }

    /**
     * A coordinate position consisting of all the maximal ordinates for each dimension for all points within the
     * {@code Envelope}.
     *
     * @return The upper corner.
     * @todo Change the return type to {@link Position2D} when we will be allowed to compile for J2SE 1.5.
     */
    @Override
    public Position getUpperCorner() {
        return new Position2D(crs, getMaxX(), getMaxY());
    }

    /** Creates an exception for an index out of bounds. */
    private static IndexOutOfBoundsException indexOutOfBounds(final int dimension) {
        return new IndexOutOfBoundsException(MessageFormat.format(ErrorKeys.INDEX_OUT_OF_BOUNDS_$1, dimension));
    }

    /**
     * Returns the minimal ordinate along the specified dimension.
     *
     * @param dimension The dimension to query.
     * @return The minimal ordinate value along the given dimension.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    @Override
    public final double getMinimum(final int dimension) throws IndexOutOfBoundsException {
        switch (dimension) {
            case 0:
                return getMinX();
            case 1:
                return getMinY();
            default:
                throw indexOutOfBounds(dimension);
        }
    }

    /**
     * Returns the maximal ordinate along the specified dimension.
     *
     * @param dimension The dimension to query.
     * @return The maximal ordinate value along the given dimension.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    @Override
    public final double getMaximum(final int dimension) throws IndexOutOfBoundsException {
        switch (dimension) {
            case 0:
                return getMaxX();
            case 1:
                return getMaxY();
            default:
                throw indexOutOfBounds(dimension);
        }
    }

    /**
     * Returns the median ordinate along the specified dimension. The result should be equals (minus rounding error) to
     * <code>({@linkplain #getMaximum getMaximum}(dimension) -
     * {@linkplain #getMinimum getMinimum}(dimension)) / 2</code>.
     *
     * @param dimension The dimension to query.
     * @return The mid ordinate value along the given dimension.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    @Override
    public final double getMedian(final int dimension) throws IndexOutOfBoundsException {
        switch (dimension) {
            case 0:
                return getCenterX();
            case 1:
                return getCenterY();
            default:
                throw indexOutOfBounds(dimension);
        }
    }

    /**
     * Returns the envelope span (typically width or height) along the specified dimension. The result should be equals
     * (minus rounding error) to <code>{@linkplain #getMaximum
     * getMaximum}(dimension) - {@linkplain #getMinimum getMinimum}(dimension)</code>.
     *
     * @param dimension The dimension to query.
     * @return The difference along maximal and minimal ordinates in the given dimension.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    @Override
    public final double getSpan(final int dimension) throws IndexOutOfBoundsException {
        switch (dimension) {
            case 0:
                return getWidth();
            case 1:
                return getHeight();
            default:
                throw indexOutOfBounds(dimension);
        }
    }

    /**
     * Returns a hash value for this envelope. This value need not remain consistent between different implementations
     * of the same class.
     */
    @Override
    public int hashCode() {
        int code = super.hashCode() ^ (int) serialVersionUID;
        if (crs != null) {
            code += crs.hashCode();
        }
        return code;
    }

    /**
     * Compares the specified object with this envelope for equality.
     *
     * @param object The object to compare with this envelope.
     * @return {@code true} if the given object is equals to this envelope.
     */
    @Override
    public boolean equals(final Object object) {
        if (super.equals(object)) {
            final CoordinateReferenceSystem otherCRS =
                    object instanceof Envelope2DArchived ? ((Envelope2DArchived) object).crs : null;
            return Utilities.equals(crs, otherCRS);
        }
        return false;
    }

    /**
     * Returns {@code true} if {@code this} envelope bounds is equals to {@code that} envelope bounds in two specified
     * dimensions. The coordinate reference system is not compared, since it doesn't need to have the same number of
     * dimensions.
     *
     * @param that The envelope to compare to.
     * @param xDim The dimension of {@code that} envelope to compare to the <var>x</var> dimension of {@code this}
     *     envelope.
     * @param yDim The dimension of {@code that} envelope to compare to the <var>y</var> dimension of {@code this}
     *     envelope.
     * @param eps A small tolerance number for floating point number comparaisons. This value will be scaled according
     *     this envelope {@linkplain #width width} and {@linkplain #height height}.
     * @return {@code true} if the envelope bounds are the same (up to the specified tolerance level) in the specified
     *     dimensions, or {@code false} otherwise.
     */
    public boolean boundsEquals(final Bounds that, final int xDim, final int yDim, double eps) {
        eps *= 0.5 * (width + height);
        for (int i = 0; i < 4; i++) {
            final int dim2D = i & 1;
            final int dimND = dim2D == 0 ? xDim : yDim;
            final double value2D, valueND;
            if ((i & 2) == 0) {
                value2D = this.getMinimum(dim2D);
                valueND = that.getMinimum(dimND);
            } else {
                value2D = this.getMaximum(dim2D);
                valueND = that.getMaximum(dimND);
            }
            // Use '!' for catching NaN values.
            if (!(Math.abs(value2D - valueND) <= eps)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a string representation of this envelope. The default implementation is okay for occasional formatting
     * (for example for debugging purpose). But if there is a lot of envelopes to format, users will get more control by
     * using their own instance of {@link org.geotools.referencing.CoordinateFormat}.
     *
     * @since 2.4
     */
    @Override
    public String toString() {
        return AbstractBounds.toString(this);
    }

    // BoundingBox
    @Override
    public void setBounds(BoundingBox bounds) {
        this.crs = bounds.getCoordinateReferenceSystem();
        this.x = bounds.getMinX();
        this.y = bounds.getMinY();
        this.width = bounds.getWidth();
        this.height = bounds.getHeight();
    }

    @Override
    public void include(BoundingBox bounds) {
        if (crs == null) {
            this.crs = bounds.getCoordinateReferenceSystem();
        } else {
            ensureCompatibleReferenceSystem(bounds);
        }
        if (bounds.isEmpty()) {
            return;
        }
        if (isNull()) {
            setBounds(bounds);
        } else {
            if (bounds.getMinX() < getMinX()) {
                this.width = width + (getMinX() - bounds.getMinX());
                this.x = bounds.getMinX();
            }
            if (bounds.getMaxX() > getMaxX()) {
                this.width = width + (bounds.getMaxX() - getMaxX());
            }
            if (bounds.getMinY() < getMinY()) {
                this.height = height + (getMinY() - bounds.getMinY());
                this.y = bounds.getMinY();
            }
            if (bounds.getMaxY() > getMaxY()) {
                this.height = height + (bounds.getMaxY() - getMaxY());
            }
        }
    }

    @Override
    public void include(double x, double y) {
        if (isNull()) {
            this.x = x;
            this.y = y;
            this.width = 0;
            this.height = 0;
        } else {
            if (x < getMinX()) {
                this.width = width + (getMinX() - x);
                this.x = x;
            }
            if (x > getMaxX()) {
                this.width = width + (x - getMaxX());
            }
            if (y < getMinY()) {
                this.height = height + (getMinY() - y);
                this.y = y;
            }
            if (y > getMaxY()) {
                this.height = height + (y - getMaxY());
            }
        }
    }

    /**
     * Returns {@code true} if the interior of this bounds intersects the interior of the provided bounds.
     *
     * <p>Note this method conflicts with {@link Rectangle2D#intersects(Rectangle2D)} so you may need to call it via
     * envelope2d.intersects( (Envelope2DArchived) bounds ) in order to correctly check that the coordinate reference
     * systems match.
     *
     * @param bounds The bounds to test for intersection.
     * @return {@code true} if the two bounds intersect.
     */
    @Override
    public boolean intersects(BoundingBox bounds) {
        ensureCompatibleReferenceSystem(bounds);
        if (isNull() || bounds.isEmpty()) {
            return false;
        }

        return !(bounds.getMinX() > this.getMaxX()
                || bounds.getMaxX() < this.getMinX()
                || bounds.getMinY() > this.getMaxY()
                || bounds.getMaxY() < this.getMinY());
    }

    @Override
    public boolean contains(BoundingBox bounds) {
        ensureCompatibleReferenceSystem(bounds);
        if (isEmpty() || bounds.isEmpty()) {
            return false;
        }
        return bounds.getMinX() >= this.getMinX()
                && bounds.getMaxX() <= this.getMaxX()
                && bounds.getMinY() >= this.getMinY()
                && bounds.getMaxY() <= this.getMaxY();
    }

    @Override
    public boolean contains(Position location) {
        ensureCompatibleReferenceSystem(location);
        if (isEmpty()) {
            return false;
        }
        return location.getOrdinate(0) >= getMinX()
                && location.getOrdinate(0) <= getMaxX()
                && location.getOrdinate(1) >= getMinY()
                && location.getOrdinate(1) <= getMaxY();
    }

    @Override
    public BoundingBox toBounds(CoordinateReferenceSystem targetCRS) throws TransformException {
        Bounds transformed = new GeneralBounds((BoundingBox) this);
        transformed = CRS.transform(transformed, targetCRS);
        return new Envelope2DArchived(transformed);
    }

    // utility methods
    /**
     * Make sure that the specified bounding box uses the same CRS than this one.
     *
     * @param bbox The other bounding box to test for compatibility.
     * @throws MismatchedReferenceSystemException if the CRS are incompatibles.
     */
    private void ensureCompatibleReferenceSystem(final BoundingBox bbox) throws MismatchedReferenceSystemException {
        if (crs != null) {
            final CoordinateReferenceSystem other = bbox.getCoordinateReferenceSystem();
            if (other != null) {
                if (!CRS.equalsIgnoreMetadata(crs, other)) {
                    throw new MismatchedReferenceSystemException(ErrorKeys.MISMATCHED_COORDINATE_REFERENCE_SYSTEM);
                }
            }
        }
    }

    private void ensureCompatibleReferenceSystem(Position location) {
        if (crs != null) {
            final CoordinateReferenceSystem other = location.getCoordinateReferenceSystem();
            if (other != null) {
                if (!CRS.equalsIgnoreMetadata(crs, other)) {
                    throw new MismatchedReferenceSystemException(ErrorKeys.MISMATCHED_COORDINATE_REFERENCE_SYSTEM);
                }
            }
        }
    }

    /**
     * Used as a separate test from {@link #isEmpty()} so that we can detect when an envelope has never been used.
     *
     * @return true if envelope has just been constructed
     */
    private boolean isNull() {
        return getMinX() == 0 && getMinY() == 0 && getWidth() < 0 && getHeight() < 0;
    }
}
