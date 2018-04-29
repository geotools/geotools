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
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.geotools.ysld.Ysld;

/** Handles xml parse events for {@link org.geotools.styling.Symbolizer} elements. */
public class SymbolizerHandler extends SldTransformHandler {

    Map<String, String> options = new LinkedHashMap<String, String>();

    @Override
    public void element(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("Geometry".equals(name)) {
            context.scalar("geometry").push(new ExpressionHandler());
        } else if ("VendorOption".equals(name)) {
            String option = xml.getAttributeValue(null, "name");
            options.put(option, xml.getElementText());
        }
    }

    protected SldTransformContext dumpOptions(SldTransformContext context) throws IOException {
        if (!options.isEmpty()) {
            for (Map.Entry<String, String> opt : options.entrySet()) {
                String option = Ysld.OPTION_PREFIX + opt.getKey();
                context.scalar(option).scalar(opt.getValue());
            }
        }
        return context;
    }
}
