/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import java.util.logging.Logger;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.factory.StaticMethodsProcessFactory;
import org.geotools.text.Text;
import org.locationtech.jts.geom.Geometry;

public class CenterLineProcess extends StaticMethodsProcessFactory<CenterLineProcess> {
    private static final Logger LOG =
            Logger.getLogger("org.geotools.process.geometry.CenterLineProcess");

    public CenterLineProcess() {

        super(Text.text("geo"), "centerLine", CenterLineProcess.class);
    }

    @DescribeProcess(title = "Center Line", description = "Extract Center Line of a Polygon")
    @DescribeResult(
            description =
                    "A geometry that is the longest center line (skeleton) of the input polygon")
    public static Geometry centerLine(
            @DescribeParameter(
                            name = "geometry",
                            description = "The Geometry to extract the center line from",
                            min = 1,
                            max = 1)
                    Geometry geometry,
            @DescribeParameter(
                            name = "tolerance",
                            description =
                                    "%age of perimeter to use for densification/simplification of input/output. Optional, default is 5.0",
                            min = 0,
                            max = 1,
                            minValue = 1.0,
                            maxValue = 100.0)
                    Double tolerance) {
        LOG.fine("got " + geometry.getClass());
        double tol;
        if (tolerance == null) {
            tol = 5;
        } else {
            tol = tolerance.doubleValue();
        }
        Geometry ret = CenterLine.getCenterLine(geometry, tol);
        LOG.fine("Returning " + ret.getClass());
        return ret;
    }
}
