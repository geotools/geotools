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

import java.util.Collection;
import java.awt.geom.Point2D;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.ReferenceIdentifier;
import org.geotools.math.Complex;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.metadata.iso.citation.Citations;

import static java.lang.Math.*;


/**
 * The NZMG (New Zealand Map Grid) projection.
 * <p>
 * This is an implementation of algorithm published by
 * <a href="http://www.govt.nz/record?recordid=28">Land Information New Zealand</a>.
 * The algorithm is documented <a href="http://www.linz.govt.nz/rcs/linz/6137/">here</a>.
 * <p>
 * <b>Implementation note</b><br>
 * This class make extensive use of {@link Complex} type which may be costly unless the compiler
 * can inline on the stack. We assume that Jave 6 and above can do this optimization.
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Justin Deoliveira
 * @author Martin Desruisseaux
 */
public class NewZealandMapGrid extends MapProjection {
    /**
     * For compatibility with different versions during deserialization.
     */
    private static final long serialVersionUID = 8394817836243729133L;

    /**
     * Coefficients for forward and inverse projection.
     */
    private static final Complex[] A = {
            new Complex(  0.7557853228,  0.0         ),
            new Complex(  0.249204646,   0.003371507 ),
            new Complex( -0.001541739,   0.041058560 ),
            new Complex( -0.10162907,    0.01727609  ),
            new Complex( -0.26623489,   -0.36249218  ),
            new Complex( -0.6870983,    -1.1651967   )
        };

    /**
     * Coefficients for inverse projection.
     */
    private static final Complex[] B = {
            new Complex(  1.3231270439,   0.0         ),
            new Complex( -0.577245789,   -0.007809598 ),
            new Complex(  0.508307513,   -0.112208952 ),
            new Complex( -0.15094762,     0.18200602  ),
            new Complex(  1.01418179,     1.64497696  ),
            new Complex(  1.9660549,      2.5127645   )
        };

    /**
     * Coefficients for inverse projection.
     */
    private static final double[] TPHI = new double[] {
            1.5627014243, 0.5185406398, -0.03333098, -0.1052906, -0.0368594, 0.007317,
            0.01220, 0.00394, -0.0013
        };

    /**
     * Coefficients for forward projection.
     */
    private static final double[] TPSI = new double[] {
            0.6399175073, -0.1358797613, 0.063294409, -0.02526853, 0.0117879,
            -0.0055161, 0.0026906, -0.001333, 0.00067, -0.00034
        };

