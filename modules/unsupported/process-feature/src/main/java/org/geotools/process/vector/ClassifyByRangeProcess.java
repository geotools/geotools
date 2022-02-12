/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
 */
@DescribeProcess(
        title = "ClassifyByRange",
        description =
                "Computes a new attribute to classify another attribute by intervals over vector data sets.")
public class ClassifyByRangeProcess implements VectorProcess {

    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory(null);

    @DescribeResult(name = "result", description = "Classified feature collection")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    SimpleFeatureCollection features,
            @DescribeParameter(
                            name = "classifyOnAttribute",
                            description = "Attribute to be classified using intervals of values.")
                    String classifyOnAttribute,
            @DescribeParameter(
                            name = "thresholds",
                            min = 0,
                            description =
                                    "List of thresholds (use this one to specify custom intervals). Ignored if classifier is specified (use classes in that case).")
                    String[] thresholds,
            @DescribeParameter(
                            name = "outputValues",
                            min = 0,
                            description =
                                    "List of class values for each given threshold (+1 for out of range).")
                    String[] outputValues,
            @DescribeParameter(
                            name = "classifier",
                            min = 0,
                            description =
                                    "Classifier type (EqualInterval, Quantile, Jenks, etc.). Use with classes to calculate intervals automatically")
                    String classifier,
            @DescribeParameter(
                            name = "classes",
                            min = 0,
                            description =
                                    "Classifier # of classes, used when classifier is specified (defaults to 5).")
                    Integer classes,
            @DescribeParameter(
                            name = "include",
                            min = 0,
                            defaultValue = "FALSE",
                            description = "Include or exclude current threshold in the interval.")
                    Boolean include,
            @DescribeParameter(
                            name = "outputAttribute",
                            min = 0,
                            description =
                                    "Name of the output attribute with class values (defaults to class).")
                    String outputAttribute,
            @DescribeParameter(
                            name = "outputType",
                            min = 0,
                            description =
                                    "Optional binding type for output values (defaults to String).")
                    String outputType)
            throws ProcessException {

        if (features == null) {
            throw new ProcessException("features input cannot be null!");
        }
        SimpleFeatureType schema = features.getSchema();
        List<Definition> transform = new ArrayList<>();
        for (AttributeDescriptor descriptor : schema.getAttributeDescriptors()) {
            Definition definition = new Definition();
            definition.name = descriptor.getLocalName();
            definition.expression = FF.property(descriptor.getLocalName());
            definition.binding = descriptor.getType().getBinding();
            transform.add(definition);
        }

        Definition classify = new Definition();
        if (outputAttribute == null || outputAttribute.trim().isEmpty()) {
            outputAttribute = "class";
        }
        classify.name = outputAttribute;
        try {
            classify.binding = outputType != null ? Class.forName(outputType) : String.class;
        } catch (ClassNotFoundException e) {
            throw new ProcessException(
                    outputType + " is not a valid value for outputType: should be a class name");
        }

        AttributeDescriptor classifyDescriptor =
                features.getSchema().getDescriptor(classifyOnAttribute);
        if (classifyDescriptor == null) {
            throw new ProcessException(
                    "classifyOnAttribute is not a valid schema attribute: " + classifyOnAttribute);
        }
        Class<?> rangeType = classifyDescriptor.getType().getBinding();
        if (rangeType == null) {
            rangeType = Number.class;
        }
        List<Expression> params = new ArrayList<>();
        params.add(FF.property(classifyOnAttribute));
        if ((thresholds == null || thresholds.length == 0)
                && (classifier == null || classifier.trim().isEmpty())) {
            throw new ProcessException("at least one of thresholds and classifier is mandatory");
        }
        List<Object> ranges = new ArrayList<>();
        if (classifier != null && !classifier.trim().isEmpty()) {
            if (classes == null) {
                classes = 5;
            }
            Function classifyFun =
                    FF.function(classifier, FF.property(classifyOnAttribute), FF.literal(classes));
            RangedClassifier rc = (RangedClassifier) classifyFun.evaluate(features);
            for (int i = 0; i < rc.getSize(); i++) {
                ranges.add(rc.getMin(i));
            }
            ranges.add(rc.getMax(rc.getSize() - 1));
        } else {
            for (String threshold : thresholds) {
                ranges.add(threshold);
            }
        }

        if (outputValues == null || outputValues.length == 0) {
            outputValues = new String[ranges.size() + 1];
            for (int count = 1; count <= ranges.size(); count++) {
                outputValues[count - 1] = count + "";
            }
            outputValues[ranges.size()] = (ranges.size() + 1 + "");
        }
        if (outputValues.length != (ranges.size() + 1)) {
            throw new ProcessException("values are not consistent with thresholds (should be +1)");
        }
        for (int count = 0; count < ranges.size(); count++) {
            Object outputValue = Converters.convert(outputValues[count], classify.binding);
            if (outputValue == null) {
                throw new ProcessException(
                        "Incompatible output value found "
                                + outputValues[count]
                                + " for type "
                                + classify.binding.getName());
            }
            params.add(FF.literal(outputValue));
            Object rangeValue = Converters.convert(ranges.get(count), rangeType);
            if (rangeValue == null) {
                throw new ProcessException(
                        "Incompatible range value found "
                                + rangeValue
                                + " for type "
                                + rangeType.getName());
            }
            params.add(FF.literal(rangeValue));
        }
        params.add(
                FF.literal(
                        Converters.convert(
                                outputValues[outputValues.length - 1], classify.binding)));
        params.add(
                FF.literal(include ? CategorizeFunction.PRECEDING : CategorizeFunction.SUCCEEDING));
        classify.expression = new CategorizeFunction(params, null);

        transform.add(classify);
        return new TransformProcess().executeList(features, transform);
    }
}
