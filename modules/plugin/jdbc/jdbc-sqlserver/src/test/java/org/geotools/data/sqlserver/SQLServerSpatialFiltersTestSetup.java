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
package org.geotools.data.sqlserver;

public class SQLServerSpatialFiltersTestSetup extends SQLServerDataStoreAPITestSetup {

    protected void setUpData() throws Exception {
        runSafe("DROP TABLE ppoint");

        super.setUpData();

        run(
                "CREATE TABLE ppoint(fid int IDENTITY(0,1) PRIMARY KEY, id int, geom geometry, name nvarchar(255) )");
        run(
                "INSERT INTO PPOINT (id,geom,name) VALUES (0,"
                        + "geometry::STGeomFromText('POINT(170000 0)', 32632),"
                        + "'p1')");

        run(
                "CREATE SPATIAL INDEX _ppoint_geometry_index on ppoint(geom) WITH (BOUNDING_BOX = (166021.4431, 0.0000, 833978.5569, 9329005.1825))");
    }
}
