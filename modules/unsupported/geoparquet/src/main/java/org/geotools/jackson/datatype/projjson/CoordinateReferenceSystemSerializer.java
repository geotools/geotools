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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.geotools.jackson.datatype.projjson.model.CoordinateReferenceSystem;

/**
 * Jackson serializer for CoordinateReferenceSystem objects.
 *
 * <p>This serializer handles polymorphic serialization of different types of CRS objects by delegating to the default
 * serialization process.
 */
public class CoordinateReferenceSystemSerializer extends StdSerializer<CoordinateReferenceSystem> {

    private static final long serialVersionUID = 1L;

    /** Creates a new serializer instance. */
    public CoordinateReferenceSystemSerializer() {
        super(CoordinateReferenceSystem.class);
    }

    @Override
    public void serialize(CoordinateReferenceSystem value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        // Let the standard serialization process handle the polymorphic CRS
        // This will use the @JsonTypeInfo and other annotations
        provider.defaultSerializeValue(value, gen);
    }
}
