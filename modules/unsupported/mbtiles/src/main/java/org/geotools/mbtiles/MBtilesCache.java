/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbtiles;

import static java.util.stream.Collectors.toMap;
import static org.geotools.mbtiles.MBTilesFile.WORLD_ENVELOPE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import no.ecc.vectortile.VectorTileDecoder;
import org.apache.commons.io.IOUtils;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.CanonicalSet;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;

/** Caches MBTiles in their parsed and clipped form, to avoid re-parsing the tiles over and over. */
class MBtilesCache {

    static final Logger LOGGER = Logging.getLogger(MBtilesCache.class);

    CanonicalSet<MBTilesTileLocation> canonicalizer = CanonicalSet.newInstance(MBTilesTileLocation.class);
    SoftValueHashMap<MBTilesTileLocation, Map<String, CollectionProvider>> cache = new SoftValueHashMap<>(0);
    Map<String, SimpleFeatureType> schemas = new HashMap<>();

    public MBtilesCache(Map<String, SimpleFeatureType> schemas) {
        this.schemas = schemas;
    }

    public SimpleFeatureCollection getFeatures(MBTilesTile tile, String layerName) throws IOException {
        MBTilesTileLocation location = canonicalizer.unique(tile.toLocation());
        Map<String, CollectionProvider> layers = cache.get(location);
        if (layers == null) {
            synchronized (location) {
                layers = cache.get(location);
                if (layers == null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "Miss for " + tile + ", looking for layer " + layerName);
                    }
                    Map<String, List<VectorTileDecoder.Feature>> mvtFeaturesMap = fillCache(tile);
                    layers = mapToProviders(location, mvtFeaturesMap);
                    cache.put(location, layers);
                }
            }
        } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Hit for " + tile + ", looking for layer " + layerName);
        }
        return Optional.ofNullable(layers.get(layerName))
                .map(p -> p.getGeoToolsFeatures())
                .orElse(null);
    }

    public Map<String, CollectionProvider> mapToProviders(
            MBTilesTileLocation location, Map<String, List<VectorTileDecoder.Feature>> mvtFeaturesMap) {
        return mvtFeaturesMap.entrySet().stream().collect(toMap(e -> e.getKey(), e -> {
            SimpleFeatureType schema = schemas.get(e.getKey());
            LayerFeatureBuilder builder = new LayerFeatureBuilder(location, schema);
            return new CollectionProvider(e.getValue(), builder);
        }));
    }

    private Map<String, List<VectorTileDecoder.Feature>> fillCache(MBTilesTile tile) throws IOException {
        VectorTileDecoder decoder = new VectorTileDecoder();
        decoder.setAutoScale(false);

        byte[] gzippedData = getPbfFromTile(tile);
        Map<String, List<VectorTileDecoder.Feature>> result = new HashMap<>();
        for (VectorTileDecoder.Feature mvtFeature : decoder.decode(gzippedData)) {
            String layer = mvtFeature.getLayerName();
            // skip unknown layers, as a safety measure
            if (schemas.get(layer) == null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(
                            Level.FINE,
                            "Skipping unknown layer " + layer + " (not described in the json metadata entry)");
                }
                continue;
            }
            List<VectorTileDecoder.Feature> features = result.computeIfAbsent(layer, l -> new ArrayList<>());
            features.add(mvtFeature);
        }

        // remap to a map from names to collections
        return result;
    }

    private byte[] getPbfFromTile(MBTilesTile tile) throws IOException {
        // from spec, the MVT contents are g-zipped
        byte[] raw = tile.getData();
        try (GZIPInputStream stream = new GZIPInputStream(new ByteArrayInputStream(raw))) {
            return IOUtils.toByteArray(stream);
        }
    }

    /**
     * Collects and returns all feature collections in tiles already available in cache
     *
     * @param z The zoom level
     * @param tb The rectangle of tiles to be retrieved from cache
     * @param layerName The layer name for which the features should be read
     * @return A map between the tile location and the corresponding feature collection
     */
    public Map<MBTilesTileLocation, SimpleFeatureCollection> getCachedFeatures(
            long z, RectangleLong tb, String layerName) {
        // using linked hash map to get consistent enumeration/rendering of tiles
        Map<MBTilesTileLocation, SimpleFeatureCollection> result = new LinkedHashMap<>();
        tb.forEach((x, y) -> {
            MBTilesTileLocation loc = new MBTilesTileLocation(z, x, y);
            Map<String, CollectionProvider> tileContents = cache.get(loc);
            if (tileContents != null) {
                SimpleFeatureCollection features = null;
                if (tileContents.containsKey(layerName)) {
                    features = tileContents.get(layerName).getGeoToolsFeatures();
                } else if (schemas.get(layerName) != null) {
                    // mark that the features were just not present, as opposed to not read
                    // otherwise this might trigger an uneeded extra read
                    features = new EmptyFeatureCollection(schemas.get(layerName));
                }

                if (features != null) {
                    result.put(loc, features);
                }
            }
        });
        return result;
    }

    /** Converts MVT screen features into GeoTools geograhic features, accumulating them in a feature collection */
    private static class LayerFeatureBuilder {

        private final String featureIdPrefix;
        private final ListFeatureCollection result;
        private final SimpleFeatureBuilder builder;
        private final MBTilesTileLocation tile;
        private final String geometryName;
        private final SimpleFeatureType schema;
        private final Polygon clip;
        private final ReferencedEnvelope tileEnvelope;
        private GeometryProcessor processor;

        LayerFeatureBuilder(MBTilesTileLocation tile, SimpleFeatureType schema) {
            this.geometryName = schema.getGeometryDescriptor().getLocalName();
            String typeName = schema.getTypeName();
            this.featureIdPrefix = getFeatureIdPrefix(tile, typeName);
            this.schema = schema;
            this.builder = new SimpleFeatureBuilder(schema);
            this.result = new ListFeatureCollection(schema);
            this.tile = tile;
            this.tileEnvelope = MBTilesFile.toEnvelope(tile);
            this.clip = JTS.toGeometry(tileEnvelope);
        }

        private String getFeatureIdPrefix(MBTilesTileLocation tile, String typeName) {
            return typeName + "_" + tile.getZoomLevel() + "_" + tile.getTileRow() + "_" + tile.getTileColumn() + ".";
        }

        void addFeature(VectorTileDecoder.Feature mvtFeature) {
            Geometry geometry = getGeometry(tile, mvtFeature);
            // geometry might have been fully outside boundaries
            if (geometry == null) {
                return;
            }
            builder.set(geometryName, geometry);

            // collect all the attributes
            Map<String, Object> attributes = mvtFeature.getAttributes();
            for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
                String attributeName = ad.getLocalName();
                Object value = attributes.get(attributeName);
                if (value != null) {
                    builder.set(attributeName, value);
                }
            }

            // test for tile border intersection, and if needed, add a clip that the renderer
            // will use to make extra borders disappear, much like client side apps do
            SimpleFeature feature = builder.buildFeature(featureIdPrefix + mvtFeature.getId());
            Envelope geometryEnvelope = geometry.getEnvelopeInternal();
            if (geometryEnvelope.getMinX() < tileEnvelope.getMinX()
                    || geometryEnvelope.getMaxX() > tileEnvelope.getMaxX()
                    || geometryEnvelope.getMinY() < tileEnvelope.getMinY()
                    || geometryEnvelope.getMaxY() > tileEnvelope.getMaxY()) {
                feature.getUserData().put(Hints.GEOMETRY_CLIP, clip);
            }

            // todo: handle the un-finished features
            result.add(feature);
        }

        private Geometry getGeometry(MBTilesTileLocation tile, VectorTileDecoder.Feature mvtFeature) {
            Geometry screenGeometry = mvtFeature.getGeometry();
            int extent = mvtFeature.getExtent();
            if (this.processor == null || processor.extent != extent) {
                this.processor = new GeometryProcessor(tile, extent);
            }

            return processor.process(screenGeometry);
        }
    }

    /**
     * Processes a screen space MBTiles geometry, clipping it to the tile boundaries, and turning it into a geographic
     * space one.
     */
    private static class GeometryProcessor {
        final AffineTransformation at;
        MBTilesTileLocation tile;
        int extent;

        GeometryProcessor(MBTilesTileLocation tile, int extent) {
            // This needs an explaination... the tiles follow the TMS spec, so they go from south to
            // north, however the geometry inside the tile uses screen coordinate system, so top to
            // bottom instead. Confused? Here are excerpts from the spec:

            // The tiles table contains tiles and the values used to locate them. The zoom_level,
            // tile_column, and tile_row columns MUST encode the location of the tile, following the
            // Tile Map // Service Specification, with the restriction that the global-mercator (aka
            // Spherical Mercator) profile MUST be used.

            // Geometry data in a Vector Tile is defined in a screen coordinate system.
            // The upper left corner of the tile (as displayed by default) is the origin of the
            // coordinate system.
            // The X axis is positive to the right, and the Y axis is positive downward.
            long numberOfTiles = Math.round(Math.pow(2, tile.getZoomLevel())); // number of tile columns/rows for chosen
            // zoom level
            double resX = WORLD_ENVELOPE.getSpan(0) / numberOfTiles; // points per tile
            double resY = WORLD_ENVELOPE.getSpan(1) / numberOfTiles; // points per tile
            double offsetX = WORLD_ENVELOPE.getMinimum(0);
            double offsetY = WORLD_ENVELOPE.getMinimum(1);

            double tx = offsetX + tile.getTileColumn() * resX;
            double ty = offsetY + (tile.getTileRow() + 1) * resY;
            double mx = resX / extent;
            double my = resY / extent;

            // affine transformation
            this.at = new AffineTransformation() {
                @Override
                public void filter(CoordinateSequence seq, int i) {
                    // java-vector-tile uses the exact same Coordinate object for first and
                    // last
                    // element of a ring, but we don't want to transform it twice
                    if (seq instanceof CoordinateSequence
                            && i > 0 // don't consider points
                            && i == seq.size() - 1
                            && seq.getCoordinate(0) == seq.getCoordinate(i)) {
                        return;
                    }
                    super.filter(seq, i);
                }
            };
            at.setToScale(mx, -my);
            at.translate(tx, ty);
        }

        /**
         * Rescales the geometry to real world coordinates
         *
         * @param screenGeometry the input geometry
         * @return the rescaled geometry, might have happened either in place, so the returned geometry is the same
         *     object as the screen geometry, or could be a different object
         */
        Geometry process(Geometry screenGeometry) {
            screenGeometry.apply(at);
            return screenGeometry;
        }
    }

    /** Allows delayed conversion of java-vector-tile features into GeoTools features. */
    private static class CollectionProvider {
        List<VectorTileDecoder.Feature> mvtFeatures;
        volatile SimpleFeatureCollection converted;
        LayerFeatureBuilder builder;

        public CollectionProvider(List<VectorTileDecoder.Feature> mvtFeatures, LayerFeatureBuilder builder) {
            this.mvtFeatures = mvtFeatures;
            this.builder = builder;
        }

        public SimpleFeatureCollection getGeoToolsFeatures() {
            if (converted == null) {
                synchronized (this) {
                    if (converted == null) {
                        mvtFeatures.stream().forEach(f -> builder.addFeature(f));
                        converted = builder.result;
                        builder = null;
                        mvtFeatures = null;
                    }
                }
            }

            return converted;
        }
    }
}
