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

import java.awt.geom.Rectangle2D;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.DirectPosition;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.MismatchedReferenceSystemException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.cs.CoordinateSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.referencing.CRS;
import org.geotools.util.Classes;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;

/**
 * A JTS envelope associated with a {@linkplain CoordinateReferenceSystem coordinate reference
 * system}. In addition, this JTS envelope also implements the GeoAPI {@linkplain
 * org.geotools.api.geometry.coordinate.Envelope envelope} interface for interoperability with
 * GeoAPI.
 *
 * @since 2.2
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 * @author Simone Giannecchini
 * @see org.geotools.geometry.Envelope2D
 * @see org.geotools.geometry.GeneralEnvelope
 * @see org.geotools.api.metadata.extent.GeographicBoundingBox
 */
public class ReferencedEnvelope extends org.geotools.api.geom.ReferencedEnvelope {

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
                public boolean contains(DirectPosition pos) {
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
        super(crs);
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
            final double x1,
            final double x2,
            final double y1,
            final double y2,
            final CoordinateReferenceSystem crs)
            throws MismatchedDimensionException {
        super(x1, x2, y1, y2);
        this.crs = crs;
        checkCoordinateReferenceSystemDimension();
    }

    /**
     * Creates an envelope for a Java2D rectangle.
     *
     * <p>NOTE: if the rectangle is empty, the resulting ReferencedEnvelope will not be. In case
     * this is needed use {@link #create(Rectangle2D, CoordinateReferenceSystem)
     * ReferencedEnvelope.create(rectangle, crs)}
     *
     * @param rectangle The rectangle.
     * @param crs The coordinate reference system.
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     * @since 2.4
     */
    public ReferencedEnvelope(final Rectangle2D rectangle, final CoordinateReferenceSystem crs)
            throws MismatchedDimensionException {
        this(
                rectangle.getMinX(),
                rectangle.getMaxX(),
                rectangle.getMinY(),
                rectangle.getMaxY(),
                crs);
    }

    /**
     * Creates a new envelope from an existing envelope.
     *
     * @param envelope The envelope to initialize from
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     * @since 2.3
     */
    public ReferencedEnvelope(final ReferencedEnvelope envelope)
            throws MismatchedDimensionException {
        super(envelope);
        crs = envelope.getCoordinateReferenceSystem();
        checkCoordinateReferenceSystemDimension();
    }

    /**
     * Creates a new envelope from an existing bounding box.
     *
     * <p>NOTE: if the bounding box is empty, the resulting ReferencedEnvelope will not be. In case
     * this is needed use {@link #create(org.geotools.api.geometry.Envelope,
     * CoordinateReferenceSystem) ReferencedEnvelope.create(bbox,
     * bbox.getCoordinateReferenceSystem())}
     *
     * @param bbox The bounding box to initialize from.
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     * @since 2.4
     */
    public ReferencedEnvelope(final BoundingBox bbox) throws MismatchedDimensionException {
        this(
                bbox.getMinX(),
                bbox.getMaxX(),
                bbox.getMinY(),
                bbox.getMaxY(),
                bbox.getCoordinateReferenceSystem());
    }

