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
import javax.media.jai.BorderExtender;
import javax.media.jai.BorderExtenderConstant;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.operator.MosaicDescriptor;
import javax.media.jai.operator.MosaicType;
import org.geotools.image.ImageWorker;

/**
 * This class is responsible for implementing the strategies for the mosaicking which can be a flat
 * merge of band-stacking merge.
 *
 * @author Simone Giannecchini, GeoSolutions TODO check more conditions to use {@link
 *     MosaicDescriptor}
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
                        sources,
                        backgroundValues,
                        inputThreshold,
                        sourceAlpha,
                        sourceROI,
                        mosaicType,
                        hints);
            }

            // Step 1 check if we need to create a background mosaic
            Rectangle union = new Rectangle(PlanarImage.wrapRenderedImage(sources[0]).getBounds());
            boolean performMosaic = false;
            for (int i = 1; i < sources.length; i++) {
                // current extent
                Rectangle currentExtent = PlanarImage.wrapRenderedImage(sources[0]).getBounds();
                if (!currentExtent.equals(union)) {
                    performMosaic = true;

                    // union
                    union = union.union(currentExtent);
                }
            }

            // Step 2 (Optional) use Border operator to bring the images to the same extent

            // needs to use mosaic ? DO we have ROIs?
            boolean border = true;
            if (sourceROI != null) {
                for (ROI roi : sourceROI) {
                    if (roi != null) {
                        border = false;
                        break;
                    }
                }
            }
            if (performMosaic) {
                // BORDER extender
                final BorderExtender borderExtenderConstant =
                        new BorderExtenderConstant(backgroundValues);
                // loop on sources
                for (int i = 0; i < sources.length; i++) {
                    if (border) {
                        // do we need to extend?
                        if (!PlanarImage.wrapRenderedImage(sources[i]).getBounds().equals(union)) {
                            // current extent
                            Rectangle currentExtent =
                                    PlanarImage.wrapRenderedImage(sources[0]).getBounds();

                            // add BORDER to the current source
                            ImageWorker worker =
                                    new ImageWorker(sources[i]).setRenderingHints(hints);
                            worker.border(
                                    union.x - currentExtent.x,
                                    union.x + union.width - currentExtent.x - currentExtent.width,
                                    union.y - currentExtent.y,
                                    union.y + union.height - currentExtent.y - currentExtent.height,
                                    borderExtenderConstant);
                            sources[i] = worker.getRenderedImage();
                        }
                    } else {
                        // use msoaic in case we have source ROIs
                        final ImageLayout layout =
                                (ImageLayout)
                                        (hints != null
                                                ? hints.get(JAI.KEY_IMAGE_LAYOUT)
                                                : new ImageLayout2());
                        layout.setWidth(union.width)
                                .setHeight(union.height)
                                .setMinX(union.x)
                                .setMinY(union.y);
                        final RenderingHints localHints;
                        if (hints != null) {
                            localHints = (RenderingHints) hints.clone();
                            localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
                        } else {
                            localHints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
                        }
                        return new ImageWorker(localHints)
                                .setBackground(backgroundValues)
                                .mosaic(
                                        sources,
                                        MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                                        sourceAlpha,
                                        sourceROI,
                                        inputThreshold,
                                        null)
                                .getRenderedImage();
                    }
                }
            }

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
    };

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
