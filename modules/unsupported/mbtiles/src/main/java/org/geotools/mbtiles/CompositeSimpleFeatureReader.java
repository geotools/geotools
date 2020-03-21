/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbtiles;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import org.geotools.data.simple.SimpleFeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A reader providing features from a sequence of delagate readers provided by {@link
 * ReaderSupplier} instances (each one returning a single reader). The suppliers are used to allow
 * lazy instantiation of the delegate reader, since they might be holding onto scarce resources
 * (memory, connections, and the like)
 */
class CompositeSimpleFeatureReader implements SimpleFeatureReader {

    /**
     * Same as {@link Supplier}, but throwing IOException, and cannot return null.
     *
     * @param <T>
     */
    @FunctionalInterface
    public interface ReaderSupplier {

        @Nonnull
        SimpleFeatureReader get() throws IOException;
    }

    private final List<ReaderSupplier> readerSuppliers;
    private SimpleFeatureReader reader;
    private final SimpleFeatureType schema;

    public CompositeSimpleFeatureReader(
            SimpleFeatureType schema, List<ReaderSupplier> readerSuppliers) throws IOException {
        this.schema = schema;
        this.readerSuppliers = readerSuppliers;
        this.reader = readerSuppliers.remove(0).get();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return schema;
    }

    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return reader.next();
    }

    @Override
    public boolean hasNext() throws IOException {
        if (reader == null) return false;

        // move to the next reader if the current one is done
        while (!reader.hasNext() && !readerSuppliers.isEmpty()) {
            reader.close();
            reader = readerSuppliers.remove(0).get();
        }

        // at this point we either have features, or we exhausted all providers
        if (!reader.hasNext()) {
            reader.close();
            reader = null;
            return false;
        }

        return true;
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }
}
