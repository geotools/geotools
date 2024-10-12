/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.jdbc.JDBCDelegatingTestSetup;
import org.geotools.util.logging.Logging;

/** @author Roar Brænden */
public class PostgisCRSTestSetup extends JDBCDelegatingTestSetup {

    private static Logger LOGGER = Logging.getLogger(PostgisCRSTestSetup.class);

    protected PostgisCRSTestSetup() {
        super(new PostGISTestSetup());
    }

    @Override
    protected final void setUpData() throws Exception {
        super.setUpData();

        try {
            insertTestCRS(
                    31491,
                    "ESRI",
                    "PROJCS[\"Germany_Zone_1\",GEOGCS[\"DHDN\",DATUM[\"Deutsches_Hauptdreiecksnetz\","
                            + "SPHEROID[\"Bessel 1841\",6377397.155,299.1528128,AUTHORITY[\"EPSG\",\"7004\"]],"
                            + "AUTHORITY[\"EPSG\",\"6314\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
                            + "UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],"
                            + "AUTHORITY[\"EPSG\",\"4314\"]],PROJECTION[\"Transverse_Mercator\"],"
                            + "PARAMETER[\"latitude_of_origin\",0],PARAMETER[\"central_meridian\",3],PARAMETER[\"scale_factor\",1],"
                            + "PARAMETER[\"false_easting\",1500000],PARAMETER[\"false_northing\",0],"
                            + "UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AXIS[\"Easting\",EAST],"
                            + "AXIS[\"Northing\",NORTH],AUTHORITY[\"ESRI\",\"31491\"]]",
                    "+proj=tmerc +lat_0=0 +lon_0=3 +k=1 +x_0=1500000 +y_0=0 +ellps=bessel +units=m +no_defs");
            insertTestCRS(
                    102017,
                    "ESRI",
                    "PROJCS[\"North_Pole_Lambert_Azimuthal_Equal_Area\",GEOGCS[\"WGS 84\","
                            + "DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],"
                            + "AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0],UNIT[\"Degree\",0.0174532925199433]],"
                            + "PROJECTION[\"Lambert_Azimuthal_Equal_Area\"],PARAMETER[\"latitude_of_center\",90],"
                            + "PARAMETER[\"longitude_of_center\",0],PARAMETER[\"false_easting\",0],PARAMETER[\"false_northing\",0],"
                            + "UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AXIS[\"Easting\",SOUTH],"
                            + "AXIS[\"Northing\",SOUTH],AUTHORITY[\"ESRI\",\"102017\"]]",
                    "+proj=laea +lat_0=90 +lon_0=0 +x_0=0 +y_0=0 +datum=WGS84 +units=m +no_defs");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "We were unable to add test data to spatial_ref_sys, and tests might fail", e);
        }
    }

    private void insertTestCRS(int srid, String auth, String wkt, String proj) throws Exception {
        run(String.format(
                "INSERT INTO SPATIAL_REF_SYS (srid, auth_name, auth_srid, srtext, proj4text)"
                        + " VALUES (%d,'%s',%d,'%s','%s') ON CONFLICT DO NOTHING",
                srid, auth, srid, wkt, proj));
    }
}
