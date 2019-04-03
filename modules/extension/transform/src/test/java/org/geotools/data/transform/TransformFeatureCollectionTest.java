/* (c) 2016 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.data.transform;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.NameImpl;
import org.geotools.feature.visitor.BoundsVisitor;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.util.ProgressListener;

public class TransformFeatureCollectionTest {

    public static SimpleFeatureSource SOURCE;
    protected static String STORE_NAME = "states";

    static class TransformFeatureStoreWrapper extends TransformFeatureStore {

        private DataStore datastore;
        private boolean passedDown = false;

        class TransformFeatureCollectionWrapper extends TransformFeatureCollection {

            public TransformFeatureCollectionWrapper(
                    SimpleFeatureSource source, Transformer transformer, Query query) {
                super(source, transformer, query);
            }

            @Override
            protected void delegateVisitor(FeatureVisitor visitor, ProgressListener progress)
                    throws IOException {
                passedDown = true;
                super.delegateVisitor(visitor, progress);
            }
        }

        public TransformFeatureStoreWrapper(
                SimpleFeatureStore store,
                Name name,
                List<Definition> definitions,
                DataStore datastore)
                throws IOException {
            super(store, name, definitions);
            this.datastore = datastore;
        }

        public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
            return datastore;
        }

        @Override
        public SimpleFeatureCollection getFeatures() throws IOException {
            return getFeatures(Query.ALL);
        }

        @Override
        public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
            return getFeatures(new Query(transformer.getSchema().getTypeName(), filter));
        }

        @Override
        public SimpleFeatureCollection getFeatures(Query query) throws IOException {
            return new TransformFeatureCollectionWrapper(this, transformer, query);
        }

        /** @return the passedDown */
        public boolean isPassedDown() {
            return passedDown;
        }

        /** @param passedDown the passedDown to set */
        public void setPassedDown(boolean passedDown) {
            this.passedDown = passedDown;
        }
    }

    @BeforeClass
    public static void setup() throws Exception {
        // just to make sure the loggin is not going to cause exceptions when turned on
        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        handler.setLevel(java.util.logging.Level.FINE);
        Logging.getLogger(TransformFeatureCollectionTest.class)
                .setLevel(java.util.logging.Level.FINE);

        PropertyDataStore pds =
                new PropertyDataStore(new File("./src/test/resources/org/geotools/data/transform"));
        SOURCE = pds.getFeatureSource(STORE_NAME);
    }

    @Test
    public void testVisitorWithSelect() throws Exception {
        SimpleFeatureSource transformed = transformWithSelection();

        CountVisitor countVisitor = new CountVisitor();
        MinVisitor minVisitor = new MinVisitor("male");
        MaxVisitor maxVisitor = new MaxVisitor("male");
        UniqueVisitor uniqueVisitor = new UniqueVisitor("female");
        BoundsVisitor boundsVisitor = new BoundsVisitor();

        FeatureCalc[] visitors = {
            countVisitor, minVisitor, maxVisitor, uniqueVisitor, boundsVisitor
        };
        boolean[] expectedPass = {true, true, true, true, false};
        Object[] expectedResult = {
            10,
            282970d,
            5552233d,
            new HashSet<>(
                    Arrays.asList(
                            1663099.0, 1262929.0, 343200.0, 2462797.0, 2356394.0, 931941.0,
                            323930.0, 3149703.0, 5878369.0, 2652758.0)),
            new ReferencedEnvelope(
                    -109.047821, -75.045998, 35.989586, 42.50936100000001, CRS.decode("EPSG:4326"))
        };

        checkVisitorApplication(transformed, visitors, expectedPass, expectedResult);
    }

    @Test
    public void testVisitorWithRename() throws Exception {
        SimpleFeatureSource transformed = transformWithRename();

        CountVisitor countVisitor = new CountVisitor();
        MinVisitor minVisitor = new MinVisitor("num_of_male");
        MaxVisitor maxVisitor = new MaxVisitor("num_of_male");
        UniqueVisitor uniqueVisitor = new UniqueVisitor("num_of_female");
        BoundsVisitor boundsVisitor = new BoundsVisitor();

        FeatureCalc[] visitors = {
            countVisitor, minVisitor, maxVisitor, uniqueVisitor, boundsVisitor
        };
        boolean[] expectedPass = {true, true, true, true, false};
        Object[] expectedResult = {
            10,
            282970d,
            5552233d,
            new HashSet<>(
                    Arrays.asList(
                            1663099.0, 1262929.0, 343200.0, 2462797.0, 2356394.0, 931941.0,
                            323930.0, 3149703.0, 5878369.0, 2652758.0)),
            new ReferencedEnvelope(
                    -109.047821, -75.045998, 35.989586, 42.50936100000001, CRS.decode("EPSG:4326"))
        };

        checkVisitorApplication(transformed, visitors, expectedPass, expectedResult);
    }

    public void checkVisitorApplication(
            SimpleFeatureSource transformed,
            FeatureCalc[] visitors,
            boolean[] expectedPass,
            Object[] expectedResult)
            throws java.io.IOException {
        for (int i = 0; i < visitors.length; i++) {
            ((TransformFeatureStoreWrapper) transformed).setPassedDown(false);
            FeatureCalc visitor = visitors[i];
            transformed.getFeatures(Query.ALL).accepts(visitor, null);
            boolean passedDown = ((TransformFeatureStoreWrapper) transformed).isPassedDown();
            assertEquals("Passed down test failed on " + visitor, expectedPass[i], passedDown);
            assertEquals(
                    "Expected result failed on " + visitor,
                    expectedResult[i],
                    visitor.getResult().getValue());
        }
    }

    @Test
    public void testVisitorWithCalc() throws Exception {
        SimpleFeatureSource transformed = transformWithExpressions();

        CountVisitor countVisitor = new CountVisitor();
        MinVisitor minVisitor = new MinVisitor("total");
        MaxVisitor maxVisitor = new MaxVisitor("total");
        UniqueVisitor uniqueVisitor = new UniqueVisitor("fips");
        BoundsVisitor boundsVisitor = new BoundsVisitor();

        FeatureCalc[] visitors = {
            countVisitor, minVisitor, maxVisitor, uniqueVisitor, boundsVisitor
        };
        boolean[] expectedPass = {true, true, true, true, false};
        Object[] expectedResult = {
            10,
            606900d,
            1.1430602E7,
            new HashSet<>(
                    Arrays.asList("11", "24", "17", "29", "08", "51", "20", "10", "54", "21")),
            new ReferencedEnvelope(
                    -110.04782099895442,
                    -74.04752638847438,
                    34.98970859966714,
                    43.50933565139621,
                    CRS.decode("EPSG:4326"))
        };

        checkVisitorApplication(transformed, visitors, expectedPass, expectedResult);
    }

    public SimpleFeatureSource transformWithSelection() throws IOException {
        List<Definition> definitions = new ArrayList<>();
        definitions.add(new Definition("state_fips"));
        definitions.add(new Definition("male"));
        definitions.add(new Definition("female"));
        definitions.add(new Definition("the_geom"));

        DataStore dataStore = DataUtilities.dataStore(SOURCE);
        return new TransformFeatureStoreWrapper(
                (SimpleFeatureStore) SOURCE, new NameImpl(STORE_NAME), definitions, dataStore);
    }

    public SimpleFeatureSource transformWithRename() throws Exception {
        List<Definition> definitions = new ArrayList<>();
        definitions.add(new Definition("fips", ECQL.toExpression("state_fips")));
        definitions.add(new Definition("num_of_male", ECQL.toExpression("male")));
        definitions.add(new Definition("num_of_female", ECQL.toExpression("female")));
        definitions.add(new Definition("geom", ECQL.toExpression("the_geom")));

        DataStore dataStore = DataUtilities.dataStore(SOURCE);
        return new TransformFeatureStoreWrapper(
                (SimpleFeatureStore) SOURCE, new NameImpl(STORE_NAME), definitions, dataStore);
    }

    public SimpleFeatureSource transformWithExpressions() throws Exception {
        List<Definition> definitions = new ArrayList<>();
        definitions.add(new Definition("fips", ECQL.toExpression("state_fips")));
        definitions.add(new Definition("total", ECQL.toExpression("male + female")));
        definitions.add(new Definition("geom", ECQL.toExpression("buffer(the_geom, 1)")));

        DataStore dataStore = DataUtilities.dataStore(SOURCE);
        return new TransformFeatureStoreWrapper(
                (SimpleFeatureStore) SOURCE, new NameImpl(STORE_NAME), definitions, dataStore);
    }
}
