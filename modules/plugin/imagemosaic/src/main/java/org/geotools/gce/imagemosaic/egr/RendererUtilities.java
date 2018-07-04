/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.egr;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.locationtech.jts.geom.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.datum.PixelInCell;

/**
 * This is a reduced copy of RenderUtilities found in the render module, to avoid adding a
 * dependency on it while using only a few methods
 */
public final class RendererUtilities {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(RendererUtilities.class.getName());

    /**
     * Helper class for building affine transforms. We use one instance per thread, in order to
     * avoid the need for {@code synchronized} statements.
     */
    private static final ThreadLocal<GridToEnvelopeMapper> gridToEnvelopeMappers =
            new ThreadLocal<GridToEnvelopeMapper>() {
                @Override
                protected GridToEnvelopeMapper initialValue() {
                    final GridToEnvelopeMapper mapper = new GridToEnvelopeMapper();
                    mapper.setPixelAnchor(PixelInCell.CELL_CORNER);
                    return mapper;
                }
            };

    /** Utilities classes should not be instantiated. */
    private RendererUtilities() {};

    /**
     * Sets up the affine transform
     *
     * <p>NOTE It is worth to note that here we do not take into account the half a pixel
     * translation stated by ogc for coverages bounds. One reason is that WMS 1.1.1 does not follow
     * it!!!
     *
     * @param mapExtent the map extent
     * @param paintArea the size of the rendering output area
     * @return a transform that maps from real world coordinates to the screen
     */
    public static AffineTransform worldToScreenTransform(
            ReferencedEnvelope mapExtent, Rectangle paintArea) {

        // //
        //
        // Convert the JTS envelope and get the transform
        //
        // //
        final Envelope2D genvelope = new Envelope2D(mapExtent);

        // //
        //
        // Get the transform
        //
        // //
        final GridToEnvelopeMapper m = (GridToEnvelopeMapper) gridToEnvelopeMappers.get();
        try {
            m.setGridRange(new GridEnvelope2D(paintArea));
            m.setEnvelope(genvelope);
            return m.createAffineTransform().createInverse();
        } catch (MismatchedDimensionException e) {
            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        } catch (NoninvertibleTransformException e) {
            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * Creates the map's bounding box in real world coordinates.
     *
     * @param worldToScreen a transform which converts World coordinates to screen pixel
     *     coordinates. No assumptions are done on axis order as this is assumed to be
     *     pre-calculated. The affine transform may specify an rotation, in case the envelope will
     *     encompass the complete (rotated) world polygon.
     * @param paintArea the size of the rendering output area
     * @return the envelope in world coordinates corresponding to the screen rectangle.
     */
    public static Envelope createMapEnvelope(Rectangle paintArea, AffineTransform worldToScreen)
            throws NoninvertibleTransformException {
        //
        // (X1,Y1) (X2,Y1)
        //
        // (X1,Y2) (X2,Y2)
        double[] pts = new double[8];
        pts[0] = paintArea.getMinX();
        pts[1] = paintArea.getMinY();
        pts[2] = paintArea.getMaxX();
        pts[3] = paintArea.getMinY();
        pts[4] = paintArea.getMaxX();
        pts[5] = paintArea.getMaxY();
        pts[6] = paintArea.getMinX();
        pts[7] = paintArea.getMaxY();
        worldToScreen.inverseTransform(pts, 0, pts, 0, 4);
        double xMin = Double.MAX_VALUE;
        double yMin = Double.MAX_VALUE;
        double xMax = -Double.MAX_VALUE;
        double yMax = -Double.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            xMin = Math.min(xMin, pts[2 * i]);
            yMin = Math.min(yMin, pts[2 * i + 1]);
            xMax = Math.max(xMax, pts[2 * i]);
            yMax = Math.max(yMax, pts[2 * i + 1]);
        }
        return new Envelope(xMin, xMax, yMin, yMax);
    }
}
