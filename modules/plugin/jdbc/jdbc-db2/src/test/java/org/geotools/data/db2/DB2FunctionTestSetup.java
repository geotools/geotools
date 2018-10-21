package org.geotools.data.db2;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.SQLDialect;

public class DB2FunctionTestSetup extends DB2TestSetup {

    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);

        // the unit tests needs function encoding enabled to actually test that
        SQLDialect dialect = dataStore.getSQLDialect();
        if (dialect instanceof DB2SQLDialectBasic)
            ((DB2SQLDialectBasic) dialect).setFunctionEncodingEnabled(true);
        if (dialect instanceof DB2SQLDialectPrepared)
            ((DB2SQLDialectPrepared) dialect).setFunctionEncodingEnabled(true);
    }
}
