/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.text.MessageFormat;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.BoundingBox3D;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.Position3D;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;

/**
 * A 3D envelope associated with a {@linkplain CoordinateReferenceSystem coordinate reference system}. In addition, this
 * JTS envelope also implements the GeoAPI {@linkplain Bounds envelope} interface for interoperability with GeoAPI.
 *
 * @version $Id$
 * @author Niels Charlier
 */
public class ReferencedEnvelope3D extends ReferencedEnvelope implements BoundingBox3D {

    /** Serial number for compatibility with different versions. */
    private static final long serialVersionUID = -3188702602373537163L;

    /**
     * Test the point q to see whether it intersects the Envelope defined by p1-p2
     *
     * @param p1 one extremal point of the envelope
     * @param p2 another extremal point of the envelope
     * @param q the point to test for intersection
     * @return <code>true</code> if q intersects the envelope p1-p2
     */
    public static boolean intersects(Coordinate p1, Coordinate p2, Coordinate q) {
        // OptimizeIt shows that Math#min and Math#max here are a bottleneck.
        // Replace with direct comparisons. [Jon Aquino]
        if (((q.x >= (p1.x < p2.x ? p1.x : p2.x)) && (q.x <= (p1.x > p2.x ? p1.x : p2.x)))
                && ((q.y >= (p1.y < p2.y ? p1.y : p2.y)) && (q.y <= (p1.y > p2.y ? p1.y : p2.y)))) {
            return true;
        }
        return false;
    }

    /**
     * Test the envelope defined by p1-p2 for intersection with the envelope defined by q1-q2
     *
     * @param p1 one extremal point of the envelope P
     * @param p2 another extremal point of the envelope P
     * @param q1 one extremal point of the envelope Q
     * @param q2 another extremal point of the envelope Q
     * @return <code>true</code> if Q intersects P
     */
    public static boolean intersects(Coordinate p1, Coordinate p2, Coordinate q1, Coordinate q2) {
        double minq = Math.min(q1.x, q2.x);
        double maxq = Math.max(q1.x, q2.x);
        double minp = Math.min(p1.x, p2.x);
        double maxp = Math.max(p1.x, p2.x);

        if (minp > maxq) return false;
        if (maxp < minq) return false;

        minq = Math.min(q1.y, q2.y);
        maxq = Math.max(q1.y, q2.y);
        minp = Math.min(p1.y, p2.y);
        maxp = Math.max(p1.y, p2.y);

        if (minp > maxq) return false;
        if (maxp < minq) return false;
        return true;
    }

    /** the minimum z-coordinate */
    private double minz;

    /** the maximum z-coordinate */
    private double maxz;

    /** Initialize to a null <code>Envelope</code>. */
    @Override
    public void init() {
        setToNull();
    }

    /**
     * Initialize an <code>Envelope</code> for a region defined by maximum and minimum values.
     *
     * @param x1 the first x-value
     * @param x2 the second x-value
     * @param y1 the first y-value
     * @param y2 the second y-value
     * @param z1 the first z-value
     * @param z2 the second z-value
     */
    public void init(double x1, double x2, double y1, double y2, double z1, double z2) {
        init(x1, x2, y1, y2);
        if (z1 < z2) {
            minz = z1;
            maxz = z2;
        } else {
            minz = z2;
            maxz = z1;
        }
    }

    /**
     * Initialize an <code>Envelope</code> to a region defined by two Coordinates.
     *
     * @param p1 the first Coordinate
     * @param p2 the second Coordinate
     */
    @Override
    public void init(Coordinate p1, Coordinate p2) {
        init(p1.x, p2.x, p1.y, p2.y, p1.getZ(), p2.getZ());
    }

    /**
     * Initialize an <code>Envelope</code> to a region defined by a single Coordinate.
     *
     * @param p the coordinate
     */
    @Override
    public void init(Coordinate p) {
        init(p.x, p.x, p.y, p.y, p.getZ(), p.getZ());
    }

    @Override
    public void init(Envelope env) {
        super.init(env);
        if (env instanceof BoundingBox3D) {
            this.minz = ((BoundingBox3D) env).getMinZ();
            this.maxz = ((BoundingBox3D) env).getMaxZ();
        }
    }

