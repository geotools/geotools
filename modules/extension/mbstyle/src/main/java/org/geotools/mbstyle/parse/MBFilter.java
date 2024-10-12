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
package org.geotools.mbstyle.parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.api.style.SemanticType;
import org.geotools.mbstyle.expression.MBExpression;
import org.json.simple.JSONArray;

/**
 * MBFilter json wrapper, allowing conversion to a GeoTools Filter.
 *
 * <p>This wrapper class is used by {@link MBObjectParser} to generate rule filters when
 * transforming MBStyle.
 *
 * <p>This wrapper and {@link MBFunction} are a matched set handling data expression.
 *
 * <h2>Data expression: Decision</h2>
 *
 * <p>Implementation Note: The value for any filter may be specified as an data expression. The result type of an data expression in
 * the filter property must be boolean. See {@link org.geotools.mbstyle.expression.MBExpression} for details.
 *
 * <p>The expressions in this section can be used to add conditional logic to your styles. For example, the 'case' expression
 * provides "if/then/else" logic, and 'match' allows you to map specific values of an input expression to different
 * output expressions.
 * <ul>
 *     <li><code>["!", boolean]: boolean</code></li>
 *     <li><code>["!=", value, value]: boolean</code></li>
 *     <li><code>&lt;/code></li>
 *     <li><code>&lt;=</code></li>
 *     <li><code>==</code></li>
 *     <li><code>&gt;</code></li>
 *     <li><code>&gt;=</code></li>
 *     <li><code>all</code></li>
 *     <li><code>any</code></li>
 *     <li><code>case</code></li>
 *     <li><code>coalesce</code></li>
 *     <li><code>match</code></li>
 *     <li><code>within</code></li>
 * </ul>
 *
 * <h2>Filter Other</h2>
 *
 * <p>Implementation Note: GeoTools also supports the depreciated syntax documented here (provided
 * by a previous versions of the Mapbox style specification).
 *
 * <p>A filter selects specific features from a layer. A filter is an array of one of the following
 * forms:
 *
 * <p>Existential Filters
 *
 * <ul>
 *   <li><code>["has", key]</code> - feature[key] exists
 *   <li><code>["!has", key]</code> - feature[key] does not exist
 * </ul>
 *
 * <p>Comparison Filters:
 *
 * <ul>
 *   <li>["==", key, value] equality: feature[key] = value
 *   <li>["!=", key, value] inequality: feature[key] ≠ value
 *   <li>["&gt;", key, value] greater than: feature[key] &gt; value
 *   <li>["&gt;=", key, value] greater than or equal: feature[key] ≥ value
 *   <li>["&lt;", key, value] less than: feature[key] &lt; value
 *   <li>["&lt;=", key, value] less than or equal: feature[key] ≤ value
 * </ul>
 *
 * <p>Set Membership Filters:
 *
 * <ul>
 *   <li>["in", key, v0, ..., vn] set inclusion: feature[key] ∈ {v0, ..., vn}
 *   <li>["!in", key, v0, ..., vn] set exclusion: feature[key] ∉ {v0, ..., vn}
 * </ul>
 *
 * <p>Combining Filters:
 *
 * <ul>
 *   <li>["all", f0, ..., fn] logical AND: f0 ∧ ... ∧ fn
 *   <li>["any", f0, ..., fn] logical OR: f0 ∨ ... ∨ fn
 *   <li>["none", f0, ..., fn] logical NOR: ¬f0 ∧ ... ∧ ¬fn
 * </ul>
 *
 * <p>A key must be a string that identifies a feature property, or one of the following special
 * keys:
 *
 * <ul>
 *   <li>"$type": the feature type. This key may be used with the "==", "!=", "in", and "!in"
 *       operators. Possible values are "Point", "LineString", and "Polygon".
 *   <li>"$id": the feature identifier. This key may be used with the "==", "!=", "has", "!has",
 *       "in", and "!in" operators.
 * </ul>
 *
 * @see Filter
 * @see MBFunction
 * @see org.geotools.mbstyle.expression.MBExpression
 */
public class MBFilter {

    public static final String TYPE_POINT = "Point";
    public static final String TYPE_LINE = "LineString";
    public static final String TYPE_POLYGON = "Polygon";

    /** Default semanticType (or null for "geometry"). */
    protected final SemanticType semanticType;

    /** Parser context. */
    protected final MBObjectParser parse;

    protected final FilterFactory ff;

