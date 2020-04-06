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
package org.geotools.data.elasticsearch;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.io.IOException;

public class TotalDeserializer extends StdDeserializer<Long> {

    public TotalDeserializer() {
        this(null);
    }

    public TotalDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        try {
            return jsonParser.readValueAs(Long.class);
        } catch (MismatchedInputException e) {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            return node.get("value").longValue();
        }
    }
}
