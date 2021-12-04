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
 */
package org.geotools.process.raster;

import java.io.IOException;
import java.lang.reflect.Array;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.Converters;
import org.opengis.parameter.ParameterValueGroup;

@DescribeProcess(
        title = "Normalize Coverage",
        description = "Normalizes a coverage by dividing values by the max value")
public class NormalizeCoverageProcess implements RasterProcess {

    private final CoverageProcessor PROCESSOR = new CoverageProcessor();

    @DescribeResult(name = "result", description = "Normalized raster")
    public GridCoverage2D execute(
            @DescribeParameter(name = "data", description = "Input raster") GridCoverage2D coverage)
            throws IOException {

        ParameterValueGroup param = PROCESSOR.getOperation("Extrema").getParameters();
        param.parameter("Source").setValue(coverage);

        GridCoverage2D extrema = (GridCoverage2D) PROCESSOR.doOperation(param);
        Object max = extrema.getProperty("maximum");

        // handle zero case
        boolean allZero = true;
        for (int i = 0; i < Array.getLength(max); i++) {
            Object num = Array.get(max, i);
            boolean isZero = num instanceof Number && ((Number) num).doubleValue() == 0d;

            allZero = allZero && isZero;
            if (isZero) {
                Array.set(max, i, Converters.convert(1, num.getClass()));
            }
        }

        if (allZero) {
            // no need to do anything
            return coverage;
        }

        param = PROCESSOR.getOperation("DivideByConst").getParameters();
        param.parameter("source").setValue(coverage);
        param.parameter("constants").setValue(max);

        return (GridCoverage2D) PROCESSOR.doOperation(param);
    }
}
