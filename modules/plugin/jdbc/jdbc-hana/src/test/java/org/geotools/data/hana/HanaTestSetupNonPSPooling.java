package org.geotools.data.hana;

import java.io.IOException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * Test setup base class that uses a static connection pool without prepared statement pooling.
 *
 * @author Stefan Uhrig, SAP SE
 */
@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
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
                    ds.setMaxActive(8);
                }
                dataSource = ds;
            }
        }
        return ds;
    }
}
