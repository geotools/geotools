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

// OpenGIS dependencies

import org.opengis.parameter.ParameterValueGroup;

/**
 * Auto Gnomonic ({@code AUTO:97001}). In the notation below, "<code>${var}</code>" denotes a
 * reference to the value of a variable "{@code var}". The variables "{@code lat0}" and "{@code
 * lon0}" are the central point of the projection appearing in the CRS parameter of the map request.
 *
 * <pre>
 * PROJCS["WGS 84 / Auto Gnomonic",
 *   GEOGCS["GCS_WGS_1984",
 *     DATUM["D_WGS_1984",
 *       SPHEROID["WGS_1984",6378137.0,298.257223563]],
 *     PRIMEM["Greenwich",0.0],
 *     UNIT["Degree",0.0174532925199433]],
 *   PROJECTION["Gnomonic"],
 *   PARAMETER["False_Easting",0.0],
 *   PARAMETER["False_Northing",0.0],
 *   PARAMETER["Latitude_Of_Center",${Latitude_Of_Center}],
 *   PARAMETER["Longitude_Of_Center",${Longitude_Of_Center}],
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
final class Auto97001 extends Factlet {
    /** A shared (thread-safe) instance. */
    public static final Auto97001 DEFAULT = new Auto97001();

    /** Do not allows instantiation except the {@link #DEFAULT} constant. */
    private Auto97001() {}

    /** {@inheritDoc} */
    public int code() {
        return 97001;
    }

    /** {@inheritDoc} */
    public String getName() {
        return "WGS 84 / Auto Gnomonic";
    }

    /** {@inheritDoc} */
    public String getClassification() {
        return "Gnomonic";
    }

    /** {@inheritDoc} */
    protected void setProjectionParameters(final ParameterValueGroup parameters, final Code code) {
        final double latitudeOfCenter = code.latitude;
        final double longitudeOfCenter = code.longitude;

        parameters.parameter("Latitude_Of_Center").setValue(latitudeOfCenter);
        parameters.parameter("Longitude_Of_Center").setValue(longitudeOfCenter);
    }
}
