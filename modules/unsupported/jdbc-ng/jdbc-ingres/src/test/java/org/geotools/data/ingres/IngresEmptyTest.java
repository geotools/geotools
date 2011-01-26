package org.geotools.data.ingres;

import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCEmptyTest;
import org.geotools.jdbc.JDBCEmptyTestSetup;

public class IngresEmptyTest extends JDBCEmptyTest {

	@Override
	protected JDBCEmptyTestSetup createTestSetup() {
		return new IngresEmptyTestSetup(new IngresTestSetup());
	}
}
