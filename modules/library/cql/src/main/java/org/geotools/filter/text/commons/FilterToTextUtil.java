/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.commons;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.filter.temporal.During;
import org.opengis.geometry.BoundingBox;

/**
 * The method of this utility class allows to build the CQL/ECQL predicate associated to a {@link
 * Filter}.
 *
 * <p>Warning: This component is not published. It is part of module implementation. Client module
 * should not use this feature.
 *
 * @author Mauricio Pazos
 */
public final class FilterToTextUtil {

    private static final Logger LOGGER = Logger.getLogger(FilterToTextUtil.class.getName());

    private FilterToTextUtil() {
        // utility class
    }

    /** Process the possibly user supplied extraData parameter into a StringBuilder. */
    public static StringBuilder asStringBuilder(Object extraData) {
        if (extraData instanceof StringBuilder) {
            return (StringBuilder) extraData;
        }
        return new StringBuilder();
    }

    public static Object buildInclude(Object extraData) {
        StringBuilder output = FilterToTextUtil.asStringBuilder(extraData);
        output.append("INCLUDE");
        return output;
    }

    public static Object buildExclude(Object extraData) {
        StringBuilder output = FilterToTextUtil.asStringBuilder(extraData);
        output.append("EXCLUDE");
        return output;
    }

    /** builds: left predicate AND right predicate */
    public static Object buildBinaryLogicalOperator(
            final String operator,
            FilterVisitor visitor,
            BinaryLogicOperator filter,
            Object extraData) {

        LOGGER.finer("exporting binary logic filter");

        StringBuilder output = asStringBuilder(extraData);
        List<Filter> children = filter.getChildren();
        if (children != null) {
            for (Iterator<Filter> i = children.iterator(); i.hasNext(); ) {
                Filter child = i.next();
                if (child instanceof BinaryLogicOperator) {
                    output.append("(");
                }
                child.accept(visitor, output);
                if (child instanceof BinaryLogicOperator) {
                    output.append(")");
                }
                if (i.hasNext()) {
                    output.append(" ").append(operator).append(" ");
                }
            }
        }
        return output;
    }

    public static Object buildBetween(PropertyIsBetween filter, Object extraData) {
        LOGGER.finer("exporting PropertyIsBetween");

        ExpressionVisitor exprVisitor = new ExpressionToText();

        StringBuilder output = asStringBuilder(extraData);
        PropertyName propertyName = (PropertyName) filter.getExpression();
        propertyName.accept(exprVisitor, output);
        output.append(" BETWEEN ");
        filter.getLowerBoundary().accept(exprVisitor, output);
        output.append(" AND ");
        filter.getUpperBoundary().accept(exprVisitor, output);

        return output;
    }

    public static Object buildNot(FilterVisitor filterToCQL, Not filter, Object extraData) {
        StringBuilder output = asStringBuilder(extraData);
        output.append("NOT (");
        filter.getFilter().accept(filterToCQL, output);
        output.append(")");
        return output;
    }

    /**
     * Builds a comparison predicate inserting the operato1 or operator2 taking into account the
     * PropertyName position in the comparison filter.
     *
     * @param operator an operator
     * @return SringBuffer
     */
    public static Object buildComparison(
            BinaryComparisonOperator filter, Object extraData, String operator) {

        StringBuilder output = asStringBuilder(extraData);

        ExpressionToText visitor = new ExpressionToText();
        Expression expr = filter.getExpression1();
        expr.accept(visitor, output);
        output.append(" ").append(operator).append(" ");
        filter.getExpression2().accept(visitor, output);

        return output;
    }

    public static Object buildIsLike(PropertyIsLike filter, Object extraData) {

        StringBuilder output = asStringBuilder(extraData);

        final String pattern = filter.getLiteral();

        Expression expr = filter.getExpression();

        expr.accept(new ExpressionToText(), output);

        if (filter.isMatchingCase()) {
            output.append(" LIKE ");
        } else {
            output.append(" ILIKE ");
        }

        output.append("'");
        output.append(pattern);
        output.append("'");

        return output;
    }

