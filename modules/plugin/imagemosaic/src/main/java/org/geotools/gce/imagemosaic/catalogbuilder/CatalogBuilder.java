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
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.swing.SwingUtilities;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
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
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogFactory;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorFinder;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.io.ImageIOExt;
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
 *
 * @source $URL$
 */
@SuppressWarnings("rawtypes")
public class CatalogBuilder implements Runnable {

    final private static double RESOLUTION_TOLERANCE_FACTOR = 1E-2;

	/** Default Logger * */
	final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CatalogBuilder.class);
	
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

		private DefaultTransaction transaction;
                private volatile boolean canceled;
		
		@Override
		protected void handleCancelled(File startDirectory, Collection results,
				CancelException cancel) throws IOException {			
			super.handleCancelled(startDirectory, results, cancel);
                        //clean up objects and rollback transaction
                       if(LOGGER.isLoggable(Level.INFO))
                           LOGGER.info("Stop requested when walking directory "+startDirectory);			
			super.handleEnd(results);
		}		

		@Override
		protected boolean handleIsCancelled(final File file, final int depth, Collection results) throws IOException {	

                    //
                    // Anyone has asked us to stop?
                    //
                    if(!checkStop()){
                        canceled=true;
                        return true ; 
                    }
                    return false;
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
			        // try to use cache
			        if(cachedStreamSPI!=null ){
			            inStream=cachedStreamSPI.createInputStreamInstance(fileBeingProcessed);
			        } 
			        if(inStream==null ){
			            // failed, look for a new SPI
			            cachedStreamSPI= ImageIOExt.getImageInputStreamSPI(fileBeingProcessed);
			            if(cachedStreamSPI!=null){
			                inStream=cachedStreamSPI.createInputStreamInstance(fileBeingProcessed);
			            }
			            
			        } 
    				if(inStream==null) {
    				    // failed again
    					fireEvent(Level.INFO,fileBeingProcessed+" has been skipped since we could not get a stream for it", ((fileIndex * 100.0) / numFiles));
    					return;
    				}
				inStream.mark();
				
				
				cachedReaderSPITest: {
					// there is no cached reader spi, let's look for one
					if(cachedReaderSPI==null){
						final Iterator<ImageReader> it = ImageIO.getImageReaders(inStream);
						if (it.hasNext()) {
							imageioReader = it.next();
							if(imageioReader!=null){
								//cache the SPI
								cachedReaderSPI=imageioReader.getOriginatingProvider();
								imageioReader.setInput(inStream);
							}
						} else {
							imageioReader=null;
						}
					} else {
						// we have a cached SPI, let's try to use it
						if(!cachedReaderSPI.canDecodeInput(inStream)){				
							// the SPI is no good for this input
							cachedReaderSPI=null;
							//take me to the SPI search
							break cachedReaderSPITest;
						}
						// the spi is good
						imageioReader=cachedReaderSPI.createReaderInstance();
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
					
					//
					// at the first step we initialize everything that we will
					// reuse afterwards starting with color models, sample
					// models, crs, etc....
					//
					
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

					
					//
					// getting information about resolution
					//
					


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

					
					//
					// creating the schema
					//
					
					String schema = runConfiguration.getSchema();
					if(schema!=null){
					        schema=schema.trim();
						// get the schema
						try{
							indexSchema=DataUtilities.createType(mosaicConfiguration.getName(), schema);
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
						featureBuilder.add(runConfiguration.getLocationAttribute().trim(), String.class);
						featureBuilder.add("the_geom", Polygon.class,actualCRS);
						featureBuilder.setDefaultGeometry("the_geom");
						if(runConfiguration.getTimeAttribute()!=null)
							featureBuilder.add(runConfiguration.getTimeAttribute().trim(), Date.class);
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
				
				fireEvent(Level.INFO,"Stopping requested at file  "+fileIndex+" of "+numFiles+" files", ((fileIndex * 100.0) / numFiles));
				return false;
			}
			return true;
		}

		private boolean checkFile(final File fileBeingProcessed) {
			if(!fileBeingProcessed.exists()||!fileBeingProcessed.canRead()||!fileBeingProcessed.isFile())
			{
				// send a message
				fireEvent(Level.INFO,"Skipped file "+fileBeingProcessed+" snce it seems invalid", ((fileIndex * 99.0) / numFiles));
				return false;
			}
			return true;
		}

		public CatalogBuilderDirectoryWalker(final List<String> indexingDirectories,final FileFilter filter) throws IOException {
			super(filter,Integer.MAX_VALUE);//runConfiguration.isRecursive()?Integer.MAX_VALUE:0);
			
			this.transaction= new DefaultTransaction("MosaicCreationTransaction"+System.nanoTime());
                        indexingPreamble();

                        try {
                            // start walking directories
                            for(String indexingDirectory:indexingDirectories){
                                walk(new File(indexingDirectory), null);
                                
                                // did we cancel?
                                if(canceled)
                                    break;
                            }
                         // did we cancel?
                            if(canceled)
                                transaction.rollback();
                            else
                                transaction.commit();
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Failure occurred while collecting the granules", e);
                            transaction.rollback();
                        } finally {
                            try {
                                transaction.close();
                            } catch (Exception e) {
                                final String message = "Unable to close indexing" + e.getLocalizedMessage();
                                if (LOGGER.isLoggable(Level.WARNING)) {
                                    LOGGER.log(Level.WARNING, message, e);
                                }
                                // notify listeners
                                fireException(e);
                            }
                    
                            try {
                                indexingPostamble(!canceled);
                            } catch (Exception e) {
                                final String message = "Unable to close indexing" + e.getLocalizedMessage();
                                if (LOGGER.isLoggable(Level.WARNING)) {
                                    LOGGER.log(Level.WARNING, message, e);
                                }
                                // notify listeners
                                fireException(e);
                            }
                        }
                        
			
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
		
			
			//
			//
			// ComponentColorModel
			//
			//
			
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
			
			
			//
			//
			// IndexColorModel
			//
			//
			
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

//		@Override
//		protected void handleEnd(Collection results) throws IOException {
//			try{
//				transaction.commit();
//			}
//			finally{
//				transaction.close();
//			}		
//			indexingPostamble();
//			super.handleEnd(results);
//		}

//		@Override
//		protected void handleStart(File startDirectory, Collection results)
//				throws IOException {
//			indexingPreamble();
//			super.handleStart(startDirectory, results);
//			
//			
//		}
		
		
	}

	/** Number of files to process. */
	private int numFiles;

	/**
	 * List containing all the objects that want to be notified during
	 * processing.
	 */
	private List<ProcessingEventListener> notificationListeners = new CopyOnWriteArrayList<ProcessingEventListener>();

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
	private boolean mustConvertToRGB = Utils.DEFAULT_COLOR_EXPANSION_BEHAVIOR;

	private int fileIndex=0;

	private ColorModel defaultCM = null;

	private CoordinateReferenceSystem defaultCRS = null;

	private byte[][] defaultPalette = null;

	private CatalogBuilderConfiguration runConfiguration;

	private ImageReaderSpi cachedReaderSPI;

	private ImageInputStreamSpi cachedStreamSPI;
	
	private List<PropertiesCollector> propertiesCollectors;

	private SampleModel defaultSM;

	private ReferencedEnvelope imposedBBox;

    private SimpleFeatureType indexSchema;

    private AbstractGridFormat cachedFormat;
	
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
	                        @SuppressWarnings("unused")
				final CatalogBuilderDirectoryWalker walker = new CatalogBuilderDirectoryWalker(indexingDirectories,finalFilter);
				
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
									FileFilterUtils.suffixFileFilter("qix"),
									FileFilterUtils.suffixFileFilter("lyr"),
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
		Utilities.ensureNonNull("root location", configuration.getRootMosaicDirectory());

                // look for and indexed.properties file
                final File parent = new File( configuration.getRootMosaicDirectory());
                final File indexerProperties = new File(parent, Utils.INDEXER_PROPERTIES);
                if (Utils.checkFileReadable(indexerProperties)) {
                        // load it and parse it
                        final Properties props = Utils.loadPropertiesFromURL(DataUtilities
                                        .fileToURL(indexerProperties));

                        // name
                        if (props.containsKey(Prop.NAME))
                                configuration.setIndexName(props.getProperty(Prop.NAME));

                        // absolute
                        if (props.containsKey(Prop.ABSOLUTE_PATH))
                                configuration.setAbsolute(Boolean.valueOf(props
                                                .getProperty(Prop.ABSOLUTE_PATH)));

                        // recursive
                        if (props.containsKey(Prop.RECURSIVE))
                                configuration.setRecursive(Boolean.valueOf(props
                                                .getProperty(Prop.RECURSIVE)));

                        // wildcard
                        if (props.containsKey(Prop.WILDCARD))
                                configuration.setWildcard(props.getProperty(Prop.WILDCARD));

                        // schema
                        if (props.containsKey(Prop.SCHEMA))
                                configuration.setSchema(props.getProperty(Prop.SCHEMA));

                        // time attr
                        if (props.containsKey(Prop.TIME_ATTRIBUTE))
                                configuration.setTimeAttribute(props.getProperty(Prop.TIME_ATTRIBUTE));
                        
                        // elevation attr
                        if (props.containsKey(Prop.ELEVATION_ATTRIBUTE))
                                configuration.setElevationAttribute(props.getProperty(Prop.ELEVATION_ATTRIBUTE));                       
        
                        
                        // imposed BBOX
                        if (props.containsKey(Prop.ENVELOPE2D))
                                configuration.setEnvelope2D(props.getProperty(Prop.ENVELOPE2D));        
                        
                        // imposed Pyramid Layout
                        if (props.containsKey(Prop.RESOLUTION_LEVELS))
                                configuration.setResolutionLevels(props.getProperty(Prop.RESOLUTION_LEVELS));                   

                        // collectors
                        if (props.containsKey(Prop.PROPERTY_COLLECTORS))
                                configuration.setPropertyCollectors(props.getProperty(Prop.PROPERTY_COLLECTORS));
                        
                        if (props.containsKey(Prop.CACHING))
                                configuration.setCaching(Boolean.valueOf(props.getProperty(Prop.CACHING)));
                }

		
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
	    notificationListeners.add(listener);
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
	    notificationListeners.remove(listener);

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
		String pcConfig = runConfiguration.getPropertyCollectors();
		if (pcConfig != null && pcConfig.length()>0){
		    pcConfig=pcConfig.trim();
			// load the SPI set
			final Set<PropertiesCollectorSPI> pcSPIs = PropertiesCollectorFinder.getPropertiesCollectorSPI();
			
			// parse the string
			final List<PropertiesCollector> pcs= new ArrayList<PropertiesCollector>();
			final String[] pcsDefs=pcConfig.trim().split(",");
			for (String pcDef: pcsDefs) {
				// parse this def as NAME[CONFIG_FILE](PROPERTY;PROPERTY;....;PROPERTY)
				final int squareLPos = pcDef.indexOf("[");
				final int squareRPos = pcDef.indexOf("]");
				final int squareRPosLast = pcDef.lastIndexOf("]");
				final int roundLPos = pcDef.indexOf("(");
				final int roundRPos = pcDef.indexOf(")");
				final int roundRPosLast = pcDef.lastIndexOf(")");				
				if (squareRPos != squareRPosLast){
                                    if(LOGGER.isLoggable(Level.INFO)){
                                        LOGGER.info("Skipping unparseable PropertyCollector definition: "+pcDef);
                                    }				    
				    continue;
				}
				if (squareLPos == -1 || squareRPos == -1){
                                    if(LOGGER.isLoggable(Level.INFO)){
                                        LOGGER.info("Skipping unparseable PropertyCollector definition: "+pcDef);
                                    }   
                                    continue;
                                }
				if (squareLPos == 0){
                                    if(LOGGER.isLoggable(Level.INFO)){
                                        LOGGER.info("Skipping unparseable PropertyCollector definition: "+pcDef);
                                    }   
                                    continue;
                                }
				
				if (roundRPos != roundRPosLast){
                                    if(LOGGER.isLoggable(Level.INFO)){
                                        LOGGER.info("Skipping unparseable PropertyCollector definition: "+pcDef);
                                    }   
                                    continue;
                                }
				if (roundLPos == -1 || roundRPos == -1){
                                    if(LOGGER.isLoggable(Level.INFO)){
                                        LOGGER.info("Skipping unparseable PropertyCollector definition: "+pcDef);
                                    }   
                                    continue;
                                }
				if (roundLPos == 0){
                                    if(LOGGER.isLoggable(Level.INFO)){
                                        LOGGER.info("Skipping unparseable PropertyCollector definition: "+pcDef);
                                    }   
                                    continue;
                                }
				if (roundLPos != squareRPos + 1){//]( or exit
                                    if(LOGGER.isLoggable(Level.INFO)){
                                        LOGGER.info("Skipping unparseable PropertyCollector definition: "+pcDef);
                                    }   
                                    continue;
                                }
				if (roundRPos != (pcDef.length() - 1)){// end with )
                                    if(LOGGER.isLoggable(Level.INFO)){
                                        LOGGER.info("Skipping unparseable PropertyCollector definition: "+pcDef);
                                    }   
                                    continue;
                                }	
				
				// name
				final String name=pcDef.substring(0,squareLPos);
				PropertiesCollectorSPI selectedSPI = null;
				for (PropertiesCollectorSPI spi:pcSPIs) {
					if (spi.isAvailable() && spi.getName().equalsIgnoreCase(name)) {
						selectedSPI=spi;
						break;
					}
				}
				if (selectedSPI == null){
                                    if(LOGGER.isLoggable(Level.INFO)){
                                        LOGGER.info("Unable to find a PropertyCollector for this definition: "+pcDef);
                                    }   
					continue;
				}
				
				// config
				final String config=squareLPos<squareRPos?pcDef.substring(squareLPos+1,squareRPos):"";
				final File configFile= new File(runConfiguration.getRootMosaicDirectory(),config+".properties");
				if (!Utils.checkFileReadable(configFile)){
                                    if(LOGGER.isLoggable(Level.INFO)){
                                        LOGGER.info("Unable to access the file for this PropertyCollector: "+configFile.getAbsolutePath());
                                    }   				    
                                    continue;
                                }
				// it is readable
				
				// property names
				final String propertyNames[] = pcDef.substring(roundLPos+1, roundRPos).split(",");
				
				// create the PropertiesCollector
				final PropertiesCollector pc = selectedSPI.create(configFile, Arrays.asList(propertyNames));
				if (pc != null) {
				    pcs.add(pc);
				} else {
				    if(LOGGER.isLoggable(Level.INFO)){
				        LOGGER.info("Unable to create PropertyCollector "+ pcDef +" from config file:"+configFile);
				    }
				}
				
			}
			this.propertiesCollectors=pcs;
		}
	}

	private void indexingPostamble(final boolean success) throws IOException {
		//close shapefile elements
		closeIndexObjects();
		
		if(success){
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
        			createPropertiesFiles();
        			
        			// processing information
        			fireEvent(Level.FINE, "Done!!!", 100);				
        		} else {
        			//	processing information
        			fireEvent(Level.FINE, "Nothing to process!!!", 100);
        		}
		} else {
		    // processing information
                    fireEvent(Level.FINE, "Canceled!!!", 100);
                    
                    //TODO remove all files created so far
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
		
		//
		// FINAL STEP
		//
		// CREATING GENERAL INFO FILE
		//
		
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
		
		if (cachedReaderSPI != null){
			// suggested spi
			properties.setProperty("SuggestedSPI", cachedReaderSPI.getClass().getName());
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
				if (outStream != null) {
					IOUtils.closeQuietly(outStream);
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

    public MosaicConfigurationBean getMosaicConfiguration() {
        return new MosaicConfigurationBean(mosaicConfiguration);
    }

}
