/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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
import javax.media.jai.Interpolation;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.opengis.parameter.ParameterValueGroup;

/**
 * Applies a generic scale and translate operation to a coverage
 *
 * @author Andrea Aime - GeoSolutions
 * @author ETj <etj at geo-solutions.it>
 */
@DescribeProcess(
        title = "Scale Coverage",
        description = "Returns a scaled and translated version of a given raster")
public class ScaleCoverage implements RasterProcess {

    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();

    @DescribeResult(name = "result", description = "Scaled raster")
    public GridCoverage2D execute(
            @DescribeParameter(name = "coverage", description = "Input raster")
                    GridCoverage2D coverage,
            @DescribeParameter(name = "xScale", description = "Scale factor along the x axis")
                    double xScale,
            @DescribeParameter(name = "yScale", description = "Scale factor along the y axis")
                    double yScale,
            @DescribeParameter(name = "xTranslate", description = "Offset along the x axis")
                    double xTranslate,
            @DescribeParameter(name = "yTranslate", description = "Offset along the y axis")
                    double yTranslate,
            @DescribeParameter(
                            name = "interpolation",
                            description =
                                    "Interpolation function to use.  Values are NEAREST, BILINEAR, BICUBIC2, BICUBIC",
                            min = 0)
                    Interpolation interpolation)
            throws IOException {
        final ParameterValueGroup param = PROCESSOR.getOperation("Scale").getParameters();

        param.parameter("Source").setValue(coverage);
        param.parameter("xScale").setValue(xScale);
        param.parameter("yScale").setValue(yScale);
        param.parameter("xTrans").setValue(Float.valueOf(0.0f));
        param.parameter("yTrans").setValue(Float.valueOf(0.0f));
        if (interpolation != null) {
            param.parameter("Interpolation").setValue(interpolation);
        }

        return (GridCoverage2D) PROCESSOR.doOperation(param);
    }
}
