package org.geotools.data.sqlserver;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.jdbc.JDBCDataStore;

import java.util.Properties;

/**
 *
 *
 * @source $URL$
 */
public class SqlServerNativeSerializationTestSetup extends SQLServerTestSetup {

    protected void setUpDataStore(JDBCDataStore dataStore) {
        SQLServerDialect dialect = (SQLServerDialect) dataStore.getSQLDialect();
        dialect.setUseNativeSerialization(Boolean.TRUE);
        super.setUpDataStore(dataStore);
    }
}
