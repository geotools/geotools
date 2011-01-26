package org.geotools.data.ingres;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.jdbc.JDBCFeatureReaderTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

public class IngresFeatureReaderTest extends JDBCFeatureReaderTest {

	@Override
	protected JDBCTestSetup createTestSetup() {
		return new IngresTestSetup();
	}
}
