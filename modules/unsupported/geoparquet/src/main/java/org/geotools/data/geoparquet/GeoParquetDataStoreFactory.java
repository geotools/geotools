/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geoparquet;

import java.io.IOException;
import java.util.Map;
import org.geotools.api.data.DataAccessFactory;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.jdbc.JDBCDataStore;

/**
 * DataStoreFactory for GeoParquet files, powered by DuckDB.
 *
 * <p>This factory creates DataStore instances that can read and query GeoParquet format files, both local and remote.
 * GeoParquet is an open format for geospatial data that builds on the Apache Parquet columnar storage format, providing
 * efficient access to large geospatial datasets.
 *
 * <p>The implementation uses DuckDB and its extensions (spatial, parquet, httpfs) to handle the heavy lifting of
 * reading and querying Parquet files. This provides excellent performance and compatibility with various storage
 * backends including local files, HTTP/HTTPS, and S3.
 *
 * <p>Usage example:
 *
 * <pre>{@code
 * Map<String, Object> params = new HashMap<>();
 * params.put("dbtype", "geoparquet");
 * params.put("uri", "file:/path/to/data.parquet");
 *
 * DataStore store = DataStoreFinder.getDataStore(params);
 * }</pre>
 */
public class GeoParquetDataStoreFactory extends ForwardingDataStoreFactory<GeoParquetDataStoreFactoryDelegate>
        implements DataStoreFactorySpi {

    static final String GEOPARQUET = GeoParquetDataStoreFactoryDelegate.GEOPARQUET;

    public static final DataAccessFactory.Param DBTYPE = GeoParquetDataStoreFactoryDelegate.DBTYPE;
    public static final Param NAMESPACE = GeoParquetDataStoreFactoryDelegate.NAMESPACE;

    public static final DataAccessFactory.Param URI_PARAM = GeoParquetDataStoreFactoryDelegate.URI_PARAM;
    public static final DataAccessFactory.Param MAX_HIVE_DEPTH = GeoParquetDataStoreFactoryDelegate.MAX_HIVE_DEPTH;
    public static final Param FETCHSIZE = GeoParquetDataStoreFactoryDelegate.FETCHSIZE;
    public static final Param SCREENMAP = GeoParquetDataStoreFactoryDelegate.SCREENMAP;
    public static final Param SIMPLIFY = GeoParquetDataStoreFactoryDelegate.SIMPLIFY;
    public static final Param USE_AWS_CREDENTIAL_CHAIN = GeoParquetDataStoreFactoryDelegate.USE_AWS_CREDENTIAL_CHAIN;
    public static final Param AWS_REGION = GeoParquetDataStoreFactoryDelegate.AWS_REGION;
    public static final Param AWS_PROFILE = GeoParquetDataStoreFactoryDelegate.AWS_PROFILE;

    public GeoParquetDataStoreFactory() {
        super(new GeoParquetDataStoreFactoryDelegate());
    }

    @Override
    public GeoparquetDataStore createDataStore(Map<String, ?> params) throws IOException {
        JDBCDataStore delegateStore = delegate.createDataStore(params);
        return new GeoparquetDataStore(delegateStore);
    }
}
