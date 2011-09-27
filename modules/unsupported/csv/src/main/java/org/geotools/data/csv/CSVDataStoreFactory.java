package org.geotools.data.csv;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.util.KVP;

/**
 * 
 *
 * @source $URL$
 */
public class CSVDataStoreFactory implements DataStoreFactorySpi {

    private static final String FILE_TYPE = "csv";

    public static final Param FILE_PARAM = new Param("file", File.class,
            FILE_TYPE + " file",true, null,
            new KVP(Param.EXT,FILE_TYPE));
    
    public String getDisplayName() {
        return FILE_TYPE.toUpperCase();
    }

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
                return file.getPath().toLowerCase().endsWith("." + FILE_TYPE);
            }
        } catch (IOException e) {
            // ignore
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
        File file = (File) FILE_PARAM.lookUp(params);
        return new CSVDataStore(file);
    }

    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        return null;
    }

}
