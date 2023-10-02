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
import java.util.Arrays;
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
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;

/** {@link DataStore} for a vector mosaic. */
public class VectorMosaicFeatureReader implements SimpleFeatureReader {
    static final Logger LOGGER = Logging.getLogger(VectorMosaicFeatureReader.class);
    private final SimpleFeatureCollection delegateCollection;
    private final Query query;
    private final VectorMosaicFeatureSource source;
    private final SimpleFeatureType schema;
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
        this.schema = retype(source.getSchema(), query);
        this.delegateIterator = delegateCollection.features();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return schema;
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
                rawGranule = (SimpleFeature) granuleIterator.next();
                Feature peek = mergeGranuleAndDelegate(rawGranule, delegateFeature, query);
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
                rawGranule = (SimpleFeature) granuleIterator.next();
                Feature peek = mergeGranuleAndDelegate(rawGranule, delegateFeature, query);
                if (query.getFilter().evaluate(peek)) {
                    nextGranule = peek;
                    return true;
                }
            }
        }

        // no more
        close();
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
            SimpleFeature peekGranule, SimpleFeature delegateFeature, Query query) {
        SimpleFeatureType mergedType = getFeatureType();
        List<Object> attributes = new ArrayList<>();
        peekGranule.getType().getAttributeDescriptors().stream()
                .filter(a -> isInRetype(a, query))
                .forEach(
                        attributeDescriptor -> {
                            attributes.add(peekGranule.getAttribute(attributeDescriptor.getName()));
                        }); // type starts with of all granule attributes
        delegateFeature.getType().getAttributeDescriptors().stream()
                .filter(source::isNotMandatoryIndexType)
                .filter(a -> isInRetype(a, query))
                .forEach(
                        attributeDescriptor -> {
                            attributes.add(
                                    delegateFeature.getAttribute(attributeDescriptor.getName()));
                        }); // then add all delegate attributes except mandatory index types

        return SimpleFeatureBuilder.build(
                mergedType, attributes, peekGranule.getIdentifier().getID());
    }

    private SimpleFeatureType retype(SimpleFeatureType mergedType, Query query) {
        if (query == null || query.getPropertyNames() == Query.ALL_NAMES) {
            return mergedType;
        }
        // Create a new SimpleFeatureTypeBuilder
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();

        typeBuilder.setName(mergedType.getName());
        typeBuilder.setNamespaceURI(mergedType.getName().getNamespaceURI());

        boolean geomAdded = false;
        for (int i = 0; i < mergedType.getAttributeCount(); i++) {
            String attributeName = mergedType.getDescriptor(i).getLocalName();
            if (contains(query.getPropertyNames(), attributeName)) {
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

    private boolean contains(String[] propertyNames, String attributeName) {
        for (String propertyName : propertyNames) {
            if (propertyName.equals(attributeName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInRetype(AttributeDescriptor a, Query query) {
        if (query.getPropertyNames() == Query.ALL_NAMES) {
            return true;
        }
        boolean inRetype = Arrays.asList(query.getPropertyNames()).contains(a.getLocalName());
        return inRetype;
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
