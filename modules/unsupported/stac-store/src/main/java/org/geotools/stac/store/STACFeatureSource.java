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
package org.geotools.stac.store;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.SimpleFeatureReader;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.filter.sort.SortOrder;
import org.geotools.data.DataUtilities;
import org.geotools.data.MaxFeatureReader;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.geojson.PagingFeatureCollection;
import org.geotools.data.simple.FilteringSimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.stac.client.STACClient;
import org.geotools.stac.client.SearchQuery;
import org.geotools.util.factory.Hints;
import tools.jackson.databind.node.ObjectNode;

public class STACFeatureSource extends ContentFeatureSource {

    static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();
    static final String ASSETS = "assets";

    private final STACClient client;
    private final SearchQueryBuilder queryBuilder;

    public STACFeatureSource(ContentEntry entry, STACClient client) {
        super(entry, Query.ALL);
        this.client = client;
        this.queryBuilder = new SearchQueryBuilder(getSchema(), getDataStore());
    }

    @Override
    public STACDataStore getDataStore() {
        return (STACDataStore) super.getDataStore();
    }

    @Override
    protected QueryCapabilities buildQueryCapabilities() {
        return new QueryCapabilities() {

            @Override
            public boolean isOffsetSupported() {
                return false;
            }

            @Override
            public boolean supportsSorting(SortBy... sortAttributes) {
                return false; // STACConformance.SORT.matches(client.getLandingPage().getConformance());
            }
        };
    }

    @Override
    protected void addHints(Set<Hints.Key> hints) {
        hints.add(Hints.FEATURE_DETACHED);
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        // the STAC API does not offer a fast way to retrieve bounds based on a query,
        // it only reports the bounds of a full collection
        if (!query.equals(Query.ALL)) return null;

        String typeName = getCollectionId(query);
        // maybe we should we have a way to retrieve a single collection
        return client.getCollections().stream()
                .filter(c -> typeName.equals(c.getId()))
                .map(c -> c.getBounds())
                .findFirst()
                .orElse(null);
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        Pair<SearchQuery, Filter> pair = queryBuilder.toSearchQuery(query, true);
        // if the query cannot be fully translated, we cannot do an efficient count
        if (pair == null) return -1;

        Integer fc = getCountFromClient(pair.getKey());
        if (fc != null) {
            int hardLimit = getDataStore().getHardLimit();
            if (hardLimit > 0) return Math.min(hardLimit, fc);
            else return fc;
        }

        // no fast way to count, give up, most catalogs have too many features
        return -1;
    }

    private Integer getCountFromClient(SearchQuery searchQuery) throws IOException {
        // try to get the "matched" header from the GeoJSON collection, if available
        SimpleFeatureCollection fc = client.search(searchQuery, getDataStore().getSearchMode());
        // if there was a "next" link, the reader returns a paging collection
        if (fc instanceof PagingFeatureCollection collection) {
            return Optional.ofNullable(collection.getMatched()).orElse(-1);
        }
        // otherwise everything is in memory
        if (fc instanceof ListFeatureCollection) {
            return fc.size();
        }
        return null;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        // used during construction, cannot rely on the query builder field
        Query q = new Query();
        q.setTypeName(entry.getTypeName());
        SearchQueryBuilder builder = new SearchQueryBuilder(null, getDataStore());
        SearchQuery sq = builder.toSearchQuery(q, false).getKey();

        sq.setLimit(getDataStore().getFeatureTypeItems());
        SimpleFeatureCollection fc = client.search(sq, getDataStore().getSearchMode());
        SimpleFeatureType rawSchema = fc.getSchema();

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder(new STACFeatureTypeFactoryImpl());
        tb.init(rawSchema);
        tb.add(ASSETS, ObjectNode.class);
        tb.setName(entry.getTypeName());
        return tb.buildFeatureType();
    }

    private String getCollectionId(Query query) {
        return Optional.ofNullable(query).map(q -> q.getTypeName()).orElse(entry.getTypeName());
    }

    @Override
    protected boolean canFilter(Query query) {
        return true;
    }

    @Override
    protected boolean canSort(Query query) {
        return true;
    }

    @Override
    protected boolean canRetype(Query query) {
        return true;
    }

    @Override
    protected boolean canLimit(Query query) {
        return true;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
        Pair<SearchQuery, Filter> pair = queryBuilder.toSearchQuery(query, false);
        SearchQuery sq;
        Filter postFilter;
        if (pair == null) {
            sq = queryBuilder.toSearchQuery(Query.ALL, false).getKey();
            postFilter = query.getFilter();
        } else {
            sq = pair.getKey();
            postFilter = pair.getValue();
        }

        SimpleFeatureCollection fc = client.search(sq, getDataStore().getSearchMode(), getSchema());
        SimpleFeatureReader result = new CollectionReader(fc);

        // map out assets if necessary
        if (sq.getFields() != null && sq.getFields().stream().anyMatch(f -> usesAssets(f))) {
            result = new AssetsMappingReader(getSchema(), result);
        }

        // the hard limit must be applied before filtering, or we'll fetch more features
        int hardLimit = getDataStore().getHardLimit();
        if (hardLimit > 0) {
            result = DataUtilities.simple(new MaxFeatureReader<>(result, hardLimit));
        }

        // handle post filtering, if the full filter could not be encoded
        if (!Filter.INCLUDE.equals(postFilter)) result = new FilteringSimpleFeatureReader(result, query.getFilter());

        // handle property selection, if it was not fully encoded in the query, or a post
        // filter required more properties than strictly necessary.
        if (query.getProperties() != null
                && !query.getProperties().isEmpty()
                && (sq.getFields() == null || !Filter.INCLUDE.equals(postFilter))) {
            SimpleFeatureType targetType =
                    SimpleFeatureTypeBuilder.retype(getSchema(), Arrays.asList(query.getPropertyNames()));
            result = DataUtilities.simple(new ReTypeFeatureReader(result, targetType, false));
        }

        // handle max feature from the query
        int max = query.getMaxFeatures();
        if (max < Query.DEFAULT_MAX) result = DataUtilities.simple(new MaxFeatureReader<>(result, max));

        return result;
    }

    private static boolean usesAssets(String f) {
        return f.equals(STACFeatureSource.ASSETS) || f.startsWith(STACFeatureSource.ASSETS + ".");
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        if (visitor instanceof MinVisitor minVisitor
                && ((MinVisitor) visitor).getExpression() instanceof PropertyName) {
            String pn = ((PropertyName) minVisitor.getExpression()).getPropertyName();
            minVisitor.setValue(getExtremeValue(query, pn, SortOrder.ASCENDING));
            return true;
        } else if (visitor instanceof MaxVisitor maxVisitor
                && ((MaxVisitor) visitor).getExpression() instanceof PropertyName) {
            String pn = ((PropertyName) maxVisitor.getExpression()).getPropertyName();
            maxVisitor.setValue(getExtremeValue(query, pn, SortOrder.DESCENDING));
            return true;
        }

        return super.handleVisitor(query, visitor);
    }

    private Object getExtremeValue(Query query, String pn, SortOrder direction) throws IOException {
        Query q = new Query(query);
        q.setPropertyNames(pn);
        q.setSortBy(FF.sort(pn, direction));
        q.setMaxFeatures(1);
        ContentFeatureCollection fc = getFeatures(q);
        SimpleFeature feature = DataUtilities.first(fc);
        if (feature == null) return null;
        return feature.getAttribute(pn);
    }
}
