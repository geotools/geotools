/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid;

import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.hexagon.Hexagons;
import org.geotools.grid.oblong.Oblongs;

/**
 * A utility class to create vector grids with basic attributes. Where grids are
 * only being used for display purposes this is the only class that you have to
 * deal with.
 * <p>
 * For finer control of grid attributes, such as working with a user-supplied
 * SimpleFeatureType, see the {@code Oblongs} and {@code Hexagons} utility
 * classes.
 *
 * @see org.geotools.grid.hexagon.Hexagons
 * @see Oblongs
 *
 * @author mbedward
 * @since 2.7
 *
 * @source $URL$
 * @version $Id$
 */
public class Grids {

    /**
     * Creates a vector grid of square elements. The coordinate reference system is
     * taken from the input bounds. A {@code null} coordinate reference system is
     * permitted.
     * <p>
     * The grid's origin is the minimum X and Y point of the envelope.
     * If the width and/or height of the bounding envelope is not a simple multiple
     * of the requested side length, there will be some unfilled space.
     * <p>
     * Each square in the returned grid is represented by a {@code SimpleFeature}.
     * The feature type has two properties:
     * <ul>
     * <li>element - type Polygon
     * <li>id - type Integer
     * </ul>
     *
     * @param bounds bounds of the grid
     *
     * @param sideLen the side length of grid elements
     *
     * @return the vector grid
     *
     * @throws IllegalArgumentException
     *         if bounds is null or empty; or
     *         if sideLen is {@code <=} 0
     */
    public static SimpleFeatureSource createSquareGrid(
            ReferencedEnvelope bounds, double sideLen) {

        if (bounds == null) {
            throw new IllegalArgumentException("bounds should not be null");
        }

        return Oblongs.createGrid(bounds, sideLen, sideLen,
                new DefaultFeatureBuilder(bounds.getCoordinateReferenceSystem()));
    }

    /**
     * Creates a vector grid of square elements represented by 'densified' polygons.
     * Each polygon has additional vertices added to its edges.
     * This is useful if you plan to display the grid in a projection other
     * than the one that it was created in since the extra vertices will better
     * approximate curves. The density of vertices is controlled by
     * the value of {@code vertexSpacing} which specifies the maximum distance
     * between adjacent vertices. Vertices are added more or less uniformly.
     *
     * The coordinate reference system is taken from the input bounds.
     * A {@code null} coordinate reference system is permitted.
     * <p>
     * The grid's origin is the minimum X and Y point of the envelope.
     * If the width and/or height of the bounding envelope is not a simple multiple
     * of the requested side length, there will be some unfilled space.
     * <p>
     * Each square in the returned grid is represented by a {@code SimpleFeature}.
     * The feature type has two properties:
     * <ul>
     * <li>element - type Polygon
     * <li>id - type Integer
     * </ul>
     *
     * @param bounds bounds of the grid
     *
     * @param sideLen the side length of grid elements
     *
     * @param vertexSpacing maximum distance between adjacent vertices in a grid
     *        element; if {@code <= 0} or {@code >= sideLen / 2.0} it is ignored
     *        and the polygons will not be densified
     *
     * @return the vector grid
     *
     * @throws IllegalArgumentException
     *         if bounds is null or empty; or
     *         if sideLen is {@code <=} 0
     */
    public static SimpleFeatureSource createSquareGrid(
            ReferencedEnvelope bounds, double sideLen, double vertexSpacing) {
        
        if (bounds == null) {
            throw new IllegalArgumentException("bounds should not be null");
        }

        return Oblongs.createGrid(bounds, sideLen, sideLen, vertexSpacing,
                new DefaultFeatureBuilder(bounds.getCoordinateReferenceSystem()));
    }

    /**
     * Creates a vector grid of square elements represented by 'densified' polygons.
     * Each polygon has additional vertices added to its edges.
     * This is useful if you plan to display the grid in a projection other
     * than the one that it was created in since the extra vertices will better
     * approximate curves. The density of vertices is controlled by
     * the value of {@code vertexSpacing} which specifies the maximum distance
     * between adjacent vertices. Vertices are added more or less uniformly.
     *
     * The coordinate reference system is taken from the {@code GridFeatureBuilder}.
     * A {@code null} coordinate reference system is permitted but if both the
     * builder and bounding envelope have non-{@code null} reference systems set
     * they must be the same.
     * <p>
     * The grid's origin is the minimum X and Y point of the envelope.
     * If the width and/or height of the bounding envelope is not a simple multiple
     * of the requested side length, there will be some unfilled space.
     *
     * @param bounds bounds of the grid
     *
     * @param sideLen the side length of grid elements
     *
     * @param vertexSpacing maximum distance between adjacent vertices in a grid
     *        element; if {@code <= 0} or {@code >= sideLen / 2.0} the polygons
     *        will not be densified
     *
     * @param builder the {@code GridFeatureBuilder} used to control feature
     *        creation and the setting of feature attribute values
     *
     * @return the vector grid
     *
     * @throws IllegalArgumentException
     *         if bounds is null or empty; or
     *         if sideLen is {@code <=} 0; or
     *         if builder is null; or
     *         if the {@code CoordinateReferenceSystems}
     *         set for the bounds and the {@code GridFeatureBuilder} are both
     *         non-null but different
     */
    public static SimpleFeatureSource createSquareGrid(
            ReferencedEnvelope bounds, double sideLen, double vertexSpacing,
            GridFeatureBuilder builder) {

        return Oblongs.createGrid(bounds, sideLen, sideLen, vertexSpacing, builder);
    }

