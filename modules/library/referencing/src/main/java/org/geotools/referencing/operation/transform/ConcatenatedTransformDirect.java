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

import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.geometry.DirectPosition;


/**
 * Concatenated transform where the transfert dimension is the same than source and target
 * dimension. This fact allows some optimizations, the most important one being the possibility
 * to avoid the use of an intermediate buffer in some case.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
class ConcatenatedTransformDirect extends ConcatenatedTransform {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -3568975979013908920L;

    /**
     * Constructs a concatenated transform.
     */
    public ConcatenatedTransformDirect(final MathTransform transform1,
                                       final MathTransform transform2)
    {
        super(transform1, transform2);
    }

    /**
     * Checks if transforms are compatibles with this implementation.
     */
    @Override
    boolean isValid() {
        return super.isValid() &&
               transform1.getSourceDimensions() == transform1.getTargetDimensions() &&
               transform2.getSourceDimensions() == transform2.getTargetDimensions();
    }

    /**
     * Transforms the specified {@code ptSrc} and stores the result in {@code ptDst}.
     */
    @Override
    public DirectPosition transform(final DirectPosition ptSrc, DirectPosition ptDst)
            throws TransformException
    {
        assert isValid();
        ptDst = transform1.transform(ptSrc, ptDst);
        return  transform2.transform(ptDst, ptDst);
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     */
    @Override
    public void transform(final double[] srcPts, final int srcOff,
                          final double[] dstPts, final int dstOff, final int numPts)
            throws TransformException
    {
        assert isValid();
        transform1.transform(srcPts, srcOff, dstPts, dstOff, numPts);
        transform2.transform(dstPts, dstOff, dstPts, dstOff, numPts);
    }

    // Do NOT override the transform(float[]...) version because we really need to use an
    // intermediate buffer of type double[] for reducing rounding error. Otherwise some map
    // projection degrades image quality in an unacceptable way.
}
