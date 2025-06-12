/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.awt.geom.Point2D;
import java.util.logging.Level;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;

/**
 * Meteosat Second Generation imagery projection
 *
 * <p>Conversion of image coordinates (pixel column and row) into the corresponding geographical coordinates (Latitude
 * and Longitude) of all MSG Satellites (Meteosat-8, Meteosat-9 and Meteosat-10) Level 1.5 VIS/IR data.
 *
 * <p>Code based on reference software provided by EUMETSAT "MSG_navigation" v1.02 (see link below).
 *
 * <p>Please be aware, that the program assumes the MSG image is ordered in the operational scanning direction which
 * means from south to north and from east to west. With that the VIS/IR channels contains of 3712 x 3712 pixels, start
 * to count on the most southern line and the most eastern column with pixel number 1,1.
 *
 * <p>Conversion from native MSG files (delivered by EumetCAST) to geotiff format could be done with <a
 * href="http://sourceforge.net/projects/meteosatlib">Meteosatlib package</a>.
 *
 * <p>To extract the European area using "products" utility: {@code examples/products -a 1572,1024,90,560 -t
 * 201505080600 --products=Vis006} Coordinates above are calculated from the upper left corner. Therefore, it is
 * necessary to change it according to the EUMETCAST specification. {@code gdal_translate -a_ullr 2140.5 3622.5 1116.5
 * 3062.5 file_in.tif file_out.tif}
 *
 * <p>Additional examples could be find in the "MeteosatSG.txt" file in tests directory.
 *
 * <p>References: [1] LRIT/HRIT Global Specification (CGMS 03, Issue 2.6, 12.08.1999) for the parameters used in the
 * program. [2] MSG Ground Segment LRIT/HRIT Mission Specific Implementation, EUMETSAT Document, (EUM/MSG/SPE/057, Issue
 * 6, 21. June 2006). [3] MSG Level 1.5 Image Data Format Description (EUM/MSG/ICD/105, Issue v6, 23. February 2010).
 *
 * @see <a href="http://www.eumetsat.int/website/home/Data/DataDelivery/SupportSoftwareandTools/index.html">Navigation
 *     Software for Meteosat-9 (MSG) - Level 1.5 VIS/IR/HRV data</a>
 * @since 14.0
 * @version $Id$
 * @author Maciej Filocha (ICM)
 */
public class MeteosatSG extends MapProjection {

    /** serialVersionUID */
    private static final long serialVersionUID = -6360986801876534108L;

    /** Distance from Earth centre to satellite */
    private static final double SAT_HEIGHT = 42164.0;

    /** Radius from Earth centre to equator */
    private static final double R_EQ = 6378.169;

    /** Radius from Earth centre to pol */
    private static final double R_POL = 6356.5838;

    /** Longitude of sub-satellite point in radiant */
    private static final double SUB_LON = 0.0;

    /**
     * Scaling coefficient provided by the navigation record of the LRIT/HRIT. CFAC/LFAC are responsible for the image
     * "spread" in the NS and EW directions. They are calculated as follows: CFAC = LFAC = 2^16 / delta with delta =
     * 83.84333 micro Radian (size of one VIS/IR MSG pixel) delta_HRV = 83.84333/3 micro Radian (size of one HRV MSG
     * pixel)
     */
    private static final double CFAC_NONHRV = -781648343;

    /**
     * Scaling coefficient provided by the navigation record of the LRIT/HRIT. CFAC/LFAC are responsible for the image
     * "spread" in the NS and EW directions. They are calculated as follows: CFAC = LFAC = 2^16 / delta with delta =
     * 83.84333 micro Radian (size of one VIS/IR MSG pixel) delta_HRV = 83.84333/3 micro Radian (size of one HRV MSG
     * pixel)
     */
    private static final double LFAC_NONHRV = -781648343;

    /**
     * Scaling coefficient provided by the navigation record of the LRIT/HRIT. COFF/LOFF are the offsets for column and
     * line which are defining the middle of the Image (centre pixel) and are basically 1856/1856 for the VIS/IR
     * channels and 5566/5566 for the HRV channel reference grid.
     */
    private static final long COFF_NONHRV = 1856;

    /**
     * Scaling coefficient provided by the navigation record of the LRIT/HRIT. COFF/LOFF are the offsets for column and
     * line which are defining the middle of the Image (centre pixel) and are basically 1856/1856 for the VIS/IR
     * channels and 5566/5566 for the HRV channel reference grid.
     */
    private static final long LOFF_NONHRV = 1856;

    // TODO: Fetch actual parameter value
    private static final double SCALE_FACTOR = 6378137.0;

