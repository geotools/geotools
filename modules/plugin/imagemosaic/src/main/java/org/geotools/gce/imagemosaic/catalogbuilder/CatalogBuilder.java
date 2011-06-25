/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalogbuilder;

import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.SampleModel;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.swing.SwingUtilities;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.geotools.console.CommandLine;
import org.geotools.console.Option;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.image.WorldImageFormat;
import org.geotools.gce.imagemosaic.MosaicConfigurationBean;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogFactory;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorFinder;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.util.Utilities;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;

import com.sun.media.imageioimpl.common.BogusColorSpace;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * This class is in responsible for creating the index for a mosaic of images
 * that we want to tie together as a single coverage.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/imagemosaic/src/main/java/org/geotools/gce/imagemosaic/catalogbuilder/CatalogBuilder.java $
 */
public class CatalogBuilder implements Runnable {

    final private static double RESOLUTION_TOLERANCE_FACTOR = 1E-2;

	/** Default Logger * */
	final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CatalogBuilder.class);
	
	static class CommandLineCatalogBuilderRunner extends CommandLine {

		@Option(description="This index must use absolute or relative path",mandatory=false,name="absolute")
		private Boolean absolute;
		
		@Option(description="This index can use caching or not",mandatory=false,name="caching")
		private Boolean caching = Utils.DEFAULT_CONFIGURATION_CACHING;
		
		@Option(description="Directories where to look for file to index",mandatory=true,name="indexingDirectories")
		private String indexingDirectoriesString;
		
		@Option(description="This index must handle footprint",mandatory=false,name="footprintManagement")
                private Boolean footprintManagement;
		
		/**
		 * Index file name. Default is index.
		 */
		@Option(description="Name to use for the index of this mosaic",mandatory=false,name="index")
		private String indexName;
		
		@Option(description="Root directory where to place the index file",mandatory=true,name="rootDirectory")
		private String rootMosaicDirectory;
		
		@Option(description="Wildcard to use for building the index of this mosaic",mandatory=false,name="wildcard")
		private String wildcardString = Utils.DEFAULT_WILCARD;

		@Option(description="Default location attribute for this index",mandatory=false,name="locationAttribute")
		private String locationAttribute = Utils.DEFAULT_LOCATION_ATTRIBUTE;

		public CommandLineCatalogBuilderRunner(String[] args) {
			super(args);
			if (this.absolute == null){
				this.absolute = Utils.DEFAULT_PATH_BEHAVIOR;
			}
			if (this.caching == null){
				this.caching = Utils.DEFAULT_CONFIGURATION_CACHING;
			}
			if (this.footprintManagement == null){
                                this.footprintManagement = Utils.DEFAULT_FOOTPRINT_MANAGEMENT;
			}
			if(this.indexName==null)
				this.indexName=Utils.DEFAULT_INDEX_NAME;
		}


		public static void main(String args[]){
			final CommandLineCatalogBuilderRunner runner = new CommandLineCatalogBuilderRunner(args);
			// prepare the configuration
			final CatalogBuilderConfiguration configuration= new CatalogBuilderConfiguration();
			configuration.setAbsolute(runner.absolute);
			configuration.setIndexName(runner.indexName);
			configuration.setFootprintManagement(runner.footprintManagement);
			configuration.setCaching(runner.caching);
			configuration.setRootMosaicDirectory(runner.rootMosaicDirectory);
			configuration.setWildcard(runner.wildcardString);
			configuration.setLocationAttribute(runner.locationAttribute);
			
			final String directories= runner.indexingDirectoriesString;
			final String []dirs_=directories.split(",");
			final List<String> dirs= new ArrayList<String>();
			for(String dir:dirs_)
				dirs.add(dir);
			configuration.setIndexingDirectories(dirs);
			
			//prepare and run the index builder
			final CatalogBuilder builder= new CatalogBuilder(configuration);		
			builder.run();
	  		
		}

	}
	
	static abstract public class ProcessingEventListener implements EventListener {

		abstract public void getNotification(final ProcessingEvent event);

		abstract public void exceptionOccurred(final ExceptionEvent event);

	}

	/**
	 * @author Simone Giannecchini, GeoSolutions.
	 * 
	 */
	static public class ProcessingEvent extends EventObject {

		private static final long serialVersionUID = 6930580659705360225L;

		private String message = null;

		private double percentage = 0;

		/**
		 * @param source
		 */
		public ProcessingEvent(final Object source, final String message,
				final double percentage) {
			super(source);
			this.message = message;
			this.percentage = percentage;
		}

		public double getPercentage() {
			return percentage;
		}
	    
	    public String getMessage() {
	        return message;
	    }

	}



	/**
	 * Event launched when an exception occurs. Percentage and message may be missing, in this case
	 * they will be -1 and the exception message (localized if available, standard otherwise)
	 * 
	 * @author aaime, TOPP.
	 * 
	 */
	static public final  class ExceptionEvent extends ProcessingEvent {

		private static final long serialVersionUID = 2272452028229922551L;

		private Exception exception;

	    public ExceptionEvent(Object source, String message, double percentage, Exception exception) {
	        super(source, message, percentage);
	        this.exception = exception;
	    }

	    public ExceptionEvent(Object source, Exception exception) {
	        super(source, Utils.getMessageFromException(exception), -1);
	        this.exception = exception;
	    }

	    public Exception getException() {
	        return exception;
	    }


	}

	
	/**
	 * Private Class which simply fires the events using a copy of the listeners
	 * list in order to avoid problems with listeners that remove themselves or
	 * are removed by someone else
	 */
	final static class ProgressEventDispatchThreadEventLauncher implements Runnable {
	
		/**
		 * The event we want to fire away.
		 */
		private ProcessingEvent event;
	
		/**
		 * The list of listeners.
		 */
		private Object[] listeners;
	
		/**
		 * Default constructor.
		 * 
		 */
		ProgressEventDispatchThreadEventLauncher() {
		}
	
		/**
		 * Used to send an event to an array of listeners.
		 * 
		 * @param evt
		 *            is the {@link ProcessingEvent} to send.
		 * @param listeners
		 *            is the array of {@link ProcessingEventListener}s to
		 *            notify.
		 */
		synchronized void setEvent(final ProcessingEvent evt,
				final Object[] listeners) {
			if (listeners == null || evt == null)
				throw new NullPointerException("Input argumentBuilder cannot be null");
			this.listeners = listeners;
			this.event = evt;
	
		}
	
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			final int numListeners = listeners.length;
			if (event instanceof ExceptionEvent)
				for (int i = 0; i < numListeners; i++)
					((ProcessingEventListener) listeners[i]).exceptionOccurred((ExceptionEvent) this.event);
			else
				for (int i = 0; i < numListeners; i++)
					((ProcessingEventListener) listeners[i]).getNotification(this.event);
		}
	
	}

	/**
	 * This class is responsible for walking through he files inside a directory
	 * (and its children directories) which respect a specified wildcard.
	 * 
	 * <p>
	 * Its role is basically to simplify the construction of the mosaic by
	 * implementing a visitor pattern for the files that we have to use for the
	 * index.
	 * 
	 * <p>
	 * It is based on the Commons IO {@link DirectoryWalker} class.
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS
	 * 
	 */
	final class CatalogBuilderDirectoryWalker  extends DirectoryWalker{

		private AbstractGridFormat cachedFormat;
		private SimpleFeatureType indexSchema;
		private DefaultTransaction transaction;
		@Override
		protected void handleCancelled(File startDirectory, Collection results,
				CancelException cancel) throws IOException {			
			super.handleCancelled(startDirectory, results, cancel);
                        //clean up objects and rollback transaction
                        try{
                                transaction.rollback();
                        }
                        finally{
                                transaction.close();
                        }      
                        
			// close things related to shapefiles
			closeIndexObjects();
			
		
			
			super.handleEnd(results);
		}		

		@Override
		protected boolean handleIsCancelled(final File file, final int depth, Collection results) throws IOException {			
			return CatalogBuilder.this.stop && super.handleIsCancelled(file, depth, results);
		}

		@Override
		protected void handleFile(final File fileBeingProcessed, final int depth, final Collection results)
				throws IOException {
			
			// increment counter
			fileIndex++;

			//
			// Check that this file is actually good to go
			//
			if(!checkFile(fileBeingProcessed))
				return;

			//
			// Anyone has asked us to stop?
			//
			if(!checkStop())
				return; 
		
			// replacing chars on input path
			String validFileName;
			try {
				validFileName = fileBeingProcessed.getCanonicalPath();
				validFileName=FilenameUtils.normalize(validFileName);
			} catch (IOException e1) {
				fireException(e1);
				return;
			}
			validFileName=FilenameUtils.getName(validFileName);
			fireEvent(Level.INFO,"Now indexing file "+validFileName, ((fileIndex * 100.0) / numFiles));
			ImageInputStream inStream=null;
			ImageReader imageioReader = null;
			AbstractGridCoverage2DReader coverageReader=null;
			try {
				//
				// STEP 1
				// Getting an ImageIO reader for this coverage.
				//
				inStream = ImageIO.createImageInputStream(fileBeingProcessed);
				if(inStream==null) {
					fireEvent(Level.INFO,fileBeingProcessed+" has been skipped since we could not get a stream for it", ((fileIndex * 100.0) / numFiles));
					return;
				}
				inStream.mark();
				
				
				cachedSPITest: {
					// there is no cached reader spi, let's look for one
					if(cachedSPI==null){
						final Iterator<ImageReader> it = ImageIO.getImageReaders(inStream);
						if (it.hasNext()) {
							imageioReader = it.next();
							if(imageioReader!=null){
								//cache the SPI
								cachedSPI=imageioReader.getOriginatingProvider();
								imageioReader.setInput(inStream);
							}
						} else {
							imageioReader=null;
						}
					} else {
						// we have a cached SPI, let's try to use it
						if(!cachedSPI.canDecodeInput(inStream)){				
							// the SPI is no good for this input
							cachedSPI=null;
							//take me to the SPI search
							break cachedSPITest;
						}
						// the spi is good
						imageioReader=cachedSPI.createReaderInstance();
						imageioReader.setInput(inStream);
					}
				}
				// did we found a reader
				if (imageioReader == null) {
					// send a message
					fireEvent(Level.INFO,new StringBuilder("Skipped file ").append(fileBeingProcessed).append(":No ImageIO reader	s availaible.").toString(), ((fileIndex * 99.0) / numFiles));
					return;
				}


				//Append
				// STEP 2
				// Getting a coverage reader for this coverage.
				//
				final AbstractGridFormat format;
				if(cachedFormat == null) {
					format= (AbstractGridFormat) GridFormatFinder.findFormat(fileBeingProcessed);
				} else {
					if(cachedFormat.accepts(fileBeingProcessed)) {
						format=cachedFormat;
					} else {
						format=new UnknownFormat();
					}
				}
				if ((format instanceof UnknownFormat)||format == null) {
					fireEvent(Level.INFO,new StringBuilder("Skipped file ").append(fileBeingProcessed).append(": File format is not supported.").toString(), ((fileIndex * 99.0) / numFiles));
					return;
				}
				cachedFormat=format;
				coverageReader = (AbstractGridCoverage2DReader) format.getReader(fileBeingProcessed,runConfiguration.getHints());
				GeneralEnvelope envelope = (GeneralEnvelope) coverageReader.getOriginalEnvelope();
				CoordinateReferenceSystem actualCRS = coverageReader.getCrs();

				//
				// STEP 3
				// Get the type specifier for this image and the check that the
				// image has the correct sample model and color model.
				// If this is the first cycle of the loop we initialize everything.
				//
				final ImageTypeSpecifier its = ((ImageTypeSpecifier) imageioReader.getImageTypes(0).next());
				if (numberOfProcessedFiles==0) {
					// /////////////////////////////////////////////////////////////////////
					//
					// at the first step we initialize everything that we will
					// reuse afterwards starting with color models, sample
					// models, crs, etc....
					//
					// /////////////////////////////////////////////////////////////////////
					defaultCM = its.getColorModel();
					defaultSM = its.getSampleModel();
					if (defaultCM instanceof IndexColorModel) {
						IndexColorModel icm = (IndexColorModel) defaultCM;
						int numBands = defaultCM.getNumColorComponents();
						defaultPalette = new byte[3][icm.getMapSize()];
						icm.getReds(defaultPalette[0]);
						icm.getGreens(defaultPalette[0]);
						icm.getBlues(defaultPalette[0]);
						if (numBands == 4)
							icm.getAlphas(defaultPalette[0]);

					}
					defaultCRS = actualCRS;

					// /////////////////////////////////////////////////////////////////////
					//
					// getting information about resolution
					//
					// /////////////////////////////////////////////////////////////////////


					// get the dimension of the hr image and build the model
					// as well as computing the resolution
					// 
					// resetting reader and recreating stream, turnaround for a
					// strange imageio bug that sometimes pops up
					imageioReader.reset();
					try {
						inStream.reset();
					} catch (IOException e) {
						//close me and reopen me
						try {
							inStream.close();
						} catch (Throwable e1) {
							if(LOGGER.isLoggable(Level.FINE))
								LOGGER.log(Level.FINE,e1.getLocalizedMessage(),e1);
						}
						inStream= ImageIO.createImageInputStream(fileBeingProcessed);
					}
					
					//let's check if we got something now
					if(inStream==null) {
						//skip file
						fireEvent(Level.INFO,fileBeingProcessed+" has been skipped since we could not get a stream for it", ((fileIndex * 100.0) / numFiles));
						return;
					}
					imageioReader.setInput(inStream);
					int numberOfLevels = imageioReader.getNumImages(true);
					double[][] resolutionLevels = new double[2][numberOfLevels];
					setupResolutions(resolutionLevels, numberOfLevels, coverageReader, imageioReader, null);
					mosaicConfiguration.setLevelsNum(numberOfLevels);
					mosaicConfiguration.setLevels(resolutionLevels);

					// /////////////////////////////////////////////////////////////////////
					//
					// creating the schema
					//
					// /////////////////////////////////////////////////////////////////////
					final String schemaDef= runConfiguration.getSchema();
					if(schemaDef!=null){
						// get the schema
						try{
							indexSchema=DataUtilities.createType(mosaicConfiguration.getName()/*.toLowerCase()*/, runConfiguration.getSchema());
							//override the crs in case the provided one was wrong or absent
							indexSchema=DataUtilities.createSubType(indexSchema, DataUtilities.attributeNames(indexSchema), actualCRS);
						}
						catch (Throwable e) {
							if(LOGGER.isLoggable(Level.FINE))
								LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
							indexSchema=null;
						}
					}
					if(indexSchema==null){
						final SimpleFeatureTypeBuilder featureBuilder = new SimpleFeatureTypeBuilder();
						featureBuilder.setName(runConfiguration.getIndexName());
						featureBuilder.setNamespaceURI("http://www.geo-solutions.it/");
						featureBuilder.add(runConfiguration.getLocationAttribute(), String.class);
						featureBuilder.add("the_geom", Polygon.class,actualCRS);
						featureBuilder.setDefaultGeometry("the_geom");
						if(runConfiguration.getTimeAttribute()!=null)
							featureBuilder.add(runConfiguration.getTimeAttribute(), Date.class);
						indexSchema = featureBuilder.buildFeatureType();
					}
					// create the schema for the new shape file
					catalog.createType(indexSchema);
					
				} else {
				    if (!mosaicConfiguration.isHeterogeneous()){
				        // //
				        //
				        // There is no need to check resolutions if the mosaic
				        // has been already marked as heterogeneous
				        //
				        // //
				        int numberOfLevels = imageioReader.getNumImages(true);
                                        if (numberOfLevels != mosaicConfiguration.getLevelsNum()) {
                                            mosaicConfiguration.setHeterogeneous(true);
                                            if (numberOfLevels > mosaicConfiguration.getLevelsNum()){
                                                final double[][] resolutionLevels = new double[2][numberOfLevels];
                                                setupResolutions(resolutionLevels, numberOfLevels, coverageReader, imageioReader, null);
                                                mosaicConfiguration.setLevelsNum(numberOfLevels);
                                                mosaicConfiguration.setLevels(resolutionLevels);
                                            }
                                        } else {
                                            final double[][] mosaicLevels = mosaicConfiguration.getLevels();
                                            final double[][] resolutionLevels = new double[2][numberOfLevels];
                                            final boolean homogeneousLevels = setupResolutions(resolutionLevels, numberOfLevels, coverageReader, imageioReader, mosaicLevels);
                                            if (!homogeneousLevels){
                                                mosaicConfiguration.setHeterogeneous(true);
                                            }
                                        }
				    }
				    
				    
					// ////////////////////////////////////////////////////////
					// 
					// comparing ColorModel
					// comparing SampeModel
					// comparing CRSs
					// ////////////////////////////////////////////////////////
					ColorModel actualCM = its.getColorModel();
					if((fileIndex > 0 ? !(CRS.equalsIgnoreMetadata(defaultCRS, actualCRS)) : false)){
						fireEvent(
								Level.INFO,
								new StringBuilder("Skipping image ").append(fileBeingProcessed).append(" because CRSs do not match.").toString(),
								(((fileIndex + 1) * 99.0) / numFiles));
						return;
					}
					
					if(checkColorModels(defaultCM, defaultPalette,actualCM)){
						fireEvent(
								Level.INFO,
								new StringBuilder("Skipping image ").append(fileBeingProcessed).append(" because color models do not match.").toString(),
								(((fileIndex + 1) * 99.0) / numFiles));
						return;
					}							
				}

				// ////////////////////////////////////////////////////////
				//
				// STEP 4
				//
				// create and store features
				//
				// ////////////////////////////////////////////////////////
				final SimpleFeature feature = DataUtilities.template(indexSchema);
				feature.setAttribute(indexSchema.getGeometryDescriptor().getLocalName(), geomFactory.toGeometry(new ReferencedEnvelope((Envelope) envelope)));
				feature.setAttribute(runConfiguration.getLocationAttribute(), prepareLocation(fileBeingProcessed));
				
				// collect and dump properties
				if(propertiesCollectors!=null&&propertiesCollectors.size()>0)
					for(PropertiesCollector pc: propertiesCollectors)
					{
						pc.collect(fileBeingProcessed).collect(coverageReader).collect(imageioReader).setProperties(feature);
						pc.reset();
					}

				catalog.addGranule(feature,transaction);

				// fire event
				fireEvent(Level.FINE,"Done with file "+fileBeingProcessed, (((fileIndex + 1) * 99.0) / numFiles));
			

				// advance files
				numberOfProcessedFiles++;
			} catch (IOException e) {
				fireException(e);
				return;
			} catch (ArrayIndexOutOfBoundsException e) {
				fireException(e);
				return;
			} 
			finally{
				// ////////////////////////////////////////////////////////
				//
				// STEP 5
				//
				// release resources
				//
				// ////////////////////////////////////////////////////////
				try {
					if(inStream!=null)
						inStream.close();
				} catch (Throwable e) {
					// ignore exception
					if(LOGGER.isLoggable(Level.FINEST))
						LOGGER.log(Level.FINEST,e.getLocalizedMessage(),e);
				}
				try {
					if(imageioReader!=null)
						imageioReader.dispose();
				} catch (Throwable e) {
					// ignore exception
					if(LOGGER.isLoggable(Level.FINEST))
						LOGGER.log(Level.FINEST,e.getLocalizedMessage(),e);					
				}				
		
				
				try {
					if(coverageReader!=null)
						// release resources
						coverageReader.dispose();
				} catch (Throwable e) {
					// ignore exception
					if(LOGGER.isLoggable(Level.FINEST))
						LOGGER.log(Level.FINEST,e.getLocalizedMessage(),e);					
				}
			}
		
			
			super.handleFile(fileBeingProcessed, depth, results);
		}

        private String prepareLocation(final File fileBeingProcessed) throws IOException {
			//absolute
			if(runConfiguration.isAbsolute())
				return fileBeingProcessed.getAbsolutePath();
			
			// relative
			String path=fileBeingProcessed.getCanonicalPath();
			path=path.substring(runConfiguration.getRootMosaicDirectory().length());
			return path;
			
		}

		private boolean checkStop() {

			if (getStop()) {
				StringBuilder message = new StringBuilder("Stopping requested at file  ").append(fileIndex).append(" of ").append(numFiles).append(" files");
				fireEvent(Level.INFO,message.toString(), ((fileIndex * 100.0) / numFiles));
				return false;
			}
			return true;
		}

		private boolean checkFile(final File fileBeingProcessed) {
			if(!fileBeingProcessed.exists()||!fileBeingProcessed.canRead()||!fileBeingProcessed.isFile())
			{
				// send a message
				final StringBuilder message = new StringBuilder("Skipped file ").append(fileBeingProcessed).append(" snce it seems invalid.");
				fireEvent(Level.INFO,message.toString(), ((fileIndex * 99.0) / numFiles));
				return false;
			}
			return true;
		}

		public CatalogBuilderDirectoryWalker(final File root,final FileFilter filter) throws IOException {
			super(filter,Integer.MAX_VALUE);//runConfiguration.isRecursive()?Integer.MAX_VALUE:0);
			this.transaction= new DefaultTransaction("MosaicCreationTransaction"+System.nanoTime());
			walk(root, null);
		}

		public int getNumberOfProcessedFiles() {
			return numberOfProcessedFiles;
		}

		/**
		 * This method checks the {@link ColorModel} of the current image with the
		 * one of the first image in order to check if they are compatible or not in
		 * order to perform a mosaic operation.
		 * 
		 * <p>
		 * It is worth to point out that we also check if, in case we have two index
		 * color model image, we also try to suggest whether or not we should do a
		 * color expansion.
		 * 
		 * @param defaultCM
		 * @param defaultPalette
		 * @param actualCM
		 * @return a boolean asking to skip this feature.
		 */
		private boolean checkColorModels(ColorModel defaultCM,
				byte[][] defaultPalette, ColorModel actualCM) {
		
			// /////////////////////////////////////////////////////////////////////
			//
			//
			// ComponentColorModel
			//
			//
			// /////////////////////////////////////////////////////////////////////
			if (defaultCM instanceof ComponentColorModel
					&& actualCM instanceof ComponentColorModel) {
				final ComponentColorModel defCCM = (ComponentColorModel) defaultCM, actualCCM = (ComponentColorModel) actualCM;
				final ColorSpace defCS = defCCM.getColorSpace();
				final ColorSpace actualCS = actualCCM.getColorSpace();
				final boolean isBogusDef = defCS instanceof BogusColorSpace;
				final boolean isBogusActual = actualCS instanceof BogusColorSpace;
				final boolean colorSpaceIsOk;
				if (isBogusDef && isBogusActual){
				    final BogusColorSpace def = (BogusColorSpace) defCS;
				    final BogusColorSpace act = (BogusColorSpace) actualCS;
				    colorSpaceIsOk = def.getNumComponents() == act.getNumComponents() &&
				    def.isCS_sRGB() == act.isCS_sRGB() && def.getType() == act.getType(); 
				}
				else
				    colorSpaceIsOk = defCS.equals(actualCS);
							
				return !(defCCM.getNumColorComponents() == actualCCM
						.getNumColorComponents()
						&& defCCM.hasAlpha() == actualCCM.hasAlpha()
						&& colorSpaceIsOk
						&& defCCM.getTransparency() == actualCCM.getTransparency() && defCCM
						.getTransferType() == actualCCM.getTransferType());
			}
			
			// /////////////////////////////////////////////////////////////////////
			//
			//
			// IndexColorModel
			//
			//
			// /////////////////////////////////////////////////////////////////////
			if (defaultCM instanceof IndexColorModel
					&& actualCM instanceof IndexColorModel) {
				final IndexColorModel defICM = (IndexColorModel) defaultCM, actualICM = (IndexColorModel) actualCM;
				if (defICM.getNumColorComponents() != actualICM
						.getNumColorComponents()
						|| defICM.hasAlpha() != actualICM.hasAlpha()
						|| !defICM.getColorSpace()
								.equals(actualICM.getColorSpace())
						|| defICM.getTransferType() != actualICM.getTransferType())
					return true;
		
				//
				// Suggesting expansion in the simplest case
				//
				if (defICM.getMapSize() != actualICM.getMapSize()
						|| defICM.getTransparency() != actualICM.getTransparency()
						|| defICM.getTransferType() != actualICM.getTransferType()
						|| defICM.getTransparentPixel() != actualICM
								.getTransparentPixel()) {
					mustConvertToRGB = true;
					return false;
				}
		
				//
				// Now checking palettes to see if we need to do a color convert
				//
				// get the palette for this color model
				int numBands = actualICM.getNumColorComponents();
				byte[][] actualPalette = new byte[3][actualICM.getMapSize()];
				actualICM.getReds(actualPalette[0]);
				actualICM.getGreens(actualPalette[0]);
				actualICM.getBlues(actualPalette[0]);
				if (numBands == 4)
					actualICM.getAlphas(defaultPalette[0]);
				// compare them
				for (int i = 0; i < defICM.getMapSize(); i++)
					for (int j = 0; j < numBands; j++)
						if (actualPalette[j][i] != defaultPalette[j][i]) {
							mustConvertToRGB = true;
							break;
						}
				return false;
		
			}
			
			//
			// if we get here this means that the two color models where completely
			// different, hence skip this feature.
			//
			return true;
		}

		@Override
		protected void handleEnd(Collection results) throws IOException {
			try{
				transaction.commit();
			}
			finally{
				transaction.close();
			}		
			indexingPostamble();
			super.handleEnd(results);
		}

		@Override
		protected void handleStart(File startDirectory, Collection results)
				throws IOException {
			indexingPreamble();
			super.handleStart(startDirectory, results);
			
			
		}
		
		
	}

	/** Number of files to process. */
	private int numFiles;

	/**
	 * List containing all the objects that want to be notified during
	 * processing.
	 */
	private List<ProcessingEventListener> notificationListeners = Collections.synchronizedList(new ArrayList<ProcessingEventListener>());

	/**
	 * Set this to false for command line UIs where the delayed event sending
	 * may prevent some messages to be seen before the tool exits, to true for
	 * real GUI where you don't want the processing to be blocked too long, or
	 * when you have slow listeners in general.
	 */
	private boolean sendDelayedMessages = false;

	/**
	 * Proper way to stop a thread is not by calling Thread.stop() but by using
	 * a shared variable that can be checked in order to notify a terminating
	 * condition.
	 */
	private volatile boolean stop = false;

	private MosaicConfigurationBean mosaicConfiguration;

	private GeometryFactory geomFactory;

	private GranuleCatalog catalog;

	private int numberOfProcessedFiles;

	/**
	 * This field will tell the plugin if it must do a conversion of color from
	 * the original index color model to an RGB color model. This happens f the
	 * original images uses different color maps between each other making for
	 * us impossible to reuse it for the mosaic.
	 */
	private boolean mustConvertToRGB = false;

	private int fileIndex=0;

	private ColorModel defaultCM = null;

	private CoordinateReferenceSystem defaultCRS = null;

	private byte[][] defaultPalette = null;

	private CatalogBuilderConfiguration runConfiguration;

	private ImageReaderSpi cachedSPI;

	private List<PropertiesCollector> propertiesCollectors;

	private SampleModel defaultSM;

	private ReferencedEnvelope imposedBBox;
	
	/* (non-Javadoc)
	 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#run()
	 */
	public void run() {
		
		
		try {

			//
			// creating the file filters for scanning for files to check and index
			//
			final IOFileFilter finalFilter = createGranuleFilterRules();
			
			//TODO we might want to remove this in the future for performance
			numFiles=0;
			for(String indexingDirectory:runConfiguration.getIndexingDirectories()){
				final File directoryToScan = new File(indexingDirectory);
				final Collection files = FileUtils.listFiles(
						directoryToScan,
						finalFilter, 
						runConfiguration.isRecursive()?TrueFileFilter.INSTANCE:FalseFileFilter.INSTANCE);
				numFiles += files.size();
			}
			//
			// walk over the files that have filtered out
			//
			if(numFiles>0)
			{
				final List<String> indexingDirectories = runConfiguration.getIndexingDirectories();
				for(String indexingDirectory:indexingDirectories){
					@SuppressWarnings("unused")
					final CatalogBuilderDirectoryWalker walker = new CatalogBuilderDirectoryWalker(new File(indexingDirectory),finalFilter);
				}
			}
				
			
			
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE,e.getLocalizedMessage(),e);
		} 

	}

	/**
	 * @return
	 */
	private IOFileFilter createGranuleFilterRules() {
		final IOFileFilter specialWildCardFileFilter= new WildcardFileFilter(runConfiguration.getWildcard(),IOCase.INSENSITIVE);
		IOFileFilter dirFilter = 
		    FileFilterUtils.andFileFilter(FileFilterUtils.directoryFileFilter(),HiddenFileFilter.VISIBLE);
		IOFileFilter fileFilter=Utils.excludeFilters(
			FileFilterUtils.makeSVNAware(
					FileFilterUtils.makeFileOnly(
							FileFilterUtils.andFileFilter(
									specialWildCardFileFilter,HiddenFileFilter.VISIBLE))), 
									FileFilterUtils.suffixFileFilter("shp"),
									FileFilterUtils.suffixFileFilter("dbf"),
									FileFilterUtils.suffixFileFilter("shx"), 
									FileFilterUtils.suffixFileFilter("prj"), 
									FileFilterUtils.nameFileFilter("error.txt"),
									FileFilterUtils.nameFileFilter("error.txt.lck"),
									FileFilterUtils.suffixFileFilter("properties"),
									FileFilterUtils.suffixFileFilter("svn-base")
									);

		// exclude common extensions
		Set<String> extensions=WorldImageFormat.getWorldExtension("png");
		for(String ext:extensions){
			fileFilter=FileFilterUtils.andFileFilter(
					fileFilter, 
					FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter(ext.substring(1))));
		}
		extensions=WorldImageFormat.getWorldExtension("gif");
		for(String ext:extensions){
			fileFilter=FileFilterUtils.andFileFilter(
					fileFilter, 
					FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter(ext.substring(1))));
		}	
		extensions=WorldImageFormat.getWorldExtension("jpg");
		for(String ext:extensions){
			fileFilter=FileFilterUtils.andFileFilter(
					fileFilter, 
					FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter(ext.substring(1))));
		}
		extensions=WorldImageFormat.getWorldExtension("tiff");
		for(String ext:extensions){
			fileFilter=FileFilterUtils.andFileFilter(
					fileFilter, 
					FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter(ext.substring(1))));
		}		
		extensions=WorldImageFormat.getWorldExtension("bmp");
		for(String ext:extensions){
			fileFilter=FileFilterUtils.andFileFilter(
					fileFilter, 
					FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter(ext.substring(1))));
		}		
		
		//sdw
		fileFilter=FileFilterUtils.andFileFilter(
				fileFilter, 
				FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("sdw")));	
		//aux
		fileFilter=FileFilterUtils.andFileFilter(
				fileFilter, 
				FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("aux")));	
		//wld
		fileFilter=FileFilterUtils.andFileFilter(
				fileFilter, 
				FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("wld")));				
		//svn
                fileFilter=FileFilterUtils.andFileFilter(
                                fileFilter, 
                                FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("svn")));                                
                                        
		
		final IOFileFilter finalFilter=	FileFilterUtils.orFileFilter(dirFilter, fileFilter);
		return finalFilter;
	}

	/**
	 * Default constructor
	 * @throws  
	 * @throws IllegalArgumentException 
	 */
	public CatalogBuilder(final CatalogBuilderConfiguration configuration)  {
		Utilities.ensureNonNull("runConfiguration", configuration);
		//check config
		configuration.check();
		
		this.runConfiguration = new CatalogBuilderConfiguration(configuration);

	}


	/**
	 * Adding a listener to the {@link ProcessingEventListener}s' list.
	 * 
	 * @param listener
	 *            to add to the list of listeners.
	 */
	public final void addProcessingEventListener(final ProcessingEventListener listener) {
		synchronized (notificationListeners) {
			notificationListeners.add(listener);
		}
	}

	/**
	 * Perform proper clean up.
	 * 
	 * <p>
	 * Make sure to call this method when you are not running the
	 * {@link CatalogBuilder} or bad things can happen. If it is running, please
	 * stop it first.
	 */
	public  void reset() {
		removeAllProcessingEventListeners();
		// clear stop
		stop=false;
	
		closeIndexObjects();
		
		//clear other stuff
		defaultCM=null;
		defaultCRS=null;
		defaultPalette=null;
		fileIndex=0;
		numberOfProcessedFiles=0;
		
		// clear directories
		runConfiguration=null;
				
			
	}

	/**
	 * Firing an event to listeners in order to inform them about what we are
	 * doing and about the percentage of work already carried out.
	 * @param level 
	 * 
	 * @param message
	 *            The message to show.
	 * @param percentage
	 *            The percentage for the process.
	 */
	private void fireEvent(Level level, final String inMessage, final double percentage) {
		if (LOGGER.isLoggable(level)) {
			LOGGER.log(level,inMessage);
		}
		synchronized (notificationListeners) {
			final String newLine = System.getProperty("line.separator");
			final StringBuilder message = new StringBuilder("Thread Name ");
			message.append(Thread.currentThread().getName()).append(newLine);
			message.append(this.getClass().toString()).append(newLine).append(inMessage);
			final ProcessingEvent evt = new ProcessingEvent(this, message.toString(),percentage);
			ProgressEventDispatchThreadEventLauncher eventLauncher = new ProgressEventDispatchThreadEventLauncher();
			eventLauncher.setEvent(evt, this.notificationListeners.toArray());
			sendEvent(eventLauncher);
		}
	}

	/**
	 * Firing an exception event to listeners in order to inform them that
	 * processing broke and we can no longer proceed. This is a convenience
	 * method, it will call {@link #fireException(String, double, Exception)}
	 * with the exception message and -1 as percentage.
	 * 
	 * @param ex
	 *            the actual exception occurred
	 */
	private  void fireException(Exception ex) {
		synchronized (notificationListeners) {
			fireException(Utils.getMessageFromException(ex), -1, ex);
		}
	}

	/**
	 * Firing an exception event to listeners in order to inform them that
	 * processing broke and we can no longer proceed
	 * 
	 * @param string
	 *            The message to show.
	 * @param percentage
	 *            The percentage for the process.
	 * @param ex
	 *            the actual exception occurred
	 */
	private  void fireException(final String string, final double percentage,Exception ex) {
		synchronized (notificationListeners) {
			final String newLine = System.getProperty("line.separator");
			final StringBuilder message = new StringBuilder("Thread Name ");
			message.append(Thread.currentThread().getName()).append(newLine);
			message.append(this.getClass().toString()).append(newLine).append(
					string);
			final ExceptionEvent evt = new ExceptionEvent(this, string, percentage,
					ex);
			ProgressEventDispatchThreadEventLauncher eventLauncher = new ProgressEventDispatchThreadEventLauncher();
			eventLauncher.setEvent(evt, this.notificationListeners.toArray());
			sendEvent(eventLauncher);
		}
	}

	/* (non-Javadoc)
	 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#getStop()
	 */
	public  boolean getStop() {
		return stop;
	}

	public boolean isSendDelayedMessages() {
		return sendDelayedMessages;
	}

	public void setSendDelayedMessages(boolean sendDelayedMessages) {
		this.sendDelayedMessages = sendDelayedMessages;
	}
	/**
	 * Removing all the listeners.
	 * 
	 */
	public void removeAllProcessingEventListeners() {
		synchronized (notificationListeners) {
			notificationListeners.clear();
		}
		
	
	}

	/**
	 * Removing a {@link ProcessingEventListener} from the listeners' list.
	 * 
	 * @param listener
	 *            {@link ProcessingEventListener} to remove from the list of
	 *            listeners.
	 */
	public void removeProcessingEventListener(final ProcessingEventListener listener) {
		synchronized (notificationListeners) {
			notificationListeners.remove(listener);
		}
	}

	private void sendEvent(ProgressEventDispatchThreadEventLauncher eventLauncher) {
		if (sendDelayedMessages)
			SwingUtilities.invokeLater(eventLauncher);
		else
			eventLauncher.run();
	}

	/* (non-Javadoc)
	 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#stop()
	 */
	public void stop() {
		stop = true;
	}

	private void indexingPreamble()throws IOException {
		
		//
		// declaring a precision model to adhere the java double type
		// precision
		//
		final PrecisionModel precMod = new PrecisionModel(PrecisionModel.FLOATING);
		geomFactory = new GeometryFactory(precMod);
		
		//
		// create the index
		//
		// do we have a datastore.properties file?
		final File parent=new File(runConfiguration.getRootMosaicDirectory());
		final File datastoreProperties= new File(parent,"datastore.properties");
		if(Utils.checkFileReadable(datastoreProperties)){
			// read the properties file
			Properties properties = Utils.loadPropertiesFromURL(DataUtilities.fileToURL(datastoreProperties));
			if (properties == null)
				throw new IOException();

			// SPI
			final String SPIClass = properties.getProperty("SPI");
			try {
				// create a datastore as instructed
				final DataStoreFactorySpi spi = (DataStoreFactorySpi) Class.forName(SPIClass).newInstance();
				final Map<String, Serializable> params = Utils.createDataStoreParamsFromPropertiesFile(properties,spi);
				catalog=GranuleCatalogFactory.createGranuleCatalog(params,false,true, spi);
			} catch (ClassNotFoundException e) {
				final IOException ioe = new IOException();
				throw (IOException) ioe.initCause(e);
			} catch (InstantiationException e) {
				final IOException ioe = new IOException();
				throw (IOException) ioe.initCause(e);
			} catch (IllegalAccessException e) {
				final IOException ioe = new IOException();
				throw (IOException) ioe.initCause(e);
			}			
		} else {
			
			// we do not have a datastore properties file therefore we continue with a shapefile datastore
			final URL file= new File(parent ,runConfiguration.getIndexName() + ".shp").toURI().toURL();
			final Map<String, Serializable> params = new HashMap<String, Serializable>();			 
			params.put(ShapefileDataStoreFactory.URLP.key,file);
			if(file.getProtocol().equalsIgnoreCase("file"))
				params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, Boolean.TRUE);
			params.put(ShapefileDataStoreFactory.MEMORY_MAPPED.key, Boolean.TRUE);
			params.put(ShapefileDataStoreFactory.DBFTIMEZONE.key, TimeZone.getTimeZone("UTC"));
			catalog= GranuleCatalogFactory.createGranuleCatalog(params,false,true, Utils.SHAPE_SPI);
		}
	
		//
		// creating a mosaic runConfiguration bean to store the properties file elements			
		//
		mosaicConfiguration= new MosaicConfigurationBean();
		mosaicConfiguration.setName(runConfiguration.getIndexName());
		
		//
		// IMPOSED ENVELOPE
		//
		String bbox= runConfiguration.getEnvelope2D();
		try{
			this.imposedBBox=Utils.parseEnvelope(bbox);
		}catch (Exception e) {
			this.imposedBBox=null;
			if(LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING,"Unable to parse imposed bbox",e);
		}
		mosaicConfiguration.setCaching(runConfiguration.isCaching());
		//
		// load property collectors
		//
		loadPropertyCollectors();
			
	}

	private void loadPropertyCollectors() {
		// load property collectors
		final String pcConfig = runConfiguration.getPropertyCollectors();
		if (pcConfig != null && pcConfig.length()>0){
			// load the SPI set
			final Set<PropertiesCollectorSPI> pcSPIs = PropertiesCollectorFinder.getPropertiesCollectorSPI();
			
			// parse the string
			final List<PropertiesCollector> pcs= new ArrayList<PropertiesCollector>();
			final String[] pcsDefs=pcConfig.split(",");
			for (String pcDef: pcsDefs) {
				// parse this def as NAME[CONFIG_FILE](PROPERTY;PROPERTY;....;PROPERTY)
				final int squareLPos = pcDef.indexOf("[");
				final int squareRPos = pcDef.indexOf("]");
				final int squareRPosLast = pcDef.lastIndexOf("]");
				final int roundLPos = pcDef.indexOf("(");
				final int roundRPos = pcDef.indexOf(")");
				final int roundRPosLast = pcDef.lastIndexOf(")");				
				if (squareRPos != squareRPosLast)
					continue;
				if (squareLPos == -1 || squareRPos == -1)
					continue;
				if (squareLPos == 0)
					continue;
				
				if (roundRPos != roundRPosLast)
					continue;
				if (roundLPos == -1 || roundRPos == -1)
					continue;
				if (roundLPos == 0)
					continue;	
				if (roundLPos != squareRPos + 1)//]( or exit
					continue;		
				if (roundRPos != (pcDef.length() - 1))// end with )
					continue;	
				
				// name
				final String name=pcDef.substring(0,squareLPos);
				PropertiesCollectorSPI selectedSPI = null;
				for (PropertiesCollectorSPI spi:pcSPIs) {
					if (spi.isAvailable() && spi.getName().equalsIgnoreCase(name)) {
						selectedSPI=spi;
						break;
					}
				}
				if (selectedSPI == null)
					continue;
				
				// config
				final String config=squareLPos<squareRPos?pcDef.substring(squareLPos+1,squareRPos):"";
				final File configFile= new File(runConfiguration.getRootMosaicDirectory(),config+".properties");
				if (!Utils.checkFileReadable(configFile))
					continue;
				// it is readable
				
				// property names
				final String propertyNames[] = pcDef.substring(roundLPos+1, roundRPos).split(",");
				
				// create the PropertiesCollector
				final PropertiesCollector pc = selectedSPI.create(configFile, Arrays.asList(propertyNames));
				if (pc != null) {
				    pcs.add(pc);
				} else {
				    if(LOGGER.isLoggable(Level.INFO)){
				        LOGGER.info("Unable to create PropertyCollector from config file:"+configFile);
				    }
				}
				
			}
			this.propertiesCollectors=pcs;
		}
	}

	private void indexingPostamble() throws IOException {
		//close shapefile elements
		closeIndexObjects();
		
		// create sample image if the needed elements are available
		createSampleImage();
		
		// complete initialization of mosaic configuration
		if(numberOfProcessedFiles>0){
			mosaicConfiguration.setName(runConfiguration.getIndexName());
			mosaicConfiguration.setExpandToRGB(mustConvertToRGB);
			mosaicConfiguration.setAbsolutePath(runConfiguration.isAbsolute());
			mosaicConfiguration.setLocationAttribute(runConfiguration.getLocationAttribute());
			mosaicConfiguration.setCaching(runConfiguration.isCaching());
			final String timeAttribute= runConfiguration.getTimeAttribute();
			if (timeAttribute != null) {
				mosaicConfiguration.setTimeAttribute(runConfiguration.getTimeAttribute());
			}
			final String elevationAttribute= runConfiguration.getElevationAttribute();
			if (elevationAttribute != null) {
				mosaicConfiguration.setElevationAttribute(runConfiguration.getElevationAttribute());
			}
			final String runtimeAttribute= runConfiguration.getRuntimeAttribute();
			if (runtimeAttribute != null) {
				mosaicConfiguration.setRuntimeAttribute(runConfiguration.getRuntimeAttribute());
			}
			createPropertiesFiles();
			
			// processing information
			fireEvent(Level.FINE, "Done!!!", 100);				
		} else {
			//	processing information
			fireEvent(Level.FINE, "Nothing to process!!!", 100);
		}
	}
	

	/**
	 * Store a sample image frmo which we can derive the default SM and CM
	 */
	private void createSampleImage() {
		// create a sample image to store SM and CM
		if (defaultCM !=null && defaultSM != null){
			
			// sample image file
			final File sampleImageFile= new File(runConfiguration.getRootMosaicDirectory() + "/sample_image");
			try {
				Utils.storeSampleImage(sampleImageFile, defaultSM, defaultCM);
			} catch (IOException e) {
				fireEvent(Level.SEVERE,e.getLocalizedMessage(), 0);
			}			
		}
	}

	private void closeIndexObjects() {
		try {
			if (catalog != null) {
				catalog.dispose();
			}
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	
		catalog=null;
	}

	/**
	 * Creates the final properties file.
	 */
	private void createPropertiesFiles() {
		// /////////////////////////////////////////////////////////////////////
		//
		// FINAL STEP
		//
		// CREATING GENERAL INFO FILE
		//
		// /////////////////////////////////////////////////////////////////////
		fireEvent(Level.INFO,"Creating final properties file ", 99.9);
	
		// envelope
		final Properties properties = new Properties();
		properties.setProperty("AbsolutePath", Boolean.toString(mosaicConfiguration.isAbsolutePath()));
		properties.setProperty("LocationAttribute", mosaicConfiguration.getLocationAttribute());
		final String timeAttribute=mosaicConfiguration.getTimeAttribute();
		if (timeAttribute != null) {
			properties.setProperty("TimeAttribute", mosaicConfiguration.getTimeAttribute());
		}
		final String elevationAttribute=mosaicConfiguration.getElevationAttribute();
		if (elevationAttribute != null) {
			properties.setProperty("ElevationAttribute", mosaicConfiguration.getElevationAttribute());
		}
		final String runtimeAttribute=mosaicConfiguration.getRuntimeAttribute();
		if (runtimeAttribute != null) {
			properties.setProperty("RuntimeAttribute", mosaicConfiguration.getRuntimeAttribute());
		}
		
		final int numberOfLevels=mosaicConfiguration.getLevelsNum();
		final double[][] resolutionLevels=mosaicConfiguration.getLevels();
		properties.setProperty("LevelsNum", Integer.toString(numberOfLevels));
		final StringBuilder levels = new StringBuilder();
		for (int k = 0; k < numberOfLevels; k++) {
			levels.append(Double.toString(resolutionLevels[0][k])).append(",").append(Double.toString(resolutionLevels[1][k]));
			if (k < numberOfLevels - 1)
				levels.append(" ");
		}
		properties.setProperty("Levels", levels.toString());
		properties.setProperty("Name", runConfiguration.getIndexName());
		properties.setProperty("ExpandToRGB", Boolean.toString(mustConvertToRGB));
		properties.setProperty("Heterogeneous", Boolean.toString(mosaicConfiguration.isHeterogeneous()));
		
		if (cachedSPI != null){
			// suggested spi
			properties.setProperty("SuggestedSPI", cachedSPI.getClass().getName());
		}

		// write down imposed bbox
		if (imposedBBox != null){
			properties.setProperty("Envelope2D", imposedBBox.getMinX()+","+imposedBBox.getMinY()+" "+imposedBBox.getMaxX()+","+imposedBBox.getMaxY());
		}
		properties.setProperty("Caching", Boolean.toString(mosaicConfiguration.isCaching()));
		OutputStream outStream=null;
		try {
			outStream = new BufferedOutputStream(new FileOutputStream(runConfiguration.getRootMosaicDirectory() + "/" + runConfiguration.getIndexName() + ".properties"));
			properties.store(outStream, "-Automagically created from GeoTools-");
		} catch (FileNotFoundException e) {
			fireEvent(Level.SEVERE,e.getLocalizedMessage(), 0);
		} catch (IOException e) {
			fireEvent(Level.SEVERE,e.getLocalizedMessage(), 0);
		} finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
			} catch (Throwable e) {
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
			}
		}
	}

	public void dispose() {
		reset();
	}
	
	/**
	 * 
	 * @param resolutionLevels
	 * @param numberOfLevels
	 * @param coverageReader
	 * @param imageioReader
	 * @param compareLevels optional resolutionLevels to be compared.
	 * @return 
	 * @throws IndexOutOfBoundsException
	 * @throws IOException
	 */
	private static boolean setupResolutions(
                final double[][] resolutionLevels, 
                final int numberOfLevels, 
                final AbstractGridCoverage2DReader coverageReader, 
                final ImageReader imageioReader,
                final double[][] compareLevels
                ) throws IndexOutOfBoundsException, IOException {
            double[] res = CoverageUtilities.getResolution((AffineTransform) coverageReader.getOriginalGridToWorld(PixelInCell.CELL_CORNER));
            resolutionLevels[0][0] = res[0];
            resolutionLevels[1][0] = res[1];
            final boolean checkLevels = compareLevels != null;

            // resolutions levels are computed using the raster space scale factors
            if (numberOfLevels >= 1) {
                for (int k = 0; k < numberOfLevels; k++) {
                    resolutionLevels[0][k] = resolutionLevels[0][0]*coverageReader.getOriginalGridRange().getSpan(0)/(1.0*imageioReader.getWidth(k));
                    resolutionLevels[1][k] = resolutionLevels[1][0]*coverageReader.getOriginalGridRange().getSpan(1)/(1.0*imageioReader.getHeight(k));
                    if (checkLevels)  {
                        if (Math.abs(resolutionLevels[0][k] - compareLevels[0][k]) > RESOLUTION_TOLERANCE_FACTOR * compareLevels[0][k] ||
                            Math.abs(resolutionLevels[1][k] - compareLevels[1][k]) > RESOLUTION_TOLERANCE_FACTOR * compareLevels[1][k]){
                            return false;
                        }
                    }
                }
            }
            return true;
        }

}
