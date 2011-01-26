/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.transform;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import javax.measure.unit.SI;
import static java.lang.Math.*;

import org.opengis.util.GenericName;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.Transformation;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.FloatParameter;
import org.geotools.parameter.Parameter;
import org.geotools.parameter.ParameterGroup;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;


/**
 * Two- or three-dimensional datum shift using the (potentially abridged) Molodensky transformation.
 * The Molodensky transformation (EPSG code 9604) and the abridged Molodensky transformation (EPSG
 * code 9605) transform two or three dimensional geographic points from one geographic coordinate
 * reference system to another (a datum shift), using three shift parameters (delta X, delta Y,
 * delta Z) and the difference between the semi-major axis and flattenings of the two ellipsoids.
 * <p>
 *
 * Unlike the Bursa-Wolf 3 parameter method (which acts on geocentric coordinates),
 * this transformation can be performed directly on geographic coordinates.
 * <p>
 *
 * <strong>References:</strong><ul>
 *   <li> Defense Mapping Agency (DMA), Datums, Ellipsoids, Grids and Grid Reference Systems,
 *        Technical Manual 8358.1.
 *        Available from <a href="http://earth-info.nga.mil/GandG/pubs.html">http://earth-info.nga.mil/GandG/pubs.html</a></li>
 *   <li> Defense Mapping Agency (DMA), The Universal Grids: Universal Transverse
 *        Mercator (UTM) and Universal Polar Stereographic (UPS), Fairfax VA, Technical Manual 8358.2.
 *        Available from <a href="http://earth-info.nga.mil/GandG/pubs.html">http://earth-info.nga.mil/GandG/pubs.html</a></li>
 *   <li> National Imagery and Mapping Agency (NIMA), Department of Defense World
 *        Geodetic System 1984, Technical Report 8350.2.
 *        Available from <a href="http://earth-info.nga.mil/GandG/pubs.html">http://earth-info.nga.mil/GandG/pubs.html</a></li>
 *   <li> "Coordinate Conversions and Transformations including Formulas",
 *        EPSG Guidence Note Number 7, Version 19.</li>
 * </ul>
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Rueben Schulz
 * @author Martin Desruisseaux
 */