    /**
     * Initialize an <code>Envelope</code> from an existing 3D Envelope.
     *
     * @param env the 3D Envelope to initialize from
     */
    public void init(ReferencedEnvelope3D env) {
        super.init((Envelope) env);
        this.minz = env.minz;
        this.maxz = env.maxz;
    }

    /** Makes this <code>Envelope</code> a "null" envelope, that is, the envelope of the empty geometry. */
    @Override
    public void setToNull() {
        super.setToNull();
        minz = 0;
        maxz = -1;
    }

    /**
     * Returns the difference between the maximum and minimum z values.
     *
     * @return max z - min z, or 0 if this is a null <code>Envelope</code>
     */
    public double getDepth() {
        if (isNull()) {
            return 0;
        }
        return maxz - minz;
    }

    /**
     * Returns the <code>Envelope</code>s minimum z-value. min z > max z indicates that this is a null <code>Envelope
     * </code>.
     *
     * @return the minimum z-coordinate
     */
    @Override
    public double getMinZ() {
        return minz;
    }

    /**
     * Returns the <code>Envelope</code>s maximum z-value. min z > max z indicates that this is a null <code>Envelope
     * </code>.
     *
     * @return the maximum z-coordinate
     */
    @Override
    public double getMaxZ() {
        return maxz;
    }

    /**
     * Gets the volume of this envelope.
     *
     * @return the volume of the envelope, 0.0 if the envelope is null
     */
    public double getVolume() {
        return getWidth() * getHeight() * getDepth();
    }

    /**
     * Gets the minimum extent of this envelope across all three dimensions.
     *
     * @return the minimum extent of this envelope
     */
    @Override
    public double minExtent() {
        if (isNull()) return 0.0;
        return Math.min(getWidth(), Math.min(getHeight(), getDepth()));
    }

    /**
     * Gets the maximum extent of this envelope across both dimensions.
     *
     * @return the maximum extent of this envelope
     */
    @Override
    public double maxExtent() {
        if (isNull()) return 0.0;
        return Math.max(getWidth(), Math.max(getHeight(), getDepth()));
    }

    /**
     * Enlarges this <code>Envelope</code> so that it contains the given {@link Coordinate}. Has no effect if the point
     * is already on or within the envelope.
     *
     * @param p the Coordinate to expand to include
     */
    @Override
    public void expandToInclude(Coordinate p) {
        expandToInclude(p.x, p.y, p.getZ());
    }

    /**
     * Expands this envelope by a given distance in all directions. Both positive and negative distances are supported.
     *
     * @param distance the distance to expand the envelope
     */
    @Override
    public void expandBy(double distance) {
        expandBy(distance, distance, distance);
    }

    /**
     * Expands this envelope by a given distance in all directions. Both positive and negative distances are supported.
     *
     * @param deltaX the distance to expand the envelope along the the X axis
     * @param deltaY the distance to expand the envelope along the the Y axis
     */
    public void expandBy(double deltaX, double deltaY, double deltaZ) {
        if (isNull()) return;

        minz -= deltaZ;
        maxz += deltaZ;
        expandBy(deltaX, deltaY);

        // check for envelope disappearing
        if (minz > maxz) setToNull();
    }

    /**
     * Enlarges this <code>Envelope</code> so that it contains the given point. Has no effect if the point is already on
     * or within the envelope.
     *
     * @param x the value to lower the minimum x to or to raise the maximum x to
     * @param y the value to lower the minimum y to or to raise the maximum y to
     * @param z the value to lower the minimum z to or to raise the maximum z to
     */
    public void expandToInclude(double x, double y, double z) {
        if (isNull()) {
            expandToInclude(x, y);
            minz = z;
            maxz = z;
        } else {
            expandToInclude(x, y);
            if (z < minz) {
                minz = z;
            }
            if (z > maxz) {
                maxz = z;
            }
        }
    }

