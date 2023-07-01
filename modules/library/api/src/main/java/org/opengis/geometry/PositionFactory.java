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

import java.util.List;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A factory for managing {@linkplain DirectPosition direct position} creation.
 *
 * <p>This factory will be created for a known {@linkplain CoordinateReferenceSystem Coordinate
 * Reference System} and {@linkplain Precision precision} model.
 *
 * @author Jody Garnett
 * @since GeoAPI 2.1
 */
public interface PositionFactory {
    /**
     * Returns the coordinate reference system in use for all {@linkplain DirectPosition direct
     * positions} to be created through this interface.
     */
    CoordinateReferenceSystem getCoordinateReferenceSystem();

    /**
     * The Precision used used by {@linkplain DirectPosition direct positions} created via this
     * factory.
     *
     * <p>The Precision is used to inform topological operations of the number of significant digits
     * maintained by the {@link DirectPosition} instances. This information both helps operations
     * stop when the correct level of detail is reached, and ensure the result will be valid when
     * rounded to the required precision.
     */
    Precision getPrecision();

    /** Creates a direct position with ordinates set to zero */
    DirectPosition createDirectPosition() throws MismatchedDimensionException;

    /**
     * Creates a direct position at the specified location specified by coordinates.
     *
     * <p>Implementations have the option of taking ownership of the provided coordinate array. You
     * should not attempt to reuse this array after it has been provided to this factory method.
     *
     * @param coordinates Array of ordinates used for this DirectPosition
     * @throws MismatchedDimensionException if the coordinates array length doesn't match the
     *     {@linkplain #getCoordinateReferenceSystem coordinate reference system} dimension.
     */
    DirectPosition createDirectPosition(double[] coordinates) throws MismatchedDimensionException;

    /**
     * Constructs a position from an other position by copying the coordinate values of the
     * position. There will be no further reference to the position instance.
     *
     * @param position A position.
     * @return The position which defines the coordinates for the other position.
     */
    Position createPosition(Position position);

    /**
     * Creates a (possibiliy optimized) list for positions. The list is initially empty. New direct
     * positions can be stored using the {@link List#add} method.
     */
    PointArray createPointArray();

    /**
     * Creates a list for positions initialised from the specified values.
     *
     * <p>Implementations have the option of taking ownership of the provided coordinate array. You
     * should not attempt to reuse this array after it has been provided to this factory method.
     *
     * @param coordinates The coordinates to assign to the list of positions.
     * @param start The first valid value in the {@code coordinates} array.
     * @param length The number of valid values in the {@code coordinates} array.
     * @return The list of positions.
     */
    PointArray createPointArray(double[] coordinates, int start, int length);

    /**
     * Creates a list for positions initialized from the specified values.
     *
     * <p>Implementations have the option of taking ownership of the provided coordinate array. You
     * should not attempt to reuse this array after it has been provided to this factory method.
     *
     * @param coordinates The coordinates to assign to the list of positions.
     * @param start The first valid value in the {@code coordinates} array.
     * @param length The number of valid values in the {@code coordinates} array.
     * @return The list of positions.
     */
    PointArray createPointArray(float[] coordinates, int start, int length);
}
