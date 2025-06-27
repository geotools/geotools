/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.coverage.grid.GridCoverage;
import org.geotools.api.coverage.grid.GridCoverageWriter;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.Position;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.util.ProgressListener;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverageWriter;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.grassraster.core.GrassBinaryRasterWriteHandler;
import org.geotools.gce.grassraster.format.GrassCoverageFormat;
import org.geotools.gce.grassraster.spi.GrassBinaryImageWriterSpi;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Coverage Writer class for writing GRASS raster maps.
 *
 * <p>The class writes a GRASS raster map to a GRASS workspace (see package documentation for further info). The writing
 * is really done via Imageio extended classes.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 3.0
 * @see GrassBinaryImageWriter
 * @see GrassBinaryRasterWriteHandler
 */
public class GrassCoverageWriter extends AbstractGridCoverageWriter implements GridCoverageWriter {
    private File output;
    private ProgressListener monitor = new DummyProgressListener();

    /** Constructor for the {@link GrassCoverageWriter}. */
    public GrassCoverageWriter(Object output) {
        if (output instanceof File) {
            this.output = (File) output;
        } else {
            throw new IllegalArgumentException("Illegal input argument!");
        }
    }

    public void setProgressListener(ProgressListener monitor) {
        this.monitor = monitor;
    }

    /**
     * Writes the {@link GridCoverage2D supplied coverage} to disk.
     *
     * <p>Note that this also takes care to cloes the file handle after writing to disk.
     *
     * @param gridCoverage2D the coverage to write.
     */
    public void writeRaster(GridCoverage2D gridCoverage2D) throws IOException {
        try {
            ReferencedEnvelope env = gridCoverage2D.getEnvelope2D();
            GridEnvelope2D worldToGrid = gridCoverage2D.getGridGeometry().worldToGrid(env);

            double xRes = env.getWidth() / worldToGrid.getWidth();
            double yRes = env.getHeight() / worldToGrid.getHeight();

            JGrassRegion region =
                    new JGrassRegion(env.getMinX(), env.getMaxX(), env.getMinY(), env.getMaxY(), xRes, yRes);

            GrassBinaryImageWriterSpi writerSpi = new GrassBinaryImageWriterSpi();
            GrassBinaryImageWriter writer = new GrassBinaryImageWriter(writerSpi, monitor);
            RenderedImage renderedImage = gridCoverage2D.getRenderedImage();
            writer.setOutput(output, region);
            writer.write(renderedImage);
            writer.dispose();
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    public void writeRaster(GridCoverage2D gridCoverage2D, GeneralParameterValue... params) throws IOException {
        GeneralBounds requestedEnvelope = null;
        Rectangle dim = null;
        JGrassRegion writeRegion = null;
        if (params != null) {
            for (GeneralParameterValue generalParameterValue : params) {
                final ParameterValue<?> param = (ParameterValue<?>) generalParameterValue;
                final String name = param.getDescriptor().getName().getCode();
                if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString())) {
                    final GridGeometry2D gg = (GridGeometry2D) param.getValue();
                    requestedEnvelope = new GeneralBounds((Bounds) gg.getEnvelope2D());
                    dim = gg.getGridRange2D().getBounds();
                    continue;
                }
            }
            if (requestedEnvelope != null && dim != null) {
                Position lowerCorner = requestedEnvelope.getLowerCorner();
                double[] westSouth = lowerCorner.getCoordinate();
                Position upperCorner = requestedEnvelope.getUpperCorner();
                double[] eastNorth = upperCorner.getCoordinate();
                writeRegion =
                        new JGrassRegion(westSouth[0], eastNorth[0], westSouth[1], eastNorth[1], dim.height, dim.width);
            }
        }

        if (writeRegion == null) {
            throw new IOException("Unable to define writing region.");
        }

        GrassBinaryImageWriterSpi writerSpi = new GrassBinaryImageWriterSpi();
        GrassBinaryImageWriter writer = new GrassBinaryImageWriter(writerSpi, monitor);
        RenderedImage renderedImage = gridCoverage2D.getRenderedImage();
        writer.setOutput(output, writeRegion);
        writer.write(renderedImage);
        writer.dispose();
    }

    @Override
    public Format getFormat() {
        return new GrassCoverageFormat();
    }

    @Override
    public void write(GridCoverage coverage, GeneralParameterValue... parameters)
            throws IllegalArgumentException, IOException {
        if (coverage instanceof GridCoverage2D) {
            GridCoverage2D gridCoverage = (GridCoverage2D) coverage;
            // beware a call with no values mean an empty array.
            if (parameters == null || parameters.length == 0) {
                writeRaster(gridCoverage);
            } else {
                writeRaster(gridCoverage, parameters);
            }
        } else {
            throw new IllegalArgumentException("Coverage not a GridCoverage2D");
        }
    }
}
