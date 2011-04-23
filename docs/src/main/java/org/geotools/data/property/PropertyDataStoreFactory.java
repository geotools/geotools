package org.geotools.data.property;

import java.awt.RenderingHints;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;

/**
 * DataStore factory that creates {@linkplain org.geotools.data.property.PropertyDataStore}s
 * 
 * @author Jody Garnett
 */
public class PropertyDataStoreFactory implements DataStoreFactorySpi {

    /**
     * Public "no arguments" constructor called by Factory Service Provider (SPI) based on entry in
     * META-INF/services/org.geotools.data.DataStoreFactorySpi
     */
    public PropertyDataStoreFactory() {
    }

    /**
     * No implementation hints are provided at this time.
     */
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    // definition end

    // metadata start
    public String getDisplayName() {
        return "Properties";
    }

    public String getDescription() {
        return "Allows access to Java Property files containing Feature information";
    }

    /**
     * Test to see if this datastore is available, if it has all the appropriate libraries to
     * construct a datastore. This datastore just returns true for now.
     * 
     * This method is used for interactive applications, so as to not advertise data store
     * capabilities they don't actually have.
     * 
     * @return <tt>true</tt> if and only if this factory is available to create DataStores.
     * 
     * @task <code>true</code> property datastore is always available
     */
    public boolean isAvailable() {
        return true;
    }

    // metadata end

    // getParametersInfo start
    public static final Param DIRECTORY = new Param("directory", File.class,
            "Directory containting property files", true);

    public static final Param NAMESPACE = new Param("namespace", String.class,
            "namespace of datastore", false);

    /**
     * @see #DIRECTORY
     * @see PropertyDataStoreFactory#NAMESPACE
     */
    public Param[] getParametersInfo() {
        return new Param[] { DIRECTORY, NAMESPACE };
    }

    // getParametersInfo end

    // canProcess start
    /**
     * Works for a file directory or property file
     * 
     * @param params Connection parameters
     * 
     * @return true for connection parameters indicating a directory or property file
     */
    public boolean canProcess(Map<String, Serializable> params) {
        try {
            directoryLookup(params);
            return true;
        } catch (Exception erp) {
            return false; // can't process, just return false
        }
    }

    /**
     * Lookups the directory containing property files in the params argument, and returns the
     * corresponding <code>java.io.File</code>.
     * <p>
     * The file is first checked for existence as an absolute path in the filesystem. If such a
     * directory is not found, then it is treated as a relative path, taking Java system property
     * <code>"user.dir"</code> as the base.
     * </p>
     * 
     * @param params
     * @throws IllegalArgumentException if directory is not a directory.
     * @throws FileNotFoundException if directory does not exists
     * @throws IOException if {@linkplain #DIRECTORY} doesn't find parameter in <code>params</code>
     *         file does not exists.
     */
    private File directoryLookup(Map<String, java.io.Serializable> params) throws IOException,
            FileNotFoundException, IllegalArgumentException {
        File file = (File) DIRECTORY.lookUp(params);
        if (!file.exists()) {
            File currentDir = new File(System.getProperty("user.dir"));
            String path = DIRECTORY.lookUp(params).toString();
            file = new File(currentDir, path);
            if (!file.exists()) {
                throw new FileNotFoundException(file.getAbsolutePath());
            }
        }
        if (file.isDirectory()) {
            return file;
        } else {
            // check if they pointed to a properties file; and use the parent directory
            if (file.getPath().endsWith(".properties")) {
                return file.getParentFile();
            } else {
                throw new IllegalArgumentException(file.getAbsolutePath() + " is not a directory");
            }
        }
    }

    // canProcess end

    // createDataStore start
    public DataStore createDataStore(Map<String, java.io.Serializable> params) throws IOException {
        File dir = directoryLookup(params);
        String namespaceURI = (String) NAMESPACE.lookUp(params);
        if (dir.exists() && dir.isDirectory()) {
            return new PropertyDataStore(dir, namespaceURI);
        } else {
            throw new IOException("Directory is required");
        }
    }
    public DataStore createNewDataStore(Map<String, java.io.Serializable> params)
            throws IOException {
        File dir = directoryLookup(params);

        if (dir.exists()) {
            throw new IOException(dir + " already exists");
        }

        boolean created;

        created = dir.mkdir();

        if (!created) {
            throw new IOException("Could not create the directory" + dir);
        }

        String namespaceURI = (String) NAMESPACE.lookUp(params);
        return new PropertyDataStore(dir, namespaceURI);
    }

    // createDataStore end

}