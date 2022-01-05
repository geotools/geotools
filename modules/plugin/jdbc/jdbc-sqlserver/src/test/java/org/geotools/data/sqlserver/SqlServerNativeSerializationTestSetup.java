package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCDataStore;

public class SqlServerNativeSerializationTestSetup extends SQLServerTestSetup {

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        SQLServerDialect dialect = (SQLServerDialect) dataStore.getSQLDialect();
        dialect.setUseNativeSerialization(Boolean.TRUE);
        super.setUpDataStore(dataStore);
    }
}
