/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.pmtiles.store;

import io.tileverse.pmtiles.PMTilesReader;
import io.tileverse.pmtiles.store.PMTilesVectorTileStore;
import io.tileverse.rangereader.RangeReader;
import java.io.IOException;
import java.util.logging.Level;
import org.geotools.vectortiles.store.VectorTilesDataStore;
import org.jspecify.annotations.NullMarked;

/**
 * GeoTools DataStore implementation for reading PMTiles archives containing Mapbox Vector Tiles.
 *
 * <p>This datastore provides access to cloud-optimized PMTiles archives from various sources including local files,
 * HTTP/HTTPS servers, and cloud storage (AWS S3, Azure Blob Storage, Google Cloud Storage). It uses the Tileverse Range
 * Reader library for efficient byte-range access and the Tileverse PMTiles library for reading the PMTiles format.
 *
 * <p><b>Features:</b>
 *
 * <ul>
 *   <li>Reads PMTiles v3 archives with embedded vector tiles
 *   <li>Supports multiple data sources via {@link RangeReader}
 *   <li>Automatic zoom level selection based on query resolution
 *   <li>Spatial filtering and bbox optimization
 *   <li>Coordinate reference system transformation
 *   <li>Thread-safe concurrent access
 * </ul>
 *
 * @see PMTilesDataStoreFactory
 * @see VectorTilesDataStore
 * @see <a href="https://github.com/protomaps/PMTiles">PMTiles Specification</a>
 */
@NullMarked
public class PMTilesDataStore extends VectorTilesDataStore {

    private PMTilesReader pmtilesReader;

    /**
     * Creates a new PMTiles datastore from a {@link PMTilesReader}.
     *
     * <p>The PMTilesReader provides access to the PMTiles archive and manages the underlying byte-range reader and
     * caches. The datastore takes ownership of the reader and will close it when {@link #dispose()} is called.
     *
     * @param factory the factory that created this datastore, used for accessing shared resources
     * @param pmtilesReader the PMTilesReader providing access to the PMTiles archive
     * @throws IOException if the PMTiles archive cannot be read or is invalid
     */
    public PMTilesDataStore(PMTilesDataStoreFactory factory, PMTilesReader pmtilesReader) throws IOException {
        super(factory, new PMTilesVectorTileStore(pmtilesReader));
        this.pmtilesReader = pmtilesReader;
    }

    /**
     * Disposes of this datastore and releases all resources, including closing the underlying {@link PMTilesReader}.
     *
     * <p>After calling this method, the datastore should not be used anymore.
     */
    @Override
    public void dispose() {
        try {
            super.dispose();
        } finally {
            closePmtilesReader();
        }
    }

    private void closePmtilesReader() {
        if (pmtilesReader != null) {
            String sourceIdentifier = pmtilesReader.getSourceIdentifier();
            try {
                pmtilesReader.close();
            } catch (IOException e) {
                super.LOGGER.log(Level.WARNING, "Error closing range reader %s".formatted(sourceIdentifier), e);
            }
        }
    }
}
