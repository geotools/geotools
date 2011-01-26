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
import static java.lang.Math.cos;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.logging.Level;

import javax.measure.unit.NonSI;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.MathTransform;


/**
 * Equidistant cylindrical projection (EPSG code 9823).  In the particular case
 * where the {@code standard_parallel_1} is 0°, this projection is also called
 * {@linkplain PlateCarree Plate Carree} or Equirectangular. This is used in,
 * for example, <cite>WGS84 / Plate Carree</cite> (EPSG:32662).
 * <p>
 * <b>References:</b>
 * <ul>
 *   <li>John P. Snyder (Map Projections - A Working Manual,<br>
 *       U.S. Geological Survey Professional Paper 1395, 1987)</li>
 *   <li>"Coordinate Conversions and Transformations including Formulas",<br>
 *       EPSG Guidence Note Number 7 part 2, Version 24.</li>
 * </ul>
 *
 * @see <A HREF="http://mathworld.wolfram.com/CylindricalEquidistantProjection.html">Cylindrical Equidistant projection on MathWorld</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/equirectangular.html">"Equirectangular" on RemoteSensing.org</A>
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author John Grange
 * @author Martin Desruisseaux
 */
public class EquidistantCylindrical extends MapProjection {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -848975059471102069L;

    /**
     * Cosinus of the {@code "standard_parallel_1"} parameter.
     */
    private final double cosStandardParallel;

    /**
     * {@linkplain Provider#STANDARD_PARALLEL_1 Standard parallel} parameter.
     * Set to 0° for the {@link PlateCarree} case.
     */
    protected final double standardParallel;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected EquidistantCylindrical(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        // Fetch parameters
        super(parameters);
        final Collection<GeneralParameterDescriptor> expected = getParameterDescriptors().descriptors();
        if (expected.contains(Provider.STANDARD_PARALLEL_1)) {
            standardParallel = abs(doubleValue(expected, Provider.STANDARD_PARALLEL_1, parameters));
            ensureLatitudeInRange(Provider.STANDARD_PARALLEL_1, standardParallel, false);
            cosStandardParallel = cos(standardParallel);
        } else {
            // standard parallel is the equator (Plate Carree or Equirectangular)
            standardParallel = 0;
            cosStandardParallel = 1.0;
        }
        assert latitudeOfOrigin == 0 : latitudeOfOrigin;
    }

    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        if (!Double.isNaN(standardParallel)) {
            final Collection<GeneralParameterDescriptor> expected = getParameterDescriptors().descriptors();
            set(expected, Provider.STANDARD_PARALLEL_1, values, standardParallel);
        }
        return values;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
     * (units in radians) and stores the result in {@code ptDst} (linear distance
     * on a unit sphere).
     *
     * @param x The longitude of the coordinate, in <strong>radians</strong>.
     * @param y The  latitude of the coordinate, in <strong>radians</strong>.
     */
    protected Point2D transformNormalized(double x, double y, final Point2D ptDst)
            throws ProjectionException
    {
        x *= cosStandardParallel;
        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }

    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinates
     * and stores the result in {@code ptDst}.
     */
    protected Point2D inverseTransformNormalized(double x, double y, final Point2D ptDst)
            throws ProjectionException
    {
        x /= cosStandardParallel;
        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }

    /**
     * Returns a hash value for this projection.
     */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(standardParallel);
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
            final EquidistantCylindrical that = (EquidistantCylindrical) object;
            return equals(this.standardParallel,  that.standardParallel);
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
     * provider} for an {@linkplain EquidistantCylindrical Equidistant Cylindrical} projection
     * (EPSG code 9823).
     *
     * @since 2.2
     * @version $Id$
     * @author John Grange
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = -278288251842178001L;

        /**
         * The parameters group. Note the EPSG includes a "Latitude of natural origin" parameter instead
         * of "standard_parallel_1". I have sided with ESRI and Snyder in this case.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Equidistant_Cylindrical"),
                new NamedIdentifier(Citations.EPSG,     "Equidistant Cylindrical"),
                new NamedIdentifier(Citations.ESRI,     "Equidistant_Cylindrical"),
                new NamedIdentifier(Citations.EPSG,     "9823"),
                new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.formatInternational(
                                    VocabularyKeys.EQUIDISTANT_CYLINDRICAL_PROJECTION))
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,       SEMI_MINOR,
                CENTRAL_MERIDIAN, LATITUDE_OF_ORIGIN, STANDARD_PARALLEL_1,
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
        public Class<CylindricalProjection> getOperationType() {
            return CylindricalProjection.class;
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         * @throws FactoryException if the projection can not be created.
         */
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException, FactoryException
        {
            if (!isSpherical(parameters)) {
                LOGGER.log(Level.FINE, "GeoTools EquidistantCylindrical is defined only on the sphere, " +
                		"we're going to use spherical equations even if the projection is using an ellipsoid");
            }
            return new EquidistantCylindrical(parameters);
        }
    }
    
    
    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for an {@linkplain EquidistantCylindrical Equidistant Cylindrical} projection,
     * spherical case
     *
     * @since 2.6
     * @version $Id$
     * @author John Grange
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class SphericalProvider extends AbstractProvider {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = 8929981563074475828L;
        
        /**
         * The parameters group. Note the EPSG includes a "Latitude of natural origin" parameter instead
         * of "standard_parallel_1". I have sided with ESRI and Snyder in this case.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.EPSG,     "Equidistant Cylindrical (Spherical)"),
                new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.formatInternational(
                                    VocabularyKeys.EQUIDISTANT_CYLINDRICAL_PROJECTION))
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,       SEMI_MINOR,
                CENTRAL_MERIDIAN, LATITUDE_OF_ORIGIN, STANDARD_PARALLEL_1,
                FALSE_EASTING,    FALSE_NORTHING
            });

        /**
         * Constructs a new provider.
         */
        public SphericalProvider() {
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
         * @throws FactoryException if the projection can not be created.
         */
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException, FactoryException
        {
            return new EquidistantCylindrical(parameters) {
                @Override
                public ParameterDescriptorGroup getParameterDescriptors() {
                    return SphericalProvider.PARAMETERS;
                }
            };
        }
    }
}
