/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io.footprint;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ConstantDescriptor;
import javax.media.jai.operator.MosaicDescriptor;
import org.geotools.image.ImageWorker;
import org.geotools.util.factory.Hints;

/**
 * Handles the way footprints should be treated.
 *
 * @author Andrea Aime, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public enum FootprintBehavior {
    None(false) {
        @Override
        public RenderedImage postProcessBlankResponse(
                RenderedImage finalImage, RenderingHints hints) {
            return finalImage;
        }
    },
    Cut(true),
    Transparent(true) {
        @Override
        public RenderedImage postProcessMosaic(
                RenderedImage mosaic, ROI overallROI, RenderingHints hints) {

            // force the current image in RGB or Gray
            final ImageWorker imageWorker = new ImageWorker(mosaic);
            hints = prepareHints(hints);
            imageWorker.setRenderingHints(hints);

            // make sure the mosaic image is either gray of RGB
            if (!imageWorker.isColorSpaceGRAYScale()) {
                if (!imageWorker.isColorSpaceRGB()) {
                    imageWorker.forceColorSpaceRGB();
                }
            }
            imageWorker.forceComponentColorModel(); // todo optimize with paletted imagery

            // do we already have a alpha band in the input image?
            if (imageWorker.getRenderedImage().getColorModel().hasAlpha()) {
                // if so we reuse it applying the ROI on top of it
                RenderedImage alpha =
                        new ImageWorker(imageWorker.getRenderedImage())
                                .retainLastBand()
                                .getRenderedImage();
                RenderedImage maskedAlpha =
                        new ImageWorker(hints)
                                .mosaic(
                                        new RenderedImage[] {alpha},
                                        MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                                        null,
                                        new ROI[] {overallROI},
                                        null,
                                        null)
                                .getRenderedImage();

                imageWorker.retainBands(mosaic.getColorModel().getNumColorComponents());
                imageWorker.addBand(maskedAlpha, false, true, null);
            } else {

                // turn the roi into a single band image and add it to the mosaic as transparency
                final ImageWorker roiImageWorker = new ImageWorker(overallROI.getAsImage());
                roiImageWorker.setRenderingHints(hints);

                PlanarImage alpha =
                        roiImageWorker
                                .forceComponentColorModel()
                                .retainFirstBand()
                                .getPlanarImage();
                if (!alpha.getBounds().equals(imageWorker.getPlanarImage().getBounds())) {
                    // build final layout and use it for giving the alpha band a simil size and
                    // tiling
                    // to the one of the image
                    final ImageLayout layout =
                            new ImageLayout(
                                    mosaic.getMinX(),
                                    mosaic.getMinY(),
                                    mosaic.getWidth(),
                                    mosaic.getHeight());

                    final SampleModel sampleModel = mosaic.getSampleModel();
                    layout.setTileHeight(sampleModel.getWidth())
                            .setTileWidth(sampleModel.getHeight());
                    hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));

                    // correct bounds of the current image
                    alpha =
                            new ImageWorker(hints)
                                    .mosaic(
                                            new RenderedImage[] {alpha},
                                            MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                                            null,
                                            new ROI[] {overallROI},
                                            null,
                                            null)
                                    .getRenderedOperation();
                }
                imageWorker.addBand(alpha, false, true, null);
            }

            RenderedImage result = imageWorker.getRenderedImage();
            return result;
        }

        /** */
        private RenderingHints prepareHints(RenderingHints hints) {
            if (hints == null) {
                hints = new Hints();
            } else {
                hints = (RenderingHints) hints.clone();
            }
            hints.remove(JAI.KEY_IMAGE_LAYOUT); // remove an eventual layout passed down to us
            return hints;
        }

        @Override
        public RenderedImage postProcessBlankResponse(
                RenderedImage finalImage, RenderingHints hints) {
            // force the current image in RGB or Gray
            final ImageWorker imageWorker = new ImageWorker(finalImage);
            hints = prepareHints(hints);
            imageWorker.setRenderingHints(hints);

            // make sure the mosaic image is either gray of RGB
            if (!imageWorker.isColorSpaceGRAYScale()) {
                if (!imageWorker.isColorSpaceRGB()) {
                    imageWorker.forceColorSpaceRGB();
                }
            }
            imageWorker.forceComponentColorModel(); // todo optimize with paletted imagery

            if (!imageWorker.getRenderedImage().getColorModel().hasAlpha()) {
                // create an alpha band that is transparent
                final ImageWorker imageWorker2 = new ImageWorker(finalImage);
                imageWorker2.setRenderingHints(hints);

                // trick to get a 0 band
                RenderedImage alpha =
                        imageWorker2
                                .retainFirstBand()
                                .multiplyConst(new double[] {0.0})
                                .getRenderedImage();
                imageWorker2.dispose();
                imageWorker.addBand(alpha, false, true, null);
            }

            RenderedImage result = imageWorker.getRenderedImage();
            imageWorker.dispose();

            return super.postProcessBlankResponse(result, hints);
        }
    };

    private boolean handleFootprints;

    FootprintBehavior(boolean handleFootprints) {
        this.handleFootprints = handleFootprints;
    }

    public boolean handleFootprints() {
        return handleFootprints;
    }

    /**
     * Retrieves the default {@link FootprintBehavior}.
     *
     * @return the default {@link FootprintBehavior}.
     */
    public static FootprintBehavior getDefault() {
        return None;
    }

    /**
     * Retrieves the possible values as Strings.
     *
     * @return an arrays of {@link String} that contains the representation of each value.
     */
    public static String[] valuesAsStrings() {
        final FootprintBehavior[] values = FootprintBehavior.values();
        final String[] valuesS = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            valuesS[i] = values[i].toString();
        }
        return valuesS;
    }

    /** Applies post processing to the result mosaic, eventually making certain areas transparent */
    public RenderedImage postProcessMosaic(
            RenderedImage mosaic, ROI overallROI, RenderingHints hints) {
        return mosaic;
    }

    /** Post processes a blank image response, eventually making it transparent */
    public RenderedImage postProcessBlankResponse(RenderedImage finalImage, RenderingHints hints) {
        // prepare a ROI made of only zeroes
        ImageLayout layout =
                new ImageLayout(
                        finalImage.getMinX(),
                        finalImage.getMinY(),
                        finalImage.getWidth(),
                        finalImage.getHeight());
        RenderedOp roi =
                ConstantDescriptor.create(
                        (float) finalImage.getWidth(),
                        (float) finalImage.getHeight(),
                        new Byte[] {0},
                        new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));

        ImageWorker iw = new ImageWorker(finalImage);
        iw.setROI(new ROI(roi));
        return iw.getRenderedImage();
    }
}
