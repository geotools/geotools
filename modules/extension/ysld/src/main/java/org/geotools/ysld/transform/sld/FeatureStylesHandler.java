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

/** Handles xml parse events for {@link org.geotools.styling.FeatureTypeStyle} elements. */
public class FeatureStylesHandler extends SldTransformHandler {
    @Override
    public void element(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("FeatureTypeStyle".equals(name)) {
            context.mapping();
        } else if ("Name".equals(name)) {
            context.scalar("name");
            context.scalar(xml.getElementText());
        } else if ("Title".equals(name)) {
            context.scalar("title");
            context.scalar(xml.getElementText());
        } else if ("Abstract".equals(name)) {
            context.scalar("abstract");
            context.scalar(xml.getElementText());
        } else if ("Rule".equals(name)) {
            context.scalar("rules").sequence().push(new RulesHandler());
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        if ("FeatureTypeStyle".equals(xml.getLocalName())) {
            context.endSequence().endMapping();
        } else if ("UserStyle".equals(xml.getLocalName())) {
            context.pop();
        }
    }
}
