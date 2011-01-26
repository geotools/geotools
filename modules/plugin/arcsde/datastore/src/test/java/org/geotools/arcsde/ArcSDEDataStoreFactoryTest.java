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
 *
 */
package org.geotools.arcsde;

import static org.geotools.arcsde.data.ArcSDEDataStoreConfig.VERSION_PARAM_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geotools.arcsde.data.ArcSDEDataStore;
import org.geotools.arcsde.data.ArcSDEDataStoreConfig;
import org.geotools.arcsde.data.InProcessViewSupportTestData;
import org.geotools.arcsde.data.TestData;
import org.geotools.arcsde.session.ArcSDEConnectionConfig;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeVersion;

/**
 * Test suite for {@link ArcSDEDataStoreFactory}
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/test/java
 *         /org/geotools/arcsde/ArcSDEDataStoreFactoryTest.java $
 * @version $Id$
 * @since 2.4.x
 */
public class ArcSDEDataStoreFactoryTest {

    /**
     * A datastore factory set up with the {@link #workingParams}
     */
    private ArcSDEDataStoreFactory dsFactory;

    /**
     * A set of datastore parameters that are meant to work
     */
    private Map<String, Serializable> workingParams;

    /**
     * Aset of datastore parameters that though valid (contains all the required parameters) point
     * to a non available server
     */
    private Map<String, Serializable> nonWorkingParams;

    /**
     * A set of datastore parameters that does not conform to the parameters required by the ArcSDE
     * plugin
     */
    private Map<String, Serializable> illegalParams;

    private TestData testData;

    @Before
    public void setUp() throws Exception {
        this.testData = new TestData();
        testData.setUp();

        workingParams = testData.getConProps();

        nonWorkingParams = new HashMap<String, Serializable>(workingParams);
        nonWorkingParams.put(ArcSDEConnectionConfig.SERVER_NAME_PARAM_NAME, "non-existent-server");

        illegalParams = new HashMap<String, Serializable>(workingParams);
        illegalParams.put(ArcSDEDataStoreConfig.DBTYPE_PARAM_NAME, "non-existent-db-type");

        dsFactory = new ArcSDEDataStoreFactory();
    }

    @After
    public void tearDown() throws Exception {
        this.testData.tearDown(true, true);
    }

    @Test
    public void testGetDataStore() throws IOException {
        DataStore dataStore;

        try {
            DataStoreFinder.getDataStore(nonWorkingParams);
            fail("should have failed with non working parameters");
        } catch (IOException e) {
            assertTrue(true);
        }
        dataStore = DataStoreFinder.getDataStore(workingParams);
        assertNotNull(dataStore);
        assertTrue(dataStore instanceof ArcSDEDataStore);
        dataStore.dispose();
    }

    @Test
    public void testDataStoreFinderFindsIt() throws IOException {
        Iterator<DataStoreFactorySpi> allFactories = DataStoreFinder.getAllDataStores();
        ArcSDEDataStoreFactory sdeFac = null;
        while (allFactories.hasNext()) {
            DataAccessFactory next = allFactories.next();
            if (next instanceof ArcSDEDataStoreFactory) {
                sdeFac = (ArcSDEDataStoreFactory) next;
                break;
            }
        }
        assertNotNull(sdeFac);
    }

    @Test
    public void testDataAccessFinderFindsIt() throws IOException {
        Iterator<DataAccessFactory> allFactories = DataAccessFinder.getAllDataStores();
        ArcSDEDataStoreFactory sdeFac = null;
        while (allFactories.hasNext()) {
            DataAccessFactory next = allFactories.next();
            if (next instanceof ArcSDEDataStoreFactory) {
                sdeFac = (ArcSDEDataStoreFactory) next;
                break;
            }
        }
        assertNotNull(sdeFac);
    }

    /**
     * Test method for
     * {@link org.geotools.arcsde.ArcSDEDataStoreFactory#createNewDataStore(java.util.Map)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testCreateNewDataStore() {
        try {
            dsFactory.createNewDataStore((Map<String, Serializable>) Collections.EMPTY_MAP);
            fail("Expected UOE as we can't create new datastores");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    /**
     * Test method for {@link org.geotools.arcsde.ArcSDEDataStoreFactory#canProcess(java.util.Map)}.
     */
    @Test
    public void testCanProcess() {
        assertFalse(dsFactory.canProcess(illegalParams));
        assertTrue(dsFactory.canProcess(nonWorkingParams));
        assertTrue(dsFactory.canProcess(workingParams));
    }