    @Override
    public void expandToInclude(Position pt) {
        double x = pt.getOrdinate(0);
        double y = pt.getOrdinate(1);
        double z = pt.getDimension() >= 3 ? pt.getOrdinate(2) : Double.NaN;
        expandToInclude(x, y, z);
    }
    /**
     * Translates this envelope by given amounts in the X and Y direction.
     *
     * @param transX the amount to translate along the X axis
     * @param transY the amount to translate along the Y axis
     * @param transZ the amount to translate along the Z axis
     */
    public void translate(double transX, double transY, double transZ) {
        if (isNull()) {
            return;
        }
        init(
                getMinX() + transX,
                getMaxX() + transX,
                getMinY() + transY,
                getMaxY() + transY,
                getMinZ() + transZ,
                getMaxZ() + transZ);
    }

    /**
     * Computes the coordinate of the centre of this envelope (as long as it is non-null
     *
     * @return the centre coordinate of this envelope <code>null</code> if the envelope is null
     */
    @Override
    public Coordinate centre() {
        if (isNull()) return null;
        return new Coordinate(
                (getMinX() + getMaxX()) / 2.0, (getMinY() + getMaxY()) / 2.0, (getMinZ() + getMaxZ()) / 2.0);
    }

    /**
     * Check if the region defined by <code>other</code> overlaps (intersects) the region of this <code>Envelope</code>.
     *
     * @param other the <code>Envelope</code> which this <code>Envelope</code> is being checked for overlapping
     * @return <code>true</code> if the <code>Envelope</code>s overlap
     */
    public boolean intersects(ReferencedEnvelope3D other) {
        if (isNull() || other.isNull()) {
            return false;
        }
        return super.intersects((Envelope) other) && !(other.minz > maxz || other.maxz < minz);
    }

    /**
     * Check if the point <code>p</code> overlaps (lies inside) the region of this <code>Envelope
     * </code>.
     *
     * @param p the <code>Coordinate</code> to be tested
     * @return <code>true</code> if the point overlaps this <code>Envelope</code>
     */
    @Override
    public boolean intersects(Coordinate p) {
        return intersects(p.x, p.y, p.getZ());
    }

    /** @deprecated Use #intersects instead. */
    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public boolean overlaps(Coordinate p) {
        return intersects(p);
    }

    /**
     * Check if the point <code>(x, y)</code> overlaps (lies inside) the region of this <code>
     * Envelope</code>.
     *
     * @param x the x-ordinate of the point
     * @param y the y-ordinate of the point
     * @param z the z-ordinate of the point
     * @return <code>true</code> if the point overlaps this <code>Envelope</code>
     */
    public boolean intersects(double x, double y, double z) {
        if (isNull()) return false;
        return intersects(x, y) && !(z > maxz || z < minz);
    }

    /**
     * Tests if the given point lies in or on the envelope.
     *
     * <p>Note that this is <b>not</b> the same definition as the SFS <tt>contains</tt>, which would exclude the
     * envelope boundary.
     *
     * @param p the point which this <code>Envelope</code> is being checked for containing
     * @return <code>true</code> if the point lies in the interior or on the boundary of this <code>
     *     Envelope</code>.
     * @see #covers(Coordinate)
     */
    @Override
    public boolean contains(Coordinate p) {
        return covers(p);
    }

    /**
     * Tests if the given point lies in or on the envelope.
     *
     * <p>Note that this is <b>not</b> the same definition as the SFS <tt>contains</tt>, which would exclude the
     * envelope boundary.
     *
     * @param x the x-coordinate of the point which this <code>Envelope</code> is being checked for containing
     * @param y the y-coordinate of the point which this <code>Envelope</code> is being checked for containing
     * @return <code>true</code> if <code>(x, y)</code> lies in the interior or on the boundary of this <code>Envelope
     *     </code>.
     * @see #covers(double, double)
     */
    @Override
    public boolean contains(double x, double y, double z) {
        return covers(x, y, z);
    }

    /**
     * Tests if the given point lies in or on the envelope.
     *
     * @param x the x-coordinate of the point which this <code>Envelope</code> is being checked for containing
     * @param y the y-coordinate of the point which this <code>Envelope</code> is being checked for containing
     * @return <code>true</code> if <code>(x, y)</code> lies in the interior or on the boundary of this <code>Envelope
     *     </code>.
     */
    public boolean covers(double x, double y, double z) {
        if (isNull()) return false;
        return covers(x, y) && z >= minz && z <= maxz;
    }

