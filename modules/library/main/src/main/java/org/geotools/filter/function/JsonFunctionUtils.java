package org.geotools.filter.function;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;

import java.io.IOException;

public interface JsonFunctionUtils {

    static final JsonToken END_OF_STREAM = null;

    default void serializeContents(JsonParser parser, JsonGenerator generator) throws IOException {
        switch (parser.currentTokenId()) {
            case JsonTokenId.ID_START_ARRAY:
                serializeArray(parser, generator);
                break;
            case JsonTokenId.ID_START_OBJECT:
                serializeObject(parser, generator);
                break;
        }
    }

    default void serializeArray(JsonParser parser, JsonGenerator generator) throws IOException {
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

    default void serializeObject(JsonParser parser, JsonGenerator generator) throws IOException {
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
