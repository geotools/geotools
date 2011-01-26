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
package org.geotools.gce.imagemosaic;

import it.geosolutions.imageio.stream.input.spi.URLImageInputStreamSpi;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.RasterFactory;
import javax.media.jai.remote.SerializableRenderedImage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.gce.imagemosaic.IndexBuilder.ExceptionEvent;
import org.geotools.gce.imagemosaic.IndexBuilder.IndexBuilderConfiguration;
import org.geotools.gce.imagemosaic.IndexBuilder.ProcessingEvent;
import org.geotools.gce.imagemosaic.IndexBuilder.ProcessingEventListener;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;
import org.opengis.geometry.BoundingBox;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Sparse utilities for the various mosaic classes. I use them to extract complex code from other places.
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 *
 */
class ImageMosaicUtils {
	
	static class Prop {
		final static String LOCATION_ATTRIBUTE = "LocationAttribute";
		final static String ENVELOPE2D = "Envelope2D";
		final static String LEVELS_NUM = "LevelsNum";
		final static String LEVELS = "Levels";
		final static String SUGGESTED_SPI = "SuggestedSPI";
		final static String EXP_RGB = "ExpandToRGB";
		final static String ABSOLUTE_PATH = "AbsolutePath";
		final static String NAME = "Name";
		final static String FOOTPRINT_MANAGEMENT = "FootprintManagement";	
	}
	
	/**
	 * Discriminator for the type of queue we should use.
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
	 */
    enum QueueType{
    	UNBOUNDED, DIRECT;

		public static QueueType getDefault() {
			return UNBOUNDED;
		}
    }
    
	final static Boolean IGNORE_FOOTPRINT = Boolean.getBoolean("org.geotools.footprint.ignore");
	
	final static String THREADPOOL_CONFIG_FILE = "mosaicthreadpoolconfig.properties";

	static final int DEFAULT_CORE_POOLSIZE = 5;	
            
	/**{@link AffineTransform} that can be used to go from an image datum placed at the center of pixels to one that is placed at ULC.*/
	final static AffineTransform CENTER_TO_CORNER= AffineTransform.getTranslateInstance(
			PixelTranslation.getPixelTranslation(PixelInCell.CELL_CORNER),
			PixelTranslation.getPixelTranslation(PixelInCell.CELL_CORNER));
	/**{@link AffineTransform} that can be used to go from an image datum placed at the ULC corner of pixels to one that is placed at center.*/
	final static AffineTransform CORNER_TO_CENTER= AffineTransform.getTranslateInstance(
			-PixelTranslation.getPixelTranslation(PixelInCell.CELL_CORNER),
			-PixelTranslation.getPixelTranslation(PixelInCell.CELL_CORNER));
	/**
	 * Very simple bean to hold the configuration of the mosaic.
	 * 
	 * @author Simone Giannecchini, GeoSolutions S.A.S.
	 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
	 */
	static final class MosaicConfigurationBean {

		/**
		 * <code>true</code> it tells us if the mosaic points to absolute paths or to relative ones. (in case of <code>false</code>).
		 */
		private boolean absolutePath;
		
		/**
		 * <code>true</code> if we need to expand to RGB(A) the single tiles in case they use a different {@link IndexColorModel}.
		 */
		private boolean expandToRGB;
		
		/**
		 * <code>true</code> if we need to manage footprint if available.
		 */
		private boolean footprintManagement;
		
		/** The envelope for the whole mosaic.**/
		private Envelope2D envelope2D;
		
		/** OverviewLevel levels */
		private double[][] levels;
		
		/** name for the mosaic.*/
		private String name;
		
		/** number of levels*/
		private int levelsNum;
		
		/** location attribute name*/
		private String locationAttribute;
		
		/**Suggested SPI for the various tiles. May be null.**/
		private String suggestedSPI;
		

		/**
		 * @return the suggestedSPI
		 */
		public String getSuggestedSPI() {
			return suggestedSPI;
		}
		/**
		 * @param suggestedSPI the suggestedSPI to set
		 */
		public void setSuggestedSPI(String suggestedSPI) {
			this.suggestedSPI = suggestedSPI;
		}
		
		public boolean isAbsolutePath() {
			return absolutePath;
		}
		public void setAbsolutePath(boolean absolutePath) {
			this.absolutePath = absolutePath;
		}
		public boolean isExpandToRGB() {
			return expandToRGB;
		}
		public void setExpandToRGB(boolean expandToRGB) {
			this.expandToRGB = expandToRGB;
		}
		public boolean isFootprintManagement() {
			return footprintManagement;
		}
		public void setFootprintManagement(boolean footprintManagement) {
			this.footprintManagement = footprintManagement;
		}
		public Envelope2D getEnvelope2D() {
			return envelope2D;
		}
		public void setEnvelope2D(Envelope2D envelope2D) {
			this.envelope2D = envelope2D;
		}

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getLevelsNum() {
			return levelsNum;
		}
		public void setLevelsNum(int levelsNum) {
			this.levelsNum = levelsNum;
		}
		public double[][] getLevels() {
			return levels.clone();
		}
		public void setLevels(double[][] levels) {
			this.levels = levels.clone();
		}
		public String getLocationAttribute() {
			return locationAttribute;
		}
		public void setLocationAttribute(String locationAttribute) {
			this.locationAttribute = locationAttribute;
		}
		
		


	}
	
