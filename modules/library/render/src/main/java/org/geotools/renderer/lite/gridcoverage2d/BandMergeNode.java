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

import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ComponentColorModel;
import java.awt.image.RenderedImage;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import org.geotools.api.coverage.grid.GridCoverage;
import org.geotools.api.util.InternationalString;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.ImageWorker;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Vocabulary;
import org.geotools.renderer.i18n.VocabularyKeys;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.Hints;

/**
 * This {@link BandMergeNode} wraps a {@link JAI} {@link BandMergeDescriptor} operation for usage withing SLD 1.0
 * processing.
 *
 * @author Simone Giannecchini, GeoSolutions. TODO we should preserve properties
 */
class BandMergeNode extends BaseCoverageProcessingNode implements CoverageProcessingNode {

    /*
     * (non-Javadoc)
     * @see CoverageProcessingNode#getName()
     */
    @Override
    public InternationalString getName() {
        return Vocabulary.formatInternational(VocabularyKeys.BAND_MERGE);
    }

    /*
     * (non-Javadoc)
     * @see org.geotools.renderer.lite.gridcoverage2d.BaseCoverageProcessingNode#dispose(boolean)
     */
    @Override
    public void dispose(boolean force) {
        ///////////////////////////////////////////////////////////////////////
        //
        // Dispose local intermediate operations
        //
        ///////////////////////////////////////////////////////////////////////
        final Iterator<RenderedImage> it = intermediateOps.iterator();
        while (it.hasNext()) {
            final PlanarImage image = PlanarImage.wrapRenderedImage(it.next());
            image.dispose();
        }
        super.dispose(force);
    }

    /**
     * Holds the intermediate {@link RenderedOp} we create along the path for this {@link CoverageProcessingNode} in
     * order to be able to dispose them later on.
     */
    private Stack<RenderedImage> intermediateOps = new Stack<>();

    /** alpha channel from previous nodes to be restored (it may be null) */
    private RenderedImage alpha;

    /**
     * Default constructor for the {@link BandMergeNode} which merge multiple single bands into s single coverage.
     *
     * @param hints {@link Hints} to control this node behavior.
     */
    public BandMergeNode(Hints hints) {
        // we use at most 3 bands for a band merge node according to the specs
        super(
                3,
                hints,
                SimpleInternationalString.wrap("BandMergeNode"),
                SimpleInternationalString.wrap("Node which applies a BandMergeNode following SLD 1.0 spec."));
    }

