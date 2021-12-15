/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.vsi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Properties;
import org.gdal.gdal.gdal;
import org.junit.Test;

/**
 * Tests for VSIProperties class
 *
 * @author Matthew Northcott <matthewnorthcott@catalyst.net.nz>
 */
public final class VSIPropertiesTest {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    @Test(expected = IOException.class)
    public void testParsePropertiesUnset() throws IOException {
        new VSIProperties();
    }

    @Test(expected = FileNotFoundException.class)
    public void testParsePropertiesDoesNotExist() throws IOException {
        System.setProperty(VSIProperties.LOCATION_PROPERTY, "/not/a/location");

        new VSIProperties();
    }

    @Test
    public void testParsePropertiesEmpty() throws IOException {
        final File file = Paths.get(TEMP_DIR, "empty.properties").toFile();

        file.createNewFile();
        System.setProperty(VSIProperties.LOCATION_PROPERTY, file.getAbsolutePath());

        assertTrue(new VSIProperties().isEmpty());

        file.delete();
    }

    @Test
    public void testPropertiesValid() throws IOException {
        final File file = Paths.get(TEMP_DIR, "vsi.properties").toFile();
        final Properties prop = new Properties();

        prop.setProperty("OS_IDENTITY_API_VERSION", "3");
        prop.setProperty("OS_AUTH_URL", "https://cloud.provider.net/v3");
        prop.setProperty("OS_USERNAME", "myuser");
        prop.setProperty("OS_PASSWORD", "hunter2");
        prop.setProperty("OS_USER_DOMAIN_NAME", "Default");
        prop.setProperty("OS_PROJECT_NAME", "myproject");
        prop.setProperty("OS_PROJECT_DOMAIN_NAME", "default");

        file.createNewFile();
        System.setProperty(VSIProperties.LOCATION_PROPERTY, file.getAbsolutePath());

        try (FileOutputStream stream = new FileOutputStream(file)) {
            prop.store(stream, null);
        } catch (IOException ex) {
            throw ex;
        }

        final VSIProperties properties = new VSIProperties();

        file.delete();

        assertEquals(properties.size(), 7);
        assertEquals(properties.getProperty("OS_IDENTITY_API_VERSION"), "3");
        assertEquals(properties.getProperty("OS_AUTH_URL"), "https://cloud.provider.net/v3");
        assertEquals(properties.getProperty("OS_USERNAME"), "myuser");
        assertEquals(properties.getProperty("OS_PASSWORD"), "hunter2");
        assertEquals(properties.getProperty("OS_USER_DOMAIN_NAME"), "Default");
        assertEquals(properties.getProperty("OS_PROJECT_NAME"), "myproject");
        assertEquals(properties.getProperty("OS_PROJECT_DOMAIN_NAME"), "default");
    }

    @Test
    public void testApply() throws IOException, URISyntaxException {
        final File file = Paths.get(TEMP_DIR, "vsi.properties").toFile();
        final Properties prop = new Properties();

        prop.setProperty("OS_IDENTITY_API_VERSION", "3");
        prop.setProperty("OS_AUTH_URL", "https://cloud.provider.net/v3");
        prop.setProperty("OS_USERNAME", "myuser");
        prop.setProperty("OS_PASSWORD", "hunter2");
        prop.setProperty("OS_USER_DOMAIN_NAME", "Default");
        prop.setProperty("OS_PROJECT_NAME", "myproject");
        prop.setProperty("OS_PROJECT_DOMAIN_NAME", "default");

        file.createNewFile();
        System.setProperty(VSIProperties.LOCATION_PROPERTY, file.getAbsolutePath());

        try (FileOutputStream stream = new FileOutputStream(file)) {
            prop.store(stream, null);
        } catch (IOException ex) {
            throw ex;
        }

        try {
            new VSIProperties().apply();

            assertEquals(gdal.GetConfigOption("OS_IDENTITY_API_VERSION"), "3");
            assertEquals(gdal.GetConfigOption("OS_AUTH_URL"), "https://cloud.provider.net/v3");
            assertEquals(gdal.GetConfigOption("OS_USERNAME"), "myuser");
            assertEquals(gdal.GetConfigOption("OS_PASSWORD"), "hunter2");
            assertEquals(gdal.GetConfigOption("OS_USER_DOMAIN_NAME"), "Default");
            assertEquals(gdal.GetConfigOption("OS_PROJECT_NAME"), "myproject");
            assertEquals(gdal.GetConfigOption("OS_PROJECT_DOMAIN_NAME"), "default");
        } catch (NoClassDefFoundError | UnsatisfiedLinkError ex) {
            assumeNoException(ex);
        } finally {
            file.delete();
        }
    }
}
