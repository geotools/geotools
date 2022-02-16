/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.geom.AffineTransform;
import javax.media.jai.Interpolation;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.opengis.parameter.ParameterValueGroup;

/**
 * This process computes an Affine transform on the input Coverage. The transformation will set
 * background values in the areas which does not represent valid data.
 *
 * @author Nicola Lagomarsini - GeoSolutions S.A.S.
 */
@DescribeProcess(
        title = "Transformed Coverage",
        description = "Returns the result of an Affine transformation on the input raster.")
public class AffineProcess implements RasterProcess {

    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();

    @DescribeResult(name = "result", description = "Raster transformed by an Affine transformation")
    public GridCoverage2D execute(
            @DescribeParameter(name = "coverage", description = "Input raster", min = 1)
                    GridCoverage2D coverage,
            @DescribeParameter(
                            name = "scalex",
                            description = "Scale parameter for the X direction",
                            min = 0)
                    Double scaleX,
            @DescribeParameter(
                            name = "scaley",
                            description = "Scale parameter for the Y direction",
                            min = 0)
                    Double scaleY,
            @DescribeParameter(
                            name = "shearx",
                            description = "Shear parameter for the X direction",
                            min = 0)
                    Double shearX,
            @DescribeParameter(
                            name = "sheary",
                            description = "Shear parameter for the Y direction",
                            min = 0)
                    Double shearY,
            @DescribeParameter(
                            name = "translatex",
                            description = "Offset parameter for the X direction",
                            min = 0)
                    Double translateX,
            @DescribeParameter(
                            name = "translatey",
                            description = "Offset parameter for the Y direction",
                            min = 0)
                    Double translateY,
            @DescribeParameter(
                            name = "nodata",
                            description = "Output coverage nodata values",
                            min = 0)
                    double[] nodata,
            @DescribeParameter(
                            name = "interpolation",
                            description =
                                    "Interpolation function to use.  Values are NEAREST, BILINEAR, BICUBIC2, BICUBIC",
                            min = 0)
                    Interpolation interp)
            throws ProcessException {

        // //
        //
        // Creation of the Affine transformation
        //
        // //
        double xScale = scaleX != null ? scaleX : 1;
        double yScale = scaleY != null ? scaleY : 1;
        double xShear = shearX != null ? shearX : 0;
        double yShear = shearY != null ? shearY : 0;
        double xTrans = translateX != null ? translateX : 0;
        double yTrans = translateY != null ? translateY : 0;
        AffineTransform transform =
                new AffineTransform(xScale, yShear, xShear, yScale, xTrans, yTrans);

        // //
        //
        // Selection of the Operation parameters
        //
        // //
        final ParameterValueGroup params = PROCESSOR.getOperation("Affine").getParameters();
        // Setting of the Source Coverage
        params.parameter("Source").setValue(coverage);
        // Setting of the Transformation parameter
        params.parameter("transform").setValue(transform);
        // Setting of the background value to use if present
        if (nodata != null) {
            params.parameter("backgroundValues").setValue(nodata);
        }

        // If interpolation is present then it is added as a parameter
        if (interp != null) {
            params.parameter("interpolation").setValue(interp);
        }

        // //
        //
        // Execution of the Operation
        //
        // //
        return (GridCoverage2D) PROCESSOR.doOperation(params);
    }
}
