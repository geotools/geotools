/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.crs;

import static org.geotools.referencing.operation.projection.GeostationarySatellite.Provider.SATELLITE_HEIGHT;
import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.CENTRAL_MERIDIAN;

import java.awt.geom.Point2D;
import java.util.regex.Pattern;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.api.referencing.datum.Ellipsoid;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.util.SoftValueHashMap;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;

/**
 * Computes and caches the GEOS (geostationary) valid area geometry.
 *
 * <p>Compute is expensive; cache is SoftValueHashMap-backed (thread-safe) so values may be reclaimed under memory
 * pressure.
 *
 * <p>The valid-area is computed *from the GEOS CRS itself* by probing the exact GeoTools forward transform domain
 * (works for the Ellipsoidal implementation too).
 */
final class GeosValidAreaCache {

    private static final int AZIMUTH_SAMPLES = 360;
    private static final int BISECTION_ITERS = 25;

    // Step slightly inside the last transformable to avoid numeric edge failures
    private static final double INWARD_FACTOR = 0.9999;

    private static final GeometryFactory GF = new GeometryFactory();

    // World envelopes for dateline split/translate
    private static final Geometry WORLD = GF.toGeometry(new Envelope(-180, 180, -90, 90));
    private static final Geometry WORLD_LEFT = GF.toGeometry(new Envelope(-360, -180, -90, 90));
    private static final Geometry WORLD_RIGHT = GF.toGeometry(new Envelope(180, 360, -90, 90));

    /** CRS signature -> valid area already. */
    private final SoftValueHashMap<CrsKey, Geometry> validAreaCache = new SoftValueHashMap<>();

    /**
     * Returns the valid area geometry for GEOS projected CRS in WGS84.
     *
     * @param geosCrs the GEOS projected CRS for which to compute the valid area
     * @return a Geometry in WGS84 representing the valid area of the GEOS CRS
     */
    public Geometry getValidAreaInGeosCrs(CoordinateReferenceSystem geosCrs)
            throws FactoryException, TransformException {
        CrsKey key = CrsKey.of(geosCrs);
        Geometry cached = validAreaCache.get(key);
        if (cached != null) return cached;

        synchronized (validAreaCache) {
            cached = validAreaCache.get(key);
            if (cached != null) return cached;

            Geometry wgs84 = buildValidAreaWgs84(geosCrs);
            validAreaCache.put(key, wgs84);
            return wgs84;
        }
    }

    /**
     * Builds the valid area geometry in WGS84 by probing the forward transform domain of the GEOS CRS.
     *
     * @param geostationary the GEOS CRS to probe and extract parameters from
     * @return a Geometry in WGS84 representing the valid area of the GEOS CRS
     * @throws FactoryException if there is an error accessing the CRS parameters or creating the math transform
     */
    private static Geometry buildValidAreaWgs84(CoordinateReferenceSystem geostationary) throws FactoryException {
        // Extract what we need from the CRS itself
        if (!(geostationary instanceof ProjectedCRS)) {
            throw new IllegalArgumentException("Expected a ProjectedCRS, got: " + geostationary.getClass());
        }

        // Parameters defined by GeoTools geostationary projection:
        // central_meridian, satellite_height, sweep_axis, semi_major, semi_minor, etc.
        MapProjection mapProjection = CRS.getMapProjection(geostationary);
        ParameterValueGroup params = mapProjection.getParameterValues();
        double centralMeridian =
                params.parameter(CENTRAL_MERIDIAN.getName().getCode()).doubleValue();
        double satelliteHeight =
                params.parameter(SATELLITE_HEIGHT.getName().getCode()).doubleValue();

        // Compute a conservative "angular radius" to compute the max visible central angle, and compute the valid area
        Ellipsoid ellipsoid = CRS.getEllipsoid(geostationary);
        double angle = computeAngularRadiusDeg(ellipsoid, satelliteHeight);
        return buildValidAreaWgs84(geostationary, centralMeridian, angle, ellipsoid, AZIMUTH_SAMPLES, BISECTION_ITERS);
    }

    /**
     * Angular radius of the visible disk, as a bound for probing.
     *
     * <p>We just need an upper bound for binary search; correctness comes from probing. For geostationary height H
     * above surface and Earth radius R: central angle to limb ~ acos(R/(R+H)).
     *
     * <p>For ellipsoid, use semi-major axis as a conservative bound.
     */
    private static double computeAngularRadiusDeg(Ellipsoid ellipsoid, double satelliteHeightMeters) {
        double r = ellipsoid.getSemiMajorAxis();
        double h = satelliteHeightMeters;
        double cos = r / (r + h);
        // numeric safety
        cos = Math.max(-1.0, Math.min(1.0, cos));
        double angleRad = Math.acos(cos);
        return Math.toDegrees(angleRad);
    }

