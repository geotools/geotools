/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.palette;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.ServiceLoader;
import java.util.Set;
import org.eclipse.imagen.OperationDescriptor;
import org.eclipse.imagen.OperationRegistry;
import org.eclipse.imagen.spi.RegistryAllowListProvider;
import org.geotools.test.imagen.RegistryFileTestSupport;
import org.junit.Test;

public class PaletteRegistryAllowListProviderTest {

    @Test
    public void providerIsDiscoverableViaServiceLoader() {
        RegistryAllowListProvider provider = loadProvider();

        assertNotNull(provider);
        assertEquals(PaletteRegistryAllowListProvider.class, provider.getClass());
    }

    @Test
    public void providerMatchesBundledRegistryFile() throws Exception {
        RegistryAllowListProvider provider = loadProvider();
        Set<String> expected = readRegistryClasses();

        assertNotNull(expected);
        assertEquals(expected, provider.getAllowedRegistryClasses());
    }

    @Test
    public void registerServicesLoadsCoverageDescriptorsWithoutManualConfiguration() throws Exception {
        OperationRegistry registry = new OperationRegistry();

        registry.registerServices(getClass().getClassLoader());

        assertNotNull(registry.getDescriptor(OperationDescriptor.class, ColorReductionDescriptor.OPERATION_NAME));
        assertNotNull(registry.getDescriptor(OperationDescriptor.class, ColorInversionDescriptor.OPERATION_NAME));
    }

    private RegistryAllowListProvider loadProvider() {
        for (RegistryAllowListProvider provider : ServiceLoader.load(RegistryAllowListProvider.class)) {
            if (provider.getClass().equals(PaletteRegistryAllowListProvider.class)) {
                return provider;
            }
        }
        return null;
    }

    private Set<String> readRegistryClasses() throws IOException {
        Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/registryFile.imagen");
        while (resources.hasMoreElements()) {
            try (InputStream stream = resources.nextElement().openStream()) {
                Set<String> classes = readRegistryClasses(stream);
                if (classes.contains(ColorReductionDescriptor.class.getName())) {
                    return classes;
                }
            }
        }
        return null;
    }

    private Set<String> readRegistryClasses(InputStream stream) throws IOException {
        assertNotNull(stream);
        return RegistryFileTestSupport.parseRegistryClasses(stream);
    }
}
