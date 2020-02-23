/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.mbstyle.expression;

import org.geotools.mbstyle.parse.MBFormatException;
import org.json.simple.JSONArray;
import org.opengis.filter.expression.Expression;

public class MBLookup extends MBExpression {

    public MBLookup(JSONArray json) {
        super(json);
    }

    /** Retrieves an item from an array. Example: ["at", number, array]: ItemType */
    public Expression lookupAt() {
        // requires an instance of a "literal" array expression ie. non-expression array
        if (json.size() == 3 && parse.string(json, 2) != null) {
            Expression e = parse.string(json, 2);
            Expression at = parse.string(json, 1);
            return ff.function("at", e, at);
        }
        throw new MBFormatException(
                "The \"at\" expression requires an integer value at index 1, and a literal"
                        + " array value at index 2");
    }

    /**
     * Retrieves a property value from the current feature's properties, or from another object if a
     * second argument is provided. Returns null if the requested property is missing. Example:
     * ["get", string]: value ["get", string, object]: value
     *
     * <p>As a note, the mbstyle requires json objects for lookup, and evaluates the object as such.
     */
    public Expression lookupGet() {
        if (json.size() == 2 || json.size() == 3) {
            if (json.size() == 2) {
                Expression property = parse.string(json, 1);
                return ff.function("property", property);
            }
            if (json.size() == 3) {
                Expression value = parse.string(json, 1);
                Expression object = parse.string(json, 2);
                return ff.function("get", value, object);
            }
        }
        throw new MBFormatException("Expression \"get\" requires a maximum of 2 arguments.");
    }

    /**
     * Tests for the presence of an property value in the current feature's properties, or from
     * another object if a second argument is provided. Example: ["has", string]: boolean ["has",
     * string, object]: boolean
     *
     * <p>As a note, the mbstyle requires json objects for lookup, and evaluates the object as such.
     */
    public Expression lookupHas() {
        if (json.size() == 2 || json.size() == 3) {
            if (json.size() == 2) {
                Expression value = parse.string(json, 1);
                return ff.function("PropertyExists", value);
            }
            if (json.size() == 3) {
                Expression value = parse.string(json, 1);
                Expression object = parse.string(json, 2);
                return ff.function("has", value, object);
            }
        }
        throw new MBFormatException(
                "Expression \"has\" requires 1 or 2 arguments " + json.size() + " arguments found");
    }

    /**
     * Gets the length of an array or string. Example: ["length", string]: number ["length", array]:
     * number
     */
    public Expression lookupLength() {
        Expression e = parse.string(json, 1);
        return ff.function("mbLength", e);
    }

    @Override
    public Expression getExpression() throws MBFormatException {
        switch (name) {
            case "at":
                return lookupAt();
            case "get":
                return lookupGet();
            case "has":
                return lookupHas();
            case "length":
                return lookupLength();
            default:
                throw new MBFormatException(name + " is an unsupported lookup expression");
        }
    }
}
