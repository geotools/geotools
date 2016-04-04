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
package org.geotools.ysld.parse;

import org.geotools.ysld.YamlObject;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.events.*;
import org.yaml.snakeyaml.reader.UnicodeReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;

/**
 * Base Yaml parsing class.
 */
public class YamlParser {

    Reader yaml;

    public YamlParser(InputStream yaml) {
        this(new UnicodeReader(yaml));
    }

    public YamlParser(Reader yaml) {
        this.yaml = yaml;
    }

    public <T extends YamlParseHandler> T parse(T root) throws IOException {
        return parse(root, Collections.<String,Object>emptyMap());
    }

    public <T extends YamlParseHandler> T parse(T root, Map<String, Object> hints) throws IOException {
        Object parsed = new Yaml().load(yaml);

        YamlParseContext context = new YamlParseContext();
        context.mergeDocHints(hints);
        context.push(YamlObject.create(parsed), root);

        while(context.next());

        return root;
    }
}
