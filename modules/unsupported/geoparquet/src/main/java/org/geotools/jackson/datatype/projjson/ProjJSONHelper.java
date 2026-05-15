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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.jackson.datatype.projjson.model.GeographicCRS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * Helper class for working with PROJJSON in GeoParquet files.
 *
 * <p>This class provides utility methods for parsing PROJJSON strings into GeoTools CRS objects, and converting between
 * the PROJJSON model and GeoTools referencing objects.
 */
public class ProjJSONHelper {

    private static final Logger LOGGER = Logger.getLogger(ProjJSONHelper.class.getName());

    private static final ObjectMapper OBJECT_MAPPER =
            JsonMapper.builder().addModule(new ProjJSONModule()).build();

    /**
     * Parses a PROJJSON string into a GeoTools CRS object.
     *
     * @param projjson The PROJJSON string to parse
     * @return The parsed CRS, or WGS84 if parsing fails
     */
    public static CoordinateReferenceSystem parseCRS(String projjson) {
        if (projjson == null || projjson.trim().isEmpty()) {
            return DefaultGeographicCRS.WGS84;
        }

        try {
            // Parse the PROJJSON string to our model
            org.geotools.jackson.datatype.projjson.model.CoordinateReferenceSystem modelCrs;
            modelCrs = OBJECT_MAPPER.readValue(
                    projjson, org.geotools.jackson.datatype.projjson.model.CoordinateReferenceSystem.class);

            // Convert from model to GeoTools CRS
            return convertToGeoToolsCRS(modelCrs);
        } catch (JacksonException e) {
            LOGGER.log(Level.WARNING, "Failed to parse PROJJSON: " + e.getMessage(), e);
            return DefaultGeographicCRS.WGS84;
        }
    }

    /**
     * Converts a PROJJSON model CRS to a GeoTools CRS.
     *
     * @param modelCrs The PROJJSON model CRS
     * @return The equivalent GeoTools CRS
     */
    private static CoordinateReferenceSystem convertToGeoToolsCRS(
            org.geotools.jackson.datatype.projjson.model.CoordinateReferenceSystem modelCrs) {

        // First check for common cases we can handle directly
        if (modelCrs instanceof GeographicCRS geographicCRS) {
            String name = geographicCRS.getName();

            // Check for common geographic CRSs
            if ("WGS 84".equals(name)) {
                return DefaultGeographicCRS.WGS84;
            }
        }

        // For more complex CRSs, we might need to use the EPSG code if available
        Object id = modelCrs.getId();
        if (id instanceof org.geotools.jackson.datatype.projjson.model.Identifier identifier) {

            if ("EPSG".equals(identifier.getAuthority())) {
                try {
                    return CRS.decode("EPSG:" + identifier.getCode(), true);
                } catch (FactoryException e) {
                    LOGGER.log(Level.WARNING, "Failed to decode EPSG code: " + e.getMessage(), e);
                }
            }
        }

        // For now, default to WGS84 if we can't convert
        // A more complete implementation would build the CRS from the PROJJSON definition
        LOGGER.log(Level.WARNING, "Couldn't convert PROJJSON CRS: " + modelCrs.getName() + ", defaulting to WGS84");
        return DefaultGeographicCRS.WGS84;
    }
}