    /** Wrapped json */
    protected final JSONArray json;

    public MBFilter(JSONArray json) {
        this(json, new MBObjectParser(MBFilter.class));
    }

    public MBFilter(JSONArray json, MBObjectParser parse) {
        this(json, parse, null);
    }

    public MBFilter(JSONArray json, MBObjectParser parse, SemanticType semanticType) {
        this.parse = parse == null ? new MBObjectParser(MBFilter.class) : new MBObjectParser(MBFilter.class, parse);
        this.ff = this.parse.getFilterFactory();
        this.json = json;
        this.semanticType = semanticType;
    }

    /**
     * Translate "$type": the feature type we need This key may be used with the "==", "!=", "in",
     * and "!in" operators. Possible values are "Point", "LineString", and "Polygon".
     *
     * @return Set of GeoTools SemanticType (Point, LineString, Polygon).
     */
    public Set<SemanticType> semanticTypeIdentifiers() {
        if (json == null || json.isEmpty()) {
            return semanticTypeIdentifiersDefaults();
        }
        Set<SemanticType> semanticTypes = semanticTypeIdentifiers(json);
        return semanticTypes.isEmpty() ? semanticTypeIdentifiersDefaults() : semanticTypes;
    }

    /**
     * Generate default set of semantic types based on {@link #semanticType} default.
     *
     * @return default to use, if nothing is provided explicitly by json "$type" field.
     */
    private Set<SemanticType> semanticTypeIdentifiersDefaults() {
        Set<SemanticType> defaults = new HashSet<>();
        if (semanticType != null) {
            defaults.add(semanticType); // default as provided
        }
        return defaults;
    }

    /**
     * Utility method to convert json to set of {@link SemanticType}.
     *
     * <p>This method is called recursively to handle <code>all</code> and <code>any</code> filter
     * operators.
     *
     * @param array JSON array defining filter
     * @return SemanticTypes from provided json, may be nested
     */
    Set<SemanticType> semanticTypeIdentifiers(JSONArray array) {
        if (array == null || array.isEmpty()) {
            throw new MBFormatException("MBFilter expected");
        }
        String operator = parse.get(array, 0);
        if (("==".equals(operator) || "!=".equals(operator) || "in".equals(operator) || "!in".equals(operator))
                && parse.isString(array, 1)
                && "$type".equals(parse.get(array, 1))) {
            return semanticTypeByGeometryType(array, operator);
        }
        if ("all".equals(operator)) {
            return semanticTypeAll(array);
        } else if ("any".equals(operator)) {
            return semanticTypeAny(array);
        } else if ("none".equals(operator)) {
            return semanticTypeNone(array);
        }
        return Collections.emptySet();
    }

    private Set<SemanticType> semanticTypeNone(JSONArray array) {
        Set<SemanticType> semanticTypes = new HashSet<>(Arrays.asList(SemanticType.values()));
        for (int i = 1; i < array.size(); i++) {
            Set<SemanticType> types = semanticTypeIdentifiers((JSONArray) array.get(i));
            semanticTypes.removeAll(types);
        }
        return semanticTypes;
    }

    private Set<SemanticType> semanticTypeAny(JSONArray array) {
        Set<SemanticType> semanticTypes = new HashSet<>();
        for (int i = 1; i < array.size(); i++) {
            Set<SemanticType> types = semanticTypeIdentifiers((JSONArray) array.get(i));
            semanticTypes.addAll(types);
        }
        return semanticTypes;
    }

    private Set<SemanticType> semanticTypeAll(JSONArray array) {
        Set<SemanticType> semanticTypes = new HashSet<>();
        for (int i = 1; i < array.size(); i++) {
            JSONArray alternative = (JSONArray) array.get(i);
            Set<SemanticType> types = semanticTypeIdentifiers(alternative);
            if (types.isEmpty()) {
                continue;
            }
            if (semanticTypes.isEmpty()) {
                // exactly one alternative is okay
                semanticTypes.addAll(types);
            } else {
                throw new MBFormatException("Only one \"all\" alternative may be a $type filter");
            }
        }
        return semanticTypes;
    }

