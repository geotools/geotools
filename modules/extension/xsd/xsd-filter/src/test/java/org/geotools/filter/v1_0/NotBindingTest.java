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

import org.geotools.xsd.Binding;
import org.opengis.filter.Not;
import org.w3c.dom.Document;

public class NotBindingTest extends FilterTestSupport {
    public void testType() {
        assertEquals(Not.class, binding(OGC.Not).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.Not).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.not(document, document);

        Not not = (Not) parse();

        assertNotNull(not.getFilter());
    }

    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.not(), OGC.Not);
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyIsEqualTo.getLocalPart())
                        .getLength());
    }

    public void testNotNullEncode() throws Exception {
        Document dom = encode(FilterMockData.notIsNull(), OGC.Not);
        /*
        <?xml version="1.0" encoding="UTF-8"?>
        <ogc:Not xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc">
          <ogc:PropertyIsNull>
            <ogc:PropertyName>foo</ogc:PropertyName>
          </ogc:PropertyIsNull>
        </ogc:Not>
         */
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyIsNull.getLocalPart())
                        .getLength());
    }
}
