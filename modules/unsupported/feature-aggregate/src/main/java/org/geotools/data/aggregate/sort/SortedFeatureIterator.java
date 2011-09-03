package org.geotools.data.aggregate.sort;

import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;

import org.geotools.data.simple.DelegateSimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.sort.SortBy;

public class SortedFeatureIterator implements SimpleFeatureIterator {

    FeatureReaderFeatureIterator delegate;

    /**
     * Checks if the schema and the sortBy are suitable for merge/sort. All attributes need to be
     * {@link Serializable}, all sorting attributes need to be {@link Comparable}
     * 
     * @param schema
     * @param sortBy
     * @return
     */
    public static final boolean canSort(SimpleFeatureType schema, SortBy[] sortBy) {
        return SortedFeatureReader.canSort(schema, sortBy);
    }

    public SortedFeatureIterator(SimpleFeatureIterator iterator, SimpleFeatureType schema,
            SortBy[] sortBy, int maxFeatures, int maxFiles) throws IOException {
        DelegateSimpleFeatureReader reader = new DelegateSimpleFeatureReader(schema, iterator);
        SimpleFeatureReader sorted = new SortedFeatureReader(reader, sortBy, maxFeatures, maxFiles);
        this.delegate = new FeatureReaderFeatureIterator(sorted);
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public SimpleFeature next() throws NoSuchElementException {
        return delegate.next();
    }

    @Override
    public void close() {
        delegate.close();

    }
}
