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
package org.geotools.gce.imagemosaic.catalog;

import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageReadParam;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.coverage.grid.io.imageio.ReadType;
import org.geotools.geometry.jts.GeometryClipper;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.awt.ShapeReader;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;

/**
 * A ROIGeometry provider that handles multi-scale ROI, supporting per imageIndex overviews's geometries being stored in
 * separate sidecar file
 *
 * @author Daniele Romagnoli - GeoSolutions
 */
public class MultiLevelROIGeometryOverviews implements MultiLevelROI {

    /** {@link Logger} used for logging exceptions */
    private static final Logger LOGGER = Logging.getLogger(MultiLevelROIGeometryOverviews.class);

    private static final AffineTransformation Y_INVERSION = new AffineTransformation(1, 0, 0, 0, -1, 0);

    /** The original footprint geometry */
    private Geometry originalFootprint;

    /** The overviews footprints geometries */
    private List<Geometry> multilevelFootprints;

    private SoftValueHashMap<AffineTransform, ROIGeometry> roiCache = new SoftValueHashMap<>(10);

    private boolean empty;

    /** The number of overviews */
    private int numOverviews;

    private Hints hints;

    /**
     * Flag specifying whether overview's ROI are expressed in raster space coordinates (True) or model space
     * coordinates (False)
     */
    private boolean overviewsRoiInRasterSpace;

    public MultiLevelROIGeometryOverviews(
            Geometry footprint, List<Geometry> multilevelFootprints, boolean overviewsInRasterSpace, Hints hints) {
        this.originalFootprint = footprint;
        this.multilevelFootprints = multilevelFootprints;
        this.numOverviews = multilevelFootprints != null ? multilevelFootprints.size() : 0;
        this.overviewsRoiInRasterSpace = overviewsInRasterSpace;
        this.empty = originalFootprint.isEmpty();
        this.hints = hints;
    }

    @Override
    public ROIGeometry getTransformedROI(
            AffineTransform at, int imageIndex, Rectangle imgBounds, ImageReadParam params, ReadType readType) {
        if (empty) {
            return null;
        }
        if (at == null) {
            at = new AffineTransform();
        }
        ROIGeometry roiGeometry = roiCache.get(at);
        if (roiGeometry == null) {
            boolean useOverviews = imageIndex != 0 && numOverviews > 0;
            Geometry rescaled =
                    useOverviews ? multilevelFootprints.get(imageIndex - 1).copy() : originalFootprint.copy();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Using footprint's overview: " + useOverviews);
            }
            AffineTransformation geometryAT = null;
            if (useOverviews && overviewsRoiInRasterSpace) {
                Rectangle sourceRegion = params.getSourceRegion();
                final double xScale = imgBounds.getWidth() / sourceRegion.getWidth() * 1.0;
                final double yScale = imgBounds.getHeight() / sourceRegion.getHeight() * 1.0;

                // Need to align the Overview's geometry to the read area (sourceRegion offset)
                geometryAT = new AffineTransformation(1, 0, -sourceRegion.x, 0, 1, -sourceRegion.y);
                // Need to scale it if the requested extent was different with respect to the
                // returned extent
                geometryAT.scale(xScale, yScale);

                // rescale the geometry to align it with the read portion
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Adapting overview's geometry to current image, using transformation: "
                            + geometryAT.toString());
                }

                rescaled.apply(geometryAT);

            } else {
                geometryAT = new AffineTransformation(
                        at.getScaleX(),
                        at.getShearX(),
                        at.getTranslateX(),
                        at.getShearY(),
                        at.getScaleY(),
                        at.getTranslateY());
                rescaled.apply(geometryAT);
            }

            if (!rescaled.isEmpty()) {

                // the geometry is likely to have way more precision than needed, simplify it
                // so that the error is significantly less than one pixel
                Geometry simplified = TopologyPreservingSimplifier.simplify(rescaled, 0.333);
                // build a ROI geometry optimized for rectangle clipping
                roiGeometry = new FastClipROIGeometry(simplified, hints);
                roiCache.put(at, roiGeometry);
            } else {
                return null;
            }
        }

        return roiGeometry;
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public Geometry getFootprint() {
        return originalFootprint;
    }

    /**
     * A ROIGeometry leveraging {@link GeometryClipper} for fast clipping against rectangles
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class FastClipROIGeometry extends ROIGeometry {

        private static final long serialVersionUID = -4283288388988174306L;
        private Hints hints;

        public FastClipROIGeometry(Geometry geom, Hints hints) {
            super(geom, hints);
            this.hints = hints;
        }

        @Override
        public ROI intersect(ROI roi) {
            final Geometry geom = getGeometry(roi);
            // is it a rectangle?
            if (geom != null && geom.equalsExact(geom.getEnvelope())) {
                GeometryClipper clipper = new GeometryClipper(geom.getEnvelopeInternal());
                Geometry intersect = clipper.clip(getAsGeometry(), true);
                return new ROIGeometry(intersect, hints);

            } else {
                return super.intersect(roi);
            }
        }

        /**
         * Gets a {@link Geometry} from an input {@link ROI}.
         *
         * @param roi the ROI
         * @return a {@link Geometry} instance from the provided input; null in case the input roi is neither a
         *     geometry, nor a shape.
         */
        private Geometry getGeometry(ROI roi) {
            if (roi instanceof ROIGeometry) {
                return ((ROIGeometry) roi).getAsGeometry();
            } else if (roi instanceof ROIShape) {
                final Shape shape = roi.getAsShape();
                final Geometry geom = ShapeReader.read(shape, 0, new GeometryFactory());
                geom.apply(Y_INVERSION);
                return geom;
            }
            return null;
        }
    }
}
