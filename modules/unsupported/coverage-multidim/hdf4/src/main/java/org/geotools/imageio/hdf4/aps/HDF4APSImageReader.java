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
package org.geotools.imageio.hdf4.aps;

import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.imageio.hdf4.HDF4ImageReader;
import org.geotools.imageio.hdf4.HDF4ImageReaderSpi.HDF4_TYPE;
import org.geotools.imageio.hdf4.terascan.HDF4TeraScanImageMetadata;
import org.geotools.imageio.netcdf.NetCDFUtilities;
import org.geotools.imageio.unidata.UnidataImageMetadata;
import org.geotools.imageio.unidata.VariableWrapper;

import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;

/**
 * {@link HDF4APSImageReader} is a {@link ImageReader} able to create
 * {@link RenderedImage} from APS generated HDF sources.
 * 
 * @author Daniele Romagnoli
 */
public class HDF4APSImageReader extends HDF4ImageReader {

    /** The Products Dataset List contained within the APS File */
    private String[] productList;

    /** The name of the SDS containing projection */
    private String projectionDatasetName;

    private HDF4APSStreamMetadata streamMetadata;

    Map<String, String> projectionMap = null;

    private Map<Range, APSDatasetWrapper> indexMap;

    private class APSDatasetWrapper extends HDF4DatasetWrapper {
        private APSDatasetWrapper( Variable var ) {
            super(var);
        }
    }

    /**
     * Initialize main properties for this <code>HDF4APSImageReader</code>
     */
    protected void initializeProfile() throws IOException {

        if (dataset == null) {
            throw new IOException("Unable to initialize profile due to a null dataset");
        }
        final List<Variable> variables = dataset.getVariables();
        final List<Attribute> attributes = dataset.getGlobalAttributes();
        final int numVars = variables.size();
        numGlobalAttributes = attributes.size();

        // //
        //
        // Getting projection dataset name
        //
        // //

        final String navAttrib = NetCDFUtilities.getGlobalAttributeAsString(dataset, HDF4APSProperties.PFA_NA_MAPPROJECTION);
        if (navAttrib != null && navAttrib.length() > 0) {
            projectionDatasetName = navAttrib;
        }

        final String prodAttrib = NetCDFUtilities.getGlobalAttributeAsString(dataset, HDF4APSProperties.PRODLIST);
        int numImages = 0;
        if (prodAttrib != null && prodAttrib.length() > 0) {
            String products[] = prodAttrib.split(",");
            productList = HDF4APSProperties.refineProductList(products);
            numImages = productList.length;
        } else {
            numImages = numVars;
        }
        setNumImages(numImages);
        indexMap = new HashMap<Range, APSDatasetWrapper>(numImages);

        Variable varProjection;
        // //
        //
        // Setting spatial domain
        //
        // //

        // getting map dataset
        varProjection = dataset.findVariable(projectionDatasetName);
        if (varProjection != null && varProjection.getName().equalsIgnoreCase(projectionDatasetName)) {
            // TODO: All projection share the same dataset
            // structure?
            Array data = varProjection.read();
            final int datatype = NetCDFUtilities.getRawDataType(varProjection);
            if (projectionMap == null) {
                projectionMap = buildProjectionAttributesMap(data, datatype);
                // Force UoM of MapBoundary product as the last element in
                // the map
            }
        }
        try {
            // Scanning all the datasets
            for( Variable var : variables ) {
                final String name = var.getName();
                for( int j = 0; j < numImages; j++ ) {
                    // Checking if the actual dataset is a product.
                    if (name.equals(productList[j])) {
                        // Updating the subDatasetsMap map
                        indexMap.put(new Range(j, j + 1), new APSDatasetWrapper(var));
                        break;
                    }
                }
            }
        } catch (InvalidRangeException e) {
            throw new IllegalArgumentException("Error occurred during NetCDF file parsing", e);
        }
    }

