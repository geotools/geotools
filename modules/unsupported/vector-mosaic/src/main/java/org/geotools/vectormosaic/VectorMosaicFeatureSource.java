/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import static org.geotools.data.util.PropertiesTransformer.loadProperties;
import static org.geotools.vectormosaic.VectorMosaicGranule.GRANULE_CONFIG_FIELDS;
import static org.geotools.vectormosaic.VectorMosaicStore.buildName;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Strings;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.Repository;
import org.geotools.api.data.SimpleFeatureReader;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.data.DataUtilities;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.sort.SortedFeatureReader;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.store.ContentFeatureSource.ContentQueryCapabilities;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.FeatureAttributeVisitor;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.NearestVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;

/** FeatureSource for a vector mosaic. */
public class VectorMosaicFeatureSource extends ContentFeatureSource {
    static final Logger LOGGER = Logging.getLogger(VectorMosaicFeatureSource.class);

    /**
     * Regular expression to validate store name. Store name must not contain an equals sign, a forward slash, a period,
     * or a colon (e.g., it does not look like a property file, nor like a URI/URL).
     */
    private static final Pattern STORE_NAME_REGEX =
            Pattern.compile("^(?!.*=)(?!.*(?:\\/|\\.|\\w+:\\w+)).*$", Pattern.DOTALL);

    private final Repository repository;
    private final String delegateStoreTypeName;
    private final String delegateStoreName;
    private final String preferredSPI;
    private final String connectionParameterKey;
    private final Properties commonParameters;
    // included for cache testing purposes
    protected ContentState state;
    protected FilterTracker filterTracker = new FilterTracker();
    protected GranuleStoreFinder finder;

    /**
     * VectorMosaicFeatureSource constructor.
     *
     * @param contentEntry The content entry for the feature source.
     * @param store The data store for the feature source.
     */
    public VectorMosaicFeatureSource(ContentEntry contentEntry, VectorMosaicStore store) {
        super(contentEntry, null);
        delegateStoreTypeName =
                Strings.CS.removeEnd(contentEntry.getName().getLocalPart(), VectorMosaicStore.MOSAIC_TYPE_SUFFIX);
        repository = store.getRepository();
        delegateStoreName = store.getDelegateStoreName();
        this.preferredSPI = store.getPreferredSPI();
        this.commonParameters = store.getCommonParameters();
        finder = new GranuleStoreFinderImpl(preferredSPI, repository, commonParameters);
        this.connectionParameterKey = store.getConnectionParameterKey();
    }

    /**
     * Get the FeatureStore for the feature source.
     *
     * @return The FeatureStore for the feature source.
     */
    public VectorMosaicStore getStore() {
        return (VectorMosaicStore) getEntry().getDataStore();
    }

    protected Filter getSplitFilter(Query query, DataStore dataStore, String typeName, boolean isDelegate) {
        Filter originalFilter = query.getFilter();
        try {
            SimpleFeatureType indexFeatureType = dataStore.getSchema(typeName);
            VectorMosaicPostPreFilterSplitter splitter = new VectorMosaicPostPreFilterSplitter(indexFeatureType);

            if (isDelegate) {
                String source = indexFeatureType.getGeometryDescriptor().getLocalName();
                String target = getSchema().getGeometryDescriptor().getLocalName();
                @SuppressWarnings("ArgumentSelectionDefectChecker") // REVISIT: are target,source swapped?
                AttributeRenameVisitor renameVisitor = new AttributeRenameVisitor(target, source);
                Filter renamedFilter = (Filter) originalFilter.accept(renameVisitor, null);
                renamedFilter.accept(splitter, null);
                filterTracker.setDelegateFilter(splitter.getFilterPre());
            } else {
                originalFilter.accept(splitter, null);
                filterTracker.setGranuleFilter(splitter.getFilterPre());
            }
            return splitter.getFilterPre();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not get schema for " + typeName, e);
            return originalFilter;
        }
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        Name dsname = buildName(delegateStoreName);
        DataStore delegateDataStore = repository.dataStore(dsname);
        Filter splitFilter = getSplitFilter(query, delegateDataStore, delegateStoreTypeName, true);
        FeatureSource featureSource = delegateDataStore.getFeatureSource(delegateStoreTypeName);
        if (splitFilter != Filter.INCLUDE) {
            query.setFilter(splitFilter);
        }
        return getReferenceEnvelopeMaybeManually(featureSource, query);
    }

