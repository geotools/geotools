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
package org.geotools.utils.imageoverviews;

import it.geosolutions.imageio.plugins.tiff.BaselineTIFFTagSet;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageMetadata;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriter;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.TileCache;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.commons.cli2.validation.Validator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.geotools.image.io.ImageIOExt;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.utils.CoverageToolsConstants;
import org.geotools.utils.WriteProgressListenerAdapter;
import org.geotools.utils.progress.BaseArgumentsManager;
import org.geotools.utils.progress.ExceptionEvent;
import org.geotools.utils.progress.ProcessingEvent;
import org.geotools.utils.progress.ProcessingEventListener;


/**
 * <pre>
 *  Example of usage:
 * <code>
 * OverviewsEmbedder -s &quot;/usr/home/tmp&quot; -w *.tiff -t &quot;512,512&quot; -f 32 -n 8 -a nn -c 512
 * </code>
 *  &lt;pre&gt;
 *  
 * <p>
 *  HINT: Take more memory as the 64Mb default by using the following Java Options&lt;BR/&gt;
 * <code>
 * -Xmx1024M - Xms512M
 * </code>
 * </p>
 *  &#064;author Simone Giannecchini (GeoSolutions)
 *  &#064;author Alessio Fabiani (GeoSolutions)
 *  &#064;since 2.3.x
 *  &#064;version 0.4
 * 
 *
 * @source $URL$
 */
public class OverviewsEmbedder extends BaseArgumentsManager implements Runnable, ProcessingEventListener {

	public static enum SubsampleAlgorithm{

