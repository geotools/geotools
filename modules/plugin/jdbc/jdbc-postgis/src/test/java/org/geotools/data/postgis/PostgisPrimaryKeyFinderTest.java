package org.geotools.data.postgis;

import java.util.HashMap;

import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class PostgisPrimaryKeyFinderTest extends JDBCPrimaryKeyFinderTest {

    @Override
    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new PostgisPrimaryKeyFinderTestSetup();
    }

	@Override
	protected HashMap createDataStoreFactoryParams() throws Exception {
		HashMap params = super.createDataStoreFactoryParams();
		params.put(JDBCDataStoreFactory.PK_METADATA_TABLE.key, "gt_pk_metadata");
		return params;
	}

}
