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
package org.geotools.arcsde.session;

import static org.geotools.arcsde.session.ArcSDEConnectionConfig.INSTANCE_NAME_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.MAX_CONNECTIONS_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.MIN_CONNECTIONS_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.SERVER_NAME_PARAM_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the functionality of a pool of ArcSDE connection objects over a live ArcSDE database
 * 
 * @author Gabriel Roldan
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/test/java
 *         /org/geotools/arcsde/pool/SessionPoolTest.java $
 * @version $Id$
 */
public class SessionPoolTest {

    private static Logger LOGGER = Logger.getLogger("org.geotools.data.sde");

    private Map<String, Serializable> connectionParameters;

    private ArcSDEConnectionConfig connectionConfig = null;

    private ISessionPool pool = null;

    /**
     * loads {@code test-data/testparams.properties} to get connection parameters and sets up a
     * ArcSDEConnectionConfig with them for tests to set up SessionPool's as required
     * 
     * @throws Exception
     * 
     * @throws IllegalStateException
     */
    @Before
    public void setUp() throws Exception {

        Properties conProps = new Properties();
        String propsFile = "testparams.properties";
        URL conParamsSource = org.geotools.test.TestData.url(null, propsFile);

        LOGGER.fine("loading connection parameters from " + conParamsSource.toExternalForm());

        InputStream in = conParamsSource.openStream();

        if (in == null) {
            throw new IllegalStateException("cannot find test params: "
                    + conParamsSource.toExternalForm());
        }

        conProps.load(in);
        in.close();
        connectionParameters = new HashMap<String, Serializable>();
        for (Entry<Object, Object> e : conProps.entrySet()) {
            connectionParameters.put((String) e.getKey(), (String) e.getValue());
        }

        // test that mandatory connection parameters are set
        try {
            connectionConfig = ArcSDEConnectionConfig.fromMap(connectionParameters);
        } catch (Exception ex) {
            throw new IllegalStateException("No valid connection parameters found in "
                    + conParamsSource.toExternalForm() + ": " + ex.getMessage());
        }
    }

    /**
     * closes the connection pool if it's still open
     * 
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        connectionConfig = null;

        if (pool != null) {
            pool.close();
        }

        pool = null;
    }

    /**
     * Sets up a new SessionPool with the connection parameters stored in <code>connParams</code>
     * and throws an exception if something goes wrong
     * 
     * @param connParams
     *            a set of connection parameters from where the new SessionPool will connect to the
     *            SDE database and create connections
     * @return
     * @throws IllegalArgumentException
     *             if the set of connection parameters are not propperly set
     * @throws NullPointerException
     *             if <code>connParams</code> is null
     * @throws IOException
     *             if the pool can't create the connections with the passed arguments (i.e. can't
     *             connect to SDE database)
     */
    private ISessionPool createPool(Map<String, Serializable> connParams)
            throws IllegalArgumentException, NullPointerException, IOException {
        this.connectionConfig = ArcSDEConnectionConfig.fromMap(connParams);
        LOGGER.fine("creating a new SessionPool with " + connectionConfig);

        if (this.pool != null) {
            LOGGER.fine("pool already created, closing it");
            this.pool.close();
        }

        this.pool = new SessionPool(connectionConfig);
        LOGGER.fine("pool created");

        return this.pool;
    }

    /**
     * tests that a connection to a live ArcSDE database can be established with the parameters
     * defined int testparams.properties, and a SessionPool can be properly setted up
     * 
     * @throws IOException
     */
    @Test
    public void testConnect() throws IOException {
        LOGGER.fine("testing connection to the sde database");

        ISessionPoolFactory pf = SessionPoolFactory.getInstance();
        ArcSDEConnectionConfig config = ArcSDEConnectionConfig.fromMap(connectionParameters);

        ISessionPool connPool = null;

        connPool = pf.createPool(config);
        LOGGER.fine("connection succeed " + connPool.getPoolSize() + " connections ready");
        connPool.close();
    }

    @Test
    public void testConnectFailure() throws IOException {

        connectionParameters.put(SERVER_NAME_PARAM_NAME, "unreacheable-server-name");
        ArcSDEConnectionConfig config = ArcSDEConnectionConfig.fromMap(connectionParameters);

        try {
            ISessionPool connPool = new SessionPool(config);
            connPool.close();
            fail("Expected IOE for unreachable server");
        } catch (IOException e) {
            assertTrue(true);
        }

    }

