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
package org.geotools.data.mongodb.complex;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.feature.NameImpl;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

/** Extracts all the values of a given JSON path. */
public class JsonSelectAllFunction extends FunctionExpressionImpl implements PropertyName {

    private static final NamespaceSupport NAMESPACE_SUPPORT = new NamespaceSupport();

    public static FunctionName DEFINITION =
            new FunctionNameImpl("jsonSelectAll", parameter("path", String.class));

    public JsonSelectAllFunction() {
        super(DEFINITION);
    }

    public Object evaluate(Object object) {
        String path = (String) this.params.get(0).evaluate(object);
        if (object == null) {
            return new AttributeExpressionImpl(new NameImpl(path));
        }
        return MongoComplexUtilities.getValues(object, path);
    }

    public String getJsonPath() {
        return (String) this.params.get(0).evaluate(null);
    }

    @Override
    public String getPropertyName() {
        // returns the json path that corresponds to this property
        return getJsonPath();
    }

    @Override
    public NamespaceSupport getNamespaceContext() {
        // static name space support
        return NAMESPACE_SUPPORT;
    }

    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        if (visitor instanceof FilterAttributeExtractor) {
            // we explicitly handle the attribute extractor filter
            return visitor.visit((PropertyName) this, extraData);
        }
        // proceed with the normal behavior
        return super.accept(visitor, extraData);
    }
}
