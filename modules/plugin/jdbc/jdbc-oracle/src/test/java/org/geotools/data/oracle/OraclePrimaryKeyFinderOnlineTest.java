package org.geotools.data.oracle;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.jdbc.JDBCPrimaryKeyFinderOnlineTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public class OraclePrimaryKeyFinderOnlineTest extends JDBCPrimaryKeyFinderOnlineTest {

    static final Logger LOGGER = Logging.getLogger(OraclePrimaryKeyFinderOnlineTest.class);

    @Override
    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new OraclePrimaryKeyFinderTestSetup();
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put(JDBCDataStoreFactory.PK_METADATA_TABLE.key, "GT_PK_METADATA");
        return params;
    }

    /** override to add failure diagnostic message. */
    @Override
    protected void addFeature(SimpleFeatureType featureType, JDBCFeatureStore features)
            throws Exception {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureType);
        b.add("four");
        b.add(new GeometryFactory().createPoint(new Coordinate(4, 4)));

        SimpleFeature f = b.buildFeature(null);
        features.addFeatures(DataUtilities.collection(f));

        LOGGER.info("new feature has fid: " + f.getUserData().get("id"));

        // pattern match to handle the multi primary key case
        assertTrue(
                "actual fid: "
                        + f.getUserData().get("id")
                        + " does not match expected "
                        + tname(featureType.getTypeName())
                        + ".4(\\..*)?",
                ((String) f.getUserData().get("fid"))
                        .matches(tname(featureType.getTypeName()) + ".4(\\..*)?"));
    }
}
