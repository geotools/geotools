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

import org.geotools.styling.TextSymbolizerImpl;
import org.junit.Test;

public class SLDTextSymbolizerImplBindingTest extends SLDTestSupport {
    @Test
    public void testType() throws Exception {
        assertEquals(TextSymbolizerImpl.class, new SLDTextSymbolizerBinding(null).getType());
    }

    @Test
    public void test() throws Exception {
        SLDMockData.textSymbolizer(document, document);

        TextSymbolizerImpl ts = (TextSymbolizerImpl) parse();
        assertNotNull(ts);
        assertNotNull(ts.getFill());
        assertEquals(1, ts.fonts().size());
        assertNotNull(ts.getGeometryPropertyName());
        assertNotNull(ts.getLabelPlacement());
        assertNotNull(ts.getHalo());
        assertNotNull(ts.getLabel());
    }

    @Test
    public void testWithVendorOptions() throws Exception {
        SLDMockData.textSymbolizerWithVendorOptions(document, document);

        TextSymbolizerImpl ts = (TextSymbolizerImpl) parse();
        assertNotNull(ts);

        // vendorOption(document, textSymbolizer, "followLine", "true");
        // vendorOption(document, textSymbolizer, "spaceAround", "10");
        assertEquals("true", ts.getOptions().get("followLine"));
        assertEquals("10", ts.getOptions().get("spaceAround"));
    }
}
