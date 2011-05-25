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

import org.opengis.util.InternationalString;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.PlanarProjection;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.util.Utilities;

import static java.lang.Math.*;


/**
 * Stereographic Projection. The directions starting from the central point are true,
 * but the areas and the lengths become increasingly deformed as one moves away from
 * the center.  This projection is used to represent polar areas.  It can be adapted
 * for other areas having a circular form.
 * <p>
 * This implementation, and its subclasses, provides transforms for six cases of the
 * stereographic projection:
 * <ul>
 *   <li>{@code "Oblique_Stereographic"} (EPSG code 9809), alias {@code "Double_Stereographic"}
 *       in ESRI software</li>
 *   <li>{@code "Stereographic"} in ESRI software (<strong>NOT</strong> EPSG code 9809)</li>
 *   <li>{@code "Polar_Stereographic"} (EPSG code 9810, uses a series calculation for the
 *       inverse)</li>
 *   <li>{@code "Polar_Stereographic (variant B)"} (EPSG code 9829, uses a series calculation
 *       for the inverse)</li>
 *   <li>{@code "Stereographic_North_Pole"} in ESRI software (uses iteration for the inverse)</li>
 *   <li>{@code "Stereographic_South_Pole"} in ESRI software (uses iteration for the inverse)</li>
 * </ul>
 * <p>
 * Both the {@code "Oblique_Stereographic"} and {@code "Stereographic"}
 * projections are "double" projections involving two parts: 1) a conformal
 * transformation of the geographic coordinates to a sphere and 2) a spherical
 * Stereographic projection. The EPSG considers both methods to be valid, but
 * considers them to be a different coordinate operation methods.
 * <p>
 * The {@code "Stereographic"} case uses the USGS equations of Snyder.
 * This employs a simplified conversion to the conformal sphere that
 * computes the conformal latitude of each point on the sphere.
 * <p>
 * The {@code "Oblique_Stereographic"} case uses equations from the EPSG.
 * This uses a more generalized form of the conversion to the conformal sphere; using only
 * a single conformal sphere at the origin point. Since this is a "double" projection,
 * it is sometimes called the "Double Stereographic". The {@code "Oblique_Stereographic"}
 * is used in New Brunswick (Canada) and the Netherlands.
 * <p>
 * The {@code "Stereographic"} and {@code "Double_Stereographic"} names are
 * used in ESRI's ArcGIS 8.x product. The {@code "Oblique_Stereographic"}
 * name is the EPSG name for the later only.
 * <p>
 * <strong>WARNING:</strong> Tests points calculated with ArcGIS's {@code "Double_Stereographic"}
 * are not always equal to points calculated with the {@code "Oblique_Stereographic"}.
 * However, where there are differences, two different implementations of these equations
 * (EPSG guidence note 7 and {@code libproj}) calculate the same values as we do. Until these
 * differences are resolved, please be careful when using this projection.
 * <p>
 * If a {@link Stereographic.Provider#LATITUDE_OF_ORIGIN "latitude_of_origin"} parameter is
 * supplied and is not consistent with the projection classification (for example a latitude
 * different from &plusmn;90° for the polar case), then the oblique or polar case will be
 * automatically inferred from the latitude. In other words, the latitude of origin has
 * precedence on the projection classification. If ommited, then the default value is 90°N
 * for {@code "Polar_Stereographic"} and 0° for {@code "Oblique_Stereographic"}.
 * <p>
 * Polar projections that use the series equations for the inverse calculation will
 * be little bit faster, but may be a little bit less accurate. If a polar
 * {@link Stereographic.Provider#LATITUDE_OF_ORIGIN "latitude_of_origin"} is used for
 * the {@code "Oblique_Stereographic"} or {@code "Stereographic"}, the iterative
 * equations will be used for inverse polar calculations.
 * <p>
 * The {@code "Polar Stereographic (variant B)"}, {@code "Stereographic_North_Pole"},
 * and {@code "Stereographic_South_Pole"} cases include a
 * {@link StereographicPole.ProviderB#STANDARD_PARALLEL "standard_parallel_1"} parameter.
 * This parameter sets the latitude with a scale factor equal to the supplied
 * scale factor. The {@code "Polar Stereographic (variant A)"} receives its
 * {@code "latitude_of_origin"} parameter value from the hemisphere of the
 * {@link StereographicPole.Provider#LATITUDE_OF_ORIGIN "latitude_of_origin"} value
 * (i.e. the value is forced to &plusmn;90°).
 * <p>
 * <b>References:</b>
 * <ul>
 *   <li>John P. Snyder (Map Projections - A Working Manual,<br>
 *       U.S. Geological Survey Professional Paper 1395, 1987)</li>
 *   <li>"Coordinate Conversions and Transformations including Formulas",<br>
 *       EPSG Guidence Note Number 7, Version 19.</li>
 *   <li>Gerald Evenden. <A HREF="http://members.bellatlantic.net/~vze2hc4d/proj4/sterea.pdf">
 *       "Supplementary PROJ.4 Notes - Oblique Stereographic Alternative"</A></li>
 *   <li>Krakiwsky, E.J., D.B. Thomson, and R.R. Steeves. 1977. A Manual
 *       For Geodetic Coordinate Transformations in the Maritimes.
 *       Geodesy and Geomatics Engineering, UNB. Technical Report No. 48.</li>
 *   <li>Thomson, D.B., M.P. Mepham and R.R. Steeves. 1977.
 *       The Stereographic Double Projection.
 *       Geodesy and Geomatics Engineereng, UNB. Technical Report No. 46.</li>
 * </ul>
 *
 * @see <A HREF="http://mathworld.wolfram.com/StereographicProjection.html">Stereographic projection on MathWorld</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/polar_stereographic.html">Polar_Stereographic</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/oblique_stereographic.html">Oblique_Stereographic</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/stereographic.html">Stereographic</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/random_issues.html#stereographic">Some Random Stereographic Issues</A>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author André Gosselin
 * @author Martin Desruisseaux (PMO, IRD)
 * @author Rueben Schulz
 */
