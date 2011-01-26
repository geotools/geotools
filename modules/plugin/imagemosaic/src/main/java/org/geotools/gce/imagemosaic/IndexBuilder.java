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
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
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
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.image.WorldImageFormat;
import org.geotools.gce.imagemosaic.ImageMosaicUtils.MosaicConfigurationBean;
import org.geotools.geometry.Envelope2D;
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
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * This class is in responsible for creating the index for a mosaic of images
 * that we want to tie together as a single coverage.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 */
@SuppressWarnings("unchecked")
final class IndexBuilder implements Runnable {
    
	/** Default Logger * */
	final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(IndexBuilder.class);
	
	/**
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
	 */
	static class IndexBuilderConfiguration{

		public IndexBuilderConfiguration() {
		}
		
		public IndexBuilderConfiguration(final IndexBuilderConfiguration that) {
			this.absolute=that.absolute;
			this.indexingDirectories=new ArrayList<String>(that.indexingDirectories);
			this.indexName=that.indexName;
			this.locationAttribute=that.locationAttribute;
			this.rootMosaicDirectory=that.rootMosaicDirectory;
			this.wildcardString=that.wildcardString;
			this.footprintManagement = that.footprintManagement;
		}

		public void setIndexingDirectories(List<String> indexingDirectories) {
			this.indexingDirectories = indexingDirectories;
		}


		private boolean absolute = ImageMosaicUtils.DEFAULT_PATH_BEHAVIOR;
		/**
		 * Index file name. Default is index.
		 */
		private String indexName = ImageMosaicUtils.DEFAULT_INDEX_NAME;
		private String locationAttribute = ImageMosaicUtils.DEFAULT_LOCATION_ATTRIBUTE;
		@Option(description="Root directory where to place the index file",mandatory=true,name="rootDirectory")
		private String rootMosaicDirectory;
		@Option(description="Wildcard to use for building the index of this mosaic",mandatory=false,name="wildcard")
		private String wildcardString = ImageMosaicUtils.DEFAULT_WILCARD;
		private List<String> indexingDirectories;
		private boolean footprintManagement = ImageMosaicUtils.DEFAULT_FOOTPRINT_MANAGEMENT;

		public boolean isFootprintManagement() {
            return footprintManagement;
        }

		public List<String> getIndexingDirectories() {
			return indexingDirectories;
		}

		/* (non-Javadoc)
		 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#getIndexName()
		 */
		public String getIndexName() {
			return indexName;
		}

		/* (non-Javadoc)
		 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#getLocationAttribute()
		 */
		public String getLocationAttribute() {
			return locationAttribute;
		}

		/* (non-Javadoc)
		 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#getRootMosaicDirectory()
		 */
		public String getRootMosaicDirectory() {
			return rootMosaicDirectory;
		}

		/* (non-Javadoc)
		 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#getWildcardString()
		 */
		public String getWildcardString() {
			return wildcardString;
		}

		/* (non-Javadoc)
		 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#isAbsolute()
		 */
		public boolean isAbsolute() {
			return absolute;
		}

		/* (non-Javadoc)
		 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#setAbsolute(boolean)
		 */
		public void setAbsolute(boolean absolute) {
			this.absolute = absolute;
		}

		/* (non-Javadoc)
		 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#setIndexName(java.lang.String)
		 */
		public void setIndexName(String indexName) {
			this.indexName = indexName;
		}

		/* (non-Javadoc)
		 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#setLocationAttribute(java.lang.String)
		 */
		public void setLocationAttribute(String locationAttribute) {
			this.locationAttribute = locationAttribute;
		}

		/* (non-Javadoc)
		 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#setRootMosaicDirectory(java.lang.String)
		 */
		public void setRootMosaicDirectory(final String rootMosaicDirectory) {
			 Utilities.ensureNonNull("rootMosaicDirectory", rootMosaicDirectory);
			 String testingDirectory = rootMosaicDirectory;
			 DataUtilities.checkDirectory(new File(testingDirectory));
			 this.rootMosaicDirectory=testingDirectory;

		}		


		/* (non-Javadoc)
		 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#setWildcardString(java.lang.String)
		 */
		public void setWildcardString(String wildcardString) {
			this.wildcardString = wildcardString;
		}

