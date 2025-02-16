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

import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.feature.NameImpl;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Function that selects a JSON object using a JSON path. This function implements the {@link PropertyName} interface
 * for compatibility with the filtering processing stack.
 */
public final class JsonSelectFunction extends FunctionExpressionImpl implements PropertyName {

    private static final NamespaceSupport NAMESPACE_SUPPORT = new NamespaceSupport();

    private static final FunctionName DEFINITION = new FunctionNameImpl("jsonSelect", parameter("path", String.class));

    public JsonSelectFunction() {
        super(DEFINITION);
    }

    /**
     * Evaluates this function against the provided object. If a NULL object is provided and attribute expression will
     * be returned.
     */
    @Override
    public Object evaluate(Object object) {
        // get the JSOn object
        String path = (String) this.params.get(0).evaluate(object);
        if (object instanceof MongoCollectionFeature) {
            // in the case of a mongo collection we need to append the parent path
            String parentPath = ((MongoCollectionFeature) object).getCollectionPath();
            // full path of the referenced property
            path = parentPath + "." + path;
        }
        if (object == null) {
            // return an attribute describing this property using the JSON path
            return new AttributeExpressionImpl(new NameImpl(path));
        }
        // simple case, just return the value from the object that corresponds to the JSON path
        return MongoComplexUtilities.getValue(object, path);
    }

    /** Returns the JSON path to be selected. */
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
