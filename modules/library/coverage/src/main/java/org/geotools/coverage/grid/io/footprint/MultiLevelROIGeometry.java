/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageReadParam;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import org.geotools.coverage.grid.io.imageio.ReadType;
import org.geotools.geometry.jts.GeometryClipper;
import org.geotools.util.SoftValueHashMap;
import org.locationtech.jts.awt.ShapeReader;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;

/**
 * A ROIGeometry provider that handles multi-scale ROI with some extras:
 *
 * <ul>
 *   <li>Caching of reduced resolution of the same ROI
 *   <li>Management of the footprint inset
 * </ul>
 *
 * @author Andrea Aime - GeoSolutions
 */
public class MultiLevelROIGeometry implements MultiLevelROI {

    private Geometry originalFootprint;

    private Geometry insetFootprint;

    private Geometry granuleBounds;

    private double inset;

    private FootprintInsetPolicy insetPolicy;

    private SoftValueHashMap<AffineTransform, ROIGeometry> roiCache =
            new SoftValueHashMap<AffineTransform, ROIGeometry>(10);

    private boolean empty;

    public MultiLevelROIGeometry(
            Geometry footprint,
            Geometry granuleBounds,
            double inset,
            FootprintInsetPolicy insetPolicy) {
        this.originalFootprint = footprint;
        this.granuleBounds = granuleBounds;
        this.inset = inset;
        this.insetPolicy = insetPolicy;
        if (inset > 0) {
            insetFootprint = insetPolicy.applyInset(originalFootprint, granuleBounds, inset);
            this.empty = insetFootprint.isEmpty();
        } else {
            this.empty = originalFootprint.isEmpty();
        }
    }

    public ROIGeometry getTransformedROI(
            AffineTransform at,
            int imageIndex,
            Rectangle imgBounds,
            ImageReadParam params,
            ReadType readType) {
        if (empty) {
            return null;
        }
        if (at == null) {
            at = new AffineTransform();
        }
        ROIGeometry roiGeometry = roiCache.get(at);
        if (roiGeometry == null) {
            Geometry rescaled;
            AffineTransformation geometryAT =
                    new AffineTransformation(
                            at.getScaleX(),
                            at.getShearX(),
                            at.getTranslateX(),
                            at.getShearY(),
                            at.getScaleY(),
                            at.getTranslateY());
            if (inset > 0) {
                double scale = Math.min(Math.abs(at.getScaleX()), Math.abs(at.getScaleY()));
                double rescaledInset = scale * inset;
                if (rescaledInset < 1) {
                    // just apply a 1 pixel inset on the rescaled geometry
                    Geometry copy = originalFootprint.copy();
                    copy.apply(geometryAT);
                    Geometry bounds = granuleBounds.copy();
                    bounds.apply(geometryAT);
                    rescaled = insetPolicy.applyInset(copy, bounds, 1.5);
                } else {
                    // use the original footprint
                    rescaled = insetFootprint.copy();
                    rescaled.apply(geometryAT);
                }
            } else {
                rescaled = originalFootprint.copy();
                rescaled.apply(geometryAT);
            }

            if (!rescaled.isEmpty()) {

                // the geometry is likely to have way more precision than needed, simplify it
                // so that the error is significantly less than one pixel
                Geometry simplified = TopologyPreservingSimplifier.simplify(rescaled, 0.333);
                // build a ROI geometry optimized for rectangle clipping
                roiGeometry = new FastClipROIGeometry(simplified);
                roiCache.put(at, roiGeometry);
            } else {
                return null;
            }
        }

        return roiGeometry;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Geometry getFootprint() {
        if (inset == 0) {
            return originalFootprint;
        } else {
            return insetFootprint;
        }
    }

    /**
     * A ROIGeometry leveraging {@link GeometryClipper} for fast clipping against rectangles
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class FastClipROIGeometry extends ROIGeometry {

        private static final long serialVersionUID = -4283288388988174306L;
        private static final AffineTransformation Y_INVERSION =
                new AffineTransformation(1, 0, 0, 0, -1, 0);

        public FastClipROIGeometry(Geometry geom) {
            super(geom);
        }

        @Override
        public ROI intersect(ROI roi) {
            final Geometry geom = getGeometry(roi);
            // is it a rectangle?
            if (geom != null && geom.equalsExact(geom.getEnvelope())) {
                GeometryClipper clipper = new GeometryClipper(geom.getEnvelopeInternal());
                Geometry intersect = clipper.clip(getAsGeometry(), true);
                return new ROIGeometry(intersect);

            } else {
                return super.intersect(roi);
            }
        }

        /**
         * Gets a {@link Geometry} from an input {@link ROI}.
         *
         * @param roi the ROI
         * @return a {@link Geometry} instance from the provided input; null in case the input roi
         *     is neither a geometry, nor a shape.
         */
        private Geometry getGeometry(ROI roi) {
            if (roi instanceof ROIGeometry) {
                return ((ROIGeometry) roi).getAsGeometry();
            } else if (roi instanceof ROIShape) {
                final Shape shape = ((ROIShape) roi).getAsShape();
                final Geometry geom = ShapeReader.read(shape, 0, new GeometryFactory());
                geom.apply(Y_INVERSION);
                return geom;
            }
            return null;
        }
    }
}
