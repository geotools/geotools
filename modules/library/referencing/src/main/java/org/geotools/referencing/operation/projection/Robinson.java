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

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;


/**
 * Robinson projection 
 *
 * @see <A HREF="http://en.wikipedia.org/wiki/Robinson_projection">Robinson projection on Wikipedia</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/robinson.html">"Robinson" on RemoteSensing.org</A>
 *
 * @since 2.6.3
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.6.x/modules/library/referencing/src/main/java/org/geotools/referencing/operation/projection/Mercator.java $
 * @author Andrea Aime
 */
public class Robinson extends MapProjection {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 8428056162968814860L;
    
    static final class Coeff {
        double c0, c1, c2, c3;

        public Coeff(double c0, double c1, double c2, double c3) {
            super();
            this.c0 = c0;
            this.c1 = c1;
            this.c2 = c2;
            this.c3 = c3;
        }
        
        public Coeff(Coeff other) {
            super();
            this.c0 = other.c0;
            this.c1 = other.c1;
            this.c2 = other.c2;
            this.c3 = other.c3;
        }
        
        public final double forward(double z) {
            return c0 + z * (c1 + z * (c2 + z * c3));
        }
        
        public final double inverse(double z) {
            return c1 + z * (c2 + c2 + z * 3d * c3);
        }
    }
    
    /**
     * X translation table
     */
    private final static Coeff X[] = {
        new Coeff(1,  -5.67239e-12,   -7.15511e-05,   3.11028e-06),
        new Coeff(0.9986, -0.000482241,   -2.4897e-05,    -1.33094e-06),
        new Coeff(0.9954, -0.000831031,   -4.4861e-05,    -9.86588e-07),
        new Coeff(0.99,   -0.00135363,    -5.96598e-05,   3.67749e-06),
        new Coeff(0.9822, -0.00167442,    -4.4975e-06,    -5.72394e-06),
        new Coeff(0.973,  -0.00214869,    -9.03565e-05,   1.88767e-08),
        new Coeff(0.96,   -0.00305084,    -9.00732e-05,   1.64869e-06),
        new Coeff(0.9427, -0.00382792,    -6.53428e-05,   -2.61493e-06),
        new Coeff(0.9216, -0.00467747,    -0.000104566,   4.8122e-06),
        new Coeff(0.8962, -0.00536222,    -3.23834e-05,   -5.43445e-06),
        new Coeff(0.8679, -0.00609364,    -0.0001139, 3.32521e-06),
        new Coeff(0.835,  -0.00698325,    -6.40219e-05,   9.34582e-07),
        new Coeff(0.7986, -0.00755337,    -5.00038e-05,   9.35532e-07),
        new Coeff(0.7597, -0.00798325,    -3.59716e-05,   -2.27604e-06),
        new Coeff(0.7186, -0.00851366,    -7.0112e-05,    -8.63072e-06),
        new Coeff(0.6732, -0.00986209,    -0.000199572,   1.91978e-05),
        new Coeff(0.6213, -0.010418,  8.83948e-05,    6.24031e-06),
        new Coeff(0.5722, -0.00906601,    0.000181999,    6.24033e-06),
        new Coeff(0.5322, 0., 0., 0.)
    };

