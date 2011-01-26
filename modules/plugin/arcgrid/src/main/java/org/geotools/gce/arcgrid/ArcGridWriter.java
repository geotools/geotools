/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.gce.arcgrid;

import it.geosolutions.imageio.plugins.arcgrid.AsciiGridsImageMetadata;
import it.geosolutions.imageio.plugins.arcgrid.AsciiGridsImageWriter;
import it.geosolutions.imageio.plugins.arcgrid.spi.AsciiGridsImageWriterSpi;

import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.Interpolation;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverageWriter;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.operation.Resample;
import org.geotools.coverage.processing.operation.SelectSampleDimension;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;

/**
 * {@link ArcGridWriter} supports writing of an ArcGrid GridCoverage to a
 * Desination object
 * 
 * XXX We cannot write a rotated grid coverage, we have to enforce that!
 * 
 * @author Daniele Romagnoli
 * @author Simone Giannecchini (simboss)
 */
public final class ArcGridWriter extends AbstractGridCoverageWriter implements GridCoverageWriter {
	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger("org.geotools.gce.arcgrid");

	/** Imageio {@link AsciiGridsImageWriter} we will use to write out. */
	private AsciiGridsImageWriter mWriter = new AsciiGridsImageWriter(
			new AsciiGridsImageWriterSpi());

	/** Default {@link ParameterValueGroup} for doing a bandselect. */
	private final static ParameterValueGroup bandSelectParams;

	/** Default {@link ParameterValueGroup} for doing a reshape. */
	private final static ParameterValueGroup reShapeParams;

	/** Caching a {@link Resample} operation. */
	private static final Resample resampleFactory = new Resample();

	/** Caching a {@link SelectSampleDimension} operation. */
	private static final SelectSampleDimension bandSelectFactory = new SelectSampleDimension();
	static {
		CoverageProcessor processor = new CoverageProcessor(new Hints(
				Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));
		bandSelectParams = (ParameterValueGroup) processor.getOperation(
				"SelectSampleDimension").getParameters();

		reShapeParams = (ParameterValueGroup) processor
				.getOperation("Resample").getParameters();
	}

	/** Small number for comparisons of angles in this pugin. */
	private static final double ROTATION_EPS = 1E-3;

	/** The band of the provided coverage that we want to write down. */
	private int writeBand = -1;

	/**
	 * Takes either a URL or a String that points to an ArcGridCoverage file and
	 * converts it to a URL that can then be written to.
	 * 
	 * @param destination
	 *            the URL or String pointing to the file to load the ArcGrid
	 * @throws DataSourceException
	 */
	public ArcGridWriter(Object destination) throws DataSourceException {
		this(destination, null);
	}

