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

import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

public abstract class ParameterCQLExpressionPropertyName implements PropertyName {

    private final String name;

    public ParameterCQLExpressionPropertyName(String name) {
        this.name = name;
    }

    @Override
    public Object evaluate(Object object) {
        ParameterMappingContext contextObj = (ParameterMappingContext) object;
        return get(contextObj);
    }

    protected abstract Object get(ParameterMappingContext context);

    @Override
    public <T> T evaluate(Object object, Class<T> context) {
        ParameterMappingContext contextObj = (ParameterMappingContext) object;
        return (T) get(contextObj);
    }

    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    @Override
    public String getPropertyName() {
        return name;
    }

    @Override
    public NamespaceSupport getNamespaceContext() {
        return null;
    }
}
