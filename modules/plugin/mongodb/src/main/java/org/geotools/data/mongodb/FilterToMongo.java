/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.data.mongodb.complex.JsonSelectAllFunction;
import org.geotools.data.mongodb.complex.JsonSelectFunction;

/**
 * Visitor responsible for generating a BasicDBObject to use as a MongoDB query.
 *
 * @author Gerald Gay, Data Tactics Corp.
 * @author Alan Mangan, Data Tactics Corp.
 * @author Tom Kunicki, Boundless Spatial Inc. (C) 2011, Open Source Geospatial Foundation (OSGeo)
 * @see The GNU Lesser General Public License (LGPL)
 */
public class FilterToMongo extends AbstractFilterToMongo {

    final CollectionMapper mapper;

    public FilterToMongo(CollectionMapper mapper) {
        super();
        this.mapper = mapper;
    }

    private Class getJsonSelectType(Expression expression) {
        if (expression instanceof JsonSelectFunction) {
            PropertyDescriptor descriptor =
                    featureType.getDescriptor(((JsonSelectFunction) expression).getJsonPath());
            return descriptor == null ? null : descriptor.getType().getBinding();
        }
        if (expression instanceof JsonSelectAllFunction) {
            PropertyDescriptor descriptor =
                    featureType.getDescriptor(((JsonSelectAllFunction) expression).getJsonPath());
            return descriptor == null ? null : descriptor.getType().getBinding();
        }
        return null;
    }

    @Override
    protected Class<?> getValueTypeInternal(Expression e) {

        Class<?> valueType = getJsonSelectType(e);
        if (valueType != null) {
            return valueType;
        }

        if (e instanceof PropertyName && featureType != null) {
            // we should get the value type from the correspondent attribute descriptor
            AttributeDescriptor attType = (AttributeDescriptor) e.evaluate(featureType);
            if (attType != null) {
                valueType = attType.getType().getBinding();
            }
        }

        return valueType;
    }

    @Override
    protected String getGeometryPath() {
        return mapper.getGeometryPath();
    }

    @Override
    protected String getPropertyPath(String prop) {
        return mapper.getPropertyPath(prop);
    }
}