		Nearest, Bilinear, Bicubic, Average, Filtered; 

//		public abstract RenderedImage scale(); 
	}
	/**
	 * 
	 * @author Simone Giannecchini
	 * @since 2.3.x
	 * 
	 */
	private class OverviewsEmbedderWriteProgressListener extends
			WriteProgressListenerAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.geosolutions.pyramids.DefaultWriteProgressListener#imageComplete(javax.imageio.ImageWriter)
		 */
		public void imageComplete(ImageWriter source) {

			OverviewsEmbedder.this.fireEvent(new StringBuilder(
					"Started with writing out overview number ").append(
					overviewInProcess + 1.0).toString(),
					(overviewInProcess + 1 / numSteps) * 100.0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.geosolutions.pyramids.DefaultWriteProgressListener#imageProgress(javax.imageio.ImageWriter,
		 *      float)
		 */
		public void imageProgress(ImageWriter source, float percentageDone) {
			OverviewsEmbedder.this.fireEvent(new StringBuilder(
					"Writing out overview ").append(overviewInProcess + 1)
					.toString(), (overviewInProcess / numSteps + percentageDone
					/ (100 * numSteps)) * 100.0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.geosolutions.pyramids.DefaultWriteProgressListener#imageStarted(javax.imageio.ImageWriter,
		 *      int)
		 */
		public void imageStarted(ImageWriter source, int imageIndex) {
			OverviewsEmbedder.this.fireEvent(new StringBuilder(
					"Completed writing out overview number ").append(
					overviewInProcess + 1).toString(), (overviewInProcess)
					/ numSteps * 100.0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.geosolutions.pyramids.DefaultWriteProgressListener#warningOccurred(javax.imageio.ImageWriter,
		 *      int, java.lang.String)
		 */
		public void warningOccurred(ImageWriter source, int imageIndex,
				String warning) {
			OverviewsEmbedder.this.fireEvent(new StringBuilder(
					"Warning at overview ").append((overviewInProcess + 1))
					.toString(), 0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.geosolutions.pyramids.DefaultWriteProgressListener#writeAborted(javax.imageio.ImageWriter)
		 */
		public void writeAborted(ImageWriter source) {
			OverviewsEmbedder.this.fireEvent(new StringBuilder(
					"Aborted writing process.").toString(), 100.0);
		}
	}

	/**
	 * The default listener for checking the progress of the writing process.
	 */
	private final OverviewsEmbedderWriteProgressListener writeProgressListener = new OverviewsEmbedderWriteProgressListener();

	private final static String NAME = "OverviewsEmbedder";

	/** Program Version */
	private final static String VERSION = "0.3";

	/** Commons-cli option for the input location. */
	private Option locationOpt;

	/** Commons-cli option for the tile dimension. */
	private Option tileDimOpt;

	/** Commons-cli option for the scale algorithm. */
	private Option scaleAlgorithmOpt;

	/** Commons-cli option for the wild card to use. */
	private Option wildcardOpt;

	/** Commons-cli option for the tile numbe of subsample step to use. */
	private Option numStepsOpt;

	/** Commons-cli option for the scale factor to use. */
	private Option scaleFactorOpt;

	private Option compressionRatioOpt;

	private Option compressionTypeOpt;

	/** Tile width. */
	private int tileW = -1;

	/** Tile height. */
	private int tileH = -1;

	/** Scale algorithm. */
	private String scaleAlgorithm;

	/** Logger for this class. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger(OverviewsEmbedder.class.toString());

	/** Default border extender. */
	private BorderExtender borderExtender = CoverageToolsConstants.DEFAULT_BORDER_EXTENDER;

	/** Downsampling step. */
	private int downsampleStep;

	/** Low pass filter. */
	private float[] lowPassFilter = CoverageToolsConstants.DEFAULT_KERNEL_GAUSSIAN;

	/**
	 * The source path. It could point to a single file or to a directory when
	 * we want to embed overwies into a set of files having a certain name.
	 */
	private String sourcePath;

	private String compressionScheme = null;

	private double compressionRatio = CoverageToolsConstants.DEFAULT_COMPRESSION_RATIO;

	private int numSteps;

	private String wildcardString = "*.*";

	private int fileBeingProcessed;

	private int overviewInProcess;



	/**
	 * Simple constructor for a pyramid generator. Use the input string in order
	 * to read an image.
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public OverviewsEmbedder() {
		super(NAME, VERSION);
		// /////////////////////////////////////////////////////////////////////
		// Options for the command line
		// /////////////////////////////////////////////////////////////////////
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
											throw new InvalidArgumentException("Source can be a single file or  directory ");
										final File source = new File(
												(String) args.get(0));
										if (!source.exists())
											throw new InvalidArgumentException(
													new StringBuilder(
															"The provided source is invalid! ")

													.toString());
									}

								}).create()).withDescription(
						"path where files are located").withRequired(true)
				.create();

		tileDimOpt = optionBuilder.withShortName("t").withLongName(
				"tiled_dimension").withArgument(
				argumentBuilder.withName("t").withMinimum(0).withMaximum(1)
						.create()).withDescription(
				"tile dimensions as a couple width,height in pixels")
				.withRequired(false).create();

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
													"Only one scale factor at a time can be chosen");
										int factor = Integer
												.parseInt((String) args.get(0));
										if (factor <= 0)
											throw new InvalidArgumentException(
													new StringBuilder(
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

		wildcardOpt = optionBuilder.withShortName("w").withLongName(
				"wildcardOpt").withArgument(
				argumentBuilder.withName("wildcardOpt").withMinimum(0)
						.withMaximum(1).create()).withDescription(
				"wildcardOpt to use for selecting files").withRequired(false)
				.create();

		numStepsOpt = optionBuilder.withShortName("n")
				.withLongName("num_steps").withArgument(
						argumentBuilder.withName("n").withMinimum(1)
								.withMaximum(1).withValidator(new Validator() {

									public void validate(List args)
											throws InvalidArgumentException {
										final int size = args.size();
										if (size > 1)
											throw new InvalidArgumentException(
													"Only one  number of step at a time can be chosen");
										int steps = Integer
												.parseInt((String) args.get(0));
										if (steps <= 0)
											throw new InvalidArgumentException(
													new StringBuilder(
															"The provided number of step is negative! ")

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
										final SubsampleAlgorithm algorithm=SubsampleAlgorithm.valueOf((String)args.get(0)); 
										if (algorithm!=null)
											throw new InvalidArgumentException(
													new StringBuilder(
															"The scaling algorithm ")
															.append(args.get(0))
															.append(
																	" is not supported")
															.toString());

									}
								}).create())
				.withDescription(
						"name of the scaling algorithm, eeither one of average (a), filtered	 (f), bilinear (bil), nearest neigbhor (nn)")
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

		addOption(locationOpt);
		addOption(tileDimOpt);
		addOption(scaleFactorOpt);
		addOption(scaleAlgorithmOpt);
		addOption(numStepsOpt);
		addOption(wildcardOpt);
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
	 * This method retiles the original image using a specified tile width and
	 * height.
	 * 
	 * @param Original
	 *            image to be tiled or retiled.
	 * @param tileWidth
	 *            Tile width.
	 * @param tileHeight
	 *            Tile height.
	 * @param tileGrdiOffseX
	 * @param tileGrdiOffseY
	 * @param interpolation
	 *            Interpolation method used.
	 * 
	 * @return RenderedOp containing the chain to obtain the tiled image.
	 */
	private static ImageLayout createTiledLayout(
			final int tileWidth, 
			final int tileHeight,
			final int tileGrdiOffseX, 
			final int tileGrdiOffseY) {

		// //
		//
		// creating a new layout for this image
		// using tiling
		//
		// //
		ImageLayout layout = new ImageLayout();

		// //
		//
		// changing parameters related to the tiling
		//
		//
		// //
		layout.setTileGridXOffset(tileGrdiOffseX);
		layout.setTileGridYOffset(tileGrdiOffseY);
		layout.setValid(ImageLayout.TILE_GRID_X_OFFSET_MASK);
		layout.setValid(ImageLayout.TILE_GRID_Y_OFFSET_MASK);
		layout.setTileWidth(tileWidth);
		layout.setTileHeight(tileHeight);
		layout.setValid(ImageLayout.TILE_HEIGHT_MASK);
		layout.setValid(ImageLayout.TILE_WIDTH_MASK);

		return layout;
	}

	/**
	 * This methods built up a RenderedOp for subsampling an image in order to
	 * create various previes. I wanted to use the filtered subsample but It was
	 * giving me problems in the native libraries therefore I am doing a two
	 * steps downsampling:
	 * 
	 * Step 1: low pass filtering.
	 * 
	 * Step 2: Subsampling.
	 * 
	 * @param src
	 *            Image to subsample.
	 * @param scaleTC 
	 * @param scale
	 *            Scale factor.
	 * @param interpolation
	 *            Interpolation method used.
	 * @param tileHints
	 *            Hints provided.
	 * 
	 * @return The subsampled RenderedOp.
	 */
	private RenderedOp subsample(RenderedOp src, TileCache scaleTC, final Interpolation interpolation) {
		final RenderingHints newHints = new RenderingHints(JAI.KEY_TILE_CACHE,scaleTC);
		newHints.add(ImageUtilities.DONT_REPLACE_INDEX_COLOR_MODEL);
		newHints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER, this.borderExtender));
		
		// using filtered subsample operator to do a subsampling
		final ParameterBlockJAI pb = new ParameterBlockJAI("filteredsubsample");
		pb.addSource(src);
		pb.setParameter("scaleX", new Integer(downsampleStep));
		pb.setParameter("scaleY", new Integer(downsampleStep));
		pb.setParameter("qsFilterArray", new float[] { 1.0f });
		pb.setParameter("Interpolation", interpolation);
		// remember to add the hint to avoid replacement of the original
		// IndexColorModel
		// in future versions we might want to make this parametrix XXX TODO
		// @task
		return JAI.create("filteredsubsample", pb, newHints);
	}

	public int getDownsampleStep() {
		return downsampleStep;
	}

	public void setDownsampleStep(int downsampleWH) {
		this.downsampleStep = downsampleWH;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;

	}

	public int getTileHeight() {
		return tileH;
	}

	public void setTileHeight(int tileHeight) {
		this.tileH = tileHeight;
	}

	public int getTileWidth() {
		return tileW;
	}

	public void setTileWidth(int tileWidth) {
		this.tileW = tileWidth;
	}

	/**
	 * Creating the scale operation using the FilteredSubSample operation with a
	 * null filter, which basically does not do any filtering. This is a hint I
	 * found on the JAI mailing list, a SUN engineer suggested to use this
	 * instead of scale since it uses a integer factor which is easier for the
	 * library to handle than a float scale factor like Scale operation is
	 * using.
	 * 
	 * @param src
	 *            Source image to be scaled.
	 * @param scaleTC 
	 * @param factor
	 *            Scale factor.
	 * @param interpolation
	 *            Interpolation used.
	 * @param hints
	 *            Hints provided to this method.
	 * 
	 * @return The scaled image.
	 */
	private RenderedOp filteredSubsample(RenderedImage src, TileCache scaleTC) {
		final RenderingHints newHints = new RenderingHints(JAI.KEY_TILE_CACHE,scaleTC);
		newHints.add(ImageUtilities.DONT_REPLACE_INDEX_COLOR_MODEL);
		
		// using filtered subsample operator to do a subsampling
		final ParameterBlockJAI pb = new ParameterBlockJAI("filteredsubsample");
		pb.addSource(src);
		pb.setParameter("scaleX", new Integer(downsampleStep));
		pb.setParameter("scaleY", new Integer(downsampleStep));
		pb.setParameter("qsFilterArray", lowPassFilter);
		pb.setParameter("Interpolation", new InterpolationNearest());
		return JAI.create("filteredsubsample", pb,newHints);
	}

	/**
	 * Creating the scale operation using the FilteredSubSample operation with a
	 * null filter, which basically does not do any filtering. This is a hint I
	 * found on the JAI mailing list, a SUN engineer suggested to use this
	 * instead of scale since it uses a integer factor which is easier for the
	 * library to handle than a float scale factor like Scale operation is
	 * using.
	 * 
	 * @param src
	 *            Source image to be scaled.
	 * @param scaleTC 
	 * @param factor
	 *            Scale factor.
	 * @param interpolation
	 *            Interpolation used.
	 * @param hints
	 *            Hints provided to this method.
	 * 
	 * @return The scaled image.
	 */
	private RenderedOp scaleAverage(RenderedImage src, TileCache scaleTC) {
		final RenderingHints newHints = new RenderingHints(JAI.KEY_TILE_CACHE,scaleTC);
		newHints.add(ImageUtilities.DONT_REPLACE_INDEX_COLOR_MODEL);
		newHints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER, this.borderExtender));
		// using filtered subsample operator to do a subsampling
		final ParameterBlockJAI pb = new ParameterBlockJAI("SubsampleAverage");
		pb.addSource(src);
		pb.setParameter("scaleX", new Double(1.0 / downsampleStep));
		pb.setParameter("scaleY", new Double(1.0 / downsampleStep));
		return JAI.create("SubsampleAverage", pb, newHints);
	}

