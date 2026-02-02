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
import tools.jackson.core.ObjectReadContext;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
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
        ObjectReadContext rc = p.objectReadContext();
        JsonNode root = rc.readTree(p);

        JsonNode typeNode = root.get("type");
        if (typeNode == null) {
            throw new RuntimeException("Missing required 'type' field in CRS JSON");
        }
        String type = typeNode.asString();

        Class<? extends CoordinateReferenceSystem> target =
                switch (type) {
                    case "GeographicCRS" -> GeographicCRS.class;
                    case "ProjectedCRS" -> ProjectedCRS.class;
                    case "CompoundCRS" -> CompoundCRS.class;
                    case "BoundCRS" -> BoundCRS.class;
                    case "Transformation" -> Transformation.class;
                    default -> throw new RuntimeException("Unknown CRS type: " + type);
                };

        // Convert tree -> tokens -> value
        JsonParser treeParser = rc.treeAsTokens(root);
        treeParser.nextToken(); // required: treeAsTokens() is not positioned yet
        return rc.readValue(treeParser, target);
    }
}
