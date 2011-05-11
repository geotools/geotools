/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.coverage.grid.io.AbstractGridCoverageWriter;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.grassraster.core.GrassBinaryRasterWriteHandler;
import org.geotools.gce.grassraster.format.GrassCoverageFormat;
import org.geotools.gce.grassraster.spi.GrassBinaryImageWriterSpi;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.resources.coverage.CoverageUtilities;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.util.ProgressListener;

/**
 * Coverage Writer class for writing GRASS raster maps.
 * <p>
 * The class writes a GRASS raster map to a GRASS workspace (see package documentation for further
 * info). The writing is really done via Imageio extended classes.
 * </p>
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 3.0
 * @see GrassBinaryImageWriter
 * @see GrassBinaryRasterWriteHandler
 */
public class GrassCoverageWriter extends AbstractGridCoverageWriter implements GridCoverageWriter {
    private File output;
    private ProgressListener monitor = new DummyProgressListener();

    /**
     * Constructor for the {@link GrassCoverageWriter}.
     */
    public GrassCoverageWriter( Object output ) {
        if (output instanceof File) {
            this.output = (File) output;
        } else {
            throw new IllegalArgumentException("Illegal input argument!");
        }
    }

    public void setProgressListener( ProgressListener monitor ) {
        this.monitor = monitor;
    }

    /**
     * Writes the {@link GridCoverage2D supplied coverage} to disk.
     * <p>
     * Note that this also takes care to cloes the file handle after writing to disk.
     * </p>
     * 
     * @param gridCoverage2D the coverage to write.
     * @throws IOException
     */
    public void writeRaster( GridCoverage2D gridCoverage2D ) throws IOException {
        try {
            Envelope2D env = gridCoverage2D.getEnvelope2D();
            GridEnvelope2D worldToGrid = gridCoverage2D.getGridGeometry().worldToGrid(env);

            double xRes = env.getWidth() / worldToGrid.getWidth();
            double yRes = env.getHeight() / worldToGrid.getHeight();

            JGrassRegion region = new JGrassRegion(env.getMinX(), env.getMaxX(), env.getMinY(), env
                    .getMaxY(), xRes, yRes);

            GrassBinaryImageWriterSpi writerSpi = new GrassBinaryImageWriterSpi();
            GrassBinaryImageWriter writer = new GrassBinaryImageWriter(writerSpi, monitor);
            RenderedImage renderedImage = gridCoverage2D.view(ViewType.GEOPHYSICS)
                    .getRenderedImage();
            writer.setOutput(output, region);
            writer.write(renderedImage);
            writer.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void writeRaster( GridCoverage2D gridCoverage2D, GeneralParameterValue[] params )
            throws IOException {
        GeneralEnvelope requestedEnvelope = null;
        Rectangle dim = null;
        JGrassRegion writeRegion = null;
        if (params != null) {
            for( int i = 0; i < params.length; i++ ) {
                final ParameterValue< ? > param = (ParameterValue< ? >) params[i];
                final String name = param.getDescriptor().getName().getCode();
                if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString())) {
                    final GridGeometry2D gg = (GridGeometry2D) param.getValue();
                    requestedEnvelope = new GeneralEnvelope((Envelope) gg.getEnvelope2D());
                    dim = gg.getGridRange2D().getBounds();
                    continue;
                }
            }
            if (requestedEnvelope != null && dim != null) {
                DirectPosition lowerCorner = requestedEnvelope.getLowerCorner();
                double[] westSouth = lowerCorner.getCoordinate();
                DirectPosition upperCorner = requestedEnvelope.getUpperCorner();
                double[] eastNorth = upperCorner.getCoordinate();
                writeRegion = new JGrassRegion(westSouth[0], eastNorth[0], westSouth[1],
                        eastNorth[1], dim.height, dim.width);
            }
        }

        if (writeRegion == null) {
            throw new IOException("Unable to define writing region.");
        }

        GrassBinaryImageWriterSpi writerSpi = new GrassBinaryImageWriterSpi();
        GrassBinaryImageWriter writer = new GrassBinaryImageWriter(writerSpi, monitor);
        RenderedImage renderedImage = gridCoverage2D.view(ViewType.GEOPHYSICS).getRenderedImage();
        writer.setOutput(output, writeRegion);
        writer.write(renderedImage);
        writer.dispose();
    }

    public Format getFormat() {
        return new GrassCoverageFormat();
    }

    public void write( GridCoverage coverage, GeneralParameterValue[] parameters )
            throws IllegalArgumentException, IOException {
        if (coverage instanceof GridCoverage2D) {
            GridCoverage2D gridCoverage = (GridCoverage2D) coverage;
            if (parameters == null) {
                writeRaster(gridCoverage);
            } else {
                writeRaster(gridCoverage, parameters);
            }
        } else {
            throw new IllegalArgumentException("Coverage not a GridCoverage2D");
        }
    }

}
