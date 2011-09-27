/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.raster.gs;

import java.io.IOException;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.renderer.lite.gridcoverage2d.RasterSymbolizerHelper;
import org.geotools.renderer.lite.gridcoverage2d.SubchainStyleVisitorCoverageProcessingAdapter;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Style;

/**
 * Applies a raster symbolizer to the coverage
 * 
 * @author Andrea Aime - GeoSolutions
 * @author ETj <etj at geo-solutions.it>
 *
 * @source $URL$
 */
@DescribeProcess(title = "styleCoverage", description = "Applies a raster symbolizer to the coverage")
public class StyleCoverage implements GSProcess {

    @DescribeResult(name = "result", description = "The styled raster")
    public GridCoverage2D execute(
            @DescribeParameter(name = "coverage", description = "The raster to be styled") GridCoverage2D coverage,
            @DescribeParameter(name = "style", description = "A SLD style containing a raster symbolizer") Style style)
            throws IOException {
        // TODO: perform a lookup in the entire style?
        final RasterSymbolizer symbolizer = (RasterSymbolizer) style.featureTypeStyles().get(0)
                .rules().get(0).symbolizers().get(0);

        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(coverage,
                null);
        rsh.visit(symbolizer);
        GridCoverage2D g = ((GridCoverage2D) rsh.execute()).geophysics(false);
        return g;
    }

}
