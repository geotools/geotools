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

import org.geotools.geometry.DirectPosition1D;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.operation.NoninvertibleTransformException;


/**
 * Concatenated transform in which the resulting transform is one-dimensional.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class ConcatenatedTransform1D extends ConcatenatedTransform implements MathTransform1D {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 8150427971141078395L;

    /**
     * Constructs a concatenated transform.
     */
    public ConcatenatedTransform1D(final MathTransform transform1,
                                   final MathTransform transform2)
    {
        super(transform1, transform2);
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
        final double[] values = new double[] {value};
        final double[] buffer = new double[] {transform1.getTargetDimensions()};
        transform1.transform(values, 0, buffer, 0, 1);
        transform2.transform(buffer, 0, values, 0, 1);
        return values[0];
    }

    /**
     * Gets the derivative of this function at a value.
     */
    public double derivative(final double value) throws TransformException {
        final DirectPosition1D p = new DirectPosition1D(value);
        final Matrix m = derivative(p);
        assert m.getNumRow()==1 && m.getNumCol()==1;
        return m.getElement(0,0);
    }

    /**
     * Creates the inverse transform of this object.
     */
    @Override
    public MathTransform1D inverse() throws NoninvertibleTransformException {
        return (MathTransform1D) super.inverse();
    }
}
