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
package org.geotools.ysld.transform.ysld;

import org.geotools.ysld.YamlObject;
import org.geotools.ysld.parse.YamlParseContext;
import org.geotools.ysld.parse.YamlParseHandler;
import org.xml.sax.ContentHandler;

public class YsldParseHandler extends YamlParseHandler {

    protected ContentHandler xml;

    public YsldParseHandler(ContentHandler xml) {
        this.xml = xml;
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {

    }
}