    /**
     * Creates a new envelope from an existing OGC envelope.
     *
     * <p>NOTE: if the envelope is empty, the resulting ReferencedEnvelope will not be. In case this
     * is needed use {@link #create(org.geotools.api.geometry.Envelope, CoordinateReferenceSystem)
     * ReferencedEnvelope.create(envelope, envelope.getCoordinateReferenceSystem())}
     *
     * @param envelope The envelope to initialize from.
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     * @since 2.4
     */
    public ReferencedEnvelope(final org.geotools.api.geometry.Envelope envelope)
            throws MismatchedDimensionException {
        super(
                envelope.getMinimum(0),
                envelope.getMaximum(0),
                envelope.getMinimum(1),
                envelope.getMaximum(1));
        this.crs = envelope.getCoordinateReferenceSystem();
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

    /**
     * Transforms the referenced envelope to the specified coordinate reference system.
     *
     * <p>This method can handle the case where the envelope contains the North or South pole, or
     * when it cross the &plusmn;180ï¿½ longitude.
     *
     * @param targetCRS The target coordinate reference system.
     * @param lenient {@code true} if datum shift should be applied even if there is insuffisient
     *     information. Otherwise (if {@code false}), an exception is thrown in such case.
     * @return The transformed envelope.
     * @throws FactoryException if the math transform can't be determined.
     * @throws TransformException if at least one coordinate can't be transformed.
     * @see CRS#transform(CoordinateOperation, org.geotools.api.geometry.Envelope)
     */
    @Override
    public ReferencedEnvelope transform(CoordinateReferenceSystem targetCRS, boolean lenient)
            throws TransformException, FactoryException {
        return transform(targetCRS, lenient, 5);
    }

    /**
     * Transforms the referenced envelope to the specified coordinate reference system using the
     * specified amount of points.
     *
     * <p>This method can handle the case where the envelope contains the North or South pole, or
     * when it cross the &plusmn;180ï¿½ longitude.
     *
     * @param targetCRS The target coordinate reference system.
     * @param lenient {@code true} if datum shift should be applied even if there is insuffisient
     *     information. Otherwise (if {@code false}), an exception is thrown in such case.
     * @param numPointsForTransformation The number of points to use for sampling the envelope.
     * @return The transformed envelope.
     * @throws FactoryException if the math transform can't be determined.
     * @throws TransformException if at least one coordinate can't be transformed.
     * @see CRS#transform(CoordinateOperation, org.geotools.api.geometry.Envelope)
     * @since 2.3
     */
    @Override
    public ReferencedEnvelope transform(
            final CoordinateReferenceSystem targetCRS,
            final boolean lenient,
            final int numPointsForTransformation)
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
        if (getDimension() != targetCRS.getCoordinateSystem().getDimension()) {
            if (lenient) {
                return JTS.transformTo3D(this, targetCRS, lenient, numPointsForTransformation);
            } else {
                throw new MismatchedDimensionException(
                        Errors.format(
                                ErrorKeys.MISMATCHED_DIMENSION_$3,
                                crs.getName().getCode(),
                                Integer.valueOf(getDimension()),
                                Integer.valueOf(targetCRS.getCoordinateSystem().getDimension())));
            }
        }
        /*
         * Gets a first estimation using an algorithm capable to take singularity in account
         * (North pole, South pole, 180ï¿½ longitude). We will expand this initial box later.
         */
        CoordinateOperationFactory coordinateOperationFactory =
                CRS.getCoordinateOperationFactory(lenient);

        final CoordinateOperation operation =
                coordinateOperationFactory.createOperation(crs, targetCRS);
        final GeneralEnvelope transformed = CRS.transform(operation, this);
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
     * Returns a hash value for this envelope. This value need not remain consistent between
     * different implementations of the same class.
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
                    (object instanceof ReferencedEnvelope)
                            ? ((ReferencedEnvelope) object).crs
                            : null;

            return CRS.equalsIgnoreMetadata(crs, otherCRS);
        }
        return false;
    }

    /**
     * Returns a string representation of this envelope. The default implementation is okay for
     * occasional formatting (for example for debugging purpose).
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
     * Utility method to create a ReferencedEnvelope from an opengis Envelope class, supporting 2d
     * as well as 3d envelopes (returning the right class).
     *
     * @param env The opgenis Envelope object
     * @return ReferencedEnvelope, ReferencedEnvelope3D if it is 3d,<br>
     *     results in a null/an empty envelope, if input envelope was a null/an empty envelope
     * @see {@link #reference(org.geotools.api.geometry.Envelope)}
     */
    public static ReferencedEnvelope create(
            org.geotools.api.geometry.Envelope env, CoordinateReferenceSystem crs) {

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
     * Utility method to create a ReferencedEnvelope from an opengis Envelope class, supporting 2d
     * as well as 3d envelopes (returning the right class).
     *
     * @param env The opgenis Envelope object
     * @return ReferencedEnvelope, ReferencedEnvelope3D if it is 3d
     */
    public static ReferencedEnvelope create(ReferencedEnvelope env, CoordinateReferenceSystem crs) {
        return create((org.geotools.api.geometry.Envelope) env, crs);
    }
    /**
     * Utility method to create a ReferencedEnvelope from an JTS Envelope class, supporting 2d as
     * well as 3d envelopes (returning the right class).
     *
     * @param env The JTS Envelope object
     * @return ReferencedEnvelope, ReferencedEnvelope3D if it is 3d,<br>
     *     results in a null/an empty envelope, if input envelope was a null/an empty envelope
     */
    public static ReferencedEnvelope create(Envelope env, CoordinateReferenceSystem crs) {
        if (env == null) {
            return null;
        }
        if (crs != null && crs.getCoordinateSystem().getDimension() >= 3) {
            if (env.isNull()) {
                return new ReferencedEnvelope3D(crs);
            } else {
                return new ReferencedEnvelope3D(
                        env.getMinX(),
                        env.getMaxX(),
                        env.getMinY(),
                        env.getMaxY(),
                        Double.NaN,
                        Double.NaN,
                        crs);
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
     * <p>This method first checks if <tt>e</tt> is an instanceof {@link ReferencedEnvelope}, if it
     * is, itself is returned. If not <code>new ReferencedEnvelpe(e,null)</code> is returned.
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
     * <p>This method first checks if <tt>e</tt> is an instanceof {@link ReferencedEnvelope}, if it
     * is, itself is returned. If not <code>new ReferencedEnvelpe(e)</code> is returned.
     *
     * @param e The envelope.
     */
    public static ReferencedEnvelope reference(ReferencedEnvelope e) {
        return reference((org.geotools.api.geometry.Envelope) e);
    }

    /**
     * Cast to a ReferencedEnvelope (used to ensure that an Envelope if a ReferencedEnvelope).
     * Supporting 2d as well as 3d envelopes (returning the right class).
     *
     * @param env The opgenis Envelope object
     * @return ReferencedEnvelope, ReferencedEnvelope3D if it is 3d,<br>
     *     results in a null/an empty envelope, if input envelope was a null/an empty envelope (by
     *     JTS Envelope definition: getMaximum(0) < getMinimum(0))
     */
    public static ReferencedEnvelope reference(org.geotools.api.geometry.Envelope env) {

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
            // emptiness test according to org.locationtech.jts.geom.Envelope
            if (env.getMaximum(0) < env.getMinimum(0)) {
                return new ReferencedEnvelope3D(env.getCoordinateReferenceSystem());
            } else {
                return new ReferencedEnvelope3D(env);
            }
        }

        // emptiness test according to org.locationtech.jts.geom.Envelope
        if (env.getMaximum(0) < env.getMinimum(0))
            return new ReferencedEnvelope(env.getCoordinateReferenceSystem());

        return new ReferencedEnvelope(env);
    }

    /**
     * Utility method to create a ReferencedEnvelope from a Java2D Rectangle class, supporting empty
     * rectangles.
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