    private static Map<String, String> buildProjectionAttributesMap( final Array data, int datatype ) {
        final Map<String, String> projMap = new LinkedHashMap<String, String>(29);

        if (datatype == DataBuffer.TYPE_DOUBLE && data instanceof ArrayDouble) {
            double[] values = (double[]) data.get1DJavaArray(double.class);
            // TODO: I need to build a parser or a formatter to properly
            // interpret these settings
            projMap.put("Code", Double.toString(values[0]));
            projMap.put(HDF4APSStreamMetadata.PROJECTION, Double.toString(values[1]));
            projMap.put(HDF4APSStreamMetadata.ZONE, Double.toString(values[2]));
            projMap.put(HDF4APSStreamMetadata.DATUM, Double.toString(values[3]));
            projMap.put(HDF4APSStreamMetadata.SEMI_MAJOR_AXIS, Double.toString(values[4]));
            projMap.put(HDF4APSStreamMetadata.SEMI_MINOR_AXIS, Double.toString(values[5]));
            projMap.put("Param2", Double.toString(values[6]));
            projMap.put("Param3", Double.toString(values[7]));
            projMap.put(HDF4APSStreamMetadata.LONGITUDE_OF_CENTRAL_MERIDIAN, Double.toString(values[8]));
            projMap.put(HDF4APSStreamMetadata.LATITUDE_OF_TRUE_SCALE, Double.toString(values[9]));
            projMap.put(HDF4APSStreamMetadata.FALSE_EASTINGS, Double.toString(values[10]));
            projMap.put(HDF4APSStreamMetadata.FALSE_NORTHINGS, Double.toString(values[11]));
            projMap.put("Param8", Double.toString(values[12]));
            projMap.put("Param9", Double.toString(values[13]));
            projMap.put("Param10", Double.toString(values[14]));
            projMap.put("Param11", Double.toString(values[15]));
            projMap.put("Param12", Double.toString(values[16]));
            projMap.put("Param13", Double.toString(values[17]));
            projMap.put("Param14", Double.toString(values[18]));
            projMap.put("Width", Double.toString(values[19]));
            projMap.put("Height", Double.toString(values[20]));
            projMap.put("Longitude_1", Double.toString(values[21]));
            projMap.put("Latitude_1", Double.toString(values[22]));
            projMap.put("Pixel_1", Double.toString(values[23]));
            projMap.put("Line_1", Double.toString(values[24]));
            projMap.put("Longitude_2", Double.toString(values[25]));
            projMap.put("Latitude_2", Double.toString(values[26]));
            projMap.put("Delta", Double.toString(values[27]));
            projMap.put("Aspect", Double.toString(values[28]));
        }
        return projMap;
    }

    public HDF4APSImageReader( ImageReaderSpi originatingProvider ) {
        super(originatingProvider);
    }

    protected int getBandNumberFromProduct( String productName ) {
        return HDF4APSProperties.apsProducts.get(productName).getNBands();
    }

    public void dispose() {
        super.dispose();
        productList = null;
        streamMetadata = null;
    }

    String getDatasetName( final int imageIndex ) {
        checkImageIndex(imageIndex);
        String datasetName = "";
        APSDatasetWrapper wrapper = (APSDatasetWrapper) getDatasetWrapper(imageIndex);
        if (wrapper != null) {
            datasetName = wrapper.getVariable().getName();
        }
        return datasetName;
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
        if (formatName.equalsIgnoreCase(HDF4APSImageMetadata.nativeMetadataFormatName))
            return new HDF4APSImageMetadata(this, imageIndex);

        // fallback on the super type metadata
        return super.getImageMetadata(imageIndex, formatName, nodeNames);
    }

    /**
     * @see javax.imageio.ImageReader#getStreamMetadata(java.lang.String, java.util.Set)
     */
    @Override
    public synchronized IIOMetadata getStreamMetadata( String formatName, Set<String> nodeNames ) throws IOException {
        if (formatName.equalsIgnoreCase(HDF4APSStreamMetadata.nativeMetadataFormatName)) {
            if (streamMetadata == null)
                streamMetadata = new HDF4APSStreamMetadata(this);
            return streamMetadata;
        }
        return super.getStreamMetadata(formatName, nodeNames);
    }

    public synchronized IIOMetadata getStreamMetadata() throws IOException {
        return getStreamMetadata(HDF4APSStreamMetadata.nativeMetadataFormatName, null);
    }

    @Override
    protected HDF4DatasetWrapper getDatasetWrapper( int imageIndex ) {
        return (HDF4DatasetWrapper) getVariableWrapper(imageIndex);
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
        return HDF4_TYPE.APS;
    }

}
