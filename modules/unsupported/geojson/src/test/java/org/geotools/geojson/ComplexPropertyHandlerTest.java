/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.geotools.geojson.feature.ComplexPropertyHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 *
 * @source $URL$
 */
public class ComplexPropertyHandlerTest extends GeoJSONTestSupport {

    public void testSimpleStructures() throws Exception {
        // Yeah, it works for simple values, too
        assertEquals(1L, parseJSON("1"));
        assertEquals(emptyMap(), parseJSON("{}"));
        assertEquals(emptyList(), parseJSON("[]"));
        assertEquals(singletonList(emptyList()), parseJSON("[[]]"));
        assertEquals(singletonMap("a", emptyMap()), parseJSON("{\"a\":{}}"));
        assertEquals(singletonList(singletonMap("a", singletonList(emptyMap()))), parseJSON("[{\"a\":[{}]}]"));
    }

    public void testFeatureWrite() throws Exception {
        Map result = (Map) parseJSON("{\"a\":1, \"b\":\"s\", \"c\":[1]}");
        assertEquals(1L, result.get("a"));
        assertEquals("s", result.get("b"));
        assertEquals(Arrays.asList(1L), result.get("c"));
    }
    
    private Object parseJSON(String json) throws IOException, ParseException {
        ComplexPropertyHandler handler = new ComplexPropertyHandler();
        new JSONParser().parse(new StringReader(json), handler, true);
        return handler.getValue();
    }
}
