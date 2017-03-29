/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.process.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.LatLong;
import com.vividsolutions.jts.geom.Envelope;

public abstract class GeoHashGrid {

    private final static Logger LOGGER = Logging.getLogger(GeoHashGrid.class);

    private static final int DEFAULT_PRECISION = 2;

    public static final String BUCKET_NAME_KEY = "key";
    public static final String BUCKETS_KEY = "buckets";
    public static final String DOC_COUNT_KEY = "doc_count";
    public static final String VALUE_KEY = "value";

    private double cellWidth;

    private double cellHeight;

    private Envelope envelope;

    private ReferencedEnvelope boundingBox;

    private List<Map<String, Object>> buckets;

    private float emptyCellValue = 0;

    private float[][] grid;
    
    private RasterScale scale = new RasterScale(null);
        
    public GeoHashGrid initalize(ReferencedEnvelope srcEnvelope, SimpleFeatureCollection features) throws NoSuchAuthorityCodeException, TransformException, FactoryException {
        this.buckets = readFeatures(features);

        final String firstGeohash = buckets.isEmpty() ? null : (String) buckets.get(0).get("key");
        final int precision;
        if (firstGeohash == null || !isValid(firstGeohash)) {
            LOGGER.fine("No aggregations found or missing/invalid geohash key");
            precision = DEFAULT_PRECISION;
        } else {
            precision = ((String) buckets.get(0).get("key")).length();
        }

        cellWidth = GeoHash.widthDegrees(precision);
        cellHeight = GeoHash.heightDegrees(precision);

        if (srcEnvelope.getCoordinateReferenceSystem() != null) {
            srcEnvelope = srcEnvelope.transform(DefaultGeographicCRS.WGS84,false);
        }
        envelope = computeEnvelope(srcEnvelope, precision);

        boundingBox = new ReferencedEnvelope(envelope.getMinX()-cellWidth/2.0, envelope.getMaxX()+cellWidth/2.0,
                envelope.getMinY()-cellHeight/2.0, envelope.getMaxY()+cellHeight/2.0, DefaultGeographicCRS.WGS84);

        final int numCol = (int) Math.round((envelope.getMaxX()-envelope.getMinX())/cellWidth+1);
        final int numRow = (int) Math.round((envelope.getMaxY()-envelope.getMinY())/cellHeight+1);
        grid = new float[numRow][numCol];

        if (emptyCellValue != 0) {
            for (float[] row: grid)
                Arrays.fill(row, emptyCellValue);
        }
        List<GridCell> cells = new ArrayList<GridCell>();
        buckets.stream().forEach(bucket -> {
           Number rasterValue =  computeCellValue(bucket);
           cells.add(new GridCell((String) bucket.get("key"), rasterValue));
           scale.prepareScale(rasterValue.floatValue());
        });
        cells.stream().forEach(cell -> updateGrid(cell.getGeohash(), cell.getValue()));
        return this;
    }

    public abstract Number computeCellValue(Map<String,Object> bucket);

    protected boolean updateGrid(String geohash, Number value) {
        final boolean valid;
        if (geohash != null && value != null) {
            final LatLong latLon = GeoHash.decodeHash(geohash);
            final double lat = latLon.getLat();
            final double lon = latLon.getLon();
            valid = isValid(lat,lon);
            if (valid) {
                final int row = grid.length-(int) Math.round((lat-envelope.getMinY())/cellHeight)-1;
                final int col = (int) Math.round((lon-envelope.getMinX())/cellWidth);
                grid[Math.min(row,grid.length-1)][Math.min(col,grid[0].length-1)] = scale.scaleValue(value.floatValue());
            }
        } else {
            valid = false;
        }
        return valid;
    }
    
    public GridCoverage2D toGridCoverage2D() {
        final GridCoverageFactory coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
        return coverageFactory.create("geohashGridAgg", grid, boundingBox);
    }

