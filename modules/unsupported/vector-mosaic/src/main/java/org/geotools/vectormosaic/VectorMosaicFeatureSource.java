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

import static org.geotools.vectormosaic.VectorMosaicStore.buildName;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.Repository;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;

/** FeatureSource for a vector mosaic. */
public class VectorMosaicFeatureSource extends ContentFeatureSource {
    static final Logger LOGGER = Logging.getLogger(VectorMosaicFeatureSource.class);
    private final Repository repository;
    private final String delegateStoreTypeName;
    private final String delegateStoreName;
    private final String preferredSPI;
    private final String connectionParameterKey;
    protected FilterTracker filterTracker = new FilterTracker();
    protected Set<String> granuleTracker;

    /**
     * VectorMosaicFeatureSource constructor.
     *
     * @param contentEntry The content entry for the feature source.
     * @param store The data store for the feature source.
     */
    public VectorMosaicFeatureSource(ContentEntry contentEntry, VectorMosaicStore store) {
        super(contentEntry, null);
        delegateStoreTypeName =
                StringUtils.removeEnd(
                        contentEntry.getName().getLocalPart(),
                        VectorMosaicStore.MOSAIC_TYPE_SUFFIX);
        repository = store.getRepository();
        delegateStoreName = store.getDelegateStoreName();
        this.preferredSPI = store.getPreferredSPI();
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

    protected Filter getSplitFilter(
            Query query, DataStore dataStore, String typeName, boolean isDelegate) {
        Filter originalFilter = query.getFilter();
        try {
            SimpleFeatureType indexFeatureType = dataStore.getSchema(typeName);
            VectorMosaicPostPreFilterSplitter splitter =
                    new VectorMosaicPostPreFilterSplitter(indexFeatureType);

            if (isDelegate) {
                String source = indexFeatureType.getGeometryDescriptor().getLocalName();
                String target = buildFeatureType().getGeometryDescriptor().getLocalName();
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
     * Get the reference envelope for the feature source, maybe manually if featuresource does not
     * support direct query
     *
     * @param source the feature source
     * @param boundsQuery the query
     * @return the reference envelope
     * @throws IOException exception retrieving features to build the envelope
     */
    private ReferencedEnvelope getReferenceEnvelopeMaybeManually(
            FeatureSource source, Query boundsQuery) throws IOException {
        ReferencedEnvelope bounds = null;
        bounds = source.getBounds(boundsQuery);
        if (bounds == null) {
            bounds = new ReferencedEnvelope(source.getSchema().getCoordinateReferenceSystem());

            try (SimpleFeatureIterator features =
                    (SimpleFeatureIterator) source.getFeatures(boundsQuery).features(); ) {
                while (features.hasNext()) {
                    SimpleFeature feature = features.next();
                    bounds.expandToInclude((ReferencedEnvelope) feature.getBounds());
                }
            }
        }

        return bounds;
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        return -1;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        Name dsname = buildName(delegateStoreName);
        DataStore delegateDataStore = repository.dataStore(dsname);
        Filter splitFilterIndex =
                getSplitFilter(query, delegateDataStore, delegateStoreTypeName, true);
        SimpleFeatureSource delegateFeatureSource =
                delegateDataStore.getFeatureSource(delegateStoreTypeName);
        SimpleFeatureCollection features = delegateFeatureSource.getFeatures(splitFilterIndex);
        return new VectorMosaicFeatureReader(features, query, this);
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

    /**
     * Get the feature type for the granules.
     *
     * @return the feature type for the granules.
     * @throws IOException if the feature type can't be found.
     */
    protected SimpleFeatureType getGranuleType() throws IOException {
        Name dsname = buildName(delegateStoreName);
        DataStore delegateDataStore = repository.dataStore(dsname);
        SimpleFeatureSource delegateFeatureSource =
                delegateDataStore.getFeatureSource(delegateStoreTypeName);
        SimpleFeatureCollection features = delegateFeatureSource.getFeatures();
        try (SimpleFeatureIterator fi = features.features(); ) {
            SimpleFeature firstDelegateFeature = null;
            if (fi.hasNext()) {
                firstDelegateFeature = fi.next();
            }
            if (firstDelegateFeature == null) {
                throw new IOException("No index features found in " + delegateStoreName);
            }
            try (VectorMosaicGranule granule =
                    VectorMosaicGranule.fromDelegateFeature(firstDelegateFeature); ) {
                initGranule(granule, true);
                SimpleFeatureType granuleFeatureType = null;
                try {
                    granuleFeatureType =
                            granule.getDataStore().getSchema(granule.getGranuleTypeName());
                } catch (Exception e) {
                    LOGGER.log(
                            Level.WARNING,
                            "Could not get schema for " + granule.getGranuleTypeName(),
                            e);
                    throw new IOException(
                            "Could not get schema for " + granule.getGranuleTypeName(), e);
                }
                return granuleFeatureType;
            }
        }
    }

    /**
     * Builds a feature type for the target feature source by merging the index feature type with
     * the granule feature type
     *
     * @param indexFeatureType the index feature type
     * @param granuleFeatureType the granule feature type
     * @return the merged feature type
     * @throws IOException if feature type can't be built
     */
    protected SimpleFeatureType getFeatureType(
            SimpleFeatureType indexFeatureType, SimpleFeatureType granuleFeatureType)
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
        return (!VectorMosaicGranule.CONNECTION_PARAMETERS_DELEGATE_FIELD_DEFAULT.equals(name)
                && !(descriptor.getType() instanceof GeometryType)
                && (!name.equals(VectorMosaicGranule.GRANULE_ID_FIELD)));
    }

    /**
     * Initializes the granule by setting the data store and granule type name
     *
     * @param granule the granule
     * @throws IOException if data store can't be found
     */
    public void initGranule(VectorMosaicGranule granule, boolean isSampleForType)
            throws IOException {
        validateAndLoadConnectionStringProperties(granule);
        findDataStore(granule, isSampleForType);
        populateGranuleTypeName(granule);
    }

    /**
     * Validates and loads the connection string properties
     *
     * @param granule the granule
     * @throws IOException if connection string properties can't be loaded
     */
    private void populateGranuleTypeName(VectorMosaicGranule granule) throws IOException {
        if (granule.getGranuleTypeName() == null || granule.getGranuleTypeName().isEmpty()) {
            DataStore granuleDataStore = granule.getDataStore();
            if (granuleDataStore == null) {
                throw new IOException(
                        "Could not find data store for granule " + granule.getParams());
            }
            String[] typeNames = granuleDataStore.getTypeNames();
            if (typeNames.length > 0) {
                granule.setGranuleTypeName(typeNames[0]);
            } else {
                throw new IOException("No type names found in the granule data store");
            }
        }
    }

    /**
     * Validates and loads the connection string properties into the granule
     *
     * @param granule the granule to validate and load the properties into
     */
    private void validateAndLoadConnectionStringProperties(VectorMosaicGranule granule) {
        Properties connProps = new Properties();
        try {
            if (isURI(granule.getParams())) {
                connProps.put(
                        connectionParameterKey,
                        toConnectionParameter(connectionParameterKey, granule.getParams()));
            } else {
                connProps.load(new StringReader(granule.getParams()));
            }
        } catch (IOException e) {
            connProps.put(
                    connectionParameterKey,
                    toConnectionParameter(connectionParameterKey, granule.getParams()));
        }
        granule.setConnProperties(connProps);
    }

    /**
     * Checks if the given string is a valid URI, necessary because properties can delimit with
     * colons
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

    /**
     * * Finds the data store for the given granule
     *
     * @param granule the granule to find the data store for
     */
    private void findDataStore(VectorMosaicGranule granule, boolean isSampleForType) {
        VectorMosaicGranuleStoreFinder finder = null;
        if (granuleTracker == null) {
            finder = new VectorMosaicGranuleStoreFinderImpl(preferredSPI);
            finder.findDataStore(granule, isSampleForType);
        } else {
            finder = new VectorMosaicGranuleStoreFinderMonitoring(preferredSPI, granuleTracker);
            finder.findDataStore(granule, isSampleForType);
        }
    }
}
