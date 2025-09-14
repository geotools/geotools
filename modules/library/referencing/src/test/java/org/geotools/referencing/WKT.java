/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing;

/**
 * Predefined CRS as WKT strings. Hard-coded constants are more convenient for debugging than strings read from a file.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class WKT {
    /** Do not allow instantiation of this class. */
    private WKT() {}

    /** Geographic CRS on a sphere. */
    public static final String SPHERE =
            """
            GEOGCS["Sphere",
              DATUM["Sphere",
                SPHEROID["Sphere", 6370997.0, 0.0],
                TOWGS84[0,0,0,0,0,0,0]],
              PRIMEM["Greenwich", 0.0],
              UNIT["degree", 0.017453292519943295],
              AXIS["Longitude", EAST],
              AXIS["Latitude", NORTH]]""";

    /** Geographic CRS on NAD27 datum (EPSG:4267). */
    public static final String NAD27 =
            """
            GEOGCS["NAD27",
              DATUM["North_American_Datum_1927",
                SPHEROID["Clarke 1866", 6378206.4, 294.978698213901,
                  AUTHORITY["EPSG","7008"]],
                TOWGS84[-3,142,183,0,0,0,0],
                AUTHORITY["EPSG","6267"]],
              PRIMEM["Greenwich", 0, AUTHORITY["EPSG","8901"]],
              UNIT["DMSH",0.0174532925199433, AUTHORITY["EPSG","9108"]],
              AXIS["Lat",NORTH],
              AXIS["Long",EAST],
              AUTHORITY["EPSG","4267"]]""";

    /** Geographic CRS on NAD83 datum (EPSG:4269). */
    public static final String NAD83 =
            """
            GEOGCS["NAD83",
              DATUM["North_American_Datum_1983",
                SPHEROID["GRS 1980", 6378137.0, 298.257222101,
                  AUTHORITY["EPSG","7019"]],
                TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
                AUTHORITY["EPSG","6269"]],
              PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]],
              UNIT["degree", 0.017453292519943295],
              AXIS["Lon", EAST],
              AXIS["Lat", NORTH],
              AUTHORITY["EPSG","4269"]]""";

    /** Geographic CRS (EPSG:4326). */
    public static final String WGS84 =
            """
            GEOGCS["WGS84",
              DATUM["WGS84",
                SPHEROID["WGS84", 6378137.0, 298.257223563]],
              PRIMEM["Greenwich", 0.0],
              UNIT["degree", 0.017453292519943295],
              AXIS["Longitude",EAST],\
              AXIS["Latitude",NORTH]]""";

    /** Geographic CRS (EPSG:4326) with a different datum name */
    public static final String WGS84_ALTERED =
            """
            GEOGCS["WGS84",
              DATUM["WGS84_altered",
                SPHEROID["WGS84", 6378137.0, 298.257223563]],
              PRIMEM["Greenwich", 0.0],
              UNIT["degree", 0.017453292519943295],
              AXIS["Longitude",EAST],\
              AXIS["Latitude",NORTH]]""";

    /** Geographic CRS with DMHS units. */
    public static final String WGS84_DMHS =
            """
            GEOGCS["WGS 84",
              DATUM["WGS_1984",
                SPHEROID["WGS 84", 6378137, 298.257223563,
                  AUTHORITY["EPSG","7030"]],
                TOWGS84[0,0,0,0,0,0,0],
                AUTHORITY["EPSG","6326"]],
              PRIMEM["Greenwich", 0, AUTHORITY["EPSG","8901"]],
              UNIT["DMSH",0.0174532925199433, AUTHORITY["EPSG","9108"]],
              AXIS["Lat",NORTH],
              AXIS["Long",EAST],
              AUTHORITY["EPSG","4326"]]""";

    /** Geographic CRS used in France (EPSG:4807). */
    public static final String GEOGRAPHIC_NTF =
            """
            GEOGCS["NTF (Paris)",
              DATUM["Nouvelle_Triangulation_Francaise",
                SPHEROID["Clarke 1880 (IGN)", 6378249.2, 293.466021293627,
                  AUTHORITY["EPSG","7011"]],
                TOWGS84[-168,-60,320,0,0,0,0],
                AUTHORITY["EPSG","6275"]],
              PRIMEM["Paris", 2.5969213, AUTHORITY["EPSG","8903"]],
              UNIT["grad", 0.015707963267949, AUTHORITY["EPSG", "9105"]],
              AXIS["Lat",NORTH],
              AXIS["Long",EAST],
              AUTHORITY["EPSG","4807"]]""";

    /** Mercator projection (EPSG:3395). */
    public static final String MERCATOR =
            """
            PROJCS["WGS 84 / World Mercator",
              GEOGCS["WGS 84",
                DATUM["World Geodetic System 1984",
                  SPHEROID["WGS 84", 6378137.0, 298.257223563, AUTHORITY["EPSG","7030"]],
                  AUTHORITY["EPSG","6326"]],
                PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]],
                UNIT["degree", 0.017453292519943295],
                AXIS["Geodetic latitude", NORTH],
                AXIS["Geodetic longitude", EAST],
                AUTHORITY["EPSG","4326"]],
              PROJECTION["Mercator (1SP)", AUTHORITY["EPSG","9804"]],
              PARAMETER["latitude_of_origin", 0.0],
              PARAMETER["central_meridian", 0.0],
              PARAMETER["scale_factor", 1.0],
              PARAMETER["false_easting", 0.0],
              PARAMETER["false_northing", 0.0],
              UNIT["m", 1.0],
              AXIS["Easting", EAST],
              AXIS["Northing", NORTH],
              AUTHORITY["EPSG","3395"]]""";

    /** Google projection. */
    public static final String MERCATOR_GOOGLE =
            """
            PROJCS["Google Mercator",
              GEOGCS["WGS 84",
                DATUM["World Geodetic System 1984",
                  SPHEROID["WGS 84", 6378137.0, 298.257223563, AUTHORITY["EPSG","7030"]],
                  AUTHORITY["EPSG","6326"]],
                PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]],
                UNIT["degree", 0.017453292519943295],
                AXIS["Geodetic latitude", NORTH],
                AXIS["Geodetic longitude", EAST],
                AUTHORITY["EPSG","4326"]],
              PROJECTION["Mercator_1SP"],
              PARAMETER["semi_minor", 6378137.0],
              PARAMETER["latitude_of_origin", 0.0],
              PARAMETER["central_meridian", 0.0],
              PARAMETER["scale_factor", 1.0],
              PARAMETER["false_easting", 0.0],
              PARAMETER["false_northing", 0.0],
              UNIT["m", 1.0],
              AXIS["Easting", EAST],
              AXIS["Northing", NORTH],
              AUTHORITY["EPSG","900913"]]""";

    /** Transverse Mercator on West Coast of America. */
    public static final String UTM_10N =
            """
            PROJCS["NAD_1983_UTM_Zone_10N",
              GEOGCS["GCS_North_American_1983",
                DATUM["D_North_American_1983",
                  TOWGS84[0,0,0,0,0,0,0],
                  SPHEROID["GRS_1980", 6378137, 298.257222101]],
                PRIMEM["Greenwich",0],
                UNIT["Degree", 0.017453292519943295]],
              PROJECTION["Transverse_Mercator"],
              PARAMETER["False_Easting",500000],
              PARAMETER["False_Northing",0],
              PARAMETER["Central_Meridian",-123],
              PARAMETER["Scale_Factor",0.9996],
              PARAMETER["Latitude_Of_Origin",0],
              UNIT["Meter",1]]""";

    /** Transverse Mercator on New-Caledonia (EPSG:2995). */
    public static final String UTM_58S =
            """
            PROJCS["IGN53 Mare / UTM zone 58S",
              GEOGCS["IGN53 Mare",
                DATUM["IGN53 Mare",
                  SPHEROID["International 1924", 6378388.0, 297.0, AUTHORITY["EPSG","7022"]],
                  TOWGS84[-408.809, 366.856, -412.987, 1.8842, -0.5308, 2.1655, -24.978523651158998],
                  AUTHORITY["EPSG","6641"]],
                PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]],
                UNIT["degree", 0.017453292519943295],
                AXIS["Geodetic latitude", NORTH, AUTHORITY["EPSG","106"]],
                AXIS["Geodetic longitude", EAST, AUTHORITY["EPSG","107"]],
                AUTHORITY["EPSG","4641"]],
              PROJECTION["Transverse Mercator", AUTHORITY["EPSG","9807"]],
              PARAMETER["central_meridian", 165.0],
              PARAMETER["latitude_of_origin", 0.0],
              PARAMETER["scale_factor", 0.9996],
              PARAMETER["false_easting", 500000.0],
              PARAMETER["false_northing", 10000000.0],
              UNIT["m", 1.0],
              AXIS["Easting", EAST, AUTHORITY["EPSG","1"]],
              AXIS["Northing", NORTH, AUTHORITY["EPSG","2"]],
              AUTHORITY["EPSG","2995"]]""";

    /** Antartic on WGS84 datum. */
    public static final String POLAR_STEREOGRAPHIC =
            """
            PROJCS["WGS 84 / Antarctic Polar Stereographic",
              GEOGCS["WGS 84",
                DATUM["World Geodetic System 1984",
                  SPHEROID["WGS 84", 6378137.0, 298.257223563]],
                PRIMEM["Greenwich", 0.0],
                UNIT["degree", 0.017453292519943295]],
              PROJECTION["Polar Stereographic (variant B)"],
              PARAMETER["standard_parallel_1", -71.0],
              UNIT["m", 1.0]]""";

    /** Vertical CRS over the ellipsoid. */
    public static final String Z =
            """
            VERT_CS["ellipsoid Z in meters",
              VERT_DATUM["Ellipsoid",2002],
              UNIT["metre", 1],
              AXIS["Z",UP]]""";

    /** Mean sea level (EPSG:5714). */
    public static final String HEIGHT =
            """
            VERT_CS["mean sea level height",
              VERT_DATUM["Mean Sea Level", 2005, AUTHORITY["EPSG","5100"]],
              UNIT["metre", 1, AUTHORITY["EPSG","9001"]],
              AXIS["Z",UP], AUTHORITY["EPSG","5714"]]""";
}