public class MolodenskiTransform extends AbstractMathTransform implements Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 7536566033885338422L;

    /**
     * The tolerance error for assertions, in decimal degrees.
     */
    private static final float EPS = 1E-5f;

    /**
     * {@code true} for the abridged formula, or {@code false} for the complete version.
     */
    private final boolean abridged;

    /**
     * {@code true} for a 3D transformation, or
     * {@code false} for a 2D transformation.
     */
    private final boolean source3D, target3D;

    /**
     * X,Y,Z shift in meters.
     */
    private final double dx, dy, dz;

    /**
     * Semi-major (<var>a</var>) semi-minor (<var>b/<var>) radius in meters.
     */
    private final double a, b;

    /**
     * Difference in the semi-major ({@code da = target a - source a}) and semi-minor
     * ({@code db = target b - source b}) axes of the target and source ellipsoids.
     */
    private final double da, db;

    /**
     * Difference between the flattenings ({@code df = target f - source f})
     * of the target and source ellipsoids.
     */
    private final double df;

    /**
     * Ratio of the Semi-major (<var>a</var>) semi-minor (<var>b/<var>) axis
     * values ({@code a_b = a/b} and {@code b_a = b/a}).
     */
    private final double b_a, a_b;

    /**
     * Some more constants (<code>daa = da*a</code> and {@code da_a = da/a}).
     */
    private final double daa, da_a;

    /**
     * The square of excentricity of the ellipsoid: e² = (a²-b²)/a² where
     * <var>a</var> is the semi-major axis length and
     * <var>b</var> is the semi-minor axis length.
     */
    private final double e2;

    /**
     * Defined as <code>(a*df) + (f*da)</code>.
     */
    private final double adf;

    /**
     * The inverse of this transform. Will be created only when first needed.
     */
    private transient MolodenskiTransform inverse;

    /**
     * Constructs a Molodenski transform from the specified parameters.
     *
     * @param abridged {@code true} for the abridged formula, or {@code false} for the complete one.
     * @param a        The source semi-major axis length in meters.
     * @param b        The source semi-minor axis length in meters.
     * @param source3D {@code true} if the source has a height.
     * @param ta       The target semi-major axis length in meters.
     * @param tb       The target semi-minor axis length in meters.
     * @param target3D {@code true} if the target has a height.
     * @param dx       The <var>x</var> translation in meters.
     * @param dy       The <var>y</var> translation in meters.
     * @param dz       The <var>z</var> translation in meters.
     */
    public MolodenskiTransform(final boolean abridged,
                               final double  a, final double  b, final boolean source3D,
                               final double ta, final double tb, final boolean target3D,
                               final double dx, final double dy, final double  dz)
    {
        this.abridged = abridged;
        this.source3D = source3D;
        this.target3D = target3D;
        this.dx       = dx;
        this.dy       = dy;
        this.dz       = dz;
        this.a        = a;
        this.b        = b;

        da    =  ta - a;
        db    =  tb - b;
        a_b   =  a / b;
        b_a   =  b / a;
        daa   =  da * a;
        da_a  =  da / a;
        df    =  (ta-tb)/ta - (a-b)/a;
        e2    =  1 - (b*b)/(a*a);
        adf   =  (a*df) + (a-b)*da/a;
    }

    /**
     * Returns the parameter descriptors for this math transform.
     */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return abridged ? ProviderAbridged.PARAMETERS : Provider.PARAMETERS;
    }

    /**
     * Returns the parameters for this math transform.
     *
     * @return The parameters for this math transform.
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterValue<Integer> dim = new Parameter<Integer>(Provider.DIM);
        dim.setValue(getSourceDimensions());
        return new ParameterGroup(getParameterDescriptors(),
               new ParameterValue[] {
                   dim,
                   new FloatParameter(Provider.DX,             dx),
                   new FloatParameter(Provider.DY,             dy),
                   new FloatParameter(Provider.DZ,             dz),
                   new FloatParameter(Provider.SRC_SEMI_MAJOR, a),
                   new FloatParameter(Provider.SRC_SEMI_MINOR, b),
                   new FloatParameter(Provider.TGT_SEMI_MAJOR, a+da),
                   new FloatParameter(Provider.TGT_SEMI_MINOR, b+db)
               });
    }

    /**
     * Gets the dimension of input points.
     */
    public int getSourceDimensions() {
        return source3D ? 3 : 2;
    }

    /**
     * Gets the dimension of output points.
     */
    public final int getTargetDimensions() {
        return target3D ? 3 : 2;
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     * This method is provided for efficiently transforming many points.
     * The supplied array of ordinal values will contain packed ordinal
     * values.  For example, if the source dimension is 3, then the ordinals
     * will be packed in this order:
     *
     * (<var>x<sub>0</sub></var>,<var>y<sub>0</sub></var>,<var>z<sub>0</sub></var>,
     *  <var>x<sub>1</sub></var>,<var>y<sub>1</sub></var>,<var>z<sub>1</sub></var> ...).
     *
     * @param srcPts the array containing the source point coordinates.
     * @param srcOff the offset to the first point to be transformed
     *               in the source array.
     * @param dstPts the array into which the transformed point
     *               coordinates are returned. May be the same
     *               than {@code srcPts}.
     * @param dstOff the offset to the location of the first
     *               transformed point that is stored in the
     *               destination array.
     * @param numPts the number of point objects to be transformed.
     */
    public void transform(double[] srcPts, int srcOff,
                          double[] dstPts, int dstOff, int numPts)
    {
        transform(null, srcPts, srcOff, null, dstPts, dstOff, numPts);
        /*
         * Assertions: computes the inverse transform in the 3D-case only
         *             (otherwise the transform is too approximative).
         *
         * NOTE: The somewhat complicated expression below executes 'maxError' *only* if
         * 1) assertions are enabled and 2) the conditions before 'maxError' are meet. Do
         * not factor the call to 'maxError' outside the 'assert' statement, otherwise it
         * would be executed everytime and would hurt performance for normal operations
         * (instead of slowing down during debugging only).
         */
        assert !(target3D && srcPts!=dstPts &&
                (maxError(null, srcPts, srcOff, null, dstPts, dstOff, numPts)) > EPS);
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     * This method is provided for efficiently transforming many points.
     * The supplied array of ordinal values will contain packed ordinal
     * values.  For example, if the source dimension is 3, then the ordinals
     * will be packed in this order:
     *
     * (<var>x<sub>0</sub></var>,<var>y<sub>0</sub></var>,<var>z<sub>0</sub></var>,
     *  <var>x<sub>1</sub></var>,<var>y<sub>1</sub></var>,<var>z<sub>1</sub></var> ...).
     *
     * @param srcPts the array containing the source point coordinates.
     * @param srcOff the offset to the first point to be transformed
     *               in the source array.
     * @param dstPts the array into which the transformed point
     *               coordinates are returned. May be the same
     *               than {@code srcPts}.
     * @param dstOff the offset to the location of the first
     *               transformed point that is stored in the
     *               destination array.
     * @param numPts the number of point objects to be transformed.
     */
    @Override
    public void transform(final float[] srcPts, int srcOff,
                          final float[] dstPts, int dstOff, int numPts)
    {
        transform(srcPts, null, srcOff, dstPts, null, dstOff, numPts);
        /*
         * Assertions: computes the inverse transform in the 3D-case only
         *             (otherwise the transform is too approximative).
         *
         * NOTE: The somewhat complicated expression below executes 'maxError' *only* if
         * 1) assertions are enabled and 2) the conditions before 'maxError' are meet. Do
         * not factor the call to 'maxError' outside the 'assert' statement, otherwise it
         * would be executed everytime and would hurt performance for normal operations
         * (instead of slowing down during debugging only).
         */
        assert !(target3D && srcPts!=dstPts &&
                (maxError(srcPts, null, srcOff, dstPts, null, dstOff, numPts)) > EPS);
    }

    /**
     * Implementation of the transformation methods for all cases.
     */
    private void transform(final float[] srcPts1, final double[] srcPts2, int srcOff,
                           final float[] dstPts1, final double[] dstPts2, int dstOff, int numPts)
    {
        int step = 0;
        if ((srcPts2!=null ? srcPts2==dstPts2 : srcPts1==dstPts1) &&
            srcOff<dstOff && srcOff+numPts*getSourceDimensions()>dstOff)
        {
            if (source3D != target3D) {
                // TODO: we need to figure out a general way to handle this case
                //       (overwritting the source array  while source and target
                //       dimensions are not the same).   This case occurs enough
                //       in the CTS implementation...
                throw new UnsupportedOperationException("Not yet implemented.");
            }
            step = getSourceDimensions();
            srcOff += (numPts-1)*step;
            dstOff += (numPts-1)*step;
            step *= -2;
        }
        while (--numPts >= 0) {
            double x,y,z;
            if (srcPts2 != null) {
                x =              srcPts2[srcOff++];
                y =              srcPts2[srcOff++];
                z = (source3D) ? srcPts2[srcOff++] : 0.0;
            } else {
                x =              srcPts1[srcOff++];
                y =              srcPts1[srcOff++];
                z = (source3D) ? srcPts1[srcOff++] : 0.0;
            }
            x = toRadians(x);
            y = toRadians(y);
            final double sinX = sin(x);
            final double cosX = cos(x);
            final double sinY = sin(y);
            final double cosY = cos(y);
            final double sin2Y = sinY * sinY;
            final double Rn = a / sqrt(1 - e2*sin2Y);
            final double Rm = Rn * (1 - e2) / (1 - e2*sin2Y);

            // Note: Computation of 'x' and 'y' ommit the division by sin(1"), because
            //       1/sin(1") / (60*60*180/PI) = 1.0000000000039174050898603898692...
            //       (60*60 is for converting the final result from seconds to degrees,
            //       and 180/PI is for converting degrees to radians). This is an error
            //       of about 8E-7 arc seconds, probably close to rounding errors anyway.
            if (abridged) {
                y += (dz*cosY - sinY*(dy*sinX + dx*cosX) + adf*sin(2*y)) / Rm;
                x += (dy*cosX - dx*sinX) / (Rn*cosY);
            } else {
                y += (dz*cosY - sinY*(dy*sinX + dx*cosX) + da_a*(Rn*e2*sinY*cosY) +
                      df*(Rm*(a_b) + Rn*(b_a))*sinY*cosY) / (Rm + z);
                x += (dy*cosX - dx*sinX) / ((Rn + z)*cosY);
            }
            // stay within latitude +-90 deg. and longitude +-180 deg.
            if (abs(y) > PI/2.0) {
                if (dstPts2 != null) {
                    dstPts2[dstOff++] = 0.0;
                    dstPts2[dstOff++] = (y > 0.0) ? 90.0 : -90.0;
                } else {
                    dstPts1[dstOff++] = 0.0f;
                    dstPts1[dstOff++] = (y > 0.0f) ? 90.0f : -90.0f;
                }
            } else {
                x = toDegrees(rollLongitude(x));
                y = toDegrees(y);
                if (dstPts2 != null) {
                    dstPts2[dstOff++] = x;
                    dstPts2[dstOff++] = y;
                } else {
                    dstPts1[dstOff++] = (float) x;
                    dstPts1[dstOff++] = (float) y;
                }
            }
            if (target3D) {
                if (abridged) {
                    z += dx*cosY*cosX + dy*cosY*sinX + dz*sinY + adf*sin2Y - da;
                } else {
                    z += dx*cosY*cosX + dy*cosY*sinX + dz*sinY + df*(b_a)*Rn*sin2Y - daa/Rn;
                }
                if (dstPts2 != null) {
                    dstPts2[dstOff++] = z;
                } else {
                    dstPts1[dstOff++] = (float) z;
                }
            }
            srcOff += step;
            dstOff += step;
        }
    }

    /**
     * After a call to {@code transform}, applies the <em>inverse</em> transform on {@code dstPts}
     * and compares the result with {@code srcPts}. The maximal difference (in absolute value) is
     * returned. This method is used for assertions.
     */
    private float maxError(final float[] srcPts1, final double[] srcPts2, int srcOff,
                           final float[] dstPts1, final double[] dstPts2, int dstOff, int numPts)
    {
        float max = 0f;
        if (inverse == null) {
            inverse();
            if (inverse == null) {
                return max; // Custom user's subclass; can't do the test.
            }
        }
        final int sourceDim = getSourceDimensions();
        final float[] tmp = new float[numPts * sourceDim];
        inverse.transform(dstPts1, dstPts2, dstOff, tmp, null, 0, numPts);
        for (int i=0; i<tmp.length; i++,srcOff++) {
            final float expected = (srcPts2!=null) ? (float)srcPts2[srcOff] : srcPts1[srcOff];
            float error = abs(tmp[i] - expected);
            switch (i % sourceDim) {
                case 0: error -= 360 * floor(error / 360); break; // Rool Longitude
                case 2: continue; // Ignore height because inacurate.
            }
            if (error > max) {
                max = error;
            }
        }
        return max;
    }

    /**
     * Returns {@code true} if this transform is the identity one.
     * This transform is considered identity (minus rounding errors) if:
     * <p>
     * <ul>
     *   <li>the X,Y,Z shift are zero</li>
     *   <li>the source and target axis length are the same</li>
     *   <li>the input and output dimension are the same.</li>
     * </ul>
     *
     * @since 2.5
     */
    @Override
    public boolean isIdentity() {
        return dx == 0 && dy == 0 && dz == 0 && da == 0 && db == 0 && source3D == target3D;
    }

    /**
     * Creates the inverse transform of this object.
     */
    @Override
    public MathTransform inverse() {
        if (inverse == null) {
            inverse = new MolodenskiTransform(abridged,
                      a+da, b+db, target3D, a, b, source3D, -dx, -dy, -dz);
            inverse.inverse = this;
        }
        return inverse;
    }

    /**
     * Returns a hash value for this transform.
     */
    @Override
    public final int hashCode() {
        final long code = Double.doubleToLongBits(dx) +
                      37*(Double.doubleToLongBits(dy) +
                      37*(Double.doubleToLongBits(dz) +
                      37*(Double.doubleToLongBits(a ) +
                      37*(Double.doubleToLongBits(b ) +
                      37*(Double.doubleToLongBits(da) +
                      37*(Double.doubleToLongBits(db)))))));
        int c = (int) code ^ (int) (code >>> 32) ^ (int)serialVersionUID;
        if (abridged) c = ~c;
        return c;
    }

    /**
     * Compares the specified object with this math transform for equality.
     */
    @Override
    public final boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final MolodenskiTransform that = (MolodenskiTransform) object;
            return this.abridged == that.abridged &&
                   this.source3D == that.source3D &&
                   this.target3D == that.target3D &&
                   Double.doubleToLongBits(this.dx) == Double.doubleToLongBits(that.dx) &&
                   Double.doubleToLongBits(this.dy) == Double.doubleToLongBits(that.dy) &&
                   Double.doubleToLongBits(this.dz) == Double.doubleToLongBits(that.dz) &&
                   Double.doubleToLongBits(this.a ) == Double.doubleToLongBits(that.a ) &&
                   Double.doubleToLongBits(this.b ) == Double.doubleToLongBits(that.b ) &&
                   Double.doubleToLongBits(this.da) == Double.doubleToLongBits(that.da) &&
                   Double.doubleToLongBits(this.db) == Double.doubleToLongBits(that.db);
        }
        return false;
    }

    /**
     * A Molodenski transforms in 2D. This implementation is identical to
     * {@link MolodenksiTransform} except that it implements {@link MathTransform2D}.
     */
    private static final class As2D extends MolodenskiTransform implements MathTransform2D {
        /** Serial number for compatibility with different versions. */
        private static final long serialVersionUID = 8098439371246167474L;

        /** Constructs a 2D transform using Molodenski formulas. */
        public As2D(final boolean abridged,
                    final double  a, final double  b,
                    final double ta, final double tb,
                    final double dx, final double dy, final double  dz)
        {
            super(abridged, a, b, false, ta, tb, false, dx, dy, dz);
        }

        /** Creates the inverse transform of this object. */
        @Override
        public MathTransform2D inverse() {
            if (super.inverse == null) {
                super.inverse = new As2D(super.abridged,
                        super.a + super.da, super.b + super.db,
                        super.a, super.b, -super.dx, -super.dy, -super.dz);
                super.inverse.inverse = this;
            }
            return (MathTransform2D) super.inverse;
        }
    }

    /**
     * The provider for {@link MolodenskiTransform}. This provider will construct
     * transforms from {@linkplain org.geotools.referencing.crs.DefaultGeographicCRS geographic}
     * to {@linkplain org.geotools.referencing.crs.DefaultGeographicCRS geographic} coordinate
     * reference systems.
     * <p>
     * <strong>Note:</strong>
     * The EPSG does not use src_semi_major, etc. parameters and instead uses
     * "Semi-major axis length difference" and "Flattening difference".
     *
     * @version $Id$
     * @author Rueben Schulz
     */
    public static class Provider extends MathTransformProvider {
        /**
         * Serial number for interoperability with different versions.
         */
        private static final long serialVersionUID = -5332126871499059030L;

        /**
         * The default value for source and target geographic dimensions, which is 2.
         */
        // NOTE: If this default value is modified, then
        // the handling of the 3D cases must be adjusted.
        static final int DEFAULT_DIMENSION = GeocentricTranslation.Provider.DEFAULT_DIMENSION;

        /**
         * The number of geographic dimension (2 or 3). This argument applies on
         * both the source and the target dimension. The default value is 2.
         */
        public static final ParameterDescriptor<Integer> DIM =DefaultParameterDescriptor.create(
                    Collections.singletonMap(NAME_KEY,
                        new NamedIdentifier(Citations.OGC, "dim")),
                    DEFAULT_DIMENSION, 2, 3, false);

        /**
         * The number of source geographic dimension (2 or 3).
         * This is a Geotools-specific argument.
         *
         * @todo Not yet used by this provider. See GEOT-411.
         */
        public static final ParameterDescriptor<Integer> SRC_DIM =
                GeocentricTranslation.Provider.SRC_DIM;

        /**
         * The number of target geographic dimension (2 or 3).
         * This is a Geotools-specific argument.
         *
         * @todo Not yet used by this provider. See GEOT-411.
         */
        public static final ParameterDescriptor<Integer> TGT_DIM =
                GeocentricTranslation.Provider.TGT_DIM;

        /**
         * The operation parameter descriptor for the <cite>X-axis translation</cite> ("dx")
         * parameter value. Valid values range from -infinity to infinity. Units are meters.
         */
        public static final ParameterDescriptor<Double> DX =
                GeocentricTranslation.Provider.DX;

        /**
         * The operation parameter descriptor for the <cite>Y-axis translation</cite> ("dy")
         * parameter value. Valid values range from -infinity to infinity. Units are meters.
         */
        public static final ParameterDescriptor<Double> DY =
                GeocentricTranslation.Provider.DY;

        /**
         * The operation parameter descriptor for the <cite>Z-axis translation</cite> ("dz")
         * parameter value. Valid values range from -infinity to infinity. Units are meters.
         */
        public static final ParameterDescriptor<Double> DZ =
                GeocentricTranslation.Provider.DZ;

        /**
         * The operation parameter descriptor for the "src_semi_major" parameter value.
         * Valid values range from 0 to infinity.
         */
        public static final ParameterDescriptor<Double> SRC_SEMI_MAJOR = createDescriptor(
                identifiers(GeocentricTranslation.Provider.SRC_SEMI_MAJOR),
                Double.NaN, 0.0, Double.POSITIVE_INFINITY, SI.METER);

        /**
         * The operation parameter descriptor for the "src_semi_minor" parameter value.
         * Valid values range from 0 to infinity.
         */
        public static final ParameterDescriptor<Double> SRC_SEMI_MINOR = createDescriptor(
                identifiers(GeocentricTranslation.Provider.SRC_SEMI_MINOR),
                Double.NaN, 0.0, Double.POSITIVE_INFINITY, SI.METER);

        /**
         * The operation parameter descriptor for the "tgt_semi_major" parameter value.
         * Valid values range from 0 to infinity.
         */
        public static final ParameterDescriptor<Double> TGT_SEMI_MAJOR = createDescriptor(
                identifiers(GeocentricTranslation.Provider.TGT_SEMI_MAJOR),
                Double.NaN, 0.0, Double.POSITIVE_INFINITY, SI.METER);

        /**
         * The operation parameter descriptor for the "tgt_semi_minor" parameter value.
         * Valid values range from 0 to infinity.
         */
        public static final ParameterDescriptor<Double> TGT_SEMI_MINOR = createDescriptor(
                identifiers(GeocentricTranslation.Provider.TGT_SEMI_MINOR),
                Double.NaN, 0.0, Double.POSITIVE_INFINITY, SI.METER);

        /** Helper method for parameter descriptor creation. */
        private static final NamedIdentifier[] identifiers(final ParameterDescriptor parameter) {
            final Collection<GenericName> id = parameter.getAlias();
            return id.toArray(new NamedIdentifier[id.size()]);
        }

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Molodenski"),
                new NamedIdentifier(Citations.EPSG,     "Molodenski"),
                new NamedIdentifier(Citations.EPSG,     "9604"),
                new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.formatInternational(
                                                        VocabularyKeys.MOLODENSKI_TRANSFORM))
            }, new ParameterDescriptor[] {
                DIM, DX, DY, DZ,
                SRC_SEMI_MAJOR, SRC_SEMI_MINOR,
                TGT_SEMI_MAJOR, TGT_SEMI_MINOR
            });

        /**
         * The provider for the 3D case. Will be constructed
         * by {@link #getMethod} when first needed.
         */
        private transient Provider withHeight;

        /**
         * Constructs a provider.
         */
        public Provider() {
            super(DEFAULT_DIMENSION, DEFAULT_DIMENSION, PARAMETERS);
        }

        /**
         * Constructs a provider from a set of parameters.
         *
         * @param sourceDimensions Number of dimensions in the source CRS of this operation method.
         * @param targetDimensions Number of dimensions in the target CRS of this operation method.
         * @param parameters       The set of parameters (never {@code null}).
         */
        Provider(final int sourceDimensions,
                 final int targetDimensions,
                 final ParameterDescriptorGroup parameters)
        {
            super(sourceDimensions, targetDimensions, parameters);
        }

        /**
         * Returns the operation type.
         */
        @Override
        public Class<Transformation> getOperationType() {
            return Transformation.class;
        }

        /**
         * Creates a math transform from the specified group of parameter values.
         *
         * @param  values The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        protected MathTransform createMathTransform(final ParameterValueGroup values)
                throws ParameterNotFoundException
        {
            final boolean hasHeight;
            final int dim = intValue(DIM, values);
            switch (dim) {
                case 0: // Default value: fall through
                case DEFAULT_DIMENSION: {
                    hasHeight = false;
                    break;
                }
                case 3: {
                    hasHeight = true;
                    if (withHeight == null) {
                        withHeight = create3D();
                    }
                    break;
                }
                default: {
                    throw new IllegalArgumentException(Errors.format(
                            ErrorKeys.ILLEGAL_ARGUMENT_$2, "dim", dim));
                }
            }
            final double  a = doubleValue(SRC_SEMI_MAJOR, values);
            final double  b = doubleValue(SRC_SEMI_MINOR, values);
            final double ta = doubleValue(TGT_SEMI_MAJOR, values);
            final double tb = doubleValue(TGT_SEMI_MINOR, values);
            final double dx = doubleValue(DX,             values);
            final double dy = doubleValue(DY,             values);
            final double dz = doubleValue(DZ,             values);
            final boolean abridged = isAbridged();
            if (!hasHeight) {
                return new As2D(abridged, a, b, ta, tb, dx, dy, dz);
            } else {
                return new Delegate(new MolodenskiTransform(
                        abridged, a, b, hasHeight, ta, tb, hasHeight, dx, dy, dz), withHeight);
            }
        }

        /**
         * Creates the 3D-version of this provider.
         * This method is overridden by {@link ProviderAbridged}.
         */
        Provider create3D() {
            return new Provider(3, 3, PARAMETERS);
        }

        /**
         * Returns {@code true} for the abridged formulas.
         * This method is overridden by {@link ProviderAbridged}.
         */
        boolean isAbridged() {
            return false;
        }
    }

    /**
     * The provider for abridged {@link MolodenskiTransform}. This provider will construct
     * transforms from {@linkplain org.geotools.referencing.crs.DefaultGeographicCRS geographic}
     * to {@linkplain org.geotools.referencing.crs.DefaultGeographicCRS geographic} coordinate
     * reference systems.
     * <p>
     * <strong>Note:</strong>
     * The EPSG does not use src_semi_major, etc. parameters and instead uses
     * "Semi-major axis length difference" and "Flattening difference".
     *
     * @version $Id$
     * @author Martin Desruisseaux
     * @author Rueben Schulz
     */
    public static class ProviderAbridged extends Provider {
        /**
         * Serial number for interoperability with different versions.
         */
        private static final long serialVersionUID = 9148242601566635131L;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Abridged_Molodenski"),
                new NamedIdentifier(Citations.EPSG,     "Abridged Molodenski"),
                new NamedIdentifier(Citations.EPSG,     "9605"),
                new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.format(
                                    VocabularyKeys.ABRIDGED_MOLODENSKI_TRANSFORM))
            }, new ParameterDescriptor[] {
                DIM, DX, DY, DZ,
                SRC_SEMI_MAJOR, SRC_SEMI_MINOR,
                TGT_SEMI_MAJOR, TGT_SEMI_MINOR
            });

        /**
         * Constructs a provider.
         */
        public ProviderAbridged() {
            super(DEFAULT_DIMENSION, DEFAULT_DIMENSION, PARAMETERS);
        }

        /**
         * Constructs a provider from a set of parameters.
         *
         * @param sourceDimensions Number of dimensions in the source CRS of this operation method.
         * @param targetDimensions Number of dimensions in the target CRS of this operation method.
         * @param parameters       The set of parameters (never {@code null}).
         */
        private ProviderAbridged(final int sourceDimensions,
                                 final int targetDimensions,
                                 final ParameterDescriptorGroup parameters)
        {
            super(sourceDimensions, targetDimensions, parameters);
        }

        /**
         * Creates the 3D-version of this provider.
         */
        @Override
        Provider create3D() {
            return new ProviderAbridged(3, 3, PARAMETERS);
        }

        /**
         * Returns {@code true} for the abridged formulas.
         */
        @Override
        boolean isAbridged() {
            return true;
        }
    }
}
