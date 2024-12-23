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
package org.geotools.geopkg;

import java.io.IOException;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.jdbc.JDBCIAUTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class GeoPkgIAUTestSetup extends JDBCIAUTestSetup {

    protected GeoPkgIAUTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createMarsPoiTable() throws Exception {
        run(
                "INSERT INTO gpkg_spatial_ref_sys(srs_name, srs_id, organization, organization_coordsys_id, definition) "
                        + "VALUES ('GCS_Mars_2000_Sphere', 949900, 'IAU', 49900, 'GEOGCS[\"Mars\",DATUM[\"D_Mars\",SPHEROID[\"Mars\",3396190,169.8944472236118]],PRIMEM[\"Greenwich\",0],UNIT[\"Decimal_Degree\",0.0174532925199433]]')");

        GeometryBuilder gb = new GeometryBuilder();
        run("CREATE TABLE mars_poi (id integer primary key autoincrement, geom blob, name varchar, diameter double)");
        run("INSERT INTO mars_poi (geom, name, diameter) VALUES (X'"
                + toPoint(gb, -36.897, -27.2282)
                + "', 'Blunck', 66.485)");
        run("INSERT INTO mars_poi (geom, name, diameter) VALUES (X'"
                + toPoint(gb, -36.4134, -30.3621)
                + "', 'Martynov', 61)");
        run("INSERT INTO mars_poi (geom, name, diameter) VALUES (X'"
                + toPoint(gb, -2.75999999999999, -86.876)
                + "', 'Australe Mensa', 172)");

        run("INSERT INTO gpkg_geometry_columns VALUES ('mars_poi', 'geom', 'POINT', 949900, 0, 0)");

        run("INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES "
                + "('mars_poi', 'features', 'mars_poi', 949900)");
    }

    private String toPoint(GeometryBuilder gb, double x, double y) throws IOException {
        return ((GeoPkgTestSetup) delegate).toString(gb.point(x, y));
    }

    @Override
    protected void dropMarsPoiTable() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("mars_poi");
        runSafe("delete from gpkg_spatial_ref_sys where srs_id = 949900");
    }

    @Override
    protected void dropMarsGeology() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("mars_geology");
    }

    @Override
    protected void removeIAU49901() throws Exception {
        run("DELETE FROM gpkg_spatial_ref_sys WHERE organization_coordsys_id = 49901 and organization = 'IAU'");
    }
}
