/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.And;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.BinaryLogicOperator;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.IncludeFilter;
import org.geotools.api.filter.Not;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsGreaterThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.PropertyIsLessThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNil;
import org.geotools.api.filter.PropertyIsNotEqualTo;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.expression.Add;
import org.geotools.api.filter.expression.Divide;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.Multiply;
import org.geotools.api.filter.expression.NilExpression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.Subtract;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.api.filter.temporal.After;
import org.geotools.api.filter.temporal.AnyInteracts;
import org.geotools.api.filter.temporal.Before;
import org.geotools.api.filter.temporal.Begins;
import org.geotools.api.filter.temporal.BegunBy;
import org.geotools.api.filter.temporal.During;
import org.geotools.api.filter.temporal.EndedBy;
import org.geotools.api.filter.temporal.Ends;
import org.geotools.api.filter.temporal.Meets;
import org.geotools.api.filter.temporal.MetBy;
import org.geotools.api.filter.temporal.OverlappedBy;
import org.geotools.api.filter.temporal.TContains;
import org.geotools.api.filter.temporal.TEquals;
import org.geotools.api.filter.temporal.TOverlaps;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * Encodes a compliant filter to the "restricted where" syntax supported by OGR:
 *
 * <pre>
 * @condition@ = @field_name@ @binary_operator@ @value@ | "(" @condition@ ")" @binary_logical_operator@
 *             "(" @condition@ ")"
 * @binary_operator@ = "<" | ">" | "<=" | ">=" | "<>" | "="
 * @binary_logical_operator@ = "AND" | "OR"
 * @field_name@ = @string_token@
 * @value@ = @string_token@ | @numeric_value@ | @string_value@
 * @string_value@ = "'" @characters@ "'"
 * </pre>
 *
 * Implementation wise this is a widely cut down version of JDBC's module FilterToSQL
 *
 * @author Andrea Aime - GeoSolutions
 */
class FilterToRestrictedWhere implements FilterVisitor, ExpressionVisitor {

    static final Logger LOGGER = Logging.getLogger(FilterToRestrictedWhere.class);

    /** error message for exceptions */
    protected static final String IO_ERROR = "io problem writing filter";

    /** the schema the encoder will be used to be encode sql for */
    protected SimpleFeatureType featureType;

    StringWriter out = new StringWriter();

    public FilterToRestrictedWhere(SimpleFeatureType featureType) {
        this.featureType = featureType;
    }

    public String getRestrictedWhere() {
        return out.toString();
    }

