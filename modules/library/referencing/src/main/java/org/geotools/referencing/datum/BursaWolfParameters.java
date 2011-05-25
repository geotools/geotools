/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.datum;

import java.io.Serializable;
import static java.lang.Double.doubleToLongBits;

import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.operation.Matrix;
import org.opengis.util.Cloneable;

import org.geotools.referencing.operation.matrix.Matrix4;
import org.geotools.referencing.operation.matrix.XMatrix;
import org.geotools.referencing.wkt.Formattable;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;


/**
 * Parameters for a geographic transformation between two datum.
 * The Bursa Wolf parameters should be applied to geocentric coordinates,
 * where the <var>X</var> axis points towards the Greenwich Prime Meridian,
 * the <var>Y</var> axis points East, and the <var>Z</var> axis points North.
 * The "Bursa-Wolf" formula is expressed in matrix form with 7 parameters:
 *
 * <p align="center"><img src="../doc-files/BursaWolf.png"></p>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class BursaWolfParameters extends Formattable implements Cloneable, Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 754825592343010900L;

    /** Bursa Wolf shift in meters. */
    public double dx;

    /** Bursa Wolf shift in meters. */
    public double dy;

    /** Bursa Wolf shift in meters. */
    public double dz;

    /** Bursa Wolf rotation in arc seconds. */
    public double ex;

    /** Bursa Wolf rotation in arc seconds. */
    public double ey;

    /** Bursa Wolf rotation in arc seconds. */
    public double ez;

    /** Bursa Wolf scaling in parts per million. */
    public double ppm;

    /** The target datum for this parameters. */
    public final GeodeticDatum targetDatum;

    /**
     * Constructs a transformation info with all parameters set to 0.
     *
     * @param target The target datum for this parameters.
     */
    public BursaWolfParameters(final GeodeticDatum target) {
        this.targetDatum = target;
    }

    /**
     * Returns {@code true} if this Bursa Wolf parameters performs no operation.
     * This is true when all parameters are set to zero.
     *
     * @return {@code true} if the parameters describe no operation.
     */
    public boolean isIdentity() {
        return dx==0 && dy==0 && dz==0 && ex==0 && ey==0 && ez==0 && ppm==0;
    }

    /**
     * Returns {@code true} if this Bursa Wolf parameters contains only translation terms.
     *
     * @return {@code true} if the parameters describe to a translation only.
     */
    public boolean isTranslation() {
        return ex==0 && ey==0 && ez==0 && ppm==0;
    }

    /**
     * Returns an affine transform that can be used to define this
     * Bursa Wolf transformation. The formula is as follows:
     *
     * <blockquote><pre>
     * S = 1 + {@link #ppm}/1000000
     *
     * [ X ]    [     S   -{@link #ez}*S   +{@link #ey}*S   {@link #dx} ]  [ X ]
     * [ Y ]  = [ +{@link #ez}*S       S   -{@link #ex}*S   {@link #dy} ]  [ Y }
     * [ Z ]    [ -{@link #ey}*S   +{@link #ex}*S       S   {@link #dz} ]  [ Z ]
     * [ 1  ]    [     0       0       0    1 ]  [ 1 ]
     * </pre></blockquote>
     *
     * This affine transform can be applied on <strong>geocentric</strong> coordinates.
     *
     * @return An affine transform created from the parameters.
     */
    public XMatrix getAffineTransform() {
        /*
         * Note: (ex, ey, ez) is a rotation in arc seconds. We need to convert it into radians
         *       (the R factor in RS). TODO: to be strict, are we supposed to take the sinus of
         *       rotation angles?
         */
        final double  S = 1 + ppm/1E+6;
        final double RS = (Math.PI/(180*3600)) * S;
        return new Matrix4(
                 S,  -ez*RS,  +ey*RS,  dx,
            +ez*RS,       S,  -ex*RS,  dy,
            -ey*RS,  +ex*RS,       S,  dz,
                 0,       0,       0,   1);
    }

    /**
     * Sets transformation info from the specified matrix, which must be affine.
     * In addition, the matrix minus the last row and last column must be
     * <A HREF="http://mathworld.wolfram.com/AntisymmetricMatrix.html">antisymmetric</a>.
     *
     * @param matrix The matrix to fit as a Bursa-Wolf construct.
     * @param eps    The tolerance error for the antisymmetric matrix test. Should be a small
     *               number like {@code 1E-4}.
     * @throws IllegalArgumentException if the specified matrix doesn't meet the conditions.
     *
     * @since 2.2
     */
    public void setAffineTransform(final Matrix matrix, final double eps)
            throws IllegalArgumentException
    {
        if (matrix.getNumCol()!=4 || matrix.getNumRow()!=4) {
            // TODO: localize. Same message than Matrix4
            throw new IllegalArgumentException("Illegal matrix size.");
        }
        for (int i=0; i<4; i++) {
            if (matrix.getElement(3,i) != (i==3 ? 1 : 0)) {
                throw new IllegalArgumentException(Errors.format(ErrorKeys.NON_AFFINE_TRANSFORM));
            }
        }
        dx = matrix.getElement(0,3);
        dy = matrix.getElement(1,3);
        dz = matrix.getElement(2,3);
        final double S = (matrix.getElement(0,0) +
                          matrix.getElement(1,1) +
                          matrix.getElement(2,2)) / 3;
        final double RS = (Math.PI/(180*3600)) * S;
        ppm = (S-1) * 1E+6;
        for (int j=0; j<2; j++) {
            final double eltS = (matrix.getElement(j,j)-1) * 1E+6;
            if (!(Math.abs(eltS - ppm) <= eps)) {
                // TODO: localize
                throw new IllegalArgumentException("Scale is not uniform.");
            }
            for (int i=j+1; i<3; i++) {
                final double elt1 = matrix.getElement(j,i) / RS;
                final double elt2 = matrix.getElement(i,j) / RS;
                // Note: compare with +, not -, because the two values should be opposite.
                if (!(Math.abs(elt1 + elt2) <= eps)) {
                    // TODO: localize
                    throw new IllegalArgumentException("Matrix is not antisymmetric.");
                }
                final double value = 0.5*(elt1 - elt2);
                if (j==0) switch (i) {
                    case 1: ez = -value; continue;
                    case 2: ey = +value; continue;
                }
                assert j==1 && i==2;
                ex = -value;
            }
        }
        assert getAffineTransform().equals(matrix, eps*RS);
    }

    /**
     * Returns a hash value for this object.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        long code = serialVersionUID;
        code = code*37 + doubleToLongBits(dx );
        code = code*37 + doubleToLongBits(dy );
        code = code*37 + doubleToLongBits(dz );
        code = code*37 + doubleToLongBits(ex );
        code = code*37 + doubleToLongBits(ey );
        code = code*37 + doubleToLongBits(ez );
        code = code*37 + doubleToLongBits(ppm);
        return (int)(code >>> 32) ^ (int)code;
    }

    /**
     * Returns a copy of this object.
     *
     * @return A clone of the parameters.
     */
    @Override
    public BursaWolfParameters clone() {
        try {
            return (BursaWolfParameters) super.clone();
        }  catch (CloneNotSupportedException exception) {
            // Should not happen, since we are cloneable.
            throw new AssertionError(exception);
        }
    }

    /**
     * Compares the specified object with this object for equality.
     *
     * @param object The object to compare with the parameters.
     * @return {@code true} if the given object is equals to the parameters.
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof BursaWolfParameters) {
            final BursaWolfParameters that = (BursaWolfParameters) object;
            return Utilities.equals(this.dx,  that.dx)  &&
                   Utilities.equals(this.dy,  that.dy)  &&
                   Utilities.equals(this.dz,  that.dz)  &&
                   Utilities.equals(this.ex,  that.ex)  &&
                   Utilities.equals(this.ey,  that.ey)  &&
                   Utilities.equals(this.ez,  that.ez)  &&
                   Utilities.equals(this.ppm, that.ppm) &&
                   Utilities.equals(this.targetDatum, that.targetDatum);
        }
        return false;
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element. The WKT contains the parameters in <i>translation</i>,
     * <i>rotation</i>, <i>scale</i> order, as in
     * <code>TOWGS84[{@linkplain #dx}, {@linkplain #dy}, {@linkplain #dz},
     * {@linkplain #ex}, {@linkplain #ey}, {@linkplain #ez}, {@linkplain #ppm}]</code>.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name.
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        formatter.append(dx);
        formatter.append(dy);
        formatter.append(dz);
        formatter.append(ex);
        formatter.append(ey);
        formatter.append(ez);
        formatter.append(ppm);
        if (!DefaultGeodeticDatum.isWGS84(targetDatum)) {
            if (targetDatum != null) {
                formatter.append(targetDatum.getName().getCode());
            }
            return super.formatWKT(formatter);
        }
        return "TOWGS84";
    }
}
