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

import java.io.Serializable;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.expression.Expression;

public class ParameterMappingExpressionValue implements ParameterMapping, Serializable {
    private String expressionLanguage;
    private String expression;

    private transient Expression cqlExpression;
    private String parameterName;
    private boolean forcible;

    public ParameterMappingExpressionValue(String name, String language, String expression) {
        setParameterName(name);
        setExpressionLanguage(language);
        setExpression(expression);
    }

    public ParameterMappingExpressionValue() {}

    public void setForcible(boolean forcible) {
        this.forcible = forcible;
    }

    @Override
    public boolean isForcible() {
        return forcible;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public String getParameterName() {
        return parameterName;
    }

    public void setExpressionLanguage(String expressionLanguage) {
        this.expressionLanguage = expressionLanguage;
    }

    public String getExpressionLanguage() {
        return expressionLanguage;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    private Expression ensureExpression() {
        if (cqlExpression != null) return cqlExpression;

        try {
            cqlExpression =
                    CQL.toExpression(expression, new ParameterCQLExpressionFilterFactoryImpl());
        } catch (CQLException ce) {
            throw new IllegalArgumentException("Illegal CQL expression", ce);
        }

        return cqlExpression;
    }

    public String evaluate(ParameterMappingContext mappingContext) {

        Object obj = ensureExpression().evaluate(mappingContext);

        String ret;

        if (obj == null) {
            ret = null;
        } else if (obj instanceof String) {
            ret = (String) obj;
        } else {
            ret = obj.toString();
        }
        return ret;
    }
}
