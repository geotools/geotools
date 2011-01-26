/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.gml3.bindings;

import java.lang.reflect.Constructor;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.gml3.GML;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.Binding;

/**
 * Test {@link GMLConfiguration}.
 */
public class GMLConfigurationTest extends TestCase {

    /**
     * Check that all bindings in GMLConfiguration have a target (XSD name), and that the GML ones
     * have a Java type.
     */
    @SuppressWarnings("unchecked")
    public void testBindingTypes() throws Exception {
        GMLConfiguration configuration = new GMLConfiguration();
        assertEquals(GML.NAMESPACE, configuration.getNamespaceURI());
        Map bindings = configuration.setupBindings();
        for (Object object : bindings.values()) {
            if (object instanceof Class) {
                Class type = (Class) object;
                if (Binding.class.isAssignableFrom(type)) {
                    Constructor c = type.getConstructors()[0];
                    Object[] params = new Object[c.getParameterTypes().length];
                    Binding binding = (Binding) c.newInstance(params);
                    assertNotNull(binding.getTarget());
                    if (binding.getTarget().getNamespaceURI().equals(GML.NAMESPACE)) {
                        assertNotNull(binding.getTarget() + " has a null type", binding.getType());
                    }
                }
            }
        }
    }
    
}
