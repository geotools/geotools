package org.geotools.gce.pgraster;

import static org.geotools.coverage.grid.io.GridCoverage2DReader.HAS_TIME_DOMAIN;
import static org.geotools.coverage.grid.io.GridCoverage2DReader.TIME_DOMAIN_MAXIMUM;
import static org.geotools.coverage.grid.io.GridCoverage2DReader.TIME_DOMAIN_MINIMUM;
import static org.junit.Assert.assertNotEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.parameter.Parameter;
import org.geotools.test.OnlineTestCase;
import org.geotools.test.TestData;
import org.opengis.parameter.GeneralParameterValue;

public class PGRasterTest extends OnlineTestCase {

    PGRasterConfig config;

    @Override
    protected String getFixtureId() {
        return "postgis-raster";
    }

    @Override
    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put("driver", "org.postgresql.Driver");
        fixture.put("host", "localhost");
        fixture.put("database", "geotools");
        fixture.put("schema", "public");
        fixture.put("port", "5432");
        fixture.put("username", System.getProperty("user.name"));
        fixture.put("password", "");
        return fixture;
    }

    @Override
    protected boolean isOnline() throws Exception {
        Class.forName(fixture.getProperty("driver"));
        Connection cx =
                DriverManager.getConnection(
                        url(fixture),
                        fixture.getProperty("username"),
                        fixture.getProperty("password"));

        String schema = fixture.getProperty("schema");
        String table = "gray";

        SQL sql = new SQL("SELECT * FROM ");
        if (schema != null) {
            sql.name(schema).append(".");
        }
        sql.name(table);
        sql.append(" WHERE 1 = 0");

        try (Statement st = cx.createStatement()) {
            st.execute(sql.toString());
        }
        cx.close();
        return true;
    }

    @Override
    protected void connect() throws Exception {
        BasicDataSource db = new BasicDataSource();
        db.setDriverClassName(fixture.getProperty("driver"));
        db.setUrl(url(fixture));
        db.setUsername(fixture.getProperty("username"));
        db.setPassword(fixture.getProperty("password"));

        db.getConnection().close();

        config = new PGRasterConfig();
        config.enableDrivers = "ENABLE_ALL";
        config.schema = fixture.getProperty("schema", "public");
        config.dataSource = db;
    }

    @Override
    protected void disconnect() throws Exception {
        ((BasicDataSource) config.dataSource).close();
    }

    String url(Properties f) {
        fixture.put("url", "jdbc:postgresql://localhost/mydbname");

        return "jdbc:postgresql://"
                + f.getProperty("host", "localhost")
                + ":"
                + f.getProperty("port", "5432")
                + "/"
                + f.getProperty("database");
    }

    public void testGray() throws Exception {
        config.table = "gray";

        PGRasterReader reader = new PGRasterReader(config, null, null);
        GridCoverage2D rast = reader.read(null);
        assertNotNull(rast);

        show(rast);
    }

    public void testRGB() throws Exception {
        config.table = "world";

        PGRasterReader reader = new PGRasterReader(config, null, null);
        GridCoverage2D rast = reader.read(null);
        assertNotNull(rast);

        show(rast);
    }

    public void testTime() throws Exception {
        config.table = "world_with_time";

        PGRasterReader reader = new PGRasterReader(config, null, null);

        List<String> names = Arrays.asList(reader.getMetadataNames());
        assertTrue(names.contains(HAS_TIME_DOMAIN));
        assertTrue(names.contains(TIME_DOMAIN_MAXIMUM));
        assertTrue(names.contains(TIME_DOMAIN_MINIMUM));

        String max = reader.getMetadataValue(TIME_DOMAIN_MAXIMUM);
        assertNotNull(max);
        String min = reader.getMetadataValue(TIME_DOMAIN_MINIMUM);
        assertNotNull(min);
        assertNotEquals(min, max);

        GridCoverage2D r1 =
                reader.read(
                        new GeneralParameterValue[] {
                            new Parameter<>(AbstractGridFormat.TIME, Collections.singletonList(min))
                        });
        GridCoverage2D r2 =
                reader.read(
                        new GeneralParameterValue[] {
                            new Parameter<>(AbstractGridFormat.TIME, Collections.singletonList(max))
                        });

        show(r1);
        show(r2);
    }

    private void show(GridCoverage2D rast) {
        if (TestData.isInteractiveTest()) {
            rast.show();
        }
    }
}
