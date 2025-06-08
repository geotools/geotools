/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.MessageFormat;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.MismatchedReferenceSystemException;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.cs.CoordinateSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.AbstractPosition;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.Position2D;
import org.geotools.geometry.util.XRectangle2D;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.Classes;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;

/**
 * A JTS envelope associated with a {@linkplain CoordinateReferenceSystem coordinate reference system}. In addition,
 * this JTS envelope also implements the GeoAPI {@linkplain org.geotools.api.geometry.coordinate.Envelope envelope}
 * interface for interoperability with GeoAPI.
 *
 * @since 2.2
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 * @author Simone Giannecchini
 * @see org.geotools.geometry.Envelope2D
 * @see GeneralBounds
 * @see org.geotools.api.metadata.extent.GeographicBoundingBox
 */
public class ReferencedEnvelope extends Envelope implements Bounds, BoundingBox {

    /** A ReferencedEnvelope containing "everything" */
    public static ReferencedEnvelope EVERYTHING =
            new ReferencedEnvelope(
                    Double.NEGATIVE_INFINITY,
                    Double.POSITIVE_INFINITY,
                    Double.NEGATIVE_INFINITY,
                    Double.POSITIVE_INFINITY,
                    null) {
                private static final long serialVersionUID = -3188702602373537164L;

                @Override
                public boolean contains(BoundingBox bbox) {
                    return true;
                }

                @Override
                public boolean contains(Coordinate p) {
                    return true;
                }

                @Override
                public boolean contains(Position pos) {
                    return true;
                }

                @Override
                public boolean contains(double x, double y) {
                    return true;
                }

                @Override
                public boolean contains(Envelope other) {
                    return true;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public boolean isNull() {
                    return true;
                }

                @Override
                public double getArea() {
                    // return super.getArea();
                    return Double.POSITIVE_INFINITY;
                }

                @Override
                public void setBounds(BoundingBox arg0) {
                    throw new IllegalStateException("Cannot modify ReferencedEnvelope.EVERYTHING");
                }

                @Override
                public Coordinate centre() {
                    return new Coordinate();
                }

                @Override
                public void setToNull() {
                    // um ignore this as we are already "null"
                }

                @Override
                public boolean equals(Object obj) {
                    if (obj == EVERYTHING) {
                        return true;
                    }
                    if (obj instanceof ReferencedEnvelope) {
                        ReferencedEnvelope other = (ReferencedEnvelope) obj;
                        if (other.crs != EVERYTHING.crs) return false;
                        if (other.getMinX() != EVERYTHING.getMinX()) return false;
                        if (other.getMinY() != EVERYTHING.getMinY()) return false;
                        if (other.getMaxX() != EVERYTHING.getMaxX()) return false;
                        if (other.getMaxY() != EVERYTHING.getMaxY()) return false;

                        return true;
                    }
                    return super.equals(obj);
                }

                @Override
                public String toString() {
                    return "ReferencedEnvelope.EVERYTHING";
                }
            };

    /** Serial number for compatibility with different versions. */
    private static final long serialVersionUID = -3188702602373537163L;

    /** The coordinate reference system, or {@code null}. */
    protected CoordinateReferenceSystem crs;

    /** Creates a null envelope with a null coordinate reference system. */
    public ReferencedEnvelope() {
        this((CoordinateReferenceSystem) null);
    }

    /**
     * Creates a null envelope with the specified coordinate reference system.
     *
     * @param crs The coordinate reference system.
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     */
    public ReferencedEnvelope(CoordinateReferenceSystem crs) throws MismatchedDimensionException {
        this.crs = crs;
        checkCoordinateReferenceSystemDimension();
    }

    /**
     * Creates an envelope for a region defined by maximum and minimum values.
     *
     * @param x1 The first x-value.
     * @param x2 The second x-value.
     * @param y1 The first y-value.
     * @param y2 The second y-value.
     * @param crs The coordinate reference system.
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     */
    public ReferencedEnvelope(
            final double x1, final double x2, final double y1, final double y2, final CoordinateReferenceSystem crs)
            throws MismatchedDimensionException {
        super(x1, x2, y1, y2);
        this.crs = crs;
        checkCoordinateReferenceSystemDimension();
    }

    /**
     * Creates an envelope for a Java2D rectangle.
     *
     * <p>NOTE: if the rectangle is empty, the resulting ReferencedEnvelope will not be. In case this is needed use
     * {@link #create(Rectangle2D, CoordinateReferenceSystem) ReferencedEnvelope.create(rectangle, crs)}
     *
     * @param rectangle The rectangle.
     * @param crs The coordinate reference system.
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     * @since 2.4
     */
    public ReferencedEnvelope(final Rectangle2D rectangle, final CoordinateReferenceSystem crs)
            throws MismatchedDimensionException {
        this(rectangle.getMinX(), rectangle.getMaxX(), rectangle.getMinY(), rectangle.getMaxY(), crs);
    }

    /**
     * Creates a new envelope from an existing envelope.
     *
     * @param envelope The envelope to initialize from
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     * @since 2.3
     */
    public ReferencedEnvelope(final ReferencedEnvelope envelope) throws MismatchedDimensionException {
        super(envelope);
        crs = envelope.getCoordinateReferenceSystem();
        checkCoordinateReferenceSystemDimension();
    }

    /**
     * Creates a new envelope from an existing bounding box.
     *
     * <p>NOTE: if the bounding box is empty, the resulting ReferencedEnvelope will not be. In case this is needed use
     * {@link #create(Bounds, CoordinateReferenceSystem) ReferencedEnvelope.create(bbox,
     * bbox.getCoordinateReferenceSystem())}
     *
     * @param bbox The bounding box to initialize from.
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     * @since 2.4
     */
    public ReferencedEnvelope(final BoundingBox bbox) throws MismatchedDimensionException {
        this(bbox.getMinX(), bbox.getMaxX(), bbox.getMinY(), bbox.getMaxY(), bbox.getCoordinateReferenceSystem());
    }

    /**
     * Creates a new bounds from an existing OGC bounds.
     *
     * <p>NOTE: if the bounds is empty, the resulting ReferencedEnvelope will not be. In case this is needed use
     * {@link #create(Bounds, CoordinateReferenceSystem) ReferencedEnvelope.create(bounds,
     * bounds.getCoordinateReferenceSystem())}
     *
     * @param bounds The bounds to initialize from.
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     * @since 2.4
     */
    public ReferencedEnvelope(final Bounds bounds) throws MismatchedDimensionException {
        super(bounds.getMinimum(0), bounds.getMaximum(0), bounds.getMinimum(1), bounds.getMaximum(1));
        this.crs = bounds.getCoordinateReferenceSystem();
        checkCoordinateReferenceSystemDimension();
    }

    /**
     * Creates a new envelope from an existing JTS envelope.
     *
     * @param envelope The envelope to initialize from.
     * @param crs The coordinate reference system.
     * @throws MismatchedDimensionExceptionif the CRS dimension is not valid.
     */
    public ReferencedEnvelope(final Envelope envelope, final CoordinateReferenceSystem crs)
            throws MismatchedDimensionException {
        super(envelope);
        this.crs = crs;
        checkCoordinateReferenceSystemDimension();
    }

    /** Sets this envelope to the specified bounding box. */
    public void init(BoundingBox bounds) {
        super.init(bounds.getMinimum(0), bounds.getMaximum(0), bounds.getMinimum(1), bounds.getMaximum(1));
        this.crs = bounds.getCoordinateReferenceSystem();
    }

    /** Returns the specified bounding box as a JTS envelope. */
    private static Envelope toJTSEnvelope(final BoundingBox bbox) {
        if (bbox == null) {
            throw new NullPointerException("Provided bbox envelope was null");
        }
        if (bbox instanceof Envelope) {
            return (Envelope) bbox;
        }
        // safe creation if empty bounds
        return ReferencedEnvelope.create(bbox, bbox.getCoordinateReferenceSystem());
    }

    /**
     * Convenience method for checking coordinate reference system validity.
     *
     * @throws IllegalArgumentException if the CRS dimension is not valid.
     */
    protected void checkCoordinateReferenceSystemDimension() throws MismatchedDimensionException {
        if (crs != null) {
            final int expected = getDimension();
            final int dimension = crs.getCoordinateSystem().getDimension();
            if (dimension > expected) {
                // check dimensions and choose ReferencedEnvelope or ReferencedEnvelope3D
                // or the factory method ReferencedEnvelope.reference( CoordinateReferenceSystem )
                final Object arg0 = crs.getName().getCode();
                throw new MismatchedDimensionException(MessageFormat.format(
                        ErrorKeys.MISMATCHED_DIMENSION_$3,
                        arg0,
                        Integer.valueOf(dimension),
                        Integer.valueOf(expected)));
            }
        }
    }

    /**
     * Make sure that the specified bounding box uses the same CRS than this one.
     *
     * @param bbox The other bounding box to test for compatibility.
     * @throws MismatchedReferenceSystemException if the CRS are incompatible.
     */
    protected void ensureCompatibleReferenceSystem(final BoundingBox bbox) throws MismatchedReferenceSystemException {
        if (crs != null) {
            final CoordinateReferenceSystem other = bbox.getCoordinateReferenceSystem();
            if (other != null) {
                if (!CRS.isEquivalent(crs, other)) {
                    throw new MismatchedReferenceSystemException(ErrorKeys.MISMATCHED_COORDINATE_REFERENCE_SYSTEM);
                }
            }
        }
    }

    /**
     * Make sure that the specified location uses the same CRS as this one.
     *
     * @throws MismatchedReferenceSystemException if the CRS are incompatible.
     */
    protected void ensureCompatibleReferenceSystem(Position location) {
        if (crs != null) {
            final CoordinateReferenceSystem other = location.getCoordinateReferenceSystem();
            if (other != null) {
                if (!CRS.isEquivalent(crs, other)) {
                    throw new MismatchedReferenceSystemException(ErrorKeys.MISMATCHED_COORDINATE_REFERENCE_SYSTEM);
                }
            }
        }
    }

    /** Returns the coordinate reference system associated with this envelope. */
    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
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
    public int getDimension() {
        return 2;
    }

    /** Returns the minimal ordinate along the specified dimension. */
    @Override
    public double getMinimum(final int dimension) {
        switch (dimension) {
            case 0:
                return getMinX();

            case 1:
                return getMinY();

            default:
                throw new IndexOutOfBoundsException(String.valueOf(dimension));
        }
    }

    /** Returns the maximal ordinate along the specified dimension. */
    @Override
    public double getMaximum(final int dimension) {
        switch (dimension) {
            case 0:
                return getMaxX();

            case 1:
                return getMaxY();

            default:
                throw new IndexOutOfBoundsException(String.valueOf(dimension));
        }
    }

    /** Returns the center ordinate along the specified dimension. */
    @Override
    public double getMedian(final int dimension) {
        switch (dimension) {
            case 0:
                return 0.5 * (getMinX() + getMaxX());

            case 1:
                return 0.5 * (getMinY() + getMaxY());

            default:
                throw new IndexOutOfBoundsException(String.valueOf(dimension));
        }
    }

    /**
     * Returns the envelope length along the specified dimension. This length is equals to the maximum ordinate minus
     * the minimal ordinate.
     */
    @Override
    public double getSpan(final int dimension) {
        switch (dimension) {
            case 0:
                return getWidth();

            case 1:
                return getHeight();

            default:
                throw new IndexOutOfBoundsException(String.valueOf(dimension));
        }
    }

    /**
     * A coordinate position consisting of all the minimal ordinates for each dimension for all points within the
     * {@code Envelope}.
     */
    @Override
    public Position getLowerCorner() {
        return new Position2D(crs, getMinX(), getMinY());
    }

    /**
     * A coordinate position consisting of all the maximal ordinates for each dimension for all points within the
     * {@code Envelope}.
     */
    @Override
    public Position getUpperCorner() {
        return new Position2D(crs, getMaxX(), getMaxY());
    }

    /**
     * Returns {@code true} if lengths along all dimension are zero.
     *
     * @since 2.4
     */
    @Override
    public boolean isEmpty() {
        return super.isNull();
    }

    /**
     * Returns {@code true} if the provided location is contained by this bounding box.
     *
     * @since 2.4
     */
    @Override
    public boolean contains(Position pos) {
        ensureCompatibleReferenceSystem(pos);
        return super.contains(pos.getOrdinate(0), pos.getOrdinate(1));
    }

    /**
     * Check if provided bounds are contained by this ReferencedEnvelope.
     *
     * @param bounds
     * @return {@code true} if bounds are contained by this ReferencedEnvelope
     */
    public boolean contains(final ReferencedEnvelope bounds) {
        ensureCompatibleReferenceSystem(bounds);
        return super.contains(toJTSEnvelope(bounds));
    }

    /**
     * Check if provided bbox is contained by this ReferencedEnvelope.
     *
     * @param bbox
     * @return {@code true} if bbox is contained by this ReferencedEnvelope
     * @since 2.4
     */
    @Override
    public boolean contains(final BoundingBox bbox) {
        ensureCompatibleReferenceSystem(bbox);
        return super.contains(toJTSEnvelope(bbox));
    }

    /**
     * Check if this bounding box intersects the provided bounds.
     *
     * @since 2.4
     */
    @Override
    public boolean intersects(final BoundingBox bbox) {
        ensureCompatibleReferenceSystem(bbox);

        return super.intersects(toJTSEnvelope(bbox));
    }

    /** Check if this bounding box intersects the provided bounds. */
    @Override
    public ReferencedEnvelope intersection(Envelope env) {
        if (env instanceof BoundingBox) {
            BoundingBox bbox = (BoundingBox) env;
            ensureCompatibleReferenceSystem(bbox);
        }
        return new ReferencedEnvelope(super.intersection(env), this.getCoordinateReferenceSystem());
    }

    /**
     * Include the provided bounding box, expanding as necessary.
     *
     * @since 2.4
     */
    @Override
    public void include(final BoundingBox bbox) {
        if (crs == null) {
            this.crs = bbox.getCoordinateReferenceSystem();
        } else {
            ensureCompatibleReferenceSystem(bbox);
        }
        expandToInclude(ReferencedEnvelope.reference(bbox));
    }

    /** Expand to include the provided DirectPosition */
    public void expandToInclude(Position pt) {
        Coordinate coordinate = new Coordinate(pt.getOrdinate(0), pt.getOrdinate(1));
        expandToInclude(coordinate);
    }

    public void expandToInclude(Point2D pt) {
        Position pos = new Position2D();
        ((Position2D) pos).setLocation(pt);
        this.expandToInclude(pos);
    }

    /**
     * Returns the X coordinate of the center of the rectangle.
     *
     * <p>Method compatibility with {@link XRectangle2D#getCenterX()}
     */
    public double getCenterX() {
        return getMedian(0);
    }

    /**
     * Returns the Y coordinate of the center of the rectangle.
     *
     * <p>Method compatibility with {@link XRectangle2D#getCenterY()}
     */
    public double getCenterY() {
        return getMedian(1);
    }

    /** Include the provided envelope, expanding as necessary. */
    @Override
    public void expandToInclude(Envelope other) {
        if (other instanceof BoundingBox) {
            if (other.isNull()) {
                return;
            }

            BoundingBox bbox = (BoundingBox) other;
            ensureCompatibleReferenceSystem(bbox);

            expandToInclude(bbox.getLowerCorner());
            expandToInclude(bbox.getUpperCorner());
        } else {
            super.expandToInclude(other);
        }
    }

    /**
     * Include the provided coordinates, expanding as necessary.
     *
     * @since 2.4
     */
    @Override
    public void include(double x, double y) {
        super.expandToInclude(x, y);
    }

    /**
     * Initialize the bounding box with another bounding box.
     *
     * @since 2.4
     */
    @Override
    public void setBounds(final BoundingBox bbox) {
        ensureCompatibleReferenceSystem(bbox);
        super.init(toJTSEnvelope(bbox));
    }

    /**
     * Initialize the bounding box using a line from the center to a corner.
     *
     * @param center location of the new bounding box
     * @param corner location of a corner establishing the extent of the bounding box
     */
    public void setFrameFromCenter(Point2D center, Point2D corner) {
        double widthDelta = Math.abs(corner.getX() - center.getX());
        double heightDelta = Math.abs(corner.getY() - center.getY());
        super.init(
                center.getX() - widthDelta, center.getX() + widthDelta,
                center.getY() - heightDelta, center.getY() + heightDelta);
    }

    /**
     * Initialize the bounding box using a line from the lower left to upper right corners.
     *
     * @param lowerLeft Lower left extent of the new bounding box
     * @param upperRight Upper right extent of the new bounding box
     */
    public void setFrameFromDiagonal(Point2D lowerLeft, Point2D upperRight) {
        super.init(lowerLeft.getX(), upperRight.getX(), lowerLeft.getY(), upperRight.getY());
    }

    /** Rectangle style x,y,width,height bounds definition */
    public void setFrame(double x, double y, double width, double height) {
        super.init(x, x + width, y, y + height);
    }

    /**
     * Returns a new bounding box which contains the transformed shape of this bounding box. This is a convenience
     * method that delegate its work to the {@link #transform transform} method.
     *
     * @since 2.4
     */
    @Override
    public BoundingBox toBounds(final CoordinateReferenceSystem targetCRS) throws TransformException {
        try {
            return transform(targetCRS, true);
        } catch (FactoryException e) {
            throw new TransformException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * Transforms the referenced envelope to the specified coordinate reference system.
     *
     * <p>This method can handle the case where the envelope contains the North or South pole, or when it cross the
     * &plusmn;180ï¿½ longitude.
     *
     * @param targetCRS The target coordinate reference system.
     * @param lenient {@code true} if datum shift should be applied even if there is insuffisient information. Otherwise
     *     (if {@code false}), an exception is thrown in such case.
     * @return The transformed envelope.
     * @throws FactoryException if the math transform can't be determined.
     * @throws TransformException if at least one coordinate can't be transformed.
     * @see CRS#transform(CoordinateOperation, Bounds)
     */
    public ReferencedEnvelope transform(CoordinateReferenceSystem targetCRS, boolean lenient)
            throws TransformException, FactoryException {
        return transform(targetCRS, lenient, 5);
    }

    /**
     * Transforms the referenced envelope to the specified coordinate reference system using the specified amount of
     * points.
     *
     * <p>This method can handle the case where the envelope contains the North or South pole, or when it cross the
     * &plusmn;180ï¿½ longitude.
     *
     * @param targetCRS The target coordinate reference system.
     * @param lenient {@code true} if datum shift should be applied even if there is insuffisient information. Otherwise
     *     (if {@code false}), an exception is thrown in such case.
     * @param numPointsForTransformation The number of points to use for sampling the envelope.
     * @return The transformed envelope.
     * @throws FactoryException if the math transform can't be determined.
     * @throws TransformException if at least one coordinate can't be transformed.
     * @see CRS#transform(CoordinateOperation, Bounds)
     * @since 2.3
     */
    public ReferencedEnvelope transform(
            final CoordinateReferenceSystem targetCRS, final boolean lenient, final int numPointsForTransformation)
            throws TransformException, FactoryException {
        if (crs == null) {
            if (isEmpty()) {
                // We don't have a CRS yet because we are still empty, being empty is
                // something we can represent in the targetCRS
                return new ReferencedEnvelope(targetCRS);
            } else {
                // really this is a the code that created this ReferencedEnvelope
                throw new NullPointerException(
                        "Unable to transform referenced envelope, crs has not yet been provided.");
            }
        }
        if (!this.isEmpty() && this.getWidth() == 0 && this.getHeight() == 0) {
            // single point envelope, no need to perform complex operations
            CoordinateOperationFactory coordinateOperationFactory = CRS.getCoordinateOperationFactory(lenient);

            final CoordinateOperation operation = coordinateOperationFactory.createOperation(crs, targetCRS);
            double[] position = {getMinX(), getMinY()};
            operation.getMathTransform().transform(position, 0, position, 0, 1);
            return new ReferencedEnvelope(position[0], position[0], position[1], position[1], targetCRS);
        }
        if (getDimension() != targetCRS.getCoordinateSystem().getDimension()) {
            if (lenient) {
                return JTS.transformTo3D(this, targetCRS, lenient, numPointsForTransformation);
            } else {
                final Object arg0 = crs.getName().getCode();
                final Object arg1 = Integer.valueOf(getDimension());
                final Object arg2 =
                        Integer.valueOf(targetCRS.getCoordinateSystem().getDimension());
                throw new MismatchedDimensionException(
                        MessageFormat.format(ErrorKeys.MISMATCHED_DIMENSION_$3, arg0, arg1, arg2));
            }
        }
        /*
         * Gets a first estimation using an algorithm capable to take singularity in account
         * (North pole, South pole, 180ï¿½ longitude). We will expand this initial box later.
         */
        CoordinateOperationFactory coordinateOperationFactory = CRS.getCoordinateOperationFactory(lenient);

        final CoordinateOperation operation = coordinateOperationFactory.createOperation(crs, targetCRS);
        final GeneralBounds transformed = CRS.transform(operation, this);
        transformed.setCoordinateReferenceSystem(targetCRS);

        /*
         * Now expands the box using the usual utility methods.
         */
        final ReferencedEnvelope target = new ReferencedEnvelope(transformed);
        final MathTransform transform = operation.getMathTransform();
        JTS.transform(this, target, transform, numPointsForTransformation);

        return target;
    }

    /**
     * Returns a hash value for this envelope. This value need not remain consistent between different implementations
     * of the same class.
     */
    @Override
    public int hashCode() {
        int code = super.hashCode() ^ (int) serialVersionUID;
        if (crs != null) {
            code ^= crs.hashCode();
        }
        return code;
    }

    /** Compares the specified object with this envelope for equality. */
    @Override
    public boolean equals(final Object object) {
        if (super.equals(object)) {
            final CoordinateReferenceSystem otherCRS =
                    object instanceof ReferencedEnvelope ? ((ReferencedEnvelope) object).crs : null;

            return CRS.equalsIgnoreMetadata(crs, otherCRS);
        }
        return false;
    }

    /**
     * Compare the bounds of this envelope with those of another.
     *
     * <p>Note: in this test:
     *
     * <ul>
     *   <li>the coordinate reference systems of the envelopes are not examined
     *   <li>only the first two dimensions of the envelopes are compared
     *   <li>it is assumed that each dimension equates to the same axis for both envelopes
     * </ul>
     *
     * @param other other envelope
     * @param eps a small tolerance factor (e.g. 1.0e-6d) which will be scaled relative to this envlope's width and
     *     height
     * @return true if all bounding coordinates are equal within the set tolerance; false otherwise
     */
    public boolean boundsEquals2D(final Bounds other, double eps) {
        eps *= 0.5 * (getWidth() + getHeight());

        double[] delta = new double[4];
        delta[0] = getMinimum(0) - other.getMinimum(0);
        delta[1] = getMaximum(0) - other.getMaximum(0);
        delta[2] = getMinimum(1) - other.getMinimum(1);
        delta[3] = getMaximum(1) - other.getMaximum(1);

        for (double v : delta) {
            // Use ! here to catch any NaN values
            if (!(Math.abs(v) <= eps)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compare the bounds of this envelope with those of another.
     *
     * <p>Note: in this test:
     *
     * <ul>
     *   <li>the coordinate reference systems of the envelopes are not examined
     *   <li>only the first two dimensions of the envelopes are compared
     *   <li>it is assumed that each dimension equates to the same axis for both envelopes
     * </ul>
     *
     * @param other other envelope
     * @param eps a small tolerance factor (e.g. 1.0e-6d) which will be scaled relative to this envlope's width and
     *     height
     * @return true if all bounding coordinates are equal within the set tolerance; false otherwise
     */
    public boolean boundsEquals2D(final Rectangle2D other, double eps) {
        eps *= 0.5 * (getWidth() + getHeight());

        double[] delta = new double[4];
        delta[0] = getMinimum(0) - other.getMinX();
        delta[1] = getMaximum(0) - other.getMaxX();
        delta[2] = getMinimum(1) - other.getMinY();
        delta[3] = getMaximum(1) - other.getMaxX();

        for (double v : delta) {
            // Use ! here to catch any NaN values
            if (!(Math.abs(v) <= eps)) {
                return false;
            }
        }
        return true;
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
        eps *= 0.5 * (getWidth() + getHeight());
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
     * (for example for debugging purpose).
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder(Classes.getShortClassName(this)).append('[');
        final int dimension = getDimension();

        for (int i = 0; i < dimension; i++) {
            if (i != 0) {
                buffer.append(", ");
            }

            buffer.append(getMinimum(i)).append(" : ").append(getMaximum(i));
        }
        buffer.append(']');

        final CoordinateReferenceSystem crs = getCoordinateReferenceSystem();
        if (crs != null) {
            buffer.append(" ")
                    .append(Classes.getShortClassName(crs))
                    .append("[")
                    .append(crs.getName())
                    .append("]");
            final CoordinateSystem cs = crs.getCoordinateSystem();
            if (cs != null) {
                for (int i = 0; i < cs.getDimension(); i++) {
                    buffer.append(" ").append(cs.getAxis(i));
                }
            }
        }

        return buffer.toString();
    }

    /**
     * Factory method to create the correct ReferencedEnvelope.
     *
     * @param original ReferencedEnvelope being duplicated
     * @return ReferencedEnvelope, ReferencedEnvelope3D if it is 3d
     */
    public static ReferencedEnvelope create(ReferencedEnvelope original) {
        if (original instanceof ReferencedEnvelope3D) {
            return new ReferencedEnvelope3D((ReferencedEnvelope3D) original);
        }
        return new ReferencedEnvelope(original);
    }

    /**
     * Factory method to create the correct ReferencedEnvelope implementation for the provided
     * CoordinateReferenceSystem.
     *
     * @param crs CoordinateReferenceSystem used to select ReferencedEnvelope implementation
     * @return ReferencedEnvelope, ReferencedEnvelope3D if it is 3d
     */
    public static ReferencedEnvelope create(CoordinateReferenceSystem crs) {
        if (crs != null && crs.getCoordinateSystem().getDimension() > 2) {
            return new ReferencedEnvelope3D(crs);
        }
        return new ReferencedEnvelope(crs);
    }

    /**
     * Utility method to create a ReferencedEnvelope from an opengis Envelope class, supporting 2d as well as 3d
     * envelopes (returning the right class).
     *
     * @param bounds The opgenis Envelope object
     * @return ReferencedEnvelope, ReferencedEnvelope3D if it is 3d,<br>
     *     results in a null/an empty envelope, if input envelope was a null/an empty envelope
     * @see {@link #reference(Bounds)}
     */
    public static ReferencedEnvelope create(Bounds bounds) {
        if (bounds == null) {
            return null;
        }

        if (bounds.getDimension() >= 3) {
            // emptiness test is inside reference-method
            return new ReferencedEnvelope3D(
                    (ReferencedEnvelope3D) reference(bounds), bounds.getCoordinateReferenceSystem());
        }

        return new ReferencedEnvelope(reference(bounds), bounds.getCoordinateReferenceSystem());
    }

    /**
     * Utility method to create a ReferencedEnvelope from provided {@code Bounds}.
     *
     * <p>Uses {@code crs} to determine if {@code ReferencedEnvelope} or {@code ReferencedEnvelope3D} is required.
     *
     * @param env Bounds representing extent
     * @param crs CRS
     * @return ReferencedEnvelope, ReferencedEnvelope3D if crs is 3d,<br>
     *     results in a null/an empty envelope, if input envelope was a null/an empty envelope.
     * @see {@link #reference(Bounds)}
     */
    public static ReferencedEnvelope create(Bounds env, CoordinateReferenceSystem crs) {

        if (env == null) {
            return null;
        }

        if (env.getDimension() >= 3) {
            // emptiness test is inside reference-method
            return new ReferencedEnvelope3D((ReferencedEnvelope3D) reference(env), crs);
        }

        return new ReferencedEnvelope(reference(env), crs);
    }

    /**
     * Utility method to copy a ReferencedEnvelope.
     *
     * <p>Uses {@code crs} to determine if {@code ReferencedEnvelope} or {@code ReferencedEnvelope3D} is required.
     *
     * @param crs CRS
     * @return ReferencedEnvelope, ReferencedEnvelope3D if crs is 3d,<br>
     *     results in a null/an empty envelope, if input envelope was a null/an empty envelope.
     * @return ReferencedEnvelope, ReferencedEnvelope3D if it is 3d
     */
    //    public static ReferencedEnvelope create(ReferencedEnvelope env, CoordinateReferenceSystem
    // crs) {
    //        return create((Bounds) env, crs);
    //    }

    /**
     * Construct 2D WGS84 referenced envelope using rectangle conventions using width and height.
     *
     * @param rectangle Rectangle defining extend of Referenced Envelope.
     */
    public static ReferencedEnvelope rect(Rectangle2D rectangle) {
        if (rectangle.isEmpty()) {
            return new ReferencedEnvelope(DefaultGeographicCRS.WGS84);
        }
        return new ReferencedEnvelope(
                rectangle.getX(),
                rectangle.getWidth(),
                rectangle.getY(),
                rectangle.getHeight(),
                DefaultGeographicCRS.WGS84);
    }

    /**
     * Construct referenced envelope using rectangle conventions using width and height.
     *
     * @param rectangle Rectangle defining extend of Referenced Envelope.
     */
    public static ReferencedEnvelope rect(Rectangle2D rectangle, CoordinateReferenceSystem crs) {
        if (rectangle.isEmpty()) {
            return new ReferencedEnvelope(crs);
        }
        return new ReferencedEnvelope(
                rectangle.getMinX(), rectangle.getMaxX(), rectangle.getMinY(), rectangle.getMaxY(), crs);
    }

    /**
     * Construct 2D WGS84 referenced envelope using rectangle conventions using width and height.
     *
     * <p>Migration method, previously {@code new Envelope2D(crs,x,y,w,h)}.
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public static ReferencedEnvelope rect(double x, double y, double width, double height) {
        return new ReferencedEnvelope(x, x + width, y, y + height, DefaultGeographicCRS.WGS84);
    }

    /**
     * Construct referenced envelope using rectangle conventions using width and height.
     *
     * <p>Migration method, previously {@code new Envelope2D(crs,x,y,w,h)}.
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @return ReferencedEnvelope WGS84
     */
    public static ReferencedEnvelope rect(
            double x, double y, double width, double height, CoordinateReferenceSystem crs) {
        return new ReferencedEnvelope(x, x + width, y, y + height, crs);
    }

    /**
     * Utility method to create a ReferencedEnvelope a plain JTS Envelope class, supporting 2d as well as 3d envelopes
     * (returning the right class).
     *
     * @param env The JTS Envelope object
     * @return ReferencedEnvelope, ReferencedEnvelope3D if it is 3d,<br>
     *     results in a null/an empty envelope, if input envelope was a null/an empty envelope
     */
    public static ReferencedEnvelope envelope(Envelope env, CoordinateReferenceSystem crs) {
        if (env == null) {
            return null;
        }
        if (crs != null && crs.getCoordinateSystem().getDimension() >= 3) {
            if (env.isNull()) {
                return new ReferencedEnvelope3D(crs);
            } else {
                return new ReferencedEnvelope3D(
                        env.getMinX(), env.getMaxX(), env.getMinY(), env.getMaxY(), Double.NaN, Double.NaN, crs);
            }
        }

        if (env.isNull()) {
            return new ReferencedEnvelope(crs);
        } else {
            return new ReferencedEnvelope(env, crs);
        }
    }

    /**
     * Cast to a ReferencedEnvelope (used to ensure that an Envelope is a ReferencedEnvelope).
     *
     * <p>This method first checks if <tt>e</tt> is an instanceof {@link ReferencedEnvelope}, if it is, itself is
     * returned. If not <code>new ReferencedEnvelpe(e,null)</code> is returned.
     *
     * <p>If e is null, null is returned.
     *
     * @param e The envelope. Can be null.
     * @return A ReferencedEnvelope using the specified envelope, or null if the envelope was null.
     */
    public static ReferencedEnvelope reference(Envelope e) {
        if (e == null) {
            return null;
        } else {
            if (e instanceof ReferencedEnvelope3D) {
                return (ReferencedEnvelope3D) e;
            }

            if (e instanceof ReferencedEnvelope) {
                return (ReferencedEnvelope) e;
            }

            return new ReferencedEnvelope(e, null);
        }
    }

    /**
     * Utility method to ensure that an BoundingBox in a ReferencedEnvelope.
     *
     * <p>This method first checks if <tt>e</tt> is an instanceof {@link ReferencedEnvelope}, if it is, itself is
     * returned. If not <code>new ReferencedEnvelpe(e)</code> is returned.
     *
     * @param e The envelope.
     */
    public static ReferencedEnvelope reference(ReferencedEnvelope e) {
        return reference((Bounds) e);
    }

    /**
     * Cast to a ReferencedEnvelope (used to ensure that an Envelope if a ReferencedEnvelope). Supporting 2d as well as
     * 3d envelopes (returning the right class).
     *
     * @param env The opgenis Envelope object
     * @return ReferencedEnvelope, ReferencedEnvelope3D if it is 3d,<br>
     *     results in a null/an empty envelope, if input envelope was a null/an empty envelope (by JTS Envelope
     *     definition: getMaximum(0) < getMinimum(0))
     */
    public static ReferencedEnvelope reference(Bounds env) {

        if (env == null) {
            return null;
        }

        if (env instanceof ReferencedEnvelope3D) {
            return (ReferencedEnvelope3D) env;
        }

        if (env instanceof ReferencedEnvelope) {
            return (ReferencedEnvelope) env;
        }

        if (env.getDimension() >= 3) {
            // emptiness test according to org.locationtech.jts.geom.Envelope using ! to catch NaN
            if (!(env.getMinimum(0) < env.getMaximum(0))) {
                return new ReferencedEnvelope3D(env.getCoordinateReferenceSystem());
            } else {
                return new ReferencedEnvelope3D(env);
            }
        }

        // emptiness test according to org.locationtech.jts.geom.Envelope using ! to catch NaN
        if (!(env.getMinimum(0) < env.getMaximum(0))) {
            // but for geographic crs the above means the original envelope
            // wrapped the dateline, while we cannot expressed the same in JTS,
            // we'll at least convert it to a whole world envelope
            if (env.getCoordinateReferenceSystem() instanceof GeographicCRS && env.getMinimum(1) <= env.getMaximum(1)) {
                return new ReferencedEnvelope(
                        -180.0, 180.0, env.getMinimum(1), env.getMaximum(1), env.getCoordinateReferenceSystem());
            }
            return new ReferencedEnvelope(env.getCoordinateReferenceSystem());
        }
        return new ReferencedEnvelope(env);
    }

    /**
     * Utility method to create a ReferencedEnvelope from a Java2D Rectangle class, supporting empty rectangles.
     *
     * @param rectangle The Java2D Rectangle object
     * @return ReferencedEnvelope,<br>
     *     results in a null/an empty envelope, if input rectangle was empty
     */
    public static ReferencedEnvelope create(Rectangle2D rectangle, CoordinateReferenceSystem crs) {
        if (rectangle.isEmpty()) {
            return new ReferencedEnvelope(crs);
        } else {
            return new ReferencedEnvelope(rectangle, crs);
        }
    }
}
