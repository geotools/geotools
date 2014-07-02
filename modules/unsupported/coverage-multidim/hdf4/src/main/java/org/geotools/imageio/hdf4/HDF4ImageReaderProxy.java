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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.imageio.hdf4.HDF4ImageReaderSpi.HDF4_TYPE;
import org.geotools.imageio.netcdf.NetCDFUtilities;
import org.geotools.imageio.netcdf.NetCDFUtilities.KeyValuePair;
import org.geotools.imageio.unidata.VariableWrapper;

import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.iosp.hdf4.H4iosp;
/**
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class HDF4ImageReaderProxy extends HDF4ImageReader{
	
	private HDF4ImageReader wrappee;

	protected HDF4ImageReaderProxy(ImageReaderSpi originatingProvider) {
		super(originatingProvider);
		if(!(originatingProvider instanceof HDF4ImageReaderSpi))
			throw new IllegalArgumentException("The originatingProvider is not of type HDF4TeraScanImageReaderSpi but of type "+originatingProvider.getClass().toString());
	}

	public void dispose() {
		try{
			wrappee.dispose();
		}
		finally{
			wrappee=null;
		}

	}

	public KeyValuePair getAttribute(int imageIndex, int attributeIndex)
			throws IOException {
		return wrappee.getAttribute(imageIndex, attributeIndex);
	}

	public String getAttributeAsString(int imageIndex, String attributeName) {
		return wrappee.getAttributeAsString(imageIndex, attributeName);
	}

	public String getAttributeAsString(int imageIndex, String attributeName,
			boolean isUnsigned) {
		return wrappee.getAttributeAsString(imageIndex, attributeName,isUnsigned);
	}

	public KeyValuePair getGlobalAttribute(int attributeIndex)
			throws IOException {
		return wrappee.getGlobalAttribute(attributeIndex);
	}

	public int getHeight(int imageIndex) throws IOException {
		return wrappee.getHeight(imageIndex);
	}

	public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
		return wrappee.getImageMetadata(imageIndex);
	}

	public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex)
			throws IOException {
		return wrappee.getImageTypes(imageIndex);
	}

	public IIOMetadata getStreamMetadata() throws IOException {
		return null;
	}

	public int getTileHeight(int imageIndex) throws IOException {
		return wrappee.getTileHeight(imageIndex);
	}

	public int getTileWidth(int imageIndex) throws IOException {
		return wrappee.getTileWidth(imageIndex);
	}

	public int getWidth(int imageIndex) throws IOException {
		return wrappee.getWidth(imageIndex);
	}

	public BufferedImage read(int imageIndex, ImageReadParam param)
			throws IOException {
		return wrappee.read(imageIndex, param);
	}

	public void reset() {
		wrappee.reset();
		wrappee=null;

	}

	@Override
	public void setInput(Object input, boolean seekForwardOnly,boolean ignoreMetadata) {
	      // ////////////////////////////////////////////////////////////////////
	      //
	      // Reset the state of this reader
	      //
	      // Prior to set a new input, I need to do a pre-emptive reset in order
	      // to clear any value-object related to the previous input.
	      // ////////////////////////////////////////////////////////////////////
	
	      // TODO: Add URL & String support.
	      if (wrappee != null)
	          reset();
	      try {
	      	//open up a dataset and check that it actually is an hdf4
	      	final NetcdfDataset dataset = NetCDFUtilities.getDataset(input);
	      	
	      	// is it open? is it an hdf4?
	      	if(dataset!=null){
	      		if(!(dataset.getIosp() instanceof H4iosp))
	      			throw new IllegalArgumentException("Provided dataset is not an HDF4 file");
	      	}
	      	else
	      		throw new IllegalArgumentException("Provided dataset is not an HDF4 file");
	      	
	          wrappee.setInput(input, seekForwardOnly, ignoreMetadata);	          
	          wrappee.initialize();
	      } catch (IOException e) {
	          throw new IllegalArgumentException("Not a Valid Input", e);
	      }		

	}
	@Override
	public void setInput(Object input, boolean seekForwardOnly) {
        this.setInput(input, seekForwardOnly, true);

	}
	@Override
	public void setInput(Object input) {
        this.setInput(input, true, true);

	}
	
	public HDF4ImageReader getWrappee(){
		return wrappee;
	}

	@Override
	protected HDF4DatasetWrapper getDatasetWrapper(int imageIndex) {
		return wrappee.getDatasetWrapper(imageIndex);
	}

	@Override
	protected void initializeProfile() throws IOException {
		wrappee.initializeProfile();
	}

    @Override
    public VariableWrapper getVariableWrapper( int imageIndex ) {
        return wrappee.getVariableWrapper(imageIndex);
    }

    @Override
    public int getNumGlobalAttributes() {
        return wrappee.getNumGlobalAttributes();
    }

    @Override
    protected HDF4_TYPE getHDF4Type() {
        return wrappee.getHDF4Type();
    }

    @Override
    protected IIOMetadata getImageMetadata( int imageIndex, String metadataFormat ) throws IOException {
        return wrappee.getImageMetadata(imageIndex, metadataFormat);
    }
}
