/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.xml;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.text.WKTParser;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.PrecisionType;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** @author <a href="mailto:joel@lggi.com">Joel Skelton</a> */
public class GeometryTestParser {
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(GeometryTestParser.class);

    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private WKTParser wktFactory;

    /** Constructor */
    public GeometryTestParser() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("", e);
        }

        GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
        GeometryFactory geomFact = builder.getGeometryFactory();
        PrimitiveFactory primFact = builder.getPrimitiveFactory();
        wktFactory =
                new WKTParser(
                        geomFact,
                        primFact,
                        builder.getPositionFactory(),
                        builder.getAggregateFactory());
    }

    /** @return GeometryTestContainer */
    public GeometryTestContainer parseTestDefinition(InputSource inputSource) {
        Document doc = null;
        try {
            doc = documentBuilder.parse(inputSource);
        } catch (SAXException e) {
            LOGGER.log(Level.FINE, e.getMessage(), e);
            throw new RuntimeException("", e);

        } catch (IOException e) {
            LOGGER.log(Level.FINE, e.getMessage(), e);
            throw new RuntimeException("", e);
        }

        Element element = doc.getDocumentElement();
        GeometryTestContainer test = null;
        try {
            test = processRootNode(element);
        } catch (ParseException e) {
            LOGGER.log(Level.FINE, e.getMessage(), e);
            throw new RuntimeException("", e);
        }

        return test;
    }

    /**
     * Processes the root "run" node
     *
     * @return GeometryTestContainer
     */
    public GeometryTestContainer processRootNode(Node node) throws ParseException {
        if (!node.getNodeName().equalsIgnoreCase("run")) {
            throw new ParseException("Expected run tag, found " + node.getNodeName(), 0);
        }
        GeometryTestContainer test = new GeometryTestContainer();
        Node child = node.getFirstChild();
        // default precision type is float
        PrecisionType precisionType = PrecisionType.FLOAT;
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String name = child.getNodeName();
                if (name.equalsIgnoreCase("case")) {
                    GeometryTestCase testCase = readTestCase(child);
                    test.addTestCase(testCase);
                } else if (name.equalsIgnoreCase("precisionmodel")) {
                    precisionType = getPrecisionModel(child);
                } else {
                    throw new ParseException("Unexpected: " + name, 0);
                }
            }
            child = child.getNextSibling();
        }
        test.setPrecisionModel(new PrecisionModel(precisionType));
        return test;
    }

    /**
     * parse a single test case
     *
     * <p>From looking at various JTS test cases and seeing how their testbuilder program works, I
     * think its safe to assume that there will always be just one or two objects, named a and b.
     *
     * @return GeometryTestCase
     */
    private GeometryTestCase readTestCase(Node testCaseNode) throws ParseException {
        Node child = testCaseNode.getFirstChild();
        GeometryTestCase testCase = new GeometryTestCase();

        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String name = child.getNodeName();
                if (name.equalsIgnoreCase("test")) {
                    GeometryTestOperation op = loadTestOperation(child);
                    testCase.addTestOperation(op);
                } else if (name.equalsIgnoreCase("a")) {
                    testCase.setGeometryA(loadTestGeometry(child));
                } else if (name.equalsIgnoreCase("b")) {
                    testCase.setGeometryB(loadTestGeometry(child));
                } else if (name.equalsIgnoreCase("desc")) {
                    testCase.setDescription(getNodeText(child));
                } else {
                    throw new ParseException("Unexpected: " + name, 0);
                }
            }
            child = child.getNextSibling();
        }

        return testCase;
    }

    /**
     * Loads a test operation. Assumes that there _must_ be a name attribute, and looks for arg1,
     * arg2, and arg3. The value of the text subnode is the value of the expected result
     *
     * @param testNode a test node from the xml file
     * @return GeometryTestOperation
     */
    private GeometryTestOperation loadTestOperation(Node testNode) {

        Node node = getNamedChild(testNode, "op");
        if (node == null) {
            throw new RuntimeException("<test> element must have <op> subelement");
        }

        String operation = getNodeAttribute(node, "name");
        String arg1 = getNodeAttribute(node, "arg1");
        String arg2 = getNodeAttribute(node, "arg2");
        String arg3 = getNodeAttribute(node, "arg3");
        Object expectedResult;

        String expectedString = getNodeText(node);
        if (expectedString.trim().equalsIgnoreCase("true")) {
            expectedResult = Boolean.TRUE;
        } else if (expectedString.trim().equalsIgnoreCase("false")) {
            expectedResult = Boolean.FALSE;
        } else {
            try {
                expectedResult = wktFactory.parse(expectedString);
            } catch (ParseException e) {
                LOGGER.log(Level.FINE, "Couldn't parse [" + expectedString + "]", e);
                throw new RuntimeException("Couldn't parse [" + expectedString + "]", e);
            }
        }

        return new GeometryTestOperation(operation, arg1, arg2, arg3, expectedResult);
    }

    private Geometry loadTestGeometry(Node node) {
        String wktString = getNodeText(node);
        Geometry geom = null;
        try {
            geom = wktFactory.parse(wktString);
        } catch (ParseException e) {
            LOGGER.log(Level.FINE, "Can't parse [" + wktString + "]", e);
            throw new RuntimeException("Can't parse [" + wktString + "]", e);
        }
        return geom;
    }

    private PrecisionType getPrecisionModel(Node child) {
        String val = getNodeAttribute(child, "type");
        if (val == "") {
            // if scale is 1.0 then set the precision to type FIXED
            String scale = getNodeAttribute(child, "scale");
            if (scale.equalsIgnoreCase("1.0")) return PrecisionType.FIXED;
        } else if (val.equalsIgnoreCase("DOUBLE")) {
            return PrecisionType.DOUBLE;
        }
        // default
        return PrecisionType.FLOAT;
    }

    private String getNodeAttribute(Node node, String attrName) {
        String emptyString = "";
        NamedNodeMap attrs = node.getAttributes();
        if (attrs == null) {
            return emptyString;
        }

        Node attrNode = attrs.getNamedItem(attrName);
        if (attrNode == null) {
            return emptyString;
        }
        return attrNode.getNodeValue();
    }

    private String getNodeText(Node node) {
        Node child = node.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.TEXT_NODE) {
                return child.getNodeValue();
            }
        }
        return "";
    }

    private Node getNamedChild(Node node, String name) {
        Node child = node.getFirstChild();
        while (child != null) {
            if (name.equalsIgnoreCase(child.getNodeName())) {
                return child;
            }
            child = child.getNextSibling();
        }
        return null;
    }
}
