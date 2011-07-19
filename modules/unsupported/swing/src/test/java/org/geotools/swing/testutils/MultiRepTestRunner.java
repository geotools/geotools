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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

/**
 * A runner for unit tests that should be run multiple times (e.g. concurrency
 * testing). To use, annotate the test class with {@code @RunWith(MultiRepTestRunner.class)}.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class MultiRepTestRunner extends JUnit4ClassRunner {
    
    // Set this to 1 for Hudson builds. Set to larger values when 
    // running the relevant tests on a local machine.
    final int N = 1;
    
    int numFailures;

    public MultiRepTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        Logger logger = Logging.getLogger("org.geotools.swing");
        logger.setLevel(Level.WARNING);
        
        notifier.addListener(new RunListener() {
            @Override
            public void testFailure(Failure failure) throws Exception {
                numFailures++ ;
            }
        });
        
        for (int i = 0; i < N; i++) {
            super.run(notifier);
        }
        
        System.out.printf("%d failures in %d runs\n", numFailures, N);
    }
    
}
