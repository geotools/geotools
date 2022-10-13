package org.geotools.data.hana;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.jdbc.SQLDialect;
import org.junit.Test;

public class HanaSelectHintOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new HanaTestSetupPSPooling();
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put("SELECT Hints", "MYHINT1, MYHINT2");
        return params;
    }

    @Test
    public void testSelectHint() throws Exception {
        SQLDialect dialect = dataStore.getSQLDialect();
        StringBuffer sql = new StringBuffer();
        dialect.handleSelectHints(sql, null, null);
        assertEquals(" WITH HINT( MYHINT1, MYHINT2 )", sql.toString());
    }
}
