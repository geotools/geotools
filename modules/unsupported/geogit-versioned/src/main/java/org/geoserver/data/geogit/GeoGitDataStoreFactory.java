package org.geoserver.data.geogit;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.geogit.repository.Repository;
import org.geogit.storage.RepositoryDatabase;
import org.geogit.storage.bdbje.EntityStoreConfig;
import org.geogit.storage.bdbje.EnvironmentBuilder;
import org.geogit.storage.bdbje.JERepositoryDatabase;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;

import com.sleepycat.je.Environment;

public class GeoGitDataStoreFactory implements DataStoreFactorySpi {

    public static final Param USE_EMBEDDED_REPO = new Param("GEOGIT_EMBEDDED",
            Boolean.class, "Use Embedded GeoGIT Repository");

    public static final Param DEFAULT_NAMESPACE = new Param("namespace",
            String.class, "Default namespace", false);

    public static final Param DATA_ROOT = new Param("data_root", String.class,
            "Root directory for the versioned data store", false);

    public static final Param REPO_PATH = new Param("repo_path", String.class,
            "Path, within the data root, for the GeoGIT repository", false);

    public static final Param INDEX_PATH = new Param("index_path",
            String.class,
            "Path, within the data root, for the GeoGIT index repository",
            false);

    @Override
    public String getDisplayName() {
        return "GeoGIT";
    }

    @Override
    public String getDescription() {
        return "GeoGIT Versioning DataStore";
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[] { USE_EMBEDDED_REPO, DEFAULT_NAMESPACE };
    }

    @Override
    public boolean canProcess(Map<String, Serializable> params) {
        try {
            Object lookUp = USE_EMBEDDED_REPO.lookUp(params);
            return Boolean.TRUE.equals(lookUp);
        } catch (IOException e) {
            //
        }
        return false;
    }

    /**
     * @see org.geotools.data.DataAccessFactory#isAvailable()
     */
    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    @Override
    public DataStore createDataStore(Map<String, Serializable> params)
            throws IOException {
        Object lookUp = USE_EMBEDDED_REPO.lookUp(params);
        if (!Boolean.TRUE.equals(lookUp)) {
            throw new DataSourceException(USE_EMBEDDED_REPO.key
                    + " is not true");
        }

        String defaultNamespace = (String) DEFAULT_NAMESPACE.lookUp(params);

        String dataRootPath = (String) DATA_ROOT.lookUp(params);
        String repoPath = (String) REPO_PATH.lookUp(params);
        String indexPath = (String) INDEX_PATH.lookUp(params);

        final File dataRoot = new File(dataRootPath);
        final File geogitRepo = new File(dataRoot, repoPath);
        final File indexRepo = new File(dataRoot, indexPath);

        EnvironmentBuilder esb = new EnvironmentBuilder(new EntityStoreConfig());
        Properties bdbEnvProperties = null;
        Environment geogitEnv = esb.buildEnvironment(geogitRepo,
                bdbEnvProperties);
        Environment indexEnv = esb
                .buildEnvironment(indexRepo, bdbEnvProperties);

        RepositoryDatabase ggitRepoDb = new JERepositoryDatabase(geogitEnv,
                indexEnv);

        Repository repository = new Repository(ggitRepoDb, dataRoot);
        repository.create();

        // Repository repository = GEOGIT.get().getRepository();
        GeoGitDataStore store = new GeoGitDataStore(repository,
                defaultNamespace);
        return store;
    }

    /**
     * @see org.geotools.data.DataStoreFactorySpi#createNewDataStore(java.util.Map)
     */
    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params)
            throws IOException {
        throw new UnsupportedOperationException();
    }

}
