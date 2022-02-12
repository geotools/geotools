/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.process.geometry;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.factory.StaticMethodsProcessFactory;
import org.geotools.text.Text;
import org.locationtech.jts.geom.Geometry;

/**
 * Based on Vladimir Agafonkin's Algorithm https://www.mapbox.com/blog/polygon-center/
 *
 * @author Ian Turton
 * @author Casper Børgesen
 */
public class PolygonLabelProcess extends StaticMethodsProcessFactory<PolygonLabelProcess> {
    public PolygonLabelProcess() {
        super(Text.text("PolygonLabelProcess"), "polygonlabelprocess", PolygonLabelProcess.class);
    }

    @DescribeProcess(
            title = "Polygon label process",
            description =
                    "Calculate the Pole of accessibility, the most distant interior point in a polygon.")
    @DescribeResult(description = "Pole of accessibility")
    public static Geometry PolyLabeller(
            @DescribeParameter(name = "polygon", description = "Input polygon") Geometry polygon,
            @DescribeParameter(name = "precision", description = "Tolerance") double tolerance) {
        return PolyLabeller.getPolylabel(polygon, tolerance);
    }
}
