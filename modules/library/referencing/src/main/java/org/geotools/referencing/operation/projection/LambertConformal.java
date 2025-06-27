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

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static java.lang.Math.toDegrees;

import java.awt.geom.Point2D;
import java.text.MessageFormat;
import java.util.Collection;
import org.geotools.api.parameter.GeneralParameterDescriptor;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.measure.Latitude;
import org.geotools.metadata.i18n.ErrorKeys;

/**
 * Lambert Conical Conformal Projection. Areas and shapes are deformed as one moves away from standard parallels. The
 * angles are true in a limited area. This projection is used for the charts of North America.
 *
 * <p>This implementation provides transforms for three cases of the lambert conic conformal projection:
 *
 * <p>
 *
 * <ul>
 *   <li>{@code Lambert_Conformal_Conic_1SP} (EPSG code 9801)
 *   <li>{@code Lambert_Conformal_Conic_2SP} (EPSG code 9802)
 *   <li>{@code Lambert_Conic_Conformal_2SP_Belgium} (EPSG code 9803)
 *   <li>{@code Lambert_Conformal_Conic} - An alias for the ESRI 2SP case that includes a scale_factor parameter
 * </ul>
 *
 * <p>For the 1SP case the latitude of origin is used as the standard parallel (SP). To use 1SP with a latitude of
 * origin different from the SP, use the 2SP and set the SP1 to the single SP. The {@code standard_parallel_2"}
 * parameter is optional and will be given the same value as {@code "standard_parallel_1"} if not set (creating a 1
 * standard parallel projection).
 *
 * <p><b>References:</b>
 *
 * <ul>
 *   <li>John P. Snyder (Map Projections - A Working Manual,<br>
 *       U.S. Geological Survey Professional Paper 1395, 1987)
 *   <li>"Coordinate Conversions and Transformations including Formulas",<br>
 *       EPSG Guidence Note Number 7, Version 19.
 * </ul>
 *
 * @see <A HREF="http://mathworld.wolfram.com/LambertConformalConicProjection.html">Lambert conformal conic projection
 *     on MathWorld</A>
 * @see <A
 *     HREF="http://www.remotesensing.org/geotiff/proj_list/lambert_conic_conformal_1sp.html">lambert_conic_conformal_1sp</A>
 * @see <A
 *     HREF="http://www.remotesensing.org/geotiff/proj_list/lambert_conic_conformal_2sp.html">lambert_conic_conformal_2sp</A>
 * @see <A
 *     HREF="http://www.remotesensing.org/geotiff/proj_list/lambert_conic_conformal_2sp_belgium.html">lambert_conic_conformal_2sp_belgium</A>
 * @since 2.1
 * @version $Id$
 * @author André Gosselin
 * @author Martin Desruisseaux (PMO, IRD)
 * @author Rueben Schulz
 */
public abstract class LambertConformal extends MapProjection {
    /** For cross-version compatibility. */
    private static final long serialVersionUID = 1275881689637308614L;

    /** Maximum difference allowed when comparing real numbers. */
    private static final double EPSILON = 1E-6;

    /** Constant for the belgium 2SP case. This is 29.2985 seconds, given here in radians. */
    private static final double BELGE_A = 0.00014204313635987700;

    /** Standards parallel 1 in radians, for {@link #getParameterValues} implementation. */
    private final double phi1;

    /** Standards parallel 2 in radians, for {@link #getParameterValues} implementation. */
    private final double phi2;

    /** Internal variables for computation. */
    private final double n, F, rho0;

