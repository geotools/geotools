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
import javax.media.jai.KernelJAI;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.jaitools.media.jai.kernel.KernelFactory;
import org.opengis.parameter.ParameterValueGroup;

@DescribeProcess(
        title = "Convolve Coverage",
        description = "Returns a convoluted version of a given raster")
public class ConvolveCoverageProcess implements RasterProcess {

    private final CoverageProcessor PROCESSOR = new CoverageProcessor();

    @DescribeResult(name = "result", description = "Convoluted raster")
    public GridCoverage2D execute(
            @DescribeParameter(name = "data", description = "Input raster") GridCoverage2D coverage,
            @DescribeParameter(name = "kernel", description = "Convolution kernel", min = 0)
                    KernelJAI kernel,
            @DescribeParameter(
                            name = "kernelRadius",
                            description = "Radius for a circular kernel",
                            min = 0)
                    Integer kernelRadius,
            @DescribeParameter(
                            name = "kernelWidth",
                            description = "Width for rectangular kernel",
                            min = 0)
                    Integer kernelWidth,
            @DescribeParameter(
                            name = "kernelHeight",
                            description = "Height for rectangular kernel",
                            min = 0)
                    Integer kernelHeight)
            throws IOException {
        final ParameterValueGroup param = PROCESSOR.getOperation("Convolve").getParameters();

        param.parameter("Source").setValue(coverage);

        if (kernel == null) {
            if (kernelRadius != null && kernelRadius > 0) {
                // circular kernel
                kernel = KernelFactory.createCircle(kernelRadius);
            } else if (kernelWidth != null) {
                if (kernelHeight == null) {
                    kernelHeight = kernelWidth;
                }

                kernel = KernelFactory.createRectangle(kernelWidth, kernelHeight);
            }
        }

        if (kernel == null) {
            throw new ProcessException("No kernel argument specified");
        }

        param.parameter("kernel").setValue(kernel);

        return (GridCoverage2D) PROCESSOR.doOperation(param);
    }
}
