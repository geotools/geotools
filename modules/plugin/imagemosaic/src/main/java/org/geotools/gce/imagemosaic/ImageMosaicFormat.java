/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.ImageMosaicUtils.MosaicConfigurationBean;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * {@link AbstractGridFormat} subclass for controlling {@link ImageMosaicReader} creation. As the
 * name says, it handles mosaic of georeferenced images, which means
 * <ol>
 * <li>tiff+tfw+prj</li>
 * <li>jpeg+tfw+prj</li>
 * <li>png+tfw+prj</li>
 * <li>geotiff</li>
 * </ol>
 * This does not mean that you throw there a couple of images and it will do the trick no matter how
 * these images are. Requirements are:
 * <ul>
 * <li>(almost) equal spatial resolution</li>
 * <li>same number of bands</li>
 * <li>same data type</li>
 * <li>same projection</li>
 * </ul>
 * The first requirement can be relaxed a little but if they have the same spatial resolution the
 * performances are much better. There are parameters that you can use to control the behaviour of
 * the mosaic in terms of thresholding and transparency. They are as follows:
 * <ul>
 * <li>--DefaultParameterDescriptor FINAL_ALPHA = new DefaultParameterDescriptor( "FinalAlpha",
 * Boolean.class, null, Boolean.FALSE)-- It asks the plugin to add transparency on the final created
 * mosaic. IT simply performs a threshonding looking for areas where there is no data, i.e.,
 * intensity is really low and transform them into transparent areas. It is obvious that depending
 * on the nature of the input images it might interfere with the original values.</li>
 * <li>---ALPHA_THRESHOLD = new DefaultParameterDescriptor( "AlphaThreshold", Double.class, null,
 * new Double(1));--- Controls the transparency addition by specifying the treshold to use.</li>
 * <li>INPUT_IMAGE_THRESHOLD = new DefaultParameterDescriptor( "InputImageROI", Boolean.class,
 * null, Boolean.FALSE)--- INPUT_IMAGE_THRESHOLD_VALUE = new DefaultParameterDescriptor(
 * "InputImageROIThreshold", Integer.class, null, new Integer(1));--- These two can be used to
 * control the application of ROIs on the input images based on tresholding values. Basically using
 * the threshold you can ask the mosaic plugin to load or not certain pixels of the original images.</li>
 * 
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 * @since 2.3
 */
