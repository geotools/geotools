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

package org.geotools.grid.oblong;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.GridFeatureBuilder;
import org.geotools.referencing.CRS;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A utility class with static methods to create and work with oblong grid elements.
 *
 * @author mbedward
 * @since 2.7
 * @source $URL$
 * @version $Id$
 */
public class Oblongs {

    /**
     * Creates a new {@code Oblong} object.
     *
     * @param minX the min X ordinate
     *
     * @param minY the min Y ordinate
     *
     * @param width the width
     *
     * @param height the height
     *
     * @param crs the coordinate reference system (may be {@code null})
     *
     * @return a new {@code Oblong} object
     *
     * @throws IllegalArgumentException if either {@code width} or {@code height}
     *         are {@code <=} 0
     */
    public static Oblong create(double minX, double minY, double width, double height,
            CoordinateReferenceSystem crs) {
        return new OblongImpl(minX, minY, width, height, crs);
    }

    /**
     * Creates a new grid of oblongs within a bounding rectangle with grid elements
     * represented by simple (ie. undensified) polygons.
     *
     * @param bounds the bounding rectangle
     *
     * @param width oblong width
     *
     * @param height oblong height
     *
     * @param vertexSpacing maximum distance between adjacent vertices in a grid
     *        element; if {@code <= 0} or {@code >= min(width, height) / 2.0} it
     *        is ignored and the polygons will not be densified
     *
     * @param gridBuilder an instance of {@code GridFeatureBuilder}
     *
     * @return a new grid
     *
     * @throws IllegalArgumentException
     *         if bounds is null or empty; or
     *         if either width or height is {@code <=} 0; or
     *         if the {@code CoordinateReferenceSystems}
     *         set for the bounds and the {@code GridFeatureBuilder} are both
     *         non-null but different
     */
    public static SimpleFeatureSource createGrid(ReferencedEnvelope bounds,
            double width, double height, GridFeatureBuilder gridBuilder) {
        return createGrid(bounds, width, height, -1.0, gridBuilder);
    }
    

    /**
     * Creates a new grid of oblongs within a bounding rectangle with grid elements
     * represented by densified polygons (ie. additional vertices added to each
     * edge).
     *
     * @param bounds the bounding rectangle
     *
     * @param width oblong width
     *
     * @param height oblong height
     *
     * @param vertexSpacing maximum distance between adjacent vertices in a grid
     *        element; if {@code <= 0} or {@code >= min(width, height) / 2.0} it
     *        is ignored and the polygons will not be densified
     *
     * @param gridFeatureBuilder an instance of {@code GridFeatureBuilder}
     *
     * @return the vector grid
     *
     * @throws IllegalArgumentException
     *         if bounds is null or empty; or
     *         if either width or height is {@code <=} 0; or
     *         if the {@code CoordinateReferenceSystems}
     *         set for the bounds and the {@code GridFeatureBuilder} are both
     *         non-null but different
     */
    public static SimpleFeatureSource createGrid(
            ReferencedEnvelope bounds,
            double width, double height, 
            double vertexSpacing, GridFeatureBuilder gridFeatureBuilder) {

        if (bounds == null || bounds.isEmpty() || bounds.isNull()) {
            throw new IllegalArgumentException("bounds should not be null or empty");
        }

        if (width <= 0) {
            throw new IllegalArgumentException("width must be greater than 0");
        }

        if (height <= 0) {
            throw new IllegalArgumentException("height must be greater than 0");
        }

        CoordinateReferenceSystem boundsCRS = bounds.getCoordinateReferenceSystem();
        CoordinateReferenceSystem builderCRS = gridFeatureBuilder.getType().getCoordinateReferenceSystem();
        if (boundsCRS != null && builderCRS != null &&
                !CRS.equalsIgnoreMetadata(boundsCRS, builderCRS)) {
            throw new IllegalArgumentException("Different CRS set for bounds and grid feature builder");
        }

        final SimpleFeatureCollection fc = new ListFeatureCollection(gridFeatureBuilder.getType());
        OblongGridBuilder gridBuilder = new OblongGridBuilder(bounds, width, height);
        gridBuilder.buildGrid(fc, gridFeatureBuilder, vertexSpacing);
        return DataUtilities.source(fc);
    }

}
