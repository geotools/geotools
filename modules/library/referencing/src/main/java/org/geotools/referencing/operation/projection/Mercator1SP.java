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
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.MathTransform;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.i18n.Vocabulary;


/**
 * Mercator Cylindrical 1SP Projection.
 *
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/mercator_1sp.html">"Mercator 1SP" on RemoteSensing.org</A>
 *
 * @since 2.2
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Rueben Schulz
 */
public class Mercator1SP extends Mercator {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 8391549772210490073L;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected Mercator1SP(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        super(parameters);
    }

    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }


    /**
     * Provides the transform equations for the spherical case of the Mercator projection.
     *
     * @version $Id$
     * @author Martin Desruisseaux
     * @author Rueben Schulz
     */
    private static final class Spherical extends Mercator.Spherical {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = 1347778643385433516L;

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
            return Provider.PARAMETERS;
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
     * provider} for a {@linkplain Mercator1SP Mercator 1SP} projection (EPSG code 9804).
     *
     * @since 2.2
     * @version $Id$
     * @author Martin Desruisseaux
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = -5886510621481710072L;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Mercator_1SP"),
                new NamedIdentifier(Citations.EPSG,     "Mercator (1SP)"),
                new NamedIdentifier(Citations.EPSG,     "Mercator (1SP) (Spherical)"),
                new NamedIdentifier(Citations.EPSG,     "9804"),
                new NamedIdentifier(Citations.GEOTIFF,  "CT_Mercator"),
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
        public Provider() {
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
            if (isSpherical(parameters)) {
                return new Spherical(parameters);
            } else {
                return new Mercator1SP(parameters);
            }
        }
    }
}
