/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.solr.complex;

import static org.geotools.data.complex.config.AppSchemaDataAccessConfigurator.parseOgcCqlExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.geotools.appschema.filter.FilterFactoryImplReportInvalidProperty;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.config.MultipleValue;
import org.opengis.feature.Feature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;

/** Allows Solr multivalued fields to be used in App-Schema mappings. */
public final class SolrMultipleValue implements MultipleValue {

    private Expression expression;

    private final FilterFactory filterFactory = new FilterFactoryImplReportInvalidProperty();

    public void setExpression(String expression) {
        try {
            this.expression = parseOgcCqlExpression(expression, filterFactory);
        } catch (Exception exception) {
            throw new RuntimeException(
                    String.format("Error parsing target value expression '%s'.", expression),
                    exception);
        }
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String getId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void setFeatureTypeMapping(FeatureTypeMapping featureTypeMapping) {}

    @Override
    public void setAttributeMapping(AttributeMapping attributeMapping) {}

    @Override
    public List<Object> getValues(Feature feature, AttributeMapping attributeMapping) {
        Object value = expression.evaluate(feature);
        if (!(value instanceof String)) {
            return Collections.emptyList();
        }
        List<Object> values = new ArrayList<>();
        String[] parts = ((String) value).split(";");
        values.addAll(Arrays.asList(parts));
        return values;
    }

    @Override
    public Object evaluate(Object object) {
        return expression.evaluate(object);
    }

    @Override
    public <T> T evaluate(Object object, Class<T> context) {
        return expression.evaluate(object, context);
    }

    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return expression.accept(visitor, extraData);
    }
}