    public static Object buildIsNull(PropertyIsNull filter, Object extraData) {
        StringBuilder output = asStringBuilder(extraData);

        filter.getExpression().accept(new ExpressionToText(), output);
        output.append(" IS NULL");
        return output;
    }

    public static Object buildBBOX(BBOX filter, Object extraData) {

        StringBuilder output = asStringBuilder(extraData);
        BoundingBox bounds = filter.getBounds();
        output.append("BBOX(");
        output.append(((PropertyName) filter.getExpression1()).getPropertyName());
        output.append(", ");
        output.append(bounds.getMinX());
        output.append(",");
        output.append(bounds.getMinY());
        output.append(",");
        output.append(bounds.getMaxX());
        output.append(",");
        output.append(bounds.getMaxY());
        output.append(")");

        return output;
    }

    public static Object buildDistanceBufferOperation(
            final String geoOperation, DistanceBufferOperator filter, Object extraData) {
        ExpressionToText visitor = new ExpressionToText();

        return buildDistanceBufferOperation(geoOperation, filter, extraData, visitor);
    }

    public static Object buildDistanceBufferOperation(
            String geoOperation,
            DistanceBufferOperator filter,
            Object extraData,
            ExpressionToText visitor) {
        LOGGER.finer("exporting " + geoOperation);
        StringBuilder output = asStringBuilder(extraData);

        output.append(geoOperation).append("(");
        Expression expr = filter.getExpression1();
        expr.accept(visitor, output);
        output.append(", ");
        filter.getExpression2().accept(visitor, output);
        output.append(", ");
        output.append(filter.getDistance());
        output.append(", ");
        output.append(filter.getDistanceUnits());
        output.append(")");

        return output;
    }

    public static Object buildDWithin(DWithin filter, Object extraData) {
        ExpressionToText visitor = new ExpressionToText();

        return buildDWithin(filter, extraData, visitor);
    }

    public static Object buildDWithin(DWithin filter, Object extraData, ExpressionToText visitor) {
        LOGGER.finer("exporting DWITHIN");
        StringBuilder output = asStringBuilder(extraData);

        output.append("DWITHIN(");

        filter.getExpression1().accept(visitor, output);
        output.append(", ");
        filter.getExpression2().accept(visitor, output);
        output.append(", ");
        output.append(filter.getDistance());
        output.append(", ");
        output.append(filter.getDistanceUnits());
        output.append(")");

        return output;
    }

    public static Object buildBinarySpatialOperator(
            final String spatialOperator, final BinarySpatialOperator filter, Object extraData) {

        ExpressionToText visitor = new ExpressionToText();

        return buildBinarySpatialOperator(spatialOperator, filter, extraData, visitor);
    }

    public static Object buildBinarySpatialOperator(
            String spatialOperator,
            BinarySpatialOperator filter,
            Object extraData,
            ExpressionToText visitor) {
        LOGGER.finer("exporting " + spatialOperator);
        StringBuilder output = asStringBuilder(extraData);

        output.append(spatialOperator).append("(");
        Expression expr = filter.getExpression1();

        expr.accept(visitor, output);
        output.append(", ");
        filter.getExpression2().accept(visitor, output);
        output.append(")");

        return output;
    }

    public static Object buildBinaryTemporalOperator(
            final String temporalOperator, BinaryTemporalOperator filter, Object extraData) {

        LOGGER.finer("exporting " + temporalOperator);

        StringBuilder output = asStringBuilder(extraData);

        PropertyName propertyName = (PropertyName) filter.getExpression1();
        ExpressionToText visitor = new ExpressionToText();
        propertyName.accept(visitor, output);

        output.append(" ").append(temporalOperator).append(" ");

        Literal expr2 = (Literal) filter.getExpression2();
        expr2.accept(visitor, output);

        return output;
    }

    public static Object buildDuring(During during, Object extraData) {

        LOGGER.finer("exporting DURING");

        StringBuilder output = asStringBuilder(extraData);

        PropertyName propertyName = (PropertyName) during.getExpression1();
        ExpressionToText visitor = new ExpressionToText();
        propertyName.accept(visitor, output);

        output.append(" DURING ");

        Literal expr2 = (Literal) during.getExpression2();

        expr2.accept(visitor, output);

        return output;
    }
}
