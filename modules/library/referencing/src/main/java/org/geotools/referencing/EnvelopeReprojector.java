/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing;

import static org.geotools.referencing.CRS.AxisOrder.NORTH_EAST;
import static org.geotools.referencing.CRS.findMathTransform;
import static org.geotools.referencing.CRS.getMapProjection;

import java.util.logging.Level;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.operation.projection.AzimuthalEquidistant;
import org.geotools.referencing.operation.projection.LambertAzimuthalEqualArea;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.referencing.operation.projection.PolarStereographic;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * Handles reprojection of envelopes based on a {@link CoordinateOperation}. Takes into account a
 * number of notable situations where the envelope reprojection is not a mere sampling of the
 * reprojected points, including poles, datelines, specific issues polar and azimuthal projections
 */
class EnvelopeReprojector {

    /**
     * Transforms an envelope using the given {@linkplain CoordinateOperation coordinate operation}.
     * The transformation is only approximative. It may be bigger than the smallest possible
     * bounding box, but should not be smaller. Note that the returned envelope may not have the
     * same number of dimensions than the original envelope.
     *
     * <p>This method can handle the case where the envelope contains the North or South pole, or
     * when it cross the &plusmn;180° longitude.
     *
     * @param operation The operation to use. Source and target dimension must be 2.
     * @param envelope Envelope to transform, or {@code null}. This envelope will not be modified.
     * @return The transformed envelope, or {@code null} if {@code envelope} was null.
     * @throws TransformException if a transform failed.
     * @since 2.4
     * @see CRS#transform(MathTransform, Envelope)
     */
    static GeneralEnvelope transform(final CoordinateOperation operation, final Envelope envelope)
            throws TransformException {
        if (envelope == null) {
            return null;
        }
        final CoordinateReferenceSystem sourceCRS = operation.getSourceCRS();
        if (sourceCRS != null) {
            final CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
            if (crs != null && !CRS.equalsIgnoreMetadata(crs, sourceCRS)) {
                throw new MismatchedReferenceSystemException(
                        Errors.format(ErrorKeys.MISMATCHED_COORDINATE_REFERENCE_SYSTEM));
            }
        }
        MathTransform mt = operation.getMathTransform();
        final GeneralDirectPosition centerPt = new GeneralDirectPosition(mt.getTargetDimensions());
        final GeneralEnvelope transformed = CRS.transform(mt, envelope, centerPt);

        expandOnAxisExtremCrossing(envelope, sourceCRS, mt, transformed);

        // check the target CRS
        final CoordinateReferenceSystem targetCRS = operation.getTargetCRS();
        if (targetCRS == null) {
            return transformed;
        }

        /*
         * Special case for polar stereographic, if the envelope contains the origin, then
         * the whole set of longitudes should be included
         */
        MapProjection sourceProjection = getMapProjection(sourceCRS);
        GeneralEnvelope generalEnvelope = toGeneralEnvelope(envelope);
        expandOnPolarOrigin(
                sourceCRS, mt, transformed, targetCRS, sourceProjection, generalEnvelope);
        MapProjection targetProjection = getMapProjection(targetCRS);
        if (targetProjection instanceof PolarStereographic && sourceCRS instanceof GeographicCRS) {
            expandOnPolarQuadrands(envelope, sourceCRS, mt, transformed, generalEnvelope);
        }

        /*
         * Now takes the target CRS in account...
         */
        transformed.setCoordinateReferenceSystem(targetCRS);
        final CoordinateSystem targetCS = targetCRS.getCoordinateSystem();
        if (targetCS == null) {
            // It should be an error, but we keep this method tolerant.
            return transformed;
        }
        GeneralEnvelope transformedSingularities =
                expandSingularityPoints(mt, centerPt, transformed, generalEnvelope, targetCS);
        if (transformedSingularities != null) return transformedSingularities;

        if (targetProjection != null) {
            // the points intersecting the rays emanating from the center of the projection in polar
            // stereographic and other projections is a maximum deformation point,
            // add those to the envelope too
            getProjectionCenterLonLat(targetCRS, centerPt);
            // now try to intesect the source envelope with the center point
            if (isPole(centerPt, DefaultGeographicCRS.WGS84)) {
                includePoles(
                        envelope, sourceCRS, centerPt, transformed, targetCRS, targetProjection);
            }

            // Azimuthal equidistant is weird, the extreme points in the output map are close
            // to the negated center latitude so in WGS84 they might be in the middle of the box,
            // whilst so far we are mostly considering the border
            if (targetProjection instanceof AzimuthalEquidistant.Abstract) {
                expandOnAntimeridian(envelope, sourceCRS, centerPt, transformed, targetCRS);
            }
        }

        return transformed;
    }

