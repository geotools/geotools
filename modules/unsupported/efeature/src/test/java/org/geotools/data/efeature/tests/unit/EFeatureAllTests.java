/**
 * <copyright> </copyright>
 *
 * <p>$Id$
 */
package org.geotools.data.efeature.tests.unit;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.tests.unit.conditions.EAttributeFilterAllTests;

/**
 * Test suite for {@link EFeature} implementation.
 *
 * @author kengu
 * @source $URL$
 */
public class EFeatureAllTests extends TestSuite {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public EFeatureAllTests(String name) {
        super(name);
    }

    public static Test suite() {
        //
        // Create test suite
        //
        TestSuite suite = new EFeatureAllTests("EFeature Tests");
        //
        // Add tests
        //
        suite.addTestSuite(DataBuilderTest.class);
        suite.addTestSuite(EFeatureContextTest.class);
        suite.addTestSuite(EFeatureQueryTest.class);
        suite.addTestSuite(EFeatureDataStoreTest.class);
        suite.addTestSuite(EFeatureReaderTest.class);
        suite.addTestSuite(EFeatureHintsTest.class);
        suite.addTest(EAttributeFilterAllTests.suite());
        //
        // Ready to execute
        //
        return suite;
    }
} // EFeatureAllTests
