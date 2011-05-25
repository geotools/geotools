/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Collection;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.referencing.operation.MathTransform;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.metadata.iso.citation.Citations;


/**
 * Hotine Oblique Mercator projection. It is similar to the {@link ObliqueMercator oblique mercator}
 * projection, except that coordinates start at the intersection of the central line and the equator
 * of the aposphere.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Gerald I. Evenden (for original code in Proj4)
 * @author Rueben Schulz
 */
public class HotineObliqueMercator extends ObliqueMercator {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 7376814731765422533L;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     *
     * @todo Current implementation assumes a "azimuth" case. We may try to detect
     *       automatically the "two points" case in a future version if needed. Note
     *       that this limitation doesn't apply to projection created from the
     *       {@link Provider_TwoPoint}.
     */
    protected HotineObliqueMercator(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        this(parameters, Provider.PARAMETERS.descriptors(), false);
    }

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param parameters The parameter values in standard units.
     * @param expected The expected parameter descriptors.
     * @param twoPoint {@code true} for the "two points" case, or {@code false} for the
     *         "azimuth" case. The former is used by ESRI but not EPSG.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    HotineObliqueMercator(final ParameterValueGroup parameters,
                          final Collection<GeneralParameterDescriptor> expected,
                          final boolean twoPoint)
            throws ParameterNotFoundException
    {
        // Fetch parameters
        super(parameters, expected, twoPoint, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return (twoPoint) ? Provider_TwoPoint.PARAMETERS : Provider.PARAMETERS;
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
     * provider} for a {@linkplain HotineObliqueMercator Hotine Oblique Mercator} projection
     * (EPSG code 9812).
     *
     * @since 2.4
     * @version $Id$
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static final class Provider extends ObliqueMercator.Provider {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = 5822488360988630419L;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Hotine_Oblique_Mercator"),
                new NamedIdentifier(Citations.EPSG,     "Hotine Oblique Mercator"),
                new NamedIdentifier(Citations.EPSG,     "9812"),
                new NamedIdentifier(Citations.GEOTIFF,  "CT_ObliqueMercator_Hotine"),
                new NamedIdentifier(Citations.ESRI,     "Hotine_Oblique_Mercator_Azimuth_Natural_Origin"),
                new NamedIdentifier(Citations.ESRI,     "Rectified_Skew_Orthomorphic_Natural_Origin"),
                new NamedIdentifier(Citations.GEOTOOLS, NAME)
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                LONGITUDE_OF_CENTRE, LATITUDE_OF_CENTRE,
                AZIMUTH,             RECTIFIED_GRID_ANGLE,
                SCALE_FACTOR,
                FALSE_EASTING,       FALSE_NORTHING
            });

        /**
         * Constructs a new provider.
         */
        public Provider() {
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
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            final Collection<GeneralParameterDescriptor> descriptors = PARAMETERS.descriptors();
            return new HotineObliqueMercator(parameters, descriptors, false);
        }
    }


    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for a {@linkplain HotineObliqueMercator Hotine Oblique Mercator} projection,
     * specified with two points on the central line (instead of a central point and azimuth).
     *
     * @since 2.4
     * @version $Id$
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static final class Provider_TwoPoint extends ObliqueMercator.Provider_TwoPoint {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = -3104452416276842816L;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.ESRI,     "Hotine_Oblique_Mercator_Two_Point_Natural_Origin"),
                new NamedIdentifier(Citations.GEOTOOLS, NAME)
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                LAT_OF_1ST_POINT,    LONG_OF_1ST_POINT,
                LAT_OF_2ND_POINT,    LONG_OF_2ND_POINT,
                LATITUDE_OF_CENTRE,  SCALE_FACTOR,
                FALSE_EASTING,       FALSE_NORTHING
            });

        /**
         * Constructs a new provider.
         */
        public Provider_TwoPoint() {
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
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            final Collection<GeneralParameterDescriptor> descriptors = PARAMETERS.descriptors();
            return new HotineObliqueMercator(parameters, descriptors, true);
        }
    }
}
