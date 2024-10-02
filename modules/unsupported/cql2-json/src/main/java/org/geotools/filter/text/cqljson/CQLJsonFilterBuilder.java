/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cqljson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

final class CQLJsonFilterBuilder {

    /** New instance of CQLJsonFilterBuilder */
    public CQLJsonFilterBuilder(final FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    private final FilterFactory filterFactory;

    private static final List<String> ARITHMETIC_OPERATORS;

    static {
        ARITHMETIC_OPERATORS = Arrays.asList("+", "-", "*", "/");
    }

    private String toCompareString(JsonNode jsonNode) throws CQLException {
        if (jsonNode != null && jsonNode.getNodeType() == JsonNodeType.STRING) {
            return jsonNode.textValue();
        } else {
            throw new CQLException("Expected string but got null or other type.");
        }
    }

    private Expression toCharacterExpression(JsonNode jsonNode)
            throws CQLException, IOException, ParseException {
        if (jsonNode != null && jsonNode.getNodeType() == JsonNodeType.STRING) {
            String stringExpression = jsonNode.textValue();
            return filterFactory.literal(stringExpression);
        } else if (jsonNode != null && jsonNode.getNodeType() == JsonNodeType.OBJECT) {
            if (isProperty(jsonNode)) {
                return getPropertyName(jsonNode);
            } else if (isFunction(jsonNode)) {
                return getFunction(jsonNode);
            } else {
                throw new CQLException("Expected character expression but got null or other type.");
            }
        } else {
            throw new CQLException("Expected character expression but got null or other type.");
        }
    }

    private Function getFunction(JsonNode node) throws CQLException, IOException, ParseException {
        if (isFunction(node)) {
            ObjectNode function = (ObjectNode) node.get("function");
            String functionName = function.get("name").textValue();
            List<Expression> expressions = new ArrayList<>();
            ArrayNode args = (ArrayNode) function.get("args");
            for (final JsonNode argNode : args) {
                expressions.add(getExpression(argNode));
            }
            return filterFactory.function(functionName, expressions.toArray(new Expression[0]));
        } else {
            throw new CQLException("Expected function but got null or other type.");
        }
    }

    /**
     * Convert a JSON node to an expression.
     *
     * @param node the JSON node
     * @return the expression
     * @throws CQLException if the node is not an expression
     * @throws IOException if there is an error parsing the JSON
     * @throws ParseException if there is an error parsing the WKT
     */
    public Expression getExpression(JsonNode node)
            throws CQLException, IOException, ParseException {
        Expression expression = null;
        ObjectMapper mapper = new ObjectMapper();
        if (node.getNodeType() != JsonNodeType.OBJECT) {
            expression = filterFactory.literal(mapper.convertValue(node, Object.class));
        } else if (node.getNodeType() == JsonNodeType.ARRAY) {
            throw new CQLException("Geotools filters do not have an array type");
        } else {
            if (isFunction(node)) {
                expression = getFunction(node);
            } else if (isProperty(node)) {
                expression = getPropertyName(node);
            } else if (isGeometry(node)) {
                expression = getGeometry(node);
            } else if (isTime(node)) {
                expression = getTime(node);
            } else if (isArithmetic(node)) {
                expression = getArithmetic(node);
            } else if (isInterval(node)) {
                expression = filterFactory.literal(getInterval(node));
            }
        }
        return expression;
    }

    private List<Expression> getInterval(JsonNode node)
            throws CQLException, IOException, ParseException {
        List<Expression> out = new ArrayList<>();
        Expression expression1 = getExpression(node.get("interval").get(0));
        Expression expression2 = getExpression(node.get("interval").get(0));
        out.add(expression1);
        out.add(expression2);
        return out;
    }

    private Expression getArithmetic(JsonNode argNode) throws CQLException {
        throw new CQLException("arithmetic operators are not supported by Geotools filters");
    }

    private Expression getGeometry(JsonNode node) throws ParseException {
        Geometry geom = null;
        if (node.get("bbox") != null) {
            ArrayNode bbox = (ArrayNode) node.get("bbox");
            StringBuilder stringBuffer = new StringBuilder("POLYGON((");
            stringBuffer.append(bbox.get(0).doubleValue());
            stringBuffer.append(" ");
            stringBuffer.append(bbox.get(1).doubleValue());
            stringBuffer.append(", ");
            stringBuffer.append(bbox.get(0).doubleValue());
            stringBuffer.append(" ");
            stringBuffer.append(bbox.get(3).doubleValue());
            stringBuffer.append(", ");
            stringBuffer.append(bbox.get(2).doubleValue());
            stringBuffer.append(" ");
            stringBuffer.append(bbox.get(3).doubleValue());
            stringBuffer.append(", ");
            stringBuffer.append(bbox.get(2).doubleValue());
            stringBuffer.append(" ");
            stringBuffer.append(bbox.get(1).doubleValue());
            stringBuffer.append(", ");
            stringBuffer.append(bbox.get(0).doubleValue());
            stringBuffer.append(" ");
            stringBuffer.append(bbox.get(1).doubleValue());
            stringBuffer.append("))");
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

            WKTReader reader = new WKTReader(geometryFactory);
            geom = reader.read(stringBuffer.toString());
        } else {
            geom = GeoJSONReader.parseGeometry(node.toString());
        }
        return filterFactory.literal(geom);
    }

    private boolean isArithmetic(JsonNode node) {
        boolean out = false;
        if (node != null && node.getNodeType() == JsonNodeType.OBJECT) {
            if (node.get("op") != null) {
                if (ARITHMETIC_OPERATORS.contains(node.get("op").textValue())) {
                    return true;
                }
            }
        }
        return out;
    }

    private boolean isInterval(JsonNode node) {
        boolean out = false;
        if (node != null && node.getNodeType() == JsonNodeType.OBJECT) {
            if (node.get("interval") != null) {
                return true;
            }
        }
        return out;
    }

    private boolean isTime(JsonNode node) {
        boolean out = false;
        if (node.get("date") != null) {
            return true;
        }
        if (node.get("timestamp") != null) {
            return true;
        }
        return out;
    }

    private Expression getTime(JsonNode node) throws CQLException {
        if (node != null && node.getNodeType() == JsonNodeType.OBJECT) {
            if (node.get("date") != null) {
                return filterFactory.literal(node.get("date").textValue());
            }
            if (node.get("timestamp") != null) {
                return filterFactory.literal(node.get("timestamp").textValue());
            }
        }
        throw new CQLException("date, or time type not found");
    }

    private boolean isGeometry(JsonNode node) {
        boolean out = false;
        if (node != null && node.getNodeType() == JsonNodeType.OBJECT) {
            if (node.get("bbox") != null) {
                return true;
            }
            if (node.get("coordinates") != null) {
                return true;
            }
        }
        return out;
    }

    private boolean isFunction(JsonNode node) {
        boolean out = false;
        if (node != null
                && node.getNodeType() == JsonNodeType.OBJECT
                && node.get("function") != null) {
            return true;
        }
        return out;
    }

    private boolean isProperty(JsonNode node) {
        boolean out = false;
        if (node != null && node.get("property") != null) {
            return true;
        }
        return out;
    }

    private PropertyName getPropertyName(JsonNode node) throws CQLException {
        if (node != null && node.get("property") != null) {
            return filterFactory.property(node.get("property").textValue());
        } else {
            throw new CQLException("Expected property but got null or other type.");
        }
    }

    /**
     * Convert to LIKE FIlter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertLike(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression characterExpression = toCharacterExpression(args.get(0));
        String stringLiteral = toCompareString(args.get(1));
        return filterFactory.like(characterExpression, stringLiteral);
    }

    /**
     * Convert to EQUALS Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertEquals(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.equals(expression1, expression2);
    }

    /**
     * Convert to EQUALS Filter
     *
     * @param expression1
     * @param expression2
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertEquals(Expression expression1, Expression expression2) {
        return filterFactory.equals(expression1, expression2);
    }

    /**
     * Convert to NOT EQUALS Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertNotEquals(ArrayNode args)
            throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.notEqual(expression1, expression2);
    }

    /**
     * Convert to Greater Than Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertGreaterThan(ArrayNode args)
            throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.greater(expression1, expression2);
    }

    /**
     * Convert to Less Than Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertLessThan(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.less(expression1, expression2);
    }

    /**
     * Convert to Greater Than or Equals To Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertGreaterThanOrEq(ArrayNode args)
            throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.greaterOrEqual(expression1, expression2);
    }

    /**
     * Convert to Less Than or Equals To Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertLessThanOrEq(ArrayNode args)
            throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.lessOrEqual(expression1, expression2);
    }

    /**
     * Convert to BETWEEN Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertBetween(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        Expression expression3 = getExpression(args.get(2));
        return filterFactory.between(expression1, expression2, expression3);
    }

    /**
     * Converto IS NULL Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertIsNull(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        return filterFactory.isNull(expression1);
    }

    /**
     * Convert to OR Filter
     *
     * @param cqlJsonCompiler
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertOr(CQLJsonCompiler cqlJsonCompiler, ArrayNode args)
            throws CQLException, IOException, ParseException {
        List<Filter> filters = new ArrayList<>();
        for (JsonNode arg : args) {
            filters.add(cqlJsonCompiler.convertToFilter(arg));
        }
        return filterFactory.or(filters);
    }

    /**
     * Convert to OR Filter
     *
     * @param filters
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertOr(List<Filter> filters) {
        return filterFactory.or(filters);
    }

    /**
     * Convert to AND Filter
     *
     * @param cqlJsonCompiler
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertAnd(CQLJsonCompiler cqlJsonCompiler, ArrayNode args)
            throws CQLException, IOException, ParseException {
        List<Filter> filters = new ArrayList<>();
        for (JsonNode arg : args) {
            filters.add(cqlJsonCompiler.convertToFilter(arg));
        }
        return filterFactory.and(filters);
    }

    /**
     * Convert to AND Filter
     *
     * @param filter1
     * @param filter2
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertAnd(Filter filter1, Filter filter2) {
        return filterFactory.and(filter1, filter2);
    }

    /**
     * Convert to IN Literal Array Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertIn(ArrayNode args) throws CQLException, IOException, ParseException {
        List<Filter> filters = new ArrayList<>();
        Expression expression1 = getExpression(args.get(0));
        ArrayNode inArray = (ArrayNode) args.get(1);
        for (JsonNode inElement : inArray) {
            Expression expression2 = getExpression(inElement);
            filters.add(convertEquals(expression1, expression2));
        }
        return convertOr(filters);
    }

    /**
     * Convert to CONTAINS Geometry Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertContains(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.contains(expression1, expression2);
    }

    /**
     * Convert to CROSSES Geometry Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertCrosses(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.crosses(expression1, expression2);
    }

    /**
     * Convert to DISJOINT Geometry Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertDisjoint(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.disjoint(expression1, expression2);
    }

    /**
     * Convert to EQUALS Geometry Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertSEquals(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.equal(expression1, expression2);
    }

    /**
     * Convert to INTERSECTS Geometry Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertIntersects(ArrayNode args)
            throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.intersects(expression1, expression2);
    }

    /**
     * Convert to Overlaps Geometry Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertOverlaps(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.overlaps(expression1, expression2);
    }

    /**
     * Convert to Touches Geometry Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertTouches(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.touches(expression1, expression2);
    }

    /**
     * Convert to WITHIN Geometry Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertWithin(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.within(expression1, expression2);
    }

    /**
     * Convert to NOT Filter
     *
     * @param cqlJsonCompiler
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertNot(CQLJsonCompiler cqlJsonCompiler, ArrayNode args)
            throws CQLException, IOException, ParseException {
        Filter filter = cqlJsonCompiler.convertToFilter(args.get(0));
        return filterFactory.not(filter);
    }

    /**
     * Convert to AFTER Temporal Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertAfter(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.after(expression1, expression2);
    }

    /**
     * Convert to AFTER Temporal Filter
     *
     * @param expression1
     * @param expression2
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertAfter(Expression expression1, Expression expression2) {
        return filterFactory.after(expression1, expression2);
    }

    /**
     * Convert to BEFORE Temporal Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertBefore(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.before(expression1, expression2);
    }

    /**
     * Convert to BEFORE Temporal Filter
     *
     * @param expression1
     * @param expression2
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertBefore(Expression expression1, Expression expression2) {
        return filterFactory.before(expression1, expression2);
    }

    /**
     * Convert to DISJOINT Temporal Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertTDisjoint(ArrayNode args)
            throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        Filter before = convertBefore(expression1, expression2);
        Filter after = convertAfter(expression1, expression2);
        return convertAnd(before, after);
    }

    /**
     * Convert to DURING Temporal Filter
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertDuring(ArrayNode args) throws CQLException, IOException, ParseException {
        Expression expression1 = getExpression(args.get(0));
        Expression expression2 = getExpression(args.get(1));
        return filterFactory.during(expression1, expression2);
    }

    /**
     * FINISHED BY Temporal Filter Not Supported
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertFinishedBy(ArrayNode args)
            throws CQLException, IOException, ParseException {
        throw new CQLException("Finished by not supported by GeoTools filters");
    }
    /**
     * FINISHING Temporal Filter Not Supported
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertFinishing(ArrayNode args)
            throws CQLException, IOException, ParseException {
        throw new CQLException("Finishing not supported by GeoTools filters");
    }
    /**
     * INTERSECTS Temporal Filter Not Supported
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertTIntersects(ArrayNode args)
            throws CQLException, IOException, ParseException {
        throw new CQLException("TIntersects not supported by GeoTools filters");
    }
    /**
     * MEETS Temporal Filter Not Supported
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertMeets(ArrayNode args) throws CQLException, IOException, ParseException {
        throw new CQLException("Meets not supported by GeoTools filters");
    }
    /**
     * MET BY Temporal Filter Not Supported
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertMetBy(ArrayNode args) throws CQLException, IOException, ParseException {
        throw new CQLException("Met by not supported by GeoTools filters");
    }
    /**
     * OVERLAPPED BY Temporal Filter Not Supported
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertOverlappedBy(ArrayNode args)
            throws CQLException, IOException, ParseException {
        throw new CQLException("Overlapped by not supported by GeoTools filters");
    }
    /**
     * OVERLAPS Temporal Filter Not Supported
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertTOverlaps(ArrayNode args)
            throws CQLException, IOException, ParseException {
        throw new CQLException("Time Overlaps not supported by GeoTools filters");
    }
    /**
     * STARTED BY Temporal Filter Not Supported
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertStartedBy(ArrayNode args)
            throws CQLException, IOException, ParseException {
        throw new CQLException("Started by not supported by GeoTools filters");
    }
    /**
     * STARTS Temporal Filter Not Supported
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertStarts(ArrayNode args) throws CQLException, IOException, ParseException {
        throw new CQLException("Starts not supported by GeoTools filters");
    }
    /**
     * CONTAINED BY Array Filter Not Supported
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertAContainedBy(ArrayNode args)
            throws CQLException, IOException, ParseException {
        throw new CQLException("Array Contained By not supported by GeoTools filters");
    }
    /**
     * CONTAINING Array Filter Not Supported
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertAContaining(ArrayNode args)
            throws CQLException, IOException, ParseException {
        throw new CQLException("Array Containing not supported by GeoTools filters");
    }
    /**
     * EQUALS Array Filter Not Supported
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertArrayEquals(ArrayNode args)
            throws CQLException, IOException, ParseException {
        throw new CQLException("Array Equals not supported by GeoTools filters");
    }
    /**
     * OVERLAPS Array Filter Not Supported
     *
     * @param args
     * @return
     * @throws CQLException
     * @throws IOException
     * @throws ParseException
     */
    public Filter convertAOverlaps(ArrayNode args)
            throws CQLException, IOException, ParseException {
        throw new CQLException("Array Overlaps not supported by GeoTools filters");
    }
}