public abstract class Stereographic extends MapProjection {
    /**
     * For compatibility with different versions during deserialization.
     */
    private static final long serialVersionUID = -176731870235252852L;

    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double EPSILON = 1E-6;

    /**
     * The parameter descriptor group to be returned by {@link #getParameterDescriptors()}.
     */
    private final ParameterDescriptorGroup descriptor;

    /**
     * Creates a transform from the specified group of parameter values.
     *
     * @param  parameters The group of parameter values.
     * @param  descriptor The expected parameter descriptor.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    Stereographic(final ParameterValueGroup parameters, final ParameterDescriptorGroup descriptor)
            throws ParameterNotFoundException
    {
        // Fetch parameters
        super(parameters, descriptor.descriptors());
        this.descriptor = descriptor;
    }

    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return descriptor;
    }

    /**
     * Compares the specified object with this map projection for equality.
     */
    @Override
    public boolean equals(final Object object) {
        /*
         * Implementation note: usually, we define this method in the last subclass, which may
         * compare every fields.  However, all fields in subclasses like StereographicUSGS are
         * fully determined by the parameters like "latitude_of_origin", which are already
         * compared by super.equals(object). Comparing those derived fields would be redundant.
         */
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final Stereographic that = (Stereographic) object;
            return Utilities.equals(this.descriptor, that.descriptor);
        }
        return false;
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
     * provider} for a {@linkplain Stereographic Stereographic} projections using USGS equations.
     * This is <strong>not</strong> the provider for EPSG 9809. For the later, use
     * {@link ObliqueStereographic.Provider} instead.
     *
     * @since 2.4
     * @version $Id$
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /**
         * For compatibility with different versions during deserialization.
         */
        private static final long serialVersionUID = 1243300263948365065L;

        /**
         * The localized name for stereographic projection.
         */
        static final InternationalString NAME =
                Vocabulary.formatInternational(VocabularyKeys.STEREOGRAPHIC_PROJECTION);

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.ESRI,     "Stereographic"),
                new NamedIdentifier(Citations.GEOTIFF,  "CT_Stereographic"),
                new NamedIdentifier(Citations.GEOTOOLS, NAME)
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                CENTRAL_MERIDIAN,    LATITUDE_OF_ORIGIN,
                SCALE_FACTOR,
                FALSE_EASTING,       FALSE_NORTHING
            });

        /**
         * Constructs a new provider with default parameters for EPSG stereographic oblique.
         */
        public Provider() {
            this(PARAMETERS);
        }

        /**
         * Constructs a math transform provider from a set of parameters. The provider
         * {@linkplain #getIdentifiers identifiers} will be the same than the parameter
         * ones.
         *
         * @param parameters The set of parameters (never {@code null}).
         */
        protected Provider(final ParameterDescriptorGroup parameters) {
            super(parameters);
        }

        /**
         * Returns the operation type for this map projection.
         */
        @Override
        public Class<PlanarProjection> getOperationType() {
            return PlanarProjection.class;
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            // Values here are in radians (the standard units for the map projection package)
            final double latitudeOfOrigin = abs(AbstractProvider.doubleValue(LATITUDE_OF_ORIGIN, parameters));
            final boolean     isSpherical = isSpherical(parameters);
            final ParameterDescriptorGroup descriptor = getParameters();
            // Polar case.
            if (abs(latitudeOfOrigin - PI/2) < EPSILON) {
                if (isSpherical) {
                    return new PolarStereographic.Spherical(parameters, descriptor, null);
                } else {
                    return new PolarStereographic(parameters, descriptor, null);
                }
            } else
            // Equatorial case.
            if (latitudeOfOrigin < EPSILON) {
                if (isSpherical) {
                    return new EquatorialStereographic.Spherical(parameters, descriptor);
                } else {
                    return createMathTransform(parameters, descriptor);
                }
            } else
            // Generic (oblique) case.
            if (isSpherical) {
                return new StereographicUSGS.Spherical(parameters, descriptor);
            } else {
                return createMathTransform(parameters, descriptor);
            }
        }

        /**
         * Creates the general case. To be overriden by the EPSG case only.
         */
        MathTransform createMathTransform(final ParameterValueGroup parameters,
                                          final ParameterDescriptorGroup descriptor)
                throws ParameterNotFoundException
        {
            return new StereographicUSGS(parameters, descriptor);
        }
    }
}
