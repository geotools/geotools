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
package org.geotools.renderer.style;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

/**
 * Makes sure the symbol factory lookup works as advertised
 * @author Andrea Aime - TOPP
 *
 *
 *
 * @source $URL$
 */
public class DynamicSymbolFactoryFinderTest extends TestCase {

    public void testLookupMarkFactories() {
        List<MarkFactory> result = loadIterator(DynamicSymbolFactoryFinder.getMarkFactories());
        assertTrue(result.size() >= 2);
        assertContainsClassInstance(result, WellKnownMarkFactory.class);
        assertContainsClassInstance(result, TTFMarkFactory.class);
    }
    
    public void testLookupExternalGraphicFactories() {
        List<ExternalGraphicFactory> result = loadIterator(DynamicSymbolFactoryFinder.getExternalGraphicFactories());
        assertTrue(result.size() >= 1);
        assertContainsClassInstance(result, ImageGraphicFactory.class);
    }
    
    public void assertContainsClassInstance(List list, Class clazz) {
        for (Object item : list) {
            if(item != null && clazz.isAssignableFrom(item.getClass()))
                return;
        }
        fail("List does not contain any element of class " + clazz.getName());
    }
    
    public <T> List<T> loadIterator(Iterator<T> iterator) {
        List<T> result = new ArrayList<T>();
        while(iterator.hasNext())
            result.add(iterator.next());
        return result;
    }
}
