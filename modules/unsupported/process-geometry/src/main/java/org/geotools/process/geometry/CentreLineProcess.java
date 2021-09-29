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

public class CentreLineProcess extends StaticMethodsProcessFactory<CentreLineProcess> {
    private static final Logger LOG =
            Logger.getLogger("com.ianturton.cookbook.processes.CentreLineProcess");

    @SuppressWarnings("unchecked")
    public CentreLineProcess() {

        super(Text.text("CentreLine"), "CentreLine", CentreLineProcess.class);
    }

    @DescribeProcess(title = "Centre Line", description = "Extract Centre Line of a Polygon")
    @DescribeResult(description = "A Geometry that is the centre line (skeleton) of the input")
    public static Geometry centreLine(
            @DescribeParameter(
                        name = "geometry",
                        description = "The Geometry to extract the centre line from",
                        min = 1,
                        max = 1
                    )
                    Geometry geometry) {
        LOG.info("got " + geometry.getClass());
        Geometry ret = CentreLine.getCentreLine(geometry, 5.0);
        LOG.info("Returning " + ret.getClass());
        return ret;
    }
}
