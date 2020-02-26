/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;

/**
 * Returns the output type of the visited expression, taking into account functions output types,
 * property types, and general promotion rules in arithmetic expressions
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ExpressionTypeVisitor implements ExpressionVisitor {

    static final Map<Class<?>, List<Class<?>>> PROMOTIONS =
            new HashMap<Class<?>, List<Class<?>>>() {
                {
                    put(
                            Byte.class,
                            Arrays.asList(
                                    (Class<?>) Byte.class,
                                    Short.class,
                                    Integer.class,
                                    Long.class,
                                    Float.class,
                                    Double.class,
                                    BigInteger.class,
                                    BigDecimal.class));
                    put(
                            Short.class,
                            Arrays.asList(
                                    (Class<?>) Short.class,
                                    Integer.class,
                                    Long.class,
                                    Float.class,
                                    Double.class,
                                    BigInteger.class,
                                    BigDecimal.class));
                    put(
                            Integer.class,
                            Arrays.asList(
                                    (Class<?>) Integer.class,
                                    Long.class,
                                    Float.class,
                                    Double.class,
                                    BigInteger.class,
                                    BigDecimal.class));
                    put(
                            Long.class,
                            Arrays.asList(
                                    (Class<?>) Long.class,
                                    Double.class,
                                    BigInteger.class,
                                    BigDecimal.class));
                    put(
                            Float.class,
                            Arrays.asList((Class<?>) Float.class, Double.class, BigDecimal.class));
                    put(Double.class, Arrays.asList((Class<?>) Double.class, BigDecimal.class));
                    put(
                            BigInteger.class,
                            Arrays.asList((Class<?>) BigInteger.class, BigDecimal.class));
                }
            };

    FeatureType featureType;

    public ExpressionTypeVisitor(FeatureType featureType) {
        this.featureType = featureType;
    }

    @Override
    public Object visit(NilExpression expression, Object extraData) {
        return Object.class;
    }

    @Override
    public Object visit(Add expression, Object extraData) {
        return visitBinaryExpression(expression);
    }

    @Override
    public Object visit(Divide expression, Object extraData) {
        return visitBinaryExpression(expression);
    }

    @Override
    public Object visit(Multiply expression, Object extraData) {
        return visitBinaryExpression(expression);
    }

    @Override
    public Object visit(Subtract expression, Object extraData) {
        return visitBinaryExpression(expression);
    }

    protected Class<?> visitBinaryExpression(BinaryExpression expression) {
        Class<?> type1 = (Class<?>) expression.getExpression1().accept(this, null);
        Class<?> type2 = (Class<?>) expression.getExpression2().accept(this, null);
        return largestType(type1, type2);
    }

    Class<?> largestType(Class<?> type1, Class<?> type2) {
        // start with the easy cases
        if (type1.isAssignableFrom(type2)) {
            return type1;
        } else if (type2.isAssignableFrom(type1)) {
            return type2;
        }

        // consider numbers and promotions
        if (Number.class.isAssignableFrom(type1) && Number.class.isAssignableFrom(type2)) {
            List<Class<?>> c1 = PROMOTIONS.get(type1);
            List<Class<?>> c2 = PROMOTIONS.get(type2);
            if (c1 == null || c2 == null) {
                return Object.class;
            }
            List<Class<?>> intersection = new ArrayList<>(c1);
            intersection.retainAll(c2);
            if (intersection.isEmpty()) {
                return Object.class;
            } else {
                // lowest shared type
                return intersection.get(0);
            }
        }

        // ok, let's get the most specific superclass then...
        return getCommonSuperclass(type1, type2);
    }

    /**
     * Traverses the super-classes trying to find a common superclass. Won't consider interfaces
     * (good enough for the moment, all basic types we handle have a common superclass)
     */
    Class<?> getCommonSuperclass(Class<?> c1, Class<?> c2) {
        Class<?> curr = c1;
        while (!curr.isAssignableFrom(c2)) {
            curr = curr.getSuperclass();
        }
        return curr;
    }

    @Override
    public Object visit(Function expression, Object extraData) {
        FunctionName name = expression.getFunctionName();
        if (name != null && name.getReturn() != null) {
            return name.getReturn().getType();
        } else {
            return Object.class;
        }
    }

    @Override
    public Object visit(Literal expression, Object extraData) {
        if (expression.getValue() != null) {
            return expression.getValue().getClass();
        } else {
            return Object.class;
        }
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        AttributeDescriptor ad = expression.evaluate(featureType, AttributeDescriptor.class);
        if (ad != null) {
            return ad.getType().getBinding();
        } else {
            return Object.class;
        }
    }
}
