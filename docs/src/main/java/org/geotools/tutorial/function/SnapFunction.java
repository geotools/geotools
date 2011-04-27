package org.geotools.tutorial.function;

import java.util.List;

import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.Converters;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.linearref.LinearLocation;
import com.vividsolutions.jts.linearref.LocationIndexedLine;

/**
 * Quick function that illustrates snapping to a line.
 * 
 * @author jody
 */
public class SnapFunction implements Function {

static FunctionName NAME = new FunctionNameImpl("snap", "point", "line");

    private final List<Expression> parameters;
    
    private final Literal fallback;
    
    public SnapFunction(List<Expression> parameters, Literal fallback) {
        if (parameters == null) {
            throw new NullPointerException("parameters required");
        }
        if (parameters.size() != 2) {
            throw new IllegalArgumentException(
                    "snap( point, line) requires two parameters only");
        }
        this.parameters = parameters;
        this.fallback = fallback;
    }
    
    public Object evaluate(Object object) {
        return evaluate(object, Point.class);
    }
    
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
    
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }
    
    public String getName() {
        return NAME.getName();
    }
    
    public FunctionName getFunctionName() {
        return NAME;
    }
    
    public List<Expression> getParameters() {
        return parameters;
    }
    
    public Literal getFallbackValue() {
        return fallback;
    }

}
