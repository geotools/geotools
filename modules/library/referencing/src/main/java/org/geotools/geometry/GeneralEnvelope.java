/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.io.Serializable;
import java.util.Arrays;
import javax.measure.unit.Unit;
import javax.measure.converter.ConversionException;

import org.opengis.util.Cloneable;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.RangeMeaning;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;

import org.geotools.referencing.CRS;
import org.geotools.resources.Classes;
import org.geotools.util.Utilities;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.geometry.XRectangle2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.metadata.iso.spatial.PixelTranslation;

/**
 * A minimum bounding box or rectangle. Regardless of dimension, an {@code Envelope} can be
 * represented without ambiguity as two {@linkplain DirectPosition direct positions} (coordinate
 * points). To encode an {@code Envelope}, it is sufficient to encode these two points.
 * <p>
 * This particular implementation of {@code Envelope} is said "General" because it uses coordinates
 * of an arbitrary dimension.
 * <p>
 * <strong>Tip:</strong> The metadata package provides a
 * {@link org.opengis.metadata.extent.GeographicBoundingBox}, which can be used as a kind of
 * envelope with a coordinate reference system fixed to WGS 84 (EPSG:4326).
 * 
 * @since 2.0
 * @source $URL:
 *         http://svn.osgeo.org/geotools/branches/2.6.x/modules/library/referencing/src/main/java
 *         /org/geotools/geometry/GeneralEnvelope.java $
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini
 * 
 * @see Envelope2D
 * @see org.geotools.geometry.jts.ReferencedEnvelope
 * @see org.opengis.metadata.extent.GeographicBoundingBox
 */
