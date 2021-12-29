/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geojson;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Support class providing {@link com.fasterxml.jackson.databind.ObjectMapper} instances configured
 * with the desired settings.
 */
class ObjectMapperFactory {

    private static ObjectMapper DEFAULT_MAPPER;

    static {
        DEFAULT_MAPPER = new ObjectMapper();
        DEFAULT_MAPPER.registerModule(new JtsModule());
    }

    /**
     * Returns an {@link ObjectMapper} set up to parse JTS geometries, with the default settings.
     * See also {@link JtsModule#DEFAULT_MAX_DECIMALS}, {@link JtsModule#DEFAULT_MIN_DECIMALS} and
     * {@link JtsModule#DEFAULT_ROUND_MODE}. It is to be noticed, the settings above only affects
     * writing, so the default mapper should be used for all read operations.
     *
     * @return
     */
    public static ObjectMapper getDefaultMapper() {
        return DEFAULT_MAPPER;
    }
}
