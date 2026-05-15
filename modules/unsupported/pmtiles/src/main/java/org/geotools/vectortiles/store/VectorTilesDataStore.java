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
package org.geotools.vectortiles.store;

import static java.util.Objects.requireNonNull;

import io.tileverse.jackson.databind.tilejson.v3.VectorLayer;
import io.tileverse.pmtiles.store.VectorTileStore;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.feature.type.Name;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.pmtiles.store.PMTilesDataStore;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;

/**
 * Abstract base GeoTools DataStore implementation for tiled vector data sources.
 *
 * <p>This class provides the foundation for creating GeoTools DataStores that read vector tiles from tile archives like
 * PMTiles or other tiled formats. It bridges between the Tileverse {@link VectorTileStore} abstraction and GeoTools'
 * {@link ContentDataStore} architecture.
 *
 * <p><b>Architecture:</b>
 *
 * <ul>
 *   <li>Wraps a {@link VectorTileStore} that provides access to the underlying tile data
 *   <li>Creates one {@link VectorTilesFeatureSource} per vector layer in the tile metadata
 *   <li>Automatically derives {@link org.geotools.api.feature.simple.SimpleFeatureType} from layer metadata
 *   <li>Supports spatial filtering, CRS transformation, and zoom level optimization
 * </ul>
 *
 * <p><b>Features:</b>
 *
 * <ul>
 *   <li>Automatic feature type discovery from TileJSON metadata
 *   <li>Spatial query optimization using tile extent calculations
 *   <li>Automatic zoom level selection based on query resolution hints
 *   <li>CRS transformation support
 *   <li>Thread-safe concurrent access
 * </ul>
 *
 * <p><b>Subclass Implementation:</b> Concrete subclasses must provide a {@link VectorTileStore} instance through the
 * constructor. The store handles all tile-specific logic (reading, caching, spatial indexing), while this class handles
 * GeoTools integration.
 *
 * <p><b>Feature Type Mapping:</b> Each vector layer in the tile metadata becomes a feature type. The schema is derived
 * from the layer's field definitions in the TileJSON metadata, with geometry attributes added automatically.
 *
 * @see VectorTileStore
 * @see VectorTilesFeatureSource
 * @see PMTilesDataStore
 * @see <a href="https://github.com/mapbox/tilejson-spec">TileJSON Specification</a>
 */
public abstract class VectorTilesDataStore extends ContentDataStore {

    public static final GeometryFactory DEFAULT_GEOMETRY_FACTORY =
            new GeometryFactory(new PackedCoordinateSequenceFactory());

    private VectorTileStore tileStore;

    /**
     * Creates a new vector tiles datastore.
     *
     * @param factory the factory that created this datastore
     * @param tileStore the underlying tile store providing access to vector tiles
     * @throws IOException if the tile store cannot be initialized or metadata cannot be read
     */
    protected VectorTilesDataStore(DataStoreFactorySpi factory, VectorTileStore tileStore) throws IOException {
        this.tileStore = requireNonNull(tileStore, "tileStore is null");
        setDataStoreFactory(requireNonNull(factory, "factory is null"));
        setFeatureTypeFactory(CommonFactoryFinder.getFeatureTypeFactory(null));
        setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
        setFilterFactory(CommonFactoryFinder.getFilterFactory());
        setGeometryFactory(VectorTilesDataStore.DEFAULT_GEOMETRY_FACTORY);
    }

    /**
     * Returns the underlying {@link VectorTileStore} that provides access to the tile data.
     *
     * <p>Used by {@link VectorTilesFeatureSource}
     *
     * @return the vector tile store
     */
    public VectorTileStore getTileStore() {
        return this.tileStore;
    }

    /**
     * Creates the list of feature type names available in this datastore.
     *
     * <p>Each vector layer in the tile metadata becomes a feature type. The names are derived from the layer IDs in the
     * TileJSON metadata.
     *
     * @return list of feature type names
     */
    @Override
    protected List<Name> createTypeNames() {
        return tileStore.getVectorLayersMetadata().stream()
                .map(VectorLayer::id)
                .map(super::name)
                .toList();
    }

    /**
     * Creates a feature source for a specific vector layer.
     *
     * @param entry the content entry for the feature type
     * @return a new {@link VectorTilesFeatureSource} for the layer
     * @throws IOException if the layer does not exist or cannot be accessed
     */
    @Override
    protected VectorTilesFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        String typeName = entry.getTypeName();
        Optional<VectorLayer> layerMetadata = tileStore.getLayerMetadata(typeName);
        if (layerMetadata.isEmpty()) {
            throw new IOException("Vector layer %s does not exist.".formatted(typeName));
        }
        return new VectorTilesFeatureSource(entry, layerMetadata.orElseThrow());
    }
}
