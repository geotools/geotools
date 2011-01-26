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
package org.geotools.gce.imagepyramid;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;

import org.apache.commons.io.IOUtils;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.PrjFileReader;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.util.SoftValueHashMap;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * This reader is repsonsible for providing access to a pyramid of mosaics of
 * georeferenced coverages that are read directly through imageio readers, like
 * tiff, pngs, etc...
 * 
 * <p>
 * Specifically this plugin relies on the image mosaic plugin to handle each
 * single level of resolutions avaible, hence all the magic is done inside the
 * mosaic plugin.
 * 
 * 
 * <p>
 * For information on how to build a mosaic, please refere to the
 * {@link ImageMosaicReader} documentation.
 * 
 * <p>
 * If you are looking for information on how to create a pyramid, here you go.
 * 
 * The pyramid itself does no magic. All the magic is performed by the single
 * mosaic readers that are polled depending on the requeste resolution levels.
 * Therefore the <b>first step</b> is having a mosaic of images like geotiff,
 * tiff, jpeg, or png which is going to be the base for te pyramid.
 * 
 * <p>
 * The <b>second step</b> is to build the next (lower resolution) levels for
 * the pyramid. <br>
 * If you look inside the spike dire of the geotools project you will find a
 * (growing) set of tools that can be used for doing processing on coverages.
 * <br>
 * Specifically there is one tool called PyramidBuilder that can be used to
 * build the pyramid level by level.
 * 
 * <p>
 * <b>Last step</b> is providing a prj file with the projection of the pyramid
 * (btw all the levels has to be in the same projection) as well as a properties
 * file with this structure:
 * 
 * <pre>
 *           #
 *           #Mon Aug 21 22:23:27 CEST 2006
 *           #name of the coverage
 *           Name=ikonos
 *           #different resolution levels available
 *           Levels=1.2218682749859724E-5,9.220132503102996E-6 2.4428817977683634E-5,1.844026500620314E-5 4.8840552865873626E-5,3.686350299024973E-5 9.781791400307775E-5,7.372700598049946E-5 1.956358280061555E-4,1.4786360643866836E-4 3.901787184256844E-4,2.9572721287731037E-4
 *           #where all the levels reside
 *           LevelsDirs=0 2 4 8 16 32
 *           #number of levels availaible
 *           LevelsNum=6
 *           #envelope for this pyramid
 *           Envelope2D=13.398228477973406,43.591366397808976 13.537912459169803,43.67121274528585
 * </pre>
 * 
 * @author Simone Giannecchini
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties like URLs
 * @since 2.3
 * 
 */
