package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class EmptyJDBCDataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {

	protected EmptyJDBCDataStoreAPITestSetup(JDBCTestSetup delegate) {
		super(delegate);
	}

	@Override
	protected void dropRoadTable() throws Exception {
	}

	@Override
	protected void dropRiverTable() throws Exception {
	}

	@Override
	protected void dropLakeTable() throws Exception {
	}

	@Override
	protected void dropBuildingTable() throws Exception {
	}

	@Override
	protected void createRoadTable() throws Exception {
	}

	@Override
	protected void createRiverTable() throws Exception {
	}

	@Override
	protected void createLakeTable() throws Exception {
	}
}