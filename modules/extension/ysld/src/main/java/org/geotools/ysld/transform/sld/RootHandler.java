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
 * Handles xml parse events for top-level elements such as Name, and UserStyle. Delegates to {@link
 * FeatureStylesHandler} where applicable.
 */
public class RootHandler extends SldTransformHandler {
    @Override
    public void document(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        super.document(xml, context);
        context.document();
    }

    @Override
    public void element(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        super.element(xml, context);
        if ("UserStyle".equals(xml.getName().getLocalPart())) {
            context.mapping().push(new UserStyleHandler());
        }
    }

    @Override
    public void endDocument(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        super.endDocument(xml, context);
        context.endDocument();
    }

    static class UserStyleHandler extends SldTransformHandler {

        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("Name".equals(name)) {
                context.scalar("name");
                context.scalar(xml.getElementText());
            } else if ("Title".equals(name)) {
                context.scalar("title");
                context.scalar(xml.getElementText());
            } else if ("Abstract".equals(name)) {
                context.scalar("abstract");
                context.scalar(xml.getElementText());
            } else if ("FeatureTypeStyle".equals(name)) {
                context.scalar("feature-styles").sequence().push(new FeatureStylesHandler());
            }
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            if ("UserStyle".equalsIgnoreCase(xml.getLocalName())) {
                context.endSequence().endMapping().pop();
            }
        }
    }
}