	public void setBorderExtender(BorderExtender borderExtender) {
		this.borderExtender = borderExtender;
	}


	public float[] getLowPassFilter() {
		return lowPassFilter;
	}

	public void setLowPassFilter(float[] lowPassFilter) {
		this.lowPassFilter = lowPassFilter;
	}

public void run() {

		//
		// CHECK INPUT DIRECTORIES/FILES
		//
		if(sourcePath==null)
		{
			fireEvent("Provided sourcePath is null", 0);
			return;
		}
		// getting an image input stream to the file
		final File file = new File(sourcePath);
		final File[] files;
		int numFiles = 1;
		StringBuilder message;
		if(!file.canRead()||!file.exists())
		{
			fireEvent("Provided file "+file.getAbsolutePath()+" cannot be read or does not exist", 0);
			return;
		}
		if (file.isDirectory()) {
			if(wildcardString==null){
				fireEvent("Provided wildcardString is null", 0);
				return;
			}
			final FileFilter fileFilter = new WildcardFileFilter(wildcardString);
			files = file.listFiles(fileFilter);
			numFiles = files.length;
			if (numFiles <= 0) {
				message = new StringBuilder("No files to process!");
				if (LOGGER.isLoggable(Level.FINE)) {
					LOGGER.fine(message.toString());
				}
				fireEvent(message.toString(), 100);

			}

		} else
			files = new File[] { file };
		
		if(files==null||files.length==0)
		{
			fireEvent("Unable to find input files for the provided wildcard "+wildcardString+ " and input path "+sourcePath,0);
			return;
		}

		//
		// ADDING OVERVIEWS TO ALL FOUND FILES
		//
		for (fileBeingProcessed = 0; fileBeingProcessed < numFiles; fileBeingProcessed++) {

			message = new StringBuilder("Managing file  ").append(fileBeingProcessed).append(" of ").append(files[fileBeingProcessed]).append(" files");
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine(message.toString());
			}
			fireEvent(message.toString(),((fileBeingProcessed * 100.0) / numFiles));

			if (getStopThread()) {
				message = new StringBuilder("Stopping requested at file  ").append(fileBeingProcessed).append(" of ").append(numFiles).append(" files");
				if (LOGGER.isLoggable(Level.FINE)) {
					LOGGER.fine(message.toString());
				}
				fireEvent(message.toString(),((fileBeingProcessed * 100.0) / numFiles));
				return;
			}

			ImageInputStream stream=null;
			ImageWriter writer =null;
			ImageOutputStream streamOut=null;
			TileCache baseTC= null;			
			TileCache scaleTC=null;
			RenderedOp currentImage = null;
			RenderedOp newImage=null;				
			try{

				//
				// get a stream
				//
				stream = ImageIO.createImageInputStream(files[fileBeingProcessed]);
				if(stream==null)
				{
					
					message = new StringBuilder("Unable to create an input stream for file").append(files[fileBeingProcessed]);
					if (LOGGER.isLoggable(Level.SEVERE)) {
						LOGGER.severe(message.toString());
					}
					fireEvent(message.toString(),((fileBeingProcessed * 100.0) / numFiles));
					break;
				}
				stream.mark();

	
				//
				// get a reader
				//
				final Iterator<ImageReader> it = ImageIO.getImageReaders(stream);
				if (!it.hasNext()) {
					message = new StringBuilder("Unable to find a reader for file").append(files[fileBeingProcessed]);
					if (LOGGER.isLoggable(Level.SEVERE)) {
						LOGGER.severe(message.toString());
					}
					fireEvent(message.toString(),((fileBeingProcessed * 100.0) / numFiles));
					break;
				}
				final ImageReader reader = (ImageReader) it.next();
				stream.reset();
				stream.mark();
				// is it a geotiff reader or not?
				if(!reader.getFormatName().toLowerCase().startsWith("tif")){
                                    if (LOGGER.isLoggable(Level.INFO)) {
                                        LOGGER.info("Discarding input file "+files[fileBeingProcessed] + " since it is not a proper tif file.");
                                    }
				    continue;
				}


				//
				// set input
				//
				reader.setInput(stream);
				ImageLayout layout = null;
				// tiling the image if needed
				int actualTileW = reader.getTileWidth(0);
				int actualTileH = reader.getTileHeight(0);
				final int numImages = reader.getNumImages(true);
				if (!reader.isImageTiled(0)||
						(reader.isImageTiled(0) && (actualTileH != tileH  && tileH != -1)|| (actualTileW != tileW && tileW != -1))) {

					message = new StringBuilder("Retiling image  ").append(fileBeingProcessed);
					if (LOGGER.isLoggable(Level.FINE)) {
						LOGGER.fine(message.toString());
					}
					fireEvent(message.toString(),
							((fileBeingProcessed * 100.0) / numFiles));
					layout = createTiledLayout(tileW, tileH, 0, 0);
				}
				stream.reset();
				reader.reset();
				reader.dispose();

				//
				// output image stream
				//
				streamOut = ImageIOExt.createImageOutputStream(null, files[fileBeingProcessed]);
				if (streamOut == null) {
					message = new StringBuilder(
							"Unable to acquire an ImageOutputStream for the file ")
							.append(files[fileBeingProcessed].toString());
					if (LOGGER.isLoggable(Level.SEVERE)) {
						LOGGER.severe(message.toString());
					}
					fireEvent(message.toString(),
							((fileBeingProcessed * 100.0) / numFiles));
					break;
				}


				//
				// Preparing to write the set of images. First of all I write
				// the first image `
				//
				// getting a writer for this reader
				writer = new TIFFImageWriterSpi().createWriterInstance();//ImageIO.getImageWriter(reader);
				writer.setOutput(streamOut);
				writer.addIIOWriteProgressListener(writeProgressListener);
				writer.addIIOWriteWarningListener(writeProgressListener);
				ImageWriteParam param = writer.getDefaultWriteParam();

				// can we tile this image? (TIFF or JPEG2K)
				if (!(param.canWriteTiles())) {
					message = new StringBuilder("This format do not support tiling!");
					if (LOGGER.isLoggable(Level.SEVERE)) {
						LOGGER.severe(message.toString());
					}
					fireEvent(message.toString(),((fileBeingProcessed * 100.0) / numFiles));
					break;
				}

				// can we write a sequence for these images?
				if (!(writer.canInsertImage(numImages))) {
					message = new StringBuilder("This format do not support overviews!");
					if (LOGGER.isLoggable(Level.SEVERE)) {
						LOGGER.severe(message.toString());
					}
					fireEvent(message.toString(),((fileBeingProcessed * 100.0) / numFiles));
					break;

				}

				//
				// setting tiling on the first image using writing parameters
				//
				if (tileH != -1 & tileW != -1) {
					param.setTilingMode(ImageWriteParam.MODE_EXPLICIT);
					param.setTiling(tileW, tileH, 0, 0);

				} else {
					param.setTilingMode(ImageWriteParam.MODE_EXPLICIT);
					param.setTiling(actualTileW, actualTileH, 0, 0);
				}
				if (this.compressionScheme != null && !Double.isNaN(compressionRatio)) {
					param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
					param.setCompressionType(compressionScheme);
					param.setCompressionQuality((float) this.compressionRatio);
				}

				//
				// creating the image to use for the successive
				// subsampling
				//
				final long tilecacheSize=this.getTileCacheSize()<=0?DEFAULT_TILE_CACHE_SIZE:super.getTileCacheSize();
				baseTC= JAI.createTileCache();
				baseTC.setMemoryCapacity(tilecacheSize/2);
				baseTC.setMemoryThreshold(1.0f);

				
				scaleTC= JAI.createTileCache();
				scaleTC.setMemoryCapacity(tilecacheSize/2);
				scaleTC.setMemoryThreshold(1.0f);
				
				final RenderingHints newHints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
				newHints.add(new RenderingHints(JAI.KEY_TILE_CACHE,baseTC));
				
				// read base image
				ParameterBlock pbjRead = new ParameterBlock();
				pbjRead.add(stream);
				pbjRead.add(Integer.valueOf(0));
				pbjRead.add(Boolean.FALSE);
				pbjRead.add(Boolean.FALSE);
				pbjRead.add(Boolean.FALSE);
				pbjRead.add(null);
				pbjRead.add(null);
				pbjRead.add(null);
				pbjRead.add(null);
				currentImage = JAI.create("ImageRead", pbjRead,newHints);
				message = new StringBuilder("Read original image  ").append(fileBeingProcessed);
				if (LOGGER.isLoggable(Level.FINE)) {
					LOGGER.fine(message.toString());
				}
				fireEvent(message.toString(),((fileBeingProcessed * 100.0) / numFiles));
				for (overviewInProcess = 0; overviewInProcess < numSteps; overviewInProcess++) {

					message = new StringBuilder("Subsampling step ").append(overviewInProcess).append(" of image  ").append(fileBeingProcessed);
					if (LOGGER.isLoggable(Level.FINE)) {
						LOGGER.fine(message.toString());
					}
					fireEvent(message.toString(),((fileBeingProcessed * 100.0) / numFiles));

					// paranoiac check
					if (currentImage.getWidth() / downsampleStep <= 0|| currentImage.getHeight() / downsampleStep <= 0)
						break;

					// SCALE
					
					// subsampling the input image using the chosen algorithm
					final SubsampleAlgorithm algorithm=SubsampleAlgorithm.valueOf(scaleAlgorithm);
					switch (algorithm) {
					case Average:
						newImage = scaleAverage(currentImage,scaleTC);
						break;
					case Filtered:
						newImage = filteredSubsample(currentImage,scaleTC);
						break;
					case Bilinear:
						newImage = subsample(currentImage,scaleTC,new InterpolationBilinear());
						break;	
					case Bicubic:
						newImage = subsample(currentImage,scaleTC,new InterpolationBicubic(2));
						break;
					case Nearest:
						newImage = subsample(currentImage,scaleTC, new InterpolationNearest());
						break;						
					default:
						throw new IllegalArgumentException("Invalid scaling algorithm "+scaleAlgorithm);//cannot get here
						
					}
						
					
					//set relevant metadata
					IIOMetadata imageMetadata = null;                                        
                    if (writer instanceof TIFFImageWriter){
                       imageMetadata = writer.getDefaultImageMetadata(new ImageTypeSpecifier(newImage), param);
                       if (imageMetadata != null)
                            ((TIFFImageMetadata)imageMetadata).addShortOrLongField(BaselineTIFFTagSet.TAG_NEW_SUBFILE_TYPE, BaselineTIFFTagSet.NEW_SUBFILE_TYPE_REDUCED_RESOLUTION);
                    }
                    // write out
                    writer.writeInsert(-1, new IIOImage(newImage, null, imageMetadata), param);

					message = new StringBuilder("Step ").append(
							overviewInProcess).append(" of image  ").append(
							fileBeingProcessed).append(" done!");
					if (LOGGER.isLoggable(Level.FINE)) {
						LOGGER.fine(message.toString());
					}
					fireEvent(message.toString(),
							((fileBeingProcessed * 100.0) / numFiles));

					// flushing cache on the old image which we are not going to use anymore
					baseTC.flush();
					
					// switching caches and images
					final TileCache appo= baseTC;
					baseTC=scaleTC;
					scaleTC=appo;
					currentImage = newImage;


				}
				
				// close message
				message = new StringBuilder("Done with  image  ")
						.append(fileBeingProcessed);
				if (LOGGER.isLoggable(Level.FINE)) {
					LOGGER.fine(message.toString());
				}
				fireEvent(message.toString(),
						(((fileBeingProcessed + 1) * 100.0) / numFiles));
			}catch (Throwable e) {
				fireException(e);
			}finally{
				// clean up
				
				// clean caches
				if(baseTC!=null)
				    try{
				        baseTC.flush();
				    } catch (Exception e) {
                                    }
				if(scaleTC!=null)
                                    try{
                                        scaleTC.flush();
                                    } catch (Exception e) {
                                    }
				
				//
				// free everything
				try {
					if(streamOut!=null)
					    streamOut.flush();
				} catch (Throwable e) {
					if(LOGGER.isLoggable(Level.FINE))
						LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
				}
				try {
				    if(streamOut!=null)
                                        streamOut.close();
				} catch (Throwable e) {
					if(LOGGER.isLoggable(Level.FINE))
						LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
				}
				
				try {
				    if(writer!=null)
                                        writer.dispose();
				} catch (Throwable e) {
					if(LOGGER.isLoggable(Level.FINE))
						LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
				}
                
                                try {
                                    if (currentImage != null)
                                        currentImage.dispose();
                                } catch (Throwable e) {
                                    if (LOGGER.isLoggable(Level.FINE))
                                                LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
                                }
                                                

				if(newImage!=null)
					newImage.dispose();
				try {
					stream.close();				
					
				} catch (Throwable e) {
					if(LOGGER.isLoggable(Level.FINE))
						LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
				}
			}
		}

	

		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Done!!!");

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.utils.progress.ProcessingEventListener#getNotification(it.geosolutions.utils.progress.ProcessingEvent)
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

