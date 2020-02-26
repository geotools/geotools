/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.geotiff;

import it.geosolutions.imageio.plugins.tiff.TIFFImageWriteParam;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageMetadata;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriter;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverageWriter;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.coverage.grid.io.imageio.geotiff.CRS2GeoTiffMetadataAdapter;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffConstants;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffException;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataEncoder;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.data.WorldFileWriter;
import org.geotools.image.io.GridCoverageWriterProgressAdapter;
import org.geotools.image.io.ImageIOExt;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.util.ProgressListener;
import org.w3c.dom.Node;

/**
 * {@link AbstractGridCoverageWriter} implementation for the geotiff format.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class GeoTiffWriter extends AbstractGridCoverageWriter implements GridCoverageWriter {
    private static final Logger LOGGER = Logging.getLogger(GeoTiffWriter.class);

    private final Map<String, String> metadataKeyValue = new HashMap<String, String>();

    /** Constructor for a {@link GeoTiffWriter}. */
    public GeoTiffWriter(Object destination) throws IOException {
        this(destination, null);
    }

    /**
     * Allows to setup metadata by leveraging on Ascii TIFF Tags.
     *
     * @param name is the Ascii TIFF Tag identifier. It can be a String representing: 1) a simple
     *     Integer (referring to a tag ID) (in that case it will refer to the BaselineTIFFTagSet 2)
     *     OR an identifier in the form: TIFFTagSet:TIFFTagID. As an instance:
     *     "BaselineTIFFTagSet:305" in order to add the Copyright info.
     * @param value is the value to be assigned to that tag.
     * @see GeoTiffIIOMetadataEncoder.TagSet
     */
    @Override
    public void setMetadataValue(String name, String value) throws IOException {
        if (name != null && name.length() > 0) {
            metadataKeyValue.put(name, value);
        }
    }

    /** Constructor for a {@link GeoTiffWriter}. */
    public GeoTiffWriter(Object destination, Hints hints) throws IOException {

        this.destination = destination;
        if (destination instanceof File)
            this.outStream = ImageIOExt.createImageOutputStream(null, destination);
        else if (destination instanceof URL) {
            final URL dest = (URL) destination;
            if (dest.getProtocol().equalsIgnoreCase("file")) {
                final File destFile = URLs.urlToFile(dest);
                this.outStream = ImageIOExt.createImageOutputStream(null, destFile);
            }

        } else if (destination instanceof OutputStream) {

            this.outStream = ImageIOExt.createImageOutputStream(null, (OutputStream) destination);

        } else if (destination instanceof ImageOutputStream)
            this.outStream = (ImageOutputStream) destination;
        else throw new IllegalArgumentException("The provided destination canno be used!");
        // //
        //
        // managing hints
        //
        // //
        if (this.hints == null) this.hints = new Hints();
        if (hints != null) {
            // prevent the use from reordering axes
            hints.remove(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
            this.hints.add(hints);
            this.hints.add(new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.coverage.grid.GridCoverageWriter#getFormat()
     */
    public Format getFormat() {
        return new GeoTiffFormat();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.coverage.grid.GridCoverageWriter#write(org.opengis.coverage.grid.GridCoverage,
     *      org.opengis.parameter.GeneralParameterValue[])
     */
    @SuppressWarnings("rawtypes")
    public void write(final GridCoverage gc, final GeneralParameterValue[] params)
            throws IllegalArgumentException, IOException, IndexOutOfBoundsException {

        GeoToolsWriteParams gtParams = null;
        boolean writeTfw = GeoTiffFormat.WRITE_TFW.getDefaultValue();
        ProgressListener listener = null;
        boolean retainAxesOrder = false;
        boolean writeNodata = GeoTiffFormat.WRITE_NODATA.getDefaultValue();
        // /////////////////////////////////////////////////////////////////////
        //
        // Checking params
        //
        // /////////////////////////////////////////////////////////////////////
        if (params != null) {
            Parameter<?> param;
            final int length = params.length;
            for (int i = 0; i < length; i++) {
                param = (Parameter) params[i];
                final ReferenceIdentifier name = param.getDescriptor().getName();
                if (name.equals(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName())) {
                    gtParams = (GeoToolsWriteParams) param.getValue();
                    continue;
                }
                if (name.equals(GeoTiffFormat.WRITE_TFW.getName())) {
                    writeTfw = (Boolean) param.getValue();
                    continue;
                }
                if (name.equals(GeoTiffFormat.PROGRESS_LISTENER.getName())) {
                    listener = (ProgressListener) param.getValue();
                    continue;
                }
                if (name.equals(GeoTiffFormat.RETAIN_AXES_ORDER.getName())) {
                    retainAxesOrder = (Boolean) param.getValue();
                    continue;
                }
                if (name.equals(GeoTiffFormat.WRITE_NODATA.getName())) {
                    writeNodata = (Boolean) param.getValue();
                    continue;
                }
            }
        }

        if (gtParams == null) gtParams = new GeoTiffWriteParams();

        //
        // getting the coordinate reference system
        //
        final GridGeometry2D gg = (GridGeometry2D) gc.getGridGeometry();
        GridEnvelope2D range = gg.getGridRange2D();
        final Rectangle sourceRegion = gtParams.getSourceRegion();
        if (sourceRegion != null) {
            range = new GridEnvelope2D(sourceRegion);
        }
        final AffineTransform tr = (AffineTransform) gg.getGridToCRS2D();
        final CoordinateReferenceSystem crs = gg.getCoordinateReferenceSystem2D();

        //
        // we handle just projected and geographic crs
        //
        if (!(crs instanceof ProjectedCRS || crs instanceof GeographicCRS)) {
            throw new GeoTiffException(
                    null,
                    "The supplied grid coverage uses an unsupported crs! You are allowed to use only projected and geographic coordinate reference systems",
                    null);
        }

        // creating geotiff metadata
        final CRS2GeoTiffMetadataAdapter adapter = new CRS2GeoTiffMetadataAdapter(crs);
        final GeoTiffIIOMetadataEncoder metadata = adapter.parseCoordinateReferenceSystem();

        // setting georeferencing
        setGeoReference(crs, metadata, tr, range, retainAxesOrder);

        // handling noData
        final double inNoData = CoverageUtilities.getBackgroundValues((GridCoverage2D) gc)[0];
        if (!Double.isNaN(inNoData) && writeNodata) {
            metadata.setNoData(inNoData);
        }
        if (metadataKeyValue != null && !metadataKeyValue.isEmpty()) {
            metadata.setTiffTagsMetadata(metadataKeyValue);
        }

        //
        // write image
        //
        writeImage(
                ((GridCoverage2D) gc).getRenderedImage(),
                this.outStream,
                metadata,
                gtParams,
                listener);

        //
        // write tfw
        //
        if (writeTfw && (destination instanceof File)) {
            handleTFW(gc, tr);
        }
    }

    /**
     * Takes care of writing the world file for this geotiff
     *
     * @param gc the {@link GridCoverage} to take the georefeerincing from.
     * @throws IOException in case something bad occurs while writing.
     */
    private void handleTFW(final GridCoverage gc, AffineTransform tr) throws IOException {
        final File destFile = (File) this.destination;
        final File parentFile = destFile.getParentFile();
        final String name = destFile.getName();
        final File tfw = new File(parentFile, name.replace("tif", "tfw"));
        final File prj = new File(parentFile, name.replace("tif", "prj"));
        try (final BufferedWriter outW = new BufferedWriter(new FileWriter(prj))) {
            new WorldFileWriter(tfw, tr); // side effect of creation writes the file
            outW.write(gc.getCoordinateReferenceSystem().toWKT());
        }
    }

    /**
     * This method is used to set the tie point and the scale parameters for the GeoTiff file we are
     * writing or the ModelTransformation in case a more general {@link AffineTransform} is needed
     * to represent the raster space to model space transform.
     *
     * <p>This method works regardles of the nature fo the crs without making any assumptions on the
     * order or the direction of the axes, but checking them from the supplied CRS.
     *
     * @see {@link http://lists.maptools.org/pipermail/geotiff/2006-January/000213.html}
     * @see {@http://lists.maptools.org/pipermail/geotiff/2006-January/000212.html}
     * @param crs The {@link CoordinateReferenceSystem} of the {@link GridCoverage2D} to encode.
     * @param metadata where to set the georeferencing information.
     * @param range that describes the raster space for this geotiff.
     * @param rasterToModel describes the {@link AffineTransform} between raster space and model
     *     space.
     * @param retainAxesOrder <code>true</code> in case we want to retain the axes order, <code>
     *     false</code> otherwise for lon-lat enforcing.
     * @throws IOException in case something bad happens during the write operation.
     */
    private static void setGeoReference(
            final CoordinateReferenceSystem crs,
            final GeoTiffIIOMetadataEncoder metadata,
            final AffineTransform rasterToModel,
            GridEnvelope2D range,
            boolean retainAxesOrder)
            throws IOException {

        //
        // We have to set an affine transformation which is going to be 2D
        // since we support baseline GeoTiff.
        //
        final AffineTransform modifiedRasterToModel = new AffineTransform(rasterToModel);
        // move the internal grid to world to corner from center
        modifiedRasterToModel.concatenate(CoverageUtilities.CENTER_TO_CORNER);
        int minx = range.getLow(0), miny = range.getLow(1);
        if (minx != 0 || miny != 0) {
            // //
            //
            // Preconcatenate a transform to have raster space beginning at
            // (0,0) as this is not captured by the TIFF spec
            //
            // //
            modifiedRasterToModel.concatenate(AffineTransform.getTranslateInstance(minx, miny));
        }

        //
        // Setting raster type to pixel corner since that is the default for geotiff
        // and makes most software happy
        //
        metadata.addGeoShortParam(
                GeoTiffConstants.GTRasterTypeGeoKey, GeoTiffConstants.RasterPixelIsArea);

        //
        // AXES Swap Management
        //
        // we need to understand how the axes of this gridcoverage are
        // specified, trying to understand the direction of the first axis in
        // order to correctly use transformations.
        //
        boolean swapAxes =
                XAffineTransform.getSwapXY(modifiedRasterToModel) == -1
                        || CRS.getAxisOrder(crs).equals(AxisOrder.NORTH_EAST);
        swapAxes &= !retainAxesOrder;

        //
        // Deciding how to save the georef with respect to the CRS.
        //
        // Notice that if we were asked to retain the axes order we don't swap axes!
        //
        if (swapAxes) {
            modifiedRasterToModel.preConcatenate(CoverageUtilities.AXES_SWAP);
        }
        metadata.setModelTransformation(modifiedRasterToModel);
    }

    /**
     * Writes the provided rendered image to the provided image output stream using the supplied
     * geotiff metadata.
     */
    private boolean writeImage(
            final RenderedImage image,
            final ImageOutputStream outputStream,
            final GeoTiffIIOMetadataEncoder geoTIFFMetadata,
            GeoToolsWriteParams gtParams,
            ProgressListener listener)
            throws IOException {
        if (image == null || outputStream == null) {
            throw new NullPointerException("Some input parameters are null");
        }
        final ImageWriteParam params = gtParams.getAdaptee();
        if (params instanceof TIFFImageWriteParam && gtParams instanceof GeoTiffWriteParams) {
            TIFFImageWriteParam param = (TIFFImageWriteParam) params;
            param.setForceToBigTIFF(((GeoTiffWriteParams) gtParams).isForceToBigTIFF());
        }
        //
        // GETTING READER AND METADATA
        //
        final TIFFImageWriter writer =
                (TIFFImageWriter) GeoTiffFormat.IMAGEIO_WRITER_FACTORY.createWriterInstance();
        try {
            final IIOMetadata metadata =
                    createGeoTiffIIOMetadata(
                            writer,
                            ImageTypeSpecifier.createFromRenderedImage(image),
                            geoTIFFMetadata,
                            params);

            //
            // IMAGEWRITE
            //
            writer.setOutput(outputStream);
            // listeners
            if (listener != null) {
                final GridCoverageWriterProgressAdapter progressAdapter =
                        new GridCoverageWriterProgressAdapter(listener);
                writer.addIIOWriteProgressListener(progressAdapter);
                writer.addIIOWriteWarningListener(progressAdapter);
            }
            writer.write(
                    writer.getDefaultStreamMetadata(params),
                    new IIOImage(image, null, metadata),
                    params);

            outputStream.flush();
        } finally {

            try {
                if (!(destination instanceof ImageOutputStream) && outputStream != null)
                    outputStream.close();
            } catch (Throwable e) {
                // eat me
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                }
            }

            try {
                if (writer != null) writer.dispose();
            } catch (Throwable e) {
                // eat me
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                }
            }
        }

        return true;
    }

    /**
     * Creates image metadata which complies to the GeoTIFFWritingUtilities specification for the
     * given image writer, image type and GeoTIFFWritingUtilities metadata.
     *
     * @param writer the image writer, must not be null
     * @param type the image type, must not be null
     * @param geoTIFFMetadata the GeoTIFFWritingUtilities metadata, must not be null
     * @return the image metadata, never null
     * @throws IIOException if the metadata cannot be created
     */
    public static final IIOMetadata createGeoTiffIIOMetadata(
            ImageWriter writer,
            ImageTypeSpecifier type,
            GeoTiffIIOMetadataEncoder geoTIFFMetadata,
            ImageWriteParam params)
            throws IIOException {
        IIOMetadata imageMetadata = writer.getDefaultImageMetadata(type, params);
        imageMetadata = writer.convertImageMetadata(imageMetadata, type, params);
        org.w3c.dom.Element w3cElement =
                (org.w3c.dom.Element)
                        imageMetadata.getAsTree(GeoTiffConstants.GEOTIFF_IIO_METADATA_FORMAT_NAME);

        geoTIFFMetadata.assignTo(w3cElement);
        try {
            Node TIFFIFD = w3cElement.getChildNodes().item(0);
            final IIOMetadata iioMetadata =
                    new TIFFImageMetadata(TIFFImageMetadata.parseIFD(TIFFIFD));
            imageMetadata = iioMetadata;
        } catch (IIOInvalidTreeException e) {
            throw new IIOException("Failed to set GeoTIFFWritingUtilities specific tags.", e);
        }

        return imageMetadata;
    }

    @Override
    public void dispose() {
        // release any metadata
        metadataKeyValue.clear();

        // release father
        super.dispose();
    }
}
