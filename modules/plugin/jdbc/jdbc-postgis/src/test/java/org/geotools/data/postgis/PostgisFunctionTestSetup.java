package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCDataStore;

public class PostgisFunctionTestSetup extends PostGISTestSetup {
    
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        
        // the unit tests needs function encoding enabled to actually test that
        ((PostGISDialect) dataStore.getSQLDialect()).setFunctionEncodingEnabled(true);
    }

}