    private static Geometry buildValidAreaWgs84(
            CoordinateReferenceSystem geostationary,
            double centralMeridian,
            double angle,
            Ellipsoid ellipsoid,
            int azimuthSamples,
            int bisectionIters)
            throws FactoryException {

        MathTransform fwd = CRS.findMathTransform(DefaultGeographicCRS.WGS84, geostationary, true);
        GeodeticCalculator gc = new GeodeticCalculator(ellipsoid);

        // Upper bound distance along surface for the search (bound only)
        double dMaxMeters = Math.toRadians(angle) * ellipsoid.getSemiMajorAxis();

        Coordinate[] coords = new Coordinate[azimuthSamples + 1];
        double step = 360.0 / azimuthSamples;
        for (int i = 0; i < azimuthSamples; i++) {
            double az = i * step;

            double lo = 0.0;
            double hi = dMaxMeters;

            for (int k = 0; k < bisectionIters; k++) {
                double d = (lo + hi) * 0.5;
                Point2D ll = destination(gc, centralMeridian, az, d);

                if (isForwardTransformOk(fwd, ll.getX(), ll.getY())) {
                    lo = d;
                } else {
                    hi = d;
                }
            }

            double dSafe = lo * INWARD_FACTOR;
            Point2D p = destination(gc, centralMeridian, az, dSafe);

            double lon = p.getX();
            double lat = p.getY();

            coords[i] = new Coordinate(lon, lat);
        }

        // close the ring
        coords[azimuthSamples] = new Coordinate(coords[0]);

        // return the result
        Polygon polygon = GF.createPolygon(GF.createLinearRing(coords));
        return normalizeDateline(polygon);
    }

    /** Compute the destination point from a start point, azimuth and distance using the geodetic calculator. */
    private static Point2D destination(GeodeticCalculator gc, double lon0, double azDeg, double distMeters) {
        gc.setStartingGeographicPoint(lon0, 0.0);
        gc.setDirection(azDeg, distMeters);
        return gc.getDestinationGeographicPoint();
    }

    /** Check if the forward transform can be applied to the given lon/lat without throwing */
    private static boolean isForwardTransformOk(MathTransform fwd, double lon, double lat) {
        try {
            double[] src = {lon, lat};
            double[] dst = new double[2];
            fwd.transform(src, 0, dst, 0, 1);
            return Double.isFinite(dst[0]) && Double.isFinite(dst[1]);
        } catch (Exception e) {
            return false;
        }
    }

    private static Geometry normalizeDateline(Geometry poly) {
        Envelope env = poly.getEnvelopeInternal();

        // if no dateline crossing expected, return as is
        if (env.getWidth() < 180) {
            return poly;
        }

        // update coordinates to be in the -180,180 range for easier intersection; we will translate back the pieces
        // later
        WrappingCoordinateFilter filter = new WrappingCoordinateFilter(180, 360, null, false, true);
        poly.apply(filter);
        poly.geometryChanged();

        // get the envelope again, as the polygon got updated
        env = poly.getEnvelopeInternal();

        if (env.getMinX() < -180) {
            Geometry left = poly.intersection(WORLD_LEFT);
            Geometry leftTranslated =
                    AffineTransformation.translationInstance(360, 0).transform(left);
            Geometry right = poly.intersection(WORLD);
            return right.union(leftTranslated);
        } else if (env.getMaxX() > 180) {
            Geometry left = poly.intersection(WORLD);
            Geometry right = poly.intersection(WORLD_RIGHT);
            Geometry rightTranslated =
                    AffineTransformation.translationInstance(-360, 0).transform(right);
            return rightTranslated.union(left);
        } else {
            return poly;
        }
    }

    /** CRS key based on normalized WKT, with pre-computed hash for faster lookups. */
    private static final class CrsKey {
        private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

        private final String signature;
        private final int hash;

        private CrsKey(String signature) {
            this.signature = signature;
            this.hash = signature.hashCode();
        }

        static CrsKey of(CoordinateReferenceSystem crs) {
            String wkt = crs.toWKT().replaceAll("\\s+", " ").trim();
            String canonical = WHITESPACE_PATTERN.matcher(wkt).replaceAll(" ").trim();
            return new CrsKey(canonical);
        }

        @Override
        public boolean equals(Object o) {
            return (this == o) || (o instanceof CrsKey && signature.equals(((CrsKey) o).signature));
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }
}