    /**
     * Checks that after creation the pool has the specified initial number of connections.
     * 
     * @throws IOException
     * 
     * @throws UnavailableConnectionException
     */
    @Test
    public void testInitialCount() throws IOException, UnavailableConnectionException {
        int MIN_CONNECTIONS = 2;
        int MAX_CONNECTIONS = 6;

        // override pool.minConnections and pool.maxConnections from
        // the configured parameters to test the connections' pool
        // availability
        Map<String, Serializable> params = new HashMap<String, Serializable>(
                this.connectionParameters);
        params.put(MIN_CONNECTIONS_PARAM_NAME, Integer.valueOf(MIN_CONNECTIONS));
        params.put(MAX_CONNECTIONS_PARAM_NAME, Integer.valueOf(MAX_CONNECTIONS));

        createPool(params);

        // check that after creation, the pool contains the minimun number
        // of connections specified
        assertEquals(
                "after creation, the pool must contain the minimun number of connections specified",
                MIN_CONNECTIONS, this.pool.getPoolSize());
    }

    /**
     * Tests that the pool creation fails if a wrong set of parameters is passed (i.e.
     * maxConnections is lower than minConnections)
     * 
     * @throws IOException
     * @throws UnavailableConnectionException
     */
    @Test
    public void testChecksLimits() throws IOException, UnavailableConnectionException {
        int MIN_CONNECTIONS = 2;

        // override pool.minConnections and pool.maxConnections from
        // the configured parameters to test the connections' pool
        // availability
        Map<String, Serializable> params = new HashMap<String, Serializable>(
                this.connectionParameters);
        params.put(MIN_CONNECTIONS_PARAM_NAME, Integer.valueOf(MIN_CONNECTIONS));
        params.put(MAX_CONNECTIONS_PARAM_NAME, Integer.valueOf(1));

        // this MUST fail, since maxConnections is lower than minConnections
        try {
            LOGGER.fine("testing parameters' sanity check at pool creation time");
            ISessionPool pool = createPool(params);
            pool.close();
            fail("the connection pool creation must have failed since a wrong set of arguments was passed");
        } catch (IllegalArgumentException ex) {
            // it's ok, it is what's expected
            LOGGER.fine("pramams assertion passed");
        }
    }

    /**
     * tests that no more than pool.maxConnections connections can be created, and once one
     * connection is freed, it is ready to be used again.
     * 
     * @throws Exception
     */
    @Test
    public void testMaxConnections() throws Exception {
        final int MIN_CONNECTIONS = 2;
        final int MAX_CONNECTIONS = 2;

        Map<String, Serializable> params = new HashMap<String, Serializable>(
                this.connectionParameters);
        params.put(MIN_CONNECTIONS_PARAM_NAME, Integer.valueOf(MIN_CONNECTIONS));
        params.put(MAX_CONNECTIONS_PARAM_NAME, Integer.valueOf(MAX_CONNECTIONS));

        createPool(params);

        ISession[] sessions = new ISession[MAX_CONNECTIONS];
        // try to get the maximun number of connections specified, and do not
        // release anyone
        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            sessions[i] = pool.getSession();
        }

        // now that the max number of connections is reached, the pool
        // should throw an UnavailableArcSDEConnectionException
        try {
            this.pool.getSession();
            fail("since the max number of connections was reached, the pool should have throwed an UnavailableArcSDEConnectionException");
        } catch (UnavailableConnectionException ex) {
            LOGGER.fine("maximun number of connections reached, got an UnavailableArcSDEConnectionException, it's OK");
        }

        // now, free one and check the same conection is returned on the
        // next call to getConnection()
        ISession expected = sessions[0];
        expected.dispose();

        Thread.sleep(1000);
        ISession session = this.pool.getSession();
        assertEquals(expected, session);
    }

    /**
     * a null database name should not be an impediment to create the pool
     * 
     * @throws IOException
     */
    @Test
    public void testCreateWithNullDBName() throws IOException {
        Map<String, Serializable> params = new HashMap<String, Serializable>(
                this.connectionParameters);
        params.remove(INSTANCE_NAME_PARAM_NAME);
        createPool(params);
    }

    /**
     * an empty database name should not be an impediment to create the pool
     * 
     * @throws IOException
     */
    @Test
    public void testCreateWithEmptyDBName() throws IOException {
        Map<String, Serializable> params = new HashMap<String, Serializable>(
                this.connectionParameters);
        params.put(INSTANCE_NAME_PARAM_NAME, "");
        createPool(params);
    }
}
