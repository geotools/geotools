/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import org.geotools.api.data.SimpleFeatureReader;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;

/**
 * Reads from a list of {@link FeatureBlockReader} backed by a {@link RandomAccessFile} and performs the classic
 * merge-sort algorithm
 *
 * @author Andrea Aime - GeoSolutions
 */
class MergeSortReader implements SimpleFeatureReader {

    // block readers ordered by their current feature. Blocks sit in the file in input order, and the
    // offset of a reader stays inside its block, so the offset breaks ties in input order
    PriorityQueue<FeatureBlockReader> readers;

    SimpleFeatureIO io;

    SimpleFeatureType schema;

    public MergeSortReader(
            SimpleFeatureType schema,
            SimpleFeatureIO io,
            List<FeatureBlockReader> blocks,
            Comparator<SimpleFeature> comparator)
            throws IOException {
        this.schema = schema;
        this.io = io;
        Comparator<FeatureBlockReader> byFeature = (a, b) -> comparator.compare(a.curr, b.curr);
        this.readers = new PriorityQueue<>(blocks.size(), byFeature.thenComparingLong(r -> r.offset));
        for (FeatureBlockReader block : blocks) {
            if (block.feature() != null) {
                readers.add(block);
            }
        }
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return schema;
    }

    @Override
    public SimpleFeature next() throws IOException, IllegalArgumentException, NoSuchElementException {
        FeatureBlockReader reader = readers.poll();
        if (reader == null) {
            throw new NoSuchElementException();
        }

        // return the smallest feature, put its block back with the next one
        SimpleFeature sf = reader.curr;
        if (reader.next() != null) {
            readers.add(reader);
        }
        return sf;
    }

    @Override
    public boolean hasNext() throws IOException {
        return !readers.isEmpty();
    }

    @Override
    public void close() throws IOException {
        io.close(true);
    }
}
