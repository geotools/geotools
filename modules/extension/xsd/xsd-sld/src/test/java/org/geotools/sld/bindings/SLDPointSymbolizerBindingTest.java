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

import org.geotools.api.filter.expression.Function;
import org.geotools.api.style.PointSymbolizer;
import org.junit.Test;

public class SLDPointSymbolizerBindingTest extends SLDTestSupport {
    @Test
    public void testType() throws Exception {
        assertEquals(PointSymbolizer.class, new SLDPointSymbolizerBinding(null).getType());
    }

    @Test
    public void test() throws Exception {
        SLDMockData.pointSymbolizer(document, document);

        PointSymbolizer ps = (PointSymbolizer) parse();
        assertNotNull(ps);
        assertNotNull(ps.getGeometryPropertyName());
        assertNotNull(ps.getGraphic());
    }

    @Test
    public void testTransform() throws Exception {
        SLDMockData.transformedPointSymbolizer(document, document);

        PointSymbolizer ps = (PointSymbolizer) parse();
        assertNotNull(ps);
        assertNotNull(ps.getGeometry());
        Function tx = (Function) ps.getGeometry();
        assertEquals("buffer", tx.getName());
    }
}
