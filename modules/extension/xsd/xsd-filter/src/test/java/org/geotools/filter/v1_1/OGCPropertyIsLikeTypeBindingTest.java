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
package org.geotools.filter.v1_1;

import org.geotools.filter.v1_0.OGC;
import org.opengis.filter.PropertyIsLike;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OGCPropertyIsLikeTypeBindingTest extends FilterTestSupport {

    public void testParse() throws Exception {
        FilterMockData.propertyIsLike(document, document);

        PropertyIsLike isLike = (PropertyIsLike) parse();

        assertNotNull(isLike.getExpression());
        assertNotNull(isLike.getLiteral());

        assertEquals("x", isLike.getWildCard());
        assertEquals("y", isLike.getSingleChar());
        assertEquals("z", isLike.getEscape());
        assertEquals(false, isLike.isMatchingCase());
    }

    public void testEncode() throws Exception {
        Document doc = encode(FilterMockData.propertyIsLike(), OGC.PropertyIsLike);

        Element pn = getElementByQName(doc, OGC.PropertyName);
        assertNotNull(pn);
        assertEquals("foo", pn.getFirstChild().getNodeValue());

        Element l = getElementByQName(doc, OGC.Literal);
        assertEquals("foo", l.getFirstChild().getNodeValue());

        assertEquals("x", doc.getDocumentElement().getAttribute("wildCard"));
        assertEquals("y", doc.getDocumentElement().getAttribute("singleChar"));
        assertEquals("z", doc.getDocumentElement().getAttribute("escapeChar"));
        assertEquals("false", doc.getDocumentElement().getAttribute("matchCase"));
    }

    public void testEncodeAsFilter() throws Exception {
        Document doc = encode(FilterMockData.propertyIsLike(), OGC.Filter);
        // print(doc);

        assertEquals(
                1,
                doc.getDocumentElement()
                        .getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                doc.getDocumentElement()
                        .getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart())
                        .getLength());

        Element e = getElementByQName(doc, OGC.PropertyIsLike);
        assertEquals("x", e.getAttribute("wildCard"));
        assertEquals("y", e.getAttribute("singleChar"));
        assertEquals("z", e.getAttribute("escapeChar"));
        assertEquals("false", e.getAttribute("matchCase"));
    }
}
