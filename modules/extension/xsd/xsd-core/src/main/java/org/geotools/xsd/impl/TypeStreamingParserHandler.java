/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xsd.impl;

import org.geotools.xsd.Configuration;

public class TypeStreamingParserHandler extends StreamingParserHandler {
    Class<?> type;

    public TypeStreamingParserHandler(Configuration config, Class<?> type) {
        super(config);
        this.type = type;
    }

    @Override
    protected boolean stream(ElementHandler handler) {
        return handler.getParseNode().getValue() != null
                && type.isAssignableFrom(handler.getParseNode().getValue().getClass());
    }
}
