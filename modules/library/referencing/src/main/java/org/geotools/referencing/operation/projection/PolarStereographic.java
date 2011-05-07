/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 1999-2008, Open Source Geospatial Foundation (OSGeo)
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
 *    This package contains formulas from the PROJ package of USGS.
 *    USGS's work is fully acknowledged here. This derived work has
 *    been relicensed under LGPL with Frank Warmerdam's permission.
 */
package org.geotools.referencing.operation.projection;

import java.awt.geom.Point2D;
import java.util.Collection;
import javax.measure.unit.NonSI;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.referencing.operation.MathTransform;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.metadata.iso.citation.Citations;

import static java.lang.Math.*;


/**
 * The polar case of the {@linkplain Stereographic stereographic} projection.
 * This default implementation uses USGS equation (i.e. iteration) for computing
 * the {@linkplain #inverseTransformNormalized inverse transform}.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author André Gosselin
 * @author Martin Desruisseaux (PMO, IRD)
 * @author Rueben Schulz
 */
public class PolarStereographic extends Stereographic {
    /**
     * For compatibility with different versions during deserialization.
     */
    private static final long serialVersionUID = -6635298308431138524L;

    /**
     * Maximum number of iterations for iterative computations.
     */
    private static final int MAXIMUM_ITERATIONS = 15;

    /**
     * Difference allowed in iterative computations.
     */
    private static final double ITERATION_TOLERANCE = 1E-10;

    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double EPSILON = 1E-8;

    /**
     * A constant used in the transformations.
     * This is <strong>not</strong> equal to the {@link #scaleFactor}.
     */
    private final double k0;

    /**
     * Latitude of true scale, in radians (a.k.a. {@code "standard_parallel_1").
     */
    final double standardParallel;

    /**
     * {@code true} if this projection is for the south pole, or {@code false}
     * if it is for the north pole.
     */
    final boolean southPole;

    /**
     * {@code true} if {@link #southPole} was forced, or {@code false} if it was auto-detected.
     */
    private final boolean poleForced;

