/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.operation;

import org.opengis.annotation.Extension;


/**
 * Transforms one-dimensional coordinate points.
 * {@link CoordinateOperation#getMathTransform} may returns instance of this
 * interface when source and destination coordinate systems are both one dimensional.
 * {@code MathTransform1D} extends {@link MathTransform} by adding a simple method
 * transforming a value without the overhead of creating data array.
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/operation/MathTransform1D.java $
 */
@Extension
public interface MathTransform1D extends MathTransform {
    /**
     * Transforms the specified value.
     *
     * @param value The value to transform.
     * @return the transformed value.
     * @throws TransformException if the value can't be transformed.
     */
    double transform(double value) throws TransformException;

    /**
     * Gets the derivative of this function at a value. The derivative is the
     * 1&times;1 matrix of the non-translating portion of the approximate affine
     * map at the value.
     *
     * @param  value The value where to evaluate the derivative.
     * @return The derivative at the specified point.
     * @throws TransformException if the derivative can't be evaluated at the
     *         specified point.
     */
    double derivative(double value) throws TransformException;

    /**
     * Creates the inverse transform of this object.
     *
     * @return The inverse transform.
     * @throws NoninvertibleTransformException if the transform can't be inversed.
     *
     * @since GeoAPI 2.2
     */
    MathTransform1D inverse() throws NoninvertibleTransformException;
}
