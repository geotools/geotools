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

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.projection.MapProjection.AbstractProvider;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.MathTransform;

/**
 * Supports the popular visualisation projection used by Google, Microsoft, Yahoo, OSM and others
 * @author Andrea Aime - OpenGeo
 *
 *
 * @source $URL$
 */
public class MercatorPseudoProvider extends AbstractProvider {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 118002069939741891L;

    /**
     * The parameters group.
     */
    static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
            new NamedIdentifier(Citations.EPSG, "Popular Visualisation Pseudo Mercator"),  
            new NamedIdentifier (Citations.EPSG, "1024"),
            new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.formatInternational(
                    VocabularyKeys.CYLINDRICAL_MERCATOR_PROJECTION))
        }, new ParameterDescriptor[] {
            SEMI_MAJOR, SEMI_MINOR,
            LATITUDE_OF_ORIGIN, CENTRAL_MERIDIAN, SCALE_FACTOR,
            FALSE_EASTING, FALSE_NORTHING
        });

    /**
     * Constructs a new provider.
     */
    public MercatorPseudoProvider() {
        super(PARAMETERS);
    }

    /**
     * Returns the operation type for this map projection.
     */
    @Override
    public Class<CylindricalProjection> getOperationType() {
        return CylindricalProjection.class;
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
     // make sure we assume a spherical reference
        parameters.parameter("semi_minor").setValue(parameters.parameter("semi_major").getValue());
        return new Spherical(parameters);
    }
    
    /**
     * Just like the {@link Mercator1SP.Spherical} but returning the proper parameter for the 
     * pseudo mercartor case 
     */
    private static final class Spherical extends Mercator.Spherical {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = -7583892502939355783L;

        /**
         * Constructs a new map projection from the suplied parameters.
         *
         * @param  parameters The parameter values in standard units.
         * @throws ParameterNotFoundException if a mandatory parameter is missing.
         */
        protected Spherical(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            super(parameters);
        }

        /**
         * {@inheritDoc}
         */
        public ParameterDescriptorGroup getParameterDescriptors() {
            return MercatorPseudoProvider.PARAMETERS;
        }
        
		@Override
		protected double getToleranceForAssertions(double longitude,
				double latitude) {
			final double delta = abs(longitude - centralMeridian) / 2
					+ abs(latitude - latitudeOfOrigin);
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
