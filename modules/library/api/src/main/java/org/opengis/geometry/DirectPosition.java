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

import static org.opengis.annotation.Obligation.MANDATORY;
import static org.opengis.annotation.Specification.ISO_19107;

import org.opengis.annotation.UML;
import org.opengis.geometry.coordinate.Position;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Holds the coordinates for a position within some coordinate reference system. Since {@code
 * DirectPosition}s, as data types, will often be included in larger objects (such as {@linkplain
 * org.opengis.geometry.Geometry geometries}) that have references to {@linkplain
 * CoordinateReferenceSystem coordinate reference system}, the {@link #getCoordinateReferenceSystem}
 * method may returns {@code null} if this particular {@code DirectPosition} is included in a larger
 * object with such a reference to a {@linkplain CoordinateReferenceSystem coordinate reference
 * system}. In this case, the cordinate reference system is implicitly assumed to take on the value
 * of the containing object's {@linkplain CoordinateReferenceSystem coordinate reference system}.
 *
 * <p><b>Note:</b> this interface does not extends {@link org.opengis.util.Cloneable} on purpose,
 * since {@code DirectPosition} implementations are most likely to be backed by references to
 * internal structures of the geometry containing this position. A direct position may or may not be
 * cloneable at implementor choice.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier = "DirectPosition", specification = ISO_19107)
public interface DirectPosition extends Position {
    /**
     * The coordinate reference system in which the coordinate is given. May be {@code null} if this
     * particular {@code DirectPosition} is included in a larger object with such a reference to a
     * {@linkplain CoordinateReferenceSystem coordinate reference system}. In this case, the
     * cordinate reference system is implicitly assumed to take on the value of the containing
     * object's {@linkplain CoordinateReferenceSystem coordinate reference system}.
     *
     * @return The coordinate reference system, or {@code null}.
     */
    @UML(
            identifier = "coordinateReferenceSystem",
            obligation = MANDATORY,
            specification = ISO_19107)
    CoordinateReferenceSystem getCoordinateReferenceSystem();

    /**
     * The length of coordinate sequence (the number of entries). This is determined by the
     * {@linkplain #getCoordinateReferenceSystem() coordinate reference system}.
     *
     * @return The dimensionality of this position.
     */
    @UML(identifier = "dimension", obligation = MANDATORY, specification = ISO_19107)
    int getDimension();

    /**
     * A <b>copy</b> of the ordinates presented as an array of double values. Please note that this
     * is only a copy (the real values may be stored in another format) so changes to the returned
     * array will not affect the source DirectPosition.
     *
     * <blockquote>
     *
     * <pre>
     * final int dim = position.{@linkplain #getDimension getDimension}();
     * for (int i=0; i&lt;dim; i++) {
     *     position.{@linkplain #getOrdinate getOrdinate}(i); // no copy overhead
     * }
     * </pre>
     *
     * </blockquote>
     *
     * To manipulate ordinates, the following idiom can be used:
     *
     * <blockquote>
     *
     * <pre>
     * position.{@linkplain #setOrdinate setOrdinate}(i, value); // edit in place
     * </pre>
     *
     * </blockquote>
     *
     * There are a couple reasons for requerying a copy:
     *
     * <p>
     *
     * <ul>
     *   <li>We want an array of coordinates with the intend to modify it for computation purpose
     *       (without modifying the original {@code DirectPosition}), or we want to protect the
     *       array from future {@code DirectPosition} changes.
     *   <li>If {@code DirectPosition.getOrdinates()} is garanteed to not return the backing array,
     *       then we can work directly on this array. If we don't have this garantee, then we must
     *       conservatively clone the array in every cases.
     *   <li>Cloning the returned array is useless if the implementation cloned the array or was
     *       forced to returns a new array anyway (for example because the coordinates were computed
     *       on the fly)
     * </ul>
     *
     * <p>Precedence is given to data integrity over {@code getOrdinates()} performance. Performance
     * concern can be avoided with usage of {@link #getOrdinate(int)}.
     *
     * @return A copy of the coordinates. Changes in the returned array will not be reflected back
     *     in this {@code DirectPosition} object.
     */
    @UML(identifier = "coordinate", obligation = MANDATORY, specification = ISO_19107)
    double[] getCoordinate();

    /**
     * Returns the ordinate at the specified dimension.
     *
     * @param dimension The dimension in the range 0 to {@linkplain #getDimension dimension}-1.
     * @return The coordinate at the specified dimension.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater than
     *     the {@linkplain #getDimension envelope dimension}.
     */
    double getOrdinate(int dimension) throws IndexOutOfBoundsException;

    /**
     * Sets the ordinate value along the specified dimension.
     *
     * @param dimension the dimension for the ordinate of interest.
     * @param value the ordinate value of interest.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater than
     *     the {@linkplain #getDimension envelope dimension}.
     * @throws UnsupportedOperationException if this direct position is immutable.
     */
    void setOrdinate(int dimension, double value)
            throws IndexOutOfBoundsException, UnsupportedOperationException;

    /**
     * Compares this direct position with the specified object for equality. Two direct positions
     * are considered equal if the following conditions are meet:
     *
     * <p>
     *
     * <ul>
     *   <li>{@code object} is non-null and is an instance of {@code DirectPosition}.
     *   <li>Both direct positions have the same {@linkplain #getDimension number of dimension}.
     *   <li>Both direct positions have the same or equal {@linkplain #getCoordinateReferenceSystem
     *       coordinate reference system}.
     *   <li>For all dimension <var>i</var>, the {@linkplain #getOrdinate ordinate value} of both
     *       direct positions at that dimension are equals in the sense of {@link Double#equals}. In
     *       other words, <code>{@linkplain java.util.Arrays#equals(double[],double[])
     *       Arrays.equals}({@linkplain #getCoordinate()}, object.getCoordinate())</code> returns
     *       {@code true}.
     * </ul>
     *
     * @param object The object to compare with this direct position for equality.
     * @return {@code true} if the given object is equals to this direct position.
     * @since GeoAPI 2.1
     */
    @Override
    /// @Override
    boolean equals(Object object);

    /**
     * Returns a hash code value for this direct position. This method should returns the same value
     * as: <code>{@linkplain java.util.Arrays.hashCode(double[]) Arrays.hashCode}({@linkplain
     * #getCoordinate()}) + {@linkplain #getCoordinateReferenceSystem()}.hashCode()</code> where the
     * right hand side of the addition is omitted if the coordinate reference system is null.
     *
     * @return A hash code value for this direct position.
     * @since GeoAPI 2.1
     */
    @Override
    /// @Override
    int hashCode();
}