    private static void expandOnAntimeridian(
            Envelope envelope,
            CoordinateReferenceSystem sourceCRS,
            GeneralDirectPosition centerPt,
            GeneralEnvelope transformed,
            CoordinateReferenceSystem targetCRS) {
        getProjectionCenterLonLat(targetCRS, centerPt);
        try {
            MathTransform geoToTarget;
            Envelope geoEnvelope;
            if (sourceCRS instanceof GeographicCRS) {
                // this is a simplification to avoid dateline flips due to datum differences
                geoToTarget = findMathTransform(sourceCRS, targetCRS);
                geoEnvelope = envelope;
            } else {
                MathTransform mtWgs84 = findMathTransform(sourceCRS, DefaultGeographicCRS.WGS84);
                geoToTarget = findMathTransform(DefaultGeographicCRS.WGS84, targetCRS);
                geoEnvelope = CRS.transform(mtWgs84, envelope, null);
            }
            double nagativeMeridian = -centerPt.getOrdinate(1);

            if (geoEnvelope.getMinimum(1) <= nagativeMeridian
                    && geoEnvelope.getMaximum(1) >= nagativeMeridian) {
                expandOnMeridian(
                        transformed, geoToTarget, geoEnvelope, nagativeMeridian - 1e-6, 50);
                expandOnMeridian(
                        transformed, geoToTarget, geoEnvelope, nagativeMeridian + 1e-6, 50);
            }
        } catch (FactoryException | TransformException e) {
            CRS.LOGGER.log(
                    Level.FINE,
                    "Failed to transform from source to WGS84 to further enlarge the "
                            + "envelope on extreme points, proceeding without expansion",
                    e);
        }
    }

    private static void includePoles(
            Envelope envelope,
            CoordinateReferenceSystem sourceCRS,
            GeneralDirectPosition centerPt,
            GeneralEnvelope transformed,
            CoordinateReferenceSystem targetCRS,
            MapProjection targetProjection) {
        try {
            MathTransform geoToTarget;
            Envelope geoEnvelope;
            if (sourceCRS instanceof GeographicCRS) {
                // this is a simplification to avoid dateline flips due to datum differences
                geoToTarget = findMathTransform(sourceCRS, targetCRS);
                geoEnvelope = envelope;
            } else {
                MathTransform mtWgs84 = findMathTransform(sourceCRS, DefaultGeographicCRS.WGS84);
                geoToTarget = findMathTransform(DefaultGeographicCRS.WGS84, targetCRS);
                geoEnvelope = CRS.transform(mtWgs84, envelope, null);
            }
            expandEnvelopeOnExtremePoints(centerPt, transformed, geoToTarget, geoEnvelope);
            if (targetProjection instanceof PolarStereographic
                    || targetProjection instanceof LambertAzimuthalEqualArea) {
                // sample quadrant points too
                centerPt.setOrdinate(0, rollLongitude(centerPt.getOrdinate(0) - 90));
                expandEnvelopeOnExtremePoints(centerPt, transformed, geoToTarget, geoEnvelope);
                centerPt.setOrdinate(0, rollLongitude(centerPt.getOrdinate(0) - 90));
                expandEnvelopeOnExtremePoints(centerPt, transformed, geoToTarget, geoEnvelope);
                centerPt.setOrdinate(0, rollLongitude(centerPt.getOrdinate(0) - 90));
                expandEnvelopeOnExtremePoints(centerPt, transformed, geoToTarget, geoEnvelope);
            }
        } catch (FactoryException | TransformException e) {
            CRS.LOGGER.log(
                    Level.FINE,
                    "Failed to transform from source to WGS84 to further enlarge the "
                            + "envelope on extreme points, proceeding without expansion",
                    e);
        }
    }

