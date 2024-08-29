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
 *
 */

package org.geotools.process.geometry;

import java.util.List;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.Converters;
import org.locationtech.jts.awt.PointShapeFactory.Point;
import org.locationtech.jts.geom.Geometry;

public class PolygonLabelFunction implements Function {
    static FunctionName NAME =
            new FunctionNameImpl(
                    "labelPoint",
                    Point.class,
                    FunctionNameImpl.parameter("polygon", Geometry.class),
                    FunctionNameImpl.parameter("tolerance", Double.class));

    private final List<Expression> parameters;

    private final Literal fallback;

    public PolygonLabelFunction(List<Expression> parameters, Literal fallback) {
        if (parameters == null) {
            throw new NullPointerException("parameters required");
        }
        if (parameters.isEmpty() || parameters.size() > 2) {
            throw new IllegalArgumentException(
                    "labelPoint((multi)polygon, tolerance) requires one or two parameters (tolerance is optional)");
        }
        this.parameters = parameters;
        this.fallback = fallback;
    }

    @Override
    public Object evaluate(Object object) {
        return evaluate(object, Point.class);
    }

    @Override
    public <T> T evaluate(Object object, Class<T> context) {
        Expression geometryExpression = parameters.get(0);
        Geometry polygon = geometryExpression.evaluate(object, Geometry.class);

        Double tolerance = null;
        if (parameters.size() == 2) {
            tolerance = parameters.get(1).evaluate(object, Double.class);
        }

        Geometry point = PolyLabeller.getPolylabel(polygon, tolerance);

        return Converters.convert(point, context); // convert to requested format
    }

    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    @Override
    public String getName() {
        return NAME.getName();
    }

    @Override
    public FunctionName getFunctionName() {
        return NAME;
    }

    @Override
    public List<Expression> getParameters() {
        return parameters;
    }

    @Override
    public Literal getFallbackValue() {
        return fallback;
    }
}
