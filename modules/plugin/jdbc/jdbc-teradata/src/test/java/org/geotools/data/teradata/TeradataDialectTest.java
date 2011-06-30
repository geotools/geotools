package org.geotools.data.teradata;

import com.vividsolutions.jts.geom.Geometry;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Handler;
import java.util.logging.Level;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.spatial.BBOX;

/**
 * Test the LOB workaround directly
 * @author Ian Schneider
 */
public class TeradataDialectTest extends JDBCTestSupport {
    static int cnt = 99;
    
    static void enableLogging(Level level) {
        Handler handler = Logging.getLogger("").getHandlers()[0];
        handler.setLevel(level);
        
        org.geotools.util.logging.Logging.getLogger("org.geotools.jdbc").setLevel(level);
    }

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        enableLogging(Level.INFO);
    }

    @Override
    protected void tearDownInternal() throws Exception {
        super.tearDownInternal();
    }

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new DialectTestSetup();
    }
    public void testSmallWKT() throws Exception {
        int coords = insertGeom(16000);
        read(coords, cnt - 1);
    }

    public void testLargeWKT() throws Exception {
        int coords = insertGeom(60000);
        read(coords, cnt - 1);
    }
    
    // this currently doesn't exercise the indexing since tessalation doesn' exist
    public void testLargerWKTBBox() throws Exception {
        enableLogging(Level.FINE);
        int coords = insertGeom(30000);
        BBOX bbox = CommonFactoryFinder.getFilterFactory2(null).bbox("geometry", -181.8,-90.868,181.8,84.492,null);
        read(coords, cnt - 1,bbox);
    }

    private int insertGeom(int size) throws SQLException {
        StringBuilder geom = new StringBuilder("LINESTRING(");
        int coords = 0;
        while (geom.length() < size) {
            if (coords > 0) {
                geom.append(',');
            }
            double coord = -90 + (coords % 90);
            geom.append(' ');
            geom.append(coord);
            geom.append(' ');
            geom.append(coord);
            coords++;
        }
        geom.append(")");
        try {
            PreparedStatement ps = dataStore.getDataSource().getConnection().prepareStatement("INSERT INTO \"ft3\" VALUES(?,new ST_Geometry(?),0,0.0,'zero')");
            ps.setInt(1, cnt++);
            ps.setCharacterStream(2, new StringReader(geom.toString()), geom.length());
            ps.execute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return coords;
    }

    private void read(int size, int id) throws Exception {
        read(size,id,Filter.INCLUDE);
    }
    private void read(int size, int id,Filter f) throws Exception {
        final String fid = "ft3." + id;
        SimpleFeatureSource featureSource = dataStore.getFeatureSource("ft3");
        Query q = new Query();
        q.setFilter(f);
        SimpleFeatureIterator features = featureSource.getFeatures(q).features();
        Geometry g = null;
        try {
            while (features.hasNext()) {
                SimpleFeature next = features.next();
                if (next.getID().equals(fid)) {
                    g = (Geometry) next.getDefaultGeometry();
                    break;
                }
            }
        } finally {
            features.close();
        }
        assertNotNull("could not locate " + fid, g);
        assertEquals(size, g.getCoordinates().length);
    }

    static class DialectTestSetup extends TeradataTestSetup {
        @Override
        protected void setUpData() throws Exception {
            super.setUpData();
            enableLogging(Level.FINE);
            runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'ft3'");
            runSafe("DROP TRIGGER \"ft3_geometry_mi\"");
            runSafe("DROP TRIGGER \"ft3_geometry_mu\"");
            runSafe("DROP TRIGGER \"ft3_geometry_md\"");
            runSafe("DROP TABLE \"ft3_geometry_idx\"");
            runSafe("DROP TABLE \"ft3\"");

            run("CREATE TABLE \"ft3\"(" //
                    + "\"id\" PRIMARY KEY not null integer, " //
                    + "\"geometry\" ST_GEOMETRY, " //
                    + "\"intProperty\" int," //
                    + "\"doubleProperty\" double precision, " //
                    + "\"stringProperty\" varchar(200) casespecific)");
            run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS VALUES('"
                    + fixture.getProperty("database") + "', '" + fixture.getProperty("schema")
                    + "', 'ft3', 'geometry', 2, " + srid4326 + ", 'LINESTRING',-180,-90,180,90)");
            //@todo when things are fixed on teradata side of things, add back primary index
            run("CREATE MULTISET TABLE \"ft3_geometry_idx\""
//                    + " (id INTEGER NOT NULL, cellid INTEGER NOT NULL) PRIMARY INDEX (cellid)");
                    + " (id INTEGER NOT NULL, cellid INTEGER NOT NULL)");
            TeradataDialect.installTriggers(getDataSource().getConnection(),"ft3","geometry","ft3_geometry_idx");
        }
    }

}