    /**
     * Creates a vector grid of hexagonal elements. Hexagon size is expressed as
     * side length. To create hexagons of specified area you can use the static
     * {@linkplain Hexagons#areaToSideLength(double)} method to calculate the
     * equivalent side length.
     * <p>
     * The hexagons created by this method are orientated with a pair of edges
     * parallel to the top and bottom of the bounding envelope
     * ({@linkplain org.geotools.grid.hexagon.Hexagon.Orientation#FLAT}). The
     * bounding rectangle of each hexagon has width {@code sideLen * 2.0} and
     * height {@code sideLen * sqrt(3.0)}.
     * <p>
     * The grid's origin is the minimum X and Y point of the envelope.
     * <p>
     * The feature type of grid features has two properties:
     * <ul>
     * <li>element - type Polygon
     * <li>id - type Integer
     * </ul>
     *
     * @param bounds bounds of the grid
     *
     * @param sideLen the length
     *
     * @return the vector grid
     *
     * @throws IllegalArgumentException
     *         if bounds is null or empty; or
     *         if sideLen is {@code <=} 0
     */
    public static SimpleFeatureSource createHexagonalGrid(
            ReferencedEnvelope bounds, double sideLen) {

        if (bounds == null) {
            throw new IllegalArgumentException("bounds should not be null");
        }

        return Hexagons.createGrid(bounds, sideLen, Orientation.FLAT,
                new DefaultFeatureBuilder(bounds.getCoordinateReferenceSystem()));
    }

    /**
     * Creates a vector grid of hexagonal elements represented by 'densified' polygons.
     * Each polygon has additional vertices added to its edges.
     * This is useful if you plan to display the grid in a projection other
     * than the one that it was created in since the extra vertices will better
     * approximate curves. The density of vertices is controlled by
     * the value of {@code vertexSpacing} which specifies the maximum distance
     * between adjacent vertices. Vertices are added more or less uniformly.
     * <p>
     * Hexagon size is expressed as
     * side length. To create hexagons of specified area you can use the static
     * {@linkplain Hexagons#areaToSideLength(double)} method to calculate the
     * equivalent side length.
     * <p>
     * The hexagons created by this method are orientated with a pair of edges
     * parallel to the top and bottom of the bounding envelope
     * ({@linkplain org.geotools.grid.hexagon.Hexagon.Orientation#FLAT}). The
     * bounding rectangle of each hexagon has width {@code sideLen * 2.0} and
     * height {@code sideLen * sqrt(3.0)}.
     * <p>
     * The grid's origin is the minimum X and Y point of the envelope.
     * <p>
     * The feature type of grid features has two properties:
     * <ul>
     * <li>element - type Polygon
     * <li>id - type Integer
     * </ul>
     *
     * @param bounds bounds of the grid
     *
     * @param sideLen the length
     *
     * @param vertexSpacing maximum distance between adjacent vertices in a grid
     *        element; if {@code <= 0} or {@code >= sideLen / 2.0} the polygons
     *        will not be densified
     *
     * @return the vector grid
     *
     * @throws IllegalArgumentException
     *         if bounds is null or empty; or
     *         if sideLen is {@code <=} 0
     */
    public static SimpleFeatureSource createHexagonalGrid(
            ReferencedEnvelope bounds, double sideLen, double vertexSpacing) {

        if (bounds == null) {
            throw new IllegalArgumentException("bounds should not be null");
        }

        return Hexagons.createGrid(bounds, sideLen, vertexSpacing, Orientation.FLAT,
                new DefaultFeatureBuilder(bounds.getCoordinateReferenceSystem()));
    }

    /**
     * Creates a vector grid of hexagonal elements represented by 'densified' polygons.
     * Each polygon has additional vertices added to its edges.
     * This is useful if you plan to display the grid in a projection other
     * than the one that it was created in since the extra vertices will better
     * approximate curves. The density of vertices is controlled by
     * the value of {@code vertexSpacing} which specifies the maximum distance
     * between adjacent vertices. Vertices are added more or less uniformly.
     * <p>
     * Hexagon size is expressed as
     * side length. To create hexagons of specified area you can use the static
     * {@linkplain Hexagons#areaToSideLength(double)} method to calculate the
     * equivalent side length.
     * <p>
     * The hexagons created by this method are orientated with a pair of edges
     * parallel to the top and bottom of the bounding envelope
     * ({@linkplain org.geotools.grid.hexagon.Hexagon.Orientation#FLAT}). The
     * bounding rectangle of each hexagon has width {@code sideLen * 2.0} and
     * height {@code sideLen * sqrt(3.0)}.
     * <p>
     * The grid's origin is the minimum X and Y point of the envelope.
     * <p>
     * The feature type of grid features has two properties:
     * <ul>
     * <li>element - type Polygon
     * <li>id - type Integer
     * </ul>
     *
     * @param bounds bounds of the grid
     *
     * @param sideLen the length
     *
     * @param vertexSpacing maximum distance between adjacent vertices in a grid
     *        element; if {@code <= 0} or {@code >= sideLen / 2.0} the polygons
     *        will not be densified
     *
     * @param builder the {@code GridFeatureBuilder} used to control feature
     *        creation and the setting of feature attribute values
     *
     * @return the vector grid
     *
     * @throws IllegalArgumentException
     *         if bounds is null or empty; or
     *         if sideLen is {@code <=} 0; or
     *         if builder is null; or
     *         if the {@code CoordinateReferenceSystems}
     *         set for the bounds and the {@code GridFeatureBuilder} are both
     *         non-null but different
     */
    public static SimpleFeatureSource createHexagonalGrid(
            ReferencedEnvelope bounds, double sideLen, double vertexSpacing,
            GridFeatureBuilder builder) {

        if (bounds == null) {
            throw new IllegalArgumentException("bounds should not be null");
        }

        return Hexagons.createGrid(bounds, sideLen, vertexSpacing,
                Orientation.FLAT, builder);
    }

}