	public boolean parseArgs(String[] args) {
		if (!super.parseArgs(args))
			return false;
		// ////////////////////////////////////////////////////////////////
		//
		// parsing command line parameters and setting up
		// Mosaic Index Builder options
		//
		// ////////////////////////////////////////////////////////////////
		sourcePath = (String) getOptionValue(locationOpt);

		// tile dim
		if (hasOption(tileDimOpt)) {
			final String tileDim = (String) getOptionValue(tileDimOpt);
			final String[] pairs = tileDim.split(",");
			tileW = Integer.parseInt(pairs[0]);
			tileH = Integer.parseInt(pairs[1]);
		}
		// //
		//
		// scale factor
		//
		// //
		final String scaleF = (String) getOptionValue(scaleFactorOpt);
		downsampleStep = Integer.parseInt(scaleF);

		// //
		//
		// wildcard
		//
		// //
		if (hasOption(wildcardOpt))
			wildcardString = (String) getOptionValue(wildcardOpt);

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

		return true;

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
		final OverviewsEmbedder overviewsEmbedder = new OverviewsEmbedder();
		// adding the embedder itself as a listener
		overviewsEmbedder.addProcessingEventListener(overviewsEmbedder);
		// parsing input argumentBuilder
		if (overviewsEmbedder.parseArgs(args)) {
			// creating a thread to execute the request process, with the
			// provided priority
			final Thread t = new Thread(overviewsEmbedder, "OverviewsEmbedder");
			t.setPriority(overviewsEmbedder.getPriority());
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

	/**
	 * Sets the wildcar string to use.
	 * 
	 * @param wildcardString
	 *            the wildcardString to set
	 */
	public final void setWildcardString(String wildcardString) {
		this.wildcardString = wildcardString;
	}

	public final double getCompressionRatio() {
		return compressionRatio;
	}

	public final String getCompressionScheme() {
		return compressionScheme;
	}

	public void setCompressionRatio(double compressionRatio) {
		this.compressionRatio = compressionRatio;
	}

	public void setCompressionScheme(String compressionScheme) {
		this.compressionScheme = compressionScheme;
	}

	public String getScaleAlgorithm() {
		return scaleAlgorithm;
	}

	public void setScaleAlgorithm(String scaleAlgorithm) {
		this.scaleAlgorithm = scaleAlgorithm;
	}

	public int getNumSteps() {
		return numSteps;
	}

	public void setNumSteps(int numSteps) {
		this.numSteps = numSteps;
	}

	public OverviewsEmbedderWriteProgressListener getWriteProgressListener() {
		return writeProgressListener;
	}

	public String getWildcardString() {
		return wildcardString;
	}
}
