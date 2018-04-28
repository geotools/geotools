/* GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2010-2014, Open Source Geospatial Foundation (OSGeo)
 *
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file. Use it well and enjoy!
 */
// header start
package org.geotools.tutorial.csv;

import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import org.geotools.data.Query;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;

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
     * Allow read access to file; for our package visible "friends". Please close the reader when
     * done.
     *
     * @return CsvReader for file
     */
    CsvReader read() throws IOException {
        Reader reader = new FileReader(file);
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