    /**
     * Get the reference envelope for the feature source, maybe manually if featuresource does not support direct query
     *
     * @param source the feature source
     * @param boundsQuery the query
     * @return the reference envelope
     * @throws IOException exception retrieving features to build the envelope
     */
    private ReferencedEnvelope getReferenceEnvelopeMaybeManually(FeatureSource source, Query boundsQuery)
            throws IOException {
        ReferencedEnvelope bounds = null;
        bounds = source.getBounds(boundsQuery);
        if (bounds == null) {
            bounds = new ReferencedEnvelope(source.getSchema().getCoordinateReferenceSystem());

            try (SimpleFeatureIterator features =
                    (SimpleFeatureIterator) source.getFeatures(boundsQuery).features()) {
                while (features.hasNext()) {
                    SimpleFeature feature = features.next();
                    bounds.expandToInclude((ReferencedEnvelope) feature.getBounds());
                }
            }
        }

        return bounds;
    }

    /**
     * Counts the matching granules by delegating to the underlying granule stores. The stores could potentially return
     * a mix of "-1" and actual counts, the current implementation assumes that the it's convenient to grab the count
     * forcefully when it's not available from the fast count, should still be better than counting all features after
     * they got merged with the delegate ones.
     *
     * <p>A take that a single "-1" would make this method also return "-1" could also be reasoable, we might want to
     * make the behavior configurable in the future. Currently most code really wants the actual count, not an estimate,
     * hence the current implementation.
     */
    @Override
    protected int getCountInternal(Query query) throws IOException {
        int count = 0;
        // ContentFeatureSource.getCount subtracts the offset afterwards, so count up to startIndex + maxFeatures
        long limit = SortedFeatureReader.getTopN(query);
        boolean applyLimit = limit != Long.MAX_VALUE;
        SimpleFeatureCollection features = getDelegateFeatures(query, List.of());
        try (GranuleSourceProvider provider = new GranuleSourceProvider(this, features, query)) {
            SimpleFeatureSource source;
            while ((source = provider.getNextGranuleSource()) != null) {
                // get the query, set it to count at most the leftover to the max
                Query granuleQuery = provider.getGranuleQuery();
                if (applyLimit) granuleQuery.setMaxFeatures((int) Math.min(limit - count, Integer.MAX_VALUE));

                // actual count
                int granuleCount = source.getCount(granuleQuery);
                if (granuleCount < 0)
                    granuleCount = source.getFeatures(granuleQuery).size();
                count += granuleCount;

                // early bail out, and for extra measure, we don't want to count more than the max
                if (applyLimit && count >= limit) {
                    count = (int) limit;
                    break;
                }
            }
        }

        return count;
    }

