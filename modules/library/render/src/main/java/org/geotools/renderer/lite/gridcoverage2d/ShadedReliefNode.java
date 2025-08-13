/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderedImageFactory;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import javax.measure.Unit;
import org.eclipse.imagen.JAI;
import org.eclipse.imagen.OperationDescriptor;
import org.eclipse.imagen.OperationRegistry;
import org.eclipse.imagen.ROI;
import org.eclipse.imagen.RenderedOp;
import org.eclipse.imagen.media.range.NoDataContainer;
import org.eclipse.imagen.media.range.Range;
import org.eclipse.imagen.media.shadedrelief.ShadedReliefAlgorithm;
import org.eclipse.imagen.media.shadedrelief.ShadedReliefDescriptor;
import org.eclipse.imagen.media.shadedrelief.ShadedReliefRIF;
import org.eclipse.imagen.registry.RenderedRegistryMode;
import org.geotools.api.coverage.grid.GridCoverage;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.style.ShadedRelief;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.util.InternationalString;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.ImageWorker;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.Hints;
import si.uom.SI;

/**
 * This implementations of {@link CoverageProcessingNode} takes care of the {@link ShadedRelief} element of the SLD 1.0
 * spec.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
class ShadedReliefNode extends StyleVisitorCoverageProcessingNodeAdapter
        implements StyleVisitor, CoverageProcessingNode {

    static {
        // Initialize JAIExt operations
        OperationRegistry registry = JAI.getDefaultInstance().getOperationRegistry();
        OperationDescriptor op = new ShadedReliefDescriptor();

        // registering the descriptor
        registry.registerDescriptor(op);
        String descName = op.getName();

        // registering the RIF
        RenderedImageFactory rif = new ShadedReliefRIF();
        registry.registerFactory(RenderedRegistryMode.MODE_NAME, descName, "org.geotools.gce.processing", rif);
    }

    private boolean brightnessOnly = false;

    private double reliefFactor = Double.NaN;

    public double getReliefFactor() {
        return reliefFactor;
    }

    @Override
    public InternationalString getName() {
        return new SimpleInternationalString("Shaded Relief");
    }

    @Override
    public void visit(final ShadedRelief sr) {
        // /////////////////////////////////////////////////////////////////////
        //
        // Do nothing if we don't have a valid ShadedRelief element.
        //
        // /////////////////////////////////////////////////////////////////////
        if (sr == null) {
            return;
        }

        // /////////////////////////////////////////////////////////////////////
        //
        // Brightness Only
        //
        // /////////////////////////////////////////////////////////////////////
        brightnessOnly = sr.isBrightnessOnly();

        // /////////////////////////////////////////////////////////////////////
        //
        // Relief Factor
        //
        // /////////////////////////////////////////////////////////////////////
        final Expression rFactor = sr.getReliefFactor();
        if (rFactor != null) {
            final Number number = rFactor.evaluate(null, Double.class);
            if (number != null) {
                reliefFactor = number.doubleValue();
                // check the gamma value
                if (reliefFactor < 0 || Double.isNaN(reliefFactor) || Double.isInfinite(reliefFactor)) {
                    throw new IllegalArgumentException(
                            MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "reliefFactor", number));
                }
            }
        }
    }

    /** Default constructor */
    public ShadedReliefNode() {
        this(null);
    }

    /**
     * Constructor for a {@link ShadedReliefNode} which allows to specify a {@link Hints} instance to control internal
     * factory machinery.
     *
     * @param hints {@link Hints} instance to control internal factory machinery.
     */
    public ShadedReliefNode(final Hints hints) {
        super(
                1,
                hints,
                SimpleInternationalString.wrap("ShadedReliefNode"),
                SimpleInternationalString.wrap("Node which applies ShadedRelief following SLD 1.0 spec."));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected GridCoverage2D execute() {
        final Hints hints = getHints();

        // /////////////////////////////////////////////////////////////////////
        //
        // Get the sources and see what we got to do. Note that if we have more
        // than one source we'll use only the first one
        //
        // /////////////////////////////////////////////////////////////////////
        final List<CoverageProcessingNode> sources = this.getSources();
        if (sources != null && !sources.isEmpty()) {
            CoverageProcessingNode nodeSource = getSource(0);

            CoverageProcessingNode colorMapNode = null;
            if (nodeSource instanceof ColorMapNode && ((ColorMapNode) nodeSource).getType() != ColorMapNode.TYPE_NONE) {

                // If colormap is present, ShadedRelief need to be computed on previous source
                colorMapNode = nodeSource;
                nodeSource = nodeSource.getSource(0);
            }

            final GridCoverage2D source = (GridCoverage2D) nodeSource.getOutput();
            GridCoverageRendererUtilities.ensureSourceNotNull(
                    source, this.getName().toString());

            final RenderedImage sourceImage = source.getRenderedImage();

            // /////////////////////////////////////////////////////////////////////
            //
            // PREPARATION
            //
            // /////////////////////////////////////////////////////////////////////

            // //
            //
            // Get the ROI and NoData from the input coverage
            //
            // //

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
            ImageWorker worker = new ImageWorker(sourceImage)
                    .setROI(roi)
                    .setNoData(nodata)
                    .setRenderingHints(hints)
                    .forceComponentColorModel();

            final int numbands = worker.getNumBands();

            if (numbands > 1) {
                throw new IllegalArgumentException("ShadedRelief can only be applied to single band images");
            }

            performShadedRelief(worker, source, hints);

            // /////////////////////////////////////////////////////////////////////
            //
            // OUTPUT
            //
            // /////////////////////////////////////////////////////////////////////
            final int numSourceBands = source.getNumSampleDimensions();
            RenderedImage finalImage = worker.getRenderedImage();

            final int numActualBands = finalImage.getSampleModel().getNumBands();
            final GridCoverageFactory factory = getCoverageFactory();
            final HashMap<Object, Object> props = new HashMap<>();
            if (source.getProperties() != null) {
                props.putAll(source.getProperties());
            }
            // Setting ROI and NODATA
            //            if (worker.getNoData() != null) {
            //                props.put(NoDataContainer.GC_NODATA, new
            // NoDataContainer(worker.getNoData()));
            //            }
            if (worker.getROI() != null) {
                props.put("GC_ROI", worker.getROI());
            }

            final GridSampleDimension[] sd;
            if (numActualBands == numSourceBands) {
                sd = source.getSampleDimensions();
            } else {
                sd = new GridSampleDimension[numActualBands];
                for (int i = 0; i < numActualBands; i++) sd[i] = source.getSampleDimension(0);
            }

            GridCoverage2D output = factory.create(
                    "sr_coverage" + source.getName(),
                    finalImage,
                    source.getGridGeometry(),
                    sd,
                    new GridCoverage[] {source},
                    props);

            if (colorMapNode != null) {
                GridCoverage2D mapCoverage = (GridCoverage2D) colorMapNode.getOutput();
                if (!brightnessOnly) {
                    // When brigthnessOnly is set to true I have to prepare a separated
                    // image to be applied to the rendering and return the previous source's
                    // output as output
                    props.put(GridCoverageRenderer.KEY_COMPOSITING, new Compositing(finalImage));
                    output = factory.create(
                            "sr_coverage" + source.getName().toString(),
                            mapCoverage.getRenderedImage(),
                            source.getGridGeometry(),
                            output.getSampleDimensions(),
                            new GridCoverage[] {output},
                            props);
                }
            }
            return output;
        }
        final Object arg0 = this.getName().toString();
        throw new IllegalStateException(MessageFormat.format(ErrorKeys.SOURCE_CANT_BE_NULL_$1, arg0));
    }

    private RenderedImage performShadedRelief(ImageWorker intensityWorker, GridCoverage2D source, Hints hints) {
        RenderedImage ri = source.getRenderedImage();
        MathTransform g2w = source.getGridGeometry().getGridToCRS();
        AffineTransform af = (AffineTransform) g2w;
        CoordinateReferenceSystem crs = source.getCoordinateReferenceSystem();
        Unit<?> unit = crs.getCoordinateSystem().getAxis(0).getUnit();
        boolean isMeter = unit.isCompatible(SI.METRE);
        double resX = af.getScaleX();
        double resY = af.getScaleY();

        double[] destNoData = intensityWorker.getDestinationNoData();
        double destinationNoData = destNoData != null ? destNoData[0] : 0;

        RenderedOp finalImage = ShadedReliefDescriptor.create(
                ri,
                intensityWorker.getROI(),
                intensityWorker.getNoData(),
                destinationNoData,
                resX,
                resY,
                reliefFactor,
                isMeter ? 1 : ShadedReliefDescriptor.DEFAULT_SCALE,
                ShadedReliefDescriptor.DEFAULT_ALTITUDE,
                ShadedReliefDescriptor.DEFAULT_AZIMUTH,
                ShadedReliefAlgorithm.ZEVENBERGEN_THORNE_COMBINED,
                hints);
        intensityWorker.setImage(finalImage);
        return finalImage;
    }
}
