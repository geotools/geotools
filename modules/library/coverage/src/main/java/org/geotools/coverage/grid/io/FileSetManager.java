package org.geotools.coverage.grid.io;

import java.util.List;

/**
 * 
 * @author Daniele Romagnoli, GeoSolutions SaS
 *
 */
public interface FileSetManager {

    /** 
     * Add a file to the file set manager 
     */
    void addFile(final String filePath);

    /** 
     * Return the list of all the files currently added to the manager
     */
    List<String> list();

    /**
     * Remove a file from the manager (An implementation may also physically remove the file)
     */
    void removeFile(final String filePath);

    /**
     * Remove all the files from the manager (An implementation may also physically remove all the files)
     */
    void purge();
}
