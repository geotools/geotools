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
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

public class JsonArrayContainsFunction extends FunctionExpressionImpl implements JsonFunctionUtils {

    private static final JsonToken END_OF_STREAM = null;
    private final JsonFactory factory;

    public static FunctionName NAME =
            new FunctionNameImpl(
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
        final String pointerSpec = getExpression(1).evaluate(object, String.class);
        final String expected = getExpression(2).evaluate(object, String.class);
        boolean found = false;

        if (json != null) {
            final JsonPointer expectedPointer = JsonPointer.compile(pointerSpec);
            try (JsonParser parser = factory.createParser(json)) {
                while (parser.nextToken() != END_OF_STREAM && !found) {
                    final JsonPointer pointer = parser.getParsingContext().pathAsPointer();
                    if (pointer.equals(expectedPointer)
                            && parser.currentTokenId() == JsonTokenId.ID_START_ARRAY) {
                        StringWriter writer = new StringWriter();
                        try (final JsonGenerator generator = factory.createGenerator(writer)) {
                            serializeArray(parser, generator);
                        }
                        found = writer.toString().contains(expected);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return found;
    }
}
