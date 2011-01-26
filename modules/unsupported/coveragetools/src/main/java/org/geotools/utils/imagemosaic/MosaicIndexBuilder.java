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
package org.geotools.utils.imagemosaic;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.cli2.Option;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.DataSourceException;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.resources.CRSUtilities;
import org.geotools.utils.progress.BaseArgumentsManager;
import org.geotools.utils.progress.ExceptionEvent;
import org.geotools.utils.progress.ProcessingEvent;
import org.geotools.utils.progress.ProcessingEventListener;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * This class is in responsible for creating the index for a mosaic of images
 * that we want to tie together as a single coverage.
 * 
 * <p>
 * To get instructions on how to run the tool just run it without any argument
 * and a nice and clean help message will be printed to the command line.
 * 
 * <p>
 * Anyway an example of a suitable list of argumentBuilder can be seen here
 * below:
 * 
 * <p>
 * -s H:\\work\\data\\merano_aime -w *.tif -name merano -abs
 * <p>
 * where:
 * <ol>
 * <li>-s H:\\work\\data\\merano_aime is the source directory</li>
 * <li>-w *.tif is he wildcard for the files to process</li>
 * <li>-name merano sets the name for the output shape</li>
 * <li>-abs asks the tool to use absolute paths instead of relative</li>
 * </ol>
 * 
 * 
 * <p>
 * It is worth to point out that this tool comes as a command line tool but it
 * has been built with GUI in mind . It has the capability to register
 * {@link ProcessingEventListener} object that receive notifications about what
 * is going on. Moreover it delegates all the computations to an external
 * thread, hence we can stop the tool in the middle of processing with no so
 * many concerns (hopefully :-) ).
 * <p>
 * 
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 * @author Blaz Repnik
 *
 * @source $URL$
 * @version 0.3
 * 
 */
