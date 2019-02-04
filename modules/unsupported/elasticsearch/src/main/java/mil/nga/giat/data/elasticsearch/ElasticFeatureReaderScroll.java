/*
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.data.FeatureReader;
import org.geotools.data.store.ContentState;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

class ElasticFeatureReaderScroll implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private final static Logger LOGGER = Logging.getLogger(ElasticFeatureReaderScroll.class);

    private final ContentState contentState;

    private final int maxFeatures;

    private String nextScrollId;

    private ElasticFeatureReader delegate;

    private int numFeatures;

    private boolean lastScroll;

    private final Set<String> scrollIds;

    public ElasticFeatureReaderScroll(ContentState contentState, ElasticResponse searchResponse, int maxFeatures) {
        this.contentState = contentState;
        this.maxFeatures = maxFeatures;
        this.numFeatures = 0;
        this.scrollIds = new HashSet<>();
        processResponse(searchResponse);
    }

    private void advanceScroll() throws IOException {
        final ElasticDataStore dataStore;
        dataStore = (ElasticDataStore) contentState.getEntry().getDataStore();
        processResponse(dataStore.getClient().scroll(nextScrollId, dataStore.getScrollTime()));
    }

    private void processResponse(ElasticResponse searchResponse) {
        final int numHits = searchResponse.getNumHits();
        final List<ElasticHit> hits;
        if (numFeatures+numHits <= maxFeatures) {
            hits = searchResponse.getResults().getHits();
        } else {
            final int n = maxFeatures-numFeatures;
            hits = searchResponse.getResults().getHits().subList(0,n);
        }
        delegate = new ElasticFeatureReader(contentState, hits, searchResponse.getAggregations(), 0);
        nextScrollId = searchResponse.getScrollId();
        lastScroll = numHits == 0 || numFeatures+hits.size()>=maxFeatures;
        LOGGER.fine("Scoll numHits=" + hits.size() + " (total=" + numFeatures+hits.size());
        scrollIds.add(nextScrollId);
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return delegate.getFeatureType();
    }

    @Override
    public SimpleFeature next() throws IOException {
        final SimpleFeature feature;
        if (hasNext()) {
            numFeatures++;
            feature = delegate.next();
        } else {
            throw new NoSuchElementException();
        }
        return feature;
    }

    @Override
    public boolean hasNext() throws IOException {
        if (!delegate.hasNext() && !lastScroll) {
            advanceScroll();
        }
        return (delegate.hasNext() || !lastScroll) && numFeatures<maxFeatures;
    }

    @Override
    public void close() throws IOException {
        if (!scrollIds.isEmpty()) {
            final ElasticDataStore dataStore;
            dataStore = (ElasticDataStore) contentState.getEntry().getDataStore();
            dataStore.getClient().clearScroll(scrollIds);
        }
        delegate.close();
    }

}
