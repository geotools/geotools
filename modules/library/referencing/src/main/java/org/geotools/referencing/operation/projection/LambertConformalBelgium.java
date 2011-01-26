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

import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.ConicProjection;
import org.opengis.referencing.operation.MathTransform;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.i18n.Vocabulary;


/**
 * Lambert Conical Conformal 2SP Belgium Projection.
 *
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/lambert_conic_conformal_2sp_belgium.html">lambert_conic_conformal_2sp_belgium</A>
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Rueben Schulz
 * @author Martin Desruisseaux
 */
public class LambertConformalBelgium extends LambertConformal {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -3441696724046319189L;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected LambertConformalBelgium(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        super(parameters, true);
    }

    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
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
     * provider} for a {@linkplain LambertConformalBelgium Lambert Conformal 2SP Belgium}
     * projection (EPSG code 9803).
     *
     * @since 2.2
     * @version $Id$
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
     public static final class Provider extends AbstractProvider {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = -6388030784088639876L;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Lambert_Conformal_Conic_2SP_Belgium"),
                new NamedIdentifier(Citations.EPSG,     "Lambert Conic Conformal (2SP Belgium)"),
                new NamedIdentifier(Citations.EPSG,     "9803"),
                new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.formatInternational(
                                                        VocabularyKeys.LAMBERT_CONFORMAL_PROJECTION))
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                CENTRAL_MERIDIAN,    LATITUDE_OF_ORIGIN,
                STANDARD_PARALLEL_1, STANDARD_PARALLEL_2,
                FALSE_EASTING,       FALSE_NORTHING
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
        public Class<ConicProjection> getOperationType() {
            return ConicProjection.class;
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
            return new LambertConformalBelgium(parameters);
        }
    }
}