@SuppressWarnings("deprecation")
public final class ImageMosaicFormat extends AbstractGridFormat implements Format {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageMosaicFormat.class.toString());
    
    /** The {@code String} representing the parameter to customize tile sizes */
    private static final String SUGGESTED_TILESIZE = "SUGGESTED_TILE_SIZE";

    /**
     * This {@link GeneralParameterValue} can be provided to the
     * {@link GridCoverageReader}s through the
     * {@link GridCoverageReader#read(GeneralParameterValue[])} method in order
     * to specify the suggested size of tiles to avoid long time reading
     * occurring with JAI ImageRead on striped images. (Images with tiles Nx1)
     * Value should be a String in the form of "W,H" (without quotes) where W is
     * a number representing the suggested tileWidth and H is a number
     * representing the suggested tileHeight.
     */
    public static final DefaultParameterDescriptor<String> SUGGESTED_TILE_SIZE = new DefaultParameterDescriptor<String>(
    		SUGGESTED_TILESIZE, String.class, null, "512,512");

    public static final String TILE_SIZE_SEPARATOR = ",";
    
    /** Control the type of the final mosaic. */
    public static final ParameterDescriptor<Boolean> FADING = new DefaultParameterDescriptor<Boolean>(
            "Fading", Boolean.class, new Boolean[]{Boolean.TRUE,Boolean.FALSE}, Boolean.FALSE);

    /** Control the transparency of the input coverages. */
    public static final ParameterDescriptor<Color> INPUT_TRANSPARENT_COLOR = new DefaultParameterDescriptor<Color>(
            "InputTransparentColor", Color.class, null, null);

    /** Control the transparency of the output coverage. */
    public static final ParameterDescriptor<Color> OUTPUT_TRANSPARENT_COLOR = new DefaultParameterDescriptor<Color>(
            "OutputTransparentColor", Color.class, null, null);

    /** Control the thresholding on the input coverage.
     * 
     * @deprecated we don't use this param anymore, since it is confusing and interact badly with the transparency
     */
    public static final ParameterDescriptor<Double> INPUT_IMAGE_THRESHOLD_VALUE = new DefaultParameterDescriptor<Double>(
            "InputImageThresholdValue", Double.class, null, new Double(Double.NaN));
    
    /** Control the thresholding on the input coverage */
    public static final ParameterDescriptor<Integer> MAX_ALLOWED_TILES = new DefaultParameterDescriptor<Integer>(
            "MaxAllowedTiles", Integer.class, null, Integer.MAX_VALUE);
    
    /** Control the threading behavior for this plugin. This parameter contains the number of thread that we should use to load the granules. Default value is 0 which means not additional thread, max value is 8.*/
    public static final ParameterDescriptor<Boolean> ALLOW_MULTITHREADING = new DefaultParameterDescriptor<Boolean>(
            "AllowMultithreading", Boolean.class, new Boolean[]{Boolean.TRUE,Boolean.FALSE}, Boolean.FALSE);
    
    /** Control the footprint management.*/
    public static final ParameterDescriptor<Boolean> HANDLE_FOOTPRINT = new DefaultParameterDescriptor<Boolean>(
            "HandleFootprint", Boolean.class, new Boolean[]{Boolean.TRUE,Boolean.FALSE}, Boolean.FALSE);
    
    /** Control the background values for the output coverage */
    public static final ParameterDescriptor<double[]> BACKGROUND_VALUES = new DefaultParameterDescriptor<double[]>(
            "BackgroundValues", double[].class, null, null);
    
    /**
     * Creates an instance and sets the metadata.
     */
    public ImageMosaicFormat() {
        setInfo();
    }

    /**
     * Sets the metadata information.
     */
    private void setInfo() {
        final HashMap<String,String> info = new HashMap<String,String> ();
        info.put("name", "ImageMosaic");
        info.put("description", "Image mosaicking plugin");
        info.put("vendor", "Geotools");
        info.put("docURL", "");
        info.put("version", "1.0");
        mInfo = info;

        // reading parameters
        readParameters = new ParameterGroup(new DefaultParameterDescriptorGroup(mInfo,
                new GeneralParameterDescriptor[]{
        		READ_GRIDGEOMETRY2D,
        		INPUT_TRANSPARENT_COLOR,
                INPUT_IMAGE_THRESHOLD_VALUE, 
                OUTPUT_TRANSPARENT_COLOR,
                USE_JAI_IMAGEREAD,
                HANDLE_FOOTPRINT,
                BACKGROUND_VALUES,
                SUGGESTED_TILE_SIZE,
                ALLOW_MULTITHREADING,
                MAX_ALLOWED_TILES}));

        // reading parameters
        writeParameters = null;
    }

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#getReader(Object)
     */
    public ImageMosaicReader getReader( Object source ) {
        return getReader(source, null);
    }

    /**
     * 
     */
    public GridCoverageWriter getWriter( Object destination ) {
        throw new UnsupportedOperationException("This plugin does not support writing.");
    }

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#accepts(Object input)
     */
    public boolean accepts( Object source ) {
        try {
            URL sourceURL = ImageMosaicUtils.checkSource(source);
            if(sourceURL==null)
            	return false;
            // /////////////////////////////////////////////////////////////////////
            //
            // Load tiles informations, especially the bounds, which will be
            // reused
            //
            // /////////////////////////////////////////////////////////////////////
            ShapefileDataStore tileIndexStore = null;
            CoordinateReferenceSystem crs=null;
            try{
            	tileIndexStore=new ShapefileDataStore(sourceURL);
                final String[] typeNames = tileIndexStore.getTypeNames();
                if (typeNames.length <= 0)
                    return false;
                final String typeName = typeNames[0];
                final FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = tileIndexStore.getFeatureSource(typeName);
                final SimpleFeatureType schema = featureSource.getSchema();
                crs = featureSource.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem();
   
	            // /////////////////////////////////////////////////////////////////////
	            //
	            // Now look for the properties file and try to parse relevant fields
	            //
	            // /////////////////////////////////////////////////////////////////////            
                URL propsUrl = DataUtilities.changeUrlExt(sourceURL, "properties");
                try {
                	propsUrl.openStream().close();
                } catch (Exception e) {
	                throw new FileNotFoundException(".properties file, descibing the ImageMoasic, cant be opened:"+propsUrl);
				}
	            
	            //get the properties file
	            final MosaicConfigurationBean props = ImageMosaicUtils.loadPropertiesFile(sourceURL, crs,ImageMosaicUtils.DEFAULT_LOCATION_ATTRIBUTE);
	            if(props==null)
	            	return false;
	            
                // looking for the location attribute
	            final String locationAttributeName=props.getLocationAttribute();
                if (schema.getDescriptor(locationAttributeName) == null)
                    return false;   
                
	    		return true;
    		
            }finally{
        		try {
        			if(tileIndexStore!=null)
        				tileIndexStore.dispose();
        		} catch (Throwable e) {
        			if (LOGGER.isLoggable(Level.FINE))
        				LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
        		}            	
            }
    		
       } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            return false;

        }
       

    }

	/**
     * @see AbstractGridFormat#getReader(Object, Hints)
     */
    public ImageMosaicReader getReader( Object source, Hints hints ) {
        try {

            return new ImageMosaicReader(source, hints);
        } catch (MalformedURLException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * Throw an exception since this plugin is readonly.
     * 
     * @return nothing.
     */
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        throw new UnsupportedOperationException("Unsupported method.");
    }

}
