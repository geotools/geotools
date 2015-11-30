package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.geotools.data.FeatureReader;
import org.geotools.data.store.ContentState;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class ElasticFeatureReaderScroll implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    
    private final static Logger LOGGER = Logging.getLogger(ElasticFeatureReaderScroll.class);
    
    private final ContentState contentState;
    
    private final int maxFeatures;
    
    private String nextScrollId;
    
    private ElasticFeatureReader delegate;
    
    private int numFeatures;
    
    private boolean lastScroll;
    
    public ElasticFeatureReaderScroll(ContentState contentState, String scrollId, int maxFeatures) {
        this.contentState = contentState;
        this.nextScrollId = scrollId;
        this.maxFeatures = maxFeatures;
        this.numFeatures = 0;
        advanceScroll();
    }
    
    private void advanceScroll() {
        final ElasticDataStore dataStore;
        dataStore = (ElasticDataStore) contentState.getEntry().getDataStore();
        final SearchResponse searchResponse = dataStore.getClient()
                .prepareSearchScroll(nextScrollId)
                .setScroll(TimeValue.timeValueSeconds(dataStore.getScrollTime()))
                .execute().actionGet();
        final int numHits = searchResponse.getHits().hits().length;
        final List<SearchHit> hits;
        if (numFeatures+numHits <= maxFeatures) {
            hits = Arrays.asList(searchResponse.getHits().hits());
        } else {
            final int n = maxFeatures-numFeatures;
            hits = Arrays.asList(searchResponse.getHits().hits()).subList(0,n);
        }
        delegate = new ElasticFeatureReader(contentState, hits.iterator());
        nextScrollId = searchResponse.getScrollId();
        lastScroll = numHits == 0 || numFeatures+hits.size()>=maxFeatures;
        LOGGER.fine("Scoll numHits=" + hits.size() + " (total=" + numFeatures+hits.size());
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
        delegate.close();
    }

}
