/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.factory;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import org.geotools.referencing.CRS.AxisOrder;
import org.locationtech.jts.geom.Geometry;

@DescribeProcess(title = "Defaults", description = "Process used to test default value processing")
public class DefaultsProcess {

    static final String GREET_DEFAULT = "Hello!";

    @DescribeResults({
        @DescribeResult(name = "string", type = String.class),
        @DescribeResult(name = "geometry", type = Geometry.class),
        @DescribeResult(name = "int", type = Integer.class),
        @DescribeResult(name = "double", type = Double.class),
        @DescribeResult(name = "axisOrder", type = AxisOrder.class),
        @DescribeResult(name = "short", type = Short.class),
        @DescribeResult(name = "greet", type = String.class),
        @DescribeResult(name = "rect", type = Rectangle.class)
    })
    public Map<String, Object> execute(
            // default converters usage
            @DescribeParameter(name = "string", defaultValue = "default string") String string,
            @DescribeParameter(name = "geometry", defaultValue = "POINT(0 0)") Geometry geometry,
            @DescribeParameter(name = "int", defaultValue = "1", minValue = -1d, maxValue = 2d)
                    int i,
            @DescribeParameter(
                            name = "double",
                            defaultValue = "0.65e-10",
                            minValue = -1.5,
                            maxValue = 2.5)
                    double d,
            // checking out enum conversion
            @DescribeParameter(name = "axisOrder", defaultValue = "EAST_NORTH") AxisOrder axisOrder,
            // reference to a constant in the target type
            @DescribeParameter(name = "short", defaultValue = "MAX_VALUE") short s,
            // reference to a constant in the process
            @DescribeParameter(name = "greet", defaultValue = "GREET_DEFAULT") String greet,
            // absolute reference to constant
            @DescribeParameter(
                            name = "rect",
                            defaultValue =
                                    "org.geotools.process.factory.BeanProcessFactoryTest#DEFAULT_RECTANGLE")
                    Rectangle rect) {
        Map<String, Object> results = new HashMap<>();
        results.put("string", string);
        results.put("geometry", geometry);
        results.put("int", i);
        results.put("double", d);
        results.put("axisOrder", axisOrder);
        results.put("short", s);
        results.put("greet", greet);
        results.put("rect", rect);

        return results;
    }
}
