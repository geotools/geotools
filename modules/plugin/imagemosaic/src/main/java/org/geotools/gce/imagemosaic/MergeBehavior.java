/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import it.geosolutions.jaiext.utilities.ImageLayout2;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.util.Arrays;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.operator.MosaicDescriptor;
import javax.media.jai.operator.MosaicType;
import org.geotools.image.ImageWorker;

/**
 * This class is responsible for implementing the strategies for the mosaicking which can be a flat merge of
 * band-stacking merge.
 *
 * @author Simone Giannecchini, GeoSolutions TODO check more conditions to use {@link MosaicDescriptor}
 */
public enum MergeBehavior {
    STACK {
        @Override
        public RenderedImage process(
                RenderedImage[] sources,
                double[] backgroundValues,
                double[][] inputThreshold,
                PlanarImage[] sourceAlpha,
                ROI[] sourceROI,
                MosaicType mosaicType,
                RenderingHints hints) {

            // checks
            if (sources.length == 1) {
                return FLAT.process(
                        sources, backgroundValues, inputThreshold, sourceAlpha, sourceROI, mosaicType, hints);
            }

            // put in the same envelope
            sources = harmonizeSources(sources, backgroundValues, inputThreshold, sourceAlpha, sourceROI, hints);

            // Step 3, band merge the images
            // loop on sources
            final ImageWorker worker = new ImageWorker(sources[0]);
            worker.setRenderingHints(hints);
            for (int i = 1; i < sources.length; i++) {
                worker.addBand(sources[i], false);
            }

            // return final image
            return worker.getRenderedImage();
        }
    },
    FLAT {
        @Override
        public RenderedImage process(
                RenderedImage[] sources,
                double[] backgroundValues,
                double[][] inputThreshold,
                PlanarImage[] sourceAlpha,
                ROI[] sourceROI,
                MosaicType mosaicType,
                RenderingHints localHints) {
            return new ImageWorker(localHints)
                    .setBackground(backgroundValues)
                    .mosaic(sources, mosaicType, sourceAlpha, sourceROI, inputThreshold, null)
                    .getRenderedImage();
        }
    },
    MAX {
        @Override
        public RenderedImage process(
                RenderedImage[] sources,
                double[] backgroundValues,
                double[][] inputThreshold,
                PlanarImage[] sourceAlpha,
                ROI[] sourceROI,
                MosaicType mosaicType,
                RenderingHints localHints) {

            // checks
            if (sources.length == 1) {
                return FLAT.process(
                        sources, backgroundValues, inputThreshold, sourceAlpha, sourceROI, mosaicType, localHints);
            }

            // put in the same envelope
            sources = harmonizeSources(sources, backgroundValues, inputThreshold, sourceAlpha, sourceROI, localHints);

            return new ImageWorker(localHints).setImage(sources[0]).max(sources).getRenderedImage();
        }
    },
    MIN {
        @Override
        public RenderedImage process(
                RenderedImage[] sources,
                double[] backgroundValues,
                double[][] inputThreshold,
                PlanarImage[] sourceAlpha,
                ROI[] sourceROI,
                MosaicType mosaicType,
                RenderingHints localHints) {

            // checks
            if (sources.length == 1) {
                return FLAT.process(
                        sources, backgroundValues, inputThreshold, sourceAlpha, sourceROI, mosaicType, localHints);
            }

            // put in the same envelope
            sources = harmonizeSources(sources, backgroundValues, inputThreshold, sourceAlpha, sourceROI, localHints);

            return new ImageWorker(localHints).setImage(sources[0]).min(sources).getRenderedImage();
        }
    };

    /**
     * Prepares multiple sources, potentially not sharing the same extent, for an operation that requires an exact
     * overlap.
     */
    private static RenderedImage[] harmonizeSources(
            RenderedImage[] sources,
            double[] backgroundValues,
            double[][] inputThreshold,
            PlanarImage[] sourceAlpha,
            ROI[] sourceROI,
            RenderingHints hints) {
        RenderedImage[] result = Arrays.copyOf(sources, sources.length);

        // Check if the images don't share the same bounds
        Rectangle union =
                new Rectangle(PlanarImage.wrapRenderedImage(sources[0]).getBounds());
        boolean performMosaic = false;
        for (int i = 1; i < sources.length; i++) {
            Rectangle currentExtent = PlanarImage.wrapRenderedImage(sources[i]).getBounds();
            if (!currentExtent.equals(union)) {
                performMosaic = true;
                union = union.union(currentExtent);
            }
        }
        if (!performMosaic) return result;

        // different extents found, use mosaic to extend each one of them to the union of the bounds
        // shared image layout for all images
        final ImageLayout layout = (ImageLayout) (hints != null ? hints.get(JAI.KEY_IMAGE_LAYOUT) : new ImageLayout2());
        layout.setWidth(union.width).setHeight(union.height).setMinX(union.x).setMinY(union.y);

        final RenderingHints localHints;
        if (hints != null) {
            localHints = (RenderingHints) hints.clone();
            localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
        } else {
            localHints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
        }

        // extend each one using mosaic
        for (int i = 0; i < sources.length; i++) {

            PlanarImage[] sourceAlphas = sourceAlpha == null ? null : new PlanarImage[] {sourceAlpha[i]};
            ROI[] sourceROIs = sourceROI == null ? null : new ROI[] {sourceROI[i]};
            ImageWorker worker = new ImageWorker(localHints);
            worker.setBackground(backgroundValues);
            result[i] = worker.mosaic(
                            new RenderedImage[] {sources[i]},
                            MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                            sourceAlphas,
                            sourceROIs,
                            inputThreshold,
                            null)
                    .getRenderedImage();
        }
        return result;
    }

    /**
     * Process input {@link RenderedImage} to produce the output of this mosaic.
     *
     * @return a {@link RenderedImage}.
     */
    public abstract RenderedImage process(
            RenderedImage[] sources,
            double[] backgroundValues,
            double[][] inputThreshold,
            PlanarImage[] sourceAlpha,
            javax.media.jai.ROI[] sourceROI,
            MosaicType mosaicType,
            RenderingHints localHints);

    /**
     * Retrieves the default {@link MergeBehavior}.
     *
     * @return the default {@link MergeBehavior}.
     */
    public static MergeBehavior getDefault() {
        return FLAT;
    }

    /**
     * Retrieves the possible values as Strings.
     *
     * @return an arrays of {@link String} that contains the representation of each value.
     */
    public static String[] valuesAsStrings() {
        final MergeBehavior[] values = MergeBehavior.values();
        final String[] valuesS = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            valuesS[i] = values[i].toString();
        }
        return valuesS;
    }
}
