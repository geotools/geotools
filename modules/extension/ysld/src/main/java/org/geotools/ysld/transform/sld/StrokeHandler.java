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

/** Handles xml parse events for {@link org.geotools.styling.Stroke} elements. */
public class StrokeHandler extends SldTransformHandler {

    @Override
    public void element(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("CssParameter".equals(name) || "SvgParameter".equals(name)) {
            context.push(new ParameterHandler().rename("stroke", "stroke-color"));
        } else if ("GraphicStroke".equals(name)) {
            context.scalar("stroke-graphic-stroke").push(new GraphicHandler());
        } else if ("GraphicFill".equals(name)) {
            context.scalar("stroke-graphic-fill").push(new GraphicHandler());
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("Stroke".equals(name)) {
            context.pop();
        }
    }
}
