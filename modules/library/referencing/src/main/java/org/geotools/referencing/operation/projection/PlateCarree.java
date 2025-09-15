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

import java.awt.geom.Point2D;
import java.io.Serial;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.CylindricalProjection;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;

/**
 * Plate Carree (or Equirectangular) projection. This is a particular case of {@linkplain EquidistantCylindrical
 * Equidistant Cylindrical} projection where the {@code standard_parallel_1} is 0°.
 *
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/equirectangular.html">"Equirectangular" on
 *     RemoteSensing.org</A>
 * @since 2.2
 * @version $Id$
 * @author John Grange
 * @author Martin Desruisseaux
 */
public class PlateCarree extends EquidistantCylindrical {
    /** For compatibility with different versions during deserialization. */
    @Serial
    private static final long serialVersionUID = -6041146276958636165L;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected PlateCarree(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);
    }

    /** {@inheritDoc} */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates (units in radians) and stores the
     * result in {@code ptDst} (linear distance on a unit sphere).
     */
    @Override
    protected Point2D transformNormalized(double x, double y, final Point2D ptDst) throws ProjectionException {
        if (ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        }
        return new Point2D.Double(x, y);
    }

    /** Transforms the specified (<var>x</var>,<var>y</var>) coordinates and stores the result in {@code ptDst}. */
    @Override
    protected Point2D inverseTransformNormalized(double x, double y, final Point2D ptDst) throws ProjectionException {
        if (ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        }
        return new Point2D.Double(x, y);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                                 PROVIDERS                                ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform provider} for an
     * {@linkplain org.geotools.referencing.operation.projection.PlateCarree Plate Carree} projection.
     *
     * @since 2.2
     * @version $Id$
     * @author John Grange
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /** For compatibility with different versions during deserialization. */
        @Serial
        private static final long serialVersionUID = 8535645757318203345L;

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.ESRI, "Plate_Carree"),
                    new NamedIdentifier(Citations.OGC, "Equirectangular"),
                    new NamedIdentifier(Citations.GEOTIFF, "CT_Equirectangular"),
                    new NamedIdentifier(Citations.PROJ, "eqc")
                },
                new ParameterDescriptor[] {SEMI_MAJOR, SEMI_MINOR, CENTRAL_MERIDIAN, FALSE_EASTING, FALSE_NORTHING});

        /** Constructs a new provider. */
        public Provider() {
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
                throws ParameterNotFoundException, FactoryException {
            if (isSpherical(parameters)) {
                return new PlateCarree(parameters);
            } else {
                throw new FactoryException(ErrorKeys.ELLIPTICAL_NOT_SUPPORTED);
            }
        }
    }
}
