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

package org.geotools.gce.imagemosaic.granulecollector;

import it.geosolutions.jaiext.JAIExt;
import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.Histogram;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import org.apache.commons.io.FilenameUtils;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.gce.imagemosaic.GranuleDescriptor.GranuleLoadingResult;
import org.geotools.gce.imagemosaic.GranuleLoader;
import org.geotools.gce.imagemosaic.MosaicElement;
import org.geotools.gce.imagemosaic.MosaicInputs;
import org.geotools.gce.imagemosaic.Mosaicker;
import org.geotools.gce.imagemosaic.RasterLayerResponse;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.egr.ROIExcessGranuleRemover;
import org.geotools.geometry.jts.JTS;
import org.geotools.image.ImageWorker;
import org.geotools.util.URLs;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

/** Basic submosaic producer. Accepts all granules and mosaics without any real special handling */
public class BaseSubmosaicProducer implements SubmosaicProducer {

    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(DefaultSubmosaicProducer.class);

    /** The final lists for granules to be computed, splitted per dimension value. */
    protected final List<Future<GranuleDescriptor.GranuleLoadingResult>> granulesFutures = new ArrayList<>();

    protected final boolean dryRun;

    protected RasterLayerResponse rasterLayerResponse;

    /** The number of collected granules. * */
    protected int granulesNumber;

    protected double[][] sourceThreshold;

    protected boolean hasAlpha;

    protected boolean doInputTransparency;

    protected Color inputTransparentColor;

    // boolean forceFootprints = JAIExt.;

    public BaseSubmosaicProducer(RasterLayerResponse rasterLayerResponse, boolean dryRun) {
        this.rasterLayerResponse = rasterLayerResponse;
        this.dryRun = dryRun;
        inputTransparentColor = rasterLayerResponse.getRequest().getInputTransparentColor();
        doInputTransparency = inputTransparentColor != null
                && !rasterLayerResponse.getFootprintBehavior().handleFootprints();
    }

