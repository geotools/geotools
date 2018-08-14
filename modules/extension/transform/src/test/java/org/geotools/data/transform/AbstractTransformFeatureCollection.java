/* (c) 2016 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.data.transform;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.util.logging.Logging;
import org.junit.BeforeClass;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

public abstract class AbstractTransformFeatureCollection {

    protected class TransformFeatureStoreWrapper extends TransformFeatureStore {

        private DataStore datastore;
        private boolean passedDown = false;

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
        public SimpleFeatureCollection getFeatures(Query query) throws IOException {
            return new TransformFeatureCollection(this, transformer, query) {
                public void accepts(
                        org.opengis.feature.FeatureVisitor visitor,
                        org.opengis.util.ProgressListener progress)
                        throws IOException {
                    if (isTypeCompatible(visitor, transformer.getSchema())) {
                        passedDown = true;
                    } else {
                        passedDown = false;
                    }
                    super.accepts(visitor, progress);
                }
            };
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

    public static SimpleFeatureSource SOURCE;
    protected static String STORE_NAME = "states";

    @BeforeClass
    public static void setup() throws Exception {
        // just to make sure the loggin is not going to cause exceptions when turned on
        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        handler.setLevel(java.util.logging.Level.FINE);
        Logging.getLogger("org.geotools.data.transform").setLevel(java.util.logging.Level.FINE);

        PropertyDataStore pds =
                new PropertyDataStore(new File("./src/test/resources/org/geotools/data/transform"));
        SOURCE = pds.getFeatureSource(STORE_NAME);
    }

    public SimpleFeatureSource transformWithSelection() throws IOException {
        List<Definition> definitions = new ArrayList<Definition>();
        definitions.add(new Definition("state_fips"));
        definitions.add(new Definition("male"));
        definitions.add(new Definition("female"));
        definitions.add(new Definition("the_geom"));

        DataStore dataStore = DataUtilities.dataStore(SOURCE);
        return new TransformFeatureStoreWrapper(
                (SimpleFeatureStore) SOURCE, new NameImpl(STORE_NAME), definitions, dataStore);
    }

    public SimpleFeatureSource transformWithRename() throws Exception {
        List<Definition> definitions = new ArrayList<Definition>();
        definitions.add(new Definition("fips", ECQL.toExpression("state_fips")));
        definitions.add(new Definition("num_of_male", ECQL.toExpression("male")));
        definitions.add(new Definition("num_of_female", ECQL.toExpression("female")));
        definitions.add(new Definition("geom", ECQL.toExpression("the_geom")));

        DataStore dataStore = DataUtilities.dataStore(SOURCE);
        return new TransformFeatureStoreWrapper(
                (SimpleFeatureStore) SOURCE, new NameImpl(STORE_NAME), definitions, dataStore);
    }

    public SimpleFeatureSource transformWithExpressions() throws Exception {
        List<Definition> definitions = new ArrayList<Definition>();
        definitions.add(new Definition("fips", ECQL.toExpression("state_fips")));
        definitions.add(new Definition("total", ECQL.toExpression("male + female")));
        definitions.add(new Definition("geom", ECQL.toExpression("buffer(the_geom, 1)")));

        DataStore dataStore = DataUtilities.dataStore(SOURCE);
        return new TransformFeatureStoreWrapper(
                (SimpleFeatureStore) SOURCE, new NameImpl(STORE_NAME), definitions, dataStore);
    }
}