    private Set<SemanticType> semanticTypeByGeometryType(JSONArray array, String operator) {
        if ("in".equals(operator) || "==".equals(operator)) {
            Set<SemanticType> semanticTypes = new HashSet<>();
            List<?> types = array.subList(2, array.size());
            for (Object type : types) {
                if (type instanceof String) {
                    String jsonText = (String) type;
                    SemanticType semanticType = translateSemanticType(jsonText);
                    semanticTypes.add(semanticType);
                } else {
                    throw new MBFormatException(
                            "[\"in\",\"$type\", ...] limited to Point, LineString, Polygon: " + type);
                }
            }
            if ("==".equals(operator) && types.size() != 1) {
                throw new MBFormatException(
                        "[\"==\",\"$type\", ...] limited one geometry type, to test more than one use \"in\" operator.");
            }
            return semanticTypes;
        } else if ("!in".equals(operator) || "!=".equals(operator)) {
            Set<SemanticType> semanticTypes = new HashSet<>(Arrays.asList(SemanticType.values()));
            List<?> types = array.subList(2, array.size());
            for (Object type : types) {
                if (type instanceof String) {
                    String jsonText = (String) type;
                    SemanticType semanticType = translateSemanticType(jsonText);
                    semanticTypes.remove(semanticType);
                } else {
                    throw new MBFormatException(
                            "[\"!in\",\"$type\", ...] limited to Point, LineString, Polygon: " + type);
                }
            }
            if ("!=".equals(operator) && types.size() != 1) {
                throw new MBFormatException(
                        "[\"!=\",\"$type\", ...] limited one geometry type, to test more than one use \"!in\" operator.");
            }
            return semanticTypes;
        }
        return Collections.emptySet();
    }

    private Filter translateType(String jsonText) {
        Expression dimension = ff.function("dimension", ff.function("geometry"));

        switch (jsonText) {
            case TYPE_POINT:
                return ff.equals(dimension, ff.literal(0));
            case TYPE_LINE:
                return ff.equals(dimension, ff.literal(1));
            case TYPE_POLYGON:
                return ff.and(
                        ff.equals(dimension, ff.literal(2)),
                        ff.not(ff.equals(ff.function("isCoverage"), ff.literal(true))));
            default:
                throw new MBFormatException("MBStyle restricted to testing Point, LineString, Polygon: " + jsonText);
        }
    }

    /**
     * Translate from json "Point", "LineString", and "Polygon".
     *
     * @return translate from jsonText
     */
    private SemanticType translateSemanticType(String jsonText) {
        switch (jsonText) {
            case TYPE_POINT:
                return SemanticType.POINT;
            case TYPE_LINE:
                return SemanticType.LINE;
            case TYPE_POLYGON:
                return SemanticType.POLYGON;
            default:
                return null;
        }
    }

    /**
     * Generate GeoTools {@link Filter} from json definition.
     *
     * <p>This filter specifying conditions on source features. Only features that match the filter
     * are displayed.
     *
     * @return GeoTools {@link Filter} specifying conditions on source features.
     */
    public Filter filter() {
        if (json == null || json.isEmpty()) {
            return Filter.INCLUDE; // by default include everything!
        }
        String operator = parse.get(json, 0);
        //
        // TYPE
        //
        if (("==".equals(operator) || "!=".equals(operator) || "in".equals(operator) || "!in".equals(operator))
                && parse.isString(json, 1)
                && "$type".equals(parse.get(json, 1))) {
            return filterByGeometryType(json, operator);
        }
        //
        // ID
        //
        if (("==".equals(operator)
                        || "!=".equals(operator)
                        || "has".equals(operator)
                        || "!has".equals(operator)
                        || "in".equals(operator)
                        || "!in".equals(operator))
                && parse.isString(json, 1)
                && "$id".equals(parse.get(json, 1))) {
            return filterByFeatureIdentifier(json, operator);
        }

        //
        // Feature Property
        //

        // Existential Filters
        if ("!has".equals(operator)) {
            String key = parse.get(json, 1);
            return ff.isNull(ff.property(key)); // null is the same as no value present
        } else if ("has".equals(operator)) {
            String key = parse.get(json, 1);
            return ff.not(ff.isNull(ff.property(key)));
        }
        // Comparison Filters
        else if ("==".equals(operator)) {
            return filterEqualTo(json);
        } else if ("!=".equals(operator)) {
            return filterNotEqual(json);
        } else if (">".equals(operator)) {
            return filterGreater(json);
        } else if (">=".equals(operator)) {
            return filterGreaterOrEqual(json);
        } else if ("<".equals(operator)) {
            return filterLess(json);
        } else if ("<=".equals(operator)) {
            return filterLessOrEqual(json);
        }
        // Set Membership Filters
        else if ("in".equals(operator)) {
            return filterIn(json, true);
        } else if ("!in".equals(operator)) {
            return filterIn(json, false);
        }
        // Combining Filters
        else if ("all".equals(operator)) {
            return filterAll(json);
        } else if ("any".equals(operator)) {
            return filterAny(json);
        } else if ("none".equals(operator)) {
            return filterNone(json);
        }
        // MBExpression filters
        else if ("case".equals(operator)) {
            Expression caseExpr = MBExpression.transformExpression(json);
            return ff.equals(caseExpr, ff.literal(true));
        } else if ("coalesce".equals(operator)) {
            Expression coalesce = MBExpression.transformExpression(json);
            return ff.equals(coalesce, ff.literal(true));
        } else if ("match".equals(operator)) {
            Expression match = MBExpression.transformExpression(json);
            return ff.equals(match, ff.literal(true));
        } else if ("within".equals(operator)) {
            Expression within = MBExpression.transformExpression(json);
            return ff.equals(within, ff.literal(true));
        } else {
            throw new MBFormatException("Data expression or filter \""
                    + operator
                    + "\" invalid. It may be misspelled or not supported by this implementation:"
                    + json);
        }
    }

