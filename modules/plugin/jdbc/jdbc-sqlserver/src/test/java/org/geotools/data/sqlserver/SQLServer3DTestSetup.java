package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBC3DTestSetup;

public class SQLServer3DTestSetup extends JDBC3DTestSetup {

    protected SQLServer3DTestSetup() {
        super(new SQLServerTestSetup());
    }

    @Override
    protected void createLine3DTable() throws Exception {
        // smuggle in the creation of the geometry columns table
        run(
                "CREATE TABLE GEOMETRY_COLUMNS_3D_TEST("
                        + "   F_TABLE_SCHEMA VARCHAR(30) NOT NULL,"
                        + "   F_TABLE_NAME VARCHAR(30) NOT NULL,"
                        + "   F_GEOMETRY_COLUMN VARCHAR(30) NOT NULL,"
                        + "   COORD_DIMENSION INTEGER,"
                        + "   SRID INTEGER NOT NULL,"
                        + "   TYPE VARCHAR(30) NOT NULL,"
                        + "   UNIQUE(F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN),"
                        + "   CHECK(TYPE IN ('POINT','LINE', 'POLYGON', 'COLLECTION', 'MULTIPOINT', 'MULTILINE', 'MULTIPOLYGON', 'GEOMETRY') ))");

        // setup table
        run(
                "CREATE TABLE \"line3d\"(\"fid\" int IDENTITY(0, 1) PRIMARY KEY, \"id\" int, "
                        + "\"geom\" geometry, \"name\" varchar(255) )");
        run(
                "CREATE SPATIAL INDEX line3d_GEOM_IDX ON \"line3d\"(\"geom\") WITH (BOUNDING_BOX = (-10, -10, 10, 10))");

        // insert data
        run(
                "INSERT INTO \"line3d\" (\"id\",\"geom\",\"name\") VALUES (0,"
                        + "geometry::STGeomFromText('LINESTRING(1 1 0, 2 2 0, 4 2 1, 5 1 1)', 4326),"
                        + "'l1')");
        run(
                "INSERT INTO \"line3d\" (\"id\",\"geom\",\"name\") VALUES (1,"
                        + "geometry::STGeomFromText('LINESTRING(3 0 1, 3 2 2, 3 3 3, 3 4 5)', 4326),"
                        + "'l2')");
    }

    @Override
    protected void createPoint3DTable() throws Exception {
        // setup table
        run(
                "CREATE TABLE \"point3d\"(\"fid\" int IDENTITY(0, 1)  PRIMARY KEY, \"id\" int, "
                        + "\"geom\" geometry, \"name\" varchar(255) )");
        run(
                "CREATE SPATIAL INDEX POINT3D_GEOM_IDX ON \"point3d\"(\"geom\") WITH (BOUNDING_BOX = (-10, -10, 10, 10))");

        // insert data
        run(
                "INSERT INTO \"point3d\" (\"id\",\"geom\",\"name\") VALUES (0, "
                        + "geometry::STGeomFromText('POINT(1 1 1)', 4326),"
                        + "'p1')");
        run(
                "INSERT INTO \"point3d\" (\"id\",\"geom\",\"name\") VALUES (1, "
                        + "geometry::STGeomFromText('POINT(3 0 1)', 4326),"
                        + "'p2')");
    }

    @Override
    protected void dropLine3DTable() throws Exception {
        runSafe("DROP TABLE \"GEOMETRY_COLUMNS_3D_TEST\"");
        runSafe("DROP TABLE \"line3d\"");
    }

    @Override
    protected void dropPoly3DTable() throws Exception {
        runSafe("DROP TABLE \"poly3d\"");
    }

    @Override
    protected void dropPoint3DTable() throws Exception {
        runSafe("DROP TABLE \"point3d\"");
    }
}
