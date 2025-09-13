/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.filter.sort.SortOrder;
import org.geotools.data.DataUtilities;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.SortByImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;

public class MongoFeatureSource extends ContentFeatureSource {

    static Logger LOG = Logging.getLogger(MongoFeatureSource.class);

    final DBCollection collection;

    CollectionMapper mapper;

    public MongoFeatureSource(ContentEntry entry, Query query, DBCollection collection) {
        super(entry, query);
        this.collection = collection;
        initMapper();
    }

    final void initMapper() {
        // use schema with mapping info if it exists
        SimpleFeatureType type = entry.getState(null).getFeatureType();
        setMapper(
                type != null
                        ? new MongoSchemaMapper(type)
                        : new MongoInferredMapper(
                                getDataStore().getSchemaInitParams().orElse(null)));
    }

    public DBCollection getCollection() {
        return collection;
    }

    public CollectionMapper getMapper() {
        return mapper;
    }

    public void setMapper(CollectionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        SimpleFeatureType type = mapper.buildFeatureType(entry.getName(), collection);
        getDataStore().getSchemaStore().storeSchema(type);
        return type;
    }

    @Override
    public MongoDataStore getDataStore() {
        return (MongoDataStore) super.getDataStore();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        // TODO: crs?
        try (FeatureReader<SimpleFeatureType, SimpleFeature> r = getReader(query)) {
            ReferencedEnvelope e = new ReferencedEnvelope();
            if (r.hasNext()) {
                e.init(r.next().getBounds());
            }
            while (r.hasNext()) {
                e.include(r.next().getBounds());
            }
            return e;
        }
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        Filter f = query.getFilter();
        if (isAll(f)) {
            LOG.fine("count(all)");
            return (int) collection.count();
        }

        Filter[] split = splitFilter(f);
        if (!isAll(split[1])) {
            return -1;
        }

        DBObject q = toQuery(f);
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("count(" + q + ")");
        }
        return (int) collection.count(q);
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {

        List<Filter> postFilterList = new ArrayList<>();
        List<String> postFilterAttributes = new ArrayList<>();
        @SuppressWarnings("PMD.CloseResource") // wrapped and returned
        DBCursor cursor = toCursor(query, postFilterList, postFilterAttributes);
        FeatureReader<SimpleFeatureType, SimpleFeature> r = new MongoFeatureReader(cursor, this);

        if (!postFilterList.isEmpty() && !isAll(postFilterList.get(0))) {
            r = new FilteringFeatureReader<>(r, postFilterList.get(0));

            // check whether attributes not present in the original query have been
            // added to the set of retrieved attributes for the sake of
            // post-filters; if so, wrap the FeatureReader in a ReTypeFeatureReader
            if (!postFilterAttributes.isEmpty()) {
                // build the feature type returned by this query
                SimpleFeatureType returnedSchema =
                        SimpleFeatureTypeBuilder.retype(getSchema(), query.getPropertyNames());
                r = new ReTypeFeatureReader(r, returnedSchema);
            }
        }
        return r;
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        // Don't do the following shortcuts if we don't request all features, as it doesn't make
        // sense.
        if (query.getMaxFeatures() != Integer.MAX_VALUE) {
            return false;
        }

        if (visitor instanceof MinVisitor minVisitor) {
            List<Expression> expressions = minVisitor.getExpressions();
            if (expressions.size() != 1 || !(expressions.get(0) instanceof PropertyName)) {
                return false;
            }

            PropertyName propertyName = (PropertyName) expressions.get(0);
            SortBy sortBy = new SortByImpl(propertyName, SortOrder.ASCENDING);

            Query newQuery = new Query(query);
            newQuery.setSortBy(sortBy);

            // Sorting to get min only need to get one result
            newQuery.setMaxFeatures(1);

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReader(newQuery)) {
                if (reader.hasNext()) {
                    // Don't need to visit all features, retrieved the min value lets just tell the
                    // MinVisitor
                    minVisitor.setValue(propertyName.evaluate(reader.next()));
                }
            }
        } else if (visitor instanceof MaxVisitor maxVisitor) {
            List<Expression> expressions = maxVisitor.getExpressions();
            if (expressions.size() != 1 || !(expressions.get(0) instanceof PropertyName)) {
                return false;
            }

            PropertyName propertyName = (PropertyName) expressions.get(0);
            SortBy sortBy = new SortByImpl(propertyName, SortOrder.DESCENDING);

            Query newQuery = new Query(query);
            newQuery.setSortBy(sortBy);

            // Sorting to get max only need to get one result
            newQuery.setMaxFeatures(1);

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReader(newQuery)) {
                if (reader.hasNext()) {
                    // Don't need to visit all features, retrieved the min value lets just tell the
                    // MaxVisitor
                    maxVisitor.setValue(propertyName.evaluate(reader.next()));
                }
            }
        } else {
            return false;
        }

        return true;
    }

    @Override
    protected boolean canOffset(Query query) {
        return true;
    }

    @Override
    protected boolean canLimit(Query query) {
        return true;
    }

    @Override
    protected boolean canRetype(Query query) {
        return true;
    }

    @Override
    protected boolean canSort(Query query) {
        return true;
    }

    @Override
    protected boolean canFilter(Query query) {
        return true;
    }

    DBCursor toCursor(Query q, List<Filter> postFilter, List<String> postFilterAttrs) {
        DBObject query = new BasicDBObject();

        Filter f = q.getFilter();
        if (!isAll(f)) {
            Filter[] split = splitFilter(f);
            query = toQuery(split[0]);
            if (!isAll(split[1])) {
                postFilter.add(split[1]);
            }
        }

        DBCursor c;
        if (q.getPropertyNames() != Query.ALL_NAMES) {
            BasicDBObject keys = new BasicDBObject();
            for (String p : q.getPropertyNames()) {
                keys.put(mapper.getPropertyPath(p), 1);
            }
            // add properties from post filters
            for (Filter filter : postFilter) {
                String[] attributeNames = DataUtilities.attributeNames(filter);
                for (String attrName : attributeNames) {
                    if (attrName != null && !attrName.isEmpty() && !keys.containsField(attrName)) {
                        keys.put(mapper.getPropertyPath(attrName), 1);
                        postFilterAttrs.add(attrName);
                    }
                }
            }
            if (!keys.containsField(mapper.getGeometryPath())) {
                keys.put(mapper.getGeometryPath(), 1);
            }
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("find(%s, %s)".formatted(query, keys));
            }
            c = collection.find(query, keys);
        } else {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("find(%s)".formatted(query));
            }
            c = collection.find(query);
        }

        if (q.getStartIndex() != null && q.getStartIndex() != 0) {
            c = c.skip(q.getStartIndex());
        }
        if (q.getMaxFeatures() != Integer.MAX_VALUE) {
            c = c.limit(q.getMaxFeatures());
        }

        if (q.getSortBy() != null) {
            BasicDBObject orderBy = new BasicDBObject();
            for (SortBy sortBy : q.getSortBy()) {
                if (sortBy.getPropertyName() != null) {
                    String propName = sortBy.getPropertyName().getPropertyName();
                    String property = mapper.getPropertyPath(propName);
                    orderBy.append(property, sortBy.getSortOrder() == SortOrder.ASCENDING ? 1 : -1);
                }
            }
            c = c.sort(orderBy);
        }

        return c;
    }

    DBObject toQuery(Filter f) {
        if (isAll(f)) {
            return new BasicDBObject();
        }

        FilterToMongo v = new FilterToMongo(mapper);
        v.setFeatureType(getSchema());

        return (DBObject) f.accept(v, null);
    }

    boolean isAll(Filter f) {
        return f == null || f == Filter.INCLUDE;
    }

    Filter[] splitFilter(Filter f) {
        FilterCapabilities filterCapabilities = getDataStore().getFilterCapabilities();
        MongoFilterSplitter splitter =
                new MongoFilterSplitter(filterCapabilities, null, null, new MongoCollectionMeta(getIndexesInfoMap()));
        f.accept(splitter, null);
        return new Filter[] {splitter.getFilterPre(), splitter.getFilterPost()};
    }

    private Map<String, String> getIndexesInfoMap() {
        Map<String, String> indexes = new HashMap<>();
        for (DBObject object : collection.getIndexInfo()) {
            BasicDBObject key = (BasicDBObject) object.get("key");
            for (Map.Entry entry : key.entrySet()) {
                indexes.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return indexes;
    }

    @Override
    protected QueryCapabilities buildQueryCapabilities() {
        if (queryCapabilities == null) return new MongoQueryCapabilities(this);
        else return queryCapabilities;
    }
}
