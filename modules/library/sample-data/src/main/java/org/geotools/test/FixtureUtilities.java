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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * Static methods to support the implementation of tests that use fixture configuration files. See
 * {@link OnlineTestCase} and {@link OnlineTestSupport} for details.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 */
public class FixtureUtilities {

    /**
     * Load {@link Properties} from a {@link File}.
     */
    public static Properties loadProperties(File file) {
        try {
            InputStream input = null;
            try {
                input = new BufferedInputStream(new FileInputStream(file));
                Properties properties = new Properties();
                properties.load(input);
                return properties;
            } finally {
                if (input != null) {
                    input.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the directory containing GeoTools test fixture configuration files. This is
     * ".geotools" in the user home directory.
     */
    public static File getFixtureDirectory() {
        return new File(System.getProperty("user.home") + File.separator + ".geotools");
    }

    /**
     * Return the file that should contain the fixture configuration properties. It is not
     * guaranteed to exist.
     * 
     * <p>
     * 
     * Dots "." in the fixture id represent a subdirectory path under the GeoTools configuration
     * file directory. For example, an id <code>a.b.foo</code> would be resolved to
     * <code>.geotools/a/b/foo.properties<code>.
     * 
     * @param fixtureDirectory
     *            the base fixture configuration file directory, typically ".geotools" in the user
     *            home directory.
     * @param fixtureId
     *            the fixture id
     */
    public static File getFixtureFile(File fixtureDirectory, String fixtureId) {
        return new File(fixtureDirectory, fixtureId.replace('.', File.separatorChar).concat(
                ".properties"));
    }

    /**
     * Print a notice that tests are being skipped, identifying the property file whose absence is
     * responsible.
     * 
     * @param fixtureId
     *            the fixture id
     * @param fixtureFile
     *            the missing fixture configuration file
     */
    public static void printSkipNotice(String fixtureId, File fixtureFile) {
        try {
            System.out.println("Skipping " + fixtureId + " tests. Fixture file "
                    + fixtureFile.getCanonicalPath() + " not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return Properties loaded from a fixture configuration file, or null if not found.
     * 
     * <p>
     * 
     * If a fixture configuration file is not found, a notice is printed to standard output stating
     * that tests for this fixture id are skipped.
     * 
     * <p>
     * 
     * This method allows tests that cannot extend {@link OnlineTestCase} or
     * {@link OnlineTestSupport} because they already extend another class (for example, a
     * non-online test framework) to access fixture configuration files in the same way that those
     * classes do. Only basic fixture configuration loading is supported. This method does not
     * support the extra services such as fixture caching and connection testing provided by
     * {@link OnlineTestCase} and {@link OnlineTestSupport}.
     * 
     * <p>
     * 
     * A JUnit 4 test fixture can readily be disabled in the absence of a fixture configuration file
     * by placing <code>Assume.assumeNotNull(FixtureUtilities.loadFixture(fixtureId))</code> or
     * similar in its <code>@BeforeClass</code> method. JUnit 3 tests must provide their own logic,
     * typically overriding {@link TestCase#run()} or {@link TestCase#runTest()}, or providing a
     * suite.
     * 
     * @param fixtureId
     *            the fixture id, where dots "." are converted to subdirectories.
     * @return the fixture Properties or null
     * @see OnlineTestCase
     * @see OnlineTestSupport
     */
    public static Properties loadFixture(String fixtureId) {
        File fixtureFile = getFixtureFile(getFixtureDirectory(), fixtureId);
        if (fixtureFile.exists()) {
            return loadProperties(fixtureFile);
        } else {
            printSkipNotice(fixtureId, fixtureFile);
            return null;
        }
    }

}
