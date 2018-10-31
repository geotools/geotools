/* GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2010-2014, Open Source Geospatial Foundation (OSGeo)
 *
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file. Use it well and enjoy!
 */
// header start
package org.geotools.tutorial.csv2;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.util.KVP;
import org.geotools.util.logging.Logging;

/** Provide access to CSV Files. */
public class CSVDataStoreFactory implements DataStoreFactorySpi {
    /**
     * Public "no argument" constructor called by Factory Service Provider (SPI) entry listed in
     * META-INF/services/org.geotools.data.DataStoreFactorySPI
     */
    public CSVDataStoreFactory() {}

    /** No implementation hints required at this time */
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    // definition end
    // metadata start
    public String getDisplayName() {
        return "CSV";
    }

    public String getDescription() {
        return "Comma delimited text file.";
    }

    /** Confirm DataStore availability, null if unknown */
    Boolean isAvailable = null;

    /**
     * Test to see if this DataStore is available, for example if it has all the appropriate
     * libraries to construct an instance.
     *
     * <p>This method is used for interactive applications, so as to not advertise support for
     * formats that will not function.
     *
     * @return <tt>true</tt> if and only if this factory is available to create DataStores.
     */
    public synchronized boolean isAvailable() {
        if (isAvailable == null) {
            try {
                Class<?> cvsReaderType = Class.forName("com.csvreader.CsvReader");
                isAvailable = cvsReaderType != null;
            } catch (ClassNotFoundException e) {
                isAvailable = false;
            }
        }
        return isAvailable;
    }

    // metadata end

    // getParametersInfo start
    /** Parameter description of information required to connect */
    public static final Param FILE_PARAM =
            new Param(
                    "file",
                    File.class,
                    "Comma seperated value file",
                    true,
                    null,
                    new KVP(Param.EXT, "csv"));

    public Param[] getParametersInfo() {
        return new Param[] {FILE_PARAM};
    }

    // getParametersInfo end
    // canProcess start
    /**
     * Works for csv file.
     *
     * @param params connection parameters
     * @return true for connection parameters indicating a csv file
     */
    public boolean canProcess(Map<String, Serializable> params) {
        try {
            File file = (File) FILE_PARAM.lookUp(params);
            if (file != null) {
                return file.getPath().toLowerCase().endsWith(".csv");
            }
        } catch (IOException e) {
            // ignore as we are expected to return true or false
        }
        return false;
    }

    // canProcess end

    // createDataStore start
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        File file = (File) FILE_PARAM.lookUp(params);
        return new CSVDataStore(file);
    }
    // createDataStore end

    private static final Logger LOGGER = Logging.getLogger(CSVDataStoreFactory.class);

    // createNewDataStore start
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        File file = (File) FILE_PARAM.lookUp(params);
        if (file.exists()) {
            LOGGER.warning("File already exsists: " + file);
        }
        return new CSVDataStore(file);
    }
    // createNewDataStore end

}