    /**
     * Tests if the given point lies in or on the envelope.
     *
     * @param p the point which this <code>Envelope</code> is being checked for containing
     * @return <code>true</code> if the point lies in the interior or on the boundary of this <code>
     *     Envelope</code>.
     */
    @Override
    public boolean covers(Coordinate p) {
        return covers(p.x, p.y, p.getZ());
    }

    /**
     * Tests if the <code>Envelope other</code> lies wholely inside this <code>Envelope</code> (inclusive of the
     * boundary).
     *
     * @param other the <code>Envelope</code> to check
     * @return true if this <code>Envelope</code> covers the <code>other</code>
     */
    public boolean covers(ReferencedEnvelope3D other) {
        if (isNull() || other.isNull()) {
            return false;
        }
        return super.covers(other) && other.getMinZ() >= minz && other.getMaxZ() <= maxz;
    }

    /**
     * Computes the distance between this and another <code>Envelope</code>. The distance between overlapping Envelopes
     * is 0. Otherwise, the distance is the Euclidean distance between the closest points.
     */
    public double distance(ReferencedEnvelope3D env) {
        if (intersects(env)) return 0;

        double dx = 0.0;
        if (getMaxX() < env.getMinX()) dx = env.getMinX() - getMaxX();
        else if (getMinX() > env.getMaxX()) dx = getMinX() - env.getMaxX();

        double dy = 0.0;
        if (getMaxY() < env.getMinY()) dy = env.getMinY() - getMaxY();
        else if (getMinY() > env.getMaxY()) dy = getMinY() - env.getMaxY();

        double dz = 0.0;
        if (maxz < env.minz) dz = env.minz - maxz;
        else if (minz > env.maxz) dz = minz - env.maxz;

        // if either is zero, the envelopes overlap either vertically or
        // horizontally
        if (dx == 0.0 && dz == 0) return dy;
        if (dy == 0.0 && dz == 0) return dx;
        if (dx == 0 && dy == 0) return dz;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    // ---------------------------------------------------------------------------------------------------------------

    /** A ReferencedEnvelope containing "everything" */
    public static ReferencedEnvelope3D EVERYTHING =
            new ReferencedEnvelope3D(
                    Double.NEGATIVE_INFINITY,
                    Double.POSITIVE_INFINITY,
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
                public boolean contains(double x, double y, double z) {
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
                public double getVolume() {
                    // return super.getArea();
                    return Double.POSITIVE_INFINITY;
                }

                @Override
                public void setBounds(BoundingBox3D arg0) {
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
                    if (obj instanceof ReferencedEnvelope3D) {
                        ReferencedEnvelope3D other = (ReferencedEnvelope3D) obj;
                        if (other.crs != EVERYTHING.crs) return false;
                        if (other.getMinX() != EVERYTHING.getMinX()) return false;
                        if (other.getMinY() != EVERYTHING.getMinY()) return false;
                        if (other.getMinZ() != EVERYTHING.getMinZ()) return false;
                        if (other.getMaxX() != EVERYTHING.getMaxX()) return false;
                        if (other.getMaxY() != EVERYTHING.getMaxY()) return false;
                        if (other.getMaxZ() != EVERYTHING.getMaxZ()) return false;

                        return true;
                    }
                    return super.equals(obj);
                }

                @Override
                public String toString() {
                    return "ReferencedEnvelope.EVERYTHING";
                }
            };

    /** Creates a null envelope with a null coordinate reference system. */
    public ReferencedEnvelope3D() {
        this((CoordinateReferenceSystem) null);
    }

    /**
     * Creates a null envelope with the specified coordinate reference system.
     *
     * @param crs The coordinate reference system.
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     */
    public ReferencedEnvelope3D(CoordinateReferenceSystem crs) throws MismatchedDimensionException {
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
     * @param z1 The first y-value.
     * @param z2 The second y-value.
     * @param crs The coordinate reference system.
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     */
    public ReferencedEnvelope3D(
            final double x1,
            final double x2,
            final double y1,
            final double y2,
            final double z1,
            final double z2,
            final CoordinateReferenceSystem crs)
            throws MismatchedDimensionException {
        init(x1, x2, y1, y2, z1, z2);
        this.crs = crs;
        checkCoordinateReferenceSystemDimension();
    }

    /**
     * Creates a new envelope from an existing envelope.
     *
     * @param envelope The envelope to initialize from
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     */
    public ReferencedEnvelope3D(final ReferencedEnvelope3D envelope) throws MismatchedDimensionException {
        init(envelope);
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
     */
    public ReferencedEnvelope3D(final BoundingBox3D bbox) throws MismatchedDimensionException {
        this(
                bbox.getMinX(),
                bbox.getMaxX(),
                bbox.getMinY(),
                bbox.getMaxY(),
                bbox.getMinZ(),
                bbox.getMaxZ(),
                bbox.getCoordinateReferenceSystem());
    }

    /**
     * Creates a new envelope from an existing JTS envelope.
     *
     * @param envelope The envelope to initialize from.
     * @param crs The coordinate reference system.
     * @throws MismatchedDimensionExceptionif the CRS dimension is not valid.
     */
    public ReferencedEnvelope3D(final Envelope envelope, final CoordinateReferenceSystem crs)
            throws MismatchedDimensionException {
        super(envelope, crs);
        if (envelope instanceof ReferencedEnvelope3D) {
            this.minz = ((ReferencedEnvelope3D) envelope).getMinZ();
            this.maxz = ((ReferencedEnvelope3D) envelope).getMaxZ();
        }
    }

    /**
     * Creates a new envelope from an existing OGC envelope.
     *
     * <p>NOTE: if the envelope is empty, the resulting ReferencedEnvelope will not be. In case this is needed use
     * {@link #create(Bounds, CoordinateReferenceSystem) ReferencedEnvelope.create(envelope,
     * envelope.getCoordinateReferenceSystem())}
     *
     * @param envelope The envelope to initialize from.
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     */
    public ReferencedEnvelope3D(final Bounds envelope) throws MismatchedDimensionException {
        init(
                envelope.getMinimum(0),
                envelope.getMaximum(0),
                envelope.getMinimum(1),
                envelope.getMaximum(1),
                envelope.getMinimum(2),
                envelope.getMaximum(2));
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
    public ReferencedEnvelope3D(final ReferencedEnvelope3D envelope, final CoordinateReferenceSystem crs)
            throws MismatchedDimensionException {
        init(envelope);
        this.crs = crs;
        checkCoordinateReferenceSystemDimension();
    }

    /** Sets this envelope to the specified bounding box. */
    @Override
    public void init(BoundingBox bounds) {
        init(
                bounds.getMinimum(0),
                bounds.getMaximum(0),
                bounds.getMinimum(1),
                bounds.getMaximum(1),
                bounds.getMinimum(2),
                bounds.getMaximum(2));
        this.crs = bounds.getCoordinateReferenceSystem();
    }

    /** Returns the specified bounding box as a JTS envelope. */
    private static ReferencedEnvelope3D getJTSEnvelope(final BoundingBox3D bbox) {
        if (bbox == null) {
            throw new NullPointerException("Provided bbox envelope was null");
        }
        if (bbox instanceof ReferencedEnvelope3D) {
            return (ReferencedEnvelope3D) bbox;
        }
        return new ReferencedEnvelope3D(bbox);
    }

    /** Returns the number of dimensions. */
    @Override
    public int getDimension() {
        return 3;
    }

    /** Returns the minimal ordinate along the specified dimension. */
    @Override
    public double getMinimum(final int dimension) {
        switch (dimension) {
            case 0:
                return getMinX();

            case 1:
                return getMinY();

            case 2:
                return getMinZ();

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

            case 2:
                return getMaxZ();

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

            case 2:
                return 0.5 * (getMinZ() + getMaxZ());

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

            case 2:
                return getDepth();

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
        return new Position3D(crs, getMinX(), getMinY(), getMinZ());
    }

    /**
     * A coordinate position consisting of all the maximal ordinates for each dimension for all points within the
     * {@code Envelope}.
     */
    @Override
    public Position getUpperCorner() {
        return new Position3D(crs, getMaxX(), getMaxY(), getMaxZ());
    }

    /** Returns {@code true} if lengths along all dimension are zero. */
    @Override
    public boolean isEmpty() {
        return super.isNull();
    }

    /** Returns {@code true} if the provided location is contained by this bounding box. */
    @Override
    public boolean contains(Position pos) {
        ensureCompatibleReferenceSystem(pos);
        return contains(pos.getOrdinate(0), pos.getOrdinate(1), pos.getOrdinate(2));
    }

    /** Returns {@code true} if the provided bounds are contained by this bounding box. */
    public boolean contains(final BoundingBox3D bbox) {
        ensureCompatibleReferenceSystem(bbox);

        return covers(getJTSEnvelope(bbox));
    }

    /** Check if this bounding box intersects the provided bounds. */
    public boolean intersects(final BoundingBox3D bbox) {
        ensureCompatibleReferenceSystem(bbox);

        return intersects(getJTSEnvelope(bbox));
    }

    /**
     * Computes the intersection of two {@link Envelope}s.
     *
     * @param env the envelope to intersect with
     * @return a new Envelope representing the intersection of the envelopes (this will be the null envelope if either
     *     argument is null, or they do not intersect
     */
    public ReferencedEnvelope3D intersection(ReferencedEnvelope3D env) {
        ensureCompatibleReferenceSystem(env);

        if (isNull() || env.isNull() || !intersects(env)) return new ReferencedEnvelope3D();

        double intMinX = getMinX() > env.getMinX() ? getMinX() : env.getMinX();
        double intMinY = getMinY() > env.getMinY() ? getMinY() : env.getMinY();
        double intMinZ = minz > env.minz ? minz : env.minz;
        double intMaxX = getMaxX() < env.getMaxX() ? getMaxX() : env.getMaxX();
        double intMaxY = getMaxY() < env.getMaxY() ? getMaxY() : env.getMaxY();
        double intMaxZ = maxz < env.maxz ? maxz : env.maxz;

        return new ReferencedEnvelope3D(
                intMinX, intMaxX, intMinY, intMaxY, intMinZ, intMaxZ, env.getCoordinateReferenceSystem());
    }

    /** Include the provided bounding box, expanding as necessary. */
    public void include(final BoundingBox3D bbox) {
        if (crs == null) {
            this.crs = bbox.getCoordinateReferenceSystem();
        }
        expandToInclude(getJTSEnvelope(bbox));
    }

    /**
     * Enlarges this <code>Envelope</code> so that it contains the <code>other</code> Envelope. Has no effect if <code>
     * other</code> is wholly on or within the envelope.
     *
     * @param other the <code>Envelope</code> to expand to include
     */
    public void expandToInclude(ReferencedEnvelope3D other) {
        ensureCompatibleReferenceSystem(other);

        if (other.isNull()) {
            return;
        }
        if (isNull()) {
            super.expandToInclude(other);
            minz = other.getMinZ();
            maxz = other.getMaxZ();
        } else {
            super.expandToInclude(other);
            if (other.minz < minz) {
                minz = other.minz;
            }
            if (other.maxz > maxz) {
                maxz = other.maxz;
            }
        }
    }

    /** Include the provided coordinates, expanding as necessary. */
    @Override
    public void include(double x, double y, double z) {
        expandToInclude(x, y, z);
    }

    /**
     * Initialize the bounding box with another bounding box.
     *
     * @since 2.4
     */
    public void setBounds(final BoundingBox3D bbox) {
        ensureCompatibleReferenceSystem(bbox);
        init(getJTSEnvelope(bbox));
    }

    /**
     * Returns a new bounding box which contains the transformed shape of this bounding box. This is a convenience
     * method that delegate its work to the {@link #transform transform} method.
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
    @Override
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
     */
    @Override
    public ReferencedEnvelope transform(
            final CoordinateReferenceSystem targetCRS, final boolean lenient, final int numPointsForTransformation)
            throws TransformException, FactoryException {
        // TODO: implement 3D behaviour for this method
        // falls back on 2D behaviour (3rd coordinate is preserved!)

        if (crs == null) {
            if (isEmpty()) {
                // We don't have a CRS yet because we are still empty, being empty is
                // something we can represent in the targetCRS
                return new ReferencedEnvelope3D(targetCRS);
            } else {
                // really this is a the code that created this ReferencedEnvelope
                throw new NullPointerException(
                        "Unable to transform referenced envelope, crs has not yet been provided.");
            }
        }
        if (getDimension() != targetCRS.getCoordinateSystem().getDimension()) {
            if (lenient) {
                return JTS.transformTo2D(this, targetCRS, lenient, numPointsForTransformation);
            } else {
                final Object arg0 = crs.getName().getCode();
                final Object arg1 = Integer.valueOf(getDimension());
                final Object arg2 =
                        Integer.valueOf(targetCRS.getCoordinateSystem().getDimension());
                throw new MismatchedDimensionException(
                        MessageFormat.format(ErrorKeys.MISMATCHED_DIMENSION_$3, arg0, arg1, arg2));
            }
        }
        // Gets a first estimation using an algorithm capable to take singularity in account
        // (North pole, South pole, 180ï¿½ longitude). We will expand this initial box later.

        CoordinateOperationFactory coordinateOperationFactory = CRS.getCoordinateOperationFactory(lenient);

        final CoordinateOperation operation = coordinateOperationFactory.createOperation(crs, targetCRS);
        final GeneralBounds transformed = CRS.transform(operation, this);
        transformed.setCoordinateReferenceSystem(targetCRS);

        // Now expands the box using the usual utility methods.

        final ReferencedEnvelope3D target = new ReferencedEnvelope3D(transformed);
        final MathTransform transform = operation.getMathTransform();
        JTS.transform(this, target, transform, numPointsForTransformation);
        // smuggle back third coordinate
        target.expandToInclude(0, 0, this.minz);
        target.expandToInclude(0, 0, this.maxz);

        return target;
    }

    /**
     * Returns a hash value for this envelope. This value need not remain consistent between different implementations
     * of the same class.
     */
    @Override
    public int hashCode() {
        // Algorithm from Effective Java by Joshua Bloch [Jon Aquino]
        int result = super.hashCode();
        result = 37 * result + Coordinate.hashCode(minz);
        result = 37 * result + Coordinate.hashCode(maxz);

        int code = result ^ (int) serialVersionUID;
        if (crs != null) {
            code ^= crs.hashCode();
        }
        return code;
    }

    /** Compares the specified object with this envelope for equality. */
    @Override
    public boolean equals(final Object other) {

        if (!(other instanceof ReferencedEnvelope3D)) {
            return false;
        }
        ReferencedEnvelope3D otherEnvelope = (ReferencedEnvelope3D) other;
        if (isNull()) {
            return otherEnvelope.isNull();
        }
        if (super.equals(other) && minz == otherEnvelope.getMinZ() && maxz == otherEnvelope.getMaxZ()) {
            final CoordinateReferenceSystem otherCRS =
                    (other instanceof ReferencedEnvelope3D) ? ((ReferencedEnvelope3D) other).crs : null;

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
     *   <li>only the first three dimensions of the envelopes are compared
     *   <li>it is assumed that each dimension equates to the same axis for both envelopes
     * </ul>
     *
     * @param other other envelope
     * @param eps a small tolerance factor (e.g. 1.0e-6d) which will be scaled relative to this envlope's width and
     *     height
     * @return true if all bounding coordinates are equal within the set tolerance; false otherwise
     */
    public boolean boundsEquals3D(final Bounds other, double eps) {
        eps *= 0.5 * (getWidth() + getHeight());

        double[] delta = new double[6];
        delta[0] = getMinimum(0) - other.getMinimum(0);
        delta[1] = getMaximum(0) - other.getMaximum(0);
        delta[2] = getMinimum(1) - other.getMinimum(1);
        delta[3] = getMaximum(1) - other.getMaximum(1);
        delta[4] = getMinimum(2) - other.getMinimum(2);
        delta[5] = getMaximum(2) - other.getMaximum(2);

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

    @Override
    public void include(double x, double y) {
        super.expandToInclude(x, y);
    }
}
