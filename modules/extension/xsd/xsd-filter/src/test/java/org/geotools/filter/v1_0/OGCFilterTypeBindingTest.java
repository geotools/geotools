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
package org.geotools.filter.v1_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.xsd.Binding;
import org.junit.Test;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.spatial.Intersects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OGCFilterTypeBindingTest extends FilterTestSupport {

    @Test
    public void testType() {
        assertEquals(Filter.class, binding(OGC.FilterType).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.FilterType).getExecutionMode());
    }

    @Test
    public void testParseSpatial() throws Exception {
        Element filterElement = FilterMockData.element(document, document, OGC.Filter);
        FilterMockData.intersects(document, filterElement);

        Filter filter = (Filter) parse();
        assertTrue(filter instanceof Intersects);
    }

    @Test
    public void testEncodeSpatial() throws Exception {
        Document doc = encode(FilterMockData.intersects(), OGC.Filter);
        assertEquals("ogc:Filter", doc.getDocumentElement().getNodeName());

        assertEquals(1, doc.getElementsByTagNameNS(OGC.NAMESPACE, "Intersects").getLength());
    }

    @Test
    public void testParseComparison() throws Exception {
        Element filterElement = FilterMockData.element(document, document, OGC.Filter);
        FilterMockData.propertyIsEqualTo(document, filterElement);

        Filter filter = (Filter) parse();
        assertTrue(filter instanceof PropertyIsEqualTo);
    }

    @Test
    public void testEncodeComparison() throws Exception {
        Document doc = encode(FilterMockData.propertyIsEqualTo(), OGC.Filter);

        assertEquals("ogc:Filter", doc.getDocumentElement().getNodeName());
        assertEquals(1, doc.getElementsByTagNameNS(OGC.NAMESPACE, "PropertyIsEqualTo").getLength());
    }

    @Test
    public void testParseLogical() throws Exception {
        Element filterElement = FilterMockData.element(document, document, OGC.Filter);
        FilterMockData.and(document, filterElement);

        Filter filter = (Filter) parse();
        assertTrue(filter instanceof And);
    }

    @Test
    public void testEncodeLogical() throws Exception {
        Document doc = encode(FilterMockData.and(), OGC.Filter);

        assertEquals("ogc:Filter", doc.getDocumentElement().getNodeName());
        assertEquals(1, doc.getElementsByTagNameNS(OGC.NAMESPACE, "And").getLength());

        doc = encode(FilterMockData.not(), OGC.Filter);

        assertEquals("ogc:Filter", doc.getDocumentElement().getNodeName());
        assertEquals(1, doc.getElementsByTagNameNS(OGC.NAMESPACE, "Not").getLength());
    }
}
