/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.FeatureReader;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Provides access to a specific type within the Elasticsearch index described
 * by the associated data store.
 *
 */
public class ElasticFeatureSource extends ContentFeatureSource {

    private final static Logger LOGGER = Logging.getLogger(ElasticFeatureSource.class);

    private Boolean filterFullySupported;

    public ElasticFeatureSource(ContentEntry entry, Query query) throws IOException {
        super(entry, query);

        final ElasticDataStore dataStore = getDataStore();
        if (dataStore.getLayerConfigurations().get(entry.getName().getLocalPart()) == null) {
            final List<ElasticAttribute> attributes = dataStore.getElasticAttributes(entry.getName());
            final ElasticLayerConfiguration config = new ElasticLayerConfiguration(entry.getName().getLocalPart());
            config.getAttributes().addAll(attributes);
            dataStore.setLayerConfiguration(config);
        }
    }

    /**
     * Access parent datastore
     */
    public ElasticDataStore getDataStore() {
        return (ElasticDataStore) super.getDataStore();
    }

    /**
     * Implementation that generates the total bounds
     */
    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        LOGGER.fine("getBoundsInternal");
        final CoordinateReferenceSystem crs = getSchema().getCoordinateReferenceSystem();
        final ReferencedEnvelope bounds = new ReferencedEnvelope(crs);

        try (FeatureReader<SimpleFeatureType, SimpleFeature> featureReader = getReaderInternal(query)) {
            while (featureReader.hasNext()) {
                final SimpleFeature feature = featureReader.next();
                bounds.include(feature.getBounds());
            }
        }
        return bounds;
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        LOGGER.fine("getCountInternal");
        int hits = 0;
        final ElasticRequest searchRequest = prepareSearchRequest(query, false);
        try {
            if (!filterFullySupported) {
                try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReaderInternal(query)) {
                    while (reader.hasNext()) {
                        reader.next();
                        hits++;
                    }
                }
            } else {
                searchRequest.setSize(0);
                final ElasticDataStore dataStore = getDataStore();
                final String docType = dataStore.getDocType(entry.getName());
                final ElasticResponse sr = dataStore.getClient().search(dataStore.getIndexName(), docType, searchRequest);
                final int totalHits = (int) sr.getTotalNumHits();
                final int size = getSize(query);
                final int from = getStartIndex(query);
                hits = Math.max(0, Math.min(totalHits - from, size));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new IOException("Error executing count search", e);
        }