    private static GeneralEnvelope expandSingularityPoints(
            MathTransform mt,
            GeneralDirectPosition centerPt,
            GeneralEnvelope transformed,
            GeneralEnvelope generalEnvelope,
            CoordinateSystem targetCS) {
        /*
         * Checks for singularity points. For example the south pole is a singularity point in
         * geographic CRS because we reach the maximal value allowed on one particular geographic
         * axis, namely latitude. This point is not a singularity in the stereographic projection,
         * where axis extends toward infinity in all directions (mathematically) and south pole
         * has nothing special apart being the origin (0,0).
         *
         * Algorithm:
         *
         * 1) Inspect the target axis, looking if there is any bounds. If bounds are found, get
         *    the coordinates of singularity points and project them from target to source CRS.
         *
         *    Example: if the transformed envelope above is (80°S to 85°S, 10°W to 50°W), and if
         *             target axis inspection reveal us that the latitude in target CRS is bounded
         *             at 90°S, then project (90°S,30°W) to source CRS. Note that the longitude is
         *             set to the the center of the envelope longitude range (more on this later).
         *
         * 2) If the singularity point computed above is inside the source envelope, add that
         *    point to the target (transformed) envelope.
         *
         * Note: We could choose to project the (-180, -90), (180, -90), (-180, 90), (180, 90)
         * points, or the (-180, centerY), (180, centerY), (centerX, -90), (centerX, 90) points
         * where (centerX, centerY) are transformed from the source envelope center. It make
         * no difference for polar projections because the longitude is irrelevant at pole, but
         * may make a difference for the 180° longitude bounds.  Consider a Mercator projection
         * where the transformed envelope is between 20°N and 40°N. If we try to project (-180,90),
         * we will get a TransformException because the Mercator projection is not supported at
         * pole. If we try to project (-180, 30) instead, we will get a valid point. If this point
         * is inside the source envelope because the later overlaps the 180° longitude, then the
         * transformed envelope will be expanded to the full (-180 to 180) range. This is quite
         * large, but at least it is correct (while the envelope without expansion is not).
         */
        DirectPosition sourcePt = null;
        DirectPosition targetPt = null;
        final int dimension = targetCS.getDimension();
        for (int i = 0; i < dimension; i++) {
            final CoordinateSystemAxis axis = targetCS.getAxis(i);
            if (axis == null) { // Should never be null, but check as a paranoiac safety.
                continue;
            }
            boolean testMax = false; // Tells if we are testing the minimal or maximal value.
            do {
                final double extremum = testMax ? axis.getMaximumValue() : axis.getMinimumValue();
                if (Double.isInfinite(extremum) || Double.isNaN(extremum)) {
                    /*
                     * The axis is unbounded. It should always be the case when the target CRS is
                     * a map projection, in which case this loop will finish soon and this method
                     * will do nothing more (no object instantiated, no MathTransform inversed...)
                     */
                    continue;
                }
                if (targetPt == null) {
                    try {
                        mt = mt.inverse();
                    } catch (NoninvertibleTransformException exception) {
                        /*
                         * If the transform is non invertible, this method can't do anything. This
                         * is not a fatal error because the envelope has already be transformed by
                         * the caller. We lost the check for singularity points performed by this
                         * method, but it make no difference in the common case where the source
                         * envelope didn't contains any of those points.
                         *
                         * Note that this exception is normal if target dimension is smaller than
                         * source dimension, since the math transform can not reconstituate the
                         * lost dimensions. So we don't log any warning in this case.
                         */
                        if (dimension >= mt.getSourceDimensions()) {
                            CRS.unexpectedException("transform", exception);
                        }
                        return transformed;
                    }
                    targetPt = new GeneralDirectPosition(mt.getSourceDimensions());
                    for (int j = 0; j < dimension; j++) {
                        targetPt.setOrdinate(j, centerPt.getOrdinate(j));
                    }
                }
                targetPt.setOrdinate(i, extremum);
                try {
                    sourcePt = mt.transform(targetPt, sourcePt);
                } catch (Exception e) {
                    /*
                     * This exception may be normal. For example we are sure to get this exception
                     * when trying to project the latitude extremums with a cylindrical Mercator
                     * projection. Do not log any message and try the other points.
                     */
                    continue;
                }
                if (generalEnvelope.contains(sourcePt)) {
                    transformed.add(targetPt);
                }
            } while ((testMax = !testMax) == true);
            if (targetPt != null) {
                targetPt.setOrdinate(i, centerPt.getOrdinate(i));
            }
        }
        return null;
    }

