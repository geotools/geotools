/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
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
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;

import java.awt.geom.Point2D;
import java.text.MessageFormat;
import java.util.Collection;
import org.geotools.api.parameter.GeneralParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.PlanarProjection;
import org.geotools.measure.Latitude;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;

/**
 * Equidistant Conic Projection.
 *
 * <p><b>NOTE:</b> formulae used below are from a port, to Java, of the {@code proj4} package of the USGS survey. USGS
 * work is acknowledged here.
 *
 * <p><b>References:</b>
 *
 * <ul>
 *   <li>Proj-4.4.7 available at <A HREF="http://trac.osgeo.org/proj/">trac.osgeo.org/proj/</A>.<br>
 *       Relevant files are: {@code PJ_eqdc.c}, {@code pj_mlfn.c}, {@code pj_msfn.c}, {@code pj_fwd.c} and
 *       {@code pj_inv.c}.
 * </ul>
 *
 * @see <A HREF="http://mathworld.wolfram.com/ConicEquidistantProjection.html">Conic Equidistant projection on
 *     mathworld.wolfram.com</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/equidistant_conic.html">"Equidistant Conic" on
 *     www.remotesensing.org</A>
 * @since 2.6.1
 * @version $Id$
 * @author Ivan Boldyrev
 */
public class EquidistantConic extends MapProjection {
    /** For compatibility with different versions during deserialization. */
    private static final long serialVersionUID = 5885522933843653157L;

    /** Maximum difference allowed when comparing real numbers. */
    private static final double EPSILON = 1E-6;

    /** Internal value of projection. */
    private final double rho0, n, c;

    /** First standard parallel. */
    private final double phi1;

    /** Second standard parallel. */
    private double phi2;

    /**
     * Creates a transform from the specified group of parameter values.
     *
     * @param parameters The group of parameter values.
     * @throws ParameterNotFoundException if a required parameter was not found.
     * @since 2.4
     */
    protected EquidistantConic(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);

        /* Non-final placeholder for value of final this.n. */
        double n;

        // Fetch parameters
        final Collection<GeneralParameterDescriptor> expected =
                getParameterDescriptors().descriptors();
        phi1 = doubleValue(expected, Provider.STANDARD_PARALLEL_1, parameters);
        ensureLatitudeInRange(Provider.STANDARD_PARALLEL_1, phi1, true);
        phi2 = doubleValue(expected, Provider.STANDARD_PARALLEL_2, parameters);
        if (Double.isNaN(phi2)) {
            phi2 = phi1;
        }
        ensureLatitudeInRange(Provider.STANDARD_PARALLEL_2, phi2, true);

        // Compute Constants
        if (abs(phi1 + phi2) < EPSILON) {
            final Object arg0 = new Latitude(toDegrees(phi1));
            final Object arg1 = new Latitude(toDegrees(phi2));
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.ANTIPODE_LATITUDES_$2, arg0, arg1));
        }

        double sinphi = n = sin(phi1);
        double cosphi = cos(phi1);
        boolean secant = abs(phi1 - phi2) >= EPSILON;

        if (isSpherical) {
            if (secant) {
                n = (cosphi - cos(phi2)) / (phi2 - phi1);
            }
            c = phi1 + cos(phi1) / n;
            rho0 = c - latitudeOfOrigin;
            en0 = en1 = en2 = en3 = en4 = 0.0;
        } else {
            double m1 = msfn(sinphi, cosphi);

            double ml1 = mlfn(phi1, sinphi, cosphi);
            if (secant) {
                sinphi = sin(phi2);
                cosphi = cos(phi2);
                n = (m1 - msfn(sinphi, cosphi)) / (mlfn(phi2, sinphi, cosphi) - ml1);
            }
            c = ml1 + m1 / n;
            rho0 = c - mlfn(latitudeOfOrigin, sin(latitudeOfOrigin), cos(latitudeOfOrigin));
        }
        this.n = n;
    }

    /** {@inheritDoc} */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /** {@inheritDoc} */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        final Collection<GeneralParameterDescriptor> expected =
                getParameterDescriptors().descriptors();
        set(expected, Provider.STANDARD_PARALLEL_1, values, phi1);
        set(expected, Provider.STANDARD_PARALLEL_2, values, phi2);
        return values;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates (units in radians) and stores the
     * result in {@code ptDst} (linear distance on a unit sphere).
     */
    @Override
    protected Point2D transformNormalized(double x, double y, final Point2D ptDst) throws ProjectionException {
        final double cosphi = cos(y);
        final double sinphi = sin(y);
        final double rho = c - (this.isSpherical ? y : mlfn(y, sinphi, cosphi));

        final double x1 = rho * sin(x *= this.n);
        final double y1 = this.rho0 - rho * cos(x);

        if (ptDst != null) {
            ptDst.setLocation(x1, y1);
            return ptDst;
        }
        return new Point2D.Double(x1, y1);
    }

    /** Transforms the specified (<var>x</var>,<var>y</var>) coordinates and stores the result in {@code ptDst}. */
    @Override
    protected Point2D inverseTransformNormalized(double x, double y, final Point2D ptDst) throws ProjectionException {
        double rho = hypot(x, y = this.rho0 - y);
        double phi, lam;

        if (rho != 0.0) {
            if (this.n < 0.) {
                rho = -rho;
                x = -x;
                y = -y;
            }
            phi = this.c - rho;
            if (!isSpherical) {
                phi = inv_mlfn(phi);
            }
            lam = atan2(x, y) / this.n;
        } else {
            lam = 0;
            phi = this.n > 0 ? PI / 2. : -PI / 2.;
        }

        if (ptDst != null) {
            ptDst.setLocation(lam, phi);
            return ptDst;
        }
        return new Point2D.Double(lam, phi);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                                 PROVIDERS                                ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform provider} for a
     * {@linkplain EquidistantConic EquidistantConic} projection.
     *
     * @since 2.6.1
     * @version $Id$
     * @author Ivan Boldyrev
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static final class Provider extends AbstractProvider {
        /** For compatibility with different versions during deserialization. */
        private static final long serialVersionUID = 1995516958029802849L;

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.GEOTIFF, "CT_Equidistant_Conic"),
                    new NamedIdentifier(Citations.ESRI, "Equidistant_Conic"),
                    new NamedIdentifier(
                            Citations.GEOTOOLS,
                            Vocabulary.formatInternational(VocabularyKeys.EQUIDISTANT_CONIC_PROJECTION)),
                    new NamedIdentifier(Citations.PROJ, "eqdc")
                },
                new ParameterDescriptor[] {
                    SEMI_MAJOR, SEMI_MINOR,
                    CENTRAL_MERIDIAN, LATITUDE_OF_ORIGIN,
                    STANDARD_PARALLEL_1, STANDARD_PARALLEL_2,
                    FALSE_EASTING, FALSE_NORTHING
                });

        /** Constructs a new provider. */
        public Provider() {
            super(PARAMETERS);
        }

        /** Returns the operation type for this map projection. */
        @Override
        public Class<PlanarProjection> getOperationType() {
            return PlanarProjection.class;
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
            return new EquidistantConic(parameters);
        }
    }
}
