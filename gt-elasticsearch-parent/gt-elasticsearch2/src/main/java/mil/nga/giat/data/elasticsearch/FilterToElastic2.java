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

import static mil.nga.giat.data.elasticsearch.ElasticLayerConfiguration.DATE_FORMAT;

import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.elasticsearch.common.joda.Joda;
import org.elasticsearch.common.xcontent.json.JsonXContentParser;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.geotools.data.Query;
import org.geotools.geojson.geom.GeometryJSON;
import org.joda.time.format.DateTimeFormatter;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

public class FilterToElastic2 extends FilterToElastic {

    public static DateTimeFormatter DEFAULT_DATE_FORMATTER = Joda.forPattern("date_optional_time").printer();
    
    protected QueryBuilder filterBuilder;

    private DateTimeFormatter dateFormatter;

    public FilterToElastic2() {
        filterBuilder = QueryBuilders.matchAllQuery();
        nativeQueryBuilder = QueryBuilders.matchAllQuery();
        helper = new FilterToElasticHelper2(this);
    }


    // BEGIN IMPLEMENTING org.opengis.filter.FilterVisitor METHODS

    /**
     * Writes the FilterBuilder for the ExcludeFilter.
     * 
     * @param filter the filter to be visited
     */
    public Object visit(ExcludeFilter filter, Object extraData) {
        filterBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.matchAllQuery());
//        filterBuilder = FilterBuilders.notFilter(FilterBuilders.matchAllFilter());
        return extraData;
    }

    /**
     * Writes the FilterBuilder for the IncludeFilter.
     * 
     * @param filter the filter to be visited
     *  
     */
    public Object visit(IncludeFilter filter, Object extraData) {
        filterBuilder = QueryBuilders.matchAllQuery();
//        filterBuilder = FilterBuilders.matchAllFilter();
        return extraData;
    }

    public Object visit(PropertyIsBetween filter, Object extraData) {
        super.visit(filter, extraData);

        filterBuilder = QueryBuilders.rangeQuery(key).gte(lower).lte(upper);
//      filterBuilder = FilterBuilders.rangeFilter(key).gte(lower).lte(upper);
        if(nested) {
            filterBuilder = QueryBuilders.nestedQuery(path, filterBuilder);
        }

        return extraData;
    }

    public Object visit(PropertyIsLike filter, Object extraData) {
        super.visit(filter, extraData);
        
        if (analyzed) {
            // use query string query post filter for analyzed fields
            filterBuilder = QueryBuilders.queryStringQuery(pattern).defaultField(key);
//            filterBuilder = FilterBuilders.queryFilter(QueryBuilders.queryString(pattern).defaultField(key));
        } else {
            // default to regexp filter
            filterBuilder = QueryBuilders.regexpQuery(key, pattern);
//            filterBuilder = FilterBuilders.regexpFilter(key, pattern);
        }
        if (nested) {
            filterBuilder = QueryBuilders.nestedQuery(path,filterBuilder);
//            filterBuilder = FilterBuilders.nestedFilter(path,filterBuilder);
        }

        return extraData;
    }

    public Object visit(Not filter, Object extraData) {
        super.visit(filter, extraData);
        
        if(filter.getFilter() instanceof PropertyIsNull) {
            filterBuilder = QueryBuilders.existsQuery((String) field);
//            filterBuilder = FilterBuilders.existsFilter((String) field);
        } else {
            filterBuilder = QueryBuilders.boolQuery().mustNot(filterBuilder);
//            filterBuilder = FilterBuilders.notFilter(filterBuilder);
        }
        return extraData;
    }

    @Override
    protected Object visit(BinaryLogicOperator filter, Object extraData) {
        LOGGER.finer("exporting LogicFilter");

        final List<QueryBuilder> filterList = new ArrayList<>();
//        final List<FilterBuilder> filterList = new ArrayList<>();
        for (final Filter child : filter.getChildren()) {
            child.accept(this, extraData);
            filterList.add(filterBuilder);
        }
        final QueryBuilder[] filters;
//        final FilterBuilder[] filters;
        filters = filterList.toArray(new QueryBuilder[filterList.size()]);
//        filters = filterList.toArray(new FilterBuilder[filterList.size()]);
        if (extraData.equals("AND")) {
            BoolQueryBuilder andQ = QueryBuilders.boolQuery();
            for (QueryBuilder filterQ: filters){
                andQ.must(filterQ);
            }
            filterBuilder = andQ;
//            filterBuilder = FilterBuilders.andFilter(filters);
        } else if (extraData.equals("OR")) {
            BoolQueryBuilder orQ = QueryBuilders.boolQuery();
            for (QueryBuilder filterQ: filters){
                orQ.should(filterQ);
            }
            filterBuilder = orQ;
//            filterBuilder = FilterBuilders.orFilter(filters);
        }
        return extraData;
    }

    protected void visitBinaryComparisonOperator(BinaryComparisonOperator filter, Object extraData) {
        super.visitBinaryComparisonOperator(filter, extraData);

        if (type.equals("=")) {
            filterBuilder = QueryBuilders.termQuery(key, field);
//            filterBuilder = FilterBuilders.termFilter(key, field);
        } else if (type.equals("!=")) {
            filterBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery(key, field));
//            TermFilterBuilder equalsFilter;
//            equalsFilter = FilterBuilders.termFilter(key, field);
//            filterBuilder = FilterBuilders.notFilter(equalsFilter);
        } else if (type.equals(">")) {
            filterBuilder = QueryBuilders.rangeQuery(key).gt(field);
//            filterBuilder = FilterBuilders.rangeFilter(key).gt(field);
        } else if (type.equals(">=")) {
            filterBuilder = QueryBuilders.rangeQuery(key).gte(field);
//            filterBuilder = FilterBuilders.rangeFilter(key).gte(field);
        } else if (type.equals("<")) {
            filterBuilder = QueryBuilders.rangeQuery(key).lt(field);
//            filterBuilder = FilterBuilders.rangeFilter(key).lt(field);
        } else if (type.equals("<=")) {
            filterBuilder = QueryBuilders.rangeQuery(key).lte(field);
//            filterBuilder = FilterBuilders.rangeFilter(key).lte(field);
        }

        if (nested) {
            filterBuilder = QueryBuilders.nestedQuery(path,filterBuilder);
        }
    }

    public Object visit(PropertyIsNull filter, Object extraData) {
        super.visit(filter, extraData);

        filterBuilder = QueryBuilders.missingQuery((String) field);
//        filterBuilder = FilterBuilders.missingFilter((String) field);
        return extraData;
    }

    public Object visit(Id filter, Object extraData) {
        super.visit(filter, extraData);

        filterBuilder = QueryBuilders.idsQuery().addIds(ids);
//        filterBuilder = FilterBuilders.idsFilter().addIds(ids);
        return extraData;
    }

    protected Object visitBinaryTemporalOperator(BinaryTemporalOperator filter, 
            PropertyName property, Literal temporal, boolean swapped, Object extraData) { 

        super.visitBinaryTemporalOperator(filter, property, temporal, swapped, extraData);
        
        if (filter instanceof After || filter instanceof Before) {
            if (period != null) {
                if ((op.equals(" > ") && !swapped) || (op.equals(" < ") && swapped)) {
                    filterBuilder = QueryBuilders.rangeQuery(key).gt(end);
//                    filterBuilder = FilterBuilders.rangeFilter(key).gt(end);
                } else {
                    filterBuilder = QueryBuilders.rangeQuery(key).lt(begin);
//                    filterBuilder = FilterBuilders.rangeFilter(key).lt(begin);
                }
            }
            else {
                if (op.equals(" < ") || swapped) {
                    filterBuilder = QueryBuilders.rangeQuery(key).lt(field);
//                    filterBuilder = FilterBuilders.rangeFilter(key).lt(field);
                } else {
                    filterBuilder = QueryBuilders.rangeQuery(key).gt(field);
//                    filterBuilder = FilterBuilders.rangeFilter(key).gt(field);
                }
            }
        }
        else if (filter instanceof Begins || filter instanceof Ends || 
                filter instanceof BegunBy || filter instanceof EndedBy ) {

            filterBuilder = QueryBuilders.termQuery(key, field);
//            filterBuilder = FilterBuilders.termFilter(key, field);
        }
        else if (filter instanceof During || filter instanceof TContains){
            filterBuilder = QueryBuilders.rangeQuery(key).gt(lower).lt(field);
//            filterBuilder = FilterBuilders.rangeFilter(key).gt(lower).lt(field);
        }
        else if (filter instanceof TEquals) {
            filterBuilder = QueryBuilders.termQuery(key, field);
//            filterBuilder = FilterBuilders.termFilter(key, field);
        }
        
        if (nested) {
            filterBuilder = QueryBuilders.nestedQuery(path,filterBuilder);
        }

        return extraData;
    }

    // END IMPLEMENTING org.opengis.filter.FilterVisitor METHODS


    // START IMPLEMENTING org.opengis.filter.ExpressionVisitor METHODS
    
    protected void writeLiteral(Object literal) {
        super.writeLiteral(literal);
        
        if (Date.class.isAssignableFrom(literal.getClass())) {
            field = dateFormatter.print(((Date) literal).getTime());
        }
    }
    
    protected void visitLiteralGeometry(Literal expression) throws IOException {
        super.visitLiteralGeometry(expression);

        final GeometryJSON gjson = new GeometryJSON();
        final String geoJson = gjson.toString(currentGeometry);
        final JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createJsonParser(geoJson);
        final JsonXContentParser xParser = new JsonXContentParser(parser);
        xParser.nextToken();
        currentShapeBuilder = ShapeBuilder.parse(xParser);
    }
    
    // END IMPLEMENTING org.opengis.filter.ExpressionVisitor METHODS
    
    @Override
    protected void updateDateFormatter(AttributeDescriptor attType) {
        dateFormatter = DEFAULT_DATE_FORMATTER;
        if (attType != null) {
            final String format = (String) attType.getUserData().get(DATE_FORMAT);
            if (format != null) {
                dateFormatter = Joda.forPattern(format).printer();
            }
        }
    }

    protected void addViewParams(Query query) {
        super.addViewParams(query);
        
        if (parameters != null) {
            if (nativeOnly) {
                LOGGER.fine("Ignoring GeoServer filter (Elasticsearch native query/post filter only)");
                filterBuilder = QueryBuilders.matchAllQuery();
            }
            for (final Map.Entry<String, String> entry : parameters.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("q")) {
                    final String value = entry.getValue();
                    nativeQueryBuilder = QueryBuilders.wrapperQuery(value);
                }
                if (entry.getKey().equalsIgnoreCase("f")) {
                    final String value = entry.getValue();
                    if (nativeOnly || filterBuilder.toString().equals(QueryBuilders.matchAllQuery().toString())) {
//                  if (filterBuilder.toString().equals(FilterBuilders.matchAllFilter().toString())) {
                        filterBuilder = QueryBuilders.wrapperQuery(value);
//                        filterBuilder = FilterBuilders.wrapperFilter(value);
                    } else {
                        filterBuilder = QueryBuilders.boolQuery().must(filterBuilder).must(
                                QueryBuilders.wrapperQuery(value));
//                        filterBuilder = FilterBuilders.andFilter(filterBuilder,
//                                FilterBuilders.wrapperFilter(value));
                    }
                }
            }
        }
    }

    @Override
    public QueryBuilder getQueryBuilder() {
//        return QueryBuilders.boolQuery().must(queryBuilder).filter(filterBuilder);
        final QueryBuilder queryBuilder;
        if (nativeQueryBuilder.toString().equals(QueryBuilders.matchAllQuery().toString())){
            queryBuilder = filterBuilder;
        } else {
            queryBuilder = QueryBuilders.boolQuery().must(nativeQueryBuilder).must(filterBuilder);
        }
        return queryBuilder;
    }
    
}
