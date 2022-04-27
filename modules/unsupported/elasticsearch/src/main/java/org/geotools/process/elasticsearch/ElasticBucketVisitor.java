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
package org.geotools.process.elasticsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.FeatureAttributeVisitor;
import org.geotools.util.factory.Hints;
import org.opengis.feature.Feature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/** Visitor to generate Elastic aggregate buckets or native queries */
public class ElasticBucketVisitor implements FeatureAttributeVisitor {
    public static final Hints.ClassKey ES_AGGREGATE_BUCKET = new Hints.ClassKey(Map.class);
    private List<Map<String, Object>> buckets;
    private String aggregationDefinition;
    private String queryDefinition;
    private Expression expr;

    public ElasticBucketVisitor(String aggregationDefinition, String queryDefinition) {
        this.buckets = new ArrayList<>();
        this.aggregationDefinition = aggregationDefinition;
        this.queryDefinition = queryDefinition;
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr = factory.property("_aggregation");
    }

    @Override
    public void visit(Feature feature) {
        throw new RuntimeException(
                "The ElasticBucketVisitor is only for Elasticsearch data sources "
                        + "and is not designed to work in memory.");
    }

    public List<Map<String, Object>> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Map<String, Object>> buckets) {
        this.buckets = buckets;
    }

    public String getAggregationDefinition() {
        return aggregationDefinition;
    }

    public void setAggregationDefinition(String aggregationDefinition) {
        this.aggregationDefinition = aggregationDefinition;
    }

    public String getQueryDefinition() {
        return queryDefinition;
    }

    public void setQueryDefinition(String queryDefinition) {
        this.queryDefinition = queryDefinition;
    }

    @Override
    public List<Expression> getExpressions() {
        return Arrays.asList(expr);
    }
}