    /**
     * Y translation table
     */
    private final static Coeff Y[] = {
        new Coeff(0,  0.0124, 3.72529e-10,    1.15484e-09),
        new Coeff(0.062,  0.0124001,  1.76951e-08,    -5.92321e-09),
        new Coeff(0.124,  0.0123998,  -7.09668e-08,   2.25753e-08),
        new Coeff(0.186,  0.0124008,  2.66917e-07,    -8.44523e-08),
        new Coeff(0.248,  0.0123971,  -9.99682e-07,   3.15569e-07),
        new Coeff(0.31,   0.0124108,  3.73349e-06,    -1.1779e-06),
        new Coeff(0.372,  0.0123598,  -1.3935e-05,    4.39588e-06),
        new Coeff(0.434,  0.0125501,  5.20034e-05,    -1.00051e-05),
        new Coeff(0.4968, 0.0123198,  -9.80735e-05,   9.22397e-06),
        new Coeff(0.5571, 0.0120308,  4.02857e-05,    -5.2901e-06),
        new Coeff(0.6176, 0.0120369,  -3.90662e-05,   7.36117e-07),
        new Coeff(0.6769, 0.0117015,  -2.80246e-05,   -8.54283e-07),
        new Coeff(0.7346, 0.0113572,  -4.08389e-05,   -5.18524e-07),
        new Coeff(0.7903, 0.0109099,  -4.86169e-05,   -1.0718e-06),
        new Coeff(0.8435, 0.0103433,  -6.46934e-05,   5.36384e-09),
        new Coeff(0.8936, 0.00969679, -6.46129e-05,   -8.54894e-06),
        new Coeff(0.9394, 0.00840949, -0.000192847,   -4.21023e-06),
        new Coeff(0.9761, 0.00616525, -0.000256001,   -4.21021e-06),
        new Coeff(1., 0., 0., 0)
    };

    private final static double FXC = 0.8487;
    private final static double FYC = 1.3523;
    private final static double C1 = 11.45915590261646417544;
    private final static double RC1 = 0.08726646259971647884;
    private final int NODES = 18;
    private final static double ONEEPS = 1.000001;
    private final static double EPS = 1e-12;
    

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected Robinson(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);
    }
    
    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
     * (units in radians) and stores the result in {@code ptDst} (linear distance
     * on a unit sphere).
     */
    protected Point2D transformNormalized(double lam, double phi, final Point2D ptDst)
            throws ProjectionException {

        double dphi = abs(phi);
        int i = (int) floor(dphi * C1);
        if (i >= NODES) {
            i = NODES - 1;
        }
        dphi = toDegrees(dphi - RC1 * i);
        double x = X[i].forward(dphi) * FXC * lam;
        double y = Y[i].forward(dphi) * FYC;
        if (phi < 0d) {
            y = -y;
        }
        
        if(ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        } else {
            return new Point2D.Double(x, y);
        }
    }

    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinates
     * and stores the result in {@code ptDst}.
     */
    protected Point2D inverseTransformNormalized(double x, double y, final Point2D ptDst)
            throws ProjectionException {
        double lam = x / FXC;
        double phi = abs(y / FYC);
        if (phi >= 1d) { /* simple pathologic cases */
            if (phi > ONEEPS) {
                phi = y < 0. ? -PI / 2 : PI / 2;
                lam /= X[NODES].c0;
            }
        } else { /* general problem */
            /* in Y space, reduce to table interval */
            int i;
            for (i = (int) floor(phi * NODES);;) {
                if (Y[i].c0 > phi) {
                    --i;
                } else if (Y[i+1].c0 <= phi) {
                    ++i;
                } else {
                    break;
                }
            }
            Coeff T = new Coeff(Y[i]);
            /* first guess, linear interp */
            double t = 5d * (phi - T.c0) / (Y[i+1].c0 - T.c0);
            /* make into root */
            T.c0 -= phi;
            for (;;) { /* Newton-Raphson reduction */
                double t1 = T.forward(t) / T.inverse(t);
                t -= t1;
                if (abs(t1) < EPS) {
                    break;
                }
            }
            phi = toRadians(5 * i + t);
            if (y < 0d) {
                phi = -phi;
            }
            lam /= X[i].forward(t);
        }
        
        if(ptDst != null) {
            ptDst.setLocation(lam, phi);
            return ptDst;
        } else {
            return new Point2D.Double(lam, phi);
        }
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
        return super.equals(object);
    }
    
    @Override
    protected double getToleranceForAssertions(double longitude, double latitude) {
        // the Robinson projection is meant for world-wide displays, don't be picky
        return 2;
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
     * provider} for the Robinson projection (not part of the EPSG database).
     *
     * @since 2.6.3
     * @author Andrea Aime
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = 3586488124601927036L;
        
        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.GEOTOOLS, "Robinson"),
                new NamedIdentifier(Citations.ESRI, "Robinson")
            }, new ParameterDescriptor[] {
                SEMI_MAJOR, SEMI_MINOR, CENTRAL_MERIDIAN
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
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            return new Robinson(parameters);
        }
    }
    