		@Override
		protected IndexBuilderConfiguration clone() throws CloneNotSupportedException {
			return new IndexBuilderConfiguration(this);
		}

		@Override
		public boolean equals(Object obj) {
			if(this==obj)
				return true;
			if(!(obj instanceof IndexBuilderConfiguration))
				return false;
			final IndexBuilderConfiguration that=(IndexBuilderConfiguration) obj;
			
			if(this.absolute!=that.absolute)
				return false;
			if(this.footprintManagement!=that.footprintManagement)
				 return false;
			if(!(this.indexName==null&&that.indexName==null)&&!this.indexName.equals(that.indexName))
				return false;	
			if(!(this.locationAttribute==null&&that.locationAttribute==null)&&!this.locationAttribute.equals(that.locationAttribute))
				return false;			
			if(!(this.rootMosaicDirectory==null&&that.rootMosaicDirectory==null)&&!this.rootMosaicDirectory.equals(that.rootMosaicDirectory))
				return false;		
			if(!Utilities.deepEquals(this.indexingDirectories, that.indexingDirectories))
				return false;
			
				
			return true;
		}

		@Override
		public int hashCode() {
			int seed=37;
			seed=Utilities.hash(absolute, seed);
			seed=Utilities.hash(footprintManagement, seed);
			seed=Utilities.hash(locationAttribute, seed);
			seed=Utilities.hash(indexName, seed);
			seed=Utilities.hash(wildcardString, seed);
			seed=Utilities.hash(rootMosaicDirectory, seed);
			seed=Utilities.hash(indexingDirectories, seed);
			return seed;
		}

		@Override
		public String toString() {
			final StringBuilder builder= new StringBuilder();
			builder.append("IndexBuilderConfiguration").append("\n");
			builder.append("wildcardString:\t\t\t").append(wildcardString).append("\n");
			builder.append("indexName:\t\t\t").append(indexName).append("\n");
			builder.append("absolute:\t\t\t").append(absolute).append("\n");
			builder.append("footprintManagement:\t\t\t").append(footprintManagement).append("\n");
			builder.append("locationAttribute:\t\t\t").append(locationAttribute).append("\n");
			builder.append("rootMosaicDirectory:\t\t\t").append(rootMosaicDirectory).append("\n");
			builder.append("indexingDirectories:\t\t\t").append(Utilities.deepToString(indexingDirectories)).append("\n");
			return builder.toString();
		}
		
	}
	static class CommandLineIndexBuilderRunner extends CommandLine {

		@Option(description="This index must use absolute or relative path",mandatory=false,name="absolute")
		private Boolean absolute;
		
		@Option(description="This index must handle footprint",mandatory=false,name="footprintManagement")
		private Boolean footprintManagement;
		
		@Option(description="Directories where to look for file to index",mandatory=true,name="indexingDirectories")
		private String indexingDirectoriesString;
		/**
		 * Index file name. Default is index.
		 */
		@Option(description="Name to use for the index of this mosaic",mandatory=false,name="index")
		private String indexName;
		
		@Option(description="Root directory where to place the index file",mandatory=true,name="rootDirectory")
		private String rootMosaicDirectory;
		
		@Option(description="Wildcard to use for building the index of this mosaic",mandatory=false,name="wildcard")
		private String wildcardString = ImageMosaicUtils.DEFAULT_WILCARD;

		@Option(description="Default location attribute for this index",mandatory=false,name="locationAttribute")
		private String locationAttribute = ImageMosaicUtils.DEFAULT_LOCATION_ATTRIBUTE;

		public CommandLineIndexBuilderRunner(String[] args) {
			super(args);
			if(this.absolute == null)
				this.absolute = ImageMosaicUtils.DEFAULT_PATH_BEHAVIOR;
			if(this.footprintManagement == null)
				this.footprintManagement = ImageMosaicUtils.DEFAULT_FOOTPRINT_MANAGEMENT;
			if(this.indexName == null)
				this.indexName = ImageMosaicUtils.DEFAULT_INDEX_NAME;
		}


