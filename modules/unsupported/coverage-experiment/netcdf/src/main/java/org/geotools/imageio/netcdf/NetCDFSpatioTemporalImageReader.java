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
package org.geotools.imageio.netcdf;

import it.geosolutions.imageio.ndplugin.BaseImageMetadata;
import it.geosolutions.imageio.plugins.netcdf.NetCDFImageReader;
import it.geosolutions.imageio.plugins.netcdf.NetCDFUtilities;
import it.geosolutions.imageio.plugins.netcdf.NetCDFUtilities.CheckType;
import it.geosolutions.imageio.utilities.SoftValueHashMap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.imageio.DefaultSliceDescriptor;
import org.geotools.imageio.SliceDescriptor;
import org.geotools.imageio.SpatioTemporalImageReader;
import org.geotools.imageio.metadata.SpatioTemporalMetadata;

import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import ucar.nc2.Variable;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.VariableDS;

/**
 * Class implementing a {@link SpatioTemporalImageReader} to handle NetCDF-CF
 * data source.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/netcdf/src/main/java/org/geotools/imageio/netcdf/NetCDFSpatioTemporalImageReader.java $
 */
public class NetCDFSpatioTemporalImageReader extends SpatioTemporalImageReader {
    
    private final static Logger LOGGER = Logger
    .getLogger("org.geotools.imageio.netcdf");

    private SoftValueHashMap<Integer, IIOMetadata> metadataMap = new SoftValueHashMap<Integer, IIOMetadata>();
    
    /** Inner map to cache the SpatioTemporal metadata instances */
    private SoftValueHashMap<Integer, SpatioTemporalMetadata> spatioTemporalMetadataMap = new SoftValueHashMap<Integer, SpatioTemporalMetadata>();

    /** The underlying direct imageio reader to be used by this spatiotemporal one */
    private NetCDFImageReader directReader;

    /** An inner map useful to associate NetCDF Variable with imageIndex */
    private Map<Range, Variable> variableMap;
    
    /** An inner map useful to associate NetCDF Variable with imageIndex */
    private Map<String, Variable> coordinatesMap;
    
    private NetcdfDataset dataset;

    protected NetCDFSpatioTemporalImageReader(ImageReaderSpi originatingProvider) {
        super(originatingProvider);
        directReader = new NetCDFImageReader(originatingProvider);
    }

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
        
        try {
            super.setInput(input, seekForwardOnly, ignoreMetadata);
            dataset = NetCDFUtilities.getDataset(input);
            if (dataset == null)
                throw new IllegalArgumentException("Error occurred during NetCDF file parsing: obtained a null netcdf dataset");
            int numImages = 0;
            final CheckType checkType = NetCDFUtilities.getCheckType(dataset);
            variableMap = new HashMap<Range, Variable>();
            coordinatesMap = new HashMap<String, Variable>();
            if (dataset != null) {
                final List<Variable> variables = dataset.getVariables();
                if (variables != null) {
                    for (final Variable variable : variables) {
                        if (variable != null && variable instanceof VariableDS) {
                        	String varName = variable.getName();
                        	if (variable.isCoordinateVariable())
                        		coordinatesMap.put(varName, variable);
                            if (variable instanceof CoordinateAxis1D){
                                
                                //Due to a netCDF library bug, coordinates values need to
                                //be read to be properly obtained afterwards.
                                //Otherwise it may return NaN in some coords.
                                if (varName.equalsIgnoreCase(NetCDFUtilities.LAT)||
                                        varName.equalsIgnoreCase(NetCDFUtilities.LATITUDE)||
                                        varName.equalsIgnoreCase(NetCDFUtilities.LON)||
                                        varName.equalsIgnoreCase(NetCDFUtilities.LONGITUDE)){
                                    variable.read();
                                }
                            }
                            
                            if (!NetCDFUtilities.isVariableAccepted(variable,
                                    checkType))
                                continue;

                            int[] shape = variable.getShape();
                            switch (shape.length) {
                            case 2:
                                variableMap.put(new Range(numImages,
                                        numImages + 1), variable);
                                numImages++;
                                break;
                            case 3:
                                variableMap.put(new Range(numImages, numImages
                                        + shape[0]), variable);
                                numImages += shape[0];
                                break;
                            case 4:
                                variableMap.put(new Range(numImages, numImages
                                        + shape[0] * shape[1]), variable);
                                numImages += shape[0] * shape[1];
                                break;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Error occurred during NetCDF file parsing", e);
        } catch (InvalidRangeException e) {
            throw new IllegalArgumentException(
                    "Error occurred during NetCDF file parsing", e);
        }
        directReader.setInput(input, seekForwardOnly, ignoreMetadata);
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
        directReader.dispose();
        directReader = null;
        metadataMap.clear();
        metadataMap = null;
        variableMap.clear();
        variableMap = null;
        coordinatesMap.clear();
        coordinatesMap = null;
        spatioTemporalMetadataMap.clear();
        spatioTemporalMetadataMap = null;
        try {
            dataset.close();
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Error closing the netcdf dataset",e);
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
     * Returns the ImageMetadata obtained from the underlying flatReader
     * 
     * @param imageIndex
     *                the index of the specified 2D raster.
     */
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
                metadata = new NetCDFSpatioTemporalMetadata(this, imageIndex);
                spatioTemporalMetadataMap.put(imageIndex, metadata);
            } else {
                metadata = spatioTemporalMetadataMap.get(imageIndex);
                if (metadata == null) {
                    metadata = new NetCDFSpatioTemporalMetadata(this,
                            imageIndex);
                    spatioTemporalMetadataMap.put(imageIndex, metadata);
                }
            }
        }
        return metadata;
    }

    // /////////////////////////////////////////////////////////////////////////
    //
    // Set of Package private methods to ease the access to NetCDF variables and
    // Ranges without needs to re-create a mapping. They will be used by the
    // metadata class.
    //
    // /////////////////////////////////////////////////////////////////////////
    /**
     * Package private method simply returning the netCDF Variable related to
     * the specified imageIndex.
     * 
     * @param imageIndex
     * @return the requested Variable.
     */
    Variable getVariable(final int imageIndex) {
        Variable var = null;
        Range rangeFound = getRange(imageIndex);
        if (rangeFound != null)
            var = variableMap.get(rangeFound);
        return var;
    }

    /**
     * Package private method simply returning the netCDF Variable related to
     * the specified Range.
     * 
     * @param range
     * @return the requested Variable.
     */
    Variable getVariable(final Range range) {
        Variable var = null;
        if (range != null && variableMap.containsKey(range))
            var = variableMap.get(range);
        return var;
    }

    /**
     * Package private method simply returning the range containing the
     * specified imageIndex.
     * 
     * @param imageIndex
     * @return the requested Range.
     */
    Range getRange(int imageIndex) {
        Range rangeFound = null;
        for (Range range : variableMap.keySet()) {
            if (range.contains(imageIndex) && range.first() <= imageIndex
                    && imageIndex < range.last()) {
                rangeFound = range;
                break;
            }
        }
        return rangeFound;
    }
    
    Variable getCoordinate(final String coordName){
    	if (coordName!=null && coordName.length()>0 && coordinatesMap.containsKey(coordName))
    		return coordinatesMap.get(coordName);
    	return null;
    }
}
