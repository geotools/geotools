package org.geotools.data.solr;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.locationtech.jts.geom.Geometry;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

public final class TestContainersSupport {

    private static final DockerImageName SOLR_IMAGE =
            DockerImageName.parse(System.getProperty("gt.solr.test.image", "solr:9.6.1"));
    private static final DockerImageName POSTGIS_IMAGE =
            DockerImageName.parse(System.getProperty("gt.postgis.test.image", "postgis/postgis:16-3.4"));
    private static final Path JTS_JAR = jarPath(Geometry.class);

    private static final GenericContainer<?> SOLR = new GenericContainer<>(SOLR_IMAGE)
            .withCopyFileToContainer(
                    MountableFile.forHostPath(JTS_JAR),
                    "/opt/solr/server/solr-webapp/webapp/WEB-INF/lib/" + JTS_JAR.getFileName())
            .withExposedPorts(8983)
            .waitingFor(Wait.forHttp("/solr/admin/info/system").forStatusCode(200))
            .withStartupTimeout(Duration.ofMinutes(3))
            .withCommand("solr-foreground");

    private static final int POSTGRES_PORT = 5432;

    private static final GenericContainer<?> POSTGIS = new GenericContainer<>(POSTGIS_IMAGE)
            .withEnv("POSTGRES_DB", "stations")
            .withEnv("POSTGRES_USER", "sisapp")
            .withEnv("POSTGRES_PASSWORD", "sis98")
            .withExposedPorts(POSTGRES_PORT)
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("test-data/appschema-indexes/stations_complex/postgis.sql"),
                    "/docker-entrypoint-initdb.d/postgis.sql")
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2))
            .withStartupTimeout(Duration.ofMinutes(3));

    private static final Set<String> CORES = ConcurrentHashMap.newKeySet();

    private TestContainersSupport() {}

    public static Properties offlineFixture() {
        Properties fixture = new Properties();
        fixture.setProperty("skip.on.failure", "false");
        return fixture;
    }

    public static String solrServerUrl() {
        startSolr();
        return "http://%s:%d/solr".formatted(SOLR.getHost(), SOLR.getMappedPort(8983));
    }

    public static String solrCoreUrl(String core) {
        ensureSolrCore(core);
        return solrServerUrl() + "/" + core;
    }

    public static String postgresHost() {
        startPostgis();
        return POSTGIS.getHost();
    }

    public static String postgresPort() {
        startPostgis();
        return Integer.toString(POSTGIS.getMappedPort(POSTGRES_PORT));
    }

    public static String postgresDatabase() {
        startPostgis();
        return "stations";
    }

    public static String postgresUser() {
        startPostgis();
        return "sisapp";
    }

    public static String postgresPassword() {
        startPostgis();
        return "sis98";
    }

    private static synchronized void startSolr() {
        if (!SOLR.isRunning()) {
            SOLR.start();
        }
    }

    private static synchronized void startPostgis() {
        if (!POSTGIS.isRunning()) {
            POSTGIS.start();
        }
    }

    private static synchronized void ensureSolrCore(String core) {
        if (CORES.contains(core)) {
            return;
        }
        startSolr();
        try {
            Container.ExecResult result = SOLR.execInContainer("solr", "create", "-p", "8983", "-c", core);
            if (result.getExitCode() != 0) {
                throw new RuntimeException(
                        "Error creating Solr core '%s': %s %s".formatted(core, result.getStdout(), result.getStderr()));
            }
            CORES.add(core);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException("Error creating Solr core '%s'.".formatted(core), exception);
        }
    }

    private static Path jarPath(Class<?> type) {
        try {
            return Path.of(
                    type.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException exception) {
            throw new RuntimeException("Error locating jar for '%s'.".formatted(type.getName()), exception);
        }
    }
}
