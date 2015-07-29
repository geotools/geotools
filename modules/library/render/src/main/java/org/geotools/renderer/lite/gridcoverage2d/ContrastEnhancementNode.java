/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import it.geosolutions.jaiext.lookup.LookupTable;
import it.geosolutions.jaiext.lookup.LookupTableFactory;
import it.geosolutions.jaiext.piecewise.DefaultPiecewiseTransform1D;
import it.geosolutions.jaiext.piecewise.DefaultPiecewiseTransform1DElement;
import it.geosolutions.jaiext.piecewise.PiecewiseTransform1D;
import it.geosolutions.jaiext.range.NoDataContainer;
import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;

import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.media.jai.Histogram;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ROI;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.factory.Hints;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.piecewise.MathTransformationAdapter;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.renderer.i18n.Vocabulary;
import org.geotools.renderer.i18n.VocabularyKeys;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.image.ColorUtilities;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.StyleVisitor;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.filter.expression.Expression;
import org.opengis.referencing.operation.TransformException;
import org.opengis.style.ContrastMethod;
import org.opengis.util.InternationalString;


/**
 * This implementations of {@link CoverageProcessingNode} takes care of the
 * {@link ContrastEnhancement} element of the SLD 1.0 spec.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 */
class ContrastEnhancementNode extends StyleVisitorCoverageProcessingNodeAdapter
		implements StyleVisitor, CoverageProcessingNode {
    /**
     * Minimal normalized value.
     */
    private static final double MIN_VALUE = 0d;
    
    /**
     * Maximal normalized value.
     */
    private static final double MAX_VALUE = 1d;

	/*
	 * (non-Javadoc)
	 * @see CoverageProcessingNode#getName() 
	 */
	public InternationalString getName() {
		return Vocabulary.formatInternational(VocabularyKeys.CONTRAST_ENHANCEMENT);
	}

	/**
	 * Specified the supported Histogram Enhancement algorithms.
	 * 
	 * @todo in the future this should be pluggable.
	 */
	private final static Set<String> SUPPORTED_HE_ALGORITHMS;

	/**
	 * This are the different types f histogram equalization that we support for
	 * the moment. MOre should be added soon.
	 * 
	 */
	static {
		//load the contrast enhancement operations 
		final HashSet<String> heAlg = new HashSet<String>(2, 1.0f);
		heAlg.add("NORMALIZE");
		heAlg.add("HISTOGRAM");
		heAlg.add("LOGARITHMIC");
		heAlg.add("EXPONENTIAL");
		SUPPORTED_HE_ALGORITHMS = Collections.unmodifiableSet(heAlg);
	}

	/** Histogram Enhancement algorithm to use. */
	private String type = null;

	/**
	 * Value we'll use for the gamma correction operation.
	 */
	private double gammaValue = Double.NaN;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.StyleVisitorAdapter#visit(org.geotools.styling.ContrastEnhancement)
	 */
	public void visit(final ContrastEnhancement ce) {
		// /////////////////////////////////////////////////////////////////////
		//
		// Do nothing if we don't have a valid ContrastEnhancement element. This
		// would protect us against bad SLDs
		//
		// /////////////////////////////////////////////////////////////////////
		if (ce == null){
		    return;
		}

		// /////////////////////////////////////////////////////////////////////
		//
		// TYPE of the operation to perform
		//
		// /////////////////////////////////////////////////////////////////////
		final Expression expType = ce.getType();
		if (expType != null) {
			final String type = expType.evaluate(null, String.class);
			if (type != null && !type.equalsIgnoreCase("None")) {
				this.type = type.toUpperCase();
				if (!SUPPORTED_HE_ALGORITHMS.contains(type.toUpperCase()))
					throw new IllegalArgumentException(Errors.format(ErrorKeys.OPERATION_NOT_FOUND_$1, type.toUpperCase()));
			}
		}

		// /////////////////////////////////////////////////////////////////////
		//
		// GAMMA
		//
		// /////////////////////////////////////////////////////////////////////
		final Expression gamma = ce.getGammaValue();
		if (gamma != null) {
			final Number number = gamma.evaluate(null, Double.class);
			if (number != null) {
				gammaValue = number.doubleValue();
				// check the gamma value
				if (gammaValue < 0)
					throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "Gamma", number));
				if (Double.isNaN(gammaValue) || Double.isInfinite(gammaValue))
					throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "Gamma", number));
			}
		}

	}

	/**
	 * Default constructor
	 */
	public ContrastEnhancementNode() {
		this(null);
	}

	/**
	 * Constructor for a {@link ContrastEnhancementNode} which allows to specify
	 * a {@link Hints} instance to control internal factory machinery.
	 * 
	 * @param hints
	 *            {@link Hints} instance to control internal factory machinery.
	 */
	public ContrastEnhancementNode(final Hints hints) {
		super(
				1,
				hints,
				SimpleInternationalString.wrap("ContrastEnhancementNode"),
				SimpleInternationalString
						.wrap("Node which applies ContrastEnhancement following SLD 1.0 spec."));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.StyleVisitorCoverageProcessingNodeAdapter#execute()
	 */
    @SuppressWarnings("unchecked")
	protected GridCoverage2D execute() {
		final Hints hints = getHints();

		// /////////////////////////////////////////////////////////////////////
		//
		// Get the sources and see what we got to do. Note that if we have more
		// than once source we'll use only the first one but we'll
		//
		// /////////////////////////////////////////////////////////////////////
		final List<CoverageProcessingNode> sources = this.getSources();
		if (sources != null && !sources.isEmpty()) {
			final GridCoverage2D source = (GridCoverage2D) getSource(0).getOutput();
			GridCoverageRendererUtilities.ensureSourceNotNull(source, this.getName().toString());
			GridCoverage2D output;
			if ((!Double.isNaN(gammaValue) && 
					!Double.isInfinite(gammaValue) && 
					!(Math.abs(gammaValue -1)<1E-6))||
					(type != null && type.length() > 0)) {

				// /////////////////////////////////////////////////////////////////////
				//
				// We have a valid gamma value, let's go ahead.
				//
				// /////////////////////////////////////////////////////////////////////
				final RenderedImage sourceImage = source.getRenderedImage();


				// /////////////////////////////////////////////////////////////////////
				//
				// PREPARATION
				//
				// /////////////////////////////////////////////////////////////////////

	                        // //
                                //
                                // Get the ROI and NoData from the input coverageS
                                //
                                ////
				ROI roi = CoverageUtilities.getROIProperty(source);
				NoDataContainer noDataContainer = CoverageUtilities.getNoDataProperty(source);
                                Range nodata = noDataContainer != null ? noDataContainer.getAsRange() : null;
				
				// //
				//
				// Get the source image and if necessary convert it to use a
				// ComponentColorModel. This way we are sure we will have a
				// visible image most part of the time.
				//
				// //
				////
				//
				// @todo TODO HACK we need to convert to byte the image when going to
				// apply HISTOGRAM anyway
				//
				////
                                ImageWorker worker;
				if(type!=null&&type.equalsIgnoreCase("HISTOGRAM"))
				{
				        worker = 
						new ImageWorker(sourceImage)
					                .setROI(roi).setNoData(nodata)
							.setRenderingHints(hints)
							.forceComponentColorModel()
							.rescaleToBytes();
				}
				else
				{
                                        worker =  
						new ImageWorker(sourceImage)
					                .setROI(roi).setNoData(nodata)
							.setRenderingHints(hints)
							.forceComponentColorModel();
				}
				final int numbands = worker.getNumBands();


				// //
				//
				// Save the alpha band if present, in order to put it back
				// later in the loop. We are not going to use it anyway for
				// the IHS conversion.
				//
				// //
				RenderedImage alphaBand = null;
				if (numbands % 2 == 0) {
					// get the alpha band
					alphaBand = new ImageWorker(worker.getRenderedImage())
							.setRenderingHints(hints).retainLastBand()
							.getRenderedImage();
					// strip the alpha band from the original image
					        worker
							.setRenderingHints(hints).retainBands(numbands - 1);
				}

				// //
				//
				// Get the single band to work on, which might be the
				// intensity for RGB(A) or the GRAY channel for Gray(A)
				//
				// //
				ImageWorker intensityWorker;
				RenderedImage hChannel = null;
				RenderedImage sChannel = null;
				final boolean intensity;
				RenderedImage IHS = null;
				if (numbands > 1) {
					// convert the prepared image to IHS colorspace to work
					// on I band
					IHS = worker
							.setRenderingHints(hints).forceColorSpaceIHS()
							.getRenderedImage();

					// get the various singular bands
					intensityWorker = worker.setRenderingHints(hints).retainFirstBand();
					sChannel = new ImageWorker(IHS).setRenderingHints(hints)
							.retainLastBand().getRenderedImage();
					hChannel = new ImageWorker(IHS).setRenderingHints(hints)
							.retainBands(new int[] { 1 }).getRenderedImage();
					intensity = true;
				} else {
					// //
					//
					// we have only one band we don't need to go to IHS
					//
					// //
				        intensityWorker = worker;
				        intensity = false;
				}

				// /////////////////////////////////////////////////////////////////////
				//
				// HISTOGRAM ENHANCEMENT
				//
				// 
				//
				// /////////////////////////////////////////////////////////////////////
				performContrastEnhancement(intensityWorker, hints);	

				// /////////////////////////////////////////////////////////////////////
				//
				// GAMMA CORRECTION
				//
				// Lookup for building the actual lut that caches the gamma
				// correction function's values.
				//
				// /////////////////////////////////////////////////////////////////////
				performGammaCorrection(intensityWorker,hints);

				// /////////////////////////////////////////////////////////////////////
				//
				// POSTPROCESSING
				//
				// Take care of the intermediated image we left back. This
				// means, handle the fact that we might have gone to IHS and
				// the alpha band.
				//
				// /////////////////////////////////////////////////////////////////////
				if (intensity) {

					// //
					//
					// IHS --> RGB
					//
					// Let's merge the modified IHS image. The message on
					// the mailing list (see comments for this class)
					// mentioned that it is required to pass a RenderingHing
					// with a ImageLayout with the IHS color
					// model.
					//
					// //
					final ImageLayout imageLayout = new ImageLayout();
					imageLayout.setColorModel(IHS.getColorModel());
					imageLayout.setSampleModel(IHS.getSampleModel());
					final RenderingHints rendHints = new RenderingHints(Collections.EMPTY_MAP);
					rendHints.add(hints);
					rendHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT,imageLayout));
					
					// merge and go to rgb again
					intensityWorker.setRenderingHints(rendHints).addBands(new RenderedImage[]{hChannel, sChannel}, false, null);
					intensityWorker.setRenderingHints(hints).forceColorSpaceRGB();
				}

				// //
				//
				// ALPHA BAND
				//
				// Let's merge the alpha band with the image we have rebuilt.
				//
				// //
				if (alphaBand != null) {
					final ColorModel cm = new ComponentColorModel(
							numbands >= 3 ? ColorSpace
									.getInstance(ColorSpace.CS_sRGB)
									: ColorSpace
											.getInstance(ColorSpace.CS_GRAY),
							numbands >= 3 ? new int[] { 8, 8, 8, 8 }
									: new int[] { 8, 8 }, true, false,
							Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
					final ImageLayout imageLayout = new ImageLayout();
					imageLayout.setColorModel(cm);
					imageLayout.setSampleModel(cm.createCompatibleSampleModel(intensityWorker.getRenderedImage().getWidth(), intensityWorker.getRenderedImage().getHeight()));
					// merge and go to rgb
					intensityWorker
							.setRenderingHints(hints)
							.setRenderingHint(JAI.KEY_IMAGE_LAYOUT,imageLayout)
							.addBand(alphaBand, false, true, null);

				}

				// /////////////////////////////////////////////////////////////////////
				//
				// OUTPUT
				//
				// /////////////////////////////////////////////////////////////////////
				final int numSourceBands=source.getNumSampleDimensions();
				final RenderedImage finalImage = intensityWorker.getRenderedImage();
				final int numActualBands= finalImage.getSampleModel().getNumBands();
				final GridCoverageFactory factory = getCoverageFactory();
                final HashMap<Object,Object> props = new HashMap<Object,Object>();
                if(source.getProperties() != null) {
                    props.putAll(source.getProperties());
                }
                // Setting ROI and NODATA
                if(intensityWorker.getNoData() != null){
                    props.put(NoDataContainer.GC_NODATA, new NoDataContainer(intensityWorker.getNoData()));
                }
                if(intensityWorker.getROI() != null){
                    props.put("GC_ROI", intensityWorker.getROI());
                }
                
                if(numActualBands==numSourceBands) {
                    final String name = "ce_coverage" + source.getName();
                    output = factory.create(
					        name, 
					        finalImage,
					        (GridGeometry2D)source.getGridGeometry(),
					        source.getSampleDimensions(),
					        new GridCoverage[]{source},
					        props);
                } else {
					// replicate input bands
					final GridSampleDimension sd[]= new GridSampleDimension[numActualBands];
					for(int i=0;i<numActualBands;i++)
						sd[i]=(GridSampleDimension) source.getSampleDimension(0);
					output = factory.create(
					        "ce_coverage"+source.getName().toString(), 
					        finalImage,
					        (GridGeometry2D)source.getGridGeometry(),
					        sd,
					        new GridCoverage[]{source},
					        props);
				}


			} else
				// /////////////////////////////////////////////////////////////////////
				//
				// We do not have a valid gamma value, let's try with a
				// conservative approach that is, let's forward the source
				// coverage intact.
				//
				// /////////////////////////////////////////////////////////////////////
				output = source;
			return output;

		}
		throw new IllegalStateException(Errors.format(
				ErrorKeys.SOURCE_CANT_BE_NULL_$1, this.getName().toString()));

	}

	/**
	 * Performs a contrast enhancement operation on the input image. Note that
	 * not all the contrast enhancement operations have been implemented in a
	 * way that is generic enough o handle all data types.
	 * 
	 * @param inputImage the input {@link RenderedImage} to work on.
	 * @param hints {@link Hints} to control the contrast enhancement process.
	 * @return a {@link RenderedImage} on which a contrast enhancement has been performed.
	 */
	private RenderedImage performContrastEnhancement(
	                ImageWorker inputWorker,
			final Hints hints) {
	        inputWorker.setRenderingHints(hints);
		if (type != null && type.length() > 0) {
		        RenderedImage inputImage = inputWorker.getRenderedImage();
			assert inputImage.getSampleModel().getNumBands() == 1:inputImage;
			final int dataType=inputImage.getSampleModel().getDataType();

			// /////////////////////////////////////////////////////////////////////
			//
			// Histogram Normalization
			//
			// 
			//
			// /////////////////////////////////////////////////////////////////////
			if (type.equalsIgnoreCase("NORMALIZE")) {
				//step 1 do the extrema to get the statistics for this image
			        final double[][] extrema = new double[2][];
			        inputWorker.removeRenderingHints();
			        extrema[0] = inputWorker.getMinimums();
			        extrema[1] = inputWorker.getMaximums();
				final int numBands = extrema[0].length;
				assert numBands == 1:inputWorker.getRenderedOperation();
				
				// //
				//
				// Shortcut fr byte datatype
				//
				// //
				if(dataType==DataBuffer.TYPE_BYTE){
					////
					//
					// Optimisation for byte images, we use the lookup operation
					//
					////
					if (extrema[1][0] == 255 && extrema[0][0] == 0)
						return inputImage;
					
					final double delta = extrema[1][0] - extrema[0][0];
					final double scale = 255 / delta;
					final double offset = -scale * extrema[0][0];
					//create the lookup table
					final byte[] lut = new byte[256];
					for (int i = 1; i < lut.length; i++)
						lut[i] = (byte) (scale * i + offset + 0.5d);

					//do the actual lookup
					LookupTable table = LookupTableFactory.create(lut, dataType);
					inputWorker.setRenderingHints(hints);
					inputWorker.lookup(table);
					return inputWorker.getRenderedImage();
				}
				
				////
				//
				// General case, we use the rescale in order to stretch the values to highest and lowest dim
				//
				////
				//get the correct dim for this data type
				final double maximum=ColorUtilities.getMaximum(dataType);
				final double minimum=ColorUtilities.getMinimum(dataType);
				if (extrema[1][0] == maximum && extrema[0][0] == minimum)
					return inputImage;
				//compute the scale factors
				final double delta = extrema[1][0] - extrema[0][0];
				final double scale = (maximum -minimum)/ delta;
				final double offset =  minimum - scale * extrema[0][0];

				//do the actual rescale
				inputWorker.setRenderingHints(hints);
				inputWorker.rescale(new double []{scale}, new double []{offset});
				return inputWorker.getRenderedImage();
			}
			
			
			// /////////////////////////////////////////////////////////////////////
			//
			// EXPONENTIAL Normalization
			//
			// 
			//
			// /////////////////////////////////////////////////////////////////////			
			if (type.equalsIgnoreCase("EXPONENTIAL")) {

				if(dataType==DataBuffer.TYPE_BYTE){
					////
					//
					// Optimisation for byte images
					//
					////
					final byte lut[] = new byte[256];
					final double normalizationFactor=255.0;
					final double correctionFactor=255.0/(Math.E-1);
					for (int i = 1; i < lut.length; i++)
						lut[i] = (byte) (0.5f + correctionFactor * (Math.exp(i / normalizationFactor) - 1.0));
					
	                                //do the actual lookup
                                        LookupTable table = LookupTableFactory.create(lut, dataType);
                                        inputWorker.lookup(table);
                                        return inputWorker.getRenderedImage();
				}
				////
				//
				// General case, we use the piecewise1D transform
				//
				////
				//
				// STEP 1 do the extrema
				//
				////
				//step 1 do the extrema to get the statistics for this image
				inputWorker.removeRenderingHints();
				final double[] minimum=inputWorker.getMinimums();
				final double[] maximum=inputWorker.getMaximums();

				final double normalizationFactor=maximum[0];
				final double correctionFactor=normalizationFactor/(Math.E-1);
				
				////
				//
				// STEP 2 do the gamma correction by using generic piecewise
				//
				////
				final DefaultPiecewiseTransform1DElement mainElement = DefaultPiecewiseTransform1DElement.create(
						"exponential-contrast-enhancement-transform", RangeFactory.create(minimum[0],maximum[0]), 
						new MathTransformationAdapter() {

									/*
									 * (non-Javadoc)
									 * @see org.opengis.referencing.operation.MathTransform1D#derivative(double)
									 */
									public double derivative(double value)
											throws TransformException {
										
										throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1));
									}
									public boolean isIdentity() {
										return false;
									}
									/*
									 * (non-Javadoc)
									 * @see org.opengis.referencing.operation.MathTransform1D#transform(double)
									 */
									public double transform(double value) {
										value = correctionFactor*(Math.exp(value/normalizationFactor)-1);
										return value;
									}

						});
				
				final PiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform = new DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> (
						new DefaultPiecewiseTransform1DElement[] {mainElement},0);

				inputWorker.piecewise(transform, Integer.valueOf(0));
				return inputWorker.getRenderedImage();
			}			
			if (type.equalsIgnoreCase("LOGARITHMIC")) {
				// /////////////////////////////////////////////////////////////////////
				//
				// Logarithm Normalization
				//
				// 
				//
				// /////////////////////////////////////////////////////////////////////
				if(dataType==DataBuffer.TYPE_BYTE){
					////
					//
					// Optimisation for byte images m we use lookup
					//
					////
					final byte lut[] = new byte[256];
					final double normalizationFactor=255.0;
					final double correctionFactor=100.0;
					for (int i = 1; i < lut.length; i++)
						lut[i] = (byte) (0.5f + normalizationFactor * Math.log((i * correctionFactor / normalizationFactor+ 1.0)));
	                                //do the actual lookup
                                        LookupTable table = LookupTableFactory.create(lut, dataType);
                                        inputWorker.lookup(table);
                                        return inputWorker.getRenderedImage();				
				}
				////
				//
				// General case
				//
				////
				//define a specific piecewise for the logarithm

				////
				//
				// STEP 1 do the extrema
				//
				////
				//step 1 do the extrema to get the statistics for this image
				inputWorker.removeRenderingHints();
				final double[] minimum=inputWorker.getMinimums();
                                final double[] maximum=inputWorker.getMaximums();
				final double normalizationFactor=maximum[0];
				final double correctionFactor=100.0;
				
				////
				//
				// STEP 2 do the gamma correction by using generic piecewise
				//
				////
				final DefaultPiecewiseTransform1DElement mainElement = DefaultPiecewiseTransform1DElement.create(
						"logarithmic-contrast-enhancement-transform", RangeFactory.create(minimum[0],maximum[0]), 
						new MathTransformationAdapter() {

									/*
									 * (non-Javadoc)
									 * @see org.opengis.referencing.operation.MathTransform1D#derivative(double)
									 */
									public double derivative(double value)
											throws TransformException {
										
										throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1));
									}
									public boolean isIdentity() {
										return false;
									}
									/*
									 * (non-Javadoc)
									 * @see org.opengis.referencing.operation.MathTransform1D#transform(double)
									 */
									public double transform(double value) {
										value =normalizationFactor*Math.log(1+(value*correctionFactor/normalizationFactor));
										return value;
									}

									
						});
				
				final PiecewiseTransform1D<DefaultPiecewiseTransform1DElement>  transform = new DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> (
						new DefaultPiecewiseTransform1DElement[] {mainElement},0);

				inputWorker.piecewise(transform, Integer.valueOf(0));
				return inputWorker.getRenderedImage();
			}			
			if (type.equalsIgnoreCase("HISTOGRAM")) {
				// /////////////////////////////////////////////////////////////////////
				//
				// Histogram Equalization
				//
				// IT WORKS ONLY ON BYTE DATA TYPE!!!
				//
				// /////////////////////////////////////////////////////////////////////

				//convert the input image to 8 bit
				inputWorker.rescaleToBytes();
				
				// compute the histogram
				final Histogram h = inputWorker.removeRenderingHints().getHistogram(null, null, null);
				// now compute the PDF and the CDF for the original image
				final byte[] cumulative = new byte[h.getNumBins(0)];

				// sum of bins (we might have excluded 0 hence we cannot really
				// optimise)
				float totalBinSum = 0;
				for (int i = 0; i < cumulative.length; i++) {
					totalBinSum += h.getBinSize(0, i);
				}

				// this is the scale factor for the histogram equalization
				// process
				final float scale = (float) (h.getHighValue(0) - 1 - h.getLowValue(0))/ totalBinSum;
				float sum = 0;
				for (int i = 1; i < cumulative.length; i++) {
					sum += h.getBinSize(0, i - 1);
					cumulative[i] = (byte) ((sum * scale + h.getLowValue(0)) + .5F);
				}

                                //do the actual lookup
                                LookupTable table = LookupTableFactory.create(cumulative, DataBuffer.TYPE_BYTE);
                                inputWorker.setRenderingHints(hints);
                                inputWorker.lookup(table);
                                return inputWorker.getRenderedImage();
			}
		}

		return inputWorker.getRenderedImage();
	}

	/**
	 * Performs a gamma correction operation on the input image.
	 * 
	 * @param inputImage the input {@link RenderedImage} to work on.
	 * @param hints {@link Hints} to control the contrast enhancement process.
	 * @return a {@link RenderedImage} on which a gamma correction has been performed.
	 */
	private RenderedImage performGammaCorrection(
			ImageWorker worker,
	                final Hints hints) {
	        worker.setRenderingHints(hints);
		//note that we should work on a single band
	        RenderedImage inputImage = worker.getRenderedOperation();
		assert inputImage.getSampleModel().getNumBands() == 1:inputImage;
		
		final int dataType=inputImage.getSampleModel().getDataType();
		RenderedImage result=inputImage;
		if (!Double.isNaN(gammaValue) && Math.abs(gammaValue - 1.0) > 1E-6) {
			if (dataType == DataBuffer.TYPE_BYTE) {

				////
				//
				// Byte case, use lookup to optimize
				// 
				////
				final byte[] lut = new byte[256];
				for (int i = 1; i < lut.length; i++)
					lut[i] = (byte) (255.0 * Math.pow(i / 255.0, gammaValue) + 0.5d);

				// apply the operation now
				LookupTable table = LookupTableFactory.create(lut, dataType);
                                worker.lookup(table);
			}
			else
			{
				////
				//
				// Generic case
				// 
				////
				//
				// STEP 1 do the extrema
				//
				////
				//step 1 do the extrema to get the statistics for this image
			        final double[] minimum=worker.getMinimums();
                                final double[] maximum=worker.getMaximums();
			        final double scale  = (maximum[0]-minimum[0])/(MAX_VALUE-MIN_VALUE);
		                final double offset = minimum[0] - MIN_VALUE*scale;
				////
				//
				// STEP 2 do the gamma correction by using generic piecewise
				//
				////
				final DefaultPiecewiseTransform1DElement mainElement = DefaultPiecewiseTransform1DElement.create(
						"gamma-correction-transform", RangeFactory.create(minimum[0],maximum[0]), 
						new MathTransformationAdapter() {

									/*
									 * (non-Javadoc)
									 * @see org.opengis.referencing.operation.MathTransform1D#derivative(double)
									 */
									public double derivative(double value)
											throws TransformException {
										
										throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1));
									}
									public boolean isIdentity() {
										return false;
									}
									/*
									 * (non-Javadoc)
									 * @see org.opengis.referencing.operation.MathTransform1D#transform(double)
									 */
									public double transform(double value) {
										value = (value-offset)/scale;
										return offset+Math.pow(value, gammaValue)*scale;
									}

						});
				
				final PiecewiseTransform1D<DefaultPiecewiseTransform1DElement>  transform = new DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> (
						new DefaultPiecewiseTransform1DElement[] {mainElement},0);
				worker.piecewise(transform, Integer.valueOf(0));
			}
		}
		result = worker.getRenderedImage();
		assert result.getSampleModel().getNumBands() == 1:result;
		return result;
	}

    @Override
    public void visit(ContrastMethod method) {
        // TODO Auto-generated method stub
        
    }



}
