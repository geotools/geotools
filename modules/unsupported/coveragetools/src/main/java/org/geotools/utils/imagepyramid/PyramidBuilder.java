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
package org.geotools.utils.imagepyramid;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.DefaultOption;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.commons.cli2.validation.Validator;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.utils.CoverageToolsConstants;
import org.geotools.utils.coveragetiler.CoverageTiler;
import org.geotools.utils.imagemosaic.MosaicIndexBuilder;
import org.geotools.utils.progress.BaseArgumentsManager;
import org.geotools.utils.progress.ExceptionEvent;
import org.geotools.utils.progress.ProcessingEvent;
import org.geotools.utils.progress.ProcessingEventListener;

/**
 * Given an original image, builds an image pyramid out of it by combining the
 * various tiler, mosaic and pyramid layer builder tools.
 * 
 * <pre>
 * Example of use:
 *   PyramidBuilder -s &quot;/usr/home/data/home.tif&quot; -f 2 -n 4 -t &quot;25,25&quot; -w
 * </pre>
 * 
 * @author Andrea Aime
 * @author Simone Giannecchini, GeoSolutions.
 * @since 2.3.x
 * 
 *
 *
 * @source $URL$
 */
public class PyramidBuilder extends BaseArgumentsManager implements Runnable,
		ProcessingEventListener {

	/** Static immutable set for scaling algorithms. */
	private static final Set<String> scalingAlgorithms;
	static {
		scalingAlgorithms = new HashSet<String>();
		scalingAlgorithms.add("nn");
		scalingAlgorithms.add("bil");
		scalingAlgorithms.add("bic");
		scalingAlgorithms.add("avg");
		scalingAlgorithms.add("filt");
	}

	/** Program Version */
	private final static String VERSION = "0.3";

	private final static String NAME = "PyramidBuilder";

	/** Commons-cli option for the input location. */
	private DefaultOption locationOpt;

	/** Commons-cli option for the output location. */
	private DefaultOption outputLocationOpt;

	/** Output folder, defaults to the "pyramid" subfolder */
	private File outputLocation;

	/** Commons-cli option for the tile dimension. */
	private Option tileDimOpt;

	/** Commons-cli option for the scale algorithm. */
	private Option scaleAlgorithmOpt;

	/** Commons-cli option for the tile numbe of subsample step to use. */
	private Option numStepsOpt;

	/** Commons-cli option for the scale factor to use. */
	private Option scaleFactorOpt;

	/**
	 * Commons-cli options for overwriting the output layer dirs if already
	 * available
	 */
	private Option overwriteOpt;

	/** Tile width. */
	private int tileW = -1;

	/** Tile height. */
	private int tileH = -1;

	/** Scale algorithm. */
	private String scaleAlgorithm;

	/** Logger for this class. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger(PyramidBuilder.class.toString());

	/** Downsampling step. */
	private int scaleFactor;

	/**
	 * The source path.
	 */
	private File inputLocation;

	/**
	 * The name of the output pyramid, will simply be "pyramid" if not set
	 */
	private String name;

	/**
	 * Commons-cli option for the pyramid name
	 */
	private Option nameOpt;

	private int numSteps;

	private boolean exceptionOccurred = false;

	private boolean overwriteOutputDirs = false;

	private double currStep = 0;

	private double totalSteps = 0;

	/**
	 * Re-launches slave tools progress with the appropriate percentage
	 * corrections
	 */
	private ProcessingEventListener slaveToolsListener = new ProcessingEventListener() {

		public void getNotification(ProcessingEvent event) {
			fireEvent(event.getMessage(), (currStep / totalSteps) * 100
					+ event.getPercentage() / totalSteps);
		}

		public void exceptionOccurred(ExceptionEvent event) {
			fireException(event.getMessage(), event.getPercentage(), event
					.getException());
			exceptionOccurred = true;
		}

	};

	private GeneralEnvelope envelope;

	private double[][] resolutions;


	private Option compressionRatioOpt;


	private Option compressionTypeOpt;

	private Option internalTileDimOpt;

	private int internalTileWidth=CoverageToolsConstants.DEFAULT_INTERNAL_TILE_WIDTH;

	private int internalTileHeight=CoverageToolsConstants.DEFAULT_INTERNAL_TILE_HEIGHT;

	private String compressionScheme = CoverageToolsConstants.DEFAULT_COMPRESSION_SCHEME;

	private double compressionRatio = CoverageToolsConstants.DEFAULT_COMPRESSION_RATIO;
	/**
	 * Simple constructor for a pyramid generator. Use the input string in order
	 * to read an image.
	 * 
	 * 
	 */
	public PyramidBuilder() {
		super(NAME, VERSION);
		// /////////////////////////////////////////////////////////////////////
		// Options for the command line
		// /////////////////////////////////////////////////////////////////////

		internalTileDimOpt = optionBuilder.withShortName("it").withLongName(
				"internal_tile_dimension").withArgument(
				argumentBuilder.withName("it").withMinimum(0).withMaximum(1)
						.create()).withDescription(
				"Internal width and height of each tile we generate")
				.withRequired(false).create();

		locationOpt = optionBuilder
				.withShortName("s")
				.withLongName("source")
				.withArgument(
						argumentBuilder.withName("source").withMinimum(1)
								.withMaximum(1).withValidator(new Validator() {

									public void validate(List args)
											throws InvalidArgumentException {
										final int size = args.size();
										if (size > 1)
											throw new InvalidArgumentException(
													"Source can be a single file or directory ");
										final File source = new File(
												(String) args.get(0));
										if (!source.exists())
											throw new InvalidArgumentException(
													new StringBuffer(
															"The provided source is invalid! ")

													.toString());
									}

								}).create()).withDescription(
						"path where files are located").withRequired(true)
				.create();

		nameOpt = optionBuilder.withShortName("name").withLongName(
				"pyramid_name").withArgument(
				argumentBuilder.withName("name").withMinimum(0).withMaximum(1)
						.create()).withDescription(
				"name for the pyramid property file").withRequired(false)
				.create();

		tileDimOpt = optionBuilder.withShortName("t").withLongName(
				"tiled_dimension").withArgument(
				argumentBuilder.withName("t").withMinimum(0).withMaximum(1)
						.create()).withDescription(
				"tile dimensions as a couple width,height in pixels")
				.withRequired(true).create();

		scaleFactorOpt = optionBuilder
				.withShortName("f")
				.withLongName("scale_factor")
				.withArgument(
						argumentBuilder.withName("f").withMinimum(1)
								.withMaximum(1).withValidator(new Validator() {

									public void validate(List args)
											throws InvalidArgumentException {
										final int size = args.size();
										if (size > 1)
											throw new InvalidArgumentException(
													"Only one scaling algorithm at a time can be chosen");
										int factor = Integer
												.parseInt((String) args.get(0));
										if (factor <= 0)
											throw new InvalidArgumentException(
													new StringBuffer(
															"The provided scale factor is negative! ")

													.toString());
										if (factor == 1) {
											LOGGER
													.warning("The scale factor is 1, program will exit!");
											System.exit(0);
										}
									}

								}).create()).withDescription(
						"integer scale factor")
				.withRequired(true).create();

		numStepsOpt = optionBuilder.withShortName("n")
				.withLongName("num_steps").withArgument(
						argumentBuilder.withName("n").withMinimum(1)
								.withMaximum(1).withValidator(new Validator() {

									public void validate(List args)
											throws InvalidArgumentException {
										final int size = args.size();
										if (size > 1)
											throw new InvalidArgumentException(
													"Only one scaling algorithm at a time can be chosen");
										int steps = Integer
												.parseInt((String) args.get(0));
										if (steps <= 0)
											throw new InvalidArgumentException(
													new StringBuffer(
															"The provided scale factor is negative! ")

													.toString());

									}

								}).create()).withDescription(
						"integer scale factor").withRequired(true).create();

		scaleAlgorithmOpt = optionBuilder
				.withShortName("a")
				.withLongName("scaling_algorithm")
				.withArgument(
						argumentBuilder.withName("a").withMinimum(0)
								.withMaximum(1).withValidator(new Validator() {

									public void validate(List args)
											throws InvalidArgumentException {
										final int size = args.size();
										if (size > 1)
											throw new InvalidArgumentException(
													"Only one scaling algorithm at a time can be chosen");
										if (!scalingAlgorithms.contains(args
												.get(0)))
											throw new InvalidArgumentException(
													new StringBuffer(
															"The output format ")
															.append(args.get(0))
															.append(
																	" is not permitted")
															.toString());

									}
								}).create())
				.withDescription(
						"name of the scaling algorithm, eeither one of average (a), filtered (f), bilinear (bil), nearest neigbhor (nn)")
				.withRequired(false).create();

		overwriteOpt = optionBuilder.withShortName("w").withLongName(
				"overwrite").withDescription(
				"completely wipe out existing layer dirs before proceeding.")
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

		addOption(compressionTypeOpt);
		addOption(compressionRatioOpt);
		addOption(locationOpt);
		addOption(tileDimOpt);
		addOption(scaleFactorOpt);
		addOption(scaleAlgorithmOpt);
		addOption(numStepsOpt);
		addOption(overwriteOpt);
		addOption(internalTileDimOpt);
		finishInitialization();
	}

	public void run() {

		// /////////////////////////////////////////////////////////////////////
		//
		// Gather reader to compute tile x and y from tile size
		//
		// /////////////////////////////////////////////////////////////////////
		final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder
				.findFormat(inputLocation);
		if (format == null) {
			String message = "Could not find a format for this coverage";
			fireException(message, 0, new IOException(message));
			return;
		}
		final AbstractGridCoverage2DReader inReader = (AbstractGridCoverage2DReader) format
				.getReader(inputLocation);
		if (inReader == null) {
			String message = "Unable to instantiate a reader for this coverage";
			fireException(message, 0, new IOException(message));
			return;
		}

		envelope = inReader.getOriginalEnvelope();
		inReader.dispose();

		// /////////////////////////////////////////////////////////////////////
		//
		// Create output directory
		//
		// /////////////////////////////////////////////////////////////////////
		if (!outputLocation.exists())
			if (!outputLocation.mkdir()) {
				String message = "Could not create output directory: "
						+ outputLocation;
				fireException(message, 0, new IOException(message));
				return;
			}

		// /////////////////////////////////////////////////////////////////////
		// 
		// Compute total steps and set current one so that slave tools progress
		// event percentages can be corrected to represent the global progress
		//
		// //////////////////////////////////////////////////////////////////////
		totalSteps = (numSteps + 1) * 2;
		currStep = 1;

		// /////////////////////////////////////////////////////////////////////
		//
		// Set up initial level using the coverage tiler
		//
		// /////////////////////////////////////////////////////////////////////
		File outputDir = new File(outputLocation, "0");
		if (!checkLayerDir(outputDir))
			return;

		// create first tiled set
		resolutions = new double[2][numSteps + 1];
		// tileInput(numTileX, numtileY, outputDir);
		tileInput(outputDir);
		if (exceptionOccurred)
			return;
		currStep++;

		// mosaic it
		double[] resolution = mosaicLevel(0);
		resolutions[0][0] = resolution[0];
		resolutions[1][0] = resolution[1];
		if (exceptionOccurred)
			return;
		currStep++;

		// /////////////////////////////////////////////////////////////////////
		//
		// Now do create a new level, and mosaic it, up to the final level
		//
		// /////////////////////////////////////////////////////////////////////
		int currLevel = scaleFactor;
		int prevLevel = 0;
		for (int step = 0; step < numSteps; step++) {
			// check output dir
			final File prevLevelDirectory = new File(outputLocation, String
					.valueOf(prevLevel));
			final File currLevelDirectory = new File(outputLocation, String
					.valueOf(currLevel));
			if (!checkLayerDir(currLevelDirectory))
				return;

			// create next tiled set
			buildNewLayer(prevLevelDirectory, currLevelDirectory);
			if (exceptionOccurred)
				return;
			currStep++;

			// mosaic it
			resolution = mosaicLevel(currLevel);
			resolutions[0][step + 1] = resolution[0];
			resolutions[1][step + 1] = resolution[1];
			if (exceptionOccurred)
				return;
			currStep++;

			// switch to next resolution level
			prevLevel = currLevel;
			currLevel *= scaleFactor;
		}

		// /////////////////////////////////////////////////////////////////////
		//
		// Finally, build the property file
		//
		// /////////////////////////////////////////////////////////////////////

		fireEvent("Creating final properties file ", 99.9);
		createPropertiesFiles();
		if (!exceptionOccurred)
			fireEvent("Done!!!", 100);
	}

	private boolean checkLayerDir(File outputDir) {
		if (!outputDir.exists())
			return true;
		if (!overwriteOutputDirs) {
			fireException(new IOException("Layer directory " + outputDir
					+ " already exist. Use -w to force its deletion"));
			return false;
		}
		try {
			FileUtils.deleteDirectory(outputDir);
		} catch (IOException e) {
			fireException(e);
			return false;
		}
		return true;
	}

	// private void tileInput(final int numTileX, final int numTileY,
	private void tileInput(File outputDir) {
		CoverageTiler tiler = new CoverageTiler();
		tiler.addProcessingEventListener(slaveToolsListener);
		tiler.setInputLocation(inputLocation);
		tiler.setOutputLocation(outputDir);
		tiler.setTileWidth(tileW);
		tiler.setTileHeight(tileH);
		tiler.setInternalTileHeight(internalTileHeight);
		tiler.setInternalTileWidth(internalTileWidth);
		tiler.setCompressionRatio(this.compressionRatio);
		tiler.setCompressionScheme(this.compressionScheme);
		tiler.run();
		tiler.removeAllProcessingEventListeners();
	}

	private void buildNewLayer(File prevLevelDirectory, File currLevelDirectory) {
		PyramidLayerBuilder layerBuilder = new PyramidLayerBuilder();
		layerBuilder.addProcessingEventListener(slaveToolsListener);
		layerBuilder.setInputLocation(new File(prevLevelDirectory, name
				+ ".shp"));
		layerBuilder.setOutputLocation(currLevelDirectory);
		layerBuilder.setScaleAlgorithm(scaleAlgorithm);
		layerBuilder.setScaleFactor(scaleFactor);
		layerBuilder.setTileHeight(tileH);
		layerBuilder.setTileWidth(tileW);
		layerBuilder.setCompressionRatio(this.compressionRatio);
		layerBuilder.setCompressionScheme(this.compressionScheme);
		layerBuilder.run();
		layerBuilder.removeAllProcessingEventListeners();
	}

	private double[] mosaicLevel(int level) {
		MosaicIndexBuilder builder = new MosaicIndexBuilder();
		builder.addProcessingEventListener(slaveToolsListener);
		builder.setLocationPath(new File(outputLocation, String.valueOf(level))
				.getAbsolutePath());
		builder.setIndexName(name);
		builder.run();
		builder.removeAllProcessingEventListeners();
		return new double[] { builder.getResolutionX(),
				builder.getResolutionY() };
	}

	/**
	 * @param envelope
	 * @param doneSomething
	 */
	private void createPropertiesFiles() {
		// envelope
		final Properties properties = new Properties();
		properties.setProperty("Envelope2D", new StringBuffer(Double
				.toString(envelope.getMinimum(0))).append(",").append(
				Double.toString(envelope.getMinimum(1))).append(" ").append(
				Double.toString(envelope.getMaximum(0))).append(",").append(
				Double.toString(envelope.getMaximum(1))).toString());
		properties.setProperty("LevelsNum", Integer.toString(numSteps + 1));
		final StringBuffer levels = new StringBuffer();
		final StringBuffer levelDirs = new StringBuffer();
		for (int i = 0; i < numSteps + 1; i++) {
			levels.append(Double.toString(resolutions[0][i])).append(",")
					.append(Double.toString(resolutions[1][i]));
			levelDirs.append(i == 0 ? "0" : Integer.toString((int) Math.pow(
					scaleFactor, i)));
			if (i < numSteps) {
				levels.append(" ");
				levelDirs.append(" ");
			}
		}
		properties.setProperty("Levels", levels.toString());
		properties.setProperty("LevelsDirs", levelDirs.toString());
		properties.setProperty("Name", name);
		try {
			properties.store(new BufferedOutputStream(new FileOutputStream(
					new File(outputLocation, name + ".properties"))), "");

			// //
			// Creating PRJ file
			// //
			final File prjFile = new File(outputLocation, name + ".prj");
			BufferedWriter out = new BufferedWriter(new FileWriter(prjFile));
			out.write(envelope.getCoordinateReferenceSystem().toWKT());
			out.close();
		} catch (FileNotFoundException e) {
			fireException(e);
		} catch (IOException e) {
			fireException(e);
		}
	}

	public void getNotification(ProcessingEvent event) {
		LOGGER.info(new StringBuffer("Progress is at ").append(
				event.getPercentage()).append("\n").append(
				"attached message is: ").append(event.getMessage()).toString());
	}

	public void exceptionOccurred(ExceptionEvent event) {
		LOGGER.log(Level.SEVERE, "An error occurred during processing", event
				.getException());
	}

	public boolean parseArgs(String[] args) {
		if (!super.parseArgs(args))
			return false;
		// ////////////////////////////////////////////////////////////////
		//
		// parsing command line parameters and setting up
		// Pyramid Builder options
		//
		// ////////////////////////////////////////////////////////////////
		inputLocation = new File((String) getOptionValue(locationOpt));

		// output files' directory
		if (hasOption(outputLocationOpt))
			outputLocation = new File(
					(String) getOptionValue(outputLocationOpt));
		else
			outputLocation = new File(inputLocation.getParentFile(), "pyramid");

		// output file name
		if (hasOption(nameOpt))
			name = (String) getOptionValue(nameOpt);
		else
			name = "pyramid";

		// shall we overwrite the output dirs?
		overwriteOutputDirs = hasOption(overwriteOpt);

		// tile dim
		final String tileDim = (String) getOptionValue(tileDimOpt);
		String[] pairs = tileDim.split(",");
		tileW = Integer.parseInt(pairs[0]);
		tileH = Integer.parseInt(pairs[1]);

		// //
		//
		// scale factor
		//
		// //
		final String scaleF = (String) getOptionValue(scaleFactorOpt);
		scaleFactor = Integer.parseInt(scaleF);

		// //
		//
		// scaling algorithm (default to nearest neighbour)
		//
		// //
		scaleAlgorithm = (String) getOptionValue(scaleAlgorithmOpt);
		if (scaleAlgorithm == null)
			scaleAlgorithm = "nn";

		// //
		//
		// number of steps
		//
		// //
		numSteps = Integer.parseInt((String) getOptionValue(numStepsOpt));

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
		} else {
			internalTileWidth = tileW;
			internalTileHeight = tileH;
		}

		return true;

	}

	public double getCompressionRatio() {
		return compressionRatio;
	}

	public String getCompressionScheme() {
		return compressionScheme;
	}

	public void setCompressionRatio(double compressionRatio) {
		this.compressionRatio = compressionRatio;
	}

	public void setCompressionScheme(String compressionScheme) {
		this.compressionScheme = compressionScheme;
	}

	public int getInternalTileHeight() {
		return internalTileHeight;
	}

	public int getInternalTileWidth() {
		return internalTileWidth;
	}

	public void setInternalTileHeight(int internalTileHeight) {
		this.internalTileHeight = internalTileHeight;
	}

	public void setInternalTileWidth(int internalTileWidth) {
		this.internalTileWidth = internalTileWidth;
	}

	/**
	 * This tool is designed to be used by the command line using this main
	 * class but it can also be used from an GUI by using the setters and
	 * getters.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IllegalArgumentException,
			IOException, InterruptedException {

		// creating an overviews embedder
		final PyramidBuilder builder = new PyramidBuilder();
		// adding the embedder itself as a listener
		builder.addProcessingEventListener(builder);
		// parsing input argumentBuilder
		if (builder.parseArgs(args)) {
			// creating a thread to execute the request process, with the
			// provided priority
			final Thread t = new Thread(builder, "PyramidBuilder");
			t.setPriority(builder.getPriority());
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}

		} else if (LOGGER.isLoggable(Level.FINE))
			LOGGER
					.fine("Unable to parse command line argumentBuilder, exiting...");

	}
}
