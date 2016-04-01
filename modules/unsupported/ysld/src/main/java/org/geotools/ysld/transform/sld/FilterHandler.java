package org.geotools.ysld.transform.sld;

import org.geotools.filter.text.ecql.ECQL;
import org.geotools.xml.Configuration;
import org.geotools.xml.impl.ParserHandler;
import org.opengis.filter.Filter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

public class FilterHandler extends SldTransformHandler {
    ParserHandler delegate;
    @Override
    public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        try {
            if (delegate == null && "Filter".equals(xml.getLocalName())) {
                delegate = new ParserHandler(config(context));
                delegate.startDocument();
            }
            if (delegate != null) {
                delegate.startElement(xml.getNamespaceURI(), xml.getLocalName(), qname(xml.getName()), attributes(xml));
            }
        }
        catch(SAXException e) {
            throw new XMLStreamException(e);
        }
    }

    @Override
    public void characters(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        if (delegate != null) {
            try {
                delegate.characters(xml.getTextCharacters(), xml.getTextStart(), xml.getTextLength());
            } catch (SAXException e) {
                throw new XMLStreamException(e);
            }
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        if (delegate != null) {
            try {
                delegate.endElement(xml.getNamespaceURI(), xml.getLocalName(), qname(xml.getName()));
            } catch (SAXException e) {
                throw new XMLStreamException(e);
            }
        }

        if ("Filter".equals(xml.getLocalName())) {
            Filter filter = (Filter) delegate.getValue();
            context.scalar(ECQL.toCQL(filter)).pop();
            delegate = null;
        }
    }

    String qname(QName name) {
        return name.getPrefix() != null && !"".equals(name.getPrefix())
            ? name.getPrefix() + ":" + name.getLocalPart() : name.getLocalPart();
    }

    Attributes attributes(XMLStreamReader xml) {
        AttributesImpl atts = new AttributesImpl();
        for (int i = 0; i < xml.getAttributeCount(); i++) {
            atts.addAttribute(xml.getAttributeNamespace(i), xml.getAttributeLocalName(i),
                qname(xml.getAttributeName(i)), xml.getAttributeType(i), xml.getAttributeValue(i));
        }
        return atts;
    }

    Configuration config(SldTransformContext context) {
        if (context.version().equals(SldTransformContext.V_110)) {
            return new org.geotools.filter.v1_1.OGCConfiguration();
        }
        else {
            return new org.geotools.filter.v1_0.OGCConfiguration();
        }
    }
}
