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
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.opengis.filter.expression.Expression;

public class MBLookup extends MBExpression {
    public MBLookup(JSONArray json) {
        super(json);
    }

    /**
     * Retrieves an item from an array.
     * Example:
     *   ["at", number, array]: ItemType
     * @return
     */
    public Expression lookupAt(){ return null;}

    /**
     * Retrieves a property value from the current feature's properties, or from another object if a second argument
     * is provided. Returns null if the requested property is missing.
     * Example:
     *   ["get", string]: value
     *   ["get", string, object]: value
     * @return
     */
    public Expression lookupGet(){ return null;}

    /**
     * Tests for the presence of an property value in the current feature's properties, or from another object
     * if a second argument is provided.
     * Example:
     *   ["has", string]: boolean
     *   ["has", string, object]: boolean
     * @return
     */
    public Expression lookupHas(){ return null;}

    /**
     * Gets the length of an array or string.
     * Example:
     *   ["length", string]: number
     *   ["length", array]: number
     * @return
     */
    public Expression lookupLength(){ return null;}

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
