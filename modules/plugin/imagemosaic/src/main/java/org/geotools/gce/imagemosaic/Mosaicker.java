/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.gce.imagemosaic;

import static org.geotools.gce.imagemosaic.ImageMosaicConfigHandler.LOGGER;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import javax.media.jai.TileCache;
import javax.media.jai.TileScheduler;
import javax.media.jai.operator.MosaicDescriptor;

import org.geotools.coverage.grid.io.footprint.FootprintBehavior;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.resources.geometry.XRectangle2D;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.util.logging.Logging;
import it.geosolutions.jaiext.vectorbin.ROIGeometry;

import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.jaiext.range.NoDataContainer;

/**
 * A class doing the mosaic operation on top of a List of {@link MosaicElement}s.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class Mosaicker {

    private static final Logger LOGGER = Logging.getLogger(Mosaicker.class);

    private final List<MosaicElement> inputs;

    private final double[][] sourceThreshold;

    private final boolean doInputTransparency;

    private final boolean hasAlpha;

    private final MergeBehavior mergeBehavior;

    private RasterLayerResponse rasterLayerResponse;

    public Mosaicker(RasterLayerResponse rasterLayerResponse, MosaicInputs inputs,
            MergeBehavior mergeBehavior) {
        this.inputs = new ArrayList<>(inputs.getSources());
        this.sourceThreshold = inputs.getSourceThreshold();
        this.doInputTransparency = inputs.isDoInputTransparency();
        this.hasAlpha = inputs.isHasAlpha();
        this.mergeBehavior = mergeBehavior;
        this.rasterLayerResponse = rasterLayerResponse;
    }

    /**
     * @return
     * @param useFinalImageLayout whether the layout should be created from the requested bounds or no layout should be provided
     */
    private RenderingHints prepareHints(boolean useFinalImageLayout) {
        final RenderingHints localHints = new RenderingHints(null);

        if (useFinalImageLayout) {
            // build final layout and use it for cropping purposes
            final ImageLayout layout = new ImageLayout(rasterLayerResponse.getRasterBounds().x,
                    rasterLayerResponse.getRasterBounds().y, rasterLayerResponse.getRasterBounds().width,
                    rasterLayerResponse.getRasterBounds().height);
            Dimension tileDimensions = rasterLayerResponse.getRequest().getTileDimensions();
            if (tileDimensions == null) {
                tileDimensions = (Dimension) JAI.getDefaultTileSize().clone();
            }
            layout.setTileGridXOffset(0).setTileGridYOffset(0);
            layout.setTileHeight(tileDimensions.height).setTileWidth(tileDimensions.width);

            localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
        }

        // look for additional hints for caching and tile scheduling
        if (rasterLayerResponse.getHints() != null && !rasterLayerResponse.getHints().isEmpty()) {

            // TileCache
            TileCache tc = Utils.getTileCacheHint(rasterLayerResponse.getHints());
            if (tc != null) {
                localHints.add(new RenderingHints(JAI.KEY_TILE_CACHE, tc));
            }

            // BorderExtender
            localHints.add(ImageUtilities.BORDER_EXTENDER_HINTS);// default
            BorderExtender be = Utils.getBorderExtenderHint(rasterLayerResponse.getHints());
            if (be != null) {
                localHints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER, be));
            }

            // TileScheduler
            TileScheduler tileScheduler = Utils.getTileSchedulerHint(rasterLayerResponse.getHints());
            if (tileScheduler != null) {
                localHints.add(new RenderingHints(JAI.KEY_TILE_SCHEDULER, tileScheduler));
            }
        }
        return localHints;
    }

    /**
     * Once we reach this method it means that we have loaded all the images which were intersecting the requested envelope. Next step is to
     * create the final mosaic image and cropping it to the exact requested envelope.
     *
     * @return A {@link MosaicElement}}.
     */
    public MosaicElement createMosaic() throws IOException {
        return createMosaic(true);
    }

    /**
     * Once we reach this method it means that we have loaded all the images which were intersecting the requested envelope. Next step is to
     * create the final mosaic image and cropping it to the exact requested envelope.
     *
     * @return A {@link MosaicElement}}.
     * @param useFinalImageLayout whether the final image layout requested should be used. if false then a default layout will be used. useful if
     *        your mosaic layout doesn't match the final layout. default layout will be whatever layout is necessary to do the mosaic op
     */
    public MosaicElement createMosaic(boolean useFinalImageLayout) throws IOException {
        return createMosaic(useFinalImageLayout, false);
    }

    /**
     * Once we reach this method it means that we have loaded all the images which were intersecting the requested envelope. Next step is to
     * create the final mosaic image and cropping it to the exact requested envelope.
     *
     * @return A {@link MosaicElement}}.
     * @param useFinalImageLayout whether the final image layout requested should be used. if false then a default layout will be used. useful if
     *        your mosaic layout doesn't match the final layout. default layout
     * @param skipSingleElementOptimization whether the single element case should be optimized. some callers wish to skip this since there are a
     *        few differences along this path that can cause issues (namely using the final image layout for operations)
     */
    public MosaicElement createMosaic(boolean useFinalImageLayout,
            boolean skipSingleElementOptimization) throws IOException {

        // anything to do?
        final int size = inputs.size();
        if (size <= 0) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Unable to load any granuleDescriptor ");
            }
            return null;
        }

        // === prepare hints
        final RenderingHints localHints = prepareHints(useFinalImageLayout);

        //
        // SPECIAL CASE
        // 1 single tile, we try not do a mosaic.
        if (!skipSingleElementOptimization && size == 1 && Utils.OPTIMIZE_CROP) {
            // prepare input
            MosaicElement in = inputs.get(0);
            if (in == null) {
                throw new NullPointerException(
                        "The list of MosaicElements contains one element but it's null");
            }
            PAMDataset pamDataset = in.pamDataset;

            // the roi is exactly equal to the image
            ROI roi = in.roi;
            if (roi != null) {
                Rectangle bounds = Utils.toRectangle(roi.getAsShape());
                if (bounds != null) {
                    RenderedImage mosaic = in.source;
                    Rectangle imageBounds = PlanarImage.wrapRenderedImage(mosaic).getBounds();
                    if (imageBounds.equals(bounds)) {

                        // do we need to crop? (image is bigger than requested?)
                        if (!rasterLayerResponse.getRasterBounds().contains(imageBounds)) {
                            // we have to crop
                            XRectangle2D.intersect(imageBounds,
                                rasterLayerResponse.getRasterBounds(), imageBounds);

                            if (imageBounds.isEmpty()) {
                                // return back a constant image
                                return null;
                            }
                            // crop
                            ImageWorker iw = new ImageWorker(mosaic);
                            iw.setRenderingHints(localHints);
                            iw.crop(imageBounds.x, imageBounds.y, imageBounds.width,
                                    imageBounds.height);
                            mosaic = iw.getRenderedImage();
                            // Propagate NoData
                            PlanarImage t = PlanarImage.wrapRenderedImage(mosaic);
                            if (iw.getNoData() != null) {
                                t.setProperty(NoDataContainer.GC_NODATA,
                                        new NoDataContainer(iw.getNoData()));
                                mosaic = t;
                            }
                            imageBounds = t.getBounds();
                        }

                        // and, do we need to add a BORDER around the image?
                        if (!imageBounds.contains(rasterLayerResponse.getRasterBounds())) {
                            mosaic = MergeBehavior.FLAT.process(new RenderedImage[] { mosaic },
                                rasterLayerResponse.getBackgroundValues(), sourceThreshold,
                                    (hasAlpha || doInputTransparency)
                                            ? new PlanarImage[] { in.alphaChannel }
                                            : new PlanarImage[] { null },
                                    new ROI[] { in.roi },
                                    rasterLayerResponse.getRequest().isBlend()
                                            ? MosaicDescriptor.MOSAIC_TYPE_BLEND
                                            : MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                                    localHints);
                            roi = roi.add(new ROIGeometry(JTS.toGeometry(new ReferencedEnvelope(
                                rasterLayerResponse.getRasterBounds(), null))));
                            if (rasterLayerResponse.getFootprintBehavior() != FootprintBehavior.None) {
                                // Adding globalRoi to the output
                                RenderedOp rop = (RenderedOp) mosaic;
                                rop.setProperty("ROI", in.roi);


                                mosaic = rasterLayerResponse.getFootprintBehavior().postProcessMosaic(mosaic, in.roi,
                                    localHints);
                            }
                        }

                        // add to final list
                        return new MosaicElement(in.alphaChannel, roi, mosaic, pamDataset);
                    }
                }
            }
        }

        // === do the mosaic as usual
        // prepare sources for the mosaic operation
        final RenderedImage[] sources = new RenderedImage[size];
        final PlanarImage[] alphas = new PlanarImage[size];
        ROI[] rois = new ROI[size];
        final PAMDataset[] pams = new PAMDataset[size];
        int realROIs = 0;
        for (int i = 0; i < size; i++) {
            final MosaicElement mosaicElement = inputs.get(i);
            sources[i] = mosaicElement.source;
            alphas[i] = mosaicElement.alphaChannel;
            rois[i] = mosaicElement.roi;
            pams[i] = mosaicElement.pamDataset;

            // If we have an alpha, mask it by the ROI
            if (alphas[i] != null && rois[i] != null) {
                // Get ROI as image, fix color space
                ImageWorker roi = new ImageWorker(rois[i].getAsImage());
                roi.forceComponentColorModel();
                ImageWorker alpha = new ImageWorker(alphas[i]);
                alpha.multiply(roi.getRenderedImage());

                alphas[i] = alpha.getPlanarImage();
            }
            // compose the overall ROI if needed
            if (mosaicElement.roi != null) {
                realROIs++;
            }
        }
        if (realROIs == 0) {
            rois = null;
        }

        // execute mosaic
        final RenderedImage mosaic = mergeBehavior
                .process(sources, rasterLayerResponse.getBackgroundValues(), sourceThreshold,
                        (hasAlpha || doInputTransparency) ? alphas : null, rois,
                        rasterLayerResponse.getRequest().isBlend()
                                ? MosaicDescriptor.MOSAIC_TYPE_BLEND
                                : MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                        localHints);

        Object property = mosaic.getProperty("ROI");
        ROI overallROI = (property instanceof ROI) ? (ROI) property : null;
        final RenderedImage postProcessed = rasterLayerResponse.getFootprintBehavior()
                .postProcessMosaic(mosaic, overallROI, localHints);

        // prepare for next step
        if (hasAlpha || doInputTransparency) {
            return new MosaicElement(
                    new ImageWorker(postProcessed).retainLastBand().getPlanarImage(),
                    overallROI, postProcessed, Utils.mergePamDatasets(pams));
        } else {
            return new MosaicElement(null, overallROI, postProcessed,
                    Utils.mergePamDatasets(pams));
        }

    }

}
