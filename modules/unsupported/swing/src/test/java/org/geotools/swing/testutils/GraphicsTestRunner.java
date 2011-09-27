/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.testutils;

import java.awt.GraphicsEnvironment;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.notification.RunNotifier;

/**
 * A test runner that will skip tests if the build is headless or if
 * the {@code org.geotools.test.interactive} system property is {@code false}
 * or undefined. To use this runner, annotate the test class with 
 * {@code @RunWith(GraphicsTestRunner.class)}.
 * <p>
 * The system property can be set via the Maven command line with 
 * {@code -Dinteractive.tests}.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class GraphicsTestRunner extends JUnit4ClassRunner {
    
    private static final Boolean INTERACTIVE = Boolean.getBoolean("org.geotools.test.interactive");
    
    public GraphicsTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        Logger logger = Logging.getLogger("org.geotools.swing");
        
        if (!INTERACTIVE || GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance()) {
            logger.log(Level.INFO, "Skipping graphics tests in {0}", getTestClass().getName());
        } else {
            super.run(notifier);
        }
    }
    
}
