package org.geoserver.data;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.geogit.repository.Repository;
import org.geogit.storage.RepositoryDatabase;
import org.geogit.storage.bdbje.EntityStoreConfig;
import org.geogit.storage.bdbje.EnvironmentBuilder;
import org.geogit.storage.bdbje.JERepositoryDatabase;
import org.geoserver.data.geogit.GeoGITRepositoryTestCase;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;

import com.sleepycat.je.Environment;
import com.vividsolutions.jts.io.ParseException;

public abstract class RepositoryTestCase extends TestCase {
    protected Repository repo;
    
    private File envHome;

    // prevent recursion
    private boolean setup = false;

    protected RepositoryDatabase repositoryDatabase;
    
    protected static final Logger LOGGER = Logging
            .getLogger(GeoGITRepositoryTestCase.class);

    @Override
    protected void setUp() throws Exception {
        if (setup) {
            throw new IllegalStateException("Are you calling super.setUp()!?");
        }
        setup = true;
        Logging.ALL.forceMonolineConsoleOutput();
        envHome = new File(new File("target"), "mockblobstore");
        final File repositoryHome = new File(envHome, "repository");
        final File indexHome = new File(envHome, "index");

        FileUtils.deleteDirectory(envHome);
        repositoryHome.mkdirs();
        indexHome.mkdirs();

        EntityStoreConfig config = new EntityStoreConfig();
        config.setCacheMemoryPercentAllowed(25);
        EnvironmentBuilder esb = new EnvironmentBuilder(config);
        Properties bdbEnvProperties = null;
        Environment environment;
        environment = esb.buildEnvironment(repositoryHome, bdbEnvProperties);

        Environment stagingEnvironment;
        stagingEnvironment = esb.buildEnvironment(indexHome, bdbEnvProperties);

        repositoryDatabase = new JERepositoryDatabase(environment, stagingEnvironment);

        // repositoryDatabase = new FileSystemRepositoryDatabase(envHome);

        repo = new Repository(repositoryDatabase, envHome);

        repo.create();

        setUpInternal();
    }

    @Override
    protected void tearDown() throws Exception {
        if (!setup) {
            throw new IllegalStateException("Are you calling super.setUp()!?");
        }
        setup = false;
        tearDownInternal();
        if (repo != null) {
            repo.close();
            repo = null;
        }
        repositoryDatabase = null;
        
        FileUtils.deleteDirectory(envHome);
    }
   
    protected abstract void setUpInternal() throws Exception;
    
    protected abstract void tearDownInternal() throws Exception;

    protected Feature feature(SimpleFeatureType type, String id, Object... values)
            throws ParseException {
                SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
                for (int i = 0; i < values.length; i++) {
                    Object value = values[i];
                    if (type.getDescriptor(i) instanceof GeometryDescriptor) {
                        if (value instanceof String) {
                            value = new WKTReader2().read((String) value);
                        }
                    }
                    builder.set(i, value);
                }
                return builder.buildFeature(id);
            }
}
