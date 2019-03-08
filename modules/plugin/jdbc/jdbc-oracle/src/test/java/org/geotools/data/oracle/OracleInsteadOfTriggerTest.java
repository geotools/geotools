/* (c) 2019 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.data.oracle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCFeatureStoreOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/** @author ImranR */
public class OracleInsteadOfTriggerTest extends JDBCFeatureStoreOnlineTest {

    private OracleTestSetup oracleTestSetup;

    @Override
    protected JDBCTestSetup createTestSetup() {
        oracleTestSetup = new OracleTestSetup();

        return oracleTestSetup;
    }

    protected void connect() throws Exception {
        fixture.put(JDBCDataStoreFactory.PK_METADATA_TABLE, "GT_PK_METADATA");
        super.connect();

        setUptestInsteadOfTriggerInsert();
    }

    private void setUptestInsteadOfTriggerInsert() throws Exception {
        // set testInsteadOfTriggerInsert

        // create sequence
        oracleTestSetup.run("CREATE SEQUENCE POI_ID");
        // create actual table
        oracleTestSetup.run(
                "CREATE TABLE POI_T "
                        + "("
                        + "    POI_ID  NUMBER(10),"
                        + "    NAME VARCHAR2(255 CHAR),"
                        + "    LOCATION SDO_GEOMETRY, "
                        + "    PRIMARY KEY (POI_ID)"
                        + ")");

        // create view POI_V
        oracleTestSetup.run("CREATE OR REPLACE VIEW POI_V AS SELECT T.* FROM POI_T T");
        // insert view entry into GT_PK_METADATA with pfsequence policy
        oracleTestSetup.run(
                "INSERT INTO GT_PK_METADATA values (user,'POI_V', 'POI_ID', 1, 'pfsequence','POI_ID')");

        // create triggers
        // first for insert
        oracleTestSetup.run(
                "CREATE OR REPLACE TRIGGER POI_INS INSTEAD OF INSERT ON POI_V FOR EACH ROW "
                        + "BEGIN"
                        + "    INSERT INTO POI_T "
                        + "    ("
                        + "        POI_ID, "
                        + "        NAME,"
                        + "        LOCATION"
                        + "    ) "
                        + "    VALUES "
                        + "    ("
                        + "        :NEW.POI_ID,"
                        + "        :NEW.NAME,"
                        + "        :NEW.LOCATION"
                        + "    ); "
                        + "END;");

        // then for delete
        oracleTestSetup.run(
                "CREATE OR REPLACE TRIGGER POI_DEL INSTEAD OF DELETE ON POI_V FOR EACH ROW "
                        + "BEGIN"
                        + "  DELETE FROM POI_T"
                        + "  WHERE POI_ID=:OLD.POI_ID "
                        + "END;");
        // for update
        oracleTestSetup.run(
                "CREATE OR REPLACE TRIGGER POI_UPD INSTEAD OF UPDATE ON POI_V FOR EACH ROW "
                        + "BEGIN "
                        + "    UPDATE POI_T"
                        + "    SET "
                        + "      NAME=:NEW.NAME,"
                        + "      LOCATION=:NEW.LOCATION"
                        + "    WHERE POI_ID=:OLD.POI_ID"
                        + "END;");

        // USER_SDO_GEOM_METADATA entry
        oracleTestSetup.run(
                "INSERT INTO USER_SDO_GEOM_METADATA VALUES ('POI_T', 'LOCATION', MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('Long',-180,180,0.000018),MDSYS.SDO_DIM_ELEMENT('Lat',-90,90,0.000018)), 4326)");
        // create spatial index
        oracleTestSetup.run(
                "CREATE INDEX POI_LOC_IDX ON POI_T(LOCATION) INDEXTYPE IS MDSYS.SPATIAL_INDEX PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=POINT')");

        oracleTestSetup.run(
                "INSERT INTO GT_GEOM_METADATA values (user,'POI_V', 'LOCATION', 2, 4326, 'POINT')");
    }

    private void tearDownInsteadOfTriggerInsert() throws Exception {
        oracleTestSetup.dropView("POI_V");
        oracleTestSetup.deleteSpatialTable("POI_T");
        oracleTestSetup.run("DROP SEQUENCE POI_ID");
        oracleTestSetup.run("DROP TRIGGER POI_INS");
        oracleTestSetup.run("DROP TRIGGER POI_UPD");
        oracleTestSetup.run("DROP TRIGGER POI_DEL");
        oracleTestSetup.run("DROP INDEX POI_LOC_IDX");

        oracleTestSetup.run("DELETE FROM GT_PK_METADATA where TABLE_NAME ='POI_V'");
        oracleTestSetup.run("DELETE FROM GT_GEOM_METADATA where F_TABLE_NAME ='POI_V'");
        oracleTestSetup.getDataSource().getConnection().commit();
    }

    @Override
    protected void tearDownInternal() throws Exception {

        super.tearDownInternal();
        tearDownInsteadOfTriggerInsert();
    }

    // https://osgeo-org.atlassian.net/projects/GEOT/issues/GEOT-5989
    public void testInsteadOfTriggerInsert() throws IOException {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        // get the view

        // SimpleFeatureSource source =  (JDBCFeatureSource)
        // dataStore.getFeatureSource(tname("BUSINESS_POI"),Transaction.AUTO_COMMIT);
        SimpleFeatureStore featureStore =
                (SimpleFeatureStore) dataStore.getFeatureSource(tname("POI_V"));
        assertNotNull(featureStore);
        SimpleFeatureType type = featureStore.getSchema();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);

        featureBuilder.set("NAME", "first feature");
        Point point = geometryFactory.createPoint(new Coordinate(73, 33));
        point.setSRID(4326);
        featureBuilder.set("LOCATION", point);

        SimpleFeature featureToInsert = featureBuilder.buildFeature(null);

        featureBuilder.set("NAME", "second feature");
        Point point2 = geometryFactory.createPoint(new Coordinate(23, 73));
        point2.setSRID(4326);
        featureBuilder.set("LOCATION", point2);

        SimpleFeature featureToInsert2 = featureBuilder.buildFeature(null);
        List<SimpleFeature> features =
                new ArrayList<SimpleFeature>(Arrays.asList(featureToInsert, featureToInsert2));

        // NOW INSERTING
        // Transaction transaction = new DefaultTransaction("create");
        // SimpleFeatureStore featureStore = (SimpleFeatureStore) source; //for writing
        //   featureStore.setTransaction(transaction);
        SimpleFeatureCollection fcollection = new ListFeatureCollection(type, features);

        featureStore.addFeatures(fcollection);

        // transaction.commit();

        // there should be two features inserted
        assertTrue(featureStore.getFeatures().size() == 2);
    }
}
