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

import org.geotools.jackson.datatype.projjson.model.BoundCRS;
import org.geotools.jackson.datatype.projjson.model.CompoundCRS;
import org.geotools.jackson.datatype.projjson.model.CoordinateReferenceSystem;
import org.geotools.jackson.datatype.projjson.model.GeographicCRS;
import org.geotools.jackson.datatype.projjson.model.ProjectedCRS;
import org.geotools.jackson.datatype.projjson.model.Transformation;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.deser.std.StdDeserializer;

/**
 * Jackson deserializer for CoordinateReferenceSystem objects.
 *
 * <p>This deserializer handles polymorphic deserialization of different types of CRS objects based on the "type"
 * property in the JSON.
 */
public class CoordinateReferenceSystemDeserializer extends StdDeserializer<CoordinateReferenceSystem> {

    /** Creates a new deserializer instance. */
    public CoordinateReferenceSystemDeserializer() {
        super(CoordinateReferenceSystem.class);
    }

    @Override
    public CoordinateReferenceSystem deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {

        // Parse the JSON tree
        ObjectMapper mapper = (ObjectMapper) p.objectReadContext();
        JsonNode root = mapper.readTree(p);

        // Get the type field to determine which concrete class to use
        if (root.has("type")) {
            String type = root.get("type").asString();

            return switch (type) {
                case "GeographicCRS" -> mapper.treeToValue(root, GeographicCRS.class);
                case "ProjectedCRS" -> mapper.treeToValue(root, ProjectedCRS.class);
                case "CompoundCRS" -> mapper.treeToValue(root, CompoundCRS.class);
                case "BoundCRS" -> mapper.treeToValue(root, BoundCRS.class);
                case "Transformation" -> mapper.treeToValue(root, Transformation.class);
                default -> throw new RuntimeException("Unknown CRS type: " + type);
            };
        } else {
            throw new RuntimeException("Missing required 'type' field in CRS JSON");
        }
    }
}
