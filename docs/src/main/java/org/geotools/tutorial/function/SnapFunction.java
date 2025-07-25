/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.tutorial.function;

import java.util.List;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.linearref.LinearLocation;
import org.locationtech.jts.linearref.LocationIndexedLine;

/**
 * Quick function that illustrates snapping to a line.
 *
 * @author jody
 */
public class SnapFunction implements Function {

    static FunctionName NAME = new FunctionNameImpl(
            "snap",
            Point.class,
            FunctionNameImpl.parameter("point", Point.class),
            FunctionNameImpl.parameter("line", Geometry.class));

    private final List<Expression> parameters;

    private final Literal fallback;

    public SnapFunction(List<Expression> parameters, Literal fallback) {
        if (parameters == null) {
            throw new NullPointerException("parameters required");
        }
        if (parameters.size() != 2) {
            throw new IllegalArgumentException("snap( point, line) requires two parameters only");
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
        Expression pointExpression = parameters.get(0);
        Point point = pointExpression.evaluate(object, Point.class);

        Expression lineExpression = parameters.get(1);
        Geometry line = lineExpression.evaluate(object, Geometry.class);

        LocationIndexedLine index = new LocationIndexedLine(line);

        LinearLocation location = index.project(point.getCoordinate());

        Coordinate snap = index.extractPoint(location);

        Point pt = point.getFactory().createPoint(snap);

        return Converters.convert(pt, context); // convert to requested format
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
