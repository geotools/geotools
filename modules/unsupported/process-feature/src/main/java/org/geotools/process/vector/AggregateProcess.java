/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2016, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.*;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.util.ProgressListener;

/**
 * Computes various attribute statistics over vector data sets.
 *
 * @author Andrea Aime
 * @source $URL$
 */
@DescribeProcess(
    title = "Aggregate",
    description =
            "Computes one or more aggregation functions on a feature attribute. Functions include Count, Average, Max, Median, Min, StdDev, and Sum."
)
public class AggregateProcess implements VectorProcess {
    // the functions this process can handle
    public enum AggregationFunction {
        Count,
        Average,
        Max,
        Median,
        Min,
        StdDev,
        Sum,
        SumArea;
    }
    /**
     * Computes various attribute statistics over vector data sets
     *
     * @param features FeatureCollection to aggregate
     * @param aggAttribute target attribute
     * @param functions
     * @param singlePass
     * @param progressListener
     * @return aggregate Results
     */
    public static Results process(
            SimpleFeatureCollection features,
            String aggAttribute,
            Set<AggregationFunction> functions,
            Boolean singlePass,
            ProgressListener progressListener)
            throws ProcessException, IOException {
        return process(features, aggAttribute, functions, null, singlePass, progressListener);
    }

    public static Results process(
            SimpleFeatureCollection features,
            String aggAttribute,
            Set<AggregationFunction> functions,
            List<String> groupByAttributes,
            Boolean singlePass,
            ProgressListener progressListener)
            throws ProcessException, IOException {
        AggregateProcess process = new AggregateProcess();
        return process.execute(
                features, aggAttribute, functions, singlePass, groupByAttributes, progressListener);
    }

    public Results execute(
            SimpleFeatureCollection features,
            String aggAttribute,
            Set<AggregationFunction> functions,
            boolean singlePass,
            ProgressListener progressListener)
            throws ProcessException, IOException {
        return execute(features, aggAttribute, functions, singlePass, null, progressListener);
    }

    @DescribeResult(
        name = "result",
        description = "Aggregation results (one value for each function computed)"
    )
    public Results execute(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    SimpleFeatureCollection features,
            @DescribeParameter(
                        name = "aggregationAttribute",
                        min = 0,
                        description = "Attribute on which to perform aggregation"
                    )
                    String aggAttribute,
            @DescribeParameter(
                        name = "function",
                        description =
                                "An aggregate function to compute. Functions include Count, Average, Max, Median, Min, StdDev, Sum and SumArea.",
                        collectionType = AggregationFunction.class
                    )
                    Set<AggregationFunction> functions,
            @DescribeParameter(
                        name = "singlePass",
                        description =
                                "If True computes all aggregation values in a single pass (this will defeat DBMS-specific optimizations)",
                        defaultValue = "false"
                    )
                    boolean singlePass,
            @DescribeParameter(
                        name = "groupByAttributes",
                        min = 0,
                        description = "List of group by attributes",
                        collectionType = String.class
                    )
                    List<String> groupByAttributes,
            ProgressListener progressListener)
            throws ProcessException, IOException {

        if (groupByAttributes != null && !groupByAttributes.isEmpty()) {
            // this request as group by attributes which need special care
            return handleGroupByVisitor(
                    features, aggAttribute, functions, groupByAttributes, progressListener);
        }

        int attIndex = -1;
        List<AttributeDescriptor> atts = features.getSchema().getAttributeDescriptors();
        for (int i = 0; i < atts.size(); i++) {
            if (atts.get(i).getLocalName().equals(aggAttribute)) {
                attIndex = i;
                break;
            }
        }

        if (attIndex == -1) {
            throw new ProcessException(
                    "Could not find attribute "
                            + "["
                            + aggAttribute
                            + "] "
                            + " the valid values are "
                            + attNames(atts));
        }
        if (functions == null) {
            throw new NullPointerException("Aggregate function to call is required");
        }
        List<AggregationFunction> functionList = new ArrayList<AggregationFunction>(functions);
        List<FeatureCalc> visitors = new ArrayList<FeatureCalc>();

        for (AggregationFunction function : functionList) {
            FeatureCalc calc;
            if (function == AggregationFunction.Average) {
                calc = new AverageVisitor(attIndex, features.getSchema());
            } else if (function == AggregationFunction.Count) {
                calc = new CountVisitor();
            } else if (function == AggregationFunction.Max) {
                calc = new MaxVisitor(attIndex, features.getSchema());
            } else if (function == AggregationFunction.Median) {
                calc = new MedianVisitor(attIndex, features.getSchema());
            } else if (function == AggregationFunction.Min) {
                calc = new MinVisitor(attIndex, features.getSchema());
            } else if (function == AggregationFunction.StdDev) {
                calc =
                        new StandardDeviationVisitor(
                                CommonFactoryFinder.getFilterFactory(null).property(aggAttribute));
            } else if (function == AggregationFunction.Sum) {
                calc = new SumVisitor(attIndex, features.getSchema());
            } else if (function == AggregationFunction.SumArea) {
                calc = new SumAreaVisitor(attIndex, features.getSchema());
            } else {
                throw new ProcessException("Uknown method " + function);
            }
            visitors.add(calc);
        }

        EnumMap<AggregationFunction, Number> results =
                new EnumMap<AggregationFunction, Number>(AggregationFunction.class);
        if (singlePass) {
            AggregateFeatureCalc calc = new AggregateFeatureCalc(visitors);
            features.accepts(calc, new NullProgressListener());
            List<CalcResult> resultList = (List<CalcResult>) calc.getResult().getValue();
            for (int i = 0; i < functionList.size(); i++) {
                CalcResult result = resultList.get(i);
                if (result != null) {
                    results.put(functionList.get(i), (Number) result.getValue());
                }
            }
        } else {
            for (int i = 0; i < functionList.size(); i++) {
                final FeatureCalc calc = visitors.get(i);
                features.accepts(calc, new NullProgressListener());
                results.put(functionList.get(i), (Number) calc.getResult().getValue());
            }
        }

        return new Results(aggAttribute, functions, results);
    }