    private static void expandOnPolarQuadrands(
            Envelope envelope,
            CoordinateReferenceSystem sourceCRS,
            MathTransform mt,
            GeneralEnvelope transformed,
            GeneralEnvelope generalEnvelope)
            throws TransformException {
        final CoordinateSystem sourceCS = sourceCRS.getCoordinateSystem();
        for (int i = 0; i < sourceCS.getDimension(); i++) {
            final CoordinateSystemAxis axis = sourceCS.getAxis(i);
            if (CRS.equalsIgnoreMetadata(DefaultCoordinateSystemAxis.LONGITUDE, axis)) {
                double minLon = envelope.getMinimum(i);
                double maxLon = envelope.getMaximum(i);
                DirectPosition lower = generalEnvelope.getLowerCorner();
                DirectPosition upper = generalEnvelope.getUpperCorner();
                DirectPosition dest = new DirectPosition2D();
                // world spanning longitude? add points around the globe quadrants then
                if ((maxLon - minLon) >= 360) {
                    for (int lon = -180; lon <= 180; lon += 90) {
                        addLowerUpperPoints(mt, transformed, i, lower, upper, dest, lon);
                    }
                } else {
                    // quadrants are still extreme points of the reprojected "circle",
                    // add them if the envelope happens to cross them, or if the envelope
                    // is "world spanning"
                    for (int lon = -180; lon <= 180; lon += 90) {
                        if (minLon < lon && maxLon > lon) {
                            addLowerUpperPoints(mt, transformed, i, lower, upper, dest, lon);
                        }
                    }
                }
            }
        }
    }

