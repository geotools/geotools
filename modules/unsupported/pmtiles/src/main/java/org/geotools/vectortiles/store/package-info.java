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
 * Generic GeoTools DataStore abstractions for tiled vector data sources.
 *
 * <h2>Overview</h2>
 *
 * <p>This package provides reusable abstractions for creating GeoTools DataStores that read vector tiles from tile
 * archives or services. While designed primarily for PMTiles, these abstractions can be used with any tiled vector data
 * source that implements the Tileverse {@link io.tileverse.pmtiles.store.VectorTileStore} interface.
 *
 * <h2>Architecture</h2>
 *
 * <p>The package follows a layered architecture that separates GeoTools integration concerns from tile-specific logic:
 *
 * <pre>
 * ┌─────────────────────────────────────────────┐
 * │  GeoTools Query API (Query, Filter, etc.)   │
 * └─────────────────┬───────────────────────────┘
 *                   │
 * ┌─────────────────▼───────────────────────────┐
 * │  VectorTilesFeatureSource                   │
 * │  - Query translation                        │
 * │  - Zoom level selection                     │
 * │  - Spatial filtering optimization           │
 * │  - CRS transformation                       │
 * └─────────────────┬───────────────────────────┘
 *                   │
 * ┌─────────────────▼───────────────────────────┐
 * │  VectorTilesFeatureReader                   │
 * │  - Vector tile → GeoTools feature mapping   │
 * │  - Clip mask handling                       │
 * │  - Feature filtering                        │
 * └─────────────────┬───────────────────────────┘
 *                   │
 * ┌─────────────────▼───────────────────────────┐
 * │  Tileverse VectorTileStore                  │
 * │  - Tile reading                             │
 * │  - Spatial indexing                         │
 * │  - MVT decoding                             │
 * └─────────────────────────────────────────────┘
 * </pre>
 *
 * <h2>Core Classes</h2>
 *
 * <h3>Data Store Layer</h3>
 *
 * <ul>
 *   <li>{@link org.geotools.vectortiles.store.VectorTilesDataStore} - Abstract base DataStore for vector tiles
 * </ul>
 *
 * <h3>Feature Access Layer</h3>
 *
 * <ul>
 *   <li>{@link org.geotools.vectortiles.store.VectorTilesFeatureSource} - Feature source with query optimization
 *   <li>{@link org.geotools.vectortiles.store.VectorTilesFeatureReader} - Converts vector tile features to GeoTools
 *       features
 *   <li>{@link org.geotools.vectortiles.store.VectorTilesSimpleFeature} - SimpleFeature wrapper for vector tile
 *       features
 * </ul>
 *
 * <h3>Utility Classes</h3>
 *
 * <ul>
 *   <li>{@link org.geotools.vectortiles.store.StreamFeatureReader} - Stream-based feature reader with filtering and
 *       pagination
 *   <li>{@link org.geotools.vectortiles.store.ExtractMultiBoundsFilterVisitor} - Extracts bounding boxes from complex
 *       filters as a list of individual envelopes from spatial predicates, for
 *       {@link io.tileverse.pmtiles.store.VectorTileStore} to skip tiles that would otherwise be included only as the
 *       result of union'ing all the bounding boxes in the GeoTools filter.
 *   <li>{@link org.geotools.vectortiles.store.VectorTilesFeaturePropertyAccessorFactory} - Property access for
 *       filtering at the {@link io.tileverse.vectortile.model.VectorTile.Layer.Feature vector tile} level
 * </ul>
 *
 * <h2>Main Features</h2>
 *
 * <h3>Query Optimization</h3>
 *
 * <ul>
 *   <li><b>Automatic Zoom Level Selection</b>: Selects the optimal zoom level based on query resolution hints
 *       ({@link org.geotools.util.factory.Hints#GEOMETRY_DISTANCE}, {@code GEOMETRY_SIMPLIFICATION},
 *       {@code GEOMETRY_GENERALIZATION})
 *   <li><b>Spatial Filtering</b>: Extracts bounding boxes from filters to minimize tile reads
 *   <li><b>Pre-Filtering</b>: Applies filters at the vector tile level before converting to GeoTools features
 *   <li><b>Property Selection</b>: Supports schema subsetting to reduce data transfer
 * </ul>
 *
 * <h3>CRS Transformation</h3>
 *
 * <ul>
 *   <li><b>Efficient Reprojection</b>: Uses affine transformations for in-place coordinate sequence transformation
 *       where possible
 *   <li><b>Filter Reprojection</b>: Automatically reprojects spatial filters to the native CRS
 * </ul>
 *
 * <h3>Rendering Support</h3>
 *
 * <ul>
 *   <li><b>Clip Masks</b>: Provides tile boundary clip masks via {@link org.geotools.util.factory.Hints#GEOMETRY_CLIP}
 *       for correct rendering
 *   <li><b>Generalization Hints</b>: Supports topology-preserving and non-topology-preserving simplification
 * </ul>
 *
 * <h2>Query Capabilities</h2>
 *
 * <p>The vector tiles feature source supports:
 *
 * <ul>
 *   <li><b>Filtering</b>: Full GeoTools filter support (spatial, attribute, logical)
 *   <li><b>Reprojection</b>: Automatic CRS transformation
 *   <li><b>Retyping</b>: Property selection and schema subsetting
 *   <li><b>Pagination</b>: Offset and limit support
 *   <li><b>Sorting</b>: Natural order only (tile order)
 * </ul>
 *
 * <h2>Feature Type Schema</h2>
 *
 * <p>Feature types are automatically derived from vector layer metadata (TileJSON):
 *
 * <ul>
 *   <li><b>Attribute Fields</b>: Mapped from TileJSON field definitions (String, Number, Boolean)
 *   <li><b>Geometry Attribute</b>: Added automatically with the tile matrix set's CRS
 *   <li><b>Type Name</b>: Derived from the layer ID
 *   <li><b>Description</b>: Optional layer description from metadata
 * </ul>
 *
 * <h2>Zoom Level Selection Strategy</h2>
 *
 * <p>The zoom level selection algorithm considers:
 *
 * <ol>
 *   <li>Query resolution hints (GEOMETRY_DISTANCE, etc.)
 *   <li>Layer-specific min/max zoom levels from TileJSON
 *   <li>Tile matrix set available zoom levels
 *   <li>Query strategy:
 *       <ul>
 *         <li><b>SPEED</b>: When simplification hints are present, favors lower zoom levels
 *         <li><b>QUALITY</b>: When no simplification hints, favors higher zoom levels
 *       </ul>
 * </ol>
 *
 * <h2>Implementation Guide</h2>
 *
 * <p>To create a new vector tile datastore:
 *
 * <pre>{@code
 * // 1. Implement VectorTileStore for your tile source
 * public class MyVectorTileStore implements VectorTileStore {
 *     // Implement tile reading, metadata, etc.
 * }
 *
 * // 2. Extend VectorTilesDataStore
 * public class MyDataStore extends VectorTilesDataStore {
 *     public MyDataStore(VectorTileStore tileStore) throws IOException {
 *         super(MyDataStoreFactory.INSTANCE, tileStore);
 *     }
 * }
 *
 * // 3. Create a DataStoreFactory
 * public class MyDataStoreFactory implements DataStoreFactorySpi {
 *     public MyDataStore createDataStore(Map<String, ?> params) {
 *         // Create VectorTileStore and wrap it
 *     }
 * }
 * }</pre>
 *
 * <h2>Thread Safety</h2>
 *
 * <p>All classes in this package are designed for thread-safe concurrent access. The underlying
 * {@link io.tileverse.pmtiles.store.VectorTileStore} must also be thread-safe.
 *
 * <h2>Performance Considerations</h2>
 *
 * <ul>
 *   <li><b>Feature Count</b>: Returns -1 (expensive to calculate for tiled data)
 *   <li><b>Bounds Calculation</b>: Returns null for filtered queries (requires full scan)
 *   <li><b>Memory Usage</b>: Features are streamed, not loaded entirely into memory
 *   <li><b>Filter Pushdown</b>: Spatial and attribute filters are applied at the vector tile level before feature
 *       conversion
 * </ul>
 *
 * <h2>Related Packages</h2>
 *
 * <ul>
 *   <li>{@link org.geotools.pmtiles.store} - Concrete PMTiles DataStore implementation
 * </ul>
 *
 * @see org.geotools.vectortiles.store.VectorTilesDataStore
 * @see org.geotools.vectortiles.store.VectorTilesFeatureSource
 * @see io.tileverse.pmtiles.store.VectorTileStore
 * @see <a href="https://github.com/mapbox/vector-tile-spec">Mapbox Vector Tile Specification</a>
 * @see <a href="https://github.com/mapbox/tilejson-spec">TileJSON Specification</a>
 */
package org.geotools.vectortiles.store;
