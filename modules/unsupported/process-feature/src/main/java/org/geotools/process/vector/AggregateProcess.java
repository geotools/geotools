/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.AbstractCalcResult;
import org.geotools.feature.visitor.AverageVisitor;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MedianVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.StandardDeviationVisitor;
import org.geotools.feature.visitor.SumVisitor;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.util.ProgressListener;

/**
 * Computes various attribute statistics over vector data sets.
 * 
 * @author Andrea Aime
 *
 * @source $URL$
 */
@DescribeProcess(title = "Aggregate", description = "Computes one or more aggregation functions on a feature attribute. Functions include Count, Average, Max, Median, Min, StdDev, and Sum.")
public class AggregateProcess implements VectorProcess {
    // the functions this process can handle
    public enum AggregationFunction {
        Count, Average, Max, Median, Min, StdDev, Sum;
    }
    /**
     * Computes various attribute statistics over vector data sets
     * @param features FeatureCollection to aggregate
     * @param aggAttribute target attribute
     * @param functions
     * @param singlePass
     * @param progressListener
     * @return aggregate Results
     */
    public static Results process(SimpleFeatureCollection features, String aggAttribute, Set<AggregationFunction> functions, Boolean singlePass, ProgressListener progressListener) throws ProcessException, IOException {
        AggregateProcess process = new AggregateProcess();
        return process.execute(features, aggAttribute, functions, singlePass, progressListener);
    }
    
    @DescribeResult(name = "result", description = "Aggregation results (one value for each function computed)")
    public Results execute(
            @DescribeParameter(name = "features", description = "Input feature collection") SimpleFeatureCollection features,
            @DescribeParameter(name = "aggregationAttribute", min = 0, description = "Attribute on which to perform aggregation") String aggAttribute,
            @DescribeParameter(name = "function", description = "An aggregate function to compute. Functions include Count, Average, Max, Median, Min, StdDev, and Sum.", collectionType = AggregationFunction.class) Set<AggregationFunction> functions,
            @DescribeParameter(name = "singlePass", description = "If True computes all aggregation values in a single pass (this will defeat DBMS-specific optimizations)", defaultValue = "false") boolean singlePass,
            ProgressListener progressListener) throws ProcessException, IOException {

        int attIndex = -1;
        List<AttributeDescriptor> atts = features.getSchema().getAttributeDescriptors();
        for (int i = 0; i < atts.size(); i++) {
            if (atts.get(i).getLocalName().equals(aggAttribute)) {
                attIndex = i;
                break;
            }
        }

        if (attIndex == -1) {
            throw new ProcessException("Could not find attribute " +
            		"[" + aggAttribute + "] "
                    + " the valid values are " + attNames(atts));
        }
        if (functions == null ){
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
                calc = new StandardDeviationVisitor(CommonFactoryFinder.getFilterFactory(null).property(aggAttribute));
            } else if (function == AggregationFunction.Sum) {
                calc = new SumVisitor(attIndex, features.getSchema());
            } else {
                throw new ProcessException("Uknown method " + function);
            }
            visitors.add(calc);
        }

        EnumMap<AggregationFunction, Number> results = new EnumMap<AggregationFunction, Number>(AggregationFunction.class);
        if (singlePass) {
            AggregateFeatureCalc calc = new AggregateFeatureCalc(visitors);
            features.accepts(calc, new NullProgressListener());
            List<CalcResult> resultList = (List<CalcResult>) calc.getResult().getValue();
            for (int i = 0; i < functionList.size(); i++) {
                CalcResult result = resultList.get(i);
                if(result != null) {
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

        return new Results(results);
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

    /**
     * The aggregate function results
     */
    public static final class Results {
        Double min;
        Double max;
        Double median;
        Double average;
        Double standardDeviation;
        Double sum;
        Long count;
        
        public Results(EnumMap<AggregationFunction, Number> results) {
            min = toDouble(results.get(AggregationFunction.Min));
            max = toDouble(results.get(AggregationFunction.Max));
            median = toDouble(results.get(AggregationFunction.Median));
            average = toDouble(results.get(AggregationFunction.Average));
            standardDeviation = toDouble(results.get(AggregationFunction.StdDev));
            sum = toDouble(results.get(AggregationFunction.Sum));
            Number nc = results.get(AggregationFunction.Count);
            if(nc != null) {
                count = nc.longValue();
            }
        }
        
        Double toDouble(Number number) {
            if(number == null) {
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

        public Long getCount() {
            return count;
        }

    }

}
