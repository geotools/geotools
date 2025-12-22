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
import static java.util.function.Predicate.not;
import static org.geotools.util.factory.Hints.GEOMETRY_CLIP;
import static org.geotools.util.factory.Hints.GEOMETRY_DISTANCE;
import static org.geotools.util.factory.Hints.GEOMETRY_GENERALIZATION;
import static org.geotools.util.factory.Hints.JTS_COORDINATE_SEQUENCE_FACTORY;
import static org.geotools.util.factory.Hints.JTS_GEOMETRY_FACTORY;

import io.tileverse.jackson.databind.tilejson.v3.VectorLayer;
import io.tileverse.pmtiles.store.VectorTileStore;
import io.tileverse.pmtiles.store.VectorTilesQuery;
import io.tileverse.tiling.common.BoundingBox2D;
import io.tileverse.tiling.matrix.TileMatrixSet;
import io.tileverse.tiling.store.TileStore;
import io.tileverse.tiling.store.TileStore.Strategy;
import io.tileverse.vectortile.model.VectorTile;
import io.tileverse.vectortile.model.VectorTile.Layer.Feature;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.FeatureTypeFactory;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.spatial.DefaultCRSFilterVisitor;
import org.geotools.filter.spatial.ReprojectingFilterVisitor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.filter.visitor.SpatialFilterVisitor;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.util.AffineTransformation;

/**
 * Feature source for a single vector layer in a tiled vector dataset.
 *
 * <p>This class provides GeoTools feature access to a single vector layer from a tile archive. It handles the
 * complexity of:
 *
 * <ul>
 *   <li>Converting between GeoTools queries and tile-based queries
 *   <li>Automatic zoom level selection based on query resolution hints
 *   <li>Spatial filtering and bounding box optimization
 *   <li>Coordinate reference system transformation
 *   <li>Feature attribute filtering and property selection
 *   <li>Pagination (offset/limit support)
 * </ul>
 *
 * <p><b>Query Optimization:</b>
 *
 * <ul>
 *   <li><b>Zoom Level Selection:</b> Automatically selects the best zoom level based on
 *       {@link Hints#GEOMETRY_DISTANCE}, or {@link Hints#GEOMETRY_GENERALIZATION} hints
 *   <li><b>Spatial Filtering:</b> Extracts bounding boxes from query filters to minimize tile reads
 *   <li><b>CRS Transformation:</b> Efficiently transforms geometries using affine transformations where possible
 *   <li><b>Pre-filtering:</b> Applies filters at the vector tile level before converting to GeoTools features
 * </ul>
 *
 * <p><b>Supported Operations:</b>
 *
 * <ul>
 *   <li>{@link #canFilter(Query)} - Full filter support including spatial and attribute filters
 *   <li>{@link #canReproject()} - Automatic CRS transformation
 *   <li>{@link #canRetype(Query)} - Property selection and schema subsetting
 *   <li>{@link #canOffset(Query)} and {@link #canLimit(Query)} - Pagination support
 *   <li>{@link #canSort(Query)} - Natural order only (tile order)
 * </ul>
 *
 * <p><b>Feature Type Schema:</b> The schema is automatically derived from the vector layer metadata (TileJSON), with
 * field types mapped from TileJSON types (String, Number, Boolean) to Java types.
 *
 * @see VectorTilesDataStore
 * @see VectorTilesFeatureReader
 * @see io.tileverse.pmtiles.store.VectorTileStore
 */
public class VectorTilesFeatureSource extends ContentFeatureSource {

    private static final Logger LOGGER = Logging.getLogger(VectorTilesFeatureSource.class);

    /** TileJSON layer metadata */
    protected final VectorLayer layerMetadata;

    /**
     * Creates a new feature source for a vector layer.
     *
     * @param entry the content entry for this feature type
     * @param layerMetadata the TileJSON metadata for this vector layer
     */
    public VectorTilesFeatureSource(ContentEntry entry, VectorLayer layerMetadata) {
        super(entry, Query.ALL);
        this.layerMetadata = layerMetadata;
    }

    /**
     * @return {@code true}
     * @see ContentFeatureSource#canRetype
     */
    @Override
    protected boolean canRetype(Query query) {
        return true;
    }