    /**
     * Helper method that handle requests that have group by attributes by wrapping the functions in
     * group by visitors.
     */
    private Results handleGroupByVisitor(
            SimpleFeatureCollection features,
            String aggAttribute,
            Set<AggregationFunction> functions,
            List<String> rawGroupByAttributes,
            ProgressListener progressListener)
            throws IOException {
        // building a group by visitor for every aggregate function

        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);

        List<GroupByVisitor> groupByVisitors =
                functions
                        .stream()
                        .map(
                                function ->
                                        new GroupByVisitorBuilder()
                                                .withAggregateAttribute(
                                                        function == AggregationFunction.SumArea
                                                                ? factory.function(
                                                                        "area2",
                                                                        factory.property(
                                                                                features.getSchema()
                                                                                        .getDescriptor(
                                                                                                aggAttribute)
                                                                                        .getLocalName()))
                                                                : factory.property(
                                                                        features.getSchema()
                                                                                .getDescriptor(
                                                                                        aggAttribute)
                                                                                .getLocalName()))
                                                .withAggregateVisitor(function.name())
                                                .withGroupByAttributes(
                                                        rawGroupByAttributes, features.getSchema())
                                                .withProgressListener(progressListener)
                                                .build())
                        .collect(Collectors.toList());
        // visiting the features collection with each visitor
        for (GroupByVisitor visitor : groupByVisitors) {
            features.accepts(visitor, progressListener);
        }
        // extracting the results from each group by visitor
        List<Map<List<Object>, Object>> results =
                groupByVisitors
                        .stream()
                        .map(visitor -> (Map<List<Object>, Object>) visitor.getResult().toMap())
                        .collect(Collectors.toList());
        return new Results(
                aggAttribute,
                functions,
                rawGroupByAttributes,
                mergeResults(results, rawGroupByAttributes.size()));
    }

    /**
     * Helper method that merge all group by visitors results in a tabular format. Each line of the
     * table is composed of the group by attributes values and the aggregation functions results.
     */
    private List<Object[]> mergeResults(
            List<Map<List<Object>, Object>> results, int groupByAttributesNumber) {
        List<Object[]> mergedResults = new ArrayList<>();
        if (results.isEmpty()) {
            // no results so nothing to do
            return mergedResults;
        }
        // the size of each line is the number of the group by attributes plus the number of
        // aggregation functions
        int resultSize = groupByAttributesNumber + results.size();
        // the group by attributes values are equal for all the visitors so we use the first visitor
        // result to grab all the group by values
        for (List<Object> groupByAttributes : results.get(0).keySet()) {
            // we create the table line that will contains all the results
            Object[] mergedResult = Arrays.copyOf(groupByAttributes.toArray(), resultSize);
            // we extract from each group by visitor result the aggregation function result and add
            // it to out table line
            for (int i = 0; i < results.size(); i++) {
                mergedResult[groupByAttributesNumber + i] = results.get(i).get(groupByAttributes);
            }
            // we add the current line to the table
            mergedResults.add(mergedResult);
        }
        return mergedResults;
    }

    private List<String> attNames(List<AttributeDescriptor> atts) {
        List<String> result = new ArrayList<String>();
        for (AttributeDescriptor ad : atts) {
            result.add(ad.getLocalName());
        }
        return result;
    }

    /**
     * Runs various {@link FeatureCalc} in a single pass
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class AggregateFeatureCalc implements FeatureCalc {
        List<FeatureCalc> delegates;

        public AggregateFeatureCalc(List<FeatureCalc> delegates) {
            super();
            this.delegates = delegates;
        }

        public CalcResult getResult() {
            final List<CalcResult> results = new ArrayList<CalcResult>();
            for (FeatureCalc delegate : delegates) {
                results.add(delegate.getResult());
            }

            return new AbstractCalcResult() {
                @Override
                public Object getValue() {
                    return results;
                }
            };
        }

        public void visit(Feature feature) {
            for (FeatureCalc delegate : delegates) {
                delegate.visit(feature);
            }
        }
    }

    /** The aggregate function results */
    public static final class Results {
        Double min;
        Double max;
        Double median;
        Double average;
        Double standardDeviation;
        Double sum;
        Double area;
        Long count;

        // this values are used by output formats that want to add more meta information (the JSON
        // tabular output format for example)
        String aggregateAttribute;
        Set<AggregationFunction> functions;
        List<String> groupByAttributes;
        List<Object[]> groupByResult;
        EnumMap<AggregationFunction, Number> results;

        // this constructor is used to output group by results
        public Results(
                String aggregateAttribute,
                Set<AggregationFunction> functions,
                List<String> groupByAttributes,
                List<Object[]> groupByResult) {
            this.aggregateAttribute = aggregateAttribute;
            this.functions = functions;
            this.groupByAttributes = groupByAttributes;
            this.groupByResult = groupByResult;
        }

        // this constructor is used to output normal aggregations results
        public Results(
                String aggregateAttribute,
                Set<AggregationFunction> functions,
                EnumMap<AggregationFunction, Number> results) {
            this.aggregateAttribute = aggregateAttribute;
            this.functions = functions;
            this.results = results;
            min = toDouble(results.get(AggregationFunction.Min));
            max = toDouble(results.get(AggregationFunction.Max));
            median = toDouble(results.get(AggregationFunction.Median));
            average = toDouble(results.get(AggregationFunction.Average));
            standardDeviation = toDouble(results.get(AggregationFunction.StdDev));
            sum = toDouble(results.get(AggregationFunction.Sum));
            area = toDouble(results.get(AggregationFunction.SumArea));
            Number nc = results.get(AggregationFunction.Count);
            if (nc != null) {
                count = nc.longValue();
            }
        }

        Double toDouble(Number number) {
            if (number == null) {
                return null;
            } else {
                return number.doubleValue();
            }
        }

        public Double getMin() {
            return min;
        }

        public Double getMax() {
            return max;
        }

        public Double getMedian() {
            return median;
        }

        public Double getAverage() {
            return average;
        }

        public Double getStandardDeviation() {
            return standardDeviation;
        }

        public Double getSum() {
            return sum;
        }

        public Double getArea() {
            return area;
        }

        public Long getCount() {
            return count;
        }

        public String getAggregateAttribute() {
            return aggregateAttribute;
        }

        public Set<AggregationFunction> getFunctions() {
            return functions;
        }

        public List<String> getGroupByAttributes() {
            return groupByAttributes;
        }

        public List<Object[]> getGroupByResult() {
            return groupByResult;
        }

        public EnumMap<AggregationFunction, Number> getResults() {
            return results;
        }
    }
}