    /**
     * Reads the features of the mosaic in the order of the query:
     *
     * <ul>
     *   <li>no sort, or a lone NATURAL_ORDER: the index order, then the order of each granule;
     *   <li>a sort on index attributes only: the index store sorts the granules;
     *   <li>any other sort: the index store sorts the granules on the leading index attributes, and
     *       {@link GroupSortingReader} sorts each group of granules on the full sort. Without leading index attributes
     *       the whole mosaic is one group, and rejectFullScanSort rejects the query.
     * </ul>
     */
    @Override
    // original instance is null, no need to clase at reassignment
    @SuppressWarnings("PMD.CloseResource")
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
        SortBy[] sortBy = query.getSortBy();
        List<SortBy> indexSort = getIndexSortPrefix(query);
        boolean groupSort = isSorted(query) && !isNaturalOrder(sortBy) && indexSort.size() < sortBy.length;
        if (isSorted(query) && !isIndexedSort(query) && getStore().isRejectFullScanSort()) {
            // NATURAL_ORDER and REVERSE_ORDER have no property name: they sort on the feature identifier
            PropertyName firstKey = sortBy[0].getPropertyName();
            String sortAttribute = firstKey == null ? "the feature identifier" : firstKey.getPropertyName();
            throw new IOException("Cannot sort " + getSchema().getTypeName() + " on " + sortAttribute
                    + ": the first sort attribute must be one of the index attributes "
                    + new TreeSet<>(getIndexAttributeNames())
                    + ", because parameter rejectFullScanSort is enabled");
        }
        Query loadingQuery = groupSort ? addSortProperties(query) : query;
        if (groupSort && !SortedFeatureReader.canSort(getLoadedType(loadingQuery), query.getSortBy())) {
            List<String> keys = Arrays.stream(query.getSortBy())
                    .map(sb -> sb.getPropertyName() == null
                            ? sb.toString()
                            : sb.getPropertyName().getPropertyName())
                    .toList();
            throw new IllegalArgumentException("Cannot sort " + getSchema().getTypeName() + " on " + keys
                    + ": the sort attributes must be comparable and the loaded attributes serializable");
        }
        SimpleFeatureCollection features = getDelegateFeatures(loadingQuery, indexSort);

        // The reader merges the features from delegate and granule, filters contents, and
        // retypes to the target feature type
        GranuleSourceProvider granuleSources =
                new GranuleSourceProvider(this, features, loadingQuery, groupSort ? indexSort : List.of());
        SimpleFeatureReader reader = new VectorMosaicFeatureReader(granuleSources, this);
        if (groupSort) {
            reader = new GroupSortingReader(reader, loadingQuery, granuleSources);
        }

