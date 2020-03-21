/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.elasticsearch;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.Operations;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;

class GridCoverageUtil {

    public static GridCoverage2D scale(GridCoverage2D coverage, float width, float height) {
        final RenderedImage renderedImage = coverage.getRenderedImage();
        final Raster renderedGrid = renderedImage.getData();
        float yScale = width / renderedGrid.getWidth();
        float xScale = height / renderedGrid.getHeight();

        final Operations ops = new Operations(null);
        return (GridCoverage2D) ops.scale(coverage, xScale, yScale, 0, 0);
    }

    public static GridCoverage2D crop(GridCoverage2D coverage, Envelope envelope) {
        final CoverageProcessor processor = new CoverageProcessor();

        final ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();

        final GeneralEnvelope crop = new GeneralEnvelope(envelope);
        param.parameter("Source").setValue(coverage);
        param.parameter("Envelope").setValue(crop);

        return (GridCoverage2D) processor.doOperation(param);
    }
}
