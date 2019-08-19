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
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.impl.ParserHandler;
import org.opengis.filter.Filter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/** Handles xml parse events for filter expressions. */
public class FilterHandler extends SldTransformHandler {
    ParserHandler delegate;

    @Override
    public void element(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        try {
            if (delegate == null && "Filter".equals(xml.getLocalName())) {
                delegate = new ParserHandler(config(context));
                delegate.startDocument();
            }
            if (delegate != null) {
                delegate.startElement(
                        xml.getNamespaceURI(),
                        xml.getLocalName(),
                        qname(xml.getName()),
                        attributes(xml));
            }
        } catch (SAXException e) {
            throw new XMLStreamException(e);
        }
    }

    @Override
    public void characters(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        if (delegate != null) {
            try {
                delegate.characters(
                        xml.getTextCharacters(), xml.getTextStart(), xml.getTextLength());
            } catch (SAXException e) {
                throw new XMLStreamException(e);
            }
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        if (delegate != null) {
            try {
                delegate.endElement(
                        xml.getNamespaceURI(), xml.getLocalName(), qname(xml.getName()));
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
                ? name.getPrefix() + ":" + name.getLocalPart()
                : name.getLocalPart();
    }

    Attributes attributes(XMLStreamReader xml) {
        AttributesImpl atts = new AttributesImpl();
        for (int i = 0; i < xml.getAttributeCount(); i++) {
            atts.addAttribute(
                    xml.getAttributeNamespace(i),
                    xml.getAttributeLocalName(i),
                    qname(xml.getAttributeName(i)),
                    xml.getAttributeType(i),
                    xml.getAttributeValue(i));
        }
        return atts;
    }

    Configuration config(SldTransformContext context) {
        if (context.version().equals(SldTransformContext.V_110)) {
            return new org.geotools.filter.v1_1.OGCConfiguration();
        } else {
            return new org.geotools.filter.v1_0.OGCConfiguration();
        }
    }
}
