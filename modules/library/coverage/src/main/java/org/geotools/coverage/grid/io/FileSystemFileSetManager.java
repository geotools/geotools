package org.geotools.coverage.grid.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSystemFileSetManager implements FileSetManager{

    private static Logger LOGGER = Logger.getLogger(FileSystemFileSetManager.class.toString()); 

    private List<String> fileSet = Collections.synchronizedList(new ArrayList<String>());

    @Override
    public void addFile(String filePath) {
        fileSet.add(filePath);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Adding file " + filePath + " to the fileSet");
        }
    }

    @Override
    public List<String> list() {
        return Collections.unmodifiableList(fileSet);
    }

    @Override
    public void removeFile(String filePath) {
        final boolean contains = fileSet.contains(filePath);
        if (contains) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Removing file " + filePath + " to the fileSet and deleting it");
            }
            try {
                final File file = new File(filePath);
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File _file: files) {
                        deleteFile(_file);
                    }
                }
                deleteFile(file);
            } catch (Throwable t){
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Exception occurred while deleting file: " + filePath + 
                            "\n" + t.getLocalizedMessage());
                }
            }
        }
        if (contains) {
            fileSet.remove(filePath);
        }
    }

    private void deleteFile(File file) {
        boolean deleted = file.delete();
        if (!deleted && LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Unable to delete file " + file.getAbsolutePath());
        }
    }

    @Override
    public void purge() {
        if (!fileSet.isEmpty()) {
            String files[] = (String[])fileSet.toArray(new String[fileSet.size()]);
            for (String filePath: files) {
                removeFile(filePath);
            }
        }
    }

}
