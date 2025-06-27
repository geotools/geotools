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
 */
package org.geotools.referencing.operation.projection;

import static java.lang.Math.abs;

import java.util.Collection;
import org.geotools.api.parameter.GeneralParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.operation.CylindricalProjection;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.projection.MapProjection.AbstractProvider;
import si.uom.NonSI;

/**
 * Supports the popular visualisation projection used by Google, Microsoft, Yahoo, OSM and others
 *
 * @author Andrea Aime - OpenGeo
 */
public class MercatorPseudoProvider extends AbstractProvider {
    /** For cross-version compatibility. */
    private static final long serialVersionUID = 118002069939741891L;

    /**
     * The Auxiliary Sphere Type parameter accepts:
     *
     * <ul>
     *   <li>0 (use semimajor axis or radius of the geographic coordinate system)
     *   <li>
     *   <li>1 (use semiminor axis or radius)
     *   <li>2 (calculate and use authalic radius), or 3 (use authalic radius and convert geodetic latitudes to authalic
     *       latitudes).
     * </ul>
     *
     * Geotools only supports 0 for the moment
     */
    public static final ParameterDescriptor AUXILIARY_SPHERE_TYPE = createDescriptor(
            new NamedIdentifier[] {
                new NamedIdentifier(Citations.ESRI, "Auxiliary_sphere_type"),
            },
            0,
            0,
            0,
            null);

    public static final ParameterDescriptor FAKE_ESRI_STANDARD_PARALLELL = createDescriptor(
            new NamedIdentifier[] {
                new NamedIdentifier(Citations.ESRI, "Standard_parallel_1"),
            },
            0,
            0,
            0,
            NonSI.DEGREE_ANGLE);

    /** The parameters group. */
    static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
            new NamedIdentifier[] {
                new NamedIdentifier(Citations.EPSG, "Popular Visualisation Pseudo Mercator"),
                new NamedIdentifier(Citations.EPSG, "1024"),
                new NamedIdentifier(Citations.ESRI, "Mercator_Auxiliary_Sphere"),
                new NamedIdentifier(
                        Citations.GEOTOOLS,
                        Vocabulary.formatInternational(VocabularyKeys.CYLINDRICAL_MERCATOR_PROJECTION)),
                new NamedIdentifier(Citations.PROJ, "merc")
            },
            new ParameterDescriptor[] {
                SEMI_MAJOR,
                SEMI_MINOR,
                LATITUDE_OF_ORIGIN,
                CENTRAL_MERIDIAN,
                SCALE_FACTOR,
                FALSE_EASTING,
                FALSE_NORTHING,
                // these two added for ESRI compatibility
                FAKE_ESRI_STANDARD_PARALLELL,
                AUXILIARY_SPHERE_TYPE
            });

    static final ParameterDescriptorGroup BASE_PARAMETERS = createDescriptorGroup(
            new NamedIdentifier[] {
                new NamedIdentifier(Citations.EPSG, "Popular Visualisation Pseudo Mercator"),
                new NamedIdentifier(Citations.EPSG, "1024"),
                new NamedIdentifier(
                        Citations.GEOTOOLS,
                        Vocabulary.formatInternational(VocabularyKeys.CYLINDRICAL_MERCATOR_PROJECTION))
            },
            new ParameterDescriptor[] {
                SEMI_MAJOR,
                SEMI_MINOR,
                LATITUDE_OF_ORIGIN,
                CENTRAL_MERIDIAN,
                SCALE_FACTOR,
                FALSE_EASTING,
                FALSE_NORTHING,
            });

    /** Constructs a new provider. */
    public MercatorPseudoProvider() {
        super(PARAMETERS);
    }

    /** Returns the operation type for this map projection. */
    @Override
    public Class<CylindricalProjection> getOperationType() {
        return CylindricalProjection.class;
    }

    /**
     * Creates a transform from the specified group of parameter values.
     *
     * @param parameters The group of parameter values.
     * @return The created math transform.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    @Override
    protected MathTransform createMathTransform(final ParameterValueGroup parameters)
            throws ParameterNotFoundException {
        // make sure we assume a spherical reference
        parameters
                .parameter("semi_minor")
                .setValue(parameters.parameter("semi_major").getValue());
        return new Spherical(parameters);
    }

    /** Just like the {@link Mercator1SP.Spherical} but returning the proper parameter for the pseudo mercartor case */
    private static final class Spherical extends Mercator.Spherical {
        /** For cross-version compatibility. */
        private static final long serialVersionUID = -7583892502939355783L;

        /**
         * Constructs a new map projection from the suplied parameters.
         *
         * @param parameters The parameter values in standard units.
         * @throws ParameterNotFoundException if a mandatory parameter is missing.
         */
        Spherical(final ParameterValueGroup parameters) throws ParameterNotFoundException {
            super(parameters);
        }

        /** Override to discard ESRI extra parameters, they are adding un-welcomed shift in the reprojection results */
        @Override
        public ParameterValueGroup getParameterValues() {
            final ParameterDescriptorGroup descriptor = BASE_PARAMETERS;
            final Collection<GeneralParameterDescriptor> expected = descriptor.descriptors();
            final ParameterValueGroup values = descriptor.createValue();
            set(expected, AbstractProvider.SEMI_MAJOR, values, semiMajor);
            set(expected, AbstractProvider.SEMI_MINOR, values, semiMinor);
            set(expected, AbstractProvider.CENTRAL_MERIDIAN, values, centralMeridian);
            set(expected, AbstractProvider.LATITUDE_OF_ORIGIN, values, latitudeOfOrigin);
            set(expected, AbstractProvider.SCALE_FACTOR, values, scaleFactor);
            set(expected, AbstractProvider.FALSE_EASTING, values, falseEasting);
            set(expected, AbstractProvider.FALSE_NORTHING, values, falseNorthing);
            if (!Double.isNaN(standardParallel)) {
                set(expected, AbstractProvider.STANDARD_PARALLEL_1, values, standardParallel);
            }
            return values;
        }

        /** {@inheritDoc} */
        @Override
        public ParameterDescriptorGroup getParameterDescriptors() {
            return MercatorPseudoProvider.PARAMETERS;
        }

        @Override
        protected double getToleranceForAssertions(double longitude, double latitude) {
            final double delta = abs(longitude - centralMeridian) / 2 + abs(latitude - latitudeOfOrigin);
            if (delta > 40) {
                // When far from the valid area, use a larger tolerance.
                return 1;
            } else {
                // this projection forte is not exactly precision
                return 0.1;
            }
        }
    }
}
