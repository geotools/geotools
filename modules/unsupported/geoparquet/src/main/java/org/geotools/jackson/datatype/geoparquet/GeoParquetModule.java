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
package org.geotools.jackson.datatype.geoparquet;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.Serial;
import org.geotools.jackson.datatype.projjson.ProjJSONModule;

/**
 * Jackson module for GeoParquet metadata handling.
 *
 * <p>This module configures Jackson for parsing GeoParquet metadata structures, including support for PROJJSON
 * coordinate reference systems through the ProjJSONModule.
 *
 * <p>Note: If not using the {@link #createObjectMapper()} factory method, make sure to also register the
 * {@link org.geotools.jackson.datatype.projjson.ProjJSONModule} to properly handle CRS objects in GeoParquet metadata.
 */
public class GeoParquetModule extends SimpleModule {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Creates a new GeoParquetModule. */
    public GeoParquetModule() {
        super("GeoParquetModule", new Version(1, 0, 0, null, "org.geotools.jackson.datatype", "geoparquet"));
    }

    /**
     * Creates and configures an ObjectMapper with this module and the ProjJSONModule registered.
     *
     * @return a configured ObjectMapper instance
     */
    public static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new GeoParquetModule());
        mapper.registerModule(new ProjJSONModule());
        return mapper;
    }
}
