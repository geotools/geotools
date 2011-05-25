package org.geotools.data.directory;

import java.io.File;
import java.io.IOException;

import org.geotools.data.DataStore;

/**
 * A delegate that finds the files managed by the directory store and
 * @author Andrea Aime - OpenGeo
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/data/src/main/java/org/geotools/data/directory/FileStoreFactory.java $
 */
public interface FileStoreFactory {
    
    /**
     * Returns a store for the specified file
     * @param file
     * @return
     */
    DataStore getDataStore(File file) throws IOException;
}
