/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.geotools.arcsde.data.ArcSDEDataStore;
import org.geotools.arcsde.data.ArcSDEDataStoreFactory;
import org.geotools.arcsde.data.ArcSDEJNDIDataStoreFactory;
import org.geotools.arcsde.data.TestData;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Transaction;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Logging;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.7
 */
public class ArcSDEJNDIDataStoreFactoryTest {

    private static final Logger LOGGER = Logging.getLogger(ArcSDEJNDIDataStoreFactoryTest.class);

    private static ArcSDEJNDIDataStoreFactory factory;

    private static TestData testData;

    static final String IC_FACTORY_PROPERTY = "java.naming.factory.initial";

    static final String JNDI_ROOT = "org.osjava.sj.root";

    static final String JNDI_DELIM = "org.osjava.jndi.delimiter";

    /** @throws java.lang.Exception */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        factory = new ArcSDEJNDIDataStoreFactory();
        setupJNDIEnvironment();
        testData = new TestData();
        testData.setUp();
        testData.getConProps();
    }

    @AfterClass
    public static void cleanupAfterClass() throws Exception {
        // clean up the context, otherwise other tests in other JNDI tests might fail
        // as a consequence
        Context ctx = GeoTools.getInitialContext(GeoTools.getDefaultHints());
        ctx.close();

        // clean up system properties too
        System.clearProperty(IC_FACTORY_PROPERTY);
        System.clearProperty(JNDI_DELIM);
        System.clearProperty(JNDI_ROOT);
    }

    @Test
    public void testDataStoreFinderFindsIt() throws IOException {
        Iterator<DataStoreFactorySpi> allFactories = DataStoreFinder.getAllDataStores();
        ArcSDEJNDIDataStoreFactory sdeFac = null;
        while (allFactories.hasNext()) {
            DataAccessFactory next = allFactories.next();
            if (next instanceof ArcSDEJNDIDataStoreFactory) {
                sdeFac = (ArcSDEJNDIDataStoreFactory) next;
                break;
            }
        }
        assertNotNull(sdeFac);
    }

    @Test
    public void testDataAccessFinderFindsIt() throws IOException {
        Iterator<DataAccessFactory> allFactories = DataAccessFinder.getAllDataStores();
        ArcSDEJNDIDataStoreFactory sdeFac = null;
        while (allFactories.hasNext()) {
            DataAccessFactory next = allFactories.next();
            if (next instanceof ArcSDEJNDIDataStoreFactory) {
                sdeFac = (ArcSDEJNDIDataStoreFactory) next;
                break;
            }
        }
        assertNotNull(sdeFac);
    }

    /** Test method for {@link ArcSDEJNDIDataStoreFactory#createDataStore(java.util.Map)}. */
    @Test
    @Ignore // TODO: revisit
    public void testCreateDataStore_MapParams() throws IOException {
        String jndiRef = "MyArcSdeResource";
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ArcSDEJNDIDataStoreFactory.JNDI_REFNAME.key, jndiRef);

        Map<String, Serializable> config = testData.getConProps();
        try {
            InitialContext initialContext = GeoTools.getInitialContext(GeoTools.getDefaultHints());
            initialContext.bind(jndiRef, config);
            assertNotNull(initialContext.lookup(jndiRef));
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        ArcSDEDataStore dataStore = (ArcSDEDataStore) factory.createDataStore(params);
        assertNotNull(dataStore);
        ISession session = dataStore.getSession(Transaction.AUTO_COMMIT);
        assertNotNull(session);
        try {
            assertEquals(
                    String.valueOf(config.get("user")).toUpperCase(),
                    session.getUser().toUpperCase());
        } finally {
            session.dispose();
        }
    }

    @Test
    @Ignore // TODO: revisit
    public void testCreateDataStore_SessionPool() throws IOException {
        String jndiRef = "MyArcSdeResource_SessionPool";
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ArcSDEJNDIDataStoreFactory.JNDI_REFNAME.key, jndiRef);

        ISessionPool pool = testData.getConnectionPool();
        try {
            InitialContext initialContext = GeoTools.getInitialContext(GeoTools.getDefaultHints());
            initialContext.bind(jndiRef, pool);
            assertNotNull(initialContext.lookup(jndiRef));
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        ArcSDEDataStore dataStore = (ArcSDEDataStore) factory.createDataStore(params);
        assertNotNull(dataStore);
        ISession session = dataStore.getSession(Transaction.AUTO_COMMIT);
        assertNotNull(session);
        session.dispose();
    }

    /** Test method for {@link ArcSDEJNDIDataStoreFactory#canProcess(java.util.Map)}. */
    @Test
    public void testCanProcess() {
        assertFalse(factory.canProcess(null));
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        assertFalse(factory.canProcess(params));
        String jndiRef = "java:comp/env/MyArcSdeResource";
        params.put(ArcSDEJNDIDataStoreFactory.JNDI_REFNAME.key, jndiRef);
        assertTrue(factory.canProcess(params));
    }

    /** Test method for {@link ArcSDEJNDIDataStoreFactory#getParametersInfo()}. */
    @Test
    public void testGetParametersInfo() {
        Param[] parametersInfo = factory.getParametersInfo();
        assertNotNull(parametersInfo);
        assertEquals(4, parametersInfo.length);
        assertSame(ArcSDEJNDIDataStoreFactory.JNDI_REFNAME, parametersInfo[0]);
        assertSame(ArcSDEDataStoreFactory.NAMESPACE_PARAM, parametersInfo[1]);
        assertSame(ArcSDEDataStoreFactory.VERSION_PARAM, parametersInfo[2]);
        assertSame(ArcSDEDataStoreFactory.ALLOW_NON_SPATIAL_PARAM, parametersInfo[3]);
    }

    /** Test method for {@link ArcSDEJNDIDataStoreFactory#createNewDataStore(java.util.Map)}. */
    @Test
    public void testCreateNewDataStore() throws IOException {
        try {
            factory.createNewDataStore(new HashMap<String, Serializable>());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    private static void setupJNDIEnvironment() throws IOException {

        File jndi = new File("target/jndi");
        jndi.mkdirs();

        if (System.getProperty(IC_FACTORY_PROPERTY) == null) {
            System.setProperty(IC_FACTORY_PROPERTY, "org.osjava.sj.SimpleContextFactory");
        }

        if (System.getProperty(JNDI_ROOT) == null) {
            System.setProperty(JNDI_ROOT, jndi.getAbsolutePath());
        }

        if (System.getProperty(JNDI_DELIM) == null) {
            System.setProperty(JNDI_DELIM, "/");
        }

        LOGGER.fine(IC_FACTORY_PROPERTY + " = " + System.getProperty(IC_FACTORY_PROPERTY));
        LOGGER.fine(JNDI_ROOT + " = " + System.getProperty(JNDI_ROOT));
        LOGGER.fine(JNDI_DELIM + " = " + System.getProperty(JNDI_DELIM));
    }
}