    private Filter filterNone(JSONArray array) {
        List<Filter> none = new ArrayList<>();
        for (int i = 1; i < array.size(); i++) {
            if (parse.isArray(array, i)) {
                // using not here so we can short circuit the and filter below
                MBFilter mbFilter = new MBFilter((JSONArray) array.get(i));
                Filter filter = mbFilter.filter();
                if (filter != Filter.INCLUDE) {
                    none.add(ff.not(filter));
                }
            } else {
                throw new MBFormatException("None filter does not support: \"" + json.get(i) + "\"");
            }
        }
        return ff.and(none);
    }

    private Filter filterAny(JSONArray array) {
        List<Filter> any = new ArrayList<>();
        for (int i = 1; i < array.size(); i++) {
            if (parse.isArray(array, i)) {
                MBFilter mbFilter = new MBFilter((JSONArray) array.get(i));
                Filter filter = mbFilter.filter();
                if (filter != Filter.INCLUDE) {
                    any.add(filter);
                }
            } else {
                throw new MBFormatException("Any filter does not support: \"" + json.get(i) + "\"");
            }
        }
        return ff.or(any);
    }

    private Filter filterAll(JSONArray array) {
        List<Filter> all = new ArrayList<>();
        for (int i = 1; i < array.size(); i++) {
            if (parse.isArray(array, i)) {
                MBFilter mbFilter = new MBFilter((JSONArray) array.get(i));
                Filter filter = mbFilter.filter();
                if (filter != Filter.INCLUDE) {
                    all.add(filter);
                }
            } else {
                throw new MBFormatException("All filter does not support: \"" + json.get(i) + "\"");
            }
        }
        return ff.and(all);
    }

    private Filter filterIn(JSONArray array, boolean in) {
        String key = parse.get(array, 1);
        Expression[] args = new Expression[array.size() - 1];
        args[0] = ff.property(key);
        for (int i = 1; i < args.length; i++) {
            Expression expression = parse.string(array, i + 1);
            args[i] = expression;
        }
        Function function = ff.function("in", args);
        return ff.equals(function, ff.literal(in));
    }

    private Filter filterByFeatureIdentifier(JSONArray array, String operator) {
        Set<FeatureId> fids = new HashSet<>();
        for (Object value : array.subList(2, array.size())) {
            if (value instanceof String) {
                String fid = (String) value;
                fids.add(ff.featureId(fid));
            }
        }
        if ("has".equals(operator) || "in".equals(operator)) {
            return ff.id(fids);
        } else if ("!has".equals(operator) || "!in".equals(operator)) {
            return ff.not(ff.id(fids));
        } else {
            throw new UnsupportedOperationException("$id \"" + operator + "\" not valid");
        }
    }

