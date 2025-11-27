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

import java.io.Serial;
import org.geotools.jackson.datatype.projjson.model.CoordinateReferenceSystem;
import tools.jackson.core.Version;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.module.SimpleModule;

/**
 * Jackson Module that supports serialization and deserialization of PROJJSON objects.
 *
 * <p>PROJJSON is a standard representation for coordinate reference systems (CRS) in JSON format, used in various
 * geospatial applications, including GeoParquet for storing CRS information.
 *
 * <p>The Schema for the GeoJSON format is defined at: <a
 * href="https://proj.org/en/latest/schemas/v0.7/projjson.schema.json">
 * https://proj.org/en/latest/schemas/v0.7/projjson.schema.json </a>
 *
 * <p>To use this module with an existing {@link tools.jackson.databind.ObjectMapper}:
 *
 * <pre>
 * ObjectMapper mapper = new ObjectMapper();
 * mapper.registerModule(new ProjJSONModule());
 * </pre>
 */
public class ProjJSONModule extends SimpleModule {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Creates a new instance of the PROJJSON module with default settings. */
    public ProjJSONModule() {
        super("ProjJSONModule", new Version(1, 0, 0, null, "org.geotools.jackson.datatype", "projjson"));

        // Register the deserializers (serialization seems to be covered fine by the default behavior)
        addDeserializer(CoordinateReferenceSystem.class, new CoordinateReferenceSystemDeserializer());
    }

    /**
     * Gets the new module instance. This is an alternative to constructor, to support SPI-based auto-registration.
     *
     * @return The new module instance
     */
    public static JacksonModule getInstance() {
        return new ProjJSONModule();
    }
}
