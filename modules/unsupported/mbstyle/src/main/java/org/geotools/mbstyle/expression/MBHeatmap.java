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

public class MBHeatmap extends MBExpression {

    public MBHeatmap(JSONArray json) {
        super(json);
    }

    // TODO The MBHeatmap layer needs to be implemented

    /**
     * Gets the kernel density estimation of a pixel in a heatmap layer, which is a relative measure
     * of how many data points are crowded around a particular pixel. Can only be used in the
     * heatmap-color property. Example: ["heatmap-density"]: number
     */
    public Expression heatmapDensity() {
        return null;
    }

    @Override
    public Expression getExpression() throws MBFormatException {
        switch (name) {
            case "heatmap-density":
                return heatmapDensity();
            default:
                throw new MBFormatException(name + " is an unsupported heatmap expression");
        }
    }
}
