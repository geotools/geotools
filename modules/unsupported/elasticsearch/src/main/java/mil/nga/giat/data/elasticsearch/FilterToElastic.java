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
package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static mil.nga.giat.data.elasticsearch.ElasticLayerConfiguration.ANALYZED;
import static mil.nga.giat.data.elasticsearch.ElasticLayerConfiguration.DATE_FORMAT;
import static mil.nga.giat.data.elasticsearch.ElasticLayerConfiguration.NESTED;

import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.elasticsearch.common.jackson.core.JsonFactory;
import org.elasticsearch.common.jackson.core.JsonParser;
import org.elasticsearch.common.joda.Joda;
import org.elasticsearch.common.joda.time.format.DateTimeFormatter;
import org.elasticsearch.common.xcontent.json.JsonXContentParser;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.filter.FilterCapabilities;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNil;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;
import org.opengis.temporal.Period;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;

/**
 * Encodes a {@link Filter} to an Elasticsearch {@link FilterBuilder}. Optionally 
 * applies SQL View parameters from {@link Query} defining Elasticsearch 
 * {@link QueryBuilder} ("Q") and FilterBuilder ("F") directly. If 
 * provided, specified FilterBuilder is added to the generated FilterBuilder to 
 * produce an {@link AndFilterBuilder}.
 * 
 * Based on org.geotools.data.jdbc.FilterToSQL in the GeoTools library/jdbc module.
 */
public class FilterToElastic implements FilterVisitor, ExpressionVisitor {

    /** Standard java logger */
    protected static Logger LOGGER = Logging.getLogger(FilterToElastic.class);

    public static DateTimeFormatter DEFAULT_DATE_FORMATTER = Joda.forPattern("date_optional_time").printer();

    /** filter factory */
    protected static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    /** The filter types that this class can encode */
    protected FilterCapabilities capabilities = null;

    /** the schmema the encoder will use */
    protected SimpleFeatureType featureType;

    protected Geometry currentGeometry;

    protected FilterBuilder filterBuilder;

    protected QueryBuilder queryBuilder;

    protected Object field;

    protected ShapeBuilder currentShapeBuilder;
    
    protected Boolean fullySupported;

    protected FilterToElasticHelper helper;

    private DateTimeFormatter dateFormatter;

    public FilterToElastic() {
        filterBuilder = FilterBuilders.matchAllFilter();
        queryBuilder = QueryBuilders.matchAllQuery();
        helper = new FilterToElasticHelper(this);
        fullySupported = null;
    }

    /**
     * Performs the encoding and populates the {@link FilterBuilder}.
     *
     * @param filter the Filter to be encoded.
     *
     * @throws FilterToElasticException If there were io problems.
     */
    public void encode(Filter filter) throws FilterToElasticException {
        fullySupported = getCapabilities().fullySupports(filter);
        filter.accept(this, null);
    }

    /**
     * Performs the encoding and populates the {@link FilterBuilder}.
     * If SQL View parameters are provided in the query hints, they will be used
     * to define and/or update the {@link QueryBuilder} and {@link FilterBuilder}.
     *
     * @param query the Query to be encoded.
     *
     * @throws FilterToElasticException If there were io problems.
     */
    public void encode(Query query) throws FilterToElasticException {
        encode(query.getFilter());
        addViewParams(query);
    }

    /**
     * Sets the featuretype the encoder is encoding for.
     * <p>
     * This is used for context for attribute expressions. 
     * </p>
     * 
     * @param featureType
     */
    public void setFeatureType(SimpleFeatureType featureType) {
        this.featureType = featureType;
    }

    /**
     * Sets the capabilities of this filter.
     *
     * @return FilterCapabilities for this Filter
     */
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities capabilities = new FilterCapabilities();

        capabilities.addAll(FilterCapabilities.LOGICAL_OPENGIS);
        capabilities.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
        capabilities.addType(PropertyIsNull.class);
        capabilities.addType(PropertyIsBetween.class);
        capabilities.addType(Id.class);
        capabilities.addType(IncludeFilter.class);
        capabilities.addType(ExcludeFilter.class);
        capabilities.addType(PropertyIsLike.class);

