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

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageWriteParam;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.commons.cli2.validation.Validator;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.utils.CoverageToolsConstants;
import org.geotools.utils.progress.BaseArgumentsManager;
import org.geotools.utils.progress.ExceptionEvent;
import org.geotools.utils.progress.ProcessingEvent;
import org.geotools.utils.progress.ProcessingEventListener;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;

/**
 * <p>
 * Usage:<br/> <code>PyramidLayerBuilder -h -v -s -t -f -a -o -p -c</code>
 * </p>
 * 
 * <pre>
 *                                                                                                                                            
 *                         where:                                                                                                                   
 *                          -h : Prints a nice command line Help                                                                                    
 *                          -v : Prints the tools Version                                                                                           
 *                          -s : Is the path where the raster(s) is(are) located                                                                    
 *                          -t : Is the tile dimensions as a couple width,height in pixels (e.g. 512,512)                                           
 *                          -f : Represents the scale factor. If you want a raster which is 1/2 resolution                                          
 *                               of the original, f should be 2                                                                                     
 *                          -a : Represents the Scaling algorithm to use. You can choose among one of the following                                 
 *                               nn, bil, avg, filt                                                                                                 
 *                          -o : Represents the output format. It can be one of the following                                                       
 *                               tiff, tif, gtiff, gtif, png, jpeg                                                                                  
 *                          -p : Is the Thread Priority, a number between 1 and 10 -&gt; 1 [LOW] - 5 [MED] - 10 [HIGH]                              
 *                          -c : Represents the JAI TileCache dimension. This is an optional parameter which allows                                 
 *                               you to tune the tool performances.                                                                                 
 * </pre>
 * 
 * <p>
 * Example of usage:<br/>
 * <code>PyramidLayerBuilder -t "512,512" -s "/usr/home/tmp/tiled/world.200412.3x21600x21600.a1_ref.shp" -f 2 -a nn -c 512</code>
 * </p>
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Alessio Fabiani. GeoSolutions
 *
 * @source $URL$
 * @version 0.3
 * 
 */
