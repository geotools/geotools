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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods to support interpolation of properties in a file.
 * 
 * <p>
 * 
 * Interpolation means the substitution of a string of the form ${some.property} with the value of
 * the property called "some.property".
 * 
 * <p>
 * 
 * Interpolation is performed repeatedly, so can values can contain new interpolations. Infinite
 * loops are supported. This is not a feature.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 *
 *
 * @source $URL$
 */
public class PropertyInterpolationUtils {

    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(PropertyInterpolationUtils.class.getPackage().getName());

    /**
     * Pattern to match a property to be substituted. Note the reluctant quantifier.
     */
    private static final Pattern PROPERTY_INTERPOLATION_PATTERN = Pattern.compile("\\$\\{(.+?)\\}");

    /**
     * Interpolate all the properties in the input string.
     * 
     * <p>
     * 
     * Properties are of the form ${some.property}, for which the value of property "some.property"
     * is used.
     * 
     * <p>
     * 
     * It is an error for interpolated properties to not exist. A {@link RuntimeException} is thrown
     * if the value of a referenced property is null.
     * 
     * @param properties
     *            properties to be interpolated
     * @param input
     *            string on which interpolation is to be performed
     * @return string with all properties expanded
     */
    public static String interpolate(Properties properties, String input) {
        String result = input;
        Matcher matcher = PROPERTY_INTERPOLATION_PATTERN.matcher(result);
        while (matcher.find()) {
            String propertyName = matcher.group(1);
            String propertyValue = (String) properties.get(propertyName);
            if (propertyValue == null) {
                throw new RuntimeException("Interpolation failed for missing property "
                        + propertyName);
            } else {
                result = result.replace(matcher.group(), propertyValue);
                matcher.reset(result);
            }
        }
        return result;
    }

    /**
     * Load properties from a configuration file.
     * 
     * <p>
     * 
     * The name of the properties file is constructed by appending ".properties" to the identifier.
     * If there is a system property with the name of this property file, it is used as a file to
     * load, otherwise the property file is loaded from the root of the classpath.
     * 
     * <p>
     * 
     * For example, if the identifier is <tt>app-schema</tt>:
     * 
     * <ul>
     * 
     * <li>If the system property <tt>app-schema.properties</tt> is set, e.g.
     * <tt>-Dapp-schema.properties=/path/to/some/local.properties</tt>, the indicated file, in this
     * case <tt>/path/to/some/local.properties</tt>, is loaded.
     * 
     * <li>Otherwise, the classpath resource <tt>/app-schema.properties</tt> is loaded.
     * 
     * </ul>
     * 
     * Before the properties are returned, all system properties are copied; this means that system
     * properties override any properties in the configuration file.
     * 
     * @param identifier
     *            string used to construct property file name
     * @return loaded properties
     */
    public static Properties loadProperties(String identifier) {
        String propertiesName = identifier + ".properties";
        Properties properties = new Properties();
        String propertiesFileName = System.getProperty(propertiesName);
        if (propertiesFileName == null) {
            String propertiesResourceName = "/" + propertiesName;
            InputStream stream = PropertyInterpolationUtils.class
                    .getResourceAsStream(propertiesResourceName);
            if (stream != null) {
                try {
                    LOGGER.info("Loading properties from classpath resource "
                            + propertiesResourceName);
                    properties.load(new BufferedInputStream(stream));
                } catch (Exception e) {
                    throw new RuntimeException("Error loading properties from classpath resource "
                            + propertiesResourceName, e);
                } finally {
                    try {
                        stream.close();
                    } catch (Exception e) {
                        // ignore, we tried
                    }
                }
            }
        } else {
            File propertiesFile = new File(propertiesFileName).getAbsoluteFile();
            try {
                propertiesFile = propertiesFile.getCanonicalFile();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING,
                        "An error occurred while trying to construct the canonical path"
                                + " for properties file " + propertiesFile.toString(), e);
            }
            InputStream stream = null;
            try {
                stream = new BufferedInputStream(new FileInputStream(propertiesFile));
                LOGGER.info("Loading properties file " + propertiesFile.toString());
                properties.load(stream);
            } catch (Exception e) {
                throw new RuntimeException("Error loading properties file "
                        + propertiesFile.toString(), e);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Exception e) {
                        // ignore, we tried
                    }
                }
            }
        }
        properties.putAll(System.getProperties());
        return properties;
    }

    /**
     * Read everything from an input stream into a String, reconstructing line endings.
     * 
     * @param input
     *            the stream to be read
     * @return a string that contains the content of input
     */
    public static String readAll(InputStream input) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuffer buffer = new StringBuffer();
        while (true) {
            String line;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) {
                break;
            } else {
                buffer.append(line);
                buffer.append("\n");
            }
        }
        return buffer.toString();
    }

}
