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
package org.geotools.imageio.hdf4.terascan;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.imageio.hdf4.HDF4ImageReader;
import org.geotools.imageio.hdf4.HDF4ImageReaderSpi.HDF4_TYPE;
import org.geotools.imageio.unidata.UnidataImageMetadata;
import org.geotools.imageio.unidata.VariableWrapper;

import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;

/**
 * Specific Implementation of the <code>BaseHDF4ImageReader</code> needed
 * to work on Terascan produced HDF
 * 
 * @author Romagnoli Daniele
 */
public class HDF4TeraScanImageReader extends HDF4ImageReader {

    /** The Products Dataset List contained within the Terascan File */
    private String[] productList;

    private IIOMetadata streamMetadata = null;

    private Map<Range, TerascanDatasetWrapper> indexMap;

    public HDF4TeraScanImageReader( ImageReaderSpi originatingProvider ) {
        super(originatingProvider);
    }

    /**
     * Inner class to represent interesting attributes of a Terascan Dataset
     * 
     * @author Daniele Romagnoli, GeoSolutions.
     */
    private class TerascanDatasetWrapper extends HDF4DatasetWrapper {

        public TerascanDatasetWrapper( final Variable var ) {
            super(var);
        }
    }

    /**
     * Retrieve Terascan specific information.
     * 
     * @throws IOException
     */
    protected void initializeProfile() throws IOException {
        boolean checkProducts = true;
        if (dataset == null) {
            throw new IOException("Unable to initialize profile due to a null dataset");
        }
        final List<Variable> variables = dataset.getVariables();
        final List<Attribute> attributes = dataset.getGlobalAttributes();
        numGlobalAttributes = attributes.size();
        productList = HDF4TeraScanProperties.refineProductList(variables);
        final int numImages = productList != null ? productList.length : 0;
        setNumImages(numImages);

        indexMap = new HashMap<Range, TerascanDatasetWrapper>(numImages);

        // Scanning all the datasets
        try {
            for( Variable var : variables ) {
                final String name = var.getName();
                for( int j = 0; j < numImages; j++ ) {
                    // Checking if the actual dataset is a product.
                    if (!checkProducts || name.equals(productList[j])) {
                        // Updating the subDatasetsMap map
                        indexMap.put(new Range(j, j + 1), new TerascanDatasetWrapper(var));
                        break;
                    }
                }
            }
        } catch (InvalidRangeException e) {
            throw new IllegalArgumentException("Error occurred during NetCDF file parsing", e);
        }
    }

    /**
     * Returns a {@link TerascanDatasetWrapper} given a specified imageIndex.
     * 
     * @param imageIndex
     * @return a {@link TerascanDatasetWrapper}.
     */
    @Override
    protected HDF4DatasetWrapper getDatasetWrapper( int imageIndex ) {
        return (HDF4DatasetWrapper) getVariableWrapper(imageIndex);
    }

    public void dispose() {
        super.dispose();
        productList = null;
        streamMetadata = null;
        numGlobalAttributes = -1;
        numRasters = -1;
    }

    /**
     * Retrieve the fillValue for the specified imageIndex.
     * 
     * @throws IOException
     */
    public double getFillValue( final int imageIndex ) throws IOException {
        // FIXME this is the same as in UcarIR, just the fill constant is different
        // (but the same value) could it be merged?

        double fillValue = Double.NaN;
        String fillS = getAttributeAsString(imageIndex, HDF4TeraScanProperties.DatasetAttribs.FILL_VALUE);
        if (fillS != null && fillS.trim().length() > 0)
            fillValue = Double.parseDouble(fillS);
        return fillValue;
    }

    protected int getBandNumberFromProduct( String productName ) {
        return HDF4TeraScanProperties.terascanProducts.get(productName).getNBands();
    }

    /**
     * Retrieve the longName for the specified imageIndex.
     * 
     * @throws IOException
     */
    String getLongName( final int imageIndex ) throws IOException {
        String name = "";
        String nameS = getAttributeAsString(imageIndex, HDF4TeraScanProperties.DatasetAttribs.LONG_NAME);
        if (nameS != null) {
            name = nameS;
        }
        return name;
    }