    /**
     * This methods collects the granules from their eventual multithreaded processing and turn them into a
     * {@link MosaicInputs} object.
     *
     * @return a {@link MosaicInputs} ready to be mosaicked.
     */
    protected MosaicInputs collectGranules() throws IOException {
        // do we have anything to do?
        if (granulesNumber <= 0) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "granules number <= 0");
            }
            return null;
        }

        // execute them all
        final StringBuilder paths = new StringBuilder();
        URL sourceUrl = null;
        final List<MosaicElement> returnValues = new ArrayList<>();
        // collect sources for the current dimension and then process them
        for (Future<GranuleDescriptor.GranuleLoadingResult> future : granulesFutures) {

            try {
                // get the resulting RenderedImage
                final GranuleDescriptor.GranuleLoadingResult result = future.get();
                if (result == null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(
                                Level.FINE,
                                "Unable to load the raster for granule with request "
                                        + rasterLayerResponse.getRequest().toString());
                    }
                    continue;
                }
                final RenderedImage loadedImage = result.getRaster();
                if (loadedImage == null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(
                                Level.FINE,
                                "Unable to load the raster for granuleDescriptor "
                                        + result.getGranuleUrl()
                                        + " with request "
                                        + rasterLayerResponse.getRequest().toString());
                    }
                    continue;
                }

                // perform excess granule removal in case multithreaded loading is enabled
                if (isMultithreadedLoadingEnabled()) {
                    ROIExcessGranuleRemover remover = rasterLayerResponse.getExcessGranuleRemover();
                    if (remover != null) {
                        if (remover.isRenderingAreaComplete()) {
                            break;
                        }
                        if (!remover.addGranule(result)) {
                            // skip this granule
                            continue;
                        }
                    }
                }

                // now process it
                if (sourceThreshold == null) {
                    //
                    // We check here if the images have an alpha channel or some
                    // other sort of transparency. In case we have transparency
                    // I also save the index of the transparent channel.
                    //
                    // Specifically, I have to check if the loaded image have
                    // transparency, because if we do a ROI and/or we have a
                    // transparent color to set we have to remove it.
                    //
                    final ColorModel cm = loadedImage.getColorModel();
                    hasAlpha |= cm.hasAlpha();

                    //
                    // we set the input threshold accordingly to the input
                    // image data type. I find the default value (which is 0) very bad
                    // for data type other than byte and ushort. With float and double
                    // it can cut off a large par of the dynamic.
                    //
                    sourceThreshold = new double[][] {
                        {
                            CoverageUtilities.getMosaicThreshold(
                                    loadedImage.getSampleModel().getDataType())
                        }
                    };
                }

                // moving on
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Adding to mosaic granule " + result.getGranuleUrl());
                }

                // path management
                String fileCanonicalPath = null;
                URL url = result.getGranuleUrl();
                File inputFile = URLs.urlToFile(url);
                if (inputFile != null) {
                    String canonicalPath = inputFile.getCanonicalPath();
                    // Remove ovr extension if present
                    fileCanonicalPath = canonicalPath;
                    if (canonicalPath.endsWith(".ovr")) {
                        fileCanonicalPath = canonicalPath.substring(0, canonicalPath.length() - 4);
                    }
                    paths.append(canonicalPath).append(",");
                } else {
                    // Let's go straight using the granuleUrl
                    paths.append(url.toString()).append(",");
                }
                // take only the first source URL found
                if (sourceUrl == null) {
                    sourceUrl = result.getGranuleUrl();
                }

                // add to the mosaic collection, with preprocessing
                // TODO pluggable mechanism for processing (artifacts,etc...)
                MosaicElement input = preProcessGranuleRaster(loadedImage, result, fileCanonicalPath);
                returnValues.add(input);

            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("Adding to mosaic failed, original request was " + rasterLayerResponse.getRequest());
                }
                throw new IOException(e);
            }
        }

        // collect paths
        rasterLayerResponse.addGranulePaths(paths.length() > 1 ? paths.substring(0, paths.length() - 1) : "");
        rasterLayerResponse.setSourceUrl(sourceUrl);

        if (returnValues == null || returnValues.isEmpty()) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("The MosaicElement list is null or empty");
            }
        }
        return new MosaicInputs(doInputTransparency, hasAlpha, returnValues, sourceThreshold);
    }

    private MosaicElement preProcessGranuleRaster(
            RenderedImage granule, final GranuleDescriptor.GranuleLoadingResult result, String canonicalPath) {

        //
        // INDEX COLOR MODEL EXPANSION
        //
        // Take into account the need for an expansions of the original color
        // model.
        //
        // If the original color model is an index color model an expansion
        // might be requested in case the different palettes are not all the
        // same. In this case the mosaic operator from JAI would provide wrong
        // results since it would take the first palette and use that one for
        // all the other images.
        //
        // There is a special case to take into account here. In case the input
        // images use an IndexColorModel it might happen that the transparent
        // color is present in some of them while it is not present in some
        // others. This case is the case where for sure a color expansion is
        // needed. However we have to take into account that during the masking
        // phase the images where the requested transparent color was present
        // will have 4 bands, the other 3. If we want the mosaic to work we
        // have to add an extra band to the latter type of images for providing
        // alpha information to them.
        //
        //
        if (rasterLayerResponse.getRasterManager().isExpandMe() && granule.getColorModel() instanceof IndexColorModel) {
            granule = new ImageWorker(granule).forceComponentColorModel().getRenderedImage();
        }

        //
        // TRANSPARENT COLOR MANAGEMENT
        //
        boolean granuleHasAlpha = false;
        if (doInputTransparency || hasAlpha) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Support for alpha on input granule " + result.getGranuleUrl());
            }
            if (doInputTransparency)
                granule = new ImageWorker(granule)
                        .makeColorTransparent(inputTransparentColor)
                        .getRenderedImage();
            granuleHasAlpha = granule.getColorModel().hasAlpha();
            if (!granule.getColorModel().hasAlpha()) {
                // if the resulting image has no transparency (can happen with IndexColorModel then
                // we need to try component color model
                granule = new ImageWorker(granule)
                        .forceComponentColorModel(true)
                        .makeColorTransparent(inputTransparentColor)
                        .getRenderedImage();
                granuleHasAlpha = granule.getColorModel().hasAlpha();
            }
            assert granuleHasAlpha;
        } else if (!rasterLayerResponse.getFootprintBehavior().handleFootprints()) {
            granuleHasAlpha = granule.getColorModel().hasAlpha();
        }

        PlanarImage alphaChannel = null;
        if (granuleHasAlpha || doInputTransparency) {
            ImageWorker w = new ImageWorker(granule);
            if (granule.getSampleModel() instanceof MultiPixelPackedSampleModel
                    || granule.getColorModel() instanceof IndexColorModel) {
                w.forceComponentColorModel();
                granule = w.getRenderedImage();
            }
            // doing this here gives the guarantee that I get the correct index for the transparency
            // band
            int[] alphaIndex = {granule.getColorModel().getNumComponents() - 1};
            assert alphaIndex[0] < granule.getSampleModel().getNumBands();

            //
            // ALPHA in INPUT
            //
            // I have to select the alpha band and provide it to the final
            // mosaic operator. I have to force going to ComponentColorModel in
            // case the image is indexed.
            //
            alphaChannel = w.retainBands(alphaIndex).getPlanarImage();
        }

        //
        // ROI
        //
        // we need to add its roi in order to avoid problems with the mosaics sources overlapping
        ROI imageROI = null;
        if (rasterLayerResponse.getFootprintBehavior().handleFootprints()
                || rasterLayerResponse.isHeterogeneousCRS()
                || !JAIExt.isJAIExtOperation("Mosaic")) {
            final Rectangle bounds = PlanarImage.wrapRenderedImage(granule).getBounds();
            Geometry mask = JTS.toGeometry(
                    new Envelope(bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(), bounds.getMaxY()));
            imageROI = new ROIGeometry(mask);

            // get the real footprint
            final ROI footprint = result.getFootprint();
            if (footprint != null) {
                if (imageROI.contains(footprint.getBounds2D().getBounds())) {
                    imageROI = footprint;
                } else {
                    imageROI = imageROI.intersect(footprint);
                }
            }
        }

        if (rasterLayerResponse.getFootprintBehavior().handleFootprints()) {

            // ARTIFACTS FILTERING
            if (rasterLayerResponse.getDefaultArtifactsFilterThreshold() != Integer.MIN_VALUE
                    && result.isDoFiltering()) {
                int artifactThreshold = rasterLayerResponse.getDefaultArtifactsFilterThreshold();
                if (rasterLayerResponse.getArtifactsFilterPTileThreshold() != -1) {

                    // Looking for a histogram for that granule in order to
                    // setup dynamic threshold
                    if (canonicalPath != null) {
                        final String path = FilenameUtils.getFullPath(canonicalPath);
                        final String baseName = FilenameUtils.getBaseName(canonicalPath);
                        final String histogramPath = path + baseName + "." + "histogram";
                        final Histogram histogram = Utils.getHistogram(histogramPath);
                        if (histogram != null) {
                            final double[] p =
                                    histogram.getPTileThreshold(rasterLayerResponse.getArtifactsFilterPTileThreshold());
                            artifactThreshold = (int) p[0];
                        }
                    }
                }
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Filtering granules artifacts");
                }
                ImageWorker w = new ImageWorker(granule)
                        .setRenderingHints(rasterLayerResponse.getHints())
                        .setROI(imageROI);
                w.setBackground(new double[] {0});
                w.artifactsFilter(artifactThreshold, 3);
                granule = w.getRenderedImage();
            }
        }

        // preparing input
        return new MosaicElement(alphaChannel, imageROI, granule, result.getPamDataset());
    }

    @Override
    public List<MosaicElement> createMosaic() throws IOException {
        final MosaicElement mosaic = new Mosaicker(
                        this.rasterLayerResponse,
                        collectGranules(),
                        rasterLayerResponse.getRequest().getMergeBehavior())
                .createMosaic();
        if (mosaic == null) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(mosaic);
        }
    }

    @Override
    public boolean accept(GranuleDescriptor granuleDescriptor) {
        return this.acceptGranule(granuleDescriptor);
    }

    protected boolean acceptGranule(GranuleDescriptor granuleDescriptor) {
        SimpleFeature originator = granuleDescriptor.getOriginator();
        Object imageIndex = originator != null ? originator.getAttribute("imageindex") : null;

        if (imageIndex != null && imageIndex instanceof Integer) {
            rasterLayerResponse.setImageChoice((Integer) imageIndex);
        }

        final GranuleLoader loader = new GranuleLoader(
                rasterLayerResponse.getBaseReadParameters(),
                rasterLayerResponse.getImageChoice(),
                rasterLayerResponse.getMosaicBBox(),
                rasterLayerResponse.getFinalWorldToGridCorner(),
                granuleDescriptor,
                rasterLayerResponse.getRequest(),
                rasterLayerResponse.getHints());
        if (!dryRun) {
            final boolean multiThreadedLoading = isMultithreadedLoadingEnabled();
            if (multiThreadedLoading) {
                // MULTITHREADED EXECUTION submitting the task
                final ExecutorService mtLoader =
                        rasterLayerResponse.getRasterManager().getParentReader().getMultiThreadedLoader();
                granulesFutures.add(mtLoader.submit(loader));
            } else {
                // SINGLE THREADED Execution, we defer the execution to when we have done the
                // loading
                final FutureTask<GranuleDescriptor.GranuleLoadingResult> task = new FutureTask<>(loader);
                task.run(); // run in current thread

                // perform excess granule removal, as it makes sense in single threaded mode to
                // do it while loading, to allow for an early bail out reading granules
                ROIExcessGranuleRemover remover = rasterLayerResponse.getExcessGranuleRemover();
                GranuleLoadingResult result;
                if (remover != null) {
                    try {
                        result = task.get();
                        if (!remover.addGranule(result)) {
                            return false;
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
                granulesFutures.add(task);
            }
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("We added the granule " + granuleDescriptor.toString());
        }

        // we added it
        granulesNumber++;
        return true;
    }

    private boolean isMultithreadedLoadingEnabled() {
        final ExecutorService mtLoader =
                rasterLayerResponse.getRasterManager().getParentReader().getMultiThreadedLoader();
        final boolean multiThreadedLoading = rasterLayerResponse.isMultithreadingAllowed() && mtLoader != null;
        return multiThreadedLoading;
    }

    @Override
    public boolean doInputTransparency() {
        return doInputTransparency;
    }

    @Override
    public double[][] getSourceThreshold() {
        return sourceThreshold;
    }

    @Override
    public boolean hasAlpha() {
        return hasAlpha;
    }
}
