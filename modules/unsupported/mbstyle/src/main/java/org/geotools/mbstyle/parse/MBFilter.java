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
import org.json.simple.JSONArray;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.identity.FeatureId;
import org.opengis.style.SemanticType;

/**
 * MBFilter json wrapper, allowing conversion to a GeoTools Filter.
 * <p>
 * This wrapper class is used by {@link MBObjectParser} to generate rule filters when transforming
 * MBStyle.
 * </p>
 * <p>
 * This wrapper and {@link MBFunction} are a matched set handling dynamic data.
 * </p>
 *
 * <h2>About MapBox Filter</h2>
 * <p>
 * A filter selects specific features from a layer. A filter is an array of one of the following
 * forms:
 * </p>
 * <p>
 * Existential Filters
 * </p>
 * <ul>
 * <li><code>["has", key]<code> - feature[key] exists</li>
 * <li><code>["!has", key]</code> - feature[key] does not exist</li>
 * </ul>
 * <p>
 * Comparison Filters:
 * </p>
 * <ul>
 * <li>["==", key, value] equality: feature[key] = value</li>
 * <li>["!=", key, value] inequality: feature[key] ≠ value</li>
 * <li>[">", key, value] greater than: feature[key] > value</li>
 * <li>[">=", key, value] greater than or equal: feature[key] ≥ value</li>
 * <li>["<", key, value] less than: feature[key] < value</li>
 * <li>["<=", key, value] less than or equal: feature[key] ≤ value</li>
 * </ul>
 * <p>
 * Set Memmbership Filters:</p>
 * <ul>
 * <li>["in", key, v0, ..., vn] set inclusion: feature[key] ∈ {v0, ..., vn}</li>
 * <li>["!in", key, v0, ..., vn] set exclusion: feature[key] ∉ {v0, ..., vn}</li>
 * </ul>
 * <p>
 * Combining Filters:</p>
 * <ul>
 * <li>["all", f0, ..., fn] logical AND: f0 ∧ ... ∧ fn</li>
 * <li>["any", f0, ..., fn] logical OR: f0 ∨ ... ∨ fn</li>
 * <li>["none", f0, ..., fn] logical NOR: ¬f0 ∧ ... ∧ ¬fn</li>
 * </ul>
 * <p>
 * A key must be a string that identifies a feature property, or one of the following special keys:</p>
 * <ul>
 * <li>"$type": the feature type. This key may be used with the "==",  "!=", "in", and "!in" operators. Possible values are  "Point", "LineString", and "Polygon".</li>
 * <li>"$id": the feature identifier. This key may be used with the "==",  "!=", "has", "!has", "in", and "!in" operators.</li>
 * </ul>
 *
 * @see Filter
 * @see MBFunction
 */
public class MBFilter {

    public static final String TYPE_POINT = "Point";
    public static final String TYPE_LINE = "LineString";
    public static final String TYPE_POLYGON = "Polygon";

    /** Default semanticType (or null for "geometry"). */
    protected final SemanticType semanticType;

    /** Parser context. */
    protected final MBObjectParser parse;

    /** Wrapped json */
    protected final JSONArray json;

    public MBFilter(JSONArray json) {
        this(json, new MBObjectParser(MBFilter.class));
    }

    public MBFilter(JSONArray json, MBObjectParser parse) {
        this(json, parse, null);
    }

    public MBFilter(JSONArray json, MBObjectParser parse, SemanticType semanticType) {
        this.parse =
                parse == null
                        ? new MBObjectParser(MBFilter.class)
                        : new MBObjectParser(MBFilter.class, parse);
        this.json = json;
        this.semanticType = semanticType;
    }

