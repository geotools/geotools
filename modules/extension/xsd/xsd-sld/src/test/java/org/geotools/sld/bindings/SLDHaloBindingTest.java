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

import org.geotools.filter.Filters;
import org.geotools.styling.Halo;


/**
 * 
 *
 * @source $URL$
 */
public class SLDHaloBindingTest extends SLDTestSupport {
    public void testType() throws Exception {
        assertEquals(Halo.class, new SLDHaloBinding(null, null).getType());
    }

    public void test() throws Exception {
        SLDMockData.halo(document, document, true);

        Halo halo = (Halo) parse();
        assertNotNull(halo);
        assertNotNull(halo.getFill());
        assertEquals(1.0d, Filters.asDouble(halo.getRadius()), 0d);
    }
    
    public void testDefaults() throws Exception {
        SLDMockData.halo(document, document, false);

        Halo halo = (Halo) parse();
        assertNotNull(halo);
        assertNotNull(halo.getFill());
        assertEquals(1.0d, Filters.asDouble(halo.getRadius()), 0d);
    }
}
