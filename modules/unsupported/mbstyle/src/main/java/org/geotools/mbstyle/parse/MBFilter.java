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
import java.util.List;

import org.json.simple.JSONArray;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * MBFilter json wrapper, allowing conversion to a GeoTools Filter.
 * <p>
 * This wrapper class is used by {@link MBObjectParser} to generate rule filters when transforming
 * MBStyle.
 * </p>
 * <p>
 * This warpper and {@link MBFunction} are a matched set handling dynamic data.
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
    final protected MBObjectParser parse;

    final protected JSONArray json;

    private FilterFactory2 ff;

    public MBFilter(JSONArray json) {
        this(new MBObjectParser(MBFilter.class), json);
    }

    public MBFilter(MBObjectParser parse, JSONArray json) {
        this.parse = parse;
        this.ff = parse.getFilterFactory();

        this.json = json;
    }

    /**
     * Generate GeoTools {@link Filter} from json definition.
     * <p>
     * This filter specifying conditions on source features. Only features that match the filter are
     * displayed.
     * </p>
     * 
     * @return GeoTools {@link Filter} specifying conditions on source features.
     */
    Filter filter() {
        if (json == null || json.isEmpty()) {
            return Filter.INCLUDE; // by default include everything!
        }
        String operator = parse.get(json, 0);
        
        //
        // TYPE
        //
        if(("==".equals(operator) || "!=".equals(operator) ||
                "in".equals(operator) || "!in".equals(operator))&&
                "$type".equals(parse.get(json, 1))){
            throw new UnsupportedOperationException("$type Point,LineString,Polygon comparisions not yet supported");
        }
        if(("==".equals(operator) || "!=".equals(operator) ||
                "has".equals(operator) || "!has".equals(operator) ||
                "in".equals(operator) || "!in".equals(operator))&&
                "$id".equals(parse.get(json, 1))){
            throw new UnsupportedOperationException("$id comparisions not yet supported");
        }
        // ID
        //
        
        //
        // Feature Property
        //
        
        // Existential Filters
        if( "has".equals(operator)){
            String key = parse.get(json, 1);
            return ff.isNull(ff.property(key)); // null is the same as no value present
        }
        else if( "!has".equals(operator)){
            String key = parse.get(json, 1);
            return ff.not(ff.isNull(ff.property(key)));
        }
        // Comparison Filters
        else if( "==".equals(operator)){
            String key = parse.get(json, 1);
            Object value = parse.value(json,2);
            return ff.equal(ff.property(key),ff.literal(value), false);
        }
        else if( "!=".equals(operator)){
            String key = parse.get(json, 1);
            Object value = parse.value(json,2);
            return ff.notEqual(ff.property(key),ff.literal(value), false);
        }
        else if( ">".equals(operator)){
            String key = parse.get(json, 1);
            Object value = parse.value(json,2);
            return ff.greater(ff.property(key),ff.literal(value), false);
        }
        else if( ">=".equals(operator)){
            String key = parse.get(json, 1);
            Object value = parse.value(json,2);
            return ff.greaterOrEqual(ff.property(key),ff.literal(value), false);
        }
        else if( "<".equals(operator)){
            String key = parse.get(json, 1);
            Object value = parse.value(json,2);
            return ff.less(ff.property(key),ff.literal(value), false);
        }
        else if( "<=".equals(operator)){
            String key = parse.get(json, 1);
            Object value = parse.value(json,2);
            return ff.lessOrEqual(ff.property(key),ff.literal(value), false);
        }
        // Set Membership Filters
        else if( "in".equals(operator)){
            String key = parse.get(json, 1);
            Expression[] args = new Expression[ json.size()-2];
            args[0] = ff.property(key);
            for(int i=1; i<json.size()-2;i++){
                Object value = parse.value( json,1+i);
                args[i] = ff.literal( value );
            }
            Function in = ff.function("in", args );
            return ff.equal( in, ff.literal(true));
        }
        else if( "!in".equals(operator)){
            String key = parse.get(json, 1);
            Expression[] args = new Expression[ json.size()-2];
            args[0] = ff.property(key);
            for(int i=1; i<json.size()-2;i++){
                Object value = parse.value( json,1+i);
                args[i] = ff.literal( value );
            }
            Function in = ff.function("in", args );
            return ff.equal( in, ff.literal(false));
        }
        // Combining Filters
        else if( "all".equals(operator)){
            List<Filter> all = new ArrayList<>();
            for( int i = 1; i < json.size();i++){
                MBFilter filter = new MBFilter((JSONArray) json.get(i));
                all.add( filter.filter() );
            }
            return ff.and(all);
        }
        else if( "any".equals(operator)){
            List<Filter> any = new ArrayList<>();
            for( int i = 1; i < json.size();i++){
                MBFilter filter = new MBFilter((JSONArray) json.get(i));
                any.add( filter.filter() );
            }
            return ff.or(any);
        }
        else if( "none".equals(operator)){
            List<Filter> none = new ArrayList<>();
            for( int i = 1; i < json.size();i++){
                // using not here so we can short circuit the and filter below

                MBFilter filter = new MBFilter((JSONArray) json.get(i));
                none.add( ff.not(filter.filter()));
            }
            return ff.and(none);
        }
        else {
            throw new MBFormatException("Unsupported filter "+json);
        }
    }
}
