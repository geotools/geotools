/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.tutorial.process;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.factory.StaticMethodsProcessFactory;
import org.geotools.text.Text;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.OctagonalEnvelope;

public class ProcessTutorial extends StaticMethodsProcessFactory<ProcessTutorial> {
    // constructor start
    public ProcessTutorial() {
        super(Text.text("Tutorial"), "tutorial", ProcessTutorial.class);
    }
    // constructor end

    // octagonalEnvelope start
    @DescribeProcess(
            title = "Octagonal Envelope",
            description = "Get the octagonal envelope of this Geometry.")
    @DescribeResult(description = "octagonal of geom")
    public static Geometry octagonalEnvelope(@DescribeParameter(name = "geom") Geometry geom) {
        return new OctagonalEnvelope(geom).toGeometry(geom.getFactory());
    }
    // octagonalEnvelope end
}
