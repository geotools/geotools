/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.elasticsearch;

import static org.geotools.process.elasticsearch.ElasticBucketVisitor.ES_AGGREGATE_BUCKET;
import static org.geotools.util.factory.Hints.VIRTUAL_TABLE_PARAMETERS;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.And;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.BinaryLogicOperator;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.IncludeFilter;
import org.geotools.api.filter.Not;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsGreaterThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.PropertyIsLessThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNil;
import org.geotools.api.filter.PropertyIsNotEqualTo;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.expression.Add;
import org.geotools.api.filter.expression.BinaryExpression;
import org.geotools.api.filter.expression.Divide;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.Multiply;
import org.geotools.api.filter.expression.NilExpression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.Subtract;
import org.geotools.api.filter.identity.Identifier;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.api.filter.temporal.After;
import org.geotools.api.filter.temporal.AnyInteracts;
import org.geotools.api.filter.temporal.Before;
import org.geotools.api.filter.temporal.Begins;
import org.geotools.api.filter.temporal.BegunBy;
import org.geotools.api.filter.temporal.BinaryTemporalOperator;
import org.geotools.api.filter.temporal.During;
import org.geotools.api.filter.temporal.EndedBy;
import org.geotools.api.filter.temporal.Ends;
import org.geotools.api.filter.temporal.Meets;
import org.geotools.api.filter.temporal.MetBy;
import org.geotools.api.filter.temporal.OverlappedBy;
import org.geotools.api.filter.temporal.TContains;
import org.geotools.api.filter.temporal.TEquals;
import org.geotools.api.filter.temporal.TOverlaps;
import org.geotools.api.temporal.Period;
import org.geotools.data.elasticsearch.date.DateFormat;
import org.geotools.data.elasticsearch.date.ElasticsearchDateConverter;
import org.geotools.data.geojson.GeoJSONWriter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.Capabilities;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectReader;

/**
 * Encodes an OGC {@link Filter} and creates a filter for an Elasticsearch query. Optionally applies SQL View parameters
 * from {@link Query} defining Elasticsearch query directly.
 *
 * <p>Based on org.geotools.data.jdbc.FilterToSQL in the GeoTools library/jdbc module.
 */
class FilterToElastic implements FilterVisitor, ExpressionVisitor {

    /** Standard java logger */
    static final Logger LOGGER = Logging.getLogger(FilterToElastic.class);

    /** filter factory */
    private static final FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final ObjectReader mapReader =
            mapper.readerWithView(Map.class).forType(HashMap.class);

    private static final ElasticsearchDateConverter DEFAULT_DATE_FORMATTER =
            ElasticsearchDateConverter.of(DateFormat.date_optional_time);

    /** The filter types that this class can encode */
    private Capabilities capabilities = null;

    /** the schmema the encoder will use */
    SimpleFeatureType featureType;

    Geometry currentGeometry;

    Object field;

    Map<String, Object> currentShapeBuilder;

    Boolean fullySupported;

    Map<String, Object> queryBuilder;

    Map<String, Object> nativeQueryBuilder;

    Map<String, Map<String, Map<String, Object>>> aggregations;

    private final FilterToElasticHelper helper;

    private String key;

    private Object lower;

    private Boolean nested;

    private String path;

    private String op;

    private Object begin;

    private Object end;

    private ElasticsearchDateConverter dateFormatter;

    public FilterToElastic() {
        queryBuilder = ElasticConstants.MATCH_ALL;
        nativeQueryBuilder = ImmutableMap.of("match_all", Collections.emptyMap());
        helper = new FilterToElasticHelper(this);
    }

    /**
     * Performs the encoding.
     *
     * @param filter the Filter to be encoded.
     * @throws FilterToElasticException If there were io problems.
     */
    public void encode(Filter filter) throws FilterToElasticException {
        fullySupported = getCapabilities().fullySupports(filter);
        filter.accept(this, null);
    }

    /**
     * Performs the encoding. If SQL View parameters are provided in the query hints, they will be used to define and/or
     * update the query.
     *
     * @param query the Query to be encoded.
     * @throws FilterToElasticException If there were io problems.
     */
    public void encode(Query query) throws FilterToElasticException {
        encode(query.getFilter());
        addViewParams(query);
    }

    /**
     * Sets the featuretype the encoder is encoding for.
     *
     * <p>This is used for context for attribute expressions.
     *
     * @param featureType Feature tag
     */
    public void setFeatureType(SimpleFeatureType featureType) {
        this.featureType = featureType;
    }

    /**
     * Sets the capabilities of this filter.
     *
     * @return Capabilities for this Filter
     */
    Capabilities createCapabilities() {
        return new ElasticCapabilities();
    }

