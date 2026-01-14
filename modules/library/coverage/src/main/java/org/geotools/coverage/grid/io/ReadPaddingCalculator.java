/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.imagen.Interpolation;
import org.eclipse.imagen.InterpolationNearest;
import org.geotools.api.metadata.spatial.PixelOrientation;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.logging.Logging;

/**
 * Helper class to compute read padding to be applied when reading data for rendering, taking into account interpolation
 * and oversampling.
 */
public class ReadPaddingCalculator {

    /** Default padding to be applied around the requested area to accommodate interpolation needs. */
    public static final int DEFAULT_PADDING = 10;

    private static final Logger LOGGER = Logging.getLogger(ReadPaddingCalculator.class);

    private final int padding;
    private final GridCoverage2DReader reader;
    private final Interpolation interpolation;
    private final String coverageName;

    public ReadPaddingCalculator(GridCoverage2DReader reader, Interpolation interpolation) throws IOException {
        this(reader, interpolation, DEFAULT_PADDING);
    }

    public ReadPaddingCalculator(GridCoverage2DReader reader, Interpolation interpolation, int padding)
            throws IOException {
        this(reader, null, interpolation, padding);
    }

    public ReadPaddingCalculator(
            GridCoverage2DReader reader, String coverageName, Interpolation interpolation, int padding)
            throws IOException {
        this.reader = reader;
        this.interpolation = interpolation;
        this.padding = padding;
        if (coverageName == null)
            coverageName = Optional.ofNullable(reader.getGridCoverageNames())
                    .filter(names -> names.length > 0)
                    .map(names -> names[0])
                    .orElse(null);
        this.coverageName = coverageName;
    }

    /**
     * Computes a new grid geometry padded according to the interpolation needs at the rendering resolution of the
     * requested grid geometry.
     *
     * @param requestedGridGeometry the requested grid geometry
     * @return the padded grid geometry
     * @throws IOException
     */
    public GridGeometry2D padGridGeometry(GridGeometry2D requestedGridGeometry) throws IOException {
        int[] paddings = computeRenderingPaddings(requestedGridGeometry);

        // expand the map raster area
        GridEnvelope2D requestedGridEnvelope = new GridEnvelope2D(requestedGridGeometry.getGridRange2D());
        applyReadGutter(requestedGridEnvelope, paddings[0], paddings[1]);

        // now create the final envelope accordingly
        try {
            return new GridGeometry2D(
                    requestedGridEnvelope,
                    PixelInCell.CELL_CORNER,
                    requestedGridGeometry
                            .getCRSToGrid2D(PixelOrientation.UPPER_LEFT)
                            .inverse(),
                    requestedGridGeometry.getCoordinateReferenceSystem(),
                    null);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected issue while computing updated grid geometry", e);
        }
    }

    /**
     * Computes padding factors at the rendering resolution. In case we are oversampling the reader native resolution,
     * the interpolation might need a few pixels, which might be thousands of pixels at the rendering resolution
     */
    private int[] computeRenderingPaddings(GridGeometry2D requestedGridGeometry) throws IOException {
        MathTransform originalGridToWorld = reader.getOriginalGridToWorld(coverageName, PixelInCell.CELL_CORNER);
        int[] paddings = {this.padding, this.padding};
        double[][] levels = reader.getResolutionLevels(coverageName);

        // cases where the calculation cannot be performed
        if (!(originalGridToWorld instanceof AffineTransform2D)
                || levels == null
                || interpolation instanceof InterpolationNearest) return paddings;

        // scale up padding factors if needed
        ReferencedEnvelope mapExtent = ReferencedEnvelope.reference(requestedGridGeometry.getEnvelope());
        try {
            CoordinateReferenceSystem readerCRS = reader.getCoordinateReferenceSystem(coverageName);
            ReadResolutionCalculator cc = new ReadResolutionCalculator(requestedGridGeometry, readerCRS, levels[0]);
            double[] requestedRes = cc.computeRequestedResolution(mapExtent.transform(readerCRS, true));
            double[] nativeRes = levels[0];

            // if upscaling, we need to pad the requested area
            if (nativeRes[0] > requestedRes[0])
                paddings[0] = (int) Math.round(nativeRes[0] / requestedRes[0] * padding);
            if (nativeRes[1] > requestedRes[1])
                paddings[1] = (int) Math.round(nativeRes[1] / requestedRes[1] * padding);
        } catch (Exception e) {
            LOGGER.log(
                    Level.FINE,
                    "Failed to account for oversampling in padding calculation, will use standard padding",
                    e);
        }

        return paddings;
    }

    private void applyReadGutter(GridEnvelope2D gridRange, int padX, int padY) {
        gridRange.setBounds(
                gridRange.x - padX, gridRange.y - padY, gridRange.width + padX * 2, gridRange.height + padY * 2);
    }
}