public final class ImagePyramidReader extends AbstractGridCoverage2DReader implements GridCoverageReader {

	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImagePyramidReader.class.toString());

	/**
	 * The input properties file to read the pyramid information from.
	 */
	private URL sourceURL;

	/**
	 * The directories where to find the different resolutions levels in
	 * descending order.
	 */
	private String[] levelsDirs;

	/**
	 * Cache of {@link ImageMosaicReader} objects for the different levels.
	 * 
	 */
	private Map<Integer, ImageMosaicReader> readers;

	/**
	 * Constructor for an {@link ImagePyramidReader}.
	 * 
	 * @param source
	 *            The source object.
	 * @param uHints
	 *            {@link Hints} to control the behaviour of this reader.
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public ImagePyramidReader(Object source, Hints uHints) throws IOException {
		// //
		//
		// managing hints
		//
		// //
		if (this.hints == null)
			this.hints = new Hints();
		if (uHints != null) {
			this.hints.add(uHints);
		}
		this.coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(this.hints);

		// //
		//
		// Check source
		//
		// //
		if (source == null) {

			final NullPointerException ex = new NullPointerException("ImagePyramidReader:No source set to read this coverage.");
			throw new DataSourceException(ex);
		}
		this.source = source;
		this.sourceURL = Utils.checkSource(source, uHints);
		if(sourceURL == null) {
		    throw new IllegalArgumentException(
                "This plugin accepts only File, URL and String pointing to a file");
		} 
		
		// //
		//
		// Load tiles informations, especially the bounds, which will be
		// reused
		//
		// //
		// //
		//
		// get the crs if able to
		//
		// //		
		final URL prjURL = DataUtilities.changeUrlExt(sourceURL, "prj"); 
		PrjFileReader crsReader=null;
		try {
			crsReader = new PrjFileReader(Channels.newChannel(prjURL.openStream()));
		} catch (FactoryException e) {
			throw new DataSourceException(e);
		}finally{
			try{
				crsReader.close();
			}
			catch (Throwable e) {
				if(LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
			}
		}
		final Object tempCRS = hints.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
		if (tempCRS != null) {
			this.crs = (CoordinateReferenceSystem) tempCRS;
			LOGGER.log(Level.WARNING, "Using forced coordinate reference system "+crs.toWKT());
		} else {
			final CoordinateReferenceSystem tempcrs = crsReader.getCoordinateReferenceSystem();
			if (tempcrs == null) {
				// use the default crs
				crs = AbstractGridFormat.getDefaultCRS();
				LOGGER.log(Level.WARNING,"Unable to find a CRS for this coverage, using a default one: "+crs.toWKT());
			} else
				crs = tempcrs;
		}
		
		//
		// Load properties file with information about levels and envelope
		//
		
		// property file
		parseMainFile(sourceURL);
	}

	/**
	 * Parses the main properties file loading the information regarding
	 * geographic extent and overviews.
	 * 
	 * @param sourceFile
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void parseMainFile(final URL sourceURL) throws IOException {
		BufferedInputStream propertyStream = null;
		InputStream openStream = null;
		try {
			openStream = sourceURL.openStream();
			propertyStream = new BufferedInputStream(openStream);
			final Properties properties = new Properties();
			properties.load(propertyStream);

			// load the envelope
			final String envelope = properties.getProperty("Envelope2D");
			String[] pairs = envelope.split(" ");
			final double cornersV[][] = new double[2][2];
			String pair[];
			for (int i = 0; i < 2; i++) {
				pair = pairs[i].split(",");
				cornersV[i][0] = Double.parseDouble(pair[0]);
				cornersV[i][1] = Double.parseDouble(pair[1]);
			}
			this.originalEnvelope = new GeneralEnvelope(cornersV[0],cornersV[1]);
			this.originalEnvelope.setCoordinateReferenceSystem(crs);
			
			// overviews dir
			numOverviews = Integer.parseInt(properties.getProperty("LevelsNum")) - 1;
			levelsDirs = properties.getProperty("LevelsDirs").split(" ");

			// readers soft map
			final int readersCacheSize = (numOverviews + 1) / 3;
			readers = new SoftValueHashMap<Integer, ImageMosaicReader>(readersCacheSize == 0 ? numOverviews + 1 : readersCacheSize);

			// resolutions levels
			final String levels = properties.getProperty("Levels");
			pairs = levels.split(" ");
			overViewResolutions = numOverviews >= 1 ? new double[numOverviews][2]: null;
			pair = pairs[0].split(",");
			highestRes = new double[2];
			highestRes[0] = Double.parseDouble(pair[0].trim());
			highestRes[1] = Double.parseDouble(pair[1].trim());
			for (int i = 1; i < numOverviews + 1; i++) {
				pair = pairs[i].split(",");
				overViewResolutions[i - 1][0] = Double.parseDouble(pair[0].trim());
				overViewResolutions[i - 1][1] = Double.parseDouble(pair[1].trim());
			}

			// name
			coverageName = properties.getProperty("Name");

			// original gridrange (estimated)
			originalGridRange = new GridEnvelope2D(
					new Rectangle(
							(int) Math.round(originalEnvelope.getSpan(0)/ highestRes[0]), 
							(int) Math.round(originalEnvelope.getSpan(1)/ highestRes[1])
							)
					);
			final GridToEnvelopeMapper geMapper= new GridToEnvelopeMapper(originalGridRange,originalEnvelope);
			geMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
			raster2Model= geMapper.createTransform();			
		}  finally {
			// close input stream
			if (propertyStream != null)
				IOUtils.closeQuietly(propertyStream);
			
			if (openStream != null) 
				IOUtils.closeQuietly(openStream);
		}

	}

	/**
	 * Constructor for an {@link ImagePyramidReader}.
	 * 
	 * @param source
	 *            The source object.
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public ImagePyramidReader(Object source) throws IOException {
		this(source, null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
	 */
	public Format getFormat() {
		return new ImagePyramidFormat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.coverage.grid.GridCoverageReader#read(org.opengis.parameter.GeneralParameterValue[])
	 */
	@SuppressWarnings("unchecked")
	public GridCoverage2D read(GeneralParameterValue[] params) throws IOException {

		GeneralEnvelope requestedEnvelope = null;
		Rectangle dim = null;
		OverviewPolicy overviewPolicy=null;
		if (params != null) {
			// /////////////////////////////////////////////////////////////////////
			//
			// Checking params
			//
			// /////////////////////////////////////////////////////////////////////
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					final ParameterValue param = (ParameterValue) params[i];
					if (param == null){
						continue;
					}
					final String name = param.getDescriptor().getName().getCode();
					if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString())) {
						final GridGeometry2D gg = (GridGeometry2D) param.getValue();
						requestedEnvelope = new GeneralEnvelope((Envelope)gg.getEnvelope2D());
						dim = gg.getGridRange2D().getBounds();
						continue;
					}
					if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName().toString())) {
						overviewPolicy = (OverviewPolicy) param.getValue();
						continue;
					}
				}
			}
		}
		// /////////////////////////////////////////////////////////////////////
		//
		// Loading tiles
		//
		// /////////////////////////////////////////////////////////////////////
		return loadTiles(requestedEnvelope, dim, params, overviewPolicy);
	}

	/**
	 * Loading the tiles which overlap with the requested envelope.
	 * 
	 * 
	 * 
	 * @param requestedEnvelope
	 * @param dim
	 * @param params
	 * @param overviewPolicy
	 * @return A {@link GridCoverage}, well actually a {@link GridCoverage2D}.
	 * @throws IOException
	 */
	private GridCoverage2D loadTiles(GeneralEnvelope requestedEnvelope,
			Rectangle dim, GeneralParameterValue[] params, OverviewPolicy overviewPolicy)
			throws IOException {

		// /////////////////////////////////////////////////////////////////////
		//
		// Check if we have something to load by intersecting the requested
		// envelope with the bounds of the data set.
		//
		// If the requested envelope is not in the same crs of the data set crs
		// we have to perform a conversion towards the latter crs before
		// intersecting anything.
		//
		// /////////////////////////////////////////////////////////////////////
		if (requestedEnvelope != null) {
			if (!CRS.equalsIgnoreMetadata(requestedEnvelope
					.getCoordinateReferenceSystem(), this.crs)) {
				try {
					// transforming the envelope back to the data set crs
					final MathTransform transform = CRS.findMathTransform(requestedEnvelope.getCoordinateReferenceSystem(),	crs,true);
					if (!transform.isIdentity()) {
						requestedEnvelope = CRS.transform(transform,requestedEnvelope);
						requestedEnvelope.setCoordinateReferenceSystem(this.crs);

						if (LOGGER.isLoggable(Level.FINE))
							LOGGER.fine(new StringBuilder("Reprojected envelope ").append(requestedEnvelope.toString()).append(" crs ").append(crs.toWKT()).toString());
					}
				} catch (TransformException e) {
					throw new DataSourceException("Unable to create a coverage for this source", e);
				} catch (FactoryException e) {
					throw new DataSourceException("Unable to create a coverage for this source", e);
				}
			}
			if (!requestedEnvelope.intersects(this.originalEnvelope, false))
				return null;

			// intersect the requested area with the bounds of this layer
			requestedEnvelope.intersect(originalEnvelope);

		} else {
			requestedEnvelope = new GeneralEnvelope(originalEnvelope);

		}
		requestedEnvelope.setCoordinateReferenceSystem(this.crs);
		// ok we got something to return
		try {
			return loadRequestedTiles(requestedEnvelope, dim, params,overviewPolicy);
		} catch (TransformException e) {
			throw new DataSourceException(e);
		}

	}

	/**
	 * This method loads the tiles which overlap the requested envelope using
	 * the provided values for alpha and input ROI.
	 * 
	 * @param requestedEnvelope
	 * @param alpha
	 * @param alphaThreshold
	 * @param singleImageROI
	 * @param singleImageROIThreshold
	 * @param dim
	 * @param overviewPolicy 
	 * @param ggParam
	 * @return A {@link GridCoverage}, well actually a {@link GridCoverage2D}.
	 * @throws TransformException
	 * @throws IOException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException
	 * @throws FactoryRegistryException
	 */
	private GridCoverage2D loadRequestedTiles(
			GeneralEnvelope requestedEnvelope,
			Rectangle dim, 
			GeneralParameterValue[] params, 
			OverviewPolicy overviewPolicy)
			throws TransformException, IOException {

		// if we get here we have something to load
		// /////////////////////////////////////////////////////////////////////
		//
		// compute the requested resolution
		//
		// /////////////////////////////////////////////////////////////////////
		final ImageReadParam readP = new ImageReadParam();
		Integer imageChoice=0;
		if (dim != null)
			imageChoice = setReadParams(overviewPolicy, readP,requestedEnvelope, dim);
		// /////////////////////////////////////////////////////////////////////
		//
		// Check to have the needed reader in memory
		// 
		// /////////////////////////////////////////////////////////////////////
		ImageMosaicReader reader = null;
		synchronized (readers) {
			Object o = readers.get(imageChoice);
			if (o == null) {
				final String levelDirName = levelsDirs[imageChoice.intValue()];
				final URL parentUrl = DataUtilities.getParentUrl(sourceURL);
				// look for a shapefile first
				final String extension = new StringBuilder(levelDirName).append("/").append(coverageName).append(".shp").toString();
				final URL shpFileUrl = DataUtilities.extendURL(parentUrl,extension);
				if(shpFileUrl.getProtocol()!=null&&shpFileUrl.getProtocol().equalsIgnoreCase("file")&&!DataUtilities.urlToFile(shpFileUrl).exists())
					reader= new ImageMosaicReader(DataUtilities.extendURL(parentUrl,levelDirName), hints);
				else
					reader = new ImageMosaicReader(shpFileUrl,hints);
				readers.put(imageChoice, reader);

			} else
				reader = (ImageMosaicReader) o;
		}

	
		//
		// Abusing of the created ImageMosaicreader for getting a
		// gridcoverage2d, then rename it
		//
		GridCoverage2D mosaicCoverage = reader.read(params);
		if(mosaicCoverage != null) {
		    return new GridCoverage2D(coverageName, mosaicCoverage);
		} else {
		    // the mosaic can still return null in corner cases, handle that gracefully
		    return null;
		}
	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageReader#dispose()
	 */
	@Override
	public synchronized void dispose() {
		super.dispose();
		readers.clear();
	}
	
	/**
	 * Number of coverages for this reader is 1
	 * 
	 * @return the number of coverages for this reader.
	 */
	@Override
	public int getGridCoverageCount() {
		return 1;
	}

    /**
	 * Returns the highest resolution available.
	 * 
	 * TODO The instance variable comes from
	 * {@link AbstractGridCoverage2DReader}, so maybe we should move the getter
	 * there.
	 * */
	double[] getHighestRes() {
		return highestRes;
	}

}