		public static void main(String args[]){
			final CommandLineIndexBuilderRunner runner = new CommandLineIndexBuilderRunner(args);
			// prepare the configuration
			final IndexBuilderConfiguration configuration = new IndexBuilderConfiguration();
			configuration.absolute = runner.absolute;
			configuration.indexName = runner.indexName;
			configuration.footprintManagement = runner.footprintManagement;
			configuration.rootMosaicDirectory = runner.rootMosaicDirectory;
			configuration.wildcardString = runner.wildcardString;
			configuration.locationAttribute = runner.locationAttribute;
			
			final String directories = runner.indexingDirectoriesString;
			final String []dirs_ = directories.split(",");
			final List<String> dirs = new ArrayList<String>();
			for(String dir:dirs_)
				dirs.add(dir);
			configuration.indexingDirectories=dirs;
			
			//prepare and run the index builder
			final IndexBuilder builder= new IndexBuilder(configuration);		
			builder.run();
	  		
		}

	}
	
	static abstract class ProcessingEventListener implements EventListener {

		abstract void getNotification(final ProcessingEvent event);

		abstract void exceptionOccurred(final ExceptionEvent event);

	}

	/**
	 * @author Simone Giannecchini, GeoSolutions.
	 * 
	 */
	static class ProcessingEvent extends EventObject {

		/**
		 * 
		 */
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
	static final  class ExceptionEvent extends ProcessingEvent {

	    /**
		 * 
		 */
		private static final long serialVersionUID = 2272452028229922551L;
		private Exception exception;

	    public ExceptionEvent(Object source, String message, double percentage, Exception exception) {
	        super(source, message, percentage);
	        this.exception = exception;
	    }

