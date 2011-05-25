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
package org.geotools.utils.coveragetiler;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageWriteParam;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.commons.cli2.validation.Validator;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.utils.CoverageToolsConstants;
import org.geotools.utils.progress.BaseArgumentsManager;
import org.geotools.utils.progress.ExceptionEvent;
import org.geotools.utils.progress.ProcessingEvent;
import org.geotools.utils.progress.ProcessingEventListener;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

/**
 * <p>
 * This utility splits rasters into smaller pieces. One can control both the
 * dimension of the tile that will be generated as well as the dimension of the
 * internal tiles for the, improvements. This would allows us not only to break
 * a big coverage into smaller tiles, but also to do the opposite. One may want
 * to compose a mosaic and retile it into bigger tiles, well this can be easily
 * done with this utility.
 * </p>
 * 
 * <p>
 * Example of usage:<br/>
 * <code>CoverageTiler -t "8192,8192" -it "512,512" -s "/usr/home/tmp/myImage.tiff"</code>
 * </p>
 * 
 * <p>
 * The tiles will be stored on the folder <code>"/usr/home/tmp/tiled"</code>,
 * which will be automatically created.
 * </p>
 * 
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 *
 *
 * @source $URL$
 * @version 0.3
 * 
 */
public class CoverageTiler extends BaseArgumentsManager implements
		ProcessingEventListener, Runnable {
	/** Default Logger * */
	private final static Logger LOGGER = Logger.getLogger(CoverageTiler.class
			.toString());

	/** Program Version */
	private final static String VERSION = "0.3";

	private static final String NAME = "CoverageTiler";

	private Option inputLocationOpt;

	private Option outputLocationOpt;

	private Option tileDimOpt;

	private Option compressionTypeOpt;

	private Option compressionRatioOpt;

	private Option internalTileDimOpt;

	private File inputLocation;

	private File outputLocation;

	private int tileWidth;

	private int tileHeight;

	private int internalTileWidth = CoverageToolsConstants.DEFAULT_INTERNAL_TILE_WIDTH;

	private int internalTileHeight = CoverageToolsConstants.DEFAULT_INTERNAL_TILE_HEIGHT;

	private String compressionScheme = CoverageToolsConstants.DEFAULT_COMPRESSION_SCHEME;

	private double compressionRatio = CoverageToolsConstants.DEFAULT_COMPRESSION_RATIO;

	/**
	 * Default constructor
	 */
	public CoverageTiler() {
		super(NAME, VERSION);

		// /////////////////////////////////////////////////////////////////////
		// Options for the command line
		// /////////////////////////////////////////////////////////////////////
		inputLocationOpt = optionBuilder.withShortName("s").withLongName(
				"src_coverage").withArgument(
				argumentBuilder.withName("source").withMinimum(1)
						.withMaximum(1).create()).withDescription(
				"path where the source code is located").withRequired(true)
				.create();
		outputLocationOpt = optionBuilder
				.withShortName("d")
				.withLongName("dest_directory")
				.withArgument(
						argumentBuilder.withName("destination").withMinimum(0)
								.withMaximum(1).create())
				.withDescription(
						"output directory, if none is provided, the \"tiled\" directory will be used")
				.withRequired(false).create();
		tileDimOpt = optionBuilder.withShortName("t").withLongName(
				"tile_dimension").withArgument(
				argumentBuilder.withName("t").withMinimum(1).withMaximum(1)
						.create()).withDescription(
				"Width and height of each tile we generate").withRequired(true)
				.create();

		internalTileDimOpt = optionBuilder.withShortName("it").withLongName(
				"internal_tile_dimension").withArgument(
				argumentBuilder.withName("it").withMinimum(0).withMaximum(1)
						.create()).withDescription(
				"Internal width and height of each tile we generate")
				.withRequired(false).create();

		compressionTypeOpt = optionBuilder
				.withShortName("z")
				.withLongName("compressionType")
				.withDescription("compression type.")
				.withArgument(
						argumentBuilder.withName("compressionType")
								.withMinimum(0).withMaximum(1).withValidator(
										new Validator() {

											public void validate(List args)
													throws InvalidArgumentException {
												final int size = args.size();
												if (size > 1)
													throw new InvalidArgumentException(
															"Only one scaling algorithm at a time can be chosen");

											}
										}).create()).withRequired(false)
				.create();

		compressionRatioOpt = optionBuilder
				.withShortName("r")
				.withLongName("compressionRatio")
				.withDescription("compression ratio.")
				.withArgument(
						argumentBuilder.withName("compressionRatio")
								.withMinimum(0).withMaximum(1).withValidator(
										new Validator() {

											public void validate(List args)
													throws InvalidArgumentException {
												final int size = args.size();
												if (size > 1)
													throw new InvalidArgumentException(
															"Only one scaling algorithm at a time can be chosen");
												final String val = (String) args
														.get(0);

												final double value = Double
														.parseDouble(val);
												if (value <= 0 || value > 1)
													throw new InvalidArgumentException(
															"Invalid compressio ratio");

											}
										}).create()).withRequired(false)
				.create();

		addOption(tileDimOpt);
		addOption(inputLocationOpt);
		addOption(outputLocationOpt);
		addOption(internalTileDimOpt);
		addOption(compressionTypeOpt);
		addOption(compressionRatioOpt);

		// /////////////////////////////////////////////////////////////////////
		//
		// Help Formatter
		//
		// /////////////////////////////////////////////////////////////////////
		finishInitialization();

	}

	/**
	 * @param args
	 * @throws MalformedURLException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws MalformedURLException,
			InterruptedException {

		final CoverageTiler coverageTiler = new CoverageTiler();
		coverageTiler.addProcessingEventListener(coverageTiler);
		if (coverageTiler.parseArgs(args)) {
			final Thread t = new Thread(coverageTiler, NAME);
			t.setPriority(coverageTiler.getPriority());
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}

		} else
			LOGGER.fine("Exiting...");
	}

	/**
	 * This method is responsible for sending the process progress events to the
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
		LOGGER.info(new StringBuilder("Progress is at ").append(
				event.getPercentage()).append("\n").append(
				"attached message is: ").append(event.getMessage()).toString());

	}

	public void exceptionOccurred(ExceptionEvent event) {
		LOGGER.log(Level.SEVERE, "An error occurred during processing", event
				.getException());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.utils.progress.ProgressManager#run()
	 */
	@SuppressWarnings("deprecation")
	public void run() {

		// /////////////////////////////////////////////////////////////////////
		//
		//
		// Trying to acquire a reader for the provided source file.
		// 
		// 
		// /////////////////////////////////////////////////////////////////////
		StringBuilder message = new StringBuilder("Acquiring a reader to  ")
				.append(inputLocation);
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(message.toString());
		fireEvent(message.toString(), 0);
		// get the format of this file, if it is recognized!
		final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(inputLocation);
		if (format == null || format instanceof UnknownFormat) {
			fireException(
					"Unable to decide format for this coverage",
					0,
					new IOException("Could not find a format for this coverage"));
			return;
		}
		// get a reader for this file
		final AbstractGridCoverage2DReader inReader = 
			(AbstractGridCoverage2DReader) format.getReader(inputLocation, new Hints(Hints.OVERVIEW_POLICY, OverviewPolicy.IGNORE));
		if (inReader == null) {
			message = new StringBuilder(
					"Unable to instantiate a reader for this coverage");
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.fine(message.toString());
			fireEvent(message.toString(), 0);
			return;
		}

		// /////////////////////////////////////////////////////////////////////
		//
		//
		// If everything went fine, let's proceed with tiling this coverage.
		// 
		// 
		// /////////////////////////////////////////////////////////////////////
		if (!outputLocation.exists())
			outputLocation.mkdir();

		// //
		//
		// getting source envelope and crs
		//
		// //
		final GeneralEnvelope envelope = inReader.getOriginalEnvelope();
		message = new StringBuilder("Original envelope is ").append(envelope
				.toString());
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(message.toString());
		fireEvent(message.toString(), 0);

		// //
		//
		// getting source gridrange and checking tile dimensions to be not
		// bigger than the original coverage size
		//
		// //
		final GridEnvelope range = inReader.getOriginalGridRange();
		final int w = range.getSpan(0);
		final int h = range.getSpan(1);
		tileWidth = tileWidth > w ? w : tileWidth;
		tileHeight = tileHeight > h ? h : tileHeight;
		message = new StringBuilder("Original range is ").append(range.toString());
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(message.toString());
		fireEvent(message.toString(), 0);
		message = new StringBuilder("New matrix dimension is (cols,rows)==(")
				.append(tileWidth).append(",").append(tileHeight).append(")");
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(message.toString());
		fireEvent(message.toString(), 0);

		// //
		//
		// read the coverage
		//
		// //
		GridCoverage2D gc;
		try {
			gc = (GridCoverage2D) inReader.read(null);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			fireException(e);
			return;

		}

		// ///////////////////////////////////////////////////////////////////
		//
		// MAIN LOOP
		//
		//
		// ///////////////////////////////////////////////////////////////////
		final int numTileX = (int) (w / (tileWidth * 1.0) + 1);
		final int numTileY = (int) (h / (tileHeight * 1.0) + 1);
		for (int i = 0; i < numTileX; i++)
			for (int j = 0; j < numTileY; j++) {

				// //
				//
				// computing the bbox for this tile
				//
				// //
				final Rectangle sourceRegion = new Rectangle(i * tileWidth, j* tileHeight, tileWidth, tileHeight);
				message = new StringBuilder("Writing region  ").append(sourceRegion);
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.fine(message.toString());
				fireEvent(message.toString(), (i + j)
						/ (numTileX * numTileY * 1.0));

				// //
				//
				// building gridgeometry for the read operation with the actual
				// envelope
				//
				// //
				final File fileOut = new File(outputLocation, new StringBuilder(
						"mosaic").append("_").append(
						Integer.toString(i * tileWidth + j)).append(".")
						.append("tiff").toString());
				// remove an old output file if it exists
				if (fileOut.exists())
					fileOut.delete();

				message = new StringBuilder(
						"Preparing to write tile (col,row)==(").append(j)
						.append(",").append(i).append(") to file ").append(
								fileOut);
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.fine(message.toString());
				fireEvent(message.toString(), (i + j)
						/ (numTileX * numTileY * 1.0));

				// //
				//
				// Write this coverage out as a geotiff
				//
				// //
				final AbstractGridFormat outFormat = new GeoTiffFormat();
				try {

					final GeoTiffWriteParams wp = new GeoTiffWriteParams();
					wp.setTilingMode(GeoToolsWriteParams.MODE_EXPLICIT);
					wp.setTiling(internalTileWidth, internalTileHeight);
					wp.setSourceRegion(sourceRegion);
					if (this.compressionScheme != null&& !Double.isNaN(compressionRatio)) {
						wp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
						wp.setCompressionType(compressionScheme);
						wp.setCompressionQuality((float) this.compressionRatio);
					}
					final ParameterValueGroup params = outFormat.getWriteParameters();
					params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);

					final GeoTiffWriter writerWI = new GeoTiffWriter(fileOut);
					writerWI.write(gc, (GeneralParameterValue[]) params.values().toArray(new GeneralParameterValue[1]));
					writerWI.dispose();
				} catch (IOException e) {
					fireException(e);
					return;
				}

			}

		message = new StringBuilder("Done...");
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(message.toString());
		fireEvent(message.toString(), 100);
	}

	public boolean parseArgs(String[] args) {
		if (!super.parseArgs(args))
			return false;

		// ////////////////////////////////////////////////////////////////
		//
		// Parsing command line parameters and setting up
		// Mosaic Index Builder options
		//
		// ////////////////////////////////////////////////////////////////
		inputLocation = new File((String) getOptionValue(inputLocationOpt));

		// output files' directory
		if (hasOption(outputLocationOpt))
			outputLocation = new File(
					(String) getOptionValue(outputLocationOpt));
		else
			outputLocation = new File(inputLocation.getParentFile(), "tiled");
		// //
		//
		// tile dim
		//
		// //
		final String tileDim = (String) getOptionValue(tileDimOpt);
		String[] pairs = tileDim.split(",");
		tileWidth = Integer.parseInt(pairs[0]);
		tileHeight = Integer.parseInt(pairs[1]);

		// //
		//
		// Internal Tile dim
		//
		// //
		final String internalTileDim = (String) getOptionValue(internalTileDimOpt);
		if (internalTileDim != null && internalTileDim.length() > 0) {
			pairs = internalTileDim.split(",");
			internalTileWidth = Integer.parseInt(pairs[0]);
			internalTileHeight = Integer.parseInt(pairs[1]);
		}

		// //
		//
		// Compression params
		//
		// //
		// index name
		if (hasOption(compressionTypeOpt)) {
			compressionScheme = (String) getOptionValue(compressionTypeOpt);
			if (compressionScheme == "")
				compressionScheme = null;
		}
		if (hasOption(compressionRatioOpt)) {
			try {
				compressionRatio = Double
						.parseDouble((String) getOptionValue(compressionRatioOpt));
			} catch (Exception e) {
				compressionRatio = Double.NaN;
			}

		}

		return true;

	}

	public File getInputLocation() {
		return inputLocation;
	}

	public void setInputLocation(File inputLocation) {
		this.inputLocation = inputLocation;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int numTileX) {
		this.tileWidth = numTileX;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int numTileY) {
		this.tileHeight = numTileY;
	}

	public File getOutputLocation() {
		return outputLocation;
	}

	public void setOutputLocation(File outputLocation) {
		this.outputLocation = outputLocation;
	}

	public final double getCompressionRatio() {
		return compressionRatio;
	}

	public final void setCompressionRatio(double compressionRatio) {
		this.compressionRatio = compressionRatio;
	}

	public final String getCompressionScheme() {
		return compressionScheme;
	}

	public final void setCompressionScheme(String compressionScheme) {
		this.compressionScheme = compressionScheme;
	}

	public int getInternalTileHeight() {
		return internalTileHeight;
	}

	public void setInternalTileHeight(int internalTileHeight) {
		this.internalTileHeight = internalTileHeight;
	}

	public int getInternalTileWidth() {
		return internalTileWidth;
	}

	public void setInternalTileWidth(int internalTileWidth) {
		this.internalTileWidth = internalTileWidth;
	}

}
