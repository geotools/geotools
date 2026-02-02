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

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.exc.MismatchedInputException;

public class TotalDeserializer extends StdDeserializer<Long> {

    public TotalDeserializer() {
        this(Long.class);
    }

    public TotalDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws JacksonException {
        try {
            return jsonParser.readValueAs(Long.class);
        } catch (MismatchedInputException e) {
            JsonNode node = jsonParser.objectReadContext().readTree(jsonParser);
            return node.get("value").longValue();
        }
    }
}
