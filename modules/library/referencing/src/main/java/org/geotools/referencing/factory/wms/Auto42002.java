/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 * Auto Transverse Mercator ({@code AUTO:42002}). In the notation below, "<code>${var}</code>"
 * denotes a reference to the value of a variable "{@code var}". The variables "{@code lat0}" and
 * "{@code lon0}" are the central point of the projection appearing in the CRS parameter of the map
 * request. The coordinate operation method uses ellipsoidal formulas.
 *
 * <pre>
 * PROJCS["WGS 84 / Auto Tr. Mercator",
 *   GEOGCS["WGS 84",
 *     DATUM["WGS_1984",
 *       SPHEROID["WGS_1984", 6378137, 298.257223563]],
 *     PRIMEM["Greenwich", 0],
 *     UNIT["Decimal_Degree", 0.0174532925199433]],
 *   PROJECTION["Transverse_Mercator"],
 *   PARAMETER["Latitude_of_Origin", 0],
 *   PARAMETER["Central_Meridian", ${central_meridian}],
 *   PARAMETER["False_Easting", 500000],
 *   PARAMETER["False_Northing", ${false_northing}],
 *   PARAMETER["Scale_Factor", 0.9996],
 *   UNIT["Meter", 1]]
 * </pre>
 *
 * Where:
 *
 * <pre>
 * ${central_meridian} = ${lon0}
 * ${false_northing}   = (${lat0} >= 0.0) ? 0.0 : 10000000.0
 * </pre>
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 * @author Rueben Schulz
 * @author Martin Desruisseaux
 */
final class Auto42002 extends Factlet {
    /** A shared (thread-safe) instance. */
    public static final Auto42002 DEFAULT = new Auto42002();

    /** Do not allows instantiation except the {@link #DEFAULT} constant. */
    private Auto42002() {}

    /** {@inheritDoc} */
    public int code() {
        return 42002;
    }

    /** {@inheritDoc} */
    public String getName() {
        return "WGS 84 / Auto Tr. Mercator";
    }

    /** {@inheritDoc} */
    public String getClassification() {
        return "Transverse_Mercator";
    }

    /** {@inheritDoc} */
    protected void setProjectionParameters(final ParameterValueGroup parameters, final Code code) {
        final double centralMeridian = code.longitude;
        final double falseNorthing = code.latitude >= 0.0 ? 0.0 : 10000000.0;

        parameters.parameter("latitude_of_origin").setValue(0.0);
        parameters.parameter("central_meridian").setValue(centralMeridian);
        parameters.parameter("false_easting").setValue(500000.0);
        parameters.parameter("false_northing").setValue(falseNorthing);
        parameters.parameter("scale_factor").setValue(0.9996);
    }
}