	    public ExceptionEvent(Object source, Exception exception) {
	        super(source, ImageMosaicUtils.getMessageFromException(exception), -1);
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
	 * This class is responsible for walking through the files inside a directory
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
	final class MosaicDirectoryWalker  extends DirectoryWalker{

		private AbstractGridFormat cachedFormat;
		
		@Override
		protected void handleCancelled(File startDirectory, Collection results,
				CancelException cancel) throws IOException {
			// close things related to shapefiles
			closeShapeFileStores();
			
			//clean up objects
			
			
			super.handleCancelled(startDirectory, results, cancel);
		}		

		@Override
		protected boolean handleIsCancelled(File file, int depth,Collection results) throws IOException {			
			return IndexBuilder.this.stop&&super.handleIsCancelled(file, depth, results);
		}

		@Override
		protected void handleFile(final File fileBeingProcessed,final int depth,final Collection results)
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
			ImageInputStream inStream = null;
			ImageReader imageioReader = null;
			AbstractGridCoverage2DReader coverageReader=null;
			try {
				//
				// STEP 1
				// Getting an ImageIO reader for this coverage.
				//
				inStream = ImageIO.createImageInputStream(fileBeingProcessed);
				if(inStream==null)
				{
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
						}
						else
							imageioReader=null;
					}
					else
					{
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
				if(imageioReader==null)
				{
					// send a message
					fireEvent(Level.INFO,new StringBuilder("Skipped file ").append(fileBeingProcessed).append(":No ImageIO readeres avalaible.").toString(), ((fileIndex * 99.0) / numFiles));
					return;
				}


				//
				// STEP 2
				// Getting a coverage reader for this coverage.
				//
				final AbstractGridFormat format;
				if(cachedFormat==null)
				{
					format= (AbstractGridFormat) GridFormatFinder.findFormat(fileBeingProcessed);
				}
				else
					if(cachedFormat.accepts(fileBeingProcessed))
						format=cachedFormat;
					else
						format=new UnknownFormat();
				if ((format instanceof UnknownFormat)||format == null) {
					fireEvent(Level.INFO,new StringBuilder("Skipped file ").append(fileBeingProcessed).append(": File format is not supported.").toString(), ((fileIndex * 99.0) / numFiles));
					return;
				}
				cachedFormat=format;
				coverageReader = (AbstractGridCoverage2DReader) format.getReader(fileBeingProcessed);
				GeneralEnvelope envelope = (GeneralEnvelope) coverageReader.getOriginalEnvelope();
				CoordinateReferenceSystem actualCRS = coverageReader.getCrs();

				//
				// STEP 3
				// Get the type specifier for this image and the check that the
				// image has the correct sample model and color model.
				// If this is the first cycle of the loop we initialize
				// eveything.
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
					globalEnvelope = new GeneralEnvelope(envelope);

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
					try{
						inStream.reset();
					}catch (IOException e) {
						//close me and reopen me
						try{
							inStream.close();
						}catch (Throwable e1) {
							if(LOGGER.isLoggable(Level.FINE))
								LOGGER.log(Level.FINE,e1.getLocalizedMessage(),e1);
						}
						inStream= ImageIO.createImageInputStream(fileBeingProcessed);
					}
					
					//let's check if we got something now
					if(inStream==null)
					{
						//skip file
						fireEvent(Level.INFO,fileBeingProcessed+" has been skipped since we could not get a stream for it", ((fileIndex * 100.0) / numFiles));
						return;
					}
					imageioReader.setInput(inStream);
					int numberOfLevels = imageioReader.getNumImages(true);
					double[][] resolutionLevels = new double[2][numberOfLevels];
					double[] res = CoverageUtilities.getResolution((AffineTransform) coverageReader.getOriginalGridToWorld(PixelInCell.CELL_CORNER));
					resolutionLevels[0][0] = res[0];
					resolutionLevels[1][0] = res[1];

					// resolutions levels are computed using the raster space scale factors
					if (numberOfLevels > 1) {

						for (int k = 0; k < numberOfLevels; k++) {
							resolutionLevels[0][k] = resolutionLevels[0][0]*coverageReader.getOriginalGridRange().getSpan(0)/(1.0*imageioReader.getWidth(k));
							resolutionLevels[1][k] = resolutionLevels[1][0]*coverageReader.getOriginalGridRange().getSpan(1)/(1.0*imageioReader.getHeight(k));
						}
					}
					
					mosaicConfiguration.setLevelsNum(numberOfLevels);
					mosaicConfiguration.setLevels(resolutionLevels);

					// /////////////////////////////////////////////////////////////////////
					//
					// creating the schema
					//
					// /////////////////////////////////////////////////////////////////////
					final SimpleFeatureTypeBuilder featureBuilder = new SimpleFeatureTypeBuilder();
					featureBuilder.setName("mosaic_index");
					featureBuilder.setNamespaceURI("http://www.geo-solutions.it/");
					featureBuilder.add(runConfiguration.getLocationAttribute(), String.class);
					featureBuilder.add("the_geom", Polygon.class,actualCRS);
					featureBuilder.setDefaultGeometry("the_geom");
					final SimpleFeatureType simpleFeatureType = featureBuilder.buildFeatureType();
					// create the schema for the new shape file
					store.createSchema(simpleFeatureType);
					// get a feature writer
					fw = store.getFeatureWriter(simpleFeatureType.getTypeName(),Transaction.AUTO_COMMIT);
				} else {
					// ////////////////////////////////////////////////////////
					// 
					// comparing ColorModel
					// comparing SampeModel
					// comparing CRSs
					// ////////////////////////////////////////////////////////
					globalEnvelope.add(envelope);
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
					
					// defaultCM.getNumComponents()==actualCM.getNumComponents()&&
					// defaultCM.getClass().equals(actualCM.getClass())
					// && defaultSM.getNumBands() == actualSM
					// .getNumBands()
					// && defaultSM.getDataType() == actualSM
					// .getDataType() &&
					//
//						 if (skipFeature)
//						 LOGGER
//						 .warning(new StringBuilder("Skipping image ")
//						 .append(files.get(fileIndex))
//						 .append(
//						 " because cm or sm does not match.")
//						 .toString());
//						 double[] res = getResolution(envelope, new Rectangle(r.getWidth(0), r.getHeight(0)), defaultCRS);
//						 if (Math.abs((resX - res[0]) / resX) > EPS || Math.abs(resY - res[1]) > EPS) {
//							 LOGGER.warning(new StringBuilder("Skipping image").append( files.get(fileIndex)).append(" because resolutions does not match.")
//						 .toString());
//						 	skipFeature = true;
//						 }
				}

				// ////////////////////////////////////////////////////////
				//
				// STEP 4
				//
				// create and store features
				//
				// ////////////////////////////////////////////////////////
				final SimpleFeature feature = fw.next();
				feature.setAttribute(1, geomFactory.toGeometry(new ReferencedEnvelope((Envelope) envelope)));
				final String location = prepareLocation(fileBeingProcessed);
				feature.setAttribute(0, location);
				fw.write();

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
					if (inStream != null)
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
			if(runConfiguration.absolute)
				return fileBeingProcessed.getAbsolutePath();
			
			// relative
			String path=FilenameUtils.normalize(fileBeingProcessed.getAbsolutePath());
			path=path.substring(runConfiguration.rootMosaicDirectory.length());
			String relative = new File(runConfiguration.rootMosaicDirectory).toURI().relativize(new File(fileBeingProcessed.getAbsolutePath()).toURI()).getPath();
			return relative;
			
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

		public MosaicDirectoryWalker(final File root,final FileFilter filter) throws IOException {
			super(filter,Integer.MAX_VALUE);
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

	private ShapefileDataStore store;
	
	private ShapefileDataStore footprintStore;

	private FeatureWriter<SimpleFeatureType, SimpleFeature> fw = null;
	
	private int numberOfProcessedFiles;

	/**
	 * This field will tell the plugin if it must do a conversion of color from
	 * the original index color model to an RGB color model. This happens f the
	 * original images uses different color maps between each other making for
	 * us impossible to reuse it for the mosaic.
	 */
	private boolean mustConvertToRGB = false;

	private GeneralEnvelope globalEnvelope = null;

	private int fileIndex=0;
	
	/** A Map containing footprints represented as <location, Geometry> pairs
	 * where location refers to a mosaic granule location.   
	 */
	private Map<String,Geometry> footprintsLocationGeometryMap;

	private File footprintShapeFile;
        
	private File footprintSummaryFile;
        
	private ColorModel defaultCM = null;
	
	private SampleModel defaultSM;

	private CoordinateReferenceSystem defaultCRS = null;

	private byte[][] defaultPalette = null;

	private IndexBuilderConfiguration runConfiguration;

	private ImageReaderSpi cachedSPI;


	/* (non-Javadoc)
	 * @see org.geotools.gce.imagemosaic.JMXIndexBuilderMBean#run()
	 */
	public void run() {
		
		
		try {

			//
			// creating the file filters for scanning for files to check and index
			//
			final IOFileFilter finalFilter = createIndexingFilter(new WildcardFileFilter(runConfiguration.wildcardString,IOCase.INSENSITIVE));
			
			//TODO we might want to remove this in the future for performance
			numFiles=0;
			for(String indexingDirectory:runConfiguration.indexingDirectories){
				final File directoryToScan = new File(indexingDirectory);
				final Collection files = FileUtils.listFiles(
						directoryToScan,
						finalFilter, 
						TrueFileFilter.INSTANCE);
				numFiles += files.size();
			}
			//
			// walk over the files that have filtered out
			//
			if(numFiles>0) {
				indexingPreamble();
				for(String indexingDirectory:runConfiguration.indexingDirectories){
					@SuppressWarnings("unused")
					final MosaicDirectoryWalker walker = new MosaicDirectoryWalker(new File(indexingDirectory),finalFilter);
				}
				indexingPostamble();
			}
			
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE,e.getLocalizedMessage(),e);
		} 

	}
	
	/**
	 * @return
	 */
	static IOFileFilter createIndexingFilter(final IOFileFilter specialWildCardFileFilter) {
		IOFileFilter dirFilter = 
		    FileFilterUtils.andFileFilter(FileFilterUtils.directoryFileFilter(),HiddenFileFilter.VISIBLE);
		IOFileFilter fileFilter= FileFilterUtils.asFileFilter(DataUtilities.excludeFilters(
			FileFilterUtils.makeSVNAware(
					FileFilterUtils.makeFileOnly(
							FileFilterUtils.andFileFilter(
									specialWildCardFileFilter,HiddenFileFilter.VISIBLE))), 
									FileFilterUtils.suffixFileFilter("shp"),
									FileFilterUtils.suffixFileFilter("dbf"),
									FileFilterUtils.suffixFileFilter("fpt"),
									FileFilterUtils.suffixFileFilter("shx"),
									FileFilterUtils.prefixFileFilter(FootprintUtils.FOOTPRINT_PREFIX), 
									FileFilterUtils.suffixFileFilter("prj"), 
									FileFilterUtils.nameFileFilter("error.txt"),
									FileFilterUtils.nameFileFilter("error.txt.lck"),
									FileFilterUtils.suffixFileFilter("properties"),
									FileFilterUtils.suffixFileFilter("svn-base")
									));

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
		
		//mrsid
		fileFilter=FileFilterUtils.andFileFilter(
				fileFilter, 
				FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("sdw")));	
		//aux
		fileFilter=FileFilterUtils.andFileFilter(
				fileFilter, 
				FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("aux")));	
		//aux
		fileFilter=FileFilterUtils.andFileFilter(
				fileFilter, 
				FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("wld")));				
					
		
		final IOFileFilter finalFilter=	FileFilterUtils.orFileFilter(dirFilter, fileFilter);
		return finalFilter;
	}

