/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import org.geotools.jdbc.EnumMapping;
import org.geotools.util.Converters;

/** Helper class to parse and encode JSON arrays */
class JSONArrayIO {

    JsonFactory factory = new JsonFactory();

    /** Parses a JSON string, meant to be a JSON array, into a Java array */
    public <T> T[] parse(String json, Class<T> type, EnumMapping mapping) {
        List<T> result = new ArrayList<>();
        try (JsonParser parser = factory.createParser(json)) {
            JsonToken token = parser.nextToken();
            if (token.id() != JsonTokenId.ID_START_ARRAY) {
                throw new IllegalArgumentException("Not a JSON array: " + json);
            }
            while ((token = parser.nextToken()).id() != JsonTokenId.ID_END_ARRAY) {
                if (token.isScalarValue()) {
                    if (mapping != null) {
                        @SuppressWarnings("unchecked")
                        T value = (T) mapping.fromKey(parser.getValueAsString());
                        result.add(value);
                    } else {
                        result.add(Converters.convert(parser.getText(), type));
                    }
                } else {
                    throw new IllegalArgumentException("Unexpected nested object found");
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        Object array = Array.newInstance(type, result.size());
        for (int i = 0; i < result.size(); i++) {
            Array.set(array, i, result.get(i));
        }
        @SuppressWarnings("unchecked")
        T[] ra = (T[]) array;
        return ra;
    }
}
