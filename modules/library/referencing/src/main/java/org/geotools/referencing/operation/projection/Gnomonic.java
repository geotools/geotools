/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.referencing.operation.projection;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.awt.geom.Point2D;
import java.util.Collection;
import org.geotools.api.parameter.GeneralParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import si.uom.NonSI;

/**
 * The gnomonic projection using a spheroid algorithm.
 *
 * @since 12.3
 * @version $Id$
 * @author Simon Schafer
 */
public final class Gnomonic extends MapProjection {
    /** For cross-version compatibility. */
    private static final long serialVersionUID = -1334127158883911268L;

    /** Maximum difference allowed when comparing real numbers. */
    private static final double EPSILON = 1E-6;

    private final double sinPhi0, cosPhi0;
    private final double latitudeOfCentre;
    private final double primeVert0;
    private final double projectedCylindricalZ0;

    /**
     * Constructs a gnomonic projection using a spheroid algorithm.
     *
     * @param parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    Gnomonic(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);
        final Collection<GeneralParameterDescriptor> expected =
                getParameterDescriptors().descriptors();
        latitudeOfCentre = doubleValue(expected, Provider.LATITUDE_OF_CENTRE, parameters);
        centralMeridian = doubleValue(expected, Provider.LONGITUDE_OF_CENTRE, parameters);
        ensureLatitudeInRange(Provider.LATITUDE_OF_CENTRE, latitudeOfCentre, true);
        ensureLongitudeInRange(Provider.LONGITUDE_OF_CENTRE, centralMeridian, true);

        sinPhi0 = sin(latitudeOfCentre);
        cosPhi0 = cos(latitudeOfCentre);
        primeVert0 = 1 / sqrt(1.0 - excentricitySquared * sinPhi0 * sinPhi0);
        projectedCylindricalZ0 = primeVert0 * sinPhi0;
    }

    /** {@inheritDoc} */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /** {@inheritDoc} */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        final Collection<GeneralParameterDescriptor> expected =
                getParameterDescriptors().descriptors();
        set(expected, Provider.LATITUDE_OF_CENTRE, values, latitudeOfCentre);
        set(expected, Provider.LONGITUDE_OF_CENTRE, values, centralMeridian);
        return values;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates (units in radians) and stores the
     * result in {@code ptDst} (linear distance on a unit sphere).
     */
    @Override
    protected Point2D transformNormalized(double lambda, double phi, Point2D ptDst) throws ProjectionException {
        final double sinPhi = sin(phi);
        final double cosPhi = cos(phi);
        final double sinLam = sin(lambda);
        final double cosLam = cos(lambda);

        final double primeVert = 1 / sqrt(1.0 - excentricitySquared * sinPhi * sinPhi);
        final double projected_cylindrical_z = primeVert * sinPhi;
        final double delta_projected_cylindrical_z =
                excentricitySquared * (projected_cylindrical_z - projectedCylindricalZ0);

        final double z_factor = cosPhi0 * cosPhi * cosLam + sinPhi0 * sinPhi;

        if (z_factor <= EPSILON) {
            throw new ProjectionException(ErrorKeys.POINT_OUTSIDE_HEMISPHERE);
        }

        final double height = (primeVert0 + delta_projected_cylindrical_z * sinPhi0) / z_factor;

        final double x = height * cosPhi * sinLam;

        final double y =
                height * (cosPhi0 * sinPhi - sinPhi0 * cosPhi * cosLam) - delta_projected_cylindrical_z * cosPhi0;

        if (ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        }
        return new Point2D.Double(x, y);
    }

