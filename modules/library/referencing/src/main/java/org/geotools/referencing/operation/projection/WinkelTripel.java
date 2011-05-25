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

import static java.lang.Math.*;

import java.awt.geom.Point2D;
import java.util.Collection;

import javax.measure.unit.NonSI;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;


/**
 * Winkel Tripel and Hammer Aitoff projection 
 * 
 * <b>References:</b>
 * <ul>
 *   <li>http://en.wikipedia.org/wiki/Winkel_tripel_projection</li>
 *   <li>http://en.wikipedia.org/wiki/Hammer_projection</li>
 * </ul>
 *
 * @see <A HREF="http://mathworld.wolfram.com/PolyconicProjection.html">Polyconic projection on MathWorld</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/polyconic.html">"Polyconic" on RemoteSensing.org</A>
 *
 * @since 2.6.3
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/referencing/src/main/java/org/geotools/referencing/operation/projection/WinkelTripel.java $
 * @author Andrea Aime
 */
public class WinkelTripel extends MapProjection {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -8643765000703074857L;
    
    private enum ProjectionMode {Winkel, Aitoff};
    
    /**
     * Cosine of the standard parallel
     * Used for calculations for the ellipsoid.
     */
    private final double cosphi1;
    
    private final ProjectionMode mode;
    
    private ParameterDescriptorGroup descriptors;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected WinkelTripel(ProjectionMode mode, final ParameterDescriptorGroup descriptors, final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters, descriptors.descriptors());
        this.descriptors = descriptors;
        invertible = false;
        
        //  Compute constants
        if(mode == ProjectionMode.Winkel) {
            final Collection<GeneralParameterDescriptor> expected = getParameterDescriptors().descriptors();
            final double phi1 = doubleValue(expected, WinkelProvider.STANDARD_PARALLEL_1, parameters);
            cosphi1 = cos(phi1);
        } else {
            cosphi1 = 0;
        }
        this.mode = mode;
    }
    
    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return descriptors;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
     * (units in radians) and stores the result in {@code ptDst} (linear distance
     * on a unit sphere).
     */
    protected Point2D transformNormalized(double lam, double phi, final Point2D ptDst)
            throws ProjectionException {
        double c, d;
        double x, y;

        if((d = acos(cos(phi) * cos(c = 0.5 * lam))) != 0) {/* basic Aitoff */
            x = 2. * d * cos(phi) * sin(c) * (y = 1. / sin(d));
            y *= d * sin(phi);
        } else {
            x = y = 0;
        }
        
        if(mode == ProjectionMode.Winkel) {
            x = (x + lam * cosphi1) * 0.5;
            y = (y + phi) * 0.5;
        }
        
        if(ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        } else {
            return new Point2D.Double(x, y);
        }
    }

    protected Point2D inverseTransformNormalized(double x, double y, final Point2D ptDst)
            throws ProjectionException
    {
        throw new UnsupportedOperationException("Cannot invert this transformation");
    }
    
    /**
     * Returns a hash value for this projection.
     */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(cosphi1);
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
            final WinkelTripel that = (WinkelTripel) object;
            return equals(this.cosphi1,  that.cosphi1);
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
     * provider} for the Winkle Tripel projection projection (not part of the EPSG database).
     *
     * @since 2.6.3
     * @author Andrea Aime
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class WinkelProvider extends AbstractProvider {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = -2484567298319140781L;
        
        /**
         * The operation parameter descriptor for the standard parallel 1 parameter value.
         * Valid values range is from -90 to 90°. Default value is 0.
         */
        public static final ParameterDescriptor STANDARD_PARALLEL_1 = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC,      "standard_parallel_1"),
                    new NamedIdentifier(Citations.EPSG,     "Latitude of 1st standard parallel"),
                    new NamedIdentifier(Citations.GEOTIFF,  "StdParallel1")
                },
                toDegrees(0.880689235), -90, 90, NonSI.DEGREE_ANGLE);

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.ESRI, "Winkel_Tripel"),
                new NamedIdentifier(Citations.GEOTOOLS, "Winkel Tripel")
            }, new ParameterDescriptor[] {
                SEMI_MAJOR, SEMI_MINOR, STANDARD_PARALLEL_1
            });

        /**
         * Constructs a new provider.
         */
        public WinkelProvider() {
            super(PARAMETERS);
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
            return new WinkelTripel(ProjectionMode.Winkel, PARAMETERS, parameters);
        }
    }
    
    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for the Aitoff projection (not part of the EPSG database).
     *
     * @since 2.7.0
     * @author Andrea Aime
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class AitoffProvider extends AbstractProvider {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = 1189973109778926762L;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.ESRI, "Aitoff"),
                new NamedIdentifier(Citations.GEOTOOLS, "Aitoff"),
            }, new ParameterDescriptor[] {
                SEMI_MAJOR, SEMI_MINOR
            });

        /**
         * Constructs a new provider.
         */
        public AitoffProvider() {
            super(PARAMETERS);
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
            return new WinkelTripel(ProjectionMode.Aitoff, PARAMETERS, parameters);
        }
    }
}
