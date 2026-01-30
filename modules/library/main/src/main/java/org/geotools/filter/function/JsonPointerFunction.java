/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
import static org.geotools.filter.function.JsonFunctionUtils.serializeContents;

import java.io.IOException;
import java.io.StringWriter;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonPointer;
import tools.jackson.core.JsonTokenId;
import tools.jackson.core.ObjectReadContext;
import tools.jackson.core.ObjectWriteContext;
import tools.jackson.core.json.JsonFactory;

/** Applies a JSON pointer on a given JSON string, extracting a value out of it */
public class JsonPointerFunction extends FunctionExpressionImpl {
    private final JsonFactory factory;

    public static FunctionName NAME = new FunctionNameImpl(
            "jsonPointer",
            parameter("result", Object.class),
            parameter("json", String.class),
            parameter("pointer", String.class));

    public JsonPointerFunction() {
        super(NAME);
        this.factory = JsonFactory.builder().build();
    }

    @Override
    public Object evaluate(Object object) {
        final String json = getExpression(0).evaluate(object, String.class);
        final String pointerSpec = getExpression(1).evaluate(object, String.class);

        JsonPointer expectedPointer = JsonPointer.compile(pointerSpec);
        if (json == null) return null;
        try (JsonParser parser = factory.createParser(ObjectReadContext.empty(), json)) {
            while (parser.nextToken() != JsonFunctionUtils.END_OF_STREAM) {
                final JsonPointer pointer = parser.streamReadContext().pathAsPointer();
                if (pointer.equals(expectedPointer) && parser.currentTokenId() != JsonTokenId.ID_PROPERTY_NAME) {
                    switch (parser.currentTokenId()) {
                        case JsonTokenId.ID_STRING:
                            return parser.getString();
                        case JsonTokenId.ID_NUMBER_FLOAT:
                            return parser.getFloatValue();
                        case JsonTokenId.ID_NUMBER_INT:
                            return parser.getIntValue();
                        case JsonTokenId.ID_TRUE:
                        case JsonTokenId.ID_FALSE:
                            return parser.getBooleanValue();
                        default:
                            StringWriter writer = new StringWriter();
                            try (final JsonGenerator generator =
                                    factory.createGenerator(ObjectWriteContext.empty(), writer)) {
                                serializeContents(parser, generator);
                            }
                            return writer.toString();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // not found
        return null;
    }
}
