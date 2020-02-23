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

public class MBFeatureData extends MBExpression {

    // static String operators
    private static final String GEOMETRY_TYPE = "geometry-type";
    private static final String ID = "id";
    private static final String PROPERTIES = "properties";

    public MBFeatureData(JSONArray json) {
        super(json);
    }

    /**
     * Gets the feature's geometry type: Point, MultiPoint, LineString, MultiLineString, Polygon,
     * MultiPolygon.Example: ["geometry-type"]: string
     */
    public Expression featureGeometryType() {
        return ff.function("geometryType", ff.function("geometry", ff.literal(true)));
    }

    /** Gets the feature's id, if it has one. Example: ["id"]: value */
    public Expression featureId() {
        return ff.function("id");
    }

    /**
     * Gets the feature properties object. Note that in some cases, it may be more efficient to use
     * ["get", "property_name"] directly. Example: ["properties"]: object
     */
    public Expression featureProperties() {
        // not supported
        throw new UnsupportedOperationException(
                "FeatureData \"properties\" is not currently supported, please use \"[\"get\", <propertyName>]\"");
    }

    @Override
    public Expression getExpression() throws MBFormatException {
        switch (name) {
            case GEOMETRY_TYPE:
                return featureGeometryType();
            case ID:
                return featureId();
            case PROPERTIES:
                return featureProperties();
            default:
                throw new MBFormatException(name + " is an unsupported string expression");
        }
    }
}
