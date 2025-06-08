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

import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import org.geotools.api.data.Query;
import org.geotools.api.feature.type.Name;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;

/**
 * DataStore for Comma Seperated Value (CSV) files.
 *
 * @author Jody Garnett (Boundless)
 */
public class CSVDataStore extends ContentDataStore {
    // header end

    // constructor start
    File file;

    public CSVDataStore(File file) {
        this.file = file;
    }
    // constructor end

    // reader start
    /**
     * Allow read access to file; for our package visible "friends". Please close the reader when done.
     *
     * @return CsvReader for file
     */
    CsvReader read() throws IOException {
        Reader reader = new FileReader(file, StandardCharsets.UTF_8);
        CsvReader csvReader = new CsvReader(reader);
        return csvReader;
    }
    // reader end

    // createTypeNames start
    protected List<Name> createTypeNames() throws IOException {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf('.'));

        Name typeName = new NameImpl(name);
        return Collections.singletonList(typeName);
    }
    // createTypeNames end

    // createFeatureSource start
    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return new CSVFeatureSource(entry, Query.ALL);
    }
    // createFeatureSource end
}
