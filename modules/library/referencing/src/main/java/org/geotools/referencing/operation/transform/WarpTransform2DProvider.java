/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.MathTransformProvider;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Transformation;

/**
 * The provider for the {@link WarpTransform2D}. This provider constructs a JAI
 * {@linkplain WarpPolynomial image warp} from a set of polynomial coefficients,
 * and wrap it in a {@link WarpTransform2D} object.
 *
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class WarpTransform2DProvider extends MathTransformProvider {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -7949539694656719923L;

    /** Descriptor for the "{@link WarpPolynomial#getDegree degree}" parameter value. */
    public static final ParameterDescriptor<Integer> DEGREE = DefaultParameterDescriptor.create(
            "degree", 2, 1, WarpTransform2D.MAX_DEGREE);

    /** Descriptor for the "{@link WarpPolynomial#getXCoeffs xCoeffs}" parameter value. */
    public static final ParameterDescriptor X_COEFFS = new DefaultParameterDescriptor(
            "xCoeffs", float[].class, null, null);

    /** Descriptor for the "{@link WarpPolynomial#getYCoeffs yCoeffs}" parameter value. */
    public static final ParameterDescriptor Y_COEFFS = new DefaultParameterDescriptor(
            "yCoeffs", float[].class, null, null);

    /** Descriptor for the "{@link WarpPolynomial#getPreScaleX preScaleX}" parameter value. */
    public static final ParameterDescriptor PRE_SCALE_X;

    /** Descriptor for the "{@link WarpPolynomial#getPreScaleY preScaleY}" parameter value. */
    public static final ParameterDescriptor PRE_SCALE_Y;

    /** Descriptor for the "{@link WarpPolynomial#getPostScaleX postScaleX}" parameter value. */
    public static final ParameterDescriptor POST_SCALE_X;

    /** Descriptor for the "{@link WarpPolynomial#getPostScaleY postScaleY}" parameter value. */
    public static final ParameterDescriptor<Float> POST_SCALE_Y;
    static {
        final Float ONE = 1f;
         PRE_SCALE_X = DefaultParameterDescriptor.create( "preScaleX",null, Float.class, ONE, false);
         PRE_SCALE_Y = DefaultParameterDescriptor.create( "preScaleY", null, Float.class, ONE, false);
         POST_SCALE_X = DefaultParameterDescriptor.create("postScaleX", null, Float.class, ONE, false);
         POST_SCALE_Y = DefaultParameterDescriptor.create("postScaleY", null, Float.class, ONE, false);
    }

    /**
     * The parameters group.
     */
    static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
            new NamedIdentifier(Citations.GEOTOOLS, "WarpPolynomial")
        }, new ParameterDescriptor[] {
            DEGREE, X_COEFFS, Y_COEFFS, PRE_SCALE_X, PRE_SCALE_Y, POST_SCALE_X, POST_SCALE_Y
        });

    /**
     * Create a provider for warp transforms.
     */
    public WarpTransform2DProvider() {
        super(2, 2, PARAMETERS);
    }

    /**
     * Returns the operation type.
     */
    @Override
    public Class<Transformation> getOperationType() {
        return Transformation.class;
    }

    /**
     * Creates a warp transform from the specified group of parameter values.
     *
     * @param  values The group of parameter values.
     * @return The created math transform.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    protected MathTransform createMathTransform(final ParameterValueGroup values)
            throws ParameterNotFoundException
    {
        final int      degree   =        intValue(DEGREE,   values);
        final float[] xCoeffs   = (float[]) value(X_COEFFS, values);
        final float[] yCoeffs   = (float[]) value(Y_COEFFS, values);
        final float   preScaleX = scale( PRE_SCALE_X, values);
        final float   preScaleY = scale( PRE_SCALE_Y, values);
        final float  postScaleX = scale(POST_SCALE_X, values);
        final float  postScaleY = scale(POST_SCALE_Y, values);
        try {
            final Object warp;
            switch (degree) {
                case 1:  warp = createWarp("javax.media.jai.WarpAffine",
                                           xCoeffs, yCoeffs, preScaleX, preScaleY, postScaleX, postScaleY); break;
                case 2:  warp = createWarp("javax.media.jai.WarpQuadratic",
                                            xCoeffs, yCoeffs, preScaleX, preScaleY, postScaleX, postScaleY); break;
                case 3:  warp = createWarp("javax.media.jai.WarpCubic",
                                            xCoeffs, yCoeffs, preScaleX, preScaleY, postScaleX, postScaleY); break;
                default: warp = createWarp("javax.media.jai.WarpGeneralPolynomial",
                                            xCoeffs, yCoeffs, preScaleX, preScaleY, postScaleX, postScaleY); break;
            }
            Class<? extends MathTransform> transformClass;
            transformClass = (Class<? extends MathTransform>) Class.forName("org.geotools.referencing.operation.transform.WarpTransform2D");
            Class warpClass = Class.forName("javax.media.jai.Warp");
            
            Constructor<? extends MathTransform> createTransform = transformClass.getConstructor( new Class[]{warpClass,warpClass});
            return createTransform.newInstance( warp, null );
        } catch (Exception jaiUnavailable) {
            throw new UnsupportedOperationException("WarpTransform2D requires Java Advanced Imaging extension");
        }
    }

    private Object createWarp(String warpName, final float[] xCoeffs, final float[] yCoeffs,
            final float preScaleX, final float preScaleY, final float postScaleX,
            final float postScaleY) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class warpClass = Class.forName(warpName);
        Class[] params = new Class[]{float[].class,float[].class,float.class,float.class,float.class,float.class,float.class};
        Constructor<?> constrctor = warpClass.getConstructor(params);
        return constrctor.newInstance( xCoeffs, yCoeffs, preScaleX, preScaleY, postScaleX, postScaleY);
    }
    

    /**
     * Returns the parameter value for the specified operation parameter.
     *
     * @param  param The parameter to look for.
     * @param  group The parameter value group to search into.
     * @return The requested parameter value, or {@code 1} if none.
     */
    private static float scale(final ParameterDescriptor param,
                               final ParameterValueGroup group)
            throws ParameterNotFoundException
    {
        final Object value = value(param, group);
        return (value!=null) ? ((Number) value).floatValue() : 1;
    }
}