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

import java.io.IOException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.JsonTokenId;

public final class JsonFunctionUtils {

    static final JsonToken END_OF_STREAM = null;

    private JsonFunctionUtils() {}

    static void serializeContents(JsonParser parser, JsonGenerator generator) throws IOException {
        switch (parser.currentTokenId()) {
            case JsonTokenId.ID_START_ARRAY:
                serializeArray(parser, generator);
                break;
            case JsonTokenId.ID_START_OBJECT:
                serializeObject(parser, generator);
                break;
        }
    }

    static void serializeArray(JsonParser parser, JsonGenerator generator) throws IOException {
        generator.writeStartArray();
        for (JsonToken token = parser.nextToken();
                token != END_OF_STREAM && token != JsonToken.END_ARRAY;
                token = parser.nextToken()) {
            switch (parser.currentTokenId()) {
                case JsonTokenId.ID_STRING:
                    generator.writeString(parser.getString());
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

    static void serializeObject(JsonParser parser, JsonGenerator generator) throws IOException {
        generator.writeStartObject();
        for (JsonToken token = parser.nextToken();
                token != END_OF_STREAM && token != JsonToken.END_OBJECT;
                token = parser.nextToken()) {
            switch (parser.currentTokenId()) {
                case JsonTokenId.ID_PROPERTY_NAME:
                    generator.writeName(parser.currentName());
                    break;
                case JsonTokenId.ID_STRING:
                    generator.writeString(parser.getString());
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