    /**
     * Retrieve the scale factor for the specified imageIndex. Return
     * {@code Double.NaN} if parameter isn't available
     * 
     * @throws IOException
     */
    public double getScale( final int imageIndex ) throws IOException {
        // FIXME this is the same as in UcarIR, just the scale constant is different
        // (but the same value) could it be merged?

        double scale = Double.NaN;
        String scaleS = getAttributeAsString(imageIndex, HDF4TeraScanProperties.DatasetAttribs.SCALE_FACTOR);
        if (scaleS != null && scaleS.trim().length() > 0)
            scale = Double.parseDouble(scaleS);
        return scale;
    }

    /**
     * Retrieve the offset factor for the specified imageIndex. Return
     * {@code Double.NaN} if parameter isn't available
     * 
     * @throws IOException
     */
    public double getOffset( final int imageIndex ) throws IOException {
        // FIXME this is the same as in UcarIR, just the offset constant is different
        // (but the same value) could it be merged?

        double offset = Double.NaN;
        String offsetS = getAttributeAsString(imageIndex, HDF4TeraScanProperties.DatasetAttribs.ADD_OFFSET);
        if (offsetS != null && offsetS.trim().length() > 0)
            offset = Double.parseDouble(offsetS);
        return offset;
    }

    public void reset() {
        super.reset();
    }

    /**
     * @see javax.imageio.ImageReader#getImageMetadata(int, java.lang.String, java.util.Set)
     */
    @Override
    public IIOMetadata getImageMetadata( int imageIndex, String formatName, Set<String> nodeNames ) throws IOException {
        initialize();
        checkImageIndex(imageIndex);
        if (formatName.equalsIgnoreCase(HDF4TeraScanImageMetadata.nativeMetadataFormatName))
            return new HDF4TeraScanImageMetadata(this, imageIndex);

        // fallback on the super type metadata
        return super.getImageMetadata(imageIndex, formatName, nodeNames);
    }

    /**
     * @see javax.imageio.ImageReader#getStreamMetadata(java.lang.String, java.util.Set)
     */
    @Override
    public synchronized IIOMetadata getStreamMetadata( String formatName, Set<String> nodeNames ) throws IOException {
        if (formatName.equalsIgnoreCase(HDF4TeraScanStreamMetadata.nativeMetadataFormatName)) {
            if (streamMetadata == null)
                streamMetadata = new HDF4TeraScanStreamMetadata(this);
            return streamMetadata;
        }
        return super.getStreamMetadata(formatName, nodeNames);
    }

    public synchronized IIOMetadata getStreamMetadata() throws IOException {
        return getStreamMetadata(HDF4TeraScanStreamMetadata.nativeMetadataFormatName, null);
    }

    @Override
    public VariableWrapper getVariableWrapper( int imageIndex ) {
        checkImageIndex(imageIndex);
        VariableWrapper wrapper = null;
        for( Range range : indexMap.keySet() ) {
            if (range.contains(imageIndex) && range.first() <= imageIndex && imageIndex < range.last()) {
                wrapper = indexMap.get(range);
            }
        }
        return wrapper;
    }

    @Override
    public int getNumGlobalAttributes() {
        return numGlobalAttributes;
    }

    /**
     * Returns the ImageMetadata obtained from the underlying flatReader
     * 
     * @param imageIndex
     *                the index of the specified 2D raster.
     */
    public IIOMetadata getImageMetadata( int imageIndex, final String metadataFormat ) throws IOException {
        IIOMetadata metadata;
        synchronized (metadataMap) {
            if (!metadataMap.containsKey(imageIndex)) {
                metadata = new HDF4TeraScanImageMetadata(this, imageIndex);
                metadataMap.put(imageIndex, metadata);
            } else {
                metadata = (UnidataImageMetadata) metadataMap.get(imageIndex);
                if (metadata == null) {
                    metadata = new HDF4TeraScanImageMetadata(this, imageIndex);
                    metadataMap.put(imageIndex, metadata);
                }
            }
        }
        return metadata;
    }

    public IIOMetadata getImageMetadata( int imageIndex ) throws IOException {
        IIOMetadata metadata;
        synchronized (metadataMap) {
            if (!metadataMap.containsKey(imageIndex)) {
                metadata = new HDF4TeraScanImageMetadata(this, imageIndex);
                metadataMap.put(imageIndex, metadata);
            } else {
                metadata = (UnidataImageMetadata) metadataMap.get(imageIndex);
                if (metadata == null) {
                    metadata = new HDF4TeraScanImageMetadata(this, imageIndex);
                    metadataMap.put(imageIndex, metadata);
                }
            }
        }
        return metadata;
    }

    public HDF4_TYPE getHDF4Type() {
        return HDF4_TYPE.TeraScan;
    }

}
