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
package org.geotools.processing.jai;

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

public class TransparencyFillRegistryAllowListProviderTest {

    @Test
    public void providerIsDiscoverableViaServiceLoader() {
        RegistryAllowListProvider provider = loadProvider();

        assertNotNull(provider);
        assertEquals(TransparencyFillRegistryAllowListProvider.class, provider.getClass());
    }

    @Test
    public void providerMatchesBundledRegistryFile() throws Exception {
        RegistryAllowListProvider provider = loadProvider();
        Set<String> expected = readRegistryClasses();

        assertNotNull(expected);
        assertEquals(expected, provider.getAllowedRegistryClasses());
    }

    @Test
    public void registerServicesLoadsTransparencyFillDescriptorWithoutManualConfiguration() throws Exception {
        OperationRegistry registry = new OperationRegistry();

        registry.registerServices(getClass().getClassLoader());

        assertNotNull(registry.getDescriptor(OperationDescriptor.class, new TransparencyFillDescriptor().getName()));
    }

    private RegistryAllowListProvider loadProvider() {
        for (RegistryAllowListProvider provider : ServiceLoader.load(RegistryAllowListProvider.class)) {
            if (provider.getClass().equals(TransparencyFillRegistryAllowListProvider.class)) {
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
                if (classes.contains(TransparencyFillDescriptor.class.getName())) {
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
