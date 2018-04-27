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

import org.geotools.feature.NameImpl;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

/**
 * Function that selects a JSON object using a JSON path.
 */
public final class JsonSelectFunction extends FunctionExpressionImpl {

    private static final FunctionName DEFINITION = new FunctionNameImpl(
            "jsonSelect", parameter("path", String.class));

    public JsonSelectFunction() {
        super(DEFINITION);
    }

    public Object evaluate(Object object) {
        String path = (String) this.params.get(0).evaluate(object);
        if (object instanceof MongoCollectionFeature) {
            if (!MongoComplexUtilities.useLegacyPaths()) {
                String parentPath = ((MongoCollectionFeature) object).getCollectionPath();
                path = parentPath + "." + path;
            }
        }
        if (object == null) {
            return new AttributeExpressionImpl(new NameImpl(path));
        }
        return MongoComplexUtilities.getValue(object, path);
    }

    /**
     * Return the JSON path to be selected.
     */
    public String getJsonPath() {
        return (String) this.params.get(0).evaluate(null);
    }
}
