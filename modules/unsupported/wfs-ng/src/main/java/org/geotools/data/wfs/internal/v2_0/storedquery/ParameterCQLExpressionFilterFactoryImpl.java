/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.wfs.internal.v2_0.storedquery;

import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.Filters;
import org.geotools.filter.expression.AddImpl;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

/**
 * Stored Query parameters may be configured as CQL expressions. This factory extends the basic
 * CQL language by adding support to string concatenation. The add (+) operand is extended so
 * that if either left hand or right hand value is not a number, the values are concatenated as
 * strings (via toString()).
 * 
 * The following properties are supported:
 * 
 * <pre>
 *  bboxMinX       Filter envelope bounds
 *  bboxMaxX
 *  bboxMinY
 *  bboxMaxY
 *  defaultSRS	   The defaultSRS of the Feature Type in question
 *  viewparam:name View parameter used in original request ('name' is the name of the parameter) 
 * </pre>
 * 
 * @author Sampo Savolainen
 *
 */
public class ParameterCQLExpressionFilterFactoryImpl extends FilterFactoryImpl {

    @Override
    public PropertyName property(String name) {
        if (name.equals("bboxMinX")) {
            return new ParameterCQLExpressionPropertyName(name) {
                @Override
                protected Object get(ParameterMappingContext context) {
                    return context.getBBOX().getMinX();
                }
            };
        } else if (name.equals("bboxMaxX")) {
            return new ParameterCQLExpressionPropertyName(name) {
                @Override
                protected Object get(ParameterMappingContext context) {
                    return context.getBBOX().getMaxX();
                }
            };
        } else if (name.equals("bboxMinY")) {
            return new ParameterCQLExpressionPropertyName(name) {
                @Override
                protected Object get(ParameterMappingContext context) {
                    return context.getBBOX().getMinY();
                }
            };
        } else if (name.equals("bboxMaxY")) {
            return new ParameterCQLExpressionPropertyName(name) {
                @Override
                protected Object get(ParameterMappingContext context) {
                    return context.getBBOX().getMaxY();
                }
            };
        } else if (name.equals("defaultSRS")) {
            return new ParameterCQLExpressionPropertyName(name) {
                @Override
                protected Object get(ParameterMappingContext context) {
                    return context.getFeatureTypeInfo().getDefaultSRS();
                }
            };
        } else if (name.startsWith("viewparam:")) {
            final String paramName = name.substring(10);
            return new ParameterCQLExpressionPropertyName(name) {
                @Override
                protected Object get(ParameterMappingContext context) {
                    return context.getViewParams().get(paramName);
                }
            };
        }

        return super.property(name);
    }

    @Override
    public Add add(Expression expr1, Expression expr2) {
        return new StringConcatenatingAddImpl(expr1,expr2);
    }

    private static class StringConcatenatingAddImpl extends AddImpl
    {
        public StringConcatenatingAddImpl(Expression expr1, Expression expr2) {
            super(expr1, expr2);
        }

        @Override
        public Object evaluate(Object feature) throws IllegalArgumentException {
            ensureOperandsSet();

            Object leftValue = getExpression1().evaluate(feature);
            Object rightValue = getExpression2().evaluate(feature);

            if (leftValue instanceof Number && rightValue instanceof Number) {
                double leftDouble = Filters.number( leftValue );
                double rightDouble = Filters.number( rightValue );
                return number(leftDouble + rightDouble);

            } else {
                return leftValue.toString() + rightValue.toString();
            }

        }

    }

}
