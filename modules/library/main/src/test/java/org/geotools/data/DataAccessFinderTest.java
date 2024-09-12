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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataAccessFactory;
import org.geotools.api.data.DataAccessFinder;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.LockingManager;
import org.geotools.api.data.Query;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

        Map<String, Serializable> params = new HashMap<>();
        params.put(MOCK_DS_PARAM_KEY, MockUnavailableDataStoreFactory.class);

        DataStore dataStore = DataStoreFinder.getDataStore(params);
        Assert.assertNull(dataStore);

        params.put(MOCK_DS_PARAM_KEY, MockDataStoreFactory.class);

        dataStore = DataStoreFinder.getDataStore(params);
        Assert.assertSame(MOCK_DATASTORE, dataStore);
    }

    /** Can both DataStores and plain DataAccess be aquired through {@link DataAccessFinder}? */
    @Test
    public void testGetDataAccess() throws IOException {

        Map<String, Serializable> params = new HashMap<>();
        params.put(MOCK_DS_PARAM_KEY, MockUnavailableDataStoreFactory.class);

        DataAccess<FeatureType, Feature> dataStore = DataAccessFinder.getDataStore(params);
        Assert.assertNull(dataStore);

        params.put(MOCK_DS_PARAM_KEY, MockDataStoreFactory.class);

        dataStore = DataAccessFinder.getDataStore(params);
        Assert.assertSame(MOCK_DATASTORE, dataStore);

        params.put(MOCK_DS_PARAM_KEY, MockDataAccessFactory.class);

        dataStore = DataAccessFinder.getDataStore(params);
        Assert.assertSame(MOCK_DATAACCESS, dataStore);
    }

    @Test
    public void testGetAllDataStores() {
        Iterator<DataStoreFactorySpi> availableDataStores = DataStoreFinder.getAllDataStores();

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
    public void testGetAllDataAccess() {
        Iterator<DataAccessFactory> availableDataStores = DataAccessFinder.getAllDataStores();

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
    public void testGetAvailableDataStores() {
        Iterator<DataStoreFactorySpi> availableDataStores =
                DataStoreFinder.getAvailableDataStores();

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
    public void testGetAvailableDataAccess() {
        Iterator<DataAccessFactory> availableDataAccess = DataAccessFinder.getAvailableDataStores();

        Assert.assertNotNull(availableDataAccess);
        Assert.assertTrue(availableDataAccess.hasNext());

        Set<Class> classes = new HashSet<>();

        DataAccessFactory daf = availableDataAccess.next();
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

    @Test
    @SuppressWarnings("unchecked")
    public void testDynamicRegistration() throws Exception {
        // setting up the mocks
        Map<String, ?> params = Map.of("testKey", "testValue");
        DataAccessFactory mockFactory = createMock(DataAccessFactory.class);
        expect(mockFactory.isAvailable()).andReturn(true).anyTimes();
        expect(mockFactory.canProcess(params)).andReturn(true).anyTimes();
        DataAccess mockAccess = createMock(DataAccess.class);
        expect(mockFactory.createDataStore(params)).andReturn(mockAccess).anyTimes();
        replay(mockFactory);

        // first lookup attempt, not registered
        assertNull(DataAccessFinder.getDataStore(params));

        // register and second lookup
        DataAccessFinder.registerFactrory(mockFactory);
        assertEquals(mockAccess, DataAccessFinder.getDataStore(params));

        // unregister, should stop working
        DataAccessFinder.deregisterFactrory(mockFactory);
        assertNull(DataAccessFinder.getDataStore(params));
    }

    /**
     * @author Gabriel Roldan (TOPP)
     * @version $Id$
     * @since 2.5.x
     */
    public static class MockDataAccessFactory implements DataAccessFactory {
        @Override
        public boolean isAvailable() {
            return true;
        }

        /**
         * returns true if the {@link DataAccessFinderTest#MOCK_DS_PARAM_KEY mock param} contains
         * this class as value
         */
        @Override
        public boolean canProcess(Map<String, ?> params) {
            return MockDataAccessFactory.class.equals(params.get(MOCK_DS_PARAM_KEY));
        }

        /** @return {@link DataAccessFinderTest#MOCK_DATAACCESS} */
        @Override
        public DataAccess<FeatureType, Feature> createDataStore(Map<String, ?> params)
                throws IOException {
            return MOCK_DATAACCESS;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public String getDisplayName() {
            return null;
        }

        @Override
        public DataAccessFactory.Param[] getParametersInfo() {
            return null;
        }

        @Override
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
        @Override
        public boolean isAvailable() {
            return true;
        }

        /**
         * returns true if the {@link DataAccessFinderTest#MOCK_DS_PARAM_KEY mock param} contains
         * this class as value
         */
        @Override
        public boolean canProcess(Map<String, ?> params) {
            return MockDataStoreFactory.class.equals(params.get(MOCK_DS_PARAM_KEY));
        }

        /** @return {@link DataAccessFinderTest#MOCK_DATASTORE} */
        @Override
        public DataStore createDataStore(Map<String, ?> params) throws IOException {
            return MOCK_DATASTORE;
        }

        @Override
        public DataStore createNewDataStore(Map<String, ?> params) throws IOException {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public String getDisplayName() {
            return null;
        }

        @Override
        public DataAccessFactory.Param[] getParametersInfo() {
            return null;
        }

        @Override
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

                @Override
                public void createSchema(FeatureType featureType) throws IOException {}

                @Override
                public void dispose() {}

                @Override
                public FeatureSource<FeatureType, Feature> getFeatureSource(Name typeName)
                        throws IOException {
                    return null;
                }

                @Override
                public ServiceInfo getInfo() {
                    return null;
                }

                @Override
                public List<Name> getNames() throws IOException {
                    return null;
                }

                @Override
                public FeatureType getSchema(Name name) throws IOException {
                    return null;
                }

                @Override
                public void updateSchema(Name typeName, FeatureType featureType)
                        throws IOException {}

                @Override
                public void removeSchema(Name typeName) throws IOException {}
            };

    /** Fake datastore returned by {@link MockDataStoreFactory} */
    private static final DataStore MOCK_DATASTORE =
            new DataStore() {

                @Override
                public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
                        Query query, Transaction transaction) throws IOException {
                    return null;
                }

                @Override
                public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
                    return null;
                }

                @Override
                public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
                        String typeName, Filter filter, Transaction transaction)
                        throws IOException {
                    return null;
                }

                @Override
                public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
                        String typeName, Transaction transaction) throws IOException {
                    return null;
                }

                @Override
                public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
                        String typeName, Transaction transaction) throws IOException {
                    return null;
                }

                @Override
                public LockingManager getLockingManager() {
                    return null;
                }

                @Override
                public SimpleFeatureType getSchema(String typeName) throws IOException {
                    return null;
                }

                @Override
                public String[] getTypeNames() throws IOException {
                    return null;
                }

                @Override
                public void updateSchema(String typeName, SimpleFeatureType featureType)
                        throws IOException {}

                @Override
                public void removeSchema(String typeName) throws IOException {}

                @Override
                public void createSchema(SimpleFeatureType featureType) throws IOException {}

                @Override
                public void dispose() {}

                @Override
                public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
                    return null;
                }

                @Override
                public ServiceInfo getInfo() {
                    return null;
                }

                @Override
                public List<Name> getNames() throws IOException {
                    return null;
                }

                @Override
                public SimpleFeatureType getSchema(Name name) throws IOException {
                    return null;
                }

                @Override
                public void updateSchema(Name typeName, SimpleFeatureType featureType)
                        throws IOException {}

                @Override
                public void removeSchema(Name typeName) throws IOException {}
            };
}