    private static void expandOnPolarOrigin(
            CoordinateReferenceSystem sourceCRS,
            MathTransform mt,
            GeneralEnvelope transformed,
            CoordinateReferenceSystem targetCRS,
            MapProjection sourceProjection,
            GeneralEnvelope generalEnvelope)
            throws TransformException {
        if (sourceProjection instanceof PolarStereographic
                || (sourceProjection instanceof LambertAzimuthalEqualArea)) {
            ParameterValue<?> fe =
                    sourceProjection
                            .getParameterValues()
                            .parameter(
                                    MapProjection.AbstractProvider.FALSE_EASTING
                                            .getName()
                                            .getCode());
            double originX = fe.doubleValue();
            ParameterValue<?> fn =
                    sourceProjection
                            .getParameterValues()
                            .parameter(
                                    MapProjection.AbstractProvider.FALSE_NORTHING
                                            .getName()
                                            .getCode());
            double originY = fn.doubleValue();
            DirectPosition2D origin = new DirectPosition2D(originX, originY);
            if (isPole(origin, sourceCRS)) {
                if (generalEnvelope.contains(origin)) {
                    if (targetCRS instanceof GeographicCRS) {
                        DirectPosition lowerCorner = transformed.getLowerCorner();
                        if (CRS.getAxisOrder(targetCRS) == AxisOrder.NORTH_EAST) {
                            lowerCorner.setOrdinate(1, -180);
                            transformed.add(lowerCorner);
                            lowerCorner.setOrdinate(1, 180);
                            transformed.add(lowerCorner);
                        } else {
                            lowerCorner.setOrdinate(0, -180);
                            transformed.add(lowerCorner);
                            lowerCorner.setOrdinate(0, 180);
                            transformed.add(lowerCorner);
                        }
                    } else {
                        // there is no guarantee that the whole range of longitudes will make
                        // sense for the target projection. We do a 1deg sampling as a compromise
                        // between
                        // speed and accuracy
                        DirectPosition lc = transformed.getLowerCorner();
                        DirectPosition uc = transformed.getUpperCorner();
                        for (int j = -180; j < 180; j++) {
                            expandEnvelopeByLongitude(j, lc, transformed, targetCRS);
                            expandEnvelopeByLongitude(j, uc, transformed, targetCRS);
                        }
                    }
                } else {
                    // check where the point closes to the origin is, make sure it's included
                    // in the tranformation points
                    if (generalEnvelope.getMinimum(0) < originX
                            && generalEnvelope.getMaximum(0) > originX) {
                        DirectPosition lc = generalEnvelope.getLowerCorner();
                        lc.setOrdinate(0, originX);
                        mt.transform(lc, lc);
                        transformed.add(lc);
                        DirectPosition uc = generalEnvelope.getUpperCorner();
                        uc.setOrdinate(0, originX);
                        mt.transform(uc, uc);
                        transformed.add(uc);
                    }
                    if (generalEnvelope.getMinimum(1) < originY
                            && generalEnvelope.getMaximum(1) > originY) {
                        DirectPosition lc = generalEnvelope.getLowerCorner();
                        lc.setOrdinate(1, originY);
                        mt.transform(lc, lc);
                        transformed.add(lc);
                        DirectPosition uc = generalEnvelope.getUpperCorner();
                        uc.setOrdinate(1, originY);
                        mt.transform(uc, uc);
                        transformed.add(uc);
                    }
                }
            }
        }
    }

    /**
     * If the source envelope crosses the expected range of valid coordinates, also projects the
     * range bounds as a safety. Example: if the source envelope goes from 150 to 200°E, some map
     * projections will interpret 200° as if it was -160°, and consequently produce an envelope
     * which do not include the 180°W extremum. We will add those extremum points explicitly as a
     * safety. It may leads to bigger than necessary target envelope, but the contract is to include
     * at least the source envelope, not to returns the smallest one.
     */
    private static void expandOnAxisExtremCrossing(
            Envelope envelope,
            CoordinateReferenceSystem sourceCRS,
            MathTransform mt,
            GeneralEnvelope transformed)
            throws TransformException {
        if (sourceCRS != null) {
            final CoordinateSystem cs = sourceCRS.getCoordinateSystem();
            if (cs != null) { // Should never be null, but check as a paranoiac safety.
                DirectPosition sourcePt = null;
                DirectPosition targetPt = null;
                final int dimension = cs.getDimension();
                for (int i = 0; i < dimension; i++) {
                    final CoordinateSystemAxis axis = cs.getAxis(i);
                    if (axis == null) { // Should never be null, but check as a paranoiac safety.
                        continue;
                    }
                    final double min = envelope.getMinimum(i);
                    final double max = envelope.getMaximum(i);
                    final double v1 = axis.getMinimumValue();
                    final double v2 = axis.getMaximumValue();
                    final boolean b1 = (v1 > min && v1 < max);
                    final boolean b2 = (v2 > min && v2 < max);
                    if (!b1 && !b2) {
                        continue;
                    }
                    if (sourcePt == null) {
                        sourcePt = new GeneralDirectPosition(dimension);
                        for (int j = 0; j < dimension; j++) {
                            sourcePt.setOrdinate(j, envelope.getMedian(j));
                        }
                    }
                    if (b1) {
                        sourcePt.setOrdinate(i, v1);
                        transformed.add(targetPt = mt.transform(sourcePt, targetPt));
                    }
                    if (b2) {
                        sourcePt.setOrdinate(i, v2);
                        transformed.add(targetPt = mt.transform(sourcePt, targetPt));
                    }
                    sourcePt.setOrdinate(i, envelope.getMedian(i));
                }
            }
        }
    }