public class GeneralEnvelope extends AbstractEnvelope implements Cloneable, Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 1752330560227688940L;

    /**
     * Minimum and maximum ordinate values. The first half contains minimum ordinates, while the
     * last half contains maximum ordinates. This layout is convenient for the creation of lower and
     * upper corner direct positions.
     * <p>
     * Consider this reference as final; it is modified by {@link #clone} only.
     */
    private double[] ordinates;

    /**
     * The coordinate reference system, or {@code null}.
     */
    private CoordinateReferenceSystem crs;

    /**
     * Constructs an empty envelope of the specified dimension. All ordinates are initialized to 0
     * and the coordinate reference system is undefined.
     * 
     * @param dimension
     *            The envelope dimension.
     */
    public GeneralEnvelope(final int dimension) {
        ordinates = new double[dimension * 2];
    }

    /**
     * Constructs one-dimensional envelope defined by a range of values.
     * 
     * @param min
     *            The minimal value.
     * @param max
     *            The maximal value.
     */
    public GeneralEnvelope(final double min, final double max) {
        ordinates = new double[] { min, max };
        checkCoordinates(ordinates);
    }

    /**
     * Constructs a envelope defined by two positions.
     * 
     * @param minDP
     *            Minimum ordinate values.
     * @param maxDP
     *            Maximum ordinate values.
     * @throws MismatchedDimensionException
     *             if the two positions don't have the same dimension.
     * @throws IllegalArgumentException
     *             if an ordinate value in the minimum point is not less than or equal to the
     *             corresponding ordinate value in the maximum point.
     */
    public GeneralEnvelope(final double[] minDP, final double[] maxDP)
            throws IllegalArgumentException {
        ensureNonNull("minDP", minDP);
        ensureNonNull("maxDP", maxDP);
        ensureSameDimension(minDP.length, maxDP.length);
        ordinates = new double[minDP.length + maxDP.length];
        System.arraycopy(minDP, 0, ordinates, 0, minDP.length);
        System.arraycopy(maxDP, 0, ordinates, minDP.length, maxDP.length);
        checkCoordinates(ordinates);
    }

    /**
     * Constructs a envelope defined by two positions. The coordinate reference system is inferred
     * from the supplied direct position.
     * 
     * @param minDP
     *            Point containing minimum ordinate values.
     * @param maxDP
     *            Point containing maximum ordinate values.
     * @throws MismatchedDimensionException
     *             if the two positions don't have the same dimension.
     * @throws MismatchedReferenceSystemException
     *             if the two positions don't use the same CRS.
     * @throws IllegalArgumentException
     *             if an ordinate value in the minimum point is not less than or equal to the
     *             corresponding ordinate value in the maximum point.
     */
    public GeneralEnvelope(final GeneralDirectPosition minDP, final GeneralDirectPosition maxDP)
            throws MismatchedReferenceSystemException, IllegalArgumentException {
        // Uncomment next lines if Sun fixes RFE #4093999
        // ensureNonNull("minDP", minDP);
        // ensureNonNull("maxDP", maxDP);
        this(minDP.ordinates, maxDP.ordinates);
        crs = getCoordinateReferenceSystem(minDP, maxDP);
        AbstractDirectPosition.checkCoordinateReferenceSystemDimension(crs, ordinates.length / 2);
    }

    /**
     * Constructs an empty envelope with the specified coordinate reference system. All ordinates
     * are initialized to 0.
     * 
     * @param crs
     *            The coordinate reference system.
     * 
     * @since 2.2
     */
    public GeneralEnvelope(final CoordinateReferenceSystem crs) {
        // Uncomment next line if Sun fixes RFE #4093999
        // ensureNonNull("envelope", envelope);
        this(crs.getCoordinateSystem().getDimension());
        this.crs = crs;
    }

    /**
     * Constructs a new envelope with the same data than the specified envelope.
     * 
     * @param envelope
     *            The envelope to copy.
     */
    public GeneralEnvelope(final Envelope envelope) {
        ensureNonNull("envelope", envelope);
        if (envelope instanceof GeneralEnvelope) {
            final GeneralEnvelope e = (GeneralEnvelope) envelope;
            ordinates = e.ordinates.clone();
            crs = e.crs;
        } else {
            crs = envelope.getCoordinateReferenceSystem();
            final int dimension = envelope.getDimension();
            ordinates = new double[2 * dimension];
            for (int i = 0; i < dimension; i++) {
                ordinates[i] = envelope.getMinimum(i);
                ordinates[i + dimension] = envelope.getMaximum(i);
            }
            checkCoordinates(ordinates);
        }
    }

    /**
     * Constructs a new envelope with the same data than the specified geographic bounding box. The
     * coordinate reference system is set to {@linkplain DefaultGeographicCRS#WGS84 WGS84}.
     * 
     * @param box
     *            The bounding box to copy.
     * 
     * @since 2.4
     */
    public GeneralEnvelope(final GeographicBoundingBox box) {
        ensureNonNull("box", box);
        ordinates = new double[] { box.getWestBoundLongitude(), box.getSouthBoundLatitude(),
                box.getEastBoundLongitude(), box.getNorthBoundLatitude() };
        crs = DefaultGeographicCRS.WGS84;
    }

    /**
     * Constructs two-dimensional envelope defined by a {@link Rectangle2D}. The coordinate
     * reference system is initially undefined.
     * 
     * @param rect
     *            The rectangle to copy.
     */
    public GeneralEnvelope(final Rectangle2D rect) {
        ensureNonNull("rect", rect);
        ordinates = new double[] { rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY() };
        checkCoordinates(ordinates);
    }

    /**
     * Creates an envelope for a grid range transformed to an envelope using the specified math
     * transform. The <cite>grid to CRS</cite> transform should map either the
     * {@linkplain PixelInCell#CELL_CENTER cell center} (as in OGC convention) or
     * {@linkplain PixelInCell#CELL_CORNER cell corner} (as in Java2D/JAI convention) depending on
     * the {@code anchor} value. This constructor creates an envelope containing entirely all pixels
     * on a <cite>best effort</cite> basis - usually accurate for affine transforms.
     * <p>
     * <b>Note:</b> The convention is specified as a {@link PixelInCell} code instead than the more
     * detailled {@link org.opengis.metadata.spatial.PixelOrientation}, because the later is
     * restricted to the two-dimensional case while the former can be used for any number of
     * dimensions.
     * 
     * @param gridRange
     *            The grid range.
     * @param anchor
     *            Whatever grid range coordinates map to pixel center or pixel corner.
     * @param gridToCRS
     *            The transform (usually affine) from grid range to the envelope CRS.
     * @param crs
     *            The envelope CRS, or {@code null} if unknow.
     * 
     * @throws MismatchedDimensionException
     *             If one of the supplied object doesn't have a dimension compatible with the other
     *             objects.
     * @throws IllegalArgumentException
     *             if an argument is illegal for some other reason, including failure to use the
     *             provided math transform.
     * 
     * @since 2.3
     * 
     * @see org.geotools.coverage.grid.GeneralGridEnvelope#GeneralGridEnvelope(Envelope,PixelInCell,boolean)
     */
    public GeneralEnvelope(final GridEnvelope gridRange, final PixelInCell anchor,
            final MathTransform gridToCRS, final CoordinateReferenceSystem crs)
            throws IllegalArgumentException {
        ensureNonNull("gridRange", gridRange);
        ensureNonNull("gridToCRS", gridToCRS);
        final int dimRange = gridRange.getDimension();
        final int dimSource = gridToCRS.getSourceDimensions();
        final int dimTarget = gridToCRS.getTargetDimensions();
        ensureSameDimension(dimRange, dimSource);
        ensureSameDimension(dimRange, dimTarget);
        ordinates = new double[dimSource * 2];
        final double offset = PixelTranslation.getPixelTranslation(anchor) + 0.5;
        for (int i = 0; i < dimSource; i++) {
            /*
             * According OpenGIS specification, GridGeometry maps pixel's center. We want a bounding
             * box for all pixels, not pixel's centers. Offset by 0.5 (use -0.5 for maximum too, not
             * +0.5, since maximum is exclusive).
             * 
             * Note: the offset of 1 after getHigh(i) is because high values are inclusive according
             * ISO specification, while our algorithm and Java usage expect exclusive values.
             */
            setRange(i, gridRange.getLow(i) - offset, gridRange.getHigh(i) - (offset - 1));
        }
        final GeneralEnvelope transformed;
        try {
            transformed = CRS.transform(gridToCRS, this);
        } catch (TransformException exception) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.BAD_TRANSFORM_$1, Classes
                    .getClass(gridToCRS)), exception);
        }
        assert transformed.ordinates.length == this.ordinates.length;
        System.arraycopy(transformed.ordinates, 0, this.ordinates, 0, ordinates.length);
        setCoordinateReferenceSystem(crs);
    }

    /**
     * Makes sure an argument is non-null.
     * 
     * @param name
     *            Argument name.
     * @param object
     *            User argument.
     * @throws InvalidParameterValueException
     *             if {@code object} is null.
     */
    private static void ensureNonNull(final String name, final Object object)
            throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, name));
        }
    }

    /**
     * Makes sure the specified dimensions are identical.
     */
    private static void ensureSameDimension(final int dim1, final int dim2)
            throws MismatchedDimensionException {
        if (dim1 != dim2) {
            throw new MismatchedDimensionException(Errors.format(ErrorKeys.MISMATCHED_DIMENSION_$2,
                    dim1, dim2));
        }
    }

    /**
     * Checks if ordinate values in the minimum point are less than or equal to the corresponding
     * ordinate value in the maximum point.
     * <p>
     * This code will recognize the following exceptions:
     * <ul>
     * <li>ordinates encoding isNil</li>
     * <li>ordinates encoding isEmpty</li>
     * </ul>
     * @throws IllegalArgumentException
     *             if an ordinate value in the minimum point is not less than or equal to the
     *             corresponding ordinate value in the maximum point.
     */
    private static void checkCoordinates(final double[] ordinates) throws IllegalArgumentException {
        if( isNilCoordinates( ordinates )){
            return; // null ordinates are okay            
        }
        if( isEmptyOrdinates(ordinates)){
            return; // empty ordinates are also a valid encoding....
        }
        final int dimension = ordinates.length / 2;
        for (int i = 0; i < dimension; i++) {
            if (!(ordinates[i] <= ordinates[dimension + i])) { // Use '!' in order to catch 'NaN'.
                throw new IllegalArgumentException(Errors.format(
                        ErrorKeys.ILLEGAL_ENVELOPE_ORDINATE_$1, i));
            }
        }
    }

    /**
     * Returns the coordinate reference system in which the coordinates are given.
     * 
     * @return The coordinate reference system, or {@code null}.
     */
    public final CoordinateReferenceSystem getCoordinateReferenceSystem() {
        assert crs == null || crs.getCoordinateSystem().getDimension() == getDimension();
        return crs;
    }

    /**
     * Sets the coordinate reference system in which the coordinate are given. This method
     * <strong>do not</strong> reproject the envelope, and do not check if the envelope is contained
     * in the new domain of validity. The later can be enforced by a call to {@link #normalize}.
     * 
     * @param crs
     *            The new coordinate reference system, or {@code null}.
     * @throws MismatchedDimensionException
     *             if the specified CRS doesn't have the expected number of dimensions.
     */
    public void setCoordinateReferenceSystem(final CoordinateReferenceSystem crs)
            throws MismatchedDimensionException {
        AbstractDirectPosition.checkCoordinateReferenceSystemDimension(crs, getDimension());
        this.crs = crs;
    }

    /**
     * Restricts this envelope to the CS or CRS
     * {@linkplain CoordinateReferenceSystem#getDomainOfValidity domain of validity}. This method
     * performs two steps:
     * 
     * <ol>
     * <li>
     * <p>
     * First, it ensures that the envelope is contained in the {@linkplain CoordinateSystem
     * coordinate system} domain. Out of range ordinates are validated in a way that depends on the
     * {@linkplain CoordinateSystemAxis#getRangeMeaning range meaning}:
     * <ul>
     * <li>If {@linkplain RangeMeaning#EXACT EXACT} (typically <em>latitudes</em> ordinates), values
     * greater than the {@linkplain CoordinateSystemAxis#getMaximumValue maximum value} are replaced
     * by the maximum, and values smaller than the {@linkplain CoordinateSystemAxis#getMinimumValue
     * minimum value} are replaced by the minimum.</li>
     * 
     * <li>If {@linkplain RangeMeaning#WRAPAROUND WRAPAROUND} (typically <em>longitudes</em>
     * ordinates), a multiple of the range (e.g. 360° for longitudes) is added or subtracted. If a
     * value stay out of range after this correction, then the ordinates are set to the full [
     * {@linkplain CoordinateSystemAxis#getMinimumValue minimum} ...
     * {@linkplain CoordinateSystemAxis#getMaximumValue maximum}] range.
     * 
     * <blockquote> <b>Example:</b> [185° ... 190°] of longitude is equivalent to [-175° ...
     * -170°]. But [175° ... 185°] would be equivalent to [175° ... -175°], which is likely to
     * mislead most users of {@link Envelope} since the lower bounds is numerically greater than the
     * upper bounds. Reordering as [-175° ... 175°] would interchange the meaning of what is
     * "inside" and "outside" the envelope. So this implementation conservatively expands the range
     * to [-180° ... 180°] in order to ensure that the validated envelope fully contains the
     * original envelope. </blockquote></li>
     * </ul>
     * </p>
     * </li>
     * <li>
     * <p>
     * Second and only if {@code crsDomain} is {@code true}, the envelope normalized in the previous
     * step is intersected with the CRS {@linkplain CoordinateReferenceSystem#getDomainOfValidity
     * domain of validity}, if any.
     * </p>
     * </li>
     * </ol>
     * 
     * @param crsDomain
     *            {@code true} if the envelope should be restricted to the CRS domain in addition of
     *            the CS domain.
     * @return {@code true} if this envelope has been modified, or {@code false} if no change was
     *         done.
     * 
     * @since 2.5
     */
    public boolean normalize(final boolean crsDomain) {
        boolean changed = false;
        if (crs != null) {
            final int dimension = ordinates.length / 2;
            final CoordinateSystem cs = crs.getCoordinateSystem();
            for (int i = 0; i < dimension; i++) {
                final int j = i + dimension;
                final CoordinateSystemAxis axis = cs.getAxis(i);
                final double minimum = axis.getMinimumValue();
                final double maximum = axis.getMaximumValue();
                final RangeMeaning rm = axis.getRangeMeaning();
                if (RangeMeaning.EXACT.equals(rm)) {
                    if (ordinates[i] < minimum) {
                        ordinates[i] = minimum;
                        changed = true;
                    }
                    if (ordinates[j] > maximum) {
                        ordinates[j] = maximum;
                        changed = true;
                    }
                } else if (RangeMeaning.WRAPAROUND.equals(rm)) {
                    final double length = maximum - minimum;
                    if (length > 0 && length < Double.POSITIVE_INFINITY) {
                        final double offset = Math.floor((ordinates[i] - minimum) / length)
                                * length;
                        if (offset != 0) {
                            ordinates[i] -= offset;
                            ordinates[j] -= offset;
                            changed = true;
                        }
                        if (ordinates[j] > maximum) {
                            ordinates[i] = minimum; // See method Javadoc
                            ordinates[j] = maximum;
                            changed = true;
                        }
                    }
                }
            }
            if (crsDomain) {
                final Envelope domain = CRS.getEnvelope(crs);
                if (domain != null) {
                    final CoordinateReferenceSystem domainCRS = domain
                            .getCoordinateReferenceSystem();
                    if (domainCRS == null) {
                        intersect(domain);
                    } else {
                        /*
                         * The domain may have fewer dimensions than this envelope (typically only
                         * the ones relative to horizontal dimensions). We can rely on directions
                         * for matching axis since CRS.getEnvelope(crs) should have transformed the
                         * domain to this envelope CRS.
                         */
                        final CoordinateSystem domainCS = domainCRS.getCoordinateSystem();
                        final int domainDimension = domainCS.getDimension();
                        for (int i = 0; i < domainDimension; i++) {
                            final double minimum = domain.getMinimum(i);
                            final double maximum = domain.getMaximum(i);
                            final AxisDirection direction = domainCS.getAxis(i).getDirection();
                            for (int j = 0; j < dimension; j++) {
                                if (direction.equals(cs.getAxis(j).getDirection())) {
                                    final int k = j + dimension;
                                    if (ordinates[j] < minimum)
                                        ordinates[j] = minimum;
                                    if (ordinates[k] > maximum)
                                        ordinates[k] = maximum;
                                }
                            }
                        }
                    }
                }
            }
        }
        return changed;
    }

    /**
     * Returns the number of dimensions.
     */
    public final int getDimension() {
        return ordinates.length / 2;
    }

    /**
     * A coordinate position consisting of all the {@linkplain #getMinimum minimal ordinates} for
     * each dimension for all points within the {@code Envelope}.
     * 
     * @return The lower corner.
     */
    @Override
    public DirectPosition getLowerCorner() {
        final int dim = ordinates.length / 2;
        final GeneralDirectPosition position = new GeneralDirectPosition(dim);
        System.arraycopy(ordinates, 0, position.ordinates, 0, dim);
        position.setCoordinateReferenceSystem(crs);
        return position;
    }

    /**
     * A coordinate position consisting of all the {@linkplain #getMaximum maximal ordinates} for
     * each dimension for all points within the {@code Envelope}.
     * 
     * @return The upper corner.
     */
    @Override
    public DirectPosition getUpperCorner() {
        final int dim = ordinates.length / 2;
        final GeneralDirectPosition position = new GeneralDirectPosition(dim);
        System.arraycopy(ordinates, dim, position.ordinates, 0, dim);
        position.setCoordinateReferenceSystem(crs);
        return position;
    }

    /**
     * A coordinate position consisting of all the {@linkplain #getCenter(int) middle ordinates} for
     * each dimension for all points within the {@code Envelope}.
     * 
     * @return The center coordinates.
     * 
     * @since 2.3
     * 
     * @deprecated Renamed as {@link #getMedian(}.
     */
    @Deprecated
    public DirectPosition getCenter() {
        return getMedian();
    }

    /**
     * A coordinate position consisting of all the {@linkplain #getCenter(int) middle ordinates} for
     * each dimension for all points within the {@code Envelope}.
     * 
     * @return The median coordinates.
     * 
     * @since 2.5
     */
    public DirectPosition getMedian() {
        final GeneralDirectPosition position = new GeneralDirectPosition(ordinates.length / 2);
        for (int i = position.ordinates.length; --i >= 0;) {
            position.ordinates[i] = getMedian(i);
        }
        position.setCoordinateReferenceSystem(crs);
        return position;
    }

    /**
     * Creates an exception for an index out of bounds.
     */
    private static IndexOutOfBoundsException indexOutOfBounds(final int dimension) {
        return new IndexOutOfBoundsException(Errors.format(ErrorKeys.INDEX_OUT_OF_BOUNDS_$1,
                dimension));
    }

    /**
     * Returns the minimal ordinate along the specified dimension.
     * 
     * @param dimension
     *            The dimension to query.
     * @return The minimal ordinate value along the given dimension.
     * @throws IndexOutOfBoundsException
     *             If the given index is out of bounds.
     */
    public final double getMinimum(final int dimension) throws IndexOutOfBoundsException {
        if (dimension < ordinates.length / 2) {
            return ordinates[dimension];
        } else {
            throw indexOutOfBounds(dimension);
        }
    }

    /**
     * Returns the maximal ordinate along the specified dimension.
     * 
     * @param dimension
     *            The dimension to query.
     * @return The maximal ordinate value along the given dimension.
     * @throws IndexOutOfBoundsException
     *             If the given index is out of bounds.
     */
    public final double getMaximum(final int dimension) throws IndexOutOfBoundsException {
        if (dimension >= 0) {
            return ordinates[dimension + ordinates.length / 2];
        } else {
            throw indexOutOfBounds(dimension);
        }
    }

    /**
     * Returns the center ordinate along the specified dimension.
     * 
     * @param dimension
     *            The dimension to query.
     * @return The mid ordinate value along the given dimension.
     * 
     * @deprecated Renamed as {@link #getMedian(int)}.
     */
    @Deprecated
    public final double getCenter(final int dimension) {
        return getMedian(dimension);
    }

    /**
     * Returns the median ordinate along the specified dimension. The result should be equals (minus
     * rounding error) to <code>({@linkplain #getMaximum getMaximum}(dimension) -
     * {@linkplain #getMinimum getMinimum}(dimension)) / 2</code>.
     * 
     * @param dimension
     *            The dimension to query.
     * @return The mid ordinate value along the given dimension.
     * @throws IndexOutOfBoundsException
     *             If the given index is out of bounds.
     */
    public final double getMedian(final int dimension) throws IndexOutOfBoundsException {
        return 0.5 * (ordinates[dimension] + ordinates[dimension + ordinates.length / 2]);
    }

    /**
     * Returns the envelope length along the specified dimension. This length is equals to the
     * maximum ordinate minus the minimal ordinate.
     * 
     * @param dimension
     *            The dimension to query.
     * @return The difference along maximal and minimal ordinates in the given dimension.
     * 
     * @deprecated Renamed as {@link #getSpan(int)}.
     */
    @Deprecated
    public final double getLength(final int dimension) {
        return getSpan(dimension);
    }

    /**
     * Returns the envelope span (typically width or height) along the specified dimension. The
     * result should be equals (minus rounding error) to <code>{@linkplain #getMaximum
     * getMaximum}(dimension) - {@linkplain #getMinimum getMinimum}(dimension)</code>.
     * 
     * @param dimension
     *            The dimension to query.
     * @return The difference along maximal and minimal ordinates in the given dimension.
     * @throws IndexOutOfBoundsException
     *             If the given index is out of bounds.
     */
    public final double getSpan(final int dimension) throws IndexOutOfBoundsException {
        return ordinates[dimension + ordinates.length / 2] - ordinates[dimension];
    }

    /**
     * Returns the envelope length along the specified dimension, in terms of the given units.
     * 
     * @param dimension
     *            The dimension to query.
     * @param unit
     *            The unit for the return value.
     * @return The length in terms of the given unit.
     * @throws ConversionException
     *             if the length can't be converted to the specified units.
     * 
     * @since 2.2
     * 
     * @deprecated Renamed as {@link #getSpan(int,Unit)}.
     */
    @Deprecated
    public double getLength(final int dimension, final Unit<?> unit) throws ConversionException {
        return getSpan(dimension, unit);
    }

    /**
     * Returns the envelope span along the specified dimension, in terms of the given units.
     * 
     * @param dimension
     *            The dimension to query.
     * @param unit
     *            The unit for the return value.
     * @return The span in terms of the given unit.
     * @throws IndexOutOfBoundsException
     *             If the given index is out of bounds.
     * @throws ConversionException
     *             if the length can't be converted to the specified units.
     * 
     * @since 2.5
     */
    public double getSpan(final int dimension, final Unit<?> unit)
            throws IndexOutOfBoundsException, ConversionException {
        double value = getSpan(dimension);
        if (crs != null) {
            final Unit<?> source = crs.getCoordinateSystem().getAxis(dimension).getUnit();
            if (source != null) {
                value = source.getConverterTo(unit).convert(value);
            }
        }
        return value;
    }

    /**
     * Sets the envelope's range along the specified dimension.
     * 
     * @param dimension
     *            The dimension to set.
     * @param minimum
     *            The minimum value along the specified dimension.
     * @param maximum
     *            The maximum value along the specified dimension.
     * @throws IndexOutOfBoundsException
     *             If the given index is out of bounds.
     */
    public void setRange(final int dimension, double minimum, double maximum)
            throws IndexOutOfBoundsException {
        if (minimum > maximum) {
            // Make an empty envelope (min == max)
            // while keeping it legal (min <= max).
            minimum = maximum = 0.5 * (minimum + maximum);
        }
        if (dimension >= 0) {
            // An exception will be thrown before any change if 'dimension' is out of range.
            ordinates[dimension + ordinates.length / 2] = maximum;
            ordinates[dimension] = minimum;
        } else {
            throw indexOutOfBounds(dimension);
        }
    }

    /**
     * Sets the envelope to the specified values, which must be the lower corner coordinates
     * followed by upper corner coordinates. The number of arguments provided shall be twice this
     * {@linkplain #getDimension envelope dimension}, and minimum shall not be greater than maximum.
     * <p>
     * <b>Example:</b> (<var>x</var><sub>min</sub>, <var>y</var><sub>min</sub>,
     * <var>z</var><sub>min</sub>, <var>x</var><sub>max</sub>, <var>y</var><sub>max</sub>,
     * <var>z</var><sub>max</sub>)
     * 
     * @param ordinates
     *            The new ordinate values.
     * 
     * @since 2.5
     */
    public void setEnvelope(final double... ordinates) {
        if ((ordinates.length & 1) != 0) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ODD_ARRAY_LENGTH_$1,
                    ordinates.length));
        }
        final int dimension = ordinates.length >>> 1;
        final int check = this.ordinates.length >>> 1;
        if (dimension != check) {
            throw new MismatchedDimensionException(Errors.format(ErrorKeys.MISMATCHED_DIMENSION_$3,
                    "ordinates", dimension, check));
        }
        checkCoordinates(ordinates);
        System.arraycopy(ordinates, 0, this.ordinates, 0, ordinates.length);
    }

    /**
     * Sets this envelope to the same coordinate values than the specified envelope.
     * 
     * @param envelope
     *            The new envelope to copy coordinates from.
     * @throws MismatchedDimensionException
     *             if the specified envelope doesn't have the expected number of dimensions.
     * 
     * @since 2.2
     */
    public void setEnvelope(final GeneralEnvelope envelope) throws MismatchedDimensionException {
        ensureNonNull("envelope", envelope);
        AbstractDirectPosition.ensureDimensionMatch("envelope", envelope.getDimension(),
                getDimension());
        System.arraycopy(envelope.ordinates, 0, ordinates, 0, ordinates.length);
        if (envelope.crs != null) {
            crs = envelope.crs;
            assert crs.getCoordinateSystem().getDimension() == getDimension() : crs;
            assert !envelope.getClass().equals(getClass()) || equals(envelope) : envelope;
        }
    }

    /**
     * Sets the lower corner to {@linkplain Double#NEGATIVE_INFINITY negative infinity} and the
     * upper corner to {@linkplain Double#POSITIVE_INFINITY positive infinity}. The
     * {@linkplain #getCoordinateReferenceSystem coordinate reference system} (if any) stay
     * unchanged.
     * 
     * @since 2.2
     */
    public void setToInfinite() {
        final int mid = ordinates.length / 2;
        Arrays.fill(ordinates, 0, mid, Double.NEGATIVE_INFINITY);
        Arrays.fill(ordinates, mid, ordinates.length, Double.POSITIVE_INFINITY);
        assert isInfinite() : this;
    }

    /**
     * Returns {@code true} if at least one ordinate has an {@linkplain Double#isInfinite infinite}
     * value.
     * 
     * @return {@code true} if this envelope has infinite value.
     * 
     * @since 2.2
     */
    public boolean isInfinite() {
        for (int i = 0; i < ordinates.length; i++) {
            if (Double.isInfinite(ordinates[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets all ordinate values to {@linkplain Double#NaN NaN}. The
     * {@linkplain #getCoordinateReferenceSystem coordinate reference system} (if any) stay
     * unchanged.
     * 
     * @since 2.2
     */
    public void setToNull() {
        Arrays.fill(ordinates, Double.NaN);
        assert isNull() : this;
    }

    /**
     * Returns {@code false} if at least one ordinate value is not {@linkplain Double#NaN NaN}. The
     * {@code isNull()} check is a little bit different than {@link #isEmpty()} since it returns
     * {@code false} for a partially initialized envelope, while {@code isEmpty()} returns {@code
     * false} only after all dimensions have been initialized. More specifically, the following
     * rules apply:
     * <p>
     * <ul>
     * <li>If <code>isNull() == true</code>, then <code>{@linkplain #isEmpty()} == true</code></li>
     * <li>If <code>{@linkplain #isEmpty()} == false</code>, then <code>isNull() == false</code></li>
     * <li>The converse of the above-cited rules are not always true.</li>
     * </ul>
     * 
     * @return {@code true} if this envelope has NaN values.
     * 
     * @since 2.2
     */
    public boolean isNull() {
        if (!isNilCoordinates(ordinates)) {
            return false;
        }
        assert isEmpty() : this;
        return true;
    }

    /**
     * Check if the ordinates indicate a "nil" envelope.
     * @param ordinates
     * @return
     * @throws IllegalArgumentException
     */
    private static boolean isNilCoordinates(final double[] ordinates)
            throws IllegalArgumentException {
        for (int i = 0; i < ordinates.length; i++) {
            if (!Double.isNaN(ordinates[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether or not this envelope is empty. An envelope is non-empty only if it has at
     * least one {@linkplain #getDimension dimension}, and the {@linkplain #getLength length} is
     * greater than 0 along all dimensions. Note that a non-empty envelope is always non-
     * {@linkplain #isNull null}, but the converse is not always true.
     * 
     * @return {@code true} if this envelope is empty.
     */
    public boolean isEmpty() {
        if( isEmptyOrdinates(ordinates)){
            return true;
        }
        assert !isNull() : this; // JG I worry that this is circular
        return false;
    }
    /**
     * Static method used to recognize an empty encoding of ordindates
     * @param ordinates
     * @return true of the ordinates indicate an empty envelope
     * @see #isEmpty()
     */
    private static boolean isEmptyOrdinates( double ordinates[] ){
        final int dimension = ordinates.length / 2;
        if (dimension == 0) {
            return true;
        }
        for (int i = 0; i < dimension; i++) {
            if (!(ordinates[i] < ordinates[i + dimension])) { // Use '!' in order to catch NaN
                return true;
            }
        }
        return false;
    }
    /**
     * Returns {@code true} if at least one of the specified CRS is null, or both CRS are equals.
     * This special processing for {@code null} values is different from the usual contract of an
     * {@code equals} method, but allow to handle the case where the CRS is unknown.
     */
    private static boolean equalsIgnoreMetadata(final CoordinateReferenceSystem crs1,
            final CoordinateReferenceSystem crs2) {
        return crs1 == null || crs2 == null || CRS.equalsIgnoreMetadata(crs1, crs2);
    }

    /**
     * Adds a point to this envelope. The resulting envelope is the smallest envelope that contains
     * both the original envelope and the specified point. After adding a point, a call to
     * {@link #contains} with the added point as an argument will return {@code true}, except if one
     * of the point's ordinates was {@link Double#NaN} (in which case the corresponding ordinate
     * have been ignored).
     * <p>
     * This method assumes that the specified point uses the same CRS than this envelope. For
     * performance reason, it will no be verified unless J2SE assertions are enabled.
     * 
     * @param position
     *            The point to add.
     * @throws MismatchedDimensionException
     *             if the specified point doesn't have the expected dimension.
     */
    public void add(final DirectPosition position) throws MismatchedDimensionException {
        ensureNonNull("position", position);
        final int dim = ordinates.length / 2;
        AbstractDirectPosition.ensureDimensionMatch("position", position.getDimension(), dim);
        assert equalsIgnoreMetadata(crs, position.getCoordinateReferenceSystem()) : position;
        for (int i = 0; i < dim; i++) {
            final double value = position.getOrdinate(i);
            if (value < ordinates[i])
                ordinates[i] = value;
            if (value > ordinates[i + dim])
                ordinates[i + dim] = value;
        }
        assert isEmpty() || contains(position);
    }

    /**
     * Adds an envelope object to this envelope. The resulting envelope is the union of the two
     * {@code Envelope} objects.
     * <p>
     * This method assumes that the specified envelope uses the same CRS than this envelope. For
     * performance reason, it will no be verified unless J2SE assertions are enabled.
     * 
     * @param envelope
     *            the {@code Envelope} to add to this envelope.
     * @throws MismatchedDimensionException
     *             if the specified envelope doesn't have the expected dimension.
     */
    public void add(final Envelope envelope) throws MismatchedDimensionException {
        ensureNonNull("envelope", envelope);
        final int dim = ordinates.length / 2;
        AbstractDirectPosition.ensureDimensionMatch("envelope", envelope.getDimension(), dim);
        assert equalsIgnoreMetadata(crs, envelope.getCoordinateReferenceSystem()) : envelope;
        for (int i = 0; i < dim; i++) {
            final double min = envelope.getMinimum(i);
            final double max = envelope.getMaximum(i);
            if (min < ordinates[i])
                ordinates[i] = min;
            if (max > ordinates[i + dim])
                ordinates[i + dim] = max;
        }
        assert isEmpty() || contains(envelope, true);
    }

    /**
     * Tests if a specified coordinate is inside the boundary of this envelope.
     * <p>
     * This method assumes that the specified point uses the same CRS than this envelope. For
     * performance reason, it will no be verified unless J2SE assertions are enabled.
     * 
     * @param position
     *            The point to text.
     * @return {@code true} if the specified coordinates are inside the boundary of this envelope;
     *         {@code false} otherwise.
     * @throws MismatchedDimensionException
     *             if the specified point doesn't have the expected dimension.
     */
    public boolean contains(final DirectPosition position) throws MismatchedDimensionException {
        ensureNonNull("position", position);
        final int dim = ordinates.length / 2;
        AbstractDirectPosition.ensureDimensionMatch("point", position.getDimension(), dim);
        assert equalsIgnoreMetadata(crs, position.getCoordinateReferenceSystem()) : position;
        for (int i = 0; i < dim; i++) {
            final double value = position.getOrdinate(i);
            if (!(value >= ordinates[i]))
                return false;
            if (!(value <= ordinates[i + dim]))
                return false;
            // Use '!' in order to take 'NaN' in account.
        }
        return true;
    }

    /**
     * Returns {@code true} if this envelope completly encloses the specified envelope. If one or
     * more edges from the specified envelope coincide with an edge from this envelope, then this
     * method returns {@code true} only if {@code edgesInclusive} is {@code true}.
     * <p>
     * This method assumes that the specified envelope uses the same CRS than this envelope. For
     * performance reason, it will no be verified unless J2SE assertions are enabled.
     * 
     * @param envelope
     *            The envelope to test for inclusion.
     * @param edgesInclusive
     *            {@code true} if this envelope edges are inclusive.
     * @return {@code true} if this envelope completly encloses the specified one.
     * @throws MismatchedDimensionException
     *             if the specified envelope doesn't have the expected dimension.
     * 
     * @see #intersects(Envelope, boolean)
     * @see #equals(Envelope, double)
     * 
     * @since 2.2
     */
    public boolean contains(final Envelope envelope, final boolean edgesInclusive)
            throws MismatchedDimensionException {
        ensureNonNull("envelope", envelope);
        final int dim = ordinates.length / 2;
        AbstractDirectPosition.ensureDimensionMatch("envelope", envelope.getDimension(), dim);
        assert equalsIgnoreMetadata(crs, envelope.getCoordinateReferenceSystem()) : envelope;
        for (int i = 0; i < dim; i++) {
            double inner = envelope.getMinimum(i);
            double outer = ordinates[i];
            if (!(edgesInclusive ? inner >= outer : inner > outer)) { // ! is for catching NaN.
                return false;
            }
            inner = envelope.getMaximum(i);
            outer = ordinates[i + dim];
            if (!(edgesInclusive ? inner <= outer : inner < outer)) { // ! is for catching NaN.
                return false;
            }
        }
        assert intersects(envelope, edgesInclusive);
        return true;
    }

    /**
     * Returns {@code true} if this envelope intersects the specified envelope. If one or more edges
     * from the specified envelope coincide with an edge from this envelope, then this method
     * returns {@code true} only if {@code edgesInclusive} is {@code true}.
     * <p>
     * This method assumes that the specified envelope uses the same CRS than this envelope. For
     * performance reason, it will no be verified unless J2SE assertions are enabled.
     * 
     * @param envelope
     *            The envelope to test for intersection.
     * @param edgesInclusive
     *            {@code true} if this envelope edges are inclusive.
     * @return {@code true} if this envelope intersects the specified one.
     * @throws MismatchedDimensionException
     *             if the specified envelope doesn't have the expected dimension.
     * 
     * @see #contains(Envelope, boolean)
     * @see #equals(Envelope, double)
     * 
     * @since 2.2
     */
    public boolean intersects(final Envelope envelope, final boolean edgesInclusive)
            throws MismatchedDimensionException {
        ensureNonNull("envelope", envelope);
        final int dim = ordinates.length / 2;
        AbstractDirectPosition.ensureDimensionMatch("envelope", envelope.getDimension(), dim);
        assert equalsIgnoreMetadata(crs, envelope.getCoordinateReferenceSystem()) : envelope;
        for (int i = 0; i < dim; i++) {
            double inner = envelope.getMaximum(i);
            double outer = ordinates[i];
            if (!(edgesInclusive ? inner >= outer : inner > outer)) { // ! is for catching NaN.
                return false;
            }
            inner = envelope.getMinimum(i);
            outer = ordinates[i + dim];
            if (!(edgesInclusive ? inner <= outer : inner < outer)) { // ! is for catching NaN.
                return false;
            }
        }
        return true;
    }

    /**
     * Sets this envelope to the intersection if this envelope with the specified one.
     * <p>
     * This method assumes that the specified envelope uses the same CRS than this envelope. For
     * performance reason, it will no be verified unless J2SE assertions are enabled.
     * 
     * @param envelope
     *            the {@code Envelope} to intersect to this envelope.
     * @throws MismatchedDimensionException
     *             if the specified envelope doesn't have the expected dimension.
     */
    public void intersect(final Envelope envelope) throws MismatchedDimensionException {
        ensureNonNull("envelope", envelope);
        final int dim = ordinates.length / 2;
        AbstractDirectPosition.ensureDimensionMatch("envelope", envelope.getDimension(), dim);
        assert equalsIgnoreMetadata(crs, envelope.getCoordinateReferenceSystem()) : envelope;
        for (int i = 0; i < dim; i++) {
            double min = Math.max(ordinates[i], envelope.getMinimum(i));
            double max = Math.min(ordinates[i + dim], envelope.getMaximum(i));
            if (min > max) {
                // Make an empty envelope (min==max)
                // while keeping it legal (min<=max).
                min = max = 0.5 * (min + max);
            }
            ordinates[i] = min;
            ordinates[i + dim] = max;
        }
    }

    /**
     * Returns a new envelope that encompass only some dimensions of this envelope. This method copy
     * this envelope's ordinates into a new envelope, beginning at dimension <code>lower</code> and
     * extending to dimension <code>upper-1</code>. Thus the dimension of the subenvelope is
     * <code>upper-lower</code>.
     * 
     * @param lower
     *            The first dimension to copy, inclusive.
     * @param upper
     *            The last dimension to copy, exclusive.
     * @return The subenvelope.
     * @throws IndexOutOfBoundsException
     *             if an index is out of bounds.
     */
    public GeneralEnvelope getSubEnvelope(final int lower, final int upper)
            throws IndexOutOfBoundsException {
        final int curDim = ordinates.length / 2;
        final int newDim = upper - lower;
        if (lower < 0 || lower > curDim) {
            throw new IndexOutOfBoundsException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,
                    "lower", lower));
        }
        if (newDim < 0 || upper > curDim) {
            throw new IndexOutOfBoundsException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,
                    "upper", upper));
        }
        final GeneralEnvelope envelope = new GeneralEnvelope(newDim);
        System.arraycopy(ordinates, lower, envelope.ordinates, 0, newDim);
        System.arraycopy(ordinates, lower + curDim, envelope.ordinates, newDim, newDim);
        return envelope;
    }

    /**
     * Returns a new envelope with the same values than this envelope minus the specified range of
     * dimensions.
     * 
     * @param lower
     *            The first dimension to omit, inclusive.
     * @param upper
     *            The last dimension to omit, exclusive.
     * @return The subenvelope.
     * @throws IndexOutOfBoundsException
     *             if an index is out of bounds.
     */
    public GeneralEnvelope getReducedEnvelope(final int lower, final int upper)
            throws IndexOutOfBoundsException {
        final int curDim = ordinates.length / 2;
        final int rmvDim = upper - lower;
        if (lower < 0 || lower > curDim) {
            throw new IndexOutOfBoundsException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,
                    "lower", lower));
        }
        if (rmvDim < 0 || upper > curDim) {
            throw new IndexOutOfBoundsException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,
                    "upper", upper));
        }
        final GeneralEnvelope envelope = new GeneralEnvelope(curDim - rmvDim);
        System.arraycopy(ordinates, 0, envelope.ordinates, 0, lower);
        System.arraycopy(ordinates, lower, envelope.ordinates, upper, curDim - upper);
        return envelope;
    }

    /**
     * Returns a {@link Rectangle2D} with the same bounds as this {@code Envelope}. This is a
     * convenience method for interoperability with Java2D.
     * 
     * @return This envelope as a twp-dimensional rectangle.
     * @throws IllegalStateException
     *             if this envelope is not two-dimensional.
     */
    public Rectangle2D toRectangle2D() throws IllegalStateException {
        if (ordinates.length == 4) {
            return XRectangle2D.createFromExtremums(ordinates[0], ordinates[1], ordinates[2],
                    ordinates[3]);
        } else {
            throw new IllegalStateException(Errors.format(ErrorKeys.NOT_TWO_DIMENSIONAL_$1,
                    getDimension()));
        }
    }

    /**
     * Returns a hash value for this envelope.
     */
    @Override
    public int hashCode() {
        int code = Arrays.hashCode(ordinates);
        if (crs != null) {
            code += crs.hashCode();
        }
        assert code == super.hashCode();
        return code;
    }

    /**
     * Compares the specified object with this envelope for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object != null && object.getClass().equals(getClass())) {
            final GeneralEnvelope that = (GeneralEnvelope) object;
            return Arrays.equals(this.ordinates, that.ordinates)
                    && Utilities.equals(this.crs, that.crs);
        }
        return false;
    }

    /**
     * Compares to the specified envelope for equality up to the specified tolerance value. The
     * tolerance value {@code eps} can be either relative to the {@linkplain #getLength envelope
     * length} along each dimension or can be an absolute value (as for example some ground
     * resolution of a {@linkplain org.opengis.coverage.grid.GridCoverage grid coverage}).
     * <p>
     * If {@code relativeToLength} is set to {@code true}, the actual tolerance value for a given
     * dimension <var>i</var> is {@code eps}&times;{@code length} where {@code length} is the
     * maximum of {@linkplain #getLength this envelope length} and the specified envelope length
     * along dimension <var>i</var>.
     * <p>
     * If {@code relativeToLength} is set to {@code false}, the actual tolerance value for a given
     * dimension <var>i</var> is {@code eps}.
     * <p>
     * Relative tolerance value (as opposed to absolute tolerance value) help to workaround the fact
     * that tolerance value are CRS dependent. For example the tolerance value need to be smaller
     * for geographic CRS than for UTM projections, because the former typically has a range of -180
     * to 180° while the later can have a range of thousands of meters.
     * <p>
     * This method assumes that the specified envelope uses the same CRS than this envelope. For
     * performance reason, it will no be verified unless J2SE assertions are enabled.
     * 
     * @param envelope
     *            The envelope to compare with.
     * @param eps
     *            The tolerance value to use for numerical comparaisons.
     * @param epsIsRelative
     *            {@code true} if the tolerance value should be relative to axis length, or {@code
     *            false} if it is an absolute value.
     * @return {@code true} if the given object is equals to this envelope up to the given tolerance
     *         value.
     * 
     * @see #contains(Envelope, boolean)
     * @see #intersects(Envelope, boolean)
     * 
     * @since 2.4
     */
    public boolean equals(final Envelope envelope, final double eps, final boolean epsIsRelative) {
        ensureNonNull("envelope", envelope);
        final int dimension = getDimension();
        if (envelope.getDimension() != dimension) {
            return false;
        }
        assert equalsIgnoreMetadata(crs, envelope.getCoordinateReferenceSystem()) : envelope;
        for (int i = 0; i < dimension; i++) {
            double epsilon;
            if (epsIsRelative) {
                epsilon = Math.max(getSpan(i), envelope.getSpan(i));
                epsilon = (epsilon > 0 && epsilon < Double.POSITIVE_INFINITY) ? epsilon * eps : eps;
            } else {
                epsilon = eps;
            }
            // Comparaison below uses '!' in order to catch NaN values.
            if (!(Math.abs(getMinimum(i) - envelope.getMinimum(i)) <= epsilon && Math
                    .abs(getMaximum(i) - envelope.getMaximum(i)) <= epsilon)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a deep copy of this envelope.
     * 
     * @return A clone of this envelope.
     */
    @Override
    public GeneralEnvelope clone() {
        try {
            GeneralEnvelope e = (GeneralEnvelope) super.clone();
            e.ordinates = e.ordinates.clone();
            return e;
        } catch (CloneNotSupportedException exception) {
            // Should not happen, since we are cloneable.
            throw new AssertionError(exception);
        }
    }
}
