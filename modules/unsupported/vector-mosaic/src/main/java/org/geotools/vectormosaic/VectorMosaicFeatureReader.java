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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.logging.Logging;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;

/** {@link DataStore} for a vector mosaic. */
public class VectorMosaicFeatureReader implements SimpleFeatureReader {
    static final Logger LOGGER = Logging.getLogger(VectorMosaicFeatureReader.class);
    private final SimpleFeatureCollection delegateCollection;
    private final Query query;
    private final VectorMosaicFeatureSource source;
    private final SimpleFeatureType granuleFeatureType;
    FeatureIterator delegateIterator;
    FeatureIterator granuleIterator;
    DataStore granuleDataStore;
    private Feature nextGranule = null;
    SimpleFeature delegateFeature = null;

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
        delegateIterator = delegateCollection.features();
        try {
            granuleFeatureType = this.source.getGranuleType();
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to get granule feature type", e);
        }
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        try {
            return source.getFeatureType(delegateCollection.getSchema(), granuleFeatureType);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to get feature type", e);
            return null;
        }
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
        SimpleFeature peekGranule = null;
        if (granuleIterator != null && delegateFeature != null) {
            while (granuleIterator.hasNext()) {
                peekGranule = (SimpleFeature) granuleIterator.next();
                Feature peek = mergeGranuleAndDelegate(peekGranule, delegateFeature);
                if (query.getFilter().evaluate(peek)) {
                    nextGranule = peek;
                    return true;
                }
            }
        }

        // get the next delegate, because granule is exhausted or not initialized
        while (delegateIterator.hasNext()) {
            // close current before we move to next
            if (granuleIterator != null) {
                granuleIterator.close();
            }
            if (granuleDataStore != null) {
                granuleDataStore.dispose();
            }

            delegateFeature = (SimpleFeature) delegateIterator.next();
            VectorMosaicGranule granule = VectorMosaicGranule.fromDelegateFeature(delegateFeature);
            source.initGranule(granule, false);
            Filter granuleFilter =
                    source.getSplitFilter(
                            query, granule.getDataStore(), granule.getGranuleTypeName(), false);
            granuleDataStore = granule.getDataStore();
            granuleIterator =
                    granuleDataStore
                            .getFeatureSource(granule.getGranuleTypeName())
                            .getFeatures(granuleFilter)
                            .features();

            if (granuleIterator.hasNext()) {
                peekGranule = (SimpleFeature) granuleIterator.next();
                Feature peek = mergeGranuleAndDelegate(peekGranule, delegateFeature);
                if (query.getFilter().evaluate(peek)) {
                    nextGranule = peek;
                    return true;
                }
            }
        }

        // no more
        if (granuleIterator != null) {
            // close the last iterator
            granuleIterator.close();
        }
        if (granuleDataStore != null) {
            granuleDataStore.dispose();
        }
        return false;
    }

    /**
     * Merge the granule feature with the delegate feature
     *
     * @param peekGranule the granule feature
     * @param delegateFeature the delegate feature
     * @return the merged feature
     */
    private Feature mergeGranuleAndDelegate(
            SimpleFeature peekGranule, SimpleFeature delegateFeature) {
        SimpleFeatureType mergedType = getFeatureType();
        List<Object> attributes = new ArrayList<>();
        peekGranule.getType().getAttributeDescriptors().stream()
                .forEach(
                        attributeDescriptor -> {
                            attributes.add(peekGranule.getAttribute(attributeDescriptor.getName()));
                        }); // type starts with of all granule attributes
        delegateFeature.getType().getAttributeDescriptors().stream()
                .filter(source::isNotMandatoryIndexType)
                .forEach(
                        attributeDescriptor -> {
                            attributes.add(
                                    delegateFeature.getAttribute(attributeDescriptor.getName()));
                        }); // then add all delegate attributes except mandatory index types

        return SimpleFeatureBuilder.build(
                mergedType, attributes, peekGranule.getIdentifier().getID());
    }

    @Override
    public void close() throws IOException {
        if (delegateIterator != null) {
            delegateIterator.close();
        }
        if (granuleIterator != null) {
            granuleIterator.close();
        }
        if (granuleDataStore != null) {
            granuleDataStore.dispose();
        }
    }
}
