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

import org.geotools.styling.LineSymbolizer;


public class SLDLineSymbolizerBindingTest extends SLDTestSupport {
    public void testType() throws Exception {
        assertEquals(LineSymbolizer.class, new SLDLineSymbolizerBinding(null).getType());
    }

    public void test() throws Exception {
        SLDMockData.lineSymbolizer(document, document);

        LineSymbolizer lineSymbolizer = (LineSymbolizer) parse();
        assertNotNull(lineSymbolizer);

        assertNotNull(lineSymbolizer.getStroke());
    }
}