        // drop the attributes loaded only for sorting
        if (loadingQuery != query) {
            SimpleFeatureType target =
                    SimpleFeatureTypeBuilder.retype(reader.getFeatureType(), query.getPropertyNames());
            return new ReTypeFeatureReader(reader, target, false);
        }
        return reader;
    }

    /** The mosaic sorts every query itself, see {@link #getReaderInternal} */
    @Override
    protected boolean canSort(Query query) {
        return true;
    }

    /**
     * Returns true if the mosaic sorts the query without reading every granule:
     *
     * <ul>
     *   <li>a first sort key that is an index attribute. The index store sorts the granules on the leading index
     *       attributes, so the reader opens them in order and stops once it has enough features. The keys after them
     *       sort the features inside each group of granules, see {@link GroupSortingReader};
     *   <li>a lone NATURAL_ORDER. The reader returns the index rows and the features of each granule in their natural
     *       order, see {@link GranuleSourceProvider#getGranuleFeatures}.
     * </ul>
     */
    private boolean isIndexedSort(Query query) {
        return isNaturalOrder(query.getSortBy()) || !getIndexSortPrefix(query).isEmpty();
    }

    /** With rejectFullScanSort set, the capabilities report only the sorts the mosaic does itself */
    @Override
    public QueryCapabilities getQueryCapabilities() {
        QueryCapabilities capabilities = super.getQueryCapabilities();
        if (!getStore().isRejectFullScanSort()) {
            return capabilities;
        }
        return new ContentQueryCapabilities() {
            @Override
            public boolean supportsSorting(SortBy... sortAttributes) {
                if (sortAttributes == null || sortAttributes.length == 0) {
                    return true;
                }
                Query query = new Query();
                query.setSortBy(sortAttributes);
                return isIndexedSort(query) && capabilities.supportsSorting(sortAttributes);
            }
        };
    }

    private static boolean isSorted(Query query) {
        return query.getSortBy() != null && query.getSortBy().length > 0;
    }

    /** Returns the leading sort keys the index can sort on */
    private List<SortBy> getIndexSortPrefix(Query query) {
        if (!isSorted(query)) {
            return List.of();
        }
        SortBy[] sortBy = query.getSortBy();
        if (isNaturalOrder(sortBy)) {
            // without native support the index store returns its own order
            return isNativeIndexSort(sortBy) ? List.of(sortBy) : List.of();
        }
        Set<String> indexAttributes = getIndexAttributeNames();
        return Arrays.stream(sortBy)
                .takeWhile(sb -> isIndexAttribute(sb, indexAttributes))
                .toList();
    }

    /** Returns true for a lone NATURAL_ORDER */
    static boolean isNaturalOrder(SortBy[] sortBy) {
        return sortBy != null && sortBy.length == 1 && sortBy[0] == SortBy.NATURAL_ORDER;
    }

    /**
     * Returns true if the source sorts on the given keys itself. JDBC stores accept every sort, but throw on the
     * natural order for a table without a primary key.
     */
    static boolean isNativeSort(SimpleFeatureSource source, SortBy... sortBy) {
        return source.getQueryCapabilities() instanceof ContentQueryCapabilities capabilities
                && capabilities.getStoreCapabilities().supportsSorting(sortBy);
    }

    private boolean isNativeIndexSort(SortBy... sortBy) {
        try {
            return isNativeSort(getDataStoreByName(delegateStoreName).getFeatureSource(delegateStoreTypeName), sortBy);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not get the index source " + delegateStoreTypeName, e);
        }
    }

    private static boolean isIndexAttribute(SortBy sortBy, Set<String> indexAttributes) {
        PropertyName property = sortBy.getPropertyName();
        return property != null && indexAttributes.contains(property.getPropertyName());
    }

    /** Returns the index attributes the mosaic exposes */
    Set<String> getIndexAttributeNames() {
        try {
            SimpleFeatureType indexType = getDataStoreByName(delegateStoreName).getSchema(delegateStoreTypeName);
            return indexType.getAttributeDescriptors().stream()
                    .filter(this::isNotMandatoryIndexType)
                    .map(AttributeDescriptor::getLocalName)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new UncheckedIOException("Could not get schema for " + delegateStoreTypeName, e);
        }
    }

    /** Returns the type of the features the reader loads for the query */
    private SimpleFeatureType getLoadedType(Query query) throws IOException {
        if (query.getPropertyNames() == Query.ALL_NAMES) {
            return getSchema();
        }
        return SimpleFeatureTypeBuilder.retype(getSchema(), query.getPropertyNames());
    }

    /** Adds the sort attributes to the selected properties. The reader needs them to sort. */
    private static Query addSortProperties(Query query) {
        if (query.getPropertyNames() == Query.ALL_NAMES) {
            return query;
        }
        Set<String> properties = new LinkedHashSet<>(Arrays.asList(query.getPropertyNames()));
        for (SortBy sb : query.getSortBy()) {
            if (sb.getPropertyName() != null) {
                properties.add(sb.getPropertyName().getPropertyName());
            }
        }
        if (properties.size() == query.getPropertyNames().length) {
            return query;
        }
        Query loadingQuery = new Query(query);
        loadingQuery.setPropertyNames(properties.toArray(String[]::new));
        return loadingQuery;
    }

    private SimpleFeatureCollection getDelegateFeatures(Query query, List<SortBy> indexSort) throws IOException {
        Name dsname = buildName(delegateStoreName);
        DataStore delegateDataStore = repository.dataStore(dsname);
        Filter splitFilterIndex = getSplitFilter(query, delegateDataStore, delegateStoreTypeName, true);
        SimpleFeatureSource delegateFeatureSource = delegateDataStore.getFeatureSource(delegateStoreTypeName);
        Query delegateQuery = new Query(delegateStoreTypeName, splitFilterIndex);
        // Granules with equal index values come in the order the index store returns them. A database can change
        // that order between requests, so pages of a sorted result can repeat or skip features.
        // Do not add NATURAL_ORDER as a tie-breaker: JDBC stores throw on index tables without a primary key.
        delegateQuery.setSortBy(indexSort.toArray(SortBy[]::new));
        if (query.getPropertyNames() != Query.ALL_NAMES) {
            String[] filteredArray =
                    getOnlyTypeMatchingAttributes(delegateDataStore, delegateStoreTypeName, query, true);
            delegateQuery.setPropertyNames(filteredArray);
        }
        SimpleFeatureCollection features = delegateFeatureSource.getFeatures(delegateQuery);
        return features;
    }

    static String[] getOnlyTypeMatchingAttributes(DataStore typeStore, String typeName, Query query, boolean isDelegate)
            throws IOException {
        SimpleFeatureType featureType = typeStore.getSchema(typeName);
        Set<String> attributeNames = VectorMosaicFeatureSource.getAttributeNamesForType(featureType);
        Set<String> queryNames = new LinkedHashSet<>(Arrays.asList(query.getPropertyNames()));
        if (query.getFilter() != null) {
            FilterAttributeExtractor extractor = new FilterAttributeExtractor(featureType);
            query.getFilter().accept(extractor, null);
            queryNames.addAll(Arrays.asList(extractor.getAttributeNames()));
        }
        List<String> filteredAttributes =
                queryNames.stream().filter(attributeNames::contains).collect(Collectors.toList());
        // delegate must have the extra granule configuration fields
        if (isDelegate) {
            for (String field : GRANULE_CONFIG_FIELDS) {
                // some of the config fields are optional, were they included in the first place?
                if (attributeNames.contains(field) && !filteredAttributes.contains(field)) {
                    filteredAttributes.add(field);
                }
            }
        }

        return filteredAttributes.toArray(n -> new String[n]);
    }

    /**
     * Uses first granule to build a schema for the target feature type
     *
     * @return the schema
     * @throws IOException If feature type can't be built or found
     */
    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {

        Name dsname = buildName(delegateStoreName);
        DataStore delegateDataStore = repository.dataStore(dsname);
        SimpleFeatureType indexFeatureType = delegateDataStore.getSchema(delegateStoreTypeName);
        SimpleFeatureType granuleFeatureType = getGranuleType();
        return getFeatureType(indexFeatureType, granuleFeatureType);
    }

    @Override
    public ContentState getState() {
        if (state != null) {
            return state;
        } else {
            return super.getState();
        }
    }

    /**
     * Get the feature type for the granules.
     *
     * @return the feature type for the granules.
     * @throws IOException if the feature type can't be found.
     */
    protected SimpleFeatureType getGranuleType() throws IOException {
        VectorMosaicState state = (VectorMosaicState) getState();
        // caching should be done at the data store level!
        if (state.getGranuleFeatureType() != null) return state.getGranuleFeatureType();

        Name dsname = buildName(delegateStoreName);
        DataStore delegateDataStore = repository.dataStore(dsname);
        SimpleFeatureSource delegateFeatureSource = delegateDataStore.getFeatureSource(delegateStoreTypeName);
        Query q = new Query(delegateFeatureSource.getSchema().getTypeName());
        q.setMaxFeatures(1);
        SimpleFeatureCollection features = delegateFeatureSource.getFeatures(q);
        SimpleFeature firstDelegateFeature = DataUtilities.first(features);
        if (firstDelegateFeature == null) {
            throw new IOException("No index features found in " + delegateStoreName);
        }
        VectorMosaicGranule granule = VectorMosaicGranule.fromDelegateFeature(firstDelegateFeature);
        DataStore granuleDataStore = initGranule(granule, true);
        SimpleFeatureType granuleFeatureType = null;
        try {
            granuleFeatureType = granuleDataStore.getSchema(granule.getGranuleTypeName());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not get schema for " + granule.getGranuleTypeName(), e);
            throw new IOException("Could not get schema for " + granule.getGranuleTypeName(), e);
        } finally {
            if (granuleDataStore != null) {
                granuleDataStore.dispose();
            }
        }
        state.setGranuleFeatureType(granuleFeatureType);
        return granuleFeatureType;
    }

    /**
     * Builds a feature type for the target feature source by merging the index feature type with the granule feature
     * type
     *
     * @param indexFeatureType the index feature type
     * @param granuleFeatureType the granule feature type
     * @return the merged feature type
     * @throws IOException if feature type can't be built
     */
    protected SimpleFeatureType getFeatureType(SimpleFeatureType indexFeatureType, SimpleFeatureType granuleFeatureType)
            throws IOException {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.init(granuleFeatureType);
        tb.setName(delegateStoreTypeName + VectorMosaicStore.MOSAIC_TYPE_SUFFIX);
        tb.setNamespaceURI(getStore().getNamespaceURI());
        for (AttributeDescriptor descriptor : indexFeatureType.getAttributeDescriptors()) {
            if (isNotMandatoryIndexType(descriptor)) {
                tb.add(descriptor);
            }
        }
        schema = tb.buildFeatureType();
        return schema;
    }

    /**
     * Checks if the attribute descriptor is not a mandatory index type
     *
     * @param descriptor the attribute descriptor
     * @return true if not mandatory index type
     */
    protected boolean isNotMandatoryIndexType(AttributeDescriptor descriptor) {
        String name = descriptor.getLocalName();
        return !VectorMosaicGranule.CONNECTION_PARAMETERS_DELEGATE_FIELD_DEFAULT.equals(name)
                && !VectorMosaicGranule.GRANULE_TYPE_NAME.equals(name)
                && !VectorMosaicGranule.GRANULE_FILTER.equals(name)
                && !(descriptor.getType() instanceof GeometryType)
                && !name.equals(VectorMosaicGranule.GRANULE_ID_FIELD);
    }

    /**
     * Initializes the granule by setting the data store and granule type name
     *
     * @param granule the granule
     * @throws IOException if data store can't be found
     */
    public DataStore initGranule(VectorMosaicGranule granule, boolean isSampleForType) throws IOException {
        DataStore dataStore = null;
        setupGranuleStore(granule);
        Optional<DataStore> dataStoreOptional = finder.findDataStore(granule, isSampleForType);
        dataStore = dataStoreOptional.orElseThrow(() -> new IOException("No data store found for granule"));
        populateGranuleTypeName(granule, dataStore);
        return dataStore;
    }

    private DataStore getDataStoreByName(String typeName) {
        Name dsname = buildName(typeName);
        return repository.dataStore(dsname);
    }

    private Set<String> getAttributeNamesForType(String storeName, String typeName) throws IOException {
        DataStore dataStore = getDataStoreByName(storeName);
        SimpleFeatureType featureType = dataStore.getSchema(typeName);
        return getAttributeNamesForType(featureType);
    }

    public static Set<String> getAttributeNamesForType(SimpleFeatureType featureType) throws IOException {
        return featureType.getAttributeDescriptors().stream()
                .map(AttributeDescriptor::getLocalName)
                .collect(Collectors.toSet());
    }

    private boolean allFilterAttributesAreInType(String storeName, String typeName, Filter filter) throws IOException {
        Set<String> indexAttributeNames = getAttributeNamesForType(storeName, typeName);
        String[] filterAttributeNames = DataUtilities.attributeNames(filter);
        return indexAttributeNames.containsAll(Arrays.asList(filterAttributeNames));
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        // check if visitor is aggregate visitor
        if (visitor instanceof MaxVisitor
                || visitor instanceof MinVisitor
                || visitor instanceof UniqueVisitor
                || visitor instanceof NearestVisitor) {
            Set<String> indexAttributeNames = getAttributeNamesForType(delegateStoreName, delegateStoreTypeName);
            // check if all filter attributes are in index/delegate
            if (query.getFilter() != null) {
                if (!allFilterAttributesAreInType(delegateStoreName, delegateStoreTypeName, query.getFilter())) {
                    return false;
                }
            }
            // check if all visitor expressions are covered by index/delegate
            List<String> visitorExpressionFields = ((FeatureAttributeVisitor) visitor)
                    .getExpressions().stream()
                            .map(DataUtilities::attributeNames)
                            .flatMap(Arrays::stream)
                            .collect(Collectors.toList());

            if (!indexAttributeNames.containsAll(visitorExpressionFields)) {
                return false;
            }

            // this avoids query properties being set with granule attributes
            query.setPropertyNames(visitorExpressionFields);
            // visitor is aggregate type and all attributes are in index/delegate
            DataStore delegateDataStore = getDataStoreByName(delegateStoreName);
            delegateDataStore
                    .getFeatureSource(delegateStoreTypeName)
                    .getFeatures(query)
                    .accepts(visitor, null);
            return true;
        }

        return false;
    }

    /**
     * Validates and loads the connection string properties
     *
     * @param granule the granule
     * @throws IOException if connection string properties can't be loaded
     */
    protected void populateGranuleTypeName(VectorMosaicGranule granule, DataStore dataStore) throws IOException {
        if (granule.getGranuleTypeName() == null || granule.getGranuleTypeName().isEmpty()) {
            String[] typeNames = dataStore.getTypeNames();
            if (typeNames.length > 0) {
                granule.setGranuleTypeName(typeNames[0]);
            } else {
                throw new IOException("No type names found in the granule data store");
            }
        }
    }

    @Override
    protected boolean canRetype(Query query) {
        return true;
    }

    @Override
    protected boolean canFilter(Query query) {
        return true;
    }

    /**
     * Validates and loads the connection string properties into the granule
     *
     * @param granule the granule to validate and load the properties into
     */
    private void setupGranuleStore(VectorMosaicGranule granule) {
        Properties connProps = new Properties();
        String params = granule.getParams();
        if (STORE_NAME_REGEX.matcher(params).matches()) {
            granule.setStoreName(params);
            return;
        }
        try {
            if (isURI(params)) {
                connProps.put(connectionParameterKey, toConnectionParameter(connectionParameterKey, params));
            } else {
                loadProperties(params, connProps);
            }
        } catch (IOException e) {
            connProps.put(connectionParameterKey, toConnectionParameter(connectionParameterKey, granule.getParams()));
        }
        if (commonParameters != null && !commonParameters.isEmpty()) {
            connProps.putAll(commonParameters);
        }
        granule.setConnProperties(connProps);
    }

    /**
     * Checks if the given string is a valid URI, necessary because properties can delimit with colons
     *
     * @param connectionString the string to check
     * @return true if the string is a valid URI
     */
    private boolean isURI(String connectionString) {
        return connectionString.startsWith("file:")
                || connectionString.startsWith("http:")
                || connectionString.startsWith("https:");
    }

    /**
     * Converts connection string to connection parameter of the correct type
     *
     * @param connectionParameterKey the key to use for the connection parameter
     * @param params the connection string
     * @return the connection parameter with the correct type
     */
    private Object toConnectionParameter(String connectionParameterKey, String params) {
        if (connectionParameterKey.equals(VectorMosaicStore.CONNECTION_PARAMETER_KEY_URL)) {
            try {
                return new URL(params);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Invalid connection parameter", e);
            }
        } else if (connectionParameterKey.equals(VectorMosaicStore.CONNECTION_PARAMETER_KEY_FILE)) {
            return new File(params);
        } else {
            return params;
        }
    }
}
