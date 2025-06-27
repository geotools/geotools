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
import it.geosolutions.jaiext.piecewise.DefaultPiecewiseTransform1DElement;
import it.geosolutions.jaiext.piecewise.PiecewiseTransform1D;
import it.geosolutions.jaiext.range.NoDataContainer;
import it.geosolutions.jaiext.range.Range;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ROI;
import org.geotools.api.coverage.grid.GridCoverage;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.ContrastEnhancement;
import org.geotools.api.style.ContrastMethod;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.util.InternationalString;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.ImageWorker;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Vocabulary;
import org.geotools.renderer.i18n.VocabularyKeys;
import org.geotools.styling.AbstractContrastMethodStrategy;
import org.geotools.styling.ExponentialContrastMethodStrategy;
import org.geotools.styling.HistogramContrastMethodStrategy;
import org.geotools.styling.LogarithmicContrastMethodStrategy;
import org.geotools.styling.NormalizeContrastMethodStrategy;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.Hints;

/**
 * This implementations of {@link CoverageProcessingNode} takes care of the {@link ContrastEnhancement} element of the
 * SLD 1.0 spec.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @authod Daniele Romagnoli, GeoSolutions
 */
class ContrastEnhancementNode extends StyleVisitorCoverageProcessingNodeAdapter
        implements StyleVisitor, CoverageProcessingNode {

    /*
     * (non-Javadoc)
     * @see CoverageProcessingNode#getName()
     */
    @Override
    public InternationalString getName() {
        return Vocabulary.formatInternational(VocabularyKeys.CONTRAST_ENHANCEMENT);
    }

    /**
     * Specified the supported Histogram Enhancement algorithms.
     *
     * @todo in the future this should be pluggable.
     */
    private static final Set<String> SUPPORTED_HE_ALGORITHMS;

    /**
     * This are the different types f histogram equalization that we support for the moment. MOre should be added soon.
     */
    static {
        // load the contrast enhancement operations
        final HashSet<String> heAlg = new HashSet<>(2, 1.0f);
        heAlg.add("NORMALIZE");
        heAlg.add("HISTOGRAM");
        heAlg.add("LOGARITHMIC");
        heAlg.add("EXPONENTIAL");
        SUPPORTED_HE_ALGORITHMS = Collections.unmodifiableSet(heAlg);
    }

    /** ContrastMethod */
    AbstractContrastMethodStrategy contrastEnhancementMethod = null;

    /** Enhancement type to use. */
    private String type = null;

    /** Value we'll use for the gamma correction operation. */
    private double gammaValue = Double.NaN;

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.renderer.lite.gridcoverage2d.StyleVisitorAdapter#visit(org.geotools.styling.ContrastEnhancement)
     */
    @Override
    public void visit(final ContrastEnhancement ce) {
        // /////////////////////////////////////////////////////////////////////
        //
        // Do nothing if we don't have a valid ContrastEnhancement element. This
        // would protect us against bad SLDs
        //
        // /////////////////////////////////////////////////////////////////////
        if (ce == null) {
            return;
        }

        // /////////////////////////////////////////////////////////////////////
        //
        // TYPE of the operation to perform
        //
        // /////////////////////////////////////////////////////////////////////

        ContrastMethod contrastMethod = ce.getMethod();
        if (contrastMethod != null) {
            final String type = contrastMethod.name();
            if (type != null && !type.equalsIgnoreCase("None")) {
                this.type = type.toUpperCase();
                if (!SUPPORTED_HE_ALGORITHMS.contains(type.toUpperCase()))
                    throw new IllegalArgumentException(
                            MessageFormat.format(ErrorKeys.OPERATION_NOT_FOUND_$1, type.toUpperCase()));
                this.contrastEnhancementMethod = parseContrastEnhancementMethod(contrastMethod, ce.getOptions());
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
                    throw new IllegalArgumentException(
                            MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "Gamma", number));
                if (Double.isNaN(gammaValue) || Double.isInfinite(gammaValue))
                    throw new IllegalArgumentException(
                            MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "Gamma", number));
            }
        }
    }

    private AbstractContrastMethodStrategy parseContrastEnhancementMethod(
            ContrastMethod method, Map<String, Expression> options) {
        String name = method.name().toUpperCase();
        AbstractContrastMethodStrategy ceMethod = null;
        if ("NORMALIZE".equals(name)) {
            Expression algorithm = options.get(AbstractContrastMethodStrategy.ALGORITHM);
            ceMethod = new NormalizeContrastMethodStrategy();
            if (algorithm != null) {
                ceMethod.setAlgorithm(algorithm);
            }
        } else if ("LOGARITHMIC".equalsIgnoreCase(name)) {
            ceMethod = new LogarithmicContrastMethodStrategy();
        } else if ("EXPONENTIAL".equalsIgnoreCase(name)) {
            ceMethod = new ExponentialContrastMethodStrategy();
        } else if ("HISTOGRAM".equalsIgnoreCase(name)) {
            ceMethod = new HistogramContrastMethodStrategy();
        } else {
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.UNSUPPORTED_METHOD_$1, method));
        }
        ceMethod.setOptions(options);
        return ceMethod;
    }

    /** Default constructor */
    public ContrastEnhancementNode() {
        this(null);
    }

    /**
     * Constructor for a {@link ContrastEnhancementNode} which allows to specify a {@link Hints} instance to control
     * internal factory machinery.
     *
     * @param hints {@link Hints} instance to control internal factory machinery.
     */
    public ContrastEnhancementNode(final Hints hints) {
        super(
                1,
                hints,
                SimpleInternationalString.wrap("ContrastEnhancementNode"),
                SimpleInternationalString.wrap("Node which applies ContrastEnhancement following SLD 1.0 spec."));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.renderer.lite.gridcoverage2d.StyleVisitorCoverageProcessingNodeAdapter#execute()
     */
    @Override
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
            GridCoverageRendererUtilities.ensureSourceNotNull(
                    source, this.getName().toString());
            GridCoverage2D output;
            if (!Double.isNaN(gammaValue) && !Double.isInfinite(gammaValue) && !(Math.abs(gammaValue - 1) < 1E-6)
                    || type != null && type.length() > 0) {

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
                if (type != null && type.equalsIgnoreCase("HISTOGRAM")) {
                    worker = new ImageWorker(sourceImage)
                            .setROI(roi)
                            .setNoData(nodata)
                            .setRenderingHints(hints)
                            .forceComponentColorModel()
                            .rescaleToBytes();
                } else {
                    worker = new ImageWorker(sourceImage)
                            .setROI(roi)
                            .setNoData(nodata)
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
                            .setRenderingHints(hints)
                            .retainLastBand()
                            .getRenderedImage();
                    // strip the alpha band from the original image
                    worker.setRenderingHints(hints).retainBands(numbands - 1);
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
                    IHS = worker.setRenderingHints(hints).forceColorSpaceIHS().getRenderedImage();

                    // get the various singular bands
                    intensityWorker = worker.setRenderingHints(hints).retainFirstBand();
                    sChannel = new ImageWorker(IHS)
                            .setRenderingHints(hints)
                            .retainLastBand()
                            .getRenderedImage();
                    hChannel = new ImageWorker(IHS)
                            .setRenderingHints(hints)
                            .retainBands(new int[] {1})
                            .getRenderedImage();
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
                performGammaCorrection(intensityWorker, hints);

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
                    final RenderingHints rendHints = new RenderingHints(Collections.emptyMap());
                    rendHints.add(hints);
                    rendHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, imageLayout));

                    // merge and go to rgb again
                    intensityWorker
                            .setRenderingHints(rendHints)
                            .addBands(new RenderedImage[] {hChannel, sChannel}, false, null);
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
                            numbands >= 3
                                    ? ColorSpace.getInstance(ColorSpace.CS_sRGB)
                                    : ColorSpace.getInstance(ColorSpace.CS_GRAY),
                            numbands >= 3 ? new int[] {8, 8, 8, 8} : new int[] {8, 8},
                            true,
                            false,
                            Transparency.TRANSLUCENT,
                            DataBuffer.TYPE_BYTE);
                    final ImageLayout imageLayout = new ImageLayout();
                    imageLayout.setColorModel(cm);
                    imageLayout.setSampleModel(cm.createCompatibleSampleModel(
                            intensityWorker.getRenderedImage().getWidth(),
                            intensityWorker.getRenderedImage().getHeight()));
                    // merge and go to rgb
                    intensityWorker
                            .setRenderingHints(hints)
                            .setRenderingHint(JAI.KEY_IMAGE_LAYOUT, imageLayout)
                            .addBand(alphaBand, false, true, null);
                }

                // /////////////////////////////////////////////////////////////////////
                //
                // OUTPUT
                //
                // /////////////////////////////////////////////////////////////////////
                final int numSourceBands = source.getNumSampleDimensions();
                final RenderedImage finalImage = intensityWorker.getRenderedImage();
                final int numActualBands = finalImage.getSampleModel().getNumBands();
                final GridCoverageFactory factory = getCoverageFactory();
                final HashMap<Object, Object> props = new HashMap<>();
                if (source.getProperties() != null) {
                    props.putAll(source.getProperties());
                }
                // Setting ROI and NODATA
                if (intensityWorker.getNoData() != null) {
                    props.put(NoDataContainer.GC_NODATA, new NoDataContainer(intensityWorker.getNoData()));
                }
                if (intensityWorker.getROI() != null) {
                    props.put("GC_ROI", intensityWorker.getROI());
                }

                if (numActualBands == numSourceBands) {
                    final String name = "ce_coverage" + source.getName();
                    output = factory.create(
                            name,
                            finalImage,
                            source.getGridGeometry(),
                            source.getSampleDimensions(),
                            new GridCoverage[] {source},
                            props);
                } else {
                    // replicate input bands
                    final GridSampleDimension[] sd = new GridSampleDimension[numActualBands];
                    for (int i = 0; i < numActualBands; i++) sd[i] = source.getSampleDimension(0);
                    output = factory.create(
                            "ce_coverage" + source.getName().toString(),
                            finalImage,
                            source.getGridGeometry(),
                            sd,
                            new GridCoverage[] {source},
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
        final Object arg0 = this.getName().toString();
        throw new IllegalStateException(MessageFormat.format(ErrorKeys.SOURCE_CANT_BE_NULL_$1, arg0));
    }

    /**
     * Performs a contrast enhancement operation on the input image. Note that not all the contrast enhancement
     * operations have been implemented in a way that is generic enough o handle all data types.
     *
     * @param inputWorker the input {@link ImageWorker} to work on.
     * @param hints {@link Hints} to control the contrast enhancement process.
     * @return a {@link RenderedImage} on which a contrast enhancement has been performed.
     */
    private RenderedImage performContrastEnhancement(ImageWorker inputWorker, final Hints hints) {
        inputWorker.setRenderingHints(hints);

        if (contrastEnhancementMethod != null) {
            RenderedImage inputImage = inputWorker.getRenderedImage();
            assert inputImage.getSampleModel().getNumBands() == 1 : inputImage;

            ContrastEnhancementType ceType = ContrastEnhancementType.getType(contrastEnhancementMethod);
            return ceType.process(inputWorker, hints, contrastEnhancementMethod.getParameters());
        }

        return inputWorker.getRenderedImage();
    }

    /**
     * Performs a gamma correction operation on the input image.
     *
     * @param worker the input {@link ImageWorker} to work on.
     * @param hints {@link Hints} to control the contrast enhancement process.
     * @return a {@link RenderedImage} on which a gamma correction has been performed.
     */
    private RenderedImage performGammaCorrection(ImageWorker worker, final Hints hints) {
        worker.setRenderingHints(hints);

        // note that we should work on a single band
        RenderedImage inputImage = worker.getRenderedOperation();
        assert inputImage.getSampleModel().getNumBands() == 1 : inputImage;

        final int dataType = inputImage.getSampleModel().getDataType();
        if (!Double.isNaN(gammaValue) && Math.abs(gammaValue - 1.0) > 1E-6) {
            if (dataType == DataBuffer.TYPE_BYTE) {

                // //
                //
                // Byte case, use lookup to optimize
                //
                // //
                final byte[] lut = new byte[256];
                for (int i = 1; i < lut.length; i++) {
                    lut[i] = (byte) (255.0 * Math.pow(i / 255.0, gammaValue) + 0.5d);
                }

                // apply the operation now
                LookupTable table = LookupTableFactory.create(lut, dataType);
                worker.lookup(table);
            } else {
                //
                // Generic case
                //
                //
                // STEP 1 do the extrema
                //
                final double[] minimum = worker.getMinimums();
                final double[] maximum = worker.getMaximums();

                //
                // STEP 2 do the gamma correction by using generic piecewise
                //
                final PiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                        ContrastEnhancementType.generateGammaCorrectedPiecewise(minimum[0], maximum[0], gammaValue);
                worker.piecewise(transform, Integer.valueOf(0));
            }
        }
        RenderedImage result = worker.getRenderedImage();
        assert result.getSampleModel().getNumBands() == 1 : result;
        return result;
    }

    public String getType() {
        return type;
    }
}
