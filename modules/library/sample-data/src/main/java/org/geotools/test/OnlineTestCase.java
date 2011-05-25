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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;
import junit.framework.TestResult;


/**
 * Test support for test cases that require an "online" resource, such as an
 * external server or database.
 * <p>
 * Online tests work off of a "fixture". A fixture is a properties file which
 * defines connection parameters for some remote service. Each online test case
 * must define the id of the fixture is uses with {@link #getFixtureId()}.
 * </p>
 * <p>
 * Fixtures are stored under the users home directory, under the "<code>.geotools</code>"
 * directory. Dots "." in the fixture id represent a subdirectory path under this
 * configuration file directory. For example, a fixture id <code>a.b.foo</code> would be
 * resolved to <code>.geotools/a/b/foo.properties<code>.
 * </p>
 * <p>
 * In the event that a fixture does not exist, the test case is
 * aborted.
 * </p>
 * <p>
 * Online tests connect to remote / online resources. Test cases should do all
 * connection / disconnection in the {@link #connect} and {@link #disconnect()}
 * methods.
 * </p>
 *
 * <p>
 * The default behaviour of this class is that if {@link #connect()} throws an exception, the test
 * suite is disabled, causing each test to pass without being run. In addition, exceptions thrown by
 * {@link #disconnect()} are ignored. This behaviour allows tests to be robust against transient
 * outages of online resources, but also means that local software failures in {@link #connect()} or
 * {@link #disconnect()} will be silent.
 * </p>
 * 
 * <p>
 * To have exceptions thrown by {@link #connect()} and {@link #disconnect()} cause tests to fail,
 * set <code>skip.on.failure=false</code> in the fixture property file. This restores the
 * traditional behaviour of unit tests, that is, that exceptions cause unit tests to fail.
 * </p>
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Justin Deoliveira, The Open Planning Project
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 */
public abstract class OnlineTestCase extends TestCase {
    /**
     * System property set to totally disable any online tests
     */
    public static final String ONLINE_TEST_PROFILE = "onlineTestProfile";
    
    /**
     * The key in the test fixture property file used to set the behaviour of the online test if
     * {@link #connect()} fails.
     */
    public static final String SKIP_ON_FAILURE_KEY = "skip.on.failure";

    /**
     * The default value used for {@link #SKIP_ON_FAILURE_KEY} if it is not present.
     */
    public static final String SKIP_ON_FAILURE_DEFAULT = "true";

    /**
     * A static map which tracks which fixtures are offline. This prevents continually trying to 
     * run a test when an external resource is offline.  
     */
    protected static Map<String,Boolean> online = new HashMap<String,Boolean>();
    
    /**
     * A static map which tracks which fixture files can not be found. This prevents
     * continually looking up the file and reporting it not found to the user.
     */
    protected static Map<String,Boolean> found = new HashMap<String,Boolean>();
    /**
     * The test fixture, {@code null} if the fixture is not available.
     */
    protected Properties fixture;
    /**
     * Flag that determines effect of exceptions in connect/disconnect. If true (the default),
     * exceptions in connect cause the the test to be disabled, and exceptions in disconnect to be
     * ignored. If false, exceptions will be rethrown, and cause the test to fail.
     */
    protected boolean skipOnFailure = true;

    /**
     * Override which checks if the fixture is available. If not the test is not
     * executed.
     */
    @Override
    public void run(TestResult result) {
        if (checkAvailable()) {
            super.run(result);
        }    
    }

    /**
     * Check whether the fixture is available. This method also loads the configuration if present,
     * and tests the connection using {@link #isOnline()}.
     * 
     * @return true if fixture is available for use
     */
    boolean checkAvailable() {
        configureFixture();
        if (fixture == null) {
            return false;
        } else {
            String fixtureId = getFixtureId();
            // do an online/offline check
            Boolean available = (Boolean) online.get(fixtureId);
            if (available == null || available.booleanValue()) {
                // test the connection
                try {
                    available = isOnline();
                } catch (Throwable t) {
                    System.out.println("Skipping " + fixtureId
                            + " tests, resources not available: " + t.getMessage());
                    t.printStackTrace();
                    available = Boolean.FALSE;
                }
                online.put(fixtureId, available);
            }
            return available;
        }
    }

