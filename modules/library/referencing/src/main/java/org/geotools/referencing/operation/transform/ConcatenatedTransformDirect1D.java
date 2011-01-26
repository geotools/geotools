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
 */
package org.geotools.referencing.operation.transform;

import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.operation.NoninvertibleTransformException;


/**
 * Concatenated transform where both transforms are one-dimensional.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class ConcatenatedTransformDirect1D extends ConcatenatedTransformDirect
                                       implements MathTransform1D
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 1064398659892864966L;

    /**
     * The first math transform. This field is identical
     * to {@link ConcatenatedTransform#transform1}. Only
     * the type is different.
     */
    private final MathTransform1D transform1;

    /**
     * The second math transform. This field is identical
     * to {@link ConcatenatedTransform#transform1}. Only
     * the type is different.
     */
    private final MathTransform1D transform2;

    /**
     * Constructs a concatenated transform.
     */
    public ConcatenatedTransformDirect1D(final MathTransform1D transform1,
                                         final MathTransform1D transform2)
    {
        super(transform1, transform2);
        this.transform1 = transform1;
        this.transform2 = transform2;
    }

    /**
     * Checks if transforms are compatibles with this implementation.
     */
    @Override
    boolean isValid() {
        return super.isValid() && getSourceDimensions()==1 && getTargetDimensions()==1;
    }

    /**
     * Transforms the specified value.
     */
    public double transform(final double value) throws TransformException {
        return transform2.transform(transform1.transform(value));
    }

    /**
     * Gets the derivative of this function at a value.
     */
    public double derivative(final double value) throws TransformException {
        final double value1 = transform1.derivative(value);
        final double value2 = transform2.derivative(transform1.transform(value));
        return value2 * value1;
    }

    /**
     * Creates the inverse transform of this object.
     */
    @Override
    public MathTransform1D inverse() throws NoninvertibleTransformException {
        return (MathTransform1D) super.inverse();
    }
}
