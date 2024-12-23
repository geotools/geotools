package org.geotools.gce.imagemosaic;

import java.awt.RenderingHints;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;

/** Simple wrapper class used for tests, collects all the Query objects passed to it, and allows to inspect them */
class QueryCollectingFeatureSource implements SimpleFeatureSource {

    SimpleFeatureSource delegate;
    List<Query> queries;

    public QueryCollectingFeatureSource(SimpleFeatureSource delegate, List<Query> queries) {
        this.delegate = delegate;
        this.queries = queries;
    }

    @Override
    public Name getName() {
        return delegate.getName();
    }

    @Override
    public ResourceInfo getInfo() {
        return delegate.getInfo();
    }

    @Override
    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        return delegate.getDataStore();
    }

    @Override
    public QueryCapabilities getQueryCapabilities() {
        return delegate.getQueryCapabilities();
    }

    @Override
    public void addFeatureListener(FeatureListener listener) {
        delegate.addFeatureListener(listener);
    }

    @Override
    public void removeFeatureListener(FeatureListener listener) {
        delegate.removeFeatureListener(listener);
    }

    @Override
    public SimpleFeatureType getSchema() {
        return delegate.getSchema();
    }

    @Override
    public ReferencedEnvelope getBounds() throws IOException {
        return delegate.getBounds();
    }

    @Override
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return delegate.getBounds(query);
    }

    @Override
    public int getCount(Query query) throws IOException {
        queries.add(query);
        return delegate.getCount(query);
    }

    @Override
    public Set<RenderingHints.Key> getSupportedHints() {
        return delegate.getSupportedHints();
    }

    @Override
    public SimpleFeatureCollection getFeatures() throws IOException {
        return delegate.getFeatures();
    }

    @Override
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        return delegate.getFeatures(filter);
    }

    @Override
    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        queries.add(query);
        return delegate.getFeatures(query);
    }

    public List<Query> getQueries() {
        return queries;
    }
}