public class MosaicIndexBuilder extends BaseArgumentsManager implements
		Runnable, ProcessingEventListener {

	/** Default Logger * */
	private final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger("it.geosolutions.utils.imagemosaic");

	/** Program Version */
	private final static String VERSION = "0.3";

	private final static String NAME = "MosaicIndexBuilder";

	private final Option locationOpt;

	private final Option nameOpt;

	private final Option relativePathOpt;

	private final Option wildcardOpt;

	private String locationPath;

	/**
	 * Number of resolution levels for the coverages.
	 */
	private int numberOfLevels;

	/**
	 * Resolutions levels.
	 */
	private double[][] resolutionLevels;

	/** Number of files to process. */
	private int numFiles;

	private String wildcardString = "*.*";

	/**
	 * Index file name. Default is index.
	 */
	private String indexName = "index";

	/**
	 * This field will tell the plugin if it must do a conversion of color from
	 * the original index color model to an RGB color model. This happens f the
	 * original images uses different color maps between each other making for
	 * us impossible to reuse it for the mosaic.
	 */
	private boolean mustConvertToRGB = false;

	private ColorModel actualCM = null;

	private ColorModel defaultCM = null;

	private SampleModel defaultSM = null;

	private SampleModel actualSM = null;

	private GeneralEnvelope globEnvelope = null;

	private GeneralEnvelope envelope = null;

	private byte[][] defaultPalette = null;

	private CoordinateReferenceSystem defaultCRS = null;

	private CoordinateReferenceSystem actualCRS = null;

	private boolean absolute = false;

	/**
	 * Recurses the directory tree and returns valid files.
	 */
	private void recurse(List<File> allFiles, String locationPath) {
		final File dir = new File(locationPath);
		final FileFilter fileFilter = new WildcardFileFilter(wildcardString);
		final File[] files = dir.listFiles(fileFilter);
		final File[] dirs = dir
				.listFiles((FileFilter) DirectoryFileFilter.INSTANCE);

		for (int i = 0; i < files.length; i++) {
			allFiles.add(files[i]);
		}

		for (int i = 0; i < dirs.length; i++) {
			recurse(allFiles, new StringBuilder(locationPath).append('/')
					.append(dirs[i].getName()).toString());
		}
	}

	/**
	 * Main thread for the mosaic index builder.
	 */
	public void run() {

		// /////////////////////////////////////////////////////////////////////
		//
		// CREATING INDEX FILE
		//
		// /////////////////////////////////////////////////////////////////////

		// /////////////////////////////////////////////////////////////////////
		//
		// Create a file handler that write log record to a file called
		// my.log
		//
		// /////////////////////////////////////////////////////////////////////
		FileHandler handler=null;
		try {
			boolean append = true;
			handler = new FileHandler(new StringBuffer(locationPath).append(
					"/error.txt").toString(), append);
			handler.setLevel(Level.SEVERE);
			// Add to the desired logger
			LOGGER.addHandler(handler);
			

		// /////////////////////////////////////////////////////////////////////
		//
		// Create a set of file names that have to be skipped since these are
		// our metadata files
		//
		// /////////////////////////////////////////////////////////////////////
		final Set<String> skipFiles = new HashSet<String>(Arrays
				.asList(new String[] { indexName + ".shp", indexName + ".dbf",
						indexName + ".shx", indexName + ".prj", "error.txt",
						"error.txt.lck", indexName + ".properties" }));

		// /////////////////////////////////////////////////////////////////////
		//
		// Creating temp vars
		//
		// /////////////////////////////////////////////////////////////////////
		ShapefileDataStore index = null;
		Transaction t = new DefaultTransaction();
		// declaring a preciosion model to adhere the java double type
		// precision
		PrecisionModel precMod = new PrecisionModel(PrecisionModel.FLOATING);
		GeometryFactory geomFactory = new GeometryFactory(precMod);
		try {
				index = new ShapefileDataStore(new File(locationPath + File.separator
					+ indexName + ".shp").toURI().toURL());
		} catch (MalformedURLException ex) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			fireException(ex);
			return;
		}

		final List<File> files = new ArrayList<File>();
		recurse(files, locationPath);

		// /////////////////////////////////////////////////////////////////////
		//
		// Cycling over the files that have filtered out
		//
		// /////////////////////////////////////////////////////////////////////
		numFiles = files.size();
		String validFileName = null;
		final Iterator<File> filesIt = files.iterator();
		FeatureWriter<SimpleFeatureType, SimpleFeature> fw = null;
		boolean doneSomething = false;
		for (int i = 0; i < numFiles; i++) {

			
			StringBuffer message;
			// //
			//
			// Check that this file is actually good to go
			//
			// //			
			final File fileBeingProcessed = ((File) filesIt.next());
			if(!fileBeingProcessed.exists()||!fileBeingProcessed.canRead()||!fileBeingProcessed.isFile())
			{
				// send a message
				message = new StringBuffer("Skipped file ").append(
						files.get(i)).append(
						" snce it seems invalid.");
				if (LOGGER.isLoggable(Level.INFO))
					LOGGER.info(message.toString());
				fireEvent(message.toString(), ((i * 99.0) / numFiles));
				continue;
			}

			// //
			//
			// Anyone has asked us to stop?
			//
			// //
			if (getStopThread()) {
				message = new StringBuffer("Stopping requested at file  ")
						.append(i).append(" of ").append(numFiles).append(
								" files");
				if (LOGGER.isLoggable(Level.FINE)) {
					LOGGER.fine(message.toString());
				}
				fireEvent(message.toString(), ((i * 100.0) / numFiles));
				return;
			} // replacing chars on input path
			try {
				validFileName = fileBeingProcessed.getCanonicalPath();
			} catch (IOException e1) {
				fireException(e1);
				return;
			}
			validFileName = validFileName.replace('\\', '/');
			validFileName = validFileName.substring(locationPath.length() + 1,
					fileBeingProcessed.getAbsolutePath().length());
			if (skipFiles.contains(validFileName))
				continue;
			message = new StringBuffer("Now indexing file ")
					.append(validFileName);

			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine(message.toString());
			}
			fireEvent(message.toString(), ((i * 100.0) / numFiles));
			try {
				// ////////////////////////////////////////////////////////
				//
				//
				// STEP 1
				// Getting an ImageIO reader for this coverage.
				//
				//
				// ////////////////////////////////////////////////////////
				ImageInputStream inStream = ImageIO
						.createImageInputStream(fileBeingProcessed);
				if(inStream==null)
				{
					if(LOGGER.isLoggable(Level.SEVERE))
						LOGGER.severe(fileBeingProcessed+" has been skipped since we could not get a stream for it");
					continue;
				}
				inStream.mark();
				final Iterator<ImageReader> it = ImageIO.getImageReaders(inStream);
				ImageReader r = null;
				if (it.hasNext()) {
					r = (ImageReader) it.next();
					r.setInput(inStream);
				} else {
					// release resources
					try {
						inStream.close();
					} catch (Exception e) {
						// ignore exception
					}
//					try {
//						r.dispose();
//					} catch (Exception e) {
//						// ignore exception
//					}

					// send a message
					message = new StringBuffer("Skipped file ").append(
							files.get(i)).append(
							":No ImageIO readeres avalaible.");
					if (LOGGER.isLoggable(Level.INFO))
						LOGGER.info(message.toString());
					fireEvent(message.toString(), ((i * 99.0) / numFiles));
					continue;
				}

				// ////////////////////////////////////////////////////////
				//
				// STEP 2
				// Getting a coverage reader for this coverage.
				//
				// ////////////////////////////////////////////////////////
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER
							.fine(new StringBuffer("Getting a reader")
									.toString());
				final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder
						.findFormat(files.get(i));
				if (format == null||!format.accepts(files.get(i))) {
					// release resources
					try {
						inStream.close();
					} catch (Exception e) {
						// ignore exception
					}
					try {
						r.dispose();
					} catch (Exception e) {
						// ignore exception
					}

					message = new StringBuffer("Skipped file ").append(
							files.get(i)).append(
							": File format is not supported.");
					if (LOGGER.isLoggable(Level.INFO))
						LOGGER.info(message.toString());
					fireEvent(message.toString(), ((i * 99.0) / numFiles));
					continue;
				}
				final AbstractGridCoverage2DReader reader = (AbstractGridCoverage2DReader) format
						.getReader(files.get(i));
				envelope = (GeneralEnvelope) reader.getOriginalEnvelope();
				actualCRS = reader.getCrs();

				// /////////////////////////////////////////////////////////////////////
				//
				// STEP 3
				// Get the type specifier for this image and the check that the
				// image has the correct sample model and color model.
				// If this is the first cycle of the loop we initialize
				// eveything.
				//
				// /////////////////////////////////////////////////////////////////////
				final ImageTypeSpecifier its = ((ImageTypeSpecifier) r.getImageTypes(0).next());
				boolean skipFeature = false;
				if (globEnvelope == null) {
					// /////////////////////////////////////////////////////////////////////
					//
					// at the first step we initialize everything that we will
					// reuse afterwards starting with color models, sample
					// models, crs, etc....
					//
					// /////////////////////////////////////////////////////////////////////
					defaultCM = its.getColorModel();
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
					defaultSM = its.getSampleModel();
					defaultCRS = actualCRS;
					globEnvelope = new GeneralEnvelope(envelope);

					// /////////////////////////////////////////////////////////////////////
					//
					// getting information about resolution
					//
					// /////////////////////////////////////////////////////////////////////

					// //
					//
					// get the dimension of the hr image and build the model
					// as well as
					// computing the resolution
					// //
					// resetting reader and recreating stream, turnaround for a
					// strange imageio bug
					r.reset();
					try{
						inStream.reset();
					}catch (IOException e) {
						inStream= ImageIO.createImageInputStream(fileBeingProcessed);
					}
					//let's check if we got something now
					if(inStream==null)
					{
						//skip file
						if(LOGGER.isLoggable(Level.WARNING))
							LOGGER.warning("Skipping file "+fileBeingProcessed.toString());
						continue;
					}
					r.setInput(inStream);
					numberOfLevels = r.getNumImages(true);
					resolutionLevels = new double[2][numberOfLevels];
					double[] res = getResolution(envelope, new Rectangle(r
							.getWidth(0), r.getHeight(0)), defaultCRS);
					resolutionLevels[0][0] = res[0];
					resolutionLevels[1][0] = res[1];

					// resolutions levels
					if (numberOfLevels > 1) {

						for (int k = 0; k < numberOfLevels; k++) {
							res = getResolution(envelope, new Rectangle(r
									.getWidth(k), r.getHeight(k)), defaultCRS);
							resolutionLevels[0][k] = res[0];
							resolutionLevels[1][k] = res[1];
						}
					}

					// /////////////////////////////////////////////////////////////////////
					//
					// creating the schema
					//
					// /////////////////////////////////////////////////////////////////////
					final SimpleFeatureTypeBuilder featureBuilder = new SimpleFeatureTypeBuilder();
					featureBuilder.setName("Flag");
					featureBuilder.setNamespaceURI("http://www.geo-solutions.it/");
					featureBuilder.add("location", String.class);
					featureBuilder.add("the_geom", Polygon.class,this.actualCRS);
					featureBuilder.setDefaultGeometry("the_geom");
					final SimpleFeatureType simpleFeatureType = featureBuilder.buildFeatureType();
					// create the schema for the new shape file
					index.createSchema(simpleFeatureType);

					// get a feature writer
					fw = index.getFeatureWriter( t);
				} else {
					// ////////////////////////////////////////////////////////
					// 
					// comparing ColorModel
					// comparing SampeModel
					// comparing CRSs
					// ////////////////////////////////////////////////////////
					globEnvelope.add(envelope);
					actualCM = its.getColorModel();
					actualSM = its.getSampleModel();
					skipFeature = (i > 0 ? !(CRS.equalsIgnoreMetadata(
							defaultCRS, actualCRS)) : false);
					if (skipFeature)
						LOGGER.warning(new StringBuffer("Skipping image ")
								.append(files.get(i)).append(
										" because CRSs do not match.")
								.toString());
					skipFeature = checkColorModels(defaultCM, defaultPalette,
							actualCM);
					if (skipFeature)
						LOGGER.warning(new StringBuffer("Skipping image ")
								.append(files.get(i)).append(
										" because color models do not match.")
								.toString());
					// defaultCM.getNumComponents()==actualCM.getNumComponents()&&
					// defaultCM.getClass().equals(actualCM.getClass())
					// && defaultSM.getNumBands() == actualSM
					// .getNumBands()
					// && defaultSM.getDataType() == actualSM
					// .getDataType() &&
					//
					// if (skipFeature)
					// LOGGER
					// .warning(new StringBuffer("Skipping image ")
					// .append(files.get(i))
					// .append(
					// " because cm or sm does not match.")
					// .toString());
					// res = getResolution(envelope, new
					// Rectangle(r.getWidth(0),
					// r.getHeight(0)), defaultCRS);
					// if (Math.abs((resX - res[0]) / resX) > EPS
					// || Math.abs(resY - res[1]) > EPS) {
					// LOGGER.warning(new StringBuffer("Skipping image
					// ").append(
					// files.get(i)).append(
					// " because resolutions does not match.")
					// .toString());
					// skipFeature = true;
					// }
				}

				// ////////////////////////////////////////////////////////
				//
				// STEP 4
				//
				// create and store features
				//
				// ////////////////////////////////////////////////////////
				if (!skipFeature) {

					final SimpleFeature feature = fw.next();
					feature.setAttribute(1, geomFactory
							.toGeometry(new ReferencedEnvelope(
									(Envelope) envelope)));
					feature.setAttribute(0, absolute ? new StringBuilder(
							this.locationPath).append(File.separatorChar)
							.append(validFileName).toString() : validFileName);
					fw.write();

					message = new StringBuffer("Done with file ").append(files
							.get(i));
					if (LOGGER.isLoggable(Level.FINE)) {
						LOGGER.fine(message.toString());
					}
					message.append('\n');
					fireEvent(message.toString(), (((i + 1) * 99.0) / numFiles));
					doneSomething = true;
				} else
					skipFeature = false;

				// ////////////////////////////////////////////////////////
				//
				// STEP 5
				//
				// release resources
				//
				// ////////////////////////////////////////////////////////
				try {
					inStream.close();
				} catch (Exception e) {
					// ignore exception
				}
				try {
					r.dispose();
				} catch (Exception e) {
					// ignore exception
				}
				// release resources
				reader.dispose();

			} catch (IOException e) {
				fireException(e);
				break;
			} catch (ArrayIndexOutOfBoundsException e) {
				fireException(e);
				break;
			}

		}
		try {
			if (fw != null)
				fw.close();
			t.commit();
			t.close();
			index.dispose();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		createPropertiesFiles(globEnvelope, doneSomething);
		} catch (SecurityException el) {
			fireException(el);
			return;
		} catch (IOException el) {
			fireException(el);
			return;
		}
		finally{
			try{
				if(handler!=null)
					handler.close();
			}catch (Throwable e) {
				// ignore
			}	
			
		}

	}

	/**
	 * @param globEnvelope
	 * @param doneSomething
	 */
	private void createPropertiesFiles(GeneralEnvelope globEnvelope,
			boolean doneSomething) {
		StringBuffer message;
		if (numFiles > 0 && doneSomething) {
			// /////////////////////////////////////////////////////////////////////
			//
			// FINAL STEP
			//
			// CREATING GENERAL INFO FILE
			//
			// /////////////////////////////////////////////////////////////////////
			message = new StringBuffer("Creating final properties file ");
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine(message.toString());
			}
			fireEvent(message.toString(), 99.9);

			// envelope
			final Properties properties = new Properties();
			properties.setProperty("AbsolutePath", Boolean.toString(absolute));
			properties.setProperty("NumFiles", Integer.toString(numFiles));
			properties.setProperty("Envelope2D", new StringBuffer(Double
					.toString(globEnvelope.getMinimum(0))).append(",").append(
					Double.toString(globEnvelope.getMinimum(1))).append(" ")
					.append(Double.toString(globEnvelope.getMaximum(0)))
					.append(",").append(
							Double.toString(globEnvelope.getMaximum(1)))
					.toString());
			properties.setProperty("LevelsNum", Integer
					.toString(numberOfLevels));
			final StringBuffer levels = new StringBuffer();
			for (int k = 0; k < numberOfLevels; k++) {
				levels.append(Double.toString(resolutionLevels[0][k])).append(
						",").append(Double.toString(resolutionLevels[1][k]));
				if (k < numberOfLevels - 1)
					levels.append(" ");
			}
			properties.setProperty("Levels", levels.toString());
			properties.setProperty("Name", indexName);
			properties.setProperty("ExpandToRGB", Boolean
					.toString(mustConvertToRGB));
			OutputStream outStream=null;
			try {
				outStream=new BufferedOutputStream(new FileOutputStream(locationPath + "/" + indexName + ".properties"));
				properties.store(outStream, "");
			} catch (FileNotFoundException e) {
				fireEvent(e.getLocalizedMessage(), 0);
			} catch (IOException e) {
				fireEvent(e.getLocalizedMessage(), 0);
			}
			finally{
				try{
					if(outStream!=null)
						outStream.close();
				}catch (Throwable e) {

					if (LOGGER.isLoggable(Level.FINE))
						LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
				}
			}

			// processing information
			message = new StringBuffer("Done!!!");
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine(message.toString());
			}
			fireEvent(message.toString(), 100);
		} else {
			// processing information
			message = new StringBuffer("No file to process!!!");
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine(message.toString());
			}
			fireEvent(message.toString(), 100);
		}
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
			return !(defCCM.getNumColorComponents() == actualCCM
					.getNumColorComponents()
					&& defCCM.hasAlpha() == actualCCM.hasAlpha()
					&& defCCM.getColorSpace().equals(actualCCM.getColorSpace())
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
			// ///
			//
			// Suggesting expansion in the simplest case
			//
			// ///
			if (defICM.getMapSize() != actualICM.getMapSize()
					|| defICM.getTransparency() != actualICM.getTransparency()
					|| defICM.getTransferType() != actualICM.getTransferType()
					|| defICM.getTransparentPixel() != actualICM
							.getTransparentPixel()) {
				mustConvertToRGB = true;
				return false;
			}
			// //
			//
			// Now checking palettes to see if we need to do a color convert
			//
			// //
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
		// //
		//
		// if we get here this means that the two color models where completely
		// different, hence skip this feature.
		//
		// //
		return true;
	}

	/**
	 * Default constructor
	 */
	public MosaicIndexBuilder() {
		super(NAME, VERSION);
		// /////////////////////////////////////////////////////////////////////
		// Options for the command line
		// /////////////////////////////////////////////////////////////////////
		locationOpt = optionBuilder.withShortName("s").withLongName(
				"source_directory").withArgument(
				argumentBuilder.withName("source").withMinimum(1)
						.withMaximum(1).create()).withDescription(
				"path where files are located").withRequired(true).create();
		wildcardOpt = optionBuilder.withShortName("w").withLongName(
				"wildcardOpt").withArgument(
				argumentBuilder.withName("wildcardOpt").withMinimum(0)
						.withMaximum(1).create()).withDescription(
				"wildcardOpt to use for selecting files").withRequired(false)
				.create();

		nameOpt = optionBuilder.withShortName("name")
				.withLongName("index_name").withArgument(
						argumentBuilder.withName("name").withMinimum(0)
								.withMaximum(1).create()).withDescription(
						"name for the index file").withRequired(false).create();

		relativePathOpt = optionBuilder.withShortName("abs").withLongName(
				"absolute_path").withDescription(
				"whether or not paths shuld be absolute").withRequired(false)
				.create();

		addOption(locationOpt);
		addOption(wildcardOpt);
		addOption(nameOpt);
		addOption(relativePathOpt);

		// /////////////////////////////////////////////////////////////////////
		//
		// Help Formatter
		//
		// /////////////////////////////////////////////////////////////////////
		finishInitialization();
	}

	/**
	 * Entry point for the index builder.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		final MosaicIndexBuilder mosaicIndexBuilder = new MosaicIndexBuilder();
		mosaicIndexBuilder.addProcessingEventListener(mosaicIndexBuilder);
		if (mosaicIndexBuilder.parseArgs(args)) {
			final Thread t = new Thread(mosaicIndexBuilder,
					"MosaicIndexBuilder");
			t.setPriority(mosaicIndexBuilder.getPriority());
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}

		} else
			LOGGER.fine("Exiting...");

	}

	public boolean parseArgs(String[] args) {
		if (!super.parseArgs(args))
			return false;
		// ////////////////////////////////////////////////////////////////
		//
		// parsing command line parameters and setting up
		// Mosaic Index Builder options
		//
		// ////////////////////////////////////////////////////////////////
		locationPath = (String) getOptionValue(locationOpt);
		final File inDir = new File(locationPath);
		if (!inDir.isDirectory()) {
			LOGGER.severe("Provided input dir does not exist or is not a dir!");
			return false;
		}
		try {
			locationPath = inDir.getCanonicalPath();
			locationPath = locationPath.replace('\\', '/');
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		}
		// wildcard
		if (hasOption(wildcardOpt))
			wildcardString = (String) getOptionValue(wildcardOpt);

		// index name
		if (hasOption(nameOpt))
			indexName = (String) getOptionValue(nameOpt);

		// //
		//
		// Type of path
		//
		// //
		if (hasOption(relativePathOpt))
			absolute = true;
		return true;

	}

	/**
	 * This method is responsible for computing the resolutions in for the
	 * provided grid geometry in the provided crs.
	 * 
	 * <P>
	 * It is worth to note that the returned resolution array is of length of 2
	 * and it always is lon, lat for the moment.<br>
	 * It might be worth to remove the axes reordering code when we are
	 * confident enough with the code to handle the north-up crs.
	 * <p>
	 * TODO use orthodromic distance?
	 * 
	 * @param envelope
	 *            the GeneralEnvelope
	 * @param dim
	 * @param crs
	 * @throws DataSourceException
	 */
	protected final double[] getResolution(GeneralEnvelope envelope,
			Rectangle2D dim, CoordinateReferenceSystem crs)
			throws DataSourceException {
		double[] requestedRes = null;
		try {
			if (dim != null && envelope != null) {
				// do we need to transform the originalEnvelope?
				final CoordinateReferenceSystem crs2D = CRSUtilities
						.getCRS2D(envelope.getCoordinateReferenceSystem());

				if (crs != null && !CRS.equalsIgnoreMetadata(crs, crs2D)) {
					final MathTransform tr = CRS.findMathTransform(crs2D, crs);
					if (!tr.isIdentity())
						envelope = CRS.transform(tr, envelope);
				}
				requestedRes = new double[2];
				requestedRes[0] = envelope.getLength(0) / dim.getWidth();
				requestedRes[1] = envelope.getLength(1) / dim.getHeight();
			}
			return requestedRes;
		} catch (TransformException e) {
			throw new DataSourceException("Unable to get the resolution", e);
		} catch (FactoryException e) {
			throw new DataSourceException("Unable to get the resolution", e);
		}
	}

	/**
	 * This method is repsonbile for sending the process progress events to the
	 * logger.
	 * 
	 * <p>
	 * It should be used to do normal logging when running this tools as command
	 * line tools but it should be disable when putting the tool behind a GUI.
	 * In such a case the GUI should register itself as a
	 * {@link ProcessingEventListener} and consume the processing events.
	 * 
	 * @param event
	 *            is a {@link ProcessingEvent} that informs the receiver on the
	 *            precetnage of the progress as well as on what is happening.
	 */
	public void getNotification(ProcessingEvent event) {
		LOGGER.info(new StringBuffer("Progress is at ").append(
				event.getPercentage()).append("\n").append(
				"attached message is: ").append(event.getMessage()).toString());

	}

	public void exceptionOccurred(ExceptionEvent event) {
		LOGGER.log(Level.SEVERE, "An error occurred during processing", event
				.getException());
	}

	/**
	 * @param locationPath
	 *            the locationPath to set
	 */
	public final void setLocationPath(String locationPath) {
		this.locationPath = locationPath;
		final File inDir = new File(locationPath);
		if (!inDir.isDirectory()) {
			LOGGER.severe("Provided input dir does not exist or is not a dir!");
			throw new IllegalArgumentException(
					"Provided input dir does not exist or is not a dir!");
		}
		try {
			locationPath = inDir.getCanonicalPath();
			locationPath = locationPath.replace('\\', '/');
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			final IllegalArgumentException ex = new IllegalArgumentException();
			ex.initCause(e);
			throw ex;
		}
	}

	/**
	 * @param wildcardString
	 *            the wildcardString to set
	 */
	public final void setWildcardString(String wildcardString) {
		this.wildcardString = wildcardString;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public double getResolutionX() {
		return this.resolutionLevels[0][0];
	}

	public double getResolutionY() {
		return this.resolutionLevels[1][0];
	}
}
