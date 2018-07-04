/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.data.DataSourceException;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * Class that supports readers in computing the proper reading resolution for a given grid geometry
 */
public class ReadResolutionCalculator {

    static final Logger LOGGER = Logging.getLogger(ReadResolutionCalculator.class);

    private ReferencedEnvelope requestedBBox;

    private Rectangle requestedRasterArea;

    private AffineTransform requestedGridToWorld;

    private double[] fullResolution;

    private boolean accurateResolution;

    private MathTransform destinationToSourceTransform;

    public ReadResolutionCalculator(
            GridGeometry2D requestedGridGeometry,
            CoordinateReferenceSystem nativeCrs,
            double[] fullResolution)
            throws FactoryException {
        Utilities.ensureNonNull("gridGeometry", requestedGridGeometry);
        this.requestedBBox =
                new ReferencedEnvelope((Envelope) requestedGridGeometry.getEnvelope2D());
        this.requestedRasterArea = requestedGridGeometry.getGridRange2D().getBounds();
        this.requestedGridToWorld = (AffineTransform) requestedGridGeometry.getGridToCRS2D();
        this.fullResolution = fullResolution;
        // the reader might not know (e.g., wms cascading reader) in this case we
        // pick the classic computation results, it's better than nothing
        if (fullResolution == null) {
            this.fullResolution = computeClassicResolution();
        }
        CoordinateReferenceSystem requestedCRS =
                requestedGridGeometry.getCoordinateReferenceSystem();
        if (!CRS.equalsIgnoreMetadata(nativeCrs, requestedCRS)) {
            this.destinationToSourceTransform = CRS.findMathTransform(requestedCRS, nativeCrs);
        }
    }

    /**
     * Computes the requested resolution which is going to be used for selecting overviews and or
     * deciding decimation factors on the target coverage.
     *
     * <p>In case the requested envelope is in the same {@link CoordinateReferenceSystem} of the
     * coverage we compute the resolution using the requested {@link MathTransform}. Notice that it
     * must be a {@link LinearTransform} or else we fail.
     *
     * <p>In case the requested envelope is not in the same {@link CoordinateReferenceSystem} of the
     * coverage we
     *
     * @throws DataSourceException in case something bad happens during reprojections and/or
     *     intersections.
     */
    public double[] computeRequestedResolution(ReferencedEnvelope readBounds) {
        try {

            // let's try to get the resolution from the requested gridToWorld
            if (requestedGridToWorld instanceof LinearTransform) {

                //
                // the crs of the request and the one of the coverage are NOT the
                // same and the conversion is not , we can get the resolution from envelope + raster
                // directly
                //
                if (destinationToSourceTransform != null
                        && !destinationToSourceTransform.isIdentity()) {
                    if (accurateResolution) {
                        return computeAccurateResolution(readBounds);
                    } else {
                        return computeClassicResolution();
                    }
                } else {
                    // the crs of the request and the one of the coverage are the
                    // same, we can get the resolution from the grid to world
                    return new double[] {
                        XAffineTransform.getScaleX0(requestedGridToWorld),
                        XAffineTransform.getScaleY0(requestedGridToWorld)
                    };
                }
            } else {
                // should not happen
                throw new UnsupportedOperationException(
                        Errors.format(
                                ErrorKeys.UNSUPPORTED_OPERATION_$1,
                                requestedGridToWorld.toString()));
            }
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, "Unable to compute requested resolution", e);
        }

        //
        // use the coverage resolution since we cannot compute the requested one
        //
        LOGGER.log(
                Level.WARNING,
                "Unable to compute requested resolution, the reader will pick the native one");
        return fullResolution;
    }

    /**
     * Classic way of computing the requested resolution
     *
     * @return
     */
    private double[] computeClassicResolution() {
        final GridToEnvelopeMapper geMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(requestedRasterArea), requestedBBox);
        final AffineTransform tempTransform = geMapper.createAffineTransform();

        return new double[] {
            XAffineTransform.getScaleX0(tempTransform), XAffineTransform.getScaleY0(tempTransform)
        };
    }

    /**
     * Compute the resolutions through a more accurate logic: Compute the resolution in 9 points,
     * the corners of the requested area and the middle points and take the better one. This will
     * provide better results for cases where there is a lot more deformation on a subregion
     * (top/bottom/sides) of the requested bbox with respect to others.
     *
     * @return
     * @throws TransformException
     * @throws NoninvertibleTransformException
     * @throws FactoryException
     */
    private double[] computeAccurateResolution(ReferencedEnvelope readBBox)
            throws TransformException, NoninvertibleTransformException, FactoryException {
        if (!CRS.equalsIgnoreMetadata(
                readBBox.getCoordinateReferenceSystem(),
                requestedBBox.getCoordinateReferenceSystem())) {
            readBBox = readBBox.transform(requestedBBox.getCoordinateReferenceSystem(), true);
        }
        double resX = XAffineTransform.getScaleX0(requestedGridToWorld);
        double resY = XAffineTransform.getScaleY0(requestedGridToWorld);
        GeneralEnvelope cropBboxTarget =
                CRS.transform(readBBox, requestedBBox.getCoordinateReferenceSystem());
        final int NPOINTS = 36;
        double[] points = new double[NPOINTS * 2];
        for (int i = 0; i < 3; i++) {
            double x;
            if (i == 0) {
                x = cropBboxTarget.getMinimum(0) + resX / 2;
            } else if (i == 1) {
                x = cropBboxTarget.getMedian(0);
            } else {
                x = cropBboxTarget.getMaximum(0) - resX / 2;
            }
            for (int j = 0; j < 3; j++) {
                double y;
                if (j == 0) {
                    y = cropBboxTarget.getMinimum(1) + resY / 2;
                } else if (j == 1) {
                    y = cropBboxTarget.getMedian(1);
                } else {
                    y = cropBboxTarget.getMaximum(1) - resY / 2;
                }

                int k = (i * 3 + j) * 8;
                points[k] = x - resX / 2;
                points[k + 1] = y;
                points[k + 2] = x + resX / 2;
                points[k + 3] = y;
                points[k + 4] = x;
                points[k + 5] = y - resY / 2;
                points[k + 6] = x;
                points[k + 7] = y + resY / 2;
            }
        }
        destinationToSourceTransform.transform(points, 0, points, 0, NPOINTS);

        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < points.length && minDistance > 0; i += 4) {
            double dx = points[i + 2] - points[i];
            double dy = points[i + 3] - points[i + 1];
            double d = Math.sqrt(dx * dx + dy * dy);
            if (d < minDistance) {
                minDistance = d;
            }
        }

        // reprojection can turn a segment into a zero lenght one, in that case, fall back on
        // the full resolution in that case
        double minDistanceX = Math.max(minDistance, fullResolution[0]);
        double minDistanceY = Math.max(minDistance, fullResolution[1]);
        return new double[] {minDistanceX, minDistanceY};
    }

    public boolean isAccurateResolution() {
        return accurateResolution;
    }

    public void setAccurateResolution(boolean accurateResolution) {
        this.accurateResolution = accurateResolution;
    }
}