	/**
	 * Takes either a URL or a String that points to an ArcGridCoverage file and
	 * converts it to a URL that can then be written to.
	 * 
	 * @param destination
	 *            the URL or String pointing to the file to load the ArcGrid
	 * @throws DataSourceException
	 */
	public ArcGridWriter(Object destination, Hints hints)
			throws DataSourceException {
		this.destination = destination;
		if (destination instanceof File)
			try {
				super.outStream = ImageIO.createImageOutputStream(destination);
			} catch (IOException e) {
				if (LOGGER.isLoggable(Level.SEVERE))
					LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
				throw new DataSourceException(e);
			}
		else if (destination instanceof URL) {
			final URL dest = (URL) destination;
			if (dest.getProtocol().equalsIgnoreCase("file")) {
				File destFile;
				destFile = DataUtilities.urlToFile(dest);
				try {
					super.outStream = ImageIO.createImageOutputStream(destFile);
				} catch (IOException e) {
					if (LOGGER.isLoggable(Level.SEVERE))
						LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
					throw new DataSourceException(e);
				}
			}

		} else if (destination instanceof OutputStream) {

			try {
				super.outStream = ImageIO
						.createImageOutputStream((OutputStream) destination);
			} catch (IOException e) {
				if (LOGGER.isLoggable(Level.SEVERE))
					LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
				throw new DataSourceException(e);
			}

		} else if (destination instanceof ImageOutputStream)
			this.destination = outStream = (ImageOutputStream) destination;
		else
			throw new DataSourceException(
					"The provided destination cannot be used!");
		// //
		//
		// managing hints
		//
		// //
		if (hints != null) {
			if (this.hints == null) {
				this.hints = new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);
			}
			this.hints.add(hints);
		}
	}

	/**
	 * Creates a Format object describing the Arc Grid Format
	 * 
	 * @return the format of the data source we will write to. (ArcGridFormat in
	 *         this case)
	 * 
	 * @see org.opengis.coverage.grid.GridCoverageWriter#getFormat()
	 */
	public Format getFormat() {
		return new ArcGridFormat();
	}

	/**
	 * This method was copied from ArcGridData source
	 * 
	 * @param gc
	 *            the grid coverage that will be written to the destination
	 * @param parameters
	 *            to control this writing process
	 * @param grass
	 *            tells me whether to write this coverage in the grass format.
	 * 
	 * @throws DataSourceException
	 *             indicates an unexpected exception
	 */
	private void writeGridCoverage(GridCoverage2D gc,
			GeneralParameterValue[] parameters) throws DataSourceException {
		try {
			// /////////////////////////////////////////////////////////////////////
			//
			// Checking writing params
			//
			// /////////////////////////////////////////////////////////////////////
			GeoToolsWriteParams gtParams = null;
			boolean grass = false;
			boolean forceCellSize = false;
			final String grassParam = ArcGridFormat.GRASS.getName().getCode().toString();
			final String cellSizeParam = ArcGridFormat.FORCE_CELLSIZE
					.getName().getCode().toString();
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					Parameter param = (Parameter) parameters[i];
					String name = param.getDescriptor().getName().toString();
					if (param.getDescriptor().getName().getCode().equals(
							AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName()
									.toString())) {
						gtParams = (GeoToolsWriteParams) param.getValue();
					}
					if (name.equalsIgnoreCase(grassParam))
						grass = param.booleanValue();
					if (name.equalsIgnoreCase(cellSizeParam))
						forceCellSize = param.booleanValue();
				}
			}
			if (gtParams == null)
				gtParams = new ArcGridWriteParams();
			// write band
			int[] writeBands = gtParams.getSourceBands();
			writeBand = CoverageUtilities.getVisibleBand(gc.getRenderedImage());
			if ((writeBands == null || writeBands.length == 0 || writeBands.length > 1)
					&& (writeBand < 0 || writeBand > gc
							.getNumSampleDimensions()))
				throw new IllegalArgumentException(
						"You need to supply a valid index for deciding which band to write.");
			if (!((writeBands == null || writeBands.length == 0 || writeBands.length > 1)))
				writeBand = writeBands[0];

			// /////////////////////////////////////////////////////////////////
			//
			// Getting CRS and envelope information
			//
			// /////////////////////////////////////////////////////////////////
			final CoordinateReferenceSystem crs = gc.getCoordinateReferenceSystem2D();

			// /////////////////////////////////////////////////////////////////
			//
			// getting visible band, if needed
			// /////////////////////////////////////////////////////////////////
			final int numBands = gc.getNumSampleDimensions();
			if (numBands > 1) {
				final int visibleBand;
				if (writeBand > 0 && writeBand < numBands)
					visibleBand = writeBand;
				else
					visibleBand = CoverageUtilities.getVisibleBand(gc);

				final ParameterValueGroup param = (ParameterValueGroup) ArcGridWriter.bandSelectParams
						.clone();
				param.parameter("source").setValue(gc);
				param.parameter("SampleDimensions").setValue(
						new int[] { visibleBand });
				gc = (GridCoverage2D) bandSelectFactory
						.doOperation(param, null);
			}
			// /////////////////////////////////////////////////////////////////
			//
			// checking if the coverage needs to be resampled in order to have
			// square pixels for the esri format
			//
			// /////////////////////////////////////////////////////////////////
			if (!grass && forceCellSize)
				gc = reShapeData(gc,hints);

			// /////////////////////////////////////////////////////////////////
			//
			// Preparing to write header information
			//
			// /////////////////////////////////////////////////////////////////
			// getting the new envelope after the reshaping
			final Envelope newEnv = gc.getEnvelope2D();

			// trying to prepare the header
			final AffineTransform gridToWorld = (AffineTransform) ((GridGeometry2D) gc
					.getGridGeometry()).getGridToCRS2D();
			final double xl = newEnv.getLowerCorner().getOrdinate(0);
			final double yl = newEnv.getLowerCorner().getOrdinate(1);
			final double cellsizeX = Math.abs(gridToWorld.getScaleX());
			final double cellsizeY = Math.abs(gridToWorld.getScaleY());

			// /////////////////////////////////////////////////////////////////
			//	
			// Preparing source image and metadata
			//
			// /////////////////////////////////////////////////////////////////
			final RenderedImage source = gc.getRenderedImage();
			final int cols = source.getWidth();
			final int rows = source.getHeight();

			// Preparing main parameters for JAI imageWrite Operation
			// //
			// Setting Output
			// //
			//mWriter.reset();
			mWriter.setOutput(outStream);

			// //
			// no data management
			// //
			double inNoData = getCandidateNoData(gc);

			// //
			// Construct a proper asciiGridRaster which supports metadata
			// setting
			// //

			// Setting the source for the image write operation
			mWriter.write(null, new IIOImage(source, null,
					new AsciiGridsImageMetadata(cols, rows, cellsizeX,cellsizeY, xl, yl,  true,grass, inNoData)), null);

			// writing crs info
			writeCRSInfo(crs);

			// /////////////////////////////////////////////////////////////////
			//
			// Creating the imageWrite Operation
			//
			// /////////////////////////////////////////////////////////////////
		} catch (IOException e) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new DataSourceException(e);
		}finally{

			mWriter.dispose();
		}
	}

	/**
	 * Resampling the raster in order to have square pixels instead of
	 * rectangular which are not suitable for an Esrii ascii grid.
	 * 
	 * @param gc
	 *            Input coverage.
	 * @return A new coverage with square pixels.
	 */
	private static GridCoverage2D reShapeData(final GridCoverage2D gc, final Hints hints) {

		// /////////////////////////////////////////////////////////////////////
		//
		// Getting the dx and dy for this coverage and checking if they differ
		// so much that we need to reshape in order to have square pixels
		//
		// /////////////////////////////////////////////////////////////////////
		final AffineTransform gridToWorld = (AffineTransform) ((GridGeometry2D) gc.getGridGeometry()).getGridToCRS2D();
		final double dx = XAffineTransform.getScaleX0(gridToWorld);
		final double dy = XAffineTransform.getScaleY0(gridToWorld);
		if (AsciiGridsImageWriter.resolutionCheck(dx, dy, AsciiGridsImageWriter.EPS)) {
			return gc;
		}

		// /////////////////////////////////////////////////////////////////////
		//
		// Getting info about the original image in order to create the new
		// gridgeometry
		//
		// /////////////////////////////////////////////////////////////////////
		final RenderedImage image = gc.getRenderedImage();
		int Nx = image.getWidth();
		int Ny = image.getHeight();
		final double _Nx;
		final double _Ny;

		// /////////////////////////////////////////////////////////////////////
		//
		// Getting info about the original evelope n order to create the new
		// gridgeometry
		//
		// /////////////////////////////////////////////////////////////////////
		final Envelope oldEnv = gc.getEnvelope2D();
		final double W = oldEnv.getSpan(0);
		final double H = oldEnv.getSpan(1);
		if ((dx - dy) > ArcGridWriter.ROTATION_EPS) {
			/**
			 * we have higher resolution on the Y axis we have to increase it on
			 * the X axis as well.
			 */

			// new number of columns
			_Nx = W / dy;
			Nx = (int) (_Nx + 0.5);
		} else {
			/**
			 * we have higher resolution on the X axis we have to increase it on
			 * the Y axis as well.
			 */

			// new number of rows
			_Ny = H / dx;
			Ny = (int) (_Ny + 0.5);
		}

		// new grid range
		final GeneralGridEnvelope newGridrange = new GeneralGridEnvelope(new int[] {0, 0 }, new int[] { Nx, Ny });
		final GridGeometry2D newGridGeometry = new GridGeometry2D(newGridrange,new GeneralEnvelope(gc.getEnvelope()));

		// /////////////////////////////////////////////////////////////////////
		//
		// Reshaping using the resample operation for having best precision.
		//
		// /////////////////////////////////////////////////////////////////////
		final ParameterValueGroup param = (ParameterValueGroup) reShapeParams
				.clone();
		param.parameter("source").setValue(gc);
		param.parameter("CoordinateReferenceSystem").setValue(
				gc.getCoordinateReferenceSystem2D());
		param.parameter("GridGeometry").setValue(newGridGeometry);
		param.parameter("InterpolationType").setValue(
				Interpolation.getInstance(Interpolation.INTERP_NEAREST));
		return (GridCoverage2D) resampleFactory.doOperation(param, hints);
	}

	/**
	 * Writing {@link CoordinateReferenceSystem} WKT representation on a prj
	 * file.
	 * 
	 * @param crs
	 *            the {@link CoordinateReferenceSystem} to be written out.
	 * 
	 * @throws IOException
	 */
	private void writeCRSInfo(CoordinateReferenceSystem crs) throws IOException {
		// is it null?
		if (crs == null) {
			throw new NullPointerException("CRS cannot be null!");
		}

		// get the destination path
		// getting the path of this object and the name
		URL url = null;

		if (this.destination instanceof String) {
			url = (new File((String) this.destination)).toURI().toURL();
		} else if (this.destination instanceof File) {
			url = ((File) this.destination).toURI().toURL();
		} else if (this.destination instanceof URL) {
			url = (URL) this.destination;
		} else {
			// do nothing for the moment
			return;
		}

		// build up the name
		File ascFile = DataUtilities.urlToFile(url);
		String prjName = ascFile.getName().substring(0, ascFile.getName().lastIndexOf(".")) + ".prj";
		File prjFile = new File (ascFile.getParent(), prjName);

		// create the file
		final BufferedWriter fileWriter = new BufferedWriter(new FileWriter(prjFile));
		try {
			// write information on crs
			fileWriter.write(crs.toWKT());
		}
		finally{
			try{
				fileWriter.close();
			}
			catch (Throwable e) {
				if(LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
			}
		}
	}

	/**
	 * Note: The geotools GridCoverage class does not implement the geoAPI
	 * GridCoverage Interface so this method shows an error. All other methods
	 * are using the geotools GridCoverage class
	 * 
	 * @see org.opengis.coverage.grid.GridCoverageWriter#write(org.opengis.coverage.grid.GridCoverage,
	 *      org.opengis.parameter.GeneralParameterValue[])
	 */
	public void write(GridCoverage coverage, GeneralParameterValue[] parameters)
			throws IllegalArgumentException, IOException {
		ensureWeCanWrite(coverage, parameters);
		writeGridCoverage((GridCoverage2D) coverage,parameters);

	}

	/**
	 * Ascii grids have a few limitations.
	 * 
	 * <ol>
	 * <li>The gridcoverage they contain must have a gridToWorld transform
	 * which is a compositions of scale and translate, no skew, no rotation.</li>
	 * <li>The PRJ must be lon-lat (this is an assumption form real world).</li>
	 * </ol>
	 * 
	 * @param coverage
	 *            to check for the possibility to be written b this writer.
	 * @param parameters
	 *            to control the writing process.
	 * @throws IOException
	 */
	private static void ensureWeCanWrite(GridCoverage coverage,
			GeneralParameterValue[] parameters) throws IOException {
		// /////////////////////////////////////////////////////////////////////
		//
		// RULE 1
		//
		// Checking the grid to world transform for having only scale and
		// translate.
		//
		// /////////////////////////////////////////////////////////////////////
		final AffineTransform gridToWorldTransform = new AffineTransform(
				(AffineTransform) ((GridGeometry2D) coverage.getGridGeometry()).getGridToCRS2D());

		final int swapXY = XAffineTransform.getSwapXY(gridToWorldTransform);
		XAffineTransform.round(gridToWorldTransform, ROTATION_EPS);
		final double rotation = XAffineTransform
				.getRotation(gridToWorldTransform);
		if (swapXY == -1)
			throw new DataSourceException(
					"Impossible to encode this coverage as an ascii grid since its"
							+ "transformation is not a simple scale and translate");
		if (rotation != 0)
			throw new DataSourceException(
					"Impossible to encode this coverage as an ascii grid since its"
							+ "transformation is not a simple scale and translate");

		// /////////////////////////////////////////////////////////////////////
		//
		// RULE 2
		//
		// Checking the CRS to have flip only at first axis
		//
		// /////////////////////////////////////////////////////////////////////
		final int flip = XAffineTransform.getFlip(gridToWorldTransform);
		final CoordinateReferenceSystem crs2D = ((GridCoverage2D) coverage)
				.getCoordinateReferenceSystem2D();
		// flip==-1 means there is a flip.
		if (flip > 0)
			throw new DataSourceException(
					"Impossible to encode this coverage as an ascii grid since its"
							+ "coordinate reference system has strange axes orientation");
		// let's check that its the Y axis that's flipped
		if (!AxisDirection.NORTH.equals(crs2D.getCoordinateSystem().getAxis(1)
				.getDirection()))
			throw new DataSourceException(
					"Impossible to encode this coverage as an ascii grid since its"
							+ "coordinate reference system has strange axes orientation");
		if (!AxisDirection.EAST.equals(crs2D.getCoordinateSystem().getAxis(0)
				.getDirection()))
			throw new DataSourceException(
					"Impossible to encode this coverage as an ascii grid since its"
							+ "coordinate reference system has strange axes orientation");

		// /////////////////////////////////////////////////////////////////////
		//
		// RULE 3
		//
		// Check that we are actually writing a GridCoverage2D
		//
		// /////////////////////////////////////////////////////////////////////
		if(coverage instanceof GridCoverage2D && !(coverage.getGridGeometry() instanceof GridGeometry2D))
			throw new DataSourceException("The provided coverage is not a GridCoverage2D");
	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageWriter#dispose()
	 */
	public void dispose() {

		if (mWriter != null) {
			try{
				mWriter.dispose();
			}catch (Exception e) {
				// swallow
			}
			mWriter = null;
		}
	}

	static double getCandidateNoData(GridCoverage2D gc) {
		// no data management
		final GridSampleDimension sd = (GridSampleDimension) gc
				.getSampleDimension(0);
		final List<Category> categories = sd.getCategories();
		final Iterator<Category> it = categories.iterator();
		Category candidate;
		double inNoData = Double.NaN;
		final String noDataName = Vocabulary.format(VocabularyKeys.NODATA);
		while (it.hasNext()) {
			candidate = (Category) it.next();
			final String name = candidate.getName().toString();
			if (name.equalsIgnoreCase("No Data")
					|| name.equalsIgnoreCase(noDataName)) {
				inNoData = candidate.getRange().getMaximum();
			}
		}

		return inNoData;
	}

}
