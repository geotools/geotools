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

import com.google.common.annotations.VisibleForTesting;
import io.tileverse.pmtiles.PMTilesReader;
import io.tileverse.pmtiles.store.PMTilesVectorTileStore;
import io.tileverse.pmtiles.store.VectorTileStore;
import io.tileverse.rangereader.RangeReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.geotools.vectortiles.store.VectorTilesDataStore;

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
public class PMTilesDataStore extends VectorTilesDataStore {

    static final Logger LOGGER = Logging.getLogger(PMTilesDataStore.class);

    private RangeReader rangeReader;

    /**
     * Creates a new PMTiles datastore from a {@link RangeReader}.
     *
     * <p>The RangeReader provides the underlying byte-range access to the PMTiles archive, which can be from any
     * supported source (local file, HTTP, cloud storage). The datastore takes ownership of the RangeReader and will
     * close it when {@link #dispose()} is called.
     *
     * @param rangeReader the RangeReader providing access to the PMTiles archive
     * @throws IOException if the PMTiles archive cannot be read or is invalid
     */
    public PMTilesDataStore(RangeReader rangeReader) throws IOException {
        super(PMTilesDataStoreFactory.INSTANCE, createTileStore(rangeReader));
        this.rangeReader = rangeReader;
    }

    /**
     * Creates a new PMTiles datastore from a {@link PMTilesReader} for testing purposes.
     *
     * @param pmtiles the PMTilesReader to use
     * @throws IOException if the PMTiles archive cannot be read or is invalid
     */
    @VisibleForTesting
    PMTilesDataStore(PMTilesReader pmtiles) throws IOException {
        super(PMTilesDataStoreFactory.INSTANCE, new PMTilesVectorTileStore(pmtiles));
    }

    private static VectorTileStore createTileStore(RangeReader rangeReader) throws IOException {
        PMTilesReader pmtilesReader = new PMTilesReader(rangeReader::asByteChannel);
        return new PMTilesVectorTileStore(pmtilesReader);
    }

    /**
     * Disposes of this datastore and releases all resources, including closing the underlying {@link RangeReader}.
     *
     * <p>After calling this method, the datastore should not be used anymore.
     */
    @Override
    public void dispose() {
        try {
            super.dispose();
        } finally {
            closeRangeReader();
        }
    }

    private void closeRangeReader() {
        String sourceIdentifier = null;
        try (RangeReader reader = this.rangeReader) {
            this.rangeReader = null;
            if (reader != null) {
                sourceIdentifier = reader.getSourceIdentifier();
                LOGGER.fine("Closing range reader " + sourceIdentifier);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error closing range reader " + sourceIdentifier, e);
        }
    }
}