    private static void expandOnMeridian(
            GeneralEnvelope target,
            MathTransform geoToTarget,
            Envelope geoEnvelope,
            double antimeridian,
            int numPoints)
            throws TransformException {
        double minLon = geoEnvelope.getMinimum(0);
        double maxLon = geoEnvelope.getMaximum(0);
        double[] points = new double[numPoints * 2];
        double lon = minLon;
        double delta = (maxLon - minLon) / (numPoints - 1); // (n points, n-1 intervals)
        for (int i = 0; i < points.length; ) {
            points[i++] = lon;
            points[i++] = antimeridian;
            lon = minLon + delta * i / 2;
        }
        geoToTarget.transform(points, 0, points, 0, numPoints);
        DirectPosition dp = new DirectPosition2D();
        for (int i = 0; i < points.length; ) {
            dp.setOrdinate(0, points[i++]);
            dp.setOrdinate(1, points[i++]);
            target.add(dp);
        }
    }

    private static void addLowerUpperPoints(
            MathTransform mt,
            GeneralEnvelope transformed,
            int axis,
            DirectPosition lower,
            DirectPosition upper,
            DirectPosition dest,
            double ordinate)
            throws TransformException {
        lower.setOrdinate(axis, ordinate);
        mt.transform(lower, dest);
        transformed.add(dest);
        upper.setOrdinate(axis, ordinate);
        mt.transform(upper, dest);
        transformed.add(dest);
    }

    private static GeneralEnvelope toGeneralEnvelope(final Envelope envelope) {
        // TODO: avoid the hack below if we provide a contains(DirectPosition)
        // method in GeoAPI Envelope interface.
        GeneralEnvelope generalEnvelope;
        if (envelope instanceof GeneralEnvelope) {
            generalEnvelope = (GeneralEnvelope) envelope;
        } else {
            generalEnvelope = new GeneralEnvelope(envelope);
        }
        return generalEnvelope;
    }

    private static boolean isPole(DirectPosition point, CoordinateReferenceSystem crs) {
        DirectPosition result = new DirectPosition2D();
        GeographicCRS geographic;
        try {
            ProjectedCRS projectedCRS = CRS.getProjectedCRS(crs);
            if (projectedCRS != null) {
                geographic = projectedCRS.getBaseCRS();
                MathTransform mt = findMathTransform(projectedCRS, geographic);
                mt.transform(point, result);
            } else if (crs instanceof GeographicCRS) {
                result = point;
                geographic = (GeographicCRS) crs;
            } else {
                return false;
            }
        } catch (MismatchedDimensionException | TransformException | FactoryException e) {
            return false;
        }

        final double EPS = 1e-6;
        if (CRS.getAxisOrder(geographic) == NORTH_EAST) {
            return Math.abs(result.getOrdinate(0) - 90) < EPS
                    || Math.abs(result.getOrdinate(0) + 90) < EPS;
        } else {
            return Math.abs(result.getOrdinate(1) - 90) < EPS
                    || Math.abs(result.getOrdinate(1) + 90) < EPS;
        }
    }

