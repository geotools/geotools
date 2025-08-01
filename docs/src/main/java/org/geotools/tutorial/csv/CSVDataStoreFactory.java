/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
// header start
package org.geotools.tutorial.csv;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.util.KVP;

/** Provide access to CSV Files. */
public class CSVDataStoreFactory implements DataStoreFactorySpi {
    /**
     * Public "no argument" constructor called by Factory Service Provider (SPI) entry listed in
     * META-INF/services/org.geotools.data.DataStoreFactorySPI
     */
    public CSVDataStoreFactory() {}

    /** No implementation hints required at this time */
    @Override
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    // definition end
    // metadata start
    @Override
    public String getDisplayName() {
        return "CSV";
    }

    @Override
    public String getDescription() {
        return "Comma delimited text file.";
    }

    /** Confirm DataStore availability, null if unknown */
    Boolean isAvailable = null;

    /**
     * Test to see if this DataStore is available, for example if it has all the appropriate libraries to construct an
     * instance.
     *
     * <p>This method is used for interactive applications, so as to not advertise support for formats that will not
     * function.
     *
     * @return <tt>true</tt> if and only if this factory is available to create DataStores.
     */
    @Override
    public synchronized boolean isAvailable() {
        if (isAvailable == null) {
            try {
                Class cvsReaderType = Class.forName("com.csvreader.CsvReader");
                isAvailable = true;
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
            new Param("file", File.class, "Comma seperated value file", true, null, new KVP(Param.EXT, "csv"));

    @Override
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
    @Override
    public boolean canProcess(Map<String, ?> params) {
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
    @Override
    public DataStore createDataStore(Map<String, ?> params) throws IOException {
        File file = (File) FILE_PARAM.lookUp(params);
        return new CSVDataStore(file);
    }

    // createDataStore end

    // createNewDataStore start
    @Override
    public DataStore createNewDataStore(Map<String, ?> params) throws IOException {
        throw new UnsupportedOperationException("CSV Datastore is read only");
    }
    // createNewDataStore end

}
