/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.api.parameter.ParameterValueGroup;

/**
 * Auto Geostationary Satellite ({@code AUTO:97004}). In the notation below, "<code>${var}</code>"
 * denotes a reference to the value of a variable "{@code var}". The variables "{@code lon0}" and
 * "{@code satellite_height}" are the central point of the projection and the height of the
 * satellite above the equator, respectively. For convenience, if the height is 0, it's assumed to
 * default to 35785831 meters, which is the height of the geostationary satellites.
 *
 * <pre>
 * PROJCS["Geostationary_Satellite",
 *   GEOGCS["WGS 84",
 *     DATUM["WGS_1984",
 *     SPHEROID["WGS 84",6378137,298.257223563]],
 *     PRIMEM["Greenwich",0.0],UNIT["degree",0.01745329251994328]],
 *   PROJECTION["Geostationary_Satellite"],
 *   PARAMETER["central_meridian", ${Latitude_Of_Center}],
 *   PARAMETER["satellite_height", ${Satellite_Height}],
 *   PARAMETER["false_easting",0],
 *   PARAMETER["false_northing",0],UNIT["meter",1]]
 * </pre>
 *
 * Where:
 *
 * <pre>
 * ${Latitude_Of_Center} = ${lon0}
 * ${Satellite_Height}   = ${lat0}
 * </pre>
 *
 * @version $Id$
 * @author Andrea Aime
 */
final class Auto97004 extends Factlet {
    /** A shared (thread-safe) instance. */
    public static final Auto97004 DEFAULT = new Auto97004();

    public static final double SATELLITE_HEIGHT = 35785831;

    /** Do not allows instantiation except the {@link #DEFAULT} constant. */
    private Auto97004() {}

    /** {@inheritDoc} */
    @Override
    public int code() {
        return 97004;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Geostationary Satellite";
    }

    /** {@inheritDoc} */
    @Override
    public String getClassification() {
        return "Geostationary_Satellite";
    }

    /** {@inheritDoc} */
    @Override
    protected void setProjectionParameters(final ParameterValueGroup parameters, final Code code) {
        final double centralMeridian = code.longitude;
        final double height = code.latitude == 0 ? SATELLITE_HEIGHT : code.latitude;

        parameters.parameter("central_meridian").setValue(centralMeridian);
        parameters.parameter("satellite_height").setValue(height);
    }
}
