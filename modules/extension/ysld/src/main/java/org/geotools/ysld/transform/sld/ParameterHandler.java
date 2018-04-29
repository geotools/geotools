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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Handles xml parse events for CssParameter and SvgParameter. These get converted to YSLD elements
 * like stroke-width or stroke-color.
 */
public class ParameterHandler extends SldTransformHandler {

    Map<String, String> rename;

    List<String> strip;

    public ParameterHandler rename(String from, String to) {
        if (rename == null) {
            rename = new LinkedHashMap<String, String>();
        }
        rename.put(from, to);
        return this;
    }

    public ParameterHandler strip(String prefix) {
        if (strip == null) {
            strip = new ArrayList();
        }
        strip.add(prefix);
        return this;
    }

    @Override
    public void element(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("CssParameter".equals(name) || "SvgParameter".equals(name)) {
            String parameter = xml.getAttributeValue(null, "name");
            if (rename != null) {
                for (Map.Entry<String, String> e : rename.entrySet()) {
                    if (e.getKey().equals(parameter)) {
                        parameter = e.getValue();
                        break;
                    }
                }
            }
            if (strip != null) {
                for (String prefix : strip) {
                    if (parameter.startsWith(prefix + "-")) {
                        parameter = parameter.substring(prefix.length() + 1);
                        break;
                    }
                }
            }
            context.scalar(parameter).push(new ExpressionHandler());
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("CssParameter".equals(name) || "SvgParameter".equals(name)) {
            context.pop();
        }
    }
}
