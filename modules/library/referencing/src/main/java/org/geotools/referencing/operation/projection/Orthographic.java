/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2000-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.logging.Level;

import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.PlanarProjection;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;

import static java.lang.Math.*;


/**
 * Orthographic Projection. This is a perspective azimuthal (planar) projection
 * that is neither conformal nor equal-area. It resembles a globe and only
 * one hemisphere can be seen at a time, since it is
 * a perspectiove projection from infinite distance. While not useful for
 * accurate measurements, this projection is useful for pictorial views of the
 * world. Only the spherical form is given here.
 * <p>
 * <b>NOTE:</b>
 * formulae used below are from a port, to java, of the {@code proj}
 * package of the USGS survey. USGS work is acknowledged here.
 * <p>
 * <b>References:</b>
 * <ul>
 *   <li>Proj-4.4.7 available at <A HREF="http://www.remotesensing.org/proj">www.remotesensing.org/proj</A><br>
 *       Relevant files are: {@code PJ_ortho.c}, {@code pj_fwd.c} and {@code pj_inv.c}.</li>
 *   <li>John P. Snyder (Map Projections - A Working Manual,
 *       U.S. Geological Survey Professional Paper 1395, 1987)</li>
 * </ul>
 *
 * @see <A HREF="http://mathworld.wolfram.com/OrthographicProjection.html">Orthographic projection on mathworld.wolfram.com</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/orthographic.html">"Orthographic" on www.remotesensing.org</A>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Rueben Schulz
 */
public abstract class Orthographic extends MapProjection {
    /**
     * For compatibility with different versions during deserialization.
     */
    private static final long serialVersionUID = -6489939032996419868L;

    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double EPSILON = 1E-6;

    /**
     * Creates a transform from the specified group of parameter values.
     *
     * @param  parameters The group of parameter values.
     * @throws ParameterNotFoundException if a required parameter was not found.
     *
     * @since 2.4
     */
    protected Orthographic(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        // Fetch parameters
        super(parameters);
    }

    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
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
        // Relevant parameters are already compared in MapProjection
        return super.equals(object);
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
     * provider} for a {@linkplain Orthographic Orthographic} projection.
     *
     * @since 2.1
     * @version $Id$
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static final class Provider extends AbstractProvider {
        /**
         * For compatibility with different versions during deserialization.
         */
        private static final long serialVersionUID = 3180410512573499562L;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Orthographic"),
                new NamedIdentifier(Citations.GEOTIFF,  "CT_Orthographic"),
                new NamedIdentifier(Citations.ESRI,     "Orthographic"),
                new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.formatInternational(
                                                        VocabularyKeys.ORTHOGRAPHIC_PROJECTION))
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,       SEMI_MINOR,
                CENTRAL_MERIDIAN, LATITUDE_OF_ORIGIN,
                SCALE_FACTOR,
                FALSE_EASTING,    FALSE_NORTHING
            });

        /**
         * Constructs a new provider.
         */
        public Provider() {
            super(PARAMETERS);
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
                throws ParameterNotFoundException, FactoryException
        {
            // Values here are in radians (the standard units for the map projection package)
            final double latitudeOfOrigin = abs(AbstractProvider.doubleValue(LATITUDE_OF_ORIGIN, parameters));
            if (!isSpherical(parameters)) {
                LOGGER.log(Level.FINE, "GeoTools Orthographic is defined only on the sphere, " +
                        "we're going to use spherical equations even if the projection is using an ellipsoid");
            }
            // Polar case.
            if (abs(latitudeOfOrigin - PI/2) < EPSILON) {
                return new PolarOrthographic(parameters);
            }
            // Equatorial case.
            else if (latitudeOfOrigin < EPSILON) {
                return new EquatorialOrthographic(parameters);
            }
            // Generic (oblique) case.
            else {
                return new ObliqueOrthographic(parameters);
            }
        }
    }
}
