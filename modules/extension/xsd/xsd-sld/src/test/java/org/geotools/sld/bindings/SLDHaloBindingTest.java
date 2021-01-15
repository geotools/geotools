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
package org.geotools.sld.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.filter.Filters;
import org.geotools.styling.Halo;
import org.junit.Test;

public class SLDHaloBindingTest extends SLDTestSupport {
    @Test
    public void testType() throws Exception {
        assertEquals(Halo.class, new SLDHaloBinding(null, null).getType());
    }

    @Test
    public void test() throws Exception {
        SLDMockData.halo(document, document, true);

        Halo halo = (Halo) parse();
        assertNotNull(halo);
        assertNotNull(halo.getFill());
        assertEquals(1.0d, Filters.asDouble(halo.getRadius()), 0d);
    }

    @Test
    public void testDefaults() throws Exception {
        SLDMockData.halo(document, document, false);

        Halo halo = (Halo) parse();
        assertNotNull(halo);
        assertNotNull(halo.getFill());
        assertEquals(1.0d, Filters.asDouble(halo.getRadius()), 0d);
    }
}
