/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCIAUTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class PostgisIAUTestSetup extends JDBCIAUTestSetup {

    public PostgisIAUTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createMarsPoiTable() throws Exception {
        run(
                "INSERT INTO spatial_ref_sys(srid, auth_name, auth_srid, srtext, proj4text) "
                        + "VALUES (949900, 'IAU', 49900, 'GEOGCS[\"Mars\",DATUM[\"D_Mars\",SPHEROID[\"Mars\",3396190,169.8944472236118]],PRIMEM[\"Greenwich\",0],UNIT[\"Decimal_Degree\",0.0174532925199433]]', '+proj=longlat +R=3396190 +no_defs')");

        run(
                "CREATE TABLE MARS_POI (id serial primary key, geom Geometry(Point, 949900), name varchar, diameter double precision)");
        run(
                "INSERT INTO MARS_POI (geom, name, diameter) VALUES (ST_GeomFromText('POINT (-36.897 -27.2282)', 949900), 'Blunck', 66.485)");
        run(
                "INSERT INTO MARS_POI (geom, name, diameter) VALUES (ST_GeomFromText('POINT (-36.4134 -30.3621)', 949900), 'Martynov', 61)");
        run(
                "INSERT INTO MARS_POI (geom, name, diameter) VALUES (ST_GeomFromText('POINT (-2.75999999999999 -86.876)', 949900), 'Australe Mensa', 172)");
    }

    @Override
    protected void dropMarsPoiTable() throws Exception {
        runSafe("DELETE FROM spatial_ref_sys where srid = 949900");
        runSafe("DROP TABLE mars_poi");
    }

    @Override
    protected void dropMarsGeology() throws Exception {
        runSafe("DROP TABLE mars_geology");
        runSafe("DROP TABLE mars_geology2");
    }

    @Override
    protected void removeIAU49901() throws Exception {
        runSafe("DELETE FROM spatial_ref_sys where auth_name = 'IAU' and auth_srid = 49901");
    }
}
