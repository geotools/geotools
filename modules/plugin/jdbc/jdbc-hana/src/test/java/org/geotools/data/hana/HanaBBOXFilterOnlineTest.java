package org.geotools.data.hana;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BBOX3D;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class HanaBBOXFilterOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new HanaTestSetup();
    }

    @Test
    public void testPrepare2DBBOXFilter() throws Exception {
        HanaDialect dialect = new HanaDialect(dataStore);
        HanaVersion version = new HanaVersion("2.00.60.0.0");
        HanaFilterToSQL filterToSQL = new HanaFilterToSQL(dialect, true, version);

        FilterFactory ff = dataStore.getFilterFactory();
        BBOX filter = ff.bbox(aname("GEOM"), 1, 2, 3, 4, "EPSG:4326");
        String s = filterToSQL.encodeToString(filter);
        assertEquals(
                "WHERE GEOM.ST_IntersectsRectPlanar(ST_GeomFromWKB(?, -1), ST_GeomFromWKB(?, -1)) = 1",
                s);

        GeometryFactory f = new GeometryFactory();
        List<Object> literals = filterToSQL.getLiteralValues();
        assertEquals(2, literals.size());
        assertEquals(f.createPoint(new Coordinate(1, 2)), literals.get(0));
        assertEquals(f.createPoint(new Coordinate(3, 4)), literals.get(1));
    }

    @Test
    public void testPrepare3DBBOXFilter() throws Exception {
        HanaDialect dialect = new HanaDialect(dataStore);
        HanaVersion version = new HanaVersion("2.00.60.0.0");
        HanaFilterToSQL filterToSQL = new HanaFilterToSQL(dialect, true, version);

        FilterFactory ff = dataStore.getFilterFactory();
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
        BBOX3D filter = ff.bbox("GEOM", new ReferencedEnvelope3D(1, 2, 3, 4, 5, 6, crs));
        String s = filterToSQL.encodeToString(filter);
        assertEquals(
                "WHERE GEOM.ST_IntersectsRectPlanar(ST_GeomFromWKB(?, -1), ST_GeomFromWKB(?, -1)) = 1 AND GEOM.ST_ZMin() <= ? AND GEOM.ST_ZMax() >= ?",
                s);

        GeometryFactory f = new GeometryFactory();
        List<Object> literals = filterToSQL.getLiteralValues();
        assertEquals(4, literals.size());
        assertEquals(f.createPoint(new Coordinate(1, 3)), literals.get(0));
        assertEquals(f.createPoint(new Coordinate(2, 4)), literals.get(1));
        assertEquals(6.0, literals.get(2));
        assertEquals(5.0, literals.get(3));
    }
}
