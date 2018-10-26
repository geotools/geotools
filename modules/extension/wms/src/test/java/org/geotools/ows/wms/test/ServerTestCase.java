/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wms.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
import junit.framework.TestCase;

/**
 * A JUnit TestCase subclass that allows other test cases to talk to remote servers, but does not
 * fail if those servers are down or otherwise unavailable.
 *
 * <p>Note that it is still the responsibility of the individual test case to decrease time-out
 * time, if that is desired.
 *
 * <p>It is recommended that developers using this class do not output any other text from within
 * their test cases. This means that output will only be generated if there is an issue, and can
 * make scanning for problems easier.
 *
 * <p>It will currently catch and print out network-related IOExceptions thrown by setup() and
 * test*() methods.
 *
 * @author rgould
 */
public class ServerTestCase extends TestCase {

    public ServerTestCase() {
        super();
    }

    public ServerTestCase(String arg0) {
        super(arg0);
    }

    public void runBare() throws Throwable {
        try {
            super.runBare();
        } catch (ConnectException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        } catch (UnknownHostException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        } catch (NoRouteToHostException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        } catch (FileNotFoundException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        } catch (IOException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    protected void runTest() throws Throwable {
        try {
            super.runTest();
        } catch (ConnectException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        } catch (UnknownHostException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        } catch (NoRouteToHostException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        } catch (FileNotFoundException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        } catch (IOException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }
}
