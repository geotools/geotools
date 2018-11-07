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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.mongodb.complex.JsonSelectAllFunction;
import org.geotools.data.mongodb.complex.JsonSelectFunction;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.filter.SortByImpl;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

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
        setMapper(type != null ? new MongoSchemaMapper(type) : new MongoInferredMapper());
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
        FeatureReader<SimpleFeatureType, SimpleFeature> r = getReader(query);
        try {
            ReferencedEnvelope e = new ReferencedEnvelope();
            if (r.hasNext()) {
                e.init(r.next().getBounds());
            }
            while (r.hasNext()) {
                e.include(r.next().getBounds());
            }
            return e;
        } finally {
            r.close();
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
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {

        List<Filter> postFilterList = new ArrayList<Filter>();
        List<String> postFilterAttributes = new ArrayList<String>();
        DBCursor cursor = toCursor(query, postFilterList, postFilterAttributes);
        FeatureReader<SimpleFeatureType, SimpleFeature> r = new MongoFeatureReader(cursor, this);

        if (!postFilterList.isEmpty() && !isAll(postFilterList.get(0))) {
            r =
                    new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(
                            r, postFilterList.get(0));

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

        if (visitor instanceof MinVisitor) {
            MinVisitor minVisitor = (MinVisitor) visitor;
            List<Expression> expressions = minVisitor.getExpressions();
            if (expressions.size() != 1 || !(expressions.get(0) instanceof PropertyName)) {
                return false;
            }

            PropertyName propertyName = (PropertyName) expressions.get(0);
            SortBy sortBy = new SortByImpl(propertyName, SortOrder.ASCENDING);

            Query newQuery = new Query(query);
            newQuery.setSortBy(new SortBy[] {sortBy});

            // Sorting to get min only need to get one result
            newQuery.setMaxFeatures(1);

            FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReader(newQuery);
            if (reader.hasNext()) {
                // Don't need to visit all features, retrieved the min value lets just tell the
                // MinVisitor
                minVisitor.setValue(propertyName.evaluate(reader.next()));
            }
        } else if (visitor instanceof MaxVisitor) {
            MaxVisitor maxVisitor = (MaxVisitor) visitor;
            List<Expression> expressions = maxVisitor.getExpressions();
            if (expressions.size() != 1 || !(expressions.get(0) instanceof PropertyName)) {
                return false;
            }

            PropertyName propertyName = (PropertyName) expressions.get(0);
            SortBy sortBy = new SortByImpl(propertyName, SortOrder.DESCENDING);

            Query newQuery = new Query(query);
            newQuery.setSortBy(new SortBy[] {sortBy});

            // Sorting to get max only need to get one result
            newQuery.setMaxFeatures(1);

            FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReader(newQuery);
            if (reader.hasNext()) {
                // Don't need to visit all features, retrieved the min value lets just tell the
                // MaxVisitor
                maxVisitor.setValue(propertyName.evaluate(reader.next()));
            }
        } else {
            return false;
        }

        return true;
    }

    @Override
    protected boolean canOffset() {
        return true;
    }

    @Override
    protected boolean canLimit() {
        return true;
    }

    @Override
    protected boolean canRetype() {
        return true;
    }

    @Override
    protected boolean canSort() {
        return true;
    }

    @Override
    protected boolean canFilter() {
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
                LOG.fine(String.format("find(%s, %s)", query, keys));
            }
            c = collection.find(query, keys);
        } else {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine(String.format("find(%s)", query));
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
                String propName = sortBy.getPropertyName().getPropertyName();
                String property = mapper.getPropertyPath(propName);
                orderBy.append(property, sortBy.getSortOrder() == SortOrder.ASCENDING ? 1 : -1);
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

    @SuppressWarnings("deprecation")
    Filter[] splitFilter(Filter f) {
        PostPreProcessFilterSplittingVisitor splitter =
                new PostPreProcessFilterSplittingVisitor(
                        getDataStore().getFilterCapabilities(), null, null) {

                    @Override
                    protected void visitBinaryComparisonOperator(BinaryComparisonOperator filter) {
                        Expression expression1 = filter.getExpression1();
                        Expression expression2 = filter.getExpression2();
                        if ((expression1 instanceof JsonSelectFunction
                                        || expression1 instanceof JsonSelectAllFunction)
                                && expression2 instanceof Literal) {
                            preStack.push(filter);
                        } else if ((expression2 instanceof JsonSelectFunction
                                        || expression2 instanceof JsonSelectAllFunction)
                                && expression1 instanceof Literal) {
                            preStack.push(filter);
                        }
                    }

                    public Object visit(PropertyIsLike filter, Object notUsed) {
                        if (original == null) original = filter;

                        if (!fcs.supports(PropertyIsLike.class)) {
                            // MongoDB can only encode like expressions using propertyName
                            postStack.push(filter);
                            return null;
                        }
                        if (!(filter.getExpression() instanceof PropertyName)) {
                            // MongoDB can only encode like expressions using propertyName
                            postStack.push(filter);
                            return null;
                        }

                        int i = postStack.size();
                        filter.getExpression().accept(this, null);

                        if (i < postStack.size()) {
                            postStack.pop();
                            postStack.push(filter);

                            return null;
                        }

                        preStack.pop(); // value
                        preStack.push(filter);
                        return null;
                    }
                };
        f.accept(splitter, null);
        return new Filter[] {splitter.getFilterPre(), splitter.getFilterPost()};
    }
}
