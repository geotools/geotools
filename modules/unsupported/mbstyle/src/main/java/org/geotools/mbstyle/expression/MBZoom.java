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

/** This class is here to get zoom level properties from a map */
public class MBZoom extends MBExpression {
    public MBZoom(JSONArray json) {
        super(json);
    }

    /**
     * Gets the current zoom level. Note that in style layout and paint properties, ["zoom"] may
     * only appear as the input to a top-level "step" or "interpolate" expression.
     *
     * <p>Example: ["zoom]: number
     *
     * <p>Note: MBStyle is currently only supporting srid ESPG:3857
     */
    private Expression mbZoom() {
        return ff.function(
                "zoomLevel",
                ff.function("env", ff.literal("wms_scale_denominator")),
                ff.literal("EPSG:3857"));
    }

    @Override
    public Expression getExpression() throws MBFormatException {
        switch (name) {
            case "zoom":
                return mbZoom();
            default:
                throw new MBFormatException(name + " is an unsupported zoom expression");
        }
    }
}
