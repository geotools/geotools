package org.geotools.data.oracle;

import java.util.HashMap;
import java.util.logging.Logger;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.jdbc.JDBCPrimaryKeyFinderOnlineTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class OraclePrimaryKeyFinderOnlineTest extends JDBCPrimaryKeyFinderOnlineTest {

    static final Logger LOGGER = Logging.getLogger(OraclePrimaryKeyFinderOnlineTest.class);

    @Override
    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new OraclePrimaryKeyFinderTestSetup();
    }

    @Override
    protected HashMap createDataStoreFactoryParams() throws Exception {
        HashMap params = super.createDataStoreFactoryParams();
        params.put(JDBCDataStoreFactory.PK_METADATA_TABLE.key, "GT_PK_METADATA");
        return params;
    }

    public void testSequencedPrimaryKey() throws Exception {
        // get a dump of what's in the table
        System.out.println("Dumping seqtable contents");
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("seqtable"));
        fs.getFeatures()
                .accepts(
                        new FeatureVisitor() {
                            @Override
                            public void visit(Feature feature) {
                                System.out.println(feature);
                            }
                        },
                        null);

        super.testSequencedPrimaryKey();
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

        LOGGER.info("new feature has fid: " + f.getUserData().get("fid"));

        // pattern match to handle the multi primary key case
        String regex = tname(featureType.getTypeName()) + ".4(\\..*)?";
        assertTrue(
                "actual fid: " + f.getUserData().get("fid") + " does not match expected " + regex,
                ((String) f.getUserData().get("fid")).matches(regex));
    }
}
