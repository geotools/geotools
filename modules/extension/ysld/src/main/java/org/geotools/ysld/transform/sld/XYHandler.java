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

/** Handles xml parse events for co-ordinate tuples. */
public class XYHandler extends SldTransformHandler {

    String x, y;

    @Override
    public void element(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if (name.endsWith("X")) {
            context.push(
                    new ExpressionHandler() {
                        @Override
                        protected SldTransformContext onValue(
                                String value, SldTransformContext context) throws IOException {
                            x = value;
                            return context;
                        }
                    });
        }
        if (name.endsWith("Y")) {
            context.push(
                    new ExpressionHandler() {
                        @Override
                        protected SldTransformContext onValue(
                                String value, SldTransformContext context) throws IOException {
                            y = value;
                            return context;
                        }
                    });
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if (!name.endsWith("X") && !name.endsWith("Y")) {
            context.tuple(x, y).pop();
        }
    }
}
