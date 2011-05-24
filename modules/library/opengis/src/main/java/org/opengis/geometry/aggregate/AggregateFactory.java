/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.aggregate;

import java.util.Set;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.OrientableSurface;


/**
 * A factory of {@linkplain Aggregate aggregate} geometric objects.
 * All aggregates created through this interface will use the
 * {@linkplain #getCoordinateReferenceSystem factory's coordinate reference system}.
 * Creating aggregates in a different CRS may requires a different instance of
 * {@code AggregateFactory}.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/aggregate/AggregateFactory.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Sanjay Jena
 * @author Prof. Dr. Jackson Roehrig
 * @since GeoAPI 2.1
 *
 * @todo Need to check if ISO 19107 defines constructors for aggregates.
 */
public interface AggregateFactory {
    /**
     * Creates a {@linkplain MultiPrimitive multi primitive} by a set of {@linkplain Primitive primitives}.
     * The created {@code MultiPrimitive} will hold only references to the given primitive,
     * e.g. changes in the primitive instances will have consequences in the
     * geometric behaviour of the {@code MultiPrimitive} and vice versa.
     *
     * @param points A set of primitives.
     * @return the resulting multi primitive.
     */
    MultiPrimitive createMultiPrimitive(Set<Primitive> primitives);

    /**
     * Creates a {@linkplain MultiPoint multi point} by a set of {@linkplain Point points}.
     * The created {@code MultiPoint} will hold only references to the given points,
     * e.g. changes in the point instances will have consequences in the
     * geometric behaviour of the {@code MultiPoint} and vice versa.
     *
     * @param points A set of points.
     * @return the resulting multi point.
     */
    MultiPoint createMultiPoint(Set<Point> points);

    /**
     * Creates a {@linkplain MultiCurve multi curve} by a set of {@linkplain Curve curves}.
     * The created {@code MultiCurve} will hold only references to the given curves,
     * e.g. changes in the curve instances will have consequences in the
     * geometric behaviour of the {@code MultiCurve} and vice versa.
     *
     * @param curves A set of curves.
     * @return the resulting multi curve.
     */
    MultiCurve createMultiCurve(Set<OrientableCurve> curves);

    /**
     * Creates a {@linkplain MultiSurface multi surface} by a set of {@linkplain Surface surfaces}.
     * The created {@code MultiSurface} will hold only references to the given surfaces,
     * e.g. changes in the surface instances will have consequences in the
     * geometric behaviour of the {@code MultiSurface} and vice versa.
     *
     * @param surfaces A set of surfaces.
     * @return the resulting multi surface.
     */
    MultiSurface createMultiSurface(Set<OrientableSurface> surfaces);
}
