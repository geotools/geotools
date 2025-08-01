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

/**
 * GeoTools DataStore implementation for PMTiles archives containing Mapbox Vector Tiles.
 *
 * <h2>Overview</h2>
 *
 * <p>This package provides a complete GeoTools {@link org.geotools.api.data.DataStore} implementation for reading
 * PMTiles v3 archives with embedded vector tiles. It enables access to cloud-optimized tile archives from local files,
 * HTTP/HTTPS servers, and cloud storage providers (AWS S3, Azure Blob Storage, Google Cloud Storage).
 *
 * <h2>Architecture</h2>
 *
 * <p>The implementation consists of two main components:
 *
 * <h3>1. PMTiles-Specific Classes</h3>
 *
 * <ul>
 *   <li>{@link org.geotools.pmtiles.store.PMTilesDataStore} - Concrete DataStore implementation for PMTiles
 *   <li>{@link org.geotools.pmtiles.store.PMTilesDataStoreFactory} - Factory for creating PMTiles datastores with
 *       configuration
 * </ul>
 *
 * <h3>2. Generic Vector Tiles Support</h3>
 *
 * <ul>
 *   <li>{@link org.geotools.vectortiles.store} package - Generic abstractions for any vector tile source
 * </ul>
 *
 * <h2>Features</h2>
 *
 * <ul>
 *   <li><b>Cloud-Optimized Access</b>: Efficient byte-range reads minimize data transfer
 *   <li><b>Multi-Source Support</b>: Local files, HTTP, S3, Azure Blob, Google Cloud Storage
 *   <li><b>Automatic Zoom Selection</b>: Chooses optimal zoom level based on query resolution hints
 *   <li><b>Spatial Filtering</b>: Minimizes tile reads using bounding box extraction
 *   <li><b>CRS Transformation</b>: Native support for coordinate reference system transformations
 *   <li><b>Caching</b>: Configurable in-memory and disk caching for improved performance
 *   <li><b>Thread-Safe</b>: Supports concurrent access from multiple threads
 * </ul>
 *
 * <h2>Usage Example</h2>
 *
 * <pre>{@code
 * // Create datastore from S3
 * Map<String, Object> params = new HashMap<>();
 * params.put("pmtiles", "s3://my-bucket/tiles.pmtiles");
 * params.put("io.tileverse.rangereader.caching.enabled", true);
 * params.put("io.tileverse.rangereader.caching.blockaligned", true);
 *
 * PMTilesDataStore datastore = PMTilesDataStoreFactory.INSTANCE.createDataStore(params);
 *
 * // Query features
 * String typeName = datastore.getTypeNames()[0];
 * SimpleFeatureSource source = datastore.getFeatureSource(typeName);
 * Query query = new Query(typeName);
 * query.setFilter(BBOX(bbox));
 * FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures(query);
 * }</pre>
 *
 * <h2>Configuration Parameters</h2>
 *
 * <p>The datastore supports extensive configuration through {@link org.geotools.pmtiles.store.PMTilesDataStoreFactory}:
 *
 * <h3>Required Parameters</h3>
 *
 * <ul>
 *   <li><b>pmtiles</b>: URI to the PMTiles archive
 * </ul>
 *
 * <h3>Optional General Parameters</h3>
 *
 * <ul>
 *   <li><b>namespace</b>: Feature type namespace URI
 *   <li><b>io.tileverse.rangereader.caching.enabled</b>: Enable memory caching (default: false)
 *   <li><b>io.tileverse.rangereader.caching.blockaligned</b>: Use block-aligned caching (default: false)
 *   <li><b>io.tileverse.rangereader.caching.blocksize</b>: Cache block size in bytes
 * </ul>
 *
 * <h3>Cloud Provider Authentication</h3>
 *
 * <p>Authentication parameters are automatically loaded from the environment or can be explicitly configured:
 *
 * <ul>
 *   <li><b>AWS S3</b>: Access key, secret key, region, credential profiles
 *   <li><b>Azure Blob</b>: Account key, SAS token, blob name
 *   <li><b>Google Cloud Storage</b>: Project ID, application credentials
 * </ul>
 *
 * <p>See {@link org.geotools.pmtiles.store.PMTilesDataStoreFactory} for all available parameters.
 *
 * <h2>Performance Optimization</h2>
 *
 * <p>For optimal performance:
 *
 * <ul>
 *   <li>Enable memory caching for frequently accessed data
 *   <li>Use block-aligned caching to align reads with tile boundaries
 *   <li>Provide resolution hints ({@link org.geotools.util.factory.Hints#GEOMETRY_DISTANCE}) for automatic zoom level
 *       selection
 *   <li>Use spatial filters (BBOX) to minimize tile reads
 * </ul>
 *
 * <h2>Thread Safety</h2>
 *
 * <p>All classes in this package are designed for thread-safe concurrent access, making them suitable for server
 * environments like GeoServer where multiple requests may access the same datastore simultaneously.
 *
 * <h2>Integration with Tileverse Libraries</h2>
 *
 * <p>This package integrates three Tileverse libraries:
 *
 * <ul>
 *   <li><b>tileverse-rangereader</b>: Provides efficient byte-range access to data sources
 *   <li><b>tileverse-pmtiles</b>: Implements the PMTiles format specification
 *   <li><b>tileverse-vectortiles</b>: Handles Mapbox Vector Tile encoding/decoding
 *   <li><b>tileverse-tilematrixset</b>: Manages tile pyramids and coordinate systems
 * </ul>
 *
 * @see org.geotools.pmtiles.store.PMTilesDataStore
 * @see org.geotools.pmtiles.store.PMTilesDataStoreFactory
 * @see org.geotools.vectortiles.store
 * @see <a href="https://github.com/protomaps/PMTiles">PMTiles Specification</a>
 * @see <a href="https://tileverse.io">Tileverse Documentation</a>
 * @see <a href="https://github.com/mapbox/vector-tile-spec">Mapbox Vector Tile Specification</a>
 */
package org.geotools.pmtiles.store;