	/**
	 * Logger. 
	 */
	private final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger(ImageMosaicUtils.class.toString());
	/**
	 * Default wildcard for creating mosaics.
	 */
	static final String DEFAULT_WILCARD = "*.*";
	
	static final boolean DEFAULT_FOOTPRINT_MANAGEMENT = true;
	
	/**
	 * Default path behavior with respect to absolute paths.
	 */
	static final boolean DEFAULT_PATH_BEHAVIOR = false;

	
	
	/**
	 * Cached instance of {@link URLImageInputStreamSpi} for creating {@link ImageInputStream} instances.
	 */
	private static ImageInputStreamSpi cachedStreamSPI=new URLImageInputStreamSpi();
	
	/**
	 * Creates a mosaic for the provided input parameters.
	 * 
	 * @param location  path to the directory where to gather the elements for the mosaic.
	 * @param indexName name to give to this mosaic
	 * @param wildcard wildcard to use for walking through files. We are using commonsIO for this task
	 * @return <code>true</code> if everything is right, <code>false</code>if something bad happens, in which case the reason should be logged to the logger.
	 */
	static boolean createMosaic(
			final String location,
			final String indexName,
			final String wildcard,
			final boolean absolutePath) {
		
		
		//create a mosaic index builder and set the relevant elements
		final IndexBuilderConfiguration configuration = new IndexBuilderConfiguration();
		configuration.setAbsolute(absolutePath);
		configuration.setRootMosaicDirectory(location);
		configuration.setIndexingDirectories(Arrays.asList(location));
		configuration.setIndexName(indexName);	
		final IndexBuilder indexBuilder= new IndexBuilder(configuration);
		//this is going to help us with  catching exceptions and logging them
		final Queue<Throwable> exceptions=new LinkedList<Throwable>();
		try{


			final IndexBuilder.ProcessingEventListener listener= new ProcessingEventListener(){
		
				@Override
				public void exceptionOccurred(ExceptionEvent event) {
					final Throwable t=event.getException();
					exceptions.add(t);
					if(LOGGER.isLoggable(Level.SEVERE))
						LOGGER.log(Level.SEVERE,t.getLocalizedMessage(),t);
					
				}
				
				@Override
				public void getNotification(ProcessingEvent event) {
					if(LOGGER.isLoggable(Level.FINE))
						LOGGER.fine(event.getMessage());
					
				}
	
				
			
				
		
				
			};
			indexBuilder.addProcessingEventListener(listener);
			indexBuilder.run();
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE,"Unable to build mosaic",e);
			return false;
		} finally {
			indexBuilder.dispose();
		}
		
