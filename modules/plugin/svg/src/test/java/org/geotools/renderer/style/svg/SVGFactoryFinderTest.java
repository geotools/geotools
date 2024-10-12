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
package org.geotools.renderer.style.svg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.geotools.renderer.style.DynamicSymbolFactoryFinder;
import org.geotools.renderer.style.ExternalGraphicFactory;
import org.geotools.renderer.style.MarkFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Makes sure the symbol factory lookup works as advertised
 *
 * @author Andrea Aime - TOPP
 */
public class SVGFactoryFinderTest {

    @Test
    public void testLookupExternalGraphicFactories() {
        List<ExternalGraphicFactory> result = loadIterator(DynamicSymbolFactoryFinder.getExternalGraphicFactories());
        Assert.assertFalse(result.isEmpty());
        assertContainsClassInstance(result, SVGGraphicFactory.class);
    }

    @Test
    public void testLookupMarkFactories() {
        List<MarkFactory> result = loadIterator(DynamicSymbolFactoryFinder.getMarkFactories());
        Assert.assertFalse(result.isEmpty());
        assertContainsClassInstance(result, MarkFactory.class);
    }

    public void assertContainsClassInstance(List list, Class<?> clazz) {
        for (Object item : list) {
            if (item != null && clazz.isAssignableFrom(item.getClass())) return;
        }
        Assert.fail("List does not contain any element of class " + clazz.getName());
    }

    public <T> List<T> loadIterator(Iterator<T> iterator) {
        List<T> result = new ArrayList<>();
        while (iterator.hasNext()) result.add(iterator.next());
        return result;
    }
}
