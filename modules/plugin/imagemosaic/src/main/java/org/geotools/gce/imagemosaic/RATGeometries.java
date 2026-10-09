/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/**
 * How the mosaic handles a geometry column of a raster attribute table whose value differs between granules. A source
 * clipping the geometry to each granule, as S-102 does, makes that the normal case rather than an error.
 */
public enum RATGeometries {
    /** Leaves the cell empty. */
    OFF,
    /** Unions the granule geometries, which costs time and memory on the large ones. */
    MERGE,
    /** Treats the column like any other, so granules that disagree stop the collection. */
    COLLECT;

    static final Logger LOGGER = Logging.getLogger(RATGeometries.class);

    /** Reads the value of the {@code AttributeTableGeometries} indexer property, {@link #OFF} when it is not set. */
    public static RATGeometries fromParameter(String value) {
        if (value == null || value.isBlank()) return OFF;
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, e, () -> "Unknown AttributeTableGeometries value " + value + ", using OFF");
            return OFF;
        }
    }
}
