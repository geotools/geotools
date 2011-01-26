/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry;

import java.awt.geom.Rectangle2D; // Used in @see javadoc tags
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A minimum bounding box or rectangle. Regardless of dimension, an {@code Envelope} can
 * be represented without ambiguity as two direct positions (coordinate points). To encode an
 * {@code Envelope}, it is sufficient to encode these two points. This is consistent with
 * all of the data types in this specification, their state is represented by their publicly
 * accessible attributes.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see org.opengis.coverage.grid.GridEnvelope
 */
@UML(identifier="GM_Envelope", specification=ISO_19107)
public interface Envelope {
    /**
     * Returns the envelope coordinate reference system, or {@code null} if unknown.
     * If non-null, it shall be the same as {@linkplain #getLowerCorner lower corner}
     * and {@linkplain #getUpperCorner upper corner} CRS.
     *
     * @return The envelope CRS, or {@code null} if unknown.
     *
     * @since GeoAPI 2.1
     */
    @Extension
    CoordinateReferenceSystem getCoordinateReferenceSystem();

    /**
     * The length of coordinate sequence (the number of entries) in this envelope. Mandatory
     * even when the {@linkplain #getCoordinateReferenceSystem coordinate reference system}
     * is unknown.
     *
     * @return The dimensionality of this envelope.
     *
     * @since GeoAPI 2.0
     */
    @Extension
    int getDimension();

    /**
     * A coordinate position consisting of all the minimal ordinates for each
     * dimension for all points within the {@code Envelope}.
     *
     * @return The lower corner.
     */
    @UML(identifier="lowerCorner", obligation=MANDATORY, specification=ISO_19107)
    DirectPosition getLowerCorner();

    /**
     * A coordinate position consisting of all the maximal ordinates for each
     * dimension for all points within the {@code Envelope}.
     *
     * @return The upper corner.
     */
    @UML(identifier="upperCorner", obligation=MANDATORY, specification=ISO_19107)
    DirectPosition getUpperCorner();

    /**
     * Returns the minimal ordinate along the specified dimension. This is a shortcut for
     * the following without the cost of creating a temporary {@link DirectPosition} object:
     *
     * <blockquote><code>
     * {@linkplain #getLowerCorner}.{@linkplain DirectPosition#getOrdinate getOrdinate}(dimension)
     * </code></blockquote>
     *
     * @param  dimension The dimension for which to obtain the ordinate value.
     * @return The minimal ordinate at the given dimension.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater
     *         than the {@linkplain #getDimension envelope dimension}.
     *
     * @see Rectangle2D#getMinX
     * @see Rectangle2D#getMinY
     *
     * @since GeoAPI 2.0
     */
    @Extension
    double getMinimum(int dimension) throws IndexOutOfBoundsException;

    /**
     * Returns the maximal ordinate along the specified dimension. This is a shortcut for
     * the following without the cost of creating a temporary {@link DirectPosition} object:
     *
     * <blockquote><code>
     * {@linkplain #getUpperCorner}.{@linkplain DirectPosition#getOrdinate getOrdinate}(dimension)
     * </code></blockquote>
     *
     * @param  dimension The dimension for which to obtain the ordinate value.
     * @return The maximal ordinate at the given dimension.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater
     *         than the {@linkplain #getDimension envelope dimension}.
     *
     * @see Rectangle2D#getMaxX
     * @see Rectangle2D#getMaxY
     *
     * @since GeoAPI 2.0
     */
    @Extension
    double getMaximum(int dimension) throws IndexOutOfBoundsException;

    /**
     * Returns the center ordinate along the specified dimension.
     *
     * @param  dimension The dimension for which to obtain the ordinate value.
     * @return The center ordinate at the given dimension.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater
     *         than the {@linkplain #getDimension envelope dimension}.
     *
     * @see Rectangle2D#getCenterX
     * @see Rectangle2D#getCenterY
     *
     * @since GeoAPI 2.0
     *
     * @deprecated Renamed as {@link #getMedian}.
     */
    @Extension
    @Deprecated
    double getCenter(int dimension) throws IndexOutOfBoundsException;

    /**
     * Returns the median ordinate along the specified dimension. The result should be equals
     * (minus rounding error) to:
     *
     * <blockquote><code>
     * ({@linkplain #getMinimum getMinimum}(dimension) + {@linkplain #getMaximum getMaximum}(dimension)) / 2
     * </code></blockquote>
     *
     * @param  dimension The dimension for which to obtain the ordinate value.
     * @return The median ordinate at the given dimension.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater
     *         than the {@linkplain #getDimension envelope dimension}.
     *
     * @see Rectangle2D#getCenterX
     * @see Rectangle2D#getCenterY
     *
     * @since GeoAPI 2.2
     */
    @Extension
    double getMedian(int dimension) throws IndexOutOfBoundsException;

    /**
     * Returns the envelope length along the specified dimension.
     * This length is equals to the {@linkplain #getMaximum maximum ordinate}
     * minus the {@linkplain #getMinimum minimal ordinate}.
     *
     * @param  dimension The dimension for which to obtain the ordinate value.
     * @return The length at the given dimension.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater
     *         than the {@linkplain #getDimension envelope dimension}.
     *
     * @see Rectangle2D#getWidth
     * @see Rectangle2D#getHeight
     *
     * @since GeoAPI 2.0
     *
     * @deprecated Renamed as {@link #getSpan}.
     */
    @Extension
    @Deprecated
    double getLength(int dimension) throws IndexOutOfBoundsException;

    /**
     * Returns the envelope span (typically width or height) along the specified dimension.
     * The result should be equals (minus rounding error) to:
     *
     * <blockquote><code>
     * {@linkplain #getMaximum getMaximum}(dimension) - {@linkplain #getMinimum getMinimum}(dimension)
     * </code></blockquote>
     *
     * @param  dimension The dimension for which to obtain the ordinate value.
     * @return The span (typically width or height) at the given dimension.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater
     *         than the {@linkplain #getDimension envelope dimension}.
     *
     * @see Rectangle2D#getWidth
     * @see Rectangle2D#getHeight
     *
     * @since GeoAPI 2.2
     */
    @Extension
    double getSpan(int dimension) throws IndexOutOfBoundsException;
}
