package org.geotools.data.teradata;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/** @author Ian Schneider <ischneider@opengeo.org> */
public class TeradataFilterToSQLOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new TeradataFilterToSQLTestSetup();
    }

    // Ensure that we exercise the composite primary index with bbox sql
    public void testIt() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        Filter bbox = ff.bbox("geometry", 0, 0, 10, 10, "4326");
        Filter filter =
                ff.and(
                        bbox,
                        ff.and(
                                ff.equals(ff.literal("intProperty"), ff.literal(5)),
                                ff.equals(ff.literal("doubleProperty"), ff.literal(5))));
        ff.equals(ff.literal("intProperty"), ff.literal(5));
        SimpleFeatureIterator features =
                dataStore.getFeatureSource("cpi").getFeatures(filter).features();
        while (features.hasNext()) {
            features.next();
        }
        // if we made it here, we passed
    }

    private static class TeradataFilterToSQLTestSetup extends TeradataTestSetup {

        @Override
        protected void setUpData() throws Exception {
            super.setUpData();
            // cpi == composite primary index
            runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'cpi'");
            runSafe("DROP HASH INDEX \"cpi_geometry_idx_idx\"");
            runSafe("DROP TABLE \"cpi_geometry_idx\"");
            runSafe("DROP TABLE \"cpi\"");

            run(
                    "CREATE TABLE \"cpi\"(" //
                            + "\"id\" not null integer, " //
                            + "\"geometry\" ST_GEOMETRY, " //
                            + "\"intProperty\" int," //
                            + "\"doubleProperty\" double precision, " //
                            + "\"stringProperty\" varchar(200) casespecific) UNIQUE PRIMARY INDEX(intProperty,doubleProperty)");
            run(
                    "INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS VALUES('"
                            + fixture.getProperty("database")
                            + "', '"
                            + fixture.getProperty("schema")
                            + "', 'cpi', 'geometry', 2, "
                            + srid4326
                            + ", 'LINESTRING',-180,-90,180,90)");
            // @todo when things are fixed on teradata side of things, add back primary index
            run(
                    "CREATE MULTISET TABLE \"cpi_geometry_idx\""
                            + " (intProperty INTEGER NOT NULL, doubleProperty double precision not null, cellid INTEGER NOT NULL)"
                            + " UNIQUE PRIMARY INDEX (intProperty,doubleProperty)");
            //                    + " (id INTEGER NOT NULL, cellid INTEGER NOT NULL)");
            run(
                    "CREATE HASH INDEX cpi_geometry_idx_idx (cellid) ON cpi_geometry_idx ORDER BY (cellid);");

            runSafe("DELETE FROM sysspatial.tessellation WHERE f_table_name = 'cpi'");
            run(
                    "INSERT INTO sysspatial.tessellation VALUES ("
                            + "'geotools',"
                            + "'cpi',"
                            + "'geometry',"
                            + "-180,-90,180,90,"
                            + "1000,1000,3,.01,0"
                            + ")");
        }
    }
}
