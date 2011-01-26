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
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.ConicProjection;
import org.opengis.referencing.operation.MathTransform;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.referencing.operation.projection.MapProjection.AbstractProvider;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.util.Utilities;


/**
 * Lambert Conical Conformal 2SP Projection.
 *
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/lambert_conic_conformal_2sp.html">lambert_conic_conformal_2sp</A>
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Rueben Schulz
 */
public class LambertConformal2SP extends LambertConformal {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 7184350446186057405L;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected LambertConformal2SP(final ParameterValueGroup parameters)
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




    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                                 PROVIDERS                                ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for a {@linkplain LambertConformal2SP Lambert Conformal 2SP} projection (EPSG
     * code 9802).
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
        private static final long serialVersionUID = 3240860802816724947L;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Lambert_Conformal_Conic_2SP"),
                new NamedIdentifier(Citations.EPSG,     "Lambert Conic Conformal (2SP)"),
                new NamedIdentifier(Citations.ESRI,     "Lambert_Conformal_Conic"),
                new NamedIdentifier(Citations.ESRI,     "Lambert_Conformal_Conic_2SP"),
                new NamedIdentifier(Citations.EPSG,     "9802"),
                new NamedIdentifier(Citations.GEOTIFF,  "CT_LambertConfConic_2SP"),
                new NamedIdentifier(Citations.GEOTIFF,  "CT_LambertConfConic"),
                new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.formatInternational(
                                                        VocabularyKeys.LAMBERT_CONFORMAL_PROJECTION))
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                CENTRAL_MERIDIAN,    LATITUDE_OF_ORIGIN,
                STANDARD_PARALLEL_1, STANDARD_PARALLEL_2,
                FALSE_EASTING,       FALSE_NORTHING, 
                SCALE_FACTOR // This last parameter is for ESRI compatibility
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
            if(getParameter(STANDARD_PARALLEL_2, parameters) == null &&
               getParameter(STANDARD_PARALLEL_1, parameters) == null &&
               getParameter(LATITUDE_OF_ORIGIN, parameters) != null) {
                // handle the ESRI 1SP case
                return new LambertConformal1SP(parameters);
            } else if(Utilities.equals(doubleValue(STANDARD_PARALLEL_1, parameters),
                                doubleValue(STANDARD_PARALLEL_2, parameters)) &&
               Utilities.equals(doubleValue(STANDARD_PARALLEL_1, parameters),
                                doubleValue(LATITUDE_OF_ORIGIN, parameters))
                                ) {
                // handle the ESRI 1SP case
                return new LambertConformal1SP(parameters);
            } else if(getParameter(STANDARD_PARALLEL_2, parameters) == null &&
               Utilities.equals(doubleValue(STANDARD_PARALLEL_1, parameters),
                                doubleValue(LATITUDE_OF_ORIGIN, parameters))) {
                // handle the ESRI 1SP case
                return new LambertConformal1SP(parameters);
            } else {
                // switch sp1 and sp2 so that we get a consistent ordering, this allows to recognize
                // tow Lamber conformal with the same standard parallels declared in opposite order
                ParameterValue<Double> sp1 = getParameter(STANDARD_PARALLEL_1, parameters); 
                ParameterValue<Double> sp2 = getParameter(STANDARD_PARALLEL_2, parameters);
                if(sp1 != null && sp2 != null) {
                    if(sp1.doubleValue() < sp2.doubleValue()) {
                        final double temp = sp1.doubleValue();
                        sp1.setValue(sp2.doubleValue());
                        sp2.setValue(temp);
                    }
                }
                
                return new LambertConformal2SP(parameters);
            }
        }
    }
}
