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
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.util.Converters;
import org.geotools.util.Utilities;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.parameter.GeneralParameterDescriptor;
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
public final class ImageMosaicFormat extends AbstractGridFormat implements Format {

    final static double DEFAULT_ARTIFACTS_FILTER_PTILE_THRESHOLD = 0.1;
    
    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageMosaicFormat.class.toString());
    
    static final Interpolation DEFAULT_INTERPOLATION = new InterpolationNearest();

    /** Optional Time value for this mosaic. */
    public static final ParameterDescriptor<List> TIME = DefaultParameterDescriptor.create("TIME", "A list of time objects",List.class, null,false);    
    
    /** Filter tiles based on attributes from the input coverage*/
    public static final ParameterDescriptor<Filter> FILTER = new DefaultParameterDescriptor<Filter>("Filter", Filter.class, null, null);
    
    /** Control the type of the final mosaic. */
    public static final ParameterDescriptor<Boolean> FADING = new DefaultParameterDescriptor<Boolean>("Fading", Boolean.class, new Boolean[]{Boolean.TRUE,Boolean.FALSE}, Boolean.FALSE);

    /** Control the transparency of the output coverage. */
    public static final ParameterDescriptor<Color> OUTPUT_TRANSPARENT_COLOR = new DefaultParameterDescriptor<Color>(
            "OutputTransparentColor", Color.class, null, null);
    
    /** Control the thresholding on the input coverage */
    public static final ParameterDescriptor<Integer> MAX_ALLOWED_TILES = new DefaultParameterDescriptor<Integer>(
            "MaxAllowedTiles", Integer.class, null, Integer.MAX_VALUE);
    
    /** Control the default artifact filter luminance thresholding on the input coverages */
    public static final ParameterDescriptor<Integer> DEFAULT_ARTIFACTS_FILTER_THRESHOLD = new DefaultParameterDescriptor<Integer>(
            "DefaultArtifactsFilterThreshold", Integer.class, null, Integer.MIN_VALUE);

    /** Control the artifact filter ptile thresholding */
    public static final ParameterDescriptor<Double> ARTIFACTS_FILTER_PTILE_THRESHOLD = new DefaultParameterDescriptor<Double>(
            "ArtifactsFilterPtileThreshold", Double.class, null, Double.valueOf(DEFAULT_ARTIFACTS_FILTER_PTILE_THRESHOLD));
    
    /** Control the threading behavior for this plugin.*/
    public static final ParameterDescriptor<Boolean> ALLOW_MULTITHREADING = new DefaultParameterDescriptor<Boolean>(
            "AllowMultithreading", Boolean.class, new Boolean[]{Boolean.TRUE,Boolean.FALSE}, Boolean.FALSE);
    
    /** Control the footprint management.*/
    public static final ParameterDescriptor<Boolean> HANDLE_FOOTPRINT = new DefaultParameterDescriptor<Boolean>(
            "HandleFootprint", Boolean.class, new Boolean[]{Boolean.TRUE,Boolean.FALSE}, Boolean.TRUE);
    
    /** Control whether to add the ROI in the output mosaic. */
    public static final ParameterDescriptor<Boolean> SET_ROI_PROPERTY = new DefaultParameterDescriptor<Boolean>(
            "SetRoiProperty", Boolean.class, new Boolean[]{Boolean.TRUE,Boolean.FALSE}, Boolean.FALSE);
    
    /** Control the background values for the output coverage */
    public static final ParameterDescriptor<double[]> BACKGROUND_VALUES = new DefaultParameterDescriptor<double[]>(
            "BackgroundValues", double[].class, null, null);
    
    /** Control the interpolation to be used in mosaicking */
    public static final ParameterDescriptor<Interpolation> INTERPOLATION = new DefaultParameterDescriptor<Interpolation>(
            "Interpolation", Interpolation.class, null, DEFAULT_INTERPOLATION);

    /** Optional Elevation value for this mosaic. */
    public static final ParameterDescriptor<List> ELEVATION = DefaultParameterDescriptor.create("ELEVATION", "An elevation value",List.class, null,false);

    
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
                OUTPUT_TRANSPARENT_COLOR,
                USE_JAI_IMAGEREAD,
                BACKGROUND_VALUES,
                SUGGESTED_TILE_SIZE,
                ALLOW_MULTITHREADING,
                MAX_ALLOWED_TILES,
                TIME,
                ELEVATION,
                FILTER
        }));

        // reading parameters
        writeParameters = null;
    }

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#getReader(Object)
     */
    @Override
    public ImageMosaicReader getReader( Object source ) {
        return getReader(source, null);
    }

    /**
     * 
     */
    @Override
    public GridCoverageWriter getWriter( Object destination ) {
        throw new UnsupportedOperationException("This plugin does not support writing.");
    }

    @Override
    public boolean accepts( Object source, Hints hints ) {
        Utilities.ensureNonNull("source", source);
            if (source instanceof ImageMosaicDescriptor){
                return checkDescriptor((ImageMosaicDescriptor)source);
            } else {
                return checkForUrl(source, hints);
            }
    }
    
    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#accepts(Object input)
     */
    @Override
    public boolean accepts( Object source ) {
        return accepts(source, null);
    }
            
    /**
     * Checks that the provided {@link ImageMosaicDescriptor} is well formed.
     *             
     * @param source
     * @return 
     */
    private boolean checkDescriptor(final ImageMosaicDescriptor source) {
        //TODO: improve checks
        final GranuleCatalog catalog = source.getCatalog();
        final MosaicConfigurationBean configuration = source.getConfiguration();
        if (configuration == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Mosaic configuration is missing");
            }
            return false;
        }
        if (configuration.getLevels() == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("resolution leves is unavailable ");
            }

            return false;
        }
        if (catalog == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Granule Catalog is unavailable ");
            }
            return false;
        }
        return true;
    }

    private boolean checkForUrl( Object source, Hints hints){
         try {
            
            URL sourceURL = Utils.checkSource(source, hints);
            if(sourceURL == null){
            	return false;
            }
            if( source instanceof File){
                File file = (File) source;
                if( !file.exists() ){
                    return false; // file does not exist
                }
            }
            // /////////////////////////////////////////////////////////////////////
            //
            // Load tiles informations, especially the bounds, which will be
            // reused
            //
            // /////////////////////////////////////////////////////////////////////
            DataStore tileIndexStore = null;
            CoordinateReferenceSystem crs=null;
            boolean shapefile=true;
            try{
            	final File sourceF= DataUtilities.urlToFile(sourceURL);
            	if(FilenameUtils.getName(sourceF.getAbsolutePath()).equalsIgnoreCase("datastore.properties"))
            	{
            		shapefile=false;
            		// load spi anche check it
            		// read the properties file
        			final Properties properties = new Properties();
        			final FileInputStream stream = new FileInputStream(sourceF);
        		    try {
        		        properties.load(stream);
        		    }
        		    finally{
        		    	IOUtils.closeQuietly(stream);
        		    }
        		    
        		    // SPI
        		    final String SPIClass=properties.getProperty("SPI");
    		    	// create a datastore as instructed
    				final DataStoreFactorySpi spi= (DataStoreFactorySpi) Class.forName(SPIClass).newInstance();
    				
    				// get the params
    				final Map<String, Serializable> params = new HashMap<String, Serializable>();	
    				final Param[] paramsInfo = spi.getParametersInfo();
    				for(Param p:paramsInfo){
    					// search for this param and set the value if found
    					if(properties.containsKey(p.key))
    						params.put(p.key, (Serializable)Converters.convert(properties.getProperty(p.key), p.type));
    					else
    						if(p.required&& p.sample==null)
    						{
    			     			if (LOGGER.isLoggable(Level.FINE))
    		        				LOGGER.fine("Required parameter missing: "+p.toString());
    							return false;
    						}
    				}						
    				tileIndexStore=spi.createDataStore(params);
        			if(tileIndexStore==null)
        				return false;
              		
            	}
            	else {
            	    URL testPropertiesUrl=DataUtilities.changeUrlExt(sourceURL, "properties");
            	    File testFile= DataUtilities.urlToFile(testPropertiesUrl);
            	    if( !testFile.exists() ){
            	        return false;
            	    }
                    
            	    tileIndexStore=new ShapefileDataStore(sourceURL);
            	}
                final String[] typeNames = tileIndexStore.getTypeNames();
                if (typeNames.length <= 0)
                    return false;
                final String typeName = typeNames[0];
                if(typeName==null)
    				return false;
                
                final SimpleFeatureSource featureSource = tileIndexStore.getFeatureSource(typeName);
                if(featureSource==null)
    				return false;
                
                final SimpleFeatureType schema = featureSource.getSchema();
                if(schema==null)
    				return false;
                
                crs = featureSource.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem();
                if(crs==null)
    				return false;
   
	            // /////////////////////////////////////////////////////////////////////
	            //
	            // Now look for the properties file and try to parse relevant fields
	            //
	            // /////////////////////////////////////////////////////////////////////            
                URL propsUrl = null;
                if(shapefile)
                	propsUrl=DataUtilities.changeUrlExt(sourceURL, "properties");
                else
                {
        			//
					// do we have a datastore properties file? It will preempt on the shapefile
					//
                	final File parent=DataUtilities.urlToFile(sourceURL).getParentFile();
					
					// this can be used to look for properties files that do NOT define a datastore
					final File[] properties = parent.listFiles(
							(FilenameFilter)
							FileFilterUtils.andFileFilter(
									FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("indexer.properties")),
								FileFilterUtils.andFileFilter(
										FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("datastore.properties")),
										FileFilterUtils.makeFileOnly(FileFilterUtils.suffixFileFilter(".properties")
								)
							)
					));
					
					// do we have a valid datastore + mosaic properties pair?
					for(File propFile:properties)
						if(Utils.checkFileReadable(propFile)&&
								Utils.loadMosaicProperties(DataUtilities.fileToURL(propFile), "")!=null)
						{
							propsUrl=DataUtilities.fileToURL(propFile);
							break;
						}               	
                }
	            
	            //get the properties file
	            final MosaicConfigurationBean props = Utils.loadMosaicProperties(propsUrl, "location");
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
    @Override
    public ImageMosaicReader getReader( Object source, Hints hints ) {
        try {

            final ImageMosaicReader reader = new ImageMosaicReader(source, hints);
//            reader.multiThreadedLoader = mtLoader;
            return reader;
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
    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        throw new UnsupportedOperationException("Unsupported method.");
    }

	@Override
	public GridCoverageWriter getWriter(Object destination, Hints hints) {
		throw new UnsupportedOperationException("This plugin does not support writing.");
	}

}
