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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import java.io.IOException;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Applies a JSON pointer on a given JSON string, looking for the expected value to be present
 * inside the array
 */
public class JsonArrayContainsFunction extends FunctionExpressionImpl implements JsonFunctionUtils {

    private static final ThreadLocal<Map<String, JsonPointer>> jsonPointersCache =
            new ThreadLocal<>();

    static {
        jsonPointersCache.set(new HashMap<>());
    }

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(JsonArrayContainsFunction.class);

    private static final JsonToken END_OF_STREAM = null;
    private final JsonFactory factory;

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "jsonArrayContains",
                    Boolean.class,
                    parameter("json", String.class),
                    parameter("pointer", String.class),
                    parameter("expected", String.class));

    public JsonArrayContainsFunction() {
        super(NAME);
        this.factory = new JsonFactory();
    }

    @Override
    public Object evaluate(Object object) {
        final String json = getExpression(0).evaluate(object, String.class);
        final String pointerSpec = getExpression(1).evaluate(object, String.class);
        final String expected = getExpression(2).evaluate(object, String.class);
        boolean found = false;

        if (json != null) {
            JsonPointer expectedPointer = jsonPointersCache.get().get(pointerSpec);
            if (expectedPointer == null) {
                expectedPointer = JsonPointer.compile(pointerSpec);
                jsonPointersCache.get().put(pointerSpec, expectedPointer);
            }
            try (JsonParser parser = factory.createParser(json)) {
                // Iterate over the JSON util reach the END or found the first occurrence of the
                // expected element
                while (parser.nextToken() != END_OF_STREAM && !found) {
                    final JsonPointer pointer = parser.getParsingContext().pathAsPointer();
                    if (pointer.equals(expectedPointer)
                            && parser.currentTokenId() == JsonTokenId.ID_START_ARRAY) {
                        StringWriter writer = new StringWriter();
                        try (final JsonGenerator generator = factory.createGenerator(writer)) {
                            serializeArray(parser, generator);
                        }
                        try {
                            // parse the expected value as number, if get exception check as string
                            // with quotes
                            NumberFormat.getInstance().parse(expected);
                            // compared the array elements as string with the expected value wrapped
                            // in quotes
                            found = contains(expected, writer);
                        } catch (Exception e) {
                            // compared the array elements as string with the expected value wrapped
                            // in quotes
                            found = writer.toString().contains("\"" + expected + "\"");
                        }
                    }
                }
            } catch (IOException ioe) {
                LOGGER.severe(
                        "Error when parsing the JSON: " + json + ". Exception" + ioe.getMessage());
                throw new RuntimeException(ioe);
            }
        }
        return found;
    }

    private boolean contains(String expected, StringWriter writer) {
        return Arrays.asList(writer.toString().replace("[", "").replace("]", "").split(","))
                .contains(expected);
    }
}
