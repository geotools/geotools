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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * Feature reader for vector mosaics, it reads from the delegate and granule stores, merges the
 * features, applies the required filtering, and retypes the result to match the Query property
 * selection.
 */
class VectorMosaicFeatureReader implements SimpleFeatureReader {
    static final Logger LOGGER = Logging.getLogger(VectorMosaicFeatureReader.class);
    private final SimpleFeatureCollection delegateCollection;
    private final Query query;
    private final VectorMosaicFeatureSource source;
    private final SimpleFeatureType internalSchema;
    private SimpleFeatureType targetSchema;

    private SimpleFeatureBuilder builder;
    private SimpleFeatureBuilder retypeBuilder;
    FeatureIterator delegateIterator;
    FeatureIterator granuleIterator;
    DataStore granuleDataStore;
    protected Feature nextGranule = null;
    protected SimpleFeature delegateFeature = null;
    protected SimpleFeature rawGranule = null;

    String params;

    /**
     * Constructor
     *
     * @param delegateFeatures index features from the delegate store
     * @param query query to use to filter the index features and the granule features
     * @param vectorMosaicFeatureSource the source of the vector mosaic
     */
    public VectorMosaicFeatureReader(
            SimpleFeatureCollection delegateFeatures,
            Query query,
            VectorMosaicFeatureSource vectorMosaicFeatureSource) {
        this.delegateCollection = delegateFeatures;
        this.query = query;
        this.source = vectorMosaicFeatureSource;
        this.targetSchema = this.internalSchema = retype(source.getSchema(), query);
        this.builder = new SimpleFeatureBuilder(internalSchema);

        // if the query is not using all attributes, we migth need to retype the result post merge
        if (query.getPropertyNames() != null) {
            this.targetSchema =
                    SimpleFeatureTypeBuilder.retype(internalSchema, query.getPropertyNames());
            if (!internalSchema.equals(targetSchema))
                this.retypeBuilder = new SimpleFeatureBuilder(targetSchema);
        }

        this.delegateIterator = delegateCollection.features();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return targetSchema;
    }

    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        Feature f = null;
        if (hasNext()) {
            f = nextGranule;
            nextGranule = null;
            return (SimpleFeature) f;
        } else {
            throw new NoSuchElementException("No more features");
        }
    }

    @Override
    @SuppressWarnings(
            "PMD.CloseResource") // VectorMosaicGranule should only close the store when it is
    // exhausted or when the reader is closed
    public boolean hasNext() throws IOException {
        if (nextGranule != null) {
            return true;
        }
        if (granuleIterator != null && delegateFeature != null) {
            while (granuleIterator.hasNext()) {
                nextGranule = readNext();
                if (nextGranule != null) return true;
            }
        }

        // get the next delegate, because granule is exhausted or not initialized
        while (delegateIterator.hasNext()) {
            // close current before we move to next
            if (granuleIterator != null) {
                granuleIterator.close();
            }

            delegateFeature = (SimpleFeature) delegateIterator.next();
            VectorMosaicGranule granule = VectorMosaicGranule.fromDelegateFeature(delegateFeature);
            // if tracking granule statistics, this is how many times granules are accessed
            if (source.finder.granuleTracker != null) {
                source.finder.granuleTracker.incrementAccessCount();
            }
            if (params != null && params.equals(granule.getParams())) {
                // same connection, no need to reinitialize
                source.populateGranuleTypeName(granule, granuleDataStore);
            } else {
                // different connection, need to reinitialize
                if (granuleDataStore != null) {
                    granuleDataStore.dispose();
                }
                granuleDataStore = source.initGranule(granule, false);
                params = granule.getParams();
            }

            Filter granuleFilter =
                    source.getSplitFilter(
                            query, granuleDataStore, granule.getGranuleTypeName(), false);
            Query granuleQuery = new Query(granule.getGranuleTypeName(), granuleFilter);
            if (query.getPropertyNames() != Query.ALL_NAMES) {
                String[] filteredArray =
                        VectorMosaicFeatureSource.getOnlyTypeMatchingAttributes(
                                granuleDataStore, granule.getGranuleTypeName(), query, false);
                granuleQuery.setPropertyNames(filteredArray);
            }
            granuleIterator =
                    granuleDataStore
                            .getFeatureSource(granule.getGranuleTypeName())
                            .getFeatures(granuleQuery)
                            .features();

            while (granuleIterator.hasNext()) {
                nextGranule = readNext();
                if (nextGranule != null) return true;
            }
        }

        // no more
        close();
        return false;
    }

    private SimpleFeature readNext() {
        rawGranule = (SimpleFeature) granuleIterator.next();
        SimpleFeature peek = mergeGranuleAndDelegate(rawGranule, delegateFeature, query);
        if (query.getFilter().evaluate(peek)) {
            if (retypeBuilder != null) {
                return SimpleFeatureBuilder.retype(peek, retypeBuilder);
            } else {
                return peek;
            }
        }
        return null;
    }

    /**
     * Merge the granule feature with the delegate feature
     *
     * @param peekGranule the granule feature
     * @param delegateFeature the delegate feature
     * @return the merged feature
     */
    private SimpleFeature mergeGranuleAndDelegate(
            SimpleFeature peekGranule, SimpleFeature delegateFeature, Query query) {
        // collect attributes from the granule and the delegate, making no assumption
        // on the order, the query might have imposed one that does not match the
        // normal layout of the mosaic features
        peekGranule.getType().getAttributeDescriptors().stream()
                .filter(ad1 -> internalSchema.getDescriptor(ad1.getLocalName()) != null)
                .map(ad1 -> ad1.getLocalName())
                .forEach(name1 -> builder.set(name1, peekGranule.getAttribute(name1)));
        delegateFeature.getType().getAttributeDescriptors().stream()
                .filter(source::isNotMandatoryIndexType)
                .filter(ad -> internalSchema.getDescriptor(ad.getLocalName()) != null)
                .map(ad -> ad.getLocalName())
                .forEach(name -> builder.set(name, delegateFeature.getAttribute(name)));
        return builder.buildFeature(peekGranule.getID());
    }

    private SimpleFeatureType retype(SimpleFeatureType mergedType, Query query) {
        if (query == null || query.getPropertyNames() == Query.ALL_NAMES) {
            return mergedType;
        }
        // Create a new SimpleFeatureTypeBuilder
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();

        typeBuilder.setName(mergedType.getName());
        typeBuilder.setNamespaceURI(mergedType.getName().getNamespaceURI());

        // Collect the required attributes, and the filter attributes
        Set<String> propertyNames = new HashSet<>(Arrays.asList(query.getPropertyNames()));
        if (query.getFilter() != null) {
            FilterAttributeExtractor extractor = new FilterAttributeExtractor();
            query.getFilter().accept(extractor, null);
            propertyNames.addAll(extractor.getAttributeNameSet());
        }

        boolean geomAdded = false;
        for (int i = 0; i < mergedType.getAttributeCount(); i++) {
            String attributeName = mergedType.getDescriptor(i).getLocalName();
            if (propertyNames.contains(attributeName)) {
                typeBuilder.add(mergedType.getDescriptor(i));
                if (attributeName.equals(mergedType.getGeometryDescriptor().getLocalName())) {
                    geomAdded = true;
                }
            }
        }
        if (geomAdded) {
            typeBuilder.setDefaultGeometry(mergedType.getGeometryDescriptor().getLocalName());
            typeBuilder.setCRS(mergedType.getCoordinateReferenceSystem());
        }

        // Create the new feature type without the specified attributes
        return typeBuilder.buildFeatureType();
    }

    @Override
    public void close() throws IOException {
        try {
            if (delegateIterator != null) {
                delegateIterator.close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to close delegate iterator", e);
        }
        try {
            if (granuleIterator != null) {
                granuleIterator.close();
            }
        } catch (Exception e2) {
            LOGGER.log(Level.WARNING, "Failed to close granule iterator", e2);
        }
        if (granuleDataStore != null) {
            granuleDataStore.dispose();
        }
    }
}
