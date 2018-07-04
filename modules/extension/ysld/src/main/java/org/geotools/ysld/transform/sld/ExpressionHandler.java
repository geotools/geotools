/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.transform.sld;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Handles xml parse events for {@link org.opengis.filter.expression.Expression} elements (literals,
 * rendering transforms, and functions).
 */
public class ExpressionHandler extends SldTransformHandler {

    StringBuilder scalar;

    ExpressionHandler() {
        this(new StringBuilder());
    }

    ExpressionHandler(StringBuilder scalar) {
        this.scalar = scalar;
    }

    @Override
    public void element(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("Literal".equals(name)) {
            context.push(new LiteralHandler(scalar));
        } else if ("PropertyName".equals(name)) {
            context.push(new PropertyNameHandler(scalar));
        } else if ("Function".equals(name)) {
            context.push(new FunctionHandler(scalar));
        }
    }

    @Override
    public void characters(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        scalar.append(xml.getText());
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("Literal".equals(name) || "PropertyName".equals(name) || "Function".equals(name)) {
            onValue(scalar.toString().trim(), context).pop();

        } else {
            onValue(scalar.toString(), context).pop();
        }
    }

    protected SldTransformContext onValue(String value, SldTransformContext context)
            throws IOException {
        return context.scalar(value);
    }

    static class LiteralHandler extends ExpressionHandler {

        LiteralHandler(StringBuilder scalar) {
            super(scalar);
        }

        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            if (!"Literal".equals(xml.getLocalName())) {
                super.element(xml, context);
            }
        }

        @Override
        public void characters(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            scalar.append(xml.getText());
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            if ("Literal".equals(xml.getLocalName())) {
                context.pop();
            }
        }
    }

    static class PropertyNameHandler extends ExpressionHandler {

        PropertyNameHandler(StringBuilder scalar) {
            super(scalar);
        }

        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            if (!"PropertyName".equals(xml.getLocalName())) {
                super.element(xml, context);
            }
        }

        @Override
        public void characters(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            scalar.append("${").append(xml.getText()).append("}");
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            if ("PropertyName".equals(xml.getLocalName())) {
                context.pop();
            }
        }
    }

    static class FunctionHandler extends ExpressionHandler {

        FunctionHandler(StringBuilder scalar) {
            super(scalar);
        }

        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("Function".equals(name)) {
                scalar.append(xml.getAttributeValue(xml.getNamespaceURI(), "name") + "(");
            } else {
                super.element(xml, context);
            }
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("Function".equals(name)) {
                if ('.' == scalar.charAt(scalar.length() - 1)) {
                    scalar.setLength(scalar.length() - 1);
                }
                scalar.append(")");
                context.pop();
            }
        }
    }
}
