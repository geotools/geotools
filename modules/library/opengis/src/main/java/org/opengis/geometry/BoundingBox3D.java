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

import org.opengis.referencing.cs.AxisDirection;            // For javadoc
import org.opengis.metadata.extent.GeographicBoundingBox;   // For javadoc
import org.opengis.referencing.crs.GeographicCRS;           // For javadoc
import org.opengis.referencing.crs.CRSAuthorityFactory;     // For javadoc
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.opengis.annotation.Extension;


/**
 * Represents a three-dimensional {@linkplain Envelope envelope}. Extends  {@linkplain BoundingBox BoundingBox} to support
 * the third dimension.
 * This interface combines the ideas of {@link GeographicBoundingBox} with
 * those of {@link Envelope}. It provides convenience methods to assist
 * in accessing the formal properties of this object.
 * <p>
 * This object contains no additional information beyond that provided
 * by {@link Envelope}.
 *
 * @author Niels Charlier
 *
 *
 * @source $URL$
 */
@Extension
public interface BoundingBox3D extends BoundingBox {
    
    /**
     * Provides the minimum ordinate along the third axis.
     * This is equivalent to <code>{@linkplain #getMinimum getMinimum}(2)</code>.
     *
     * @return The minimum ordinate along the third axis.
     */
    double getMinZ();

    /**
     * Provides the maximum ordinate along the third axis.
     * This is equivalent to <code>{@linkplain #getMaximum getMaximum}(2)</code>.
     *
     * @return The maximum ordinate along the third axis.
     */
    double getMaxZ();

    /**
     * Includes the provided coordinates, expanding as necessary. Note that there is no
     * guarantee that the (<var>x</var>, <var>x</var>) values are oriented toward
     * ({@linkplain AxisDirection#EAST East}, {@linkplain AxisDirection#NORTH North}),
     * since it depends on the {@linkplain #getCoordinateReferenceSystem envelope CRS}.
     *
     * @param x The first ordinate value.
     * @param y The second ordinate value.
     * @param z The third ordinate value.
     */
    void include(double x, double y, double z);

    /**
     * Returns {@code true} if the provided location is contained by this bounding box.
     * Note that there is no guarantee that the (<var>x</var>, <var>x</var>) values are
     * oriented toward ({@linkplain AxisDirection#EAST East}, {@linkplain AxisDirection#NORTH North}),
     * since it depends on the {@linkplain #getCoordinateReferenceSystem envelope CRS}.
     *
     * @param x The first ordinate value.
     * @param y The second ordinate value.
     * @param z The second ordinate value.
     * @return {@code true} if the given position is inside this bounds.
     */
    boolean contains(double x, double y, double z);

    /**
     * Transforms this box to the specified CRS and returns a new bounding box for the
     * transformed shape. This method provides a convenient (while not always efficient)
     * way to get {@linkplain #getMinimum minimum} and {@linkplain #getMaximum maximum}
     * ordinate values toward some specific axis directions, typically
     * {@linkplain AxisDirection#EAST East} and {@linkplain AxisDirection#NORTH North}.
     * <p>
     * <b>Example:</b> if {@code box} is a bounding box using a {@linkplain GeographicCRS
     * geographic CRS} with WGS84 datum, then one can write:
     *
     * <blockquote><pre>
     * GeographicCRS targetCRS   = crsAuthorityFactory.{@linkplain CRSAuthorityFactory#createGeographicCRS createGeographicCRS}("EPSG:4326");
     * BoundingBox   targetBox   = box.toBounds(targetCRS);
     * double        minEasting  = targetBox.getMinY();
     * double        minNorthing = targetBox.getMinX();
     * </pre></blockquote>
     *
     * Be aware that {@code "EPSG:4326"} has (<var>latitude</var>, <var>longitude</var>)
     * axis order, thus the inversion of <var>X</var> and <var>Y</var> in the above code.
     * <p>
     * Sophesticated applications will typically provide more efficient way to perform
     * similar transformations in their context. For example {@linkplain Canvas} store
     * precomputed {@linkplain Canvas#getObjectiveToDisplayTransform objective to display
     * transforms}.
     *
     * @param  targetCRS The target CRS for the bounding box to be returned.
     * @return A new bounding box wich includes the shape of this box transformed
     *         to the specified target CRS.
     * @throws TransformException if no transformation path has been found from
     *         {@linkplain #getCoordinateReferenceSystem this box CRS} to the specified
     *         target CRS, or if the transformation failed for an other reason.
     */
    BoundingBox toBounds(CoordinateReferenceSystem targetCRS) throws TransformException;
}
