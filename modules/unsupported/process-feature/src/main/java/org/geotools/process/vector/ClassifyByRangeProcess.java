/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2017, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.List;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.CategorizeFunction;
import org.geotools.filter.function.RangedClassifier;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.vector.TransformProcess.Definition;
import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * Computes a new attribute to classify another attribute by intervals over vector data sets.
 * 
 * @author Mauro Bartolomeoli
 *
 * @source $URL$
 */
@DescribeProcess(title = "ClassifyByRange", description = "Computes a new attribute to classify another attribute by intervals over vector data sets.")
public class ClassifyByRangeProcess implements VectorProcess {
    
    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
    
    @DescribeResult(name = "result", description = "Classified feature collection")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "features", description = "Input feature collection") SimpleFeatureCollection features,
            @DescribeParameter(name = "classifyOnAttribute", description = "Attribute to be classified using intervals of values.") String classifyOnAttribute,
            @DescribeParameter(name = "thresholds", min = 0, description = "Comma delimited list of thresholds (use this one to specify custom intervals).") String thresholds,
            @DescribeParameter(name = "classifier", min = 0, description = "Classifier type (EqualInterval, Quantile, Jenks). Use with classes to calculate intervals automatically") String classifier,
            @DescribeParameter(name = "classes", min = 0, description = "Classifier # of classes, use with classifier (defaults to 5).") Integer classes,
            @DescribeParameter(name = "include", min = 0, defaultValue = "FALSE", description = "Include or exclude current threshold in the interval.") Boolean include,
            @DescribeParameter(name = "outputAttribute", min = 0, description = "Name of the output attribute with class values (defaults to class).") String outputAttribute,
            @DescribeParameter(name = "outputValues", min = 0, description = "Comma delimited list of class values for each given threshold (+1 for out of range).") String outputValues,
            @DescribeParameter(name = "outputType", min = 0, description = "Optional binding type for output values (defaults to String).") String outputType
            )
            throws ProcessException {
        
        if(features != null) {
            SimpleFeatureType schema = features.getSchema();
            List<Definition> transform = new ArrayList<Definition>();
            for (AttributeDescriptor descriptor : schema.getAttributeDescriptors()) {
                Definition definition = new Definition();
                definition.name = descriptor.getLocalName();
                definition.expression = filterFactory.property(descriptor.getLocalName());
                definition.binding = descriptor.getType().getBinding();
                transform.add(definition);
            }
            
            Definition classify = new Definition();
            if(outputAttribute == null || outputAttribute.trim().isEmpty()) {
                outputAttribute = "class";
            }
            classify.name = outputAttribute;
            try {
                classify.binding = outputType != null ? Class.forName(outputType) : String.class;
            } catch (ClassNotFoundException e) {
                throw new ProcessException(outputType + " is not a valid value for outputType: should be a class name");
            }

            AttributeDescriptor classifyDescriptor = features.getSchema().getDescriptor(classifyOnAttribute);
            if(classifyDescriptor == null) {
                throw new ProcessException("classifyOnAttribute is not a valid schema attribute: " + classifyOnAttribute);
            }
            Class<?> rangeType = classifyDescriptor.getType().getBinding();
            if(rangeType == null) {
                rangeType = Number.class;
            }
            List<Expression> params = new ArrayList<Expression>();
            params.add(filterFactory.property(classifyOnAttribute));
            if((thresholds == null || thresholds.trim().isEmpty()) && (classifier == null || classifier.trim().isEmpty())) {
                throw new ProcessException("at least one of thresholds and classifier is mandatory");
            }
            List<Object> ranges = new ArrayList<Object>();
            if(classifier!= null && !classifier.trim().isEmpty()) {
                if (classes == null) {
                    classes = 5;
                }
                Function classifyFun = filterFactory.function(classifier, filterFactory.property(classifyOnAttribute), 
                        filterFactory.literal(classes));
                RangedClassifier rc = (RangedClassifier)classifyFun.evaluate(features);
                for (int i = 0; i < rc.getSize(); i++) {
                    ranges.add(rc.getMin(i));
                }
                ranges.add(rc.getMax(rc.getSize() - 1));
            } else {
                for(String threshold : thresholds.split(",")) {
                    ranges.add(threshold);
                }
            }
                
            if(outputValues == null || outputValues.trim().isEmpty()) {
                outputValues = "";
                for(int count = 1; count <= ranges.size();count++) {
                    outputValues += count + ",";
                }
                outputValues += (ranges.size() + 1 + "");
            }
            String[] rangesValues = outputValues.split(",");
            if (rangesValues.length != (ranges.size() + 1)) {
                throw new ProcessException("values are not consistent with thresholds (should be +1)");
            }
            for(int count = 0; count < ranges.size(); count++) {
                params.add(filterFactory.literal(Converters.convert(rangesValues[count], classify.binding)));
                params.add(filterFactory.literal(Converters.convert(ranges.get(count), rangeType)));
            }
            params.add(filterFactory.literal(Converters.convert(rangesValues[rangesValues.length - 1], classify.binding)));
            params.add(filterFactory.literal(include ? CategorizeFunction.PRECEDING : CategorizeFunction.SUCCEEDING));
            classify.expression = new CategorizeFunction(params, null);
            
            
            transform.add(classify);
            return new TransformProcess().executeList(features, transform);
        }
        throw new ProcessException("features input cannot be null!");
        
    }
}
