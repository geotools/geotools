/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.geotools.data.DataUtilities;

import junit.framework.TestCase;

/**
 * Tests for {@link PropertyInterpolationUtils}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 *
 *
 * @source $URL$
 */
public class PropertyInterpolationUtilsTest extends TestCase {

    /**
     * Base string used for properties and filenames.
     */
    public static final String IDENTIFIER = "property-interpolation-utils-test";

    /**
     * Test property set in the property files.
     */
    public static final String TEST_PROPERTY = IDENTIFIER + ".test.property";

    /**
     * Test property set in system properties.
     */
    public static final String TEST_SYSTEM_PROPERTY = IDENTIFIER + ".test.system.property";

    /**
     * Value of property set in system properties.
     */
    public static final String TEST_SYSTEM_PROPERTY_VALUE = "system";

    /**
     * System property set to trigger file (not classpath) loading.
     */
    public static final String TEST_FILE_SYSTEM_PROPERTY = IDENTIFIER + "-file-identifier"
            + ".properties";

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty(TEST_SYSTEM_PROPERTY, TEST_SYSTEM_PROPERTY_VALUE);
        System.setProperty(TEST_FILE_SYSTEM_PROPERTY, DataUtilities.urlToFile(
                PropertyInterpolationUtilsTest.class.getResource("/" + IDENTIFIER
                        + ".file.properties")).getPath());
    }

    /**
     * Test for {@link PropertyInterpolationUtils#interpolate(Properties, String)} that properties
     * are interpolated as expected. Note that this test includes multiple lines, and also
     * opportunity for excessive regex greed, which must be avoided.
     */
    public static void testInterpolate() {
        Properties properties = new Properties();
        properties.put("foo.x", "123");
        properties.put("foo.y", "abc");
        properties.put("foo.z", "bar");
        String result = PropertyInterpolationUtils.interpolate(properties,
                "123ajh${foo.z} akl ${foo.y}${foo.y} laskj ${foo.x}\n"
                        + "foo.x${foo.x}${foo.x} ${foo.z}${foo.y}");
        assertEquals("123ajhbar akl abcabc laskj 123\nfoo.x123123 barabc", result);
    }

    /**
     * Test for {@link PropertyInterpolationUtils#interpolate(Properties, String)} that
     * interpolating a nonexistent property is an error.
     */
    public static void testInterpolateNonexistent() {
        boolean interpolatedNonexistentProperty = false;
        try {
            PropertyInterpolationUtils.interpolate(new Properties(), "123 ${does.not.exist} 456");
            interpolatedNonexistentProperty = true;
        } catch (Exception e) {
            // success
        }
        assertFalse(interpolatedNonexistentProperty);
    }

    /**
     * Test for {@link PropertyInterpolationUtils#readAll(InputStream)}.
     */
    public static void testReadAll() {
        String s = "line one\nline two\n";
        InputStream input = new ByteArrayInputStream("line one\nline two\n".getBytes());
        assertEquals(s, PropertyInterpolationUtils.readAll(input));
    }

    /**
     * Test that {@link PropertyInterpolationUtils#loadProperties(String)} can load properties from
     * the classpath.
     */
    public static void testLoadPropertiesFromClasspath() {
        Properties properties = PropertyInterpolationUtils.loadProperties(IDENTIFIER);
        assertEquals("found-on-classpath", properties.getProperty(TEST_PROPERTY));
        checkSystemProperties(properties);
    }

    /**
     * Test that in {@link PropertyInterpolationUtils#loadProperties(String)} only system properties
     * are loaded when the properties files does not exist.
     */
    public static void testLoadPropertiesDoesNotExist() {
        Properties properties = PropertyInterpolationUtils.loadProperties(IDENTIFIER
                + ".does-not-exist");
        assertNull(properties.getProperty(TEST_PROPERTY));
        checkSystemProperties(properties);
    }

    /**
     * Test that {@link PropertyInterpolationUtils#loadProperties(String)} can load properties from
     * the a file specified in a system property.
     */
    public static void testLoadPropertiesFromFile() {
        // note that the identifier does *not* match the filename
        Properties properties = PropertyInterpolationUtils.loadProperties(TEST_FILE_SYSTEM_PROPERTY
                .replace(".properties", ""));
        assertEquals("found-in-file", properties.getProperty(TEST_PROPERTY));
        checkSystemProperties(properties);
    }

    /**
     * Check that the system properties are as expected.
     * 
     * @param properties
     */
    private static void checkSystemProperties(Properties properties) {
        // check that synthetic system property is loaded
        assertNotNull(properties.getProperty(TEST_SYSTEM_PROPERTY));
        assertTrue(properties.getProperty(TEST_SYSTEM_PROPERTY).equals(TEST_SYSTEM_PROPERTY_VALUE));
        // check we are loading real system properties
        assertNotNull(properties.getProperty("java.version"));
        // system properties should override this
        assertFalse(properties.getProperty("java.version").equals("should-be-overridden"));
    }

}