    /**
     * Constructs a Meteosat Second Generation imagery projection.
     *
     * @param parameters The group of parameter values.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    protected MeteosatSG(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates (units in radians) and stores the
     * result in {@code ptDst} (pixel coordinates).
     *
     * @param x The longitude of the coordinate, in <strong>radians</strong>.
     * @param y The latitude of the coordinate, in <strong>radians</strong>.
     */
    @Override
    @SuppressWarnings("UnusedVariable") // assignments to r1, r2, r3, sn, s1, s2, and s3, are never used
    protected Point2D transformNormalized(double x, double y, Point2D ptDst) throws ProjectionException {

        /* x - lon, y -lat */

        double r1 = 0.0, r2 = 0.0, r3 = 0.0;
        double col_norm, row_norm;

        // For non-HRV images
        double lfac = LFAC_NONHRV;
        double cfac = CFAC_NONHRV;

        long coff = COFF_NONHRV;
        long loff = LOFF_NONHRV;

        /* calculate the geocentric latitude from the */
        /* geographic one using equations on page 24, Ref. [1] */

        double c_lat = atan((0.993243 * (sin(y) / cos(y))));

        // Pre-compute some values
        double cos_c_lat = cos(c_lat);
        double cos_x_SUB_LON = cos(x - SUB_LON);

        /* using c_lat calculate the length form the Earth */
        /* centre to the surface of the Earth ellipsoid */
        /* equations on page 23, Ref. [1] */

        double re = R_POL / sqrt((1.0 - 0.00675701 * cos_c_lat * cos_c_lat));

        /* calculate the forward projection using equations on */
        /* page 24, Ref. [1] */

        double rl = re;
        r1 = SAT_HEIGHT - rl * cos_c_lat * cos_x_SUB_LON;
        r2 = -rl * cos_c_lat * sin(x - SUB_LON);
        r3 = rl * sin(c_lat);
        double rn = sqrt(r1 * r1 + r2 * r2 + r3 * r3);

        /* check for visibility, whether the point on the Earth given by the */
        /* latitude/longitude pair is visible from the satellite or not. This */
        /* is given by the dot product between the vectors of: */
        /* 1) the point to the spacecraft, */
        /* 2) the point to the centre of the Earth. */
        /* If the dot product is positive the point is visible otherwise it */
        /* is invisible. */

        double dotprod = r1 * (rl * cos_c_lat * cos_x_SUB_LON) - r2 * r2 - r3 * r3 * pow((R_EQ / R_POL), 2);

        if (dotprod <= 0) {
            /*
             * Return some real coordinates instead of -999,-999 to avoid an error and to allow GeoServer to compute other points of an image.
             *
             * TODO: Is it really proper way to handle such points?
             *
             * throw new ProjectionException(Errors.format(ErrorKeys.OUT_OF_PROJECTION_VALID_AREA_$1, "lon=" + x + " lat=" + y ));
             */

            col_norm = 58.0 / SCALE_FACTOR;
            row_norm = 1856.0 / SCALE_FACTOR;

            if (ptDst != null) {
                ptDst.setLocation(col_norm, row_norm);
                LOGGER.log(
                        Level.INFO,
                        "MeteosatSG transform: Lon/lat outside vaild range, lon="
                                + x
                                + " lat="
                                + y
                                + " Col/row set arbitrary to 58,1856 (0N, 74.48E");
                return ptDst;
            }
            return new Point2D.Double(col_norm, row_norm);
        }

        /* the forward projection is x and y */

        double xx = atan((-r2 / r1));
        double yy = asin((-r3 / rn));

        /* convert to pixel column and row using the scaling functions on */
        /* page 28, Ref. [1]. And finding nearest integer value for them. */

        double cc = coff + xx * pow(2, -16) * cfac;
        double ll = loff + yy * pow(2, -16) * lfac;

        col_norm = cc / SCALE_FACTOR;
        row_norm = ll / SCALE_FACTOR;

        if (ptDst != null) {
            ptDst.setLocation(col_norm, row_norm);
            LOGGER.log(Level.FINE, "MeteosatSG transform: col=" + cc + " row=" + ll);
            return ptDst;
        }
        return new Point2D.Double(col_norm, row_norm);
    }