    /**
     * @return {@code true}
     * @see ContentFeatureSource#canReproject
     * @see #reprojectFunction(Query)
     */
    @Override
    protected boolean canReproject() {
        return true;
    }

    /**
     * @return {@code true}
     * @see ContentFeatureSource#canFilter
     * @see #preFilter(Query)
     * @see #postFilter(Query)
     */
    @Override
    protected boolean canFilter(Query query) {
        return true;
    }

    /**
     * @return {@code true}
     * @see ContentFeatureSource#canOffset
     */
    @Override
    protected boolean canOffset(Query query) {
        return true;
    }

    /**
     * @return {@code true}
     * @see ContentFeatureSource#canLimit
     */
    @Override
    protected boolean canLimit(Query query) {
        return true;
    }

    /**
     * @return {@code true} for natural order, {@code false} otherwise
     * @see ContentFeatureSource#canSort
     */
    @Override
    protected boolean canSort(Query query) {
        SortBy[] sortBy = query.getSortBy();
        return sortBy == null || sortBy.length == 0 || (sortBy.length == 1 && sortBy[0].equals(SortBy.NATURAL_ORDER));
    }

    @Override
    public VectorTilesDataStore getDataStore() {
        return (VectorTilesDataStore) super.getDataStore();
    }

    protected VectorTileStore getTileStore() {
        return getDataStore().getTileStore();
    }

    /**
     * Returns the tile matrix set for this vector layer.
     *
     * @return the tile matrix set defining the tiling scheme
     */
    protected TileMatrixSet getMatrixSet() {
        return getTileStore().matrixSet();
    }

    /**
     * Adds rendering hints supported by this feature source.
     *
     * <p>Supported hints include:
     *
     * <ul>
     *   <li>{@link Hints#FEATURE_DETACHED} - Features can be safely modified
     *   <li>{@link Hints#JTS_GEOMETRY_FACTORY} - Custom geometry factory
     *   <li>{@link Hints#JTS_COORDINATE_SEQUENCE_FACTORY} - Custom coordinate sequence factory
     *   <li>{@link Hints#GEOMETRY_GENERALIZATION} - Topology-preserving simplification distance
     *   <li>{@link Hints#GEOMETRY_DISTANCE} - Target resolution for zoom level selection
     *   <li>{@link Hints#GEOMETRY_CLIP} - Tile clip mask for rendering (this is an "output" hint, added to each
     *       returned {@link SimpleFeature#getUserData()} map. See {@link VectorTilesFeatureReader})
     * </ul>
     *
     * @param hints the set to add supported hints to
     */
    @Override
    protected void addHints(Set<Hints.Key> hints) {
        requireNonNull(hints);
        // mark the features as detached, that is, the user can directly alter them
        // without altering the state of the datastore
        hints.add(Hints.FEATURE_DETACHED);

        // the org.locationtech.jts.geom.GeometryFactory instance to use.
        hints.add(JTS_GEOMETRY_FACTORY);

        // the CoordinateSequenceFactory instance to use.
        hints.add(JTS_COORDINATE_SEQUENCE_FACTORY);

        // topology preserving on the fly generalization of the geometries.
        // the renderer tries this one first, then GEOMETRY_SIMPLIFICATION, then GEOMETRY_DISTANCE.
        // we don't advertise GEOMETRY_SIMPLIFICATION for the renderer to fall back to GEOMETRY_DISTANCE,
        // which provides better consistency for tiled vector data where zoom levels are discrete.
        hints.add(GEOMETRY_GENERALIZATION);

        // Asks a datastore having a vector pyramid (pre-generalized geometries) to return the geometry version whose
        // points have been generalized less than the specified distance (further generalization might be performed by
        // the client in memory).
        hints.add(GEOMETRY_DISTANCE);

        // Used along with vector tile geometries, includes the clip mask to be used when rendering the geometry
        hints.add(GEOMETRY_CLIP);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        FeatureTypeFactory featureTypeFactory = requireNonNull(getDataStore().getFeatureTypeFactory());
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder(featureTypeFactory);
        ftb.setNamespaceURI(getDataStore().getNamespaceURI());
        ftb.setName(layerMetadata.id());
        String description = layerMetadata.description();
        if (description != null) {
            ftb.setDescription(new SimpleInternationalString(description));
        }

        Map<String, String> fieldBindings = layerMetadata.fields();

        fieldBindings.forEach((name, typeStr) -> ftb.add(name, fieldTypeToBinding(typeStr)));

        String geometryName = dedupGeometryName(fieldBindings.keySet());
        CoordinateReferenceSystem crs = tileMatrixsetCRS();
        ftb.add(geometryName, Geometry.class, crs);

        return ftb.buildFeatureType();
    }