    /**
     * Load fixture configuration. Create example if absent.
     */
    private void configureFixture() {
        if (fixture == null) {
            String fixtureId = getFixtureId();
            if (fixtureId == null) {
                return; // not available (turn test off)
            }
            try {
                // load the fixture
                File base = FixtureUtilities.getFixtureDirectory();
                // look for a "profile", these can be used to group related fixtures
                String profile = System.getProperty(ONLINE_TEST_PROFILE);
                if (profile != null && !"".equals(profile)) {
                    base = new File(base, profile);
                }
                File fixtureFile = FixtureUtilities.getFixtureFile(base, fixtureId);
                Boolean exists = found.get(fixtureFile.getCanonicalPath());
                if (exists == null || exists.booleanValue()) {
                    if (fixtureFile.exists()) {
                        fixture = FixtureUtilities.loadProperties(fixtureFile);
                        found.put(fixtureFile.getCanonicalPath(), true);
                    } else {
                        // no fixture file, if no profile was specified write out a template
                        // fixture using the offline fixture properties
                        if (profile == null) {
                            Properties exampleFixture = createExampleFixture();
                            if (exampleFixture != null) {
                                File exFixtureFile = new File(fixtureFile.getAbsolutePath()
                                        + ".example");
                                if (!exFixtureFile.exists()) {
                                    createExampleFixture(exFixtureFile, exampleFixture);
                                }
                            }
                        }
                        found.put(fixtureFile.getCanonicalPath(), false);
                    }
                }
                if (fixture == null) {
                    fixture = createOfflineFixture();
                }
                if (fixture == null && exists == null) {
                    // only report if exists == null since it means that this is
                    // the first time trying to load the fixture
                    FixtureUtilities.printSkipNotice(fixtureId, fixtureFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    void createExampleFixture(File exFixtureFile, Properties exampleFixture) {
        try {
            exFixtureFile.getParentFile().mkdirs();
            exFixtureFile.createNewFile();
            
            FileOutputStream fout = new FileOutputStream(exFixtureFile);
        
            exampleFixture.store(fout, "This is an example fixture. Update the " +
                "values and remove the .example suffix to enable the test"); 
            fout.flush();
            fout.close();
            System.out.println("Wrote example fixture file to " + exFixtureFile);
        }
        catch(IOException ioe) {
            System.out.println("Unable to write out example fixture " + exFixtureFile); 
            ioe.printStackTrace();
        }
    }
    /**
     * Loads the test fixture for the test case.
     * <p>
     * The fixture id is obtained via {@link #getFixtureId()}.
     * </p>
     */
    @Override
    protected final void setUp() throws Exception {
        super.setUp();
        setUpInternal();
        
        skipOnFailure = Boolean.parseBoolean(fixture.getProperty(SKIP_ON_FAILURE_KEY,
                SKIP_ON_FAILURE_DEFAULT));
        // call the setUp template method
        try {
            connect();
        } catch (Exception e) {
            if (skipOnFailure) {
                // disable the test
                fixture = null;
                // leave some trace of the swallowed exception
                e.printStackTrace();
            } else {
                // do not swallow the exception
                throw e;
            }
        }
    }

    /**
     * Method for subclasses to latch onto the setup phase.
     */
    protected void setUpInternal() throws Exception {}
    
    /**
     * Tear down method for test, calls through to {@link #disconnect()} if the
     * test is active.
     */
    @Override
    protected final void tearDown() throws Exception {
        tearDownInternal();
        if (fixture != null) {
            try {
                disconnect();
            } catch (Exception e) {
                if (skipOnFailure) {
                    // do nothing
                } else {
                    throw e;
                }
            }
        }
    }
    
    /**
     * Method for subclasses to latch onto the teardown phase.
     */
    protected void tearDownInternal() throws Exception {}

    /**
     * Tests if external resources needed to run the tests are online.
     * <p>
     * This method can return false to indicate the online resources are not up, or can simply
     * throw an exception. 
     * </p>
     * @return True if external resources are online, otherwise false.
     * @throws Exception Any errors that occur determining if online resources are available.
     */
    protected boolean isOnline() throws Exception {
        return true;
    }
    
    /**
     * Connection method, called from {@link #setUp()}.
     * <p>
     * Subclasses should do all initialization / connection here. In the event
     * of a connection not being available, this method should throw an
     * exception to abort the test case.
     * </p>
     * 
     * @throws Exception if the connection failed.
     */
    protected void connect() throws Exception {
    }

    /**
     * Disconnection method, called from {@link #tearDown()}.
     * <p>
     * Subclasses should do all cleanup here.
     * </p>
     * 
     * @throws Exception if the disconnection failed.
     */
    protected void disconnect() throws Exception {
    }

    /**
     * Allows tests to create an offline fixture in cases where the user has not
     * specified an explicit fixture for the test.
     * <p>
     * Note, that this should method should on be implemented if the test case
     * is created of creating a fixture which relies soley on embedded or offline
     * resources. It should not reference any external or online resources as it
     * prevents the user from running offline. 
     * </p>
     */
    protected Properties createOfflineFixture() {
        return null;
    }
    
    /**
     * Allows test to create a sample fixture for users. 
     * <p>
     * If this method returns a value the first time a fixture is looked up and not 
     * found this method will be called to create a fixture file with teh same id, but 
     * suffixed with .template.
     * </p>
     */
    protected Properties createExampleFixture() {
        return null;
    }
    
    /**
     * The fixture id for the test case.
     * <p>
     * This name is hierarchical, similar to a java package name. Example:
     * {@code "postgis.demo_bc"}.
     * </p>
     * 
     * @return The fixture id.
     */
    protected abstract String getFixtureId();
}