		//check that nothing bad happened
		if(exceptions.size()>0)
			return false;
		return true;
	}

	


    
    static String getMessageFromException(Exception exception) {
        if(exception.getLocalizedMessage() != null)
            return exception.getLocalizedMessage();
        else
            return exception.getMessage();
    }

		static URL checkSource(Object source) throws MalformedURLException,
				DataSourceException {
			URL sourceURL=null;
			File sourceFile = null;
			// /////////////////////////////////////////////////////////////////////
			//
			// Check source
			//
			// /////////////////////////////////////////////////////////////////////
			//if it is a URL or a String let's try to see if we can get a file to check if we have to build the index
			if (source instanceof File)
			{
				sourceFile = (File)source; 				
				sourceURL = DataUtilities.fileToURL(sourceFile);
				sourceURL = checkURLForMosaicQuery((URL) sourceURL);
			}
			else if (source instanceof URL)
			{
				sourceURL = checkURLForMosaicQuery((URL) source);
				if (sourceURL.getProtocol().equals("file")) {
					sourceFile =DataUtilities.urlToFile(sourceURL);
				} 
			}
			else if (source instanceof String) { 
				//is it a File?
				final String tempSource=(String) source;
			    File tempFile = new File(tempSource);
			    if (!tempFile.exists()) 
			    {
			    	//is it a URL
			    	try{
			    		sourceURL= new URL(tempSource);
			    		sourceURL = checkURLForMosaicQuery(sourceURL);
			    		source =DataUtilities.urlToFile(sourceURL);	    		
			    	}catch (MalformedURLException e) {
			    		sourceURL=null;
			    		source=null;
					}
			    }
			    else
		        {
			    	sourceURL =  DataUtilities.fileToURL(tempFile); 

			    	// so that we can do our magic here below
			    	sourceFile=tempFile;
		        }
			}
			        
	
			// at this point we have tried to convert the thing to a File as hard as we could, let's see what we can do
			if (sourceFile != null)
			   {
				if(!sourceFile.isDirectory())
					sourceURL = DataUtilities.fileToURL((File) sourceFile);
				else
				{
					//it's a directory, let's look for a possible properties files that we want to load
					final String locationPath=sourceFile.getAbsolutePath();
					final String defaultWildcardString=DEFAULT_WILCARD;
					final String defaultIndexName=FilenameUtils.getName(locationPath);
					
					//now let's see f we have at least a properties file with its own shapefile
					final File[] properties = sourceFile.listFiles((FilenameFilter)FileFilterUtils.makeFileOnly(FileFilterUtils.suffixFileFilter(".properties")) );
					//now get the first one with a shapefile
					File granuleIndex=null;
					for(File propFile:properties){
						final File shpFile= new File(locationPath,FilenameUtils.getBaseName(propFile.getName())+".shp");
						if(shpFile.exists()&&shpFile.isFile()&&shpFile.canRead()&&propFile.canRead()&&propFile.isFile())
						{
							granuleIndex=shpFile;
							break;
						}
					}
					
					//did we find anything?
					if (granuleIndex == null){
						//try to build a mosaic inside this directory and see what happens    	
						createMosaic(locationPath, defaultIndexName,defaultWildcardString,DEFAULT_PATH_BEHAVIOR);   
						granuleIndex= new File(locationPath,defaultIndexName+".shp");
						File propertiesFile = new File(locationPath,defaultIndexName+".properties");
						if(!granuleIndex.exists()||!granuleIndex.canRead()||!propertiesFile.exists()||!propertiesFile.canRead())
							sourceURL=null;
						else
							// now set the new source and proceed
							sourceURL = granuleIndex.toURI().toURL(); //TODO Comment by Stefan Krueger: Shouldn't we use DataUtilities.fileToURL(file) 
						
						
					} else {
						if (!granuleIndex.exists()||!granuleIndex.canRead()){
							sourceURL = null;
						} else {
							final String shapeFileName = granuleIndex.getAbsolutePath();
							final String pathPrefix = FilenameUtils.getFullPathNoEndSeparator(shapeFileName) + File.separatorChar;
							final File footprintSummaryFile = new File(new StringBuilder(pathPrefix).append(FilenameUtils.getBaseName(shapeFileName)).append(FootprintUtils.FOOTPRINT_EXT).toString());
							final File footprintShapeFile = new File(new StringBuilder(pathPrefix).append(FootprintUtils.FOOTPRINT).toString());
							if (footprintShapeFile != null && footprintShapeFile.exists() && footprintShapeFile.canRead()){
								if (footprintSummaryFile != null && !footprintSummaryFile.exists()) {
									MosaicConfigurationBean props = loadPropertiesFile(DataUtilities.fileToURL(granuleIndex), null, ImageMosaicUtils.DEFAULT_LOCATION_ATTRIBUTE, FootprintUtils.IGNORE_PROPS);
									if (props.footprintManagement) {
										ShapefileDataStore footprintStore = null; 
										try {
											//associate locations to footprint to then write down granule's feature id + footprint
								            final Map <String,Geometry> footprintsLocationGeometryMap = new HashMap<String, Geometry>();
								            footprintStore = new ShapefileDataStore(DataUtilities.fileToURL(footprintShapeFile));
								            FootprintUtils.initFootprintsLocationGeometryMap(footprintStore, footprintsLocationGeometryMap);
								            FootprintUtils.writeFootprintSummary(footprintSummaryFile, granuleIndex, footprintsLocationGeometryMap);
								            footprintsLocationGeometryMap.clear();
										} catch ( Throwable t){
										    if (LOGGER.isLoggable(Level.FINE))
										    	LOGGER.log(Level.FINE, t.getLocalizedMessage(), t);
										} finally {
											
											try {
												if(footprintStore != null)
													footprintStore.dispose();
											} catch (Throwable e) {
												LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
											}
										
											footprintStore = null;
										}
										
									}
								} 
							}
							final File sampleImageFile = new File(new StringBuilder(pathPrefix).append("sample_image").toString());
						            if (!sampleImageFile.exists() || !sampleImageFile.isFile() || !sampleImageFile.canRead()) {
						                createSampleImage(pathPrefix);
						            }
							// now set the new source and proceed
							sourceURL= granuleIndex.toURI().toURL(); // TODO Comment by Stefan Krueger: Shouldn't we use DataUtilities.fileToURL(file)
						}
					}
				}
			   }
				else {
					// SK: We don't set SourceURL to null now, just because it doesn't point to a file
					// sourceURL=null;
				}
			return sourceURL;
		}
		
    /**
     * Load a sample image from which we can take the sample model and color
     * model to be used to fill holes in responses.
     * 
     * @param sampleImageFile
     *            the path to sample image.
     * @return a sample image from which we can take the sample model and color
     *         model to be used to fill holes in responses.
     */
    static RenderedImage loadSampleImage(final File sampleImageFile) {
        // serialize it
        InputStream inStream = null;
        ObjectInputStream oiStream = null;
        try {

            // do we have the sample image??
            if (DataUtilities.checkFileReadable(sampleImageFile, LOGGER)) {
                inStream = new BufferedInputStream(new FileInputStream(sampleImageFile));
                oiStream = new ObjectInputStream(inStream);

                // load the image
                return (RenderedImage) oiStream.readObject();

            } else {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.warning("Unable to find sample image for path " + sampleImageFile);
                return null;
            }
        } catch (FileNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        } catch (ClassNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        } finally {
            try {
                if (inStream != null)
                    inStream.close();
            } catch (Throwable e) {

                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
            try {
                if (oiStream != null)
                    oiStream.close();
            } catch (Throwable e) {

                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
        }
    }
		
    @SuppressWarnings("unchecked")
	private static synchronized void createSampleImage(final String pathPrefix) {
        final File sampleImageFile = new File(new StringBuilder(pathPrefix).append("sample_image").toString());
        if (!sampleImageFile.exists() || !sampleImageFile.isFile() || !sampleImageFile.canRead()) {
            final IOFileFilter finalFilter = IndexBuilder.createIndexingFilter(new WildcardFileFilter("*.*", IOCase.INSENSITIVE));
            final File directoryToScan = new File(pathPrefix);
            final Collection<File> files = FileUtils.listFiles(directoryToScan, finalFilter, TrueFileFilter.INSTANCE);
            for (File file : files) {
                ImageTypeSpecifier its = getImageTypeSpecifier(file);
                if (its != null) {
                    ColorModel defaultCM = its.getColorModel();
                    SampleModel defaultSM = its.getSampleModel();

                    try {
                        storeSampleImage(sampleImageFile, defaultSM, defaultCM);
                        break;
                    } catch (IOException e) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                        }
                    }
                }
            }
        }
    }

	
    static ImageTypeSpecifier getImageTypeSpecifier(final File fileBeingProcessed) {

        // Check that this file is actually good 
        //
        if(!fileBeingProcessed.exists() || !fileBeingProcessed.canRead() || !fileBeingProcessed.isFile())
            return null;

        // replacing chars on input path
        String validFileName;
        try {
            validFileName = fileBeingProcessed.getCanonicalPath();
            validFileName = FilenameUtils.normalize(validFileName);
        } catch (IOException e1) {
            return null;
        }
        validFileName = FilenameUtils.getName(validFileName);
        ImageInputStream inStream = null;
        ImageReader imageioReader = null;
        try {
            //
            // Getting an ImageIO reader for this file.
            //
            inStream = ImageIO.createImageInputStream(fileBeingProcessed);
            if (inStream == null) {
                return null;
            }
            inStream.mark();
            final Iterator<ImageReader> it = ImageIO.getImageReaders(inStream);
            if (it.hasNext()) {
                imageioReader = it.next();
                if (imageioReader != null) {
                    imageioReader.setInput(inStream);
                }
            } else {
                imageioReader = null;
            }
            // did we found a reader
            if (imageioReader == null) {
                return null;
            }

            //
            // Get the type specifier for this image 
            //
            final ImageTypeSpecifier its = ((ImageTypeSpecifier) imageioReader.getImageTypes(0).next());
            return its;

        } catch (IOException e) {
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        } finally {
            //
            // release resources
            //
            try {
                if (inStream != null)
                    inStream.close();
            } catch (Throwable e) {
                // ignore exception
                if (LOGGER.isLoggable(Level.FINEST))
                    LOGGER.log(Level.FINEST, e.getLocalizedMessage(), e);
            }
            try {
                if (imageioReader != null)
                    imageioReader.dispose();
            } catch (Throwable e) {
                // ignore exception
                if (LOGGER.isLoggable(Level.FINEST))
                    LOGGER.log(Level.FINEST, e.getLocalizedMessage(), e);
            }
        }

    }


    /**
	 * Checks the provided {@link URL} in order to see if it is a a query to build a mosaic or not.
	 * 
	 * @param sourceURL
	 * @return a modified version of the provided {@link URL} which points to a shapefile in case we created a mosaic, or to the original {@link URL}otherwise.
	 */
	static URL checkURLForMosaicQuery(final URL sourceURL ) {
        	////
        	//
        	// Query with parameters, it might be that the user is
			// trying to build the mosaic specifying the params as
			// well
        	//
        	////
            if (sourceURL.getProtocol().equalsIgnoreCase("file")) {
            	final String query=sourceURL.getQuery();
            	if(query!=null)
            	{
            		final String[] tokens = query.split("\\&");
            		final String locationPath=sourceURL.getPath();// remove 'file:' prefix
            		String indexName=null;
            		final File sourceDir= new File(locationPath);
            		if(!(sourceDir.isDirectory()&&sourceDir.exists()&&sourceDir.canRead()))
            			return null;
            		String wildcardString=null;
            		boolean absolutePath=DEFAULT_PATH_BEHAVIOR;
            		for(String token:tokens)
            		{
            			//splitting token
            			final String[] values=token.split("\\=");
            			if(values[0].equalsIgnoreCase("name"))
            				indexName=values[1];
            			else
            				if(values[0].equalsIgnoreCase("w")||values[0].equalsIgnoreCase("wildcard"))
            					wildcardString=values[1];
            				else
                				if(values[0].equalsIgnoreCase("p")||values[0].equalsIgnoreCase("path"))
                					absolutePath=Boolean.parseBoolean(values[1]);

            			
            		}
            		
            		//now check if the shapefle is already there
            		final File shapeFile= new File(locationPath,indexName+".shp");
          			File propertiesFile = new File(locationPath,indexName+".properties");
        			if(!shapeFile.exists()||!shapeFile.canRead()||!shapeFile.isFile()||!propertiesFile.exists()||!propertiesFile.canRead()||!propertiesFile.isFile())
    				{
        				//try to build it
        				createMosaic(locationPath, indexName!=null?indexName:FilenameUtils.getBaseName(locationPath),wildcardString!=null?wildcardString:DEFAULT_WILCARD,absolutePath);
    					
    				}  
        			
        			//check URL again
        			if(!shapeFile.exists()||!shapeFile.canRead()||!shapeFile.isFile()||!propertiesFile.exists()||!propertiesFile.canRead()||!propertiesFile.isFile())
        				return null;
					else
						try {
							return shapeFile.toURI().toURL();
						} catch (MalformedURLException e) {
							if(LOGGER.isLoggable(Level.FINE))
								LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
						}            		
    				
    				
						
            		
            	}
            		
            }
            
            return  sourceURL;
        }

	static MosaicConfigurationBean loadPropertiesFile(final URL sourceURL, final CoordinateReferenceSystem crs, final String defaultLocationAttribute){
		return loadPropertiesFile(sourceURL, crs, defaultLocationAttribute, null);
	}
	
	static Properties loadPropertiesFromURL(URL propsURL) {
		final Properties properties = new Properties();
		InputStream stream = null;
		InputStream openStream = null;
		try {
			openStream = propsURL.openStream();
			stream = new BufferedInputStream(openStream);
			properties.load(stream);
		} catch (FileNotFoundException e) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return null;
		} catch (IOException e) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return null;
		} finally {

			if (stream != null)
				IOUtils.closeQuietly(stream);

			if (openStream != null)
				IOUtils.closeQuietly(openStream);

		}
		return properties;
	}
	static MosaicConfigurationBean loadPropertiesFile(final URL sourceURL, final CoordinateReferenceSystem crs, final String defaultLocationAttribute, final Set<String> ignorePropertiesSet){
			//ret value
		    final MosaicConfigurationBean retValue= new MosaicConfigurationBean();
		    final boolean ignoreSome = ignorePropertiesSet != null && !ignorePropertiesSet.isEmpty();
		    
		    //
			// load the properties file
			//
			URL propsURL = DataUtilities.changeUrlExt(sourceURL, "properties");
			final Properties properties = loadPropertiesFromURL(propsURL);
			
			String pairs[] = null;
			String pair[] = null;
			//
			// load the envelope
			//
			if((!ignoreSome && !properties.containsKey(Prop.ENVELOPE2D))
					||ignoreSome && !ignorePropertiesSet.contains(Prop.ENVELOPE2D) && !properties.containsKey(Prop.ENVELOPE2D)) {
				if(LOGGER.isLoggable(Level.SEVERE))
					LOGGER.severe("Required key Envelope2D not found.");		
				return  null;
			}
			
			if (!ignoreSome || !ignorePropertiesSet.contains(Prop.ENVELOPE2D)){
				final String envelope = properties.getProperty(Prop.ENVELOPE2D).trim();
				pairs = envelope.split(" ");
				final double cornersV[][] = new double[2][2];
				for (int i = 0; i < 2; i++) {
					pair = pairs[i].split(",");
					cornersV[i][0] = Double.parseDouble(pair[0]);
					cornersV[i][1] = Double.parseDouble(pair[1]);
				}
				final GeneralEnvelope originalEnvelope = new GeneralEnvelope(cornersV[0], cornersV[1]);
				originalEnvelope.setCoordinateReferenceSystem(crs);
				retValue.setEnvelope2D(new Envelope2D(originalEnvelope));
			}
		
			//
			// resolutions levels
			//		
			if (!ignoreSome || !ignorePropertiesSet.contains(Prop.LEVELS)){
				int levelsNumber = Integer.parseInt(properties.getProperty(Prop.LEVELS_NUM,"1").trim()) ;
				retValue.setLevelsNum(levelsNumber);
				if(!properties.containsKey(Prop.LEVELS)) {
					if(LOGGER.isLoggable(Level.SEVERE))
						LOGGER.severe("Required key Levels not found.");		
					return  null;
				}			
				final String levels = properties.getProperty(Prop.LEVELS).trim();
				pairs = levels.split(" ");
				if (pairs == null || pairs.length != levelsNumber) {
					LOGGER.severe("Levels number is different from the provided number of levels resoltion.");
					return null;
				}
				final double[][] resolutions = new double[levelsNumber][2];
		   		for (int i = 0; i < levelsNumber; i++) {
		   			pair = pairs[i].split(",");
					if (pair == null || pair.length != 2) {
						if (LOGGER.isLoggable(Level.SEVERE))
							LOGGER.severe("OverviewLevel number is different from the provided number of levels resoltion.");
						return null;
					}       			
		   			resolutions[i][0] = Double.parseDouble(pair[0]);
		   			resolutions[i][1] = Double.parseDouble(pair[1]);
		   		}
		   		retValue.setLevels(resolutions);
			}

       		//
			// suggested spi is optional
			//
			if (!ignoreSome || !ignorePropertiesSet.contains(Prop.SUGGESTED_SPI)) {
				if (properties.containsKey(Prop.SUGGESTED_SPI)) {
					String suggestedSPI = properties.getProperty(Prop.SUGGESTED_SPI).trim();
					retValue.setSuggestedSPI(suggestedSPI);
				}
			}

			//
			// name is not optional
			//
			if (!ignoreSome || !ignorePropertiesSet.contains(Prop.NAME)){
				if(!properties.containsKey(Prop.NAME)) {
					if(LOGGER.isLoggable(Level.SEVERE))
						LOGGER.severe("Required key Name not found.");		
					return  null;
				}			
				String coverageName = properties.getProperty(Prop.NAME).trim();
				retValue.setName(coverageName);
			}

			// need a color expansion?
			// this is a newly added property we have to be ready to the case where
			// we do not find it.
			if (!ignoreSome || !ignorePropertiesSet.contains(Prop.EXP_RGB)) {
				final boolean expandMe = Boolean.valueOf(properties.getProperty(Prop.EXP_RGB,"false").trim());	
				retValue.setExpandToRGB(expandMe);
			}
			
			//
			// Absolute or relative path
			//
			if (!ignoreSome || !ignorePropertiesSet.contains(Prop.ABSOLUTE_PATH)) {
				boolean absolutePath = Boolean.parseBoolean(properties.getProperty(Prop.ABSOLUTE_PATH, Boolean.toString(ImageMosaicUtils.DEFAULT_PATH_BEHAVIOR)).trim());
				retValue.setAbsolutePath(absolutePath);
			}
			
			//
			// Footprint management
			//
			if (!ignoreSome || !ignorePropertiesSet.contains(Prop.FOOTPRINT_MANAGEMENT)) {
				final boolean footprintManagement=Boolean.valueOf(properties.getProperty(Prop.FOOTPRINT_MANAGEMENT, "false").trim());	
				retValue.setFootprintManagement(footprintManagement);
			}
			
		
		    //
		    //  location
		    //	
			if (!ignoreSome || !ignorePropertiesSet.contains(Prop.LOCATION_ATTRIBUTE)) {
				retValue.setLocationAttribute(properties.getProperty(Prop.LOCATION_ATTRIBUTE, ImageMosaicUtils.DEFAULT_LOCATION_ATTRIBUTE).trim());
			}
			//retrn value
			return retValue;			
		}

	/**
	 * Returns a suitable threshold depending on the {@link DataBuffer} type.
	 * 
	 * <p>
	 * Remember that the threshold works with >=.
	 * 
	 * @param dataType
	 *            to create a low threshold for.
	 * @return a minimum threshold value suitable for this data type.
	 */
	 static double getThreshold(int dataType) {
		switch (dataType) {
		case DataBuffer.TYPE_BYTE:
		case DataBuffer.TYPE_USHORT:
			// this may cause problems and truncations when the native mosaic operations is enabled
			return 0.0;
		case DataBuffer.TYPE_INT:
			return Integer.MIN_VALUE;
		case DataBuffer.TYPE_SHORT:
			return Short.MIN_VALUE;
		case DataBuffer.TYPE_DOUBLE:
			return -Double.MAX_VALUE;
		case DataBuffer.TYPE_FLOAT:
			return -Float.MAX_VALUE;
		}
		return 0;
	}

	/**
	 * Builds a {@link ReferencedEnvelope} from a {@link GeographicBoundingBox}.
	 * This is useful in order to have an implementation of {@link BoundingBox}
	 * from a {@link GeographicBoundingBox} which strangely does implement
	 * {@link GeographicBoundingBox}.
	 * 
	 * @param geographicBBox
	 *                the {@link GeographicBoundingBox} to convert.
	 * @return an instance of {@link ReferencedEnvelope}.
	 */
	static ReferencedEnvelope getReferencedEnvelopeFromGeographicBoundingBox(final GeographicBoundingBox geographicBBox) {
	    Utilities.ensureNonNull("GeographicBoundingBox", geographicBBox);
	    return new ReferencedEnvelope(geographicBBox.getEastBoundLongitude(),
	            geographicBBox.getWestBoundLongitude(), geographicBBox
	                    .getSouthBoundLatitude(), geographicBBox
	                    .getNorthBoundLatitude(), DefaultGeographicCRS.WGS84);
	}

	/**
	 * @param transparentColor
	 * @param image
	 * @return
	 * @throws IllegalStateException
	 */
	static RenderedImage makeColorTransparent(
			final Color transparentColor, final RenderedImage image)
			throws IllegalStateException {
		final ImageWorker w = new ImageWorker(image);
		if (image.getSampleModel() instanceof MultiPixelPackedSampleModel)
			w.forceComponentColorModel();
		return w.makeColorTransparent(transparentColor).getRenderedImage();
	}

	static ImageReadParam cloneImageReadParam(
			ImageReadParam param) {
	
	
	    // The ImageReadParam passed in is non-null. As the
	    // ImageReadParam class is not Cloneable, if the param
	    // class is simply ImageReadParam, then create a new
	    // ImageReadParam instance and set all its fields
	    // which were set in param. This will eliminate problems
	    // with concurrent modification of param for the cases
	    // in which there is not a special ImageReadparam used.
	
	    // Create a new ImageReadParam instance.
	    ImageReadParam newParam = new ImageReadParam();
	
	    // Set all fields which need to be set.
	
	    // IIOParamController field.
	    if(param.hasController()) {
	        newParam.setController(param.getController());
	    }
	
	    // Destination fields.
	    newParam.setDestination(param.getDestination());
	    if(param.getDestinationType() != null) {
	        // Set the destination type only if non-null as the
	        // setDestinationType() clears the destination field.
	        newParam.setDestinationType(param.getDestinationType());
	    }
	    newParam.setDestinationBands(param.getDestinationBands());
	    newParam.setDestinationOffset(param.getDestinationOffset());
	
	    // Source fields.
	    newParam.setSourceBands(param.getSourceBands());
	    newParam.setSourceRegion(param.getSourceRegion());
	    if(param.getSourceMaxProgressivePass() != Integer.MAX_VALUE) {
	        newParam.setSourceProgressivePasses(
	            param.getSourceMinProgressivePass(),
	            param.getSourceNumProgressivePasses());
	    }
	    if(param.canSetSourceRenderSize()) {
	        newParam.setSourceRenderSize(param.getSourceRenderSize());
	    }
	    newParam.setSourceSubsampling(param.getSourceXSubsampling(),
	                                  param.getSourceYSubsampling(),
	                                  param.getSubsamplingXOffset(),
	                                  param.getSubsamplingYOffset());
	
	    // Replace the local variable with the new ImageReadParam.
	    return newParam;
	
	}

	/**
	 * Look for an {@link ImageReader} instance that is able to read the provided {@link ImageInputStream}, which must be non null.
	 * 
	 * <p>
	 * In case no reader is found, <code>null</code> is returned.
	 * 
	 * @param inStream an instance of {@link ImageInputStream} for which we need to find a suitable {@link ImageReader}. 
	 * @return a suitable instance of {@link ImageReader} or <code>null</code> if one cannot be found.
	 */
	static ImageReader getReader(
			final ImageInputStream inStream) {
		Utilities.ensureNonNull("inStream", inStream);
		// get a reader
		inStream.mark();
		final Iterator<ImageReader> readersIt = ImageIO.getImageReaders(inStream);
		if(!readersIt.hasNext())
		{
			return null;
		}
		return readersIt.next();
	}





	/**
	 * Retrieves the dimensions of the {@link RenderedImage} at index <code>imageIndex</code> for the provided 
	 * {@link ImageReader} and {@link ImageInputStream}.
	 * 
	 * <p>
	 * Notice that none of the input parameters can be <code>null</code> or a {@link NullPointerException} will be thrown.
	 * Morevoer the <code>imageIndex</code> cannot be negative or an {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param imageIndex the index of the image to get the dimensions for.
	 * @param inStream the {@link ImageInputStream} to use as an input 
	 * @param reader the {@link ImageReader} to decode the image dimensions.
	 * @return a {@link Rectangle} that contains the dimensions for the image at index <code>imageIndex</code>
	 * @throws IOException in case the {@link ImageReader} or the {@link ImageInputStream} fail.
	 */
	static Rectangle  getDimension(
			final int imageIndex,
			final ImageInputStream inStream, 
			final ImageReader reader) throws IOException {
	        Utilities.ensureNonNull("inStream", inStream);
	        Utilities.ensureNonNull("reader", reader);
		if(imageIndex<0)
			throw new IllegalArgumentException(Errors.format(ErrorKeys.INDEX_OUT_OF_BOUNDS_$1,imageIndex));
		inStream.reset();
		reader.setInput(inStream);
		return new Rectangle(0,0,reader.getWidth(imageIndex),reader.getHeight(imageIndex));
	}





	/**
	 * Retrieves an {@link ImageInputStream} for the provided input {@link File}.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static ImageInputStream getInputStream(final File file) throws IOException {
		final ImageInputStream inStream= ImageIO.createImageInputStream(file);
		if (inStream == null) {
			return null;
		}
		return inStream;
	}
	
	/**
	 * Retrieves an {@link ImageInputStream} for the provided input {@link URL}.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	static ImageInputStream getInputStream(final URL url)
	throws IOException {
		final ImageInputStream inStream= cachedStreamSPI.createInputStreamInstance(url,ImageIO.getUseCache(),ImageIO.getCacheDirectory());
		if(inStream==null)
			return null;
		return inStream;
	}


	/**
	 * Checks that the provided <code>dimensions</code> when intersected with the source region 
	 * used by the provided {@link ImageReadParam} instance does not result in an empty {@link Rectangle}.
	 * 
	 * <p>
	 * Input parameters cannot be null.
	 * 
	 * @param readParameters an instance of {@link ImageReadParam} for which we want to check the source region element.
	 * @param dimensions an instance of {@link Rectangle} to use for the check.
	 * @return <code>true</code> if the intersection is not empty, <code>false</code> otherwise.
	 */
	static boolean checkEmptySourceRegion(
			final ImageReadParam readParameters,
			final Rectangle dimensions) {
		Utilities.ensureNonNull("readDimension", dimensions);
		Utilities.ensureNonNull("readP", readParameters);
		final Rectangle sourceRegion=readParameters.getSourceRegion();
		Rectangle.intersect(sourceRegion, dimensions, sourceRegion);	
		if(sourceRegion.isEmpty())
			return true;
		readParameters.setSourceRegion(sourceRegion);
		return false;
	}





	/**
	 * Default priority for the underlying {@link Thread}.
	 */
	public static final int DEFAULT_PRIORITY = Thread.NORM_PRIORITY;
	/**
	 * Default location attribute name.
	 */
	public static final String DEFAULT_LOCATION_ATTRIBUTE = "location";
	
	public static final String DEFAULT_INDEX_NAME = "index";

	static final int DEFAULT_MAX_POOLSIZE = 15;

	public static final int DEFAULT_KEEP_ALIVE = 30;

	static final QueueType DEFAULT_QUEUE_TYPE = QueueType.getDefault();

	static boolean checkURLReadable(URL url) {
		try {
			url.openStream().close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

    /**
     * Store a sample image from which we can derive the default SM and CM
     * 
     * @param sampleImageFile
     *            where we should store the image
     * @param defaultSM
     *            the {@link SampleModel} for the sample image.
     * @param defaultCM
     *            the {@link ColorModel} for the sample image.
     * @throws IOException
     *             in case something bad occurs during writing.
     */
    static void storeSampleImage(
            final File sampleImageFile,
            final SampleModel defaultSM, 
            final ColorModel defaultCM)
            throws IOException {
        // create 1X1 image
        final SampleModel sm = defaultSM.createCompatibleSampleModel(1, 1);
        final WritableRaster raster = RasterFactory.createWritableRaster(sm, null);
        final BufferedImage sampleImage = new BufferedImage(defaultCM, raster, false, null);

        // serialize it
        OutputStream outStream = null;
        ObjectOutputStream ooStream = null;
        try {
            outStream = new BufferedOutputStream(new FileOutputStream(sampleImageFile));
            ooStream = new ObjectOutputStream(outStream);
            ooStream.writeObject(new SerializableRenderedImage(sampleImage));
        } finally {
            try {
                if (ooStream != null)
                    ooStream.close();
            } catch (Throwable e) {
                IOUtils.closeQuietly(ooStream);
            }
            try {
                if (outStream != null)
                    outStream.close();
            } catch (Throwable e) {
                IOUtils.closeQuietly(outStream);
            }
        }
    }

    /** 
     * Build a background values array using the same dataType of the input {@link SampleModel} (if available). 
     * 
     * @param sampleModel
     * @param backgroundValues
     * @return
     */
    static Number[] getBackgroundValues(final SampleModel sampleModel, final double[] backgroundValues) {
        Number[] values = null;
        final int dataType = sampleModel != null ? sampleModel.getDataType() : DataBuffer.TYPE_DOUBLE;
        final int numBands=sampleModel.getNumBands();
        switch (dataType){
            case DataBuffer.TYPE_BYTE:
            	values = new Byte[numBands];
            	 if (backgroundValues == null){            		 
            		 Arrays.fill(values, Byte.valueOf((byte)0));
            	 }
            	 else{
            		//we have background values available
                     for (int i = 0; i < values.length; i++)
                         values[i] = i>=backgroundValues.length?Byte.valueOf((byte)backgroundValues[0]):Byte.valueOf((byte)backgroundValues[i]);
                    
            	 }
            	 break;
            case DataBuffer.TYPE_SHORT:
            case DataBuffer.TYPE_USHORT:
       		 values = new Short[numBands] ;
           	 if (backgroundValues == null)
           		 Arrays.fill(values, Short.valueOf((short)0));
        	 else{
        		//we have background values available
                 for (int i = 0; i < values.length; i++)
                     values[i] = i>=backgroundValues.length?Short.valueOf((short)backgroundValues[0]):Short.valueOf((short)backgroundValues[i]);
                 
        	 }         
           	 break;
            case DataBuffer.TYPE_INT:
            	values = new Integer[numBands] ;
            	 if (backgroundValues == null)
            		 Arrays.fill(values, Integer.valueOf((int)0));
		       	 else{
		       		//we have background values available
	                for (int i = 0; i < values.length; i++)
	                	values[i] = i>=backgroundValues.length?Integer.valueOf((int)backgroundValues[0]):Integer.valueOf((int)backgroundValues[i]);

	        	 }         
	           	 break;
            case DataBuffer.TYPE_FLOAT:
            	values = new Float[numBands] ;
              	 if (backgroundValues == null)
              		Arrays.fill(values, Float.valueOf(0.f));
            	 else{
            		//we have background values available
                     for (int i = 0; i < values.length; i++)
                    	 values[i] = i>=backgroundValues.length?Float.valueOf((float)backgroundValues[0]):Float.valueOf((float)backgroundValues[i]);

            	 }         
               	 break;   
            case DataBuffer.TYPE_DOUBLE:
            	values = new Double[numBands] ;
             	 if (backgroundValues == null)
             		Arrays.fill(values, Double.valueOf(0.d));
            	 else{
            		//we have background values available
                     for (int i = 0; i < values.length; i++)
                    	 values[i] = i>=backgroundValues.length?Double.valueOf((Double)backgroundValues[0]):Double.valueOf((Double)backgroundValues[i]);

            	 }         
               	 break;   
            }
        return values;
    }

}