    @Override
    protected GridCoverage execute() {
        assert getSources().size() <= 3;

        // /////////////////////////////////////////////////////////////////////
        //
        // Get the sources and see what we got to do. Note that if we have more
        // than once source we'll use only the first one but we'll
        //
        // /////////////////////////////////////////////////////////////////////
        final List<CoverageProcessingNode> sources = this.getSources();
        if (sources != null && !sources.isEmpty()) {
            // //
            //
            // only one source, let's forward it, nothing to do.
            //
            // //
            final int size = sources.size();
            final boolean hasAlpha = alpha != null;
            if (size == 1 && !hasAlpha) {
                // returns the source if we don't need to restore the alpha channel
                return getSource(0).getOutput();
            }
            // //
            //
            // We can accept only 3 sources OR 1 source (when alpha need to be added)
            //
            // //
            if (size != 3 && size != 1) {
                throw new IllegalArgumentException(
                        MessageFormat.format(ErrorKeys.INVALID_NUMBER_OF_SOURCES_$1, Integer.valueOf(size)));
            }

            // /////////////////////////////////////////////////////////////////////
            //
            // we have at least two sources, let's merge them
            //
            // /////////////////////////////////////////////////////////////////////
            final Iterator<CoverageProcessingNode> it = sources.iterator();
            RenderedImage op = null;
            GridGeometry2D gridGeometry = null;
            ImageLayout layout = null;
            final Hints hints = getHints();
            final List<GridCoverage2D> sourceGridCoverages = new ArrayList<>();
            ImageWorker w = new ImageWorker();
            do {
                // //
                //
                // Get the source image and do the merge
                //
                // //
                final CoverageProcessingNode currentSourceNode = it.next();
                final GridCoverage2D currentSourceCoverage = (GridCoverage2D) currentSourceNode.getOutput();
                sourceGridCoverages.add(currentSourceCoverage);
                final GridGeometry2D gg = currentSourceCoverage.getGridGeometry();
                if (gridGeometry == null) {
                    // get the envelope for the first source.
                    gridGeometry = gg;

                    // color model
                    final ColorSpace colorSpace = size == 1 && hasAlpha
                            ? ColorSpace.getInstance(ColorSpace.CS_GRAY)
                            : ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
                    final int transparency = hasAlpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE;
                    final ComponentColorModel cm = new ComponentColorModel(
                            colorSpace,
                            hasAlpha,
                            false,
                            transparency,
                            currentSourceCoverage
                                    .getRenderedImage()
                                    .getSampleModel()
                                    .getDataType());
                    layout = new ImageLayout();
                    layout.setColorModel(cm);
                } else if (!gg.equals(gridGeometry))
                    throw new IllegalArgumentException(
                            MessageFormat.format(ErrorKeys.MUST_SHARE_GRIDGEOMETRY_$1, "BandMerge"));

                // //
                //
                // Merge the current source with the results of the others
                // merges
                //
                // //
                if (op == null) {
                    op = currentSourceCoverage.getRenderedImage();
                    w.setImage(op);
                    w.setROI(CoverageUtilities.getROIProperty(currentSourceCoverage));
                    NoDataContainer container = CoverageUtilities.getNoDataProperty(currentSourceCoverage);
                    w.setNoData(container != null ? container.getAsRange() : null);
                } else {
                    w.setRenderingHints(hints);
                    // ROI handling
                    ROI roi = w.getROI();
                    ROI roiProperty = CoverageUtilities.getROIProperty(currentSourceCoverage);
                    if (roi != null) {
                        if (roiProperty != null) {
                            roi = roi.intersect(roiProperty);
                        }

                    } else if (roiProperty != null) {
                        roi = roiProperty;
                    }

                    w.setROI(roi);
                    NoDataContainer container = CoverageUtilities.getNoDataProperty(currentSourceCoverage);
                    w.addBand(
                            currentSourceCoverage.getRenderedImage(),
                            false,
                            false,
                            container != null ? container.getAsRange() : null);
                    // //
                    //
                    // Save the intermediate image
                    //
                    // //
                    op = w.getRenderedImage();
                    intermediateOps.add(op);
                }

            } while (it.hasNext());

            // /////////////////////////////////////////////////////////////////////
            //
            // let's now create the output coverage and
            //
            // /////////////////////////////////////////////////////////////////////
            if (layout != null) hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
            if (hasAlpha && !op.getColorModel().hasAlpha()) {
                // Only restore the alphaChannel if not already available
                w.addBand(alpha, false, true, null);
                op = w.getRenderedImage();
                intermediateOps.add(op);
            }
            op = w.format(op.getSampleModel().getDataType()).getRenderedImage();
            final GridSampleDimension[] sd =
                    new GridSampleDimension[op.getSampleModel().getNumBands()];
            for (int i = 0; i < sd.length; i++)
                sd[i] = new GridSampleDimension(
                        TypeMap.getColorInterpretation(op.getColorModel(), i).name());

            // Defining NoData and ROI properties
            Map<String, Object> properties = new HashMap<>();
            CoverageUtilities.setNoDataProperty(properties, w.getNoData());
            CoverageUtilities.setROIProperty(properties, w.getROI());
            return getCoverageFactory()
                    .create(
                            "BandMerge",
                            op,
                            gridGeometry,
                            sd,
                            sourceGridCoverages.toArray(new GridCoverage[sourceGridCoverages.size()]),
                            properties);
        }
        throw new IllegalStateException(MessageFormat.format(ErrorKeys.SOURCE_CANT_BE_NULL_$1, "BandMergeNode"));
    }

    /** If specified, the result of the bandMerge will contain the alpha channel. */
    public void setAlpha(RenderedImage alpha) {
        this.alpha = alpha;
    }
}
