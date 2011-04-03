package org.geotools.data.csv;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.util.KVP;

public class CSVDataStoreFactory implements DataStoreFactorySpi {
    /**
     * url to the .shp file.
     */
    public static final Param FILE_PARAM = new Param("file", File.class,
            ".csv file",true, null,
            new KVP(Param.EXT,"csv"));
    
    public String getDisplayName() {
        return "CSV";
    }
    /** Comma delimited text file */
    public String getDescription() {
        return "Comma delimited text file";
    }

    public Param[] getParametersInfo() {
        return new Param[]{ FILE_PARAM };
    }

    public boolean canProcess(Map<String, Serializable> params) {
        try {
            File file = (File) FILE_PARAM.lookUp(params);
            if ( file != null ){
                return file.getPath().endsWith(".csv") || file.getPath().endsWith("CSV");
            }
        } catch (IOException e) {
        }
        return false;
    }

    public boolean isAvailable() {
        return true;
    }

    public Map<Key, ?> getImplementationHints() {
        return null;
    }

    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        return null;
    }

    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        return null;
    }

}
