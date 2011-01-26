package org.geotools.data.directory;

import java.io.File;
import java.io.IOException;

import org.geotools.data.DataStore;

/**
 * A delegate that finds the files managed by the directory store and
 * @author Andrea Aime - OpenGeo
 *
 */
public interface FileStoreFactory {
    
    /**
     * Returns a store for the specified file
     * @param file
     * @return
     */
    DataStore getDataStore(File file) throws IOException;
}
