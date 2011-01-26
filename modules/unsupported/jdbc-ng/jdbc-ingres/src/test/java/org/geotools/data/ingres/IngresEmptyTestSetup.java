package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCEmptyTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class IngresEmptyTestSetup extends JDBCEmptyTestSetup {

	public IngresEmptyTestSetup(JDBCTestSetup delegate) {
		super(delegate);
	}

	@Override
	protected void createEmptyTable() throws Exception {
        run("CREATE TABLE empty(key int auto_increment primary key, geom GEOMETRY)");
	}

	@Override
	protected void dropEmptyTable() throws Exception {
        runSafe("DROP TABLE empty");
	}

}
