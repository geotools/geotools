package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static mil.nga.giat.data.elasticsearch.ElasticLayerConfiguration.DATE_FORMAT;

import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.elasticsearch.common.jackson.core.JsonFactory;
import org.elasticsearch.common.jackson.core.JsonParser;
import org.elasticsearch.common.joda.Joda;
import org.elasticsearch.common.joda.time.format.DateTimeFormatter;
import org.elasticsearch.common.xcontent.json.JsonXContentParser;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.geotools.data.Query;
import org.geotools.geojson.geom.GeometryJSON;
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

public class FilterToElastic1 extends FilterToElastic {

    public static DateTimeFormatter DEFAULT_DATE_FORMATTER = Joda.forPattern("date_optional_time").printer();
    
    protected FilterBuilder filterBuilder;

    private DateTimeFormatter dateFormatter;

    public FilterToElastic1() {
        filterBuilder = FilterBuilders.matchAllFilter();
        nativeQueryBuilder = QueryBuilders.matchAllQuery();
        helper = new FilterToElasticHelper1(this);
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

    public Object visit(PropertyIsBetween filter, Object extraData) {
        super.visit(filter, extraData);
        
        filterBuilder = FilterBuilders.rangeFilter(key).gte(lower).lte(upper);
        if(nested) {
            filterBuilder = FilterBuilders.nestedFilter(path,filterBuilder);
        }

        return extraData;
    }

    public Object visit(PropertyIsLike filter, Object extraData) {
        super.visit(filter, extraData);
        
        if (analyzed) {
            // use query string query post filter for analyzed fields
            filterBuilder = FilterBuilders.queryFilter(QueryBuilders.queryString(pattern).defaultField(key));
        } else {
            // default to regexp filter
            filterBuilder = FilterBuilders.regexpFilter(key, pattern);
        }
        if (nested) {
            filterBuilder = FilterBuilders.nestedFilter(path,filterBuilder);
        }

        return extraData;
    }

    public Object visit(Not filter, Object extraData) {
        super.visit(filter, extraData);
        
        if(filter.getFilter() instanceof PropertyIsNull) {
            filterBuilder = FilterBuilders.existsFilter((String) field);
        } else {
            filterBuilder = FilterBuilders.notFilter(filterBuilder);
        }
        return extraData;
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

    protected void visitBinaryComparisonOperator(BinaryComparisonOperator filter, Object extraData) {
        super.visitBinaryComparisonOperator(filter, extraData);
        
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
            filterBuilder = FilterBuilders.nestedFilter(path,filterBuilder);
        }
    }

    public Object visit(PropertyIsNull filter, Object extraData) {
        super.visit(filter, extraData);
        
        filterBuilder = FilterBuilders.missingFilter((String) field);
        return extraData;
    }

    public Object visit(Id filter, Object extraData) {
        super.visit(filter, extraData);
        
        filterBuilder = FilterBuilders.idsFilter().addIds(ids);
        return extraData;
    }

    protected Object visitBinaryTemporalOperator(BinaryTemporalOperator filter, 
            PropertyName property, Literal temporal, boolean swapped, Object extraData) {
        
        super.visitBinaryTemporalOperator(filter, property, temporal, swapped, extraData);

        if (filter instanceof After || filter instanceof Before) {
            if (period != null) {
                if ((op.equals(" > ") && !swapped) || (op.equals(" < ") && swapped)) {
                    filterBuilder = FilterBuilders.rangeFilter(key).gt(end);
                } else {
                    filterBuilder = FilterBuilders.rangeFilter(key).lt(begin);
                }
            }
            else {
                if (op.equals(" < ") || swapped) {
                    filterBuilder = FilterBuilders.rangeFilter(key).lt(field);
                } else {
                    filterBuilder = FilterBuilders.rangeFilter(key).gt(field);
                }
            }
        }
        else if (filter instanceof Begins || filter instanceof Ends || 
                filter instanceof BegunBy || filter instanceof EndedBy ) {

            filterBuilder = FilterBuilders.termFilter(key, field);
        }
        else if (filter instanceof During || filter instanceof TContains){
            filterBuilder = FilterBuilders.rangeFilter(key).gt(lower).lt(field);
        }
        else if (filter instanceof TEquals) {
            filterBuilder = FilterBuilders.termFilter(key, field);
        }
        
        if (nested) {
            filterBuilder = FilterBuilders.nestedFilter(path,filterBuilder);
        }

        return extraData;
    }

    // END IMPLEMENTING org.opengis.filter.FilterVisitor METHODS

    
    // START IMPLEMENTING org.opengis.filter.ExpressionVisitor METHODS

    /**
     * Writes out a non null, non geometry literal. The base class properly handles
     * null, numeric and booleans (true|false), and turns everything else into a string.
     * Subclasses are expected to override this shall they need a different treatment
     * (e.g. for dates)
     * @param literal
     */
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
                filterBuilder = FilterBuilders.matchAllFilter();
            }
            for (final Map.Entry<String, String> entry : parameters.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("q")) {
                    final String value = entry.getValue();
                    nativeQueryBuilder = QueryBuilders.wrapperQuery(value);
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
    
    public FilterBuilder getFilterBuilder() {
        return filterBuilder;
    }

    public QueryBuilder getQueryBuilder() {
        return QueryBuilders.filteredQuery(nativeQueryBuilder, filterBuilder);
    }
    
}