    /**
     * @return {@code -1}, too complex to calculate, tiles have different number of features at different zoom levels
     */
    @Override
    protected int getCountInternal(Query query) throws IOException {
        return -1;
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        if (null == query.getFilter() || Filter.INCLUDE.equals(query.getFilter())) {
            return getBoundsInternal();
        }
        return null;
    }

    protected ReferencedEnvelope getBoundsInternal() {
        // layerMetadata does not provide layer-specific bounds, we're stuck with the full bounds
        BoundingBox2D fullExtent = tileMatrixsetFullExtent();
        CoordinateReferenceSystem crs = getSchema().getCoordinateReferenceSystem();
        double minX = fullExtent.minX();
        double maxX = fullExtent.maxX();
        double minY = fullExtent.minY();
        double maxY = fullExtent.maxY();
        return new ReferencedEnvelope(minX, maxX, minY, maxY, crs);
    }

    @Override
    @SuppressWarnings("resource")
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
        query = simplifyFilter(query);
        SimpleFeatureType targetSchema = retype(query);
        if (Filter.EXCLUDE.equals(query.getFilter())) {
            return StreamFeatureReader.empty(targetSchema);
        }

        Optional<VectorTilesQuery> vtQuery = toVectorTilesQuery(query);
        Stream<VectorTile.Layer.Feature> vectorTileFeatures;

        if (vtQuery.isEmpty()) {
            vectorTileFeatures = Stream.empty();
        } else {
            VectorTileStore tileStore = getTileStore();
            vectorTileFeatures = tileStore.getFeatures(vtQuery.orElseThrow());
        }

