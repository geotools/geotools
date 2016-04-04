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

import org.geotools.ysld.parse.YamlParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class YsldTransformer extends YamlParser {

    ContentHandler xml;
    public YsldTransformer(InputStream yaml, ContentHandler xml) throws IOException {
        super(yaml);
        this.xml = xml;
    }

    public YsldTransformer(Reader yaml, ContentHandler xml) throws IOException {
        super(yaml);
        this.xml = xml;
    }

    public void transform() throws IOException, SAXException {
        //doParse();
    }

}
