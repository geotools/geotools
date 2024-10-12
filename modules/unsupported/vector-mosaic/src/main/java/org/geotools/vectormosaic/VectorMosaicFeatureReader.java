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
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureReader;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.util.logging.Logging;

/**
 * Feature reader for vector mosaics, it reads from the delegate and granule stores, merges the
 * features, applies the required filtering, and retypes the result to match the Query property
 * selection.
 */
class VectorMosaicFeatureReader implements SimpleFeatureReader {
    static final Logger LOGGER = Logging.getLogger(VectorMosaicFeatureReader.class);

    private final VectorMosaicFeatureSource source;
    private final SimpleFeatureType internalSchema;
    private final GranuleSourceProvider granuleSourceProvider;
    private final Query query;
    private SimpleFeatureType targetSchema;

    private SimpleFeatureBuilder builder;
    private SimpleFeatureBuilder retypeBuilder;
    FeatureIterator delegateIterator;
    FeatureIterator granuleIterator;

    protected SimpleFeature nextGranule = null;
    protected SimpleFeature delegateFeature = null;
    protected SimpleFeature rawGranule = null;

    /**
     * Constructor
     *
     * @param granuleSourceProvider provides the granule feature sources
     * @param vectorMosaicFeatureSource the source of the vector mosaic
     */
    public VectorMosaicFeatureReader(
            GranuleSourceProvider granuleSourceProvider, VectorMosaicFeatureSource vectorMosaicFeatureSource) {
        this.granuleSourceProvider = granuleSourceProvider;
        this.source = vectorMosaicFeatureSource;
        this.query = granuleSourceProvider.getQuery();
        this.targetSchema = this.internalSchema = retype(source.getSchema(), query);
        this.builder = new SimpleFeatureBuilder(internalSchema);

        // if the query is not using all attributes, we migth need to retype the result post merge
        if (query.getPropertyNames() != null) {
            this.targetSchema = SimpleFeatureTypeBuilder.retype(internalSchema, query.getPropertyNames());
            if (!internalSchema.equals(targetSchema)) this.retypeBuilder = new SimpleFeatureBuilder(targetSchema);
        }
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return targetSchema;
    }

    @Override
    public SimpleFeature next() throws IOException, IllegalArgumentException, NoSuchElementException {
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
    // Should only close the store when it is exhausted or when the reader is closed
    @SuppressWarnings("PMD.CloseResource")
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

        SimpleFeatureSource granuleSource = null;
        while ((granuleSource = granuleSourceProvider.getNextGranuleSource()) != null) {
            delegateFeature = granuleSourceProvider.getDelegateFeature();
            Query granuleQuery = granuleSourceProvider.getGranuleQuery();

            // gran the next granule iterator and look for the next granule
            granuleIterator = granuleSource.getFeatures(granuleQuery).features();
            while (granuleIterator.hasNext()) {
                nextGranule = readNext();
                if (nextGranule != null) return true;
            }
            if (nextGranule == null) {
                granuleIterator.close();
            }
        }

        // no more
        close();
        return false;
    }

    private SimpleFeature readNext() {
        rawGranule = (SimpleFeature) granuleIterator.next();
        SimpleFeature peek = mergeGranuleAndDelegate(rawGranule, delegateFeature);
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
    private SimpleFeature mergeGranuleAndDelegate(SimpleFeature peekGranule, SimpleFeature delegateFeature) {
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
            if (granuleIterator != null) {
                granuleIterator.close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to close granule iterator", e);
        }
        try {
            if (granuleSourceProvider != null) granuleSourceProvider.close();

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to close granule source provider", e);
        }
    }
}
