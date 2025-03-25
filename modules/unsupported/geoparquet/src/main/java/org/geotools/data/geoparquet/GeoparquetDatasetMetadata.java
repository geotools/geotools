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
package org.geotools.data.geoparquet;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.stream.Stream;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;

/**
 * Represents metadata for an entire GeoParquet dataset, which may contain multiple files.
 *
 * <p>This class collects and aggregates metadata from all files in a dataset, providing unified access to bounds, CRS
 * information, and other dataset-wide properties. When dealing with a directory of GeoParquet files, this class
 * provides a consolidated view of the metadata across all files.
 *
 * <p>Key functionality includes:
 *
 * <ul>
 *   <li>Computing unified bounds across all files in the dataset
 *   <li>Providing access to CRS information
 *   <li>Supporting directory-based datasets with multiple GeoParquet files
 * </ul>
 */
public class GeoparquetDatasetMetadata {

    /** Map of file names to their parsed GeoParquet metadata */
    private Map<String, GeoParquetMetadata> md;

    /**
     * Creates a new dataset metadata instance from a map of individual file metadata.
     *
     * @param md Map of file names to their GeoParquet metadata
     */
    public GeoparquetDatasetMetadata(Map<String, GeoParquetMetadata> md) {
        this.md = Map.copyOf(requireNonNull(md));
    }

    public boolean isEmpty() {
        return md.isEmpty();
    }

    CoordinateReferenceSystem getCrs() {
        try {
            return CRS.decode("EPSG:4326", true);
        } catch (FactoryException e) {
            throw new IllegalStateException(e);
        }
    }

    public ReferencedEnvelope getBounds() {

        Stream<Envelope> allFilesBounds = md.values().stream().sequential().map(GeoParquetMetadata::bounds);
        Envelope fullBounds = allFilesBounds.reduce(new Envelope(), (b1, b2) -> {
            b1.expandToInclude(b2);
            return b1;
        });
        CoordinateReferenceSystem crs = getCrs();
        return new ReferencedEnvelope(fullBounds, crs);
    }
}
