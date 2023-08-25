/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.styling.ContrastEnhancementImpl;
import org.junit.Test;

public class SLDContrastEnhancementImplBindingTest extends SLDTestSupport {
    @Test
    public void testType() throws Exception {
        assertEquals(
                ContrastEnhancementImpl.class,
                new SLDContrastEnhancementBinding(null, null).getType());
    }

    @Test
    public void testHistogram() throws Exception {
        SLDMockData.contrastEnhancement(document, document);

        ContrastEnhancementImpl ce = (ContrastEnhancementImpl) parse();
        assertNotNull(ce);

        assertNotNull(ce.getGammaValue());
        assertEquals(1.23, Filters.asDouble(ce.getGammaValue()), 0d);

        assertNotNull(ce.getMethod());
        assertEquals("histogram", ce.getMethod().name().toLowerCase());
    }

    @Test
    public void testExpressionGammaValue() throws Exception {
        SLDMockData.contrastEnhancementExpressionGammaValue(document, document);

        ContrastEnhancementImpl ce = (ContrastEnhancementImpl) parse();
        assertNotNull(ce);

        assertNotNull(ce.getGammaValue());
        assertEquals(1.5, Filters.asDouble(ce.getGammaValue()), 0d);
    }
}
