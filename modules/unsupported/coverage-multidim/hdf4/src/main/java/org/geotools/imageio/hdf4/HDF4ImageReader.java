/*
 *    ImageI/O-Ext - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    http://java.net/projects/imageio-ext/
 *    (C) 2007 - 2009, GeoSolutions
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    either version 3 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.imageio.hdf4;

import it.geosolutions.imageio.utilities.ImageIOUtilities;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BandedSampleModel;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.factory.GeoTools;
import org.geotools.imageio.SliceDescriptor;
import org.geotools.imageio.SpatioTemporalImageReader;
import org.geotools.imageio.metadataold.SpatioTemporalMetadata;
import org.geotools.imageio.netcdf.NetCDFUtilities;
import org.geotools.imageio.unidata.UnidataImageReader;
import org.geotools.imageio.unidata.VariableWrapper;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.util.SoftValueHashMap;

import ucar.ma2.Array;
import ucar.ma2.ArrayByte;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayFloat;
import ucar.ma2.ArrayInt;
import ucar.ma2.ArrayShort;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import ucar.ma2.Section;
import ucar.nc2.Variable;
import ucar.nc2.iosp.hdf4.H4iosp;

public abstract class HDF4ImageReader extends UnidataImageReader implements SpatioTemporalImageReader {

    protected SoftValueHashMap<Integer, IIOMetadata> metadataMap = new SoftValueHashMap<Integer, IIOMetadata>();

    /** Inner map to cache the SpatioTemporal metadata instances */
    protected SoftValueHashMap<Integer, SpatioTemporalMetadata> spatioTemporalMetadataMap = new SoftValueHashMap<Integer, SpatioTemporalMetadata>();

    
    protected class HDF4DatasetWrapper extends VariableWrapper {

        private int numAttributes;

        public int getNumAttributes() {
            return numAttributes;
        }

        protected HDF4DatasetWrapper( final Variable var ) {
            super(var);
            numAttributes = var.getAttributes().size();
            final int numBands = getNumBands();
            final int width = getWidth();
            final int height = getHeight();
            if (numBands == 3)
                setSampleModel(new PixelInterleavedSampleModel(NetCDFUtilities.getRawDataType(var), width, height, numBands,
                        width * numBands, new int[]{0, 1, 2}));
            else
                setSampleModel(new BandedSampleModel(NetCDFUtilities.getRawDataType(var), width, height, numBands));
        }
    }

    protected final static Logger LOGGER = Logger.getLogger("it.geosolutions.imageio.plugins.hdf4");

    /** set it to <code>true</code> when initialization has been performed */
    private boolean isInitialized = false;

    protected abstract HDF4DatasetWrapper getDatasetWrapper( final int imageIndex );

    /**
     * Additional initialization for a specific HDF "Profile". Depending on the
     * HDF data producer, the originating file has a proper data/metadata
     * structure. For this reason, a specific initialization should be
     * implemented for each different HDF "Profile". As an instance, the
     * Automated Processing System (APS) produces HDF files having a different
     * structure with respect to the HDF structure of a file produced by TIROS
     * Operational Vertical Sounder (TOVS).
     * 
     * @throws Exception
     */
    protected abstract void initializeProfile() throws IOException;

    protected abstract org.geotools.imageio.hdf4.HDF4ImageReaderSpi.HDF4_TYPE getHDF4Type();
    
    protected abstract IIOMetadata getImageMetadata( int imageIndex, final String metadataFormat ) throws IOException ;
    
    protected HDF4ImageReader( ImageReaderSpi originatingProvider ) {
        super(originatingProvider);
    }

    /**
     * Simple initialization method
     */
    protected synchronized void initialize() throws IOException {
        if (!isInitialized) {
            // initialize information specific to this profile
            initializeProfile();
            isInitialized = true;
        }
    }

    /**
     * @see it.geosolutions.imageio.plugins.hdf4.HDF4ImageReader#dispose()
     */
    public void dispose() {
        super.dispose();
        
        metadataMap.clear();
        metadataMap = null;
        spatioTemporalMetadataMap.clear();
        spatioTemporalMetadataMap = null;

        try {
            if (dataset != null) {
                dataset.close();
            }
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.warning("Errors closing NetCDF dataset." + e.getLocalizedMessage());
        } finally {
            dataset = null;
        }
        
        isInitialized = false;
    }

    @Override
    public void setInput( Object input, boolean seekForwardOnly, boolean ignoreMetadata ) {
        super.setInput(input, seekForwardOnly, ignoreMetadata);
        if (dataset != null) {
            if (!(dataset.getIosp() instanceof H4iosp))
                throw new IllegalArgumentException("Provided dataset is not an HDF4 file");
        } else
            throw new IllegalArgumentException("Provided dataset is not an HDF4 file");

        try {
            initialize();
        } catch (IOException e) {
            throw new IllegalArgumentException("Error occurred during NetCDF file parsing", e);
        }
    }

    /**
     * @see it.geosolutions.imageio.plugins.hdf4.HDF4ImageReader#getStreamMetadata()
     */
    public IIOMetadata getStreamMetadata() throws IOException {
        throw new UnsupportedOperationException(
                "Stream Metadata is not implemented for the base class, use corecommonstreammetadata");
    }

    protected BufferedImage read2DVariable( final int imageIndex, final ImageReadParam param ) throws IOException {
        BufferedImage image = null;
        final HDF4DatasetWrapper wrapper = getDatasetWrapper(imageIndex);
        final Variable variable = wrapper.getVariable();

        /*
         * Fetches the parameters that are not already processed by utility
         * methods like 'getDestination' or 'computeRegions' (invoked below).
         */
        final int strideX, strideY;
        // final int[] srcBands, dstBands;
        if (param != null) {
            strideX = param.getSourceXSubsampling();
            strideY = param.getSourceYSubsampling();
            // srcBands = param.getSourceBands();
            // dstBands = param.getDestinationBands();
        } else {
            strideX = 1;
            strideY = 1;
            // srcBands = null;
            // dstBands = null;
        }
        final int rank = variable.getRank();

        /*
         * Gets the destination image of appropriate size. We create it now
         * since it is a convenient way to get the number of destination bands.
         */
        final int width = wrapper.getWidth();
        final int height = wrapper.getHeight();
        final int numBands = wrapper.getNumBands();
        /*
         * Computes the source region (in the NetCDF file) and the destination
         * region (in the buffered image). Copies those informations into UCAR
         * Range structure.
         */
        final Rectangle srcRegion = new Rectangle();
        final Rectangle destRegion = new Rectangle();
        computeRegions(param, width, height, null, srcRegion, destRegion);
        // flipVertically(param, height, srcRegion);
        int destWidth = destRegion.x + destRegion.width;
        int destHeight = destRegion.y + destRegion.height;

        final List<Range> ranges = new LinkedList<Range>();
        for( int i = 0; i < rank; i++ ) {
            final int first, length, stride;
            switch( i ) {
            case 1: {
                first = srcRegion.x;
                length = srcRegion.width;
                stride = strideX;
                break;
            }
            case 0: {
                first = srcRegion.y;
                length = srcRegion.height;
                stride = strideY;
                break;
            }
            default: {
                first = 0;
                length = numBands;
                stride = 1;
                break;
            }
            }
            try {
                ranges.add(new Range(first, first + length - 1, stride));
            } catch (InvalidRangeException e) {
                // TODO LOGME
            }
        }
        final Section sections = new Section(ranges);

        /*
         * Setting SampleModel and ColorModel.
         */
        final SampleModel sampleModel = wrapper.getSampleModel().createCompatibleSampleModel(destWidth, destHeight);
        final ColorModel colorModel = ImageIOUtilities.createColorModel(sampleModel);

        /*
         * Reads the requested sub-region only.
         */
        final int size = destHeight * destWidth * numBands;
        Array array = null;
        try {
            array = variable.read(sections);
            DataBuffer dataBuffer = null;
            if (array instanceof ArrayByte) {
                dataBuffer = new DataBufferByte((byte[]) array.get1DJavaArray(byte.class), size);
            } else if (array instanceof ArrayShort) {
                dataBuffer = new DataBufferShort((short[]) array.get1DJavaArray(short.class), size);
            } else if (array instanceof ArrayInt) {
                dataBuffer = new DataBufferInt((int[]) array.get1DJavaArray(int.class), size);
            } else if (array instanceof ArrayFloat) {
                dataBuffer = new DataBufferFloat((float[]) array.get1DJavaArray(float.class), size);
            } else if (array instanceof ArrayDouble) {
                dataBuffer = new DataBufferDouble((double[]) array.get1DJavaArray(double.class), size);
            }

            WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, new Point(0, 0));
            image = new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
        } catch (InvalidRangeException e) {
            // TODO LOGME
        }
        return image;
    }

    /**
     * @see it.geosolutions.imageio.plugins.hdf4.HDF4ImageReader#read(int, javax.imageio.ImageReadParam)
     */
    @Override
    public BufferedImage read( final int imageIndex, final ImageReadParam param ) throws IOException {
        return read2DVariable(imageIndex, param);
    }

    /**
     * Returns a {@link SliceDescriptor} instance for the specified imageIndex.
     * 
     * @param imageIndex
     *                the index of the specified 2D raster.
     * @see SpatioTemporalImageReader#getSliceDescriptor(int)
     */
    public SliceDescriptor getSliceDescriptor(int imageIndex)
            throws IOException {
        return new SliceDescriptor(getSpatioTemporalMetadata(imageIndex),ReferencingFactoryContainer.instance(GeoTools.getDefaultHints()));
    }

    /**
     * Returns a {@link SpatioTemporalMetadata} instance for the specified
     * imageIndex.
     * 
     * @param imageIndex
     *                the index of the specified 2D raster.
     * @see SpatioTemporalImageReader#getSpatioTemporalMetadata(int)
     */
    public SpatioTemporalMetadata getSpatioTemporalMetadata(int imageIndex) {
        SpatioTemporalMetadata metadata;
        synchronized (spatioTemporalMetadataMap) {
            if (!spatioTemporalMetadataMap.containsKey(imageIndex)) {
                metadata = new HDF4SpatioTemporalMetadata(this, imageIndex);
                spatioTemporalMetadataMap.put(imageIndex, metadata);
            } else {
                metadata = spatioTemporalMetadataMap.get(imageIndex);
                if (metadata == null) {
                    metadata = new HDF4SpatioTemporalMetadata(this, imageIndex);
                    spatioTemporalMetadataMap.put(imageIndex, metadata);
                }
            }
        }
        return metadata;
    }

}