//    public static void main(String[] args) {
//        double px[] = {
//                1,  -5.67239e-12,   -7.15511e-05,   3.11028e-06,
//                0.9986, -0.000482241,   -2.4897e-05,    -1.33094e-06,
//                0.9954, -0.000831031,   -4.4861e-05,    -9.86588e-07,
//                0.99,   -0.00135363,    -5.96598e-05,   3.67749e-06,
//                0.9822, -0.00167442,    -4.4975e-06,    -5.72394e-06,
//                0.973,  -0.00214869,    -9.03565e-05,   1.88767e-08,
//                0.96,   -0.00305084,    -9.00732e-05,   1.64869e-06,
//                0.9427, -0.00382792,    -6.53428e-05,   -2.61493e-06,
//                0.9216, -0.00467747,    -0.000104566,   4.8122e-06,
//                0.8962, -0.00536222,    -3.23834e-05,   -5.43445e-06,
//                0.8679, -0.00609364,    -0.0001139, 3.32521e-06,
//                0.835,  -0.00698325,    -6.40219e-05,   9.34582e-07,
//                0.7986, -0.00755337,    -5.00038e-05,   9.35532e-07,
//                0.7597, -0.00798325,    -3.59716e-05,   -2.27604e-06,
//                0.7186, -0.00851366,    -7.0112e-05,    -8.63072e-06,
//                0.6732, -0.00986209,    -0.000199572,   1.91978e-05,
//                0.6213, -0.010418,  8.83948e-05,    6.24031e-06,
//                0.5722, -0.00906601,    0.000181999,    6.24033e-06,
//                0.5322, 0.,0.,0.  };
//
//
//
//
//
//                double py[] = {
//                0,  0.0124, 3.72529e-10,    1.15484e-09,
//                0.062,  0.0124001,  1.76951e-08,    -5.92321e-09,
//                0.124,  0.0123998,  -7.09668e-08,   2.25753e-08,
//                0.186,  0.0124008,  2.66917e-07,    -8.44523e-08,
//                0.248,  0.0123971,  -9.99682e-07,   3.15569e-07,
//                0.31,   0.0124108,  3.73349e-06,    -1.1779e-06,
//                0.372,  0.0123598,  -1.3935e-05,    4.39588e-06,
//                0.434,  0.0125501,  5.20034e-05,    -1.00051e-05,
//                0.4968, 0.0123198,  -9.80735e-05,   9.22397e-06,
//                0.5571, 0.0120308,  4.02857e-05,    -5.2901e-06,
//                0.6176, 0.0120369,  -3.90662e-05,   7.36117e-07,
//                0.6769, 0.0117015,  -2.80246e-05,   -8.54283e-07,
//                0.7346, 0.0113572,  -4.08389e-05,   -5.18524e-07,
//                0.7903, 0.0109099,  -4.86169e-05,   -1.0718e-06,
//                0.8435, 0.0103433,  -6.46934e-05,   5.36384e-09,
//                0.8936, 0.00969679, -6.46129e-05,   -8.54894e-06,
//                0.9394, 0.00840949, -0.000192847,   -4.21023e-06,
//                0.9761, 0.00616525, -0.000256001,   -4.21021e-06,
//                1., 0.,0.,0 };
//        
//        for (int i = 0; i < X.length; i++) {
//            System.out.println(X[i].c0 == px[i * 4]);
//            System.out.println(X[i].c1 == px[i * 4 + 1]);
//            System.out.println(X[i].c2 == px[i * 4 + 2]);
//            System.out.println(X[i].c3 == px[i * 4 + 3]);
//        }
//        
//        for (int i = 0; i < Y.length; i++) {
//            System.out.println(Y[i].c0 == py[i * 4]);
//            System.out.println(Y[i].c1 == py[i * 4 + 1]);
//            System.out.println(Y[i].c2 == py[i * 4 + 2]);
//            System.out.println(Y[i].c3 == py[i * 4 + 3]);
//        }
//    }
    
    public static void main(String[] args) {
        System.out.println(PI / 2d);
    }
}
