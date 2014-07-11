package org.geotools.ysld.transform.sld;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

public class ExpressionHandler extends SldTransformHandler {

    StringBuilder scalar;

    ExpressionHandler() {
        this(new StringBuilder());
    }

    ExpressionHandler(StringBuilder scalar) {
        this.scalar = scalar;
    }

    @Override
    public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("Literal".equals(name)) {
            context.push(new LiteralHandler(scalar));
        }
        else if ("PropertyName".equals(name)) {
            context.push(new PropertyNameHandler(scalar));
        }
        else if ("Function".equals(name)) {
            context.push(new FunctionHandler(scalar));
        }
    }

    @Override
    public void characters(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        scalar.append(xml.getText());
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("Literal".equals(name) ||
            "PropertyName".equals(name) ||
            "Function".equals(name)) {
            onValue(scalar.toString().trim(), context).pop();

        }
        else {
            onValue(scalar.toString(), context).pop();
        }
    }

    protected SldTransformContext onValue(String value, SldTransformContext context) throws IOException {
        return context.scalar(value);
    }

    static class LiteralHandler extends ExpressionHandler {

        LiteralHandler(StringBuilder scalar) {
            super(scalar);
        }

        @Override
        public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
            if (!"Literal".equals(xml.getLocalName())) {
                super.element(xml, context);
            }
        }

        @Override
        public void characters(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
            scalar.append(xml.getText());
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
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
        public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
            if (!"PropertyName".equals(xml.getLocalName())) {
                super.element(xml, context);
            }
        }

        @Override
        public void characters(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
            scalar.append("[").append(xml.getText()).append("]");
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
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
        public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("Function".equals(name)) {
                scalar.append(xml.getAttributeValue(xml.getNamespaceURI(), "name") + "(");
            }
            else {
                super.element(xml, context);
            }
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("Function".equals(name)) {
                if ('.' == scalar.charAt(scalar.length()-1)) {
                    scalar.setLength(scalar.length()-1);
                }
                scalar.append(")");
                context.pop();
            }
        }
    }
}