    /**
     * Constructs a polar stereographic projection.
     *
     * @param  parameters The group of parameter values.
     * @param  descriptor The expected parameter descriptor.
     * @param  forceSouthPole Forces projection to North pole if {@link Boolean#FALSE},
     *         to South pole if {@link Boolean#TRUE}, or do not force (i.e. detect
     *         from other parameter values) if {@code null}.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    PolarStereographic(final ParameterValueGroup      parameters,
                       final ParameterDescriptorGroup descriptor,
                       final Boolean                  forceSouthPole)
            throws ParameterNotFoundException
    {
        super(parameters, descriptor);
        /*
         * Get "standard_parallel_1" parameter value. This parameter should be mutually exclusive
         * with "latitude_of_origin", but this is not a strict requirement for this constructor.
         *
         *   +-----------------------------------+--------------------+-------------+
         *   | Projection                        | Parameter          | Force pole  |
         *   | --------------------------------- | ------------------ | ----------- |
         *   | Polar Stereographic (variant A)   | Latitude of origin | auto detect |
         *   | Polar Stereographic (variant B)   | Standard Parallel  | auto detect |
         *   | Stereographic North Pole          | Standard Parallel  | North pole  |
         *   | Stereographic South Pole          | Standard Parallel  | South pole  |
         *   +-----------------------------------+--------------------+-------------+
         *
         * "Standard Parallel" (a.k.a. "Latitude true scale") default to 90°N for every cases
         * (including Polar A, but it is meanless in this case), except for "Stereographic South
         * Pole" where it default to 90°S.
         */
        final ParameterDescriptor trueScaleDescriptor = Boolean.TRUE.equals(forceSouthPole) ?
                ProviderSouth.STANDARD_PARALLEL : ProviderNorth.STANDARD_PARALLEL;
        final Collection<GeneralParameterDescriptor> expected = descriptor.descriptors();
        double latitudeTrueScale;
        if (isExpectedParameter(expected, trueScaleDescriptor)) {
            // Any cases except Polar A
            latitudeTrueScale = doubleValue(expected, trueScaleDescriptor, parameters);
        } else {
            // Polar A case
            latitudeTrueScale = (latitudeOfOrigin < 0) ? -PI/2 : PI/2;
        }
        ensureLatitudeInRange(trueScaleDescriptor, latitudeTrueScale, true);
        /*
         * Forces the "standard_parallel_1" to the appropriate hemisphere,
         * and forces the "latitude_of_origin" to ±90°.
         */
        poleForced = (forceSouthPole != null);
        if (poleForced) {
            southPole = forceSouthPole.booleanValue();
            latitudeTrueScale = abs(latitudeTrueScale);
            if (southPole) {
                latitudeTrueScale = -latitudeTrueScale;
            }
        } else {
            southPole = (latitudeTrueScale < 0);
        }
        this.latitudeOfOrigin = (southPole) ? -(PI/2) : +(PI/2);
        this.standardParallel = latitudeTrueScale; // May be anything in [-90 .. +90] range.
        /*
         * Computes coefficients.
         */
        latitudeTrueScale = abs(latitudeTrueScale);
        if (abs(latitudeTrueScale - PI/2) >= EPSILON) {
            final double t = sin(latitudeTrueScale);
            k0 = msfn(t, cos(latitudeTrueScale)) /
                 tsfn(latitudeTrueScale, t); // Derives from (21-32 and 21-33)
        } else {
            // True scale at pole (part of (21-33))
            k0 = 2.0 / sqrt(pow(1+excentricity, 1+excentricity)*
                            pow(1-excentricity, 1-excentricity));
        }
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
     * (units in radians) and stores the result in {@code ptDst} (linear distance
     * on a unit sphere).
     *
     * @param x The longitude of the coordinate, in <strong>radians</strong>.
     * @param y The  latitude of the coordinate, in <strong>radians</strong>.
     */
    protected Point2D transformNormalized(double x, double y, Point2D ptDst)
            throws ProjectionException
    {
        final double sinlat = sin(y);
        final double coslon = cos(x);
        final double sinlon = sin(x);
        if (southPole) {
            final double rho = k0 * tsfn(-y, -sinlat);
            x = rho * sinlon;
            y = rho * coslon;
        } else {
            final double rho = k0 * tsfn(y, sinlat);
            x =  rho * sinlon;
            y = -rho * coslon;
        }
        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }

    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinates (units in radians)
     * and stores the result in {@code ptDst} (linear distance on a unit sphere).
     */
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst)
            throws ProjectionException
    {
        final double rho = hypot(x, y);
        if (southPole) {
            y = -y;
        }
        /*
         * Compute latitude using iterative technique.
         */
        final double t = rho/k0;
        final double halfe = excentricity/2.0;
        double phi0 = 0;
        for (int i=MAXIMUM_ITERATIONS;;) {
            final double esinphi = excentricity * sin(phi0);
            final double phi = (PI/2) - 2.0*atan(t*pow((1-esinphi)/(1+esinphi), halfe));
            if (abs(phi-phi0) < ITERATION_TOLERANCE) {
                x = (abs(rho) < EPSILON) ? 0.0 : atan2(x, -y);
                y = (southPole) ? -phi : phi;
                break;
            }
            phi0 = phi;
            if (--i < 0) {
                throw new ProjectionException(ErrorKeys.NO_CONVERGENCE);
            }
        }
        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterDescriptor trueScaleDescriptor = poleForced ? (southPole ?
                ProviderSouth.STANDARD_PARALLEL :  // forced = true,  south = true
                ProviderNorth.STANDARD_PARALLEL):  // forced = true,  south = false
                ProviderB    .STANDARD_PARALLEL ;  // forced = false
        final ParameterValueGroup values = super.getParameterValues();
        final Collection<GeneralParameterDescriptor> expected = getParameterDescriptors().descriptors();
        set(expected, trueScaleDescriptor, values, standardParallel);
        return values;
    }

    /**
     * Returns a hash value for this map projection.
     */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(k0);
        return ((int)code ^ (int)(code >>> 32)) + 37*super.hashCode();
    }

    /**
     * Compares the specified object with this map projection for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final PolarStereographic that = (PolarStereographic) object;
            return this.southPole == that.southPole &&
                   equals(this.k0,               that.k0) &&
                   equals(this.standardParallel, that.standardParallel);
        }
        return false;
    }


    /**
     * Provides the transform equations for the spherical case of the polar
     * stereographic projection.
     *
     * @version $Id$
     * @author Martin Desruisseaux (PMO, IRD)
     * @author Rueben Schulz
     */
    static final class Spherical extends PolarStereographic {
        /**
         * For compatibility with different versions during deserialization.
         */
        private static final long serialVersionUID = 1655096575897215547L;

        /**
         * A constant used in the transformations. This constant hides the {@code k0}
         * constant from the ellipsoidal case. The spherical and ellipsoidal {@code k0}
         * are not computed in the same way, and we preserve the ellipsoidal {@code k0}
         * in {@link Stereographic} in order to allow assertions to work.
         */
        private final double k0;

        /**
         * Constructs a spherical stereographic projection.
         *
         * @param  parameters The group of parameter values.
         * @param  descriptor The expected parameter descriptor.
         * @param  forceSouthPole For projection to North pole if {@link Boolean#FALSE},
         *         to South pole if {@link Boolean#TRUE}, or do not force (i.e. detect
         *         from other parameter values) if {@code null}.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        Spherical(final ParameterValueGroup      parameters,
                  final ParameterDescriptorGroup descriptor,
                  final Boolean                  forceSouthPole)
                throws ParameterNotFoundException
        {
            super(parameters, descriptor, forceSouthPole);
            ensureSpherical();
            final double phi = abs(standardParallel);
            if (abs(phi - PI/2) >= EPSILON) {
                k0 = 1 + sin(phi);  // derived from (21-7) and (21-11)
            } else {
                k0 = 2;
            }
        }

        /**
         * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
         * (units in radians) and stores the result in {@code ptDst} (linear distance
         * on a unit sphere).
         */
        @Override
        protected Point2D transformNormalized(double x, double y, Point2D ptDst)
                throws ProjectionException
        {
            // Compute using ellipsoidal formulas, for comparaison later.
            assert (ptDst = super.transformNormalized(x, y, ptDst)) != null;
            final double coslat = cos(y);
            final double sinlat = sin(y);
            final double coslon = cos(x);
            final double sinlon = sin(x);
            if (southPole) {
                if (abs(1 - sinlat) < EPSILON) {
                    throw new ProjectionException(ErrorKeys.VALUE_TEND_TOWARD_INFINITY);
                }
                // (21-12)
                final double f = k0 * coslat / (1-sinlat); // == tan (pi/4 + phi/2)
                x = f * sinlon; // (21-9)
                y = f * coslon; // (21-10)
            } else {
                if (abs(1 + sinlat) < EPSILON) {
                    throw new ProjectionException(ErrorKeys.VALUE_TEND_TOWARD_INFINITY);
                }
                // (21-8)
                final double f = k0 * coslat / (1+sinlat); // == tan (pi/4 - phi/2)
                x =  f * sinlon; // (21-5)
                y = -f * coslon; // (21-6)
            }
            assert checkTransform(x, y, ptDst);
            if (ptDst != null) {
                ptDst.setLocation(x,y);
                return ptDst;
            }
            return new Point2D.Double(x,y);
        }

        /**
         * Transforms the specified (<var>x</var>,<var>y</var>) coordinates (units in radians)
         * and stores the result in {@code ptDst} (linear distance on a unit sphere).
         */
        @Override
        protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst)
                throws ProjectionException
        {
            // Compute using ellipsoidal formulas, for comparaison later.
            assert (ptDst = super.inverseTransformNormalized(x, y, ptDst)) != null;
            final double rho = hypot(x, y);
            if (!southPole) {
                y = -y;
            }
            // (20-17) call atan2(x,y) to properly deal with y==0
            x = (abs(x)<EPSILON && abs(y)<EPSILON) ? 0.0 : atan2(x, y);
            if (abs(rho) < EPSILON) {
                y = latitudeOfOrigin;
            } else {
                final double c = 2.0 * atan(rho/k0);
                final double cosc = cos(c);
                y = (southPole) ? asin(-cosc) : asin(cosc);
                // (20-14) with phi1=90
            }
            assert checkInverseTransform(x, y, ptDst);
            if (ptDst != null) {
                ptDst.setLocation(x,y);
                return ptDst;
            }
            return new Point2D.Double(x,y);
        }
    }

    /**
     * Overides {@link PolarStereographic} to use the a series for the
     * {@link #inverseTransformNormalized inverseTransformNormalized(...)}
     * method. This is the equation specified by the EPSG. Allows for a
     * {@code "latitude_true_scale"} parameter to be used, but this
     * parameter is not listed by the EPSG and is not given as a parameter
     * by the provider.
     * <p>
     * Compared to the default {@link PolarStereographic} implementation, the series
     * implementation is a little bit faster at the expense of a little bit less
     * accuracy. The default {@link PolarStereographic} implementation
     * is a derivated work of Proj4, and is therefor better tested.
     *
     * @version $Id$
     * @author Rueben Schulz
     */
    static final class Series extends PolarStereographic {
        /**
         * For compatibility with different versions during deserialization.
         */
        private static final long serialVersionUID = 2795404156883313290L;

        /**
         * Constants used for the inverse polar series
         */
        private final double A, B;

        /**
         * Constants used for the inverse polar series
         */
        private double C, D;

        /**
         * A constant used in the transformations. This constant hides the {@code k0}
         * constant from the USGS case. The EPSG and USGS {@code k0} are not computed
         * in the same way, and we preserve the USGS {@code k0} in order to allow
         * assertions to work.
         */
        private final double k0;

        /**
         * Constructs a polar stereographic projection (seires inverse equations).
         *
         * @param  parameters The group of parameter values.
         * @param  descriptor The expected parameter descriptor.
         * @param  forceSouthPole For projection to North pole if {@link Boolean#FALSE},
         *         to South pole if {@link Boolean#TRUE}, or do not force (i.e. detect
         *         from other parameter values) if {@code null}.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        Series(final ParameterValueGroup      parameters,
               final ParameterDescriptorGroup descriptor,
               final Boolean                  forceSouthPole)
                throws ParameterNotFoundException
        {
            super(parameters, descriptor, forceSouthPole);
            // See Snyde P. 19, "Computation of Series"
            final double e4 = excentricitySquared * excentricitySquared;
            final double e6 = e4 * excentricitySquared;
            final double e8 = e4 * e4;
            C = 7.0/120.0 * e6 + 81.0/1120.0 * e8;
            D = 4279.0/161280.0 * e8;
            A = excentricitySquared/2.0 + 5.0/24.0*e4 + e6/12.0 + 13.0/360.0*e8 - C;
            B = 2.0 * (7.0/48.0*e4 + 29.0/240.0*e6 + 811.0/11520.0*e8) - 4.0*D;
            C *= 4.0;
            D *= 8.0;
            final double latTrueScale = abs(standardParallel);
            if (abs(latTrueScale - PI/2) >= EPSILON) {
                final double t = sin(latTrueScale);
                k0 = msfn(t, cos(latTrueScale)) *
                             sqrt(pow(1+excentricity, 1+excentricity)*
                                  pow(1-excentricity, 1-excentricity)) /
                             (2.0*tsfn(latTrueScale, t));
            } else {
                k0 = 1.0;
            }
        }

        /**
         * Transforms the specified (<var>x</var>,<var>y</var>) coordinates
         * and stores the result in {@code ptDst}.
         */
        @Override
        protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst)
                throws ProjectionException
        {
            // Compute using iteration formulas, for comparaison later.
            assert (ptDst = super.inverseTransformNormalized(x, y, ptDst)) != null;
            final double rho = hypot(x, y);
            if (southPole) {
                y = -y;
            }
            // The series form
            final double t = (rho/k0) * sqrt(pow(1+excentricity, 1+excentricity)*
                             pow(1-excentricity, 1-excentricity)) / 2;
            final double chi = PI/2 - 2*atan(t);

            x = (abs(rho) < EPSILON) ? 0.0 : atan2(x, -y);

            // See Snyde P. 19, "Computation of Series"
            final double sin2chi = sin(2.0 * chi);
            final double cos2chi = cos(2.0 * chi);
            y = chi + sin2chi*(A + cos2chi*(B + cos2chi*(C + D*cos2chi)));
            y = (southPole) ? -y : y;

            assert checkInverseTransform(x, y, ptDst);
            if (ptDst != null) {
                ptDst.setLocation(x,y);
                return ptDst;
            }
            return new Point2D.Double(x,y);
        }
    }




    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                                 PROVIDERS                                ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for a {@linkplain PolarStereographic Polar Stereographic} projection. This
     * provider uses the series equations for the inverse elliptical calculations.
     *
     * @since 2.4
     * @version $Id$
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static final class ProviderA extends Stereographic.Provider {
        /**
         * For compatibility with different versions during deserialization.
         */
        private static final long serialVersionUID = 9124091259039220308L;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Polar_Stereographic"),
                new NamedIdentifier(Citations.EPSG,     "Polar Stereographic (variant A)"),
                new NamedIdentifier(Citations.EPSG,     "9810"),
                new NamedIdentifier(Citations.GEOTIFF,  "CT_PolarStereographic"),
                new NamedIdentifier(Citations.GEOTOOLS, Provider.NAME)
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                CENTRAL_MERIDIAN,    LATITUDE_OF_ORIGIN,
                SCALE_FACTOR,
                FALSE_EASTING,       FALSE_NORTHING
            });

        /**
         * Constructs a new provider.
         */
        public ProviderA() {
            super(PARAMETERS);
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        @Override
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            if (isSpherical(parameters)) {
                return new PolarStereographic.Spherical(parameters, PARAMETERS, null);
            } else {
                return new PolarStereographic.Series(parameters, PARAMETERS, null);
            }
        }
    }

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for a {@linkplain PolarStereographic Polar Stereographic} (Variant B)
     * projection. This provider includes a "Standard_Parallel_1" parameter and determines
     * the hemisphere of the projection from the Standard_Parallel_1 value. It also uses the
     * series equations for the inverse elliptical calculations.
     *
     * @since 2.4
     * @version $Id$
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static final class ProviderB extends Stereographic.Provider {
        /**
         * For compatibility with different versions during deserialization.
         */
        private static final long serialVersionUID = 5188231050523249971L;

        /**
         * The operation parameter descriptor for the {@code standardParallel}
         * parameter value. Valid values range is from -90 to 90°. The default
         * value is 90°N.
         */
        public static final ParameterDescriptor STANDARD_PARALLEL = ProviderNorth.STANDARD_PARALLEL;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.EPSG,     "Polar Stereographic (variant B)"),
                new NamedIdentifier(Citations.EPSG,     "9829"),
                new NamedIdentifier(Citations.GEOTOOLS, Provider.NAME)
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                CENTRAL_MERIDIAN,    STANDARD_PARALLEL,
                FALSE_EASTING,       FALSE_NORTHING
            });

        /**
         * Constructs a new provider.
         */
        public ProviderB() {
            super(PARAMETERS);
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        @Override
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            if (isSpherical(parameters)) {
                return new PolarStereographic.Spherical(parameters, PARAMETERS, null);
            } else {
                return new PolarStereographic.Series(parameters, PARAMETERS, null);
            }
        }
    }

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for a {@linkplain PolarStereographic North Polar Stereographic} projection.
     * This provider sets the "latitude_of_origin" parameter to +90.0 degrees and uses the
     * iterative equations for the inverse elliptical calculations.
     *
     * @since 2.4
     * @version $Id$
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static final class ProviderNorth extends Stereographic.Provider {
        /**
         * For compatibility with different versions during deserialization.
         */
        private static final long serialVersionUID = 657493908431273866L;

        /**
         * The operation parameter descriptor for the {@code standardParallel}
         * parameter value. Valid values range is from -90 to 90°. The default
         * value is 90°N.
         */
        public static final ParameterDescriptor STANDARD_PARALLEL = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.ESRI, "Standard_Parallel_1"),
                    new NamedIdentifier(Citations.EPSG, "Latitude of standard parallel")
                },
                90, -90, 90, NonSI.DEGREE_ANGLE);

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.ESRI,     "Stereographic_North_Pole"),
                new NamedIdentifier(Citations.GEOTOOLS, Provider.NAME)
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                CENTRAL_MERIDIAN,    STANDARD_PARALLEL,  SCALE_FACTOR,
                FALSE_EASTING,       FALSE_NORTHING
            });

        /**
         * Constructs a new provider.
         */
        public ProviderNorth() {
            super(PARAMETERS);
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        @Override
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            if (isSpherical(parameters)) {
                return new PolarStereographic.Spherical(parameters, PARAMETERS, Boolean.FALSE);
            } else {
                return new PolarStereographic(parameters, PARAMETERS, Boolean.FALSE);
            }
        }
    }

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for a {@linkplain PolarStereographic South Polar Stereographic} projection.
     * This provider sets the "latitude_of_origin" parameter to -90.0 degrees and uses the
     * iterative equations for the inverse elliptical calculations.
     *
     * @since 2.4
     * @version $Id$
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static final class ProviderSouth extends Stereographic.Provider {
        /**
         * For compatibility with different versions during deserialization.
         */
        private static final long serialVersionUID = 6537800238416448564L;

        /**
         * The operation parameter descriptor for the {@code standardParallel}
         * parameter value. Valid values range is from -90 to 90°. The default
         * value is 90°S.
         */
        public static final ParameterDescriptor STANDARD_PARALLEL = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.ESRI, "Standard_Parallel_1"),
                    new NamedIdentifier(Citations.EPSG, "Latitude of standard parallel")
                },
                -90, -90, 90, NonSI.DEGREE_ANGLE);

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.ESRI,     "Stereographic_South_Pole"),
                new NamedIdentifier(Citations.GEOTOOLS, Provider.NAME)
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                CENTRAL_MERIDIAN,    STANDARD_PARALLEL,  SCALE_FACTOR,
                FALSE_EASTING,       FALSE_NORTHING
            });

        /**
         * Constructs a new provider.
         */
        public ProviderSouth() {
            super(PARAMETERS);
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        @Override
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            if (isSpherical(parameters)) {
                return new PolarStereographic.Spherical(parameters, PARAMETERS, Boolean.TRUE);
            } else {
                return new PolarStereographic(parameters, PARAMETERS, Boolean.TRUE);
            }
        }
    }
}
