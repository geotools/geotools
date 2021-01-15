/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.data.simple.SimpleFeatureSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 */
public class DataAccessFinderTest {

    private static final String MOCK_DS_PARAM_KEY = "MOCK_PARAM_KEY";

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testGetDataStore() throws IOException {
        DataStore dataStore;

        Map<String, Serializable> params = new HashMap<>();
        params.put(MOCK_DS_PARAM_KEY, MockUnavailableDataStoreFactory.class);

        dataStore = DataStoreFinder.getDataStore(params);
        Assert.assertNull(dataStore);

        params.put(MOCK_DS_PARAM_KEY, MockDataStoreFactory.class);

        dataStore = DataStoreFinder.getDataStore(params);
        Assert.assertSame(MOCK_DATASTORE, dataStore);
    }

    /** Can both DataStores and plain DataAccess be aquired through {@link DataAccessFinder}? */
    @Test
    public void testGetDataAccess() throws IOException {
        DataAccess<FeatureType, Feature> dataStore;

        Map<String, Serializable> params = new HashMap<>();
        params.put(MOCK_DS_PARAM_KEY, MockUnavailableDataStoreFactory.class);

        dataStore = DataAccessFinder.getDataStore(params);
        Assert.assertNull(dataStore);

        params.put(MOCK_DS_PARAM_KEY, MockDataStoreFactory.class);

        dataStore = DataAccessFinder.getDataStore(params);
        Assert.assertSame(MOCK_DATASTORE, dataStore);

        params.put(MOCK_DS_PARAM_KEY, MockDataAccessFactory.class);

        dataStore = DataAccessFinder.getDataStore(params);
        Assert.assertSame(MOCK_DATAACCESS, dataStore);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetAllDataStores() {
        Iterator<DataStoreFactorySpi> availableDataStores;
        availableDataStores = DataStoreFinder.getAllDataStores();

        Assert.assertNotNull(availableDataStores);
        Assert.assertTrue(availableDataStores.hasNext());

        DataStoreFactorySpi dsf1 = availableDataStores.next();
        Assert.assertTrue(availableDataStores.hasNext());
        DataStoreFactorySpi dsf2 = availableDataStores.next();
        Assert.assertFalse(availableDataStores.hasNext());

        Assert.assertNotNull(dsf1);
        Assert.assertNotNull(dsf2);
        // do not assume iteration order...
        if (dsf1 instanceof MockUnavailableDataStoreFactory) {
            Assert.assertTrue(dsf2 instanceof MockDataStoreFactory);
        } else {
            Assert.assertTrue(dsf1 instanceof MockDataStoreFactory);
            Assert.assertTrue(dsf2 instanceof MockUnavailableDataStoreFactory);
        }
    }

    /** Does DataAccessFinder.getAllDataStores() return both the DataStores and DataAccess? */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetAllDataAccess() {
        Iterator<DataAccessFactory> availableDataStores;
        availableDataStores = DataAccessFinder.getAllDataStores();

        Assert.assertNotNull(availableDataStores);
        Assert.assertTrue(availableDataStores.hasNext());

        DataAccessFactory dsf1 = availableDataStores.next();
        Assert.assertTrue(availableDataStores.hasNext());
        DataAccessFactory dsf2 = availableDataStores.next();
        Assert.assertTrue(availableDataStores.hasNext());
        DataAccessFactory dsf3 = availableDataStores.next();

        Assert.assertFalse(availableDataStores.hasNext());

        Assert.assertNotNull(dsf1);
        Assert.assertNotNull(dsf2);
        Assert.assertNotNull(dsf3);

        Set<Class> classes = new HashSet<>();
        classes.add(dsf1.getClass());
        classes.add(dsf2.getClass());
        classes.add(dsf3.getClass());

        Assert.assertTrue(classes.contains(MockDataAccessFactory.class));
        Assert.assertTrue(classes.contains(MockDataStoreFactory.class));
        Assert.assertTrue(classes.contains(MockUnavailableDataStoreFactory.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetAvailableDataStores() {
        Iterator<DataStoreFactorySpi> availableDataStores;
        availableDataStores = DataStoreFinder.getAvailableDataStores();

        Assert.assertNotNull(availableDataStores);
        Assert.assertTrue(availableDataStores.hasNext());

        DataStoreFactorySpi dsf = availableDataStores.next();

        Assert.assertFalse(availableDataStores.hasNext());

        Assert.assertTrue(dsf instanceof MockDataStoreFactory);
    }

    /**
     * Does DataAccessFinder.getAvailableDataStores() return both the available DataStore and
     * DataAccess factories?
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetAvailableDataAccess() {
        Iterator<DataAccessFactory> availableDataAccess;
        availableDataAccess = DataAccessFinder.getAvailableDataStores();

        Assert.assertNotNull(availableDataAccess);
        Assert.assertTrue(availableDataAccess.hasNext());

        Set<Class> classes = new HashSet<>();
        DataAccessFactory daf;

        daf = availableDataAccess.next();
        Assert.assertNotNull(daf);
        classes.add(daf.getClass());

        Assert.assertTrue(availableDataAccess.hasNext());
        daf = availableDataAccess.next();
        Assert.assertNotNull(daf);
        classes.add(daf.getClass());

        Assert.assertFalse(availableDataAccess.hasNext());

        Assert.assertTrue(classes.contains(MockDataStoreFactory.class));
        Assert.assertTrue(classes.contains(MockDataAccessFactory.class));
    }

    /**
     * @author Gabriel Roldan (TOPP)
     * @version $Id$
     * @since 2.5.x
     */
    public static class MockDataAccessFactory implements DataAccessFactory {
        public boolean isAvailable() {
            return true;
        }

        /**
         * returns true if the {@link DataAccessFinderTest#MOCK_DS_PARAM_KEY mock param} contains
         * this class as value
         */
        public boolean canProcess(Map<String, ?> params) {
            return MockDataAccessFactory.class.equals(params.get(MOCK_DS_PARAM_KEY));
        }

        /** @return {@link DataAccessFinderTest#MOCK_DATAACCESS} */
        public DataAccess<FeatureType, Feature> createDataStore(Map<String, ?> params)
                throws IOException {
            return MOCK_DATAACCESS;
        }

        public String getDescription() {
            return null;
        }

        public String getDisplayName() {
            return null;
        }

        public org.geotools.data.DataAccessFactory.Param[] getParametersInfo() {
            return null;
        }

        public Map<Key, ?> getImplementationHints() {
            return null;
        }
    }

    /**
     * A mock {@link DataStoreFactorySpi} implementation to test the factory lookup system.
     *
     * @author Gabriel Roldan (TOPP)
     * @version $Id$
     * @since 2.5.x
     */
    public static class MockDataStoreFactory implements DataStoreFactorySpi {
        public boolean isAvailable() {
            return true;
        }

        /**
         * returns true if the {@link DataAccessFinderTest#MOCK_DS_PARAM_KEY mock param} contains
         * this class as value
         */
        public boolean canProcess(Map<String, ?> params) {
            return MockDataStoreFactory.class.equals(params.get(MOCK_DS_PARAM_KEY));
        }

        /** @return {@link DataAccessFinderTest#MOCK_DATASTORE} */
        public DataStore createDataStore(Map<String, ?> params) throws IOException {
            return MOCK_DATASTORE;
        }

        public DataStore createNewDataStore(Map<String, ?> params) throws IOException {
            return null;
        }

        public String getDescription() {
            return null;
        }

        public String getDisplayName() {
            return null;
        }

        public org.geotools.data.DataAccessFactory.Param[] getParametersInfo() {
            return null;
        }

        public Map<Key, ?> getImplementationHints() {
            return null;
        }
    }

    /**
     * A test {@link DataStoreFactorySpi} implementation that is unavailable
     *
     * @author Gabriel Roldan (TOPP)
     * @version $Id$
     * @since 2.5.x
     */
    public static class MockUnavailableDataStoreFactory extends MockDataStoreFactory {

        @Override
        public boolean isAvailable() {
            return false;
        }
    }

    /** Fake DataAccess returned by {@link MockDataAccessFactory} */
    private static final DataAccess<FeatureType, Feature> MOCK_DATAACCESS =
            new DataAccess<FeatureType, Feature>() {

                public void createSchema(FeatureType featureType) throws IOException {}

                public void dispose() {}

                public FeatureSource<FeatureType, Feature> getFeatureSource(Name typeName)
                        throws IOException {
                    return null;
                }

                public ServiceInfo getInfo() {
                    return null;
                }

                public List<Name> getNames() throws IOException {
                    return null;
                }

                public FeatureType getSchema(Name name) throws IOException {
                    return null;
                }

                public void updateSchema(Name typeName, FeatureType featureType)
                        throws IOException {}

                public void removeSchema(Name typeName) throws IOException {}
            };

    /** Fake datastore returned by {@link MockDataStoreFactory} */
    private static final DataStore MOCK_DATASTORE =
            new DataStore() {

                public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
                        Query query, Transaction transaction) throws IOException {
                    return null;
                }

                public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
                    return null;
                }

                public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
                        String typeName, Filter filter, Transaction transaction)
                        throws IOException {
                    return null;
                }

                public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
                        String typeName, Transaction transaction) throws IOException {
                    return null;
                }

                public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
                        String typeName, Transaction transaction) throws IOException {
                    return null;
                }

                public LockingManager getLockingManager() {
                    return null;
                }

                public SimpleFeatureType getSchema(String typeName) throws IOException {
                    return null;
                }

                public String[] getTypeNames() throws IOException {
                    return null;
                }

                public void updateSchema(String typeName, SimpleFeatureType featureType)
                        throws IOException {}

                public void removeSchema(String typeName) throws IOException {}

                public void createSchema(SimpleFeatureType featureType) throws IOException {}

                public void dispose() {}

                public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
                    return null;
                }

                public ServiceInfo getInfo() {
                    return null;
                }

                public List<Name> getNames() throws IOException {
                    return null;
                }

                public SimpleFeatureType getSchema(Name name) throws IOException {
                    return null;
                }

                public void updateSchema(Name typeName, SimpleFeatureType featureType)
                        throws IOException {}

                public void removeSchema(Name typeName) throws IOException {}
            };
}
