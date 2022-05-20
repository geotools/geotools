package org.geotools.data.hana;

import java.io.IOException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * Test setup base class that uses a static connection pool without prepared statement pooling.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class HanaTestSetupNonPSPooling extends HanaTestSetupBase {

    private static volatile BasicDataSource dataSource;

    @Override
    public DataSource getDataSource() throws IOException {
        BasicDataSource ds = dataSource;
        if (ds == null) {
            synchronized (HanaTestSetupBase.class) {
                ds = dataSource;
                if (ds == null) {
                    ds = new BasicDataSource();
                    setCommonDataSourceOptions(ds);
                    ds.setPoolPreparedStatements(false);
                    ds.setMaxActive(4);
                }
                dataSource = ds;
            }
        }
        return ds;
    }
}
