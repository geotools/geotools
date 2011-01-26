/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2010, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;

import org.geotools.gce.grassraster.core.GrassBinaryRasterWriteHandler;
import org.geotools.gce.grassraster.spi.GrassBinaryImageWriterSpi;
import org.opengis.util.ProgressListener;

/**
 * @author Andrea Antonello - www.hydrologis.com
 */
public class GrassBinaryImageWriter extends ImageWriter {
    /** <code>true</code> if there are some listeners attached to this writer */
    private boolean hasListeners = false;

    /** The <code>ImageOutputStream</code> associated to this writer. */

    /** The {@link GrassBinaryRasterWriteHandler} to write rasters to grass binary file. */
    private GrassBinaryRasterWriteHandler rasterWriter = null;

    /**
     * The noData value
     */
    private double noDataValue = Double.NaN;

    private JGrassRegion writeRegion;

    private ProgressListener monitor = new DummyProgressListener();

    public GrassBinaryImageWriter( GrassBinaryImageWriterSpi originatingProvider, ProgressListener monitor ) {
        super(originatingProvider);
        if (monitor != null) {
            this.monitor = monitor;
        }
    }

    public void setOutput( Object output, JGrassRegion writeRegion ) {
        this.writeRegion = writeRegion;
        setOutput(output);
    }

    /**
     * Sets the output for this {@link GrassBinaryImageWriter}.
     */
    public void setOutput( Object output ) {

        if (output instanceof File) {
            final File outFile = (File) output;
            JGrassMapEnvironment tmp = new JGrassMapEnvironment(outFile);
            rasterWriter = new GrassBinaryRasterWriteHandler(tmp.getMAPSET(), tmp.getMapName(), monitor);
            try {
                if (writeRegion == null) {
                    writeRegion = rasterWriter.getWriteRegion();
                } else {
                    rasterWriter.setWriteRegion(writeRegion);
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("The supplied input isn't a GRASS raster map path!");
            }
        } else {
            // is not something we can decode
            throw new IllegalArgumentException("Input is not decodable!");
        }
    }

    public void write( IIOMetadata streamMetadata, IIOImage image, ImageWriteParam param ) throws IOException {

        hasListeners = (this.progressListeners != null && (!(this.progressListeners.isEmpty()))) ? true : false;

        if (hasListeners) {
            clearAbortRequest();
            // Broadcast the start of the image write operation
            processImageStarted(0);
        }

        RenderedImage renderedImage = image.getRenderedImage();
        // PlanarImage inputRenderedImage = PlanarImage.wrapRenderedImage(renderedImage);

        // Raster data = renderedImage.getData();
        // final RectIter iterator = RectIterFactory.create(data, null);

        // writing
        noDataValue = rasterWriter.getNoData();
        writeRegion.setCols(renderedImage.getWidth());
        writeRegion.setRows(renderedImage.getHeight());
        int nColumns = writeRegion.getCols();
        int nRows = writeRegion.getRows();
        double west = writeRegion.getWest();
        double south = writeRegion.getSouth();
        double cellsizeX = writeRegion.getWEResolution();
        double cellsizeY = writeRegion.getNSResolution();
        rasterWriter.writeRaster(renderedImage, nColumns, nRows, west, south, cellsizeX, cellsizeY, noDataValue);

        if (hasListeners) {
            // Checking the status of the write operation (aborted/completed)
            if (rasterWriter.isAborting())
                processWriteAborted();
            else
                processImageComplete();
        }
        rasterWriter.close();

    }

    /**
     * Initialize all required fields which will be written to the header.
     * 
     * @param root The root node containing metadata
     * @throws IOException
     */
    // private void retrieveMetadata( Node root ) throws IOException {
    // // Grid description
    // final Node gridDescriptorNode = root.getFirstChild();
    // NamedNodeMap gridNodeAttributes = gridDescriptorNode.getAttributes();
    // nColumns = Integer.parseInt(gridNodeAttributes.getNamedItem(GrassBinaryImageMetadata.NCOLS)
    // .getNodeValue());
    // nRows = Integer.parseInt(gridNodeAttributes.getNamedItem(GrassBinaryImageMetadata.NROWS)
    // .getNodeValue());
    // Node dummyNode = gridNodeAttributes.getNamedItem(GrassBinaryImageMetadata.NO_DATA);
    // if (dummyNode != null)
    // noDataValue = Double.parseDouble(dummyNode.getNodeValue());
    //
    // // Spatial dimensions
    // final Node envelopDescriptorNode = gridDescriptorNode.getNextSibling();
    // NamedNodeMap envelopeNodeAttributes = envelopDescriptorNode.getAttributes();
    // cellsizeX = Double.parseDouble(envelopeNodeAttributes.getNamedItem(
    // GrassBinaryImageMetadata.XRES).getNodeValue());
    // cellsizeY = Double.parseDouble(envelopeNodeAttributes.getNamedItem(
    // GrassBinaryImageMetadata.YRES).getNodeValue());
    // west = Double.parseDouble(envelopeNodeAttributes
    // .getNamedItem(GrassBinaryImageMetadata.WEST).getNodeValue());
    // south = Double.parseDouble(envelopeNodeAttributes.getNamedItem(
    // GrassBinaryImageMetadata.SOUTH).getNodeValue());
    //
    // // As an alternative for ImageReadOp images with source subsampling we
    // // could look for the image read params.
    // final int actualWidth = this.inputRenderedImage.getWidth();
    // final int actualHeight = this.inputRenderedImage.getHeight();
    // cellsizeX *= nColumns / actualWidth;
    // cellsizeY *= nRows / actualHeight;
    //
    // }
    /**
     * @see javax.imageio.ImageWriter#getDefaultImageMetadata(javax.imageio.ImageTypeSpecifier,
     *      javax.imageio.ImageWriteParam)
     */
    public IIOMetadata getDefaultImageMetadata( ImageTypeSpecifier its, ImageWriteParam param ) {
        return null;
    }

    /**
     * @see javax.imageio.ImageWriter#getDefaultIStreamMetadata(javax.imageio.ImageWriteParam)
     */
    public IIOMetadata getDefaultStreamMetadata( ImageWriteParam param ) {
        return null;
    }

    /**
     * @see javax.imageio.ImageWriter#convertStreamMetadata(javax.imageio.metadata.IIOMetadata,
     *      javax.imageio.ImageWriteParam)
     */
    public IIOMetadata convertStreamMetadata( IIOMetadata md, ImageWriteParam param ) {
        return null;
    }

    /**
     * @see javax.imageio.ImageWriter#convertImageMetadata(javax.imageio.metadata.IIOMetadata,
     *      javax.imageio.ImageTypeSpecifier, javax.imageio.ImageWriteParam)
     */
    public IIOMetadata convertImageMetadata( IIOMetadata md, ImageTypeSpecifier its, ImageWriteParam param ) {
        return md;
    }

    /**
     * Cleans this {@link GrassBinaryImageWriter}.
     */
    public void dispose() {
        try {
            rasterWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.dispose();
    }

    public synchronized void abort() {
        if (rasterWriter != null)
            rasterWriter.abort();
    }

    public synchronized boolean abortRequested() {
        return rasterWriter.isAborting();
    }

    public void reset() {
        super.reset();
        rasterWriter = null;
    }

}