	/**
	 * Default constructor
	 * @throws  
	 * @throws IllegalArgumentException 
	 */
	public IndexBuilder(final IndexBuilderConfiguration configuration)  {
		Utilities.ensureNonNull("runConfiguration", configuration);
		//check parameters
		if(configuration.indexingDirectories==null||configuration.indexingDirectories.size()<=0)
			throw new IllegalArgumentException("Indexing directories are empty");
		final List<String> directories= new ArrayList<String>();
		for(String dir:configuration.indexingDirectories) {
			directories.add(FilenameUtils.normalize(DataUtilities.checkDirectory(new File(dir)).getPath()));
		}
		configuration.indexingDirectories=directories;
		
		if(configuration.indexName==null||configuration.indexName.length()==0)
			throw new IllegalArgumentException("Index name cannot be empty");
		
		if(configuration.rootMosaicDirectory==null||configuration.rootMosaicDirectory.length()==0)
			throw new IllegalArgumentException("RootMosaicDirectory name cannot be empty");
		configuration.rootMosaicDirectory=DataUtilities.checkDirectory(new File(configuration.rootMosaicDirectory)).getAbsolutePath();
		configuration.rootMosaicDirectory=FilenameUtils.normalize(configuration.rootMosaicDirectory);
		
		if(configuration.wildcardString==null||configuration.wildcardString.length()==0)
			throw new IllegalArgumentException("WildcardString name cannot be empty");		
		this.runConfiguration = new IndexBuilderConfiguration(configuration);

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
	 * {@link IndexBuilder} or bad things can happen. If it is running, please
	 * stop it first.
	 */
	public  void reset() {
		removeAllProcessingEventListeners();
		// clear stop
		stop=false;
	
		closeShapeFileStores();
		
		//clear other stuff
		globalEnvelope=null;
		defaultCM=null;
		defaultSM=null;
		defaultCRS=null;
		defaultPalette=null;
		fileIndex=0;
		numberOfProcessedFiles=0;
		
		footprintShapeFile = null;
		footprintSummaryFile = null;
		if (footprintsLocationGeometryMap != null && !footprintsLocationGeometryMap.isEmpty())
		    footprintsLocationGeometryMap.clear();
		footprintsLocationGeometryMap = null;
		
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
			fireException(ImageMosaicUtils.getMessageFromException(ex), -1, ex);
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

    private void indexingPreamble() throws IOException {

        //
        // declaring a precision model to adhere the java double type
        // precision
        //
        final PrecisionModel precMod = new PrecisionModel(PrecisionModel.FLOATING);
        geomFactory = new GeometryFactory(precMod);
        final boolean handleFootprint = !ImageMosaicUtils.IGNORE_FOOTPRINT || runConfiguration.isFootprintManagement();
        try {
            store = new ShapefileDataStore(new File(runConfiguration.rootMosaicDirectory, runConfiguration.indexName + ".shp").toURI().toURL());
            if (handleFootprint && footprintShapeFile == null) {
                footprintShapeFile = FootprintUtils.searchFootprint(runConfiguration.rootMosaicDirectory);
            }
            if (handleFootprint && (footprintsLocationGeometryMap == null || footprintsLocationGeometryMap.isEmpty()) 
                    && footprintShapeFile != null && footprintShapeFile.exists() && footprintShapeFile.canRead()) {
                footprintsLocationGeometryMap = new HashMap<String, Geometry>();
                footprintStore = new ShapefileDataStore(DataUtilities.fileToURL(footprintShapeFile));
                footprintSummaryFile = new File(runConfiguration.rootMosaicDirectory, runConfiguration.indexName + FootprintUtils.FOOTPRINT_EXT);
                FootprintUtils.initFootprintsLocationGeometryMap(footprintStore, footprintsLocationGeometryMap);
            }
        } catch (MalformedURLException ex) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            fireException(ex);
            return;
        }

        //
        // creating a mosaic runConfiguration bean to store the properties file
        // elements
        //
        mosaicConfiguration = new ImageMosaicUtils.MosaicConfigurationBean();

    }

    private void indexingPostamble() throws IOException {
        try {
            if (fw != null)
                fw.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        fw = null;

        // close shapefile elements
        closeShapeFileStores();

        // create the sample image file
        createSampleImage();

        try {
            if (footprintSummaryFile != null) {
                FootprintUtils.writeFootprintSummary(footprintSummaryFile,new File(runConfiguration.rootMosaicDirectory, runConfiguration.indexName + ".shp"), footprintsLocationGeometryMap);
            }
        } catch (Throwable e) {
            // ignore exception
            if (LOGGER.isLoggable(Level.FINEST))
                LOGGER.log(Level.FINEST, e.getLocalizedMessage(), e);
        }
        
        // complete initialization of mosaic configuration
        if (numberOfProcessedFiles > 0) {
            mosaicConfiguration.setName(runConfiguration.indexName);
            mosaicConfiguration.setExpandToRGB(mustConvertToRGB);
            mosaicConfiguration.setAbsolutePath(runConfiguration.absolute);
            mosaicConfiguration.setLocationAttribute(runConfiguration.locationAttribute);
            mosaicConfiguration.setEnvelope2D(new Envelope2D(globalEnvelope));
            mosaicConfiguration.setFootprintManagement(runConfiguration.footprintManagement);
            createPropertiesFiles();

            // processing information
            fireEvent(Level.FINE, "Done!!!", 100);
        } else {
            // processing information
            fireEvent(Level.FINE, "Nothing to process!!!", 100);
        }
    }

    /**
     * Store a sample image from which we can derive the default SM and CM
     */
    private void createSampleImage() {
        // create a sample image to store SM and CM
        if (defaultCM != null && defaultSM != null) {

            // sample image file
            final File sampleImageFile = new File(runConfiguration.getRootMosaicDirectory() + "/sample_image");
            try {
                ImageMosaicUtils.storeSampleImage(sampleImageFile, defaultSM, defaultCM);
            } catch (IOException e) {
                fireEvent(Level.SEVERE, e.getLocalizedMessage(), 0);
            }
        }

    }

    /**
     * Attempt to close and dispose previously opened shapefile datastores
     */
    private void closeShapeFileStores() {
        try {
            if (store != null)
                store.dispose();
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        store = null;

        try {
            if (footprintStore != null)
                footprintStore.dispose();
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        footprintStore = null;
    }

	/**
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
		properties.setProperty("Envelope2D", new StringBuilder(Double.toString(globalEnvelope.getMinimum(0))).append(",").append(
				Double.toString(globalEnvelope.getMinimum(1))).append(" ")
				.append(Double.toString(globalEnvelope.getMaximum(0)))
				.append(",").append(Double.toString(globalEnvelope.getMaximum(1)))
				.toString());
		
		
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
		properties.setProperty("Name", runConfiguration.indexName);
		properties.setProperty("ExpandToRGB", Boolean.toString(mustConvertToRGB));
		
		if(cachedSPI!=null){
			// suggested spi
			properties.setProperty("SuggestedSPI", cachedSPI.getClass().getName());
		}
		properties.setProperty("FootprintManagement", Boolean.toString(runConfiguration.isFootprintManagement()));
		
		OutputStream outStream=null;
		try {
			outStream=new BufferedOutputStream(new FileOutputStream(runConfiguration.rootMosaicDirectory + "/" + runConfiguration.indexName + ".properties"));
			properties.store(outStream, "-Automagically created-");
		} catch (FileNotFoundException e) {
			fireEvent(Level.SEVERE,e.getLocalizedMessage(), 0);
		} catch (IOException e) {
			fireEvent(Level.SEVERE,e.getLocalizedMessage(), 0);
		}
		finally{
			try{
				if (outStream!=null)
					outStream.close();
			} catch (Throwable e) {
	
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
			}
		}
	
	}

	public void dispose() {
		reset();
		
	}

}
