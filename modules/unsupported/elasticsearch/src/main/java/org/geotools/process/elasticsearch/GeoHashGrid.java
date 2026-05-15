/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.elasticsearch;

import static org.geotools.process.elasticsearch.GridCoverageUtil.pad;

import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.LatLong;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.crs.ForceCoordinateSystemFeatureResults;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

abstract class GeoHashGrid {

    private static final Logger LOGGER = Logging.getLogger(GeoHashGrid.class);

    private static final int DEFAULT_PRECISION = 2;

    public static final String BUCKET_NAME_KEY = "key";

    public static final String BUCKETS_KEY = "buckets";

    public static final String DOC_COUNT_KEY = "doc_count";

    public static final String VALUE_KEY = "value";

    private double cellWidth;

    private double cellHeight;

    private double lonOffset;

    private Envelope envelope;

    private ReferencedEnvelope boundingBox;

    private float emptyCellValue;

    private float[][] grid;

    private RasterScale scale;

    GeoHashGrid() {
        this.emptyCellValue = 0;
        this.scale = new RasterScale();
    }

    public void initalize(
            ReferencedEnvelope srcEnvelope,
            SimpleFeatureCollection features,
            String aggregationDefinition,
            String queryDefinition)
            throws TransformException, FactoryException, IOException {
        final List<Map<String, Object>> buckets = readFeatures(features, aggregationDefinition, queryDefinition);

        // cannot trust the definition, as the store might update it to enforce safety limits
        // check from the first returned geohash instead
        final String firstGeohash =
                buckets.isEmpty() ? null : (String) buckets.get(0).get("key");
        Integer precision;
        if (!isValid(firstGeohash)) {
            LOGGER.fine("No aggregations found or missing/invalid GeoHash key");
            precision = DEFAULT_PRECISION;

        } else {
            precision = ((String) buckets.get(0).get("key")).length();
        }

        cellWidth = GeoHash.widthDegrees(precision);
        cellHeight = GeoHash.heightDegrees(precision);

        boolean reproject =
                !CRS.equalsIgnoreMetadata(srcEnvelope.getCoordinateReferenceSystem(), DefaultGeographicCRS.WGS84);
        if (reproject) {
            srcEnvelope = srcEnvelope.transform(DefaultGeographicCRS.WGS84, true);
        }
        computeMinLonOffset(srcEnvelope);
        envelope = computeEnvelope(srcEnvelope, precision);
        if (reproject) envelope = pad(envelope, precision);

        // the envelope is expressed on the geohash cell centers, boundingbox is the full
        // coverage of the raster instead
        boundingBox = new ReferencedEnvelope(
                envelope.getMinX() - cellWidth / 2.0,
                envelope.getMaxX() + cellWidth / 2.0,
                envelope.getMinY() - cellHeight / 2.0,
                envelope.getMaxY() + cellHeight / 2.0,
                DefaultGeographicCRS.WGS84);

        final int numCol = (int) Math.round((envelope.getMaxX() - envelope.getMinX()) / cellWidth + 1);
        final int numRow = (int) Math.round((envelope.getMaxY() - envelope.getMinY()) / cellHeight + 1);
        grid = new float[numRow][numCol];
        LOGGER.fine("Created grid with size (" + numCol + ", " + numRow + ")");

        if (emptyCellValue != 0) {
            for (float[] row : grid) Arrays.fill(row, emptyCellValue);
        }
        List<GridCell> cells = new ArrayList<>();
        buckets.forEach(bucket -> {
            Number rasterValue = computeCellValue(bucket);
            cells.add(new GridCell((String) bucket.get("key"), rasterValue));
            scale.prepareScale(rasterValue.floatValue());
        });
        cells.forEach(cell -> updateGrid(cell.getGeohash(), cell.getValue()));
        LOGGER.fine("Read " + cells.size() + " aggregation buckets");
    }

    protected abstract Number computeCellValue(Map<String, Object> bucket);

    private void updateGrid(String geohash, Number value) {
        if (geohash != null && value != null) {
            final LatLong latLon = GeoHash.decodeHash(geohash);
            final double lat = latLon.getLat();
            double lon = latLon.getLon() + lonOffset;
            if (isValid(lat, lon - 360)) updateGrid(lat, lon - 360, value);
            if (isValid(lat, lon)) updateGrid(lat, lon, value);
            while (isValid(lat, lon += 360)) {
                updateGrid(lat, lon, value);
            }
        }
    }

    private void updateGrid(double lat, double lon, Number value) {
        final int row = grid.length - (int) Math.round((lat - envelope.getMinY()) / cellHeight) - 1;
        final int col = (int) Math.round((lon - envelope.getMinX()) / cellWidth);

        grid[Math.min(row, grid.length - 1)][Math.min(col, grid[0].length - 1)] = scale.scaleValue(value.floatValue());
    }

    public GridCoverage2D toGridCoverage2D() {
        final GridCoverageFactory coverageFactory =
                CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
        return coverageFactory.create("geohashGridAgg", grid, boundingBox);
    }

