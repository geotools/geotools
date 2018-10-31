/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.wms;

import org.geotools.referencing.datum.DefaultEllipsoid;
import org.opengis.parameter.ParameterValueGroup;

/**
 * Auto Stereographic ({@code AUTO:97002}). In the notation below, "<code>${var}</code>" denotes a
 * reference to the value of a variable "{@code var}". The variables "{@code lat0}" and "{@code
 * lon0}" are the central point of the projection appearing in the CRS parameter of the map request.
 *
 * <p>Note this projection uses a sphere. It does this by setting the semi minor axis to the same
 * value as the semi major axis.
 *
 * <pre>
 * PROJCS["WGS 84 / Auto Stereographic",
 *   GEOGCS["GCS_WGS_1984",
 *     DATUM["D_WGS_1984",
 *       SPHEROID["WGS_1984",6378137.0,298.257223563]],
 *     PRIMEM["Greenwich",0.0],
 *     UNIT["Degree",0.0174532925199433]],
 *   PROJECTION["Stereographic"],
 *   PARAMETER["False_Easting",0.0],
 *   PARAMETER["False_Northing",0.0],
 *   PARAMETER["Latitude_Of_Origin",${Latitude_Of_Origin}],
 *   PARAMETER["Central_Meridian",${Central_Meridian}],
 *   UNIT["Meter",1.0]]
 * </pre>
 *
 * Where:
 *
 * <pre>
 * ${Latitude_Of_Center} = ${lat0}
 * ${Longitude_Of_Center}   = ${lon0}
 * </pre>
 *
 * @version $Id$
 * @author Simon Schafer
 */
final class Auto97002 extends Factlet {
    /** A shared (thread-safe) instance. */
    public static final Auto97002 DEFAULT = new Auto97002();

    /** Do not allows instantiation except the {@link #DEFAULT} constant. */
    private Auto97002() {}

    /** {@inheritDoc} */
    public int code() {
        return 97002;
    }

    /** {@inheritDoc} */
    public String getName() {
        return "WGS 84 / Auto Stereographic";
    }

    /** {@inheritDoc} */
    public String getClassification() {
        return "Stereographic";
    }

    /** {@inheritDoc} */
    protected void setProjectionParameters(final ParameterValueGroup parameters, final Code code) {
        final double latitudeOfOrigin = code.latitude;
        final double centralMeridian = code.longitude;

        final double semiMajor = DefaultEllipsoid.WGS84.getSemiMajorAxis();
        final double semiMinor = semiMajor;

        parameters.parameter("central_meridian").setValue(centralMeridian);
        parameters.parameter("latitude_of_origin").setValue(latitudeOfOrigin);
        parameters.parameter("semi_major").setValue(semiMajor);
        parameters.parameter("semi_minor").setValue(semiMinor);
    }
}
