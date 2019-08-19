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

/** Applies a JSON pointer on a given JSON string, extracting a value out of it */
public class JsonPointerFunction extends FunctionExpressionImpl {

    private static final JsonToken END_OF_STREAM = null;
    private final JsonFactory factory;

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "jsonPointer",
                    parameter("result", Object.class),
                    parameter("json", String.class),
                    parameter("pointer", String.class));

    public JsonPointerFunction() {
        super(NAME);
        this.factory = new JsonFactory();
    }

    @Override
    public Object evaluate(Object object) {
        final String json = getExpression(0).evaluate(object, String.class);
        final String pointerSpec = getExpression(1).evaluate(object, String.class);

        JsonPointer expectedPointer = JsonPointer.compile(pointerSpec);
        try (JsonParser parser = factory.createParser(json)) {
            while (parser.nextToken() != END_OF_STREAM) {
                final JsonPointer pointer = parser.getParsingContext().pathAsPointer();
                if (pointer.equals(expectedPointer)
                        && parser.currentTokenId() != JsonTokenId.ID_FIELD_NAME) {
                    switch (parser.currentTokenId()) {
                        case JsonTokenId.ID_STRING:
                            return parser.getText();
                        case JsonTokenId.ID_NUMBER_FLOAT:
                            return parser.getFloatValue();
                        case JsonTokenId.ID_NUMBER_INT:
                            return parser.getIntValue();
                        case JsonTokenId.ID_TRUE:
                        case JsonTokenId.ID_FALSE:
                            return parser.getBooleanValue();
                        default:
                            StringWriter writer = new StringWriter();
                            try (final JsonGenerator generator = factory.createGenerator(writer)) {
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

    private void serializeContents(JsonParser parser, JsonGenerator generator) throws IOException {
        switch (parser.currentTokenId()) {
            case JsonTokenId.ID_START_ARRAY:
                serializeArray(parser, generator);
                break;
            case JsonTokenId.ID_START_OBJECT:
                serializeObject(parser, generator);
                break;
        }
    }

    private void serializeArray(JsonParser parser, JsonGenerator generator) throws IOException {
        generator.writeStartArray();
        for (JsonToken token = parser.nextToken();
                token != END_OF_STREAM && token != JsonToken.END_ARRAY;
                token = parser.nextToken()) {
            switch (parser.currentTokenId()) {
                case JsonTokenId.ID_STRING:
                    generator.writeString(parser.getText());
                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    generator.writeNumber(parser.getFloatValue());
                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    generator.writeNumber(parser.getIntValue());
                    break;
                case JsonTokenId.ID_TRUE:
                case JsonTokenId.ID_FALSE:
                    generator.writeBoolean(parser.getBooleanValue());
                    break;
                default:
                    serializeContents(parser, generator);
            }
        }
        generator.writeEndArray();
    }

    private void serializeObject(JsonParser parser, JsonGenerator generator) throws IOException {
        generator.writeStartObject();
        for (JsonToken token = parser.nextToken();
                token != END_OF_STREAM && token != JsonToken.END_OBJECT;
                token = parser.nextToken()) {
            switch (parser.currentTokenId()) {
                case JsonTokenId.ID_FIELD_NAME:
                    generator.writeFieldName(parser.getCurrentName());
                    break;
                case JsonTokenId.ID_STRING:
                    generator.writeString(parser.getText());
                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    generator.writeNumber(parser.getFloatValue());
                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    generator.writeNumber(parser.getIntValue());
                    break;
                case JsonTokenId.ID_TRUE:
                case JsonTokenId.ID_FALSE:
                    generator.writeBoolean(parser.getBooleanValue());
                    break;
                default:
                    serializeContents(parser, generator);
            }
        }
        generator.writeEndObject();
    }
}
