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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlUtil;
import org.yaml.snakeyaml.reader.UnicodeReader;

/**
 * Base Yaml parsing class, responsible for parsing the yaml input into a {@link YamlObject}} and
 * then delegating to a {@link YamlParseHandler}. See {@link #parse(YamlParseHandler, Map))}.
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
        return parse(root, Collections.<String, Object>emptyMap());
    }

    /**
     * Parse the yaml provided to this instance using the provided {@link YamlParseHandler}.
     *
     * @param root The {@link YamlParseHandler} that handles the root of the parsed {@link
     *     YamlObject}.
     * @return The root {@link YamlParseHandler}, once it has finished handling the parsed {@link
     *     YamlObject}..
     */
    @SuppressWarnings("PMD.EmptyWhileStmt")
    public <T extends YamlParseHandler> T parse(T root, Map<String, Object> hints)
            throws IOException {
        Object parsed = YamlUtil.getSafeYaml().load(yaml);

        YamlParseContext context = new YamlParseContext();
        context.mergeDocHints(hints);
        context.push(YamlObject.create(parsed), root);

        while (context.next()) ;

        return root;
    }
}
