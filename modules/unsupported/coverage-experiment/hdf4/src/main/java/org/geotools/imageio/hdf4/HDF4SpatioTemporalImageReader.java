/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.hdf4;

import it.geosolutions.imageio.ndplugin.BaseImageMetadata;
import it.geosolutions.imageio.plugins.hdf4.BaseHDF4ImageReader;
import it.geosolutions.imageio.plugins.hdf4.aps.HDF4APSImageReader;
import it.geosolutions.imageio.plugins.hdf4.terascan.HDF4TeraScanImageReader;
import it.geosolutions.imageio.utilities.SoftValueHashMap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.imageio.DefaultSliceDescriptor;
import org.geotools.imageio.SliceDescriptor;
import org.geotools.imageio.SpatioTemporalImageReader;
import org.geotools.imageio.hdf4.HDF4SpatioTemporalImageReaderSpi.HDF4_TYPE;
import org.geotools.imageio.metadata.SpatioTemporalMetadata;

/**
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 */
public class HDF4SpatioTemporalImageReader extends SpatioTemporalImageReader {

    private SoftValueHashMap<Integer, IIOMetadata> metadataMap = new SoftValueHashMap<Integer, IIOMetadata>();

    /** Inner map to cache the SpatioTemporal metadata instances */
    private SoftValueHashMap<Integer, SpatioTemporalMetadata> spatioTemporalMetadataMap = new SoftValueHashMap<Integer, SpatioTemporalMetadata>();

    protected HDF4SpatioTemporalImageReader(ImageReaderSpi originatingProvider) {
        super(originatingProvider);
        try {
			directReader = (BaseHDF4ImageReader) originatingProvider.createReaderInstance();
		} catch (IOException e) {
			throw new RuntimeException("Exception occurred while creating a reader" +e.getLocalizedMessage(),e);
		}
    }

    private BaseHDF4ImageReader directReader;

    @Override
    public int getHeight(int imageIndex) throws IOException {
        return directReader.getHeight(imageIndex);
    }

    @Override
    public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex)
            throws IOException {
        return directReader.getImageTypes(imageIndex);
    }

    @Override
    public int getNumImages(boolean allowSearch) throws IOException {
        return directReader.getNumImages(allowSearch);
    }

    @Override
    public IIOMetadata getStreamMetadata() throws IOException {
        return directReader.getStreamMetadata();
    }

    @Override
    public int getWidth(int imageIndex) throws IOException {
        return directReader.getWidth(imageIndex);
    }

    @Override
    public BufferedImage read(int imageIndex, ImageReadParam param)
            throws IOException {
        return directReader.read(imageIndex, param);
    }

    public void setInput(Object input, boolean seekForwardOnly,
            boolean ignoreMetadata) {
        super.setInput(input, seekForwardOnly, ignoreMetadata);
        directReader.setInput(input, seekForwardOnly, ignoreMetadata);
        // Parse additional items
    }

    @Override
    public void setInput(Object input, boolean seekForwardOnly) {
        this.setInput(input, seekForwardOnly, true);

    }

    @Override
    public void setInput(Object input) {
        this.setInput(input, true, true);
    }

    @Override
    public void dispose() {

        metadataMap.clear();
        metadataMap = null;
    	try{      
    		directReader.dispose();       		
    	}
    	finally{
    		directReader = null;
    	}
    }

    @Override
    public void reset() {
        this.dispose();
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
        return new DefaultSliceDescriptor(getSpatioTemporalMetadata(imageIndex));
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

    /**
     * Returns the ImageMetadata obtained from the underlying flatReader
     * 
     * @param imageIndex
     *                the index of the specified 2D raster.
     */
    public IIOMetadata getImageMetadata(int imageIndex, final String metadataFormat) throws IOException {
        IIOMetadata metadata;
        synchronized (metadataMap) {
            if (!metadataMap.containsKey(imageIndex)) {
                metadata = directReader.getImageMetadata(imageIndex, metadataFormat);
                metadataMap.put(imageIndex, metadata);
            } else {
                metadata = (BaseImageMetadata) metadataMap.get(imageIndex);
                if (metadata == null) {
                    metadata = directReader.getImageMetadata(imageIndex, metadataFormat);
                    metadataMap.put(imageIndex, metadata);
                }
            }
        }
        return metadata;
    }
    
    public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
        IIOMetadata metadata;
        synchronized (metadataMap) {
            if (!metadataMap.containsKey(imageIndex)) {
                metadata = directReader.getImageMetadata(imageIndex);
                metadataMap.put(imageIndex, metadata);
            } else {
                metadata = (BaseImageMetadata) metadataMap.get(imageIndex);
                if (metadata == null) {
                    metadata = directReader.getImageMetadata(imageIndex);
                    metadataMap.put(imageIndex, metadata);
                }
            }
        }
        return metadata;
    }

	public HDF4_TYPE getHDF4Type() {
		if (directReader instanceof HDF4APSImageReader)
			return HDF4_TYPE.APS;
		else if (directReader instanceof HDF4TeraScanImageReader)
			return HDF4_TYPE.TeraScan;
		else 
			return HDF4_TYPE.UNDEFINED;
	}
}
