/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sort;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.geotools.data.Query;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.DelegateSimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.sort.SortBy;

class MergeSortDumper {

    static final boolean canSort(SimpleFeatureType schema, SortBy[] sortBy) {
        if (sortBy == SortBy.UNSORTED) {
            return true;
        }

        // check all attributes are serializable
        for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
            Class<?> binding = ad.getType().getBinding();
            if (!Serializable.class.isAssignableFrom(binding)) {
                return false;
            }
        }

        // check all sorting attributes are comparable
        for (SortBy sb : sortBy) {
            if (sb != SortBy.NATURAL_ORDER && sb != SortBy.REVERSE_ORDER) {
                AttributeDescriptor ad =
                        schema.getDescriptor(sb.getPropertyName().getPropertyName());
                if (ad == null) {
                    return false;
                }
                Class<?> binding = ad.getType().getBinding();
                if (!Comparable.class.isAssignableFrom(binding)
                        || Geometry.class.isAssignableFrom(binding)) {
                    return false;
                }
            }
        }

        return true;
    }

    static SimpleFeatureReader getDelegateReader(SimpleFeatureReader reader, Query query)
            throws IOException {
        int maxFeatures = getMaxFeatures(query);

        return getDelegateReader(reader, query.getSortBy(), maxFeatures);
    }

    /** Gets the max amount amount of features to keep in memory from the query and system hints */
    static int getMaxFeatures(Query query) {
        Hints hints = null;
        if (query != null) {
            hints = query.getHints();
        }
        int maxFeatures = 1000;
        if (hints != null && hints.get(Hints.MAX_MEMORY_SORT) != null) {
            maxFeatures = (Integer) hints.get(Hints.MAX_MEMORY_SORT);
        } else if (Hints.getSystemDefault(Hints.MAX_MEMORY_SORT) != null) {
            maxFeatures = (Integer) Hints.getSystemDefault(Hints.MAX_MEMORY_SORT);
        }
        return maxFeatures;
    }

    static SimpleFeatureReader getDelegateReader(
            SimpleFeatureReader reader, SortBy[] sortBy, int maxFeatures) throws IOException {
        if (maxFeatures < 0) {
            maxFeatures = getMaxFeatures(Query.ALL);
        }
        Comparator<SimpleFeature> comparator = SortedFeatureReader.getComparator(sortBy);

        // easy case, no sorting needed
        if (comparator == null) {
            return reader;
        }

        // double check
        SimpleFeatureType schema = reader.getFeatureType();
        if (!canSort(schema, sortBy)) {
            throw new IllegalArgumentException(
                    "The specified reader cannot be sorted, either the "
                            + "sorting properties are not comparable or the attributes are not serializable: "
                            + reader.getFeatureType().getTypeName()
                            + "\n "
                            + Arrays.toString(sortBy));
        }

        int count = 0;
        File file = null;
        SimpleFeatureIO io = null;
        List<SimpleFeature> features = new ArrayList<SimpleFeature>();
        List<FeatureBlockReader> readers = new ArrayList<FeatureBlockReader>();
        boolean cleanFile = true;
        try {
            // read and store into files as necessary
            while (reader.hasNext()) {
                SimpleFeature f = reader.next();
                features.add(f);
                count++;

                if (count > maxFeatures) {
                    Collections.sort(features, comparator);
                    if (io == null) {
                        file = File.createTempFile("sorted", ".features");
                        file.delete();
                        io = new SimpleFeatureIO(file, schema);
                    }
                    FeatureBlockReader fbr = storeToFile(io, features);
                    readers.add(fbr);
                    count = 0;
                    features.clear();
                }
            }
            // if we got to file storing, store residual features to file too
            if (count > 0 && io != null) {
                Collections.sort(features, comparator);
                file = File.createTempFile("sorted", ".features");
                file.delete();
                FeatureBlockReader fbr = storeToFile(io, features);
                readers.add(fbr);
            }

            // return the appropriate reader
            if (io == null) {
                // simple case, we managed to keep everything in memory, sort and return a
                // reader based on the collection contents
                Collections.sort(features, comparator);

                @SuppressWarnings("PMD.CloseResource") // returned in wrapper
                SimpleFeatureIterator fi = new ListFeatureCollection(schema, features).features();
                return new DelegateSimpleFeatureReader(schema, fi);
            } else {
                // go merge-sort
                cleanFile = false;
                return new MergeSortReader(schema, io, readers, comparator);
            }

        } finally {
            if (cleanFile && io != null) {
                io.close(true);
                file.delete();
            }

            reader.close();
        }
    }

    /** Writes the feature attributes to a binary file */
    static FeatureBlockReader storeToFile(SimpleFeatureIO io, List<SimpleFeature> features)
            throws IOException {
        long start = io.getOffset();

        // write each attribute in the random access file
        for (SimpleFeature sf : features) {
            io.write(sf);
        }

        return new FeatureBlockReader(io, start, features.size());
    }
}
