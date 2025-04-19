/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jackson.datatype.projjson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import org.geotools.jackson.datatype.projjson.model.BoundCRS;
import org.geotools.jackson.datatype.projjson.model.CompoundCRS;
import org.geotools.jackson.datatype.projjson.model.CoordinateReferenceSystem;
import org.geotools.jackson.datatype.projjson.model.GeographicCRS;
import org.geotools.jackson.datatype.projjson.model.ProjectedCRS;
import org.geotools.jackson.datatype.projjson.model.Transformation;

/**
 * Jackson deserializer for CoordinateReferenceSystem objects.
 *
 * <p>This deserializer handles polymorphic deserialization of different types of CRS objects based on the "type"
 * property in the JSON.
 */
public class CoordinateReferenceSystemDeserializer extends StdDeserializer<CoordinateReferenceSystem> {

    private static final long serialVersionUID = 1L;

    /** Creates a new deserializer instance. */
    public CoordinateReferenceSystemDeserializer() {
        super(CoordinateReferenceSystem.class);
    }

    @Override
    public CoordinateReferenceSystem deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        // Parse the JSON tree
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode root = mapper.readTree(p);

        // Get the type field to determine which concrete class to use
        if (root.has("type")) {
            String type = root.get("type").asText();

            switch (type) {
                case "GeographicCRS":
                    return mapper.treeToValue(root, GeographicCRS.class);
                case "ProjectedCRS":
                    return mapper.treeToValue(root, ProjectedCRS.class);
                case "CompoundCRS":
                    return mapper.treeToValue(root, CompoundCRS.class);
                case "BoundCRS":
                    return mapper.treeToValue(root, BoundCRS.class);
                case "Transformation":
                    return mapper.treeToValue(root, Transformation.class);
                default:
                    throw new IOException("Unknown CRS type: " + type);
            }
        } else {
            throw new IOException("Missing required 'type' field in CRS JSON");
        }
    }
}
