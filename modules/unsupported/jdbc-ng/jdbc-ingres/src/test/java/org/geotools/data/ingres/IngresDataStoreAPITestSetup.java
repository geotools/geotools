package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class IngresDataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {

	protected IngresDataStoreAPITestSetup(JDBCTestSetup delegate) {
		super(delegate);
	}

    @Override
    protected void createLakeTable() throws Exception {
    	run("CREATE SEQUENCE LAKE_FID_SEQUENCE");
        run("CREATE TABLE \"lake\"(\"fid\" int PRIMARY KEY WITH DEFAULT NEXT VALUE FOR LAKE_FID_SEQUENCE, \"id\" int, "
                + "\"geom\" POLYGON SRID 4326, \"name\" varchar(256) )");
//        run("CREATE INDEX LAKE_GEOM_INDEX ON \"lake\" USING GIST (\"geom\") "); fix later
        
        // advance the sequence to 1 to compensate for hand insertions
//        run("SELECT nextval(pg_get_serial_sequence('lake','fid'))");

        run("INSERT INTO \"lake\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (0, 0,"
                + "GeometryFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                + "'muddy')");
    }

    @Override
    protected void createRiverTable() throws Exception {
    	run("CREATE SEQUENCE RIVER_FID_SEQUENCE");
        run("CREATE TABLE \"river\"(\"fid\" int PRIMARY KEY WITH DEFAULT NEXT VALUE FOR RIVER_FID_SEQUENCE, \"id\" int, "
                + "\"geom\" MULTILINESTRING SRID 4326, \"river\" varchar , \"flow\" real )");
//        run("CREATE INDEX RIVER_GEOM_INDEX ON \"river\" USING GIST (\"geom\") ");
        
        // advance the sequence to 1 to compensate for hand insertions
//        run("SELECT nextval(pg_get_serial_sequence('river','fid'))");

        run("INSERT INTO \"river\" (\"fid\", \"id\",\"geom\",\"river\", \"flow\") VALUES (0, 0,"
                + "GeometryFromText('MULTILINESTRING((5 5, 7 4),(7 5, 9 7, 13 7),(7 5, 9 3, 11 3))',4326),"
                + "'rv1', 4.5)");
        run("INSERT INTO \"river\" (\"id\",\"geom\",\"river\", \"flow\") VALUES (1,"
                + "GeometryFromText('MULTILINESTRING((4 6, 4 8, 6 10))',4326),"
                + "'rv2', 3.0)");
    }

    @Override
    protected void createRoadTable() throws Exception {
        // create table and spatial index
    	run("CREATE SEQUENCE ROAD_FID_SEQUENCE");
        run("CREATE TABLE \"road\"(\"fid\" int PRIMARY KEY WITH DEFAULT NEXT VALUE FOR ROAD_FID_SEQUENCE, \"id\" int, "
                + "\"geom\" LINESTRING SRID 4326, \"name\" varchar(256))");
//        run("CREATE INDEX ROAD_GEOM_INDEX ON \"road\" USING GIST (\"geom\") ");

        // advance the sequence to 2 to compensate for hand insertions
//        run("SELECT nextval(pg_get_serial_sequence('road','fid'))");
//        run("SELECT nextval(pg_get_serial_sequence('road','fid'))");

        // insertions
        run("INSERT INTO \"road\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (0, 0,"
                + "GeometryFromText('LINESTRING(1 1, 2 2, 4 2, 5 1)',4326),"
                + "'r1')");
        run("INSERT INTO \"road\" (\"id\",\"geom\",\"name\") VALUES (1,"
                + "GeometryFromText('LINESTRING(3 0, 3 2, 3 3, 3 4)',4326),"
                + "'r2')");
        run("INSERT INTO \"road\" (\"id\",\"geom\",\"name\") VALUES (2,"
                + "GeometryFromText('LINESTRING(3 2, 4 2, 5 3)',4326)," + "'r3')");
    }

    @Override
    protected void dropBuildingTable() throws Exception {
        runSafe("DROP TABLE \"building\"");
    }

    @Override
    protected void dropLakeTable() throws Exception {
        runSafe("DROP TABLE \"lake\"");
        runSafe("DROP SEQUENCE LAKE_FID_SEQUENCE");
    }

    @Override
    protected void dropRiverTable() throws Exception {
        runSafe("DROP TABLE \"river\"");
        runSafe("DROP SEQUENCE RIVER_FID_SEQUENCE");
    }

    @Override
    protected void dropRoadTable() throws Exception {
        runSafe("DROP TABLE \"road\"");
        runSafe("DROP SEQUENCE ROAD_FID_SEQUENCE");
    }

}
