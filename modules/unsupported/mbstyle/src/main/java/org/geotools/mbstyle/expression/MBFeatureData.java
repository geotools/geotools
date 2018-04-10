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

public class MBFeatureData extends MBExpression {
    public MBFeatureData(JSONArray json) {
        super(json);
    }
    /**
     * Gets the feature's geometry type: Point, MultiPoint, LineString, MultiLineString, Polygon, MultiPolygon.
     * Example: ["geometry-type"]: string
     */

    public Expression featureGeometryType(){
        return null;
    }

    /**
     * Gets the feature's id, if it has one.
     * Example: ["id"]: value
     * @return
     */
    public Expression featureId(){
        return null;
    }

    /**
     * Gets the feature properties object.
     * Note that in some cases, it may be more efficient to use ["get", "property_name"] directly.
     * Example: ["properties"]: object
     * @return
     */
    public Expression featureProperties(){
        return null;
    }

    @Override
    public Expression getExpression() throws MBFormatException {
        switch (name) {
            case "geometry-type":
                return featureGeometryType();
            case "id":
                return featureId();
            case "properties":
                return featureProperties();
            default:
                throw new MBFormatException(name + " is an unsupported string expression");
        }
    }
}