        return new VectorTilesFeatureReader(targetSchema, vectorTileFeatures)
                .filter(postFilter(query))
                .offset(offset(query))
                .limit(limit(query));
    }

    private Query simplifyFilter(Query query) {
        Filter fitlter = SimplifyingFilterVisitor.simplify(query.getFilter(), getSchema());
        query = new Query(query);
        query.setFilter(fitlter);
        return query;
    }

    private Optional<VectorTilesQuery> toVectorTilesQuery(Query query) {
        // Determine the best zoom level to use
        final OptionalInt zoomLevel = determineZoomLevel(query);
        if (!zoomLevel.isPresent()) {
            return Optional.empty();
        }

        // Filter TileMatrix by bounding boxes in Query.Filter
        final List<BoundingBox2D> queryExtent = queryExtent(query);

        // Use the provided GeometryFactory
        final GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(query.getHints());

        // Reproject to the target CRS if needed, applies optimizations for in-place CoordinateSequence reprojection
        // where possible
        final UnaryOperator<Geometry> reprojectFunction = reprojectFunction(query);

        // Filter out VectorTile features before converting them to SimpleFeature
        final Predicate<VectorTile.Layer.Feature> preFilter = preFilter(query);

        VectorTilesQuery vtQuery = new VectorTilesQuery()
                .layers(this.layerMetadata.id())
                .extent(queryExtent)
                .zoomLevel(zoomLevel.getAsInt())
                .geometryFactory(geometryFactory)
                // first transform from tile extent to source CRS
                .transformToCrs(true)
                // then transform to target CRS
                .geometryTransformation(reprojectFunction)
                // finally apply the filter to the vector tile Feature
                // with VectorTilesFeaturePropertyAccessorFactory
                .filter(preFilter);
        return Optional.of(vtQuery);
    }

    private UnaryOperator<Geometry> reprojectFunction(Query query) {
        final MathTransform mathTransform = findMathTransform(query);

        if (mathTransform.isIdentity()) {
            return UnaryOperator.identity();
        }

        if (mathTransform instanceof java.awt.geom.AffineTransform at) {
            // fastest path, apply transform to CoordinateSequences in-place
            // e.g. any org.geotools.api.referencing.operation.MathTransform2D
            AffineTransformation geometryAT = new AffineTransformation(
                    at.getScaleX(),
                    at.getShearX(),
                    at.getTranslateX(),
                    at.getShearY(),
                    at.getScaleY(),
                    at.getTranslateY());

            return geom -> {
                // in-place transform
                geom.apply(geometryAT);
                return geom;
            };
        }
        // slower path, creates new Geometries
        final GeometryCoordinateSequenceTransformer transformer = new GeometryCoordinateSequenceTransformer();
        transformer.setMathTransform(mathTransform);
        return geom -> {
            try {
                return transformer.transform(geom);
            } catch (TransformException e) {
                throw new IllegalStateException(
                        "A transformation exception occurred while reprojecting data on the fly", e);
            }
        };
    }

    protected MathTransform findMathTransform(Query query) {
        CoordinateReferenceSystem targetCrs = getQueryCRS(query);
        CoordinateReferenceSystem sourceCrs = getSchema().getCoordinateReferenceSystem();
        if (CRS.equalsIgnoreMetadata(targetCrs, sourceCrs)) {
            return IdentityTransform.create(2);
        }

        try {
            return CRS.findMathTransform(sourceCrs, targetCrs);
        } catch (FactoryException e) {
            throw new IllegalArgumentException("Unable to find math transformation", e);
        }
    }

    protected CoordinateReferenceSystem getQueryCRS(Query query) {
        CoordinateReferenceSystem coordinateSystemReproject = query.getCoordinateSystemReproject();
        if (coordinateSystemReproject != null) {
            return coordinateSystemReproject;
        }
        CoordinateReferenceSystem crsOverride = query.getCoordinateSystem();
        if (crsOverride != null) {
            return crsOverride;
        }
        return getSchema().getCoordinateReferenceSystem();
    }

    protected SimpleFeatureType retype(Query query) {
        SimpleFeatureType fullSchema = getSchema();
        if (query == null || query.retrieveAllProperties()) {
            return fullSchema;
        }

        FeatureTypeFactory featureTypeFactory =
                Objects.requireNonNull(getDataStore().getFeatureTypeFactory());
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder(featureTypeFactory);
        ftb.setName(getName());

        String[] propertyNames = query.getPropertyNames();
        if (!Arrays.equals(Query.NO_NAMES, propertyNames)) {
            for (String propertyName : propertyNames) {
                AttributeDescriptor descriptor = fullSchema.getDescriptor(propertyName);
                if (descriptor == null) {
                    throw new IllegalArgumentException(
                            "Vector layer %s does not have property %s".formatted(layerMetadata.id(), propertyName));
                }
                ftb.add(descriptor);
            }
        }

        return ftb.buildFeatureType();
    }

    private long offset(Query query) {
        return query.getStartIndex() == null ? 0 : query.getStartIndex().longValue();
    }

    private long limit(Query query) {
        return query.isMaxFeaturesUnlimited() ? Long.MAX_VALUE : query.getMaxFeatures();
    }

    /**
     * Converts the {@link Query#getFilter() query filter} to a predicate to be executed against
     * {@link io.tileverse.vectortile.model.Feature} instances before they're converted to {@link SimpleFeature}, by
     * means of the {@link VectorTilesFeaturePropertyAccessorFactory}.
     */
    private Predicate<Feature> preFilter(Query query) {
        Filter filter = query.getFilter();
        return filter::evaluate;
    }

    private Filter postFilter(Query query) {
        return Filter.INCLUDE; // query.getFilter();
    }

    /**
     * @return single-element list with full layer extents if the whole layer shall be queried, an empty list of nothing
     *     should be queries, the list of valid query bounds otherwise.
     */
    private List<BoundingBox2D> queryExtent(Query query) {
        List<ReferencedEnvelope> queryBounds = extractQueryBounds(query);
        final ReferencedEnvelope maxBounds = getBoundsInternal();
        return queryBounds.stream()
                .filter(not(ReferencedEnvelope::isEmpty))
                .map(queryExtent -> clipToMaxBounds(maxBounds, queryExtent))
                .filter(Objects::nonNull)
                .map(this::toExtent)
                .toList();
    }

    private ReferencedEnvelope clipToMaxBounds(final ReferencedEnvelope maxBounds, ReferencedEnvelope queryExtent) {
        if (maxBounds.intersects((Envelope) queryExtent)) {
            return maxBounds.intersection(queryExtent);
        }
        return null;
    }

    private List<ReferencedEnvelope> extractQueryBounds(Query query) {
        Filter nativeCrsFilter = reprojectSpatialFilter(query.getFilter());
        CoordinateReferenceSystem nativeCrs = getSchema().getCoordinateReferenceSystem();
        return ExtractMultiBoundsFilterVisitor.getBounds(nativeCrsFilter, nativeCrs);
    }

    /**
     * Reprojects spatial filters so that they match the feature source native CRS, and assuming all literal geometries
     * are specified in the specified declaredCRS
     */
    private Filter reprojectSpatialFilter(Filter filter) {
        if (!hasSpatialFilter(filter)) {
            return filter;
        }

        SimpleFeatureType schema = getSchema();
        CoordinateReferenceSystem nativeCRS = schema.getCoordinateReferenceSystem();

        FilterFactory filterFactory =
                requireNonNull(getDataStore().getFilterFactory(), "DataStore must supply FilterFactory");

        // all right, we need to default the literals to the nativeCRS and then reproject to
        // the native one
        DefaultCRSFilterVisitor defaulter = new DefaultCRSFilterVisitor(filterFactory, nativeCRS);
        Filter defaulted = (Filter) filter.accept(defaulter, null);
        ReprojectingFilterVisitor reprojector = new ReprojectingFilterVisitor(filterFactory, schema);
        Filter reprojected = (Filter) defaulted.accept(reprojector, null);
        return reprojected;
    }

    private boolean hasSpatialFilter(Filter filter) {
        if (filter == null || filter == Filter.INCLUDE || filter == Filter.EXCLUDE) {
            return false;
        }
        SpatialFilterVisitor sfv = new SpatialFilterVisitor();
        filter.accept(sfv, null);
        return sfv.hasSpatialFilter();
    }

    protected CoordinateReferenceSystem tileMatrixsetCRS() throws IOException {
        String crsId = getMatrixSet().crsId();
        try {
            return CRS.decode(crsId, true);
        } catch (FactoryException e) {
            throw new IOException("Unable to parse crs id " + crsId, e);
        }
    }

    /**
     * Determines the optimal zoom level for a query based on resolution hints and layer constraints.
     *
     * <p>This method selects the best zoom level by considering (in priority order):
     *
     * <ol>
     *   <li><b>VectorTilesRequestScale:</b> If set via {@link VectorTilesRequestScale#set(double)}, the scale
     *       denominator is converted to resolution ({@code scale * 0.00028}) and used to find the best zoom level. This
     *       takes precedence over query hints.
     *   <li><b>Query resolution hints:</b> {@link Hints#GEOMETRY_GENERALIZATION} or {@link Hints#GEOMETRY_DISTANCE}
     *       from the query
     *   <li><b>Layer defaults:</b> Falls back to {@link VectorLayer#minZoom()} or the tile matrix set's minimum zoom
     *       level
     * </ol>
     *
     * <p>When a rendering query is detected (simplification hints present), the method also checks layer visibility at
     * the computed zoom level. If the layer is not visible at that zoom level (outside its min/max zoom range), an
     * empty result is returned, effectively filtering out the layer from the response.
     *
     * <p><b>Note:</b> {@link Hints#GEOMETRY_SIMPLIFICATION} is intentionally not advertised to force the renderer to
     * fall back to {@link Hints#GEOMETRY_DISTANCE}, which provides more consistent behavior with tiled vector data.
     *
     * @param query the query containing resolution hints
     * @return the selected zoom level, or empty if the layer is not visible at the computed scale
     * @see VectorTilesRequestScale
     */
    protected OptionalInt determineZoomLevel(Query query) {
        final OptionalDouble querySimplificationDistance = querySimplificationDistance(query);
        final boolean isRenderingQuery = querySimplificationDistance.isPresent();
        final OptionalDouble scaleDenominator = VectorTilesRequestScale.get();

        if (scaleDenominator.isPresent()) {
            double scale = scaleDenominator.getAsDouble();
            double resolution = scale * 0.00028;
            LOGGER.finer(() -> "scaleDenominator: %s, resolution: %f, query distance: %s"
                    .formatted(scaleDenominator, resolution, querySimplificationDistance));
            return determineZoomLevelForSimplificationDistance(query, resolution);
        }

        LOGGER.finer(() -> "query distance: %s".formatted(querySimplificationDistance));
        if (isRenderingQuery) {
            return determineZoomLevelForSimplificationDistance(query, querySimplificationDistance.getAsDouble());
        }

        if (layerMetadata.minZoom() != null) {
            return OptionalInt.of(layerMetadata.minZoom());
        }
        return OptionalInt.of(getMatrixSet().minZoomLevel());
    }

    private OptionalInt determineZoomLevelForSimplificationDistance(Query query, final double simplificationDistance) {

        final VectorTileStore tileStore = getTileStore();
        final TileMatrixSet matrixSet = getMatrixSet();

        final Strategy strategy = determineStrategy(query);

        final int matrixsetMinZoom = matrixSet.minZoomLevel();
        final int matrixsetMaxZoom = matrixSet.maxZoomLevel();

        final int bestZoom =
                tileStore.findBestZoomLevel(simplificationDistance, strategy, matrixsetMinZoom, matrixsetMaxZoom);

        final boolean layerIsVisible = layerIsVisibleAtZoomLevel(bestZoom);

        LOGGER.fine(() -> "layer %s, visible: %s, distance: %f, best zoom: %d [%d..%d], strategy: %s"
                .formatted(
                        layerMetadata.id(),
                        layerIsVisible,
                        simplificationDistance,
                        bestZoom,
                        layerMetadata.minZoom(),
                        layerMetadata.maxZoom(),
                        strategy));
        return layerIsVisible ? OptionalInt.of(bestZoom) : OptionalInt.empty();
    }

    private boolean layerIsVisibleAtZoomLevel(int z) {
        int minz = Optional.ofNullable(layerMetadata.minZoom())
                .orElse(getMatrixSet().minZoomLevel());
        int maxz = Optional.ofNullable(layerMetadata.maxZoom())
                .orElse(getMatrixSet().maxZoomLevel());

        return z >= minz && z <= maxz;
    }

    private TileStore.Strategy determineStrategy(Query query) {
        OptionalDouble querySimplificationDistance = querySimplificationDistance(query);
        return querySimplificationDistance.isPresent() ? TileStore.Strategy.SPEED : TileStore.Strategy.QUALITY;
    }

    private OptionalDouble querySimplificationDistance(Query query) {
        OptionalDouble generalizationDistance = getHint(GEOMETRY_GENERALIZATION, query);
        OptionalDouble geometryDistance = getHint(GEOMETRY_DISTANCE, query);
        if (generalizationDistance.isPresent()) {
            return generalizationDistance;
        }
        return geometryDistance;
    }

    private OptionalDouble getHint(Hints.Key key, Query query) {
        Object v = query.getHints().get(key);
        return v instanceof Number n ? OptionalDouble.of(n.doubleValue()) : OptionalDouble.empty();
    }

    protected Class<?> fieldTypeToBinding(String fieldType) {
        return switch (fieldType) {
            case "String" -> String.class;
            case "Boolean" -> Boolean.class;
            case "Number" -> Double.class;
            default -> throw new UnsupportedOperationException(
                    "Unable to map field type %s to a Class binding".formatted(fieldType));
        };
    }

    protected String dedupGeometryName(Set<String> attributeNames) {
        String geometryName = "the_geom";
        for (int i = 0; attributeNames.contains(geometryName); i++) {
            geometryName = "the_geom_" + i;
        }
        return geometryName;
    }

    private BoundingBox2D toExtent(Envelope env) {
        return new BoundingBox2D(env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY());
    }

    private BoundingBox2D tileMatrixsetFullExtent() {
        TileMatrixSet matrixSet = getMatrixSet();
        return matrixSet.boundingBox();
    }
}
