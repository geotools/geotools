/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;
import static org.geotools.filter.function.JsonFunctionUtils.serializeArray;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.logging.Logger;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;

public class JsonArrayContainsFunction extends FunctionExpressionImpl {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(JsonArrayContainsFunction.class);

    private static final HashMap<String, JsonPointer> jsonPointerCache = new HashMap<>();
    private static final JsonToken END_OF_STREAM = null;
    private final JsonFactory factory;

    public static FunctionName NAME = new FunctionNameImpl(
            "jsonArrayContains",
            Boolean.class,
            parameter("column", String.class),
            parameter("pointer", String.class),
            parameter("expected", String.class));

    public JsonArrayContainsFunction() {
        super(NAME);
        this.factory = new JsonFactory();
    }

    @Override
    public Object evaluate(Object object) {
        final String json = getExpression(0).evaluate(object, String.class);
        boolean found = false;

        if (json != null) {
            try (JsonParser parser = factory.createParser(json)) {
                found = checkExpected(parser, object);
            } catch (IOException e) {
                LOGGER.severe("Error when parsing the JSON: " + json + ". Exception: " + e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
        }
        return found;
    }

    private boolean checkExpected(JsonParser parser, Object object) throws IOException {
        final String pointerSpec = getExpression(1).evaluate(object, String.class);
        final String expected = getExpression(2).evaluate(object, String.class);

        boolean found = false;
        final JsonPointer expectedPointer =
                jsonPointerCache.getOrDefault(pointerSpec, JsonPointer.compile(pointerSpec));
        jsonPointerCache.put(pointerSpec, expectedPointer);
        /*
         * Iterate over the json until the expected value is found inside the json pointer array or the end of the json
         * is reached
         */
        while (parser.nextToken() != END_OF_STREAM && !found) {
            final JsonPointer pointer = parser.getParsingContext().pathAsPointer();
            if (pointer.equals(expectedPointer) && parser.currentTokenId() == JsonTokenId.ID_START_ARRAY) {
                StringWriter writer = new StringWriter();
                try (final JsonGenerator generator = factory.createGenerator(writer)) {
                    serializeArray(parser, generator);
                }
                found = writer.toString().contains(expected);
            }
        }
        return found;
    }
}
