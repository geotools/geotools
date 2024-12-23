/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.extent;

/**
 * Geographic position of the dataset. This is only an approximate so specifying the co-ordinate reference system is
 * unnecessary.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface GeographicBoundingBox extends GeographicExtent {
    /**
     * Returns the western-most coordinate of the limit of the dataset extent. The value is expressed in longitude in
     * decimal degrees (positive east).
     *
     * @return The western-most longitude between -180 and +180&deg;.
     * @unitof Angle
     */
    double getWestBoundLongitude();

    /**
     * Returns the eastern-most coordinate of the limit of the dataset extent. The value is expressed in longitude in
     * decimal degrees (positive east).
     *
     * @return The eastern-most longitude between -180 and +180&deg;.
     * @unitof Angle
     */
    double getEastBoundLongitude();

    /**
     * Returns the southern-most coordinate of the limit of the dataset extent. The value is expressed in latitude in
     * decimal degrees (positive north).
     *
     * @return The southern-most latitude between -90 and +90&deg;.
     * @unitof Angle
     */
    double getSouthBoundLatitude();

    /**
     * Returns the northern-most, coordinate of the limit of the dataset extent. The value is expressed in latitude in
     * decimal degrees (positive north).
     *
     * @return The northern-most latitude between -90 and +90&deg;.
     * @unitof Angle
     */
    double getNorthBoundLatitude();
}