    /**
     * Transforms the specified (<var>column</var>,<var>row</var>) coordinates (units in "normalized" pixels) and stores
     * the result in {@code ptDst}.
     */
    @Override
    @SuppressWarnings("UnusedVariable") // assignments to sn, s1, s2, and s3, are never read
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst) throws ProjectionException {

        // x- column, y -row
        double s1 = 0.0, s2 = 0.0, s3 = 0.0, sn = 0.0;
        double lat, lon;

        // For non-HRV images
        double lfac = LFAC_NONHRV;
        double cfac = CFAC_NONHRV;

        long coff = COFF_NONHRV;
        long loff = LOFF_NONHRV;

        /* Normalization(?) */
        x = x * SCALE_FACTOR;
        y = y * SCALE_FACTOR;

        /* calculate viewing angle of the satellite by use of the equation */
        /* on page 28, Ref [1]. */

        double x1 = 65536 * (x - coff) / cfac;
        double y1 = 65536 * (y - loff) / lfac;

        double sin_y1 = sin(y1);
        double cos_x1 = cos(x1);
        double cos_y1 = cos(y1);

        /* now calculate the inverse projection */

        /* first check for visibility, whether the pixel is located on the Earth */
        /* surface or in space. */
        /* To do this calculate the argument to sqrt of "sd", which is named "sa". */
        /* If it is negative then the sqrt will return NaN and the pixel will be */
        /* located in space, otherwise all is fine and the pixel is located on the */
        /* Earth surface. */

        double sa = pow(SAT_HEIGHT * cos_x1 * cos_y1, 2) - (cos_y1 * cos_y1 + 1.006803 * sin_y1 * sin_y1) * 1737121856.;

        /* produce @TODO error values */
        if (sa <= 0.0) {
            lat = 0;
            lon = 1.2998982273; // 74.48 deg
            // TODO: See comment in the respective fragment of transformNormalized method above
            if (ptDst != null) {
                ptDst.setLocation(lat, lon);
                LOGGER.log(
                        Level.INFO,
                        "MeteosatSG inverse transform: Column/row outside vaild range, x="
                                + x
                                + " y="
                                + y
                                + " Lat/lon set to (0N, 74.48E)");
                return ptDst;
            }
            return new Point2D.Double(lon, lat);
        }

        /* now calculate the rest of the formulas using equations on */
        /* page 25, Ref. [1] */

        double sd = sqrt(
                pow((SAT_HEIGHT * cos_x1 * cos_y1), 2) - (cos_y1 * cos_y1 + 1.006803 * sin_y1 * sin_y1) * 1737121856.);
        sn = (SAT_HEIGHT * cos_x1 * cos_y1 - sd) / (cos_y1 * cos_y1 + 1.006803 * sin_y1 * sin_y1);

        s1 = SAT_HEIGHT - sn * cos_x1 * cos_y1;
        s2 = sn * sin(x1) * cos_y1;
        s3 = -sn * sin_y1;

        double sxy = sqrt(s1 * s1 + s2 * s2);

        /* using the previous calculations the inverse projection can be */
        /* calculated now, which means calculating the lat./long. from */
        /* the pixel row and column by equations on page 25, Ref [1]. */

        lon = atan(s2 / s1) + SUB_LON;
        lat = atan((1.006803 * s3) / sxy);

        if (ptDst != null) {
            ptDst.setLocation(lon, lat);
            LOGGER.log(Level.FINE, "MeteosatSG inverse transform: col=" + x + " row=" + y);
            return ptDst;
        }
        return new Point2D.Double(lon, lat);
    }

    @Override
    protected double getToleranceForAssertions(final double longitude, final double latitude) {
        /*
         * Relaxed tolerance - pixel resolution of sub-satellite point is
         * 3 kilometers.
         */
        final double delta = abs(longitude - centralMeridian) / 2 + abs(latitude - latitudeOfOrigin);
        if (delta > 40) {
            // When far from the valid area, use a larger tolerance.
            return 3;
        }
        // Be less strict when the point is near an edge.
        return (abs(longitude) > 179) || (abs(latitude) > 89) ? 5E-1 : 3E-1;
    }

    /** {@inheritDoc} */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////
    // ////// ////////
    // ////// PROVIDERS ////////
    // ////// ////////
    // ////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform provider} for an
     * {@linkplain org.geotools.referencing.operation.projection.MeteosatSG Meteosat Second Generation image}
     * projection.
     *
     * @since 14.0
     * @version $Id$
     * @author Maciej Filocha (ICM)
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {

        /** serialVersionUID */
        private static final long serialVersionUID = -2722451724278085168L;

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.AUTO, "MeteosatSG"),
                },
                new ParameterDescriptor[] {
                    SEMI_MAJOR,
                    SEMI_MINOR,
                    CENTRAL_MERIDIAN,
                    LATITUDE_OF_ORIGIN,
                    SCALE_FACTOR,
                    FALSE_EASTING,
                    FALSE_NORTHING
                });

        /** Constructs a new provider. */
        public Provider() {
            super(PARAMETERS);
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
                LOGGER.log(
                        Level.INFO,
                        "MeteosatSG conversion assumes ellipsoidal Earth shape. "
                                + "Be aware of possibe errors arising from mixing ellipsoidal equations with spherical coordinates.");
                return new MeteosatSG(parameters);
            } else {
                return new MeteosatSG(parameters);
            }
        }
    }
}
