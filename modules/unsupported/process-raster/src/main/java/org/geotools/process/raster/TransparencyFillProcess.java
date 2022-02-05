/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.jaiext.range.Range;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ExtremaDescriptor;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.image.ImageWorker;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.renderer.i18n.Errors;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.util.ProgressListener;

/**
 * A transparency holes-dashes filling process
 *
 * @author Daniele Romagnoli - GeoSolutions
 */
@DescribeProcess(title = "TransparencyFill", description = "Fill transparent pixels")
public class TransparencyFillProcess implements RasterProcess {

    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();

    @DescribeResult(name = "result", description = "The processed coverage")
    public GridCoverage2D execute(
            @DescribeParameter(name = "data", description = "Input coverage")
                    GridCoverage2D coverage,
            @DescribeParameter(
                            name = "width",
                            description = "Width inside which searching for nearest pixel value",
                            min = 0,
                            max = 1)
                    Integer width,
            //            @DescribeParameter(name = "type", description = "Type of filling
            // algorithm", min = 0) FillType type,
            ProgressListener listener)
            throws ProcessException {

        if (coverage == null) {
            throw new ProcessException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "coverage"));
        }
        RenderedImage ri = coverage.getRenderedImage();
        boolean hasTransparency = false;
        Number noData = 0;
        if (ri.getColorModel().hasAlpha()) hasTransparency = true;
        else {
            Range noDataRange = new ImageWorker().extractNoDataProperty(ri);
            if (noDataRange != null) {
                noData = noDataRange.getMin();
                hasTransparency = true;
            }
        }

        if (!hasTransparency) {
            return coverage;
        }
        int numBands = ri.getSampleModel().getNumBands();
        RenderingHints renderingHints = null;

        if (numBands == 4 || numBands == 2) {
            // Looking for statistics on alpha channel
            renderingHints = ImageUtilities.getRenderingHints(ri);
            RenderedOp extremaOp =
                    ExtremaDescriptor.create(ri, null, 1, 1, false, 1, renderingHints);
            double[][] extrema = (double[][]) extremaOp.getProperty("Extrema");
            double[] mins = extrema[0];
            // check if alpha is 255 on every pixel (fully opaque)
            hasTransparency = !(mins[mins.length - 1] == 255); // fully opaque
        }

        if (!hasTransparency) {
            return coverage;
        }
        // Do the transparency fill operation
        final ParameterValueGroup param =
                PROCESSOR.getOperation("TransparencyFill").getParameters();
        param.parameter("source").setValue(coverage);
        param.parameter("noData").setValue(noData);
        param.parameter("width").setValue(width);
        //        if (type != null && type instanceof FillType) {
        //            param.parameter("type").setValue(type);
        //        }
        return (GridCoverage2D) PROCESSOR.doOperation(param);
    }
}
