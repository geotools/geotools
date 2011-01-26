/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.media.jai.Histogram;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ExtremaDescriptor;
import javax.media.jai.operator.HistogramDescriptor;
import javax.media.jai.operator.LookupDescriptor;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.factory.Hints;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.piecewise.DefaultPiecewiseTransform1D;
import org.geotools.referencing.piecewise.DefaultPiecewiseTransform1DElement;
import org.geotools.referencing.piecewise.GenericPiecewise;
import org.geotools.referencing.piecewise.MathTransform1DAdapter;
import org.geotools.referencing.piecewise.PiecewiseTransform1D;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.renderer.i18n.Vocabulary;
import org.geotools.renderer.i18n.VocabularyKeys;
import org.geotools.resources.image.ColorUtilities;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.StyleVisitor;
import org.geotools.util.NumberRange;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.filter.expression.Expression;
import org.opengis.referencing.operation.TransformException;
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
		
		//load, if it is needed the GenericPiecewise tranformation
		try{
			new ParameterBlockJAI(GenericPiecewise.OPERATION_NAME);
			
		}catch (final Exception e) {
			GenericPiecewise.register(JAI.getDefaultInstance());
		}

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
	public synchronized void visit(final ContrastEnhancement ce) {
		// /////////////////////////////////////////////////////////////////////
		//
		// Do nothing if we don't have a valid ContrastEnhancement element. This
		// would protect us against bad SLDs
		//
		// /////////////////////////////////////////////////////////////////////
		if (ce == null)
			return;

		// /////////////////////////////////////////////////////////////////////
		//
		// TYPE of the operation to perform
		//
		// /////////////////////////////////////////////////////////////////////
		final Expression expType = ce.getType();
		if (expType != null) {
			final String type = expType.evaluate(null, String.class);
			if (type != null) {
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
		assert Thread.holdsLock(this);
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
			ensureSourceNotNull(source, this.getName().toString());
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
				RenderedImage initialImage;
				if(type!=null&&type.equalsIgnoreCase("HISTOGRAM"))
				{
					initialImage = 
						new ImageWorker(sourceImage)
							.setRenderingHints(hints)
							.forceComponentColorModel()
							.rescaleToBytes()
							.getRenderedImage();
				}
				else
				{
					initialImage = 
						new ImageWorker(sourceImage)
							.setRenderingHints(hints)
							.forceComponentColorModel()
							.getRenderedImage();
				}
				final int numbands = initialImage.getSampleModel().getNumBands();


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
					alphaBand = new ImageWorker(initialImage)
							.setRenderingHints(hints).retainLastBand()
							.getRenderedImage();
					// strip the alpha band from the original image
					initialImage = new ImageWorker(initialImage)
							.setRenderingHints(hints).retainBands(numbands - 1)
							.getRenderedImage();
				}

				// //
				//
				// Get the single band to work on, which might be the
				// intensity for RGB(A) or the GRAY channel for Gray(A)
				//
				// //
				RenderedImage intensityImage = null;
				RenderedImage hChannel = null;
				RenderedImage sChannel = null;
				final boolean intensity;
				RenderedImage IHS = null;
				if (numbands > 1) {
					// convert the prepared image to IHS colorspace to work
					// on I band
					IHS = new ImageWorker(initialImage)
							.setRenderingHints(hints).forceColorSpaceIHS()
							.getRenderedImage();

					// get the various singular bands
					intensityImage = new ImageWorker(IHS).setRenderingHints(
							hints).retainFirstBand().getRenderedImage();
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
					intensityImage = initialImage;
					intensity = false;
				}

				// /////////////////////////////////////////////////////////////////////
				//
				// HISTOGRAM ENHANCEMENT
				//
				// 
				//
				// /////////////////////////////////////////////////////////////////////
				final RenderedImage heImage = performContrastEnhancement(intensityImage, hints);
					

				// /////////////////////////////////////////////////////////////////////
				//
				// GAMMA CORRECTION
				//
				// Lookup for building the actual lut that caches the gamma
				// correction function's values.
				//
				// /////////////////////////////////////////////////////////////////////
				RenderedImage finalImage = performGammaCorrection(heImage,hints);

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
					final ParameterBlock pb = new ParameterBlock();
					pb.addSource(finalImage);
					pb.addSource(hChannel);
					pb.addSource(sChannel);
					finalImage = JAI.create("bandmerge", pb, rendHints);
					finalImage = new ImageWorker(finalImage).setRenderingHints(hints).forceColorSpaceRGB().getRenderedImage();

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
					imageLayout.setSampleModel(cm.createCompatibleSampleModel(finalImage.getWidth(), finalImage.getHeight()));
					// merge and go to rgb
					finalImage = 
						new ImageWorker(finalImage)
							.setRenderingHints(hints)
							.setRenderingHint(JAI.KEY_IMAGE_LAYOUT,imageLayout)
							.addBand(alphaBand, false)
							.getRenderedOperation();

				}

				// /////////////////////////////////////////////////////////////////////
				//
				// OUTPUT
				//
				// /////////////////////////////////////////////////////////////////////
				final int numSourceBands=source.getNumSampleDimensions();
				final int numActualBands= finalImage.getSampleModel().getNumBands();
				if(numActualBands==numSourceBands)
					output = getCoverageFactory().create(
					        "ce_coverage"+source.getName().toString(), 
					        finalImage,
					        (GridGeometry2D)source.getGridGeometry(),
					        source.getSampleDimensions(),
					        new GridCoverage[]{source},
					        new HashMap<Object,Object>(source.getProperties()));
				else
				{
					//replicate input bands
					final GridSampleDimension sd[]= new GridSampleDimension[numActualBands];
					for(int i=0;i<numActualBands;i++)
						sd[i]=(GridSampleDimension) source.getSampleDimension(0);
					output = getCoverageFactory().create(
					        "ce_coverage"+source.getName().toString(), 
					        finalImage,
					        (GridGeometry2D)source.getGridGeometry(),
					        sd,
					        new GridCoverage[]{source},
					        new HashMap<Object,Object>(source.getProperties()));
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
			RenderedImage inputImage,
			final Hints hints) {
		if (type != null && type.length() > 0) {
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
				final RenderedOp image = ExtremaDescriptor.create(inputImage,
						null, Integer.valueOf(1), Integer.valueOf(1), null,
						Integer.valueOf(1), null);
				final double[][] extrema = (double[][]) image
						.getProperty("extrema");
				final int numBands = extrema[0].length;
				assert numBands == 1:image;
				
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
					final LookupTableJAI lookup = new LookupTableJAI(lut);
					final ParameterBlock pb = new ParameterBlock();
					pb.addSource(inputImage);
					pb.add(lookup);
					return JAI.create("lookup", pb, hints);					
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
				final ParameterBlock pb = new ParameterBlock();
				pb.addSource(inputImage);
				pb.add(new double []{scale});
				pb.add(new double []{offset});
				return JAI.create("rescale", pb, hints);

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
					return LookupDescriptor.create(inputImage,
							new LookupTableJAI(lut), hints);
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
				final RenderedOp statistics = ExtremaDescriptor.create(inputImage,
						null, Integer.valueOf(1), Integer.valueOf(1), null,
						Integer.valueOf(1), null);
				final double[] minimum=(double[]) statistics.getProperty("minimum");
				final double[] maximum=(double[]) statistics.getProperty("maximum");
				final double normalizationFactor=maximum[0];
				final double correctionFactor=normalizationFactor/(Math.E-1);
				
				////
				//
				// STEP 2 do the gamma correction by using generic piecewise
				//
				////
				final DefaultPiecewiseTransform1DElement mainElement = DefaultPiecewiseTransform1DElement.create(
						"exponential-contrast-enhancement-transform", NumberRange.create(minimum[0],maximum[0]), 
						new MathTransform1DAdapter() {

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
									public double transform(double value)
											throws TransformException {
										value = correctionFactor*(Math.exp(value/normalizationFactor)-1);
										return value;
									}

						});
				
				final PiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform = new DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> (
						new DefaultPiecewiseTransform1DElement[] {mainElement},0);

					final ParameterBlockJAI pbj = new ParameterBlockJAI(
							GenericPiecewise.OPERATION_NAME);
					pbj.addSource(inputImage);
					pbj.setParameter("Domain1D", transform);
					pbj.setParameter("bandIndex", Integer.valueOf(0));
					return JAI.create(
							GenericPiecewise.OPERATION_NAME, pbj);
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
					return LookupDescriptor.create(inputImage,
							new LookupTableJAI(lut), hints);				
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
				final RenderedOp statistics = ExtremaDescriptor.create(inputImage,
						null, Integer.valueOf(1), Integer.valueOf(1), null,
						Integer.valueOf(1), null);
				final double[] minimum=(double[]) statistics.getProperty("minimum");
				final double[] maximum=(double[]) statistics.getProperty("maximum");
				final double normalizationFactor=maximum[0];
				final double correctionFactor=100.0;
				
				////
				//
				// STEP 2 do the gamma correction by using generic piecewise
				//
				////
				final DefaultPiecewiseTransform1DElement mainElement = DefaultPiecewiseTransform1DElement.create(
						"logarithmic-contrast-enhancement-transform", NumberRange.create(minimum[0],maximum[0]), 
						new MathTransform1DAdapter() {

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
									public double transform(double value)
											throws TransformException {
										value =normalizationFactor*Math.log(1+(value*correctionFactor/normalizationFactor));
										return value;
									}

									
						});
				
				final PiecewiseTransform1D<DefaultPiecewiseTransform1DElement>  transform = new DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> (
						new DefaultPiecewiseTransform1DElement[] {mainElement},0);

					final ParameterBlockJAI pbj = new ParameterBlockJAI(
							GenericPiecewise.OPERATION_NAME);
					pbj.addSource(inputImage);
					pbj.setParameter("Domain1D", transform);
					pbj.setParameter("bandIndex", Integer.valueOf(0));
					return JAI.create(
							GenericPiecewise.OPERATION_NAME, pbj);
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
				inputImage=new ImageWorker(inputImage).rescaleToBytes().getRenderedImage();
				// compute the histogram
				final RenderedOp hist = HistogramDescriptor.create(inputImage,
						null, Integer.valueOf(1), Integer.valueOf(1),
						new int[] { 256 }, new double[] { 0 },
						new double[] { 256 }, null);
				final Histogram h = (Histogram) hist.getProperty("histogram");

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

				final LookupTableJAI lookup = new LookupTableJAI(cumulative);
				final ParameterBlock pb = new ParameterBlock();
				pb.addSource(hist);
				pb.add(lookup);
				return JAI.create("lookup", pb, hints);
			}
		}

		return inputImage;
	}

	/**
	 * Performs a gamma correction operation on the input image.
	 * 
	 * @param inputImage the input {@link RenderedImage} to work on.
	 * @param hints {@link Hints} to control the contrast enhancement process.
	 * @return a {@link RenderedImage} on which a gamma correction has been performed.
	 */
	private RenderedImage performGammaCorrection(
			final RenderedImage inputImage,
			final Hints hints) {
		//note that we should work on a single band
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
				final LookupTableJAI lookup = new LookupTableJAI(lut);
				final ParameterBlock pb = new ParameterBlock();
				pb.addSource(inputImage);
				pb.add(lookup);
				result = JAI.create("lookup", pb, hints);
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
				final RenderedOp statistics = ExtremaDescriptor.create(inputImage,
						null, Integer.valueOf(1), Integer.valueOf(1), null,
						Integer.valueOf(1), null);
				final double[] minimum=(double[]) statistics.getProperty("minimum");
				final double[] maximum=(double[]) statistics.getProperty("maximum");
				final double scale  = (maximum[0]-minimum[0])/(MAX_VALUE-MIN_VALUE);
		                final double offset = minimum[0] - MIN_VALUE*scale;
				////
				//
				// STEP 2 do the gamma correction by using generic piecewise
				//
				////
				final DefaultPiecewiseTransform1DElement mainElement = DefaultPiecewiseTransform1DElement.create(
						"gamma-correction-transform", NumberRange.create(minimum[0],maximum[0]), 
						new MathTransform1DAdapter() {

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
									public double transform(double value)
											throws TransformException {
										value = (value-offset)/scale;
										return offset+Math.pow(value, gammaValue)*scale;
									}

						});
				
				final PiecewiseTransform1D<DefaultPiecewiseTransform1DElement>  transform = new DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> (
						new DefaultPiecewiseTransform1DElement[] {mainElement},0);

					final ParameterBlockJAI pbj = new ParameterBlockJAI(
							GenericPiecewise.OPERATION_NAME);
					pbj.addSource(inputImage);
					pbj.setParameter("Domain1D", transform);
					pbj.setParameter("bandIndex", Integer.valueOf(0));
					result = JAI.create(
							GenericPiecewise.OPERATION_NAME, pbj);
			}
		}
		assert result.getSampleModel().getNumBands() == 1:result;
		return result;
	}



}
