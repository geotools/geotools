package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCDataStoreAPITest;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;

public class IngresDataStoreAPITest extends JDBCDataStoreAPITest {

	@Override
	protected JDBCDataStoreAPITestSetup createTestSetup() {
		return new IngresDataStoreAPITestSetup(new IngresTestSetup());
	}

    @Override
    public void testGetFeatureWriterConcurrency() throws Exception {
        // skip, does not work with Ingres
    }

    @Override
    public void testTransactionIsolation() throws Exception {
        // skip, does not work with Ingres
    }
}
