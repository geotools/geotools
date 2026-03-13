/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.duckdb;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;

/**
 * Public DuckDB datastore factory that wraps the internal JDBC datastore to avoid exposing the raw JDBC store type to
 * regular callers.
 */
public class DuckDBDataStoreFactory extends ForwardingDataStoreFactory<DuckDBJDBCDataStoreFactory>
        implements DataStoreFactorySpi {

    public static final JDBCDataStoreFactory.Param IN_MEMORY = DuckDBJDBCDataStoreFactory.IN_MEMORY;

    public static final JDBCDataStoreFactory.Param DB_PATH = DuckDBJDBCDataStoreFactory.DB_PATH;

    public DuckDBDataStoreFactory() {
        super(new DuckDBJDBCDataStoreFactory());
    }

    public void setBaseDirectory(File baseDirectory) {
        delegate.setBaseDirectory(baseDirectory);
    }

    public File getBaseDirectory() {
        return delegate.getBaseDirectory();
    }

    @Override
    public DuckDBDataStore createDataStore(Map<String, ?> params) throws IOException {
        return new DuckDBDataStore(delegate.createDataStore(params));
    }

    @Override
    public DataStore createNewDataStore(Map<String, ?> params) throws IOException {
        return new DuckDBDataStore((JDBCDataStore) delegate.createNewDataStore(params));
    }
}
