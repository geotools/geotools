/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.operation;

import org.opengis.annotation.Extension;


/**
 * A {@linkplain org.opengis.referencing.operation.Conversion conversion} transforming
 * (<var>longitude</var>,<var>latitude</var>) coordinates to cartesian coordinates
 * (<var>x</var>,<var>y</var>). Although some map projections can be represented as a
 * geometric process, in general a map projection is a set of formulae that converts geodetic
 * latitude and longitude to plane (map) coordinates. Height plays no role in this process,
 * which is entirely two-dimensional. The same map projection can be applied to many
 * {@linkplain org.opengis.referencing.crs.GeographicCRS geographic CRSs}, resulting in many
 * {@linkplain org.opengis.referencing.crs.ProjectedCRS projected CRSs} each of which is related
 * to the same {@linkplain org.opengis.referencing.datum.GeodeticDatum geodetic datum} as the
 * geographic CRS on which it was based.
 * <P>
 * An unofficial list of projections and their parameters can
 * be found <A HREF="http://www.remotesensing.org/geotiff/proj_list/">there</A>.
 * Most projections expect the following parameters:
 *  <code>"semi_major"</code> (mandatory),
 *  <code>"semi_minor"</code> (mandatory),
 *  <code>"central_meridian"</code> (default to 0),
 *  <code>"latitude_of_origin"</code> (default to 0),
 *  <code>"scale_factor"</code> (default to 1),
 *  <code>"false_easting"</code> (default to 0) and
 *  <code>"false_northing"</code> (default to 0).
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see org.opengis.referencing.crs.ProjectedCRS
 * @see <A HREF="http://mathworld.wolfram.com/MapProjection.html">Map projections on MathWorld</A>
 */
@Extension
public interface Projection extends Conversion {
}