    /**
     * Translate "$type": the feature type we need This key may be used with the "==", "!=", "in",
     * and "!in" operators. Possible values are "Point", "LineString", and "Polygon".
     *
     * @return
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
     * <p>This method recursively calls itself to handle all and any operators.
     *
     * @param array JSON array defining filter
     * @return SemanticTypes from provided json, may be nested
     */
    Set<SemanticType> semanticTypeIdentifiers(JSONArray array) {
        if (array == null || array.isEmpty()) {
            throw new MBFormatException("MBFilter expected");
        }
        String operator = parse.get(array, 0);
        if (("==".equals(operator)
                        || "!=".equals(operator)
                        || "in".equals(operator)
                        || "!in".equals(operator))
                && "$type".equals(parse.get(array, 1))) {

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
                                "[\"in\",\"$type\", ...] limited to Point, LineString, Polygon: "
                                        + type);
                    }
                }
                if ("==".equals(operator) && types.size() != 1) {
                    throw new MBFormatException(
                            "[\"==\",\"$type\", ...] limited one geometry type, to test more than one use \"in\" operator.");
                }
                return semanticTypes;
            } else if ("!in".equals(operator) || "!=".equals(operator)) {
                Set<SemanticType> semanticTypes =
                        new HashSet<>(Arrays.asList(SemanticType.values()));
                List<?> types = array.subList(2, array.size());
                for (Object type : types) {
                    if (type instanceof String) {
                        String jsonText = (String) type;
                        SemanticType semanticType = translateSemanticType(jsonText);
                        semanticTypes.remove(semanticType);
                    } else {
                        throw new MBFormatException(
                                "[\"!in\",\"$type\", ...] limited to Point, LineString, Polygon: "
                                        + type);
                    }
                }
                if ("!=".equals(operator) && types.size() != 1) {
                    throw new MBFormatException(
                            "[\"!=\",\"$type\", ...] limited one geometry type, to test more than one use \"!in\" operator.");
                }
                return semanticTypes;
            }
        }

        if ("all".equals(operator)) {
            Set<SemanticType> semanticTypes = new HashSet<>();
            for (int i = 1; i < json.size(); i++) {
                JSONArray alternative = (JSONArray) json.get(i);
                Set<SemanticType> types = semanticTypeIdentifiers(alternative);
                if (types.isEmpty()) {
                    continue;
                } else {
                    if (semanticTypes.isEmpty()) {
                        // exactly one alternative is okay
                        semanticTypes.addAll(types);
                    } else {
                        throw new MBFormatException(
                                "Only one \"all\" alternative may be a $type filter");
                    }
                }
            }
            return semanticTypes;
        } else if ("any".equals(operator)) {
            Set<SemanticType> semanticTypes = new HashSet<>();
            for (int i = 1; i < json.size(); i++) {
                Set<SemanticType> types = semanticTypeIdentifiers((JSONArray) json.get(i));
                semanticTypes.addAll(types);
            }
            return semanticTypes;
        } else if ("none".equals(operator)) {
            Set<SemanticType> semanticTypes = new HashSet<>(Arrays.asList(SemanticType.values()));
            for (int i = 1; i < json.size(); i++) {
                Set<SemanticType> types = semanticTypeIdentifiers((JSONArray) json.get(i));
                semanticTypes.removeAll(types);
            }
            return semanticTypes;
        }
        return Collections.emptySet();
    }

    private Filter translateType(String jsonText) {
        final FilterFactory2 ff = parse.getFilterFactory();
        // TODO: How to wildcard geometry
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
                return null;
        }
    }

    /**
     * Translate from json "Point", "LineString", and "Polygon".
     *
     * @param jsonText
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
        final FilterFactory2 ff = parse.getFilterFactory();

        if (json == null || json.isEmpty()) {
            return Filter.INCLUDE; // by default include everything!
        }
        String operator = parse.get(json, 0);
        //
        // TYPE
        //
        if (("==".equals(operator)
                        || "!=".equals(operator)
                        || "in".equals(operator)
                        || "!in".equals(operator))
                && "$type".equals(parse.get(json, 1))) {

            List<Filter> typeFilters = new ArrayList<>();
            List<?> types = json.subList(2, json.size());
            for (Object type : types) {
                Filter typeFilter = null;
                if (type instanceof String) {
                    typeFilter = translateType((String) type);
                }
                if (typeFilter == null) {
                    throw new MBFormatException(
                            "\"$type\" limited to Point, LineString, Polygon: " + type);
                }
                typeFilters.add(typeFilter);
            }
            if ("==".equals(operator)) {
                if (typeFilters.size() != 1) {
                    throw new MBFormatException(
                            "[\"==\",\"$type\", ...] limited one geometry type, to test more than one use \"in\" operator.");
                }
                return typeFilters.get(0);
            }
            if ("!=".equals(operator)) {
                if (typeFilters.size() != 1) {
                    throw new MBFormatException(
                            "[\"!=\",\"$type\", ...] limited one geometry type, to test more than one use \"!in\" operator.");
                }
                return ff.not(typeFilters.get(0));
            }
            if ("in".equals(operator)) {
                return ff.or(typeFilters);
            }
            if ("!in".equals(operator)) {
                return ff.not(ff.or(typeFilters));
            }
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
                && "$id".equals(parse.get(json, 1))) {

            Set<FeatureId> fids = new HashSet<>();
            for (Object value : json.subList(2, json.size())) {
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

        //
        // Feature Property
        //

        // Existential Filters
        if ("has".equals(operator)) {
            String key = parse.get(json, 1);
            return ff.isNull(ff.property(key)); // null is the same as no value present
        } else if ("!has".equals(operator)) {
            String key = parse.get(json, 1);
            return ff.not(ff.isNull(ff.property(key)));
            // Comparison Filters
        } else if ("==".equals(operator)) {
            String key = parse.get(json, 1);
            Object value = parse.value(json, 2);
            return ff.equal(ff.property(key), ff.literal(value), false);
        } else if ("!=".equals(operator)) {
            String key = parse.get(json, 1);
            Object value = parse.value(json, 2);
            return ff.notEqual(ff.property(key), ff.literal(value), false);
        } else if (">".equals(operator)) {
            String key = parse.get(json, 1);
            Object value = parse.value(json, 2);
            return ff.greater(ff.property(key), ff.literal(value), false);
        } else if (">=".equals(operator)) {
            String key = parse.get(json, 1);
            Object value = parse.value(json, 2);
            return ff.greaterOrEqual(ff.property(key), ff.literal(value), false);
        } else if ("<".equals(operator)) {
            String key = parse.get(json, 1);
            Object value = parse.value(json, 2);
            return ff.less(ff.property(key), ff.literal(value), false);
        } else if ("<=".equals(operator)) {
            String key = parse.get(json, 1);
            Object value = parse.value(json, 2);
            return ff.lessOrEqual(ff.property(key), ff.literal(value), false);
            // Set Membership Filters
        } else if ("in".equals(operator)) {
            String key = parse.get(json, 1);
            Expression[] args = new Expression[json.size() - 1];
            args[0] = ff.property(key);
            for (int i = 1; i < args.length; i++) {
                Object value = parse.value(json, i + 1);
                args[i] = ff.literal(value);
            }
            Function in = ff.function("in", args);
            return ff.equals(in, ff.literal(true));
        } else if ("!in".equals(operator)) {
            String key = parse.get(json, 1);
            Expression[] args = new Expression[json.size() - 1];
            args[0] = ff.property(key);
            for (int i = 1; i < args.length; i++) {
                Object value = parse.value(json, i + 1);
                args[i] = ff.literal(value);
            }
            Function in = ff.function("in", args);
            return ff.equals(in, ff.literal(false));
            // Combining Filters
        } else if ("all".equals(operator)) {
            List<Filter> all = new ArrayList<>();
            for (int i = 1; i < json.size(); i++) {
                MBFilter mbFilter = new MBFilter((JSONArray) json.get(i));
                Filter filter = mbFilter.filter();
                if (filter != Filter.INCLUDE) {
                    all.add(filter);
                }
            }
            return ff.and(all);
        } else if ("any".equals(operator)) {
            List<Filter> any = new ArrayList<>();
            for (int i = 1; i < json.size(); i++) {
                MBFilter mbFilter = new MBFilter((JSONArray) json.get(i));
                Filter filter = mbFilter.filter();
                if (filter != Filter.INCLUDE) {
                    any.add(filter);
                }
            }
            return ff.or(any);
        } else if ("none".equals(operator)) {
            List<Filter> none = new ArrayList<>();
            for (int i = 1; i < json.size(); i++) {
                // using not here so we can short circuit the and filter below
                MBFilter mbFilter = new MBFilter((JSONArray) json.get(i));
                Filter filter = mbFilter.filter();
                if (filter != Filter.INCLUDE) {
                    none.add(ff.not(filter));
                }
            }
            return ff.and(none);
        } else {
            throw new MBFormatException("Unsupported filter " + json);
        }
    }
}