    private static void expandEnvelopeByLongitude(
            double longitude,
            DirectPosition input,
            GeneralEnvelope transformed,
            CoordinateReferenceSystem sourceCRS) {
        try {
            MathTransform mt = findMathTransform(sourceCRS, DefaultGeographicCRS.WGS84);
            DirectPosition2D pos = new DirectPosition2D(sourceCRS);
            mt.transform(input, pos);
            pos.setOrdinate(0, longitude);
            mt.inverse().transform(pos, pos);
            transformed.add(pos);
        } catch (Exception e) {
            CRS.LOGGER.log(
                    Level.FINER,
                    "Tried to expand target envelope to include longitude "
                            + longitude
                            + " but failed. This is not necesseraly and issue, "
                            + "this is a best effort attempt to handle the polar stereographic "
                            + "pole singularity during reprojection",
                    e);
        }
    }

    private static GeneralDirectPosition getProjectionCenterLonLat(
            CoordinateReferenceSystem crs, GeneralDirectPosition centerPt) {
        // set defaults
        centerPt.setOrdinate(0, 0);
        centerPt.setOrdinate(1, 0);

        MapProjection projection = getMapProjection(crs);
        if (projection == null) {
            return centerPt;
        }

        for (GeneralParameterValue gpv : projection.getParameterValues().values()) {
            // for safety
            if (!(gpv instanceof ParameterValue)) {
                continue;
            }
            ParameterValue pv = (ParameterValue) gpv;
            ReferenceIdentifier pvName = pv.getDescriptor().getName();
            if (MapProjection.AbstractProvider.LATITUDE_OF_ORIGIN.getName().equals(pvName)) {
                centerPt.setOrdinate(1, pv.doubleValue());
            } else if (MapProjection.AbstractProvider.LATITUDE_OF_CENTRE.getName().equals(pvName)) {
                centerPt.setOrdinate(1, pv.doubleValue());
            } else if (MapProjection.AbstractProvider.LONGITUDE_OF_CENTRE
                    .getName()
                    .equals(pvName)) {
                centerPt.setOrdinate(0, pv.doubleValue());
            } else if (MapProjection.AbstractProvider.CENTRAL_MERIDIAN.getName().equals(pvName)) {
                centerPt.setOrdinate(0, pv.doubleValue());
            }
        }

        return centerPt;
    }

    private static void expandEnvelopeOnExtremePoints(
            GeneralDirectPosition centerPt,
            GeneralEnvelope transformed,
            MathTransform geoToTarget,
            Envelope geoEnvelope)
            throws TransformException {
        GeneralDirectPosition workPoint = new GeneralDirectPosition(centerPt.getDimension());
        double centerLon = centerPt.getOrdinate(0);
        double minLon = geoEnvelope.getMinimum(0);
        double maxLon = geoEnvelope.getMaximum(0);
        double minLat = geoEnvelope.getMinimum(1);
        double maxLat = geoEnvelope.getMaximum(1);
        if (minLon <= centerLon && centerLon <= maxLon) {
            // intersection at boundaries, south
            includeTransformedPoint(transformed, geoToTarget, workPoint, centerLon, minLat);
            // intersection at boundaries, north
            includeTransformedPoint(transformed, geoToTarget, workPoint, centerLon, maxLat);
        }
        double centerLat = centerPt.getOrdinate(1);
        if (minLat <= centerLat && centerLat <= maxLat) {
            // intersection at boundaries, west
            includeTransformedPoint(transformed, geoToTarget, workPoint, minLon, centerLat);
            // intersection at boundaries, east
            includeTransformedPoint(transformed, geoToTarget, workPoint, maxLon, centerLat);
        }
    }

    private static void includeTransformedPoint(
            GeneralEnvelope envelope,
            MathTransform mt,
            GeneralDirectPosition workPoint,
            double x,
            double y)
            throws TransformException {
        workPoint.setOrdinate(0, x);
        workPoint.setOrdinate(1, y);
        mt.transform(workPoint, workPoint);
        envelope.add(workPoint);
    }

    private static double rollLongitude(final double x) {
        double rolled = x - (((int) (x + Math.signum(x) * 180)) / 360) * 360.0;
        return rolled;
    }
}
