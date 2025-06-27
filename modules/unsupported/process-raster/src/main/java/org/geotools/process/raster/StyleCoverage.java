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
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.Style;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.renderer.lite.gridcoverage2d.RasterSymbolizerHelper;
import org.geotools.renderer.lite.gridcoverage2d.SubchainStyleVisitorCoverageProcessingAdapter;

/**
 * Applies a raster symbolizer to the coverage
 *
 * @author Andrea Aime - GeoSolutions
 * @author ETj <etj at geo-solutions.it>
 */
@DescribeProcess(title = "Style Coverage", description = "Styles a raster using a given SLD and raster symbolizer.")
public class StyleCoverage implements RasterProcess {

    @DescribeResult(name = "result", description = "Styled image")
    public GridCoverage2D execute(
            @DescribeParameter(name = "coverage", description = "Input raster") GridCoverage2D coverage,
            @DescribeParameter(
                            name = "style",
                            description = "Styled Layer Descriptor (SLD) style containing a raster symbolizer")
                    Style style)
            throws IOException {
        // TODO: perform a lookup in the entire style?
        final RasterSymbolizer symbolizer = (RasterSymbolizer)
                style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);

        SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(coverage, null);
        rsh.visit(symbolizer);
        return (GridCoverage2D) rsh.execute();
    }
}
