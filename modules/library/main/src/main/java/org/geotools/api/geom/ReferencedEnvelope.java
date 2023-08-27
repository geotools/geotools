package org.geotools.api.geom;

import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.DirectPosition;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.MismatchedReferenceSystemException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;

public abstract class ReferencedEnvelope extends Envelope implements org.geotools.api.geometry.Envelope, BoundingBox {
    /**
     * The coordinate reference system, or {@code null}.
     */
    protected CoordinateReferenceSystem crs;

    public ReferencedEnvelope(Envelope env) {
        super(env);
    }

    public ReferencedEnvelope(double x1, double x2, double y1, double y2) {
        super(x1, x2, y1, y2);
    }

    public ReferencedEnvelope(CoordinateReferenceSystem crs) {
        this.crs = crs;
    }

    /**
     * Returns the specified bounding box as a JTS envelope.
     */
    protected static Envelope getJTSEnvelope(final BoundingBox bbox) {
        if (bbox == null) {
            throw new NullPointerException("Provided bbox envelope was null");
        }
        if (bbox instanceof Envelope) {
            return (Envelope) bbox;
        }
        // safe creation if empty bounds
        return org.geotools.geometry.jts.ReferencedEnvelope.create(bbox, bbox.getCoordinateReferenceSystem());
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
     * @param eps   a small tolerance factor (e.g. 1.0e-6d) which will be scaled relative to this
     *              envlope's width and height
     * @return true if all bounding coordinates are equal within the set tolerance; false otherwise
     */
    public boolean boundsEquals2D(final org.geotools.api.geometry.Envelope other, double eps) {
        eps *= 0.5 * (getWidth() + getHeight());

        double[] delta = new double[4];
        delta[0] = getMinimum(0) - other.getMinimum(0);
        delta[1] = getMaximum(0) - other.getMaximum(0);
        delta[2] = getMinimum(1) - other.getMinimum(1);
        delta[3] = getMaximum(1) - other.getMaximum(1);

        for (double v : delta) {
            /*
             * As per Envelope2D#boundsEquals we use ! here to
             * catch any NaN values
             */
            if (!(Math.abs(v) <= eps)) {
                return false;
            }
        }
        return true;
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
                throw new MismatchedDimensionException(
                        Errors.format(
                                ErrorKeys.MISMATCHED_DIMENSION_$3,
                                crs.getName().getCode(),
                                Integer.valueOf(dimension),
                                Integer.valueOf(expected)));
            }
        }
    }

    /**
     * Returns {@code true} if the provided location is contained by this bounding box.
     *
     * @since 2.4
     */
    @Override
    public boolean contains(DirectPosition pos) {
        ensureCompatibleReferenceSystem(pos);
        return contains(pos.getOrdinate(0), pos.getOrdinate(1));
    }

    /**
     * Returns {@code true} if the provided bounds are contained by this bounding box.
     *
     * @since 2.4
     */
    @Override
    public boolean contains(final BoundingBox bbox) {
        ensureCompatibleReferenceSystem(bbox);

        return contains(ReferencedEnvelope.getJTSEnvelope(bbox));
    }

    /**
     * Make sure that the specified bounding box uses the same CRS than this one.
     *
     * @param bbox The other bounding box to test for compatibility.
     * @throws MismatchedReferenceSystemException if the CRS are incompatible.
     */
    protected void ensureCompatibleReferenceSystem(final BoundingBox bbox)
            throws MismatchedReferenceSystemException {
        if (crs != null) {
            final CoordinateReferenceSystem other = bbox.getCoordinateReferenceSystem();
            if (other != null) {
                if (!CRS.equalsIgnoreMetadata(crs, other)) {
                    throw new MismatchedReferenceSystemException(
                            Errors.format(ErrorKeys.MISMATCHED_COORDINATE_REFERENCE_SYSTEM));
                }
            }
        }
    }

    /**
     * Make sure that the specified location uses the same CRS as this one.
     *
     * @throws MismatchedReferenceSystemException if the CRS are incompatible.
     */
    protected void ensureCompatibleReferenceSystem(DirectPosition location) {
        if (crs != null) {
            final CoordinateReferenceSystem other = location.getCoordinateReferenceSystem();
            if (other != null) {
                if (!CRS.equalsIgnoreMetadata(crs, other)) {
                    throw new MismatchedReferenceSystemException(
                            Errors.format(ErrorKeys.MISMATCHED_COORDINATE_REFERENCE_SYSTEM));
                }
            }
        }
    }

    /**
     * Expand to include the provided DirectPosition
     */
    public void expandToInclude(DirectPosition pt) {
        Coordinate coordinate = new Coordinate(pt.getOrdinate(0), pt.getOrdinate(1));
        expandToInclude(coordinate);
    }

    /**
     * Include the provided envelope, expanding as necessary.
     */
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
     * Returns the coordinate reference system associated with this envelope.
     */
    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }

    /**
     * Returns the number of dimensions.
     */
    @Override
    public int getDimension() {
        return 2;
    }

    /**
     * A coordinate position consisting of all the minimal ordinates for each dimension for all
     * points within the {@code Envelope}.
     */
    @Override
    public DirectPosition getLowerCorner() {
        return new DirectPosition2D(crs, getMinX(), getMinY());
    }

    /**
     * Returns the maximal ordinate along the specified dimension.
     */
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

    /**
     * Returns the center ordinate along the specified dimension.
     */
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
     * Returns the minimal ordinate along the specified dimension.
     */
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

    /**
     * Returns the envelope length along the specified dimension. This length is equals to the
     * maximum ordinate minus the minimal ordinate.
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
     * A coordinate position consisting of all the maximal ordinates for each dimension for all
     * points within the {@code Envelope}.
     */
    @Override
    public DirectPosition getUpperCorner() {
        return new DirectPosition2D(crs, getMaxX(), getMaxY());
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
        expandToInclude(org.geotools.geometry.jts.ReferencedEnvelope.reference(bbox));
    }

    /**
     * Include the provided coordinates, expanding as necessary.
     *
     * @since 2.4
     */
    @Override
    public void include(double x, double y) {
        expandToInclude(x, y);
    }

    /**
     * Sets this envelope to the specified bounding box.
     */
    public void init(BoundingBox bounds) {
        init(
                bounds.getMinimum(0),
                bounds.getMaximum(0),
                bounds.getMinimum(1),
                bounds.getMaximum(1));
        this.crs = bounds.getCoordinateReferenceSystem();
    }

    /**
     * Check if this bounding box intersects the provided bounds.
     */
    @Override
    public org.geotools.geometry.jts.ReferencedEnvelope intersection(Envelope env) {
        if (env instanceof BoundingBox) {
            BoundingBox bbox = (BoundingBox) env;
            ensureCompatibleReferenceSystem(bbox);
        }
        return new org.geotools.geometry.jts.ReferencedEnvelope(super.intersection(env), this.getCoordinateReferenceSystem());
    }

    /**
     * Check if this bounding box intersects the provided bounds.
     *
     * @since 2.4
     */
    @Override
    public boolean intersects(final BoundingBox bbox) {
        ensureCompatibleReferenceSystem(bbox);

        return intersects(ReferencedEnvelope.getJTSEnvelope(bbox));
    }

    /**
     * Returns {@code true} if lengths along all dimension are zero.
     *
     * @since 2.4
     */
    @Override
    public boolean isEmpty() {
        return isNull();
    }

    /**
     * Initialize the bounding box with another bounding box.
     *
     * @since 2.4
     */
    @Override
    public void setBounds(final BoundingBox bbox) {
        ensureCompatibleReferenceSystem(bbox);
        init(ReferencedEnvelope.getJTSEnvelope(bbox));
    }

    /**
     * Returns a new bounding box which contains the transformed shape of this bounding box. This is
     * a convenience method that delegate its work to the {@link #transform transform} method.
     *
     * @since 2.4
     */
    @Override
    public BoundingBox toBounds(final CoordinateReferenceSystem targetCRS)
            throws TransformException {
        try {
            return transform(targetCRS, true);
        } catch (FactoryException e) {
            throw new TransformException(e.getLocalizedMessage(), e);
        }
    }

    public abstract org.geotools.geometry.jts.ReferencedEnvelope transform(CoordinateReferenceSystem targetCRS, boolean lenient)
            throws TransformException, FactoryException;

    public abstract org.geotools.geometry.jts.ReferencedEnvelope transform(
            CoordinateReferenceSystem targetCRS,
            boolean lenient,
            int numPointsForTransformation)
            throws TransformException, FactoryException;
}