    /**
     * Writes the SQL for the PropertyIsBetween Filter.
     *
     * @param filter the Filter to be visited.
     * @throws RuntimeException for io exception with writer
     */
    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) throws RuntimeException {
        Expression expr = filter.getExpression();
        Expression lowerbounds = filter.getLowerBoundary();
        Expression upperbounds = filter.getUpperBoundary();

        Class context;
        AttributeDescriptor attType = (AttributeDescriptor) expr.evaluate(featureType);
        if (attType != null) {
            context = attType.getType().getBinding();
        } else {
            context = String.class;
        }

        out.write("((");
        expr.accept(this, extraData);
        out.write(">=");
        lowerbounds.accept(this, context);
        out.write(") AND (");
        expr.accept(this, extraData);
        out.write("<=");
        upperbounds.accept(this, context);
        out.write("))");

        return extraData;
    }

    /**
     * Write the SQL for an And filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(And filter, Object extraData) {
        return visit((BinaryLogicOperator) filter, "AND");
    }

    /**
     * Write the SQL for an Or filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(Or filter, Object extraData) {
        return visit((BinaryLogicOperator) filter, "OR");
    }

    /**
     * Common implementation for BinaryLogicOperator filters. This way they're all handled
     * centrally.
     *
     * @param filter the logic statement to be turned into SQL.
     * @param extraData extra filter data. Not modified directly by this method.
     */
    protected Object visit(BinaryLogicOperator filter, Object extraData) {
        String type = (String) extraData;
        out.write("(");

        Iterator<Filter> list = filter.getChildren().iterator();
        while (list.hasNext()) {
            list.next().accept(this, extraData);

            if (list.hasNext()) {
                out.write(" " + type + " ");
            }
        }
        out.write(")");
        return extraData;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator(filter, "=");
        return extraData;
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator(filter, ">=");
        return extraData;
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        visitBinaryComparisonOperator(filter, ">");
        return extraData;
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        visitBinaryComparisonOperator(filter, "<");
        return extraData;
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator(filter, "<=");
        return extraData;
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator(filter, "!=");
        return extraData;
    }

    protected void visitBinaryComparisonOperator(BinaryComparisonOperator filter, Object extraData)
            throws RuntimeException {
        Expression left = filter.getExpression1();
        Expression right = filter.getExpression2();

        // see if we can get some indication on how to evaluate literals
        Class leftContext = null, rightContext = null;
        if (left instanceof PropertyName) {
            AttributeDescriptor attType = (AttributeDescriptor) left.evaluate(featureType);
            if (attType != null) {
                rightContext = attType.getType().getBinding();
            }
        }
        if (rightContext == null && right instanceof PropertyName) {
            AttributeDescriptor attType = (AttributeDescriptor) right.evaluate(featureType);
            if (attType != null) {
                leftContext = attType.getType().getBinding();
            }
        }

        String type = (String) extraData;
        left.accept(this, leftContext);
        out.write(" " + type + " ");
        right.accept(this, rightContext);
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) throws RuntimeException {
        AttributeDescriptor attribute = null;
        try {
            attribute = (AttributeDescriptor) expression.evaluate(featureType);
        } catch (Exception e) {
            // just log and fall back on just encoding propertyName straight up
            LOGGER.log(Level.FINE, "Error occurred mapping " + expression + " to feature type", e);
        }
        String name = null;
        if (attribute != null) {
            name = attribute.getLocalName();
        } else {
            name = expression.getPropertyName();
        }
        out.write(name);

        return extraData;
    }

    /**
     * Export the contents of a Literal Expresion
     *
     * @param expression the Literal to export
     * @throws RuntimeException for io exception with writer
     */
    @Override
    public Object visit(Literal expression, Object context) throws RuntimeException {
        // type to convert the literal to
        Class target = null;
        if (context instanceof Class) {
            target = (Class) context;
        }

        // evaluate the expression
        Object literal = evaluateLiteral(expression, target);
        writeLiteral(literal);
        return context;
    }

    protected Object evaluateLiteral(Literal expression, Class<?> target) {
        Object literal = null;

        // HACK: let expression figure out the right value for numbers,
        // since the context is almost always improperly set and the
        // numeric converters try to force floating points to integrals
        // JD: the above is no longer true, so instead do a safe conversion
        if (target != null) {
            // use the target type
            if (Number.class.isAssignableFrom(target)) {
                literal = Converters.convert(
                        expression.evaluate(null), target, new Hints(ConverterFactory.SAFE_CONVERSION, true));
            } else {
                literal = expression.evaluate(null, target);
            }
        }
        // if the target was not known, of the conversion failed, try the
        // type guessing dance literal expression does only for the following
        // method call
        if (literal == null) literal = expression.evaluate(null);

        // if that failed as well, grab the value as is
        if (literal == null) literal = expression.getValue();

        return literal;
    }

    /**
     * Writes out a non null, non geometry literal. The base class properly handles null, numeric
     * and booleans (true|false), and turns everything else into a string. Subclasses are expected
     * to override this shall they need a different treatment (e.g. for dates)
     */
    protected void writeLiteral(Object literal) {
        if (literal == null) {
            out.write("NULL");
        } else if (literal instanceof Number || literal instanceof Boolean) {
            out.write(String.valueOf(literal));
        } else {
            // we don't know what this is, let's convert back to a string
            String encoding = Converters.convert(literal, String.class, null);
            if (encoding == null) {
                // could not convert back to string, use original l value
                encoding = literal.toString();
            }

            // sigle quotes must be escaped to have a valid sql string
            String escaped = encoding.replaceAll("'", "''");
            out.write("'" + escaped + "'");
        }
    }

    @Override
    public Object visit(NilExpression expression, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Add expression, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Divide expression, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Function expression, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Multiply expression, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Subtract expression, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visitNullFilter(Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Not filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(After after, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Before before, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(During during, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        throw new UnsupportedOperationException("Can't encode this expression");
    }
}