    /** Transforms the specified (<var>x</var>,<var>y</var>) coordinates and stores the result in {@code ptDst}. */
    @Override
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst) {
        final double normalisedCylindricalZ = sinPhi0 * (primeVert0 * (1.0 - excentricitySquared)) + cosPhi0 * y;

        final double primeVerticalCylindricalRadius = cosPhi0 * primeVert0 - sinPhi0 * y;

        final double lambda = atan2(x, primeVerticalCylindricalRadius);

        final double normalisedCylindricalRadius = hypot(x, primeVerticalCylindricalRadius);

        final double phi = getLatitudeFromPolar(normalisedCylindricalRadius, normalisedCylindricalZ);

        if (ptDst != null) {
            ptDst.setLocation(lambda, phi);
            return ptDst;
        }
        return new Point2D.Double(lambda, phi);
    }

    /** calculates, iteratively, a latitude from cylindrical polar coordinates based at the centre of the spheroid. */
    private double getLatitudeFromPolar(double normalisedCylindricalRadius, double normalisedZ) {
        final double eccentricityRatio = excentricitySquared / sqrt(1.0 - excentricitySquared);
        final double modifiedRadiusSq =
                (normalisedCylindricalRadius * normalisedCylindricalRadius) / (1.0 - excentricitySquared);

        double zExtension = 1.0;
        double estimate = 0.0;
        while (abs(estimate - zExtension) > EPSILON) {
            zExtension = estimate;
            final double producedZ = normalisedZ + zExtension;

            estimate = eccentricityRatio * producedZ / sqrt(modifiedRadiusSq + producedZ * producedZ);
        }

        final double latitude;
        if (abs(normalisedCylindricalRadius) <= EPSILON) {
            // need to check whether at north or south pole
            if ((normalisedZ + estimate) > 0.0) { // north
                latitude = PI / 2;
            } else { // south
                latitude = -PI / 2;
            }
        } else {
            latitude = atan((normalisedZ + estimate) / normalisedCylindricalRadius);
        }
        return latitude;
    }

    /** Compares the specified object with this map projection for equality. */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final Gnomonic that = (Gnomonic) object;
            return equals(this.latitudeOfCentre, that.latitudeOfCentre);
        }
        return false;
    }

    /** Returns a hash value for this map projection. */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(latitudeOfCentre);
        return ((int) code ^ (int) (code >>> 32)) + 37 * super.hashCode();
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                                 PROVIDERS                                ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform provider} for a
     * {@linkplain Gnomonic} projection
     *
     * @since 2.4
     * @version $Id$
     * @author Simon Schafer
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /** For cross-version compatibility. */
        private static final long serialVersionUID = 7216851295693867026L;

        /**
         * The operation parameter descriptor for the {@link #latitudeOfOrigin} parameter value. Valid values range is
         * from -90 to 90°. Default value is 0.
         */
        public static final ParameterDescriptor LATITUDE_OF_CENTRE = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "latitude_of_center"),
                    new NamedIdentifier(Citations.EPSG, "Latitude of natural origin"),
                    new NamedIdentifier(Citations.EPSG, "Spherical latitude of origin"),
                    new NamedIdentifier(Citations.ESRI, "Latitude_Of_Origin"),
                    new NamedIdentifier(Citations.GEOTIFF, "ProjCenterLat"),
                    new NamedIdentifier(Citations.PROJ, "lat_0")
                },
                0,
                -90,
                90,
                NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@link #centralMeridian} parameter value. Valid values range is
         * from -180 to 180°. Default value is 0.
         */
        public static final ParameterDescriptor LONGITUDE_OF_CENTRE = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "longitude_of_center"),
                    new NamedIdentifier(Citations.EPSG, "Longitude of natural origin"),
                    new NamedIdentifier(Citations.EPSG, "Spherical longitude of origin"),
                    new NamedIdentifier(Citations.ESRI, "Central_Meridian"),
                    new NamedIdentifier(Citations.GEOTIFF, "ProjCenterLong"),
                    new NamedIdentifier(Citations.PROJ, "lon_0")
                },
                0,
                -180,
                180,
                NonSI.DEGREE_ANGLE);

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "Gnomonic"),
                    new NamedIdentifier(Citations.GEOTIFF, "CT_Gnomonic"),
                    new NamedIdentifier(Citations.PROJ, "gnom")
                },
                new ParameterDescriptor[] {
                    SEMI_MAJOR, SEMI_MINOR,
                    LATITUDE_OF_CENTRE, LONGITUDE_OF_CENTRE,
                    FALSE_EASTING, FALSE_NORTHING
                });

        /** Constructs a new provider. */
        public Provider() {
            super(PARAMETERS);
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        @Override
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException {
            return new Gnomonic(parameters);
        }
    }
}
