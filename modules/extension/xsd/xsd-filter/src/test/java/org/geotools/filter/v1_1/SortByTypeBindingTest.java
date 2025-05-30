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
package org.geotools.filter.v1_1;

import static org.junit.Assert.assertEquals;

import org.geotools.api.filter.sort.SortBy;
import org.geotools.xsd.Binding;
import org.junit.Test;
import org.w3c.dom.Document;

public class SortByTypeBindingTest extends FilterTestSupport {
    @Test
    public void testType() {
        assertEquals(SortBy[].class, binding(OGC.SortByType).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.SortByType).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        FilterMockData.sortBy(document, document);

        SortBy[] sortBy = (SortBy[]) parse();

        assertEquals(2, sortBy.length);
    }

    @Test
    public void testEncode() throws Exception {
        Document doc = encode(FilterMockData.sortBy(), OGC.SortBy);
        assertEquals(
                2, doc.getElementsByTagNameNS(OGC.NAMESPACE, "SortProperty").getLength());
    }
}