        // spatial filters
        capabilities.addType(BBOX.class);
        capabilities.addType(Contains.class);
        //capabilities.addType(Crosses.class);
        capabilities.addType(Disjoint.class);
        //capabilities.addType(Equals.class);
        capabilities.addType(Intersects.class);
        //capabilities.addType(Overlaps.class);
        //capabilities.addType(Touches.class);
        capabilities.addType(Within.class);
        capabilities.addType(DWithin.class);
        capabilities.addType(Beyond.class);

        //temporal filters
        capabilities.addType(After.class);
        capabilities.addType(Before.class);
        capabilities.addType(Begins.class);
        capabilities.addType(BegunBy.class);
        capabilities.addType(During.class);
        capabilities.addType(Ends.class);
        capabilities.addType(EndedBy.class);
        capabilities.addType(TContains.class);
        capabilities.addType(TEquals.class);

        return capabilities;
    }

    /**
     * Describes the capabilities of this encoder.
     * 
     * <p>
     * Performs lazy creation of capabilities.
     * </p>
     * 
     * If you're subclassing this class, override createFilterCapabilities
     * to declare which filtercapabilities you support.  Don't use
     * this method.
     *
     * @return The capabilities supported by this encoder.
     */
    public synchronized final FilterCapabilities getCapabilities() {
        if (capabilities == null) {
            capabilities = createFilterCapabilities();
        }

        return capabilities; //maybe clone?  Make immutable somehow
    }


    // BEGIN IMPLEMENTING org.opengis.filter.FilterVisitor METHODS

    /**
     * Writes the FilterBuilder for the ExcludeFilter.
     * 
     * @param filter the filter to be visited
     */
    public Object visit(ExcludeFilter filter, Object extraData) {
        filterBuilder = FilterBuilders.notFilter(FilterBuilders.matchAllFilter());
        return extraData;
    }


    /**
     * Writes the FilterBuilder for the IncludeFilter.
     * 
     * @param filter the filter to be visited
     *  
     */
    public Object visit(IncludeFilter filter, Object extraData) {
        filterBuilder = FilterBuilders.matchAllFilter();
        return extraData;
    }

    /**
     * Writes the FilterBuilder for the PropertyIsBetween Filter.
     *
     * @param filter the Filter to be visited.
     *
     */
    public Object visit(PropertyIsBetween filter, Object extraData) {
        LOGGER.finer("exporting PropertyIsBetween");

        Expression expr = (Expression) filter.getExpression();
        Expression lowerbounds = (Expression) filter.getLowerBoundary();
        Expression upperbounds = (Expression) filter.getUpperBoundary();

        Class context;
        boolean nested = false;
        AttributeDescriptor attType = (AttributeDescriptor)expr.evaluate(featureType);
        if (attType != null) {
            context = attType.getType().getBinding();
            if (attType.getUserData().containsKey(NESTED)) {
                nested = (Boolean) attType.getUserData().get(NESTED);
            }
            if (Date.class.isAssignableFrom(context)) {
                updateDateFormatter(attType);
            }
        } else {
            //assume it's a string?
            context = String.class;
        }
        
        expr.accept(this, extraData);
        final String key = (String) field;
        lowerbounds.accept(this, context);
        final Object lower = field;
        upperbounds.accept(this, context);
        final Object upper = field;
        filterBuilder = FilterBuilders.rangeFilter(key).gte(lower).lte(upper);
        if(nested) {
            String path = extractNestedPath(key);
            filterBuilder = FilterBuilders.nestedFilter(path,filterBuilder);
        }

        return extraData;
    }


    /**
     * Writes the FilterBuilder for the Like Filter.
     *
     * @param filter the filter to be visited
     *
     */
    public Object visit(PropertyIsLike filter, Object extraData) {
        char esc = filter.getEscape().charAt(0);
        char multi = filter.getWildCard().charAt(0);
        char single = filter.getSingleChar().charAt(0);
        boolean matchCase = false;
        if (filter.isMatchingCase()) {
            LOGGER.fine("Case sensitive search not supported");
        }

        String literal = filter.getLiteral();
        Expression att = filter.getExpression();

        AttributeDescriptor attType = (AttributeDescriptor) att.evaluate(featureType);
        boolean analyzed = false;
        boolean nested = false;
        if (attType != null) {
            if (attType.getUserData().containsKey(ANALYZED)) {
                analyzed = (Boolean) attType.getUserData().get(ANALYZED);
            }
            if (attType.getUserData().containsKey(NESTED)) {
                nested = (Boolean) attType.getUserData().get(NESTED);
            }
            if (Date.class.isAssignableFrom(attType.getType().getBinding())) {
                updateDateFormatter(attType);
            }
        }

        att.accept(this, extraData);
        final String key = (String) field;
        
        if (analyzed) {
            // use query string query post filter for analyzed fields
            String pattern = convertToQueryString(esc, multi, single, matchCase, literal);
            filterBuilder = FilterBuilders.queryFilter(QueryBuilders.queryString(pattern).defaultField(key));
        } else {
            // default to regexp filter
            String pattern = convertToRegex(esc, multi, single, matchCase, literal);
            filterBuilder = FilterBuilders.regexpFilter(key, pattern);
        }
        if (nested) {
            String path = extractNestedPath(key);
            filterBuilder = FilterBuilders.nestedFilter(path,filterBuilder);
        }

        return extraData;
    }

    /**
     * Write the FilterBuilder for an And filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(And filter, Object extraData) {
        return visit((BinaryLogicOperator)filter, "AND");
    }

    /**
     * Write the FilterBuilder for a Not filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(Not filter, Object extraData) {
        if(filter.getFilter() instanceof PropertyIsNull) {
            Expression expr = ((PropertyIsNull) filter.getFilter()).getExpression();
            expr.accept(this, extraData);
            filterBuilder = FilterBuilders.existsFilter((String) field);
        } else {
            filter.getFilter().accept(this, extraData);
            filterBuilder = FilterBuilders.notFilter(filterBuilder);
        }
        return extraData;
    }

    /**
     * Write the FilterBuilder for an Or filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(Or filter, Object extraData) {
        return visit((BinaryLogicOperator)filter, "OR");
    }

    /**
     * Common implementation for BinaryLogicOperator filters.  This way
     * they're all handled centrally.
     *
     * @param filter the logic statement.
     * @param extraData extra filter data.  Not modified directly by this method.
     */
    protected Object visit(BinaryLogicOperator filter, Object extraData) {
        LOGGER.finer("exporting LogicFilter");

        final List<FilterBuilder> filterList = new ArrayList<>();
        for (final Filter child : filter.getChildren()) {
            child.accept(this, extraData);
            filterList.add(filterBuilder);
        }
        final FilterBuilder[] filters;
        filters = filterList.toArray(new FilterBuilder[filterList.size()]);
        if (extraData.equals("AND")) {
            filterBuilder = FilterBuilders.andFilter(filters);
        } else {
            // OR
            filterBuilder = FilterBuilders.orFilter(filters);
        }
        return extraData;
    }



    /**
     * Write the FilterBuilder for this kind of filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator((BinaryComparisonOperator)filter, "=");
        return extraData;
    }

    /**
     * Write the FilterBuilder for this kind of filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator((BinaryComparisonOperator)filter, ">=");
        return extraData;
    }

    /**
     * Write the FilterBuilder for this kind of filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        visitBinaryComparisonOperator((BinaryComparisonOperator)filter, ">");
        return extraData;
    }

    /**
     * Write the FilterBuilder for this kind of filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        visitBinaryComparisonOperator((BinaryComparisonOperator)filter, "<");
        return extraData;
    }

    /**
     * Write the FilterBuilder for this kind of filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator((BinaryComparisonOperator)filter, "<=");
        return extraData;
    }

    /**
     * Write the FilterBuilder for this kind of filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator((BinaryComparisonOperator)filter, "!=");
        return extraData;
    }

    /**
     * Common implementation for BinaryComparisonOperator filters.
     *
     * @param filter the comparison.
     *
     */
    protected void visitBinaryComparisonOperator(BinaryComparisonOperator filter, Object extraData) {
        LOGGER.finer("exporting FilterBuilder ComparisonFilter");

        Expression left = filter.getExpression1();
        Expression right = filter.getExpression2();
        if (isBinaryExpression(left) || isBinaryExpression(right)) {
            throw new UnsupportedOperationException("Binary expressions not supported");
        }

        AttributeDescriptor attType = null;
        Class leftContext = null, rightContext = null;
        if (left instanceof PropertyName) {
            // It's a propertyname, we should get the class and pass it in
            // as context to the tree walker.
            attType = (AttributeDescriptor)left.evaluate(featureType);
            if (attType != null) {
                rightContext = attType.getType().getBinding();
            }
        }
        
        if (right instanceof PropertyName) {
            attType = (AttributeDescriptor)right.evaluate(featureType);
            if (attType != null) {
                leftContext = attType.getType().getBinding();
            }
        }

        boolean nested = false;
        if (attType != null) {
            if (attType.getUserData().containsKey(NESTED)) {
                nested = (Boolean) attType.getUserData().get(NESTED);
            }
            if (Date.class.isAssignableFrom(attType.getType().getBinding())) {
                updateDateFormatter(attType);
            }
        }
        
        //case sensitivity
        if ( !filter.isMatchingCase() ) {
            //we only do for = and !=
            if ( filter instanceof PropertyIsEqualTo || 
                    filter instanceof PropertyIsNotEqualTo ) {
                //and only for strings
                if ( String.class.equals( leftContext ) 
                        || String.class.equals( rightContext ) ) {
                    //matchCase = false;
                    LOGGER.fine("Case insensitive filter not supported");
                }
            }
        }

        String type = (String) extraData;
        
        final String key;
        if (left instanceof PropertyName) {
            left.accept(this, null);
            key = (String) field;
            right.accept(this, rightContext);            
        } else {
            right.accept(this, null);
            key = (String) field;
            left.accept(this, leftContext);            
        }

        if (type.equals("=")) {
            filterBuilder = FilterBuilders.termFilter(key, field);
        } else if (type.equals("!=")) {
            TermFilterBuilder equalsFilter;
            equalsFilter = FilterBuilders.termFilter(key, field);
            filterBuilder = FilterBuilders.notFilter(equalsFilter);
        } else if (type.equals(">")) {
            filterBuilder = FilterBuilders.rangeFilter(key).gt(field);
        } else if (type.equals(">=")) {
            filterBuilder = FilterBuilders.rangeFilter(key).gte(field);
        } else if (type.equals("<")) {
            filterBuilder = FilterBuilders.rangeFilter(key).lt(field);
        } else if (type.equals("<=")) {
            filterBuilder = FilterBuilders.rangeFilter(key).lte(field);
        }
        if (nested) {
            String path = extractNestedPath(key);
            filterBuilder = FilterBuilders.nestedFilter(path,filterBuilder);
        }
    }

    /*
     * determines if the function is a binary expression
     */
    boolean isBinaryExpression(Expression e) {
        return e instanceof BinaryExpression;
    }

    /**
     * Writes the FilterBuilder for the Null Filter.
     *
     * @param filter the null filter.
     *
     */
    public Object visit(PropertyIsNull filter, Object extraData) {
        LOGGER.finer("exporting NullFilter");

        Expression expr = filter.getExpression();

        expr.accept(this, extraData);
        filterBuilder = FilterBuilders.missingFilter((String) field);
        return extraData;
    }

    public Object visit(PropertyIsNil filter, Object extraData) {
        throw new UnsupportedOperationException("isNil not supported");
    }

    /**
     * Encodes an Id filter
     *
     * @param filter the
     *
     */
    public Object visit(Id filter, Object extraData) {
        final List<String> idList = new ArrayList<>();
        for (final Identifier id : filter.getIdentifiers()) {
            idList.add(id.toString());
        }
        final String[] ids = idList.toArray(new String[idList.size()]);
        filterBuilder = FilterBuilders.idsFilter().addIds(ids);
        return extraData;
    }

    public Object visit(BBOX filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Beyond filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Contains filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Crosses filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Disjoint filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(DWithin filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Equals filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Intersects filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Overlaps filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Touches filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Within filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }

    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter,
            Object extraData) {
        // basic checks
        if (filter == null)
            throw new NullPointerException(
                    "Filter to be encoded cannot be null");

        // extract the property name and the geometry literal
        BinarySpatialOperator op = (BinarySpatialOperator) filter;
        Expression e1 = op.getExpression1();
        Expression e2 = op.getExpression2();

        if (e1 instanceof Literal && e2 instanceof PropertyName) {
            e1 = (PropertyName) op.getExpression2();
            e2 = (Literal) op.getExpression1();
        }

        if (e1 instanceof PropertyName && e2 instanceof Literal) {
            //call the "regular" method
            return visitBinarySpatialOperator(filter, (PropertyName)e1, (Literal)e2, filter
                    .getExpression1() instanceof Literal, extraData);
        }
        else {
            //call the join version
            return visitBinarySpatialOperator(filter, e1, e2, extraData);
        }

    }

    protected Object visitBinaryTemporalOperator(BinaryTemporalOperator filter,
            Object extraData) {
        if (filter == null) {
            throw new NullPointerException("Null filter");
        }

        Expression e1 = filter.getExpression1();
        Expression e2 = filter.getExpression2();

        if (e1 instanceof Literal && e2 instanceof PropertyName) {
            e1 = (PropertyName) filter.getExpression2();
            e2 = (Literal) filter.getExpression1();
        }

        if (e1 instanceof PropertyName && e2 instanceof Literal) {
            //call the "regular" method
            return visitBinaryTemporalOperator(filter, (PropertyName)e1, (Literal)e2, 
                    filter.getExpression1() instanceof Literal, extraData);
        }
        else {
            //call the join version
            return visitBinaryTemporalOperator(filter, e1, e2, extraData);
        }
    }

    /**
     * Handles the common case of a PropertyName,Literal geometry binary temporal operator.
     * <p>
     * Subclasses should override if they support more temporal operators than what is handled in 
     * this base class. 
     * </p>
     */
    protected Object visitBinaryTemporalOperator(BinaryTemporalOperator filter, 
            PropertyName property, Literal temporal, boolean swapped, Object extraData) { 

        AttributeDescriptor attType = (AttributeDescriptor)property.evaluate(featureType);

        Class typeContext = null;
        boolean nested = false;
        if (attType != null) {
            typeContext = attType.getType().getBinding();
            if (attType.getUserData().containsKey(NESTED)) {
                nested = (Boolean) attType.getUserData().get(NESTED);
            }
            updateDateFormatter(attType);
        }
        
        //check for time period
        Period period = null;
        if (temporal.evaluate(null) instanceof Period) {
            period = (Period) temporal.evaluate(null);
        }

        //verify that those filters that require a time period have one
        if ((filter instanceof Begins || filter instanceof BegunBy || filter instanceof Ends ||
                filter instanceof EndedBy || filter instanceof During || filter instanceof TContains) &&
                period == null) {
            if (period == null) {
                throw new IllegalArgumentException("Filter requires a time period");
            }
        }
        if (filter instanceof TEquals && period != null) {
            throw new IllegalArgumentException("TEquals filter does not accept time period");
        }

        //ensure the time period is the correct argument
        if ((filter instanceof Begins || filter instanceof Ends || filter instanceof During) && 
                swapped) {
            throw new IllegalArgumentException("Time period must be second argument of Filter");
        }
        if ((filter instanceof BegunBy || filter instanceof EndedBy || filter instanceof TContains) && 
                !swapped) {
            throw new IllegalArgumentException("Time period must be first argument of Filter");
        }

        String key = "";
        if (filter instanceof After || filter instanceof Before) {
            String op = filter instanceof After ? " > " : " < ";

            if (period != null) {
                property.accept(this, extraData);
                key = (String) field;

                visitBegin(period, extraData);
                final Object begin = field;
                visitEnd(period, extraData);
                final Object end = field;

                if ((op.equals(" > ") && !swapped) || (op.equals(" < ") && swapped)) {
                    filterBuilder = FilterBuilders.rangeFilter(key).gt(end);
                } else {
                    filterBuilder = FilterBuilders.rangeFilter(key).lt(begin);
                }
            }
            else {
                property.accept(this, extraData);
                key = (String) field;
                temporal.accept(this, typeContext);

                if (op.equals(" < ") || swapped) {
                    filterBuilder = FilterBuilders.rangeFilter(key).lt(field);
                } else {
                    filterBuilder = FilterBuilders.rangeFilter(key).gt(field);
                }
            }
        }
        else if (filter instanceof Begins || filter instanceof Ends || 
                filter instanceof BegunBy || filter instanceof EndedBy ) {
            property.accept(this, extraData);
            key = (String) field;

            if (filter instanceof Begins || filter instanceof BegunBy) {
                visitBegin(period, extraData);
            }
            else {
                visitEnd(period, extraData);
            }
            filterBuilder = FilterBuilders.termFilter(key, field);
        }
        else if (filter instanceof During || filter instanceof TContains){
            property.accept(this, extraData);
            key = (String) field;

            visitBegin(period, extraData);
            final Object lower = field;
            visitEnd(period, extraData);
            filterBuilder = FilterBuilders.rangeFilter(key).gt(lower).lt(field);
        }
        else if (filter instanceof TEquals) {
            property.accept(this, extraData);
            key = (String) field;
            temporal.accept(this, typeContext);
            filterBuilder = FilterBuilders.termFilter(key, field);
        }
        
        if (nested) {
            String path = extractNestedPath(key);
            filterBuilder = FilterBuilders.nestedFilter(path,filterBuilder);
        }

        return extraData;
    }

    void visitBegin(Period p, Object extraData) {
        filterFactory.literal(p.getBeginning().getPosition().getDate()).accept(this, extraData);
    }

    void visitEnd(Period p, Object extraData) {
        filterFactory.literal(p.getEnding().getPosition().getDate()).accept(this, extraData);
    }

    /**
     * Handles the general case of two expressions in a binary temporal filter.
     * <p>
     * Subclasses should override if they support more temporal operators than what is handled in 
     * this base class. 
     * </p>
     */
    protected Object visitBinaryTemporalOperator(BinaryTemporalOperator filter, Expression e1, 
            Expression e2, Object extraData) {
        throw new UnsupportedOperationException("Join version of binary temporal operator not supported");
    }

    /**
     * Encodes a null filter value.  The current implementation
     * does exactly nothing.
     * @param extraData extra data to be used to evaluate the filter
     * @return the untouched extraData parameter
     */
    public Object visitNullFilter(Object extraData) {
        return extraData;
    }

    // END IMPLEMENTING org.opengis.filter.FilterVisitor METHODS


    // START IMPLEMENTING org.opengis.filter.ExpressionVisitor METHODS

    /**
     * Writes the FilterBuilder for the attribute Expression.
     * 
     * @param expression the attribute.
     *
     */
    @Override
    public Object visit(PropertyName expression, Object extraData) {
        LOGGER.finer("exporting PropertyName");

        SimpleFeatureType featureType = this.featureType;
        
        Class target = null;
        if(extraData instanceof Class) {
            target = (Class) extraData;
        }

        //first evaluate expression against feature type get the attribute, 
        AttributeDescriptor attType = (AttributeDescriptor) expression.evaluate(featureType);

        String encodedField; 
        if ( attType != null ) {
            encodedField = attType.getLocalName();
            if(target != null && target.isAssignableFrom(attType.getType().getBinding())) {
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
     * @param expression
     * the Literal to export
     *
     * @throws FilterToElasticException If there were io problems.
     */
    @Override
    public Object visit(Literal expression, Object context)
            throws FilterToElasticException {
        LOGGER.finer("exporting LiteralExpression");

        // type to convert the literal to
        Class target = null;
        if ( context instanceof Class ) {
            target = (Class) context;
        }

        try {
            //evaluate the expression
            Object literal = evaluateLiteral( expression, target );

            // handle geometry case
            if (literal instanceof Geometry) {
                // call this method for backwards compatibility with subclasses
                visitLiteralGeometry(filterFactory.literal(literal));
            }
            else {
                // write out the literal allowing subclasses to override this
                // behaviour (for writing out dates and the like using the BDMS custom functions)
                writeLiteral(literal);
            }
        } catch (IOException e) {
            throw new FilterToElasticException("IO problems writing literal", e);
        }
        return context;
    }

    protected Object evaluateLiteral(Literal expression, Class target ) {
        Object literal = null;

        // HACK: let expression figure out the right value for numbers,
        // since the context is almost always improperly set and the
        // numeric converters try to force floating points to integrals
        // JD: the above is no longer true, so instead do a safe conversion
        if(target != null) {
            // use the target type
            if (Number.class.isAssignableFrom(target)) {
                literal = safeConvertToNumber(expression, target);

                if (literal == null) {
                    literal = safeConvertToNumber(expression, Number.class);
                }
            }
            else {
                literal = expression.evaluate(null, target);
            }
        }

        //check for conversion to number
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
        if(literal == null)
            literal = expression.evaluate(null);

        // if that failed as well, grab the value as is
        if(literal == null)
            literal = expression.getValue();

        return literal;
    }

    /*
     * helper to do a safe convesion of expression to a number
     */
    Number safeConvertToNumber(Expression expression, Class target) {
        return (Number) Converters.convert(expression.evaluate(null), target, 
                new Hints(ConverterFactory.SAFE_CONVERSION, true));
    }

    /**
     * Writes out a non null, non geometry literal. The base class properly handles
     * null, numeric and booleans (true|false), and turns everything else into a string.
     * Subclasses are expected to override this shall they need a different treatment
     * (e.g. for dates)
     * @param literal
     */
    protected void writeLiteral(Object literal) {
        if (Date.class.isAssignableFrom(literal.getClass())) {
            field = dateFormatter.print(((Date) literal).getTime());
        } else {
            field = literal;
        }
    } 

    protected void visitLiteralTimePeriod(Period expression) {
        throw new UnsupportedOperationException("Time periods not supported, subclasses must implement this " +
                "method to support encoding timeperiods");
    }

    public Object visit(Add expression, Object extraData) {
        throw new UnsupportedOperationException("Add expressions not supported");
    }
    public Object visit(Divide expression, Object extraData) {
        throw new UnsupportedOperationException("Divide expressions not supported");
    }
    public Object visit(Multiply expression, Object extraData) {
        throw new UnsupportedOperationException("Multiply expressions not supported");
    }
    public Object visit(Subtract expression, Object extraData) {
        throw new UnsupportedOperationException("Subtract expressions not supported");
    }

    public Object visit(NilExpression expression, Object extraData) {
        field = null;
        return extraData;
    }

    //temporal filters, not supported
    public Object visit(After after, Object extraData) {
        return visitBinaryTemporalOperator(after, extraData);
    }
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        return visitBinaryTemporalOperator(anyInteracts, extraData);
    }
    public Object visit(Before before, Object extraData) {
        return visitBinaryTemporalOperator(before, extraData);
    }
    public Object visit(Begins begins, Object extraData) {
        return visitBinaryTemporalOperator(begins, extraData);
    }
    public Object visit(BegunBy begunBy, Object extraData) {
        return visitBinaryTemporalOperator(begunBy, extraData);
    }
    public Object visit(During during, Object extraData) {
        return visitBinaryTemporalOperator(during, extraData);
    }
    public Object visit(EndedBy endedBy, Object extraData) {
        return visitBinaryTemporalOperator(endedBy, extraData);
    }
    public Object visit(Ends ends, Object extraData) {
        return visitBinaryTemporalOperator(ends, extraData);
    }
    public Object visit(Meets meets, Object extraData) {
        return visitBinaryTemporalOperator(meets, extraData);
    }
    public Object visit(MetBy metBy, Object extraData) {
        return visitBinaryTemporalOperator(metBy, extraData);
    }
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        return visitBinaryTemporalOperator(overlappedBy, extraData);
    }
    public Object visit(TContains contains, Object extraData) {
        return visitBinaryTemporalOperator(contains, extraData);
    }
    public Object visit(TEquals equals, Object extraData) {
        return visitBinaryTemporalOperator(equals, extraData);
    }
    public Object visit(TOverlaps contains, Object extraData) {
        return visitBinaryTemporalOperator(contains, extraData);
    }

    protected void visitLiteralGeometry(Literal expression) throws IOException {
        // evaluate the literal and store it for later
        currentGeometry  = (Geometry) evaluateLiteral(expression, Geometry.class);

        if ( currentGeometry instanceof LinearRing ) {
            // convert LinearRing to LineString
            final GeometryFactory factory = currentGeometry.getFactory();
            final LinearRing linearRing = (LinearRing) currentGeometry;
            final CoordinateSequence coordinates;
            coordinates = linearRing.getCoordinateSequence();
            currentGeometry = factory.createLineString(coordinates);
        }

        final GeometryJSON gjson = new GeometryJSON();
        final String geoJson = gjson.toString(currentGeometry);
        final JsonFactory factory = new JsonFactory();
        final JsonParser parser = factory.createJsonParser(geoJson);
        final JsonXContentParser xParser = new JsonXContentParser(parser);
        xParser.nextToken();
        currentShapeBuilder = ShapeBuilder.parse(xParser);
    }

    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter,
            PropertyName property, Literal geometry, boolean swapped,
            Object extraData) {
        return helper.visitBinarySpatialOperator(filter, property, geometry,
                swapped, extraData);
    }

    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter, Expression e1, 
            Expression e2, Object extraData) {
        return helper.visitBinarySpatialOperator(filter, e1, e2, extraData);
    }

    @Override
    public Object visit(Function function, Object extraData) {
        throw new UnsupportedOperationException("Function support not implemented");
    }
    
    private void updateDateFormatter(AttributeDescriptor attType) {
        dateFormatter = DEFAULT_DATE_FORMATTER;
        if (attType != null) {
            final String format = (String) attType.getUserData().get(DATE_FORMAT);
            if (format != null) {
                dateFormatter = Joda.forPattern(format).printer();
            }
        }
    }

    protected void addViewParams(Query query) {
        final Map<String,String> parameters;
        if (query.getHints() != null) {
            final Hints hints = query.getHints();
            parameters = (Map) hints.get(Hints.VIRTUAL_TABLE_PARAMETERS);
        } else {
            parameters = null;
        }
        if (parameters != null) {
            boolean nativeOnly = false;
            for (final Map.Entry<String, String> entry : parameters.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("native-only")) {
                    nativeOnly = Boolean.valueOf(entry.getValue());
                }
            }
            if (nativeOnly) {
                LOGGER.fine("Ignoring GeoServer filter (Elasticsearch native query/post filter only)");
                filterBuilder = FilterBuilders.matchAllFilter();
            }
            for (final Map.Entry<String, String> entry : parameters.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("q")) {
                    final String value = entry.getValue();
                    queryBuilder = QueryBuilders.wrapperQuery(value);
                }
                if (entry.getKey().equalsIgnoreCase("f")) {
                    final String value = entry.getValue();
                    if (nativeOnly || filterBuilder.toString().equals(FilterBuilders.matchAllFilter().toString())) {
                        filterBuilder = FilterBuilders.wrapperFilter(value);
                    } else {
                        filterBuilder = FilterBuilders.andFilter(filterBuilder, 
                                FilterBuilders.wrapperFilter(value));
                    }
                }
            }
        }
    }

    public static String convertToQueryString(char escape, char multi, char single, 
            boolean matchCase, String pattern ) {

        StringBuffer result = new StringBuffer(pattern.length()+5);
        for (int i = 0; i < pattern.length(); i++) {
            char chr = pattern.charAt(i);
            if (chr == escape) {
                // emit the next char and skip it
                if (i!= (pattern.length()-1) ) {
                    result.append("\\");
                    result.append( pattern.charAt(i+1) );
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

    public static String convertToRegex(char escape, char multi, char single, 
            boolean matchCase, String pattern) {

        StringBuffer result = new StringBuffer(pattern.length()+5);
        for (int i = 0; i < pattern.length(); i++) {
            char chr = pattern.charAt(i);
            if (chr == escape) {
                // emit the next char and skip it
                if (i!= (pattern.length()-1) ) {
                    result.append("\\");
                    result.append( pattern.charAt(i+1) );
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
        final String base = parts[parts.length-1];
        return field.replace("." + base, "");
    }
    
    public FilterBuilder getFilterBuilder() {
        return filterBuilder;
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }

    public Boolean getFullySupported() {
        return fullySupported;
    }
    
}