        return hits;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
        LOGGER.fine("getReaderInternal");
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = null;
        try {
            final ElasticDataStore dataStore = getDataStore();
            final String docType = dataStore.getDocType(entry.getName());
            final boolean scroll = !useSortOrPagination(query) && dataStore.getScrollEnabled();
            final ElasticRequest searchRequest = prepareSearchRequest(query, scroll);
            final ElasticResponse sr = dataStore.getClient().search(dataStore.getIndexName(), docType, searchRequest);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Search response: " + sr);
            }
            if (!scroll) {
                reader = new ElasticFeatureReader(getState(), sr);
            } else {
                reader = new ElasticFeatureReaderScroll(getState(), sr, getSize(query));
            }
            if (!filterFullySupported) {
                reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader, query.getFilter());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new IOException("Error executing query search", e);
        }
        return reader;
    }

    private ElasticRequest prepareSearchRequest(Query query, boolean scroll) throws IOException {
        String naturalSortOrder = SortOrder.ASCENDING.toSQL().toLowerCase();
        final ElasticRequest searchRequest = new ElasticRequest();
        final ElasticDataStore dataStore = getDataStore();
        final String docType = dataStore.getDocType(entry.getName());

        LOGGER.fine("Preparing " + docType + " (" + entry.getName() + ") query");
        if (!scroll) {
            if (query.getSortBy()!=null){
                for (final SortBy sort : query.getSortBy()) {
                    final String sortOrder = sort.getSortOrder().toSQL().toLowerCase();
                    if (sort.getPropertyName() != null) {
                        final String name = sort.getPropertyName().getPropertyName();
                        searchRequest.addSort(name, sortOrder);
                    } else {
                        naturalSortOrder = sortOrder;
                    }
                }
            }

            // pagination
            searchRequest.setSize(getSize(query));
            searchRequest.setFrom(getStartIndex(query));
        } else {
            if (dataStore.getScrollSize() != null) {
                searchRequest.setSize(dataStore.getScrollSize().intValue());
            }
            if (dataStore.getScrollTime() != null) {
                searchRequest.setScroll(dataStore.getScrollTime());
            }
        }

        if (dataStore.isSourceFilteringEnabled()) {
            if (query.getProperties() != Query.ALL_PROPERTIES) {
                for (String property : query.getPropertyNames()) {
                    searchRequest.addSourceInclude(property);
                }
            } else {
                // add source includes
                setSourceIncludes(searchRequest);
            }
        }

        // add query and post filter
        final FilterToElastic filterToElastic = new FilterToElastic();
        filterToElastic.setFeatureType(buildFeatureType());
        filterToElastic.encode(query);
        filterFullySupported = filterToElastic.getFullySupported();
        if (!filterFullySupported) {
            LOGGER.fine("Filter is not fully supported by native Elasticsearch."
                    + " Additional post-query filtering will be performed.");
        }
        final Map<String,Object> queryBuilder = filterToElastic.getQueryBuilder();

        final Map<String,Object> nativeQueryBuilder = filterToElastic.getNativeQueryBuilder();

        searchRequest.setQuery(queryBuilder);

        if (isSort(query) && nativeQueryBuilder.equals(ElasticConstants.MATCH_ALL)) {
            searchRequest.addSort("_uid", naturalSortOrder);
        }

        if (filterToElastic.getAggregations() != null) {
            final Map<String, Map<String, Map<String, Object>>> aggregations = filterToElastic.getAggregations();
            final Envelope envelope = (Envelope) query.getFilter().accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, null);
            final long gridSize;
            if (dataStore.getGridSize() != null) {
                gridSize = dataStore.getGridSize();
            } else {
                gridSize = (Long) ElasticDataStoreFactory.GRID_SIZE.getDefaultValue();
            }
            final double gridThreshold;
            if (dataStore.getGridThreshold() != null) {
                gridThreshold = dataStore.getGridThreshold();
            } else {
                gridThreshold = (Double) ElasticDataStoreFactory.GRID_THRESHOLD.getDefaultValue();
            }
            final int precision = GeohashUtil.computePrecision(envelope, gridSize, gridThreshold);
            LOGGER.fine("Updating GeoHash grid aggregation precision to " + precision);
            GeohashUtil.updateGridAggregationPrecision(aggregations, precision);
            searchRequest.setAggregations(aggregations);
            searchRequest.setSize(0);
        }

        return searchRequest;
    }

    private void setSourceIncludes(final ElasticRequest searchRequest) throws IOException {
        final ElasticDataStore dataStore = getDataStore();
        final List<ElasticAttribute> attributes = dataStore.getElasticAttributes(entry.getName());
        for (final ElasticAttribute attribute : attributes) {
            if (attribute.isUse() && attribute.isStored()) {
                searchRequest.addField(attribute.getName());
            } else if (attribute.isUse()) {
                searchRequest.addSourceInclude(attribute.getName());
            }
        }
    }

    private boolean isSort(Query query) {
        return query.getSortBy() != null && query.getSortBy().length > 0;
    }

    private boolean useSortOrPagination(Query query) {
        return (query.getSortBy() != null && query.getSortBy().length > 0) ||
                query.getStartIndex()!=null;
    }

    private int getSize(Query query) {
        final int size;
        if (!query.isMaxFeaturesUnlimited()) {
            size = query.getMaxFeatures();
        } else {
            size = getDataStore().getDefaultMaxFeatures();
            LOGGER.fine("Unlimited maxFeatures not supported. Using default: " + size);
        }
        return size;
    }

    private int getStartIndex(Query query) {
        final int from;
        if (query.getStartIndex() != null) {
            from = query.getStartIndex();
        } else {
            from = 0;
        }
        return from;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        final ElasticDataStore ds = getDataStore();
        final ElasticLayerConfiguration layerConfig;
        layerConfig = ds.getLayerConfigurations().get(entry.getTypeName());
        final List<ElasticAttribute> attributes;
        if (layerConfig != null) {
            attributes = layerConfig.getAttributes();
        } else {
            attributes = null;
        }

        final ElasticFeatureTypeBuilder typeBuilder;
        typeBuilder = new ElasticFeatureTypeBuilder(attributes, entry.getName());
        return typeBuilder.buildFeatureType();
    }

    @Override
    protected boolean canLimit() {
        return true;
    }

    @Override
    protected boolean canOffset() {
        return true;
    }

    @Override
    protected boolean canFilter() {
        return true;
    }

    @Override
    protected boolean canSort() {
        return true;
    }

}