    /**
     * Constructs a new map projection with default parameter values.
     */
    protected NewZealandMapGrid() {
        this(Provider.PARAMETERS.createValue());
    }

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected NewZealandMapGrid(final ParameterValueGroup parameters)
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
     * Must be overridden because {@link Provider} uses instances of
     * {@link ModifiedParameterDescriptor}. This hack was needed because the New Zeland map
     * projection uses particular default values for parameters like "False Easting", etc.
     */
    @Override
    final boolean isExpectedParameter(final Collection<GeneralParameterDescriptor> expected,
                                      final ParameterDescriptor param)
    {
        return ModifiedParameterDescriptor.contains(expected, param);
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
     * (units in radians) and stores the result in {@code ptDst} (linear distance
     * on a unit sphere).
     */
    protected Point2D transformNormalized(final double x, final double y,
                                          final Point2D ptDst)
            throws ProjectionException
    {
        final double dphi = (y - latitudeOfOrigin) * (180/PI * 3600E-5);
        double dphi_pow_i = dphi;
        double dpsi       = 0;
        for (int i=0; i<TPSI.length; i++) {
            dpsi += (TPSI[i] * dphi_pow_i);
            dphi_pow_i *= dphi;
        }
        // See implementation note in class javadoc.
        final Complex theta = new Complex(dpsi, x);
        final Complex power = new Complex(theta);
        final Complex z     = new Complex();
        z.multiply(A[0], power);
        for (int i=1; i<A.length; i++) {
            power.multiply(power, theta);
            z.addMultiply(z, A[i], power);
        }
        if (ptDst != null) {
            ptDst.setLocation(z.imag, z.real);
            return ptDst;
        }
        return new Point2D.Double(z.imag, z.real);
    }

    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinates
     * and stores the result in {@code ptDst}.
     */
    protected Point2D inverseTransformNormalized(final double x, final double y,
                                                 final Point2D ptDst)
            throws ProjectionException
    {
        // See implementation note in class javadoc.
        final Complex z = new Complex(y, x);
        final Complex power = new Complex(z);
        final Complex theta = new Complex();
        theta.multiply(B[0], z);
        for (int j=1; j<B.length; j++) {
            power.multiply(power, z);
            theta.addMultiply(theta, B[j], power);
        }
        // Increasing the number of iterations through this loop decreases
        // the error in the calculation, but 3 iterations gives 10-3 accuracy.
        final Complex num   = new Complex();
        final Complex denom = new Complex();
        final Complex t     = new Complex();
        for (int j=0; j<3; j++) {
            power.power(theta, 2);
            num.addMultiply(z, A[1], power);
            for (int k=2; k<A.length; k++) {
                power.multiply(power, theta);
                t.multiply(A[k], power);
                t.multiply(t, k);
                num.add(num, t);
            }
            power.real = 1;
            power.imag = 0;
            denom.copy(A[0]);
            for (int k=1; k<A.length; k++) {
                power.multiply(power, theta);
                t.multiply(A[k], power);
                t.multiply(t, k+1);
                denom.add(denom, t);
            }
            theta.divide(num, denom);
        }
        final double dpsi = theta.real;
        double dpsi_pow_i = dpsi;
        double dphi = TPHI[0] * dpsi;
        for (int i=1; i<TPHI.length; i++) {
            dpsi_pow_i *= dpsi;
            dphi += (TPHI[i] * dpsi_pow_i);
        }
        dphi = dphi / (180/PI * 3600E-5) + latitudeOfOrigin;
        if (ptDst != null) {
            ptDst.setLocation(theta.imag, dphi);
            return ptDst;
        }
        return new Point2D.Double(theta.imag, dphi);
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
     * provider} for {@linkplain NewZealandMapGrid New Zealand Map Grid} (EPSG code 27200).
     *
     * @since 2.2
     * @version $Id$
     * @author Justin Deoliveira
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /**
         * For compatibility with different versions during deserialization.
         */
        private static final long serialVersionUID = -7716733400419275656L;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new ReferenceIdentifier[] {
                    new NamedIdentifier(Citations.OGC,  "New_Zealand_Map_Grid"),
                    new NamedIdentifier(Citations.EPSG, "New Zealand Map Grid"),
                    new NamedIdentifier(Citations.EPSG, "27200")
                },
                new ParameterDescriptor[] {
                    new ModifiedParameterDescriptor(SEMI_MAJOR,         6378388.0),
                    new ModifiedParameterDescriptor(SEMI_MINOR,         6378388.0*(1-1/297.0)),
                    new ModifiedParameterDescriptor(LATITUDE_OF_ORIGIN,     -41.0),
                    new ModifiedParameterDescriptor(CENTRAL_MERIDIAN,       173.0),
                    new ModifiedParameterDescriptor(FALSE_EASTING,      2510000.0),
                    new ModifiedParameterDescriptor(FALSE_NORTHING,     6023150.0)
                });

        /**
         * Constructs a new provider.
         */
        public Provider() {
            super(PARAMETERS);
        }

        /**
         * Creates a transform from the specified group of parameter values. This method doesn't
         * check for the spherical case, since the New Zealand Map Grid projection is used with
         * the International 1924 ellipsoid.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            return new NewZealandMapGrid(parameters);
        }
    }
}
