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

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.processing.Operations;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.gce.imagemosaic.MergeBehavior;
import org.geotools.gce.imagemosaic.MosaicElement;
import org.geotools.gce.imagemosaic.Mosaicker;
import org.geotools.gce.imagemosaic.RasterLayerRequest;
import org.geotools.gce.imagemosaic.RasterLayerResponse;
import org.geotools.gce.imagemosaic.RasterManager;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * SubmosaicProducer that can handle reprojecting its contents into the target mosaic CRS. This
 * works by grouping together everything with a like CRS (and like SortBy property if supplied) and
 * mosaicking them separately before forming a final mosaic.
 *
 * This relies on the SortBy including CRS as a final SortBy clause
 */
class ReprojectingSubmosaicProducer extends BaseSubmosaicProducer {

    private final boolean dryRun;

    private final RenderingHints renderingHints;

    // operations factory to use for resampling
    private final Operations operations;

    private CoordinateReferenceSystem targetCRS;

    private List<CRSBoundMosaicProducer> perMosaicProducers = new ArrayList<>();

    private CoordinateReferenceSystem currentCRS;

    private CRSBoundMosaicProducer currentSubmosaicProducer;

    ReprojectingSubmosaicProducer(RasterLayerRequest request, RasterLayerResponse response,
            RasterManager rasterManager, boolean dryRun) {
        super(response, dryRun);
        this.targetCRS = rasterManager.getConfiguration().getCrs();
        this.dryRun = dryRun;

        Hints hints = rasterManager.getHints();
        this.renderingHints = createRenderingHints(hints, request);
        this.operations = new Operations(renderingHints);
    }

    private static RenderingHints createRenderingHints(Hints hints, RasterLayerRequest request) {
        RenderingHints renderHints = new RenderingHints(null);
        if (request.getInterpolation() != null) {
            renderHints.put(JAI.KEY_INTERPOLATION, request.getInterpolation());
        }

        return renderHints;
    }

    @Override
    public boolean accept(GranuleDescriptor granuleDescriptor) {

        if (this.currentCRS == null) {
            //this is the first granule we're processing, let's set up the current producer and
            //"forcefully" accept it with doAccept
            this.currentCRS = granuleDescriptor.getGranuleEnvelope().getCoordinateReferenceSystem();
            this.currentSubmosaicProducer = new CRSBoundMosaicProducer(
                rasterLayerResponse,
                dryRun,
                currentCRS, granuleDescriptor);
            perMosaicProducers.add(currentSubmosaicProducer);
            return true;
        } else {
            //we have a current CRS group, either it matches or we need to create a new one
            boolean accepted = currentSubmosaicProducer.accept(granuleDescriptor);
            if (!accepted) {
                //granule was rejected by the current producer, presumably because its CRS didn't
                //match, we need to create a new one because we've moved on to the next
                this.currentSubmosaicProducer = new CRSBoundMosaicProducer(
                    rasterLayerResponse,
                    dryRun,
                    granuleDescriptor.getGranuleEnvelope().getCoordinateReferenceSystem(), granuleDescriptor);
                perMosaicProducers.add(currentSubmosaicProducer);
                accepted = currentSubmosaicProducer.acceptGranule(granuleDescriptor);
            }
            return accepted;
        }

    }

    protected static CoordinateReferenceSystem getCRS(String granuleCRSCode) throws FactoryException {
        return CRS.decode(granuleCRSCode);
    }

    @Override
    public List<MosaicElement> createMosaic() throws IOException {

        List<MosaicElement> mosaicInputs = new ArrayList<>();
        for (CRSBoundMosaicProducer mosaicProducer : this.perMosaicProducers) {
            List<MosaicElement> mosaicElement = mosaicProducer.createMosaic();
            this.hasAlpha = mosaicProducer.hasAlpha();
            try {
                for (MosaicElement e : mosaicElement) {
                    MosaicElement reprojectedMosaicElement = this.reprojectMosaicElement(e,
                            mosaicProducer);
                    mosaicInputs.add(reprojectedMosaicElement);
                }
            } catch (FactoryException e) {
                throw new IllegalStateException(e);
            }
        }

        return mosaicInputs;
    }

    private MosaicElement reprojectMosaicElement(MosaicElement mosaicElement,
            CRSBoundMosaicProducer mosaicProducer) throws FactoryException {

        if (!CRS.equalsIgnoreMetadata(targetCRS, mosaicProducer.getCrs())) {
            GridCoverageFactory factory = new GridCoverageFactory(null);

            GridCoverage2D submosaicCoverage = factory.create("resampled",
                    mosaicElement.getSource(), mosaicProducer.submosaicBBOX);
            GridCoverage2D resampledCoverage = (GridCoverage2D) operations
                    .resample(submosaicCoverage, targetCRS);

            RenderedImage resampledImage = resampledCoverage.getRenderedImage();

            PlanarImage alphaBand = resampledImage.getColorModel().hasAlpha()
                    ? new ImageWorker(resampledImage).retainLastBand().getPlanarImage() : null;

            Object property = resampledCoverage.getProperty("ROI");
            ROI overallROI = (property instanceof ROI) ? (ROI) property : null;
            return new MosaicElement(alphaBand, overallROI, resampledCoverage.getRenderedImage(),
                    mosaicElement.getPamDataset());
        } else {
            return mosaicElement;
        }
    }

    /**
     * This submosaic producer takes a CRS and then only accepts granules that match that CRS.
     *
     */
    private static class CRSBoundMosaicProducer extends BaseSubmosaicProducer {

        private final CoordinateReferenceSystem crs;

        private ReferencedEnvelope submosaicBBOX;

        public CRSBoundMosaicProducer(RasterLayerResponse rasterLayerResponse, boolean dryRun,
            CoordinateReferenceSystem crs, GranuleDescriptor templateDescriptor) {
            super(rasterLayerResponse, dryRun);
            this.crs = crs;

            //always accept the template granule descriptor
            this.doAccept(templateDescriptor);
        }

        @Override
        public List<MosaicElement> createMosaic() throws IOException {
            return Collections
                    .singletonList((new Mosaicker(this.rasterLayerResponse,
                            collectGranules(), MergeBehavior.FLAT)).createMosaic(false, true));
        }

        @Override
        public boolean accept(GranuleDescriptor granuleDescriptor) {
            //make sure the CRSs match
            boolean shouldAccept = false;

            //need to check that the granule matches CRS
            CoordinateReferenceSystem granuleCRS = granuleDescriptor.getGranuleEnvelope().getCoordinateReferenceSystem();
            shouldAccept = CRS.equalsIgnoreMetadata(granuleCRS, this.crs);

            return shouldAccept && doAccept(granuleDescriptor);
        }

        /**
         * This method skips any checking of the granuleDescriptor. Only used when we're sure this
         * descriptor belongs in this producer
         * @param granuleDescriptor
         * @return
         */
        private boolean doAccept(GranuleDescriptor granuleDescriptor) {
            // Need to keep track of the eventual bounding box of this submosaic
            if (this.submosaicBBOX == null) {
                this.submosaicBBOX = new ReferencedEnvelope(granuleDescriptor.getGranuleEnvelope());
            } else {
                this.submosaicBBOX.expandToInclude(
                    new ReferencedEnvelope(granuleDescriptor.getGranuleEnvelope()));
            }

            return super.accept(granuleDescriptor);
        }

        public CoordinateReferenceSystem getCrs() {
            return crs;
        }
    }
}