    private List<Map<String, Object>> readFeatures(
            SimpleFeatureCollection features, String aggregationDefinition, String queryDefinition) throws IOException {
        final List<Map<String, Object>> buckets = new ArrayList<>();
        FeatureCollection featureCollection = unwrap(features);
        ElasticBucketVisitor visitor = new ElasticBucketVisitor(aggregationDefinition, queryDefinition);
        if (featureCollection instanceof ContentFeatureCollection
                || featureCollection instanceof DecoratingSimpleFeatureCollection) {
            featureCollection.accepts(visitor, null);
            return visitor.getBuckets();
        } else {
            ObjectMapper mapper = new ObjectMapper();
            try (SimpleFeatureIterator iterator = features.features()) {
                while (iterator.hasNext()) {
                    final SimpleFeature feature = iterator.next();
                    if (feature.getAttribute("_aggregation") != null) {
                        final byte[] data = (byte[]) feature.getAttribute("_aggregation");
                        try {
                            final Map<String, Object> aggregation = mapper.readValue(data, new TypeReference<>() {});
                            buckets.add(aggregation);
                        } catch (JacksonException e) {
                            LOGGER.fine("Failed to parse aggregation value: " + e);
                        }
                    }
                }
            }
            return buckets;
        }
    }

    FeatureCollection unwrap(SimpleFeatureCollection features) {
        if (features instanceof ForceCoordinateSystemFeatureResults forceCoordinateSystemFeatureResults) {
            return forceCoordinateSystemFeatureResults.getOrigin();
        }
        return features;
    }

    private Envelope computeEnvelope(ReferencedEnvelope outEnvelope, int precision) {
        final String minHash =
                GeoHash.encodeHash(Math.max(-90, outEnvelope.getMinY()), outEnvelope.getMinX(), precision);
        final LatLong minLatLon = GeoHash.decodeHash(minHash);
        final double minLon = minLatLon.getLon() + lonOffset;

        final double width = Math.ceil(outEnvelope.getWidth() / cellWidth) * cellWidth;
        final double maxLon = minLon + width - cellWidth;
        final String maxHash = GeoHash.encodeHash(Math.min(90, outEnvelope.getMaxY()), maxLon, precision);
        final LatLong maxLatLon = GeoHash.decodeHash(maxHash);

        return new Envelope(minLon, maxLon, minLatLon.getLat(), maxLatLon.getLat());
    }

    private void computeMinLonOffset(ReferencedEnvelope env) {
        double minLon;
        if (env.getMinX() > 180) {
            minLon = env.getMinX() % 360;
        } else if (env.getMinX() < -180) {
            minLon = 360 - Math.abs(env.getMinX()) % 360;
        } else {
            minLon = env.getMinX() % 360;
        }
        if (minLon > 180) {
            minLon -= 360;
        }
        lonOffset = env.getMinX() - minLon;
    }

    private boolean isValid(final double lat, final double lon) {
        return lon >= envelope.getMinX()
                && lon <= envelope.getMaxX()
                && lat >= envelope.getMinY()
                && lat <= envelope.getMaxY();
    }

    private boolean isValid(String geohash) {
        return geohash != null && geohash.equals(GeoHash.encodeHash(GeoHash.decodeHash(geohash), geohash.length()));
    }

    String pluckBucketName(Map<String, Object> bucket) {
        if (!bucket.containsKey(BUCKET_NAME_KEY)) {
            LOGGER.warning("Unable to pluck key, bucket does not contain required field:" + BUCKET_NAME_KEY);
            throw new IllegalArgumentException();
        }
        return bucket.get(BUCKET_NAME_KEY) + "";
    }

    Number pluckDocCount(Map<String, Object> bucket) {
        if (!bucket.containsKey(DOC_COUNT_KEY)) {
            LOGGER.warning("Unable to pluck document count, bucket does not contain required key:" + DOC_COUNT_KEY);
            throw new IllegalArgumentException();
        }
        return (Number) bucket.get(DOC_COUNT_KEY);
    }

    Number pluckMetricValue(Map<String, Object> bucket, String metricKey, String valueKey) {
        Number value;
        if (null == metricKey || metricKey.trim().length() == 0) {
            value = pluckDocCount(bucket);
        } else {
            if (!bucket.containsKey(metricKey)) {
                LOGGER.warning("Unable to pluck metric, bucket does not contain required key:" + metricKey);
                throw new IllegalArgumentException();
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> metric = (Map<String, Object>) bucket.get(metricKey);
            if (!metric.containsKey(valueKey)) {
                LOGGER.warning("Unable to pluck value, metric does not contain required key:" + valueKey);
                throw new IllegalArgumentException();
            }
            value = (Number) metric.get(valueKey);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> pluckAggBuckets(Map<String, Object> parentBucket, String aggKey) {
        if (!parentBucket.containsKey(aggKey)) {
            LOGGER.warning(
                    "Unable to pluck aggregation results, parent bucket does not contain required key:" + aggKey);
            throw new IllegalArgumentException();
        }
        Map<String, Object> aggResults = (Map<String, Object>) parentBucket.get(aggKey);
        if (!aggResults.containsKey(BUCKETS_KEY)) {
            LOGGER.warning(
                    "Unable to pluck buckets, aggregation results bucket does not contain required key:" + BUCKETS_KEY);
            throw new IllegalArgumentException();
        }
        return (List<Map<String, Object>>) aggResults.get(BUCKETS_KEY);
    }

    public void setParams(List<String> params) {
        // ignore params
    }

    public void setEmptyCellValue(Float value) {
        if (null != value) {
            this.emptyCellValue = value;
        }
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public double getCellHeight() {
        return cellHeight;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public ReferencedEnvelope getBoundingBox() {
        return boundingBox;
    }

    public float[][] getGrid() {
        return grid;
    }

    public void setScale(RasterScale scale) {
        this.scale = scale;
    }
}