    private List<Map<String, Object>> readFeatures(SimpleFeatureCollection features) {
        final ObjectMapper mapper = new ObjectMapper();

        final List<Map<String, Object>> buckets = new ArrayList<>();
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                final SimpleFeature feature = iterator.next();
                if (feature.getAttribute("_aggregation") != null) {
                    final byte[] data = (byte[]) feature.getAttribute("_aggregation");
                    try {
                        final Map<String,Object> aggregation = mapper.readValue(data, new TypeReference<Map<String,Object>>() {});
                        buckets.add(aggregation);
                    } catch (IOException e) {
                        LOGGER.fine("Failed to parse aggregation value: " + e);
                    }
                }
            }
        }
        return buckets;
    }

    private static Envelope computeEnvelope(ReferencedEnvelope outEnvelope, int precision) throws NoSuchAuthorityCodeException, TransformException, FactoryException {
        final String minHash = GeoHash.encodeHash(Math.max(-90,outEnvelope.getMinY()), Math.max(-180, outEnvelope.getMinX()), precision);
        final String maxHash = GeoHash.encodeHash(Math.min(90, outEnvelope.getMaxY()), Math.min(180, outEnvelope.getMaxX()), precision);
        final LatLong minLatLon = GeoHash.decodeHash(minHash);
        final LatLong maxLatLon = GeoHash.decodeHash(maxHash);
        return new Envelope(minLatLon.getLon(), maxLatLon.getLon(), minLatLon.getLat(), maxLatLon.getLat());
    }

    private boolean isValid(final double lat, final double lon) {
        return lon>=envelope.getMinX() && lon<=envelope.getMaxX() && lat>=envelope.getMinY() && lat<=envelope.getMaxY();
    }

    private boolean isValid(String geohash) {
        return geohash != null && GeoHash.encodeHash(GeoHash.decodeHash(geohash), geohash.length()).equals(geohash);
    }

    protected String pluckBucketName(Map<String,Object> bucket) {
        if (!bucket.containsKey(BUCKET_NAME_KEY)) {
          LOGGER.warning("Unable to pluck key, bucket does not contain required field:" + BUCKET_NAME_KEY);
          throw new IllegalArgumentException();
        }
        return bucket.get(BUCKET_NAME_KEY) + "";
    }
    
    protected Number pluckDocCount(Map<String,Object> bucket) {
        if (!bucket.containsKey(DOC_COUNT_KEY)) {
            LOGGER.warning("Unable to pluck document count, bucket does not contain required key:" + DOC_COUNT_KEY);
            throw new IllegalArgumentException();
        }
        return (Number) bucket.get(DOC_COUNT_KEY);
    }
    
    protected Number pluckMetricValue(Map<String,Object> bucket, String metricKey, String valueKey) {
        Number value;
        if (null == metricKey || metricKey.trim().length() == 0) {
            value = pluckDocCount(bucket);
        } else {
            if (!bucket.containsKey(metricKey)) {
                LOGGER.warning("Unable to pluck metric, bucket does not contain required key:" + metricKey);
                throw new IllegalArgumentException();
            }
            Map<String,Object> metric = (Map<String,Object>) bucket.get(metricKey);
            if (!metric.containsKey(valueKey)) {
                LOGGER.warning("Unable to pluck value, metric does not contain required key:" + valueKey);
                throw new IllegalArgumentException();
            }
            value = (Number) metric.get(valueKey);
        }
        return value;
    }
    
    protected List<Map<String,Object>> pluckAggBuckets(Map<String,Object> parentBucket, String aggKey) {
        if (!parentBucket.containsKey(aggKey)) {
          LOGGER.warning("Unable to pluck aggregation results, parent bucket does not contain required key:" + aggKey);
          throw new IllegalArgumentException();
        }
        Map<String,Object> aggResults = (Map<String,Object>) parentBucket.get(aggKey);
        if (!aggResults.containsKey(BUCKETS_KEY)) {
          LOGGER.warning("Unable to pluck buckets, aggregation results bucket does not contain required key:" + BUCKETS_KEY);
          throw new IllegalArgumentException();
        }
        return (List<Map<String,Object>>) aggResults.get(BUCKETS_KEY);
    }

    public void setParams(List<String> params) {
        //ignore params
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
