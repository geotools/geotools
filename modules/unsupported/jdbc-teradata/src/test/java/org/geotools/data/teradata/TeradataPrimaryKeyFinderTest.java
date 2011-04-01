package org.geotools.data.teradata;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class TeradataPrimaryKeyFinderTest extends JDBCPrimaryKeyFinderTest {

    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new TeradataPrimaryKeyFinderTestSetup();
    }


    @Override
    public void testSequencedPrimaryKey() throws Exception {
      // sequences not a distinct type in teradata (at least as far as I can tell)
    }
}
