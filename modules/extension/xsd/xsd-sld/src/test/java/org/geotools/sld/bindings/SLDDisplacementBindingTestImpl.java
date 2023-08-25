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

import org.geotools.filter.Filters;
import org.geotools.styling.DisplacementImpl;
import org.junit.Test;

public class SLDDisplacementBindingTestImpl extends SLDTestSupport {
    @Test
    public void testType() throws Exception {
        assertEquals(DisplacementImpl.class, new SLDDisplacementBinding(null).getType());
    }

    @Test
    public void testNormal() throws Exception {
        SLDMockData.displacement(document, document);

        DisplacementImpl d = (DisplacementImpl) parse();
        assertEquals(Filters.asInt(d.getDisplacementX()), 1);
        assertEquals(Filters.asInt(d.getDisplacementY()), 2);
    }
}
