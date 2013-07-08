/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Evaluates the CRS of an expression returning geometries by static analysis if possible
 * 
 * @author Andrea Aime - GeoSolutions
 */
class CRSEvaluator implements ExpressionVisitor {

    private SimpleFeatureType schema;

    public CRSEvaluator(SimpleFeatureType schema) {
        this.schema = schema;
    }

    @Override
    public Object visit(Literal expression, Object extraData) {
        Object value = expression.getValue();
        if (value instanceof Geometry) {
            Geometry g = (Geometry) value;
            if (g.getUserData() instanceof CoordinateReferenceSystem) {
                return g.getUserData();
            } else if (g.getSRID() > 0) {
                try {
                    return CRS.decode("EPSG:" + g.getSRID());
                } catch (Exception e) {
                    return null;
                }
            }
        }

        return null;
    }

    @Override
    public Object visit(Function expression, Object extraData) {
        // geometry manipulation functions that we have today don't mess with the
        // crs, but we might have to modify this if we get a function that
        // assigns the CRS

        for (Expression param : expression.getParameters()) {
            Object result = param.accept(this, extraData);
            if (result instanceof CoordinateReferenceSystem) {
                return result;
            }
        }

        return null;
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        AttributeDescriptor ad = expression.evaluate(schema, AttributeDescriptor.class);

        if (ad == null) {
            throw new IllegalArgumentException(
                    "Original feature type does not have a property named "
                            + expression.getPropertyName());
        } else if (ad instanceof GeometryDescriptor) {
            return ((GeometryDescriptor) ad).getCoordinateReferenceSystem();
        }

        return null;
    }

    @Override
    public Object visit(NilExpression expression, Object extraData) {
        return null;
    }

    @Override
    public Object visit(Add expression, Object extraData) {
        return null;
    }

    @Override
    public Object visit(Divide expression, Object extraData) {
        return null;
    }

    @Override
    public Object visit(Multiply expression, Object extraData) {
        return null;
    }

    @Override
    public Object visit(Subtract expression, Object extraData) {
        return null;
    }

}
