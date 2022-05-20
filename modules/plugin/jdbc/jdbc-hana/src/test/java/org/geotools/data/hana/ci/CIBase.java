package org.geotools.data.hana.ci;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.hana.HanaConnectionParameters;
import org.geotools.data.hana.HanaUtil;
import org.geotools.util.logging.Logging;

public class CIBase {

    private static Logger LOGGER = Logging.getLogger(CIBase.class);

    private static final int NUM_DROP_THREADS = 8;

    protected Connection connectUsingFixture(Properties fixture) throws IOException, SQLException {
        String host = fixture.getProperty("host");
        int port = Integer.parseInt(fixture.getProperty("port"));
        String user = fixture.getProperty("user");
        String password = fixture.getProperty("password");
        String encrypt = fixture.getProperty("use ssl");
        Map<String, String> options = new HashMap<>();
        if (encrypt != null) {
            options.put("encrypt", encrypt);
        }
        HanaConnectionParameters params = HanaConnectionParameters.forPort(host, port, options);
        String url = params.buildUrl();
        LOGGER.info("Connecting via " + url);
        return DriverManager.getConnection(url, user, password);
    }

    protected File getFixtureDir() {
        return new File(new File(System.getProperty("user.home")), ".geotools");
    }

    protected File getFixtureFile() {
        return new File(getFixtureDir(), "hana.properties");
    }

    protected Properties readFixture() throws IOException {
        Properties ret = new Properties();
        try (FileInputStream is = new FileInputStream(getFixtureFile())) {
            ret.load(is);
        }
        return ret;
    }

    protected void dropSchema(Connection conn, String schema) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            LOGGER.info("Dropping schema " + schema);
            StringBuilder sb = new StringBuilder();
            sb.append("DROP SCHEMA ").append(HanaUtil.encodeIdentifier(schema)).append(" CASCADE");
            stmt.execute(sb.toString());
        }
    }

    protected void dropSchemasInParallel(Properties fixture, ArrayList<String> schemas)
            throws InterruptedException {
        if (schemas.isEmpty()) {
            return;
        }
        int numThreads = Math.min(NUM_DROP_THREADS, schemas.size());
        AtomicInteger counter = new AtomicInteger();
        SchemaDropThread[] threads = new SchemaDropThread[numThreads];
        for (int i = 0; i < numThreads; ++i) {
            threads[i] = new SchemaDropThread(fixture, schemas, counter);
            threads[i].start();
        }
        for (int i = 0; i < numThreads; ++i) {
            threads[i].join();
        }
    }

    private class SchemaDropThread extends Thread {

        public SchemaDropThread(
                Properties fixture, ArrayList<String> schemas, AtomicInteger counter) {
            this.fixture = fixture;
            this.schemas = schemas;
            this.counter = counter;
        }

        private Properties fixture;

        private ArrayList<String> schemas;

        private AtomicInteger counter;

        @Override
        public void run() {
            try (Connection conn = connectUsingFixture(fixture)) {
                while (true) {
                    int idx = counter.getAndIncrement();
                    if (idx >= schemas.size()) {
                        return;
                    }
                    String schema = schemas.get(idx);
                    dropSchema(conn, schema);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Exception in schema drop thread", e);
            }
        }
    }
}
