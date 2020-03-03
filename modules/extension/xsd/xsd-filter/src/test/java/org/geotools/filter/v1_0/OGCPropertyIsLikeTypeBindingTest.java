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

import java.io.ByteArrayInputStream;
import org.geotools.xsd.Binding;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Parser;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsLike;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class OGCPropertyIsLikeTypeBindingTest extends FilterTestSupport {
    public void testType() {
        assertEquals(PropertyIsLike.class, binding(OGC.PropertyIsLikeType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.PropertyIsLikeType).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.propertyIsLike(document, document);

        PropertyIsLike isLike = (PropertyIsLike) parse();

        assertNotNull(isLike.getExpression());
        assertNotNull(isLike.getLiteral());

        assertEquals("x", isLike.getWildCard());
        assertEquals("y", isLike.getSingleChar());
        assertEquals("z", isLike.getEscape());
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
        assertEquals("z", doc.getDocumentElement().getAttribute("escape"));
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
        assertEquals("z", e.getAttribute("escape"));
    }

    /*
     * test for GEOT-5920 can't have function on LHS Like filters
     *
     */

    public void testEncodeWithFunctionAsFilter() throws Exception {
        Document doc = encode(FilterMockData.propertyIsLike2(), OGC.Filter);
        // print(doc);

        NodeList property =
                doc.getDocumentElement()
                        .getElementsByTagNameNS(OGC.NAMESPACE, OGC.Function.getLocalPart());
        assertEquals(1, property.getLength());

        assertNotNull(property.item(0).getChildNodes());
        assertNotSame("", property.item(0).getNodeValue());
        assertEquals(
                1,
                doc.getDocumentElement()
                        .getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart())
                        .getLength());

        Element e = getElementByQName(doc, OGC.PropertyIsLike);
        assertEquals("x", e.getAttribute("wildCard"));
        assertEquals("y", e.getAttribute("singleChar"));
        assertEquals("z", e.getAttribute("escape"));

        Element p = getElementByQName(e, OGC.Function);
        assertEquals("strToLowerCase", p.getAttribute("name"));
        Element pn = getElementByQName(p, OGC.PropertyName);

        assertEquals("foo", pn.getTextContent());
    }

    /*
     * Tests for GEOS-8738 backward Like test
     */
    public void testBackwardLikeFilter() throws Exception {
        String f =
                "<ogc:Filter  xmlns:ogc=\"http://www.opengis.net/ogc\"><ogc:PropertyIsLike wildCard=\"*\" singleChar=\"#\" escapeChar=\"!\">\n"
                        + "<ogc:Literal>M*</ogc:Literal>\n"
                        + "<ogc:PropertyName>gml:name</ogc:PropertyName>\n"
                        + "</ogc:PropertyIsLike></ogc:Filter>";
        Configuration configuration = new org.geotools.filter.v1_0.OGCConfiguration();
        Parser parser = new Parser(configuration);
        Filter filter = (Filter) parser.parse(new ByteArrayInputStream(f.getBytes()));
        Document doc = encode(filter, OGC.Filter);
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
        assertEquals("*", e.getAttribute("wildCard"));
        assertEquals("#", e.getAttribute("singleChar"));
        assertEquals("!", e.getAttribute("escape"));
    }

    public void testBackwardLikeFilterWithFunction() throws Exception {
        String f =
                "<ogc:Filter  xmlns:ogc=\"http://www.opengis.net/ogc\"><ogc:PropertyIsLike wildCard=\"*\" singleChar=\"#\" escapeChar=\"!\">\n"
                        + "<ogc:Literal>M*</ogc:Literal>\n"
                        + "<ogc:Function name=\"strToLowerCase\"><ogc:PropertyName>gml:name</ogc:PropertyName></ogc:Function>\n"
                        + "</ogc:PropertyIsLike></ogc:Filter>";
        Configuration configuration = new org.geotools.filter.v1_0.OGCConfiguration();
        Parser parser = new Parser(configuration);
        Filter filter = (Filter) parser.parse(new ByteArrayInputStream(f.getBytes()));
        Document doc = encode(filter, OGC.Filter);
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
        assertEquals("*", e.getAttribute("wildCard"));
        assertEquals("#", e.getAttribute("singleChar"));
        assertEquals("!", e.getAttribute("escape"));
    }
}