    /**
     * Describes the capabilities of this encoder.
     *
     * <p>Performs lazy creation of capabilities. If you're extending this class, override {@link #createCapabilities()}
     * to declare which capabilities you support. Don't use this method.
     *
     * @return The capabilities supported by this encoder.
     */
    private synchronized Capabilities getCapabilities() {
        if (capabilities == null) {
            capabilities = createCapabilities();
        }

        return capabilities; // maybe clone?  Make immutable somehow
    }

    // BEGIN IMPLEMENTING org.geotools.api.filter.FilterVisitor METHODS

    /**
     * Writes the FilterBuilder for the ExcludeFilter.
     *
     * @param filter the filter to be visited
     */
    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {
        queryBuilder = ImmutableMap.of("bool", ImmutableMap.of("must_not", ElasticConstants.MATCH_ALL));
        return extraData;
    }

    /**
     * Writes the FilterBuilder for the IncludeFilter.
     *
     * @param filter the filter to be visited
     */
    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        queryBuilder = ElasticConstants.MATCH_ALL;
        return extraData;
    }

    /**
     * Writes the FilterBuilder for the PropertyIsBetween Filter.
     *
     * @param filter the Filter to be visited.
     */
    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        LOGGER.finest("exporting PropertyIsBetween");

        Expression expr = filter.getExpression();
        Expression lowerbounds = filter.getLowerBoundary();
        Expression upperbounds = filter.getUpperBoundary();

        Class<?> context;
        nested = false;
        AttributeDescriptor attType = (AttributeDescriptor) expr.evaluate(featureType);
        if (attType != null) {
            context = attType.getType().getBinding();
            if (attType.getUserData().containsKey(ElasticConstants.NESTED)) {
                nested = (Boolean) attType.getUserData().get(ElasticConstants.NESTED);
            }
            if (Date.class.isAssignableFrom(context)) {
                updateDateFormatter(attType);
            }
        } else {
            // assume it's a string?
            context = String.class;
        }

        expr.accept(this, extraData);
        key = (String) field;
        lowerbounds.accept(this, context);
        lower = field;
        upperbounds.accept(this, context);
        Object upper = field;

        if (nested) {
            path = extractNestedPath(key);
        }

        queryBuilder = ImmutableMap.of("range", ImmutableMap.of(key, ImmutableMap.of("gte", lower, "lte", upper)));
        if (nested) {
            queryBuilder = ImmutableMap.of("nested", ImmutableMap.of("path", path, "query", queryBuilder));
        }

        return extraData;
    }

    /**
     * Writes the FilterBuilder for the Like Filter.
     *
     * @param filter the filter to be visited
     */
    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        char esc = filter.getEscape().charAt(0);
        char multi = filter.getWildCard().charAt(0);
        char single = filter.getSingleChar().charAt(0);
        if (filter.isMatchingCase()) {
            LOGGER.fine("Case sensitive search not supported");
        }

        String literal = filter.getLiteral();
        Expression att = filter.getExpression();

        AttributeDescriptor attType = (AttributeDescriptor) att.evaluate(featureType);
        Boolean analyzed = false;
        nested = false;
        if (attType != null) {
            if (attType.getUserData().containsKey(ElasticConstants.ANALYZED)) {
                analyzed = (Boolean) attType.getUserData().get(ElasticConstants.ANALYZED);
            }
            if (attType.getUserData().containsKey(ElasticConstants.NESTED)) {
                nested = (Boolean) attType.getUserData().get(ElasticConstants.NESTED);
            }
            if (Date.class.isAssignableFrom(attType.getType().getBinding())) {
                updateDateFormatter(attType);
            }
        }

        att.accept(this, extraData);
        key = (String) field;

        String pattern;
        if (analyzed) {
            // use query string query post filter for analyzed fields
            pattern = convertToQueryString(esc, multi, single, literal);
        } else {
            // default to regexp filter
            pattern = convertToRegex(esc, multi, single, literal);
        }
        if (nested) {
            path = extractNestedPath(key);
        }

        if (analyzed) {
            // use query string query for analyzed fields
            queryBuilder = ImmutableMap.of("query_string", ImmutableMap.of("query", pattern, "default_field", key));
        } else {
            // default to regexp query
            queryBuilder = ImmutableMap.of("regexp", ImmutableMap.of(key, pattern));
        }
        if (nested) {
            queryBuilder = ImmutableMap.of("nested", ImmutableMap.of("path", path, "query", queryBuilder));
        }

        return extraData;
    }

    /**
     * Write the FilterBuilder for an And filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(And filter, Object extraData) {
        return visit((BinaryLogicOperator) filter, "AND");
    }

    /**
     * Write the FilterBuilder for a Not filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(Not filter, Object extraData) {
        if (filter.getFilter() instanceof PropertyIsNull) {
            Expression expr = ((PropertyIsNull) filter.getFilter()).getExpression();
            expr.accept(this, extraData);
        } else {
            filter.getFilter().accept(this, extraData);
        }

        if (filter.getFilter() instanceof PropertyIsNull) {
            queryBuilder = ImmutableMap.of("exists", ImmutableMap.of("field", field));
        } else {
            queryBuilder = ImmutableMap.of("bool", ImmutableMap.of("must_not", queryBuilder));
        }
        return extraData;
    }

    /**
     * Write the FilterBuilder for an Or filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(Or filter, Object extraData) {
        return visit((BinaryLogicOperator) filter, "OR");
    }

    /**
     * Common implementation for BinaryLogicOperator filters. This way they're all handled centrally.
     *
     * @param filter the logic statement.
     * @param extraData extra filter data. Not modified directly by this method.
     */
    private Object visit(BinaryLogicOperator filter, Object extraData) {
        LOGGER.finest("exporting LogicFilter");

        final List<Map<String, Object>> filters = new ArrayList<>();
        for (final Filter child : filter.getChildren()) {
            child.accept(this, extraData);
            filters.add(queryBuilder);
        }
        if (extraData.equals("AND")) {
            queryBuilder = ImmutableMap.of("bool", ImmutableMap.of("must", filters));
        } else if (extraData.equals("OR")) {
            queryBuilder = ImmutableMap.of("bool", ImmutableMap.of("should", filters));
        }
        return extraData;
    }

    /**
     * Write the FilterBuilder for this kind of filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator(filter, "=");
        return extraData;
    }

    /**
     * Write the FilterBuilder for this kind of filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator(filter, ">=");
        return extraData;
    }

    /**
     * Write the FilterBuilder for this kind of filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        visitBinaryComparisonOperator(filter, ">");
        return extraData;
    }

    /**
     * Write the FilterBuilder for this kind of filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        visitBinaryComparisonOperator(filter, "<");
        return extraData;
    }

    /**
     * Write the FilterBuilder for this kind of filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator(filter, "<=");
        return extraData;
    }

    /**
     * Write the FilterBuilder for this kind of filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator(filter, "!=");
        return extraData;
    }

    /**
     * Common implementation for BinaryComparisonOperator filters.
     *
     * @param filter the comparison.
     */
    private void visitBinaryComparisonOperator(BinaryComparisonOperator filter, Object extraData) {
        LOGGER.finest("exporting FilterBuilder ComparisonFilter");

        Expression left = filter.getExpression1();
        Expression right = filter.getExpression2();
        if (isBinaryExpression(left) || isBinaryExpression(right)) {
            throw new UnsupportedOperationException("Binary expressions not supported");
        }

        AttributeDescriptor attType = null;
        Class<?> leftContext = null, rightContext = null;
        if (left instanceof PropertyName) {
            // It's a propertyname, we should get the class and pass it in
            // as context to the tree walker.
            attType = (AttributeDescriptor) left.evaluate(featureType);
            if (attType != null) {
                rightContext = attType.getType().getBinding();
            }
        }

        if (right instanceof PropertyName) {
            attType = (AttributeDescriptor) right.evaluate(featureType);
            if (attType != null) {
                leftContext = attType.getType().getBinding();
            }
        }

        nested = false;
        if (attType != null) {
            if (attType.getUserData().containsKey(ElasticConstants.NESTED)) {
                nested = (Boolean) attType.getUserData().get(ElasticConstants.NESTED);
            }
            if (Date.class.isAssignableFrom(attType.getType().getBinding())) {
                updateDateFormatter(attType);
            }
        }

        // case sensitivity
        if (!filter.isMatchingCase()) {
            // we only do for = and !=
            if (filter instanceof PropertyIsEqualTo || filter instanceof PropertyIsNotEqualTo) {
                // and only for strings
                if (String.class.equals(leftContext) || String.class.equals(rightContext)) {
                    // matchCase = false;
                    LOGGER.fine("Case insensitive filter not supported");
                }
            }
        }

        String type = (String) extraData;

        if (left instanceof PropertyName) {
            left.accept(this, null);
            key = (String) field;
            right.accept(this, rightContext);
        } else {
            right.accept(this, null);
            key = (String) field;
            left.accept(this, leftContext);
        }

        if (nested) {
            path = extractNestedPath(key);
        }

        switch (type) {
            case "=":
                queryBuilder = ImmutableMap.of("term", ImmutableMap.of(key, field));
                break;
            case "!=":
                queryBuilder = ImmutableMap.of(
                        "bool", ImmutableMap.of("must_not", ImmutableMap.of("term", ImmutableMap.of(key, field))));
                break;
            case ">":
                queryBuilder = ImmutableMap.of("range", ImmutableMap.of(key, ImmutableMap.of("gt", field)));
                break;
            case ">=":
                queryBuilder = ImmutableMap.of("range", ImmutableMap.of(key, ImmutableMap.of("gte", field)));
                break;
            case "<":
                queryBuilder = ImmutableMap.of("range", ImmutableMap.of(key, ImmutableMap.of("lt", field)));
                break;
            case "<=":
                queryBuilder = ImmutableMap.of("range", ImmutableMap.of(key, ImmutableMap.of("lte", field)));
                break;
        }

        if (nested) {
            queryBuilder = ImmutableMap.of("nested", ImmutableMap.of("path", path, "query", queryBuilder));
        }
    }

    /*
     * determines if the function is a binary expression
     */
    private boolean isBinaryExpression(Expression e) {
        return e instanceof BinaryExpression;
    }

    /**
     * Writes the FilterBuilder for the Null Filter.
     *
     * @param filter the null filter.
     */
    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        LOGGER.finest("exporting NullFilter");

        Expression expr = filter.getExpression();

        expr.accept(this, extraData);

        queryBuilder = ImmutableMap.of(
                "bool", ImmutableMap.of("must_not", ImmutableMap.of("exists", ImmutableMap.of("field", field))));

        return extraData;
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        throw new UnsupportedOperationException("isNil not supported");
    }

    /**
     * Encodes an Id filter
     *
     * @param filter the
     */
    @Override
    public Object visit(Id filter, Object extraData) {
        final List<String> idList = new ArrayList<>();
        for (final Identifier id : filter.getIdentifiers()) {
            idList.add(id.toString());
        }

        queryBuilder = ImmutableMap.of("ids", ImmutableMap.of("values", idList));

        return extraData;
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    private Object visitBinarySpatialOperator(BinarySpatialOperator filter, Object extraData) {
        // basic checks
        if (filter == null) throw new NullPointerException("Filter to be encoded cannot be null");

        // extract the property name and the geometry literal
        Expression e1 = filter.getExpression1();
        Expression e2 = filter.getExpression2();

        if (e1 instanceof Literal && e2 instanceof PropertyName) {
            e1 = filter.getExpression2();
            e2 = filter.getExpression1();
        }

        if (e1 instanceof PropertyName name && e2 instanceof Literal literal) {
            // call the "regular" method
            return visitBinarySpatialOperator(
                    filter, name, literal, filter.getExpression1() instanceof Literal, extraData);
        } else {
            // call the join version
            return visitBinarySpatialOperator(filter, e1, e2, extraData);
        }
    }

    private Object visitBinaryTemporalOperator(BinaryTemporalOperator filter, Object extraData) {
        if (filter == null) {
            throw new NullPointerException("Null filter");
        }

        Expression e1 = filter.getExpression1();
        Expression e2 = filter.getExpression2();

        if (e1 instanceof Literal && e2 instanceof PropertyName) {
            e1 = filter.getExpression2();
            e2 = filter.getExpression1();
        }

        if (e1 instanceof PropertyName name && e2 instanceof Literal literal) {
            // call the "regular" method
            return visitBinaryTemporalOperator(
                    filter, name, literal, filter.getExpression1() instanceof Literal, extraData);
        } else {
            // call the join version
            return visitBinaryTemporalOperator();
        }
    }

    /**
     * Handles the common case of a PropertyName,Literal geometry binary temporal operator.
     *
     * <p>Subclasses should override if they support more temporal operators than what is handled in this base class.
     */
    private Object visitBinaryTemporalOperator(
            BinaryTemporalOperator filter, PropertyName property, Literal temporal, boolean swapped, Object extraData) {

        AttributeDescriptor attType = (AttributeDescriptor) property.evaluate(featureType);

        Class<?> typeContext = null;
        nested = false;
        if (attType != null) {
            typeContext = attType.getType().getBinding();
            if (attType.getUserData().containsKey(ElasticConstants.NESTED)) {
                nested = (Boolean) attType.getUserData().get(ElasticConstants.NESTED);
            }
            updateDateFormatter(attType);
        }

        // check for time period
        Period period = null;
        if (temporal.evaluate(null) instanceof Period) {
            period = (Period) temporal.evaluate(null);
        }

        // verify that those filters that require a time period have one
        if ((filter instanceof Begins
                        || filter instanceof BegunBy
                        || filter instanceof Ends
                        || filter instanceof EndedBy
                        || filter instanceof During
                        || filter instanceof TContains)
                && period == null) {
            throw new IllegalArgumentException("Filter requires a time period");
        }
        if (filter instanceof TEquals && period != null) {
            throw new IllegalArgumentException("TEquals filter does not accept time period");
        }

        // ensure the time period is the correct argument
        if ((filter instanceof Begins || filter instanceof Ends || filter instanceof During) && swapped) {
            throw new IllegalArgumentException("Time period must be second argument of Filter");
        }
        if ((filter instanceof BegunBy || filter instanceof EndedBy || filter instanceof TContains) && !swapped) {
            throw new IllegalArgumentException("Time period must be first argument of Filter");
        }

        key = "";
        if (filter instanceof After || filter instanceof Before) {
            op = filter instanceof After ? " > " : " < ";

            if (period != null) {
                property.accept(this, extraData);
                key = (String) field;

                visitBegin(period, extraData);
                begin = field;
                visitEnd(period, extraData);
                end = field;
            } else {
                property.accept(this, extraData);
                key = (String) field;
                temporal.accept(this, typeContext);
            }
        } else if (filter instanceof Begins
                || filter instanceof Ends
                || filter instanceof BegunBy
                || filter instanceof EndedBy) {
            property.accept(this, extraData);
            key = (String) field;

            if (filter instanceof Begins || filter instanceof BegunBy) {
                visitBegin(period, extraData);
            } else {
                visitEnd(period, extraData);
            }
        } else if (filter instanceof During || filter instanceof TContains) {
            property.accept(this, extraData);
            key = (String) field;

            visitBegin(period, extraData);
            lower = field;
            visitEnd(period, extraData);
        } else if (filter instanceof TEquals) {
            property.accept(this, extraData);
            key = (String) field;
            temporal.accept(this, typeContext);
        }

        if (nested) {
            path = extractNestedPath(key);
        }

        if (filter instanceof After || filter instanceof Before) {
            if (period != null) {
                if ((op.equals(" > ") && !swapped) || (op.equals(" < ") && swapped)) {
                    queryBuilder = ImmutableMap.of("range", ImmutableMap.of(key, ImmutableMap.of("gt", end)));
                } else {
                    queryBuilder = ImmutableMap.of("range", ImmutableMap.of(key, ImmutableMap.of("lt", begin)));
                }
            } else {
                if (op.equals(" < ") || swapped) {
                    queryBuilder = ImmutableMap.of("range", ImmutableMap.of(key, ImmutableMap.of("lt", field)));
                } else {
                    queryBuilder = ImmutableMap.of("range", ImmutableMap.of(key, ImmutableMap.of("gt", field)));
                }
            }
        } else if (filter instanceof Begins
                || filter instanceof Ends
                || filter instanceof BegunBy
                || filter instanceof EndedBy) {

            queryBuilder = ImmutableMap.of("term", ImmutableMap.of(key, field));
        } else if (filter instanceof During || filter instanceof TContains) {
            queryBuilder = ImmutableMap.of("range", ImmutableMap.of(key, ImmutableMap.of("gt", lower, "lt", field)));
        } else if (filter instanceof TEquals) {
            queryBuilder = ImmutableMap.of("term", ImmutableMap.of(key, field));
        }

        if (nested) {
            queryBuilder = ImmutableMap.of("nested", ImmutableMap.of("path", path, "query", queryBuilder));
        }

        return extraData;
    }

    private void visitBegin(Period p, Object extraData) {
        filterFactory.literal(p.getBeginning().getPosition().getDate()).accept(this, extraData);
    }

    private void visitEnd(Period p, Object extraData) {
        filterFactory.literal(p.getEnding().getPosition().getDate()).accept(this, extraData);
    }

    /**
     * Handles the general case of two expressions in a binary temporal filter.
     *
     * <p>Subclasses should override if they support more temporal operators than what is handled in this base class.
     */
    Object visitBinaryTemporalOperator() {
        throw new UnsupportedOperationException("Join version of binary temporal operator not supported");
    }

    /**
     * Encodes a null filter value. The current implementation does exactly nothing.
     *
     * @param extraData extra data to be used to evaluate the filter
     * @return the untouched extraData parameter
     */
    @Override
    public Object visitNullFilter(Object extraData) {
        return extraData;
    }

    // END IMPLEMENTING org.geotools.api.filter.FilterVisitor METHODS

    // START IMPLEMENTING org.geotools.api.filter.ExpressionVisitor METHODS

    /**
     * Writes the FilterBuilder for the attribute Expression.
     *
     * @param expression the attribute.
     */
    @Override
    public Object visit(PropertyName expression, Object extraData) {
        LOGGER.finest("exporting PropertyName");

        SimpleFeatureType featureType = this.featureType;

        Class<?> target = null;
        if (extraData instanceof Class<?> class1) {
            target = class1;
        }

        // first evaluate expression against feature type get the attribute,
        AttributeDescriptor attType = (AttributeDescriptor) expression.evaluate(featureType);

        String encodedField;
        if (attType != null) {
            Map<Object, Object> userData = attType.getUserData();
            if (userData != null && userData.containsKey("full_name")) {
                encodedField = userData.get("full_name").toString();
            } else {
                encodedField = attType.getLocalName();
            }
            if (target != null && target.isAssignableFrom(attType.getType().getBinding())) {
                // no need for casting, it's already the right type
                target = null;
            }
        } else {
            // fall back to just encoding the property name
            encodedField = expression.getPropertyName();
        }

        if (target != null) {
            LOGGER.fine("PropertyName type casting not implemented");
        }
        field = encodedField;

        return extraData;
    }

    /**
     * Export the contents of a Literal Expresion
     *
     * @param expression the Literal to export
     * @throws FilterToElasticException If there were io problems.
     */
    @Override
    public Object visit(Literal expression, Object context) throws FilterToElasticException {
        LOGGER.finest("exporting LiteralExpression");

        // type to convert the literal to
        Class<?> target = null;
        if (context instanceof Class<?> class1) {
            target = class1;
        }

        try {
            // evaluate the expression
            Object literal = evaluateLiteral(expression, target);

            // handle geometry case
            if (literal instanceof Geometry) {
                // call this method for backwards compatibility with subclasses
                visitLiteralGeometry(filterFactory.literal(literal));
            } else {
                // write out the literal allowing subclasses to override this
                // behaviour (for writing out dates and the like using the BDMS custom functions)
                writeLiteral(literal);
            }
        } catch (IOException e) {
            throw new FilterToElasticException("IO problems writing literal", e);
        }
        return context;
    }

    private Object evaluateLiteral(Literal expression, Class<?> target) {
        Object literal = null;

        // HACK: let expression figure out the right value for numbers,
        // since the context is almost always improperly set and the
        // numeric converters try to force floating points to integrals
        // JD: the above is no longer true, so instead do a safe conversion
        if (target != null) {
            // use the target type
            if (Number.class.isAssignableFrom(target)) {
                literal = safeConvertToNumber(expression, target);

                if (literal == null) {
                    literal = safeConvertToNumber(expression, Number.class);
                }
            } else {
                literal = expression.evaluate(null, target);
            }
        }

        // check for conversion to number
        if (target == null) {
            // we don't know the target type, check for a conversion to a number

            Number number = safeConvertToNumber(expression, Number.class);
            if (number != null) {
                literal = number;
            }
        }

        // if the target was not known, of the conversion failed, try the
        // type guessing dance literal expression does only for the following
        // method call
        if (literal == null) literal = expression.evaluate(null);

        // if that failed as well, grab the value as is
        if (literal == null) literal = expression.getValue();

        return literal;
    }

    /**
     * Writes out a non null, non geometry literal. The base class properly handles null, numeric and booleans
     * (true|false), and turns everything else into a string. Subclasses are expected to override this shall they need a
     * different treatment (e.g. for dates)
     *
     * @param literal Literal
     */
    private void writeLiteral(Object literal) {
        field = literal;

        if (Date.class.isAssignableFrom(literal.getClass())) {
            field = dateFormatter.format((Date) literal);
        }
    }

    void visitLiteralTimePeriod() {
        throw new UnsupportedOperationException("Time periods not supported, subclasses must implement this "
                + "method to support encoding timeperiods");
    }

    @Override
    public Object visit(Add expression, Object extraData) {
        throw new UnsupportedOperationException("Add expressions not supported");
    }

    @Override
    public Object visit(Divide expression, Object extraData) {
        throw new UnsupportedOperationException("Divide expressions not supported");
    }

    @Override
    public Object visit(Multiply expression, Object extraData) {
        throw new UnsupportedOperationException("Multiply expressions not supported");
    }

    @Override
    public Object visit(Subtract expression, Object extraData) {
        throw new UnsupportedOperationException("Subtract expressions not supported");
    }

    @Override
    public Object visit(NilExpression expression, Object extraData) {
        field = null;
        return extraData;
    }

    // temporal filters, not supported
    @Override
    public Object visit(After after, Object extraData) {
        return visitBinaryTemporalOperator(after, extraData);
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        return visitBinaryTemporalOperator(anyInteracts, extraData);
    }

    @Override
    public Object visit(Before before, Object extraData) {
        return visitBinaryTemporalOperator(before, extraData);
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        return visitBinaryTemporalOperator(begins, extraData);
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        return visitBinaryTemporalOperator(begunBy, extraData);
    }

    @Override
    public Object visit(During during, Object extraData) {
        return visitBinaryTemporalOperator(during, extraData);
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        return visitBinaryTemporalOperator(endedBy, extraData);
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        return visitBinaryTemporalOperator(ends, extraData);
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        return visitBinaryTemporalOperator(meets, extraData);
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        return visitBinaryTemporalOperator(metBy, extraData);
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        return visitBinaryTemporalOperator(overlappedBy, extraData);
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        return visitBinaryTemporalOperator(contains, extraData);
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        return visitBinaryTemporalOperator(equals, extraData);
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        return visitBinaryTemporalOperator(contains, extraData);
    }

    private void visitLiteralGeometry(Literal expression) throws IOException {
        // evaluate the literal and store it for later
        currentGeometry = (Geometry) evaluateLiteral(expression, Geometry.class);

        if (currentGeometry instanceof LinearRing linearRing) {
            // convert LinearRing to LineString
            final GeometryFactory factory = currentGeometry.getFactory();
            final CoordinateSequence coordinates = linearRing.getCoordinateSequence();
            currentGeometry = factory.createLineString(coordinates);
        }
        int maxDecimals = getMaxDecimalsForEnvelope(currentGeometry.getEnvelopeInternal());
        final String geoJson = GeoJSONWriter.toGeoJSON(currentGeometry, maxDecimals);
        currentShapeBuilder = mapReader.readValue(geoJson);
    }

    protected static int getMaxDecimalsForEnvelope(Envelope envelope) {
        double min = Math.min(Math.abs(envelope.getWidth()), Math.abs(envelope.getHeight()));
        if (min == 0) {
            LOGGER.log(Level.WARNING, "BBox Geometry has no width or height, it is either a point or a line.");
            return JtsModule.DEFAULT_MAX_DECIMALS;
        }
        double decimalPart = min - Math.floor(min);
        // min dimension is whole number but the other dimension might have decimals
        if (decimalPart == 0) {
            return JtsModule.DEFAULT_MAX_DECIMALS;
        }
        double log = Math.log10(decimalPart);
        int numDecimals = Math.abs((int) Math.floor(log) + 1);
        if (numDecimals <= JtsModule.DEFAULT_MAX_DECIMALS) {
            return JtsModule.DEFAULT_MAX_DECIMALS;
        } else {
            return numDecimals;
        }
    }

    private Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, PropertyName property, Literal geometry, boolean swapped, Object extraData) {
        return helper.visitBinarySpatialOperator(filter, property, geometry, swapped, extraData);
    }

    private Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, Object extraData) {
        return helper.visitBinarySpatialOperator(filter, e1, e2, extraData);
    }

    @Override
    public Object visit(Function function, Object extraData) {
        throw new UnsupportedOperationException("Function support not implemented");
    }

    // END IMPLEMENTING org.geotools.api.filter.ExpressionVisitor METHODS

    private void updateDateFormatter(AttributeDescriptor attType) {
        if (attType != null) {
            @SuppressWarnings("unchecked")
            final List<String> validFormats =
                    (List<String>) attType.getUserData().get(ElasticConstants.DATE_FORMAT);
            if (validFormats != null) {
                for (String format : validFormats) {
                    try {
                        dateFormatter = ElasticsearchDateConverter.forFormat(format);
                        break;
                    } catch (Exception e) {
                        LOGGER.fine("Unable to parse date format ('" + format + "') for " + attType);
                    }
                }
            }
        }
        if (dateFormatter == null) {
            dateFormatter = DEFAULT_DATE_FORMATTER;
        }
    }

    /*
     * helper to do a safe convesion of expression to a number
     */
    private Number safeConvertToNumber(Expression expression, Class<?> target) {
        return (Number) Converters.convert(
                expression.evaluate(null), target, new Hints(ConverterFactory.SAFE_CONVERSION, true));
    }

    void addViewParams(Query query) {
        Hints hints = query.getHints();
        // aggregation handling
        if (hints != null && hints.get(ES_AGGREGATE_BUCKET) != null) {
            @SuppressWarnings("unchecked")
            Map<String, String> parameters = (Map) hints.get(ES_AGGREGATE_BUCKET);

            boolean nativeOnly = false;
            for (final Map.Entry<String, String> entry : parameters.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("native-only")) {
                    nativeOnly = Boolean.parseBoolean(entry.getValue());
                }
            }
            if (nativeOnly) {
                LOGGER.fine("Ignoring GeoServer filter (Elasticsearch native query/post filter only)");
                queryBuilder = ElasticConstants.MATCH_ALL;
            }

            for (final Map.Entry<String, String> entry : parameters.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("q")) {
                    setupNativeQuery(entry.getValue());
                }
                if (entry.getKey().equalsIgnoreCase("a")) {
                    this.aggregations = GeohashUtil.parseAggregation(entry.getValue());

                    // map default geometry to actual underlying field name, if it was left empty
                    // (e.g, automatic grid definition in GeoHashProcess, it does not have
                    // access to the geometry name in general)
                    Optional.ofNullable(aggregations)
                            .map(a -> a.get("agg"))
                            .map(a -> a.get("geohash_grid"))
                            .ifPresent(this::setGeometryField);
                }
            }
        }
        // allow native query to be provided via view param
        if (hints != null && hints.get(VIRTUAL_TABLE_PARAMETERS) != null) {
            @SuppressWarnings("unchecked")
            Map<String, String> parameters = (Map) hints.get(VIRTUAL_TABLE_PARAMETERS);
            for (final Map.Entry<String, String> entry : parameters.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("q")) {
                    setupNativeQuery(entry.getValue());
                }
            }
        }
    }

    private void setupNativeQuery(String nativeQuery) {
        try {
            nativeQueryBuilder = mapReader.readValue(nativeQuery);
        } catch (Exception e) {
            // retry with decoded nativeQuery
            try {
                nativeQueryBuilder = mapReader.readValue(ElasticParserUtil.urlDecode(nativeQuery));
            } catch (Exception e2) {
                throw new FilterToElasticException("Unable to parse native query", e);
            }
        }
    }

    private void setGeometryField(Map<String, Object> m) {
        if ("".equals(m.get("field"))) {
            GeometryDescriptor gd = featureType.getGeometryDescriptor();
            String name = (String) gd.getUserData().get(ElasticConstants.FULL_NAME);
            if (name == null) name = gd.getLocalName();
            m.put("field", name);
        }
    }

    public static String convertToQueryString(char escape, char multi, char single, String pattern) {

        StringBuilder result = new StringBuilder(pattern.length() + 5);
        for (int i = 0; i < pattern.length(); i++) {
            char chr = pattern.charAt(i);
            if (chr == escape) {
                // emit the next char and skip it
                if (i != (pattern.length() - 1)) {
                    result.append("\\");
                    result.append(pattern.charAt(i + 1));
                }
                i++; // skip next char
            } else if (chr == single) {
                result.append('?');
            } else if (chr == multi) {
                result.append('*');
            } else {
                result.append(chr);
            }
        }

        return result.toString();
    }

    public static String convertToRegex(char escape, char multi, char single, String pattern) {

        StringBuilder result = new StringBuilder(pattern.length() + 5);
        for (int i = 0; i < pattern.length(); i++) {
            char chr = pattern.charAt(i);
            if (chr == escape) {
                // emit the next char and skip it
                if (i != (pattern.length() - 1)) {
                    result.append("\\");
                    result.append(pattern.charAt(i + 1));
                }
                i++; // skip next char
            } else if (chr == single) {
                result.append('.');
            } else if (chr == multi) {
                result.append(".*");
            } else {
                result.append(chr);
            }
        }

        return result.toString();
    }

    private static String extractNestedPath(String field) {
        final String[] parts = field.split("\\.");
        final String base = parts[parts.length - 1];
        return field.replace("." + base, "");
    }

    public Boolean getFullySupported() {
        return fullySupported;
    }

    public Map<String, Object> getNativeQueryBuilder() {
        return nativeQueryBuilder;
    }

    public Map<String, Object> getQueryBuilder() {
        final Map<String, Object> queryBuilder;
        if (nativeQueryBuilder.equals(ElasticConstants.MATCH_ALL)) {
            queryBuilder = this.queryBuilder;
        } else if (this.queryBuilder.equals(ElasticConstants.MATCH_ALL)) {
            queryBuilder = nativeQueryBuilder;
        } else {
            queryBuilder = ImmutableMap.of(
                    "bool", ImmutableMap.of("must", ImmutableList.of(nativeQueryBuilder, this.queryBuilder)));
        }
        return queryBuilder;
    }

    public Map<String, Map<String, Map<String, Object>>> getAggregations() {
        return aggregations;
    }
}
