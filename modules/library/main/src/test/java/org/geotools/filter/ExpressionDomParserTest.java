/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    Created on 17 May 2021
 */
package org.geotools.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.easymock.EasyMock;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/** @author fgdrf (Frank Gasdorf) */
public class ExpressionDomParserTest {

    @Test(expected = NullPointerException.class)
    public void nullConstructorInjection() throws Exception {
        new ExpressionDOMParser(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullSetterInjection() {
        ExpressionDOMParser parser = new ExpressionDOMParser();
        assertNotNull(parser);
        parser.setFilterFactory(null);
    }

    @Test
    public void filterFactoryWithSetterInjectionInvoked() throws Exception {
        ExpressionDOMParser parser = new ExpressionDOMParser();
        assertNotNull(parser);
        FilterFactory2 filterFactoryMock = EasyMock.createNiceMock(FilterFactory2.class);
        parser.setFilterFactory(filterFactoryMock);
        LiteralExpressionImpl expectedLiteralExpr = new LiteralExpressionImpl(3);
        EasyMock.expect(filterFactoryMock.literal("3")).andReturn(expectedLiteralExpr);
        EasyMock.replay(filterFactoryMock);
        assertEquals(expectedLiteralExpr, parser.expression(getDocumentNode()));
        EasyMock.verify(filterFactoryMock);
    }

    @Test
    public void filterFactoryWithConstructorInjectionInvoked() throws Exception {
        FilterFactory2 filterFactoryMock = EasyMock.createNiceMock(FilterFactory2.class);
        LiteralExpressionImpl expectedLiteralExpr = new LiteralExpressionImpl(3);
        ExpressionDOMParser parser = new ExpressionDOMParser(filterFactoryMock);
        assertNotNull(parser);
        EasyMock.expect(filterFactoryMock.literal("3")).andReturn(expectedLiteralExpr);
        EasyMock.replay(filterFactoryMock);
        assertEquals(expectedLiteralExpr, parser.expression(getDocumentNode()));
        EasyMock.verify(filterFactoryMock);
    }

    @Test
    public void defaultConstructorExpessionWorksAsExpected() throws Exception {
        ExpressionDOMParser parser = new ExpressionDOMParser();
        assertNotNull(parser);
        Node node = getDocumentNode();
        Expression expression = parser.expression(node);
        assertTrue(expression instanceof Literal);
    }

    Node getDocumentNode() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append("<?xml version=\"1.0\"?> <Literal>3</Literal>");
        ByteArrayInputStream input =
                new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
        return builder.parse(input).getDocumentElement();
    }
}