    /** {@code true} for Belgium 2SP. */
    private final boolean belgium;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected LambertConformal(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        this(parameters, false);
    }

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param parameters The parameter values in standard units.
     * @param belgium {@code true} for the Belgium 2SP case.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    LambertConformal(final ParameterValueGroup parameters, final boolean belgium) throws ParameterNotFoundException {
        // Fetch parameters
        super(parameters);
        final Collection<GeneralParameterDescriptor> expected =
                getParameterDescriptors().descriptors();
        final boolean sp2 = expected.contains(AbstractProvider.STANDARD_PARALLEL_2);
        this.belgium = belgium;
        if (sp2) {
            phi1 = doubleValue(expected, AbstractProvider.STANDARD_PARALLEL_1, parameters);
            ensureLatitudeInRange(AbstractProvider.STANDARD_PARALLEL_1, phi1, true);
            double phi2 = doubleValue(expected, AbstractProvider.STANDARD_PARALLEL_2, parameters);
            if (Double.isNaN(phi2)) {
                phi2 = phi1;
            }
            this.phi2 = phi2;
            ensureLatitudeInRange(AbstractProvider.STANDARD_PARALLEL_2, phi2, true);
        } else {
            if (belgium) {
                throw new IllegalArgumentException();
            }
            // EPSG says the 1SP case uses the latitude of origin as the SP
            phi1 = phi2 = latitudeOfOrigin;
        }
        // Compute constants
        if (abs(phi1 + phi2) < EPSILON) {
            final Object arg0 = new Latitude(toDegrees(phi1));
            final Object arg1 = new Latitude(toDegrees(phi2));
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.ANTIPODE_LATITUDES_$2, arg0, arg1));
        }
        final double cosphi1 = cos(phi1);
        final double sinphi1 = sin(phi1);
        final boolean secant = abs(phi1 - phi2) > EPSILON; // Should be 'true' for 2SP case.
        if (isSpherical) {
            if (secant) {
                n = log(cosphi1 / cos(phi2)) / log(tan(PI / 4 + 0.5 * phi2) / tan(PI / 4 + 0.5 * phi1));
            } else {
                n = sinphi1;
            }
            F = cosphi1 * pow(tan(PI / 4 + 0.5 * phi1), n) / n;
            if (abs(abs(latitudeOfOrigin) - PI / 2) >= EPSILON) {
                rho0 = F * pow(tan(PI / 4 + 0.5 * latitudeOfOrigin), -n);
            } else {
                rho0 = 0.0;
            }
        } else {
            final double m1 = msfn(sinphi1, cosphi1);
            final double t1 = tsfn(phi1, sinphi1);
            if (secant) {
                final double sinphi2 = sin(phi2);
                final double m2 = msfn(sinphi2, cos(phi2));
                final double t2 = tsfn(phi2, sinphi2);
                n = log(m1 / m2) / log(t1 / t2);
            } else {
                n = sinphi1;
            }
            F = m1 * pow(t1, -n) / n;
            if (abs(abs(latitudeOfOrigin) - PI / 2) >= EPSILON) {
                rho0 = F * pow(tsfn(latitudeOfOrigin, sin(latitudeOfOrigin)), n);
            } else {
                rho0 = 0.0;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        final Collection<GeneralParameterDescriptor> expected =
                getParameterDescriptors().descriptors();
        set(expected, AbstractProvider.STANDARD_PARALLEL_1, values, phi1);
        set(expected, AbstractProvider.STANDARD_PARALLEL_2, values, phi2);
        return values;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates (units in radians) and stores the
     * result in {@code ptDst} (linear distance on a unit sphere).
     */
    @Override
    protected Point2D transformNormalized(double x, double y, Point2D ptDst) throws ProjectionException {
        double rho;
        // Snyder p. 108
        if (abs(abs(y) - PI / 2) < EPSILON) {
            if (y * n <= 0) {
                throw new ProjectionException(y);
            } else {
                rho = 0;
            }
        } else if (isSpherical) {
            rho = F * pow(tan(PI / 4 + 0.5 * y), -n);
        } else {
            rho = F * pow(tsfn(y, sin(y)), n);
        }
        x *= n;
        if (belgium) {
            x -= BELGE_A;
        }
        y = rho0 - rho * cos(x);
        x = rho * sin(x);
        if (ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        }
        return new Point2D.Double(x, y);
    }

    /** Transforms the specified (<var>x</var>,<var>y</var>) coordinates and stores the result in {@code ptDst}. */
    @Override
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst) throws ProjectionException {
        double theta;
        y = rho0 - y;
        double rho = hypot(x, y); // Zero when the latitude is 90 degrees.
        if (rho > EPSILON) {
            if (n < 0) {
                rho = -rho;
                x = -x;
                y = -y;
            }
            theta = atan2(x, y);
            if (belgium) {
                theta += BELGE_A;
            }
            x = theta / n;
            if (isSpherical) {
                y = 2.0 * atan(pow(F / rho, 1.0 / n)) - PI / 2;
            } else {
                y = cphi2(pow(rho / F, 1.0 / n));
            }
        } else {
            x = 0.0;
            y = n < 0 ? -(PI / 2) : PI / 2;
        }
        if (ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        }
        return new Point2D.Double(x, y);
    }

    /** Returns a hash value for this projection. */
    @Override
    public int hashCode() {
        /*
         * This code should be computed fast. Consequently, we do not use all fields
         * in this object.  Two {@code LambertConformal} objects with different
         * {@link #phi1} and {@link #phi2} should compute a F value different enough.
         */
        final long code = Double.doubleToLongBits(F);
        return ((int) code ^ (int) (code >>> 32)) + 37 * super.hashCode();
    }

    /** Compares the specified object with this map projection for equality. */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final LambertConformal that = (LambertConformal) object;
            return this.belgium == that.belgium
                    && equals(this.n, that.n)
                    && equals(this.F, that.F)
                    && equals(this.rho0, that.rho0)
                    && equals(this.phi1, that.phi1)
                    && equals(this.phi2, that.phi2);
        }
        return false;
    }
}
