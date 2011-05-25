/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.test;

import java.util.Properties;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;

/**
 * JUnit 4 test support for test cases that require an "online" resource, such as an external server
 * or database.
 * 
 * <p>
 * 
 * See {@link OnlineTestCase} for details of behaviour and test fixture configuration.
 * 
 * <p>
 * 
 * Subclass names should end with "OnlineTest" to allow Maven to treat them specially.
 * 
 * <p>
 * 
 * This class contains an adapter to {@link OnlineTestCase} that allows its use with JUnit 4.
 * Delegation is used to recycle the behaviour of {@link OnlineTestCase} without extending
 * {@link TestCase}. This is necessary because {@link TestRunner}s appear to give priority to JUnit
 * 3 behaviour, ignoring JUnit 4 annotations in suites that extend {@link TestCase}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 * @see OnlineTestCase
 *
 * @source $URL$
 */
public abstract class OnlineTestSupport {

    /**
     * The delegate {@link OnlineTestCase} instance.
     */
    private final DelegateOnlineTestCase delegate = new DelegateOnlineTestCase();

    @Before
    public void before() throws Exception {
        // disable test if fixture not available
        // must call checkAvailable to configure fixture before calling setUp
        Assume.assumeTrue(delegate.checkAvailable());
        delegate.setUp();
    }

    @After
    public void after() throws Exception {
        delegate.tearDown();
    }

    /**
     * Subclasses must override this method to return a fixture id.
     * 
     * @return fixture id
     * @see OnlineTestCase#getFixtureId()
     */
    protected abstract String getFixtureId();

    /**
     * Override this method to connect to an online resource. Throw an exception on failure.
     * 
     * <p>
     * 
     * Subclasses do not have to override this method, but doing so allows builders to choose to
     * have this test disable itself when the online resource is not available.
     * 
     * @throws Exception
     * @see OnlineTestCase#connect()
     */
    protected void connect() throws Exception {
    }

    /**
     * Override this method to disconnect from an online resource. Throw an exception on failure.
     * 
     * @throws Exception
     * @see OnlineTestCase#disconnect()
     */
    protected void disconnect() throws Exception {
    }

    /**
     * Override this method to return false if you can detect that an online resource required for
     * this test is not available,
     * 
     * @return false if a required resource is not online
     * @throws Exception
     * @see OnlineTestCase#isOnline()
     */
    protected boolean isOnline() throws Exception {
        return true;
    }

    /**
     * Method for subclasses to latch onto the setup phase.
     * 
     * @see OnlineTestCase#setUpInternal()
     */
    protected void setUpInternal() throws Exception {
    }

    /**
     * Method for subclasses to latch onto the teardown phase.
     * 
     * @see OnlineTestCase#tearDownInternal()
     */
    protected void tearDownInternal() throws Exception {
    }

    /**
     * Allows tests to create an offline fixture in cases where the user has not specified an
     * explicit fixture for the test.
     * <p>
     * Note, that this should method should on be implemented if the test case is created of
     * creating a fixture which relies solely on embedded or offline resources. It should not
     * reference any external or online resources as it prevents the user from running offline.
     * </p>
     * 
     * @see OnlineTestCase#createOfflineFixture()
     */
    protected Properties createOfflineFixture() {
        return null;
    }

    /**
     * Allows test to create a sample fixture for users.
     * <p>
     * If this method returns a value the first time a fixture is looked up and not found this
     * method will be called to create a fixture file with the same id, but suffixed with .template.
     * </p>
     * 
     * @see OnlineTestCase#createExampleFixture()
     */
    protected Properties createExampleFixture() {
        return null;
    }

    /**
     * Return properties configured in the fixture.
     * 
     * <p>
     * 
     * This method allows subclasses in other packages to access fixture properties.
     * 
     * @return properties configured in the fixture.
     */
    protected Properties getFixture() {
        return delegate.fixture;
    }

    /**
     * The delegate {@link OnlineTestCase} adapter.
     */
    private class DelegateOnlineTestCase extends OnlineTestCase {

        /**
         * @see org.geotools.test.OnlineTestCase#getFixtureId()
         */
        @Override
        protected String getFixtureId() {
            return OnlineTestSupport.this.getFixtureId();
        }

        /**
         * @see org.geotools.test.OnlineTestCase#connect()
         */
        @Override
        protected void connect() throws Exception {
            OnlineTestSupport.this.connect();
        }

        /**
         * @see org.geotools.test.OnlineTestCase#disconnect()
         */
        @Override
        protected void disconnect() throws Exception {
            OnlineTestSupport.this.disconnect();
        }

        /**
         * @see org.geotools.test.OnlineTestCase#isOnline()
         */
        @Override
        protected boolean isOnline() throws Exception {
            return OnlineTestSupport.this.isOnline();
        }

        /**
         * @see org.geotools.test.OnlineTestCase#setUpInternal()
         */
        @Override
        protected void setUpInternal() throws Exception {
            OnlineTestSupport.this.setUpInternal();
        }

        /**
         * @see org.geotools.test.OnlineTestCase#tearDownInternal()
         */
        @Override
        protected void tearDownInternal() throws Exception {
            OnlineTestSupport.this.tearDownInternal();
        }

        /**
         * @see org.geotools.test.OnlineTestCase#createExampleFixture()
         */
        @Override
        protected Properties createExampleFixture() {
            return OnlineTestSupport.this.createExampleFixture();
        }

        /**
         * @see org.geotools.test.OnlineTestCase#createOfflineFixture()
         */
        @Override
        protected Properties createOfflineFixture() {
            return OnlineTestSupport.this.createOfflineFixture();
        }

    }

}