public class PyramidLayerBuilder extends BaseArgumentsManager implements
		Runnable, ProcessingEventListener {

	/** Static immutable map for scaling algorithms. */
	private static Set<String> scalingAlgorithms;
	static {
		scalingAlgorithms = new HashSet<String>();
		scalingAlgorithms.add("nn");
		scalingAlgorithms.add("bil");
		scalingAlgorithms.add("avg");
		scalingAlgorithms.add("filt");
	}

	/** Static immutable ap for scaling algorithms. */
	private static Set<String> outputFormats;
	static {
		outputFormats = new HashSet<String>();
		outputFormats.add("tiff");
		outputFormats.add("tif");
		outputFormats.add("gtiff");
		outputFormats.add("gtif");
		outputFormats.add("png");
		outputFormats.add("jpeg");

	}

	/** Default Logger * */
	private final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger(PyramidLayerBuilder.class.toString());

	/** Program Version */
	private final static String VERSION = "0.3";

	private final static String NAME = "PyramidLayerBuilder";

	private Option inputLocationOpt;

	private Option outputLocationOpt;

	private Option tileDimOpt;

	private Option scaleAlgorithmOpt;

	private Option outFormatOpt;

	private double tileW;

	private double tileH;

	private File inputLocation;

	private File outputLocation;

	private String scaleAlgorithm;

	private String outputFormat;

	private Option scaleFactorOpt;

	private int scaleFactor;

	private Option compressionRatioOpt;

	private Option compressionTypeOpt;

	private Option internalTileDimOpt;

	private int internalTileWidth = CoverageToolsConstants.DEFAULT_INTERNAL_TILE_WIDTH;

	private int internalTileHeight = CoverageToolsConstants.DEFAULT_INTERNAL_TILE_HEIGHT;

	private String compressionScheme = CoverageToolsConstants.DEFAULT_COMPRESSION_SCHEME;

	private double compressionRatio = CoverageToolsConstants.DEFAULT_COMPRESSION_RATIO;

	public PyramidLayerBuilder() {
		super(NAME, VERSION);
		// /////////////////////////////////////////////////////////////////////
		// Options for the command line
		// /////////////////////////////////////////////////////////////////////
		inputLocationOpt = optionBuilder.withShortName("s").withLongName(
				"source_directory").withArgument(
				argumentBuilder.withName("source").withMinimum(1)
						.withMaximum(1).withValidator(new Validator() {

							public void validate(List args)
									throws InvalidArgumentException {
								final int size = args.size();
								if (size > 1)
									throw new InvalidArgumentException(
											"Only one location can be chosen");
								final File source = new File((String) args
										.get(0));
								if (!source.isFile() || !source.exists())
									throw new InvalidArgumentException(
											new StringBuilder(
													"The provided source is invalid! ")

											.toString());
							}

						}).create()).withDescription(
				"path where files are located").withRequired(true).create();

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
				"tiled_dimension").withArgument(
				argumentBuilder.withName("t").withMinimum(1).withMaximum(1)
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
													new StringBuilder(
															"The provided scale factor is negative! ")

													.toString());
										if (factor == 1) {
											LOGGER
													.warning("The scale factor is 1!");
											System.exit(0);
										}
									}

								}).create()).withDescription(
						"integer scale factor")
				.withRequired(true).create();

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
													new StringBuilder(
															"The output format ")
															.append(args.get(0))
															.append(
																	" is not permitted")
															.toString());

									}
								}).create())
				.withDescription(
						"name of the scaling algorithm, eeither one of average (a), filtered	 (f), bilinear (bil), nearest neigbhor (nn)")
				.withRequired(false).create();

		outFormatOpt = optionBuilder
				.withShortName("o")
				.withLongName("out_format")
				.withArgument(
						argumentBuilder.withName("o").withMinimum(0)
								.withMaximum(1)
								.withDescription("output format")
								// .withDefault("gtiff")
								.withValidator(new Validator() {

									public void validate(List args)
											throws InvalidArgumentException {
										final int size = args.size();
										if (size > 1)
											throw new InvalidArgumentException(
													"Only one output format at a time can be specified");
										if (!outputFormats
												.contains(args.get(0)))
											throw new InvalidArgumentException(
													new StringBuilder(
															"The output format ")
															.append(args.get(0))
															.append(
																	" is not permitted")
															.toString());

									}
								}).create()).withDescription("output format")
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
		
		internalTileDimOpt = optionBuilder.withShortName("it").withLongName(
				"internal_tile_dimension").withArgument(
				argumentBuilder.withName("it").withMinimum(0).withMaximum(1)
						.create()).withDescription(
				"Internal width and height of each tile we generate")
				.withRequired(false).create();

		addOption(compressionTypeOpt);
		addOption(compressionRatioOpt);
		addOption(inputLocationOpt);
		addOption(tileDimOpt);
		addOption(scaleFactorOpt);
		addOption(scaleAlgorithmOpt);
		addOption(internalTileDimOpt);

		// /////////////////////////////////////////////////////////////////////
		//
		// Help Formatter
		//
		// /////////////////////////////////////////////////////////////////////
		finishInitialization();
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IllegalArgumentException,
			IOException, InterruptedException {

		final PyramidLayerBuilder pyramidBuilder = new PyramidLayerBuilder();
		pyramidBuilder.addProcessingEventListener(pyramidBuilder);
		if (pyramidBuilder.parseArgs(args)) {
			final Thread t = new Thread(pyramidBuilder, "PyramidBuilder");
			t.setPriority(pyramidBuilder.getPriority());
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
		inputLocation = new File((String) getOptionValue(inputLocationOpt));

		// tile dim
		final String tileDim = (String) getOptionValue(tileDimOpt);
		final String[] pairs = tileDim.split(",");
		tileW = Integer.parseInt(pairs[0]);
		tileH = Integer.parseInt(pairs[1]);

		// //
		//
		// scale factor
		//
		// //
		final String scaleF = (String) getOptionValue(scaleFactorOpt);
		scaleFactor = Integer.parseInt(scaleF);

		// output files' directory
		if (hasOption(outputLocationOpt))
			outputLocation = new File(
					(String) getOptionValue(outputLocationOpt));
		else
			outputLocation = new File(inputLocation.getParentFile(), String
					.valueOf(scaleFactor));

		// //
		//
		// scaling algorithm
		//
		// //
		scaleAlgorithm = (String) getOptionValue(scaleAlgorithmOpt);
		if (scaleAlgorithm == null)
			scaleAlgorithm = "nn";

		// //

		// //
		//
		// output format
		//
		// //
		outputFormat = (String) getOptionValue(outFormatOpt);

		return true;

	}

	public void run() {

		// /////////////////////////////////////////////////////////////////////
		//
		//
		// PARSING INPUT PARAMETERS
		// 
		// 
		// /////////////////////////////////////////////////////////////////////
		StringBuilder message = new StringBuilder("Requested scale factor is ").append(scaleFactor);
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(message.toString());
		fireEvent(message.toString(), 0);

		// /////////////////////////////////////////////////////////////////////
		//
		//
		// Opening the base mosaic
		// 
		// 
		// /////////////////////////////////////////////////////////////////////
		// mosaic reader
		message = new StringBuilder("Acquiring a mosaic reader to mosaic ")
				.append(inputLocation);
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(message.toString());
		fireEvent(message.toString(), 0);

		ImageMosaicReader inReader = null;
		try {
			inReader = new ImageMosaicReader(inputLocation, new Hints(Hints.OVERVIEW_POLICY, OverviewPolicy.IGNORE));
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			fireException(e);
			return;
		}

		// /////////////////////////////////////////////////////////////////////
		//
		//
		// Preparing all the params
		// 
		// 
		// /////////////////////////////////////////////////////////////////////
		// output files' directory
		if (!outputLocation.exists())
			outputLocation.mkdir();

		// getting envelope and other information about dimension
		final GeneralEnvelope envelope = inReader.getOriginalEnvelope();
		message = new StringBuilder("Original envelope is ").append(envelope.toString());
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(message.toString());
		fireEvent(message.toString(), 0);

		final GridEnvelope range = inReader.getOriginalGridRange();
		message = new StringBuilder("Original range is ").append(range
				.toString());
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(message.toString());
		fireEvent(message.toString(), 0);

		// new number of rows and columns
		final double newWidth = (range.getSpan(0) * 1.0) / scaleFactor;
		final double newHeight = (range.getSpan(1) * 1.0) / scaleFactor;
		if (tileW > newWidth)
			tileW = newWidth;
		if (tileH > newHeight)
			tileH = newHeight;

		message = new StringBuilder("New dimension is (W,H)==(").append(newWidth).append(",").append(newHeight).append(")");
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(message.toString());
		fireEvent(message.toString(), 0);

		int newCols = (int) (newWidth / tileW);
		int newRows = (int) (newHeight / tileH);
		final boolean hasRemainingColum = (newWidth % tileW) != 0;
		final boolean hasRemainingRow = (newHeight % tileH) != 0;
		message = new StringBuilder("New matrix dimension is (cols,rows)==(").append(newCols).append(",").append(newRows).append(")");
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(message.toString());
		fireEvent(message.toString(), 0);

		final double minx = envelope.getMinimum(0);
		final double miny = envelope.getMinimum(1);
		final double maxx = envelope.getMaximum(0);
		final double maxy = envelope.getMaximum(1);

		double _maxx = 0.0;
		double _maxy = 0.0;
		double _minx = 0.0;
		double _miny = 0.0;

		
			
		

		// ///////////////////////////////////////////////////////////////////
		//
		// MAIN LOOP
		//
		//
		// ///////////////////////////////////////////////////////////////////
		newRows += hasRemainingRow ? 1 : 0;
		newCols += hasRemainingColum ? 1 : 0;
		final double totalNumberOfFile = newRows * newCols;

		// getting resolution of each tile
		final double tileGeoWidth = envelope.getSpan(0) / newCols;
		final double tileGeoHeight = envelope.getSpan(1) / newRows;

		final int uppers[] = range.getHigh().getCoordinateValues();
		uppers[0] ++;
		uppers[1] ++;
		final double newRange[] = new double[] { uppers[0] / newCols,uppers[1] / newRows };
		final CoverageProcessor processor = CoverageProcessor.getInstance();
		for (int i = 0; i < newRows; i++)
			for (int j = 0; j < newCols; j++) {

				// //
				//
				// computing the bbox for this tile
				//
				// //
				_maxx = minx + (j + 1) * tileGeoWidth;
				_minx = minx + (j) * tileGeoWidth;
				_maxy = miny + (i + 1) * tileGeoHeight;
				_miny = miny + (i) * tileGeoHeight;
				if (_maxx > maxx)
					_maxx = maxx;
				if (_maxy > maxy)
					_maxy = maxy;

				// //
				//
				// creating the output file
				//
				// //
				final File fileOut = new File(outputLocation, new StringBuilder(
						"mosaic").append("_").append(
						Integer.toString(i * newCols + j)).append(".").append(
						"tiff").toString());
				if (fileOut.exists())
					fileOut.delete();

				message = new StringBuilder("Preparing tile (col,row)==(")
						.append(j)
						.append(",")
						.append(i)
						.append(") to file ")
						.append(fileOut);
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.fine(message.toString());
				fireEvent(message.toString(), (j + i * newCols)/ totalNumberOfFile);

				// //
				//
				// building gridgeometry for the read operation
				//
				// //
				final ParameterValue<GridGeometry2D> gg =  ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
				final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] { _minx, _miny }, new double[] { _maxx,_maxy });
				cropEnvelope.setCoordinateReferenceSystem(inReader.getCrs());
				//we need to supply the requeste grid range but we use a fake one since we are using the ignore overviews switch 
				gg.setValue(new GridGeometry2D(new GridEnvelope2D(new Rectangle(0, 0, 800, 800)), cropEnvelope));
				message = new StringBuilder("Reading with grid envelope ").append(cropEnvelope.toString());
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.fine(message.toString());
				fireEvent(message.toString(), (j + i * newCols)
						/ totalNumberOfFile);

				// //
				//
				// read the needed part and then crop to be sure that we have what we need
				//
				// //
				GridCoverage2D gc;
				try {
					gc = (GridCoverage2D) inReader
							.read(new GeneralParameterValue[] { gg });


				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
					fireEvent(e.getLocalizedMessage(), 0);
					return;
				}

				ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
				param.parameter("Source").setValue(gc);
				param.parameter("Envelope").setValue(cropEnvelope);
				param.parameter("ConserveEnvelope").setValue(Boolean.TRUE);
				final GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);

				// //
				//
				// Adjusting the resolution in order to be the same as for all the others coverage
				//
				// //
				final GridEnvelope2D newGridrange = new GridEnvelope2D(new Rectangle2D.Double(0.0, 0.0, newRange[0],newRange[1]).getBounds());
				final GridGeometry2D scaledGridGeometry = new GridGeometry2D(newGridrange, cropEnvelope);
				param = processor.getOperation("Resample").getParameters();
				param.parameter("Source").setValue(cropped);
				param.parameter("CoordinateReferenceSystem").setValue(inReader.getCrs());
				param.parameter("GridGeometry").setValue(scaledGridGeometry);
				param.parameter("InterpolationType").setValue(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
				gc = (GridCoverage2D) processor.doOperation(param);


				message = new StringBuilder("Scaling...");
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.fine(message.toString());
				fireEvent(message.toString(), 0);

				if (scaleAlgorithm.equalsIgnoreCase("nn")) {
					param = processor.getOperation("Scale").getParameters();
					param.parameter("Source").setValue(gc);
					param.parameter("xScale").setValue(new Float(1.0 / scaleFactor));
					param.parameter("yScale").setValue(new Float(1.0 / scaleFactor));
					param.parameter("xTrans").setValue(new Float(0));
					param.parameter("yTrans").setValue(new Float(0));
					param.parameter("Interpolation").setValue(Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
					gc = (GridCoverage2D) CoverageToolsConstants.SCALE_FACTORY.doOperation(param, new Hints());
				} else if (scaleAlgorithm.equalsIgnoreCase("filt")) {
					// scaling
					param =  CoverageToolsConstants.FILTERED_SUBSAMPLE_FACTORY.getParameters();
					param.parameter("source").setValue(gc);
					param.parameter("scaleX").setValue(new Integer((int) scaleFactor));
					param.parameter("scaleY").setValue(new Integer((int) scaleFactor));
					param.parameter("qsFilterArray").setValue(new float[] { 0.5F, 1.0F / 3.0F, 0.0F,-1.0F / 12.0F });
					param.parameter("Interpolation").setValue(new InterpolationNearest());
					gc = (GridCoverage2D) CoverageToolsConstants.FILTERED_SUBSAMPLE_FACTORY.doOperation(param, new Hints());
				} else if (scaleAlgorithm.equalsIgnoreCase("bil")) {
					param = processor.getOperation("Scale").getParameters();
					param.parameter("Source").setValue(gc);
					param.parameter("xScale").setValue(
							new Float(1.0 / scaleFactor));
					param.parameter("yScale").setValue(
							new Float(1.0 / scaleFactor));
					param.parameter("xTrans").setValue(new Float(0));
					param.parameter("yTrans").setValue(new Float(0));
					param.parameter("Interpolation").setValue(Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
					gc = (GridCoverage2D) CoverageToolsConstants.SCALE_FACTORY.doOperation(param, new Hints());
				} else if (scaleAlgorithm.equalsIgnoreCase("avg")) {
					param = processor.getOperation("SubsampleAverage").getParameters();
					param.parameter("Source").setValue(gc);
					param.parameter("scaleX").setValue(new Double(1.0 / scaleFactor));
					param.parameter("scaleY").setValue(new Double(1.0 / scaleFactor));
					param.parameter("Interpolation").setValue(scaleFactor);
					gc = (GridCoverage2D) CoverageToolsConstants.SUBSAMPLE_AVERAGE_FACTORY.doOperation(param, new Hints());
				} else 
					throw new IllegalArgumentException(
							"The provided scale algorithm is not availaible");


				// //
				//
				// Writing out this coverage
				//
				// //
				message = new StringBuilder("Writing out...");
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.fine(message.toString());
				fireEvent(message.toString(), (j + i * newCols)
						/ totalNumberOfFile);

				try {

					final GeoTiffWriter writerWI = new GeoTiffWriter(fileOut);
					final GeoToolsWriteParams wp = ((AbstractGridFormat) writerWI
							.getFormat()).getDefaultImageIOWriteParameters();
					if (this.compressionScheme != null) {
						wp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
						wp.setCompressionType(this.compressionScheme);
						wp.setCompressionQuality((float) this.compressionRatio);
					}
					wp.setTilingMode(ImageWriteParam.MODE_EXPLICIT);
					wp.setTiling(internalTileWidth, internalTileHeight, 0, 0);
					writerWI.write(gc, null);
					writerWI.dispose();

				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
					fireEvent(e.getLocalizedMessage(), 0);
					return;
				}

			}

		message = new StringBuilder("Done...");
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine(message.toString());
		fireEvent(message.toString(), 100);

	}

	public void getNotification(ProcessingEvent event) {
		LOGGER.info(new StringBuilder("Progress is at ").append(
				event.getPercentage()).append("\n").append(
				"attached message is: ").append(event.getMessage()).toString());
	}

	public void exceptionOccurred(ExceptionEvent event) {
		LOGGER.log(Level.SEVERE, "An error occurred during processing", event
				.getException());
	}

	public void setInputLocation(File inputLocation) {
		this.inputLocation = inputLocation;
	}

	public void setOutputLocation(File outputLocation) {
		this.outputLocation = outputLocation;
	}

	/**
	 * @return the outputFormat
	 */
	public String getOutputFormat() {
		return outputFormat;
	}

	/**
	 * @param outputFormat
	 *            the outputFormat to set
	 */
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	/**
	 * @return the scaleAlgorithm
	 */
	public String getScaleAlgorithm() {
		return scaleAlgorithm;
	}

	/**
	 * @param scaleAlgorithm
	 *            the scaleAlgorithm to set
	 */
	public void setScaleAlgorithm(String scaleAlgorithm) {
		this.scaleAlgorithm = scaleAlgorithm;
	}

	/**
	 * @return the scaleFactor
	 */
	public int getScaleFactor() {
		return scaleFactor;
	}

	/**
	 * @param scaleFactor
	 *            the scaleFactor to set
	 */
	public void setScaleFactor(int scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public File getInputLocation() {
		return inputLocation;
	}

	public File getOutputLocation() {
		return outputLocation;
	}

	/**
	 * @return the tileH
	 */
	public double getTileHeight() {
		return tileH;
	}

	/**
	 * @param tileH
	 *            the tileH to set
	 */
	public void setTileHeight(int tileH) {
		this.tileH = tileH;
	}

	/**
	 * @return the tileW
	 */
	public double getTileWidth() {
		return tileW;
	}

	/**
	 * @param tileW
	 *            the tileW to set
	 */
	public void setTileWidth(int tileW) {
		this.tileW = tileW;
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

}
