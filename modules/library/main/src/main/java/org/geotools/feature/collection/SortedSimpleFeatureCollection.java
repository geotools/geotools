package org.geotools.feature.collection;

import java.io.IOException;
import java.util.Iterator;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.sort.SortedFeatureIterator;
import org.geotools.data.store.FeatureIteratorIterator;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.sort.SortBy;

/**
 * A wrapper that will sort a feature collection using a size sensitive algorithm, in main memory
 * for small collections, using secondary memory otherwise. The threshold is defined by the
 * {@link Hints#MAX_MEMORY_SORT} feature count
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
public class SortedSimpleFeatureCollection extends DecoratingSimpleFeatureCollection {

    private SortBy[] sort;

    public SortedSimpleFeatureCollection(SimpleFeatureCollection delegate, SortBy[] sort) {
        super(delegate);
        this.sort = sort;
    }

    @Override
    public SimpleFeatureIterator features() {
        try {
            SimpleFeatureIterator features = ((SimpleFeatureCollection) delegate).features();
            // sort if necessary
            if (sort != null) {
                features = new SortedFeatureIterator(features, getSchema(), sort, -1);
            }
            return features;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator iterator() {
        return new FeatureIteratorIterator<SimpleFeature>(features());
    }

    @Override
    public void close(FeatureIterator<SimpleFeature> close) {
        close.close();
    }

    @Override
    public void close(Iterator<SimpleFeature> close) {
        FeatureIteratorIterator<SimpleFeature> fii = (FeatureIteratorIterator<SimpleFeature>) close;
        delegate.close(fii.getDelegate());
    }
}