    private Filter filterByGeometryType(JSONArray json, String operator) {
        List<Filter> typeFilters = new ArrayList<>();
        List<?> types = json.subList(2, json.size());
        for (Object type : types) {
            Filter typeFilter = null;
            if (type instanceof String) {
                typeFilter = translateType((String) type);
            }
            if (typeFilter == null) {
                throw new MBFormatException("\"$type\" limited to Point, LineString, Polygon: " + type);
            }
            typeFilters.add(typeFilter);
        }
        if ("==".equals(operator)) {
            if (typeFilters.size() != 1) {
                throw new MBFormatException(
                        "[\"==\",\"$type\", ...] limited one geometry type, to test more than one use \"in\" operator.");
            }
            return typeFilters.get(0);
        } else if ("!=".equals(operator)) {
            if (typeFilters.size() != 1) {
                throw new MBFormatException(
                        "[\"!=\",\"$type\", ...] limited one geometry type, to test more than one use \"!in\" operator.");
            }
            return ff.not(typeFilters.get(0));
        } else if ("in".equals(operator)) {
            return ff.or(typeFilters);
        } else if ("!in".equals(operator)) {
            return ff.not(ff.or(typeFilters));
        } else {
            throw new MBFormatException("Unsupported $type operator \"" + json + "\"");
        }
    }

    /**
     * Returns true if the input values are equal, false otherwise. The inputs must be numbers,
     * strings, or booleans, and both of the same type. Examples: ["==", number, number]: boolean
     * ["==", string, string]: boolean ["==", boolean, boolean]: boolean ["==", null, null]: boolean
     *
     * @return equal to expression
     */
    private Filter filterEqualTo(JSONArray array) {
        if (array.size() != 3) {
            throwUnexpectedArgumentCount("==", 2);
        }
        Expression expression1 = comparisonExpression1(array);
        Expression expression2 = comparisonExpression2(array);
        return ff.equals(expression1, expression2);
    }

    /**
     * Returns true if the input values are not equal, false otherwise. The inputs must be numbers,
     * strings, or booleans, and both of the same type. Examples:["!=", number, number]: boolean
     * ["!=", string, string]: boolean ["!=", boolean, boolean]: boolean ["!=", null, null]: boolean
     *
     * @return Not equals expression
     */
    private Filter filterNotEqual(JSONArray array) {
        if (array.size() != 3) {
            throwUnexpectedArgumentCount("!=", 2);
        }
        Expression expression1 = comparisonExpression1(array);
        Expression expression2 = comparisonExpression2(array);
        return ff.notEqual(expression1, expression2);
    }

    private Filter filterLessOrEqual(JSONArray array) {
        if (json.size() != 3) {
            throwUnexpectedArgumentCount("<=", 2);
        }
        Expression expression1 = comparisonExpression1(array);
        Expression expression2 = comparisonExpression2(array);
        return ff.lessOrEqual(expression1, expression2);
    }

    private Filter filterLess(JSONArray array) {
        if (json.size() != 3) {
            throwUnexpectedArgumentCount("<", 2);
        }
        Expression expression1 = comparisonExpression1(array);
        Expression expression2 = comparisonExpression2(array);
        return ff.less(expression1, expression2);
    }

    private Filter filterGreaterOrEqual(JSONArray array) {
        if (json.size() != 3) {
            throwUnexpectedArgumentCount(">=", 2);
        }
        Expression expression1 = comparisonExpression1(array);
        Expression expression2 = comparisonExpression2(array);
        return ff.greaterOrEqual(expression1, expression2);
    }

    private Filter filterGreater(JSONArray array) {
        if (json.size() != 3) {
            throwUnexpectedArgumentCount(">", 2);
        }
        Expression expression1 = comparisonExpression1(array);
        Expression expression2 = comparisonExpression2(array);
        return ff.greater(expression1, expression2);
    }

    private void throwUnexpectedArgumentCount(String expression, int argCount) throws MBFormatException {
        throw new MBFormatException(
                String.format("Expression \"%s\" should have exactly %d argument(s)", expression, argCount));
    }

    /**
     * Comparison value1 defined as an data expression (or legacy key reference).
     *
     * @param array JSON filter definition
     * @return Expression for comparison
     */
    private Expression comparisonExpression1(JSONArray array) {
        if (parse.isString(array, 1)) { // legacy filter syntax
            String key = parse.get(array, 1);
            return ff.property(key);
        } else {
            return parse.string(array, 1);
        }
    }
    /**
     * Comparison value2 defined as an data expression (or legacy literal reference).
     *
     * @param array JSON filter definition
     * @return Expression for comparison
     */
    private Expression comparisonExpression2(JSONArray array) {
        if (parse.isString(array, 1)) { // legacy filter syntax
            Object value = parse.value(array, 2);
            return ff.literal(value);
        } else {
            return parse.string(array, 2);
        }
    }
}