    /**
     * Test method for
     * {@link org.geotools.arcsde.ArcSDEDataStoreFactory#createDataStore(java.util.Map)}.
     * 
     * @throws IOException
     */
    @Test
    public void testCreateDataStore() throws IOException {
        try {
            dsFactory.createDataStore(nonWorkingParams);
        } catch (IOException e) {
            assertTrue(true);
        }

        DataStore store = dsFactory.createDataStore(workingParams);
        assertNotNull(store);
        assertTrue(store instanceof ArcSDEDataStore);
        store.dispose();
    }

    /**
     * Test method for
     * {@link org.geotools.arcsde.ArcSDEDataStoreFactory#createDataStore(java.util.Map)}.
     * 
     * @throws IOException
     * @throws SeException
     * @throws UnavailableConnectionException
     */
    @Test
    public void testCreateDataStoreWithInProcessViews() throws IOException, SeException,
            UnavailableConnectionException {
        ISession session = testData.getConnectionPool().getSession(true);
        try {
            InProcessViewSupportTestData.setUp(session, testData);
        } finally {
            session.dispose();
        }

        Map<String, Serializable> workingParamsWithSqlView = new HashMap<String, Serializable>(
                workingParams);
        workingParamsWithSqlView.putAll(InProcessViewSupportTestData.registerViewParams);

        DataStore store = dsFactory.createDataStore(workingParamsWithSqlView);
        assertNotNull(store);

        SimpleFeatureType viewType = store.getSchema(InProcessViewSupportTestData.typeName);
        assertNotNull(viewType);
        assertEquals(InProcessViewSupportTestData.typeName, viewType.getTypeName());
        store.dispose();
    }

    @Test
    public void testVersionParamCheck() throws IOException, UnavailableConnectionException {
        ISession session = testData.getConnectionPool().getSession(true);
        final String versionName = "testVersionParamCheck";
        try {
            testData.createVersion(session, versionName,
                    SeVersion.SE_QUALIFIED_DEFAULT_VERSION_NAME);
        } finally {
            session.dispose();
        }

        Map<String, Serializable> paramsWithVersion = new HashMap<String, Serializable>(
                workingParams);
        try {
            paramsWithVersion.put(VERSION_PARAM_NAME, "Non existent version name");
            dsFactory.createDataStore(paramsWithVersion);
        } catch (IOException e) {
            assertTrue(e.getMessage(),
                    e.getMessage().startsWith("Specified ArcSDE version does not exist"));
        }
    }

    @Test
    public void testVersioned() throws IOException, UnavailableConnectionException {
        ISession session = testData.getConnectionPool().getSession(true);
        final String versionName = "testVersioned";
        try {
            testData.createVersion(session, versionName,
                    SeVersion.SE_QUALIFIED_DEFAULT_VERSION_NAME);
        } finally {
            session.dispose();
        }

        Map<String, Serializable> paramsWithVersion = new HashMap<String, Serializable>(
                workingParams);
        paramsWithVersion.put(VERSION_PARAM_NAME, versionName);
        DataStore ds = dsFactory.createDataStore(paramsWithVersion);
        assertNotNull(ds);
        ds.dispose();

        String qualifiedVersionName = session.getUser() + "." + versionName;
        paramsWithVersion.put(VERSION_PARAM_NAME, qualifiedVersionName);
        ds = dsFactory.createDataStore(paramsWithVersion);
        assertNotNull(ds);
        ds.dispose();

        // version name should be case insensitive
        // EDIT: version name is NOT case sensitive, at least for sde10/oracle
        // qualifiedVersionName = qualifiedVersionName.toUpperCase();
        // paramsWithVersion.put(VERSION_PARAM_NAME, qualifiedVersionName);
        // ds = dsFactory.createDataStore(paramsWithVersion);
        // assertNotNull(ds);
        // ds.dispose();

        // qualifiedVersionName = qualifiedVersionName.toLowerCase();
        // paramsWithVersion.put(VERSION_PARAM_NAME, qualifiedVersionName);
        // ds = dsFactory.createDataStore(paramsWithVersion);
        // assertNotNull(ds);
        // ds.dispose();
    }
}
