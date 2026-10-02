/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.dggs.clickhouse;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.MetadataTablePrimaryKeyFinder;
import org.geotools.jdbc.SQLDialect;

public class ClickHouseJDBCDataStoreFactory extends JDBCDataStoreFactory {
    public static final String DATABASE_ID = "clickhouse";
    public static final String DRIVER_CLASSNAME = "com.clickhouse.jdbc.ClickHouseDriver";
    /** @deprecated ignored by the jdbc-v2 driver, which always uses Apache HttpClient 5; no longer added to the URL */
    @Deprecated
    public static final String FORCE_HTTP_CLIENT = "http_connection_provider=HTTP_CLIENT";

    /**
     * Value of the {@code typeMappings} connection property; without it date/time values shift by the offset between
     * the JVM and server time zones. JNDI datasources must set it themselves.
     */
    public static final String DATE_TIME_MAPPINGS =
            "DateTime=java.time.ZonedDateTime,DateTime64=java.time.ZonedDateTime";

    // TODO: expose parameters with sensible defaults for port, user, password

    @Override
    protected String getDatabaseID() {
        return DATABASE_ID;
    }

    @Override
    protected String getDriverClassName() {
        return DRIVER_CLASSNAME;
    }

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new ClickHouseDialect(dataStore);
    }

    @Override
    protected String getValidationQuery() {
        return "select 1";
    }

    @Override
    public String getDescription() {
        return "Clickhouse alphanumeric datastore";
    }

    @Override
    public BasicDataSource createDataSource(Map<String, ?> params) throws IOException {
        BasicDataSource ds = super.createDataSource(params);
        // jdbc-v2 rejects unknown keys, server settings need the clickhouse_setting_ prefix
        ds.addConnectionProperty("clickhouse_setting_max_query_size", "1000000");
        ds.addConnectionProperty("socket_timeout", "300000");
        ds.addConnectionProperty("typeMappings", DATE_TIME_MAPPINGS);
        // jdbc-v2 throws on setAutoCommit(false)/commit/rollback, the store is read-only so no-ops are safe
        ds.addConnectionProperty("jdbc_ignore_unsupported_values", "true");
        return ds;
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map<String, ?> params) throws IOException {
        // jdbc-v2 reports the MergeTree sorting key as primary key, but it is not unique: ignore it, as 0.6.4 did
        dataStore.setPrimaryKeyFinder(new MetadataTablePrimaryKeyFinder());
        return dataStore;
    }
}
